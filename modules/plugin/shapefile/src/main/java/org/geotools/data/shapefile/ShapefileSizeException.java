/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

/**
 * Exception thrown by the writer when the maximum size of the shapefile or dbf is reached. If the
 * client has a direct reference to the feature writer the exception is recoverable, the writer can
 * be safely closed and both shp and dbf files are currently written will be within the specified
 * size limits
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ShapefileSizeException extends IOException {
    private static final long serialVersionUID = 6539095903426714802L;

    public ShapefileSizeException(String message) {
        super.getMessage();
    }
}
