/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Set;

import org.geotools.data.AbstractFeatureSource;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * 
 *
 * @source $URL$
 */
public class PropertyFeatureSource extends AbstractFeatureSource {
    String typeName;
    SimpleFeatureType featureType;
    PropertyDataStore store;
    long cacheTimestamp = 0;
    ReferencedEnvelope cacheBounds = null;

    PropertyFeatureSource(PropertyDataStore propertyDataStore, String typeName)
            throws IOException {
        this.store = propertyDataStore;
        this.typeName = typeName;
        this.featureType = store.getSchema(typeName);
        this.queryCapabilities = new QueryCapabilities() {
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }

    public PropertyDataStore getDataStore() {
        return store;
    }

    public void addFeatureListener(FeatureListener listener) {
        store.listenerManager.addFeatureListener(this, listener);
    }

    public void removeFeatureListener(FeatureListener listener) {
        store.listenerManager.removeFeatureListener(this, listener);
    }

    public SimpleFeatureType getSchema() {
        return featureType;
    }

    // constructor end
    // getBounds start
    public ReferencedEnvelope getBounds() {
        File file = new File(store.directory, typeName + ".properties");
        if (cacheBounds != null && file.lastModified() == cacheTimestamp) {
            // we have the cache
            return cacheBounds;
        }
        try {
            // calculate and store in cache
            cacheBounds = getFeatures().getBounds();
            cacheTimestamp = file.lastModified();
            return cacheBounds;
        } catch (IOException e) {
        }
        // bounds are unavailable!
        return null;
    }
    // getBounds end
}
