package wjy.yo.ereader.remotevo;


public class PosComplete {

    private String pos;
    private PosMeaningItem[] items;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public PosMeaningItem[] getItems() {
        return items;
    }

    public void setItems(PosMeaningItem[] items) {
        this.items = items;
    }

    public static class PosMeaningItem {
        private int id;
        private String exp;
//        private String[] tags;

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }
    }
}
