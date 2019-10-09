/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb;

import java.io.File;
import org.geotools.data.mongodb.data.SchemaStoreDirectory;

/**
 * @author ImranR
 *     <p>Provides path to Temp Directory
 */
public class TempDirectory implements SchemaStoreDirectory {

    private File schemaDirectory;
    private int priority = -1;
    private String name = "temp";

    public TempDirectory() {
        schemaDirectory = new File(System.getProperty("java.io.tmpdir"));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getDirectory() {
        return schemaDirectory;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
