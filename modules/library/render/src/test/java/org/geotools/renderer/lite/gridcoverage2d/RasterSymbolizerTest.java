/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.jaiext.range.RangeFactory;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROIShape;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.xml.transform.TransformerException;
import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.test.ImageAssert;
import org.geotools.image.util.ComponentColorModelJAI;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.styling.AbstractContrastMethodStrategy;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ChannelSelectionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.styling.ContrastMethodStrategy;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDParser;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.style.ContrastMethod;

/** @author Simone Giannecchini, GeoSolutions. */
public class RasterSymbolizerTest extends org.junit.Assert {

    private static final StyleFactory sf =
            CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

    private static final double DELTA = 1E-7d;

    /**
     * Creates a simple 500x500 {@link RenderedImage} for testing purposes.
     *
     * <p>Values are randomly set to the provided noDataValue.
     */
    public static RenderedImage getSynthetic(final double noDataValue) {
        final int width = 500;
        final int height = 500;
        final WritableRaster raster =
                RasterFactory.createBandedRaster(DataBuffer.TYPE_DOUBLE, width, height, 1, null);
        final Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (random.nextFloat() > 0.9) raster.setSample(x, y, 0, noDataValue);
                else raster.setSample(x, y, 0, (x + y));
            }
        }
        final ColorModel cm =
                new ComponentColorModelJAI(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_DOUBLE);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    @Test
    public void contrastEnhancementMethods() throws Exception {
        // the GridCoverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                envelope,
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "histogram.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh_SLD =
                new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh_SLD.visit(rs_1);

        testRasterSymbolizerHelper(rsh_SLD);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh_StyleBuilder =
                new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        ;
        // cntEnh.setType(type);
        // cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeGray.setChannelName("1");
        chTypeGray.setContrastEnhancement(cntEnh);
        chSel.setGrayChannel(chTypeGray);
        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
        rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_1);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #3: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Logarithmic}
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        surl = TestData.url(this, "logarithmic.sld");
        stylereader = new SLDParser(sf, surl);
        sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        rsh_SLD = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_2 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh_SLD.visit(rs_2);

        testRasterSymbolizerHelper(rsh_SLD);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #4: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Logarithmic}
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));

        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_2 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_2 = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_2 = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_2 = new ContrastEnhancementImpl();

        cntEnh_2.setMethod(ContrastMethod.LOGARITHMIC);
        // cntEnh.setType(type);
        // cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeGray_2.setChannelName("1");
        chTypeGray_2.setContrastEnhancement(cntEnh_2);
        chSel_2.setGrayChannel(chTypeGray_2);
        rsb_2.setChannelSelection(chSel_2);
        rsb_2.setOpacity(sldBuilder.literalExpression(1.0));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_2);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #5: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Hyperbolic}
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        surl = TestData.url(this, "exponential.sld");
        stylereader = new SLDParser(sf, surl);
        sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        rsh_SLD = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_3 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh_SLD.visit(rs_3);

        testRasterSymbolizerHelper(rsh_SLD);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #6: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Hyperbolic}
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_3 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_3 = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_3 = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_3 = new ContrastEnhancementImpl();

        cntEnh_3.setMethod(ContrastMethod.EXPONENTIAL);

        // cntEnh.setType(type);
        // cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeGray_3.setChannelName("1");
        chTypeGray_3.setContrastEnhancement(cntEnh_3);
        chSel_3.setGrayChannel(chTypeGray_3);
        rsb_3.setChannelSelection(chSel_3);
        rsb_3.setOpacity(sldBuilder.literalExpression(1.0));
        rsb_3.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_3);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #7: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-StretchMinMax} with BYTE
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        surl = TestData.url(this, "normalize-stretch.sld");
        stylereader = new SLDParser(sf, surl);
        sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        rsh_SLD = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_4 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh_SLD.visit(rs_4);

        GridCoverage2D output = (GridCoverage2D) rsh_SLD.getOutput();
        ImageWorker worker = new ImageWorker(output.getRenderedImage());
        double min[] = worker.getMinimums();
        double max[] = worker.getMaximums();

        // Stretch to Minimum Maximum does a linear stretch to
        // Byte data range [0 - 255]
        assertEquals(0d, min[0], DELTA);
        assertEquals(255d, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_SLD);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #8: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-StretchMinMax} with USHORT
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "test_ushort.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_4 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_4 = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_4 = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_4 = new ContrastEnhancementImpl();
        final ContrastMethodStrategy method_4 = new NormalizeContrastMethodStrategy();
        method_4.addOption(
                "algorithm",
                sldBuilder.literalExpression(
                        ContrastEnhancementType.NORMALIZE_STRETCH_TO_MINMAX_NAME));
        method_4.addOption("minValue", sldBuilder.literalExpression(10));
        method_4.addOption("maxValue", sldBuilder.literalExpression(50));
        cntEnh_4.setMethod(method_4);

        chTypeGray_4.setChannelName("1");
        chTypeGray_4.setContrastEnhancement(cntEnh_4);
        chSel_4.setGrayChannel(chTypeGray_4);
        rsb_4.setChannelSelection(chSel_4);

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_4);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE,
                output.getRenderedImage().getSampleModel().getDataType()); // ok we went to byte
        worker = new ImageWorker(output.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();

        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(0d, min[0], DELTA);
        assertEquals(255d, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #8a: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-StretchMinMax} with Float
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "Float",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "smalldem.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_4f = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_4f = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_4f = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_4f = new ContrastEnhancementImpl();
        final ContrastMethodStrategy method_4f = new NormalizeContrastMethodStrategy();
        method_4f.addOption(
                "algorithm",
                sldBuilder.literalExpression(
                        ContrastEnhancementType.NORMALIZE_STRETCH_TO_MINMAX_NAME));
        method_4f.addOption("minValue", sldBuilder.literalExpression(10));
        method_4f.addOption("maxValue", sldBuilder.literalExpression(1200));
        cntEnh_4f.setMethod(method_4);

        chTypeGray_4f.setChannelName("1");
        chTypeGray_4f.setContrastEnhancement(cntEnh_4f);
        chSel_4f.setGrayChannel(chTypeGray_4f);
        rsb_4f.setChannelSelection(chSel_4f);

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_4f);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE,
                output.getRenderedImage().getSampleModel().getDataType()); // ok we went to byte
        worker = new ImageWorker(output.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();
        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(0d, min[0], DELTA);
        assertEquals(255d, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #9: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipMinMax} BYTE
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        surl = TestData.url(this, "normalize-clip.sld");
        stylereader = new SLDParser(sf, surl);
        sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        rsh_SLD = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_5 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh_SLD.visit(rs_5);

        output = (GridCoverage2D) rsh_SLD.getOutput();
        worker = new ImageWorker(output.getRenderedImage());
        worker.setNoData(RangeFactory.create(0, 0));
        min = worker.getMinimums();
        max = worker.getMaximums();

        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(10d, min[0], DELTA);
        assertEquals(100d, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_SLD);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #10: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipMinMax} USHORT
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_5 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_5 = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_5 = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_5 = new ContrastEnhancementImpl();

        final ContrastMethodStrategy method_5 = new NormalizeContrastMethodStrategy();

        method_5.addOption(
                "algorithm",
                sldBuilder.literalExpression(
                        ContrastEnhancementType.NORMALIZE_CLIP_TO_MINMAX_NAME));
        method_5.addOption("minValue", sldBuilder.literalExpression(50));
        method_5.addOption("maxValue", sldBuilder.literalExpression(200));
        cntEnh_5.setMethod(method_5);

        chTypeGray_5.setChannelName("1");
        chTypeGray_5.setContrastEnhancement(cntEnh_5);
        chSel_5.setGrayChannel(chTypeGray_5);
        rsb_5.setChannelSelection(chSel_5);

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_5);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        worker = new ImageWorker(output.getRenderedImage());
        worker.setNoData(RangeFactory.create(0, 0));
        min = worker.getMinimums();
        max = worker.getMaximums();

        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(50, min[0], DELTA);
        assertEquals(200, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #10a: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipMinMax} FLOAT in Byte range
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "smalldem.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_5f = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_5f = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_5f = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_5f = new ContrastEnhancementImpl();

        final ContrastMethodStrategy method_5f = new NormalizeContrastMethodStrategy();
        method_5f.addOption(
                "algorithm",
                sldBuilder.literalExpression(
                        ContrastEnhancementType.NORMALIZE_CLIP_TO_MINMAX_NAME));
        method_5f.addOption("minValue", sldBuilder.literalExpression(50));
        method_5f.addOption("maxValue", sldBuilder.literalExpression(200));
        cntEnh_5f.setMethod(method_5f);

        chTypeGray_5f.setChannelName("1");
        chTypeGray_5f.setContrastEnhancement(cntEnh_5f);
        chSel_5f.setGrayChannel(chTypeGray_5f);
        rsb_5f.setChannelSelection(chSel_5f);

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_5f);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE, output.getRenderedImage().getSampleModel().getDataType());
        // at the end the rastersymb does a rescale to byte for simplicity that tries to bring back
        // the dynamic to 8 Bits

        worker = new ImageWorker(output.getRenderedImage());
        worker.setNoData(RangeFactory.create(0, 0));
        min = worker.getMinimums();
        max = worker.getMaximums();
        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(50, min[0], DELTA);
        assertEquals(200, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #10b: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipMinMax} FLOAT outside Byte range
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "smalldem.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_5g = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_5g = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_5g = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_5g = new ContrastEnhancementImpl();

        final ContrastMethodStrategy method_5g = new NormalizeContrastMethodStrategy();
        method_5g.addOption(
                "algorithm",
                sldBuilder.literalExpression(
                        ContrastEnhancementType.NORMALIZE_CLIP_TO_MINMAX_NAME));
        method_5g.addOption("minValue", sldBuilder.literalExpression(50));
        method_5g.addOption("maxValue", sldBuilder.literalExpression(1200));
        cntEnh_5g.setMethod(method_5g);

        chTypeGray_5g.setChannelName("1");
        chTypeGray_5g.setContrastEnhancement(cntEnh_5g);
        chSel_5g.setGrayChannel(chTypeGray_5g);
        rsb_5g.setChannelSelection(chSel_5g);

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_5g);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE, output.getRenderedImage().getSampleModel().getDataType());
        // at the end the rastersymb does a rescale to byte for simplicity that tries to bring back
        // the dynamic to 8 Bits

        worker = new ImageWorker(output.getRenderedImage());
        worker.setNoData(RangeFactory.create(0, 0));
        min = worker.getMinimums();
        max = worker.getMaximums();
        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(10, min[0], DELTA); // the RasterSymbolizerHelper introduce a stretch to BYTE
        assertEquals(255, max[0], DELTA); // the RasterSymbolizerHelper introduce a stretch to BYTE
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #10c: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipMinMax} USHORT outside byte
        // range
        //
        // ////////////////////////////////////////////////////////////////////
        RenderedImage source =
                new ImageWorker(new File(TestData.url(this, "small_4bands_UInt16.tif").toURI()))
                        .retainBands(new int[] {2})
                        .getRenderedImage();
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                source,
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {new GridSampleDimension("band")},
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_5c = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_5c = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_5c = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_5c = new ContrastEnhancementImpl();

        final ContrastMethodStrategy method_5c = new NormalizeContrastMethodStrategy();

        method_5c.addOption(
                "algorithm",
                sldBuilder.literalExpression(
                        ContrastEnhancementType.NORMALIZE_CLIP_TO_MINMAX_NAME));
        method_5c.addOption("minValue", sldBuilder.literalExpression(50));
        method_5c.addOption("maxValue", sldBuilder.literalExpression(500)); // ouside byte range
        cntEnh_5c.setMethod(method_5c);

        chTypeGray_5c.setChannelName("1");
        chTypeGray_5c.setContrastEnhancement(cntEnh_5c);
        chSel_5c.setGrayChannel(chTypeGray_5c);
        rsb_5c.setChannelSelection(chSel_5c);

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_5c);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE,
                output.getRenderedImage().getSampleModel().getDataType()); // not preserved
        worker = new ImageWorker(output.getRenderedImage());
        worker.setNoData(RangeFactory.create(0, 0));
        min = worker.getMinimums();
        max = worker.getMaximums();

        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(1, min[0], DELTA);
        assertEquals(255, max[0], DELTA); // preserved
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #11: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipZeroMax} byte
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "hs.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_6 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_6 = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_6 = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_6 = new ContrastEnhancementImpl();

        final AbstractContrastMethodStrategy method_6 = new NormalizeContrastMethodStrategy();
        method_6.setAlgorithm(
                sldBuilder.literalExpression(ContrastEnhancementType.NORMALIZE_CLIP_TO_ZERO_NAME));
        method_6.addOption("minValue", sldBuilder.literalExpression(50));
        method_6.addOption("maxValue", sldBuilder.literalExpression(100));
        cntEnh_6.setMethod(method_6);

        chTypeGray_6.setChannelName("1");
        chTypeGray_6.setContrastEnhancement(cntEnh_6);
        chSel_6.setGrayChannel(chTypeGray_6);
        rsb_6.setChannelSelection(chSel_6);
        rsb_6.setOpacity(sldBuilder.literalExpression(1.0));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_6);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        worker = new ImageWorker(output.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();

        assertEquals(0, min[0], DELTA);
        assertEquals(100, max[0], DELTA);
        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #12: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipZeroMax} USHORT outside byte
        // range
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "test_ushort.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);
        worker = new ImageWorker(gc.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();
        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_7 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_7 = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_7 = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_7 = new ContrastEnhancementImpl();
        final AbstractContrastMethodStrategy method_7 = new NormalizeContrastMethodStrategy();

        method_7.setAlgorithm(
                sldBuilder.literalExpression(ContrastEnhancementType.NORMALIZE_CLIP_TO_ZERO_NAME));
        method_7.addParameter("minValue", sldBuilder.literalExpression(50));
        method_7.addParameter("maxValue", sldBuilder.literalExpression(18000));
        cntEnh_7.setMethod(method_7);

        chTypeGray_7.setChannelName("1");
        chTypeGray_7.setContrastEnhancement(cntEnh_7);
        chSel_7.setGrayChannel(chTypeGray_7);
        rsb_7.setChannelSelection(chSel_7);
        rsb_7.setOpacity(sldBuilder.literalExpression(1.0));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_7);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE,
                output.getRenderedImage().getSampleModel().getDataType()); // not preserved
        worker = new ImageWorker(output.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();
        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(0, min[0], DELTA);
        assertEquals(255, max[0], DELTA); // final rescale to bytes

        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #12a: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipZeroMax}  Float within Byte
        // range
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "smalldem.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);
        worker = new ImageWorker(gc.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();
        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_7f = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_7f = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_7f = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_7f = new ContrastEnhancementImpl();
        final AbstractContrastMethodStrategy method_7f = new NormalizeContrastMethodStrategy();

        method_7f.setAlgorithm(
                sldBuilder.literalExpression(ContrastEnhancementType.NORMALIZE_CLIP_TO_ZERO_NAME));
        method_7f.addParameter("minValue", sldBuilder.literalExpression(50));
        method_7f.addParameter("maxValue", sldBuilder.literalExpression(200));
        cntEnh_7f.setMethod(method_7f);

        chTypeGray_7f.setChannelName("1");
        chTypeGray_7f.setContrastEnhancement(cntEnh_7f);
        chSel_7f.setGrayChannel(chTypeGray_7f);
        rsb_7f.setChannelSelection(chSel_7f);
        rsb_7f.setOpacity(sldBuilder.literalExpression(1.0));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_7f);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE, output.getRenderedImage().getSampleModel().getDataType());
        // at the end the rastersymb does a rescale to byte for simplicity that tries to bring back
        // the dynamic to 8 Bits

        worker = new ImageWorker(output.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();
        // Clip to Minimum Maximum does a Clamp by forcing
        // values outside the specified range to be clamped
        // to the range bounds
        assertEquals(0, min[0], DELTA);
        assertEquals(200, max[0], DELTA);

        testRasterSymbolizerHelper(rsh_StyleBuilder);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #12b: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Normalize-ClipZeroMax}  Float outside Byte
        // range
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "smalldem.tif").toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);
        worker = new ImageWorker(gc.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();
        // build the RasterSymbolizer
        sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_7g = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel_7g = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray_7g = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh_7g = new ContrastEnhancementImpl();
        final AbstractContrastMethodStrategy method_7g = new NormalizeContrastMethodStrategy();

        method_7g.setAlgorithm(
                sldBuilder.literalExpression(ContrastEnhancementType.NORMALIZE_CLIP_TO_ZERO_NAME));
        method_7g.addParameter("minValue", sldBuilder.literalExpression(50));
        method_7g.addParameter("maxValue", sldBuilder.literalExpression(1200));
        cntEnh_7g.setMethod(method_7g);

        chTypeGray_7g.setChannelName("1");
        chTypeGray_7g.setContrastEnhancement(cntEnh_7g);
        chSel_7g.setGrayChannel(chTypeGray_7g);
        rsb_7g.setChannelSelection(chSel_7g);
        rsb_7g.setOpacity(sldBuilder.literalExpression(1.0));

        // visit the RasterSymbolizer
        rsh_StyleBuilder.visit(rsb_7g);
        output = (GridCoverage2D) rsh_StyleBuilder.getOutput();
        assertEquals(
                DataBuffer.TYPE_BYTE, output.getRenderedImage().getSampleModel().getDataType());
        // at the end the rastersymb does a rescale to byte for simplicity that tries to bring back
        // the dynamic to 8 Bits
        worker = new ImageWorker(output.getRenderedImage());
        min = worker.getMinimums();
        max = worker.getMaximums();

        assertEquals(0, min[0], DELTA); // the RasterSymbolizerHelper introduce a stretch to BYTE
        assertEquals(255, max[0], DELTA); // the RasterSymbolizerHelper introduce a stretch to BYTE

        testRasterSymbolizerHelper(rsh_StyleBuilder);
    }

    @org.junit.Test
    public void bandFloat32_SLD()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Float32.tif")
                                                        .toURI())),
                                envelope,
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);
        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "1band_Float32_test1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //
        // ////////////////////////////////////////////////////////////////////
        // the GridCoverage
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Float32.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();
        cntEnh.setMethod(ContrastMethod.LOGARITHMIC);
        chTypeGray.setChannelName("1");
        chTypeGray.setContrastEnhancement(cntEnh);
        chSel.setGrayChannel(chTypeGray);
        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    @Test
    public void test1BandFloat32_ColorMap_SLD()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Float32.tif")
                                                        .toURI())),
                                envelope,
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //    - ColorMap
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "1band_Float32_test2.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final UserLayer nl = (UserLayer) sld.getStyledLayers()[0];
        final Style style = nl.getUserStyles()[0];
        final FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        final Rule rule = fts.rules().get(0);
        final RasterSymbolizer rs_1 = (RasterSymbolizer) rule.symbolizers().get(0);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //    - ColorMap
        //
        // ////////////////////////////////////////////////////////////////////
        // the GridCoverage
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Float32.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        ;
        // cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeGray.setChannelName("1");
        chTypeGray.setContrastEnhancement(cntEnh);

        chSel.setGrayChannel(chTypeGray);

        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));

        rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        final ColorMap cm =
                sldBuilder.createColorMap(
                        new String[] { // labels
                            "category", "category", "category"
                        },
                        new double[] { // quantities
                            0.1, 50.0, 200.0
                        },
                        new Color[] { // colors with alpha
                            new Color(255, 0, 0, 255),
                            new Color(0, 255, 0, 40),
                            new Color(0, 0, 255, 125)
                        },
                        ColorMap.TYPE_RAMP);

        rsb_1.setColorMap(cm);

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void bandsUInt16_SLD()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        final GridSampleDimension[] gsd = {
            new GridSampleDimension("test1BandByte_SLD1"),
        };
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(TestData.url(this, "test.tif").toURI())),
                                envelope,
                                gsd,
                                null,
                                null);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: RGB
        //    - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "4bands_UInt16_test1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);
        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: RGB
        //	  - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_4bands_UInt16.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeRed = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeBlue = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeGreen = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        ;
        // cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeRed.setChannelName("1");
        chTypeBlue.setChannelName("2");
        chTypeGreen.setChannelName("3");

        chSel.setRGBChannels(chTypeRed, chTypeBlue, chTypeGreen);

        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
        rsb_1.setContrastEnhancement(cntEnh);
        rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    @Test
    public void bandsUInt16_SLDROI()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        final GridSampleDimension[] gsd = {
            new GridSampleDimension("test1BandByte_SLD1"),
        };
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        RenderedOp image =
                JAI.create("ImageRead", new File(TestData.url(this, "test.tif").toURI()));

        Map properties = new HashMap();
        properties.put(
                "GC_ROI",
                new ROIShape(
                        new Rectangle(
                                image.getMinX() + 1,
                                image.getMinY() + 1,
                                image.getWidth() / 2,
                                image.getHeight() / 2)));

        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create("name", image, envelope, gsd, null, properties);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        // - Opacity: 1.0
        // - ChannelSelection: RGB
        // - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "4bands_UInt16_test1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);
        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [StyleBuilder]
        // - Opacity: 1.0
        // - ChannelSelection: RGB
        // - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        RenderedOp image2 =
                JAI.create(
                        "ImageRead",
                        new File(TestData.url(this, "small_4bands_UInt16.tif").toURI()));
        properties = new HashMap();
        properties.put(
                "GC_ROI",
                new ROIShape(
                        new Rectangle(
                                image2.getMinX() + 1,
                                image2.getMinY() + 1,
                                image2.getWidth() / 2,
                                image2.getHeight() / 2)));
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                image2,
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                null,
                                null,
                                properties);
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeRed = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeBlue = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeGreen = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        ;

        chTypeRed.setChannelName("1");
        chTypeBlue.setChannelName("2");
        chTypeGreen.setChannelName("3");

        chSel.setRGBChannels(chTypeRed, chTypeBlue, chTypeGreen);

        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
        rsb_1.setContrastEnhancement(cntEnh);
        rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    private static RasterSymbolizer extractRasterSymbolizer(StyledLayerDescriptor sld) {
        final UserLayer nl = (UserLayer) sld.getStyledLayers()[0];
        final Style style = nl.getUserStyles()[0];
        final FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        final Rule rule = fts.rules().get(0);
        final RasterSymbolizer rs_1 = (RasterSymbolizer) rule.symbolizers().get(0);
        return rs_1;
    }

    @org.junit.Test
    public void bandsByte_SLD()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        GridCoverage2D gc = read3BandsByteCoverage();

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: RGB
        //    - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "3bands_Byte_test1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: RGB
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_3bands_Byte.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeRed = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeBlue = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeGreen = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeRed.setChannelName("1");
        chTypeBlue.setChannelName("2");
        chTypeGreen.setChannelName("3");
        chSel.setRGBChannels(chTypeRed, chTypeBlue, chTypeGreen);
        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
        rsb_1.setContrastEnhancement(cntEnh);
        rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void greenSelection()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        GridCoverage2D gc = read3BandsByteCoverage();

        java.net.URL surl = TestData.url(this, "greenChannelSelection.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        final RenderedImage image = rsh.getOutput().getRenderedImage();
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/greenChannleSelection.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    public GridCoverage2D read3BandsByteCoverage()
            throws URISyntaxException, FileNotFoundException {
        // the GridCoverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        final GridSampleDimension[] gsd = {
            new GridSampleDimension("test1BandByte_SLD1"),
            new GridSampleDimension("test1BandByte_SLD2"),
            new GridSampleDimension("test1BandByte_SLD3")
        };
        return CoverageFactoryFinder.getGridCoverageFactory(null)
                .create(
                        "name",
                        JAI.create(
                                "ImageRead",
                                new File(TestData.url(this, "small_3bands_Byte.tif").toURI())),
                        envelope,
                        gsd,
                        null,
                        null);
    }

    @org.junit.Test
    public void bandsByte_ColorMap_SLD()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        GridCoverage2D gc = read3BandsByteCoverage();

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: RGB
        //    - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "3bands_Byte_test2.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: RGB
        //	  - Contrast Enh: Histogram
        //
        // ////////////////////////////////////////////////////////////////////
        // the GridCoverage
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_3bands_Byte.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));

        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        // cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));

        chTypeGray.setChannelName("1");
        chSel.setGrayChannel(chTypeGray);

        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
        rsb_1.setContrastEnhancement(cntEnh);
        rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

        final ColorMap cm =
                sldBuilder.createColorMap(
                        new String[] { // labels
                            "category", "category", "category"
                        },
                        new double[] { // quantities
                            0.1, 50.0, 200.0
                        },
                        new Color[] { // colors with alpha
                            new Color(255, 0, 0, 255),
                            new Color(0, 255, 0, 40),
                            new Color(0, 0, 255, 125)
                        },
                        ColorMap.TYPE_RAMP);

        rsb_1.setColorMap(cm);

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void BandByte_SLD()
            throws IOException, TransformerException, FactoryRegistryException,
                    IllegalArgumentException, URISyntaxException {
        // the GridCoverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Byte.tif")
                                                        .toURI())),
                                envelope,
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test1BandByte_SLD")
                                },
                                null,
                                null);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #1: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //
        // ////////////////////////////////////////////////////////////////////
        java.net.URL surl = TestData.url(this, "1band_Float32_test1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

        RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #2: [StyleBuilder]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //
        // ////////////////////////////////////////////////////////////////////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Byte.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);
        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
        final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

        // this will convert to byte!!!
        cntEnh.setMethod(ContrastMethod.HISTOGRAM);
        chTypeGray.setChannelName("1");
        chTypeGray.setContrastEnhancement(cntEnh);
        chSel.setGrayChannel(chTypeGray);
        rsb_1.setChannelSelection(chSel);

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);
        final RenderedImage im = ((GridCoverage2D) rsh.getOutput()).getRenderedImage();
        Assert.assertTrue(im.getSampleModel().getDataType() == 0);

        testRasterSymbolizerHelper(rsh);

        // ////////////////////////////////////////////////////////////////////
        //
        // Test #3: [SLD]
        //    - Opacity: 1.0
        //    - ChannelSelection: Gray {Contrast Enh: Histogram}
        //    - ColorMap
        //
        // ////////////////////////////////////////////////////////////////////
        // the GridCoverage
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create(
                                        "ImageRead",
                                        new File(
                                                TestData.url(this, "small_1band_Byte.tif")
                                                        .toURI())),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));

        surl = TestData.url(this, "1band_Float32_test2.sld");
        stylereader = new SLDParser(sf, surl);
        sld = stylereader.parseSLD();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        // build the RasterSymbolizer
        rs_1 = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs_1);

        testRasterSymbolizerHelper(rsh);
    }

    @Test
    public void colorMapInterpolation() throws Exception {
        ////
        //
        // Test using an SLD file
        //
        ////
        final URL sldURL = TestData.url(this, "colormap.sld");
        final SLDParser stylereader = new SLDParser(sf, sldURL);
        final StyledLayerDescriptor sld = stylereader.parseSLD();

        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                PlanarImage.wrapRenderedImage(getSynthetic(Double.NaN)),
                                envelope,
                                new GridSampleDimension[] {
                                    new GridSampleDimension(
                                            "sd",
                                            new Category[] {new Category("", Color.BLACK, 0)},
                                            null)
                                },
                                null,
                                null);

        RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // do the rendering and check the final color model
        GridToEnvelopeMapper gtoem = new GridToEnvelopeMapper();
        gtoem.setEnvelope(envelope);
        gtoem.setGridRange(new GridEnvelope2D(0, 0, 50, 50));
        gtoem.setPixelAnchor(PixelInCell.CELL_CORNER);
        final GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84,
                        ReferencedEnvelope.reference(envelope),
                        new Rectangle(0, 0, 50, 50),
                        gtoem.createAffineTransform().createInverse());

        // bilinear
        RenderedImage image =
                renderer.renderImage(gc, rs, new InterpolationBilinear(), null, 256, 256);
        assertNotNull(image);
        assertNotNull(PlanarImage.wrapRenderedImage(image));
        assertTrue(image.getColorModel() instanceof IndexColorModel);

        // nearest
        image = renderer.renderImage(gc, rs, new InterpolationNearest(), null, 256, 256);
        assertNotNull(image);
        assertNotNull(PlanarImage.wrapRenderedImage(image));
        assertTrue(image.getColorModel() instanceof IndexColorModel);
    }

    @org.junit.Test
    public void colorMap() throws IOException, TransformerException {
        // get a coverage
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                PlanarImage.wrapRenderedImage(getSynthetic(Double.NaN)),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension(
                                            "sd",
                                            new Category[] {new Category("", Color.BLACK, 0)},
                                            null)
                                },
                                null,
                                null);
        testColorMap(gc);
    }

    @org.junit.Test
    public void colorMapGrayAlpha() throws IOException, TransformerException {
        final int width = 500;
        final int height = 500;
        final WritableRaster raster =
                RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 2, null);
        final Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (random.nextFloat() > 0.9) {
                    raster.setSample(x, y, 0, 0);
                } else {
                    raster.setSample(x, y, 0, (x + y));
                }
                raster.setSample(x, y, 1, 255);
            }
        }
        final ColorModel cm =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        true,
                        false,
                        Transparency.TRANSLUCENT,
                        DataBuffer.TYPE_BYTE);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);

        // get a coverage
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                PlanarImage.wrapRenderedImage(image),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension(
                                            "sd",
                                            new Category[] {new Category("", Color.BLACK, 0)},
                                            null),
                                    new GridSampleDimension(
                                            "sd-alpha",
                                            new Category[] {new Category("", Color.BLACK, 0)},
                                            null)
                                },
                                null,
                                null);
        testColorMap(gc);
    }

    private void testColorMap(GridCoverage2D gc) throws IOException, TransformerException {

        ////
        //
        // Test using an SLD file
        //
        ////
        final URL sldURL = TestData.url(this, "colormap.sld");
        final SLDParser stylereader = new SLDParser(sf, sldURL);
        final StyledLayerDescriptor sld = stylereader.parseSLD();

        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
        RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs);
        IndexColorModel icm1 =
                (IndexColorModel)
                        ((GridCoverage2D) rsh.getOutput()).getRenderedImage().getColorModel();
        testRasterSymbolizerHelper(rsh);

        ////
        //
        // Test using StyleBuilder
        //
        ////
        // get a coverage
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                PlanarImage.wrapRenderedImage(getSynthetic(Double.NaN)),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}),
                                new GridSampleDimension[] {
                                    new GridSampleDimension(
                                            "sd",
                                            new Category[] {new Category("", Color.BLACK, 0)},
                                            null)
                                },
                                null,
                                null);

        // build the RasterSymbolizer
        StyleBuilder sldBuilder = new StyleBuilder();
        rsh = new RasterSymbolizerHelper(gc, null);
        rs = sldBuilder.createRasterSymbolizer();
        final ColorMap cm =
                sldBuilder.createColorMap(
                        new String[] { // labels
                            "category0", "category1", "category2"
                        },
                        new double[] { // quantities
                            100.0, 500.0, 900.0
                        },
                        new Color[] { // colors
                            new Color(255, 0, 0, 255),
                            new Color(0, 255, 0, (int) (255 * 0.8)),
                            new Color(0, 0, 255, (int) (255 * 0.2))
                        },
                        ColorMap.TYPE_RAMP);
        cm.setExtendedColors(true);
        rs.setColorMap(cm);

        // visit the RasterSymbolizer
        rsh.visit(rs);
        IndexColorModel icm2 =
                (IndexColorModel)
                        ((GridCoverage2D) rsh.getOutput()).getRenderedImage().getColorModel();
        testRasterSymbolizerHelper(rsh);
        // check that the two color models are equals!
        Assert.assertTrue(icm1.equals(icm2));
    }

    @org.junit.Test
    public void rgb() throws IOException, TransformerException {
        java.net.URL surl = TestData.url(this, "testrgb.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        final GridSampleDimension[] gsd = {
            new GridSampleDimension("test1BandByte_SLD1"),
            new GridSampleDimension("test1BandByte_SLD2"),
            new GridSampleDimension("test1BandByte_SLD3"),
        };
        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        final GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "bahamas_hires.jpg")),
                                envelope,
                                gsd,
                                null,
                                null);

        // build the RasterSymbolizer
        final SubchainStyleVisitorCoverageProcessingAdapter rsh =
                new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs);

        testRasterSymbolizerHelper(rsh);
    }

    /**
     * Found out that we were creating a classification transform that was incorrect since the gaps
     * index was set to the max index + 1 in the colormap, which resulted in {@link
     * IndexOutOfBoundsException}.
     */
    @Test
    public void testGapsColor() throws IOException {
        java.net.URL surl = TestData.url(this, "Topsoil-Organic-Carbon.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        final GridSampleDimension[] gsd = {new GridSampleDimension("test1BandByte_SLD1")};
        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        final GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "toc.tif")),
                                envelope,
                                gsd,
                                null,
                                null);

        // build the RasterSymbolizer
        final SubchainStyleVisitorCoverageProcessingAdapter rsh =
                new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // visit the RasterSymbolizer
        rsh.visit(rs);

        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void dem() throws IOException, TransformerException {

        ////
        //
        // Test using an SLD file
        //
        ////
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        java.net.URL surl = TestData.url(this, "raster_dem.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // get a coverage
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "smalldem.tif")),
                                envelope,
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);
        rsh.visit(rs);
        testRasterSymbolizerHelper(rsh);

        ////
        //
        // Test using stylebuilder
        //
        ////
        gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "smalldem.tif")),
                                new GeneralEnvelope(
                                        new double[] {-90, -180}, new double[] {90, 180}));
        StyleBuilder sldBuilder = new StyleBuilder();
        // the RasterSymbolizer Helper
        rsh = new RasterSymbolizerHelper(gc, null);

        final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
        chTypeGray.setChannelName("1");
        chSel.setGrayChannel(chTypeGray);
        rsb_1.setChannelSelection(chSel);
        rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
        final ColorMap cm =
                sldBuilder.createColorMap(
                        new String[] { // labels
                            "category", "category", "category"
                        },
                        new double[] { // quantities
                            0.1, 50.0, 200.0
                        },
                        new Color[] { // colors with alpha
                            new Color(255, 0, 0, 255),
                            new Color(0, 255, 0, 40),
                            new Color(0, 0, 255, 125)
                        },
                        ColorMap.TYPE_RAMP);

        rsb_1.setColorMap(cm);

        // visit the RasterSymbolizer
        rsh.visit(rsb_1);

        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void demEmptyRange() throws IOException, TransformerException {

        // An SLD file where two entries have the same value
        ////
        java.net.URL surl = TestData.url(this, "dem_emptyRange.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "smalldem.tif")),
                                envelope,
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // used to blow up here with an exception
        rsh.visit(rs);
        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void demColorJump() throws IOException, TransformerException {

        // An SLD file where two entries have the same value
        ////
        java.net.URL surl = TestData.url(this, "dem_colorJump.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "smalldem.tif")),
                                envelope,
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);

        // used to blow up here with an exception
        rsh.visit(rs);
        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void landsat() throws IOException, TransformerException {
        java.net.URL surl = TestData.url(this, "landsat.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        final GridSampleDimension[] gsd = {
            new GridSampleDimension("test1BandByte_SLD1"),
            new GridSampleDimension("test1BandByte_SLD2"),
            new GridSampleDimension("test1BandByte_SLD3"),
            new GridSampleDimension("test1BandByte_SLD4"),
            new GridSampleDimension("test1BandByte_SLD5"),
            new GridSampleDimension("test1BandByte_SLD6"),
            new GridSampleDimension("test1BandByte_SLD")
        };
        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        final GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "landsat.tiff")),
                                envelope,
                                gsd,
                                null,
                                null);
        final SubchainStyleVisitorCoverageProcessingAdapter rsh =
                new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);
        rsh.visit(rs);

        final RenderedImage ri = ((GridCoverage2D) rsh.getOutput()).getRenderedImage();
        Assert.assertTrue(ri.getColorModel() instanceof ComponentColorModel);
        Assert.assertTrue(ri.getColorModel().getNumComponents() == 3);
        testRasterSymbolizerHelper(rsh);
    }

    private static void testRasterSymbolizerHelper(
            final SubchainStyleVisitorCoverageProcessingAdapter rsh) {
        if (TestData.isInteractiveTest()) {
            ImageIOUtilities.visualize(
                    ((GridCoverage2D) rsh.getOutput()).getRenderedImage(),
                    rsh.getName().toString());

        } else {
            PlanarImage.wrapRenderedImage(((GridCoverage2D) rsh.getOutput()).getRenderedImage())
                    .getTiles();
            rsh.dispose(new Random().nextBoolean() ? true : false);
        }
    }

    @Test
    public void twoColorsTest() throws IOException {
        java.net.URL surl = TestData.url(this, "raster_discretecolors.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // get a coverage
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "smalldem.tif")),
                                envelope,
                                new GridSampleDimension[] {new GridSampleDimension("dem")},
                                null,
                                null);
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);
        rsh.visit(rs);

        // test
        final RenderedImage ri = ((GridCoverage2D) rsh.getOutput()).getRenderedImage();
        Assert.assertTrue(ri.getColorModel() instanceof IndexColorModel);
        final IndexColorModel icm = (IndexColorModel) ri.getColorModel();
        final int mapSize = icm.getMapSize();
        Assert.assertEquals(3, mapSize);

        // get colors
        final int rgb[] = new int[mapSize];
        rgb[0] = 0xFFFFFF & icm.getRGB(0);
        rgb[1] = 0xFFFFFF & icm.getRGB(1);
        rgb[2] = 0xFFFFFF & icm.getRGB(2);

        int found = 0;
        for (int i = 0; i < mapSize; i++) {
            switch (rgb[i]) {
                case 0x008000:
                case 0x663333:
                case 0:
                    found++;
                    break;
                default:
                    throw new IllegalStateException("Found unexpected colors:" + rgb[i]);
            }
        }

        testRasterSymbolizerHelper(rsh);
    }

    @org.junit.Test
    public void testUshort() throws IOException {
        ////
        //
        // Test using an SLD file
        //
        ////
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {-180, -90}, new double[] {180, 90});
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        java.net.URL surl = TestData.url(this, "raster.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // get a coverage
        GridCoverage2D gc =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "name",
                                JAI.create("ImageRead", TestData.file(this, "test_ushort.tif")),
                                envelope,
                                new GridSampleDimension[] {
                                    new GridSampleDimension("test_dimension")
                                },
                                null,
                                null);
        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
        final RasterSymbolizer rs = extractRasterSymbolizer(sld);
        rsh.visit(rs);
        // Check if the final image has been rescaled to bytes
        RenderedImage outputImage = ((GridCoverage2D) rsh.getOutput()).getRenderedImage();
        int dataType = outputImage.getSampleModel().getDataType();
        assertEquals(DataBuffer.TYPE_BYTE, dataType);
    }

    @Test
    public void contrastEnhancementInChannelSelection() throws IOException {

        StyleBuilder sldBuilder = new StyleBuilder();
        RasterSymbolizer symbolizer = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeRed = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeBlue = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeGreen = new SelectedChannelTypeImpl();

        SelectedChannelType[] channels =
                new SelectedChannelType[] {chTypeRed, chTypeGreen, chTypeBlue};

        // Assign a different contrast method for each channel
        // by offsetting min and max of 20 on each channel
        // and assigning channels number with increments of 2
        int min = 10;
        int max = 100;
        for (int i = 0; i < 3; i++) {
            final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();
            final ContrastMethodStrategy method = new NormalizeContrastMethodStrategy();
            method.addOption(
                    "algorithm",
                    sldBuilder.literalExpression(
                            ContrastEnhancementType.NORMALIZE_STRETCH_TO_MINMAX_NAME));
            method.addOption("minValue", sldBuilder.literalExpression(min + (20 * i)));
            method.addOption("maxValue", sldBuilder.literalExpression(max + (20 * i)));
            cntEnh.setMethod(method);
            channels[i].setChannelName(Integer.toString((i * 2) + 1));
            channels[i].setContrastEnhancement(cntEnh);
        }
        chSel.setRGBChannels(chTypeRed, chTypeGreen, chTypeBlue);

        symbolizer.setChannelSelection(chSel);
        symbolizer.setOpacity(sldBuilder.literalExpression(1.0));
        symbolizer = GridCoverageRenderer.setupSymbolizerForBandsSelection(symbolizer);

        ChannelSelection cs = symbolizer.getChannelSelection();
        SelectedChannelType[] postBandSelectionChannels = cs.getRGBChannels();
        for (int i = 0; i < 3; i++) {
            SelectedChannelType postBandSelectionChannel = postBandSelectionChannels[i];
            // Before the fix, the following assertion would fail
            assertNotNull(postBandSelectionChannel);
            final ContrastEnhancement cntEnh = postBandSelectionChannel.getContrastEnhancement();
            ContrastMethod method = cntEnh.getMethod();

            // Assert channels number have been re-arranged
            assertTrue(
                    Integer.toString((i) + 1)
                            .equalsIgnoreCase(
                                    postBandSelectionChannel
                                            .getChannelName()
                                            .evaluate(null, String.class)));
            assertTrue(method.name().equalsIgnoreCase(ContrastMethod.NORMALIZE.name()));

            Map<String, Expression> options = cntEnh.getOptions();
            assertTrue(options.containsKey("algorithm"));
            assertTrue(options.containsKey("minValue"));
            assertTrue(options.containsKey("maxValue"));
            assertTrue(
                    options.get("algorithm")
                            .equals(
                                    sldBuilder.literalExpression(
                                            ContrastEnhancementType
                                                    .NORMALIZE_STRETCH_TO_MINMAX_NAME)));
            assertTrue(
                    options.get("minValue").equals(sldBuilder.literalExpression(min + (20 * i))));
            assertTrue(
                    options.get("maxValue").equals(sldBuilder.literalExpression(max + (20 * i))));
        }
    }
}
