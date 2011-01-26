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

// J2SE direct dependencies


/**
 * Capabilities used to convey supported id operators.
 * <p>
 * <pre>
 *  &lt;xsd:complexType name="Id_CapabilitiesType">
 *     &lt;xsd:choice maxOccurs="unbounded">
 *       &lt;xsd:element ref="ogc:EID"/>
 *       &lt;xsd:element ref="ogc:FID"/>
 *     &lt;/xsd:choice>
 *  &lt;/xsd:complexType>
 *  &lt;xsd:element name="EID">
 *     &lt;xsd:complexType/>
 *  &lt;/xsd:element>
 *  &lt;xsd:element name="FID">
 *     &lt;xsd:complexType/>
 *  &lt;/xsd:element>
 * </pre>
 * </p>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface IdCapabilities {

    /**
     * Indicates if the capabilities support EID.
     */
    boolean hasEID();

    /**
     * Indicates if the capabilities support FID.
     * @return
     */
    boolean hasFID();
}
