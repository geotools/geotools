/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.response;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.model.WMTSServiceType;

/** @author ian */
public class GetTileResponse extends Response {
    private WMTSServiceType type;

    private URL requestURL;

    public GetTileResponse(HTTPResponse httpResponse, WMTSServiceType wmtsServiceType)
            throws ServiceException, IOException {
        super(httpResponse);
        this.setType(wmtsServiceType);
    }

    /** @return the type */
    public WMTSServiceType getType() {
        return type;
    }

    /** @param type the type to set */
    public void setType(WMTSServiceType type) {
        this.type = type;
    }

    /** @return the requestURL */
    public URL getRequestURL() {
        return requestURL;
    }

    /** @param requestURL the requestURL to set */
    public void setRequestURL(URL requestURL) {
        this.requestURL = requestURL;
    }
}
