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
public class MBObjectZoomLevels {
    static List<Long> layerZoomLevels;
    //static Boolean hasStops = false;

    public static JSONObject getAllPropertyStops(JSONObject mbObject) {
        JSONObject mbStops = new JSONObject();
        //JSONArray layersWithStops = getLayersWithStops((JSONArray) mbObject.get("layers"));
        //layersWithStops = propertyReduce(layersWithStops);
        //mbStops.put("layers", layersWithStops);

        return mbStops;
    }

    public static List<MBStyle> getStylesForZoomLevels(List<Long> zoomLevels, JSONObject masterStyle) throws ParseException {
        JSONParser parser = new JSONParser();
        List<MBStyle> styles = new ArrayList<>();
        for (int i = 0; i < zoomLevels.size(); i++) {
            Object obj = parser.parse(masterStyle.toJSONString());
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
            styles.add(generateZoomStyle(workingStyle, range));
        }

        return styles;
    }

    public static MBStyle generateZoomStyle(MBStyle mbStyle, long[] range) {
        for (MBLayer layer : mbStyle.layers()) {
            MBLayer workingLayer = layer;
            if (layer.getPaint() != null ) {
                traverse(workingLayer.getPaint(), range);
            }
            mbStyle.layers().remove(layer);
            mbStyle.layers().add(workingLayer);
            System.out.println(workingLayer.paint().toJSONString());
        }

        return mbStyle;
    }

    public static List<Long> getZoomLevels(MBStyle mbStyle) {
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
                            if (((Long) stop.get(0)).longValue() >= range[0] &&
                                    ((Long) stop.get(0)).longValue() <= range[1]) {

                            } else {
                                objectsToRemove.add(stops.get(i));
                            }
                        }
                        if (stop.get(0) instanceof JSONObject) {
                            if (((Long)((JSONObject) stop.get(0)).get("zoom")).longValue() >= range[0] &&
                                    ((Long)((JSONObject) stop.get(0)).get("zoom")).longValue() <= range[1]) {
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
            }
        }
        System.out.println(jsonObject.toJSONString());
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



//    static JSONArray propertyReduce(JSONArray layers) {
//        for (int i = 0; i < layers.size(); i++) {
//            List<String> keysToRemove = new ArrayList<>();
//            JSONObject layer = (JSONObject) layers.get(i);
//            Set<?> keySet = layer.keySet();
//            Iterator<?> keys = keySet.iterator();
//            while (keys.hasNext() && !hasStops) {
//                String key = (String)keys.next();
//                if (layer.get(key) instanceof JSONObject) {
//                    containsBaseOrStops((JSONObject) layer.get(key));
//                    if (!hasStops) {
//                        keysToRemove.add(key);
//                    }
//                } else {
//                    keysToRemove.add(key);
//                }
//                hasStops = false;
//            }
//            for (String k : keysToRemove) {
//                if ((!k.equalsIgnoreCase("id")) && (!k.equalsIgnoreCase("type"))) {
//                    layer.remove(k);
//                }
//            }
//
//        }
//        return layers;
//    }

//    static List<MBLayer> getLayersWithStops(JSONArray layerArray) {
//        JSONArray stops = new JSONArray();
//        for (int i = 0; i < layerArray.size(); i++) {
//            // iterate over the keys of each layer object
//            JSONObject layer = (JSONObject) layerArray.get(i);
//            Boolean layerHasStops = containsBaseOrStops(layer);
//            if (layerHasStops) {
//                stops.add(layer);
//                hasStops = false;
//            }
//            hasStops = false;
//        }
//
//        return null;
//    }

//    static Boolean containsBaseOrStops(JSONObject jsonObject) {
//        if (jsonObject.containsKey("base")) {
//            return hasStops = true;
//        }
//        if (jsonObject.containsKey("stops")) {
//            return hasStops = true;
//        }
//        // This object doesn't contain our values, let's dig a little deeper.
//        Set<?> keySet = jsonObject.keySet();
//        Iterator<?> keys = keySet.iterator();
//        while (keys.hasNext() && !hasStops) {
//            String key = (String)keys.next();
//            if (jsonObject.get(key) instanceof JSONObject) {
//                containsBaseOrStops((JSONObject) jsonObject.get(key));
//            }
//        }
//
//        return hasStops;
//    }
}
