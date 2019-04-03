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
package org.geotools.feature.type;

import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

/** Test classes used to create and build {@link FeatureType} data structure. */
public class TypesTest extends TestCase {

    public void testAttributeBuilder() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        AttributeTypeBuilder builder = new AttributeTypeBuilder();

        builder.binding(Integer.class);
        builder.minOccurs(1).maxOccurs(1);
        builder.defaultValue(0);
        builder.name("percent").description("Percent between 0 and 100");
        builder.restriction(ff.greaterOrEqual(ff.property("."), ff.literal(0)))
                .restriction(ff.lessOrEqual(ff.property("."), ff.literal(100)));

        final AttributeType PERCENT = builder.buildType();

        builder.minOccurs(1).maxOccurs(1);
        builder.defaultValue(0);
        builder.name("percent").description("Percent between 0 and 100");

        AttributeDescriptor a = builder.buildDescriptor("a", PERCENT);

        assertSame(a.getType(), PERCENT);
        assertEquals(a.getDefaultValue(), 0);

        Filter restrictions = ff.and(PERCENT.getRestrictions());
        assertTrue(restrictions.evaluate(50));
        assertFalse(restrictions.evaluate(150));
    }

    public void testWithoutRestriction() {
        // used to prevent warning
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

        String attributeName = "string";
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); // $NON-NLS-1$
        builder.setName("test");
        builder.add(attributeName, String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        SimpleFeature feature =
                SimpleFeatureBuilder.build(featureType, new Object[] {"Value"}, null);

        assertNotNull(feature);
    }
    /** This utility class is used by Types to prevent attribute modification. */
    public void testRestrictionCheck() {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

        String attributeName = "string";
        PropertyIsEqualTo filter = fac.equals(fac.property("."), fac.literal("Value"));

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); // $NON-NLS-1$
        builder.setName("test");
        builder.restriction(filter).add(attributeName, String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        SimpleFeature feature =
                SimpleFeatureBuilder.build(featureType, new Object[] {"Value"}, null);

        assertNotNull(feature);
    }

    public void testAssertNamedAssignable() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Test");
        builder.add("name", String.class);
        builder.add("age", Double.class);
        SimpleFeatureType test = builder.buildFeatureType();

        builder.setName("Test");
        builder.add("age", Double.class);
        builder.add("name", String.class);
        SimpleFeatureType test2 = builder.buildFeatureType();

        builder.setName("Test");
        builder.add("name", String.class);
        SimpleFeatureType test3 = builder.buildFeatureType();

        builder.setName("Test");
        builder.add("name", String.class);
        builder.add("distance", Double.class);
        SimpleFeatureType test4 = builder.buildFeatureType();

        Types.assertNameAssignable(test, test);
        Types.assertNameAssignable(test, test2);
        Types.assertNameAssignable(test2, test);
        try {
            Types.assertNameAssignable(test, test3);
            fail("Expected assertNameAssignable to fail as age is not covered");
        } catch (IllegalArgumentException expected) {
        }

        Types.assertOrderAssignable(test, test4);
    }
}
