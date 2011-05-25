/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.filter.Filter;

/**
 * 
 * @author Justin
 *
 *
 * @source $URL$
 */
public class PostgisPermissionOnlineTest extends PostgisOnlineTestCase {

    protected String getFixtureId() {
        return "postgis.restricted";
    }

    public void testGetFeatureSource() throws IOException {

        try {
            dataStore.getFeatureSource("restricted");
            fail("user should not have been able to create SimpleFeatureSource to restricted table");
        } catch (DataSourceException e) {
        }
    }

    public void testGetFeatureWriter() throws IOException {
        try {
            dataStore.getFeatureWriter("restricted", Filter.EXCLUDE,
                    Transaction.AUTO_COMMIT);
            fail("user should not have been able to create featureWriter to restricted table");
        } catch (DataSourceException e) {
        }

        try {
            dataStore.getFeatureWriter("restricted", Transaction.AUTO_COMMIT);
            fail("user should not have been able to create featureWriter to restricted table");
        } catch (DataSourceException e) {
        }

        try {
            dataStore.getFeatureWriterAppend("restricted",
                    Transaction.AUTO_COMMIT);
            fail("user should not have been able to create featureWriter to restricted table");
        } catch (DataSourceException e) {
        }
    }

    public void testGetSchema() throws IOException {
        try {
            dataStore.getSchema("restricted");
            fail("user should not have been able to create featureWriter to restricted table");
        } catch (DataSourceException e) {
        }
    }

}
