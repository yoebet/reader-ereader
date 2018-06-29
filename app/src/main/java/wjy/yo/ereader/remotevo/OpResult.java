package wjy.yo.ereader.remotevo;

public class OpResult {

    private int ok;

    private String message;

    public boolean isOk() {
        return this.ok == 1;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
