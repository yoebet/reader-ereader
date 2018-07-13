package wjy.yo.ereader.vo;

public class OperationResult {
    private boolean success;
    private String errorCode;
    private String message;

    public final static OperationResult SUCCESS = new OperationResult(true);

    public final static OperationResult FAILURE = new OperationResult(false);

    public OperationResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
