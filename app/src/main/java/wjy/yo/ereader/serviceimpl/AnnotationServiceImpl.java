package wjy.yo.ereader.serviceimpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.anno.AnnoDao;
import wjy.yo.ereader.db.anno.AnnoFamilyDao;
import wjy.yo.ereader.db.anno.AnnoGroupDao;
import wjy.yo.ereader.entity.Ordered;
import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;
import wjy.yo.ereader.entityvo.anno.AnnotationGroup;
import wjy.yo.ereader.remote.AnnotationsAPI;
import wjy.yo.ereader.service.AnnotationService;

@Singleton
public class AnnotationServiceImpl implements AnnotationService {

    @Inject
    AnnotationsAPI annotationsAPI;

    private DB db;
    private AnnoFamilyDao annoFamilyDao;
    private AnnoGroupDao annoGroupDao;
    private AnnoDao annoDao;

    private Map<String, AnnotationFamily> annosMap = new HashMap<>();

    @Inject
    public AnnotationServiceImpl(DB db) {
        this.db = db;
        this.annoFamilyDao = db.annoFamilyDao();
        this.annoGroupDao = db.annoGroupDao();
        this.annoDao = db.annoDao();
    }

    private Maybe<AnnotationFamily> loadFromDB(String id) {
        return annoFamilyDao.loadAnnotations(id)
                .map(annof -> {
                    List<AnnotationGroup> groups = annof.getGroups();
                    if (groups == null) {
                        return annof;
                    }
                    Collections.sort(groups, Ordered.Comparator);

                    for (AnnotationGroup group : groups) {
                        List<Anno> annos = group.getAnnotations();
                        if (annos == null) {
                            continue;
                        }
                        Collections.sort(annos, Ordered.Comparator);
                    }
                    return annof;
                });
    }

    private void saveAnnotations(AnnotationFamily annof) {

        db.runInTransaction(() -> {
            annoFamilyDao.insert(annof);
            List<AnnotationGroup> groups = annof.getGroups();
            if (groups == null) {
                return;
            }

            for (AnnotationGroup group : groups) {
                annoGroupDao.insert(group);
                List<Anno> annos = group.getAnnotations();
                if (annos == null) {
                    continue;
                }
                int no = 1;
                for (Anno anno : annos) {
                    anno.setGroupId(group.getId());
                    anno.setNo(no++);
                    annoDao.insert(anno);
                }
            }
        });
    }

    private void setGroups(AnnotationFamily annof) {

        List<AnnotationGroup> groups = annof.getGroups();
        if (groups == null) {
            return;
        }

        for (AnnotationGroup group : groups) {
            List<Anno> annos = group.getAnnotations();
            if (annos == null) {
                continue;
            }
            for (Anno anno : annos) {
                anno.setGroupId(group.getId());
                anno.setGroup(group);
            }
        }
    }


    public Maybe<AnnotationFamily> loadAnnotations(String id) {
        AnnotationFamily af = annosMap.get(id);
        if (af != null) {
            return Maybe.just(af);
        }
        Maybe<AnnotationFamily> dbSource = loadFromDB(id);
        Maybe<AnnotationFamily> netSource = annotationsAPI.getAnnotations(id)
                .map(annof -> {
                    saveAnnotations(annof);
                    return annof;
                });

        return dbSource.switchIfEmpty(netSource)
                .map(annof -> {
                    annosMap.put(id, annof);
                    setGroups(annof);
                    return annof;
                });
    }
}
