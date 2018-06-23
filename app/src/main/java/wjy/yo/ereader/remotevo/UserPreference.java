package wjy.yo.ereader.remotevo;

public class UserPreference {

    private String baseVocabulary;

    private String[] wordTags;

    private long version;

    public String getBaseVocabulary() {
        return baseVocabulary;
    }

    public void setBaseVocabulary(String baseVocabulary) {
        this.baseVocabulary = baseVocabulary;
    }

    public String[] getWordTags() {
        return wordTags;
    }

    public void setWordTags(String[] wordTags) {
        this.wordTags = wordTags;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
