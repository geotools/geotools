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

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.geotools.mbstyle.MBFormatException;
import org.geotools.mbstyle.MBStyle;


/**
 * Given JSON input (as a {@link String} or {@link Reader}, parses and returns a {@link MBStyle}
 *
 * @author tbarsballe
 */
public class MBStyleParser {

    JSONParser parser;

    public MBStyleParser() {
        parser = new JSONParser();
    }

    public MBStyle parse(String json) throws ParseException, MBFormatException {
        return MBObjectParser.parseRoot(parser.parse(json));
    }

    public MBStyle parse(Reader json) throws ParseException, IOException, MBFormatException {
        return MBObjectParser.parseRoot(parser.parse(json));
    }

    public MBStyle parse(InputStream json) throws ParseException, IOException, MBFormatException {
        return MBObjectParser.parseRoot(parser.parse(new InputStreamReader(json)));
    }
}
