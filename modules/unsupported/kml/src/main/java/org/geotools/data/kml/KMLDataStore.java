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
package org.geotools.data.kml;

import org.locationtech.jts.geom.GeometryFactory;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
import org.opengis.feature.type.Name;

/**
 * KMLDataStore implementation
 *
 * @author Niels Charlier, Scitus Development
 * @source $URL$
 */
public class KMLDataStore extends ContentDataStore {
    protected File file;

    public KMLDataStore(File file) {
        this(file, null);
    }

    // constructor start
    public KMLDataStore(File file, String namespaceURI) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException(file + " must be a KML file");
        }
        if (namespaceURI == null) {
            if (file.getParent() != null) {
                namespaceURI = file.getParentFile().getName();
            }
        }
        this.file = file;
        setNamespaceURI(namespaceURI);
        //
        // factories
        setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        setGeometryFactory(new GeometryFactory());
        setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
    }
    // constructor end

    // info start
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from " + file);
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        info.setSource(file.toURI());
        try {
            info.setPublisher(new URI(System.getProperty("user.name")));
        } catch (URISyntaxException e) {
        }
        return info;
    }

    // info end

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    protected java.util.List<Name> createTypeNames() throws IOException {
        String name = file.getName();
        String typeName = name.substring(0, name.lastIndexOf('.'));
        List<Name> typeNames = new ArrayList<Name>();
        typeNames.add(new NameImpl(namespaceURI, typeName));
        return typeNames;
    }

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
        if (file.canWrite()) {
            return new KMLFeatureSource(entry, Query.ALL);
        } else {
            return new KMLFeatureSource(entry, Query.ALL);
        }
    }
}
