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

import static org.geotools.mbstyle.function.ZoomLevelFunction.EPSG_3857_O_SCALE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class provides the ability to find all the zoom levels within a MapBox Style and returns a
 * reduced list of only the layers and properties containing Base and Stops values. This class is
 * only used with "zoom" and "zoom-and-property" and excludes the use of "property".
 *
 * @author David Vick (Boundless)
 */
public class MBObjectStops {

    static final Logger LOGGER = Logging.getLogger(MBObjectStops.class);
    private static final double ZOOM_EPS = 0.01; // 1% of a zoom level

    JSONParser parser = new JSONParser();
    public LayerStops ls = new LayerStops();
    List<Double> layerZoomLevels;
    public List<Double> stops = new ArrayList<>();
    public List<MBLayer> layersForStop = new ArrayList<>();
    public List<double[]> ranges = new ArrayList<>();

    /**
     * Data structure for pre-processing a MBLayer determining whether the layer contains zoom and
     * zoom-and-property functions and if so, getting the distinct stops for each, building a list
     * of MBLayers (one for each stop) and setting ranges that are used to set min/max scale
     * denominators for each MBLayer.
     *
     * @param layer
     */
    public MBObjectStops(MBLayer layer) {
        try {
            if (layer.getPaint() != null) {
                ls = getStops(layer.getPaint(), ls);
            }
            if (layer.getLayout() != null) {
                ls = getStops(layer.getLayout(), ls);
            }
            if (ls.zoomPropertyStops) {
                stops = getStopLevels(layer);
                layersForStop = getLayerStyleForStops(layer, stops);
                ranges = getStopLevelRanges(stops);
            }

        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Failed to parse MBStiles", e);
        }
    }

    /**
     * Gets the current stop of the layer. This would be the bottom of the range i.e 0 for {0, 20}
     *
     * @param layer
     * @return
     */
    public double getCurrentStop(MBLayer layer) {
        double stop = getStop(layer);

        return stop;
    }

    /**
     * Finds all the stops within the layer and returns a sorted distinct list
     *
     * @param mbLayer
     * @return
     */
    List<Double> getStopLevels(MBLayer mbLayer) {
        Set<Double> distinctValues = new HashSet<>();
        List<Double> zoomLevels = new ArrayList<>();

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
     * This method creates a copy of the incoming layer to be used for creating the unique layer for
     * each stop.
     *
     * @param layer
     * @param layerStops
     * @return
     * @throws ParseException
     */
    List<MBLayer> getLayerStyleForStops(MBLayer layer, List<Double> layerStops)
            throws ParseException {
        List<MBLayer> layers = new ArrayList<>();

        for (int i = 0; i < layerStops.size(); i++) {
            JSONObject obj = (JSONObject) parser.parse(layer.getJson().toJSONString());
            MBLayer workingLayer = MBLayer.create(obj);
            Double maxZoom = layerStops.get(layerStops.size() - 1);
            Double current = layerStops.get(i);
            double[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = layerStops.get(i + 1);
            } else if (current.equals(maxZoom)) {
                range[0] = current;
                range[1] = maxZoom;
            }
            layers.add(createLayerStopStyle(workingLayer, range));
        }

        return layers;
    }

    /**
     * Accepts the list of stops for a layer and builds a list of ranges for each stop.
     *
     * @param stops
     * @return
     */
    List<double[]> getStopLevelRanges(List<Double> stops) {
        List<double[]> ranges = new ArrayList<>();
        for (int i = 0; i < stops.size(); i++) {
            Double maxZoom = stops.get(stops.size() - 1);
            Double current = stops.get(i);
            double[] range = {0, 0};
            if (current < maxZoom) {
                range[0] = current;
                range[1] = stops.get(i + 1);
            } else if (current.equals(maxZoom)) {
                range[0] = current;
                range[1] = -1;
            }
            ranges.add(range);
        }
        return ranges;
    }

    LayerStops getStops(JSONObject jsonObject, LayerStops ls) {
        return containsStops(jsonObject, ls);
    }

    /**
     * Finds the distinct range for the current stop.
     *
     * @param stop
     * @param ranges
     * @return
     */
    public double[] getRangeForStop(Double stop, List<double[]> ranges) {
        double[] rangeForStopLevel = {0, 0};
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
     * <p>Converting to a scale denominator at the equator is consistent with the conversion
     * elsewhere in GeoTools, e.g., in the GeoTools YSLD ZoomContextFinder.
     *
     * @param zoomLevel The zoom level
     * @return The equivalent scale denominator (at the equator)
     */
    public static Double zoomLevelToScaleDenominator(double zoomLevel) {
        return EPSG_3857_O_SCALE / Math.pow(2, zoomLevel);
    }

    public double getStop(MBLayer layer) {
        return stop(layer);
    }

    LayerStops containsStops(JSONObject jsonObject, LayerStops ls) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    if (child.containsKey("property")) {
                        // here we must determine if it is just property stops
                        // or if we are dealing with property and zoom functions
                        JSONArray stops = (JSONArray) child.get("stops");
                        JSONArray stopObject = (JSONArray) stops.get(0); // all stops are JSONArrays

                        // Check the first value of the Array, if it is a JSONObject
                        // we are dealing with zoom and property functions
                        if (stopObject.get(0) instanceof JSONObject) {
                            ls.zoomPropertyStops = true;
                        } else {
                            ls.propertyStops = true;
                        }
                    } else if (!child.containsKey("property")) {
                        ls.zoomStops = true;
                    }
                }
            }
        }
        return ls;
    }

    /**
     * Accepts a distinct MBLayer and finds the stop for this layer.
     *
     * @param layer
     * @return
     */
    double stop(MBLayer layer) {
        double s = 0;
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
     *
     * @param layer
     * @param range
     * @return
     */
    MBLayer createLayerStopStyle(MBLayer layer, double[] range) {
        if (layer.getPaint() != null) {
            reduceJsonForRange(layer.getPaint(), range);
        }
        if (layer.getLayout() != null) {
            reduceJsonForRange(layer.getLayout(), range);
        }

        return layer;
    }

    /**
     * Iterates over the JSONObject looking for the stops for the given stop.
     *
     * @param jsonObject
     * @param layerStop
     * @return
     */
    double findStop(JSONObject jsonObject, double layerStop) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
                        if (stop.get(0) instanceof Number) {
                            layerStop = ((Number) stop.get(0)).doubleValue();
                        }
                    }
                }
            }
        }
        return layerStop;
    }

    /**
     * Reduces the JSON to just the required values for the given range.
     *
     * @param jsonObject
     * @param range
     * @return
     */
    JSONObject reduceJsonForRange(JSONObject jsonObject, double[] range) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        List<String> keyToRemove = new ArrayList<>();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    List<JSONArray> objectsToEdit = new ArrayList<>();
                    List<Object> objectsToRemove = new ArrayList<>();
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
                        if (stop.get(0) instanceof JSONObject) {
                            double zoomValue =
                                    ((Number) ((JSONObject) stop.get(0)).get("zoom")).doubleValue();
                            if (zoomEquals(zoomValue, range[0])) {
                                objectsToEdit.add((JSONArray) stops.get(i));
                            } else {
                                objectsToRemove.add(stops.get(i));
                            }
                        }
                        if (stop.get(0) instanceof Number) {
                            double zoomValue = ((Number) stop.get(0)).doubleValue();
                            if (zoomEquals(zoomValue, range[0])) {
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
                        if (o.get(0) instanceof JSONObject) {
                            stopsArray.add(0, ((JSONObject) o.get(0)).get("value"));
                            stopsArray.add(1, o.get(1));
                            stops.remove(o);
                            stops.add(stopsArray);
                        }
                        if (o.get(0) instanceof Number) {
                            stopsArray.add(0, ((Number) o.get(0)).doubleValue());
                            stopsArray.add(1, o.get(1));
                            stops.remove(o);
                            stops.add(stopsArray);
                        }
                    }
                }
                if (((JSONArray) child.get("stops")).size() == 0) {
                    keyToRemove.add(key);
                }
            }
        }
        for (String key : keyToRemove) {
            jsonObject.remove(key);
        }
        return jsonObject;
    }

    private boolean zoomEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < ZOOM_EPS;
    }

    /**
     * Iterates over the JSONObject finding the stops and adding them to the list.
     *
     * @param jsonObject
     * @param layerZoomLevels
     * @return
     */
    List<Double> findStopLevels(JSONObject jsonObject, List<Double> layerZoomLevels) {
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject child = (JSONObject) jsonObject.get(key);
                if (child.containsKey("stops")) {
                    JSONArray stops = (JSONArray) child.get("stops");
                    for (int i = 0; i < stops.size(); i++) {
                        JSONArray stop = (JSONArray) stops.get(i);
                        if (stop.get(0) instanceof Number) {
                            layerZoomLevels.add(((Number) stop.get(0)).doubleValue());
                        } else if (stop.get(0) instanceof JSONObject) {
                            layerZoomLevels.add(
                                    ((Number) ((JSONObject) stop.get(0)).get("zoom"))
                                            .doubleValue());
                        } else {
                            throw new MBFormatException(
                                    "The \"property\" field missing for stops or invalid zoom.");
                        }
                    }
                }
            }
        }
        return layerZoomLevels;
    }

    public class LayerStops {
        public boolean propertyStops = false;
        public boolean zoomStops = false;
        public boolean zoomPropertyStops = false;

        public boolean isPropertyStops() {
            return propertyStops;
        }

        public void setPropertyStops(boolean propertyStops) {
            this.propertyStops = propertyStops;
        }

        public boolean isZoomStops() {
            return zoomStops;
        }

        public void setZoomStops(boolean zoomStops) {
            this.zoomStops = zoomStops;
        }

        public boolean isZoomPropertyStops() {
            return zoomPropertyStops;
        }

        public void setZoomPropertyStops(boolean zoomPropertyStops) {
            this.zoomPropertyStops = zoomPropertyStops;
        }

        public boolean hasStops() {
            return zoomPropertyStops || propertyStops || zoomStops;
        }
    }
}
