/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for InfoToolResult.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $URL$
 */
public class InfoToolResultTest {
    private InfoToolResult result;
    
    @Before
    public void setup() {
        result = new InfoToolResult();
    }
    
    @Test(expected=IllegalStateException.class)
    public void missingNewFeatureCall() {
        result.setFeatureValue("foo", 0);
    }
    
    @Test
    public void getNumFeatures() {
        result.newFeature("foo.1");
        assertEquals(1, result.getNumFeatures());
        result.newFeature("foo.2");
        assertEquals(2, result.getNumFeatures());
    }
    
    @Test
    public void getFeatureData() {
        result.newFeature("foo.1");
        result.setFeatureValue("bar", 1);
        result.setFeatureValue("baz", "one");
        
        result.newFeature("foo.2");
        result.setFeatureValue("bar", 2);
        result.setFeatureValue("baz", "two");
        
        assertEquals("foo.1", result.getFeatureId(0));
        Map<String, Object> featureData = result.getFeatureData(0);
        assertEquals(1, featureData.get("bar"));
        assertEquals("one", featureData.get("baz"));

        assertEquals("foo.2", result.getFeatureId(1));
        featureData = result.getFeatureData(1);
        assertEquals(2, featureData.get("bar"));
        assertEquals("two", featureData.get("baz"));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void getFeatureId_badIndex() {
        result.newFeature("foo.1");
        result.getFeatureId(1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void getFeatureData_badIndex() {
        result.newFeature("foo.1");
        result.getFeatureData(1);
    }
}
