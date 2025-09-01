/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.type.Types;
import org.geotools.util.NumberRange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class SimpleFeatureBuilderTest {

    SimpleFeatureBuilder builder;

    SimpleFeatureType featureType;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        AttributeTypeBuilder attributeBuilder = new AttributeTypeBuilder();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        typeBuilder.setName("test");
        typeBuilder.add("point", Point.class, (CoordinateReferenceSystem) null);

        // integer value is required, and must be non-negative
        Filter nonNegative = ff.greaterOrEqual(ff.property("."), ff.literal(0));
        attributeBuilder.setBinding(Integer.class);
        attributeBuilder.setNillable(false);
        attributeBuilder.addRestriction(nonNegative);
        typeBuilder.add(attributeBuilder.buildDescriptor("integer"));

        typeBuilder.add("float", Float.class);

        featureType = typeBuilder.buildFeatureType();

        builder = new SimpleFeatureBuilder(featureType);
        builder.setValidating(true);
    }

    @Test
    public void testSanity() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(0, 0)));
        builder.add(Integer.valueOf(1));
        builder.add(Float.valueOf(2.0f));

        SimpleFeature feature = builder.buildFeature("fid");
        Assert.assertNotNull(feature);

        Assert.assertEquals(3, feature.getAttributeCount());

        Assert.assertEquals(gf.createPoint(new Coordinate(0, 0)), feature.getAttribute("point"));
        Assert.assertEquals(Integer.valueOf(1), feature.getAttribute("integer"));
        Assert.assertEquals(Float.valueOf(2.0f), feature.getAttribute("float"));
    }

    @Test
    public void testValidation() {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(0, 0));

        builder.setValidating(false);
        builder.add(point);
        builder.add(-1);
        builder.add(0.0f);
        SimpleFeature invalidFeature = builder.buildFeature("invalid");

        Assert.assertTrue("valid", Types.isValid((Attribute) invalidFeature.getProperty("point")));
        Assert.assertFalse("valid", Types.isValid((Attribute) invalidFeature.getProperty("integer")));
        Assert.assertTrue("valid", Types.isValid((Attribute) invalidFeature.getProperty("float")));
        Assert.assertFalse("invalid", Types.isValid(invalidFeature));

        builder.setValidating(true);
        builder.add(point);
        try {
            builder.add(-1);
            Assert.fail("Builder with validation enabled should not be able to add an invalid value");
        } catch (IllegalAttributeException huh) {
            // expected error
        }

        builder.reset();
        builder.setValidating(false);
        builder.add(point);
        builder.add(-1);
        builder.add(0.0f);

        builder.setValidating(true);
        try {
            SimpleFeature brokenFeature = builder.buildFeature("broken");
            Assert.assertNotNull(brokenFeature);
            Assert.fail("Builder with validation should not produce invalid feature");
        } catch (IllegalAttributeException huh) {
            // expected error
        }
    }

    @Test
    public void testBeRelaxedAboutNumberOfAttributes() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(0, 0));
        builder.add(point);
        builder.add(Integer.valueOf(1));

        // be forgiving if too few values provided (use default values for anything missing)
        SimpleFeature feature = builder.buildFeature("fid.1");
        Assert.assertNotNull(feature);

        Assert.assertEquals(3, feature.getAttributeCount());

        Assert.assertEquals(gf.createPoint(new Coordinate(0, 0)), feature.getAttribute("point"));
        Assert.assertEquals(Integer.valueOf(1), feature.getAttribute("integer"));
        Assert.assertNull(feature.getAttribute("float"));

        List<Object> tooFew = new ArrayList<>();
        tooFew.add(point);

        SimpleFeature feature2 = SimpleFeatureBuilder.build(featureType, tooFew, "fid.2");
        Assert.assertNotNull(feature2);

        Assert.assertSame("provided point", point, feature.getAttribute(0));
        Assert.assertNotNull("default count", feature.getAttribute(1));

        // be forgiving if too many values provided
        List<Object> tooMany = new ArrayList<>();
        tooMany.add(point);
        tooMany.add(1);
        tooMany.add(3.0f);
        tooMany.add("Sasquatch");
        SimpleFeature feature3 = SimpleFeatureBuilder.build(featureType, tooMany, "fid.3");
        Assert.assertNotNull(feature3);
    }

    @Test
    public void testSetSequential() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.set("point", gf.createPoint(new Coordinate(0, 0)));
        builder.set("integer", Integer.valueOf(1));
        builder.set("float", Float.valueOf(2.0f));

        SimpleFeature feature = builder.buildFeature("fid");
        Assert.assertNotNull(feature);

        Assert.assertEquals(3, feature.getAttributeCount());

        Assert.assertEquals(gf.createPoint(new Coordinate(0, 0)), feature.getAttribute(0));
        Assert.assertEquals(Integer.valueOf(1), feature.getAttribute(1));
        Assert.assertEquals(Float.valueOf(2.0f), feature.getAttribute(2));
    }

    @Test
    public void testSetNonSequential() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.set("float", Float.valueOf(2.0f));
        builder.set("point", gf.createPoint(new Coordinate(0, 0)));
        builder.set("integer", Integer.valueOf(1));

        SimpleFeature feature = builder.buildFeature("fid");
        Assert.assertNotNull(feature);

        Assert.assertEquals(3, feature.getAttributeCount());

        Assert.assertEquals(gf.createPoint(new Coordinate(0, 0)), feature.getAttribute(0));
        Assert.assertEquals(Integer.valueOf(1), feature.getAttribute(1));
        Assert.assertEquals(Float.valueOf(2.0f), feature.getAttribute(2));
    }

    @Test
    public void testSetTooFew() throws Exception {
        builder.set("integer", Integer.valueOf(1));
        SimpleFeature feature = builder.buildFeature("fid");
        Assert.assertNotNull(feature);

        Assert.assertEquals(3, feature.getAttributeCount());

        Assert.assertNull(feature.getAttribute(0));
        Assert.assertEquals(Integer.valueOf(1), feature.getAttribute(1));
        Assert.assertNull(feature.getAttribute(2));
    }

    @Test
    public void testConverting() throws Exception {
        builder.set("integer", "1");
        builder.buildFeature("fid");
        try {
            builder.set("integer", "foo");
            Assert.fail("should have failed");
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateFeatureWithLength() throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); // $NON-NLS-1$
        builder.setName("test");
        builder.length(5).add("name", String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"Val"}, "ID");

        Assert.assertNotNull(feature);

        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {"Longer Than 5"}, "ID");
            feature.validate();
            Assert.fail("this should fail because the value is longer than 5 characters");
        } catch (Exception e) {
            // good
        }
    }

    @Test
    public void testCreateFeatureWithRestriction() throws Exception {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

        String attributeName = "string";
        PropertyIsEqualTo filter = fac.equals(fac.property("."), fac.literal("Value"));

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); // $NON-NLS-1$
        builder.setName("test");
        builder.restriction(filter).add(attributeName, String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"Value"}, "ID");

        Assert.assertNotNull(feature);

        try {
            SimpleFeature sf = SimpleFeatureBuilder.build(featureType, new Object[] {"NotValue"}, "ID");
            sf.validate();
            Assert.fail("PropertyIsEqualTo filter should have failed");
        } catch (Exception e) {
            // good
        }
    }

    @Test
    public void testCreateFeatureWithOptions() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.options("a", "b", "c").add("name", String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        // build a valid feature
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"a"}, "ID");
        Assert.assertNotNull(feature);
        feature.validate();

        // try an invalid one
        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {"d"}, "ID");
            feature.validate();
            Assert.fail("this should fail because the value is not either a, b or c, but d");
        } catch (Exception e) {
            // good
        }
    }

    /**
     * Same as {@link #testCreateFeatureWithOptions()} but using an AttributeTypeBuilder directly
     *
     * @throws Exception
     */
    @Test
    public void testCreateFeatureWithOptionsOnAttributeTypeBuilder() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.name("name").binding(String.class).options(Arrays.asList("a", "b", "c"));
        builder.add(atb.buildDescriptor("name"));
        SimpleFeatureType featureType = builder.buildFeatureType();

        // build a valid feature
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"a"}, "ID");
        Assert.assertNotNull(feature);
        feature.validate();

        // try an invalid one
        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {"d"}, "ID");
            feature.validate();
            Assert.fail("this should fail because the value is not either a, b or c, but d");
        } catch (Exception e) {
            // good
        }
    }

    @Test
    public void testCreateFeatureWithRange() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.range(NumberRange.create(0, 100)).add("size", String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        // build a valid feature
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {100}, "ID");
        Assert.assertNotNull(feature);
        feature.validate();

        // try an invalid one
        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {101}, "ID");
            feature.validate();
            Assert.fail("this should fail because the value is outside the permitted range [0,100]");
        } catch (Exception e) {
            // good
        }
    }

    /**
     * Same as {@link #testCreateFeatureWithRange()} but using an AttributeTypeBuilder directly
     *
     * @throws Exception
     */
    @Test
    public void testCreateFeatureWithRangeOnAttributeTypeBuilder() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.name("size").binding(String.class).range(NumberRange.create(0, 100));
        builder.add(atb.buildDescriptor("size"));
        SimpleFeatureType featureType = builder.buildFeatureType();

        // build a valid feature
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {100}, "ID");
        Assert.assertNotNull(feature);
        feature.validate();

        // try an invalid one
        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {101}, "ID");
            feature.validate();
            Assert.fail("this should fail because the value is outside the permitted range [0,100]");
        } catch (Exception e) {
            // good
        }
    }

    @Test
    public void testUserData() throws Exception {
        SimpleFeature feature = buildUserDataFeature();
        Assert.assertNotNull(feature);
        Assert.assertEquals("bar", feature.getUserData().get("foo"));

        builder = new SimpleFeatureBuilder(feature.getFeatureType());
        builder.init(feature);
        feature = builder.buildFeature("fid");
        Assert.assertNotNull(feature);
        Assert.assertEquals("bar", feature.getUserData().get("foo"));
    }

    private SimpleFeature buildUserDataFeature() {
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(0, 0)));
        builder.add(Integer.valueOf(1));
        builder.add(Float.valueOf(2.0f));
        builder.featureUserData("foo", "bar");

        return builder.buildFeature("fid");
    }

    @Test
    public void testCopyUserData() throws Exception {
        SimpleFeature template = buildUserDataFeature();
        builder = new SimpleFeatureBuilder(template.getFeatureType());
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(1, 1)));
        builder.add(Integer.valueOf(2));
        builder.add(Float.valueOf(4.0f));
        builder.featureUserData(template);
        SimpleFeature feature = builder.buildFeature("myfid");

        Assert.assertEquals("bar", feature.getUserData().get("foo"));
    }
}
