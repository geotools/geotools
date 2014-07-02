/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012 - 2014, Open Source Geospatial Foundation (OSGeo)
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

import javax.measure.Measure;
import javax.measure.unit.Unit;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;

import org.geotools.coverage.io.range.Axis;
import org.geotools.factory.GeoTools;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Simone Giannecchini, GeoSolutions
 * 
 * @source $URL$
 */
public class DimensionlessAxisTest extends Assert {

    /**
     * Toy Axis consisting of three bands named A, B and C.
     * <p>
     * This really is a toy example; if you have a formal fixed data dictionary consider the use of a Java Enumeration (and EnumMeasure), if you have
     * an open ended data dictionary consider a CodeList (and CodeMeasure).
     */
    @Test
    public void testTOY() {
        DimensionlessAxis TOY = new DimensionlessAxis(new String[] { "A", "B", "C" }, new NameImpl(
                "Color"), new SimpleInternationalString("Toy Example"));

        assertEquals(Unit.ONE, TOY.getUnitOfMeasure());
        BandIndexMeasure key = TOY.getKey(0);
        assertEquals("A", key.getValue());
        assertNull(TOY.getCoordinateReferenceSystem());
    }

    /**
     * Depth represented as an axis of one band
     */
    @Test
    public void testElevation() {
        DimensionlessAxis HEIGHT = new DimensionlessAxis(1, new NameImpl("height"),
                new SimpleInternationalString("Height from sealevel"));
        assertEquals(Unit.ONE, HEIGHT.getUnitOfMeasure());
        BandIndexMeasure key = HEIGHT.getKey(0);
        assertEquals("0", key.getValue());

        // Make sure we can discover everything we need via the Axis API
        Axis axis = HEIGHT;
        assertEquals(Unit.ONE, axis.getUnitOfMeasure());
        Measure measure = axis.getKey(0);
        assertEquals(Unit.ONE, measure.getUnit());
        assertEquals("0", key.getValue());
    }
    
    /**
     * Test which creates a {@link DimensionlessAxis} from an Image
     */
    @Test
    public void testImage() {
        // Image creation
        RenderedOp constant = ConstantDescriptor.create(20f, 20f, new Byte[] { 1 },
                GeoTools.getDefaultHints());
        // Axis creation
        DimensionlessAxis sample = DimensionlessAxis.createFromRenderedImage(constant);
        // Minor checks
        assertTrue(sample.getName().equals(new NameImpl("GRAY-AXIS")));
        assertTrue(sample.getDescription().compareTo(new SimpleInternationalString("Axis for GRAY bands")) == 0);
        assertNotNull(sample.getKeys());
        BandIndexMeasure band = sample.getKey(0);
        assertTrue(band != null);
        constant.dispose();
    }

}
