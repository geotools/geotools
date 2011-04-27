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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Sample DataStore implementation, please see formal tutorial included with
 * users docs.
 * 
 * @author Jody Garnett, Refractions Research Inc.
 * @source $URL$
 */
public class PropertyDataStore extends AbstractDataStore {
    protected File directory;

    protected String namespaceURI;

    public PropertyDataStore(File dir) {
        this(dir, null);
    }

    // constructor start
    public PropertyDataStore(File dir, String namespaceURI) {
        super(true); // true indicates we implement getFeatureWriter
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }
        if (namespaceURI == null) {
            namespaceURI = dir.getName();
        }
        directory = dir;
        this.namespaceURI = namespaceURI;
    }

    // constructor end

    // createSchema start
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        String typeName = featureType.getTypeName();
        File file = new File(directory, typeName + ".properties");
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
        info.setDescription("Features from Directory " + directory);
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        info.setSource(directory.toURI());
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

    public String[] getTypeNames() {
        String list[] = directory.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        });
        for (int i = 0; i < list.length; i++) {
            list[i] = list[i].substring(0, list[i].lastIndexOf('.'));
        }
        return list;
    }

    // START SNIPPET: getSchema
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        // look for type name
        String typeSpec = property(typeName, "_");
        try {
            return DataUtilities.createType(namespaceURI, typeName, typeSpec);
        } catch (SchemaException e) {
            e.printStackTrace();
            throw new DataSourceException(typeName + " schema not available", e);
        }
    }

    // END SNIPPET: getSchema

    private String property(String typeName, String key) throws IOException {
        File file = new File(directory, typeName + ".properties");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            for (String line = reader.readLine(); line != null; line = reader
                    .readLine()) {
                if (line.startsWith(key + "=")) {
                    return line.substring(key.length() + 1);
                }
            }
        } finally {
            reader.close();
        }
        return null;
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            String typeName) throws IOException {
        return new PropertyFeatureReader(directory, typeName);
    }

    // getFeatureWriter start
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName) throws IOException {
        return new PropertyFeatureWriter(this, typeName);
    }

    // getFeatureWriter end
    
    // getCount start
    int cacheCount;
    long cacheTimestamp;
    protected int getCount(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {
            String typeName = query.getTypeName();
            File file = new File(directory, typeName + ".properties");
            if (cacheCount != -1 && file.lastModified() == cacheTimestamp) {
                return cacheCount;
            }
            cacheCount = PropertyDataStore.countFile(file);
            cacheTimestamp = file.lastModified();
            return cacheCount;
        }
        else {
            return -1; // too expensive count the features
        }
    }
    /** Used to carefully count the lines in the provided properties file */
    static int countFile(File file) {
        LineNumberReader reader = null;
        try {
            int skip=1; // header
            reader = new LineNumberReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if( line.startsWith("#")||line.startsWith("!")){
                    skip++; // comment
                }
                if( line.endsWith("\\") ){
                    skip++; // multiline
                }
            }
            return reader.getLineNumber() - skip;
        } catch (IOException e) {
            return -1; // could not quickly determine length
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // we tried
                }
            }
        }
    }
    // getCount end
    
    // getBounds start
    protected ReferencedEnvelope getBounds(Query query) throws IOException {
        return null; // to expensive - calculate by visiting all the features
    }
    // getBounds end
    
    // getFeatureSource start
    public SimpleFeatureSource getFeatureSource(final String typeName)
            throws IOException {
        File file = new File( this.directory, typeName+".properties");
        if( !file.exists()){
            throw new FileNotFoundException( file.getAbsolutePath() );
        }        
        if( file.canWrite() ){
            return new PropertyFeatureStore(this, typeName);
        }
        else {
            return new PropertyFeatureSource(this, typeName);
        }
        //return super.getFeatureSource(typeName);
    }
    // getFeatureSource stop
}
