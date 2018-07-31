package wjy.yo.ereader.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.text.Layout;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.UserData;

public class Utils {

    public static String join(String[] elements) {
        return join(elements, ",");
    }

    public static String join(String[] elements, CharSequence delimiter) {
        if (elements == null || elements.length == 0) {
            return "";
        }
        StringBuilder joined = new StringBuilder();
        for (CharSequence s : elements) {
            joined.append(delimiter);
            joined.append(s);
        }
        return joined.substring(delimiter.length());
    }

    public static <T extends FetchedData> Map<String, T> collectIdMap(List<T> list) {
        Map<String, T> map = new HashMap<>();
        for (T fd : list) {
            map.put(fd.getId(), fd);
        }
        return map;
    }

    public static <T extends FetchedData> Set<String> updateData(
            List<T> list,
            List<T> localList,
            BaseDao<T> dao,
            boolean performDelete,
            BiFunction<T, T> preUpdate) {

        Map<String, T> map = collectIdMap(list);
        Map<String, T> localMap = collectIdMap(localList);

        Set<String> keepIds = new HashSet<>();
        int delete = 0, update = 0, insert = 0, skip = 0;
        Date now = new Date();
        List<String> ids = new ArrayList<>(localMap.keySet());
        for (String id : ids) {
            T local = localMap.remove(id);
            T remote = map.remove(id);
            if (local instanceof UserData && ((UserData) local).isLocal()) {
                skip++;
                keepIds.add(id);
                continue;
            }
            if (remote == null) {
                if (performDelete) {
                    dao.delete(local);
                    delete++;
                }
                continue;
            }
            if (local.equals(remote)) {
                keepIds.add(id);
                continue;
            }
            remote.setLastFetchAt(now);
            if (preUpdate != null) {
                preUpdate.apply(remote, local);
            }
            dao.update(remote);
            update++;
        }
        for (T t : map.values()) {
            t.setLastFetchAt(now);
            dao.insert(t);
            insert++;
        }
        String stat = "updateData, D " + delete + ", U " + update + ", I " + insert;
        if (skip > 0) {
            stat += ", L " + skip;
        }
        stat += ", K " + keepIds.size();
        System.out.println(stat);

        return keepIds;
    }


    public static <T extends FetchedData> Set<String> updateData(
            List<T> list,
            List<T> localList,
            BaseDao<T> dao,
            boolean performDelete) {
        return updateData(list, localList, dao, performDelete, null);
    }


    public static <T extends FetchedData, U extends FetchedData> boolean versionEquals(
            List<T> list1,
            List<U> list2) {

        if (list1 == null || list2 == null) {
            return list1 == list2;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        Iterator<T> e1 = list1.iterator();
        Iterator<U> e2 = list2.iterator();
        while (e1.hasNext() && e2.hasNext()) {
            T o1 = e1.next();
            U o2 = e2.next();
            if (o1 == null || o2 == null) {
                if (o1 == o2) {
                    continue;
                } else {
                    return false;
                }
            }
            if (!Objects.equals(o1.getId(), o2.getId())) {
                return false;
            }
            if (o1.getVersion() != o2.getVersion()) {
                return false;
            }
        }
        return true;
    }

    public static Offset calculateOffset(TextView textView, int start, int end) {

        Layout textLayout = textView.getLayout();

        float startXCoordinate = textLayout.getPrimaryHorizontal(start);
//        float endXCoordinate = textLayout.getPrimaryHorizontal(end);
//        System.out.println("XCoordinates " + startXCoordinate + "-" + endXCoordinate);

        int startLine = textLayout.getLineForOffset(start);
        int endLine = textLayout.getLineForOffset(end);
//        System.out.println("LineSpan " + startLine + "-" + endLine);

        int offsetY;
        int offsetX = 0;

//            int wh=textView.getWidth();
        if (startLine == endLine) {
            offsetX = (int) startXCoordinate;
        }

        int th = textView.getHeight();
//        System.out.println("getHeight " + th);
        Rect rect = new Rect();
        int baseLine2 = textLayout.getLineBounds(endLine, rect);
        int dy = th - rect.bottom;
        offsetY = -dy;

//        System.out.println("offset: " + offsetX + "," + offsetY);

        return new Offset(offsetX, offsetY);
    }


    public static int dpToPx(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
