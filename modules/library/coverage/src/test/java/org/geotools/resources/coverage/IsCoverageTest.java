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
package org.geotools.resources.coverage;

import static org.junit.Assert.*;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageTestBase;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * @author Davide Savazzi - GeoSolutions
 */
public class IsCoverageTest extends GridCoverageTestBase {

    @Test
    public void testEvaluateFeature() throws Exception {
        Filter isCoverage = ECQL.toFilter("isCoverage() = true");
        Filter isNotCoverage = ECQL.toFilter("isCoverage() = false");

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
        assertEquals(false, isCoverage.evaluate(feature));
        assertEquals(true, isNotCoverage.evaluate(feature));        
    }
    
    @Test 
    public void testSimplify() throws Exception {
        Filter isCoverage = ECQL.toFilter("isCoverage() = true");

        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        Filter result = (Filter) isCoverage.accept(visitor, null);
        
        assertEquals(isCoverage, result);
    }
}