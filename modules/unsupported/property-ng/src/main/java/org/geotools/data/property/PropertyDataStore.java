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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Sample DataStore implementation, please see formal tutorial included with
 * users docs.
 * 
 * @author Jody Garnett, Refractions Research Inc.
 *
 *
 * @source $URL$
 */
public class PropertyDataStore extends ContentDataStore {
    protected File file;

    public PropertyDataStore(File dir) {
        this(dir, null);
    }

    // constructor start
    public PropertyDataStore(File file, String namespaceURI) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException(file + " must be a property file");
        }
        if (namespaceURI == null) {
            if( file.getParent() != null ){
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

    // createSchema start
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        if( file.exists() ){
            throw new FileNotFoundException("Unable to create a new property file: file exists "+file);
        }
        String typeName = featureType.getTypeName();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("_=");
        writer.write(DataUtilities.spec(featureType));
        writer.flush();
        writer.close();
    }

    // createSchema end

    // info start
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from " + file );
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
        String typeName = name.substring(0,name.lastIndexOf('.'));
        List<Name> typeNames = new ArrayList<Name>();
        typeNames.add( new NameImpl(namespaceURI, typeName));
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
        if( file.canWrite() ){
            return new PropertyFeatureStore( entry, Query.ALL );
        }
        else {
            return new PropertyFeatureSource( entry, Query.ALL );
        }
    }


}
