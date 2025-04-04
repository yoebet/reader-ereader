package wjy.yo.ereader.ui.dict;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.util.Action;
import wjy.yo.ereader.vo.WordContext;

public class DictRequest {

    public final String lookupWord;
    public final DictEntry entry;
    public final DictAgent agent;

    private WordContext wordContext;

    private Action onOpen;

    private Action onClose;

    private List<String> refWords;

    private Maybe<UserWord> userWord;
    private Single<List<String>> rankLabels;
    private Maybe<WordCategory> baseVocabularyCategory;

    public DictRequest(DictAgent agent, String lookupWord, DictEntry entry) {
        this.agent = agent;
        this.lookupWord = lookupWord;
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

    public Maybe<WordCategory> getBaseVocabularyCategory() {
        return baseVocabularyCategory;
    }

    public void setBaseVocabularyCategory(Maybe<WordCategory> baseVocabularyCategory) {
        this.baseVocabularyCategory = baseVocabularyCategory;
    }

    public List<String> getRefWords() {
        return refWords;
    }

    public void setRefWords(List<String> refWords) {
        this.refWords = refWords;
    }

    public WordContext getWordContext() {
        return wordContext;
    }

    public void setWordContext(WordContext wordContext) {
        this.wordContext = wordContext;
    }

    public Action getOnOpen() {
        return onOpen;
    }

    public void setOnOpen(Action onOpen) {
        this.onOpen = onOpen;
    }

    public Action getOnClose() {
        return onClose;
    }

    public void setOnClose(Action onClose) {
        this.onClose = onClose;
    }

    public void callOnOpenAction() {
        if (onOpen != null) {
            onOpen.run();
            onOpen = null;
        }
    }

    public void callCloseAction() {
        if (onClose != null) {
            onClose.run();
            onClose = null;
        }
    }
}
