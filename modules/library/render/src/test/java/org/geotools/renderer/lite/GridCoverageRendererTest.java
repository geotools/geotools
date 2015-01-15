/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2014, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;

import org.geotools.TestData;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.factory.GeoTools;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageRenderer;
import org.geotools.styling.ColorMap;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author Simone Giannecchini
 *
 *
 * @source $URL$
 */
public class GridCoverageRendererTest  {


	String FILENAME = "TestGridCoverage.jpg";

    private GridCoverage2DReader worldReader;

    private GridCoverage2DReader rainReader;

    @Before
    public void getData() throws IOException {
        MapProjection.SKIP_SANITY_CHECKS = true;
        File coverageFile = TestData.copy(this, "geotiff/world.tiff");
        assertTrue(coverageFile.exists());
        worldReader = new GeoTiffReader(coverageFile);

        // grab also the global precipitation
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");
        URL rainURL = GridCoverageRendererTest.class
                .getResource("test-data/arcgrid/precip30min.asc");
        File rainFile = DataUtilities.urlToFile(rainURL);
        rainReader = new ArcGridReader(rainFile);
    }

    @After
    public void close() throws IOException {
        MapProjection.SKIP_SANITY_CHECKS = false;
        worldReader.dispose();
    }

	/**
	 * Returns a {@link GridCoverage} which may be used as a "real world"
	 * example.
	 * 
	 * @param number
	 *            The example number. Numbers are numeroted from 0 to
	 *            {@link #getNumExamples()} exclusive.
	 * @return The "real world" grid coverage.
	 * @throws IOException
	 *             if an I/O operation was needed and failed.
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */
	private final GridCoverage2D getGC() throws IOException,
			IllegalArgumentException, ParseException {
		final String path;
		final Rectangle2D bounds;

		// unit = "°C";
		path = "TestGridCoverage.tif";
		final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

		// 41°S - 5°N ; 35°E - 80°E (450 x 460 pixels)
		bounds = new Rectangle2D.Double(35, -41, 45, 46);
		final GeneralEnvelope envelope = new GeneralEnvelope(bounds);
		final RenderedImage image = ImageIO.read(TestData.getResource(this,path));
		final int numBands = image.getSampleModel().getNumBands();
		final GridSampleDimension[] bands = new GridSampleDimension[numBands];
		// setting bands names.
		for (int i = 0; i < numBands; i++) {

			bands[i] = new GridSampleDimension("band " + i);
		}
		final String filename = new File(path).getName();
		final GridCoverageFactory factory = org.geotools.coverage.CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
		envelope.setCoordinateReferenceSystem(crs);
		return factory.create(filename, image, envelope,bands, null, null);
	}

	/**
	 * Returns a projected CRS for test purpose.
	 */
	private static CoordinateReferenceSystem getProjectedCRS(
			final GridCoverage2D coverage) {
		try {
			final GeographicCRS base = (GeographicCRS) coverage.getCoordinateReferenceSystem();
			final Ellipsoid ellipsoid = base.getDatum().getEllipsoid();
			final DefaultMathTransformFactory factory = new DefaultMathTransformFactory();
			final ParameterValueGroup parameters = factory.getDefaultParameters("Oblique_Stereographic");
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
			//create the projected crs
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
		final MapContext context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
		final Style style = getStyle();
		context.addLayer(gc, style);

		// /////////////////////////////////////////////////////////////////
		//
		// Streaming renderer
		//
		//
		// ///////////////////////////////////////////////////////////////
		final StreamingRenderer renderer = new StreamingRenderer();
		renderer.setContext(context);
		RendererBaseTest.showRender("testGridCoverage", renderer, 1000, context.getLayerBounds());
	}
	
	/**
	 * Tests what happens when the grid coverage is associated with a broken style with
	 * no symbolizers inside. It should just render nothing, a NPE was reported instead 
	 * in GEOT-2543. 
	 * @throws Exception
	 */
	@Test
    public void paintWrongStyle() throws Exception {
        final GridCoverage2D gc = getGC();
        final MapContext context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        
        // final Style style = new StyleBuilder().createStyle((Symbolizer) null);
        final Style style = RendererBaseTest.loadStyle(this, "empty.sld");
        context.addLayer(gc, style);

        final StreamingRenderer renderer = new StreamingRenderer();
        CountingRenderListener counter = new CountingRenderListener();
        renderer.addRenderListener(counter);
        renderer.setContext(context);
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        renderer.paint(g2d, new Rectangle(0,0,300,300), context.getLayerBounds());
        g2d.dispose();
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
		final MapContext context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
		final Style style = getStyle();
		context.addLayer(coverage, style);

		// transform to a new crs
		final CoordinateReferenceSystem destCRS = getProjectedCRS(coverage);

		// ///////////////////////////////////////////////////////////////////
		//
		// Streaming renderer
		//
		// /////////////////////////////////////////////////////////////////
		final StreamingRenderer renderer = new StreamingRenderer();
		renderer.setContext(context);

		ReferencedEnvelope env = context.getLayerBounds();
		env = new ReferencedEnvelope(env.getMinX(), env.getMaxX(), env.getMinY(), env.getMaxY(), DefaultGeographicCRS.WGS84);
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

        BufferedImage image = RendererBaseTest.showRender("testGridCoverageReprojection", renderer, 1000, content.getViewport().getBounds());
        ImageAssert.assertEquals(new File("src/test/resources/org/geotools/renderer/lite/rescaled.png"), image, 1000);
    }

    @Test
    public void testInvertedColors() throws Exception {
        MapContent content = new MapContent();
        content.getViewport().setBounds(
                new ReferencedEnvelope(-179.9997834892, 180.00025801626, -89.999828389438,
                        270.00021311603, DefaultGeographicCRS.WGS84));
        RasterSymbolizer rs = buildRainColorMap();
        final Style style = new StyleBuilder().createStyle(rs);
        content.addLayer(new GridReaderLayer(rainReader, style));

        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        Map<Object, Object> rendererParams = new HashMap<Object, Object>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);

        BufferedImage image = RendererBaseTest.showRender("testGridCoverageReprojection", renderer,
                1000, content.getViewport().getBounds());
        ImageAssert.assertEquals(new File(
                "src/test/resources/org/geotools/renderer/lite/inverted.png"), image, 1000);
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
	    GridCoverage2D coverage = CoverageFactoryFinder.getGridCoverageFactory(null).create("test", bi, 
	            new ReferencedEnvelope(baseX, baseX + bi.getWidth(), baseY, baseY + bi.getHeight(), nativeCrs));
	    
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
        Layer layer = new GridReaderLayer(new GeoTiffReader(testFile), sb.createStyle(sb.createRasterSymbolizer()));
        MapContent mc = new MapContent();
        mc.getViewport().setBounds(read3857);
        mc.addLayer(layer);
        
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        BufferedImage result = RendererBaseTest.showRender("testGridCoverageBoundsReprojection", sr, 1000, read3857);
        
        // there used to be a white triangle in the lower right corner of the output
        ImageAssert.assertEquals(new File("src/test/resources/org/geotools/renderer/lite/reprojectBuffer.png"), result, 0);
	}

    @Test
    public void testReprojectGoogleMercator() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-20037508.34, 20037508.34,
                -20037508.34, 20037508.34, googleMercator);
        Rectangle screenSize = new Rectangle(200, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 200));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(googleMercator, mapExtent,
                screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        GridCoverage2D coverage = worldReader.read(null);
        RenderedImage image = renderer.renderImage(coverage, rasterSymbolizer,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST), Color.RED, 256, 256);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercator.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testReprojectGoogleMercatorLargerThanWorld() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-38448446.580832, 38448446.580832,
                -44138127.016561, 44138127.016561, googleMercator);
        Rectangle screenSize = new Rectangle(400, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(googleMercator, mapExtent,
                screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorLargerThanWorld.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testReprojectGoogleMercatorBlackLine() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-70650900.64528, 32797834.549784,
                -25517354.68145, 26207012.916082, googleMercator);
        Rectangle screenSize = new Rectangle(600, 300);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(googleMercator, mapExtent,
                screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorBlackLine.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testReprojectGoogleMercatorTouchDateline() throws Exception {
        CoordinateReferenceSystem googleMercator = CRS.decode("EPSG:3857");
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(20037508.34, 40075016.68, 0,
                20037508.34, googleMercator);
        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(googleMercator, mapExtent,
                screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();
        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/googleMercatorTouchDateline.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testAcrossDatelineBilinear() throws Exception {
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(10, 350, -90, 90,
                DefaultGeographicCRS.WGS84);
        Rectangle screenSize = new Rectangle(500, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 500));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(DefaultGeographicCRS.WGS84,
                mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/wrapDatelineNearest.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    public void testCrashOutsideValidArea() throws Exception {
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(0.00023726353151687, 180.00025801626,
                -269.99984914217, -89.999828389438, DefaultGeographicCRS.WGS84);
        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(DefaultGeographicCRS.WGS84,
                mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST), Color.RED, 256, 256);
        assertNull(image);
    }

    private void assertNull(RenderedImage image) {
        // TODO Auto-generated method stub

    }

    @Test
    public void testAcrossDatelineBicubic() throws Exception {
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(10, 350, -90, 90,
                DefaultGeographicCRS.WGS84);
        Rectangle screenSize = new Rectangle(500, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 500));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(DefaultGeographicCRS.WGS84,
                mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                Color.RED, 256, 256);
        File reference = new File(
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
        Rectangle screenSize = new Rectangle(400, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(
                mapExtent.getCoordinateReferenceSystem(),
                mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_BICUBIC),
                Color.RED, 256, 256);
        assertNotNull(image);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/utm.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testSouthPolar() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-6000000, 6000000, -8000000,
                -1000000, crs);

        Rectangle screenSize = new Rectangle(400, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(
                mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        assertNotNull(image);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/south_polar.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testNorthPolar() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3995", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-6000000, 6000000, 8000000, 1000000,
                crs);

        Rectangle screenSize = new Rectangle(400, (int) (mapExtent.getHeight()
                / mapExtent.getWidth() * 400));
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(
                mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new StyleBuilder().createRasterSymbolizer();

        RenderedImage image = renderer.renderImage(worldReader, null, rasterSymbolizer,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST), Color.RED, 256, 256);
        assertNotNull(image);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/north_polar.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testPolarCutLowCorner() throws Exception {
        // we request a small area, oversampling, and the output has some white pixels in a corner
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-2500000, -1250000, -10000000,
                -8750000, crs);

        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(
                mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRainColorMap();

        RenderedImage image = renderer.renderImage(rainReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        assertNotNull(image);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/polar_whitecorner.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    private RasterSymbolizer buildRainColorMap() {
        StyleBuilder sb = new StyleBuilder();
        ColorMap colorMap = sb
                .createColorMap(new String[] { "1", "2", "3", "4" }, new double[] { 0, 100, 2000,
                        5000 }, new Color[] { Color.RED, Color.WHITE, Color.GREEN, Color.BLUE },
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
        GridCoverageRenderer renderer = new GridCoverageRenderer(
                mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRainColorMap();

        RenderedImage image = renderer.renderImage(rainReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        assertNotNull(image);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/polar_whitecorner_up.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

    @Test
    public void testPolarTouchDateline() throws Exception {
        // we request a small area, oversampling, and the output has some white pixels in a corner
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        // across the dateline, not including the pole
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-1250000, 0, -10000000, -8750000, crs);

        Rectangle screenSize = new Rectangle(256, 256);
        AffineTransform w2s = RendererUtilities.worldToScreenTransform(mapExtent, screenSize);
        GridCoverageRenderer renderer = new GridCoverageRenderer(
                mapExtent.getCoordinateReferenceSystem(), mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = buildRainColorMap();

        RenderedImage image = renderer.renderImage(rainReader, null, rasterSymbolizer, Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                Color.RED, 256, 256);
        assertNotNull(image);
        File reference = new File(
                "src/test/resources/org/geotools/renderer/lite/gridcoverage2d/polar_touchdateline.png");
        ImageAssert.assertEquals(reference, image, 0);
    }

}
