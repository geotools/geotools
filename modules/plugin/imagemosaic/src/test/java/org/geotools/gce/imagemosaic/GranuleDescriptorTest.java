/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.URLs;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GranuleDescriptorTest {

    @Test
    public void testBounds() throws SchemaException, ParseException {

        SimpleFeatureType schema =
                DataUtilities.createType(
                        "index", "geom:Polygon:4326,location:String,geom2:Polygon:4326");
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        Geometry geometry = new WKTReader().read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
        fb.add(geometry);
        URL resource = getClass().getResource("test-data/rgb/global_mosaic_0.png");
        fb.add(URLs.urlToFile(resource));
        fb.add(geometry.buffer(10));
        SimpleFeature feature = fb.buildFeature("xyz");

        new GranuleDescriptor(feature, null, null, null, PathType.ABSOLUTE, "location", "/tmp") {
            protected void init(
                    org.opengis.geometry.BoundingBox granuleBBOX,
                    java.net.URL granuleUrl,
                    javax.imageio.spi.ImageReaderSpi suggestedSPI,
                    org.geotools.coverage.grid.io.footprint.MultiLevelROI roiProvider,
                    boolean heterogeneousGranules,
                    boolean handleArtifactsFiltering,
                    org.geotools.util.factory.Hints hints) {
                // check the bbox is the one of the first geometry, not the entire feature
                assertEquals(0, granuleBBOX.getMinimum(0), 0d);
                assertEquals(10, granuleBBOX.getMaximum(0), 0d);
                assertEquals(0, granuleBBOX.getMinimum(1), 0d);
                assertEquals(10, granuleBBOX.getMaximum(1), 0d);
            };
        };
    }
}
