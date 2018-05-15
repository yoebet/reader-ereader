package wjy.yo.ereader.service.vo;

public class Failure {

    private String category;

    private String message;

    public static Failure GENERAL = new Failure("GENERAL", "操作失败");

    public static Failure NETWORK = new Failure("NETWORK", "网络错误，请检查网络");

    public Failure(String category, String message) {
        this.category = category;
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
