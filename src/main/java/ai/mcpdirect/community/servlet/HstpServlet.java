package ai.mcpdirect.community.servlet;
import appnet.hstp.Service;
import appnet.hstp.ServiceHeaders;
import appnet.hstp.USL;
import appnet.hstp.engine.util.JSON;
import appnet.hstp.exception.ServiceNotFoundException;
import appnet.hstp.exception.USLSyntaxException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

import static appnet.hstp.Service.*;


@WebServlet(name = "HstpServlet",urlPatterns = "/hstp/")
public class HstpServlet extends AbstractHstpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(HstpServlet.class);
    public static boolean DEBUG;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        req.setCharacterEncoding("UTF-8");
        USL usl = defaultUsl;
        String hstpAuth = req.getHeader(authHeaderKey);
        if(usl==null) {
            String hstpUsl = req.getHeader(uslHeaderKey);
            if (hstpUsl == null || (hstpUsl = hstpUsl.trim()).isEmpty()) {
                resp.setCharacterEncoding("UTF-8");
                resp.setHeader("Content-type", "application/json; charset=UTF-8");
                resp.getWriter().write("{\"code\":255,\"message\":\"invalid\"}");
                return;
            }
            try {
                usl = USL.create(hstpUsl);
            } catch (USLSyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }

        String accept = req.getHeader("Accept");
        if("text/event-stream".equals(accept)){
            doSsePost(req,resp,usl,hstpAuth);
            return;
        }
        String hstpResp;
        try {
            String body = getRequestBody(req);
            ServiceHeaders serviceHeaders = new ServiceHeaders();
            serviceHeaders.addHeader(authHeaderKey,hstpAuth);
            HstpServiceRequester hstpServiceSession = new HstpServiceRequester(usl,serviceHeaders,body);
            Service service = hstpServiceSession.getService();
            if(service.getErrorCode()==SERVICE_SUCCESSFUL) {
                byte[] message = service.getResponseMessage();
                if (message != null) {
                    hstpResp = new String(message);
                } else {
                    hstpResp = "{\"code\":" + SERVICE_SUCCESSFUL + ",\"message\":\"response message is empty\"}";
                }
            }else{
                resp.setStatus(400);
                hstpResp = "{\"code\":"+service.getErrorCode()+",\"message\":"+ JSON.quote(service.getErrorMessage())+"}";
            }
        } catch (ServiceNotFoundException ex){
            hstpResp = "{\"code\":" + SERVICE_NOT_FOUND + ",\"message\":\""+usl+"\"}";
            resp.setStatus(400);
        } catch (Exception e){
            hstpResp = "{\"code\":" + SERVICE_FAILED + ",\"message\":"+JSON.quote(e.getMessage())+"}";
            resp.setStatus(400);
            LOG.error("hstpRequest({})",usl,e);
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-type", "application/json; charset=UTF-8");
        resp.getWriter().write(hstpResp);
    }

    private void doSsePost(HttpServletRequest req, HttpServletResponse resp,USL usl,String hstpAuth) throws IOException{
        resp.setContentType("text/event-stream;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");
        try {
            String body = getRequestBody(req);
            ServletOutputStream out = resp.getOutputStream();
            ServiceHeaders serviceHeaders = new ServiceHeaders();
            serviceHeaders.addHeader(authHeaderKey,hstpAuth);
            HstpServiceRequester hstpServiceSession = new HstpServiceRequester(usl,serviceHeaders,body);
            Service service = hstpServiceSession.getService();
            if(service.getErrorCode()==SERVICE_SUCCESSFUL) {
                byte[] message = service.getResponseMessage();
                if (message != null) {
                    out.write(("data: hstp: " + new String(message) + "\n\n").getBytes());
                    out.flush();
                }
                byte[] bytes = new byte[1000];
                int c;
                while ((c = service.read(bytes, 0, bytes.length)) > -1) {
                    out.write(bytes, 0, c);
                    out.flush();
                }
            }else{
                resp.setStatus(400);
                String error = "{\"code\":"+service.getErrorCode()+",\"message\":"+ JSON.quote(service.getErrorMessage())+"}";
                out.write(error.getBytes());
            }
            if (DEBUG) {
                LOG.debug("hstp({},{})", usl,body);
            }
        } catch (Exception e) {
            resp.setStatus(400);
            String error = "{\"code\":"+SERVICE_FAILED+",\"message\":"+ JSON.quote(e.getMessage())+"}";
            LOG.error("hstpSseRequest()",e);
            resp.getWriter().write(error);
        }
    }
    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder buf = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            buf.append(line);
        }
        return buf.toString();
    }

}
