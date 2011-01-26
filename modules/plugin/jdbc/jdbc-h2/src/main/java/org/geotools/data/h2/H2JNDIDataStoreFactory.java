/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.h2;

import java.io.File;

import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for H2. 
 * 
 * @author Christian Mueller
 *
 * @source $URL$
 */
public class H2JNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public H2JNDIDataStoreFactory() {
        super(new H2DataStoreFactory());
    }
 
    /**
     * @see H2DataStoreFactory#setBaseDirectory(File)
     */
    public void setBaseDirectory(File baseDirectory) {
        ((H2DataStoreFactory)delegate).setBaseDirectory(baseDirectory);
    }

    /**
     * @see H2DataStoreFactory#getBaseDirectory()
     */
    public File getBaseDirectory() {
        return ((H2DataStoreFactory)delegate).getBaseDirectory();
    }
}
