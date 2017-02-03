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

import java.util.ArrayList;
import java.util.List;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * MapBox Style implemented as wrapper around parsed JSON file.
 * <p>
 * This class is responsible for presenting the wrapped JSON in an easy to use / navigate form for
 * Java developers:
 * </p>
 * <ul>
 * <li>get methods: access the json directly</li>
 * <li>query methods: provide logic / transforms to GeoTools classes as required.</li>
 * </ul>
 * <p>
 * Access methods should return Java Objects, rather than generic maps. Additional access methods to
 * perform common queries are expected and encouraged.
 * </p>
 * <p>
 * This class works closely with {@link MBLayer} hierarchy used to represent the fill, line, symbol,
 * raster, circle layers. Additional support will be required to work with sprties and glyphs.
 * </p>
 * 
 * @author Jody Garnett (Boundless)
 */
public class MBStyle {
    
    /**
     * JSON document being wrapped by this class.
     * <p>
     * All methods act as accessors on this JSON document, no other state is maintained. This
     * allows modifications to be made cleaning with out chance of side-effect. 
     */
    JSONObject json;
    
    /** Helper class used to perform JSON travewrse json and
     * perform Expression and Filter conversions. */
    MBObjectParser parse = new MBObjectParser();

    /**
     * MBStyle wrapper on the provided json
     *
     * @param json Map Box Style as parsed JSON
     */
    public MBStyle(JSONObject json) {
        this.json = json;
    }
    /**
     * Parse MBStyle for the provided json. 
     * @param json Required to be a JSONObject
     * @return MBStyle wrapping the provided json
     * 
     * @throws MBFormatException
     */
    public static MBStyle create(Object json) throws MBFormatException {
        if (json instanceof JSONObject) {
            return new MBStyle((JSONObject) json);
        } else if (json == null) {
            throw new MBFormatException("JSONObject required: null");
        } else {
            throw new MBFormatException("Root must be a JSON Object: " + json.toString());
        }
    }
    

    public List<MBLayer> layers(){
        JSONArray layers = parse.getJSONArray(json, "layers");
        List<MBLayer> layersList = new ArrayList<>();
        for (Object obj : layers) {
            if (obj instanceof JSONObject) {
                // MBLayer layer = MBObjectParser.parseLayer(obj);
                MBLayer layer = MBLayer.create((JSONObject) obj);
                layersList.add(layer);
            } else {
                throw new MBFormatException("Unexpected layer definition " + obj);
            }
        }
        return layersList;
    }
    /**
     * Access layers matching provided source.
     * 
     * @param source
     * @return list of layers matching provided source
     */
    public List<MBLayer> layers(String source) throws MBFormatException {
        JSONArray layers = parse.getJSONArray(json, "layers");
        List<MBLayer> layersList = new ArrayList<>();
        for (Object obj : layers) {
            if (obj instanceof JSONObject) {
                MBLayer layer = MBLayer.create((JSONObject) obj);
                
                if( source.equals(layer.getSource())){
                    layersList.add(layer);
                }
            } else {
                throw new MBFormatException("Unexpected layer definition " + obj);
            }
        }
        return layersList;
    }

    /**
     * Access layers matching provided source and selector.
     * 
     * @param source
     * @param sourceLayer
     * @return list of layers matching provided source
     */
    public List<MBLayer> layers(String source, String sourceLayer) {
        JSONArray layers = parse.getJSONArray(json, "layers");
        List<MBLayer> layersList = new ArrayList<>();
        for (Object obj : layers) {
            if (obj instanceof JSONObject) {
                MBLayer layer = MBLayer.create((JSONObject) obj);
                
                if( source.equals(layer.getSource()) &&
                        sourceLayer.equals(layer.getSourceLayer())){
                    layersList.add(layer);
                }
            } else {
                throw new MBFormatException("Unexpected layer definition " + obj);
            }
        }
        return layersList;
    }
    /** 
     * A human-readable name for the style
     * @return human-readable name, optional string.
     */
    public String getName() {
        return parse.get(json,"name");
    }
}
