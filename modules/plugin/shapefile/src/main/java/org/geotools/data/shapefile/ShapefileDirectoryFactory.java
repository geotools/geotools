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
package org.geotools.data.shapefile;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.geotools.data.DataUtilities;

/**
 * Creates a directory datastore pointing to a directory of shapefiles
 * 
 * @author Andrea Aime
 * 
 * @source $URL: http://svn.geotools.org/trunk/modules/unsupported/directory/src/
 *         main/java/org/geotools/data/dir/DirectoryDataStoreFactory.java $
 */
public class ShapefileDirectoryFactory extends ShapefileDataStoreFactory {
    /** The directory to be scanned for file data stores */
    public static final Param URLP = new Param("url", URL.class,
            "Directory containing geospatial files", true);

    public String getDisplayName() {
        return "Directory of spatial files (shapefiles)";
    }

    public String getDescription() {
        return "Takes a directory of shapefiles and exposes it as a data store";
    }

    public boolean canProcess(Map params) {
        // we don't try to steal single shapefiles away from the main factory
        if (super.canProcess(params)) {
            try {
                URL url = (URL) URLP.lookUp(params);
                File f = DataUtilities.urlToFile(url);
                return f != null && f.exists() && f.isDirectory();
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
