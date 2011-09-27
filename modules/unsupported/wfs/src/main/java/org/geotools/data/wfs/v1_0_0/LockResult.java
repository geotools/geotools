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

import org.opengis.filter.Id;


/**
 * Result of locking features (listing both locked and unlocked features).
 *
 * @author dzwiers
 *
 *
 *
 * @source $URL$
 */
public class LockResult {
    protected String lockId;
    protected Id supported;
    protected Id notSupported;

    @SuppressWarnings("unused")
    private LockResult() {
        // should not be used
    }

    /**
     * 
     * @param lockId
     * @param supported
     * @param notSupported
     */
    public LockResult(String lockId, Id supported, Id notSupported) {
        this.lockId = lockId;
        this.supported = supported;
        this.notSupported = notSupported;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the lockId.
     */
    public String getLockId() {
        return lockId;
    }

    /**
     * Filter of all the features that could not be locked.
     *
     * @return Returns the notSupported.
     */
    public Id getNotSupported() {
        return notSupported;
    }

    /**
     * Filter of all the features that were locked.
     *
     * @return Returns the supported.
     */
    public Id getSupported() {
        return supported;
    }
}
