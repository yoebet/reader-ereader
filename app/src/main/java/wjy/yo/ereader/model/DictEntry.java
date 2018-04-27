package wjy.yo.ereader.model;

public class DictEntry {

    private String word;
    private String phonetic;
    private SimpleMeaning[] simple;
    private PosMeanings[] complete;

//    let updateFields = [
//            'categories', 'complete', 'phonetics', 'forms', 'baseForms', 'isPhrase', 'phrases'];


    static class SimpleMeaning {
        String pos;
        String exp;
    }

    static class MeaningItem {
        int id;
        String[] tags;
        String exp;
    }

    static class PosMeanings {
        String pos;
        MeaningItem[] items;
    }

}
