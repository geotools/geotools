/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class FlatGeobufDirectoryDataStore extends ContentDataStore {

    private File directory;

    private HashMap<String, SimpleFeatureType> createFeatureTypes;

    public FlatGeobufDirectoryDataStore(File directory) {
        this.directory = directory;
        createFeatureTypes = new HashMap<>();
    }

    protected File getDirectory() {
        return directory;
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        this.removeSchema(typeName.getLocalPart());
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        if (!typeName.endsWith(".fgb")) {
            typeName = typeName + ".fgb";
        }
        File file = new File(directory, typeName);
        if (!file.exists()) {
            throw new IOException(
                    "Can't delete " + file.getAbsolutePath() + " because it doesn't exist!");
        }
        file.delete();
    }

    protected FlatGeobufDataStore getDataStore(String name) {
        File file = new File(directory, name + ".fgb");
        URL url;
        try {
            url = file.toURI().toURL();
            FlatGeobufDataStore store = new FlatGeobufDataStore(url);
            if (createFeatureTypes.containsKey(name) && !file.exists()) {
                SimpleFeatureType featureType = createFeatureTypes.get(name);
                store.createSchema(featureType);
            }
            return store;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".fgb"));
        List<Name> names = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (createFeatureTypes.containsKey(name)) {
                    createFeatureTypes.remove(name);
                }
                names.add(new NameImpl(name.substring(0, name.lastIndexOf('.'))));
            }
        }
        for (String createName : createFeatureTypes.keySet()) names.add(new NameImpl(createName));
        return names;
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        createFeatureTypes.put(featureType.getTypeName(), featureType);
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry contentEntry)
            throws IOException {
        return getDataStore(contentEntry.getTypeName()).createFeatureSource(contentEntry);
    }
}
