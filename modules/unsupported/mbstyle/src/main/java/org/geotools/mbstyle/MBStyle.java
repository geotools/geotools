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

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.source.MBSource;
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
     * 
     * @return human-readable name, optional string.
     */
    public String getName() {
        return parse.optional(String.class, json, "name", null);
    }    
    
    /**
     * (Optional) Arbitrary properties useful to track with the stylesheet, but do not influence rendering. Properties should be prefixed to avoid
     * collisions, like 'mapbox:'.
     * 
     * @return {@link JSONObject} containing the metadata.
     */
    public JSONObject getMetadata() {
        return parse.getJSONObject(json, "metadata", new JSONObject());
    }
    
    /**
     *  (Optional) Default map center in longitude and latitude. The style center will be used only if the map has not been positioned by other means (e.g. map options or user interaction).

     * @return A {@link Point} for the map center, or null if the style contains no center.
     */
    public Point2D getCenter() {
        double[] coords = parse.array(json, "center", null);
        if (coords == null) {
            return null;
        } else if (coords.length != 2){
            throw new MBFormatException("\"center\" array must be length 2.");
        } else {            
            return new Point2D.Double(coords[0], coords[1]);            
        }
    }
    
    /**
     * (Optional) Default zoom level. The style zoom will be used only if the map has not been positioned by other means (e.g. map options or user interaction).
     * 
     * @return Number for the zoom level, or null.
     */
    public Number getZoom() {
        return parse.optional(Number.class, json, "zoom", null);
    }

    /**
     * (Optional) Default bearing, in degrees clockwise from true north. The style bearing will be used only if the map has not been positioned by
     * other means (e.g. map options or user interaction).
     * 
     * @return Number for the bearing. Units in degrees. Defaults to 0.
     * 
     */
    public Number getBearing() {
        return parse.optional(Number.class, json, "bearing", 0);
    }
    
    /**
     * (Optional) Default pitch, in degrees. Zero is perpendicular to the surface, for a look straight down at the map, while a greater value like 60
     * looks ahead towards the horizon. The style pitch will be used only if the map has not been positioned by other means (e.g. map options or user
     * interaction).
     * 
     * @return Number for the pitch.Units in degrees. Defaults to 0.
     */
    public Number getPitch() {
        return parse.optional(Number.class, json, "pitch", 0);
    }   

    /**
     * A base URL for retrieving the sprite image and metadata. The extensions .png, .json and scale factor @2x.png will be automatically appended.
     * This property is required if any layer uses the background-pattern, fill-pattern, line-pattern, fill-extrusion-pattern, or icon-image
     * properties.
     * 
     * @return The String URL, or null.
     */
    public String getSprite() {
        return parse.optional(String.class, json, "sprite", null);
    }

    /**
     * (Optional) A URL template for loading signed-distance-field glyph sets in PBF format. The URL must include {fontstack} and {range} tokens. This
     * property is required if any layer uses the text-field layout property. 
     * <br/>
     * Example: 
     * <br/>
     * <code>"glyphs": "mapbox://fonts/mapbox/{fontstack}/{range}.pbf"</code>
     * 
     * @return A String URL template, or null.
     */
    public String getGlyphs() {
        return parse.optional(String.class, json, "glyphs", null);
    }
    
    /**
     * Data source specifications. 
     * 
     * @see {@link MBSource} and its subclasses.
     * @return Map of data source name -> {@link MBSource} instances.
     */
    public Map<String, MBSource> getSources() {
        Map<String, MBSource> sourceMap = new HashMap<>();
        JSONObject sources = parse.getJSONObject(json, "sources", new JSONObject());
        for (Object o: sources.keySet()) {
            if (o instanceof String) {
                String k = (String) o;
                JSONObject j = parse.getJSONObject(sources, k);
                MBSource s = MBSource.create(j, parse);
                sourceMap.put(k, s);
            }
        }
        return sourceMap;
    }
    
}
