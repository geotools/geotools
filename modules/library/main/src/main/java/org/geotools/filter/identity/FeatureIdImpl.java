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

import org.geotools.api.feature.Feature;
import org.geotools.api.filter.identity.FeatureId;

/**
 * Implementation of {@link org.geotools.api.filter.identity.FeatureId}
 *
 * <p>This class is mutable under one condition only; during a commit a datastore can update the internal fid to reflect
 * the real identify assigned by the database or wfs.
 *
 * <p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 * @version 8.0
 */
public class FeatureIdImpl implements FeatureId {

    /** underlying fid */
    protected String fid;

    protected String origionalFid;

    public FeatureIdImpl(String fid) {
        this.fid = fid;
        if (fid == null) {
            throw new NullPointerException("fid must not be null");
        }
    }

    @Override
    public String getID() {
        return fid;
    }

    public void setID(String id) {
        if (id == null) {
            throw new NullPointerException("fid must not be null");
        }
        if (origionalFid == null) {
            origionalFid = fid;
        }
        fid = id;
    }

    public boolean matches(Feature feature) {
        if (feature == null) {
            return false;
        }
        return equalsExact(feature.getIdentifier());
    }

    @Override
    public boolean matches(Object object) {
        if (object instanceof Feature) {
            return matches((Feature) object);
        }
        return false;
    }

    @Override
    public String toString() {
        return fid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FeatureId) {
            return fid.equals(((FeatureId) obj).getID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return fid.hashCode();
    }

    @Override
    public boolean equalsExact(FeatureId id) {
        if (id != null) {
            return fid.equals(id.getID())
                    && fid.equals(id.getRid())
                    && id.getPreviousRid() == null
                    && id.getFeatureVersion() == null;
        }
        return false;
    }

    @Override
    public boolean equalsFID(FeatureId id) {
        if (id == null) return false;

        return getID().equals(id.getID());
    }

    @Override
    public String getRid() {
        return getID();
    }

    @Override
    public String getPreviousRid() {
        return null;
    }

    @Override
    public String getFeatureVersion() {
        return null;
    }
}
