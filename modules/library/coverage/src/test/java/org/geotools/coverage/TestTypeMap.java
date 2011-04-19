/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;

import org.junit.Test;
import org.opengis.coverage.SampleDimensionType;

/**
 * Testing {@link TypeMap} class.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class TestTypeMap {

    @Test
    public void testGetSampleDimensionTypeSampleModel() {
        
        final SampleModel sm=new BufferedImage(512, 512, BufferedImage.TYPE_USHORT_555_RGB).getSampleModel();
        assertSame(SampleDimensionType.UNSIGNED_8BITS, TypeMap.getSampleDimensionType(
                sm, 0));
        assertSame(SampleDimensionType.UNSIGNED_8BITS, TypeMap.getSampleDimensionType(
                sm, 1));
        assertSame(SampleDimensionType.UNSIGNED_8BITS, TypeMap.getSampleDimensionType(
                sm, 2));
    }
   

    @Test
    public void testIsSigned() {
        assertTrue(TypeMap.isSigned(SampleDimensionType.REAL_32BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.REAL_64BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.SIGNED_16BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.SIGNED_32BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.SIGNED_8BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_16BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_1BIT));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_2BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_32BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_4BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_8BITS));
        
    }
   
}
