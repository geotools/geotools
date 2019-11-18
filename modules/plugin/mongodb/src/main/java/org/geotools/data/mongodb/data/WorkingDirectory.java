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
package org.geotools.data.mongodb.data;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author ImranR
 *     <p>Provides path to working directory
 */
public class WorkingDirectory implements SchemaStoreDirectory {

    private File schemaDirectory;
    private int priority = 0;
    private String name = "gt_mongodb_schemas";

    public WorkingDirectory() {
        schemaDirectory = Paths.get("").toFile();
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
