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
package org.geotools.data.directory;

import java.io.File;
import java.io.IOException;
import org.geotools.data.DataStore;

/**
 * A delegate that finds the files managed by the directory store and
 *
 * @author Andrea Aime - OpenGeo
 */
public interface FileStoreFactory {

    /** Returns a store for the specified file */
    DataStore getDataStore(File file) throws IOException;
}
