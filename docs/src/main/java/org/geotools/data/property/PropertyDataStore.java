package org.geotools.data.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class PropertyDataStore extends AbstractDataStore {
    protected File directory;

    protected String namespaceURI;

    public PropertyDataStore(File dir) {
        this(dir, null);
    }

    public PropertyDataStore(File dir, String namespaceURI) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }
        if (namespaceURI == null) {
            namespaceURI = dir.getName();
        }
        directory = dir;
        this.namespaceURI = namespaceURI;
    }
    // definition end
    
    // getTypeNames start
    /**
     * Gets the names of feature types available in this {@code DataStore}.
     * 
     * @return array of type name's published by this datastore
     */
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

    // getTypeNames end

    // getSchema start
    /**
     * Creates a Schema (FeatureType) from the first line of the .properties file
     * 
     * @param typeName
     *            TypeName indicating the property file used
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        String typeSpec = property(typeName, "_");
        try {
            String namespace = directory.getName();
            return DataUtilities.createType(namespace + "." + typeName, typeSpec);
        } catch (SchemaException e) {
            e.printStackTrace();
            throw new DataSourceException(typeName + " schema not available", e);
        }
    }
    // getSchema end

    // property start
    /**
     * Opens the file given in typeName and reads through looking for a line that begins with key
     * and then "=".
     * 
     * @param typeName indicates file to open
     * @param key indicates the line to read
     * @return the key's values (everything to the right of the '='
     * @throws IOException
     */
    private String property(String typeName, String key) throws IOException {
        File file = new File(directory, typeName + ".properties");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.startsWith(key + "=")) {
                    return line.substring(key.length() + 1);
                }
            }
        } finally {
            reader.close();
        }
        return null;
    }
    // property end

    // getFeatureReader start
    /**
     * Implements access to the "raw" FeatureReader, this method is called internally by
     * AbstractDataStore.
     * @param typeName TypeName indicating property file to read
     * @return FeatureReader providing access to contents of file
     */
    protected FeatureReader<SimpleFeatureType,SimpleFeature> getFeatureReader(String typeName) throws IOException {
        return new PropertyFeatureReader( directory, typeName );        
    }
    // getFeatureReader end
}
