package appnet.hstp.labs.http.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import appnet.hstp.*;
import appnet.hstp.engine.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class HstpServiceRequester{
    private static final Logger LOG = LoggerFactory.getLogger(HstpServiceRequester.class);
    private final Service _service;
    public HstpServiceRequester(USL usl, Object content) throws Exception {
        this(usl,null,content);
    }
    public HstpServiceRequester(USL usl, ServiceHeaders headers, Object content) throws Exception {
        this(usl,headers,content,null);
    }
    public HstpServiceRequester(USL usl, ServiceHeaders headers, Object content, Object attachment) throws Exception {
        this(ServiceEngineFactory.getServiceEngine(),usl,headers,content,attachment,null);
    }

    public HstpServiceRequester(USL usl, ServiceHeaders headers, Object content, Object attachment, ServiceResponseHandler handler) throws Exception {
        this(ServiceEngineFactory.getServiceEngine(),usl,headers,content,attachment,handler);
    }

    public HstpServiceRequester(ServiceEngine engine, USL usl, ServiceHeaders headers, Object content,
                                  Object attachment, ServiceResponseHandler handler) throws Exception {
        byte[] _content;
        if(content instanceof String){
            _content = ((String)content).getBytes(StandardCharsets.UTF_8);
        }else if(content instanceof byte[]){
            _content = (byte[]) content;
        }else{
            _content = JSON.toJsonBytes(content);
        }
        _service = engine.request(usl, headers, _content, attachment,handler);
    }

    public Service getService() {
        return _service;
    }

    private byte[] message;
    public byte[] getResponse()throws Exception {
        if(message!=null){
            return message;
        }
        if(_service.getErrorCode()==0) {
            message = _service.getResponseMessage();
        }else{
            message = JSON.toJson(_service.getErrorCode(),_service.getErrorMessage()).getBytes();
        }
        return message;
    }

    public <T> T getResponse(Class<T> type) throws Exception {
        return JSON.fromJson(getResponse(), type);
    }
    public <T> T getResponse(TypeReference<T> type) throws Exception {

        return JSON.fromJson(getResponse(),type);
    }
}
