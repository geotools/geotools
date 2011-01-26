/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import java.io.IOException;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.jdbc.JDBC1DataStore;
import org.geotools.data.jdbc.JDBCFeatureStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;

public class OracleFeatureStore extends JDBCFeatureStore {

        public OracleFeatureStore(JDBC1DataStore jdbcDataStore, SimpleFeatureType featureType) {
        super(jdbcDataStore, featureType);
        
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    /**
     * Retrieve Bounds of Query results.
     * 
     * <p>
     * Currently returns null, consider getFeatures( query ).getBounds()
     * instead.
     * </p>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param query Query we are requesting the bounds of
     *
     * @return null representing the lack of an optimization
     *
     * @throws IOException DOCUMENT ME!
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        if(query.getTypeName() == null) {
            query = new DefaultQuery(query);
            ((DefaultQuery) query).setTypeName(getSchema().getTypeName());
        }
        return ((OracleDataStore) getDataStore()).bounds(query);
    }

    
}
