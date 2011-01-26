/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.type;

import java.util.List;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * AttributeType for hold geometry implementations, maintains CRS information.
 *
 * @source $URL$
 */
public class GeometryTypeImpl extends AttributeTypeImpl implements GeometryType {

	protected CoordinateReferenceSystem CRS;

	public GeometryTypeImpl(
		Name name, Class binding, CoordinateReferenceSystem crs, 
		boolean identified, boolean isAbstract, List<Filter> restrictions, 
		AttributeType superType, InternationalString description
	) {
		super(name, binding, identified, isAbstract, restrictions, superType, description);
		CRS = crs;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return CRS;
	}

}
