/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.image;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;


/**
 * @author Sebastian Graca, ISPiK S.A.
 */
@RunWith(Parameterized.class)
public class ImageWorkerMultiColorTransparencyTest {
    public ImageWorkerMultiColorTransparencyTest(BufferedImage image) {
        this.image = image;
    }

    @Parameterized.Parameters
    public static List<BufferedImage[]> createImages() {
        return Arrays.asList(
                new BufferedImage[]{makeIndexedImage()},
                new BufferedImage[]{makeComponentImage()}
        );
    }

    @Test
    public void noTransparency() throws Exception {
        Color[] colors = {
                new Color(0xaa, 0xbb, 0xcc),
                new Color(0x11, 0x22, 0x33)
        };
        Color[][] expected = IMAGE_DATA;
        checkTransparency(colors, expected);
    }

    @Test
    public void singleColor() throws Exception {
        Color[] colors = {
                new Color(0xaa, 0xbb, 0xcc),
                new Color(0x11, 0x22, 0x33),
                new Color(0xb1, 0xb2, 0xb3)
        };
        Color[][] expected = new Color[][]{
                {new Color(0xa1, 0xa2, 0xa3), new Color(0xb1, 0xb2, 0xb3, 0x00), new Color(0xc1, 0xc2, 0xc3)},
                {new Color(0xd1, 0xd2, 0xd3), new Color(0xe1, 0xe2, 0xe3), new Color(0xf1, 0xf2, 0xf3)}
        };
        checkTransparency(colors, expected);
    }

    @Test
    public void multipleColors() throws Exception {
        Color[] colors = {
                new Color(0xaa, 0xbb, 0xcc),
                new Color(0x11, 0x22, 0x33),
                new Color(0xb1, 0xb2, 0xb3),
                new Color(0xd1, 0xd2, 0xd3)
        };
        Color[][] expected = new Color[][]{
                {new Color(0xa1, 0xa2, 0xa3), new Color(0xb1, 0xb2, 0xb3, 0x00), new Color(0xc1, 0xc2, 0xc3)},
                {new Color(0xd1, 0xd2, 0xd3, 0x00), new Color(0xe1, 0xe2, 0xe3), new Color(0xf1, 0xf2, 0xf3)}
        };
        checkTransparency(colors, expected);
    }

    private void checkTransparency(Color[] transparentColors, Color[][] expected) {
        ImageWorker w = new ImageWorker(makeIndexedImage());
        w.makeColorsTransparent(new HashSet<Color>(Arrays.asList(transparentColors)));
        RenderedImage actual = w.getRenderedImage();
        Raster raster = actual.getData();

        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int expectedRgb = expected[y][x].getRGB();
                int actualRgb = actual.getColorModel().getRGB(raster.getDataElements(x, y, null));
                assertEquals("x=" + x + " y=" + y, expectedRgb, actualRgb);
            }
        }
    }

    private static BufferedImage makeComponentImage() {
        BufferedImage image = new BufferedImage(IMAGE_DATA[0].length, IMAGE_DATA.length, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < IMAGE_DATA.length; ++y) {
            for (int x = 0; x < IMAGE_DATA[y].length; ++x) {
                image.setRGB(x, y, IMAGE_DATA[y][x].getRGB());
            }
        }
        return image;
    }

    private static BufferedImage makeIndexedImage() {
        int width = IMAGE_DATA[0].length;
        byte[][] rgb = new byte[3][width * IMAGE_DATA.length];
        for (int y = 0; y < IMAGE_DATA.length; ++y) {
            for (int x = 0; x < IMAGE_DATA[y].length; ++x) {
                rgb[0][y * width + x] = (byte)IMAGE_DATA[y][x].getRed();
                rgb[1][y * width + x] = (byte)IMAGE_DATA[y][x].getGreen();
                rgb[2][y * width + x] = (byte)IMAGE_DATA[y][x].getBlue();
            }
        }

        IndexColorModel colorModel = new IndexColorModel(8, width * IMAGE_DATA.length, rgb[0], rgb[1], rgb[2]);
        BufferedImage image = new BufferedImage(IMAGE_DATA[0].length, IMAGE_DATA.length, BufferedImage.TYPE_BYTE_INDEXED, colorModel);
        for (int y = 0; y < IMAGE_DATA.length; ++y) {
            for (int x = 0; x < IMAGE_DATA[y].length; ++x) {
                image.setRGB(x, y, IMAGE_DATA[y][x].getRGB());
            }
        }
        return image;
    }

    private final BufferedImage image;

    private static final Color[][] IMAGE_DATA = new Color[][]{
            {new Color(0xa1, 0xa2, 0xa3), new Color(0xb1, 0xb2, 0xb3), new Color(0xc1, 0xc2, 0xc3)},
            {new Color(0xd1, 0xd2, 0xd3), new Color(0xe1, 0xe2, 0xe3), new Color(0xf1, 0xf2, 0xf3)}
    };
}
