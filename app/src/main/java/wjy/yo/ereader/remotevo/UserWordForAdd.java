package wjy.yo.ereader.remotevo;

import lombok.Data;
import wjy.yo.ereader.entity.userdata.UserWord;

@Data
public class UserWordForAdd {

    private String word;

    private int familiarity;

    private String bookId;

    private String chapId;

    private String paraId;


    public UserWordForAdd(String word) {
        this.word = word;
    }

    public static UserWordForAdd from(UserWord uw) {
        UserWordForAdd vo = new UserWordForAdd(uw.getWord());
        vo.setFamiliarity(uw.getFamiliarity());
        vo.setBookId(uw.getBookId());
        vo.setChapId(uw.getChapId());
        vo.setParaId(uw.getParaId());
        return vo;
    }

}
