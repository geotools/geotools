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
package org.geotools.data.wfs.v1_0_0;

import java.util.Map;

import org.geotools.data.FeatureLock;
import org.geotools.filter.Filter;


/**
 * DOCUMENT ME!
 *
 * @author dzwiers
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/v1_0_0/LockRequest.java $
 */
public class LockRequest implements FeatureLock {
    private long duration = 0;
    private String[] types = null;
    private Filter[] filters = null;
    private String lockId = null;

    private LockRequest() {
        // should not be used
    }

    protected LockRequest(long duration, Map dataSets) {
        this.duration = duration;
        types = (String[]) dataSets.keySet().toArray(new String[dataSets.size()]);
        filters = new Filter[types.length];

        for (int i = 0; i < types.length; i++)
            filters[i] = (Filter) dataSets.get(types[i]);
    }

    protected LockRequest(long duration, String[] types, Filter[] filters) {
        this.duration = duration;
        this.types = types;
        this.filters = filters;
    }

    /**
     * 
     * @see org.geotools.data.FeatureLock#getAuthorization()
     */
    public String getAuthorization() {
        return lockId;
    }

    protected void setAuthorization(String auth) {
        lockId = auth;
    }

    /**
     * 
     * @see org.geotools.data.FeatureLock#getDuration()
     */
    public long getDuration() {
        return duration;
    }

    /**
     * 
     * @return Type Names
     */
    public String[] getTypeNames() {
        return types;
    }

    /**
     * 
     * @return Filters
     */
    public Filter[] getFilters() {
        return filters;
    }
}
