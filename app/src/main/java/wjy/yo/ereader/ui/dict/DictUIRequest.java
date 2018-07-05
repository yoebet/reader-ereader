package wjy.yo.ereader.ui.dict;

import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;

class DictUIRequest {
    public final DictEntry entry;
    public final UserWord userWord;

    DictUIRequest(DictEntry entry, UserWord userWord) {
        this.entry = entry;
        this.userWord = userWord;
    }
}
