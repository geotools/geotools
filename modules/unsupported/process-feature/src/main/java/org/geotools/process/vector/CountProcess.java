/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

/**
 * Counts the elements in the collection (useful as a WFS sidekick)
 *
 * @author Andrea Aime
 */
@DescribeProcess(
        title = "Count Features",
        description = "Computes the number of features in a feature collection.")
public class CountProcess implements VectorProcess {
    /** The functions this process can handle */
    public enum AggregationFunction {
        Average,
        Max,
        Median,
        Min,
        StdDev,
        Sum;
    }

    @DescribeResult(name = "result", description = "Number of features")
    public Number execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features)
            throws Exception {

        return features.size();
    }
}
