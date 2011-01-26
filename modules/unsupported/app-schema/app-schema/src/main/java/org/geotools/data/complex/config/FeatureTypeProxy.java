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

import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.4.x
 * @URL $URL:
 *      http://svn.geotools.org/trunk/modules/unsupported/community-schemas/community-schema-ds/src/main/java/org/geotools/data/complex/config/FeatureTypeProxy.java $
 */
class FeatureTypeProxy extends ComplexTypeProxy implements FeatureType {

    public FeatureTypeProxy(final Name typeName, final Map registry) {
        super(typeName, registry);
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return ((FeatureType) getSubject()).getCoordinateReferenceSystem();
    }

    public GeometryDescriptor getGeometryDescriptor() {
        return ((FeatureType) getSubject()).getGeometryDescriptor();
    }

}
