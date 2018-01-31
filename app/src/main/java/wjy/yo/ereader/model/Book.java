package wjy.yo.ereader.model;

import java.util.List;

/**
 * Created by wsx on 2018/1/30.
 */

public class Book {
    private String id;
    private String name;
    private String zhName;
    private String author;
    private String zhAuthor;

    private List<Chapter> chapters;

    public Book() {
    }

    public Book(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getZhAuthor() {
        return zhAuthor;
    }

    public void setZhAuthor(String zhAuthor) {
        this.zhAuthor = zhAuthor;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
