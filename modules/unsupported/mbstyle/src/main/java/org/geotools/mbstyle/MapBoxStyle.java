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

import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBStyleParser;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.StyledLayerDescriptor;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
     * @return generated style
     */
    public static StyledLayerDescriptor parse(Reader reader) throws IOException, ParseException {
        MBStyleParser parser = new MBStyleParser();
        MBStyle style = parser.parse(reader);

        StyledLayerDescriptor sld = style.transform();
        return sld;
    }

    /**
     * Read in the provided JSON as a {@link StyledLayerDescriptor}.
     * 
     * @param stream
     * @return generated style
     */
    public static StyledLayerDescriptor parse(InputStream stream) throws IOException, ParseException {
        MBStyleParser parser = new MBStyleParser();
        MBStyle style = parser.parse(stream);

        StyledLayerDescriptor sld = style.transform();
        return sld;
    }
    
    /**
     * Validate ability to read json, and parse each layer.
     * @param reader Reader for reading the style
     * @return List of parse exceptions found. If this list is empty, the style is valid.
     */
    public static List<Exception> validate(Reader reader) {
        List<Exception> problems = new ArrayList<Exception>();
        MBStyleParser parser = new MBStyleParser();
        MBStyle style;
        try {
            style = parser.parse(reader);
        } catch (Exception invalid) {
            problems.add( invalid );
            return problems;
        }
        if (style.layers().isEmpty()) {
            problems.add( new MBFormatException("No layers defined"));
        } else {
            boolean hasVisibleLayer = false;
            for (MBLayer layer : style.layers()) {
                if (!layer.visibility()) {
                    continue;
                }
                try {
                    FeatureTypeStyle featureTypeStyle = layer.transform(style);
                    if (featureTypeStyle != null) {
                        hasVisibleLayer = true;
                    }
                } catch (Exception invalid) {
                    problems.add((MBFormatException) new MBFormatException(
                            "Layer " + layer.getId() + ":" + invalid.getMessage()).initCause(invalid));
                    return problems;
                }
            }
            if (!hasVisibleLayer) {
                problems.add( new MBFormatException("No layers were visible"));
            }
        }
        return problems;
    }
}
