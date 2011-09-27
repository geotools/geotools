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

import java.util.Date;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.Condition;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.query.EFeatureEncoderException;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsFactory;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author kengu - 14. juni 2011
 *
 *
 * @source $URL$
 */
public abstract class AbstractEAttributeValueTest extends AbstractEAttributeFilterTest<EFeatureData<Object, Geometry>> {
    
    // ---------------------------------------------
    //  Unary operators
    // ---------------------------------------------
    
    /** The {@literal IS NULL} operator. */
    public static final int IS_NULL = 1;

    // ---------------------------------------------
    //  Binary operators
    // ---------------------------------------------
    
    /** The {@literal =} operator. */
    public static final int EQUAL_TO = 2;

    /** The {@literal !=} operator. */
    public static final int NOT_EQUAL_TO = 4;

    /** The {@literal <} operator. */
    public static final int LESS_THAN = 8;

    /** The {@literal <=} operator. */
    public static final int LESS_THAN_OR_EQUAL_TO = 16;

    /** The {@literal >} operator. */
    public static final int GREATER_THAN = 32;

    /** The {@literal >=} operator. */
    public static final int GREATER_THAN_OR_EQUAL_TO = 64;

    /** The {@literal = IS LIKE} operator. */
    public static final int IS_LIKE = 128;
    
    // ---------------------------------------------
    //  Ternary operators
    // ---------------------------------------------
    
    /** The {@literal BETWEEN} operator. */
    public static final int BETWEEN = 256;

    /** The {@literal OUTSIDE}  operator. */
    public static final int OUTSIDE = 512;    
    
    // ---------------------------------------------
    //  N-ary operators
    // ---------------------------------------------
    
    /** The {@literal = IS ID} operator. */
    public static final int IS_ID = 1024;

    // ----------------------------------------------------- 
    //  Operation sets group on value type 
    // -----------------------------------------------------
        
    /** Booleans: {@link #EQUAL_TO} and {@link #NOT_EQUAL_TO} only */
    public static final int BOOLEAN_OPS = EQUAL_TO | NOT_EQUAL_TO;

    /** Numbers: All except {@link #IS_LIKE} and {@link #UNARY_OPS}*/
    public static final int NUMBER_OPS = EQUAL_TO | NOT_EQUAL_TO | LESS_THAN | LESS_THAN_OR_EQUAL_TO 
                                       | GREATER_THAN | GREATER_THAN_OR_EQUAL_TO | BETWEEN | OUTSIDE;
    
    /** Characters: All except {@link #IS_LIKE} and {@link #UNARY_OPS} */
    public static final int CHARACTER_OPS = NUMBER_OPS;
    
    /** Dates: All except {@link #IS_LIKE} and {@link #UNARY_OPS} */
    public static final int DATE_OPS = NUMBER_OPS;
    
    /** Strings: All operations */
    public static final int STRING_OPS = NUMBER_OPS | IS_LIKE;
    
    // ----------------------------------------------------- 
    //  Cached tests specifications
    // -----------------------------------------------------
    
    /** 
     * {@link #IS_NULL} operation test specification.
     * <p>
     * This is a unary operation, only requiring a single test value (no filter value).
     */
    public static final Tests IS_NULL_TESTS = new Tests(nameOf(IS_NULL), 
            new Pass(true, 0)); 
    
    /** 
     * {@link #EQUAL_TO} operation test specification.
     * <p>
     * This is a binary operation requiring two equal values
     */
    public static final Tests EQUAL_TO_TESTS = new Tests(nameOf(EQUAL_TO), 
            new Pass(true,0,0));

    /** 
     * {@link #NOT_EQUAL_TO} operation test specification.
     * <p>
     * This is a binary operation, requiring two unequal values
     */
    public static final Tests NOT_EQUAL_TO_TESTS = new Tests(nameOf(NOT_EQUAL_TO), 
            new Pass(true,0,1));

    /** 
     * {@link #LESS_THAN} operation test specification.
     * <p>
     * This is a binary operation requiring two values, one less than the other
     */
    public static final Tests LESS_THAN_TESTS = new Tests(nameOf(LESS_THAN), 
            new Pass(true,0,1));

    /** 
     * {@link #LESS_THAN_OR_EQUAL_TO} operation test specification.
     * <p>
     * This is binary operation requiring two passes. First pass verifies 
     * {@link #EQUAL_TO}, requiring one unique value. The second pass verifies
     * {@link #LESS_THAN}, requiring two values, one less than the other.
     */
    public static final Tests LESS_THAN_OR_EQUAL_TO_TESTS = new Tests(
            nameOf(LESS_THAN_OR_EQUAL_TO), EQUAL_TO_TESTS, LESS_THAN_TESTS);
    
    /** 
     * {@link #GREATER_THAN} operation test specification.
     * <p>
     * This is a binary operation requiring two values, one greater than the other
     */
    public static final Tests GREATER_THAN_TESTS = new Tests(nameOf(GREATER_THAN), 
            new Pass(true,1,0));

    /** 
     * {@link #GREATER_THAN_OR_EQUAL_TO} operation test specification.
     * <p>
     * This is binary operation requiring two passes. First pass verifies 
     * {@link #EQUAL_TO}, requiring one unique value. The second pass verifies
     * {@link #GREATER_THAN}, requiring two values, one greater than the other.
     */
    public static final Tests GREATER_THAN_OR_EQUAL_TO_TESTS = new Tests(
            nameOf(GREATER_THAN_OR_EQUAL_TO), EQUAL_TO_TESTS, GREATER_THAN_TESTS);

    /** 
     * {@link #IS_LIKE} operation test specification.
     * <p>
     * This is a binary operation requiring two values, where one match the other. 
     */
    public static final Tests IS_LIKE_TESTS = new Tests(nameOf(IS_LIKE), 
            new Pass(true,0,3));
    
    /** 
     * {@link #BETWEEN} operation test specification.
     * <p>
     * This is a ternary operation requiring three values: two defining a range,
     * and the last inside this range.
     */
    public static final Tests BETWEEN_TESTS = new Tests(nameOf(BETWEEN), 
            new Pass(true,2,0,1));
    
    /** 
     * {@link #OUTSIDE} operation test specification.
     * <p>
     * This is a ternary operation requiring three values: two defining a range, 
     * and the last outside this range. 
     */
    public static final Tests OUTSIDE_TESTS = new Tests(nameOf(OUTSIDE), 
            new Pass(true,0,2,1));

    /** 
     * {@link #IS_ID} operation test specification.
     * <p>
     * This is a n-ary operation requiring N values, N>0.
     */
    public static final Tests IS_ID_TESTS = new Tests(nameOf(IS_ID), 
            new Pass(true,2,ALL));
    
    // ----------------------------------------------------- 
    //  Cached tests
    // -----------------------------------------------------
    
    public static final Tests[] TESTS = new Tests[]{
        //
        // Unary operation tests
        //
        IS_NULL_TESTS,
        //
        // Binary operation tests
        //
        EQUAL_TO_TESTS,NOT_EQUAL_TO_TESTS,
        LESS_THAN_TESTS,LESS_THAN_OR_EQUAL_TO_TESTS,
        GREATER_THAN_TESTS, GREATER_THAN_OR_EQUAL_TO_TESTS,
        IS_LIKE_TESTS, 
        //
        // Ternary operation tests
        //
        BETWEEN_TESTS,OUTSIDE_TESTS,
        //
        // N-ary operation tests
        //
        IS_ID_TESTS
    };
    
    // ----------------------------------------------------- 
    //  Cached test values
    // -----------------------------------------------------

    /**
     * Cached <code>null</code> values
     */
    protected static final Values NULLS[] = new Values[]{
        new Values(IS_NULL,Object.class,null,null,null) };

    /**
     * Cached boolean values
     */
    protected static final Values BOOLEANS[] = new Values[]{ 
        new Values(BOOLEAN_OPS,Boolean.class,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE) };
    
    /**
     * Cached primitive values
     */
    protected static final Values NUMBERS[] = new Values[]{
        new Values(NUMBER_OPS,Integer.class,Integer.MIN_VALUE,Integer.MAX_VALUE,new Integer(1)),
        new Values(NUMBER_OPS,Double.class,Double.MIN_VALUE, Double.MAX_VALUE, new Double(1.0)),
        new Values(NUMBER_OPS,Float.class,Float.MIN_VALUE, Float.MAX_VALUE, new Float(1.0F)),
        new Values(NUMBER_OPS,Byte.class,Byte.MIN_VALUE, Byte.MAX_VALUE, new Byte((byte)1)),
        new Values(NUMBER_OPS,Short.class,Short.MIN_VALUE, Short.MAX_VALUE, new Short((short)1)),
        new Values(NUMBER_OPS,Long.class,Long.MIN_VALUE, Long.MAX_VALUE, new Long(1L)) };
    
    /**
     * Cached date values
     */
    protected static final Values DATES[] = new Values[]{
        new Values(DATE_OPS,Integer.class, new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE), new Date(0)) };
    
    /**
     * Cached character values
     */
    protected static final Values CHARACTERS[] = new Values[]{
        new Values(CHARACTER_OPS,Character.class,Character.MIN_VALUE,Character.MAX_VALUE,new Character((char)0)) };

    /**
     * Cached string values
     */
    protected static final Values STRINGS[] = new Values[]{
        new Values(STRING_OPS,String.class,"ax","az","ay","a*") };
    
    /**
     * Cached ID values
     */
    public static final Values IDS[] = new Values[]{
        new Values(IS_ID,Set.class,"F1","F100","F50") };
        
    /**
     * Concatenation of all cached values
     */
    protected static final Values VALUES[] = 
        EFeatureUtils.concat(Values.class, 
                NULLS, BOOLEANS, NUMBERS, DATES, CHARACTERS, STRINGS, IDS);

    /**
     * Cached {@link EAttribute} reference 
     */
    protected static final EAttribute eAttribute = EFeatureTestsPackage.eINSTANCE.getEFeatureData_Attribute();
    
    
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
    public AbstractEAttributeValueTest(String name, int operationID) {
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
        // Update values
        //
        eFixture.setAttribute(getTest(value, pass));
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
    
    protected static final String nameOf(int operation) throws IllegalArgumentException {
        return nameOf(operation,AbstractEAttributeValueTest.class.getFields());
    }    
}
