/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.util.List;

/** @author Daniele Romagnoli, GeoSolutions SaS */
public interface FileSetManager {

    /** Add a file to the file set manager */
    void addFile(final String filePath);

    /** Return the list of all the files currently added to the manager */
    List<String> list();

    /** Remove a file from the manager (An implementation may also physically remove the file) */
    void removeFile(final String filePath);

    /**
     * Remove all the files from the manager (An implementation may also physically remove all the
     * files)
     */
    void purge();
}
