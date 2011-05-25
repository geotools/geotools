/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Sample implementation of a {@link DataAccessFactory} for testing.
 * 
 * <p>
 * 
 * Enabled with a connection parameter "dbtype" of "sample-data-access".
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.6
 */
public class SampleDataAccessFactory implements DataAccessFactory {

    /**
     * The "dbtype" connection string required to use this factory.
     */
    public static final String DBTYPE_STRING = "sample-data-access";

    public static final DataAccessFactory.Param DBTYPE = new DataAccessFactory.Param("dbtype",
            String.class, "Fixed value '" + DBTYPE_STRING + "'", true, DBTYPE_STRING);

    /**
     * The connection parameters required to use this factory.
     */
    @SuppressWarnings("serial")
    public static final HashMap<String, Serializable> PARAMS = new HashMap<String, Serializable>() {
        {
            put(SampleDataAccessFactory.DBTYPE.key, SampleDataAccessFactory.DBTYPE_STRING);
        }
    };

    /**
     * Are these parameters for us?
     * 
     * @see org.geotools.data.DataAccessFactory#canProcess(java.util.Map)
     */
    public boolean canProcess(Map<String, Serializable> params) {
        return DBTYPE_STRING.equals(params.get(SampleDataAccessFactory.DBTYPE.key));
    }

    /**
     * Create a {@link SampleDataAccess}.
     * 
     * @see org.geotools.data.DataAccessFactory#createDataStore(java.util.Map)
     */
    public DataAccess<? extends FeatureType, ? extends Feature> createDataStore(
            Map<String, Serializable> params) throws IOException {
        return new SampleDataAccess();
    }

    /**
     * Need to implement this.
     * 
     * @see org.geotools.data.DataAccessFactory#getDescription()
     */
    public String getDescription() {
        // FIXME implement this
        return null;
    }

    /**
     * Need to implement this.
     * 
     * @see org.geotools.data.DataAccessFactory#getDisplayName()
     */
    public String getDisplayName() {
        // FIXME implement this
        return null;
    }

    /**
     * Need to implement this.
     * 
     * @see org.geotools.data.DataAccessFactory#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        // FIXME implement this
        return null;
    }

    /**
     * Returns true, as this implementation is always available.
     * 
     * @see org.geotools.data.DataAccessFactory#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns an empty list, containing no hints.
     * 
     * @see org.geotools.factory.Factory#getImplementationHints()
     */
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

}
