package wjy.yo.ereader.model;

/**
 * Created by wsx on 2018/1/30.
 */

public class Chapter {
    private String id;
    private String bookId;
    private String name;
    private String zhName;

    public Chapter() {
    }

    public Chapter(String id, String name, String zhName) {
        this.id = id;
        this.name = name;
        this.zhName = zhName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }
}
