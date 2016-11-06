/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import java.util.Properties;


/**
 * Tests if the CRS utility class is functioning correctly when using HSQL datastore.
 */
public class HSQLCRSTest extends AbstractCRSTest {

    @Override
    protected String getFixtureId() {
        return "hsql";
    }

    @Override
    protected Properties createOfflineFixture() {
        return new Properties();
    }
    
    @Override
    protected boolean supportsED50QuickScan() {
        return true;
    }

}
