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
package org.geotools.data.wfs.protocol.wfs;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;

/**
 * A factory interface meant to be used through the usual GeoTools SPI mechanism to dynamically find
 * out a parser capable of processing a WFS response based on the request to be sent.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: WFSResponseParserFactory.java 31888 2008-11-20 13:34:53Z groldan $
 * @since 2.6
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools/data/wfs/protocol/wfs/WFSResponseParserFactory.java $
 */
public interface WFSResponseParserFactory {

    /**
     * Indicates whether the factory instance is able to create parser instances.
     * 
     * @return {@code true} if there's nothing preventing the creation of the parsers this factory
     *         should produce
     */
    public boolean isAvailable();

    /**
     * Indicates whether this factory is able to produce a parser that deals with the possible
     * responses of the given WFS request.
     * <p>
     * The decision may usually be made depending on the request type, ouput format, etc
     * </p>
     * 
     * @param request
     * @return {@code true} if this factory can create a parser for the responses of the given
     *         request
     */
    public boolean canProcess(EObject request);

    /**
     * Creates a response parser for the given WFS response.
     * 
     * @param wfs
     *            the data store that issued the request that produced the given {@code response}
     * @param response the handle to the response contents the WFS sent
     * @return a {@link WFSResponseParser} that can deal with the given WFS response
     * @throws IOException
     */
    public WFSResponseParser createParser(WFS_1_1_0_DataStore wfs, WFSResponse response)
            throws IOException;

}
