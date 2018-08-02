package wjy.yo.ereader.ui.text;

import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.textview.TextStatusHolder;

public class Settings {

    public static final int DICT_MODE_SIMPLE_POPUP = 1;

    public static final int DICT_MODE_BOTTOM_SHEET = 2;

    private VocabularyService vocabularyService;

    private DictAgent dictAgent;

    private PopupWindowManager popupWindowManager;

    private int dictMode = DICT_MODE_BOTTOM_SHEET;

    private boolean handleAnnotations = true;

    private boolean handleNewWords = true;

    private TextSetting textSetting;

    private TextStatusHolder textStatusHolder;

    public VocabularyService getVocabularyService() {
        return vocabularyService;
    }

    public void setVocabularyService(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

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

    public int getDictMode() {
        return dictMode;
    }

    public void setDictMode(int dictMode) {
        this.dictMode = dictMode;
    }

    public boolean isHandleAnnotations() {
        return handleAnnotations;
    }

    public void setHandleAnnotations(boolean handleAnnotations) {
        this.handleAnnotations = handleAnnotations;
    }

    public boolean isHandleNewWords() {
        return handleNewWords;
    }

    public void setHandleNewWords(boolean handleNewWords) {
        this.handleNewWords = handleNewWords;
    }

    public TextSetting getTextSetting() {
        return textSetting;
    }

    public void setTextSetting(TextSetting textSetting) {
        this.textSetting = textSetting;
    }

    public TextStatusHolder getTextStatusHolder() {
        return textStatusHolder;
    }

    public void setTextStatusHolder(TextStatusHolder textStatusHolder) {
        this.textStatusHolder = textStatusHolder;
    }
}
