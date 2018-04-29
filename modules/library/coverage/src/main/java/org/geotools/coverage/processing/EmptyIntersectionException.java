/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

/**
 * Special subclass of {@link CannotCropException} reporting that the raster space intersection
 * between the crop area and the coverage one was found to be empty
 *
 * @author Andrea Aime - GeoSolutions
 */
public class EmptyIntersectionException extends CannotCropException {

    private static final long serialVersionUID = -6145066452614446783L;

    public EmptyIntersectionException() {
        super();
    }

    public EmptyIntersectionException(String message, Throwable exception) {
        super(message, exception);
    }

    public EmptyIntersectionException(String message) {
        super(message);
    }
}
