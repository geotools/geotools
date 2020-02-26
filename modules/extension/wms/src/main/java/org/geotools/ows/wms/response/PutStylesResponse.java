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
package org.geotools.ows.wms.response;

import java.io.IOException;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;

/**
 * Represents the results of a PutStyles request.
 *
 * <p>Success can be checked using the success() method.
 *
 * @author Richard Gould
 */
public class PutStylesResponse extends Response {

    private boolean success;

    /** */
    public PutStylesResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
        super(httpResponse);

        if ("application/vnd.ogc.success+xml".equals(getContentType())) {
            success = true;
        }
    }

    /** @return true if the request successfully executed, false otherwise */
    public boolean success() {
        return success;
    }
}
