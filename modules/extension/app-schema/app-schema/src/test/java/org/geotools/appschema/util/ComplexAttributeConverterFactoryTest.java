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

package org.geotools.appschema.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.data.util.ComplexAttributeConverterFactory;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.gml3.GMLSchema;
import org.geotools.util.Converters;
import org.geotools.xs.XSSchema;
import org.hamcrest.CoreMatchers;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.identity.FeatureId;

/**
 * Tests for {@link ComplexAttributeConverterFactory}.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Niels Charlier
 */
public class ComplexAttributeConverterFactoryTest extends TestCase {

    /** Test extracting complex attribute leaf value should be successful. */
    public void testLeafComplexAttribute() {
        Collection<Property> attributes = new ArrayList<Property>();
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        ComplexFeatureConstants.SIMPLE_CONTENT,
                        1,
                        1,
                        true,
                        (Object) null);
        attributes.add(new AttributeImpl("rini", descriptor, null));
        ComplexAttribute gmlName =
                new ComplexAttributeImpl(attributes, GMLSchema.CODETYPE_TYPE, null);
        String nameString = Converters.convert(gmlName, String.class);
        assertEquals("rini", nameString);
    }

    /** Test extracting complex attribute non-leaf value should fail. */
    public void testParentComplexAttribute() {
        Collection<Property> attributes = new ArrayList<Property>();
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        ComplexFeatureConstants.SIMPLE_CONTENT,
                        1,
                        1,
                        true,
                        (Object) null);
        attributes.add(new AttributeImpl("rini", descriptor, null));
        ComplexAttribute gmlName =
                new ComplexAttributeImpl(attributes, GMLSchema.CODETYPE_TYPE, null);

        Collection<Property> parentAttributes = new ArrayList<Property>();
        parentAttributes.add(gmlName);
        ComplexAttribute parentAtt =
                new ComplexAttributeImpl(
                        parentAttributes, GMLSchema.ABSTRACTFEATURETYPE_TYPE, null);
        String nameString = Converters.convert(parentAtt, String.class);

        assertEquals(parentAtt.toString(), nameString);
        assertNotSame("rini", nameString);
    }

    /** Test converting String to FeatureId successful. This is required by feature chaining. */
    public void testFeatureId() throws Exception {
        FeatureId id = Converters.convert("blah", FeatureId.class);
        assertNotNull(id);
        assertEquals(id.getID(), "blah");
    }

    /** Test extracting geometry from geometryattribute should be successful. */
    public void testGeometry() {
        Geometry geometry =
                new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING))
                        .createGeometryCollection();
        GeometryAttribute geoatt =
                new GeometryAttributeImpl(
                        geometry,
                        new GeometryDescriptorImpl(
                                new GeometryTypeImpl(
                                        new NameImpl(""),
                                        GeometryCollection.class,
                                        null,
                                        false,
                                        false,
                                        null,
                                        null,
                                        null),
                                new NameImpl(""),
                                0,
                                0,
                                false,
                                null),
                        null);
        Geometry geometry2 = Converters.convert(geoatt, Geometry.class);
        assertTrue(geometry == geometry2);
    }

    /** Checks that an attribute value is correctly converted to the expected type. */
    public void testAttributeConversion() {
        // create an attribute containing a double
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(
                        XSSchema.DOUBLE_TYPE,
                        ComplexFeatureConstants.SIMPLE_CONTENT,
                        1,
                        1,
                        true,
                        null);
        AttributeImpl attribute = new AttributeImpl(35.0, descriptor, null);
        // convert the attribute to a number
        Object result = Converters.convert(attribute, Double.class);
        assertThat(result, instanceOf(Double.class));
        assertThat(result, is(35.0));
        // convert the attribute to a string
        result = Converters.convert(attribute, String.class);
        assertThat(result, instanceOf(String.class));
        assertThat(result, is("35.0"));
    }

    /**
     * Checks that a list of attributes is correctly convert to a concatenated string and that only
     * string conversion is supported.
     */
    public void testAttributeListConversion() {
        // create two attributes containing an itneger
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(
                        XSSchema.INTEGER_TYPE,
                        ComplexFeatureConstants.SIMPLE_CONTENT,
                        1,
                        1,
                        true,
                        null);
        AttributeImpl attribute1 = new AttributeImpl(35, descriptor, null);
        AttributeImpl attribute2 = new AttributeImpl(40, descriptor, null);
        // create a list of attributes
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);
        // convert the list of attributes to a string
        Object result = Converters.convert(attributes, String.class);
        assertThat(result, instanceOf(String.class));
        assertThat(result, is("35, 40"));
        // try to convert to a integer, should yield NULL
        result = Converters.convert(attributes, Integer.class);
        assertThat(result, CoreMatchers.nullValue());
    }
}
