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
package org.geotools.data.shapefile.dbf;

import org.geotools.data.DataSourceException;

/**
 * Thrown when an error relating to the shapefile occurs.
 * 
 *
 * @source $URL$
 */
public class DbaseFileException extends DataSourceException {

    private static final long serialVersionUID = -6890880438911014652L;

    public DbaseFileException(String s) {
        super(s);
    }

    public DbaseFileException(String s, Throwable cause) {
        super(s, cause);
    }
}
