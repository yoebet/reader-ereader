package wjy.yo.ereader.remotevo;

import lombok.Data;

@Data
public class UserPreference {

    private String baseVocabulary;

    private String[] wordTags;

    private long version;

}
