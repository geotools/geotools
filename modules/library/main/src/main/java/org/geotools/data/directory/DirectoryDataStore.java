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
package org.geotools.data.directory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.util.URLs;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class DirectoryDataStore implements DataStore {

    DirectoryTypeCache cache;
    DirectoryLockingManager lm;

    public DirectoryDataStore(File directory, FileStoreFactory dialect) throws IOException {
        cache = new DirectoryTypeCache(directory, dialect);
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        String typeName = query.getTypeName();
        return getDataStore(typeName).getFeatureReader(query, transaction);
    }

    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        SimpleFeatureSource fs = getDataStore(typeName).getFeatureSource(typeName);
        if (fs instanceof SimpleFeatureLocking) {
            return new DirectoryFeatureLocking((SimpleFeatureLocking) fs);
        } else if (fs instanceof FeatureStore) {
            return new DirectoryFeatureStore((SimpleFeatureStore) fs);
        } else {
            return new DirectoryFeatureSource((SimpleFeatureSource) fs);
        }
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        return getDataStore(typeName).getFeatureWriter(typeName, filter, transaction);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Transaction transaction) throws IOException {
        return getDataStore(typeName).getFeatureWriter(typeName, transaction);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        return getDataStore(typeName).getFeatureWriterAppend(typeName, transaction);
    }

    public LockingManager getLockingManager() {
        if (lm == null) {
            lm = new DirectoryLockingManager(cache);
        }
        return lm;
    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getDataStore(typeName).getSchema(typeName);
    }

    public String[] getTypeNames() throws IOException {
        Set<String> typeNames = cache.getTypeNames();
        return typeNames.toArray(new String[typeNames.size()]);
    }

    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        getDataStore(typeName).updateSchema(typeName, featureType);
    }

    public void createSchema(SimpleFeatureType featureType) throws IOException {
        File f = new File(cache.directory, featureType.getTypeName() + ".shp");

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", URLs.fileToUrl(f));
        params.put("filetype", "shapefile");
        DataStore ds = null;
        try {
            ds = DataStoreFinder.getDataStore(params);
            if (ds != null) {
                ds.createSchema(featureType);
                ds.dispose();
                cache.refreshCacheContents();
            }
        } catch (Exception e) {
            throw (IOException) new IOException("Error creating new data store").initCause(e);
        }
        if (ds == null) {
            throw new IOException("Could not find the shapefile data store in the classpath");
        }
    }

    public void dispose() {
        cache.dispose();
    }

    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from Directory " + cache.directory);
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        info.setSource(cache.directory.toURI());
        try {
            info.setPublisher(new URI(System.getProperty("user.name")));
        } catch (URISyntaxException e) {
        }
        return info;
    }

    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(typeName));
        }
        return names;
    }

    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }

    /** Returns the native store for a specified type name */
    public DataStore getDataStore(String typeName) throws IOException {
        // grab the store for a specific feature type, making sure it's actually there
        DataStore store = cache.getDataStore(typeName, true);
        if (store == null) throw new IOException("Feature type " + typeName + " is unknown");
        return store;
    }

    @Override
    public void removeSchema(Name name) throws IOException {
        removeSchema(name.getLocalPart());
    }

    @Override
    public void removeSchema(String name) throws IOException {
        getDataStore(name).removeSchema(name);
    }
}
