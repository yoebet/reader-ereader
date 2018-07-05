package wjy.yo.ereader.vo;

import org.joda.time.Period;

public class VocabularyFilter {

    private boolean familiarityAll;
    private boolean familiarity1;
    private boolean familiarity2;
    private boolean familiarity3;

    private Period period;

    private GroupBy groupBy = GroupBy.NONE;

    public enum GroupBy {
        NONE,
        AddDate,
        Chapter,
        Familiarity
    }

    public boolean isFamiliarityAll() {
        return familiarityAll;
    }

    public void setFamiliarityAll(boolean familiarityAll) {
        this.familiarityAll = familiarityAll;
    }

    public boolean isFamiliarity1() {
        return familiarity1;
    }

    public void setFamiliarity1(boolean familiarity1) {
        this.familiarity1 = familiarity1;
    }

    public boolean isFamiliarity2() {
        return familiarity2;
    }

    public void setFamiliarity2(boolean familiarity2) {
        this.familiarity2 = familiarity2;
    }

    public boolean isFamiliarity3() {
        return familiarity3;
    }

    public void setFamiliarity3(boolean familiarity3) {
        this.familiarity3 = familiarity3;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public GroupBy getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
    }

    public String toString() {
        return "familiarity: " + (familiarityAll ? "all" :
                (familiarity1 ? "1" : "") + (familiarity2 ? "2" : "") + (familiarity3 ? "3" : ""))
                + " period: " + (period == null ? "all" : period)
                + " groupBy: " + groupBy;
    }
}
