/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    (C) 2001-2004 EXSE, Department of Geography, University of Bonn
 *                  lat/lon Fitzke/Fretter/Poth GbR
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.opengis.filter.capability;

// Annotations
import static org.opengis.annotation.Specification.UNSPECIFIED;

import org.opengis.annotation.UML;


/**
 * Capabilities used to convey supported scalar operators.
 * <p>
 * <pre>
 * &lt;xsd:complexType name="Scalar_CapabilitiesType">
      &lt;xsd:sequence>
         &lt;xsd:element ref="ogc:LogicalOperators"
                      minOccurs="0" maxOccurs="1"/>
         &lt;xsd:element name="ComparisonOperators"
                      type="ogc:ComparisonOperatorsType"
                      minOccurs="0" maxOccurs="1"/>
                      minOccurs="0" maxOccurs="1"/>
      &lt;/xsd:sequence>
   &lt;/xsd:complexType>
 * </pre>
 * </p>
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/capability/ScalarCapabilities.java $
 */
public interface ScalarCapabilities {
    /**
     * Indicates if logical operator support is provided.
     * <p>
     * <pre>
     * &lt;xsd:element ref="ogc:LogicalOperators" minOccurs="0" maxOccurs="1"/>
     * </pre>
     * </p>
     */
    boolean hasLogicalOperators();

    /**
     * Provided comparison operators.
     * <p>
     * <pre>
     * &lt;xsd:element name="ComparisonOperators" type="ogc:ComparisonOperatorsType"
     *    minOccurs="0" maxOccurs="1"/>
     * </pre>
     * </p>
     */
    @UML(identifier="comparisonOperators", specification=UNSPECIFIED)
    ComparisonOperators getComparisonOperators();

    /**
     * Provided arithmetic operators.
     * <p>
     * <pre>
     * &lt;xsd:element name="ComparisonOperators" type="ogc:ComparisonOperatorsType"
     *     minOccurs="0" maxOccurs="1"/>
     * </pre>
     * </p>
     */
    @UML(identifier="arithmeticOperators", specification=UNSPECIFIED)
    ArithmeticOperators getArithmeticOperators();

}
