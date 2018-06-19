package wjy.yo.ereader.serviceimpl.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.FetchedData;

public class ModelChanges {

    public static class Changes {
        List<FetchedData> toDelete;
        List<FetchedData> toInsert;
        List<FetchedData> toUpdate;

        void delete(FetchedData m) {
            if (toDelete == null) {
                toDelete = new ArrayList<>();
            }
            toDelete.add(m);
        }

        void insert(FetchedData m) {
            if (toInsert == null) {
                toInsert = new ArrayList<>();
            }
            toInsert.add(m);
        }

        void update(FetchedData m) {
            if (toUpdate == null) {
                toUpdate = new ArrayList<>();
            }
            toUpdate.add(m);
        }

    }

    private static class ModelPair {
        FetchedData m;
        FetchedData iv;
    }

    private static ModelPair getPair(Map<String, ModelPair> pairs, FetchedData m) {
        String id = m.getId();
        ModelPair pair = pairs.get(id);
        if (pair == null) {
            pair = new ModelPair();
            pairs.put(id, pair);
        }
        return pair;
    }

    public static Changes evaluateChanges(List<FetchedData> dataFromNetwork, List<FetchedData> dataFromLocalDB) {

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
                FetchedData model = dataFromNetwork.get(i);
                FetchedData iv = dataFromLocalDB.get(i);
                if (model.changed(iv) ||
                        !Objects.equals(model.getLastFetchAt(), iv.getLastFetchAt())) {
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
        for (FetchedData m : dataFromNetwork) {
            ModelPair pair = getPair(pairs, m);
            pair.m = m;
        }
        for (FetchedData iv : dataFromLocalDB) {
            ModelPair pair = getPair(pairs, iv);
            pair.iv = iv;
        }

        for (Map.Entry<String, ModelPair> pairEntry : pairs.entrySet()) {
            ModelPair pair = pairEntry.getValue();
            FetchedData m = pair.m;// fetch from Network
            FetchedData iv = pair.iv;// from local DB
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

}
