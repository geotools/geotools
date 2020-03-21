/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;
import java.util.Map;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.XSD;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Interface factoring out the differences between GML versions
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
public interface GMLDelegate {

    /**
     * Lists all properties that should be encoded for a given feature
     *
     * @param f A sample feature
     * @param element The xml element holding the feature type
     * @param e The encoder
     */
    List getFeatureProperties(SimpleFeature f, XSDElementDeclaration element, Encoder e);

    /** Creates the envelope encoder */
    ObjectEncoder createEnvelopeEncoder(Encoder e);

    /**
     * Registers all the geometry encoders for this GML version in a map, by geometry class
     * (different versions support different types of geometries, e.g., GML3 supports also curved
     * ones)
     */
    void registerGeometryEncoders(Map<Class, GeometryEncoder> encoders, Encoder encoder);

    /** Sets the SRS attribute with the proper syntax for the given GML version */
    void setSrsNameAttribute(AttributesImpl atts, CoordinateReferenceSystem crs);

    /** Sets the dimensions attribute, if available for the current GML version */
    void setGeometryDimensionAttribute(AttributesImpl srsatts, int dimension);

    /** Initializes an empty feature id attribute, the attribute must be the first one in "atts" */
    void initFidAttribute(AttributesImpl atts);

    /** Writes whatever per collection preamble is needed in this GML version */
    void startFeatures(GMLWriter handler) throws Exception;

    /** Writes whatever per feature preamble is needed in this GML version */
    void startFeature(GMLWriter handler) throws Exception;

    /** Closes a collection of features */
    void endFeatures(GMLWriter handler) throws Exception;

    /** Closes a single feature */
    void endFeature(GMLWriter handler) throws Exception;

    /** The GML prefix used by this encoding session */
    String getGmlPrefix() throws Exception;

    /** Returns true if tuple encoding is supported in this standard */
    boolean supportsTuples();

    /** Writes the tuple preamble */
    void startTuple(GMLWriter handler) throws SAXException;

    /** Closes a tuple */
    void endTuple(GMLWriter handler) throws SAXException;

    /** The XSD schema used by this GML version */
    XSD getSchema();

    /** Number of decimals used in the output */
    int getNumDecimals();

    /** Returns true if coordinates should be encoded as xs:decimal instead of xs:double */
    boolean forceDecimalEncoding();

    /**
     * Returns true if coordinates should be right-padded with zeros up to the requested number of
     * decimals.
     */
    boolean padWithZeros();

    /**
     * Controls if coordinates measures should be included in WFS outputs.
     *
     * @return TRUE if measures should be encoded, otherwise FALSE
     */
    default boolean getEncodeMeasures() {
        // by default coordinates measures are encoded
        return true;
    }
}
