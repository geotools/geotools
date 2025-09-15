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
package org.geotools.filter.identity;

import org.geotools.api.filter.identity.GmlObjectId;

/**
 * Implementation of {@link org.geotools.api.filter.identity.GmlObjectId}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class GmlObjectIdImpl implements GmlObjectId {

    /** the object id */
    String gmlId;

    public GmlObjectIdImpl(String gmlId) {
        this.gmlId = gmlId;
        if (gmlId == null) {
            throw new NullPointerException("id can not be null");
        }
    }

    @Override
    public String getID() {
        return gmlId;
    }

    @Override
    public boolean matches(Object object) {
        if (object instanceof org.geotools.api.feature.Feature feature) {
            return new FeatureIdImpl(gmlId).matches(feature);
        }

        // TODO: geometries
        return false;
    }

    @Override
    public String toString() {
        return gmlId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GmlObjectIdImpl other) {
            return gmlId.equals(other.gmlId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return gmlId.hashCode();
    }
}
