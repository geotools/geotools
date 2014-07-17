/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.range.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.junit.Test;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;

/**
 * 
 * @author Nicola Lagomarsini Geosolutions
 *
 */
public class TypeTest {

    @Test
    public void testTypes() {
        // Creation of a default field type
        Set<SampleDimension> sampleDims = new HashSet<SampleDimension>();
        GridSampleDimension sampleDim = new GridSampleDimension("test");
        sampleDims.add(sampleDim);
        NameImpl name = new NameImpl("test");
        SimpleInternationalString description = new SimpleInternationalString("test");
        FieldType fieldType = new DefaultFieldType(name, description, sampleDims);
        
        // Getting the input data and checking if they are equals
        assertSame(name, fieldType.getName());
        assertSame(description, fieldType.getDescription());
        assertTrue(fieldType.getSampleDimensions().contains(sampleDim));
        
        // Creation of a RangeType
        RangeType rangeType = new DefaultRangeType("test", "test", fieldType);
        
        assertTrue(rangeType.getName().equals(name));
        assertTrue(description.compareTo(rangeType.getDescription()) == 0);
        assertEquals(1, rangeType.getNumFieldTypes());
        assertSame(fieldType, rangeType.getFieldType("test"));
        
        Set<Name> names = rangeType.getFieldTypeNames();
        assertNotNull(names);
        assertTrue(names.size() == 1);
        assertTrue(names.contains(name));
        
        Set<FieldType> fieldTypes = rangeType.getFieldTypes();
        assertNotNull(fieldTypes);
        assertTrue(fieldTypes.size() == 1);
        assertTrue(fieldTypes.contains(fieldType));
        assertTrue(rangeType.toString().contains(fieldType.toString()));
    }
}
