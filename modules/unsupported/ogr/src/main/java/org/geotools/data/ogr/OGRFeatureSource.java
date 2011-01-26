/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import org.geotools.data.AbstractFeatureSource;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureListener;
import org.opengis.feature.simple.SimpleFeatureType;

public class OGRFeatureSource extends AbstractFeatureSource {

    private OGRDataStore store;

    private SimpleFeatureType schema;

    public OGRFeatureSource(OGRDataStore store, SimpleFeatureType schema) {
        this.store = store;
        this.schema = schema;
    }

    public void addFeatureListener(FeatureListener listener) {
        store.listenerManager.addFeatureListener(this, listener);

    }

    public DataStore getDataStore() {
        return store;
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    public void removeFeatureListener(FeatureListener listener) {
        store.listenerManager.removeFeatureListener(this, listener);
    }

}
