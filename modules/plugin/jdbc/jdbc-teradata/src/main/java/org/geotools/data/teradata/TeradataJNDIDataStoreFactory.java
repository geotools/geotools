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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

import java.util.Map;

import static org.geotools.data.teradata.TeradataDataStoreFactory.LOOSEBBOX;

/**
 * JNDI DataStoreFactory for Teradata database.
 * 
 * @author Jesse Eichar
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/jdbc/jdbc-teradata/src/main/java/org/geotools/data/teradata/TeradataJNDIDataStoreFactory.java $
 */
public class TeradataJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public TeradataJNDIDataStoreFactory() {
        super(new TeradataDataStoreFactory());
    }

    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);

        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
    }
}
