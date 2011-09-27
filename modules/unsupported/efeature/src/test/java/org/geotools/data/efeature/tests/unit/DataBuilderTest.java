/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature.tests.unit;

import java.util.Arrays;

import org.eclipse.emf.query.conditions.IDataTypeAdapter;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;
import org.geotools.data.efeature.adapters.GeometryAdapter;
import org.geotools.data.efeature.adapters.WKBAdapter;
import org.geotools.data.efeature.adapters.WKTAdapter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * This test case tests the following {@link IDataTypeAdapter}s
 * <ol>
 *  <li>{@link GeometryAdapter#DEFAULT}</li>
 *  <li>{@link WKTAdapter#DEFAULT}</li>
 *  <li>{@link WKBAdapter#DEFAULT}</li>
 * </ol>
 * 
 * against the empty WKT and WKB values given by {@link DataTypes}.
 * 
 * </p>
 * 
 * @author kengu
 *
 *
 * @source $URL$
 */
public class DataBuilderTest extends TestCase {
    
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------
    
    @org.junit.Test
    public void testWKTAdapter() throws ParseException {
        
        assertWKT(DataTypes.WKT_GEOMETRYCOLLECTION_EMPTY);
        assertWKT(DataTypes.WKT_POINT_EMPTY);
        assertWKT(EFeatureTestData.generatePointWKTs(1, Integer.MIN_VALUE, Integer.MAX_VALUE)[0]);
        assertWKT(DataTypes.WKT_LINESTRING_EMPTY);
        assertWKT(EFeatureTestData.generateLineStringWKTs(1, Integer.MIN_VALUE, Integer.MAX_VALUE,50,100)[0]);
        assertWKT(DataTypes.WKT_POLYGON_EMPTY);
        assertWKT(EFeatureTestData.generatePolygonWKTs(1, Integer.MIN_VALUE, Integer.MAX_VALUE,50,100,5,10)[0]);
        assertWKT(DataTypes.WKT_MULTIPOINT_EMPTY);
        assertWKT(DataTypes.WKT_MULTILINESTRING_EMPTY);
        assertWKT(DataTypes.WKT_MULTIPOLYGON_EMPTY);
        
    }
    
    @org.junit.Test
    public void testWKBAdapter() throws ParseException {
        
        assertWKB(DataTypes.WKB_GEOMETRYCOLLECTION_EMPTY);
        //
        // POINT EMPTY can apparently not be encoded as WKB...
        //
        //assertWKB(DataTypes.WKB_POINT_EMPTY);
        assertWKB(DataTypes.WKB_LINESTRING_EMPTY);
        assertWKB(DataTypes.WKB_POLYGON_EMPTY);
        assertWKB(DataTypes.WKB_MULTIPOINT_EMPTY);
        assertWKB(DataTypes.WKB_MULTILINESTRING_EMPTY);
        assertWKB(DataTypes.WKB_MULTIPOLYGON_EMPTY);
    }    
    
    // ----------------------------------------------------- 
    //  TestCase implementation
    // -----------------------------------------------------

    
    /**
     * Main for test runner.
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Constructor with test name.
     */
    public DataBuilderTest(String name) {
        super(name);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(DataBuilderTest.class);
    }    
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    private static void assertWKT(String wkt) throws ParseException {
        Geometry g = DataBuilder.toGeometry(wkt);
        String s = WKTAdapter.DEFAULT.toWKT(g);
        assertTrue("WKT: [" + s + "] is not equal to [" + wkt + "]",
                wkt.equals(s));
    }
    
    private static void assertWKB(byte[] wkb) throws ParseException {
        Geometry g = DataBuilder.toGeometry(wkb);
        byte[] b = WKBAdapter.DEFAULT.toWKB(g);
        assertTrue("WKB: [" + Arrays.toString(b) + "] is not equal to [" + Arrays.toString(wkb) + "]",
                Arrays.equals(wkb,b));
    }    
    

}
