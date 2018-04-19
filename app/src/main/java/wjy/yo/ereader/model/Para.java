package wjy.yo.ereader.model;

/**
 * Created by wsx on 2018/4/19.
 */

public class Para {
    private String id;
    private String bookId;
    private String chapId;
    private String content;
    private String trans;
    private long no;


    public Para(){

    }

    public Para(String id, String bookId, String chapId, String content) {
        this.id = id;
        this.bookId = bookId;
        this.chapId = chapId;
        this.content = content;
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

    public String getChapId() {
        return chapId;
    }

    public void setChapId(String chapId) {
        this.chapId = chapId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }
}
