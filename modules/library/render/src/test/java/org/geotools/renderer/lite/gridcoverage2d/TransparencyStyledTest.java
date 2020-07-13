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
package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ExtremaDescriptor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.imagemosaic.ImageMosaicFormatFactory;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * A Testing class involving footprint and transparency settings together with SLD, making sure that
 * transparency is preserved.
 */
public class TransparencyStyledTest {

    private static final StyleFactory SF =
            CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

    private GridCoverage2D readCoverage(
            File mosaicDirectory, FootprintBehavior fp, Color transparentColor)
            throws NoSuchAuthorityCodeException, FactoryException, IOException {

        ImageMosaicReader reader =
                (ImageMosaicReader)
                        new ImageMosaicFormatFactory().createFormat().getReader(mosaicDirectory);

        ParameterValue<String> footprintBehaviorParam =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintBehaviorParam.setValue(fp.name());

        ParameterValue<Color> inputTransparentColor =
                AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
        inputTransparentColor.setValue(transparentColor);

        GeneralParameterValue[] readParams =
                new GeneralParameterValue[] {footprintBehaviorParam, inputTransparentColor};
        GridCoverage2D coverage = reader.read(readParams);

        reader.dispose();
        assertNotNull(coverage);
        return coverage;
    }

    /** Dispose the provided coverage for good. */
    private void disposeCoverage(GridCoverage2D coverage) {
        if (coverage == null) {
            return;
        }
        final RenderedImage im = coverage.getRenderedImage();
        ImageUtilities.disposePlanarImageChain(PlanarImage.wrapRenderedImage(im));
        coverage.dispose(true);
    }

    @AfterClass
    public static void close() {
        System.clearProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        CRS.reset("all");
    }

    @BeforeClass
    public static void init() {

        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
    }

    /**
     * Test transparency is preserved when applying an RGB ChannelSelect and ContrastEnhancement
     * style to an imageMosaic with Transparent Footprint setting.
     */
    @Test
    public void testTransparentFootprintWithContrastEnhancementInChannelSelect()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        File mosaicDirectory = prepareDirectory("footprintCECS");
        GridCoverage2D gc = readCoverage(mosaicDirectory, FootprintBehavior.Transparent, null);

        RenderedImage ri = gc.getRenderedImage();
        RenderedOp extrema = ExtremaDescriptor.create(ri, null, 1, 1, false, 1, null);
        double[] minimum = (double[]) extrema.getProperty("minimum");
        double[] maximum = (double[]) extrema.getProperty("maximum");

        // read values, not stretched, alpha is present
        assertArrayEquals(new double[] {0, 0, 0, 0}, minimum, 1E-6);
        assertArrayEquals(new double[] {54, 54, 54, 255}, maximum, 1E-6);

        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "ce_cs.sld");
        ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a CS+CE SLD in place
        assertHasAlpha(ri);

        extrema = ExtremaDescriptor.create(ri, null, 1, 1, false, 1, null);
        minimum = (double[]) extrema.getProperty("minimum");
        maximum = (double[]) extrema.getProperty("maximum");

        // values are stretched
        assertArrayEquals(new double[] {0, 0, 0, 0}, minimum, 1E-6);
        assertArrayEquals(new double[] {255, 255, 255, 255}, maximum, 1E-6);

        disposeCoverage(output);
        ImageUtilities.disposePlanarImageChain(extrema);
    }

    /**
     * Test transparency is preserved when applying an RGB ChannelSelect and ContrastEnhancement
     * style to an imageMosaic with transparent color being set
     */
    @Test
    public void testTransparentColorWithContrastEnhancementInChannelSelect()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        File mosaicDirectory = prepareDirectory("transparentCECS");
        GridCoverage2D gc = readCoverage(mosaicDirectory, FootprintBehavior.None, Color.BLACK);

        RenderedImage ri = gc.getRenderedImage();
        RenderedOp extrema = ExtremaDescriptor.create(ri, null, 1, 1, false, 1, null);
        double[] minimum = (double[]) extrema.getProperty("minimum");
        double[] maximum = (double[]) extrema.getProperty("maximum");

        // read values, not stretched, alpha is present
        assertArrayEquals(new double[] {0, 0, 0, 0}, minimum, 1E-6);
        assertArrayEquals(new double[] {54, 54, 54, 255}, maximum, 1E-6);

        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "ce_cs.sld");
        ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a CS+CE SLD in place
        assertHasAlpha(ri);

        extrema = ExtremaDescriptor.create(ri, null, 1, 1, false, 1, null);
        minimum = (double[]) extrema.getProperty("minimum");
        maximum = (double[]) extrema.getProperty("maximum");

        // values are stretched
        assertArrayEquals(new double[] {0, 0, 0, 0}, minimum, 1E-6);
        assertArrayEquals(new double[] {255, 255, 255, 255}, maximum, 1E-6);

        disposeCoverage(output);
        ImageUtilities.disposePlanarImageChain(extrema);
    }

    /**
     * Test transparency is preserved when applying an RGB ChannelSelect and ContrastEnhancement
     * style to an imageMosaic with transparent color being set
     */
    @Test
    public void testRGBAWithContrastEnhancementInChannelSelect()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        GeoTiffReader reader = new GeoTiffReader(TestData.file(this, "map.tif"));
        GridCoverage2D gc = reader.read(null);

        RenderedImage ri = gc.getRenderedImage();
        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "ce_cs.sld");
        ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a CS+CE SLD in place
        assertHasAlpha(ri);

        disposeCoverage(output);
    }

    /**
     * Test transparency is preserved when applying a ChannelSelect style to an imageMosaic with
     * Transparent Footprint setting.
     */
    @Test
    public void testTransparentFootprintWithChannelSelectRGB()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        File mosaicDirectory = prepareDirectory("footprintCS");
        GridCoverage2D gc = readCoverage(mosaicDirectory, FootprintBehavior.Transparent, null);
        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "channelselect.sld");
        RenderedImage ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a ChannelSelect SLD in place
        assertHasAlpha(ri);
        disposeCoverage((GridCoverage2D) output);
    }

    /**
     * Test transparency is preserved when applying a ChannelSelect style to an imageMosaic with
     * Transparent color being set.
     */
    @Test
    public void testTransparentColorWithChannelSelectRGB()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        File mosaicDirectory = prepareDirectory("transparentCS");
        GridCoverage2D gc = readCoverage(mosaicDirectory, FootprintBehavior.None, Color.BLACK);
        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "channelselect.sld");
        RenderedImage ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a ChannelSelect SLD in place
        assertHasAlpha(ri);
        disposeCoverage((GridCoverage2D) output);
    }

    /**
     * Test transparency is preserved when applying a ChannelSelect and Colormap style to an
     * imageMosaic with Transparent Footprint setting.
     */
    @Test
    public void testTransparentFootprintWithChannelSelectColormap()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        File mosaicDirectory = prepareDirectory("footprintCSCM");
        GridCoverage2D gc = readCoverage(mosaicDirectory, FootprintBehavior.Transparent, null);

        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "graychannelcolormap.sld");
        RenderedImage ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a ChannelSelect SLD in place
        assertHasAlpha(ri);
        disposeCoverage((GridCoverage2D) output);
    }

    /**
     * Test transparency is preserved when applying a ChannelSelect (Gray) style to an imageMosaic
     * with Transparent Footprint setting.
     */
    @Test
    public void testTransparentFootprintWithChannelSelectGray()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        File mosaicDirectory = prepareDirectory("footprintCSGray");
        GridCoverage2D gc = readCoverage(mosaicDirectory, FootprintBehavior.Transparent, null);

        GridCoverage2D output = (GridCoverage2D) symbolizeRaster(gc, "graychannel.sld");
        RenderedImage ri = output.getRenderedImage();

        // Assert the alpha band has been preserved, even with a ChannelSelect SLD in place
        ColorModel cm = ri.getColorModel();
        assertTrue(cm.hasAlpha());
        assertEquals(2, cm.getNumComponents());

        // Make sure the topleft pixel is transparent
        Raster rasterPixel = ri.getData(new Rectangle(0, 0, 1, 1));
        assertEquals(0, rasterPixel.getSample(0, 0, 1));

        disposeCoverage((GridCoverage2D) output);
    }

    private File prepareDirectory(String subDirectory) throws IOException {
        File source = TestData.file(this, "masked3");
        File testDataDir = TestData.file(this, ".");
        File mosaicDirectory = new File(testDataDir, subDirectory);
        if (mosaicDirectory.exists()) {
            FileUtils.deleteDirectory(mosaicDirectory);
        }
        FileUtils.copyDirectory(source, mosaicDirectory);
        // remove all sld related files
        for (File file : FileUtils.listFiles(mosaicDirectory, new SuffixFileFilter(".sld"), null)) {
            assertTrue(file.delete());
        }
        return mosaicDirectory;
    }

    private static RasterSymbolizer extractRasterSymbolizer(StyledLayerDescriptor sld) {
        final UserLayer nl = (UserLayer) sld.getStyledLayers()[0];
        final Style style = nl.getUserStyles()[0];
        final FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        final Rule rule = fts.rules().get(0);
        final RasterSymbolizer rs_1 = (RasterSymbolizer) rule.symbolizers().get(0);
        return rs_1;
    }

    private GridCoverage symbolizeRaster(GridCoverage2D gc, String style) throws IOException {
        java.net.URL styleUrl = TestData.url(this, "masked3/" + style);
        SLDParser stylereader = new SLDParser(SF, styleUrl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        RasterSymbolizerHelper visitor = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        visitor.visit(rs);
        return visitor.getOutput();
    }

    private void assertHasAlpha(RenderedImage ri) {
        ColorModel cm = ri.getColorModel();
        assertTrue(cm.hasAlpha());
        int numComponents = cm.getNumComponents();
        assertEquals(4, numComponents);

        // Make sure the topleft pixel is transparent
        Raster raster = ri.getTile(0, 0);
        if (cm instanceof ComponentColorModel) {
            // Get the value of the alpha component
            assertEquals(0, raster.getSample(0, 0, numComponents - 1));
        } else if (cm instanceof IndexColorModel) {
            int transparentPixel = ((IndexColorModel) cm).getTransparentPixel();
            assertEquals(transparentPixel, raster.getSample(0, 0, 0));
        }
    }
}
