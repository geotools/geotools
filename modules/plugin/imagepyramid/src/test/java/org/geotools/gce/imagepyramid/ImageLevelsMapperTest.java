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
package org.geotools.gce.imagepyramid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralBounds;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.Test;

/** Test the resolutionLevel-to-ImageMosaicReader mapping machinery. */
public class ImageLevelsMapperTest extends AbstractPyramidTest {

    @Test
    public void multicoveragePyramidWithOverviews()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException,
                    InvalidParameterValueException, ParseException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "multipyramidwithoverviews");
        File mosaicFolder = URLs.urlToFile(testFile);
        assertNotNull(testFile);
        File[] pyramidLevels = mosaicFolder.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        for (File pyramidLevel : pyramidLevels) {
            cleanFiles(pyramidLevel);
        }
        cleanFiles(mosaicFolder);

        //
        // Get the reader
        //
        final ImagePyramidReader reader = new ImagePyramidReader(testFile);

        assertNotNull(reader);
        assertEquals(2, reader.getGridCoverageCount());

        String[] coverageNames = reader.getGridCoverageNames();
        Arrays.sort(coverageNames);
        assertEquals("gray", coverageNames[0]);
        assertEquals("rgb", coverageNames[1]);

        //
        // Get the coverage
        //
        GridCoverage2D coverage = reader.read(coverageNames[0]);
        assertNotNull(coverage);
        RenderedImage renderedImage = coverage.getRenderedImage();
        int colorSpaceType = renderedImage.getColorModel().getColorSpace().getType();
        assertEquals(ColorSpace.TYPE_GRAY, colorSpaceType);
        GridEnvelope gridEnvelope = coverage.getGridGeometry().getGridRange();
        assertEquals(64, gridEnvelope.getSpan(0), DELTA);
        assertEquals(64, gridEnvelope.getSpan(1), DELTA);

        // Read a reduced view of the RGB coverage
        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralBounds envelope = reader.getOriginalEnvelope();
        GridEnvelope2D gridRange = new GridEnvelope2D(((GridEnvelope2D) reader.getOriginalGridRange()).getBounds());
        final Dimension dim = new Dimension();
        dim.setSize(gridRange.getSpan(0) / 16.0, gridRange.getSpan(1) / 16.0);
        Rectangle rasterArea = gridRange;
        rasterArea.setSize(dim);
        GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        coverage = reader.read(coverageNames[1], gg);
        assertNotNull(coverage);
        renderedImage = coverage.getRenderedImage();
        colorSpaceType = renderedImage.getColorModel().getColorSpace().getType();
        assertEquals(ColorSpace.TYPE_RGB, colorSpaceType);
        gridEnvelope = coverage.getGridGeometry().getGridRange();
        assertEquals(4, gridEnvelope.getSpan(0), DELTA);
        assertEquals(4, gridEnvelope.getSpan(1), DELTA);

        // test on expanded Envelope (Double the size of the envelope)
        final GeneralBounds doubleEnvelope = new GeneralBounds(
                new double[] {
                    envelope.getLowerCorner().getOrdinate(0),
                    envelope.getLowerCorner().getOrdinate(1)
                },
                new double[] {
                    envelope.getLowerCorner().getOrdinate(0) + envelope.getSpan(0) * 2,
                    envelope.getLowerCorner().getOrdinate(1) + envelope.getSpan(1) * 2
                });
        doubleEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());

        GridEnvelope doubleRange = reader.getOriginalGridRange();
        dim.setSize(doubleRange.getSpan(0) * 2, doubleRange.getSpan(1) * 2);
        rasterArea = (GridEnvelope2D) doubleRange;
        rasterArea.setSize(dim);
        range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(doubleRange, doubleEnvelope));

        coverage = reader.read(coverageNames[1], gg);

        assertNotNull(coverage);
        renderedImage = coverage.getRenderedImage();

        gridEnvelope = coverage.getGridGeometry().getGridRange();
        assertEquals(64, gridEnvelope.getSpan(0), DELTA);
        assertEquals(64, gridEnvelope.getSpan(1), DELTA);

        // Check the levels mapping
        Properties properties = new Properties();
        File propertyFile = new File(mosaicFolder, "multipyramidwithoverviews.properties");

        // Test image levels mapping on the newly created pyramid properties file
        ImageLevelsMapper mapper = null;
        final double baseRes = 0.4287193d;
        try (FileInputStream fis = new FileInputStream(propertyFile)) {
            properties.load(fis);
            mapper = new ImageLevelsMapper(properties);
            assertEquals(5, mapper.getNumOverviews());

            String[] levelDirs = mapper.getLevelsDirs();
            assertNotNull(levelDirs);
            assertEquals(2, levelDirs.length);
            assertEquals("0", levelDirs[0]);
            assertEquals("1", levelDirs[1]);

            double[] highRes = mapper.getHighestResolution();
            assertNotNull(highRes);
            match(new double[] {baseRes, baseRes}, highRes);

            double[][] resolutions = mapper.getOverViewResolutions();
            assertNotNull(resolutions);
            match(new double[] {baseRes * 2, baseRes * 2}, resolutions[0]);
            match(new double[] {baseRes * 4, baseRes * 4}, resolutions[1]);
            match(new double[] {baseRes * 8, baseRes * 8}, resolutions[2]);
            match(new double[] {baseRes * 16, baseRes * 16}, resolutions[3]);
            match(new double[] {baseRes * 32, baseRes * 32}, resolutions[4]);

            assertEquals(0, mapper.getImageReaderIndex(0));
            assertEquals(0, mapper.getImageReaderIndex(1));
            assertEquals(1, mapper.getImageReaderIndex(2));
            assertEquals(1, mapper.getImageReaderIndex(3));
            assertEquals(1, mapper.getImageReaderIndex(4));
            assertEquals(1, mapper.getImageReaderIndex(5));

        } finally {
            mapper.dispose();
        }
    }

    private void match(double[] expected, double[] actual) {
        assertEquals(expected[0], actual[0], DELTA);
        assertEquals(expected[1], actual[1], DELTA);
    }
}
