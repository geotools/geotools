/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    (C) 2001 EXSE, Department of Geography, University of Bonn
 *             lat/lon Fitzke/Fretter/Poth GbR
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
import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * FilterCapabilitiesBean used to represent
 * <code>Filter<code> expressions according to the
 * 1.0.0 as well as the 1.1.1 <code>Filter Encoding Implementation Specification</code>.
 *
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 */
public interface FilterCapabilities {

	/** Version String for Filter 1.0 specification */
    public String VERSION_100 = "1.0.0";
	/** Version String for Filter 1.1 specification */
    public String VERSION_110 = "1.1.0";

    /**
     *
     */
    @UML(identifier="scalarCapabilities", specification=UNSPECIFIED)
    ScalarCapabilities getScalarCapabilities();

    /**
     *
     */
    @UML(identifier="spatialCapabilities", specification=UNSPECIFIED)
    SpatialCapabilities getSpatialCapabilities();

    /**
     *
     */
    @UML(identifier="idCapabilities", specification=UNSPECIFIED)
    IdCapabilities getIdCapabilities();

    /**
     * Returns the version.
     */
    String getVersion();
}
