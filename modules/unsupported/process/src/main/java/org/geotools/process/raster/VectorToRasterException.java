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
 *
 *    NOTICE REGARDING STATUS AS PUBLIC DOMAIN WORK AND ABSENCE OF ANY WARRANTIES
 *
 *    The work (source code) was prepared by an officer or employee of the
 *    United States Government as part of that person's official duties, thus
 *    it is a "work of the U.S. Government," which is in the public domain and
 *    not elegible for copyright protection.  See, 17 U.S.C. § 105.  No warranty
 *    of any kind is given regarding the work.
 */


package org.geotools.process.raster;

import org.geotools.process.ProcessException;

/**
 * Exception class used by {@linkplain FeatureRasterizerProcess}
 * @author Steve Ansari, NOAA
 * @author Michael Bedward
 *
 *
 * @source $URL$
 */
public class VectorToRasterException extends ProcessException {

    public VectorToRasterException(String message) {
        super(message);
    }

    public VectorToRasterException(Exception ex) {
        super(ex);
    }

}
