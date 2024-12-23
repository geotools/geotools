/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.test.TestData;
import org.junit.BeforeClass;
import org.junit.Test;

public class MergeBehaviorTest {

    static File mergeExt;

    @BeforeClass
    public static void prepareTestMosaic() throws Exception {
        File mosaicSource = TestData.file(MergeBehaviorTest.class, "merge_ext");
        mergeExt = new File("target", "merge_ext");
        FileUtils.deleteQuietly(mergeExt);
        FileUtils.copyDirectory(mosaicSource, mergeExt);
    }

    @Test
    public void testGetValuesAsString() {
        String[] values = MergeBehavior.valuesAsStrings();
        assertNotNull(values);
        Set<String> testSet = new HashSet<>(Arrays.asList(values));
        Set<String> expectedSet =
                Arrays.stream(MergeBehavior.values()).map(v -> v.name()).collect(Collectors.toSet());
        assertEquals(expectedSet, testSet);
    }

    @Test
    public void testMax() throws Exception {
        // Unioning the images should result in the maximum value of each pixel, which would look
        // like this, five high value centers, and low value at the borders.
        // @@@@%%%%%%#####%%%%%%%%%%#####%%%%%%%@@@
        // @@%%%%##############%##############%%%@@
        // %%%%####**********#####**********####%%%
        // %%####****+++++*****#****+++++*****###%%
        // %###***+++++++++++****+++++++++++***###%
        // %##***+++===-===+++**+++===-===+++***###
        // %##***+++=-:::-==++**+++=-:::-==++***###
        // %##***+++=-:::-==+++++++=-:::-==++***###
        // %###**+++=======++++++++=======+++***###
        // %###****+++++++++======++++++++++***###%
        // %%####****++++++==-::--=++++++*****###%%
        // %%%###******++++==-:::-=+++++*****####%%
        // %%###***+++++++++==--==+++++++++****###%
        // %###***+++======+++++++++======+++***##%
        // %##***+++=--::-==+++++++=--::-==++***###
        // %##***++==-:::-==++**++==-:::-==++***###
        // %##***+++==---==+++**+++==---==+++***###
        // %###***+++++=+++++****+++++=+++++***###%
        // %%###****+++++++********+++++++****###%%
        // %%%####***********####***********####%%%
        // @@%%%##############################%%%%@
        // @@@%%%%%%########%%%%%%%########%%%%%@@@

        ImageMosaicReader reader = new ImageMosaicReader(mergeExt);
        try {
            final ParameterValue<String> mergeBehavior = ImageMosaicFormat.MERGE_BEHAVIOR.createValue();
            mergeBehavior.setValue(MergeBehavior.MAX.toString());
            GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {mergeBehavior});
            assertNotNull(coverage);

            // extract underlying raster
            RenderedImage ri = coverage.getRenderedImage();
            assertEquals(160, ri.getWidth());
            assertEquals(160, ri.getHeight());
            SampleModel sm = ri.getSampleModel();
            assertEquals(1, sm.getNumBands());
            assertEquals(DataBuffer.TYPE_BYTE, sm.getDataType());

            // the various image centers have top value, 50
            assertPixelValue(50, ri, 80, 80);
            assertPixelValue(50, ri, 50, 50);
            assertPixelValue(50, ri, 50, 110);
            assertPixelValue(50, ri, 110, 110);
            assertPixelValue(50, ri, 110, 50);

            // diagonal crossings across centers, mid value, 37
            assertPixelValue(35, ri, 65, 65);
            assertPixelValue(35, ri, 95, 65);
            assertPixelValue(35, ri, 95, 95);
            assertPixelValue(35, ri, 65, 95);

            // at the edges values goes down to zero
            assertPixelValue(0, ri, 0, 0);
            assertPixelValue(0, ri, 159, 0);
            assertPixelValue(0, ri, 159, 159);
            assertPixelValue(0, ri, 0, 159);

            coverage.dispose(true);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testMin() throws Exception {
        // Unioning the images should result in the minimum value of each pixel, which would look
        // like this, basically tassellated at the overalapping image borders, where the zeroes
        // at the border of one image are replaced by the value of the other image due to the NoData
        // @@@%%%%%#######@@@%%%%@@@#######%%%%%@@@
        // @@%%%#####*****@@%%%%%%%@*****#####%%%%@
        // %%%###*********%%%####%%%*********###%%%
        // %%###***+++++++%%###*###%+++++++****###%
        // %##***+#@@@%%%%%########%%%%%@@@#+***##%
        // ###**++#@%%%%#####****######%%%@#++***##
        // ##***++*%%%###*##******##*####%%*+++**##
        // ##***++*%###***##******##***###%*++***##
        // @@@@%%%%%######@@@@%%@@@@######%%%%%@@@@
        // @@%%%##########@@%%%%%%@@##########%%%@@
        // %%%#######*****%%%####%%%*****#######%%%
        // %%%#######*****%%%####%%%*****###*###%%%
        // @@%%%######****@@%%%%%%%@#***######%%%%@
        // @@@@%%%%%######@@@@%%%@@@#######%%%%%@@@
        // ##***++*%###***##******##***###%*++***##
        // ##***++*%%####*##******##**###%%*+++**##
        // ###**++#@%%%######****######%%%@#++***##
        // %##***+#@@@%%%%%#########%%%%%@@#+***###
        // %%##****+++++++%%##**###%++++++++***###%
        // %%%###*****+++*%%%#####%%*+++*****####%%
        // @%%%#####******@%%%##%%%@******#####%%%@
        // @@@%%%%########@@@%%%%@@@########%%%%@@@

        ImageMosaicReader reader = new ImageMosaicReader(mergeExt);
        try {
            final ParameterValue<String> mergeBehavior = ImageMosaicFormat.MERGE_BEHAVIOR.createValue();
            mergeBehavior.setValue(MergeBehavior.MIN.toString());
            GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {mergeBehavior});
            assertNotNull(coverage);

            // extract underlying raster
            RenderedImage ri = coverage.getRenderedImage();
            assertEquals(160, ri.getWidth());
            assertEquals(160, ri.getHeight());
            SampleModel sm = ri.getSampleModel();
            assertEquals(1, sm.getNumBands());
            assertEquals(DataBuffer.TYPE_BYTE, sm.getDataType());

            // the center of the image is the top value amonst the minimums
            assertPixelValue(20, ri, 80, 80);

            // at the edge of the center square the values are close to zero
            assertPixelValue(9, ri, 80, 99);
            assertPixelValue(9, ri, 80, 61);
            assertPixelValue(9, ri, 99, 80);
            assertPixelValue(9, ri, 61, 80);

            coverage.dispose(true);
        } finally {
            reader.dispose();
        }
    }

    private void assertPixelValue(int value, RenderedImage ri, int x, int y) {
        Raster data = ri.getData();
        int[] pixel = new int[1];
        data.getPixel(x, y, pixel);

        assertEquals(value, pixel[0]);
    }
}
