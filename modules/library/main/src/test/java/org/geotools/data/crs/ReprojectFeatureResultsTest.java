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
package org.geotools.data.crs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.ProgressListener;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/** Test ForceCoordinateSystemFeatureResults feature collection wrapper. */
public class ReprojectFeatureResultsTest {

    private static final String FEATURE_TYPE_NAME = "testType";
    private CoordinateReferenceSystem wgs84;
    private CoordinateReferenceSystem utm32n;

    FeatureVisitor lastVisitor = null;
    private ListFeatureCollection visitorCollection;

    @Before
    public void setUp() throws Exception {
        lastVisitor = null;
        wgs84 = CRS.decode("EPSG:4326");
        utm32n = CRS.decode("EPSG:32632");

        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(FEATURE_TYPE_NAME);
        builder.setCRS(wgs84);
        builder.add("geom", Point.class);

        SimpleFeatureType ft = builder.buildFeatureType();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(ft);
        b.add(p);

        visitorCollection = new ListFeatureCollection(ft) {
            @Override
            public void accepts(FeatureVisitor visitor, ProgressListener progress) throws java.io.IOException {
                lastVisitor = visitor;
            }

            @Override
            public SimpleFeatureCollection subCollection(Filter filter) {
                if (filter == Filter.INCLUDE) {
                    return this;
                } else {
                    return super.subCollection(filter);
                }
            }
        };
    }

    @Test
    public void testMaxVisitorDelegation() throws Exception {
        MaxVisitor visitor =
                new MaxVisitor(CommonFactoryFinder.getFilterFactory().property("value"));
        assertOptimalVisit(visitor);
    }

    @Test
    public void testCountVisitorDelegation() throws Exception {
        FeatureVisitor visitor = new CountVisitor();
        assertOptimalVisit(visitor);
    }

    private void assertOptimalVisit(FeatureVisitor visitor) throws Exception {
        SimpleFeatureCollection retypedCollection = new ReprojectFeatureResults(visitorCollection, utm32n);
        retypedCollection.accepts(visitor, null);
        assertSame(lastVisitor, visitor);
    }

    @Test
    public void testBoundsNotOptimized() throws Exception {
        BoundsVisitor boundsVisitor = new BoundsVisitor();
        SimpleFeatureCollection retypedCollection = new ReprojectFeatureResults(visitorCollection, utm32n);
        retypedCollection.accepts(boundsVisitor, null);
        // not optimized
        assertNull(lastVisitor);
    }

    @Test
    public void testBoundsCRS() throws Exception {
        SimpleFeatureCollection retypedCollection = new ReprojectFeatureResults(visitorCollection, utm32n);
        ReferencedEnvelope bounds = retypedCollection.getBounds();
        assertEquals(utm32n, bounds.getCoordinateReferenceSystem());
    }
}
