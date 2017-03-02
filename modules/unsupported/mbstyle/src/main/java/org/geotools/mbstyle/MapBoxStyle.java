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
import java.util.ArrayList;
import java.util.List;

import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBStyleParser;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.styling.FeatureTypeStyle;
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
    public static StyledLayerDescriptor parse(Reader reader) throws IOException, ParseException {
        MBStyleParser parser = new MBStyleParser();
        MBStyle style = parser.parse(reader);

        MBStyleTransformer transform = new MBStyleTransformer();

        StyledLayerDescriptor sld = transform.tranform(style);
        return sld;
    }

    /**
     * Read in the provided JSON as a {@link StyledLayerDescriptor}.
     * 
     * @param stream
     * @return geneated style
     */
    public static StyledLayerDescriptor parse(InputStream stream) throws IOException, ParseException {
        MBStyleParser parser = new MBStyleParser();
        MBStyle style = parser.parse(stream);

        MBStyleTransformer transform = new MBStyleTransformer();

        StyledLayerDescriptor sld = transform.tranform(style);
        return sld;
    }
    
    /**
     * Validate ability to read json, and parse each layer.
     * @param reader
     * @return 
     * @throws IOException
     * @throws ParseException
     */
    public static List<Exception> validate(Reader reader) {
        List<Exception> problems = new ArrayList<Exception>();
        MBStyleParser parser = new MBStyleParser();
        MBStyle style;
        try {
            style = parser.parse(reader);
        }
        catch (Exception invalid){
            problems.add( invalid );
            return problems;
        }
        MBStyleTransformer transform = new MBStyleTransformer();
        if( style.layers().isEmpty()){
            problems.add( new MBFormatException("No layers defined"));
        }
        else {
            boolean hasVisibleLayer = false;
            for (MBLayer layer : style.layers()) {
                if(!layer.visibility() ){
                    continue;
                }
                try {
                    FeatureTypeStyle featureTypeStyle = transform.transform(layer, style);
                    if( featureTypeStyle != null ){
                        hasVisibleLayer = true;
                    }
                }
                catch (Exception invalid){
                    problems.add((MBFormatException) new MBFormatException(
                            "Layer " + layer.getId() + ":" + invalid.getMessage()).initCause(invalid));
                    return problems;
                }
            }
            if( !hasVisibleLayer){
                problems.add( new MBFormatException("No layers were visible"));
            }
        }
        return problems;
    }
}
