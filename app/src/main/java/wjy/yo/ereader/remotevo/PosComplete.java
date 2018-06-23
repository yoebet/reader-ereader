package wjy.yo.ereader.remotevo;

public class PosComplete {

    private String pos;
    private PosMeaningItem[] items;

    public PosComplete(String pos, PosMeaningItem[] items) {
        this.pos = pos;
        this.items = items;
    }

    public String getPos() {
        return pos;
    }

    public PosMeaningItem[] getItems() {
        return items;
    }

    public static class PosMeaningItem {
        private int id;
        private String exp;
//        private String[] tags;

        public PosMeaningItem(int id, String exp) {
            this.id = id;
            this.exp = exp;
        }

        /*public MeaningItem(int id, String exp, String[] tags) {
            this.id = id;
            this.exp = exp;
            this.tags = tags;
        }*/

        public int getId() {
            return id;
        }

//        public String[] getTags() {
//            return tags;
//        }

        public String getExp() {
            return exp;
        }
    }
}
