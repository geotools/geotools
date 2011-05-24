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

// OpenGIS direct dependencies


/**
 * Capabilities used to convey supported spatial operators.
 * <p>
 *   <pre>
 *  &lt;xsd:complexType name="Spatial_CapabilitiesType">
 *     &lt;xsd:sequence>
 *        &lt;xsd:element name="GeometryOperands"
 *                    type="ogc:GeometryOperandsType"/>
 *        &lt;xsd:element name="SpatialOperators"
 *                     type="ogc:SpatialOperatorsType"/>
 *     &lt;/xsd:sequence>
 *  &lt;/xsd:complexType>
 *   </pre>
 * </p>
 *
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </A>
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/capability/SpatialCapabilities.java $
 */
public interface SpatialCapabilities {
    /**
     * Supported geometry operands.
     * <p>
     * <pre>
     * &lt;xsd:element name="GeometryOperands" type="ogc:GeometryOperandsType"/>
     * </pre>
     * </p>
     */
    Collection<GeometryOperand> getGeometryOperands();

    /**
     * Supported spatial operators.
     * <p>
     * <pre>
     * &lt;xsd:element name="SpatialOperators" type="ogc:SpatialOperatorsType"/>
     * </pre>
     * </p>
     */
    SpatialOperators getSpatialOperators();
}
