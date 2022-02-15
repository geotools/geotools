/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@DescribeProcess(
        title = "Feature from Geometry",
        description = "Converts a geometry into a feature collection.")
public class FeatureProcess implements VectorProcess {

    @DescribeResult(name = "result", description = "Output feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "geometry", description = "Input geometry", min = 1)
                    Geometry geometry,
            @DescribeParameter(
                            name = "crs",
                            description =
                                    "Coordinate reference system of the input geometry (if not provided in the geometry)")
                    CoordinateReferenceSystem crs,
            @DescribeParameter(
                            name = "typeName",
                            description = "Feauturetype name for the feature collection",
                            min = 1)
                    String name) {
        // get the crs
        if (crs == null) {
            try {
                crs = (CoordinateReferenceSystem) geometry.getUserData();
            } catch (Exception e) {
                // may not have a CRS attached
            }
        }
        if (crs == null && geometry.getSRID() > 0) {
            try {
                crs = CRS.decode("EPSG:" + geometry.getSRID());
            } catch (Exception e) {
                // may not have a CRS attached
            }
        }

        // build the feature type
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(name);
        tb.add("geom", geometry.getClass(), crs);
        SimpleFeatureType schema = tb.buildFeatureType();

        // build the feature
        SimpleFeature sf = SimpleFeatureBuilder.build(schema, new Object[] {geometry}, null);
        ListFeatureCollection result = new ListFeatureCollection(schema);
        result.add(sf);
        return result;
    }
}
