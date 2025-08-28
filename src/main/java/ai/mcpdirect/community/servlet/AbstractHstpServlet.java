package ai.mcpdirect.community.servlet;

import appnet.hstp.USL;
import jakarta.servlet.http.HttpServlet;


public abstract class AbstractHstpServlet extends HttpServlet {
    protected String uslHeaderKey = "hstp-usl";
    protected String authHeaderKey = "hstp-auth";
    protected USL defaultUsl;
    public void setHstpUslHeaderKey(String uslHeaderKey){
        if(uslHeaderKey!=null&&!(uslHeaderKey=uslHeaderKey.replace(" ", "")).isEmpty()){
            this.uslHeaderKey = uslHeaderKey;
        }
    }
    public void setHstpAuthHeaderKey(String authHeaderKey){
        if(authHeaderKey!=null&&!(authHeaderKey=authHeaderKey.replace(" ", "")).isEmpty()){
            this.authHeaderKey = authHeaderKey;
        }
    
    }
    public void setDefaultUsl(USL usl){
        defaultUsl=usl;
    }
}
