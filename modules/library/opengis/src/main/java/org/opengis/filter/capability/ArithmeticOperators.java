/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.capability;

/**
 * Supported arithmetic operators in a filter capabilities document.
 *
 * <p>
 *
 * <pre>
 * &lt;xsd:complexType name="ArithmeticOperatorsType"&gt;
 *     &lt;xsd:choice maxOccurs="unbounded"&gt;
 *        &lt;xsd:element ref="ogc:SimpleArithmetic"/&gt;
 *        &lt;xsd:element name="Functions" type="ogc:FunctionsType"/&gt;
 *     &lt;/xsd:choice&gt;
 *  &lt;/xsd:complexType&gt;
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface ArithmeticOperators {

    /**
     * Indicates if simple arithmetic is provided.
     *
     * <p>
     *
     * <pre>
     * &lt;xsd:element ref="ogc:SimpleArithmetic"/&gt;
     * </pre>
     */
    boolean hasSimpleArithmetic();

    /**
     * Provided functions.
     *
     * <p>
     *
     * <pre>
     * &lt;xsd:element name="Functions" type="ogc:FunctionsType"/&gt;
     * </pre>
     */
    Functions getFunctions();
}
