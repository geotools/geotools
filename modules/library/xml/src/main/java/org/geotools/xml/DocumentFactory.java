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
 *   <li>{@link DocumentHandler#DEFAULT_NAMESPACE_HINT_KEY} - {@link Schema} for parsing and
 *       validation
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
     * When this hint is contained and set to Boolean.FALSE, element ordering will not be validated.
     * This key may also affect data validation within the parse routines. The inherent safety of
     * the resulting objects is weekend by turning this param to false.
     */
    public static final String VALIDATION_HINT = "DocumentFactory_VALIDATION_HINT";

    /**
     * When this hint is contained and set to Boolean.TRUE, external entities will be disabled. This
     * setting is used to alivate XXE attacks, preventing both {@link #VALIDATION_HINT} and {@link
     * XMLHandlerHints#ENTITY_RESOLVER} from being effective.
     */
    public static final String DISABLE_EXTERNAL_ENTITIES =
            "DocumentFactory_DISABLE_EXTERNAL_ENTITIES";

    /**
     * calls getInstance(URI,Level) with Level.WARNING
     *
     * @param hints May be null.
     * @return Object
     * @see DocumentFactory#getInstance(URI, Map, Level)
     */
    public static Object getInstance(URI desiredDocument, Map<String, Object> hints)
            throws SAXException {
        return getInstance(desiredDocument, hints, Level.WARNING);
    }

    /**
     * Parses the instance data provided. This method assumes that the XML document is fully
     * described using XML Schemas. Failure to be fully described as Schemas will result in errors,
     * as opposed to a vid parse.
     *
     * @param hints May be null.
     * @return Object
     * @see DocumentFactory#getInstance(URI, Map, Level, boolean)
     */
    public static Object getInstance(
            URI desiredDocument, @SuppressWarnings("rawtypes") Map hints, Level level)
            throws SAXException {
        @SuppressWarnings("unchecked")
        SAXParser parser = getParser(hints);

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(desiredDocument, hints);
        XMLSAXHandler.setLogLevel(level);

        try {
            parser.parse(desiredDocument.toString(), xmlContentHandler);
        } catch (IOException e) {
            throw new SAXException(e);
        }

        return xmlContentHandler.getDocument();
    }

    /**
     * Parses the instance data provided. This method assumes that the XML document is fully
     * described using XML Schemas. Failure to be fully described as Schemas will result in errors,
     * as opposed to a vid parse.
     *
     * @param hints May be null.
     * @return Object
     * @see DocumentFactory#getInstance(InputStream, Map, Level, boolean)
     */
    public static Object getInstance(InputStream is, Map<String, Object> hints, Level level)
            throws SAXException {
        SAXParser parser = getParser(hints);

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(hints);
        XMLSAXHandler.setLogLevel(level);

        try {
            parser.parse(is, xmlContentHandler);
        } catch (IOException e) {
            XMLSAXHandler.logger.warning(e.toString());
            throw new SAXException(e);
        }

        return xmlContentHandler.getDocument();
    }

    /*
     * Convenience method to create an instance of a SAXParser if it is null.
     */
    private static SAXParser getParser(Map<String, Object> hints) throws SAXException {
        SAXParserFactory spf = null;
        if (hints != null && hints.containsKey(XMLHandlerHints.SAX_PARSER_FACTORY)) {
            spf = (SAXParserFactory) hints.get(XMLHandlerHints.SAX_PARSER_FACTORY);
        } else {
            spf = SAXParserFactory.newInstance();
        }
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        try {
            // Extra precaution to reduce/prevent XXE attacks
            //
            // For more info: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
            // https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet

            // Step 1 distable DTD support - not needed for schema driven parser
            //
            // Note: XMLSaxHandler will reject all DTD references - but we may as well avoid early
            // spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // Step 2 optionally disable external entities
            //
            if (hints != null
                    && hints.containsKey(DISABLE_EXTERNAL_ENTITIES)
                    && Boolean.TRUE.equals(hints.get(DISABLE_EXTERNAL_ENTITIES))) {
                spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
                spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            }
            SAXParser sp = spf.newSAXParser();
            return sp;
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }
}
