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

package org.geotools.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.complex.ComplexFeatureConstants;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.gml3.GMLSchema;
import org.geotools.xs.XSSchema;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.identity.FeatureId;
import com.vividsolutions.jts.geom.EmptyGeometry;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Tests for {@link ComplexAttributeConverterFactory}.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Niels Charlier
 *
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/extension/app-schema/app-schema/src/test/java/org/geotools/util/ComplexAttributeConverterFactoryTest.java $
 */
public class ComplexAttributeConverterFactoryTest extends TestCase {
    
    /**
     * Test extracting complex attribute leaf value should be successful.
     */
    public void testLeafComplexAttribute() {
        Collection<Property> attributes = new ArrayList<Property>();
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(XSSchema.STRING_TYPE,
                ComplexFeatureConstants.SIMPLE_CONTENT, 1, 1, true, (Object) null);
        attributes.add(new AttributeImpl("rini", descriptor, null));
        ComplexAttribute gmlName = new ComplexAttributeImpl(attributes, GMLSchema.CODETYPE_TYPE, null);
        String nameString = Converters.convert(gmlName, String.class);
        assertEquals("rini", nameString);
    }
    
    /**
     * Test extracting complex attribute non-leaf value should fail.
     */
    public void testParentComplexAttribute() {
        Collection<Property> attributes = new ArrayList<Property>();
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(XSSchema.STRING_TYPE,
                ComplexFeatureConstants.SIMPLE_CONTENT, 1, 1, true, (Object) null);
        attributes.add(new AttributeImpl("rini", descriptor, null));
        ComplexAttribute gmlName = new ComplexAttributeImpl(attributes, GMLSchema.CODETYPE_TYPE, null);
        
        Collection<Property> parentAttributes = new ArrayList<Property>();
        parentAttributes.add(gmlName);
        ComplexAttribute parentAtt = new ComplexAttributeImpl(parentAttributes, GMLSchema.ABSTRACTFEATURETYPE_TYPE, null);
        String nameString = Converters.convert(parentAtt, String.class);
        
        assertEquals(parentAtt.toString(), nameString);
        assertNotSame("rini", nameString);
    }

    /**
     * Test that normal Attribute shouldn't be affected by the converter.
     */
    public void testAttribute() {
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(XSSchema.STRING_TYPE,
                ComplexFeatureConstants.SIMPLE_CONTENT, 1, 1, true, (Object) null);
        Attribute att = new AttributeImpl("rini", descriptor, null);
        Set<ConverterFactory> factories = Converters.getConverterFactories(att.getClass(), String.class);
        for (ConverterFactory factory : factories) {
            assertFalse(factory instanceof ComplexAttributeConverterFactory);
        }
    }
    
    /**
     * Test converting String to FeatureId successful. This is required by feature chaining.
     * @throws Exception
     */
    public void testFeatureId() throws Exception {
        FeatureId id = Converters.convert("blah", FeatureId.class);
        assertNotNull(id);
        assertEquals(id.getID(), "blah");
    }
    
    /**
    * Test extracting geometry from geometryattribute should be successful.
    */
    public void testGeometry() {
    	Geometry geometry = new EmptyGeometry();
		GeometryAttribute geoatt = new GeometryAttributeImpl(geometry, new GeometryDescriptorImpl(new GeometryTypeImpl(new NameImpl(""), EmptyGeometry.class, null, false, false, null, null, null), new NameImpl(""), 0, 0, false, null), null);
		Geometry geometry2 = Converters.convert(geoatt, Geometry.class);
   		assertTrue(geometry == geometry2);
    }
   
    
}
