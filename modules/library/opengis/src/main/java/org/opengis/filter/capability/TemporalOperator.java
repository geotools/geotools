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

import java.util.Collection;

import org.opengis.feature.type.Name;

/**
 * A named temporal operator.
 * <p>
 * <pre>
 *   &lt;xsd:complexType name="TemporalOperandsType">
 *    &lt;xsd:sequence>
 *         &lt;xsd:element name="TemporalOperand" maxOccurs="unbounded">
 *            &lt;xsd:complexType>
 *              &lt;xsd:attribute name="name" type="xsd:QName" use="required"/>
 *           &lt;/xsd:complexType>
 *        &lt;/xsd:element>
 *     &lt;/xsd:sequence>
 *  &lt;/xsd:complexType>
 *    &lt;xsd:complexType name="TemporalOperatorType">
 *     &lt;xsd:sequence>
 *        &lt;xsd:element name="TemporalOperands"
 *                     type="fes:TemporalOperandsType"
 *                     minOccurs="0"/>
 *     &lt;/xsd:sequence>
 *     &lt;xsd:attribute name="name"
 *                    type="fes:TemporalOperatorNameType" use="required"/>
 *  &lt;/xsd:complexType>
 * </pre>
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 * @source $URL$
 */
public interface TemporalOperator extends Operator {

    /**
     * The operands accepted by this temporal operator. 
     */
    Collection<Name> getTemporalOperands();
}
