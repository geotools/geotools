/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Test setup base class that uses a static connection pool with prepared statement pooling.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaTestSetupPSPooling extends HanaTestSetupBase {

    private static volatile BasicDataSource dataSource;

    @Override
    public DataSource getDataSource() throws IOException {
        BasicDataSource ds = dataSource;
        if (ds == null) {
            synchronized (HanaTestSetupBase.class) {
                ds = dataSource;
                if (ds == null) {
                    ds = new BasicDataSource();
                    setCommonDataSourceOptions(ds);
                    ds.setPoolPreparedStatements(true);
                    ds.setMaxActive(16);
                }
                dataSource = ds;
            }
        }
        return ds;
    }
}
