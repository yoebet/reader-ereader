package wjy.yo.ereader.remotevo;

import lombok.Data;

@Data
public class PosComplete {

    private String pos;
    private PosMeaningItem[] items;

    @Data
    public static class PosMeaningItem {
        private int id;
        private String exp;
//        private String[] tags;

    }
}
