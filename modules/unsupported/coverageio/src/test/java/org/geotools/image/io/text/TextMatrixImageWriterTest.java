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
package org.geotools.image.io.text;

import java.io.StringWriter;
import java.util.Locale;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.imageio.IIOImage;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.text.FieldPosition;
import java.text.NumberFormat;

import org.geotools.image.io.PaletteFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Tests {@link TextMatrixImageWriter}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class TextMatrixImageWriterTest extends TestCase {
    /**
     * The image to test.
     */
    private static IIOImage image;

    /**
     * The writer to test.
     */
    private static TextMatrixImageWriter writer;

    /**
     * Run the suite from the command line.
     */
    public static void main(final String[] args) {
        org.geotools.util.logging.Logging.GEOTOOLS.forceMonolineConsoleOutput();
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Returns the test suite.
     */
    public static Test suite() {
        return new TestSuite(TextMatrixImageWriterTest.class);
    }

    /**
     * Constructs a test case with the given name.
     */
    public TextMatrixImageWriterTest(final String name) {
        super(name);
    }

    /**
     * Creates the image to test.
     */
    @Override
    protected void setUp() throws IOException {
        if (image != null) {
            return;
        }
        final int width  = 8;
        final int height = 10;
        final ColorModel cm = PaletteFactory.getDefault().getContinuousPalette(
                "grayscale", 0f, 1f, DataBuffer.TYPE_FLOAT, 1, 0).getColorModel();
        final WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                double value = (10*y + x) / 100.0;
                if (y >= 5) {
                    value += 88;
                }
                raster.setSample(x, y, 0, value);
            }
        }
        image = new IIOImage(new BufferedImage(cm, raster, false, null), null, null);
        TextMatrixImageWriter.Spi spi = new TextMatrixImageWriter.Spi();
        spi.locale = Locale.CANADA;
        writer = new TextMatrixImageWriter(spi);
    }

    /**
     * Tests the number format.
     */
    public void testCreateNumberFormat() {
        assertEquals(Locale.CANADA, writer.getDataLocale(null));

        final NumberFormat format = writer.createNumberFormat(image, null);
        assertEquals(2, format.getMinimumFractionDigits());
        assertEquals(2, format.getMaximumFractionDigits());
        assertEquals(1, format.getMinimumIntegerDigits());
        assertEquals( "0.12", format.format( 0.1216));
        assertEquals("-0.30", format.format(-0.2978));

        final FieldPosition pos = writer.getExpectedFractionPosition(format);
        assertEquals("Field type", NumberFormat.FRACTION_FIELD, pos.getField());
        assertEquals("Fraction width", 2, pos.getEndIndex() - pos.getBeginIndex());
        assertEquals("Total width (including sign)", 6, pos.getEndIndex());
    }

    /**
     * Tests the write operation.
     */
    public void testWrite() throws IOException {
        final StringWriter out = new StringWriter();
        writer.setOutput(out);
        writer.write(image);
        out.close();
        final StringTokenizer tokens = new StringTokenizer(out.toString());
        while (tokens.hasMoreTokens()) {
            final String t = tokens.nextToken();
            final int dot = t.indexOf('.');
            assertTrue(dot >= 0);
            assertEquals(3, t.length() - dot);
            final float value = Float.parseFloat(t);
            assertTrue(value>=0 && value<100);
        }
    }
}
