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

import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * This class provides the ability to find all the zoom levels within a MapBox Style and returns a reduced list
 * of only the layers and properties containing Base and Stops values.
 *
 * Created by vickdw on 4/17/17.
 */
public class MBObjectStops {
    static List<Long> layerZoomLevels;

    /**
     * For each layer in a MapBox Style Document, find the distinct zoom levels for each layer
     * @param mbStyle
     * @return
     */
    public static List<Long> getStopLevels(MBStyle mbStyle) {
        Set<Long> distinctValues = new HashSet<>();
        List<Long> zoomLevels = new ArrayList<>();
        for (MBLayer layer : mbStyle.layers()) {
            layerZoomLevels = new ArrayList<>();
            if (layer.getPaint() != null) {
                traverse(layer.getPaint(), layerZoomLevels);
            }
            if (layer.getLayout() != null) {
                traverse(layer.getLayout(), layerZoomLevels);
            }
            distinctValues.addAll(layerZoomLevels);
        }
        zoomLevels.addAll(distinctValues);
        Collections.sort(zoomLevels);

        return zoomLevels;
    }

    /**
     * Get the stop levels for the layer.
     * @param mbLayer
     * @return
     */
    public static List<Long> getLayerStopLevels(MBLayer mbLayer) {
        List<Long> zoomLevels = new ArrayList<>();
        if (mbLayer.getPaint() != null) {
            traverse(mbLayer.getPaint(), zoomLevels);
        }
        if (mbLayer.getLayout() != null) {
            traverse(mbLayer.getLayout(), zoomLevels);
        }

        return zoomLevels;
    }

    public static List<MBStyle> getLayerStylesForStops(List<Long> layerZoomLevels, MBStyle mbStyle, MBLayer mbLayer)
            throws ParseException{
        JSONParser parser = new JSONParser();
        List<MBStyle> styles = new ArrayList<>();

        for (int i = 0; i < layerZoomLevels.size(); i++)  {
            JSONObject obj = (JSONObject) parser.parse(mbStyle.json.toJSONString());
            MBStyle workingStyle = getStyleForLayer(obj, mbLayer);
            Long maxZoom = layerZoomLevels.get(layerZoomLevels.size() - 1);
            Long current = layerZoomLevels.get(i);
            long[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = layerZoomLevels.get(i + 1);
            } else if (current == maxZoom) {
                range[0] = current;
                range[1] = maxZoom;
            }
            styles.add(createLayerStopStyle(workingStyle, workingStyle.layer(mbLayer.getId()), range));
        }

        return styles;
    }


    public static List<MBStyle> getStylesForStopLevels(List<Long> zoomLevels, MBStyle mbStyle) throws ParseException {
        JSONParser parser = new JSONParser();
        List<MBStyle> styles = new ArrayList<>();

        for (int i = 0; i < zoomLevels.size(); i++) {
            Object obj = parser.parse(mbStyle.json.toJSONString());
            MBStyle workingStyle = new MBStyle((JSONObject) obj);
            Long maxZoom = zoomLevels.get(zoomLevels.size() - 1);
            Long current = zoomLevels.get(i);
            long[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = zoomLevels.get(i + 1);
            } else if (current == maxZoom) {
                range[0] = current;
                range[1] = maxZoom;
            }
            styles.add(getStopStyles(workingStyle, range));
        }

        return styles;
    }

    public static List<long[]> getStopLevelRanges(List<Long> stops) {
        List<long[]> ranges = new ArrayList<>();
        for (int i = 0; i < stops.size(); i++) {
            Long maxZoom = stops.get(stops.size() - 1);
            Long current = stops.get(i);
            long[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = stops.get(i + 1);
            } else if (current == maxZoom) {
                range[0] = current;
                range[1] = maxZoom;
            }
            ranges.add(range);
        }
        return ranges;
    }

    public static long[] getRangeForStop(Long stop, List<long[]> ranges) {
        long[] rangeForStopLevel = {0,0};
        for (int i = 0; i < ranges.size(); i++) {
            if (ranges.get(i)[0] == stop) {
                rangeForStopLevel = ranges.get(i);
            }
        }
        return rangeForStopLevel;
    }    

    /**
     * Take a web mercator zoom level, and return the equivalent scale denominator (at the equator).
     * 
     * Converting to a scale denominator at the equator is consistent with the conversion elsewhere in GeoTools, e.g., in the GeoTools YSLD
     * ZoomContextFinder.
     * 
     * @param zoomLevel The zoom level
     * @return The equivalent scale denominator (at the equator)
     */
    public static Double zoomLevelToScaleDenominator(Long zoomLevel) {
        return 559_082_263.9508929 / Math.pow(2, zoomLevel);
    }

    public static long getStop(MBLayer layer) {
        return stop(layer);
    }

    static long stop (MBLayer layer) {
        long s = 0;
        if (layer.getPaint() != null) {
            s = traverse(layer.getPaint());
        }
        if (layer.getLayout() != null) {
            s = traverse(layer.getLayout());
        }
        return s;
    }

    static MBStyle getStyleForLayer(JSONObject obj, MBLayer layer) {
        JSONArray layers = (JSONArray) obj.get("layers");
        List<Object> layersToRemove = new ArrayList<>();

        for (int i = 0; i < layers.size(); i++) {
            JSONObject jsonLayer = (JSONObject)layers.get(i);
            String id = (String) jsonLayer.get("id");
            if (!id.equalsIgnoreCase(layer.getId())) {
                layersToRemove.add(layers.get(i));
            }
        }

        for (Object o : layersToRemove) {
            layers.remove(o);
        }

        MBStyle mbStyle = new MBStyle(obj);
        return mbStyle;
    }

    static MBStyle getStopStyles(MBStyle style, long[] range) {
        for (MBLayer layer : style.layers()) {
            if (layer.getPaint() != null) {
                traverse(layer.getPaint(), range);
            }
            if (layer.getLayout() != null) {
                traverse(layer.getLayout(), range);
            }
        }
        return style;
    }

    static MBStyle createLayerStopStyle(MBStyle mbStyle, MBLayer layer, long[] range) {
        if (layer.getPaint() != null ) {
            traverse(layer.getPaint(), range);
        }
        if (layer.getLayout() != null) {
            traverse(layer.getLayout(), range);
        }

        return mbStyle;
    }

    static long traverse(JSONObject jsonObject) {
        long layerStop = 0;
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
                        if (stop.get(0) instanceof Long) {
                            layerStop = (Long)stop.get(0);
                        }
                    }
                }
            }
        }
        return layerStop;
    }

    static JSONObject traverse(JSONObject jsonObject, long[] range) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        List<Object> objectsToRemove = new ArrayList<>();
        List<JSONArray> objectsToEdit = new ArrayList<>();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
                        if (stop.get(0) instanceof Long) {
                            if (((Long) stop.get(0)).longValue() != range[0]) {
                                objectsToRemove.add(stops.get(i));
                            }
                        }
                        if (stop.get(0) instanceof JSONObject) {
                            if (((Long)((JSONObject) stop.get(0)).get("zoom")).longValue() == range[0]) {
                                objectsToEdit.add((JSONArray) stops.get(i));

                            } else {
                                objectsToRemove.add(stops.get(i));
                            }
                        }
                    }
                    for (Object o : objectsToRemove) {
                        stops.remove(o);
                    }
                    for (JSONArray o : objectsToEdit) {
                        JSONArray stopsArray = new JSONArray();
                        stopsArray.add(0, ((Long)((JSONObject) o.get(0)).get("zoom")).longValue());
                        stopsArray.add(1, o.get(1));
                        stops.remove(o);
                        stops.add(stopsArray);
                    }
                }
                if (((JSONArray)child.get("stops")).size() == 0) {
                    child.remove("stops");
                }
            }
        }
        return jsonObject;
    }

    static List<Long> traverse(JSONObject jsonObject, List<Long> layerZoomLevels) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
                        if (stop.get(0) instanceof Long) {
                            layerZoomLevels.add(((Long)stop.get(0)));
                        }
                        if (stop.get(0) instanceof JSONObject) {
                            layerZoomLevels.add((Long)((JSONObject) stop.get(0)).get("zoom"));
                        }
                    }
                }
            }
        }
        return layerZoomLevels;
    }
}
