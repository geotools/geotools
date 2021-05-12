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
package org.geotools.data.jdbc.datasource;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.sql.DataSource;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataSourceException;
import org.geotools.data.Parameter;
import org.geotools.util.factory.GeoTools;

/**
 * A datasource factory SPI doing JDNI lookups
 *
 * @author Administrator
 */
public class JNDIDataSourceFactory extends AbstractDataSourceFactorySpi {

    public static final Param DSTYPE =
            new Param(
                    "dstype",
                    String.class,
                    "Must be JNDI",
                    false,
                    null,
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    public static final Param JNDI_REFNAME =
            new Param(
                    "jdniReferenceName",
                    String.class,
                    "The path where the connection pool must be located",
                    true);

    private static final Param[] PARAMS = {DSTYPE, JNDI_REFNAME};

    @Override
    public DataSource createDataSource(Map<String, ?> params) throws IOException {
        return createNewDataSource(params);
    }

    @Override
    public boolean canProcess(Map<String, ?> params) {
        return super.canProcess(params) && "JNDI".equals(params.get("dstype"));
    }

    @Override
    public DataSource createNewDataSource(Map<String, ?> params) throws IOException {
        String refName = (String) JNDI_REFNAME.lookUp(params);
        try {
            return (DataSource) GeoTools.getInitialContext().lookup(refName);
        } catch (Exception e) {
            throw new DataSourceException("Could not find the specified data source in JNDI", e);
        }
    }

    @Override
    public String getDescription() {
        return "A JNDI based DataSource locator. Provide the JDNI location of a DataSource object in order to make it work";
    }

    @Override
    public Param[] getParametersInfo() {
        return PARAMS;
    }

    /** Make sure a JNDI context is available */
    @Override
    public boolean isAvailable() {
        try {
            GeoTools.getInitialContext();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
