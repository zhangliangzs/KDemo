package zhangliang.view.android.kdemo.tool;

/**

 * 接口返回消息对象
 */
public class ResMsg {
    private String  code;
    private String message;
    private String method;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code=" + code + " message=" + message+ " method=" + method);
        return sb.toString();
    }
}
