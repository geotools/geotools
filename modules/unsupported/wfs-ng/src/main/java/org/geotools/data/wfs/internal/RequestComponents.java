/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import java.util.Collections;
import java.util.Map;

import net.opengis.wfs.GetFeatureType;

/**
 * Holds the components needed by the data store to issue and post process a GetFeature request.
 */
public class RequestComponents {

    /**
     * The GetFeature request to issue to the WFS
     */
    private GetFeatureType serverRequest;

    private Map<String, String> kvpParameters;

    public GetFeatureType getServerRequest() {
        return serverRequest;
    }

    public void setServerRequest(GetFeatureType serverRequest) {
        this.serverRequest = serverRequest;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getKvpParameters() {
        return kvpParameters == null ? Collections.EMPTY_MAP : kvpParameters;
    }

    public void setKvpParameters(Map<String, String> kvpParameters) {
        this.kvpParameters = kvpParameters;
    }
}
