/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.factory;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.ProcessException;
import org.opengis.coverage.grid.GridGeometry;

/**
 * A simple Rendering Transformation process for testing aspects of how transformations are called.
 *
 * @author Martin Davis - OpenGeo
 */
@DescribeProcess(
        title = "SimpleVectorRTProcess",
        description = "Simple test RT process taking a vector dataset as input.")
public class VectorIdentityRTProcess {
    /** Note: for testing purposes only. A real Rendering Transformation must never store state. */
    int invertQueryValue;

    @DescribeResult(name = "result", description = "The result")
    public SimpleFeatureCollection execute(
            // process data
            @DescribeParameter(name = "data", description = "Features to process")
                    SimpleFeatureCollection data,
            @DescribeParameter(name = "value", description = "Value for testing") Integer value)
            throws ProcessException {
        if (value != invertQueryValue) {
            throw new IllegalStateException("Values do not match");
        }
        return data;
    }

    public Query invertQuery(
            @DescribeParameter(name = "value", description = "Value for testing") Integer value,
            Query targetQuery,
            GridGeometry targetGridGeometry)
            throws ProcessException {

        invertQueryValue = value;

        return targetQuery;
    }
}
