package wjy.yo.ereader.vo;

import java.util.List;

import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;

public class TextSearchResult {

    public static final String SEARCH_FIELD_PARA_CONTENT = "para_content";
    public static final String SEARCH_FIELD_PARA_TRANS = "para_trans";

    public static final String RESULT_FROM_SERVER = "server";
    public static final String RESULT_FROM_FTS = "fts";
    public static final String RESULT_FROM_PARA = "para";

    private String keyword;

    private List<String> highlightWords;

    // content/trans
    private String searchField;

    // server/fts/para
    private String resultFrom;

    private List<ResultItem> resultItems;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getHighlightWords() {
        return highlightWords;
    }

    public void setHighlightWords(List<String> highlightWords) {
        this.highlightWords = highlightWords;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getResultFrom() {
        return resultFrom;
    }

    public void setResultFrom(String resultFrom) {
        this.resultFrom = resultFrom;
    }

    public List<ResultItem> getResultItems() {
        return resultItems;
    }

    public void setResultItems(List<ResultItem> resultItems) {
        this.resultItems = resultItems;
    }


    public static class ResultItem {

        private Para para;

        private Chap chap;

        private Book book;

        private boolean paraLoaded;

        public Para getPara() {
            return para;
        }

        public void setPara(Para para) {
            this.para = para;
        }

        public Chap getChap() {
            return chap;
        }

        public void setChap(Chap chap) {
            this.chap = chap;
        }

        public Book getBook() {
            return book;
        }

        public void setBook(Book book) {
            this.book = book;
        }

        public boolean isParaLoaded() {
            return paraLoaded;
        }

        public void setParaLoaded(boolean paraLoaded) {
            this.paraLoaded = paraLoaded;
        }
    }

}
