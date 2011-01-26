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
package org.geotools.data.directory;

/**
 * Implementors will provide a service that checks if directory contents are
 * changed since last refresh.
 * 
 * @author Andrea Aime
 * 
 */
interface DirectoryWatcher {

    /**
     * Reports if the directory last modified has changed since {@link #mark()}
     * was last called
     */
    public boolean isStale();

    /**
     * Marks the time the directory has been last checked at
     */
    public void mark();
}
