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
package org.geotools.filter.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;

import org.geotools.factory.Hints;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to demonstrate evaluating XPaths as attribute expressions when complex
 * attributes/features are passed in, instead of simple features. This is necessary since complex
 * features could contain nested properties, and we should be able to get properties of any level
 * from the features.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 * @source $URL$
 */
public class FeaturePropertyAccessorTest extends TestCase {
    /**
     * Mock name space prefix
     */
    static final String EG = "urn:cgi:xmlns:Example:1.0";

    static final Name SIMPLE_ATTRIBUTE = new NameImpl(EG, "simpleAttribute");

    static final Name COMPLEX_ATTRIBUTE = new NameImpl(EG, "complexAttribute");

    static final Name ROOT_ATTRIBUTE = new NameImpl(EG, "rootAttribute");

    static final Name SINGLE_LEAF_ATTRIBUTE = new NameImpl(EG, "singleLeafAttribute");

    static final Name MULTI_LEAF_ATTRIBUTE = new NameImpl(EG, "multiLeafAttribute");

    /**
     * Mock name space
     */
    static final NamespaceSupport NAMESPACES = new NamespaceSupport() {
        {
            declarePrefix("eg", EG);
        }
    };

    public void testComplexFeature() {
        FeatureType fType = createFeatureType();

        ComplexType complexAttType = (ComplexType) fType.getDescriptor(COMPLEX_ATTRIBUTE).getType();
        ComplexType rootAttType = (ComplexType) complexAttType.getDescriptor(ROOT_ATTRIBUTE)
                .getType();

        // feature properties
        Collection<Property> properties = new ArrayList<Property>(fType.getDescriptors().size());

        /**
         * Build the feature properties
         */
        // eg:simpleAttribute
        AttributeDescriptor attDesc = (AttributeDescriptor) fType.getDescriptor(SIMPLE_ATTRIBUTE);
        AttributeImpl simpleAttribute = new AttributeImpl("simple value", attDesc, null);
        properties.add(simpleAttribute);
        // eg:complexAttribute/eg:rootAttribute[1]
        Collection<Property> rootPropertiesOne = new ArrayList<Property>();
        attDesc = (AttributeDescriptor) rootAttType.getDescriptor(MULTI_LEAF_ATTRIBUTE);
        // eg:complexAttribute/eg:rootAttribute[1]/eg:multiLeafAttribute[1]
        AttributeImpl leafOne = new AttributeImpl("multi leaf value 1", attDesc, null);
        rootPropertiesOne.add(leafOne);
        // eg:complexAttribute/eg:rootAttribute[1]/eg:multiLeafAttribute[2]
        AttributeImpl leafTwo = new AttributeImpl("multi leaf value 2", attDesc, null);
        rootPropertiesOne.add(leafTwo);
        // eg:complexAttribute/eg:rootAttribute[1]/eg:singleLeafAttribute
        attDesc = (AttributeDescriptor) rootAttType.getDescriptor(SINGLE_LEAF_ATTRIBUTE);
        AttributeImpl singleLeaf = new AttributeImpl("single leaf value", attDesc, null);
        rootPropertiesOne.add(singleLeaf);

        AttributeDescriptor rootDesc = (AttributeDescriptor) complexAttType
                .getDescriptor(ROOT_ATTRIBUTE);
        AttributeImpl rootOne = new ComplexAttributeImpl(rootPropertiesOne, rootDesc, null);

        // eg:complexAttribute/eg:rootAttribute[2]
        Collection<Property> rootPropertiesTwo = new ArrayList<Property>();
        // eg:complexAttribute/eg:rootAttribute[2]/eg:multiLeafAttribute[1]
        rootPropertiesTwo.add(leafOne);
        AttributeImpl rootTwo = new ComplexAttributeImpl(rootPropertiesTwo, rootDesc, null);

        // eg:complexAttribute/eg:rootAttribute[3]
        Collection<Property> rootPropertiesThree = new ArrayList<Property>();
        // eg:complexAttribute/eg:rootAttribute[3]/eg:singleLeafAttribute
        rootPropertiesThree.add(singleLeaf);
        AttributeImpl rootThree = new ComplexAttributeImpl(rootPropertiesThree, rootDesc, null);

        Collection<Property> complexProperties = new ArrayList<Property>(2);
        complexProperties.add(rootOne);
        complexProperties.add(rootTwo);
        complexProperties.add(rootThree);
        AttributeImpl complexAttribute = new ComplexAttributeImpl(complexProperties,
                complexAttType, null);
        properties.add(complexAttribute);

        Feature feature = new FeatureImpl(properties, fType, new FeatureIdImpl("test1"));

        /**
         * Test evaluating complex feature
         */
        // test evaluating simple property
        AttributeExpressionImpl ex = new AttributeExpressionImpl("eg:simpleAttribute", new Hints(
                FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(simpleAttribute, ex.evaluate(feature));

        // test evaluating complex property
        ex = new AttributeExpressionImpl("eg:complexAttribute", new Hints(
                FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(complexAttribute, ex.evaluate(feature));

        // test multi-valued nested properties
        ex = new AttributeExpressionImpl("eg:complexAttribute/eg:rootAttribute", new Hints(
                FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        // no index would return the first one
        assertEquals(rootOne, ex.evaluate(feature));

        // index specified
        ex = new AttributeExpressionImpl("eg:complexAttribute/eg:rootAttribute[2]", new Hints(
                FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(rootTwo, ex.evaluate(feature));

        // single valued nested property
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[3]/eg:singleLeafAttribute", new Hints(
                        FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(singleLeaf, ex.evaluate(feature));

        // null values
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[3]/eg:multiLeafAttribute", new Hints(
                        FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(null, ex.evaluate(feature));

        // deeper nesting
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[2]/eg:multiLeafAttribute", new Hints(
                        FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(leafOne, ex.evaluate(feature));

        // property index doesn't exist
        ex = new AttributeExpressionImpl(
                "eg:complexAttribute/eg:rootAttribute[2]/eg:multiLeafAttribute[2]", new Hints(
                        FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(null, ex.evaluate(feature));

        // expect an exception when object supplied is not a complex attribute
        boolean exceptionThrown = false;
        try {
            ex.setLenient(false); //NC -added, only exception if lenient off
            assertEquals(null, ex.evaluate(singleLeaf));
        } catch (Exception e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            fail("Expecting Exception since object passed in is not a complex attribute.");
        }

        // invalid property
        ex = new AttributeExpressionImpl("randomAttribute", new Hints(
                FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, NAMESPACES));
        assertEquals(null, ex.evaluate(feature));
    }

    public static FeatureType createFeatureType() {
        FeatureTypeFactory tfac = new UniqueNameFeatureTypeFactoryImpl();
        Name fName = new NameImpl(EG, "complexFeatureType");

        Collection<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();

        // add simple attribute
        AttributeType attType = new AttributeTypeImpl(SIMPLE_ATTRIBUTE, String.class, false, false,
                Collections.EMPTY_LIST, null, null);

        AttributeDescriptor attDesc = new AttributeDescriptorImpl(attType, SIMPLE_ATTRIBUTE, 0, 1,
                true, null);

        schema.add(attDesc);

        // build nested attributes

        Collection<PropertyDescriptor> rootProperties = new ArrayList<PropertyDescriptor>();

        attType = new AttributeTypeImpl(SINGLE_LEAF_ATTRIBUTE, String.class, false, false,
                Collections.EMPTY_LIST, null, null);
        attDesc = new AttributeDescriptorImpl(attType, SINGLE_LEAF_ATTRIBUTE, 0, 1, true, null);
        rootProperties.add(attDesc);

        attType = new AttributeTypeImpl(MULTI_LEAF_ATTRIBUTE, String.class, false, false,
                Collections.EMPTY_LIST, null, null);
        attDesc = new AttributeDescriptorImpl(attType, MULTI_LEAF_ATTRIBUTE, 0, -1, true, null);
        rootProperties.add(attDesc);

        attType = new ComplexTypeImpl(ROOT_ATTRIBUTE, rootProperties, false, false,
                Collections.EMPTY_LIST, null, null);
        attDesc = new AttributeDescriptorImpl(attType, ROOT_ATTRIBUTE, 0, -1, true, null);

        Collection<PropertyDescriptor> nestedProperties = new ArrayList<PropertyDescriptor>(1);
        nestedProperties.add(attDesc);

        // add nested properties to complex attribute
        attType = new ComplexTypeImpl(COMPLEX_ATTRIBUTE, nestedProperties, false, false,
                Collections.EMPTY_LIST, null, null);

        attDesc = new AttributeDescriptorImpl(attType, COMPLEX_ATTRIBUTE, 0, -1, true, null);

        // add to feature schema
        schema.add(attDesc);

        return tfac.createFeatureType(fName, schema, null, false, Collections.EMPTY_LIST, null,
                null);
    }

}
