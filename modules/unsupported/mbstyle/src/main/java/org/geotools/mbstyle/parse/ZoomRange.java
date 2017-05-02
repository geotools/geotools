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

import static org.geotools.mbstyle.function.ZoomLevelFunction.EPSG_3857_O_SCALE;

/**
 * Data object capturing a range between two zoom levels.
 */
public class ZoomRange implements Comparable<ZoomRange>{
    private static MBObjectParser parse = new MBObjectParser(ZoomRange.class);
    
    /** Min zoom level, {@link Integer#MIN_VALUE} if unset. */
    int min = Integer.MIN_VALUE;

    /** Max zoom level, {@link Integer#MAX_VALUE} if unset. */
    int max = Integer.MAX_VALUE;

    public ZoomRange() {
    }

    public ZoomRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /** Min zoom level, {@link Integer#MIN_VALUE} if unset. */
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    /** Max zoom level, {@link Integer#MAX_VALUE} if unset. */
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Min scale denominator.
     * 
     * @return scale denominator, or {@link Double#MIN_VALUE} if unset.
     */
    public double minScaleDenominator() {
        return scaleDenominator(min);
    }

    /**
     * Max scale denominator.
     *
     * @return scale denominator, or {@link Double#MAX_VALUE} if unset.
     */
    public double maxScaleDenominator() {
        return scaleDenominator(max);
    }

    /**
     * Calculate scale denominator for the provided zoom level.
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
        return EPSG_3857_O_SCALE / Math.pow(2, (double) zoomLevel);
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
        str.append(min == Integer.MIN_VALUE ? "min" : String.valueOf(min));
        str.append(":");
        str.append(max == Integer.MAX_VALUE ? "max" : String.valueOf(max));

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
    public static List<ZoomRange> zoomLevels(MBLayer layer) {
        if (!layer.visibility()) {
            return Collections.emptyList();
        }
        List<ZoomRange> zoomLevels = new ArrayList<>();
        ZoomRange layerRange = new ZoomRange(layer.getMinZoom(),layer.getMaxZoom());
        
        // These are intended to go early in the rendering chain
        // prefer use of rules if possible
        JSONObject paint = layer.paint();
        
        // These are intended to go late in the rendering chain
        // prefer use of functions if possible
        JSONObject layout = layer.layout();
        
        if(zoomLevels.isEmpty()){
            zoomLevels.add(layerRange);
        }
        return zoomLevels;
    }

    @SuppressWarnings("unchecked")
    public static List<ZoomRange> zoomLevelsPaint(JSONObject paint, ZoomRange layerRange) {
        SortedSet<ZoomRange> levels = new TreeSet<>();
        for( Entry<String,Object> entry : (Set<Entry<String,Object>>) paint.entrySet() ){
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                JSONObject child = (JSONObject) value;
                if( isFunction(child) && isZoomAndPropertyFunction(child)){
                    List<ZoomRange> list = zoomLevelsFunction( child, layerRange );
                    levels.addAll(list);
                }
            }
        }
        return new ArrayList<>( levels );
    }

    private static List<ZoomRange> zoomLevelsFunction(JSONObject function, ZoomRange layerRange){
        if(!isZoomAndPropertyFunction(function)){
            return Collections.emptyList();
        }
        SortedSet<Integer> set = new TreeSet<>();
        set.add(layerRange.getMin());
        set.add(layerRange.getMax());
        
        JSONArray stops = (JSONArray) function.get("stops");
        for( Object obj : stops ){
            JSONObject stop = (JSONObject) ((JSONArray) obj).get(0);
            Object value = stop.get("zoom");
            if(!(value instanceof Number)){
                throw new MBFormatException("Zoom level required to be integer, was "+value.getClass().getSimpleName());
            }
            int zoom = ((Number)value).intValue();
            set.add(zoom);
        }

        List<ZoomRange> levels = new ArrayList<>();
        Iterator<Integer> iterator = set.iterator();
        int zoom = iterator.next();
        while( iterator.hasNext() ){
            ZoomRange range = new  ZoomRange();
            range.setMin(zoom);
            zoom = iterator.next();
            range.setMax(zoom);
            
            levels.add(range);
        }
        return levels;
    }
    
    /**
     * Review paint for zoom and property functions suitable for representation as rules.
     * @param paint
     * @return true if zoom and property function found.
     */
    private static boolean hasZoomAndPropertyFunction(JSONObject paint) {
        for( Entry<String,Object> entry : (Set<Entry<String,Object>>) paint.entrySet() ){        
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                JSONObject child = (JSONObject) value;
                if( isFunction(child) && isZoomAndPropertyFunction(child)){
                    // drill down and grab the stops ranges
                }
            }
        }
        return false;
    }

    private  static boolean isFunction(JSONObject json){
        if( json.containsKey("type")){
            return "identity".equals(json.get("type"));
        } else if (json.containsKey("stops")){
            return true;
        }
        return false;
    }
    private static boolean isZoomAndPropertyFunction(JSONObject json){
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

    @Override
    public int compareTo(ZoomRange other) {
        if( min == other.min ){
            return 0;
        }
        return min < other.min ? -1 : 1;
    }
    
}