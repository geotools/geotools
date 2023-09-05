/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageTestBase;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

/** @author Davide Savazzi - GeoSolutions */
public class IsCoverageTest extends GridCoverageTestBase {

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testEvaluateFeature() throws Exception {
        Filter isCoverage = FF.equals(FF.function("isCoverage"), FF.literal("true"));
        Filter isNotCoverage = FF.equals(FF.function("isCoverage"), FF.literal("false"));

        // a coverage
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GridCoverage2D coverage = getRandomCoverage(crs);
        SimpleFeatureCollection featureCollection = FeatureUtilities.wrapGridCoverage(coverage);
        SimpleFeature feature = featureCollection.features().next();
        assertTrue(isCoverage.evaluate(feature));
        assertFalse(isNotCoverage.evaluate(feature));

        // not a coverage
        SimpleFeatureType type = DataUtilities.createType("ns", "name:string,geom:Geometry");
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add("testName");
        build.add(null);
        feature = build.buildFeature(null);
        assertFalse(isCoverage.evaluate(feature));
        assertTrue(isNotCoverage.evaluate(feature));
    }

    @Test
    public void testSimplify() throws Exception {
        Filter isCoverage = FF.equals(FF.function("isCoverage"), FF.literal("true"));

        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        Filter result = (Filter) isCoverage.accept(visitor, null);

        assertEquals(isCoverage, result);
    }
}
