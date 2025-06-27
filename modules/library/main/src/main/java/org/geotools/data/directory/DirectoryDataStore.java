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
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileStoreFactory;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.util.URLs;

public class DirectoryDataStore implements DataStore {

    DirectoryTypeCache cache;
    DirectoryLockingManager lm;

    public DirectoryDataStore(File directory, FileStoreFactory dialect) throws IOException {
        cache = new DirectoryTypeCache(directory, dialect);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction transaction)
            throws IOException {
        String typeName = query.getTypeName();
        return getDataStore(typeName).getFeatureReader(query, transaction);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        SimpleFeatureSource fs = getDataStore(typeName).getFeatureSource(typeName);
        if (fs instanceof SimpleFeatureLocking) {
            return new DirectoryFeatureLocking((SimpleFeatureLocking) fs);
        } else if (fs instanceof FeatureStore) {
            return new DirectoryFeatureStore((SimpleFeatureStore) fs);
        } else {
            return new DirectoryFeatureSource(fs);
        }
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        return getDataStore(typeName).getFeatureWriter(typeName, filter, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction transaction)
            throws IOException {
        return getDataStore(typeName).getFeatureWriter(typeName, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        return getDataStore(typeName).getFeatureWriterAppend(typeName, transaction);
    }

    @Override
    public LockingManager getLockingManager() {
        if (lm == null) {
            lm = new DirectoryLockingManager(cache);
        }
        return lm;
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getDataStore(typeName).getSchema(typeName);
    }

    @Override
    public String[] getTypeNames() throws IOException {
        Set<String> typeNames = cache.getTypeNames();
        return typeNames.toArray(new String[typeNames.size()]);
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        getDataStore(typeName).updateSchema(typeName, featureType);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        File f = new File(cache.directory, featureType.getTypeName() + ".shp");

        Map<String, Serializable> params = new HashMap<>();
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

    @Override
    public void dispose() {
        cache.dispose();
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    @Override
    @SuppressWarnings("EmptyCatch")
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

    @Override
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(typeName));
        }
        return names;
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    @Override
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
