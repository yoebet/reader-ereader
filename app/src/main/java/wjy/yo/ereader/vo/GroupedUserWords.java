package wjy.yo.ereader.vo;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Objects;

import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.userdata.UserWord;

public class GroupedUserWords {

    private Group group;
    private List<UserWord> userWords;

    public GroupedUserWords(Group group, List<UserWord> userWords) {
        this.group = group;
        this.userWords = userWords;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<UserWord> getUserWords() {
        return userWords;
    }

    public void setUserWords(List<UserWord> userWords) {
        this.userWords = userWords;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(group.toString()).append("\n");
        for (UserWord uw : userWords) {
            sb.append("\t").append(uw).append("\n");
        }
        return sb.toString();
    }


    public static class Group {
        protected String title;

        public Group(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Group group = (Group) o;
            return Objects.equals(title, group.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title);
        }

        @Override
        public String toString() {
            return title;
        }
    }


    public static class CreatedDateGroup extends Group {
        private LocalDate createdDate;

        public CreatedDateGroup(LocalDate createdDate, String title) {
            super(title);
            this.createdDate = createdDate;
        }

        public CreatedDateGroup(LocalDate createdDate) {
            this(createdDate, (createdDate == null) ? "?" : createdDate.toString("yyyy-MM-dd"));
        }

        public LocalDate getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(LocalDate createdDate) {
            this.createdDate = createdDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CreatedDateGroup that = (CreatedDateGroup) o;
            return Objects.equals(createdDate, that.createdDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(createdDate);
        }
    }

    public static class ChapterGroup extends Group {
        private String chapId;
        private Chap chap;

        public ChapterGroup(String chapId, String title) {
            super(title);
            this.chapId = chapId;
        }

        public String getChapId() {
            return chapId;
        }

        public void setChapId(String chapId) {
            this.chapId = chapId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChapterGroup that = (ChapterGroup) o;
            return Objects.equals(chapId, that.chapId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chapId);
        }

        public Chap getChap() {
            return chap;
        }

        public void setChap(Chap chap) {
            this.chap = chap;
        }
    }
}
