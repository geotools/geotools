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
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

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
            throw new FileNotFoundException(
                    "Unable to create a new property file: file exists " + file);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("_=");
        writer.write(DataUtilities.encodeType(featureType));
        writer.flush();
        writer.close();
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
        String list[] =
                dir.list(
                        new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.endsWith(".properties");
                            }
                        });
        List<Name> typeNames = new ArrayList<Name>();
        for (int i = 0; i < list.length; i++) {
            String typeName = list[i].substring(0, list[i].lastIndexOf('.'));
            typeNames.add(new NameImpl(namespaceURI, typeName));
        }
        return typeNames;
    }

    @Override
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.length);
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
            throw new IOException(
                    "Can't delete " + file.getAbsolutePath() + " because it doesn't exist!");
        }
        file.delete();
    }
}
