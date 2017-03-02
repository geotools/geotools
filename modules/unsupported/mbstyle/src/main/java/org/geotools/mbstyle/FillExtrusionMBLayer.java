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
 *    
 */
package org.geotools.mbstyle;

import java.awt.Color;
import java.awt.Point;

import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
/**
 * MBLayer wrapper for "fill extrusion" representing extruded (3D) polygon.
 * <p>
 * Example of line JSON:<pre>
 * {    'id': 'room-extrusion',
 *      'type': 'fill-extrusion',
 *      'source': {
 *          'type': 'geojson',
 *          'data': 'https://www.mapbox.com/mapbox-gl-js/assets/data/indoor-3d-map.geojson'
 *      },
 *      'paint': {
 *          'fill-extrusion-color': { 'property': 'color', 'type': 'identity'},
 *          'fill-extrusion-height': { 'property': 'height','type': 'identity'},
 *          'fill-extrusion-base': { 'property': 'base_height','type': 'identity'},
 *          'fill-extrusion-opacity': 0.5
 *      }
 *  }
 * </pre>
 * 
 * Responsible for accessing wrapped json as expressions (for use in transformer).
 * 
 * @author jody
 */
public class FillExtrusionMBLayer extends MBLayer {

    private JSONObject paint;

    private JSONObject layout;

    private static String type = "fill-extrusion";
    
    public static enum TranslateAnchor {
        /**
         * Translation relative to the map.
         */
        MAP,

        /**
         * Translation relative to the viewport.
         */
        VIEWPORT
    }
    
    public FillExtrusionMBLayer(JSONObject json) {
        super(json,new MBObjectParser(FillExtrusionMBLayer.class));

        paint = paint();
        layout = layout();
    }
    
    /**
     * (Optional) Defaults to 1.
     * 
     * The opacity of the entire fill extrusion layer. This is rendered on a per-layer, not per-feature, basis, and data-driven styling is not available.
     * @return Number
     * @throws MBFormatException
     */
    public Number getFillExtrusionOpacity() throws MBFormatException {
    	return parse.optional(Double.class, paint, "fill-extrusion-opacity", 1.0);
    }

    /**
     * Access fill-extrusion-opacity as literal or function expression
     * @throws MBFormatException
     */
    public Expression fillExtrusionOpacity() throws MBFormatException {
    	return parse.percentage(paint, "fill-extrusion-opacity", 1.0);
    }
    
    /**
     * (Optional). Defaults to #000000. Disabled by fill-extrusion-pattern.
     * 
     * The base color of the extruded fill. The extrusion's surfaces will be shaded differently based on this color in combination with the root light settings.
     * 
     * If this color is specified as  rgba with an alpha component, the alpha component will be ignored; use fill-extrusion-opacity to set layer opacity.
     */
    public Color getFillExtrusionColor() throws MBFormatException {
        return parse.optional(Color.class, paint, "fill-extrusion-color", Color.BLACK );
    }
    
    /** Access fill-extrusion-color as literal or function expression, defaults to black. */
    public Expression fillExtrusionColor() throws MBFormatException {      
        return parse.color(paint, "fill-extrusion-color", Color.BLACK);
    }
    
    /**
     * (Optional) Units in pixels. Defaults to 0,0.
     * 
     * The geometry's offset. Values are [x, y] where negatives indicate left and up (on the flat plane), respectively.
     */
    public double[] getFillExtrusionTranslate() throws MBFormatException{
        return parse.array( paint, "fill-extrusion-translate", new double[]{ 0.0, 0.0 } ); 
    }
     
    public Point fillExtrusionTranslate() {
        if (paint.get("fill-extrusion-translate") != null) {
            JSONArray array = (JSONArray) paint.get("fill-extrusion-translate");
            Number x = (Number) array.get(0);
            Number y = (Number) array.get(1);
            return new Point(x.intValue(), y.intValue());
        } else {
            return new Point(0, 0);
        }
    }
   
    /**
     * (Optional) One of map, viewport. Defaults to map. Requires fill-extrusion-translate. 
     * 
     * Controls the translation reference point.
     * 
     * {@link TranslateAnchor#MAP}: The fill extrusion is translated relative to the map.
     * 
     * {@link TranslateAnchor#VIEWPORT}: The fill extrusion is translated relative to the viewport.
     * 
     * Defaults to {@link TranslateAnchor#MAP}.
     * 
     */
    public TranslateAnchor getFillExtrusionTranslateAnchor() {
        Object value = paint.get("fill-extrusion-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return TranslateAnchor.VIEWPORT;
        } else {
            return TranslateAnchor.MAP;
        }
    }
    
    /**
     * (Optional) Name of image in sprite to use for drawing images on extruded fills. For seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512).
     */
    public Expression getFillExtrusionPattern()  throws MBFormatException{
        return parse.string(paint, "fill-extrusion-pattern", null);
    }
    
    /**
     * (Optional) Units in meters. Defaults to 0. The height with which to extrude this layer.
     */
    public Number getFillExtrusionHeight() throws MBFormatException {
        return parse.optional(Double.class, paint, "fill-extrusion-height", 0.0);
    }

    /**
     * Access fill-extrusion-height as literal or function expression
     * @throws MBFormatException
     */
    public Expression fillExtrusionHeight() throws MBFormatException {
        return parse.percentage(paint, "fill-extrusion-height", 0.0);
    }
    
    /**
     * (Optional) Units in meters. Defaults to 0. Requires fill-extrusion-height.
     * 
     * The height with which to extrude the base of this layer. Must be less than or equal to fill-extrusion-height.
     */
    public Number getFillExtrusionBase() throws MBFormatException {
        return parse.optional(Double.class, paint, "fill-extrusion-base", 0.0);
    }

    /**
     * Access fill-extrusion-base as literal or function expression
     * @throws MBFormatException
     */
    public Expression fillExtrusionBase() throws MBFormatException {
        return parse.percentage(paint, "fill-extrusion-base", 0.0);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getType() {
        return type;
    }

}
