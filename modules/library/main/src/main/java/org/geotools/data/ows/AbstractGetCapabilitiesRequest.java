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
package org.geotools.data.ows;

import java.net.URL;

/**
 * Each Open Web Service typically defines an operation that describes what 
 * operations it supports and what data it holds. The document describing this
 * information is usually called the Capabilities document, and is usually
 * accessed using the GetCapabilities operation.
 * 
 * This class provides a basic building block for clients to implement their
 * GetCapabilitiesRequest. It automatically sets the REQUEST parameter to
 * "GetCapabilities".
 *
 * @author rgould
 * @source $URL$
 */
public abstract class AbstractGetCapabilitiesRequest extends AbstractRequest implements GetCapabilitiesRequest{
    /** Represents the SERVICE parameter */
    public static final String SERVICE = "SERVICE"; //$NON-NLS-1$

    public AbstractGetCapabilitiesRequest(URL serverURL) {
        super(serverURL, null);
    }

    /**
     * Sets the REQUEST parameter
     * <p>
     * Subclass can override if needed.
     * </p>
     */
    protected void initRequest() {
        setProperty(REQUEST, "GetCapabilities"); //$NON-NLS-1$
    }
}
