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

import java.io.IOException;
import java.net.URL;

import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;

public class GetCapabilitiesRequest extends AbstractGetCapabilitiesRequest {

    public GetCapabilitiesRequest(URL serverURL) {
        super(serverURL);
    }

    @Override
    protected void initService() {
        setProperty(Request.SERVICE, "WFS");
    }

    @Override
    protected void initRequest() {
        super.initRequest();
    }

    @Override
    protected void initVersion() {
        // do nothing, wfsStrategy is not set yet, this method is called by the super constructor
    }

    @Override
    public Response createResponse(HTTPResponse response) throws ServiceException, IOException {
        return new GetCapabilitiesResponse(response);
    }

}
