/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jamesm
 * @source $URL$
 */
public class FlatFeatureFactorySpiTest extends TestCase {
    
    public FlatFeatureFactorySpiTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FlatFeatureFactorySpiTest.class);
        return suite;
    }
    
    public void testObtainFactory(){
        FeatureTypeFactory factory = FeatureTypeFactory.newInstance("test");
        assertNotNull(factory);
    }
    
    public void testDefaultFactory() {
       FeatureTypeFactory factory = FeatureTypeFactory.newInstance("test");
       AttributeType a1 = AttributeTypeFactory.newAttributeType("X",Integer.class);
       AttributeType a2 = AttributeTypeFactory.newAttributeType("Y",Double.class);
       factory.addType(0,a1);
       assertEquals(a1, factory.get(factory.getAttributeCount() - 1));
       factory.addType(1,a2);
       assertEquals(a2, factory.get(factory.getAttributeCount() - 1));
       factory.removeType(1);
       factory.removeType(0);
       assertEquals(factory.getAttributeCount(),0);
       factory.addType(a1);
       factory.addType(a2);
       factory.swap(0,1);
       assertEquals(a1, factory.get(1));
       assertEquals(a2, factory.get(0));
       factory.removeType(a1);
       factory.removeType(a2);
       assertEquals(factory.getAttributeCount(),0);
    } 
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    
}
