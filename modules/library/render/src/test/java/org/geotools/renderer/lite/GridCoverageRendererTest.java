/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;
import javax.media.jai.ROI;
import javax.media.jai.TiledImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.geotools.TestData;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.Crop;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.test.ImageAssert;
import org.geotools.image.util.ImageUtilities;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.renderer.lite.gridcoverage2d.ContrastEnhancementType;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageReaderHelper;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageReaderHelperTest;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageRenderer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ChannelSelectionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.styling.ContrastMethodStrategy;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.util.URLs;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.filter.FilterFactory;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/** @author Simone Giannecchini */
public class GridCoverageRendererTest {

    private static final String AFRICA_EQUIDISTANT_CONIC_WKT =
            "PROJCS[\"Africa_Equidistant_Conic\","
                    + "GEOGCS[\"GCS_WGS_1984\","
                    + "DATUM[\"WGS_1984\","
                    + "SPHEROID[\"WGS_1984\",6378137,298.257223563]],"
                    + "PRIMEM[\"Greenwich\",0],"
                    + "UNIT[\"Degree\",0.017453292519943295]],"
                    + "PROJECTION[\"Equidistant_Conic\"],"
                    + "PARAMETER[\"False_Easting\",0],"
                    + "PARAMETER[\"False_Northing\",0],"
                    + "PARAMETER[\"Central_Meridian\",25],"
                    + "PARAMETER[\"Standard_Parallel_1\",20],"
                    + "PARAMETER[\"Standard_Parallel_2\",-23],"
                    + "PARAMETER[\"Latitude_Of_Origin\",0],"
                    + "UNIT[\"Meter\",1],"
                    + "AUTHORITY[\"EPSG\",\"102023\"]]";

    String FILENAME = "TestGridCoverage.jpg";

    private GridCoverage2DReader worldReader;

    private GridCoverage2DReader rainReader;

    private GridCoverage2DReader worldPaletteReader;

    private GeoTiffReader worldReader_0_360;

    private GeoTiffReader worldRoiReader;

    private GeoTiffReader sampleGribReader;

    private GeoTiffReader multiPixelPacked;

    private static double DELTA = 1E-6;

    @Before
    public void getData() throws IOException {
        MapProjection.SKIP_SANITY_CHECKS = true;
        File coverageFile = TestData.copy(this, "geotiff/world.tiff");
        assertTrue(coverageFile.exists());
        worldReader = new GeoTiffReader(coverageFile);

        coverageFile = TestData.copy(this, "geotiff/world_0_360.tiff");
        assertTrue(coverageFile.exists());
        worldReader_0_360 = new GeoTiffReader(coverageFile);

        coverageFile = TestData.copy(this, "geotiff/worldPalette.tiff");
        assertTrue(coverageFile.exists());
        worldPaletteReader = new GeoTiffReader(coverageFile);

        // grab also the global precipitation
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");
        URL rainURL =
                GridCoverageRendererTest.class.getResource("test-data/arcgrid/precip30min.asc");
        File rainFile = URLs.urlToFile(rainURL);
        rainReader = new ArcGridReader(rainFile);

        // read a image with a roi (mask)
        coverageFile = TestData.copy(this, "geotiff/world-roi.tiff");
        assertTrue(coverageFile.exists());
        worldRoiReader = new GeoTiffReader(coverageFile);

        // sampleGrib.tif has longitudes from 302 to 308 degrees East
        coverageFile =
                URLs.urlToFile(
                        GridCoverageRendererTest.class.getResource("test-data/sampleGrib.tif"));
        assertTrue(coverageFile.exists());
        sampleGribReader = new GeoTiffReader(coverageFile);

        // multi pixel packed sample model test case, two bits per pixel
        coverageFile =
                URLs.urlToFile(
                        GridCoverageRendererTest.class.getResource(
                                "test-data/multi_pixel_packed.tif"));
        assertTrue(coverageFile.exists());
        multiPixelPacked = new GeoTiffReader(coverageFile);
    }

    @After
    public void close() throws IOException {
        MapProjection.SKIP_SANITY_CHECKS = false;
        worldReader.dispose();
        EnvFunction.clearLocalValues();
    }

    /**
     * Returns a {@link GridCoverage} which may be used as a "real world" example.
     *
     * @param number The example number. Numbers are numeroted from 0 to {@link #getNumExamples()}
     *     exclusive.
     * @return The "real world" grid coverage.
     * @throws IOException if an I/O operation was needed and failed.
     */
    private final GridCoverage2D getGC()
            throws IOException, IllegalArgumentException, ParseException {
        final String path;
        final Rectangle2D bounds;

        // unit = "°C";
        path = "TestGridCoverage.tif";
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        // 41°S - 5°N ; 35°E - 80°E (450 x 460 pixels)
        bounds = new Rectangle2D.Double(35, -41, 45, 46);
        final GeneralEnvelope envelope = new GeneralEnvelope(bounds);
        final RenderedImage image = ImageIO.read(TestData.getResource(this, path));
        final int numBands = image.getSampleModel().getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.
        for (int i = 0; i < numBands; i++) {

            bands[i] = new GridSampleDimension("band " + i);
        }
        final String filename = new File(path).getName();
        final GridCoverageFactory factory =
                org.geotools.coverage.CoverageFactoryFinder.getGridCoverageFactory(
                        GeoTools.getDefaultHints());
        envelope.setCoordinateReferenceSystem(crs);
        return factory.create(filename, image, envelope, bands, null, null);
    }

    /** Returns a projected CRS for test purpose. */
    private static CoordinateReferenceSystem getProjectedCRS(final GridCoverage2D coverage) {
        try {
            final GeographicCRS base = (GeographicCRS) coverage.getCoordinateReferenceSystem();
            final Ellipsoid ellipsoid = base.getDatum().getEllipsoid();
            final DefaultMathTransformFactory factory = new DefaultMathTransformFactory();
            final ParameterValueGroup parameters =
                    factory.getDefaultParameters("Oblique_Stereographic");
            parameters.parameter("semi_major").setValue(ellipsoid.getSemiMajorAxis());
            parameters.parameter("semi_minor").setValue(ellipsoid.getSemiMinorAxis());
            parameters.parameter("central_meridian").setValue(5);
            parameters.parameter("latitude_of_origin").setValue(-5);
            final MathTransform mt;
            try {
                mt = factory.createParameterizedTransform(parameters);
            } catch (FactoryException exception) {
                fail(exception.getLocalizedMessage());
                return null;
            }
            // create the projected crs
            return new DefaultProjectedCRS(
                    java.util.Collections.singletonMap("name", "Stereographic"),
                    base,
                    mt,
                    DefaultCartesianCS.PROJECTED);
        } catch (NoSuchIdentifierException exception) {
            fail(exception.getLocalizedMessage());
            return null;
        }
    }

    @Test
    public void paint() throws Exception {

        //
        // /////////////////////////////////////////////////////////////////
        //
        // CREATING A GRID COVERAGE
        //
        //
        // /////////////////////////////////////////////////////////////////
        final GridCoverage2D gc = getGC();

        //
        //
        // /////////////////////////////////////////////////////////////////
        //
        // MAP CONTEXT
        //
        //
        // /////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        final Style style = getStyle();
        map.addLayer(new GridCoverageLayer(gc, style));

        // /////////////////////////////////////////////////////////////////
        //
        // Streaming renderer
        //
        //
        // ///////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);
        RendererBaseTest.showRender("testGridCoverage", renderer, 1000, map.getMaxBounds());
    }

    /**
     * Tests what happens when the grid coverage is associated with a broken style with no
     * symbolizers inside. It should just render nothing, a NPE was reported instead in GEOT-2543.
     */
    @Test
    public void paintWrongStyle() throws Exception {
        final GridCoverage2D gc = getGC();
        final MapContent map = new MapContent();

        // final Style style = new StyleBuilder().createStyle((Symbolizer) null);
        final Style style = RendererBaseTest.loadStyle(this, "empty.sld");
        map.addLayer(new GridCoverageLayer(gc, style));

        final StreamingRenderer renderer = new StreamingRenderer();
        CountingRenderListener counter = new CountingRenderListener();
        renderer.addRenderListener(counter);
        renderer.setMapContent(map);
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        renderer.paint(g2d, new Rectangle(0, 0, 300, 300), map.getMaxBounds());
        g2d.dispose();
        map.dispose();
        // make sure no errors and no features
        assertEquals(0, counter.errors);
        assertEquals(0, counter.features);
    }

    @Test
    public void reproject() throws Exception {

        // ///////////////////////////////////////////////////////////////////
        //
        // CREATING A GRID COVERAGE
        //
        //
        // // /////////////////////////////////////////////////////////////////
        final GridCoverage2D coverage = getGC();
        //
        // ///////////////////////////////////////////////////////////////////
        //
        // MAP CONTEXT
        // We want to show the context in a different CRS
        //
        //
        // /////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        final Style style = getStyle();
        map.addLayer(new GridCoverageLayer(coverage, style));

        // transform to a new crs
        final CoordinateReferenceSystem destCRS = getProjectedCRS(coverage);

        // ///////////////////////////////////////////////////////////////////
        //
        // Streaming renderer
        //
        // /////////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);

        ReferencedEnvelope env = map.getMaxBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX(),
                        env.getMaxX(),
                        env.getMinY(),
                        env.getMaxY(),
                        DefaultGeographicCRS.WGS84);
        final ReferencedEnvelope newbounds = env.transform(destCRS, true);

        RendererBaseTest.showRender("testGridCoverageReprojection", renderer, 1000, newbounds);
    }

    @Test
    public void testRenderingBuffer() throws Exception {
        // prepare the layer
        MapContent content = new MapContent();
        final Style style = getStyle();
        content.addLayer(new GridReaderLayer(worldReader, style));

        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        renderer.setRendererHints(Collections.singletonMap("renderingBuffer", 1024));

        BufferedImage image =
                RendererBaseTest.showRender(
                        "testGridCoverageReprojection",
                        renderer,
                        1000,
                        content.getViewport().getBounds());
        ImageAssert.assertEquals(
                new File("src/test/resources/org/geotools/renderer/lite/rescaled.png"),
                image,
                1000);
    }

    @Test
    public void testInterpolationBicubic() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -20037508.34, 20037508.34, -20037508.34, 20037508.34, googleMercator);
        Rectangle screenSize =
                new Rectangle(200, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 200));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        GridCoverage2D coverage = worldReader.read(null);
        RenderedImage image =
                renderer.renderImage(
                        coverage,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                        Color.RED,
                        256,
                        256);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorBicubic.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testInvertedColors() throws Exception {
        MapContent content = new MapContent();
        content.getViewport()
                .setBounds(
                        new ReferencedEnvelope(
                                -179.9997834892,
                                180.00025801626,
                                -89.999828389438,
                                270.00021311603,
                                DefaultGeographicCRS.WGS84));
        RasterSymbolizer rs = buildRainColorMap();
        final Style style = new StyleBuilder().createStyle(rs);
        content.addLayer(new GridReaderLayer(rainReader, style));

        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        Map<Object, Object> rendererParams = new HashMap<Object, Object>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);

        BufferedImage image =
                RendererBaseTest.showRender(
                        "testGridCoverageReprojection",
                        renderer,
                        1000,
                        content.getViewport().getBounds());
        ImageAssert.assertEquals(
                new File("src/test/resources/org/geotools/renderer/lite/inverted.png"),
                image,
                1000);
    }

    private static Style getStyle() {
        StyleBuilder sb = new StyleBuilder();
        Style rasterstyle = sb.createStyle();
        RasterSymbolizer raster = sb.createRasterSymbolizer();
        rasterstyle.featureTypeStyles().add(sb.createFeatureTypeStyle(raster));
        rasterstyle.featureTypeStyles().get(0).setName("GridCoverage");
        return rasterstyle;
    }

    @Test
    public void testReprojectBuffer() throws Exception {
        // create a solid color 1m coverage in EPSG:26915
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bi.createGraphics();
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        graphics.dispose();

        double baseX = 529687;
        double baseY = 3374773;
        CoordinateReferenceSystem nativeCrs = CRS.decode("EPSG:26915", true);
        GridCoverage2D coverage =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create(
                                "test",
                                bi,
                                new ReferencedEnvelope(
                                        baseX,
                                        baseX + bi.getWidth(),
                                        baseY,
                                        baseY + bi.getHeight(),
                                        nativeCrs));

        // write out as geotiff
        File testFile = new File("./target/testReprojection.tiff");
        GeoTiffWriter writer = new GeoTiffWriter(testFile);
        writer.write(coverage, null);

        // setup to read only a block 50x50 in the middle
        MathTransform r2m = coverage.getGridGeometry().getGridToCRS();
        Envelope env = new Envelope(25, 75, 25, 75);
        ReferencedEnvelope read26915 = new ReferencedEnvelope(JTS.transform(env, r2m), nativeCrs);
        CoordinateReferenceSystem mapCRS = CRS.decode("EPSG:3857", true);
        ReferencedEnvelope read3857 = read26915.transform(mapCRS, true);

        // setup map content
        StyleBuilder sb = new StyleBuilder();
        Layer layer =
                new GridReaderLayer(
                        new GeoTiffReader(testFile), sb.createStyle(sb.createRasterSymbolizer()));
        MapContent mc = new MapContent();
        mc.getViewport().setBounds(read3857);
        mc.addLayer(layer);

        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        BufferedImage result =
                RendererBaseTest.showRender(
                        "testGridCoverageBoundsReprojection", sr, 1000, read3857);

        // there used to be a white triangle in the lower right corner of the output
        ImageAssert.assertEquals(
                new File("src/test/resources/org/geotools/renderer/lite/reprojectBuffer.png"),
                result,
                0);
    }

    @Test
    public void testReprojectGoogleMercator() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -20037508.34, 20037508.34, -20037508.34, 20037508.34, googleMercator);
        Rectangle screenSize =
                new Rectangle(200, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 200));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        GridCoverage2D coverage = worldReader.read(null);
        RenderedImage image =
                renderer.renderImage(
                        coverage,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        // always set ROI on reprojection
        assertThat(image.getProperty("roi"), instanceOf(ROI.class));

        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercator.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testReprojectGoogleMercatorLargerThanWorld() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -38448446.580832,
                        38448446.580832,
                        -44138127.016561,
                        44138127.016561,
                        googleMercator);
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorLargerThanWorld.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testReprojectGoogleMercatorBlackLine() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -70650900.64528,
                        32797834.549784,
                        -25517354.68145,
                        26207012.916082,
                        googleMercator);
        Rectangle screenSize = new Rectangle(600, 300);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        // always set ROI on reprojection
        assertThat(image.getProperty("roi"), instanceOf(ROI.class));

        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorBlackLine.png");
        ImageAssert.assertEquals(reference, image, 5);
    }

    @Test
    public void testReprojectGoogleMercatorTouchDateline() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(20037508.34, 40075016.68, 0, 20037508.34, googleMercator);
        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorTouchDateline.png");
        ImageAssert.assertEquals(reference, image, 10);
    }

    @Test
    public void testAcrossDatelineBilinear() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(10, 350, -90, 90, DefaultGeographicCRS.WGS84);
        Rectangle screenSize =
                new Rectangle(500, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 500));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(DefaultGeographicCRS.WGS84, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/wrapDatelineNearest.png");
        ImageAssert.assertEquals(reference, image, 20);
    }

    @Test
    public void testNoProjectionHandlerSet() throws Exception {
        // Request crossing dateline should not contain wrapped data
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(10, 500, -90, 90, DefaultGeographicCRS.WGS84);
        Rectangle screenSize =
                new Rectangle(500, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 500));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(DefaultGeographicCRS.WGS84, mapExtent, screenSize, w2s);
        // Setting No projectionHandler
        renderer.setAdvancedProjectionHandlingEnabled(false);
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/noProjectionHandlerSet.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testCrashOutsideValidArea() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        0.00023726353151687,
                        180.00025801626,
                        -269.99984914217,
                        -89.999828389438,
                        DefaultGeographicCRS.WGS84);
        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(DefaultGeographicCRS.WGS84, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNull(image);
    }

    private void assertNull(RenderedImage image) {
        // TODO Auto-generated method stub

    }

    @Test
    public void testAcrossDatelineBicubic() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(10, 350, -90, 90, DefaultGeographicCRS.WGS84);
        Rectangle screenSize =
                new Rectangle(500, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 500));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(DefaultGeographicCRS.WGS84, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                        Color.RED,
                        256,
                        256);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/wrapDatelineBicubic.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testUTM() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-6e6, 6e6, 0, 5.3e6, crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true).transform(crs,
        // true));
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File("src/test/resources/org/geotools/renderer/lite/gridcoverage2d/utm.png");
        ImageAssert.assertEquals(reference, image, 0);

        Object property = image.getProperty(GridCoverageRenderer.PARENT_COVERAGE_PROPERTY);
        assertNotNull(property);
        assertTrue(property instanceof GridCoverage2D);
        GridCoverage2D propertyCoverage = (GridCoverage2D) property;
        CoordinateReferenceSystem targetCrs = propertyCoverage.getCoordinateReferenceSystem();
        assertTrue(targetCrs.getName().equals(crs.getName()));
    }

    @Test
    public void testSouthPolar() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-6000000, 6000000, -8000000, -1000000, crs);

        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/south_polar.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testNorthPolar() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3995", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-6000000, 6000000, 8000000, 1000000, crs);

        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/north_polar.png");
        ImageAssert.assertEquals(reference, image, 40);
    }

    @Test
    public void testIndexedWithNoBackground() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -20037508.34, 20037508.34, -20037508.34, 20037508.34, googleMercator);
        Rectangle screenSize =
                new Rectangle(200, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 200));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image =
                renderer.renderImage(
                        worldPaletteReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                        null,
                        256,
                        256);
        assertNotNull(image);
    }

    @Test
    public void testPolarCutLowCorner() throws Exception {
        // we request a small area, oversampling, and the output has some white pixels in a corner
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-2500000, -1250000, -10000000, -8750000, crs);

        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRainColorMap();

        RenderedImage image =
                renderer.renderImage(
                        rainReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/polar_whitecorner.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    private RasterSymbolizer buildRainColorMap() {
        StyleBuilder sb = new StyleBuilder();
        ColorMap colorMap =
                sb.createColorMap(
                        new String[] {"1", "2", "3", "4"},
                        new double[] {0, 100, 2000, 5000},
                        new Color[] {Color.RED, Color.WHITE, Color.GREEN, Color.BLUE},
                        ColorMap.TYPE_RAMP);
        RasterSymbolizer rasterSymbolizer = sb.createRasterSymbolizer(colorMap, 1d);
        return rasterSymbolizer;
    }

    @Test
    public void testPolarCutUpperCorner() throws Exception {
        // We request a small area, oversampling, and the output has some white pixels in the up
        // corner
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-625000, 0, -10000000, -9375000, crs);

        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRainColorMap();

        RenderedImage image =
                renderer.renderImage(
                        rainReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/polar_whitecorner_up.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testPolarTouchDateline() throws Exception {
        // we request a small area, oversampling, and the output has some white pixels in a corner
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-1250000, 0, -10000000, -8750000, crs);

        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRainColorMap();

        RenderedImage image =
                renderer.renderImage(
                        rainReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/polar_touchdateline.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testAfricaEquidistantConic() throws Exception {
        CoordinateReferenceSystem crs = CRS.parseWKT(AFRICA_EQUIDISTANT_CONIC_WKT);
        // across the dateline
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -15814047.554122284,
                        24919762.252195686,
                        -14112074.925190449,
                        11688610.748676982,
                        crs);
        // Setting Screen size
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);
        // Apply the symbolizer
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        // Render the image
        RenderedImage image =
                renderer.renderImage(
                        worldReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        assertNotNull(image);
        // Check the image
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/africa-conic.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testAfricaEquidistantConicIndexed() throws Exception {
        CoordinateReferenceSystem crs = CRS.parseWKT(AFRICA_EQUIDISTANT_CONIC_WKT);
        // across the dateline
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -15814047.554122284,
                        24919762.252195686,
                        -14112074.925190449,
                        11688610.748676982,
                        crs);
        // Setting Screen size
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);
        // Apply the symbolizer
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        // Get the reader
        File coverageFile = TestData.copy(this, "geotiff/worldPalette.tiff");
        assertTrue(coverageFile.exists());
        GridCoverage2DReader reader = new GeoTiffReader(coverageFile);
        // Render the image
        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        // we have performed color expansion to allow for red background (it's not in the palette)
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        // Check the image
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/africa-conic-palette.png");
        ImageAssert.assertEquals(reference, image, 10);
    }

    @Test
    public void testAfricaEquidistantConicRoi() throws Exception {
        CoordinateReferenceSystem crs = CRS.parseWKT(AFRICA_EQUIDISTANT_CONIC_WKT);
        // across the dateline
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -15814047.554122284,
                        24919762.252195686,
                        -14112074.925190449,
                        11688610.748676982,
                        crs);
        // Setting Screen size
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);
        // Apply the symbolizer
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        // Get the reader
        // Render the image
        RenderedImage image =
                renderer.renderImage(
                        worldRoiReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        // Check the image
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/africa-conic-roi.png");
        ImageAssert.assertEquals(reference, image, 10);
    }

    @Test
    public void testFlippedAffine() throws Exception {
        // get the source data
        File coverageFile = TestData.copy(this, "geotiff/float64.tif");
        assertTrue(coverageFile.exists());
        GridCoverage2DReader reader = new GeoTiffReader(coverageFile);

        // Apply the symbolizer
        Style style = RendererBaseTest.loadStyle(this, "float64.sld");
        RasterSymbolizer rasterSymbolizer =
                (RasterSymbolizer)
                        style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);

        // Render, used to return a white image
        ReferencedEnvelope mapExtent = ReferencedEnvelope.reference(reader.getOriginalEnvelope());
        Rectangle screenSize = new Rectangle(50, 50);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        reader.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);
        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);
        assertNotNull(image);
        // Check the image
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/flippedAffine.png");
        ImageAssert.assertEquals(reference, image, 2);
    }

    @Test
    public void testEnvFunctionInColorMap() throws Exception {
        EnvFunction.setLocalValue("low", "-0.001");
        EnvFunction.setLocalValue("lowColor", "#000000");

        // get the source data
        File coverageFile = TestData.copy(this, "geotiff/float64.tif");
        assertTrue(coverageFile.exists());
        GridCoverage2DReader reader = new GeoTiffReader(coverageFile);

        // Apply the symbolizer
        Style style = RendererBaseTest.loadStyle(this, "float64.sld");

        final MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        BufferedImage image =
                RendererBaseTest.renderImage(renderer, mc.getViewport().getBounds(), null, 50, 50);

        // Check the image
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/flippedAffineParametric.png");
        ImageAssert.assertEquals(reference, image, 2);
    }

    @Test
    public void testCoverage_0_360() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(100, 260, -90, 90, crs);
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image =
                renderer.renderImage(
                        worldReader_0_360,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/world_0_360.png");
        ImageAssert.assertEquals(reference, image, 10);
    }

    /** Test rendering of sampleGrib.tif on its native longitude bounds (302,308). */
    @Test
    // background == nodata assumption
    public void testSampleGrib() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(302, 308, 2, 10, crs);
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image =
                renderer.renderImage(
                        sampleGribReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.GRAY,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/sampleGrib.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    /** Test that rendering of sampleGrib.tif on longitude (304,310) results in cropping. */
    @Test
    public void testSampleGribCropLongitude() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(304, 310, 2, 10, crs);
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);
        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image =
                renderer.renderImage(
                        sampleGribReader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.GRAY,
                        256,
                        256);
        assertNotNull(image);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/sampleGribCropLongitude.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    /**
     * Test to check the case where band selection cannot be pushed down to the reader, but needs to
     * be run in memory
     */
    @Test
    public void testBandSelectionOnNonSupportingReader() throws Exception {
        // Create a solid color coverage
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        graphics.dispose();

        CoordinateReferenceSystem nativeCrs = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(0, 90, 0, 90, nativeCrs);
        GridCoverage2D coverage =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create("test", bi, new ReferencedEnvelope(0, 90, 0, 90, nativeCrs));

        assertEquals(coverage.getNumSampleDimensions(), 3);

        // Write out as a geotiff
        File coverageFile = new File("./target/blue.tiff");
        GeoTiffWriter writer = new GeoTiffWriter(coverageFile);
        final GeoTiffFormat format = new GeoTiffFormat();
        final GeoTiffWriteParams wp = new GeoTiffWriteParams();

        // setting compression to LZW
        wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
        wp.setCompressionType("LZW");
        wp.setCompressionQuality(0.75F);

        final ParameterValueGroup params = format.getWriteParameters();
        List<GeneralParameterValue> paramsValues = params.values();
        writer.write(
                coverage, params.values().toArray(new GeneralParameterValue[paramsValues.size()]));

        // Get the reader, read with band selection
        assertTrue(coverageFile.exists());
        GridCoverage2DReader reader = new GeoTiffReader(coverageFile);

        // Render the image selecting blue
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(nativeCrs, mapExtent, new Rectangle(0, 0, 100, 100), null);
        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        buildChannelSelectingSymbolizer(3),
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertEquals(1, image.getSampleModel().getNumBands());
        assertEquals(255, new ImageWorker(image).getMinimums()[0], 0d);
        ImageUtilities.disposeImage(image);

        // Render again selecting red
        image =
                renderer.renderImage(
                        reader,
                        null,
                        buildChannelSelectingSymbolizer(1),
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertEquals(1, image.getSampleModel().getNumBands());
        assertEquals(0, new ImageWorker(image).getMaximums()[0], 0d);
        ImageUtilities.disposeImage(image);
    }

    /** Tests band selection with Env Function Expression */
    @Test
    public void testBandSelectionExpression() throws Exception {
        // Create a solid color coverage
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        graphics.dispose();

        CoordinateReferenceSystem nativeCrs = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(0, 90, 0, 90, nativeCrs);
        GridCoverage2D coverage =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create("test", bi, new ReferencedEnvelope(0, 90, 0, 90, nativeCrs));

        assertEquals(coverage.getNumSampleDimensions(), 3);

        // Write out as a geotiff
        File coverageFile = new File("./target/blue.tiff");
        GeoTiffWriter writer = new GeoTiffWriter(coverageFile);
        final GeoTiffFormat format = new GeoTiffFormat();
        final GeoTiffWriteParams wp = new GeoTiffWriteParams();

        // setting compression to LZW
        wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
        wp.setCompressionType("LZW");
        wp.setCompressionQuality(0.75F);

        final ParameterValueGroup params = format.getWriteParameters();
        List<GeneralParameterValue> paramsValues = params.values();
        writer.write(
                coverage, params.values().toArray(new GeneralParameterValue[paramsValues.size()]));

        // Get the reader, read with band selection
        assertTrue(coverageFile.exists());
        GridCoverage2DReader reader = new GeoTiffReader(coverageFile);

        String b1 = "B1";
        RasterSymbolizer rasterSymb = buildEnvChannelSelectingSymbolizer(b1, 3);

        // check default env value: 3
        // Render the image selecting blue as default ENV value
        EnvFunction.removeLocalValue(b1);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(nativeCrs, mapExtent, new Rectangle(0, 0, 100, 100), null);
        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        rasterSymb,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertEquals(1, image.getSampleModel().getNumBands());
        assertEquals(255, new ImageWorker(image).getMinimums()[0], 0d);

        // check instanced env value B1:1
        EnvFunction.setLocalValue(b1, "1");
        image =
                renderer.renderImage(
                        reader,
                        null,
                        rasterSymb,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertEquals(1, image.getSampleModel().getNumBands());
        assertEquals(0, new ImageWorker(image).getMinimums()[0], 0d);

        EnvFunction.removeLocalValue(b1);
        ImageUtilities.disposeImage(image);
    }

    @Test
    public void testBandSelectionSupportingReader() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(0, 90, 0, 90, DefaultGeographicCRS.WGS84);

        GridCoverage2DReader reader = new TestSingleBandReader(2);

        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84, mapExtent, new Rectangle(0, 0, 100, 100), null);
        // keeping a reference to the raster symbolizer so we can check we has not altered
        // during the band setup the raster symbolizer channel selection needs to be rearranged
        // but the original raster symbolizer should not be altered
        RasterSymbolizer rasterSymbolizer = buildChannelSelectingSymbolizer(3);
        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertEquals(1, image.getSampleModel().getNumBands());
        assertEquals(255, new ImageWorker(image).getMinimums()[0], 0d);
        // test that raster symbolizer was not altered
        RasterSymbolizer expectedRasterSymbolizer = buildChannelSelectingSymbolizer(3);
        // during the copy method contrast enhancement NULL options are converted to an empty
        // HashMap
        expectedRasterSymbolizer.getContrastEnhancement().setOptions(Collections.emptyMap());
        assertEquals(rasterSymbolizer, expectedRasterSymbolizer);
        ImageUtilities.disposeImage(image);
    }

    @Test
    public void testContrastEnhancementInChannelSelectionAfterBandSelection() throws Exception {

        GridCoverage2DReader reader = new TestMultiBandReader(0, 2, 4);
        applyAndAssertContrastEnhancement(reader);
    }

    private RasterSymbolizer createClippingChannelSelectionSymbolizer(int min, int max) {
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

        for (int i = 0; i < 3; i++) {
            final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();
            final ContrastMethodStrategy method = new NormalizeContrastMethodStrategy();
            method.addOption(
                    "algorithm",
                    sldBuilder.literalExpression(
                            ContrastEnhancementType.NORMALIZE_CLIP_TO_ZERO_NAME));
            method.addOption("minValue", sldBuilder.literalExpression(min + (20 * i)));
            method.addOption("maxValue", sldBuilder.literalExpression(max + (20 * i)));
            cntEnh.setMethod(method);
            channels[i].setChannelName(Integer.toString((i * 2) + 1));
            channels[i].setContrastEnhancement(cntEnh);
        }
        chSel.setRGBChannels(chTypeRed, chTypeGreen, chTypeBlue);

        symbolizer.setChannelSelection(chSel);
        symbolizer.setOpacity(sldBuilder.literalExpression(1.0));
        return symbolizer;
    }

    /**
     * GEOT-5773 Geoserver can generate images without a color model (before the RasterSymbolizer is
     * applied). These images should also be rendered appropriately when the symbolizer converts the
     * bands to a range that makes sense.
     */
    @Test
    public void testContrastEnhancementInChannelSelectionAfterBandSelectionWithoutColorModel()
            throws Exception {
        GridCoverage2DReader reader =
                new TestMultiBandReader(0, 2, 4) {
                    @Override
                    public GridCoverage2D read(GeneralParameterValue[] parameters)
                            throws IOException {
                        GridCoverage2D originalCoverage = super.read(parameters);
                        RenderedImage source =
                                new ImageWorker(originalCoverage.getRenderedImage())
                                        .format(DataBuffer.TYPE_USHORT)
                                        .getRenderedImage();
                        TiledImage shortImage =
                                new TiledImage(
                                        source.getMinX(),
                                        source.getMinY(),
                                        source.getWidth(),
                                        source.getHeight(),
                                        source.getTileGridXOffset(),
                                        source.getTileGridYOffset(),
                                        source.getSampleModel(),
                                        null);
                        shortImage.set(source);

                        // force color model to be null, this also occurs in real cases
                        GridCoverage2D coverage =
                                CoverageFactoryFinder.getGridCoverageFactory(null)
                                        .create(
                                                originalCoverage.getName(),
                                                shortImage,
                                                originalCoverage.getEnvelope2D());
                        return coverage;
                    }
                };
        applyAndAssertContrastEnhancement(reader);
    }

    private void applyAndAssertContrastEnhancement(GridCoverage2DReader reader)
            throws TransformException, NoninvertibleTransformException, FactoryException,
                    IOException {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(0, 90, 0, 90, DefaultGeographicCRS.WGS84);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84, mapExtent, new Rectangle(0, 0, 255, 255), null);

        int min = 10;
        int max = 100;
        RasterSymbolizer symbolizer = createClippingChannelSelectionSymbolizer(min, max);

        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        symbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertEquals(3, image.getSampleModel().getNumBands());

        // Make sure clip occurred even with optimized band selection
        ImageWorker worker = new ImageWorker(image);

        double[] maximums = worker.getMaximums();
        assertEquals(max, maximums[0], 0d);
        assertEquals(max + 20, maximums[1], 0d);
        assertEquals(max + 40, maximums[2], 0d);
        ImageUtilities.disposeImage(image);
    }

    @Test
    public void testChannelSelectionOrderWithBandSelection() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(0, 90, 0, 90, DefaultGeographicCRS.WGS84);

        GridCoverage2DReader reader = new TestMultiBandReader(4, 2, 0);

        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84, mapExtent, new Rectangle(0, 0, 255, 255), null);

        StyleBuilder sldBuilder = new StyleBuilder();

        RasterSymbolizer symbolizer = sldBuilder.createRasterSymbolizer();
        final ChannelSelection chSel = new ChannelSelectionImpl();
        final SelectedChannelType chTypeRed = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeBlue = new SelectedChannelTypeImpl();
        final SelectedChannelType chTypeGreen = new SelectedChannelTypeImpl();

        chTypeRed.setChannelName("5");
        chTypeGreen.setChannelName("3");
        chTypeBlue.setChannelName("1");

        chSel.setRGBChannels(chTypeRed, chTypeGreen, chTypeBlue);

        symbolizer.setChannelSelection(chSel);
        symbolizer.setOpacity(sldBuilder.literalExpression(1.0));

        Graphics2D graphics = ((BufferedImage) TestMultiBandReader.image).createGraphics();
        renderer.paint(
                graphics,
                reader,
                null,
                symbolizer,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.BLACK);
        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        symbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);

        // the read operation checked that the proper bandSelect (4,2,0) has been specified
        // instead of 0,1,2 (as per bugged code)
        assertEquals(3, image.getSampleModel().getNumBands());
    }

    @Test
    public void testPaintBandSelectionNonSupportingReader() throws Exception {
        File coverageFile = TestData.copy(this, "geotiff/worldPalette.tiff");
        assertTrue(coverageFile.exists());
        GridCoverage2DReader reader = new GeoTiffReader(coverageFile);

        RasterSymbolizer rasterSymbolizer = buildChannelSelectingSymbolizer(1);
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84,
                        ReferencedEnvelope.reference(reader.getOriginalEnvelope()),
                        new Rectangle(0, 0, 100, 100),
                        null);
        Graphics2D graphics = (Graphics2D) bi.getGraphics();
        renderer.paint(
                graphics,
                reader,
                null,
                rasterSymbolizer,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.BLACK);
        graphics.dispose();

        // check we got a gray image
        final double[] minimums = new ImageWorker(bi).getMaximums();
        final double[] maximums = new ImageWorker(bi).getMaximums();
        assertThat(maximums[0], equalTo(maximums[1]));
        assertThat(maximums[1], equalTo(maximums[2]));
        assertThat(minimums[0], equalTo(minimums[1]));
        assertThat(minimums[1], equalTo(minimums[2]));
    }

    @Test
    public void testPaintSelectionSupportingReader() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(0, 90, 0, 90, DefaultGeographicCRS.WGS84);

        GridCoverage2DReader reader = new TestSingleBandReader(2);

        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84, mapExtent, new Rectangle(0, 0, 100, 100), null);
        // keeping a reference to the raster symbolizer so we can check we has not altered
        // during the band setup the raster symbolizer channel selection needs to be rearranged
        // but the original raster symbolizer should not be altered
        RasterSymbolizer rasterSymbolizer = buildChannelSelectingSymbolizer(3);
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) bi.getGraphics();
        // .. the reader here checks the band selection params were passed down
        renderer.paint(
                graphics,
                reader,
                null,
                rasterSymbolizer,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.BLACK);

        // check we got a gray image
        final double[] minimums = new ImageWorker(bi).getMaximums();
        final double[] maximums = new ImageWorker(bi).getMaximums();
        assertThat(maximums[0], equalTo(maximums[1]));
        assertThat(maximums[1], equalTo(maximums[2]));
        assertThat(minimums[0], equalTo(minimums[1]));
        assertThat(minimums[1], equalTo(minimums[2]));
    }

    @Test
    public void testReprojectTransparency() throws Exception {
        ReferencedEnvelope re = new ReferencedEnvelope(0, 20, 20, 40, DefaultGeographicCRS.WGS84);
        CoordinateReferenceSystem utm32n = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope mapExtent = re.transform(utm32n, true);

        // get a subset of the coverage
        GridCoverage2D global = worldReader.read(null);
        CoverageProcessor processor =
                CoverageProcessor.getInstance(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        final ParameterValueGroup param =
                processor.getOperation("CoverageCrop").getParameters().clone();
        param.parameter("source").setValue(global);
        param.parameter("Envelope").setValue(re);
        GridCoverage2D cropped =
                (GridCoverage2D)
                        ((Crop) processor.getOperation("CoverageCrop")).doOperation(param, null);

        // render with reprojection, the ROI should be used to create transparent pixels
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = bi.createGraphics();
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(utm32n, mapExtent, new Rectangle(0, 0, 100, 100), null);
        renderer.paint(graphics, cropped, new StyleBuilder().createRasterSymbolizer());
        graphics.dispose();

        // top left and top right corners must be transparent now, the UTM shrinks towards the pole
        assertPixelIsTransparent(bi, 0, 0);
        assertPixelIsTransparent(bi, bi.getWidth() - 1, 0);
    }

    @Test
    public void testRenderOutsideBounds() throws Exception {
        ReferencedEnvelope re = new ReferencedEnvelope(0, 20, 20, 40, DefaultGeographicCRS.WGS84);

        // get a subset of the coverage
        GridCoverage2D global = worldReader.read(null);
        CoverageProcessor processor =
                CoverageProcessor.getInstance(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        final ParameterValueGroup param =
                processor.getOperation("CoverageCrop").getParameters().clone();
        param.parameter("source").setValue(global);
        param.parameter("Envelope").setValue(re);
        GridCoverage2D cropped =
                (GridCoverage2D)
                        ((Crop) processor.getOperation("CoverageCrop")).doOperation(param, null);

        ReferencedEnvelope reOutside =
                new ReferencedEnvelope(40, 60, 20, 40, DefaultGeographicCRS.WGS84);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84, reOutside, new Rectangle(0, 0, 100, 100), null);

        RasterSymbolizer symbolizer = new StyleBuilder().createRasterSymbolizer();
        Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        RenderedImage image =
                renderer.renderImage(cropped, symbolizer, interpolation, null, 256, 256);

        // outside coverage bounds, just return null but don't NPE
        assertNull(image);
    }

    @Test
    public void testRenderOffDateline() throws Exception {
        File coverageFile =
                URLs.urlToFile(
                        GridCoverageReaderHelperTest.class.getResource(
                                "test-data/off_dateline.tif"));
        assertTrue(coverageFile.exists());
        GeoTiffReader offDatelineReader = new GeoTiffReader(coverageFile);

        ReferencedEnvelope envelope =
                new ReferencedEnvelope(-180, 0, -90, 90, DefaultGeographicCRS.WGS84);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        DefaultGeographicCRS.WGS84, envelope, new Rectangle(0, 0, 450, 450), null);

        RasterSymbolizer symbolizer = new StyleBuilder().createRasterSymbolizer();
        Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        RenderedImage image =
                renderer.renderImage(
                        offDatelineReader, null, symbolizer, interpolation, null, 256, 256);
        assertNotNull(image);
        // goes out as indexed, switch to RGB as the image comparison needs it
        // (nodata = 0 in the source, the black in the original image becomes transparent)
        image =
                new ImageWorker(image)
                        .forceComponentColorModel()
                        .forceColorSpaceRGB()
                        .getRenderedImage();
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/offDateline.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testHighOversample() throws Exception {
        // tiny bbox request
        MapContent content = new MapContent();
        content.getViewport()
                .setBounds(
                        new ReferencedEnvelope(0, -0.0020, 0.0040, 0, DefaultGeographicCRS.WGS84));
        RasterSymbolizer rs = buildRainColorMap();
        final Style style = new StyleBuilder().createStyle(rs);
        content.addLayer(new GridReaderLayer(rainReader, style));

        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        Map<Object, Object> rendererParams = new HashMap<Object, Object>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);

        BufferedImage image =
                RendererBaseTest.showRender(
                        "highOversample", renderer, 1000, content.getViewport().getBounds());
        ImageAssert.assertEquals(
                new File("src/test/resources/org/geotools/renderer/lite/rainHighOversample.png"),
                image,
                1000);
    }

    @Test
    public void testHarvestSpatialTwoReaders() throws Exception {
        File source = TestData.file(GridCoverageReaderHelperTest.class, "red_footprint_test");
        File testDataDir = org.geotools.test.TestData.file(this, ".");
        File directory1 = new File(testDataDir, "redHarvest1");
        File directory2 = new File(testDataDir, "redHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // move all files except red3 to the second dir
        directory2.mkdirs();
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("red[^3].*"), null)) {
            assertTrue(file.renameTo(new File(directory2, file.getName())));
        }

        // crate the first reader
        URL harvestSingleURL = URLs.fileToUrl(directory1);
        ImageMosaicReader reader = new ImageMosaicReader(directory1, null);

        // now create a second reader that won't be informed of the harvesting changes
        // (simulating changes over a cluster, where the bbox information won't be updated from one
        // node to the other)
        ImageMosaicReader reader2 = new ImageMosaicReader(directory1, null);

        try {
            // harvest the other files with the first reader
            for (File file : directory2.listFiles()) {
                assertTrue(file.renameTo(new File(directory1, file.getName())));
            }
            reader.harvest(null, directory1, null);

            // now render an image directly
            ReferencedEnvelope readEnvelope =
                    new ReferencedEnvelope(
                            991000, 992000, 216000, 217000, reader2.getCoordinateReferenceSystem());
            Rectangle rasterArea = new Rectangle(0, 0, 10, 10);
            GridToEnvelopeMapper mapper =
                    new GridToEnvelopeMapper(new GridEnvelope2D(rasterArea), readEnvelope);
            AffineTransform affineTransform = mapper.createAffineTransform();
            GridCoverageRenderer renderer =
                    new GridCoverageRenderer(
                            reader2.getCoordinateReferenceSystem(),
                            readEnvelope,
                            rasterArea,
                            affineTransform);
            StyleBuilder sb = new StyleBuilder();
            RasterSymbolizer symbolizer = sb.createRasterSymbolizer();
            RenderedImage image =
                    renderer.renderImage(
                            reader2,
                            null,
                            symbolizer,
                            Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                            Color.BLACK,
                            256,
                            256);

            File reference =
                    new File(
                            "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/red.png");
            ImageAssert.assertEquals(reference, image, 0);

            // and render it also as streaming renderer
            MapContent mc = new MapContent();
            mc.addLayer(new GridReaderLayer(reader2, sb.createStyle(symbolizer)));
            StreamingRenderer sr = new StreamingRenderer();
            sr.setMapContent(mc);
            BufferedImage bi =
                    new BufferedImage(
                            rasterArea.width, rasterArea.height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = bi.createGraphics();
            sr.paint(graphics, rasterArea, readEnvelope);
            graphics.dispose();

            ImageAssert.assertEquals(reference, bi, 0);
        } finally {
            reader.dispose();
            reader2.dispose();
        }
    }

    /** Checks the pixel i/j is fully transparent */
    protected void assertPixelIsTransparent(BufferedImage image, int i, int j) {
        int pixel = image.getRGB(i, j);
        assertEquals(true, (pixel >> 24) == 0x00);
    }

    private RasterSymbolizer buildChannelSelectingSymbolizer(int band) {
        StyleBuilder sb = new StyleBuilder();
        RasterSymbolizer symbolizer = sb.createRasterSymbolizer();
        StyleFactory sf = sb.getStyleFactory();
        symbolizer.setChannelSelection(
                sf.createChannelSelection(
                        new SelectedChannelType[] {
                            sf.createSelectedChannelType(
                                    String.valueOf(band), (ContrastEnhancement) null)
                        }));
        return symbolizer;
    }

    /** Build a Symbolizer with ENV function ChannelName */
    private RasterSymbolizer buildEnvChannelSelectingSymbolizer(String envVar, int band) {
        StyleBuilder sb = new StyleBuilder();
        RasterSymbolizer symbolizer = sb.createRasterSymbolizer();
        StyleFactory sf = sb.getStyleFactory();
        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        symbolizer.setChannelSelection(
                sf.createChannelSelection(
                        new SelectedChannelType[] {
                            sf.createSelectedChannelType(
                                    ff.function("env", ff.literal(envVar), ff.literal(band)),
                                    (ContrastEnhancement) null)
                        }));
        return symbolizer;
    }

    /** Mock reader checking the expected band was requested */
    private static class TestSingleBandReader extends AbstractGridCoverage2DReader {

        int[] expectedBands;

        public TestSingleBandReader(int... expectedBands) {
            this.expectedBands = expectedBands;
            this.originalEnvelope =
                    new GeneralEnvelope(
                            new ReferencedEnvelope(0, 90, 0, 90, DefaultGeographicCRS.WGS84));
            this.crs = DefaultGeographicCRS.WGS84;
        }

        @Override
        public Format getFormat() {
            return new AbstractGridFormat() {

                @Override
                public GridCoverageWriter getWriter(Object destination, Hints hints) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public GridCoverageWriter getWriter(Object destination) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public AbstractGridCoverage2DReader getReader(Object source) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public boolean accepts(Object source, Hints hints) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public ParameterValueGroup getReadParameters() {
                    HashMap<String, String> info = new HashMap<String, String>();

                    info.put("name", "bandTester");
                    info.put("description", "desc");
                    info.put("vendor", "vendor");
                    info.put("docURL", "http://www.geotools.org");
                    info.put("version", "1.0");

                    List<GeneralParameterDescriptor> params =
                            new ArrayList<GeneralParameterDescriptor>();
                    params.add(AbstractGridFormat.BANDS);

                    return new ParameterGroup(
                            new DefaultParameterDescriptorGroup(
                                    info,
                                    params.toArray(new GeneralParameterDescriptor[params.size()])));
                }
            };
        }

        @Override
        public GridCoverage2D read(GeneralParameterValue[] parameters)
                throws IllegalArgumentException, IOException {
            assertTrue(
                    Arrays.stream(parameters)
                            .anyMatch(
                                    p ->
                                            "Bands".equals(p.getDescriptor().getName().toString())
                                                    && Arrays.equals(
                                                            expectedBands,
                                                            (int[])
                                                                    ((ParameterValue) p)
                                                                            .getValue())));

            // Create a solid color single band coverage
            BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = bi.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
            graphics.dispose();

            GridCoverage2D coverage =
                    CoverageFactoryFinder.getGridCoverageFactory(null)
                            .create("test", bi, getOriginalEnvelope());

            return coverage;
        }
    }

    /** Mock reader checking the expected band was requested */
    private static class TestMultiBandReader extends AbstractGridCoverage2DReader {

        int[] expectedBands;

        static BufferedImage image;

        static {
            // Create a striped image
            final int width = 255;
            final int height = 255;
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            WritableRaster raster = image.getRaster();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    for (int b = 0; b < 3; b++) {
                        raster.setSample(i, j, b, i);
                    }
                }
            }
        }

        public TestMultiBandReader(int... expectedBands) {
            this.expectedBands = expectedBands;
            this.originalEnvelope =
                    new GeneralEnvelope(
                            new ReferencedEnvelope(0, 90, 0, 90, DefaultGeographicCRS.WGS84));
            this.crs = DefaultGeographicCRS.WGS84;
        }

        @Override
        public Format getFormat() {
            return new AbstractGridFormat() {

                @Override
                public GridCoverageWriter getWriter(Object destination, Hints hints) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public GridCoverageWriter getWriter(Object destination) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public AbstractGridCoverage2DReader getReader(Object source) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public boolean accepts(Object source, Hints hints) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public ParameterValueGroup getReadParameters() {
                    HashMap<String, String> info = new HashMap<String, String>();

                    info.put("name", "bandTester");
                    info.put("description", "desc");
                    info.put("vendor", "vendor");
                    info.put("docURL", "http://www.geotools.org");
                    info.put("version", "1.0");

                    List<GeneralParameterDescriptor> params =
                            new ArrayList<GeneralParameterDescriptor>();
                    params.add(AbstractGridFormat.BANDS);

                    return new ParameterGroup(
                            new DefaultParameterDescriptorGroup(
                                    info,
                                    params.toArray(new GeneralParameterDescriptor[params.size()])));
                }
            };
        }

        @Override
        public GridCoverage2D read(GeneralParameterValue[] parameters)
                throws IllegalArgumentException, IOException {
            for (GeneralParameterValue parameter : parameters) {
                if ("Bands".equals(parameter.getDescriptor().getName().toString())) {
                    assertTrue(
                            Arrays.equals(
                                    expectedBands,
                                    (int[]) ((ParameterValue) parameter).getValue()));
                }
            }

            BufferedImage bi = image;
            GridCoverage2D coverage =
                    CoverageFactoryFinder.getGridCoverageFactory(null)
                            .create("test", bi, getOriginalEnvelope());

            return coverage;
        }
    }

    /** Test painting a request outside the valid area */
    @Test
    public void testPaintOutsideValidArea() throws Exception {

        StyleBuilder sb = new StyleBuilder();
        Style style = sb.createStyle(sb.createRasterSymbolizer());
        final MapContent content = new MapContent();
        content.addLayer(new GridReaderLayer(worldReader, style));

        final StreamingRenderer renderer = new StreamingRenderer();
        CountingRenderListener counter = new CountingRenderListener();
        renderer.addRenderListener(counter);
        renderer.setMapContent(content);
        Map<Object, Object> rendererParams = new HashMap<Object, Object>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        renderer.setRendererHints(rendererParams);
        BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3572", true);
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        -38207011.656556, -12735670.552185, 12735670.552186, 38207011.656556, crs);
        renderer.paint(g2d, new Rectangle(0, 0, 256, 256), envelope);
        g2d.dispose();
        content.dispose();
        // make sure no errors and no features
        assertEquals(0, counter.errors);
        assertEquals(0, counter.features);
    }

    @Test
    public void testNullSymbolizer() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        -20037508.34, 20037508.34, -20037508.34, 20037508.34, googleMercator);
        Rectangle screenSize =
                new Rectangle(200, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 200));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(googleMercator, mapExtent, screenSize, w2s);

        GridCoverage2D coverage = worldReader.read(null);
        RenderedImage image =
                renderer.renderImage(
                        coverage,
                        null,
                        Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                        Color.RED,
                        256,
                        256);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorBicubic.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    /** Test custom PADDING is being used when provided as Hint */
    @Test
    public void testPadding() throws Exception {
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        final double envWidth = 30;
        final double envHeight = 30;
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(0, envWidth, 0, envHeight, wgs84);
        Rectangle screenSize = new Rectangle((int) envWidth, (int) envHeight);
        checkPadding(screenSize, mapExtent, 4);
        checkPadding(screenSize, mapExtent, 12);
    }

    private void checkPadding(
            Rectangle screenSize, ReferencedEnvelope requestedEnvelope, int padding)
            throws FactoryException, IOException {
        // Setup padding Hints
        Hints hints = new Hints(GridCoverageRenderer.PADDING, padding);
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        worldReader,
                        screenSize,
                        requestedEnvelope,
                        Interpolation.getInstance(Interpolation.INTERP_BILINEAR),
                        hints);

        // Get the read envelope which should have been expanded by padding
        ReferencedEnvelope readEnvelope = helper.getReadEnvelope();
        assertEquals(requestedEnvelope.getWidth() + padding * 2, readEnvelope.getWidth(), DELTA);
        assertEquals(requestedEnvelope.getHeight() + padding * 2, readEnvelope.getHeight(), DELTA);
    }

    @Test
    public void testMultiPixelPackedRender() throws Exception {
        GridCoverage2D coverage = multiPixelPacked.read(null);
        RenderedImage image = coverage.getRenderedImage();
        ReferencedEnvelope mapExtent = ReferencedEnvelope.reference(coverage.getEnvelope2D());
        Rectangle screenSize =
                new Rectangle(
                        image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        coverage.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        Style style = RendererBaseTest.loadStyle(this, "landWaterRaster.sld");
        RasterSymbolizer rasterSymbolizer =
                (RasterSymbolizer)
                        style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        RenderedImage rendered =
                renderer.renderImage(
                        coverage,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/multiPixelPacked.png");
        ImageAssert.assertEquals(reference, rendered, 0);

        coverage.dispose(true);
    }

    @Test
    public void testChannelSelectOnRescaledDataset() throws Exception {
        URL coverageFile =
                org.geotools.test.TestData.url(GridCoverageRendererTest.class, "scale_offset");
        ImageMosaicReader reader = new ImageMosaicReader(coverageFile);

        ParameterValue<Boolean> rescalePixels = AbstractGridFormat.RESCALE_PIXELS.createValue();
        rescalePixels.setValue(true);

        ParameterValue<int[]> bands = AbstractGridFormat.BANDS.createValue();
        bands.setValue(new int[] {0});
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {rescalePixels, bands});
        NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
        assertNotNull(noDataProperty);
        double noData = noDataProperty.getAsSingleValue();

        // At that specific location (x=7,y=2) sample should be nodata
        final int noDataPixelCoordinateX = 7;
        final int noDataPixelCoordinateY = 2;
        RenderedImage image = coverage.getRenderedImage();
        Raster raster = image.getData();
        double sample = raster.getSampleDouble(noDataPixelCoordinateX, noDataPixelCoordinateY, 0);
        assertEquals(noData, sample, 1E-6);

        ReferencedEnvelope mapExtent = ReferencedEnvelope.reference(coverage.getEnvelope2D());
        Rectangle screenSize =
                new Rectangle(
                        image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        coverage.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        Style style = RendererBaseTest.loadStyle(this, "sld1.sld");
        RasterSymbolizer rasterSymbolizer =
                (RasterSymbolizer)
                        style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        image =
                renderer.renderImage(
                        coverage,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        ColorModel cm = image.getColorModel();
        assertTrue(cm instanceof IndexColorModel);
        IndexColorModel icm = (IndexColorModel) cm;
        int transparentPixel = icm.getTransparentPixel();

        raster = image.getData();
        int sampleB = raster.getSample(noDataPixelCoordinateX, noDataPixelCoordinateY, 0);
        assertEquals(transparentPixel, sampleB);
        coverage.dispose(true);
    }

    @Test
    public void testContrastEnhancementWithNodataDataset() throws Exception {
        URL coverageFile =
                org.geotools.test.TestData.url(GridCoverageRendererTest.class, "nodataNbands.tiff");
        GeoTiffReader reader = new GeoTiffReader(coverageFile);
        GridCoverage2D coverage = reader.read(null);
        NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
        assertNotNull(noDataProperty);
        double noData = noDataProperty.getAsSingleValue();

        // At that specific location (x=7,y=2) sample should be nodata
        final int noDataPixelCoordinateX = 7;
        final int noDataPixelCoordinateY = 2;
        RenderedImage image = coverage.getRenderedImage();
        Raster raster = image.getData();
        double sample = raster.getSampleDouble(noDataPixelCoordinateX, noDataPixelCoordinateY, 0);
        assertEquals(noData, sample, 1E-6);

        ReferencedEnvelope mapExtent = ReferencedEnvelope.reference(coverage.getEnvelope2D());
        Rectangle screenSize =
                new Rectangle(
                        image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        coverage.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        Style style = RendererBaseTest.loadStyle(this, "n_bands.sld");
        RasterSymbolizer rasterSymbolizer =
                (RasterSymbolizer)
                        style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        image =
                renderer.renderImage(
                        coverage,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        256,
                        256);

        raster = image.getData();
        int sampleB = raster.getSample(noDataPixelCoordinateX, noDataPixelCoordinateY, 0);
        assertEquals(0, sampleB);
        coverage.dispose(true);
    }

    @Test
    public void testRenderRadarOnAzeq() throws Exception {
        File file =
                URLs.urlToFile(
                        GridCoverageRendererTest.class.getResource(
                                "gridcoverage2d/test-data/pacific_radar.tif"));
        GeoTiffReader reader = new GeoTiffReader(file);
        System.out.println(reader.getCoordinateReferenceSystem());
        ReferencedEnvelope readerEnvelope =
                ReferencedEnvelope.reference(reader.getOriginalEnvelope());
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,170,-16", true);
        ReferencedEnvelope azeqEnvelope = readerEnvelope.transform(crs, true);

        testImage(reader, azeqEnvelope, "pacificRadar.png");
    }

    @Test
    public void testRenderRadarOnAzeqZoomedIn() throws Exception {
        File file =
                URLs.urlToFile(
                        GridCoverageRendererTest.class.getResource(
                                "gridcoverage2d/test-data/pacific_radar.tif"));
        GeoTiffReader reader = new GeoTiffReader(file);
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,170,-16", true);
        ReferencedEnvelope azeqEnvelope =
                new ReferencedEnvelope(
                        898340.67097417, 1127313.3142259, -8529.3690118708, 220443.2742399, crs);

        testImage(reader, azeqEnvelope, "pacificRadarZoomedIn.png");
    }

    @Test
    public void testRenderRadarOnCentralAzeq() throws Exception {
        File file =
                URLs.urlToFile(
                        GridCoverageRendererTest.class.getResource(
                                "gridcoverage2d/test-data/pacific_radar.tif"));
        GeoTiffReader reader = new GeoTiffReader(file);
        ReferencedEnvelope readerEnvelope =
                ReferencedEnvelope.reference(reader.getOriginalEnvelope());
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,0,0", true);
        ReferencedEnvelope azeqEnvelope =
                new ReferencedEnvelope(-10698974, 10065735, -18709397, -1.4979931, crs);

        testImage(reader, azeqEnvelope, "pacificRadarCentralAzeq.png");
    }

    private void testImage(GeoTiffReader reader, ReferencedEnvelope vaiEnvelope, String fileName)
            throws TransformException, NoninvertibleTransformException, FactoryException,
                    IOException {
        ReferencedEnvelope mapExtent = vaiEnvelope;
        Rectangle screenSize =
                new Rectangle(400, (int) (mapExtent.getHeight() / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer =
                new GridCoverageRenderer(
                        mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRadardStyle();

        RenderedImage image =
                renderer.renderImage(
                        reader,
                        null,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.BLACK,
                        256,
                        256);
        assertNotNull(image);
        // RenderedImageBrowser.showChain(image, true, true, "foo", true);
        File reference =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/" + fileName);
        ImageAssert.assertEquals(reference, image, 20);
    }

    private RasterSymbolizer buildRadardStyle() {
        StyleBuilder sb = new StyleBuilder();
        ColorMap cm =
                sb.createColorMap(
                        new String[] {"a", "b"},
                        new double[] {-20, 50},
                        new Color[] {Color.RED, Color.GREEN},
                        ColorMap.TYPE_RAMP);
        RasterSymbolizer rasterSymbolizer = sb.createRasterSymbolizer(cm, 1d);
        return rasterSymbolizer;
    }
}
