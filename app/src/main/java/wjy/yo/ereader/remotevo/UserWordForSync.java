package wjy.yo.ereader.remotevo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wjy.yo.ereader.entity.userdata.UserWord;

public class UserWordForSync {

    public String word;

    UserWordForSync(String word) {
        this.word = word;
    }

    public static class UserWordRemove extends UserWordForSync {
        public boolean removeFlag = true;

        UserWordRemove(String word) {
            super(word);
        }
    }

    public static class UserWordFamiliarity extends UserWordForSync {

        public int familiarity;

        public Date updatedAt;

        UserWordFamiliarity(String word, int familiarity, Date updatedAt) {
            super(word);
            this.familiarity = familiarity;
            this.updatedAt = updatedAt;
        }
    }

    public static class UserWordUpdateAll extends UserWordFamiliarity {

        public String bookId;

        public String chapId;

        public String paraId;

        UserWordUpdateAll(String word, int familiarity, Date updatedAt, TextSource source) {
            super(word, familiarity, updatedAt);
            this.bookId = source.bookId;
            this.chapId = source.chapId;
            this.paraId = source.paraId;
        }
    }

    public static class UserWordCreate extends UserWordUpdateAll {

        public Date createdAt;

        UserWordCreate(String word, int familiarity, Date updatedAt, TextSource source, Date createdAt) {
            super(word, familiarity, updatedAt, source);
            this.createdAt = createdAt;
        }
    }

    static class TextSource {
        public String bookId;
        public String chapId;
        public String paraId;

        TextSource(String bookId, String chapId, String paraId) {
            this.bookId = bookId;
            this.chapId = chapId;
            this.paraId = paraId;
        }
    }


    public static List<UserWordForSync> fromUserWords(List<UserWord> userWords) {

        List<UserWordForSync> forSyncList = new ArrayList<>(userWords.size());

        for (UserWord uw : userWords) {
            String changeFlag = uw.getChangeFlag();
            if (changeFlag == null) {
                changeFlag = UserWord.ChangeFlagCreate;
            }

            String word = uw.getWord();

            if (changeFlag.equals(UserWord.ChangeFlagDelete)) {
                UserWordRemove uwr = new UserWordRemove(word);
                forSyncList.add(uwr);
                continue;
            }

            int familiarity = uw.getFamiliarity();
            Date updatedAt = uw.getUpdatedAt();

            if (changeFlag.equals(UserWord.ChangeFlagFamiliarity)) {
                UserWordFamiliarity uwf = new UserWordFamiliarity(word, familiarity, updatedAt);
                forSyncList.add(uwf);
                continue;
            }

            TextSource source = new TextSource(uw.getBookId(), uw.getChapId(), uw.getParaId());

            if (changeFlag.equals(UserWord.ChangeFlagAll)) {
                UserWordUpdateAll uwa = new UserWordUpdateAll(word, familiarity, updatedAt, source);
                forSyncList.add(uwa);
                continue;
            }

            if (changeFlag.equals(UserWord.ChangeFlagCreate)) {
                Date createdAt = uw.getCreatedAt();
                UserWordCreate uwc = new UserWordCreate(word, familiarity, updatedAt, source, createdAt);
                forSyncList.add(uwc);
            }
        }

        return forSyncList;
    }
}
