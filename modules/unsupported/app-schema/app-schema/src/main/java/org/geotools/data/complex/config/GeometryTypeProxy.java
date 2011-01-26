/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.util.Map;

import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Gabriel Roldan
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
class GeometryTypeProxy extends AttributeTypeProxy implements GeometryType {

    public GeometryTypeProxy(final Name typeName, final Map registry) {
        super(typeName, registry);
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return ((GeometryType) getSubject()).getCoordinateReferenceSystem();
    }

}
