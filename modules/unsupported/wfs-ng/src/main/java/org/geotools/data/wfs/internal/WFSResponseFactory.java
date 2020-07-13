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
package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.util.List;
import org.geotools.data.ows.HTTPResponse;

/**
 * A factory interface meant to be used through the usual GeoTools SPI mechanism to dynamically find
 * out a parser capable of processing a WFS response based on the request to be sent.
 */
public interface WFSResponseFactory {

    /**
     * Indicates whether the factory instance is able to create parser instances.
     *
     * @return {@code true} if there's nothing preventing the creation of the parsers this factory
     *     should produce
     */
    public boolean isAvailable();

    /**
     * Indicates whether this factory is able to produce a parser that deals with the possible
     * responses of the given WFS request.
     *
     * <p>The decision may usually be made depending on the request type, ouput format, etc
     *
     * @return {@code true} if this factory can create a parser for the responses of the given
     *     request
     */
    public boolean canProcess(WFSRequest originatingRequest, String contentType);

    /**
     * Creates a response parser for the given WFS response.
     *
     * @param response the handle to the response contents the WFS sent
     * @return a {@link WFSResponseParser} that can deal with the given WFS response
     */
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response) throws IOException;

    public boolean canProcess(WFSOperationType operation);

    public List<String> getSupportedOutputFormats();
}
