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
public class FeatureIdVersionedImpl extends FeatureIdImpl {

    protected String featureVersion;
    protected String previousRid;

    public FeatureIdVersionedImpl(String fid, String version) {
        this(fid, version, null);
    }

    public FeatureIdVersionedImpl(String fid, String version, String previousRid) {
        super(fid);
        this.featureVersion = version;
        this.previousRid = previousRid;
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
        return featureVersion == null ? getID() : getID() + VERSION_SEPARATOR + featureVersion;
    }

    @Override
    public String getPreviousRid() {
        return previousRid;
    }

    @Override
    public String getFeatureVersion() {
        return featureVersion;
    }
}
