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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

class OGRDataSource {

    static final Logger LOGGER = Logging.getLogger(OGRDataSource.class);

    protected static final String TRACE_ENABLED_KEY = "gt2.ogr.trace";
    /**
     * When true, the stack trace that created a reader that wasn't closed is recorded and then
     * printed out when warning the user about this.
     */
    protected static final Boolean TRACE_ENABLED =
            "true".equalsIgnoreCase(System.getProperty(TRACE_ENABLED_KEY));

    long creationTime;
    OGRDataSourcePool pool;
    OGR ogr;
    Object source;
    final boolean update;
    Exception tracer;

    public OGRDataSource(OGR ogr, OGRDataSourcePool pool, Object source, boolean update) {
        this.ogr = ogr;
        this.pool = pool;
        this.source = source;
        this.update = update;
        this.creationTime = System.nanoTime();

        if (TRACE_ENABLED) {
            tracer = new Exception();
            tracer.fillInStackTrace();
        }
    }

    public int getLayerCount() {
        return ogr.DataSourceGetLayerCount(source);
    }

    public Object getLayer(int i) {
        return ogr.DataSourceGetLayer(source, i);
    }

    public Object getDriver() {
        return ogr.DataSourceGetDriver(source);
    }

    public Object createLayer(
            String typeName, Object spatialReference, long ogrGeomType, String[] options) {
        return ogr.DataSourceCreateLayer(source, typeName, spatialReference, ogrGeomType, options);
    }

    public Object getLayerByName(String layerName) {
        return ogr.DataSourceGetLayerByName(source, layerName);
    }

    public void close() {
        // already closed?
        if (source == null) {
            return;
        }

        // return to the pool or destroy
        if (pool != null) {
            try {
                pool.returnObject(this);
            } catch (Exception e) {
                // in case returning to the pool fails, destroy the source
                destroy();
            }
        } else {
            destroy();
        }
    }

    public Object executeSQL(String sql, Object spatialFilter) {
        return ogr.DataSourceExecuteSQL(source, sql, spatialFilter);
    }

    @Override
    protected void finalize() {
        if (source != null) {
            LOGGER.warning(
                    "There is code leaving feature readers/iterators open, this is leaking OGR DataSource native objects! To find out details, set the "
                            + TRACE_ENABLED_KEY
                            + " system variable to true");
            if (TRACE_ENABLED) {
                LOGGER.log(
                        Level.WARNING,
                        "The unclosed reader originated on this stack trace",
                        tracer);
            }
            close();
        }
    }

    void destroy() {
        ogr.DataSourceRelease(source);

        // release references
        pool = null;
        source = null;
        ogr = null;
        tracer = null;
    }
}
