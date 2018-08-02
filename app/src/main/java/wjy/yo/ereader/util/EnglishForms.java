package wjy.yo.ereader.util;

import java.util.ArrayList;
import java.util.List;

public class EnglishForms {

    public static List<String> guestBaseForms(final String word) {
        List<String> forms = new ArrayList<>();
        int len = word.length();
        if (len <= 3) {
            return forms;
        }
        if (word.endsWith("s")) {
            if (word.endsWith("es")) {
                if (word.endsWith("ies")) {
                    forms.add(word.substring(0, len - 3) + "y");
                } else {
                    forms.add(word.substring(0, len - 2));
                }
            }
            forms.add(word.substring(0, len - 1));
            return forms;
        }

        if (word.endsWith("ed")) {
            if (len <= 4) {
                return forms;
            }
            if (word.charAt(len - 3) == word.charAt(len - 4)) {
                forms.add(word.substring(0, len - 3));
            } else {
                forms.add(word.substring(0, len - 2));
            }
            if (word.endsWith("ied")) {
                forms.add(word.substring(0, len - 3) + "y");
            } else {
                forms.add(word.substring(0, len - 1));
            }
            return forms;
        }

        if (word.endsWith("ing")) {
            if (len <= 6) {
                return forms;
            }
            if (word.charAt(len - 4) == word.charAt(len - 5)) {
                forms.add(word.substring(0, len - 4));
            } else {
                String st = word.substring(0, len - 3);
                forms.add(st);
                forms.add(st + "e");
            }
            return forms;
        }

        if (word.endsWith("er")) {
            forms.add(word.substring(0, len - 1));
            forms.add(word.substring(0, len - 2));
            return forms;
        }

        if (word.endsWith("est")) {
            if (len <= 6) {
                return forms;
            }
            forms.add(word.substring(0, len - 2));
            if (word.charAt(len - 4) == 'i') {
                forms.add(word.substring(0, len - 4) + "y");
            } else if (word.charAt(len - 4) == word.charAt(len - 5)) {
                forms.add(word.substring(0, len - 4));
            } else {
                forms.add(word.substring(0, len - 3));
            }
        }
        return forms;
    }

    public static String guestStem(String word) {
        int len = word.length();
        String[] affixes = new String[]{"ly", "ness", "ful", "ment", "less", "or"};
        for (String affix : affixes) {
            if (word.endsWith(affix)) {
                int wlen = len - affix.length();
                if (wlen <= 3) {
                    return null;
                }
                return word.substring(0, wlen);
            }
        }
        if (word.endsWith("ion") && len > 5) {
            return word.substring(0, len - 3) + "e";
        }
        return null;
    }
}
