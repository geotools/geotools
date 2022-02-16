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

import java.io.IOException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.GeometryCollector;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.opengis.util.ProgressListener;

/**
 * Collects all geometries from the specified vector layer into a single GeometryCollection (or
 * specialized subclass of it in case the geometries are uniform)
 *
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(
        title = "Collect Geometries",
        description =
                "Collects the default geometries of the input features and combines them into a single geometry collection")
public class CollectGeometries implements VectorProcess {

    @DescribeResult(name = "result", description = "Geometry collection of all input geometries")
    public GeometryCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    FeatureCollection features,
            ProgressListener progressListener)
            throws IOException {
        int count = features.size();
        float done = 0;

        GeometryCollector collector = new GeometryCollector();
        try (FeatureIterator fi = features.features()) {
            while (fi.hasNext()) {
                Geometry g = (Geometry) fi.next().getDefaultGeometryProperty().getValue();
                collector.add(g);

                // progress notification
                done++;
                if (progressListener != null && done % 100 == 0) {
                    progressListener.progress(done / count);
                }
            }
        }
        return collector.collect();
    }
}
