package wjy.yo.ereader.model;

import java.util.Map;


public class DictEntry {

    private String word;
    private String phonetic;
    private SimpleMeaning[] simpleMeanings;
    private PosMeanings[] completeMeanings;
    private Map<String, Object> categories;
    private String[] forms;
    private String[] baseForms;

    public DictEntry(String word) {
        this.word = word;
    }

    //    ['isPhrase', 'phrases'];

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


    public String[] getForms() {
        return forms;
    }

    public void setForms(String[] forms) {
        this.forms = forms;
    }

    public String[] getBaseForms() {
        return baseForms;
    }

    public void setBaseForms(String[] baseForms) {
        this.baseForms = baseForms;
    }


    public static class MeaningItem {
        private int id;
        private String exp;
        private String[] tags;

        public MeaningItem(int id, String exp) {
            this.id = id;
            this.exp = exp;
        }

        public MeaningItem(int id, String exp, String[] tags) {
            this.id = id;
            this.exp = exp;
            this.tags = tags;
        }

        public int getId() {
            return id;
        }

        public String[] getTags() {
            return tags;
        }

        public String getExp() {
            return exp;
        }
    }

    public static class PosMeanings {
        private String pos;
        private MeaningItem[] items;

        public PosMeanings(String pos, MeaningItem[] items) {
            this.pos = pos;
            this.items = items;
        }

        public String getPos() {
            return pos;
        }

        public MeaningItem[] getItems() {
            return items;
        }
    }

    public static class SimpleMeaning {
        private String pos;
        private String exp;

        public SimpleMeaning(String pos, String exp) {
            this.pos = pos;
            this.exp = exp;
        }

        public String getPos() {
            return pos;
        }

        public String getExp() {
            return exp;
        }
    }


}

