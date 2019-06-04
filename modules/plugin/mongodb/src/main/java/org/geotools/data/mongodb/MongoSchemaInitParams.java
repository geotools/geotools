/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Holds schema generation parameters for MongoDB datastore.
 */
public class MongoSchemaInitParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> ids;
    private int maxObjects = 1;

    private MongoSchemaInitParams(List<String> ids, Integer maxObjects) {
        super();
        this.ids = ids != null ? ids : Collections.emptyList();
        this.maxObjects = maxObjects;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public int getMaxObjects() {
        return maxObjects;
    }

    public void setMaxObjects(int maxObjects) {
        this.maxObjects = maxObjects;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<String> ids;
        private int maxObjects = 1;

        private Builder() {}

        public Builder ids(String... ids) {
            this.ids = Arrays.asList(ids);
            return this;
        }

        public Builder maxObjects(int maxObjects) {
            this.maxObjects = maxObjects;
            return this;
        }

        public MongoSchemaInitParams build() {
            return new MongoSchemaInitParams(ids, maxObjects);
        }
    }
}
