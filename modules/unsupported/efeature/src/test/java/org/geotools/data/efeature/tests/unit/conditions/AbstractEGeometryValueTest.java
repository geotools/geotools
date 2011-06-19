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
package org.geotools.data.efeature.tests.unit.conditions;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.Condition;
import org.geotools.data.efeature.query.EFeatureEncoderException;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsFactory;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author kengu - 14. juni 2011
 *
 */
public abstract class AbstractEGeometryValueTest extends AbstractEAttributeFilterTest<EFeatureData<Object, Geometry>> {
    
    // ---------------------------------------------
    //  Binary operators
    // ---------------------------------------------
    
    /** The {@literal BBOX} operator. */
    public static final int BBOX = 1;

    /** The {@literal BEYOND} operator. */
    public static final int BEYOND = 2;

    /** The {@literal CONTAINS} operator. */
    public static final int CONTAINS = 4;

    /** The {@literal CROSSES} operator. */
    public static final int CROSSES = 8;

    /** The {@literal DISJOINT} operator. */
    public static final int DISJOINT = 16;

    /** The {@literal DWITHIN} operator. */
    public static final int DWITHIN = 32;

    /** The {@literal EQUALS} operator. */
    public static final int EQUALS = 64;

    /** The {@literal INTERSECTS} operator. */
    public static final int INTERSECTS = 128;
    
    /** The {@literal OVERLAPS} operator. */
    public static final int OVERLAPS = 256;

    /** The {@literal TOUCHES}  operator. */
    public static final int TOUCHES = 512;    
    
    /** The {@literal WITHIN} operator. */
    public static final int WITHIN = 1024;

    // ----------------------------------------------------- 
    //  Operation sets group on value type 
    // -----------------------------------------------------
        
    /** Geometries: All operations */
    public static final int GEOMETRY_OPS = BBOX | BEYOND | CONTAINS
                                         | CROSSES | DISJOINT | DWITHIN 
                                         | EQUALS | INTERSECTS | OVERLAPS 
                                         | TOUCHES | WITHIN;

    // ----------------------------------------------------- 
    //  Cached tests specifications
    // -----------------------------------------------------
    
    /** 
     * {@link #BBOX} operation test specification.
     * <p>
     * This is a binary operation requiring two unique geometries, 
     * one having a boundary that overlaps the boundary of the other.
     */
    public static final Tests BBOX_TESTS = new Tests(nameOf(BBOX), 
            new Pass(true,0,1)); 
    
    /** 
     * {@link #BEYOND} operation test specification.
     * <p>
     * This is a ternary operation requiring three unique geometries,
     * two unique geometries, and the last which is the minimum separating 
     * distance between given geometries.
     */
    public static final Tests BEYOND_TESTS = new Tests(nameOf(BEYOND), 
            new Pass(true,0,1,2));

    /** 
     * {@link #CONTAINS} operation test specification.
     * <p>
     * This is a binary operation requiring two unique geometries, 
     * one completely containing the other.
     */
    public static final Tests CONTAINS_TESTS = new Tests(nameOf(CONTAINS), 
            new Pass(true,0,1));

    /** 
     * {@link #LESS_THAN} operation test specification.
     * <p>
     * This is a binary operation requiring two values, one less than the other
     */
    public static final Tests CROSSES_TESTS = new Tests(nameOf(CROSSES), 
            new Pass(true,0,1));

    /** 
     * {@link #DISJOINT} operation test specification.
     * <p>
     * This is binary operation requiring two unique geometries, 
     * where none of them have a overlapping boundary.
     */
    public static final Tests DISJOINT_TESTS = new Tests(nameOf(DISJOINT), 
            new Pass(true,0,1));
    
    /** 
     * {@link #DWITHIN} operation test specification.
     * <p>
     * This is a ternary operation requiring three geometries, 
     * one greater than the other
     */
    public static final Tests DWITHIN_TESTS = new Tests(nameOf(DWITHIN), 
            new Pass(true,0,1,2));

    /** 
     * {@link #EQUALS} operation test specification.
     * <p>
     * This is binary operation requiring two equal geometries.
     */
    public static final Tests EQUALS_TESTS = new Tests(nameOf(EQUALS), 
            new Pass(true,0,0));

    /** 
     * {@link #INTERSECTS} operation test specification.
     * <p>
     * This is a binary operation requiring two geometries, 
     * one intersecting the other. 
     */
    public static final Tests INTERSECTS_TESTS = new Tests(nameOf(INTERSECTS),
            //
            // Pass 0: Check if two equal polygons intersect
            //
            new Pass(true,0,1),
            //
            // Pass 2: Check if overlapping polygons intersect
            //
            new Pass(true,0,2));
    
    /** 
     * {@link #OVERLAPS} operation test specification.
     * <p>
     * This is a binary operation requiring two unique geometries,
     * one overlapping the other.
     */
    public static final Tests OVERLAPS_TESTS = new Tests(nameOf(OVERLAPS), 
            //
            // Pass 2: Check if two intersecting polygons overlap 
            //
            new Pass(true,0,1));
    
    /** 
     * {@link #TOUCHES} operation test specification.
     * <p>
     * This is a binary operation requiring two unique geometries, 
     * one touching the other.
     */
    public static final Tests TOUCHES_TESTS = new Tests(nameOf(TOUCHES), 
            new Pass(true,0,1));

    /** 
     * {@link #WITHIN} operation test specification.
     * <p>
     * This is a binary operation requiring two unique geometries, 
     * one within the other
     */
    public static final Tests WITHIN_TESTS = new Tests(nameOf(WITHIN), 
            new Pass(true,0,1));
    
    // ----------------------------------------------------- 
    //  Cached tests
    // -----------------------------------------------------
    
    public static final Tests[] TESTS = new Tests[]{
        BBOX_TESTS,BEYOND_TESTS,CONTAINS_TESTS,
        CROSSES_TESTS,DISJOINT_TESTS,DWITHIN_TESTS, 
        EQUALS_TESTS,INTERSECTS_TESTS,OVERLAPS_TESTS,
        TOUCHES_TESTS,WITHIN_TESTS
    };
    
    // ----------------------------------------------------- 
    //  Cached test values
    // -----------------------------------------------------
    
    protected static WKTReader READER = new WKTReader();
    
    protected static final double DISTANCE = 10.0;
    
    protected static final Object[] POINTS = new Point[]{
        create(Point.class,"POINT (10 10)"),
        create(Point.class,"POINT (20 20)")
    };

    protected static final Object[] LINESTRINGS = new LineString[]{
        create(LineString.class,"LINESTRING (20 20, 40 20)")
    };
    
    protected static final Object[] POLYGONS = new Polygon[]{
        //
        // P0 BBOX | INTERSECTS {P1}, TOUCHES {P2}, CONTAINS {P3}
        //
        create(Polygon.class,"POLYGON ((10 10, 30 10, 30 30, 10 30, 10 10))"),
        create(Polygon.class,"POLYGON ((20 20, 40 20, 40 40, 20 40, 20 20))"),
        create(Polygon.class,"POLYGON ((00 30, 20 30, 20 50, 00 50, 00 30))"),
        create(Polygon.class,"POLYGON ((15 15, 25 15, 25 25, 15 25, 15 15))"),
        create(Polygon.class,"POLYGON ((50 10, 70 10, 70 30, 50 30, 50 10))"),
    };
    
    
    /**
     * Cached geometry values
     */
    protected static final Values VALUES[] = new Values[]{
        new Values(BBOX,Point.class,POLYGONS),
        new Values(EQUALS,Point.class,POINTS),
        new Values(DISJOINT,Polygon.class,POLYGONS[0],POLYGONS[4]),
        new Values(OVERLAPS,Geometry.class,POLYGONS[0],POLYGONS[1]),
        new Values(INTERSECTS,Polygon.class,POLYGONS[0],POLYGONS[0],POLYGONS[1]),
        new Values(CONTAINS,Polygon.class,POLYGONS[3],POLYGONS[0]),
        new Values(CROSSES,Geometry.class,POLYGONS[0],LINESTRINGS[0]),
        new Values(TOUCHES,Polygon.class,POLYGONS[0],POLYGONS[2]),
        new Values(WITHIN,Polygon.class,POLYGONS[0],POLYGONS[3]),
        new Values(BEYOND,Polygon.class,POLYGONS[0],POLYGONS[4],19.99),
        new Values(DWITHIN,Polygon.class,POLYGONS[0],POLYGONS[4],20.0)
    };
    
    /**
     * Cached {@link EAttribute} reference 
     */
    protected static final EAttribute eAttribute = EFeatureTestsPackage.eINSTANCE.getEFeatureData_Geometry();
    
    
    // ----------------------------------------------------- 
    //  AbstractEAttributeValueTest instance members
    // -----------------------------------------------------    
    
    /**
     * Operation ID
     */
    protected final int operationID;
    
    /**
     * Operation index
     */
    protected final int operationIndex;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * @param name
     */
    public AbstractEGeometryValueTest(String name, int operationID) {
        //
        // Forward to AbstractEAttributeFilterTest
        //
        super(name);
        //
        // Save operation ID
        //
        this.operationID = operationID;
        //
        // Map operation ID to field index
        //
        this.operationIndex = indexOf(operationID);
    }

    // ----------------------------------------------------- 
    //  AbstractEAttributeFilterTest implementation
    // -----------------------------------------------------

    @Override
    protected final EFeatureData<Object, Geometry> createFixture() {
        // 
        // Create instance using the factory
        //
        return EFeatureTestsFactory.eINSTANCE.<Object, Geometry>createEFeatureData();
    }
            
    @Override
    protected String getType(int value) {
        return VALUES[value].getType().getSimpleName();
    }

    @Override
    protected String getOperationName() {
        return TESTS[operationIndex].name;
    }

    @Override
    protected boolean isOperand(int value) {
        return VALUES[value].isOperand(operationID);
    }

    @Override
    protected int getPassCount() {
        return TESTS[operationIndex].passes.length;
    }

    @Override
    protected int getValueCount() {
        return VALUES.length;
    }
    
    @Override
    protected boolean expect(int pass) {
        return TESTS[operationIndex].passes[pass].success;
    }

    @Override
    protected final Object getFilter(int value, int pass) {
        return VALUES[value].getFilter(TESTS[operationIndex].passes[pass]);
    }

    @Override
    protected final Object getTest(int value, int pass) {
        return VALUES[value].getTest(TESTS[operationIndex].passes[pass]);
    }

    @Override
    protected final void updateFixture(EFeatureData<Object, Geometry> eFixture, int value, int pass) {
        //
        // Update geometry value
        //
        eFixture.setGeometry((Geometry)getTest(value, pass));
    }
    
    @Override
    protected final Condition createCondition(int value, int pass) throws EFeatureEncoderException {
        return createCondition(value, getFilter(value, pass));
    }
    
    // ----------------------------------------------------- 
    //  AbstractEAttributeValueTest implementation methods
    // -----------------------------------------------------    
    
    protected abstract Condition createCondition(int value, Object filter) throws EFeatureEncoderException;
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected static final String nameOf(int operation) {
        return nameOf(operation, AbstractEGeometryValueTest.class.getFields());
    }
    
    protected static final <T extends Geometry> T create(Class<T> type, String wkt) {
        try {
            return type.cast(READER.read(wkt));
        } catch (ParseException e) {
            fail(e.getMessage());
        }        
        return null;
    }
   
}
