/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.resolver.data;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;

/**
 * Sample implementation of a {@link DataAccessFactory} for testing.
 *
 * <p>Enabled with a connection parameter "dbtype" of "sample-data-access".
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.6
 */
public class SampleDataAccessFactory implements DataAccessFactory {

    /** The "dbtype" connection string required to use this factory. */
    public static final String DBTYPE_STRING = "sample-data-access";

    public static final DataAccessFactory.Param DBTYPE =
            new DataAccessFactory.Param(
                    "dbtype",
                    String.class,
                    "Fixed value '" + DBTYPE_STRING + "'",
                    true,
                    DBTYPE_STRING,
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** The connection parameters required to use this factory. */
    @SuppressWarnings("serial")
    public static final Map<String, Serializable> PARAMS =
            Map.of(SampleDataAccessFactory.DBTYPE.key, SampleDataAccessFactory.DBTYPE_STRING);

    /**
     * Are these parameters for us?
     *
     * @see DataAccessFactory#canProcess(java.util.Map)
     */
    @Override
    public boolean canProcess(Map<String, ?> params) {
        return DBTYPE_STRING.equals(params.get(SampleDataAccessFactory.DBTYPE.key));
    }

    /**
     * Create a {@link SampleDataAccess}.
     *
     * @see DataAccessFactory#createDataStore(java.util.Map)
     */
    @Override
    public DataAccess<? extends FeatureType, ? extends Feature> createDataStore(
            Map<String, ?> params) throws IOException {
        return new SampleDataAccess();
    }

    /**
     * Need to implement this.
     *
     * @see DataAccessFactory#getDescription()
     */
    @Override
    public String getDescription() {
        // FIXME implement this
        return null;
    }

    /**
     * Need to implement this.
     *
     * @see DataAccessFactory#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        // FIXME implement this
        return null;
    }

    /**
     * Need to implement this.
     *
     * @see DataAccessFactory#getParametersInfo()
     */
    @Override
    public Param[] getParametersInfo() {
        // FIXME implement this
        return null;
    }

    /**
     * Returns true, as this implementation is always available.
     *
     * @see DataAccessFactory#isAvailable()
     */
    @Override
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns an empty list, containing no hints.
     *
     * @see org.geotools.util.factory.Factory#getImplementationHints()
     */
    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
