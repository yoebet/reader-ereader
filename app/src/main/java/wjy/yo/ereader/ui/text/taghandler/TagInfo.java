package wjy.yo.ereader.ui.text.taghandler;

import java.util.Map;

import wjy.yo.ereader.ui.text.SpanLocation;

class TagInfo {

    private String name;

    private SpanLocation location;

    private Map<String, String> attributeMap;

    TagInfo(String name) {
        this.name = name;
        this.location = new SpanLocation(-1, -1);
    }

    void setLocationStart(int start) {
        location.setStart(start);
    }

    void setLocationEnd(int end) {
        location.setEnd(end);
    }

    String getName() {
        return name;
    }

    SpanLocation getLocation() {
        return location;
    }

    void setLocation(SpanLocation location) {
        this.location = location;
    }

    Map<String, String> getAttributeMap() {
        return attributeMap;
    }

    void setAttributeMap(Map<String, String> attributeMap) {
        this.attributeMap = attributeMap;
    }
}
