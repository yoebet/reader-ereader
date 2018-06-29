package wjy.yo.ereader.vo;


public class VocabularyStatistic {

    private int baseVocabularyCount;

    private int userWordsCount;

    private int familiarity1Count;

    private int familiarity2Count;

    private int familiarity3Count;

    private int unfamiliarCountInBV;

    private int familiarCountNotInBV;

    private int graspedCount;

    public int getBaseVocabularyCount() {
        return baseVocabularyCount;
    }

    public void setBaseVocabularyCount(int baseVocabularyCount) {
        this.baseVocabularyCount = baseVocabularyCount;
    }

    public int getUserWordsCount() {
        return userWordsCount;
    }

    public void setUserWordsCount(int userWordsCount) {
        this.userWordsCount = userWordsCount;
    }

    public int getFamiliarity1Count() {
        return familiarity1Count;
    }

    public void setFamiliarity1Count(int familiarity1Count) {
        this.familiarity1Count = familiarity1Count;
    }

    public int getFamiliarity2Count() {
        return familiarity2Count;
    }

    public void setFamiliarity2Count(int familiarity2Count) {
        this.familiarity2Count = familiarity2Count;
    }

    public int getFamiliarity3Count() {
        return familiarity3Count;
    }

    public void setFamiliarity3Count(int familiarity3Count) {
        this.familiarity3Count = familiarity3Count;
    }

    public int getUnfamiliarCountInBV() {
        return unfamiliarCountInBV;
    }

    public void setUnfamiliarCountInBV(int unfamiliarCountInBV) {
        this.unfamiliarCountInBV = unfamiliarCountInBV;
    }

    public int getFamiliarCountNotInBV() {
        return familiarCountNotInBV;
    }

    public void setFamiliarCountNotInBV(int familiarCountNotInBV) {
        this.familiarCountNotInBV = familiarCountNotInBV;
    }

    public int getGraspedCount() {
        return graspedCount;
    }

    public void setGraspedCount(int graspedCount) {
        this.graspedCount = graspedCount;
    }
}
