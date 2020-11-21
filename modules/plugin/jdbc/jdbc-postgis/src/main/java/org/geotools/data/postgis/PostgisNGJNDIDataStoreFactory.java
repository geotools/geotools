/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.geotools.data.postgis.PostgisNGDataStoreFactory.ENCODE_FUNCTIONS;
import static org.geotools.data.postgis.PostgisNGDataStoreFactory.ESTIMATED_EXTENTS;
import static org.geotools.data.postgis.PostgisNGDataStoreFactory.LOOSEBBOX;
import static org.geotools.data.postgis.PostgisNGDataStoreFactory.PREPARED_STATEMENTS;
import static org.geotools.data.postgis.PostgisNGDataStoreFactory.SIMPLIFICATION_METHOD;
import static org.geotools.data.postgis.PostgisNGDataStoreFactory.SIMPLIFY;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for Postgis database.
 *
 * @author Christian Mueller
 */
// temporary work around, the factory parameters map will be fixed separately
@SuppressWarnings("unchecked")
public class PostgisNGJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public PostgisNGJNDIDataStoreFactory() {
        super(new PostgisNGDataStoreFactory());
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);

        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(PREPARED_STATEMENTS.key, PREPARED_STATEMENTS);
        parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
        parameters.put(SIMPLIFY.key, SIMPLIFY);
        parameters.put(SIMPLIFICATION_METHOD.key, SIMPLIFICATION_METHOD);
    }
}
