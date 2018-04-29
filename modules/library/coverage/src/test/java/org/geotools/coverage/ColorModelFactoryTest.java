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
package org.geotools.coverage;

import static org.junit.Assert.assertEquals;

import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import org.junit.Test;

/** Tests the {@link ColorModelFactory} implementation. */
public class ColorModelFactoryTest {

    @Test
    public void testGetColorModelType() {
        Category[] categories = new Category[0];
        ColorModel cm1 = ColorModelFactory.getColorModel(categories, DataBuffer.TYPE_BYTE, 0, 1);
        assertEquals(DataBuffer.TYPE_BYTE, cm1.getTransferType());
        ColorModel cm2 = ColorModelFactory.getColorModel(categories, DataBuffer.TYPE_USHORT, 0, 1);
        assertEquals(DataBuffer.TYPE_USHORT, cm2.getTransferType());
        ColorModel cm3 = ColorModelFactory.getColorModel(categories, DataBuffer.TYPE_FLOAT, 0, 1);
        assertEquals(DataBuffer.TYPE_FLOAT, cm3.getTransferType());
        ColorModel cm4 = ColorModelFactory.getColorModel(categories, DataBuffer.TYPE_DOUBLE, 0, 1);
        assertEquals(DataBuffer.TYPE_DOUBLE, cm4.getTransferType());
    }
}
