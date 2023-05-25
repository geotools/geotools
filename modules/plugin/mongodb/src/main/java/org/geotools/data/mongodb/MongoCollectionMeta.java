/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb;

import java.util.Collections;
import java.util.Map;

/** Class used to store metadata of mongo collection */
public class MongoCollectionMeta {

    // Map with index information where key is name of the indexed field and value is its type
    private final Map<String, String> indexeMap;

    public MongoCollectionMeta(Map<String, String> indexes) {
        this.indexeMap = indexes;
    }

    public Map<String, String> getIndexes() {
        if (indexeMap == null) {
            return Collections.emptyMap();
        }
        return indexeMap;
    }
}
