package wjy.yo.ereader.remotevo;

public class OpResult {
    private int ok;

    private String message;

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return this.ok == 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ok: " + ok + ", message: " + message;
    }
}
