/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.xml.handlers.DocumentHandler;
import org.geotools.xml.schema.Schema;
import org.xml.sax.SAXException;

/**
 * This is the main entry point into the XSI parsing routines.
 *
 * <p>Example Use:
 *
 * <pre>
 *     Object x = DocumentFactory.getInstance(new URI(&quot;MyInstanceDocumentURI&quot;);
 * </pre>
 *
 * <p>A selection of the hints available to configure parsing:
 *
 * <ul>
 *   <li>{@link #VALIDATION_HINT} - Boolean.FALSE to disable validation
 *   <li>{@link DocumentHandler#DEFAULT_NAMESPACE_HINT_KEY} - {@link Schema} for parsing and validation
 *   <li>{@link XMLHandlerHints#FLOW_HANDLER_HINT}
 *   <li>{@link XMLHandlerHints#NAMESPACE_MAPPING} - Map&lt;String,URL&gt; namespace mapping
 *   <li>{@link XMLHandlerHints#ENTITY_RESOLVER} - control entry resolution
 *   <li>
 *   <li>{@link #DISABLE_EXTERNAL_ENTITIES} - Boolean.TRUE to disable entity resolution
 *   <li>
 *   <li>{@link XMLHandlerHints#SAX_PARSER_FACTORY} - supply factory used by {@link #getParser(Map)}
 * </ul>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 */
public class DocumentFactory {

    /**
     * When this hint is contained and set to {@code Boolean.FALSE}, element ordering will not be validated. This key
     * may also affect data validation within the parse routines.
     *
     * <p>The inherent safety of the resulting objects is weakened by turning this param to {@code false}.
     */
    public static final String VALIDATION_HINT = "DocumentFactory_VALIDATION_HINT";

    /**
     * When this hint is contained and set to {@code Boolean.TRUE} (the default value), external entities will be
     * disabled.
     *
     * <p>This setting is used to alleviate XXE attacks, preventing both {@link #VALIDATION_HINT} and
     * {@link XMLHandlerHints#ENTITY_RESOLVER} from being effective.
     */
    public static final String DISABLE_EXTERNAL_ENTITIES = "DocumentFactory_DISABLE_EXTERNAL_ENTITIES";

    /**
     * When this hint is contained and set to {@code Boolean.TRUE}, parsing and validation supports use of DTD.
     *
     * <p>A few standards such as SLD1.0 and WMS1.1 make use of DTD support and require this hint to be set to true.
     */
    public static final String ENABLE_DTD = "DocumentFactory_ENABLE_DTD";

    public static final String LOG_LEVEL = "DocumentFactory_LOG_LEVEL";

    /**
     * Calls getInstance(URI,Level) with Level.WARNING.
     *
     * @param hints May be null.
     * @return Object
     * @see DocumentFactory#getInstance(URI, Map, Level)
     */
    public static Object getInstance(URI desiredDocument, Map<String, Object> hints) throws SAXException {
        return getInstance(desiredDocument, hints, Level.WARNING);
    }

    /**
     * Parses the instance data provided. This method assumes that the XML document is fully described using XML
     * Schemas. Failure to be fully described as Schemas will result in errors, as opposed to a vid parse.
     *
     * @param hints May be null.
     * @return Object
     */
    public static Object getInstance(URI desiredDocument, Map<String, Object> hints, Level defaultLevel)
            throws SAXException {
        SAXParser parser = getParser(hints);

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(desiredDocument, hints);

        Level level = hint(Level.class, hints, LOG_LEVEL, defaultLevel);
        XMLSAXHandler.setLogLevel(level);
        try {
            parser.parse(desiredDocument.toString(), xmlContentHandler);
        } catch (IOException e) {
            throw new SAXException(e);
        }

        return xmlContentHandler.getDocument();
    }

    /**
     * Parses the instance data provided. This method assumes that the XML document is fully described using XML
     * Schemas. Failure to be fully described as Schemas will result in errors, as opposed to a vid parse.
     *
     * @param is InputStream to parse, will be closed by this method.
     * @param hints DocumentFactory parsing hints
     * @param defaultLevel Log level to use for logging during parsing, may be null (defaults to Level.WARNING).
     * @return Parsed
     * @throws SAXException If an error occurs during parsing, or if the provided InputStream cannot be read.
     */
    public static Object getInstance(InputStream is, Map<String, Object> hints, Level defaultLevel)
            throws SAXException {
        SAXParser parser = getParser(hints);

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(hints);
        Level level = hint(Level.class, hints, LOG_LEVEL, defaultLevel);
        XMLSAXHandler.setLogLevel(level);
        try {
            parser.parse(is, xmlContentHandler);
        } catch (IOException e) {
            throw new SAXException(e);
        }
        return xmlContentHandler.getDocument();
    }

    /*
     * Convenience method to create an instance of a SAXParser if it is null.
     *
     * Tests can request SAXParser configuration using hints {@code ENTITY_RESOLVER}, {@code SAX_PARSER_FACTORY},
     * {@code ENABLE_DTD}, {@code DISABLE_EXTERNAL_ENTITIES}.
     */
    private static SAXParser getParser(Map<String, Object> hints) throws SAXException {

        Hints factoryConfig = GeoTools.getDefaultHints();
        if (hints != null && hints.containsKey(XMLHandlerHints.ENTITY_RESOLVER)) {
            factoryConfig.put(Hints.ENTITY_RESOLVER, hints.get(XMLHandlerHints.ENTITY_RESOLVER));
        }

        SAXParserFactory saxParserFactory;
        if (hints != null && hints.containsKey(XMLHandlerHints.SAX_PARSER_FACTORY)) {
            SAXParserFactory provided = (SAXParserFactory) hints.get(XMLHandlerHints.SAX_PARSER_FACTORY);
            saxParserFactory = XMLUtils.toSAXParserFactory(provided, factoryConfig);
        } else {
            saxParserFactory = XMLUtils.newSAXParserFactory(factoryConfig);
        }
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(false);
        try {
            // Extra precaution to reduce/prevent XXE attacks
            //
            // For more info: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
            // https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet

            boolean enableDTD = hint(hints, ENABLE_DTD, false);

            // Step 1 Factory Disable/enable DTD support - not needed for schema driven parser
            //
            // Note: XMLSaxHandler will reject all DTD references - but we may as well avoid early
            XMLUtils.supportDTD(saxParserFactory, enableDTD, factoryConfig);

            // Step 2 Factory optionally disable external entities
            boolean disableExternalEntities = hint(hints, DISABLE_EXTERNAL_ENTITIES, enableDTD);
            saxParserFactory.setFeature(
                    "http://xml.org/sax/features/external-general-entities", !disableExternalEntities);
            saxParserFactory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities", !disableExternalEntities);

            // Step 3 Parser external access based on entityResolver used
            SAXParser parser = XMLUtils.newSAXParser(saxParserFactory, factoryConfig);
            XMLUtils.supportDTD(parser, enableDTD, factoryConfig);

            return parser;
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Safely check hints for provided key.
     *
     * @param hints Map of hints, may be {@null} if not provided.
     * @param key Key to look up hint value
     * @param defaultValue Default value returned if value is not available, or not a {@code Boolean}.
     * @return Value of hint if available and a {@code Boolean}, otherwise {@code defaultValue}.
     */
    public static <T> T hint(Class<T> type, Map<String, Object> hints, String key, T defaultValue) {
        if (key != null && hints != null && hints.containsKey(key)) {
            Object value = hints.get(key);
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        }
        return defaultValue;
    }

    /**
     * Safely check hints for provided key.
     *
     * @param hints Map of hints, may be {@null} if not provided.
     * @param key Key to look up hint value
     * @param defaultValue Default value returned if value is not available, or not a {@code Boolean}.
     * @return Value of hint if available and a {@code Boolean}, otherwise {@code defaultValue}.
     */
    public static boolean hint(Map<String, Object> hints, String key, boolean defaultValue) {
        if (key != null && hints != null && hints.containsKey(key)) {
            Object value = hints.get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
        }
        return defaultValue;
    }
}
