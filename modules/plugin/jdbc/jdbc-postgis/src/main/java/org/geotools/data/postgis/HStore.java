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
package org.geotools.data.postgis;

import java.util.HashMap;
import java.util.Map;

public class HStore extends HashMap<String, String> {

    private static final long serialVersionUID = -2696388478311744741L;

    public static final String TYPENAME = "hstore";

    private static final String EMPTY = "{}";

    private static final String NULL = "null";

    public HStore(Map<String, String> map) {
        if (map != null) {
            putAll(map);
        }
    }

    @Override
    public String toString() {
        // Quick shortcut
        if (isEmpty()) {
            return EMPTY;
        }
        // Setup a json output via code, avoiding to include JSON artifacts dependencies
        StringBuilder sb = new StringBuilder("{");
        String prefix = "";
        for (Map.Entry<String, String> entry : entrySet()) {
            sb.append(prefix);
            sb.append(doubleQuoteString(entry.getKey())).append(":").append(doubleQuoteString(entry.getValue()));
            prefix = ",";
        }
        sb.append("}");
        return sb.toString();
    }

    private static final String doubleQuoteString(String string) {
        return string != null ? "\"" + string + "\"" : NULL;
    }
}
