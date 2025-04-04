package wjy.yo.ereader.ui.common;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.util.ArrayDeque;

public class HtmlParser implements Html.TagHandler, ContentHandler {

    public interface TagHandler {
        boolean handleTagOpen(String tag, Editable output, Attributes attributes);

        void handleTagEnd(String tag, Editable output);
    }

    public static Spanned buildSpannedText(String html, TagHandler handler) {
        return Html.fromHtml("<inject/>" + html, null, new HtmlParser(handler));
    }

    private final TagHandler handler;
    private ContentHandler wrapped;
    private Editable text;
    private ArrayDeque<Boolean> tagStatus = new ArrayDeque<>();

    private HtmlParser(TagHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (wrapped == null) {
            text = output;

            wrapped = xmlReader.getContentHandler();

            xmlReader.setContentHandler(this);
            tagStatus.addLast(Boolean.FALSE);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        boolean isHandled = handler.handleTagOpen(localName, text, attributes);
        tagStatus.addLast(isHandled);
        if (!isHandled)
            wrapped.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!tagStatus.removeLast())
            wrapped.endElement(uri, localName, qName);
        handler.handleTagEnd(localName, text);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        wrapped.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        wrapped.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        wrapped.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        wrapped.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        wrapped.endPrefixMapping(prefix);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        wrapped.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        wrapped.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        wrapped.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        wrapped.skippedEntity(name);
    }
}