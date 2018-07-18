package wjy.yo.ereader.util;

public final class Constants {

    // SQLite Full Text Index Tables
    public static final String FTS_TABLE_PARA_CONTENT = "fts_para_content";

    // Activity
    public static final String BOOK_ID_KEY = "BOOK_ID";
    public static final String CHAP_ID_KEY = "CHAP_ID";

    // DataSyncRecord
    public static final String DSR_CATEGORY_BOOK_LIST = "BOOK_LIST";
    public static final String DSR_CATEGORY_BOOK_CHAPS = "BOOK_CHAPS";
    public static final String DSR_CATEGORY_CHAP_PARAS = "CHAP_PARAS";
    // public static final String DSR_CATEGORY_ANNO_FAMILY = "ANNO_FAMILY";
    public static final String DSR_CATEGORY_USER_BOOKS = "USER_BOOKS";
    public static final String DSR_CATEGORY_USER_WORDS = "USER_WORDS";
    public static final String DSR_CATEGORY_WORD_CATEGORIES = "WORD_CATEGORIES";
    public static final String DSR_CATEGORY_PREFERENCES = "USER_PREFERENCES";

    public static final String DSR_DIRECTION_DOWN = "D";
    public static final String DSR_DIRECTION_UP = "U";

    // Preference Code
    public static final String PREF_BASE_VOCABULARY = "baseVocabulary";
    public static final String PREF_WORD_TAGS = "wordTags";

    // Setting Code
    public static final String SETTING_OFFLINE = "offline";


    // rxjava2 onNext(xxx)
    public static final String RX_STRING_ELEMENT_NULL = "NULL";
}
