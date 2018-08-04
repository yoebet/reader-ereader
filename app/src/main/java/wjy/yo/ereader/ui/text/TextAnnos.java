package wjy.yo.ereader.ui.text;

import java.util.ArrayList;
import java.util.List;

public class TextAnnos {

    private String head;

    private List<String> annoLabels;

    private SelectedMeaning selectedMeaning;

    private Phrase phrase;

    private String note;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public List<String> getAnnoLabels() {
        return annoLabels;
    }

    public void setAnnoLabels(List<String> annoLabels) {
        this.annoLabels = annoLabels;
    }

    public void pushAnnoLabel(String label) {
        if (annoLabels == null) {
            annoLabels = new ArrayList<>();
        }
        annoLabels.add(label);
    }

    public SelectedMeaning getSelectedMeaning() {
        return selectedMeaning;
    }

    public void setSelectedMeaning(SelectedMeaning selectedMeaning) {
        this.selectedMeaning = selectedMeaning;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isEmpty() {
        return annoLabels == null && phrase == null && selectedMeaning == null && note == null;
    }

    public static class SelectedMeaning {

        private String word;

        private String mid;

        private String meaning;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getMeaning() {
            return meaning;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }
    }

    public static class Phrase {

        private String group;

        private String words;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }

}
