package wjy.yo.ereader.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VocabularyStatistic {

    private int baseVocabularyCount;

    private int userWordsCount;

    private int familiarity1Count;

    private int familiarity2Count;

    private int familiarity3Count;

    private int unfamiliarCountInBV;

    private int familiarCountNotInBV;

    private int graspedCount;
}
