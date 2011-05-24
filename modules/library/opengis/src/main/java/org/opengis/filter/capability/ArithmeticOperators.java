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
 * <p>
 * <pre>
 * &lt;xsd:complexType name="ArithmeticOperatorsType">
 *     &lt;xsd:choice maxOccurs="unbounded">
 *        &lt;xsd:element ref="ogc:SimpleArithmetic"/>
 *        &lt;xsd:element name="Functions" type="ogc:FunctionsType"/>
 *     &lt;/xsd:choice>
 *  &lt;/xsd:complexType>
 * </pre>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/capability/ArithmeticOperators.java $
 */
public interface ArithmeticOperators {

    /**
     * Indicates if simple arithmetic is provided.
     * <p>
     * <pre>
     * &lt;xsd:element ref="ogc:SimpleArithmetic"/>
     * </pre>
     * </p>
     */
    boolean hasSimpleArithmetic();

    /**
     * Provided functions.
     * <p>
     * <pre>
     * &lt;xsd:element name="Functions" type="ogc:FunctionsType"/>
     * </pre>
     * </p>
     */
    Functions getFunctions();
}
