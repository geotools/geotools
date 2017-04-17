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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class provides the ability to find all the zoom levels within a MapBox Style and returns a reduced list
 * of only the layers and properties containing Base and Stops values.
 *
 * Created by vickdw on 4/17/17.
 */
public class MBObjectStopsFinder {
    static Boolean hasStops = false;

    public static JSONObject getAllPropertyStops(JSONObject mbObject) {
        JSONObject mbStops = new JSONObject();
        JSONArray layersWithStops = getLayersWithStops((JSONArray) mbObject.get("layers"));
        layersWithStops = propertyReduce(layersWithStops);
        mbStops.put("layers", layersWithStops);

        return mbStops;
    }

    static JSONArray propertyReduce(JSONArray layers) {
        for (int i = 0; i < layers.size(); i++) {
            List<String> keysToRemove = new ArrayList<>();
            JSONObject layer = (JSONObject) layers.get(i);
            Set<?> keySet = layer.keySet();
            Iterator<?> keys = keySet.iterator();
            while (keys.hasNext() && !hasStops) {
                String key = (String)keys.next();
                if (layer.get(key) instanceof JSONObject) {
                    containsBaseOrStops((JSONObject) layer.get(key));
                    if (!hasStops) {
                        keysToRemove.add(key);
                    }
                } else {
                    keysToRemove.add(key);
                }
                hasStops = false;
            }
            for (String k : keysToRemove) {
                layer.remove(k);
            }

        }
        return layers;
    }

    static JSONArray getLayersWithStops(JSONArray layerArray) {
        JSONArray stops = new JSONArray();
        for (int i = 0; i < layerArray.size(); i++) {
            // iterate over the keys of each layer object
            JSONObject layer = (JSONObject) layerArray.get(i);
            Boolean layerHasStops = containsBaseOrStops(layer);
            if (layerHasStops) {
                stops.add(layer);
                hasStops = false;
            }
            hasStops = false;
        }

        return stops;
    }

    static Boolean containsBaseOrStops(JSONObject jsonObject) {
        if (jsonObject.containsKey("base")) {
            return hasStops = true;
        }
        if (jsonObject.containsKey("stops")) {
            return hasStops = true;
        }
        // This object doesn't contain our values, let's dig a little deeper.
        Set<?> keySet = jsonObject.keySet();
        Iterator<?> keys = keySet.iterator();
        while (keys.hasNext() && !hasStops) {
            String key = (String)keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                containsBaseOrStops((JSONObject) jsonObject.get(key));
            }
        }

        return hasStops;
    }
}
