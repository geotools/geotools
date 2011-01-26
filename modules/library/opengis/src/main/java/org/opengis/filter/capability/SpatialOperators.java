/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
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

/**
 * Supported spatial operators in a filter capabilities document.
 * <p>
 * <pre>
 *  &lt;xsd:complexType name="Spatial_OperatorsType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:BBOX"/&gt;
 *          &lt;xsd:element ref="ogc:Equals"/&gt;
 *          &lt;xsd:element ref="ogc:Disjoint"/&gt;
 *          &lt;xsd:element ref="ogc:Intersect"/&gt;
 *          &lt;xsd:element ref="ogc:Touches"/&gt;
 *          &lt;xsd:element ref="ogc:Crosses"/&gt;
 *          &lt;xsd:element ref="ogc:Within"/&gt;
 *          &lt;xsd:element ref="ogc:Contains"/&gt;
 *          &lt;xsd:element ref="ogc:Overlaps"/&gt;
 *          &lt;xsd:element ref="ogc:Beyond"/&gt;
 *          &lt;xsd:element ref="ogc:DWithin"/&gt;
 *      &lt;/xsd:choice&gt;
 *  &lt;/xsd:complexType&gt;
 * </pre>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface SpatialOperators {

    /**
     * Provided spatial operators.
     */
    Collection<SpatialOperator> getOperators();

    /**
     * Looks up an operator by name, returning null if no such operator found.
     *
     * @param name the name of the operator.
     *
     * @return The operator, or null.
     */
    SpatialOperator getOperator( String name );
}
