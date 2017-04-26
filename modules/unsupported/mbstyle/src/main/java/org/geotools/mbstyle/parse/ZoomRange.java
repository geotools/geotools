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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.geotools.mbstyle.MBLayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Data object capturing a range between two zoom levels.
 */
public class ZoomRange {

    /** Min zoom level, may {@link Integer#MIN_NORMAL} if unset. */
    int min = Integer.MIN_VALUE;

    /** Max zoom level, may {@link Integer#MAX_VALUE} if unset. */
    int max = Integer.MAX_VALUE;

    public ZoomRange() {
    }

    public ZoomRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /** Min zoom level, may {@link Integer#MIN_NORMAL} if unset. */
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    /** Max zoom level, may {@link Integer#MAX_VALUE} if unset. */
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Min scale denominator, or null if unset.
     * 
     * @return scale denominator, or {@link Double#MIN_VALUE} if unset..
     */

    public double minScaleDenominator() {
        return scaleDenominator(min);
    }

    public double maxScaleDenominator() {
        return scaleDenominator(max);
    }

    /**
     * Calcualte scale denominator for the provided zoom level.
     * 
     * @param zoomLevel
     * @return scale denominator
     */
    public static Double scaleDenominator(int zoomLevel) {
        if (zoomLevel == Integer.MIN_VALUE) {
            return Double.MIN_VALUE;
        }
        if (zoomLevel == Integer.MAX_VALUE) {
            return Double.MAX_VALUE;
        }
        return 559_082_263.9508929 / Math.pow(2, (double) zoomLevel);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (max ^ (max >>> 32));
        result = prime * result + (int) (min ^ (min >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ZoomRange other = (ZoomRange) obj;
        if (max != other.max)
            return false;
        if (min != other.min)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ZoomLevel ");
        str.append(min == Long.MIN_VALUE ? "min" : String.valueOf(min));
        str.append(":");
        str.append(max == Long.MAX_VALUE ? "max" : String.valueOf(max));

        return str.toString();
    }

    //
    // Utility Methods
    //

    /**
     * Generates zoom levels for provided layer, taking min/max zoom level into account, along with
     * any zoom and property functions.
     * 
     * @param layer
     * @return List of zoom levels, may be empty if layer is not visible.
     */
    List<ZoomRange> zoomLevels(MBLayer layer) {
        if (!layer.visibility()) {
            return Collections.emptyList();
        }
        List<ZoomRange> zoomLevels = new ArrayList<>();
        ZoomRange layerRange = new ZoomRange(layer.getMinZoom(),layer.getMaxZoom());
        
        // These are intended to go early in the rendering chain
        // prefer use of rules if possible
        JSONObject paint = layer.paint();
        
        zoomLevels.addAll( zoomLevelsPaint( paint ));
        
        // These are intended to go late in the rendering chain
        // prefer use of functions if possible
        JSONObject layout = layer.layout();
        
        if(zoomLevels.isEmpty()){
            zoomLevels.add(layerRange);
        }
        return zoomLevels;
    }

    private List<ZoomRange> zoomLevelsPaint(JSONObject paint) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Review paint for zoom and properrty functions suitable for representation as rules.
     * @param paint
     * @return true if zoom and property function found.
     */
    static boolean hasZoomAndPropertyFunction(JSONObject paint) {
        for( Entry<String,Object> entry : (Set<Entry<String,Object>>) paint.entrySet() ){        
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                JSONObject child = (JSONObject) value;
                if( isFunction(child) && isZoomAndPropertyFunction(child)){
                    return true; // we found a zoom and property function
                }
            }
        }
        return false;
    }

    static boolean isFunction(JSONObject json){
        if( json.containsKey("type")){
            return "identity".equals(json.get("type"));
        }
        else if (json.containsKey("stop")){
            return true;
        }
        return false;
    }
    static boolean isZoomAndPropertyFunction(JSONObject json){
        if (json.containsKey("property") && json.containsKey("stops")) {
            JSONArray stops = (JSONArray) json.get("stops");
            Object stop = stops.get(0);
            if( stop instanceof JSONArray ){
                Object value = ((JSONArray)stop).get(0);
                if( value instanceof JSONObject && ((JSONObject)value).containsKey("zoom")){
                    return true; // this is a zoom and property function!
                }
            }
        }
        return false;
    }
    
}