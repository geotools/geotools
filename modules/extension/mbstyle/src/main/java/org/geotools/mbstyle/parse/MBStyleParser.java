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
package org.geotools.mbstyle.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.geotools.mbstyle.MBStyle;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Given JSON input (as a {@link String} or {@link Reader}, parses and returns a {@link MBStyle}.
 *
 * @author Torben Barsballe (Boundless)
 */
public class MBStyleParser {

    JSONParser jsonParser;

    public MBStyleParser() {
        jsonParser = new JSONParser();
    }

    /**
     * Parse the provided json into MBStyle.
     *
     * <p>Please be aware that {@link MBStyle}.is a thin wrapper around the provided json and will
     * lazily parse map box style contents as required.
     *
     * @param json String
     * @return MBStyle
     * @throws ParseException If JSON is not well formed
     * @throws MBFormatException If MapBox Style is obviously not well formed
     */
    public MBStyle parse(String json) throws ParseException, MBFormatException {
        return MBStyle.create(jsonParser.parse(json));
    }

    /**
     * Parse the provided json into MBStyle.
     *
     * <p>Please be aware that {@link MBStyle}.is a thin wrapper around the provided json and will
     * lazily parse map box style contents as required.
     *
     * @param json Reader
     * @return MBStyle
     * @throws ParseException If JSON is not well formed
     * @throws IOException If json reader cannot be read
     * @throws MBFormatException If MapBox Style is obviously not well formed
     */
    public MBStyle parse(Reader json) throws ParseException, IOException, MBFormatException {
        try {
            return MBStyle.create(jsonParser.parse(json));
        } finally {
            json.close();
        }
    }

    /**
     * Parse the provided json into MBStyle.
     *
     * <p>Please be aware that {@link MBStyle}.is a thin wrapper around the provided json and will
     * lazily parse map box style contents as required.
     *
     * @param json InputStream
     * @return MBStyle
     * @throws ParseException If JSON is not well formed
     * @throws IOException If json input stream cannot be read
     * @throws MBFormatException If MapBox Style is obviously not well formed
     */
    public MBStyle parse(InputStream json) throws ParseException, IOException, MBFormatException {
        try (Reader reader = new InputStreamReader(json)) { // auto close
            Object obj = jsonParser.parse(reader);
            return MBStyle.create(obj);
        }
    }
}
