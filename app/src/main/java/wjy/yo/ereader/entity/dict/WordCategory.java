package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "dict_word_category", indices = {@Index(value = "code", unique = true)})
@ForeignKey(entity = WordCategory.class, parentColumns = "code", childColumns = "extendTo", onDelete = ForeignKey.CASCADE)
public class WordCategory extends FetchedData {

    private String code;

    private String name;

    private String dictKey;

    private Integer dictValue;

    private String dictOperator;

    private String description;

    private Integer wordCount;

    private String extendTo;

    private Integer extendedWordCount;

    private long no;

    @Ignore
    private WordCategory extend;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public Integer getDictValue() {
        return dictValue;
    }

    public void setDictValue(Integer dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictOperator() {
        return dictOperator;
    }

    public void setDictOperator(String dictOperator) {
        this.dictOperator = dictOperator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public String getExtendTo() {
        return extendTo;
    }

    public void setExtendTo(String extendTo) {
        this.extendTo = extendTo;
    }

    public Integer getExtendedWordCount() {
        return extendedWordCount;
    }

    public void setExtendedWordCount(Integer extendedWordCount) {
        this.extendedWordCount = extendedWordCount;
    }

    public WordCategory getExtend() {
        return extend;
    }

    public void setExtend(WordCategory extend) {
        this.extend = extend;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }
}
