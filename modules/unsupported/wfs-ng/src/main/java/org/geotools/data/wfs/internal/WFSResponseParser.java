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

/**
 * A simple interface to parse the response of a WFS request.
 * 
 * @see WFSResponseFactory
 * @see WFSResponse
 */
public interface WFSResponseParser {

    /**
     * Produces a parsed object from the WFS response stream.
     * <p>
     * The kind of parsed object depends on the actual response contents and what the specific
     * parser produces for it.
     * </p>
     * 
     * @param response
     *            the handle to the WFS response contents
     * @return the implementation dependent object that's parsed from the WFS response contents
     * @throws IOException
     *             if an exception is produced while parsing the response
     */
    Object parse(WFSResponse response, String axisOrder) throws IOException;

}
