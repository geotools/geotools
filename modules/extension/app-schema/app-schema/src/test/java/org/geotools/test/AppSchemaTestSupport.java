/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.test;

import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.DataAccessRegistry;
import org.geotools.xml.AppSchemaXSDRegistry;
import org.junit.AfterClass;

/**
 * @author Niels Charlier (Curtin University of Technology)
 * @source $URL$
 */
public class AppSchemaTestSupport {

    @AfterClass
    public static void oneTimeTearDown() throws Exception {
        DataAccessRegistry.unregisterAndDisposeAll();
        AppSchemaDataAccessRegistry.clearAppSchemaProperties();
        AppSchemaXSDRegistry.getInstance().dispose();
    }
}
