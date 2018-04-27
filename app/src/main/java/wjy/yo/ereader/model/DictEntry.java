package wjy.yo.ereader.model;

import java.util.Map;

import wjy.yo.ereader.model.dict.PosMeanings;
import wjy.yo.ereader.model.dict.SimpleMeaning;

public class DictEntry {

    private String word;
    private String phonetic;
    private SimpleMeaning[] simpleMeanings;
    private PosMeanings[] completeMeanings;
    private Map<String, Object> categories;

    public DictEntry(String word) {
        this.word = word;
    }

    //    let updateFields = [
//            'forms', 'baseForms', 'isPhrase', 'phrases'];


    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public SimpleMeaning[] getSimpleMeanings() {
        return simpleMeanings;
    }

    public void setSimpleMeanings(SimpleMeaning[] simpleMeanings) {
        this.simpleMeanings = simpleMeanings;
    }

    public PosMeanings[] getCompleteMeanings() {
        return completeMeanings;
    }

    public void setCompleteMeanings(PosMeanings[] completeMeanings) {
        this.completeMeanings = completeMeanings;
    }

    public Map<String, Object> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Object> categories) {
        this.categories = categories;
    }
}
