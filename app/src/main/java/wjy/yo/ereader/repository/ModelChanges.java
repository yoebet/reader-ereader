package wjy.yo.ereader.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.BaseModel;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.IdVersion;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;

class ModelChanges {

    static class Changes {
        List<String> toDelete;
        List<BaseModel> toInsert;
        List<BaseModel> toUpdate;

        void delete(String id) {
            if (toDelete == null) {
                toDelete = new ArrayList<>();
            }
            toDelete.add(id);
        }

        void insert(BaseModel m) {
            if (toInsert == null) {
                toInsert = new ArrayList<>();
            }
            toInsert.add(m);
        }

        void update(BaseModel m) {
            if (toUpdate == null) {
                toUpdate = new ArrayList<>();
            }
            toUpdate.add(m);
        }

    }

    static class ModelPair {
        BaseModel m;
        BaseModel iv;
    }

    private static ModelPair getPair(Map<String, ModelPair> pairs, BaseModel m) {
        String id = m.getId();
        ModelPair pair = pairs.get(id);
        if (pair == null) {
            pair = new ModelPair();
            pairs.put(id, pair);
        }
        return pair;
    }

    public static Changes evaluateChanges(List<BaseModel> dataFromNetwork, List<BaseModel> dataFromLocalDB) {

        Changes changes = new Changes();

        if (dataFromLocalDB == null || dataFromLocalDB.size() == 0) {
            if (dataFromNetwork.size() > 0) {
                System.out.println("insert all ...");
                changes.toInsert = dataFromNetwork;
            }
            return changes;
        }

        int newSize = dataFromNetwork.size();
        if (newSize == dataFromLocalDB.size()) {
            boolean changed = false;
            for (int i = 0; i < newSize; i++) {
                BaseModel model = dataFromNetwork.get(i);
                BaseModel iv = dataFromLocalDB.get(i);
                if (model.changed(iv)) {
                    changed = true;
                    break;
                }
            }
            if (!changed) {
                System.out.println("unchanged ...");
                return changes;
            }
        }

        Map<String, ModelPair> pairs = new HashMap<>();
        for (BaseModel m : dataFromNetwork) {
            ModelPair pair = getPair(pairs, m);
            pair.m = m;
        }
        for (BaseModel iv : dataFromLocalDB) {
            ModelPair pair = getPair(pairs, iv);
            pair.iv = iv;
        }

        for (Map.Entry<String, ModelPair> pairEntry : pairs.entrySet()) {
            ModelPair pair = pairEntry.getValue();
            BaseModel m = pair.m;// fetch from Network
            BaseModel iv = pair.iv;// from local DB
            if (m == null) {
                changes.delete(iv.getId());
                continue;
            }
            if (iv == null) {
                changes.insert(m);
                continue;
            }
            if (m.changed(iv)) {
                if (m.getVersion() > iv.getVersion()) {
                    changes.update(m);
                } else {
                    // do nothing
                    System.out.println("local version go ahead:" + m);
                }
            }
        }

        return changes;
    }


    static void applyChanges(Changes changes, BaseDao dao, boolean performDelete) {

        if (changes == null) {
            return;
        }
        if (performDelete && changes.toDelete != null) {
            dao.deleteByIds(changes.toDelete);
        }
        if (changes.toInsert != null) {
            long[] inserted = dao.insert(changes.toInsert);
            System.out.println("inserted: " + Arrays.toString(inserted));
        }
        if (changes.toUpdate != null) {
            dao.update(changes.toUpdate);
            System.out.println("updated: " + changes.toUpdate.size());
        }
    }

    static void saveIfNeeded(BaseModel fromNetwork, BaseModel fromLocal, BaseDao dao) {
        if (fromLocal == null) {
            dao.insert(fromNetwork);
            System.out.println("inserted: " + fromNetwork);
            return;
        }
        if (fromNetwork.getVersion() > fromLocal.getVersion()) {
            dao.update(fromNetwork);
            System.out.println("updated: " + fromNetwork);
        }
    }

}
