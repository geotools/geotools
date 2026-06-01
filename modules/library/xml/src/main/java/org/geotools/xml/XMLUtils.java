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
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.geotools.util.EntityResolver3;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * XML related utilities not otherwise found in base libraries
 *
 * @author Andrea Aime - GeoSolutions
 */
public class XMLUtils {

    static final Logger LOGGER = Logging.getLogger(XMLUtils.class);

    // Apache Xerces Features
    static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
    static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    // SAX Features
    static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";

    //
    // XMLInputFactory
    //

    /**
     * Creates an XMLInputFactory configured with GeoTools library defaults.
     *
     * @return XMLInputFactory
     */
    public static XMLInputFactory newXMLInputFactory() {
        return newXMLInputFactory(GeoTools.getDefaultHints());
    }
    /**
     * Creates an XMLInputFactory configured with GeoTools library defaults.
     *
     * <p>The XMLInputFactory provided will be configured to be more forgiving when {@code NullEntityResolver} is used
     * as this indicates the library is configured to work with local files.
     *
     * <p>When working with EntityResolvers allowing `http` access, such as {@code `DefaultEntityResolver`} more
     * restrictions are enforced.
     *
     * @param hints Factory hints
     * @return XMLInputFactory
     */
    public static XMLInputFactory newXMLInputFactory(Hints hints) {
        XMLInputFactory factory = XMLInputFactory.newInstance(); // NOPMD AvoidXMLInputFactory

        // Used to determine sensible defaults based on entity resolver selected
        EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
        String access = getAccess(entityResolver, "");

        // We are disabling DTD by default since it is not often used by OGC Standards
        // (only WMS1.1 and GML1 and SLD1.0 standards make use of DTD)
        if (factory.isPropertySupported(XMLInputFactory.SUPPORT_DTD)) {
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        }
        if (factory.isPropertySupported(XMLInputFactory.IS_COALESCING)) {
            // coalescing needs to be false to disable resolving of external DTD entities
            factory.setProperty(XMLInputFactory.IS_COALESCING, false);
        }

        if (access.isEmpty()) {
            // GeoTools locks down all external entity facilities by default
            if (factory.isPropertySupported(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)) {
                factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            }
            if (factory.isPropertySupported(XMLConstants.ACCESS_EXTERNAL_SCHEMA)) {
                factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
            }
            if (factory.isPropertySupported(XMLInputFactory.SUPPORT_DTD)) {
                factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            }
            if (factory.isPropertySupported(XMLInputFactory.IS_COALESCING)) {
                factory.setProperty(XMLInputFactory.IS_COALESCING, false);
            }
            if (factory.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
                factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
            }
        } else {
            // If EntityResolver3 is used to provide access information,
            // external entity facilities will be relaxed accordingly
            boolean all = access.contains("all");
            boolean internal = access.contains("all")
                    || access.contains("file")
                    || access.contains("jar")
                    || access.contains("vfs");

            boolean external = access.contains("all") || access.contains("http");
            if (factory.isPropertySupported(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)) {
                factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, internal || external);
            }
            if (factory.isPropertySupported(XMLConstants.ACCESS_EXTERNAL_SCHEMA)) {
                factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, access);
            }
            if (factory.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
                factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, all);
            }
        }
        factory.setXMLResolver(new GTXMLResolver(entityResolver, factory.getXMLResolver(), hints));
        return factory;
    }

    //
    // SAXParser and SAXParserFactory utility methods
    //
    /**
     * Create a new SAXParserFactory that respects library configuration.
     *
     * @return SAX Parser Factory
     */
    public static SAXParserFactory newSAXParserFactory() {
        return newSAXParserFactory(null);
    }

    /**
     * Create a new SAXParserFactory that respects library configuration.
     *
     * @param hints Factory hints
     * @return SAX Parser Factory
     */
    public static SAXParserFactory newSAXParserFactory(final Hints hints) {
        // Factory applying GeoTools configuration to SAXParserFactory
        return newSAXParserFactory(null, hints);
    }

    /**
     * Create a new SAXParser that respects library configuration.
     *
     * @param hints Factory hints
     * @return SAX Parser Factory
     */
    public static SAXParserFactory newSAXParserFactory(final SAXParserFactory factory, final Hints hints) {
        // Factory applying GeoTools configuration to SAXParserFactory
        return new GTSAXParserFactory(factory, hints);
    }

    /**
     * Cast / wraps to GTSAXParserFactory respecting GeoTools configuration.
     *
     * @param factory SAXParserFactory factory, or {@code null}
     * @param hints Factory configuration
     * @return GTSAXParserFactory respecting GeoTools configuration.
     */
    public static GTSAXParserFactory toSAXParserFactory(SAXParserFactory factory, Hints hints) {
        if (factory == null) {
            return new GTSAXParserFactory(hints);
        } else if (factory instanceof GTSAXParserFactory gtSaxParserFactory) {
            if (Objects.equals(gtSaxParserFactory.hints, hints)) {
                return gtSaxParserFactory;
            } else {
                // Wrap original factory with modified hints
                return new GTSAXParserFactory(gtSaxParserFactory.factory, hints);
            }
        }
        return new GTSAXParserFactory(factory, hints);
    }

    /**
     * Creates a new instance of a SAXParser using GeoTools configuration.
     *
     * @return SAXParser setup with library configuration
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        return toSAXParserFactory(null, null).newSAXParser();
    }

    /**
     * Creates a new instance of a SAXParser provided factory, applying GeoTools configuration.
     *
     * @return SAXParser
     * @param factory SAXParserFactory factory, or {@code null}
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static SAXParser newSAXParser(SAXParserFactory factory) throws ParserConfigurationException, SAXException {
        return toSAXParserFactory(factory, null).newSAXParser();
    }

    /**
     * Creates a new instance of a SAXParser provided factory, applying GeoTools configuration.
     *
     * @return SAXParser
     * @param factory SAXParserFactory factory, or {@code null}
     * @param hints Factory configuration
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static SAXParser newSAXParser(SAXParserFactory factory, Hints hints)
            throws ParserConfigurationException, SAXException {
        return toSAXParserFactory(factory, hints).newSAXParser();
    }

    //
    // TransformerFactory and Transformer utility methods
    //

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
        return newTransformerFactory(hints).newTransformer(); // NOPMD
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
     * Cast /wraps to GTTransformerFactory or GTSAXTransformFactory respecting GeoTools configuration.
     *
     * @param factory Transformer factory, or {@code null}
     * @param hints Factory configuration
     * @return GTTransformerFactory or GTSAXTransformFactory respecting GeoTools configuration.
     */
    protected static TransformerFactory toTransformerFactory(TransformerFactory factory, Hints hints) {
        if (factory == null) {
            return new GTTransformerFactory(hints);
        } else if (factory instanceof GTTransformerFactory gtTransformerFactory) {
            if (Objects.equals(gtTransformerFactory.hints, hints)) {
                return gtTransformerFactory;
            } else {
                // Wrap original factory with modified hints
                return new GTTransformerFactory(gtTransformerFactory.factory, hints);
            }
        } else if (factory instanceof GTSAXTransformerFactory gtSaxFactory) {
            if (Objects.equals(gtSaxFactory.hints, hints)) {
                return factory;
            } else {
                // Wrap original factory with modified hints
                return new GTSAXTransformerFactory(gtSaxFactory.factory, hints);
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
        return newTransformer(source, null);
    }

    /**
     * Creates a new Transformer as an alternative to direct use of {@link TransformerFactory#newTransformer()}.
     *
     * @param source Source input
     * @param hints Factory configuration
     * @return XML transformer
     */
    public static Transformer newTransformer(Source source, Hints hints) throws TransformerConfigurationException {
        return newTransformer(null, source, null);
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
        return newTransformer(factory, source, null);
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
        return toTransformerFactory(factory, hints).newTransformer(source); // NOPMD AvoidTransformer
    }

    /**
     * Alternative to direct use of {@code DOMSOurce}, allowing GeoTools library configuration to be applied when
     * traversal is required.
     *
     * @param dom Document node
     * @return Source with any XML streaming subject to GeoTools configuration including
     *     {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static Source source(Node dom) {
        // no traversal needed
        return new DOMSource(dom);
    }

    /**
     * Alternative to direct use of {@code Source}, allowing GeoTools library configuration to be applied when traversal
     * is required.
     *
     * @param source
     * @return Source with any XML streaming subject to GeoTools configuration including
     *     {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static Source source(Source source) {
        return source(source, GeoTools.getDefaultHints());
    }

    /**
     * Alternative to direct use of {@code Source}, allowing GeoTools library configuration to be applied when traversal
     * is required.
     *
     * @param source transform source
     * @param hints GeoTools library configuration
     * @return Source with any XML streaming subject to GeoTools configuration including
     *     {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static Source source(Source source, Hints hints) {
        if (source == null) return null;

        InputSource inputSource;
        if (source instanceof SAXSource saxSource) {
            inputSource = saxSource.getInputSource();
        } else if (source instanceof StreamSource streamSource) {
            inputSource = new InputSource(streamSource.getSystemId());
            inputSource.setByteStream(streamSource.getInputStream());
            inputSource.setCharacterStream(streamSource.getReader());
            inputSource.setPublicId(streamSource.getPublicId());
        } else if (source instanceof InputSource) {
            inputSource = (InputSource) source;
        } else if (source instanceof DOMSource domSource) {
            return domSource; // already traversed
        } else {
            return source; // unprocessed
        }

        // Safe traversal with GeoTools configuration
        return sax(inputSource, hints);
    }

    /**
     * Alternative to direct use of InputSource allowing SAXSource setup with GeoTools configuration to be applied.
     *
     * @param inputSource InputSource
     * @param hints GeoTools library configuration
     * @return Source with any XML streaming subject to GeoTools configuration including
     *     {@link GeoTools#getEntityResolver(Hints)}.
     */
    public static SAXSource sax(InputSource inputSource, Hints hints) {
        if (inputSource == null) return null;
        try {
            GTSAXParserFactory factory = new GTSAXParserFactory(hints);

            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setEntityResolver(GeoTools.getEntityResolver(hints));
            return new SAXSource(xmlReader, inputSource);
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
        if (factory.isValidating()) {
            builder.setErrorHandler(new SAXErrorHandler());
        }
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

    //
    // SchemaFactory
    //
    /**
     * Creates a new SchemaFactory allowing GeoTools configuration to be applied.
     *
     * @param schemaLanguage Schema language which the factory will understand
     * @return New instance of schema factory supporting {@code schemaLanguage}
     */
    public static SchemaFactory newSchemaFactory(String schemaLanguage) {
        return newSchemaFactory(schemaLanguage, GeoTools.getDefaultHints());
    }

    /**
     * Creates a new SchemaFactory allowing GeoTools configuration to be applied.
     *
     * @param schemaLanguage Schema language which the factory will understand
     * @param hints Factory hints
     * @return New instance of schema factory supporting {@code schemaLanguage}
     */
    public static SchemaFactory newSchemaFactory(String schemaLanguage, Hints hints) {
        SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage); // NOPMD AvoidSchemaFactory

        EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
        final String ACCESS = getAccess(entityResolver, "");
        try {
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ACCESS);
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            LOGGER.warning("Parser does not support ACCESS_EXTERNAL_DTD: " + e.getMessage());
        }
        try {
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ACCESS);
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            LOGGER.warning("Parser does not support ACCESS_EXTERNAL_SCHEMA: " + e.getMessage());
        }
        factory.setResourceResolver(new GTLSResourceResolver(entityResolver, factory.getResourceResolver(), hints));
        return factory;
    }

    /**
     * Sensible default for EntityResolver access based on provided entityResolver.
     *
     * <p>The provided value is based on recognizing {@code NullEntityResolver} use for local files, and
     * {@code DefaultEntityResolver} and {@code PreventLocalEntityResolver} for trusted {@code http} locations.
     *
     * <p>If the entity provider is not recognized {@code ""} is provided to cut off access.
     *
     * @param entityResolver EntityResolver
     * @param protocol Default protocol to use if entityResolver is not recognized
     * @return Entity resolution protocol: {@code "all"}, {@code "http"}, or {@code ""} based on provided entityResolver
     */
    private static String getAccess(EntityResolver entityResolver, String protocol) {
        if (entityResolver == null) {
            return "";
        }
        if (entityResolver instanceof EntityResolver3 entityResolver3) {
            return entityResolver3.getAccess();
        }
        if (protocol == null) {
            return ""; // no access
        }
        return protocol;
    }

    /**
     * Tests whether the TransformerFactory and SchemaFactory implementations support JAXP 1.5 properties to protect
     * against XML external entity injection (XXE) attacks. The internal JDK XML processors starting with JDK 7u40 would
     * support these properties but outdated versions of XML libraries (e.g., Xalan, Xerces) that do not support these
     * properties may be included in GeoServer's classpath or provided by the web application server.
     *
     * <p>GeoTools uses these properties internally.
     *
     * @throws IllegalStateException if the JAXP 1.5 properties are not supported or if there was an error checking for
     *     JAXP 1.5 support
     */
    public static void checkSupportForJAXP15Properties() {
        List<String> classes = new ArrayList<>();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance(); // NOPMD AvoidTransformerFactory
            try {
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            } catch (IllegalArgumentException e) {
                classes.add(transformerFactory.getClass().getName());
            }
            SchemaFactory schemaFactory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // NOPMD AvoidSchemaFactory
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

    /**
     * Apply GeoTools configuration to the transformer, including {@code GeoTools.getEntityResolver(hints)}.
     *
     * @param transformer XML Transformer
     * @return transformer
     */
    static Transformer config(Transformer transformer, Hints hints) {
        URIResolver uriResolver = transformer.getURIResolver();
        if (uriResolver != null && !(uriResolver instanceof GTURIResolver)) {
            final EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
            transformer.setURIResolver(new GTURIResolver(entityResolver, uriResolver, hints));
        } else {
            transformer.setURIResolver(new GTURIResolver(GeoTools.getEntityResolver(hints), null, hints));
        }
        return transformer;
    }

    /**
     * Apply GeoTools configuration to TransformHandler.
     *
     * @param handler Transform handler
     * @return Transform handler
     */
    static TransformerHandler config(TransformerHandler handler, Hints hints) {
        XMLUtils.config(handler.getTransformer(), hints);
        return handler;
    }

    /**
     * Apply GeoTools configuration to XMLFilter.
     *
     * @param filter XML Fitler
     * @return XML Filter
     */
    static XMLFilter config(XMLFilter filter, Hints hints) {
        filter.setEntityResolver(GeoTools.getEntityResolver(hints));
        return filter;
    }

    /** Wrapper ensuring system SAXParserFactory configured with {@code GeoTools.getEntityResolver(hints)}. */
    private static class GTSAXParserFactory extends SAXParserFactory {
        protected final SAXParserFactory factory;
        protected final Hints hints;

        public GTSAXParserFactory(Hints hints) {
            this(SAXParserFactory.newInstance(), hints); // NOPMD AvoidParserzfactory
        }

        public GTSAXParserFactory(SAXParserFactory factory, Hints hints) {
            this.hints = hints;
            this.factory = factory != null ? factory : SAXParserFactory.newInstance(); // NOPMD AvoidParserFactory

            // DocumentBuilderFactory and SAXParserFactory can be protected with the same techniques
            try {
                this.factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            } catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e) {
                // feature secure processing recommended, but not supported
                LOGGER.fine("Parser does not support secure processing feature: "
                        + this.factory.getClass().getName());
            }
            // Disable DTD Use
            boolean disabledDTD = false;

            // Recommended: Disable Xerces only
            try {
                this.factory.setFeature(DISALLOW_DOCTYPE_DECL, true);
                this.factory.setXIncludeAware(false);
                disabledDTD = true;
            } catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e) {
                // Xerces specific feature, so not required to be supported
                LOGGER.fine("Xerces `" + DISALLOW_DOCTYPE_DECL + "` setting not supported: "
                        + this.factory.getClass().getName());
            } catch (UnsupportedOperationException e) {
                LOGGER.fine("setXIncludeAware setting not supported: "
                        + this.factory.getClass().getName());
            }

            if (!disabledDTD) {
                // Xerces disable not supported, try another way...

                // Both EXTERNAL_GENERAL_ENTITIES and EXTERNAL_PARAMETER_ENTITIES need to be disabled together
                try {
                    this.factory.setFeature(EXTERNAL_PARAMETER_ENTITIES, false);
                } catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
                    LOGGER.fine("Sax `" + EXTERNAL_PARAMETER_ENTITIES + "` setting not supported: "
                            + this.factory.getClass().getName());
                }
                try {
                    this.factory.setFeature(EXTERNAL_GENERAL_ENTITIES, false);
                } catch (SAXNotSupportedException | ParserConfigurationException | SAXNotRecognizedException e) {
                    LOGGER.fine("Sax `" + EXTERNAL_PARAMETER_ENTITIES + "` setting not supported: "
                            + this.factory.getClass().getName());
                }
                // disable external DTDs as well
                try {
                    this.factory.setFeature(LOAD_EXTERNAL_DTD, false);
                } catch (SAXNotSupportedException | ParserConfigurationException | SAXNotRecognizedException e) {
                    LOGGER.fine("Xerces `" + LOAD_EXTERNAL_DTD + "` setting not supported: "
                            + this.factory.getClass().getName());
                }
                try {
                    this.factory.setXIncludeAware(false);
                } catch (UnsupportedOperationException e) {
                    LOGGER.fine("setXIncludeAware setting not supported: "
                            + this.factory.getClass().getName());
                }
                disabledDTD = true;
            }

            if (!disabledDTD) {
                LOGGER.warning("Unable to ensure DTD support is disabled: "
                        + this.factory.getClass().getName());
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

            EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
            if (parser.getXMLReader() != null) {
                parser.getXMLReader().setEntityResolver(entityResolver);
            }

            // Do not allow DTD access on any protocol
            try {
                parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            } catch (IllegalArgumentException notSupported) {
                LOGGER.fine("Parser does not support ACCESS_EXTERNAL_DTD: " + notSupported.getMessage());
            }
            // Sensible XSD factory defaults based on EntityResolver3
            final String ACCESS = getAccess(entityResolver, "");
            try {
                parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ACCESS);
            } catch (IllegalArgumentException notSupported) {
                LOGGER.fine("Parser does not support ACCESS_EXTERNAL_SCHEMA: " + notSupported.getMessage());
            }
            return parser;
        }

        @Override
        public String toString() {
            return "GTSAXParserFactory {" + factory + "}";
        }
    }

    /**
     * Wrapper ensuring DocumentBuilderFactory configured with {@code GeoTools.getEntityResolver(hints)}.
     *
     * <p>This class provides special handling for known trusted EntityResolver implementations:
     *
     * <ul>
     *   <li>{@code DefaultEntityResolver}: Allowing external {@code http} access
     *   <li>{@code NullEntityResolver}: Allowing external {@code all} access
     * </ul>
     */
    private static class GTDocumentBuilderFactory extends DocumentBuilderFactory {
        final Hints hints;
        private final DocumentBuilderFactory factory;

        public GTDocumentBuilderFactory(Hints hints) {
            this.hints = hints != null ? hints : GeoTools.getDefaultHints();
            this.factory = DocumentBuilderFactory.newInstance(); // NOPMD AvoidDocumentBuilderFactory

            // DocumentBuilderFactory and SAXParserFactory can be protected with the same techniques
            try {
                this.factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            } catch (ParserConfigurationException e) {
                // feature secure processing recommended, but not supported
                LOGGER.fine("Parser does not support secure processing feature: "
                        + this.factory.getClass().getName());
            }
            // Disable DTD Use
            boolean disabledDTD = false;

            // Recommended: Disable Xerces only
            try {
                this.factory.setFeature(DISALLOW_DOCTYPE_DECL, true);
                this.factory.setXIncludeAware(false);
                disabledDTD = true;
            } catch (ParserConfigurationException e) {
                // Xerces specific feature, so not required to be supported
                LOGGER.fine("Xerces `" + DISALLOW_DOCTYPE_DECL + "` setting not supported: "
                        + this.factory.getClass().getName());
            } catch (UnsupportedOperationException e) {
                LOGGER.fine("setXIncludeAware setting not supported: "
                        + this.factory.getClass().getName());
            }

            if (!disabledDTD) {
                // Xerces disable not supported, try another way...

                // Both EXTERNAL_GENERAL_ENTITIES and EXTERNAL_PARAMETER_ENTITIES need to be disabled together
                try {
                    this.factory.setFeature(EXTERNAL_PARAMETER_ENTITIES, false);
                } catch (ParserConfigurationException e) {
                    LOGGER.fine("Sax `" + EXTERNAL_PARAMETER_ENTITIES + "` setting not supported: "
                            + this.factory.getClass().getName());
                }
                try {
                    this.factory.setFeature(EXTERNAL_GENERAL_ENTITIES, false);
                } catch (ParserConfigurationException e) {
                    LOGGER.fine("Sax `" + EXTERNAL_PARAMETER_ENTITIES + "` setting not supported: "
                            + this.factory.getClass().getName());
                }
                // disable external DTDs as well
                try {
                    this.factory.setFeature(LOAD_EXTERNAL_DTD, false);
                } catch (ParserConfigurationException e) {
                    LOGGER.fine("Xerces `" + LOAD_EXTERNAL_DTD + "` setting not supported: "
                            + this.factory.getClass().getName());
                }
                try {
                    this.factory.setXIncludeAware(false);
                } catch (UnsupportedOperationException e) {
                    LOGGER.fine("setXIncludeAware setting not supported: "
                            + this.factory.getClass().getName());
                }
                try {
                    this.factory.setExpandEntityReferences(false);
                } catch (UnsupportedOperationException e) {
                    LOGGER.fine("setExpandEntityReferences setting not supported: "
                            + this.factory.getClass().getName());
                }
                disabledDTD = true;
            }

            if (!disabledDTD) {
                LOGGER.warning("Unable to ensure DTD support is disabled: "
                        + this.factory.getClass().getName());
            }

            // Do not allow DTD access on any protocol
            try {
                this.factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            } catch (IllegalArgumentException notSupported) {
                LOGGER.fine("Parser does not support ACCESS_EXTERNAL_DTD: " + notSupported.getMessage());
            }

            // Sensible XSD factory defaults based on EntityResolver3
            EntityResolver entityResolver = GeoTools.getEntityResolver(hints);
            final String ACCESS = getAccess(entityResolver, "");
            try {
                this.factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ACCESS);
            } catch (IllegalArgumentException notSupported) {
                LOGGER.fine("Parser does not support ACCESS_EXTERNAL_SCHEMA: " + notSupported.getMessage());
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
            this(null, hints);
        }

        public GTTransformerFactory(TransformerFactory factory, Hints hints) {
            this.hints = hints != null ? hints : GeoTools.getDefaultHints();
            this.factory = factory != null ? factory : TransformerFactory.newInstance(); // NOPMD AvoidTransformer
        }

        @Override
        public Transformer newTransformer(Source source) throws TransformerConfigurationException {
            return XMLUtils.config(factory.newTransformer(source), hints); // NOPMD AvoidTransformerSource
        }

        @Override
        public Transformer newTransformer() throws TransformerConfigurationException {
            return XMLUtils.config(factory.newTransformer(), hints); // NOPMD AvoidTransform
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

        // overrides

        @Override
        public Transformer newTransformer(Source source) throws TransformerConfigurationException {
            return XMLUtils.config(factory.newTransformer(source), hints); // NOPMD AvoidTransform
        }

        @Override
        public Transformer newTransformer() throws TransformerConfigurationException {
            return XMLUtils.config(factory.newTransformer(), null); // NOPMD AvoidTransform
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
            return XMLUtils.config(factory.newTransformerHandler(src), hints);
        }

        @Override
        public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
            return XMLUtils.config(factory.newTransformerHandler(templates), hints);
        }

        @Override
        public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
            return XMLUtils.config(factory.newTransformerHandler(), hints);
        }

        @Override
        public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
            return factory.newTemplatesHandler();
        }

        @Override
        public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
            return XMLUtils.config(factory.newXMLFilter(src), null);
        }

        @Override
        public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
            return XMLUtils.config(factory.newXMLFilter(templates), hints);
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
        private final Hints hints;

        public GTURIResolver(EntityResolver entityResolver) {
            this(entityResolver, null);
        }

        public GTURIResolver(EntityResolver entityResolver, URIResolver uriResolver) {
            this(entityResolver, uriResolver, null);
        }

        public GTURIResolver(EntityResolver entityResolver, URIResolver uriResolver, Hints hints) {
            this.entityResolver = entityResolver;
            this.uriResolver = uriResolver;
            this.hints = hints;
        }

        @Override
        public Source resolve(String href, String base) throws TransformerException {
            try {
                // step 1: check resolver: null (external), source (internal), or exception (forbidden)
                InputSource source = entityResolver.resolveEntity(href, base);
                if (source != null) {
                    // resolved internally, wrap and apply GeoTools configuration
                    return XMLUtils.sax(source, hints);
                }

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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("GTURIResolver{");
            sb.append("uriResolver=").append(uriResolver);
            sb.append('}');
            return sb.toString();
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

    /** SAXErrorHandler that logs errors. */
    public static class SAXErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
        }

        @Override
        public String toString() {
            return "SAXErrorHandler";
        }
    }

    /** XMLResolver that delegates to EntityResolver to verify allowed locations. */
    private static class GTXMLResolver implements XMLResolver {

        private final EntityResolver entityResolver;
        private final XMLResolver xmlResolver;
        private final Hints hints;

        public GTXMLResolver(EntityResolver entityResolver, XMLResolver xmlResolver, Hints hints) {
            this.entityResolver = entityResolver;
            this.xmlResolver = xmlResolver;
            this.hints = hints;
        }

        /**
         * java.io.InputStream (2) javax.xml.stream.XMLStreamReader (3) java.xml.stream.XMLEventReader.
         *
         * @param publicID The public identifier of the external entity being referenced, or null if none was supplied.
         * @param systemID The system identifier of the external entity being referenced.
         * @param baseURI Absolute base URI associated with systemId.
         * @param namespace The namespace of the entity to resolve.
         * @return
         * @throws XMLStreamException
         */
        @Override
        public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace)
                throws XMLStreamException {
            // step 1: check with xml resolver: null (external), source (internal), or exception (forbidden)
            InputSource source;
            try {
                if (entityResolver instanceof EntityResolver2 entityResolver2) {
                    source = entityResolver2.resolveEntity(publicID, systemID, baseURI, namespace);
                } else {
                    source = entityResolver.resolveEntity(publicID, systemID);
                }
                if (source != null) {
                    // 1. Prefer the character stream (Reader) if available
                    if (source.getCharacterStream() != null) {
                        XMLInputFactory factory = XMLUtils.newXMLInputFactory(hints);
                        return factory.createXMLStreamReader(source.getCharacterStream());
                    }
                    // 2. Fall back to the byte stream (InputStream)
                    else if (source.getByteStream() != null) {
                        XMLInputFactory factory = XMLUtils.newXMLInputFactory(hints);
                        return factory.createXMLStreamReader(source.getByteStream(), source.getEncoding());
                    }
                    // 3. Allow factory to resolve systemId
                    else if (source.getSystemId() != null) {
                        return null;
                    }
                }
                // step 2: check with uri resolver
                if (xmlResolver != null) {
                    return xmlResolver.resolveEntity(publicID, systemID, baseURI, namespace);
                }
                // step 3: allow input factory to handle
                return null;
            } catch (SAXException | IOException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("GTXMLResolver{");
            sb.append("xmlResolver=").append(xmlResolver);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Adapts InputSource to LSResource, allowing to be directly used during DOM Load and Save operations.
     *
     * <p>Adapted from GeoServer {@code InputSourceToLSResource} example.
     */
    private static class GTLSInput implements LSInput {

        private InputSource delegate;

        public GTLSInput(InputSource delegate) {
            this.delegate = delegate;
        }

        @Override
        public void setPublicId(String publicId) {
            delegate.setPublicId(publicId);
        }

        @Override
        public String getPublicId() {
            return delegate.getPublicId();
        }

        @Override
        public void setSystemId(String systemId) {
            delegate.setSystemId(systemId);
        }

        @Override
        public String getSystemId() {
            return delegate.getSystemId();
        }

        @Override
        public void setByteStream(InputStream byteStream) {
            delegate.setByteStream(byteStream);
        }

        @Override
        public InputStream getByteStream() {
            return delegate.getByteStream();
        }

        @Override
        public void setEncoding(String encoding) {
            delegate.setEncoding(encoding);
        }

        @Override
        public String getEncoding() {
            return delegate.getEncoding();
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
            delegate.setCharacterStream(characterStream);
        }

        @Override
        public Reader getCharacterStream() {
            return delegate.getCharacterStream();
        }

        @Override
        public String getStringData() {
            return null;
        }

        @Override
        public void setStringData(String stringData) {
            // nothing to do

        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public void setBaseURI(String baseURI) {}

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {}

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("GTLSResource{");
            sb.append("delegate=").append(delegate);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * LSResourceResolver that delegates to EntityResolver to verify allowed locations during DOM Load and Save
     * operations.
     */
    private static class GTLSResourceResolver implements LSResourceResolver {

        private final EntityResolver entityResolver;
        private final LSResourceResolver lsResolver;

        public GTLSResourceResolver(EntityResolver entityResolver, LSResourceResolver lsResolver, Hints hints) {
            this.entityResolver = entityResolver;
            this.lsResolver = lsResolver;
        }

        /**
         * java.io.InputStream (2) javax.xml.stream.XMLStreamReader (3) java.xml.stream.XMLEventReader.
         *
         * @param type The type of the resource being resolved, such as {@code http://www.w3.org/2001/XMLSchema}
         * @param publicId The public identifier of the external entity being referenced, or null if none was supplied.
         * @param systemId The system identifier of the external entity being referenced.
         * @param baseURI Absolute base URI associated with systemId.
         * @return Returns {@code null} to for default handling, or throws an exception if access not granted
         * @throws XMLStreamException
         */
        @Override
        public LSInput resolveResource(
                String type, String namespaceURI, String publicId, String systemId, String baseURI) {
            // step 1: check with xml resolver: null (external), source (internal), or exception (forbidden)
            if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(type)) {
                try {
                    InputSource source = entityResolver.resolveEntity(publicId, systemId);
                    if (source != null) {
                        return new GTLSInput(source);
                    }
                } catch (SAXException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // step 2: check with uri resolver
            if (lsResolver != null) {
                return lsResolver.resolveResource(type, namespaceURI, publicId, systemId, baseURI);
            }
            // step 3: allow input factory to handle
            return null;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("GTLSResourceResolver{");
            sb.append("lsResolver=").append(lsResolver);
            sb.append('}');
            return sb.toString();
        }
    }
}
