/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.ArrayList;
import org.geotools.data.complex.expression.FeaturePropertyAccessorFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.ComplexFeatureBuilder;
import org.geotools.feature.FakeTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.FeatureTypeImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.util.factory.Hints;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory2;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Test {@link AttributeCountFunction}
 *
 * @author Rini Angreani (CSIRO Mineral Resources)
 */
public class AttributeCountFunctionTest extends FunctionTestSupport {

    Feature complexFeature;

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /** @param testName */
    public AttributeCountFunctionTest(String testName) {
        super(testName);
    }

    @Override
    public void setUp() throws Exception {
        // set up simple features
        super.setUp();

        // nested feature type
        ArrayList<PropertyDescriptor> childSchema = new ArrayList<PropertyDescriptor>();
        Name attOne = new NameImpl("ns", "att1");
        AttributeDescriptor attOneDescriptor =
                new AttributeDescriptorImpl(FakeTypes.STRING_TYPE, attOne, 0, -1, false, null);
        childSchema.add(attOneDescriptor);
        FeatureType childType =
                new FeatureTypeImpl(
                        new NameImpl("ns", "childType"),
                        childSchema,
                        null,
                        false,
                        null,
                        null,
                        null);

        // parent feature type
        ArrayList<PropertyDescriptor> parentSchema = new ArrayList<PropertyDescriptor>();
        parentSchema.add(attOneDescriptor);
        Name attTwo = new NameImpl("ns", "att2");
        AttributeDescriptor attTwoDescriptor =
                new AttributeDescriptorImpl(childType, attTwo, 1, -1, false, null);
        parentSchema.add(attTwoDescriptor);
        FeatureType parentType =
                new FeatureTypeImpl(
                        new NameImpl("ns", "parentType"),
                        parentSchema,
                        null,
                        false,
                        null,
                        null,
                        null);

        // build complex feature
        ComplexFeatureBuilder builder = new ComplexFeatureBuilder(childType);
        // att2/childType/att1[1]
        builder.append(attOne, new AttributeImpl("test1", attOneDescriptor, null));
        // att2/childType/att1[2]
        builder.append(attOne, new AttributeImpl("test2", attOneDescriptor, null));
        Feature childFeature = builder.buildFeature("childFeature");

        builder = new ComplexFeatureBuilder(parentType);
        ArrayList<Property> childFeatures = new ArrayList<Property>();
        childFeatures.add(childFeature);
        builder.append(attTwo, new ComplexAttributeImpl(childFeatures, attTwoDescriptor, null));
        complexFeature = builder.buildFeature("parentFeature");
    }

    public void testComplexFeature() {
        // have to pass on namespaceSupport to enable FeaturePropertyAccessor
        NamespaceSupport ns = new NamespaceSupport();
        ns.declarePrefix("ns", "ns");
        Hints hints = new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, ns);

        // there is no parentFeature/att1
        AttributeExpressionImpl att1 = new AttributeExpressionImpl("ns:att1", hints);
        ff.function("attributeCount", att1);
        assertEquals(0, ff.function("attributeCount", att1).evaluate(complexFeature));

        // check for att2
        AttributeExpressionImpl att2 = new AttributeExpressionImpl("ns:att2", hints);
        ff.function("attributeCount", att2);
        assertEquals(1, ff.function("attributeCount", att2).evaluate(complexFeature));

        // check att2/childType/att1
        AttributeExpressionImpl nestedPath =
                new AttributeExpressionImpl("ns:att2/ns:childType/ns:att1", hints);
        ff.function("attributeCount", nestedPath);
        assertEquals(2, ff.function("attributeCount", nestedPath).evaluate(complexFeature));
    }

    public void testSimpleFeature() {
        SimpleFeature f = featureCollection.features().next();

        AttributeExpressionImpl foo = new AttributeExpressionImpl("foo");
        assertEquals(1, ff.function("attributeCount", foo).evaluate(f));

        AttributeExpressionImpl att1 = new AttributeExpressionImpl("att1");
        assertEquals(0, ff.function("attributeCount", att1).evaluate(f));
    }
}
