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
package org.geotools.data.flatgeobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class FlatgeobufDataStore extends ContentDataStore {

    private File file;

    public FlatgeobufDataStore(File file) {
        this.file = file;
    }

    protected File getFile() {
        return file;
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        Name typeName = new NameImpl(name);
        return Collections.singletonList(typeName);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            FlatgeobufWriter writer = new FlatgeobufWriter(out);
            writer.writeFeatureType(featureType);
        }
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry contentEntry)
            throws IOException {
        if (!file.exists() || file.canWrite()) {
            return new FlatgeobufFeatureStore(contentEntry, Query.ALL);
        } else {
            return new FlatgeobufFeatureSource(contentEntry, Query.ALL);
        }
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        this.removeSchema(typeName.getLocalPart());
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        if (!file.exists()) {
            throw new IOException(
                    "Can't delete " + file.getAbsolutePath() + " because it doesn't exist!");
        }
        file.delete();
    }

    protected SimpleFeatureType getFeatureType() throws IOException {
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                String name = file.getName();
                name = name.substring(0, name.lastIndexOf('.'));
                FlatgeobufReader reader = new FlatgeobufReader(name, "geom", in);
                return reader.getFeatureType();
            }
        } else {
            return null;
        }
    }
}
