/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * XML related utilities not otherwise found in base libraries
 *
 * @author Andrea Aime - GeoSolutions
 */
public class XMLUtils {

    /**
     * Create a new SAXParserFactory that respects library configuration.
     *
     * @param hints Factory hints
     * @return SAX Parser Factory
     */
    public static SAXParserFactory newSAXParserFactory(final Hints hints) {
        // Factory applying GeoTools configuration to SAXParserFactory
        return new GTSAXParserFactory(hints);
    }

    /**
     * Create a new TransformerFactory that respects library configuration.
     *
     * @return Transformer Factory
     */
    public static TransformerFactory newTransformerFactory() {
        return newTransformerFactory(GeoTools.getDefaultHints());
    }

    /**
     * Create a new TransformerFactory that respects library configuration.
     *
     * @param hints Factory hints
     * @return Transformer Factory
     */
    public static TransformerFactory newTransformerFactory(final Hints hints) {
        return new GTTransformerFactory(hints);
    }

    public static SAXTransformerFactory newSaxTransformerFactory() {
        return newSaxTransformerFactory(GeoTools.getDefaultHints());
    }

    public static SAXTransformerFactory newSaxTransformerFactory(final Hints hints) {
        return new GTSAXTransformerFactory(hints);
    }

    /** Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}. */
    public static Transformer newTransformer() throws TransformerConfigurationException {
        return newTransformer(GeoTools.getDefaultHints());
    }

    /** Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}. */
    public static Transformer newTransformer(Hints hints) throws TransformerConfigurationException {
        return newTransformerFactory(hints).newTransformer(); // NOPMD AvoidTransform
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param factory
     */
    public static Transformer newTransformer(TransformerFactory factory) throws TransformerConfigurationException {
        if (factory == null) {
            throw new NullPointerException("TransformerFactory required");
        }
        return newTransformer(factory, GeoTools.getDefaultHints());
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param factory Factory pre-configured with information such as indenting
     * @param hints Factory configuration
     * @return XML transformer
     */
    public static Transformer newTransformer(TransformerFactory factory, Hints hints)
            throws TransformerConfigurationException {
        if (factory == null) {
            throw new NullPointerException("TransformerFactory required");
        }
        return toTransformerFactory(factory, hints).newTransformer(); // NOPMD AvoidTransform
    }

    /**
     * Cast to GTTransformerFactory or GTSAXTransformFactory respecting GeoTools configuration.
     *
     * @param factory Transformer factory, or {@code null}
     * @param hints Factory configuration
     * @return GTTransformerFactory or GTSAXTransformFactory respecting GeoTools configuration.
     */
    protected static TransformerFactory toTransformerFactory(TransformerFactory factory, Hints hints) {
        if (factory == null) {
            return new GTTransformerFactory(hints);
        } else if (factory instanceof GTSAXTransformerFactory saxFactory) {
            if (saxFactory.hints == hints) {
                return factory;
            } else {
                return new GTSAXTransformerFactory(saxFactory, hints);
            }
        } else if (factory instanceof SAXTransformerFactory saxFactory) {
            return new GTSAXTransformerFactory(saxFactory, hints);
        }
        return new GTTransformerFactory(factory, hints);
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param source Source input
     * @return XML transformer
     */
    public static Transformer newTransformer(Source source) throws TransformerConfigurationException {
        return newTransformerFactory().newTransformer(source);
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param source Source input
     * @param hints Factory configuration
     * @return XML transformer
     */
    public static Transformer newTransformer(Source source, Hints hints) throws TransformerConfigurationException {
        return newTransformerFactory(hints).newTransformer(source);
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param source Source input
     * @param factory Factory pre-configured with information such as indenting
     * @return XML transformer
     */
    public static Transformer newTransformer(TransformerFactory factory, Source source)
            throws TransformerConfigurationException {
        return toTransformerFactory(factory, GeoTools.getDefaultHints()).newTransformer(source);
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param source Source input
     * @param factory Factory pre-configured with information such as indenting
     * @param hints Factory configuration
     * @return XML transformer
     */
    public static Transformer newTransformer(TransformerFactory factory, Source source, Hints hints)
            throws TransformerConfigurationException {
        return toTransformerFactory(factory, hints).newTransformer(source);
    }

    /**
     * Alternative to direct use of {@link SAXSource#sourceToInputSource}, allowing GeoTools library configuration to be
     * applied.
     *
     * @param source
     * @return SAXSource configured with GeoTools configuration including {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static SAXSource sourceToInputSource(Source source) {
        return sourceToInputSource(source, GeoTools.getDefaultHints());
    }

    /**
     * Alternative to direct use of {@link SAXSource#sourceToInputSource}, allowing GeoTools library configuration to be
     * applied.
     *
     * @param source transform source
     * @param hints GeoTools library configuration
     * @return SAXSource configured with GeoTools configuration including {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static SAXSource sourceToInputSource(Source source, Hints hints) {
        return sourceToInputSource(SAXSource.sourceToInputSource(source), hints);
    }

    /**
     * Alternative to direct use of InputSource allowing SAXSource setup with GeoTools configuration to be applied.
     *
     * @param source sax input source
     * @param hints GeoTools library configuration
     * @return SAXSource configured with GeoTools configuration including {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static SAXSource sourceToInputSource(InputSource source, Hints hints) {
        if (source == null) return null;

        SAXParserFactory factory = newSAXParserFactory(hints);
        try {
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setEntityResolver(GeoTools.getEntityResolver(hints));
            return new SAXSource(reader, source);
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new document builder, as an alternative to {@link DocumentBuilder#newDocument()}, allowing GeoTools
     * library configuration to be applied.
     *
     * <p>The document builder is configured using GeoTools configuration including
     * {@link GeoTools#getEntityResolver(Hints)} .
     *
     * @return DocumentBuilder configured with Ge
     */
    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        return newDocumentBuilder(GeoTools.getDefaultHints());
    }

    /**
     * Creates a new document builder, as an alternative to direct use of {@link DocumentBuilder#newDocument()},
     * allowing GeoTools library configuration to be applied.
     *
     * <p>The document builder is configured using GeoTools configuration including
     * {@link GeoTools#getEntityResolver(Hints)} .
     *
     * @return DocumentBuilder configured using GeoTools Hints
     */
    public static DocumentBuilder newDocumentBuilder(Hints hints) throws ParserConfigurationException {
        return newDocumentBuilder(newDocumentBuilderFactory(hints), hints);
    }

    /**
     * Create a new DocumentBuilder using a supplied Factory, allowing GeoTools library configuration to be applied.
     *
     * <p>This apporach allows document build factory to be configured to be namespace aware, validating, etc. while
     * still applying GeoTools configuration including {@link GeoTools#getEntityResolver(Hints)}.
     *
     * <pre>{@literal
     * DocumentBuilderFactory factory = XMLUtils.newDocumentBuilderFactory();
     * factory.setValidating(true);
     * factory.setNamespaceAware(true);
     * DocumentBuilder builder = XMLUtils.newDocumentBuilder(factory);
     * Document document = builder.parse(source);}</pre>
     *
     * @param factory Configured DocumentBuildFactory
     */
    public static DocumentBuilder newDocumentBuilder(DocumentBuilderFactory factory)
            throws ParserConfigurationException {
        return newDocumentBuilder(factory, GeoTools.getDefaultHints());
    }
    /**
     * Create a new DocumentBuilder using a supplied Factory, allowing GeoTools library configuration to be applied.
     *
     * <p>This approach allows document build factory to be configured to be namespace aware, validating, etc. while
     * still applying GeoTools configuration including {@link GeoTools#getEntityResolver(Hints)}.
     *
     * <pre>{@literal
     * Hints hints = GeoTools.getDefaultHints();
     * hints.put(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);
     *
     * DocumentBuilderFactory factory = XMLUtils.newDocumentBuilderFactory(hints);
     * factory.setValidating(true);
     * factory.setNamespaceAware(true);
     * DocumentBuilder builder = XMLUtils.newDocumentBuilder(factory, hints);
     * Document document = builder.parse(source);}</pre>
     *
     * The above example shows use of this method with NullEntityResolver.
     *
     * @param factory Configured DocumentBuildFactory
     * @param hints Factory hints
     */
    public static DocumentBuilder newDocumentBuilder(DocumentBuilderFactory factory, Hints hints)
            throws ParserConfigurationException {

        DocumentBuilder builder = factory.newDocumentBuilder(); // NOPMD AvoidDocumentBuilder
        builder.setEntityResolver(GeoTools.getEntityResolver(hints));
        return builder;
    }

    /** Create a new DocumentBuilderFactory allowing GeoTools library configuration to be applied. */
    public static DocumentBuilderFactory newDocumentBuilderFactory() {
        return newDocumentBuilderFactory(GeoTools.getDefaultHints());
    }

    /**
     * Create a new DocumentBuilder using a supplied Factory, allowing GeoTools library configuration to be applied.
     *
     * @param hints Factory hints
     */
    public static DocumentBuilderFactory newDocumentBuilderFactory(Hints hints) {
        return new GTDocumentBuilderFactory(hints);
    }

    /**
     * Tests whether the TransformerFactory and SchemaFactory implementations support JAXP 1.5 properties to protect
     * against XML external entity injection (XXE) attacks. The internal JDK XML processors starting with JDK 7u40 would
     * support these properties but outdated versions of XML libraries (e.g., Xalan, Xerces) that do not support these
     * properties may be included in GeoServer's classpath or provided by the web application server. This method is
     * intended to support using third-party libraries (e.g., Hazelcast) that use these properties internally.
     *
     * @throws IllegalStateException if the JAXP 1.5 properties are not supported or if there was an error checking for
     *     JAXP 1.5 support
     */
    public static void checkSupportForJAXP15Properties() {
        List<String> classes = new ArrayList<>();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            try {
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            } catch (IllegalArgumentException e) {
                classes.add(transformerFactory.getClass().getName());
            }
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            } catch (SAXException e) {
                classes.add(schemaFactory.getClass().getName());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to check support for JAXP 1.5 properties", e);
        }
        if (!classes.isEmpty()) {
            throw new IllegalStateException("JAXP 1.5 properties are not supported by: " + String.join(", ", classes));
        }
    }

    /**
     * Checks the string for XML invalid chars, and in case any is found, create a copy with the invalid ones removed.
     */
    public static String removeXMLInvalidChars(String in) {
        // sanity check
        if (in == null || "".equals(in)) {
            return in;
        }

        // verify if valid
        final int lenght = in.length();
        int invalid = 0;
        for (int i = 0; i < lenght; i++) {
            final char current = in.charAt(i);
            if (!isXMLValidChar(current)) {
                invalid++;
            }
        }

        if (invalid > 0) {
            StringBuilder out = new StringBuilder(in.length() - invalid);
            for (int i = 0; i < lenght; i++) {
                final char current = in.charAt(i);
                if (isXMLValidChar(current)) {
                    out.append(current);
                }
            }
            in = out.toString();
        }

        return in;
    }

    /**
     * Creates a qualified name from a string by parsing out the colon as the prefix / local separator.
     *
     * @param name The possibly qualified name.
     * @param namespaces The namespace prefix uri mappings.
     */
    public static QName qName(String name, NamespaceSupport namespaces) {
        int dot = name.indexOf(':');
        if (dot > -1) {
            String[] split = name.split(":");
            String prefix = split[0];
            String local = split[1];

            return new QName(namespaces.getURI(prefix), local, prefix);
        }

        return new QName(name);
    }

    /** Returns true if the character provided is valid according to XML 1.0 */
    private static boolean isXMLValidChar(char c) {
        return c == 0x9 || c == 0xA || c == 0xD || c >= 0x20 && c <= 0xD7FF || c >= 0xE000 && c <= 0xFFFD;
        // removed as a char cannot get this high
        // || ((c >= 0x10000) && (c <= 0x10FFFF));
    }

    /** Wrapper ensuring system SAXParserFactory configured with {@code GeoTools.getEntityResolver(hints)}. */
    private static class GTSAXParserFactory extends SAXParserFactory {
        private final SAXParserFactory factory;
        private final Hints hints;

        public GTSAXParserFactory(Hints hints) {
            this.hints = hints;
            this.factory = SAXParserFactory.newInstance();
            try {
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            } catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e) {
                // feature secure processing recommended, but not supported
            }
        }

        @Override
        public void setNamespaceAware(boolean awareness) {
            factory.setNamespaceAware(awareness);
        }

        @Override
        public void setValidating(boolean validating) {
            factory.setValidating(validating);
        }

        @Override
        public void setXIncludeAware(boolean state) {
            factory.setXIncludeAware(state);
        }

        @Override
        public void setSchema(Schema schema) {
            factory.setSchema(schema);
        }

        @Override
        public void setFeature(String name, boolean value)
                throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
            factory.setFeature(name, value);
        }

        @Override
        public boolean getFeature(String name)
                throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
            return factory.getFeature(name);
        }

        @Override
        public Schema getSchema() {
            return factory.getSchema();
        }

        @Override
        public boolean isNamespaceAware() {
            return factory.isNamespaceAware();
        }

        @Override
        public boolean isValidating() {
            return factory.isValidating();
        }

        @Override
        public boolean isXIncludeAware() {
            return factory.isXIncludeAware();
        }

        @Override
        public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
            SAXParser parser = factory.newSAXParser();
            parser.getXMLReader().setEntityResolver(GeoTools.getEntityResolver(hints));
            return parser;
        }

        @Override
        public String toString() {
            return "GTSAXParserFactory {" + factory + "}";
        }
    }

    /** Wrapper ensuring DocumentBuilderFactory configured with {@code GeoTools.getEntityResolver(hints)}. */
    private static class GTDocumentBuilderFactory extends DocumentBuilderFactory {
        final Hints hints;
        private final DocumentBuilderFactory factory;

        public GTDocumentBuilderFactory(Hints hints) {
            this.hints = hints != null ? hints : GeoTools.getDefaultHints();
            this.factory = DocumentBuilderFactory.newInstance(); // NOPMD AvoidDocumentBuilderFactory
            try {
                this.factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            } catch (ParserConfigurationException e) {
                // feature secure processing recommended, but not supported
            }
        }

        @Override
        public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
            DocumentBuilder builder = factory.newDocumentBuilder(); // NOPMD AvoidDocumentBuilder
            builder.setEntityResolver(GeoTools.getEntityResolver(hints));

            return builder;
        }

        @Override
        public boolean isValidating() {
            return this.factory.isValidating();
        }

        @Override
        public void setValidating(boolean validating) {
            this.factory.setValidating(validating);
        }

        @Override
        public boolean isNamespaceAware() {
            return this.factory.isNamespaceAware();
        }

        @Override
        public void setNamespaceAware(boolean namespaceAware) {
            this.factory.setNamespaceAware(namespaceAware);
        }

        @Override
        public void setAttribute(String name, Object value) throws IllegalArgumentException {
            this.factory.setAttribute(name, value);
        }

        @Override
        public Object getAttribute(String name) throws IllegalArgumentException {
            return this.factory.getAttribute(name);
        }

        @Override
        public void setFeature(String name, boolean value) throws ParserConfigurationException {
            this.factory.setFeature(name, value);
        }

        @Override
        public boolean getFeature(String name) throws ParserConfigurationException {
            return this.factory.getFeature(name);
        }

        @Override
        public String toString() {
            return "GTDocumentBuilderFactory {" + factory + "}";
        }
    }

    /** Wrapper ensuring TransformerFactory configured with {@code GeoTools.getEntityResolver(hints)}. */
    private static class GTTransformerFactory extends TransformerFactory {
        protected final Hints hints;
        protected final TransformerFactory factory;

        public GTTransformerFactory(Hints hints) {
            this(TransformerFactory.newInstance(), hints);
        }

        public GTTransformerFactory(TransformerFactory factory, Hints hints) {
            this.hints = hints != null ? hints : GeoTools.getDefaultHints();
            this.factory = factory;
        }

        @Override
        public Transformer newTransformer(Source source) throws TransformerConfigurationException {
            return config(factory.newTransformer(source));
        }

        @Override
        public Transformer newTransformer() throws TransformerConfigurationException {
            return config(factory.newTransformer()); // NOPMD AvoidTransform
        }

        /**
         * Apply GeoTools configuration to the transformer, including {@code GeoTools.getEntityResolver(hints)}.
         *
         * @param transformer XML Transformer
         * @return transformer
         */
        protected Transformer config(Transformer transformer) {
            URIResolver uriResolver = transformer.getURIResolver();
            if (uriResolver != null && !(uriResolver instanceof GTURIResolver)) {
                final EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
                transformer.setURIResolver(new GTURIResolver(entityResolver, uriResolver));
            } else {
                transformer.setURIResolver(new GTURIResolver(GeoTools.getEntityResolver(hints)));
            }
            return transformer;
        }

        @Override
        public Templates newTemplates(Source source) throws TransformerConfigurationException {
            final Templates templates = factory.newTemplates(source);
            return new GTTemplates(templates, hints);
        }

        @Override
        public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
                throws TransformerConfigurationException {
            return factory.getAssociatedStylesheet(source, media, title, charset);
        }

        @Override
        public void setURIResolver(URIResolver resolver) {
            this.factory.setURIResolver(resolver);
        }

        @Override
        public URIResolver getURIResolver() {
            return factory.getURIResolver();
        }

        @Override
        public void setFeature(String name, boolean value) throws TransformerConfigurationException {
            this.factory.setFeature(name, value);
        }

        @Override
        public boolean getFeature(String name) {
            return this.factory.getFeature(name);
        }

        @Override
        public void setAttribute(String name, Object value) {
            this.factory.setAttribute(name, value);
        }

        @Override
        public Object getAttribute(String name) {
            return this.factory.getAttribute(name);
        }

        @Override
        public void setErrorListener(ErrorListener listener) {
            this.factory.setErrorListener(listener);
        }

        @Override
        public ErrorListener getErrorListener() {
            return this.factory.getErrorListener();
        }

        @Override
        public String toString() {
            return "GTTransformerFactory {" + factory + "}";
        }
    }

    /** Templates that delegates to EntityResolver. */
    private static class GTTemplates implements Templates {
        private final Templates templates;
        private final Hints hints;

        public GTTemplates(Templates templates, Hints hints) {
            this.templates = templates;
            this.hints = hints;
        }

        @Override
        public Transformer newTransformer() throws TransformerConfigurationException {
            Transformer transformer = templates.newTransformer();
            URIResolver uriResolver = transformer.getURIResolver();
            if (uriResolver != null && !(uriResolver instanceof GTURIResolver)) {
                final EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
                transformer.setURIResolver(new GTURIResolver(entityResolver, uriResolver));
            } else {
                transformer.setURIResolver(new GTURIResolver(GeoTools.getEntityResolver(hints)));
            }
            return transformer;
        }

        @Override
        public Properties getOutputProperties() {
            return templates.getOutputProperties();
        }

        @Override
        public String toString() {
            return "GTTemplates {" + templates + "}";
        }
    }

    /** Wrapper ensuring SAXTransformerFactory configured with {@code GeoTools.getEntityResolver(hints)}. */
    private static class GTSAXTransformerFactory extends SAXTransformerFactory {
        protected final Hints hints;
        protected final SAXTransformerFactory factory;

        public GTSAXTransformerFactory(Hints hints) {
            this((SAXTransformerFactory) SAXTransformerFactory.newInstance(), hints);
        }

        public GTSAXTransformerFactory(SAXTransformerFactory factory, Hints hints) {
            this.factory = factory;
            this.hints = hints != null ? hints : GeoTools.getDefaultHints();
        }
        // apply configuration
        /**
         * Apply GeoTools configuration to the transformer, including {@code GeoTools.getEntityResolver(hints)}.
         *
         * @param transformer XML Transformer
         * @return transformer
         */
        protected Transformer config(Transformer transformer) {
            URIResolver uriResolver = transformer.getURIResolver();
            if (uriResolver != null && !(uriResolver instanceof GTURIResolver)) {
                final EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
                transformer.setURIResolver(new GTURIResolver(entityResolver, uriResolver));
            } else {
                transformer.setURIResolver(new GTURIResolver(GeoTools.getEntityResolver(hints)));
            }
            return transformer;
        }

        /**
         * Apply GeoTools configuration to TransformHandler.
         *
         * @param handler Transform handler
         * @return Transform handler
         */
        protected TransformerHandler config(TransformerHandler handler) {
            config(handler.getTransformer());
            return handler;
        }

        /**
         * Apply GeoTools configuration to XMLFilter.
         *
         * @param filter XML Fitler
         * @return XML Filter
         */
        protected XMLFilter config(XMLFilter filter) {
            filter.setEntityResolver(GeoTools.getEntityResolver(hints));
            return filter;
        }

        // overrides

        @Override
        public Transformer newTransformer(Source source) throws TransformerConfigurationException {
            return config(factory.newTransformer(source));
        }

        @Override
        public Transformer newTransformer() throws TransformerConfigurationException {
            return config(factory.newTransformer()); // NOPMD AvoidTransform
        }

        @Override
        public Templates newTemplates(Source source) throws TransformerConfigurationException {
            final Templates templates = factory.newTemplates(source);
            return new GTTemplates(templates, hints);
        }

        @Override
        public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
                throws TransformerConfigurationException {
            return factory.getAssociatedStylesheet(source, media, title, charset);
        }

        @Override
        public void setURIResolver(URIResolver resolver) {
            this.factory.setURIResolver(resolver);
        }

        @Override
        public URIResolver getURIResolver() {
            return factory.getURIResolver();
        }

        @Override
        public void setFeature(String name, boolean value) throws TransformerConfigurationException {
            this.factory.setFeature(name, value);
        }

        @Override
        public boolean getFeature(String name) {
            return this.factory.getFeature(name);
        }

        @Override
        public void setAttribute(String name, Object value) {
            this.factory.setAttribute(name, value);
        }

        @Override
        public Object getAttribute(String name) {
            return this.factory.getAttribute(name);
        }

        @Override
        public void setErrorListener(ErrorListener listener) {
            this.factory.setErrorListener(listener);
        }

        @Override
        public ErrorListener getErrorListener() {
            return this.factory.getErrorListener();
        }

        @Override
        public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
            return config(factory.newTransformerHandler(src));
        }

        @Override
        public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
            return config(factory.newTransformerHandler(templates));
        }

        @Override
        public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
            return config(factory.newTransformerHandler());
        }

        @Override
        public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
            return factory.newTemplatesHandler();
        }

        @Override
        public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
            return config(factory.newXMLFilter(src));
        }

        @Override
        public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
            return config(factory.newXMLFilter(templates));
        }

        @Override
        public String toString() {
            return "GTSAXTransformerFactory {" + factory + "}";
        }
    }

    /** URIResolver that delegates to EntityResolver. */
    private static class GTURIResolver implements URIResolver {
        private final EntityResolver entityResolver;
        private final URIResolver uriResolver;

        public GTURIResolver(EntityResolver entityResolver) {
            this(entityResolver, null);
        }

        public GTURIResolver(EntityResolver entityResolver, URIResolver uriResolver) {
            this.entityResolver = entityResolver;
            this.uriResolver = uriResolver;
        }

        @Override
        public Source resolve(String href, String base) throws TransformerException {
            try {
                // step 1: check with entity resolver, will return null (external), source (internal), or exception
                // (forbidden)
                entityResolver.resolveEntity(href, base);
                // step 2: check with uri resolver
                if (uriResolver != null) {
                    return uriResolver.resolve(href, base);
                } else {
                    // step 3: allow processor to handle URI
                    return null;
                }
            } catch (SAXParseException e) {
                TransformerException transformerException = new TransformerException(e);
                SourceLocator sourceLocator = new SaxParseExceptionSourceLocator(e);
                transformerException.setLocator(sourceLocator);
                throw transformerException;
            } catch (SAXException | IOException e) {
                throw new TransformerException(e);
            }
        }
    }

    /** Wrapper providing SourceLocator from SAXParseException information */
    private static class SaxParseExceptionSourceLocator implements SourceLocator {
        private final SAXParseException e;

        public SaxParseExceptionSourceLocator(SAXParseException saxException) {
            this.e = saxException;
        }

        @Override
        public String getPublicId() {
            return e.getPublicId();
        }

        @Override
        public String getSystemId() {
            return e.getSystemId();
        }

        @Override
        public int getLineNumber() {
            return e.getLineNumber();
        }

        @Override
        public int getColumnNumber() {
            return e.getColumnNumber();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("SaxParseExceptionSourceLocator:");
            if (e.getMessage() != null) {
                sb.append(e.getMessage());
            }
            sb.append(" {");
            if (e.getPublicId() != null) {
                sb.append(" publicId=").append(e.getPublicId());
            }
            if (e.getSystemId() != null) {
                sb.append(" systemId=").append(e.getSystemId());
            }
            if (e.getLineNumber() != -1) {
                sb.append(" lineNumber=").append(e.getLineNumber());
            }
            if (e.getColumnNumber() != -1) {
                sb.append(" columnNumber=").append(e.getColumnNumber());
            }
            sb.append('}');
            return sb.toString();
        }
    }
}
