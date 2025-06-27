/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Sample DataStore implementation, please see formal tutorial included with users docs.
 *
 * @author Jody Garnett, Refractions Research Inc.
 * @author Torben Barsballe (Boundless)
 */
public class PropertyDataStore extends ContentDataStore {
    protected File dir;

    public PropertyDataStore(File dir) {
        this(dir, null);
    }

    // constructor start
    public PropertyDataStore(File dir, String namespaceURI) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }
        if (namespaceURI == null) {
            namespaceURI = dir.getName();
        }
        this.dir = dir;
        setNamespaceURI(namespaceURI);

        // factories
        setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        setGeometryFactory(new GeometryFactory());
        setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
    }
    // constructor end

    // createSchema start
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        String typeName = featureType.getTypeName();
        File file = new File(dir, typeName + ".properties");
        if (file.exists()) {
            throw new FileNotFoundException("Unable to create a new property file: file exists " + file);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("_=");
            writer.write(DataUtilities.encodeType(featureType));
            writer.flush();
        }
    }
    // createSchema end

    // info start
    @Override
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from Directory " + dir);
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        info.setSource(dir.toURI());
        try {
            info.setPublisher(new URI(System.getProperty("user.name")));
        } catch (URISyntaxException e) {
        }
        return info;
    }
    // info end

    @Override
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    @Override
    protected java.util.List<Name> createTypeNames() throws IOException {
        String[] list = dir.list((dir, name) -> name.endsWith(".properties"));
        List<Name> typeNames = new ArrayList<>();
        if (list != null) {
            for (String s : list) {
                String typeName = s.substring(0, s.lastIndexOf('.'));
                typeNames.add(new NameImpl(namespaceURI, typeName));
            }
        }
        return typeNames;
    }

    @Override
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(namespaceURI, typeName));
        }
        return names;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        File file = new File(this.dir, entry.getTypeName() + ".properties");
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        if (file.canWrite()) {
            return new PropertyFeatureStore(entry, Query.ALL);
        } else {
            return new PropertyFeatureSource(entry, Query.ALL);
        }
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        this.removeSchema(typeName.getLocalPart());
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        if (!typeName.endsWith(".properties")) {
            typeName = typeName + ".properties";
        }
        File file = new File(dir, typeName);
        if (!file.exists()) {
            throw new IOException("Can't delete " + file.getAbsolutePath() + " because it doesn't exist!");
        }
        file.delete();
    }
}
