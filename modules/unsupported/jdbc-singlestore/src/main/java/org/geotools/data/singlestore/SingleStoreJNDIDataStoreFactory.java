/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for mysql database.
 *
 * @author Christian Mueller
 */
// temporary work around, the factory parameters map will be fixed separately
@SuppressWarnings("unchecked")
public class SingleStoreJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public SingleStoreJNDIDataStoreFactory() {
        super(new SingleStoreDataStoreFactory());
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
    }
}
