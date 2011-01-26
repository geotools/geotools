/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;


/**
 * Indicates a lock contention, and attempt was made to modify or aquire with
 * out Authroization.
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public class FeatureLockException extends IOException {
    private static final long serialVersionUID = 1L;
    String featureID;

    public FeatureLockException() {
        super();
        featureID = null;
    }

    public FeatureLockException(String message) {
        super(message);
        featureID = null;
    }

    public FeatureLockException(String message, String featureID) {
        super(message);
        this.featureID = featureID;
    }

    public FeatureLockException(String message, Throwable t) {
        this(message, null, t);
    }

    public FeatureLockException(String message, String featureID, Throwable t) {
        super(message);
        initCause(t);
        this.featureID = featureID;
    }

    /**
     * Query FeatureID the LockException was recorded against, if known.
     *
     * @return FeatureID or <code>null</code> if unknown.
     */
    public String getFeatureID() {
        return featureID;
    }
}
