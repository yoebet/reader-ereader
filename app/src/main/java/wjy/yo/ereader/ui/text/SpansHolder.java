package wjy.yo.ereader.ui.text;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpansHolder<T> {

    private Map<Class<? extends T>, List<? extends T>> spansMap;

    public SpansHolder() {
        spansMap = new ArrayMap<>();
    }

    public <S extends T> void push(S span) {
        Class<S> clazz = (Class<S>) span.getClass();
        List<S> spans = getSpansForPush(clazz);
        spans.add(span);
    }

    public <S extends T> List<S> getSpans(Class<S> clazz) {
        List<S> spans = (List<S>) spansMap.get(clazz);
        return spans;
    }

    public <S extends T> List<S> getSpansForPush(Class<S> clazz) {
        List<S> spans = getSpans(clazz);
        if (spans == null) {
            spans = new ArrayList<>();
            spansMap.put(clazz, spans);
        }
        return spans;
    }

    public <S extends T> List<S> removeSpans(Class<S> clazz) {
        List<S> spans = (List<S>) spansMap.remove(clazz);
        return spans;
    }

    public Map<Class<? extends T>, List<? extends T>> getSpansMap() {
        return spansMap;
    }
}
