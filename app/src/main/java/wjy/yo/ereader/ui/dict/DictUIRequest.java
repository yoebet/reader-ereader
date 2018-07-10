package wjy.yo.ereader.ui.dict;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;

public class DictUIRequest {
    public final String requestedWord;
    public final DictEntry entry;
    private List<String> refWords;

    private Maybe<UserWord> userWord;
    private Single<List<String>> rankLabels;

    DictUIRequest(String requestedWord, DictEntry entry) {
        this.requestedWord = requestedWord;
        this.entry = entry;
    }

    public String getWord() {
        return entry.getWord();
    }

    public Maybe<UserWord> getUserWord() {
        return userWord;
    }

    public void setUserWord(Maybe<UserWord> userWord) {
        this.userWord = userWord;
    }

    public Single<List<String>> getRankLabels() {
        return rankLabels;
    }

    public void setRankLabels(Single<List<String>> rankLabels) {
        this.rankLabels = rankLabels;
    }

    public List<String> getRefWords() {
        return refWords;
    }

    public void setRefWords(List<String> refWords) {
        this.refWords = refWords;
    }
}
