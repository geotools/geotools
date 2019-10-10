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
 *
 */

package org.geotools.data.ogr;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataSourceException;

class OGRDataSourcePool implements AutoCloseable {

    protected final GenericObjectPool readOnlyPool;
    private final String ogrSourceName;
    private final String ogrDriver;
    private final OGR ogr;
    private long lastClear;
    private final boolean primeDataSources;

    public OGRDataSourcePool(OGR ogr, String ogrSourceName, String ogrDriver, Map params)
            throws IOException {
        this.ogrSourceName = ogrSourceName;
        this.ogrDriver = ogrDriver;
        this.ogr = ogr;
        this.lastClear = System.nanoTime();

        // grab the pool configuration
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.maxIdle = (int) lookup(OGRDataStoreFactory.MAXCONN, params);
        config.minIdle = (int) lookup(OGRDataStoreFactory.MINCONN, params);
        config.maxWait = (int) lookup(OGRDataStoreFactory.MAXWAIT, params) * 1000l;
        config.minEvictableIdleTimeMillis =
                (int) lookup(OGRDataStoreFactory.MIN_EVICTABLE_TIME, params) * 1000l;
        config.numTestsPerEvictionRun =
                (int) lookup(OGRDataStoreFactory.EVICTOR_TESTS_PER_RUN, params);
        this.readOnlyPool = new GenericObjectPool(new PooledDataSourceFactory(), config);

        this.primeDataSources = (boolean) lookup(OGRDataStoreFactory.PRIME_DATASOURCE, params);
    }

    private Object lookup(DataAccessFactory.Param param, Map params) throws IOException {
        return Optional.ofNullable(param.lookUp(params)).orElse(param.getDefaultValue());
    }

    public OGRDataSource getDataSource(boolean update) throws IOException {
        OGRDataSource result;
        if (!update) {
            try {
                result = (OGRDataSource) readOnlyPool.borrowObject();
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException(e);
            }
        } else {
            Object source = getRawDataSource(true);
            if (source == null) {
                return null;
            }
            result = new OGRDataSource(ogr, this, source, update);
        }

        return result;
    }

    OGRDataSource openOrCreateDataSource(String[] options) throws IOException {
        Object source = null;
        try {
            source = getRawDataSource(true);
        } catch (IOException e) {
            if (ogrDriver != null) {
                Object driver = ogr.GetDriverByName(ogrDriver);
                if (driver != null) {
                    source = ogr.DriverCreateDataSource(driver, ogrSourceName, options);
                    ogr.DriverRelease(driver);
                }
                if (source == null)
                    throw new IOException(
                            "Could not create OGR data source with driver "
                                    + ogrDriver
                                    + " and options "
                                    + Arrays.toString(options));
            } else {
                throw new DataSourceException(
                        "Driver not provided, and could not " + "open data source neither");
            }
        }
        return new OGRDataSource(ogr, this, source, true);
    }

    private Object getRawDataSource(boolean update) throws IOException {
        int mode = update ? 1 : 0;
        Object ds;
        if (ogrDriver != null) {
            Object driver = ogr.GetDriverByName(ogrDriver);
            if (driver == null) {
                throw new IOException("Could not find a driver named " + driver);
            }
            ds = ogr.DriverOpen(driver, ogrSourceName, mode);
            if (ds == null) {
                throw new IOException(
                        "OGR could not open '"
                                + ogrSourceName
                                + "' in "
                                + (update ? "read-write" : "read-only")
                                + " mode with driver "
                                + ogrDriver);
            }
        } else {
            ds = ogr.OpenShared(ogrSourceName, mode);
            if (ds == null) {
                throw new IOException(
                        "OGR could not open '"
                                + ogrSourceName
                                + "' in "
                                + (update ? "read-write" : "read-only")
                                + " mode");
            }
        }

        return ds;
    }

    public void close() {
        try {
            readOnlyPool.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Clears the pooled entries */
    public void clear() {
        this.lastClear = System.nanoTime();
        readOnlyPool.clear();
    }

    /**
     * Retruns a data source to the pool
     *
     * @param ogrDataSource The data source
     */
    public void returnObject(OGRDataSource ogrDataSource) {
        // clear pool when returning a source that was used for writes
        if (ogrDataSource.update) {
            clear();
        }
        // if the source was created before the last clear, it means that it was meant to be
        // cleared too, so destroy it
        if (ogrDataSource.creationTime < lastClear || ogrDataSource.update) {
            ogrDataSource.destroy();
        } else {
            try {
                readOnlyPool.returnObject(ogrDataSource);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    private class PooledDataSourceFactory extends BasePoolableObjectFactory {

        @Override
        public Object makeObject() throws Exception {
            Object source = getRawDataSource(false);
            OGRDataSource ds = new OGRDataSource(ogr, OGRDataSourcePool.this, source, false);
            ds.setPrimeLayersEnabled(primeDataSources);
            return ds;
        }

        @Override
        public void destroyObject(Object obj) throws Exception {
            OGRDataSource ds = (OGRDataSource) obj;
            ds.destroy();
        }
    }
}
