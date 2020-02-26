/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xml.transform;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * TransformerBase provides support for writing Object->XML encoders. The basic pattern for useage
 * is to extend TransformerBase and implement the createTranslator(ContentHandler) method. This is
 * easiest done by extending the inner class TranslatorSupport. A Translator uses a ContentHandler
 * to issue SAX events to a javax.xml.transform.Transformer. If possible, make the translator public
 * so it can be used by others as well.
 *
 * @author Ian Schneider
 */
public abstract class TransformerBase {
    private int indentation = -1;
    private boolean xmlDecl = false;
    private boolean nsDecl = true;
    private Charset charset = Charset.forName("UTF-8");

    public static final String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";

    /** Create a Translator to issue SAXEvents to a ContentHandler. */
    public abstract Translator createTranslator(ContentHandler handler);

    /** Create a Transformer which is initialized with the settings of this TransformerBase. */
    public Transformer createTransformer() throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        if (indentation > -1) {
            try {
                tFactory.setAttribute("indent-number", Integer.valueOf(indentation));
            } catch (IllegalArgumentException e) {
                // throw away (java 1.4 doesn't support this method, but java 1.5 requires it)
            }
        }

        Transformer transformer = tFactory.newTransformer();

        if (indentation > -1) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", Integer.toString(indentation));
        } else {
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
        }

        if (xmlDecl) {
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } else {
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        }

        transformer.setOutputProperty(OutputKeys.ENCODING, charset.name());

        return transformer;
    }

    /**
     * Perform the XML encoding on the given object to the given OutputStream. Calls
     * transform(Object,StreamResult);
     */
    public void transform(Object object, java.io.OutputStream out) throws TransformerException {
        transform(object, new StreamResult(out));
    }

    /**
     * Perform the XML encoding on the given object to the given Writer. Calls
     * transform(Object,StreamResult);
     */
    public void transform(Object object, java.io.Writer out) throws TransformerException {
        transform(object, new StreamResult(out));
    }

    /**
     * Perform the XML encoding on the given object to the given OutputStream. Calls
     * createTransformer(),createXMLReader() and Transformer.transform().
     */
    public void transform(Object object, StreamResult result) throws TransformerException {

        Task t = createTransformTask(object, result);
        t.run();
        if (t.checkError()) {
            Exception e = t.getError();
            if (!TransformerException.class.isAssignableFrom(e.getClass())) {
                e = new TransformerException("Translator error", e);
            }
            throw (TransformerException) e;
        }
    }

    /**
     * Create a Transformation task. This is a Runnable task which supports aborting any processing.
     * It will not start until the run method is called.
     */
    public Task createTransformTask(Object object, StreamResult result)
            throws TransformerException {

        return new Task(object, result);
    }

    /**
     * Perform the XML encoding of the given object into an internal buffer and return the resulting
     * String. Calls transform(Object,Writer). <em>It should be noted the most efficient mechanism
     * of encoding is using the OutputStream or Writer methods</em>
     */
    public String transform(Object object) throws TransformerException {
        StringWriter sw = new StringWriter();
        transform(object, sw);
        return sw.getBuffer().toString();
    }

    /** Create an XMLReader to use in the transformation. */
    public XMLReaderSupport createXMLReader(Object object) {
        return new XMLReaderSupport(this, object);
    }

    /**
     * Get the number of spaces to indent the output xml. Defaults to -1.
     *
     * @return The number of spaces to indent, or -1, to disable.
     */
    public int getIndentation() {
        return indentation;
    }

    /**
     * Set the number of spaces to indent the output xml. Default to -1.
     *
     * @param amt The number of spaces to indent if > 0, otherwise disable.
     */
    public void setIndentation(int amt) {
        indentation = amt;
    }

    /**
     * Gets the charset to declare in the header of the response.
     *
     * @return the charset to encode with.
     */
    public Charset getEncoding() {
        return charset;
    }

    /**
     * Sets the charset to declare in the xml header returned.
     *
     * @param charset A charset object of the desired encoding
     */
    public void setEncoding(Charset charset) {
        this.charset = charset;
    }

    /**
     * Will this transformation omit the standard XML declaration. <b>Defaults to false</b>
     *
     * @return true if the XML declaration will be omitted, false otherwise.
     */
    public boolean isOmitXMLDeclaration() {
        return xmlDecl;
    }

    /**
     * Set this transformer to omit/include the XML declaration. <b>Defaults to false</b>
     *
     * @param xmlDecl Omit/include the XML declaration.
     */
    public void setOmitXMLDeclaration(boolean xmlDecl) {
        this.xmlDecl = xmlDecl;
    }

    /**
     * Should this transformer declare namespace prefixes in the first element it outputs? Defaults
     * to true.
     *
     * @return true if namespaces will be declared, false otherwise
     */
    public boolean isNamespaceDeclartionEnabled() {
        return nsDecl;
    }

    /**
     * Enable declaration of namespace prefixes in the first element. Defaults to true;
     *
     * @param enabled Enable namespace declaration.
     */
    public void setNamespaceDeclarationEnabled(boolean enabled) {
        nsDecl = enabled;
    }

    /**
     * A wrapper for a Transformation Task. Support aborting any translation activity. Because the
     * Task is Runnable, exceptions must be checked asynchronously by using the checkError and
     * getError methods.
     */
    public class Task implements Runnable {

        // private final Translator translator;
        private final Transformer transformer;
        private final Source xmlSource;
        private final StreamResult result;
        private final XMLReaderSupport reader;
        private Exception error;

        public Task(Object object, StreamResult result) throws TransformerException {
            transformer = createTransformer();
            reader = createXMLReader(object);

            xmlSource = new SAXSource(createXMLReader(object), new InputSource());

            this.result = result;
        }

        /**
         * Did an error occur?
         *
         * @return true if one did, false otherwise.
         */
        public boolean checkError() {
            return error != null;
        }

        /**
         * Get any error which occurred.
         *
         * @return An Exception if checkError returns true, null otherwise.
         */
        public Exception getError() {
            return error;
        }

        /**
         * Calls to the underlying translator to abort any calls to translation. Should return
         * silently regardless of outcome.
         */
        public void abort() {
            Translator t = reader.getTranslator();
            if (t != null) t.abort();
        }

        /**
         * Perform the translation. Exceptions are captured and can be obtained through the
         * checkError and getError methods.
         */
        public void run() {
            try {
                transformer.transform(xmlSource, result);
            } catch (Exception re) {
                error = re;
            }
        }
    }

    /**
     * Filter output from a ContentHandler and insert Namespace declarations in the first element.
     */
    private static class ContentHandlerFilter implements ContentHandler, LexicalHandler {
        private final ContentHandler original;
        private AttributesImpl namespaceDecls;

        public ContentHandlerFilter(ContentHandler original, AttributesImpl nsDecls) {
            this.original = original;
            this.namespaceDecls = nsDecls;
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            original.characters(ch, start, length);
        }

        public void endDocument() throws SAXException {
            original.endDocument();
        }

        public void endElement(String namespaceURI, String localName, String qName)
                throws SAXException {
            original.endElement(namespaceURI, localName, qName);
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            original.endPrefixMapping(prefix);
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            original.ignorableWhitespace(ch, start, length);
        }

        public void processingInstruction(String target, String data) throws SAXException {
            original.processingInstruction(target, data);
        }

        public void setDocumentLocator(org.xml.sax.Locator locator) {
            original.setDocumentLocator(locator);
        }

        public void skippedEntity(String name) throws SAXException {
            original.skippedEntity(name);
        }

        public void startDocument() throws SAXException {
            original.startDocument();
        }

        public void startElement(
                String namespaceURI, String localName, String qName, Attributes atts)
                throws SAXException {
            if (namespaceDecls != null) {
                for (int i = 0, ii = atts.getLength(); i < ii; i++) {
                    namespaceDecls.addAttribute(
                            null, null, atts.getQName(i), atts.getType(i), atts.getValue(i));
                }

                atts = namespaceDecls;
                namespaceDecls = null;
            }
            if (namespaceURI == null) namespaceURI = "";
            if (localName == null) localName = "";
            original.startElement(namespaceURI, localName, qName, atts);
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            original.startPrefixMapping(prefix, uri);
        }

        public void comment(char[] ch, int start, int length) throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).comment(ch, start, length);
            }
        }

        public void startCDATA() throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).startCDATA();
            }
        }

        public void endCDATA() throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).endCDATA();
            }
        }

        public void startDTD(String name, String publicId, String systemId) throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).startDTD(name, publicId, systemId);
            }
        }

        public void endDTD() throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).endDTD();
            }
        }

        public void startEntity(String name) throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).startEntity(name);
            }
        }

        public void endEntity(String name) throws SAXException {
            if (original instanceof LexicalHandler) {
                ((LexicalHandler) original).endEntity(name);
            }
        }
    }

    /** Support for writing Translators. */
    protected abstract static class TranslatorSupport implements Translator {
        protected final ContentHandler contentHandler;
        private String prefix;
        private String namespace;
        protected final Attributes NULL_ATTS = new AttributesImpl();
        protected NamespaceSupport nsSupport = new NamespaceSupport();
        protected SchemaLocationSupport schemaLocation;

        /**
         * The queue of write operations pending for this translator. This should be empty if no
         * mark is set.
         */
        private List<Action> pending = new ArrayList<Action>();

        /**
         * An Action records a call to one of the SAX-event-generating methods on this translator.
         */
        private interface Action {
            void commit();
        }

        /** The Start class implements an Action corresponding to starting an XML element */
        private class Start implements Action {
            private final String element;
            private final Attributes attributes;

            public Start(String element, Attributes attributes) {
                this.element = element;
                this.attributes = new AttributesImpl(attributes);
            }

            public void commit() {
                _start(element, attributes);
            }

            public String toString() {
                return "Start(" + element + ", " + attributes + ")";
            }
        }

        /**
         * The Chars class implements an Action corresponding to writing a text block in the XML
         * output
         */
        private class Chars implements Action {
            private final String text;

            public Chars(String text) {
                this.text = text;
            }

            public void commit() {
                _chars(text);
            }

            public String toString() {
                return "Chars(" + text + ")";
            }
        }

        /**
         * The CData class implements an Action corresponding to writing a CDATA block in the XML
         * output
         */
        private class CData implements Action {
            private final String text;

            public CData(String text) {
                this.text = text;
            }

            public void commit() {
                _cdata(text);
            }

            public String toString() {
                return "CData(" + text + ")";
            }
        }

        /**
         * The Comment class implements an Action corresponding to writing a comment block in the
         * XML output
         */
        private class Comment implements Action {
            private final String text;

            public Comment(String text) {
                this.text = text;
            }

            public void commit() {
                _comment(text);
            }

            public String toString() {
                return "Comment(" + text + ")";
            }
        }

        /** The End class implements an Action corresponding to closing an XML element */
        private class End implements Action {
            private final String element;

            public End(String element) {
                this.element = element;
            }

            public void commit() {
                _end(element);
            }

            public String toString() {
                return "End(" + element + ")";
            }
        }

        /** The Backend class encapsulates a strategy for writing back to the underlying stream. */
        private interface Backend {
            public void start(String element, Attributes attributes);

            public void chars(String text);

            public void cdata(String text);

            public void comment(String text);

            public void end(String element);
        }

        /**
         * The BufferedBackend queues up write operations in memory, to be committed at a later
         * time.
         */
        private class BufferedBackend implements Backend {
            public void start(String element, Attributes attributes) {
                if (element == null) {
                    throw new NullPointerException("Attempted to start XML tag with null element");
                }
                if (attributes == null) {
                    throw new NullPointerException(
                            "Attempted to start XML tag with null attributes");
                }
                pending.add(new Start(element, attributes));
            }

            public void chars(String text) {
                if (text == null) {
                    throw new NullPointerException("Attempted to start text block with null text");
                }
                pending.add(new Chars(text));
            }

            public void cdata(String text) {
                if (text == null) {
                    throw new NullPointerException("Attempted to start CDATA block with null text");
                }
                pending.add(new CData(text));
            }

            public void end(String element) {
                if (element == null) {
                    throw new NullPointerException("Attempted to close tag with null element");
                }
                pending.add(new End(element));
            }

            public void comment(String text) {
                if (text == null) {
                    throw new NullPointerException("Attempted to add comment with null text");
                }
                pending.add(new Comment(text));
            }
        }

        /** The DirectBackend writes immediately to the underlying output stream. */
        private class DirectBackend implements Backend {
            public void start(String element, Attributes attributes) {
                _start(element, attributes);
            }

            public void chars(String text) {
                _chars(text);
            }

            public void cdata(String text) {
                _cdata(text);
            }

            public void end(String element) {
                _end(element);
            }

            public void comment(String text) {
                _comment(text);
            }
        }

        /** A singleton instance of the DirectBackend for this TranslatorSupport instance */
        private final Backend directBackend = new DirectBackend();

        /** A singleton instance of the BufferedBackend for this TranslatorSupport instance */
        private final Backend bufferedBackend = new BufferedBackend();

        /**
         * The backend currently in use. This should only be modified in the mark/reset/commit
         * methods!
         */
        private Backend backend = directBackend;

        /**
         * Subclasses should check this flag in case an abort message was sent and stop any internal
         * iteration if false.
         */
        protected volatile boolean running = true;

        public TranslatorSupport(ContentHandler contentHandler, String prefix, String nsURI) {
            this.contentHandler = contentHandler;
            this.prefix = prefix;
            this.namespace = nsURI;
            if (prefix != null && nsURI != null) nsSupport.declarePrefix(prefix, nsURI);
        }

        public TranslatorSupport(
                ContentHandler contentHandler,
                String prefix,
                String nsURI,
                SchemaLocationSupport schemaLocation) {
            this(contentHandler, prefix, nsURI);
            this.schemaLocation = schemaLocation;
        }

        public void abort() {
            running = false;
        }

        /**
         * Set a mark() to which we can later "roll back" writes. After a call to mark(), the
         * Translator stores pending write operations in memory until commit() is called. The
         * pending writes can be discarded with the reset() method.
         *
         * <p>Typically, one would use marks in conjunction with an exception handler:
         *
         * <pre>
         *   void encodeFoo(Foo f) {
         *     try {
         *       mark();
         *       element(foo.riskyMethod());
         *       element(foo.dangerousMethod());
         *       commit();
         *     } catch (BadThingHappened disaster) {
         *         mitigate(disaster);
         *         reset();
         *     }
         *   }
         * </pre>
         *
         * @throws IllegalStateException if a mark is already set
         */
        protected void mark() {
            if (backend == bufferedBackend) throw new IllegalStateException("Mark already set");
            backend = bufferedBackend;
        }

        /**
         * Discard pending write operations after a mark() has been set.
         *
         * <p>This method is safe to call even if no mark is set - so it returns to a "known good"
         * state as far as marks are concerned.
         *
         * @see #mark()
         */
        protected void reset() {
            pending.clear();
            backend = directBackend;
        }

        /**
         * Commit pending write operations. After setting a mark, this method will commit the
         * pending writes.
         *
         * @see #mark()
         * @throws IllegalStateException if no mark is set
         */
        protected void commit() {
            if (backend != bufferedBackend)
                throw new IllegalStateException("Can't commit without a mark");
            for (Action a : pending) {
                try {
                    a.commit();
                } catch (Exception e) {
                    String message =
                            "Error while committing XML elements; specific element was: " + a;
                    throw new RuntimeException(message, e);
                }
            }
            pending.clear();
            backend = directBackend;
        }

        /**
         * Utility method to copy namespace declarations from "sub" translators into this ns
         * support...
         */
        protected void addNamespaceDeclarations(TranslatorSupport trans) {
            NamespaceSupport additional = trans.getNamespaceSupport();
            java.util.Enumeration declared = additional.getDeclaredPrefixes();
            while (declared.hasMoreElements()) {
                String prefix1 = declared.nextElement().toString();
                nsSupport.declarePrefix(prefix1, additional.getURI(prefix1));
            }
        }

        /**
         * Utility method for creating attributes from an array of name value pairs.
         *
         * <p>The <tt>nameValuePairs</tt> array should be of the form:
         *
         * <pre>{name1,value1,name2,value2,...,nameN,valueN}</pre>
         *
         * @param nameValuePairs The attribute names/values.
         */
        protected AttributesImpl createAttributes(String[] nameValuePairs) {
            AttributesImpl attributes = new AttributesImpl();

            for (int i = 0; i < nameValuePairs.length; i += 2) {
                String name = nameValuePairs[i];
                String value = nameValuePairs[i + 1];

                attributes.addAttribute("", name, name, "", value);
            }

            return attributes;
        }

        protected void element(String element, String content) {
            element(element, content, NULL_ATTS);
        }

        /** Will only issue the provided element if content is non empty */
        protected void elementSafe(String element, String content) {
            if (content != null && content.length() != 0) {
                element(element, content, NULL_ATTS);
            }
        }

        protected void element(String element, String content, Attributes atts) {
            start(element, atts);

            if (content != null) {
                chars(content);
            }

            end(element);
        }

        protected void start(String element) {
            start(element, NULL_ATTS);
        }

        protected void start(String element, Attributes atts) {
            backend.start(element, atts);
        }

        private void _start(String element, Attributes atts) {
            try {
                String el = (prefix == null) ? element : (prefix + ":" + element);
                contentHandler.startElement("", "", el, atts);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        protected void chars(String text) {
            backend.chars(text);
        }

        private void _chars(String text) {
            try {
                char[] ch = text.toCharArray();
                contentHandler.characters(ch, 0, ch.length);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        protected void end(String element) {
            backend.end(element);
        }

        private void _end(String element) {
            try {
                String el = (prefix == null) ? element : (prefix + ":" + element);
                contentHandler.endElement("", "", el);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        protected void cdata(String cdata) {
            backend.cdata(cdata);
        }

        private void _cdata(String cdata) {
            if (contentHandler instanceof LexicalHandler) {
                LexicalHandler lexicalHandler = (LexicalHandler) contentHandler;
                try {
                    lexicalHandler.startCDATA();
                    char[] carray = cdata.toCharArray();
                    contentHandler.characters(carray, 0, carray.length);
                    lexicalHandler.endCDATA();
                } catch (SAXException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        protected void comment(String comment) {
            backend.comment(comment);
        }

        private void _comment(String comment) {
            if (contentHandler instanceof LexicalHandler) {
                LexicalHandler lexicalHandler = (LexicalHandler) contentHandler;
                try {
                    char[] carray = comment.toCharArray();
                    lexicalHandler.comment(carray, 0, carray.length);
                } catch (SAXException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public String getDefaultNamespace() {
            return namespace;
        }

        public String getDefaultPrefix() {
            return prefix;
        }

        public NamespaceSupport getNamespaceSupport() {
            return nsSupport;
        }

        public SchemaLocationSupport getSchemaLocationSupport() {
            return schemaLocation;
        }
    }

    /**
     * Adds support for schemaLocations.
     *
     * @task REVISIT: consider combining this with NamespaceSupport, as it would make sense to just
     *     add a location to your nsURI. Or at least tie them more closely, so that you can only add
     *     a SchemaLocation if the namespace actually exists.
     */
    public static class SchemaLocationSupport {
        private Map locations = new HashMap();

        public void setLocation(String nsURI, String uri) {
            locations.put(nsURI, uri);
        }

        public String getSchemaLocation() {
            return getSchemaLocation(locations.keySet());
        }

        public String getSchemaLocation(String nsURI) {
            String uri = (String) locations.get(nsURI);

            if (uri == null) {
                return "";
            }
            return nsURI + " " + uri;
        }

        public String getSchemaLocation(Set namespaces) {
            StringBuffer location = new StringBuffer();

            for (Iterator it = namespaces.iterator(); it.hasNext(); ) {
                location.append(getSchemaLocation((String) it.next()));

                if (it.hasNext()) {
                    location.append(" "); // \n?  Pretty printing?
                }
            }

            return location.toString();
        }
    }

    /** Support for the setup of an XMLReader for use in a transformation. */
    protected static class XMLReaderSupport extends XMLFilterImpl {
        TransformerBase base;
        Object object;
        Translator translator;

        public XMLReaderSupport(TransformerBase transfomerBase, Object object) {
            this.base = transfomerBase;
            this.object = object;
        }

        public final Translator getTranslator() {
            return translator;
        }

        public void parse(InputSource in) throws SAXException {
            ContentHandler handler = getContentHandler();

            if (base.isNamespaceDeclartionEnabled()) {
                AttributesImpl atts = new AttributesImpl();
                ContentHandlerFilter filter = new ContentHandlerFilter(handler, atts);
                translator = base.createTranslator(filter);

                if (translator.getDefaultNamespace() != null) {
                    // declare the default mapping
                    atts.addAttribute(
                            XMLNS_NAMESPACE,
                            null,
                            "xmlns",
                            "CDATA",
                            translator.getDefaultNamespace());

                    // if prefix non-null, declare the mapping
                    if (translator.getDefaultPrefix() != null) {
                        atts.addAttribute(
                                XMLNS_NAMESPACE,
                                null,
                                "xmlns:" + translator.getDefaultPrefix(),
                                "CDATA",
                                translator.getDefaultNamespace());
                    }
                }

                NamespaceSupport ns = translator.getNamespaceSupport();
                java.util.Enumeration e = ns.getPrefixes();

                // TODO: only add schema locations for namespaces that are
                // actually here, or some sort of better checking.
                // Set namespaces = new HashSet();
                while (e.hasMoreElements()) {
                    String prefix = e.nextElement().toString();

                    if (prefix.equals("xml")) {
                        continue;
                    }

                    String xmlns = "xmlns:" + prefix;

                    if (atts.getValue(xmlns) == null) {
                        atts.addAttribute(XMLNS_NAMESPACE, null, xmlns, "CDATA", ns.getURI(prefix));

                        // namespaces.add(ns.getURI(prefix));
                    }
                }

                String defaultNS = ns.getURI("");

                if ((defaultNS != null) && (atts.getValue("xmlns:") == null)) {
                    atts.addAttribute(XMLNS_NAMESPACE, null, "xmlns:", "CDATA", defaultNS);

                    // namespaces.add(defaultNS);
                }

                SchemaLocationSupport schemaLocSup = translator.getSchemaLocationSupport();

                if ((schemaLocSup != null) && !schemaLocSup.getSchemaLocation().equals("")) {
                    atts.addAttribute(
                            XMLNS_NAMESPACE,
                            null,
                            "xmlns:xsi",
                            "CDATA",
                            "http://www.w3.org/2001/XMLSchema-instance");
                    atts.addAttribute(
                            null,
                            null,
                            "xsi:schemaLocation",
                            null,
                            schemaLocSup.getSchemaLocation());
                }
            } else {
                translator = base.createTranslator(handler);
            }

            handler.startDocument();
            translator.encode(object);
            handler.endDocument();
        }
    }
}
