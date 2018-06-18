package wjy.yo.ereader.entityvo.dict;

public class PosComplete {

    private String pos;
    private MeaningItem[] items;

    public PosComplete(String pos, MeaningItem[] items) {
        this.pos = pos;
        this.items = items;
    }

    public String getPos() {
        return pos;
    }

    public MeaningItem[] getItems() {
        return items;
    }

    public static class MeaningItem {
        private int id;
        private String exp;
//        private String[] tags;

        public MeaningItem(int id, String exp) {
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
