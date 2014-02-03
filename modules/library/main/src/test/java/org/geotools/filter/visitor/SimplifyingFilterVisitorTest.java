/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.math.FilterFunction_random;
import org.geotools.filter.visitor.SimplifyingFilterVisitor.FIDValidator;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.Identifier;

/**
 * 
 *
 * @source $URL$
 */
public class SimplifyingFilterVisitorTest extends TestCase {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    Id emptyFid;
    SimplifyingFilterVisitor visitor;
    PropertyIsEqualTo property;
    
    @Override
    protected void setUp() throws Exception {
        emptyFid = ff.id(new HashSet<Identifier>());
        property = ff.equal(ff.property("test"), ff.literal("oneTwoThree"), false);        
        visitor = new SimplifyingFilterVisitor();
    }
    
    @Override
    protected void tearDown() throws Exception {
        EnvFunction.clearLocalValues();
    }

    public void testIncludeAndInclude() {
        Filter result = (Filter) ff.and(Filter.INCLUDE, Filter.INCLUDE).accept(visitor, null);
        assertEquals(Filter.INCLUDE, result);
    }
    
    public void testIncludeAndExclude() {
        Filter result = (Filter) ff.and(Filter.INCLUDE, Filter.EXCLUDE).accept(visitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }
    
    public void testExcludeAndExclude() {
        Filter result = (Filter) ff.and(Filter.EXCLUDE, Filter.EXCLUDE).accept(visitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }
    
    public void testIncludeAndProperty() {
        Filter result = (Filter) ff.and(Filter.INCLUDE, property).accept(visitor, null);
        assertEquals(property, result);
    }
    
    public void testExcludeAndProperty() {
        Filter result = (Filter) ff.or(Filter.EXCLUDE, property).accept(visitor, null);
        assertEquals(property, result);
    }
    
    public void testIncludeOrInclude() {
        Filter result = (Filter) ff.or(Filter.INCLUDE, Filter.INCLUDE).accept(visitor, null);
        assertEquals(Filter.INCLUDE, result);
    }
    
    public void testIncludeOrExclude() {
        Filter result = (Filter) ff.or(Filter.INCLUDE, Filter.EXCLUDE).accept(visitor, null);
        assertEquals(Filter.INCLUDE, result);
    }
    
    public void testExcludeOrExclude() {
        Filter result = (Filter) ff.or(Filter.EXCLUDE, Filter.EXCLUDE).accept(visitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }
    
    public void testIncludeOrProperty() {
        Filter result = (Filter) ff.or(Filter.INCLUDE, property).accept(visitor, null);
        assertEquals(Filter.INCLUDE, result);
    }
    
    public void testExcludeOrProperty() {
        Filter result = (Filter) ff.or(Filter.EXCLUDE, property).accept(visitor, null);
        assertEquals(property, result);
    }
    
    public void testEmptyFid() {
        Filter result = (Filter) emptyFid.accept(visitor, null);
        assertEquals(Filter.EXCLUDE, result);    
    }
    
    public void testRecurseAnd() {
        Filter test = ff.and(Filter.INCLUDE, ff.or(property, Filter.EXCLUDE));
        assertEquals(property, test.accept(visitor, null));
    }
    
    public void testRecurseOr() {
        Filter test = ff.or(Filter.EXCLUDE, ff.and(property, Filter.INCLUDE));
        assertEquals(property, test.accept(visitor, null));
    }
    
    public void testFidValidity() {
        visitor.setFIDValidator(new SimplifyingFilterVisitor.FIDValidator() {
            public boolean isValid(String fid) {
                return fid.startsWith("pass");
            }
        });

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("notPass"));
        Id filter = ff.id(ids);

        assertEquals(Filter.EXCLUDE, filter.accept(visitor, null));

        ids.add(ff.featureId("pass1"));
        ids.add(ff.featureId("pass2"));
        filter = ff.id(ids);

        Set<Identifier> validIds = new HashSet<Identifier>();
        validIds.add(ff.featureId("pass2"));
        validIds.add(ff.featureId("pass1"));
        Filter expected = ff.id(validIds);
        assertEquals(expected, filter.accept(visitor, null));
    }

    public void testRegExFIDValidator() {
        FIDValidator validator = new SimplifyingFilterVisitor.RegExFIDValidator("abc\\.\\d+");
        visitor.setFIDValidator(validator);

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("abc.."));
        ids.add(ff.featureId(".abc.1"));
        ids.add(ff.featureId("abc.123"));
        ids.add(ff.featureId("abc.ax"));
        Id filter = ff.id(ids);
        Filter result = (Filter) filter.accept(visitor, null);
        Filter expected = ff.id(Collections.singleton(ff.featureId("abc.123")));
        
        assertEquals(expected, result);
    }
    
    public void testTypeNameDotNumberValidator() {
        final String typeName = "states";
        FIDValidator validator;
        validator = new SimplifyingFilterVisitor.TypeNameDotNumberFidValidator(typeName);
        visitor.setFIDValidator(validator);

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("_states"));
        ids.add(ff.featureId("states.abc"));
        ids.add(ff.featureId("states.."));
        ids.add(ff.featureId("states.123"));
        Id filter = ff.id(ids);
        Filter result = (Filter) filter.accept(visitor, null);
        Filter expected = ff.id(Collections.singleton(ff.featureId("states.123")));
        
        assertEquals(expected, result);
    }
    
    public void testSingleNegation() {
    	Filter f = ff.not(ff.equals(ff.property("prop"), ff.literal(10)));
    	Filter result = (Filter) f.accept(visitor, null);
    	assertEquals(f, result);
    }
    
    public void testDoubleNegation() {
    	PropertyIsEqualTo equal = ff.equals(ff.property("prop"), ff.literal(10));
		Filter f = ff.not(ff.not(equal));
    	Filter result = (Filter) f.accept(visitor, null);
    	assertEquals(equal, result);
    }
    
    public void testTripleNegation() {
    	PropertyIsEqualTo equal = ff.equals(ff.property("prop"), ff.literal(10));
		Filter f = ff.not(ff.not(ff.not(equal)));
    	Filter result = (Filter) f.accept(visitor, null);
    	assertEquals(ff.not(equal), result);
    }
    
    public void testStableFunction() {
    	EnvFunction.setLocalValue("var", "123");
    	Function f = ff.function("env", ff.literal("var"));
    	
    	Expression result = (Expression) f.accept(visitor, null);
    	assertTrue(result instanceof Literal);
    	assertEquals("123", result.evaluate(null, String.class));
    }
    
    public void testVolatileFunction() {
    	Function f = ff.function("random");
    	
    	Expression result = (Expression) f.accept(visitor, null);
    	assertTrue(result instanceof FilterFunction_random);
    }
    
    public void testNestedVolatile() {
    	EnvFunction.setLocalValue("power", 3);
    	Function f = ff.function("pow", ff.function("random"), ff.function("env", ff.literal("power")));
    	
    	Function result = (Function) f.accept(visitor, null);
    	// main function not simplified out
    	assertEquals("pow", result.getName());
    	// first argument not simplified out
    	Function param1 = (Function) result.getParameters().get(0);
		assertEquals("random", param1.getName());
		// second argument simplified out
		Expression param2 = result.getParameters().get(1);
		assertTrue(param2 instanceof Literal);
		assertEquals(Integer.valueOf(3), param2.evaluate(null, Integer.class));
    }
    
    public void testCompareFunctionNull() {
        Function f = ff.function("env", ff.literal("var"));
        PropertyIsEqualTo filter = ff.equal(f, ff.literal("test"), false);
        
        Filter simplified = (Filter) filter.accept(visitor, null);
        assertEquals(Filter.EXCLUDE, simplified);
    }
    
    public void testCompareConstantFunction() {
        EnvFunction.setLocalValue("var", "test");
        Function f = ff.function("env", ff.literal("var"));
        PropertyIsEqualTo filter = ff.equal(f, ff.literal("test"), false);
        
        Filter simplified = (Filter) filter.accept(visitor, null);
        assertEquals(Filter.INCLUDE, simplified);
    }
    
    public void testSimplifyStaticExclude() 
    {
        assertEquals(Filter.EXCLUDE, simplify(ff.greater(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.EXCLUDE, simplify(ff.greaterOrEqual(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.EXCLUDE, simplify(ff.less(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.EXCLUDE, simplify(ff.lessOrEqual(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.EXCLUDE, simplify(ff.equal(ff.literal(5), ff.literal(3), true)));
        assertEquals(Filter.EXCLUDE, simplify(ff.between(ff.literal(3), ff.literal(1), ff.literal(2))));
    }

    public void testSimplifyStaticInclude() 
    {
        assertEquals(Filter.INCLUDE, simplify(ff.less(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.INCLUDE, simplify(ff.lessOrEqual(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.INCLUDE, simplify(ff.greater(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.INCLUDE, simplify(ff.greaterOrEqual(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.INCLUDE, simplify(ff.equal(ff.literal(5), ff.literal(5), true)));
        assertEquals(Filter.INCLUDE, simplify(ff.between(ff.literal(3), ff.literal(1), ff.literal(4))));
    }
    
    private Filter simplify(Filter filter) {
        return (Filter) filter.accept(new SimplifyingFilterVisitor(), null);
    }
    
    public void testCoalesheNestedAnd()
    {
        Filter eq = ff.equal(ff.property("A"), ff.literal("3"), true);
        Filter gt = ff.greater(ff.property("b"), ff.literal("3"));
        Filter lt = ff.less(ff.property("c"), ff.literal("5"));
        And nested = ff.and(Arrays.asList(ff.and(Arrays.asList(eq, gt)), lt));
        
        And simplified = (And) nested.accept(visitor, null);
        assertEquals(3, simplified.getChildren().size());
        assertEquals(ff.and(Arrays.asList(eq, gt, lt)), simplified);
    }
    
    public void testCoalesheNestedOr()
    {
        Filter eq = ff.equal(ff.property("A"), ff.literal("3"), true);
        Filter gt = ff.greater(ff.property("b"), ff.literal("3"));
        Filter lt = ff.less(ff.property("c"), ff.literal("5"));
        Or nested = ff.or(Arrays.asList(ff.or(Arrays.asList(eq, gt)), lt));
        
        Or simplified = (Or) nested.accept(visitor, null);
        assertEquals(3, simplified.getChildren().size());
        assertEquals(ff.or(Arrays.asList(eq, gt, lt)), simplified);
    }
    
    public void testDualFilterOr() {
        Or or = ff.or(Arrays.asList(ff.not(ff.equal(ff.property("a"), ff.literal(3), true)), ff.equal(ff.property("a"), ff.literal(3), true)));
        assertEquals(Filter.INCLUDE, or.accept(visitor, null));
    }
    
    public void testDualFilterAnd() {
        Filter original = ff.and(Arrays.asList(ff.not(ff.equal(ff.property("a"), ff.literal(3), true)), ff.equal(ff.property("a"), ff.literal(3), true)));
        assertEquals(Filter.EXCLUDE, original.accept(visitor, null));
    }
    
    public void testDualFilterNullAnd() {
        Filter original = ff.and(Arrays.asList(ff.not(ff.isNull(ff.property("a"))), ff.isNull(ff.property("a"))));
        assertEquals(Filter.EXCLUDE, original.accept(visitor, null));
    }
    
    public void testDualFilterNullOr() {
        Filter original = ff.or(Arrays.asList(ff.not(ff.isNull(ff.property("a"))), ff.isNull(ff.property("a"))));
        assertEquals(Filter.INCLUDE, original.accept(visitor, null));
    }
    
}
