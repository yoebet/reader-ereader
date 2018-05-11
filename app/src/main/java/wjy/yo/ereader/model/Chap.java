package wjy.yo.ereader.model;

import java.util.List;

/**
 * Created by wsx on 2018/1/30.
 */

public class Chap {
    private final String _id;
    private String bookId;
    private String name;
    private String zhName;

    private List<Para> paras;

    public Chap(String id, String name, String zhName) {
        this._id = id;
        this.name = name;
        this.zhName = zhName;
    }

    public String getId() {
        return _id;
    }

//    public void setId(String id) {
//        this._id = id;
//    }

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

    public List<Para> getParas() {
        return paras;
    }

    public void setParas(List<Para> paras) {
        this.paras = paras;
    }

    @Override
    public String toString() {
        return _id + " " + name;
    }
}
