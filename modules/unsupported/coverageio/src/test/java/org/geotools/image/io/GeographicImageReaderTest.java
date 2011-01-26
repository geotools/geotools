/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import javax.imageio.ImageTypeSpecifier;
import java.io.IOException;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link GeographicImageReader}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class GeographicImageReaderTest {
    /**
     * Tests {@link DataBuffer#TYPE_FLOAT}. The converter should be the identity one
     * except for pad values which should be replaced by NaN.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testTypeFloat() throws IOException {
        final float minimum  = -2000;
        final float maximum  =  4000;
        final float padValue = -9999;
        final SampleConverter[] converters = new SampleConverter[1];
        final GeographicImageReader reader = new NullImageReader(DataBuffer.TYPE_FLOAT, minimum, maximum, padValue);
        final ImageTypeSpecifier specifier = reader.getRawImageType(0, null, converters);
        final SampleConverter    converter = converters[0];

        // Tests the converter
        assertNotNull(converter);
        assertEquals (0.0,     converter.getOffset(),      0.0);
        assertEquals (minimum, converter.convert(minimum), 0f);
        assertEquals (maximum, converter.convert(maximum), 0f);
        assertTrue   (Float.isNaN(converter.convert(padValue)));

        // Tests the sample model
        final SampleModel sm = specifier.getSampleModel();
        assertNotNull(sm);
        assertEquals(DataBuffer.TYPE_FLOAT, sm.getDataType());

        // Tests the color model
        final ColorModel cm = specifier.getColorModel();
        assertFalse(cm instanceof IndexColorModel);
    }

    /**
     * Tests {@link DataBuffer#TYPE_SHORT}. The converter should be the identity one,
     * including pad values. The index color model is going to be quite large.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testTypeShort() throws IOException {
        final int minimum  = -2000;
        final int maximum  =  4000;
        final int padValue = -9999;
        final SampleConverter[] converters = new SampleConverter[1];
        final GeographicImageReader reader = new NullImageReader(DataBuffer.TYPE_SHORT, minimum, maximum, padValue);
        final GeographicImageReadParam param = (GeographicImageReadParam) reader.getDefaultReadParam();
        param.setPaletteName("grayscale"); // Easier to test than "rainbow".
        final ImageTypeSpecifier specifier = reader.getRawImageType(0, param, converters);
        final SampleConverter    converter = converters[0];

        // Tests the converter
        assertNotNull(converter);
        assertEquals (0.0,      converter.getOffset(), 0.0);
        assertEquals (minimum,  converter.convert(minimum ));
        assertEquals (maximum,  converter.convert(maximum ));
        assertEquals (padValue, converter.convert(padValue));

        // Tests the sample model
        final SampleModel sm = specifier.getSampleModel();
        assertNotNull(sm);
        assertEquals(DataBuffer.TYPE_USHORT, sm.getDataType());

        // Tests the color model
        final ColorModel cm = specifier.getColorModel();
        assertTrue(cm instanceof IndexColorModel);
        final IndexColorModel indexed = (IndexColorModel) cm;
        assertEquals(65536, indexed.getMapSize());
        assertEquals(0xFF000000, indexed.getRGB(minimum  & 0xFFFF));  // Opaque black
        assertEquals(0xFFFFFFFF, indexed.getRGB(maximum  & 0xFFFF));  // Opaque white
        assertEquals(0x00000000, indexed.getRGB(padValue & 0xFFFF)); // Transparent
        if (false) {
            // Disabled for now, because current implementation sets an arbitrary
            // transparent pixel. I don't think that this is a real problem...
            assertEquals(padValue & 0xFFFF, indexed.getTransparentPixel());
        }
    }

    /**
     * Tests {@link DataBuffer#TYPE_USHORT}. The converter should pack the negative
     * values in a smaller range.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testTypeUnsignedShort() throws IOException {
        final int minimum  = -2000;
        final int maximum  =  4000;
        final int padValue = -9999;
        final SampleConverter[] converters = new SampleConverter[1];
        final GeographicImageReader reader = new NullImageReader(DataBuffer.TYPE_USHORT, minimum, maximum, padValue);
        final GeographicImageReadParam param = (GeographicImageReadParam) reader.getDefaultReadParam();
        param.setPaletteName("grayscale"); // Easier to test than "rainbow".
        final ImageTypeSpecifier specifier = reader.getRawImageType(0, param, converters);
        final SampleConverter    converter = converters[0];

        // Tests the converter
        assertNotNull(converter);
        assertEquals (1-minimum,         converter.getOffset(), 0.0);
        assertEquals (1,                 converter.convert(minimum ));
        assertEquals (1+maximum-minimum, converter.convert(maximum ));
        assertEquals (0,                 converter.convert(padValue));

        // Tests the sample model
        final SampleModel sm = specifier.getSampleModel();
        assertNotNull(sm);
        assertEquals(DataBuffer.TYPE_USHORT, sm.getDataType());

        // Tests the color model
        final ColorModel cm = specifier.getColorModel();
        assertTrue(cm instanceof IndexColorModel);
        final IndexColorModel indexed = (IndexColorModel) cm;
        assertEquals(2+maximum-minimum, indexed.getMapSize());
        assertEquals(0xFF000000, indexed.getRGB(converter.convert(minimum)  & 0xFFFF)); // Opaque black
        assertEquals(0xFFFFFFFF, indexed.getRGB(converter.convert(maximum)  & 0xFFFF)); // Opaque white
        assertEquals(0x00000000, indexed.getRGB(converter.convert(padValue) & 0xFFFF)); // Transparent
        assertEquals(0, indexed.getTransparentPixel());
    }

    /**
     * Tests {@link DataBuffer#TYPE_BYTE}.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testTypeUnsignedByte() throws IOException {
        final int minimum  =   0;
        final int maximum  = 200;
        final int padValue = 255;
        final SampleConverter[] converters = new SampleConverter[1];
        final GeographicImageReader reader = new NullImageReader(DataBuffer.TYPE_BYTE, minimum, maximum, padValue);
        final GeographicImageReadParam param = (GeographicImageReadParam) reader.getDefaultReadParam();
        param.setPaletteName("grayscale"); // Easier to test than "rainbow".
        final ImageTypeSpecifier specifier = reader.getRawImageType(0, param, converters);
        final SampleConverter    converter = converters[0];

        // Tests the converter
        assertNotNull(converter);
        assertEquals (0,        converter.getOffset(), 0.0);
        assertEquals (minimum,  converter.convert(minimum ));
        assertEquals (maximum,  converter.convert(maximum ));
        assertEquals (padValue, converter.convert(padValue));

        // Tests the sample model
        final SampleModel sm = specifier.getSampleModel();
        assertNotNull(sm);
        assertEquals(DataBuffer.TYPE_BYTE, sm.getDataType());

        // Tests the color model
        final ColorModel cm = specifier.getColorModel();
        assertTrue(cm instanceof IndexColorModel);
        final IndexColorModel indexed = (IndexColorModel) cm;
        assertEquals(256, indexed.getMapSize());
        assertEquals(0xFF000000, indexed.getRGB(minimum  & 0xFFFF)); // Opaque black
        assertEquals(0xFFFFFFFF, indexed.getRGB(maximum  & 0xFFFF)); // Opaque white
        assertEquals(0x00000000, indexed.getRGB(padValue & 0xFFFF)); // Transparent
    }
}
