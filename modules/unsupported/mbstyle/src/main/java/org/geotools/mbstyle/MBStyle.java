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

import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * MapBox Style implemented as wrapper around parsed JSON file.
 * <p>
 * This class is responsible for presenting the wrapped JSON in an easy to use / navigate form for
 * Java developers. Access methods should return Java Objects, rather than generic maps. Additional
 * access methods to perform common queries are expected and encouraged.
 * </p>
 * <p>
 * This class works closely with {@link MBLayer} hierarchy used to represent the fill, line, symbol,
 * raster, circle layers. Additional support will be required to work with sprties and glyphs.
 * </p>
 * 
 * @author Jody Garnett (Boundless)
 */
public class MBStyle {
    JSONObject json;

    /**
     * MBStyle wrapper on the provided json
     *
     * @param json Map Box Style as parsed JSON
     */
    public MBStyle(JSONObject json) {
        this.json = json;
    }

    /**
     * Access layers matching provided source.
     * 
     * @param source
     * @return list of layers matching provided source
     */
    public List<MBStyle> layers(String source) {
        return Collections.emptyList();
    }

    /**
     * Access layers matching provided source and selector.
     * 
     * @param source
     * @param selector
     * @return list of layers matching provided source
     */
    public List<MBStyle> layers(String source, String selector) {
        return Collections.emptyList();
    }
}
