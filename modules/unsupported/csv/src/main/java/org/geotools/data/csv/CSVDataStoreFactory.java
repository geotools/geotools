/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 * 	  (c) 2014 - 2015 Open Source Geospatial Foundation - all rights reserved
 * 	  (c) 2012 - 2014 OpenPlans
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
package org.geotools.data.csv;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.csv.parse.CSVAttributesOnlyStrategy;
import org.geotools.data.csv.parse.CSVLatLonStrategy;
import org.geotools.data.csv.parse.CSVSpecifiedWKTStrategy;
import org.geotools.data.csv.parse.CSVStrategy;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.util.KVP;

import com.vividsolutions.jts.geom.GeometryFactory;

public class CSVDataStoreFactory implements FileDataStoreFactorySpi {

    private static final String FILE_TYPE = "csv";

    public static final String[] EXTENSIONS = new String[] { "." + FILE_TYPE };

    public static final Param FILE_PARAM = new Param("file", File.class, FILE_TYPE + " file", false);

    public static final Param URL_PARAM = new Param("url", URL.class, FILE_TYPE + " file", false);

    public static final Param NAMESPACEP = new Param("namespace", URI.class,
            "uri to the namespace", false, null, new KVP(Param.LEVEL, "advanced"));

    public static final Param STRATEGYP = new Param("strategy", String.class, "strategy", false);

    public static final Param LATFIELDP = new Param("latField", String.class,
            "Latitude field. Assumes a CSVSpecifiedLatLngStrategy", false);

    public static final Param LnGFIELDP = new Param("lngField", String.class,
            "Longitude field. Assumes a CSVSpecifiedLatLngStrategy", false);

    public static final Param WKTP = new Param("wktField", String.class,
            "WKT field. Assumes a CSVSpecifiedWKTStrategy", false);

    public static final Param[] parametersInfo = new Param[] { FILE_PARAM, NAMESPACEP, STRATEGYP,
            LATFIELDP, LnGFIELDP, WKTP };

    @Override
    public String getDisplayName() {
        return FILE_TYPE.toUpperCase();
    }

    @Override
    public String getDescription() {
        return "Comma delimited text file";
    }

    @Override
    public Param[] getParametersInfo() {
        return parametersInfo;
    }

    private boolean canProcessExtension(String filename) {
        String extension = FilenameUtils.getExtension(filename);
        return FILE_TYPE.equalsIgnoreCase(extension);
    }

    private File fileFromParams(Map<String, Serializable> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        if (file != null) {
            return file;
        }
        URL url = (URL) URL_PARAM.lookUp(params);
        if (url != null) {
            return DataUtilities.urlToFile(url);
        }
        return null;
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        try {
            File file = fileFromParams(params);
            if (file != null) {
                return canProcessExtension(file.getPath());
            }
        } catch (IOException e) {
        }
        return false;
    }

    @Override
    public boolean isAvailable() {
        try {
            CSVDataStore.class.getName();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    public FileDataStore createDataStoreFromFile(File file) throws IOException {
        return createDataStoreFromFile(file, null);
    }

    public FileDataStore createDataStoreFromFile(File file, URI namespace) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Cannot create store from null file");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("Cannot create store with file that does not exist");
        }
        Map<String, Serializable> noParams = Collections.emptyMap();
        return createDataStoreFromFile(file, namespace, noParams);
    }

    @Override
    public FileDataStore createDataStore(Map<String, Serializable> params) throws IOException {
        File file = fileFromParams(params);
        if (file == null) {
            throw new IllegalArgumentException(
                    "Could not find file from params to create csv data store");
        }
        URI namespace = (URI) NAMESPACEP.lookUp(params);
        return createDataStoreFromFile(file, namespace, params);
    }

    private FileDataStore createDataStoreFromFile(File file, URI namespace,
            Map<String, Serializable> params) throws IOException {
        CSVFileState csvFileState = new CSVFileState(file, namespace);
        Object strategyParam = STRATEGYP.lookUp(params);
        CSVStrategy csvStrategy = null;
        if (strategyParam != null) {
            String strategyString = strategyParam.toString();
            if (strategyString.equalsIgnoreCase("guess")) {
                csvStrategy = new CSVLatLonStrategy(csvFileState);
            } else if (strategyString.equalsIgnoreCase("specify")) {
                Object latParam = LATFIELDP.lookUp(params);
                Object lngParam = LnGFIELDP.lookUp(params);
                if (latParam == null || lngParam == null) {
                    throw new IllegalArgumentException(
                            "'specify' csv strategy selected, but lat/lng params both not specified");
                }
                csvStrategy = new CSVLatLonStrategy(csvFileState, latParam.toString(),
                        lngParam.toString());
            } else if (strategyString.equalsIgnoreCase("wkt")) {
                Object wktParam = WKTP.lookUp(params);
                if (wktParam == null) {
                    throw new IllegalArgumentException(
                            "'wkt' csv strategy selected, but wktField param not specified");
                }
                csvStrategy = new CSVSpecifiedWKTStrategy(csvFileState, wktParam.toString());
            } else {
                csvStrategy = new CSVAttributesOnlyStrategy(csvFileState);
            }
        } else {
            csvStrategy = new CSVAttributesOnlyStrategy(csvFileState);
        }
        CSVDataStore store = new CSVDataStore(csvFileState, csvStrategy);
        if (namespace != null) {
            store.setNamespaceURI(namespace.toString());
        }
        store.setDataStoreFactory(this);
        store.setGeometryFactory(new GeometryFactory());
        store.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        store.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
        return store;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return createDataStore(params);
    }

    @Override
    public FileDataStore createDataStore(URL url) throws IOException {
        File file = DataUtilities.urlToFile(url);
        return createDataStoreFromFile(file);
    }

    @Override
    public String[] getFileExtensions() {
        return EXTENSIONS;
    }

    @Override
    public boolean canProcess(URL url) {
        return canProcessExtension(DataUtilities.urlToFile(url).toString());
    }

    @Override
    public String getTypeName(URL url) throws IOException {
        DataStore ds = createDataStore(url);
        String[] names = ds.getTypeNames();
        assert names.length == 1 : "Invalid number of type names for csv file store";
        ds.dispose();
        return names[0];
    }
}
