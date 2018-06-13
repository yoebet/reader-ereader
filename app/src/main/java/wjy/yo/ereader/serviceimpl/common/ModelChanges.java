package wjy.yo.ereader.serviceimpl.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.BaseModel;

public class ModelChanges {

    public static class Changes {
        List<BaseModel> toDelete;
        List<BaseModel> toInsert;
        List<BaseModel> toUpdate;

        void delete(BaseModel m) {
            if (toDelete == null) {
                toDelete = new ArrayList<>();
            }
            toDelete.add(m);
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

    private static class ModelPair {
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
                changes.delete(iv);
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


    public static void applyChanges(Changes changes, BaseDao dao, boolean performDelete) {

        if (changes == null) {
            return;
        }
        if (performDelete && changes.toDelete != null) {
            dao.delete(changes.toDelete);
        }
        if (changes.toInsert != null) {
            dao.insert(changes.toInsert);
            System.out.println("inserted: " + changes.toInsert.size());
        }
        if (changes.toUpdate != null) {
            dao.update(changes.toUpdate);
            System.out.println("updated: " + changes.toUpdate.size());
        }
    }

    public static void saveIfNeeded(BaseModel fromNetwork, BaseModel fromLocal, BaseDao dao) {
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
