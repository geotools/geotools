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
package org.geotools.gml2.simple;

import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.gml.producer.CoordinateFormatter;
import org.geotools.gml2.GML;
import org.geotools.xml.XMLUtils;
import org.locationtech.jts.geom.CoordinateSequence;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Helper class writing out GML elements and coordinates. Geared towards efficiency, write out
 * elements and ordinate lists with the minimim amount of garbage generation
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GMLWriter {

    static final QualifiedName COORDINATES = new QualifiedName(GML.NAMESPACE, "coordinates", "gml");

    static final QualifiedName POS_LIST = new QualifiedName(GML.NAMESPACE, "posList", "gml");

    private final CoordinateFormatter coordFormatter;

    /** The actual XML encoder */
    ContentHandler handler;

    /** All the namespaces known to the Encoder */
    NamespaceSupport namespaces;

    /** We use a StringBuffer because the date formatters cannot take a StringBuilder */
    StringBuffer sb = new StringBuffer();

    /**
     * The StringBuffer above gets dumped into this char buffer in order to pass the chars to the
     * handler
     */
    char[] buffer;

    /** Coordinates qualified name, with the right prefix */
    private QualifiedName coordinates;

    /** posList qualified name, with the right prefix */
    private QualifiedName posList;

    /** Controls if coordinates measures should be encoded in GML * */
    private boolean encodeMeasures;

    /**
     * Create a new content handler
     *
     * @param delegate The actual XML writer
     * @param namespaces The namespaces known to the Encoder
     * @param numDecimals How many decimals to preserve when writing ordinates
     * @param forceDecimal If xs:decimal compliant encoding should be used, or not
     * @param gmlPrefix The GML namespace prefix
     */
    public GMLWriter(
            ContentHandler delegate,
            NamespaceSupport namespaces,
            int numDecimals,
            boolean forceDecimal,
            String gmlPrefix) {
        this(delegate, namespaces, numDecimals, forceDecimal, false, gmlPrefix, true);
    }

    /**
     * Create a new content handler
     *
     * @param delegate The actual XML writer
     * @param namespaces The namespaces known to the Encoder
     * @param numDecimals How many decimals to preserve when writing ordinates
     * @param forceDecimal If xs:decimal compliant encoding should be used, or not
     * @param padWithZeros If apply zero padding
     * @param gmlPrefix The GML namespace prefix
     */
    public GMLWriter(
            ContentHandler delegate,
            NamespaceSupport namespaces,
            int numDecimals,
            boolean forceDecimal,
            boolean padWithZeros,
            String gmlPrefix) {
        this(delegate, namespaces, numDecimals, forceDecimal, padWithZeros, gmlPrefix, true);
    }

    /**
     * Create a new content handler
     *
     * @param delegate The actual XML writer
     * @param namespaces The namespaces known to the Encoder
     * @param numDecimals How many decimals to preserve when writing ordinates
     * @param forceDecimal If xs:decimal compliant encoding should be used, or not
     * @param gmlPrefix The GML namespace prefix
     * @param encodeMeasures TRUE if coordinates measures should be included
     */
    public GMLWriter(
            ContentHandler delegate,
            NamespaceSupport namespaces,
            int numDecimals,
            boolean forceDecimal,
            boolean padWithZeros,
            String gmlPrefix,
            boolean encodeMeasures) {
        this.handler = delegate;
        this.namespaces = namespaces;

        String gmlUri = namespaces.getURI(gmlPrefix);
        if (gmlUri == null) {
            gmlUri = GML.NAMESPACE;
        }

        this.coordinates = COORDINATES.derive(gmlPrefix, gmlUri);
        this.posList = POS_LIST.derive(gmlPrefix, gmlUri);

        this.coordFormatter = new CoordinateFormatter(numDecimals);
        this.coordFormatter.setForcedDecimal(forceDecimal);
        this.coordFormatter.setPadWithZeros(padWithZeros);

        this.encodeMeasures = encodeMeasures;
    }

    /** @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator) */
    public void setDocumentLocator(Locator locator) {
        handler.setDocumentLocator(locator);
    }

    /** @see org.xml.sax.ContentHandler#startDocument() */
    public void startDocument() throws SAXException {
        handler.startDocument();
    }

    /** @see org.xml.sax.ContentHandler#endDocument() */
    public void endDocument() throws SAXException {
        handler.endDocument();
    }

    /** @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String) */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        handler.startPrefixMapping(prefix, uri);
    }

    /** @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String) */
    public void endPrefixMapping(String prefix) throws SAXException {
        handler.endPrefixMapping(prefix);
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String,
     *     java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(QualifiedName qn, Attributes atts) throws SAXException {
        String qualifiedName = qn.getQualifiedName();
        if (qualifiedName == null) {
            qualifiedName = qualify(qn.getNamespaceURI(), qn.getLocalPart(), null);
        }
        if (atts == null) {
            atts = new AttributesImpl();
        }
        if (qualifiedName != null) {
            String localName = null;
            if (qualifiedName.contains(":")) {
                localName = qualifiedName.split(":")[1];
            }
            handler.startElement(qn.getNamespaceURI(), localName, qualifiedName, atts);
        } else {
            handler.startElement(qn.getNamespaceURI(), qn.getLocalPart(), null, atts);
        }
    }

    private String qualify(String uri, String localName, String qName) {
        if (qName == null) {
            String prefix = namespaces.getPrefix(uri);
            if (prefix == null) {
                return localName;
            } else {
                return prefix + ":" + localName;
            }
        }
        return qName;
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String,
     *     java.lang.String)
     */
    public void endElement(QualifiedName qn) throws SAXException {
        String qualifiedName = qn.getQualifiedName();
        if (qualifiedName == null) {
            qualifiedName = qualify(qn.getNamespaceURI(), qn.getLocalPart(), null);
        }
        if (qualifiedName != null) {
            handler.endElement(null, null, qualifiedName);
        } else {
            handler.endElement(qn.getNamespaceURI(), qn.getLocalPart(), null);
        }
    }

    /** @see org.xml.sax.ContentHandler#characters(char[], int, int) */
    private void characters(char[] ch, int start, int length) throws SAXException {
        handler.characters(ch, start, length);
    }

    void characters(StringBuffer sb) throws SAXException {
        int length = sb.length();
        if (buffer == null || buffer.length < length) {
            buffer = new char[length];
        }
        sb.getChars(0, length, buffer, 0);
        characters(buffer, 0, length);
    }

    void characters(String s) throws SAXException {
        s = XMLUtils.removeXMLInvalidChars(s);
        int length = s.length();
        if (buffer == null || buffer.length < length) {
            buffer = new char[length];
        }
        s.getChars(0, length, buffer, 0);
        characters(buffer, 0, length);
    }

    /** Writes a GML2 coordinates element */
    public void coordinates(CoordinateSequence cs) throws SAXException {
        startElement(coordinates, null);
        coordinates(cs, ',', ' ', sb);
        characters(sb);
        endElement(coordinates);
    }

    /** Writes a single x/y position, without wrapping it in any element */
    public void position(double x, double y, double z) throws SAXException {
        position(x, y, z, sb);
        characters(sb);
    }

    void position(double x, double y, double z, StringBuffer sb) {
        sb.setLength(0);
        coordFormatter.format(x, sb);
        if (!Double.isNaN(y)) {
            sb.append(" ");
            coordFormatter.format(y, sb);
        }
        if (!Double.isNaN(z)) {
            sb.append(" ");
            coordFormatter.format(z, sb);
        }
    }

    void positions(CoordinateSequence coordinates) {
        coordinates(coordinates, ' ', ' ', sb);
    }

    /**
     * Encodes the provided coordinates sequence, if encoding of measures is enabled this method
     * will encode all the available ordinates.
     *
     * @param coordinates the coordinates sequence
     */
    public void position(CoordinateSequence coordinates) throws SAXException {
        coordinates(coordinates, ' ', ' ', sb);
        characters(sb);
    }

    void coordinates(CoordinateSequence coordinates, char cs, char ts, StringBuffer sb) {
        sb.setLength(0);
        int n = coordinates.size();
        int dim = CoordinateSequences.coordinateDimension(coordinates);
        for (int i = 0; i < n; i++) {
            coordFormatter.format(coordinates.getX(i), sb).append(cs);
            coordFormatter.format(coordinates.getY(i), sb);
            if (dim > 2) {
                int totalDimensions = encodeMeasures ? dim : dim - coordinates.getMeasures();
                // encoding the remaining ordinates, typically Z and M values
                for (int j = 2; j < totalDimensions; j++) {
                    sb.append(cs);
                    coordFormatter.format(coordinates.getOrdinate(i, j), sb);
                }
            }
            sb.append(ts);
        }
        sb.setLength(sb.length() - 1);
    }

    /** Writes a single ordinate, without wrapping it inside any element */
    public void ordinate(double x) throws SAXException {
        sb.setLength(0);
        coordFormatter.format(x, sb);
        characters(sb);
    }

    /** Write a GML3 posList */
    public void posList(CoordinateSequence coordinateSequence) throws SAXException {
        startElement(posList, null);
        positions(coordinateSequence);
        characters(sb);
        endElement(posList);
    }
}
