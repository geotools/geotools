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

import org.geotools.mbstyle.MapboxTestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Created by vickdw on 4/17/17.
 */
public class MBZoomStopsTest {
    //@Test
    public void getDocumentStopLevelsTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolStyleIconTest.json");
        JSONObject stopsList = MBObjectZoomLevels.getAllPropertyStops(jsonObject);

        assertEquals(22, ((JSONArray)jsonObject.get("layers")).size());
        assertEquals(13, ((JSONArray)stopsList.get("layers")).size());

        JSONArray jsonArray = (JSONArray) stopsList.get("layers");
        for (Object o : jsonArray) {
            Set<?> keySet = ((JSONObject) o).keySet();
            Iterator<?> keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String)keys.next();
                if (((JSONObject) o).get(key) instanceof JSONObject) {
                    JSONObject propertyObject = (JSONObject) ((JSONObject) o).get(key);
                    if ((propertyObject.containsKey("stops")) || (propertyObject.containsKey("base"))) {
                        assertTrue(propertyObject.containsKey("stops"));
                        assertTrue(propertyObject.containsKey("base"));
                    }
                }
            }
        }
    }
}
