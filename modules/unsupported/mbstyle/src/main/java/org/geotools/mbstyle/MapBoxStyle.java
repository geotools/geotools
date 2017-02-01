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
package org.geotools.mbstyle;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.geotools.styling.StyledLayerDescriptor;
import org.json.simple.parser.ParseException;

/**
 * MapBox Style facade offering utility methods for quickly working with JSON and converting to
 * {@link StyledLayerDescriptor}.
 * 
 * @author Jody Garnett (Boundless)
 */
public class MapBoxStyle {

    /**
     * Read in the provided JSON as a {@link StyledLayerDescriptor}.
     * 
     * @param reader
     * @return geneated style
     */
    static StyledLayerDescriptor parse(Reader reader) throws IOException, ParseException {
        return null;
    }

    /**
     * Read in the provided JSON as a {@link StyledLayerDescriptor}.
     * 
     * @param stream
     * @return geneated style
     */
    static StyledLayerDescriptor parse(InputStream stream) throws IOException, ParseException {
        return null;
    }
}
