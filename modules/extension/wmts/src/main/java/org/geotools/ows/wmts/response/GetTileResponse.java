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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.image.io.ImageIOExt;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.model.WMTSServiceType;

/**
 * Represents the response of a tile request.
 *
 * <p>In most cases this is a image, but it can also be a file like kml with reference to an image.
 * In such cases the content-type / responseStream must be used. Error-situations is managed in the
 * same manner as other OWS responses.
 *
 * @author ian
 */
public class GetTileResponse extends Response {

    private WMTSServiceType type;

    private URL requestURL;

    private final BufferedImage tileImage;

    /**
     * Constructor of GetTileResponse. Reads the image if the content-type is set to image. Other
     * content-types should use the responseStream.
     */
    public GetTileResponse(HTTPResponse httpResponse, WMTSServiceType wmtsServiceType)
            throws ServiceException, IOException {
        super(httpResponse);
        this.setType(wmtsServiceType);

        String format = httpResponse.getContentType();
        if (format.startsWith("image")) {
            try {
                tileImage = ImageIOExt.readBufferedImage(httpResponse.getResponseStream());
            } finally {
                httpResponse.dispose();
            }
        } else {
            tileImage = null;
        }
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

    /** The tile image in cases where content-type is set to image. */
    public BufferedImage getTileImage() {
        return tileImage;
    }
}
