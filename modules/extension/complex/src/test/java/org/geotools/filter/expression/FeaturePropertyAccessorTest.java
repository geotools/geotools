/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.data.complex.expression.FeaturePropertyAccessorFactory;
import org.geotools.data.complex.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to demonstrate evaluating XPaths as attribute expressions when complex attributes/features are passed in,
 * instead of simple features. This is necessary since complex features could contain nested properties, and we should
 * be able to get properties of any level from the features.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class FeaturePropertyAccessorTest {
    /** Mock name space prefix */
    static final String EG = "urn:cgi:xmlns:Example:1.0";

    static final Name SIMPLE_ATTRIBUTE = new NameImpl(EG, "simpleAttribute");

    static final Name COMPLEX_ATTRIBUTE = new NameImpl(EG, "complexAttribute");

    static final Name ROOT_ATTRIBUTE = new NameImpl(EG, "rootAttribute");

    static final Name SINGLE_LEAF_ATTRIBUTE = new NameImpl(EG, "singleLeafAttribute");

    static final Name MULTI_LEAF_ATTRIBUTE = new NameImpl(EG, "multiLeafAttribute");

    /** Mock name space */
    static final NamespaceSupport NAMESPACES = new NamespaceSupport() {
        {
            declarePrefix("eg", EG);
        }
    };

    @Test
    public void testComplexFeature() {
        FeatureType fType = createFeatureType();

        AttributeDescriptor complexDesc = (AttributeDescriptor) fType.getDescriptor(COMPLEX_ATTRIBUTE);
        ComplexType complexAttType = (ComplexType) complexDesc.getType();
        AttributeDescriptor rootDesc = (AttributeDescriptor) complexAttType.getDescriptor(ROOT_ATTRIBUTE);
        ComplexType rootAttType = (ComplexType) rootDesc.getType();

        // feature properties
        Collection<Property> properties = new ArrayList<>(fType.getDescriptors().size());

        /** Build the feature properties */
        // eg:simpleAttribute
        AttributeDescriptor simpleAttributeDesc = (AttributeDescriptor) fType.getDescriptor(SIMPLE_ATTRIBUTE);
        AttributeImpl simpleAttribute = new AttributeImpl("simple value", simpleAttributeDesc, null);
        properties.add(simpleAttribute);
        // eg:complexAttribute/eg:rootAttribute[1]
        Collection<Property> rootPropertiesOne = new ArrayList<>();
        AttributeDescriptor multiLeafDesc = (AttributeDescriptor) rootAttType.getDescriptor(MULTI_LEAF_ATTRIBUTE);
        // eg:complexAttribute/eg:rootAttribute[1]/eg:multiLeafAttribute[1]
        AttributeImpl leafOne = new AttributeImpl("multi leaf value 1", multiLeafDesc, null);
        rootPropertiesOne.add(leafOne);
        // eg:complexAttribute/eg:rootAttribute[1]/eg:multiLeafAttribute[2]
        AttributeImpl leafTwo = new AttributeImpl("multi leaf value 2", multiLeafDesc, null);
        rootPropertiesOne.add(leafTwo);
        // eg:complexAttribute/eg:rootAttribute[1]/eg:singleLeafAttribute
        AttributeDescriptor singleLeafDesc = (AttributeDescriptor) rootAttType.getDescriptor(SINGLE_LEAF_ATTRIBUTE);
        AttributeImpl singleLeaf = new AttributeImpl("single leaf value", singleLeafDesc, null);
        rootPropertiesOne.add(singleLeaf);

        // NC- add, test xml-attribute
        Map<Name, Object> userData = new HashMap<>();
        singleLeaf.getUserData().put(Attributes.class, userData);
        userData.put(Types.typeName(EG, "att"), "test attribute");

        AttributeImpl rootOne = new ComplexAttributeImpl(rootPropertiesOne, rootDesc, null);

        // eg:complexAttribute/eg:rootAttribute[2]
        Collection<Property> rootPropertiesTwo = new ArrayList<>();
        // eg:complexAttribute/eg:rootAttribute[2]/eg:multiLeafAttribute[1]
        rootPropertiesTwo.add(leafOne);
        rootPropertiesTwo.add(singleLeaf);
        AttributeImpl rootTwo = new ComplexAttributeImpl(rootPropertiesTwo, rootDesc, null);

        // eg:complexAttribute/eg:rootAttribute[3]
        Collection<Property> rootPropertiesThree = new ArrayList<>();
        // eg:complexAttribute/eg:rootAttribute[3]/eg:singleLeafAttribute
        rootPropertiesThree.add(singleLeaf);
        AttributeImpl rootThree = new ComplexAttributeImpl(rootPropertiesThree, rootDesc, null);

        Collection<Property> complexProperties = new ArrayList<>(2);
        complexProperties.add(rootOne);
        complexProperties.add(rootTwo);
        complexProperties.add(rootThree);
        AttributeImpl complexAttribute = new ComplexAttributeImpl(complexProperties, complexDesc, null);
        properties.add(complexAttribute);

        Feature feature = new FeatureImpl(properties, fType, new FeatureIdImpl("test1"));

        /** Test evaluating complex feature */
        // test evaluating simple property
        AttributeExpressionImpl ex = new AttributeExpressionImpl(
                "eg:simpleAttribute", new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(simpleAttribute, ex.evaluate(feature));

        // test evaluating complex property
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute", new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(complexAttribute, ex.evaluate(feature));

        // test multi-valued nested properties
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        // no index would return array of all features
        Object o = ex.evaluate(feature);
        assertTrue(o instanceof List);
        assertEquals(3, ((List) o).size());
        assertEquals(rootOne, ((List) o).get(0));
        assertEquals(rootTwo, ((List) o).get(1));
        assertEquals(rootThree, ((List) o).get(2));

        // test nested on multi-valued attributes
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute/eg:singleLeafAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        // no index would return array of all features
        o = ex.evaluate(feature);
        assertTrue(o instanceof List);
        assertEquals(3, ((List) o).size());
        assertEquals(singleLeaf, ((List) o).get(0));
        assertEquals(singleLeaf, ((List) o).get(1));
        assertEquals(singleLeaf, ((List) o).get(2));

        // index specified
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[1]",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(rootOne, ex.evaluate(feature));
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[2]",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(rootTwo, ex.evaluate(feature));

        // single valued nested property
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[3]/eg:singleLeafAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(singleLeaf, ex.evaluate(feature));

        // null values
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[3]/eg:multiLeafAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertNull(ex.evaluate(feature));

        // deeper nesting
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[2]/eg:multiLeafAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(leafOne, ex.evaluate(feature));

        // property index doesn't exist
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[2]/eg:multiLeafAttribute[2]",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertNull(ex.evaluate(feature));

        // expect an exception when object supplied is not a complex attribute
        boolean exceptionThrown = false;
        try {
            ex.setLenient(false); // NC -added, only exception if lenient off
            assertNull(ex.evaluate(singleLeaf));
        } catch (Exception e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            fail("Expecting Exception since object passed in is not a complex attribute.");
        }

        // invalid property
        ex = new AttributeExpressionImpl(
                "randomAttribute", new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertNull(ex.evaluate(feature));
        assertNull(ex.evaluate(fType));

        // NC - test xml attribute
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[3]/eg:singleLeafAttribute/@eg:att",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals("test attribute", ex.evaluate(feature));

        // NC - test descriptor functionality

        ex = new AttributeExpressionImpl(
                "eg:simpleAttribute", new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(simpleAttributeDesc, ex.evaluate(fType));

        ex = new AttributeExpressionImpl(
                "eg:complexAttribute", new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(complexDesc, ex.evaluate(fType));

        // test nested properties
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(rootDesc, ex.evaluate(fType));

        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute/eg:singleLeafAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(singleLeafDesc, ex.evaluate(fType));

        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute/eg:multiLeafAttribute",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(multiLeafDesc, ex.evaluate(fType));

        // test that executing Java methods is blocked
        ex = new AttributeExpressionImpl(
                "java.lang.Thread.sleep(30000)",
                new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        long start = System.currentTimeMillis();
        assertNull(ex.evaluate(feature));
        long runtime = System.currentTimeMillis() - start;
        assertTrue("java.lang.Thread.sleep(30000) was executed", runtime < 30000);
        start = System.currentTimeMillis();
        assertNull(ex.evaluate(fType));
        runtime = System.currentTimeMillis() - start;
        assertTrue("java.lang.Thread.sleep(30000) was executed", runtime < 30000);
    }

    public static FeatureType createFeatureType() {
        FeatureTypeFactory tfac = new UniqueNameFeatureTypeFactoryImpl();
        Name fName = new NameImpl(EG, "complexFeatureType");

        Collection<PropertyDescriptor> schema = new ArrayList<>();

        // add simple attribute
        AttributeType attType = new AttributeTypeImpl(
                SIMPLE_ATTRIBUTE, String.class, false, false, Collections.emptyList(), null, null);

        AttributeDescriptor attDesc = new AttributeDescriptorImpl(attType, SIMPLE_ATTRIBUTE, 0, 1, true, null);

        schema.add(attDesc);

        // build nested attributes

        Collection<PropertyDescriptor> rootProperties = new ArrayList<>();

        attType = new AttributeTypeImpl(
                SINGLE_LEAF_ATTRIBUTE, String.class, false, false, Collections.emptyList(), null, null);
        attDesc = new AttributeDescriptorImpl(attType, SINGLE_LEAF_ATTRIBUTE, 0, 1, true, null);
        rootProperties.add(attDesc);

        attType = new AttributeTypeImpl(
                MULTI_LEAF_ATTRIBUTE, String.class, false, false, Collections.emptyList(), null, null);
        attDesc = new AttributeDescriptorImpl(attType, MULTI_LEAF_ATTRIBUTE, 0, -1, true, null);
        rootProperties.add(attDesc);

        attType =
                new ComplexTypeImpl(ROOT_ATTRIBUTE, rootProperties, false, false, Collections.emptyList(), null, null);
        attDesc = new AttributeDescriptorImpl(attType, ROOT_ATTRIBUTE, 0, -1, true, null);

        Collection<PropertyDescriptor> nestedProperties = new ArrayList<>(1);
        nestedProperties.add(attDesc);

        // add nested properties to complex attribute
        attType = new ComplexTypeImpl(
                COMPLEX_ATTRIBUTE, nestedProperties, false, false, Collections.emptyList(), null, null);

        attDesc = new AttributeDescriptorImpl(attType, COMPLEX_ATTRIBUTE, 0, -1, true, null);

        // add to feature schema
        schema.add(attDesc);

        return tfac.createFeatureType(fName, schema, null, false, Collections.emptyList(), null, null);
    }
}
