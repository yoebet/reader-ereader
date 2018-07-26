package wjy.yo.ereader.ui.text;

import wjy.yo.ereader.ui.dict.DictAgent;

public class Environment implements Cloneable {

    private DictAgent dictAgent;

    private PopupWindowManager popupWindowManager;

    public DictAgent getDictAgent() {
        return dictAgent;
    }

    public void setDictAgent(DictAgent dictAgent) {
        this.dictAgent = dictAgent;
    }

    public PopupWindowManager getPopupWindowManager() {
        return popupWindowManager;
    }

    public void setPopupWindowManager(PopupWindowManager popupWindowManager) {
        this.popupWindowManager = popupWindowManager;
    }
}
