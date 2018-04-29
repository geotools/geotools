/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.geotools.coverage.io.catalog.DataStoreConfiguration;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentDataStore;
import org.geotools.imageio.netcdf.AncillaryFileManager;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Creates a vector store that publishes the index information of the NetCDF Store. This way the
 * user can determine which combination of coordinates have data.
 *
 * @author Niels CHarlier
 */
public class NetCDFAuxiliaryStoreFactory implements DataStoreFactorySpi {

    public static final String AUXILIARY_STORE_KEY =
            "org.geotools.coverage.io.netcdf.auxiliary.store";

    public static final Param FILE_PARAM =
            new Param("File", File.class, "NetCDF File Path", true, null, Collections.emptyMap());

    public static final Param INDEX_PARAM =
            new Param(
                    "Index", String.class, "Index File Path", false, null, Collections.emptyMap());

    public static final Param DS_PARAM =
            new Param(
                    "DataStore",
                    String.class,
                    "DataStore File Path",
                    false,
                    null,
                    Collections.emptyMap());

    public static final Param NS_PARAM =
            new Param("namespace", String.class, "Namespace", false, null, Collections.emptyMap());

    @Override
    public String getDisplayName() {
        return "NetCDF Auxiliary Store";
    }

    @Override
    public String getDescription() {
        return "Published NetCDF Index Data";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM, INDEX_PARAM, DS_PARAM, NS_PARAM};
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        try {
            File file = (File) FILE_PARAM.lookUp(params);
            String indexPath = (String) INDEX_PARAM.lookUp(params);
            String dsPath = (String) DS_PARAM.lookUp(params);
            AncillaryFileManager ancilaryFileManager =
                    new AncillaryFileManager(file, indexPath, dsPath);
            DataStoreConfiguration datastoreConfig =
                    ancilaryFileManager.getDatastoreConfiguration();
            return datastoreConfig.getDatastoreSpi().canProcess(datastoreConfig.getParams());
        } catch (NoSuchAlgorithmException | JAXBException | IOException e) {
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        return "true".equalsIgnoreCase(System.getProperty(AUXILIARY_STORE_KEY));
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        String indexPath = (String) INDEX_PARAM.lookUp(params);
        String dsPath = (String) DS_PARAM.lookUp(params);
        try {
            AncillaryFileManager ancilaryFileManager =
                    new AncillaryFileManager(file, indexPath, dsPath);
            DataStoreConfiguration datastoreConfig =
                    ancilaryFileManager.getDatastoreConfiguration();

            final DataStore delegate =
                    datastoreConfig.getDatastoreSpi().createDataStore(datastoreConfig.getParams());
            String namespace = (String) NS_PARAM.lookUp(params);
            if (namespace != null && delegate instanceof ContentDataStore) {
                ((ContentDataStore) delegate).setNamespaceURI(namespace);
            }

            // make read-only wrapper
            return new DataStore() {

                @Override
                public ServiceInfo getInfo() {
                    return delegate.getInfo();
                }

                @Override
                public void createSchema(SimpleFeatureType featureType) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void updateSchema(Name typeName, SimpleFeatureType featureType)
                        throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void removeSchema(Name typeName) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public List<Name> getNames() throws IOException {
                    return delegate.getNames();
                }

                @Override
                public SimpleFeatureType getSchema(Name name) throws IOException {
                    return delegate.getSchema(name);
                }

                @Override
                public void dispose() {
                    // do nothing
                }

                @Override
                public void updateSchema(String typeName, SimpleFeatureType featureType)
                        throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void removeSchema(String typeName) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public String[] getTypeNames() throws IOException {
                    return delegate.getTypeNames();
                }

                @Override
                public SimpleFeatureType getSchema(String typeName) throws IOException {
                    return delegate.getSchema(typeName);
                }

                @Override
                public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
                    return delegate.getFeatureSource(typeName);
                }

                @Override
                public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
                    return delegate.getFeatureSource(typeName);
                }

                @Override
                public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
                        Query query, Transaction transaction) throws IOException {
                    return delegate.getFeatureReader(query, transaction);
                }

                @Override
                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
                        String typeName, Filter filter, Transaction transaction)
                        throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
                        String typeName, Transaction transaction) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
                        String typeName, Transaction transaction) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public LockingManager getLockingManager() {
                    return delegate.getLockingManager();
                }
            };
        } catch (NoSuchAlgorithmException | JAXBException e) {
            throw new IOException(e);
        }
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException();
    }
}
