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

import org.geotools.mbstyle.layer.MBLayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

import static org.geotools.mbstyle.function.ZoomLevelFunction.EPSG_3857_O_SCALE;

/**
 * This class provides the ability to find all the zoom levels within a MapBox Style and returns a reduced list
 * of only the layers and properties containing Base and Stops values.
 *
 * @author David Vick (Boundless)
 */
public class MBObjectStops {

    JSONParser parser = new JSONParser();
    List<Long> layerZoomLevels;
    public boolean hasStops = false;
    public List<Long> stops = new ArrayList<>();
    public List<MBLayer> layersForStop = new ArrayList<>();
    public List<long[]> ranges = new ArrayList<>();

    /**
     * Data structure for pre-processing a MBLayer determining whether the layer contains property and zoom functions
     * and if so, getting the distinct stops for each, building a list of MBLayers (one for each stop) and setting
     * ranges that are used to set min/max scale denominators for each MBLayer.
     * @param layer
     */
    public MBObjectStops(MBLayer layer) {
        try {
            if (layer.getPaint() != null) {
                hasStops = getStops(layer.getPaint());
            }
            if (layer.getLayout() != null && !hasStops) {
                hasStops = getStops(layer.getLayout());
            }
            if (hasStops) {
                stops = getStopLevels(layer);
                layersForStop = getLayerStyleForStops(layer, stops);
                ranges = getStopLevelRanges(stops);
            }

        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Gets the current stop of the layer. This would be the bottom of the range i.e 0 for {0, 20}
     * @param layer
     * @return
     */
    public long getCurrentStop(MBLayer layer) {
        long stop = getStop(layer);

        return stop;
    }

    /**
     * Finds all the stops within the layer and returns a sorted distinct list
     * @param mbLayer
     * @return
     */
    List<Long> getStopLevels(MBLayer mbLayer) {
        Set<Long> distinctValues = new HashSet<>();
        List<Long> zoomLevels = new ArrayList<>();

        layerZoomLevels = new ArrayList<>();
        if (mbLayer.getPaint() != null) {
            findStopLevels(mbLayer.getPaint(), layerZoomLevels);
        }
        if (mbLayer.getLayout() != null) {
            findStopLevels(mbLayer.getLayout(), layerZoomLevels);
        }
        distinctValues.addAll(layerZoomLevels);
        zoomLevels.addAll(distinctValues);
        Collections.sort(zoomLevels);

        return zoomLevels;
    }

    /**
     * This method creates a copy of the incoming layer to be used for creating the unique layer for each stop.
     * @param layer
     * @param layerStops
     * @return
     * @throws ParseException
     */
    List<MBLayer> getLayerStyleForStops(MBLayer layer, List<Long> layerStops) throws ParseException {
        List<MBLayer> layers = new ArrayList<>();

        for (int i = 0; i < layerStops.size(); i ++) {
            JSONObject obj = (JSONObject) parser.parse(layer.getJson().toJSONString());
            MBLayer workingLayer = MBLayer.create(obj);
            Long maxZoom = layerStops.get(layerStops.size() - 1);
            Long current = layerStops.get(i);
            long[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = layerStops.get(i + 1);
            } else if ( current == maxZoom) {
                range[0] = current;
                range[1] = maxZoom;
            }
            layers.add(createLayerStopStyle(workingLayer, range));
        }

        return layers;
    }

    /**
     * Accepts the list of stops for a layer and builds a list of ranges for each stop.
     * @param stops
     * @return
     */
    List<long[]> getStopLevelRanges(List<Long> stops) {
        List<long[]> ranges = new ArrayList<>();
        for (int i = 0; i < stops.size(); i++) {
            Long maxZoom = stops.get(stops.size() - 1);
            Long current = stops.get(i);
            long[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = stops.get(i + 1);
            } else if (current ==  maxZoom) {
                range[0] = current;
                range[1] = -1;
            }
            ranges.add(range);

        }
        return ranges;
    }

    boolean getStops(JSONObject jsonObject) {
        return containsStops(jsonObject);
    }

    /**
     * Finds the distinct range for the current stop.
     * @param stop
     * @param ranges
     * @return
     */
    public long[] getRangeForStop(Long stop, List<long[]> ranges) {
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
        return EPSG_3857_O_SCALE / Math.pow(2, zoomLevel);
    }

    public long getStop(MBLayer layer) {
        return stop(layer);
    }

    boolean containsStops(JSONObject jsonObject) {
        Boolean hasStops = false;

        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops") && ((JSONArray)((JSONArray)child.get("stops")).get(0)).get(0) instanceof JSONObject) {
                    hasStops = true;
                }
            }
        }
        return hasStops;
    }

    /**
     * Accepts a distinct MBLayer and finds the stop for this layer.
     * @param layer
     * @return
     */
    long stop (MBLayer layer) {
        long s = 0;
        if (layer.getPaint() != null) {
            s = findStop(layer.getPaint(), s);
        }
        if (layer.getLayout() != null) {
            s = findStop(layer.getLayout(), s);
        }
        return s;
    }

    /**
     * This method reduces the Layer to just what is needed for the given range.
     * @param layer
     * @param range
     * @return
     */
    MBLayer createLayerStopStyle(MBLayer layer, long[] range) {
        if (layer.getPaint() != null ) {
            reduceJsonForRange(layer.getPaint(), range);
        }
        if (layer.getLayout() != null) {
            reduceJsonForRange(layer.getLayout(), range);
        }

        return layer;
    }

    /**
     * Iterates over the JSONObject looking for the stops for the given stop.
     * @param jsonObject
     * @param layerStop
     * @return
     */
    long findStop(JSONObject jsonObject, long layerStop) {
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

    /**
     * Reduces the JSON to just the required values for the given range.
     * @param jsonObject
     * @param range
     * @return
     */
    JSONObject reduceJsonForRange(JSONObject jsonObject, long[] range) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        List<String> keyToRemove = new ArrayList<>();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    List<JSONArray> objectsToEdit = new ArrayList<>();
                    List<Object> objectsToRemove = new ArrayList<>();
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
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
                        stopsArray.add(0, ((JSONObject) o.get(0)).get("value"));
                        stopsArray.add(1, o.get(1));
                        stops.remove(o);
                        stops.add(stopsArray);
                    }
                }
                if (((JSONArray)child.get("stops")).size() == 0) {
                    keyToRemove.add(key);
                }
            }
        }
        for (String key : keyToRemove) {
            jsonObject.remove(key);
        }
        return jsonObject;
    }

    /**
     * Iterates over the JSONObject finding the stops and adding them to the list.
     * @param jsonObject
     * @param layerZoomLevels
     * @return
     */
    List<Long> findStopLevels(JSONObject jsonObject, List<Long> layerZoomLevels) {
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
