/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

/**
 * Indicates that the object writes to one of the Shapefile related files
 * controlled by {@link ShpFiles}
 * 
 * @author jesse
 *
 *
 * @source $URL$
 */
public interface FileWriter extends FileReader {
    /**
     * An id for the writer. This is only used for debugging.
     * 
     * @return id for the writer.
     */
    String id();
}
