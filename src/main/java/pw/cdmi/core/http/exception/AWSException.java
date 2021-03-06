package pw.cdmi.core.http.exception;

import org.springframework.http.HttpStatus;

import net.sf.json.JSONObject;
import pw.cdmi.core.http.Headers;
import pw.cdmi.core.http.rs.RequestContext;
import pw.cdmi.error.ErrorMessage;
import pw.cdmi.error.ErrorReason;

public abstract class AWSException extends RuntimeException{
	private static final long serialVersionUID = -4480701634087674688L;
	private String message = null;
	private ErrorReason reason = null;
	private HttpStatus status = null;

    public AWSException(ErrorReason reason) {
        this.reason = reason;
        this.message = "系统内部错误，请将错误信息发送给管理员进行处理";
    }
    
    public AWSException(ErrorMessage message, ErrorReason reason) {
    	this.message = message.getMessage();
    	this.reason = reason;
    }
    
    //TODO 
    /**
     * 
     * 如果是服务端异常，显示Resource， RequestId，不显示Reason。
     * 服务端的Code是固定的。
     * 
     * @return
     */
    public String getXmlText() {
        StringBuffer result = new StringBuffer();
        result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        result.append("<Error>\n");
        result.append(" <Code>" + this.reason.getCode() + "</Code>\n");
        result.append(" <Message>" + this.message + "</Message>\n");
        result.append(" <Reason>" + this.reason.toString() + "</Reason>\n");
        result.append(" <Resource>" + RequestContext.getExtParameter(Headers.RESOURCE_PATH) + "</Resource>\n");
        result.append(" <RequestId>" + RequestContext.getExtParameter(Headers.REQUEST_ID) + "</RequestId>\n");
        result.append(" <HostId>" + RequestContext.getExtParameter(Headers.HOST_ID) + "</RequestId>\n");
        result.append("</Error>");
        return result.toString();
    }

    public JSONObject getJsonText() {
        StringBuffer result = new StringBuffer("{");
        result.append("'code':" + this.reason.getCode());
        result.append(",'message':");
        if (this.message != null) {
            result.append("'" + this.message + "'");
        } else {
            result.append("''");
        }
        result.append("}");
        return JSONObject.fromObject(result.toString());
    }
    
    public int getHttpStatus(){
    	return status.value();
    }
    
    public String getRequestId(){
    	return RequestContext.getExtParameter(Headers.REQUEST_ID);
    }
    
    
    public String getMessage(){
    	return this.message;
    }
}
