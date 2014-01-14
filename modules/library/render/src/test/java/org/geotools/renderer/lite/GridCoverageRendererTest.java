/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;

import javax.imageio.ImageIO;

import org.geotools.TestData;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.GeoTools;
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
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
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
		return (GridCoverage2D) factory.create(filename, image, envelope,bands, null, null);
	}

	/**
	 * Returns a projected CRS for test purpose.
	 */
	private static CoordinateReferenceSystem getProjectedCRS(
			final GridCoverage2D coverage) {
		try {
			final GeographicCRS base = (GeographicCRS) coverage.getCoordinateReferenceSystem();
			final Ellipsoid ellipsoid = ((GeodeticDatum) base.getDatum()).getEllipsoid();
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
	    GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));
        MapContent content = new MapContent();
        final Style style = getStyle();
        content.addLayer(new GridReaderLayer(reader, style));
        
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(content);
        renderer.setRendererHints(Collections.singletonMap("renderingBuffer", 1024));

        BufferedImage image = RendererBaseTest.showRender("testGridCoverageReprojection", renderer, 1000, content.getViewport().getBounds());
        ImageAssert.assertEquals(new File("src/test/resources/org/geotools/renderer/lite/rescaled.png"), image, 1000);
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


}
