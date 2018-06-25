package wjy.yo.ereader.util;

public class Utils {

    public static String join(String[] elements) {
        return join(elements, ",");
    }

    public static String join(
            String[] elements, CharSequence delimiter) {
        if (elements == null || elements.length == 0) {
            return "";
        }
        StringBuilder joined = new StringBuilder();
        for (CharSequence s : elements) {
            joined.append(delimiter);
            joined.append(s);
        }
        return joined.substring(delimiter.length());
    }
}
