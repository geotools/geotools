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
package org.geotools.gce.imagemosaic;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageReadParam;
import javax.imageio.spi.ImageReaderSpi;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.GranuleDescriptor.GranuleOverviewLevelDescriptor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageLayout2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.NoninvertibleTransformException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Testing {@link GranuleDescriptor} class.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 *
 *
 * @source $URL$
 */
public class GranuleTest extends Assert {
    

	private static final double DELTA = 10E-6;
	
	private static final double DELTASCALE = 10E-2;

	private static CoordinateReferenceSystem WGS84;
	static{
	    try{
	        WGS84 = CRS.decode("EPSG:4326",true);
	    } catch (FactoryException fe){
	        WGS84 = DefaultGeographicCRS.WGS84;
	    }
	}
	
	private static final ReferencedEnvelope TEST_BBOX = new ReferencedEnvelope(12.139578206197234,15.036279855058655,40.5313698832181,42.5511689138571,WGS84);

	private static final ImageReaderSpi spi = new TIFFImageReaderSpi();
	
	public GranuleTest() {
	}
	
	@Test
	public void testGranuleLevels() throws FileNotFoundException, IOException {
		
		//get some test data
		final URL testUrl= TestData.url(this, "/overview/0/D220161A.tif");
		testUrl.openStream().close();
		
		//Create a GranuleDescriptor
		final GranuleDescriptor granuleDescriptor = new GranuleDescriptor(DataUtilities.urlToFile(testUrl).getAbsolutePath()
		        , TEST_BBOX, spi, (Geometry) null);
		assertNotNull(granuleDescriptor.toString());
		
		//Get a GranuleOverviewLevelDescriptor
		final GranuleOverviewLevelDescriptor granuleOverviewLevelDescriptor = granuleDescriptor.getLevel(2);
		assertNotNull(granuleOverviewLevelDescriptor);
		
		final int h = granuleOverviewLevelDescriptor.getHeight();
		final int w = granuleOverviewLevelDescriptor.getWidth();
		assertEquals(47, h);
		assertEquals(35, w);
		
		final double scaleX = granuleOverviewLevelDescriptor.getScaleX();
		final double scaleY = granuleOverviewLevelDescriptor.getScaleY();
		assertEquals("ScaleX not equal", scaleX, 4.0d, DELTASCALE);
		assertEquals("ScaleY not equal", scaleY, 3.9788d, DELTASCALE);
		
		final Rectangle rect = granuleOverviewLevelDescriptor.getBounds();
		assertEquals(rect.x, 0);
		assertEquals(rect.y, 0);
		assertEquals(rect.width, 35);
		assertEquals(rect.height, 47);
		
		final AffineTransform btlTransform = granuleOverviewLevelDescriptor.getBaseToLevelTransform();
		final double[] baseMatrix = new double[6];
		btlTransform.getMatrix(baseMatrix);
		assertEquals("m00 not equal", baseMatrix[0], 4.0d, DELTASCALE);
		assertEquals("m10 not equal", baseMatrix[1], 0.0d, DELTA);
		assertEquals("m01 not equal", baseMatrix[2], 0.0d, DELTA);
		assertEquals("m11 not equal", baseMatrix[3], 3.9788d, DELTASCALE);
		assertEquals("m02 not equal", baseMatrix[4], 0.0d, DELTA);
		assertEquals("m12 not equal", baseMatrix[5], 0.0d, DELTA);
		
		final AffineTransform2D g2wtTransform = granuleOverviewLevelDescriptor.getGridToWorldTransform();
		final double[] g2wMatrix = new double[6];
		g2wtTransform.getMatrix(g2wMatrix);
		assertEquals("m00 not equal", g2wMatrix[0], 0.08276290425318347d, DELTASCALE);
		assertEquals("m10 not equal", g2wMatrix[1], 0.0d, DELTA);
		assertEquals("m01 not equal", g2wMatrix[2], 0.0d, DELTA);
		assertEquals("m11 not equal", g2wMatrix[3], -0.04297444746040424d, DELTASCALE);
		assertEquals("m02 not equal", g2wMatrix[4], 12.139578206197234d, DELTA);
		assertEquals("m12 not equal", g2wMatrix[5], 42.5511689138571d, DELTA);
	}
	
	@Test
	public void testLoadRaster() throws FileNotFoundException, IOException, NoninvertibleTransformException {
		
		//get some test data
		final File testMosaic = TestData.file(this, "/rgb");
		assertTrue(testMosaic.exists());
		
		final URL testUrl= TestData.url(this, "/rgb/global_mosaic_12.png");
		testUrl.openStream().close();
		
		final GranuleDescriptor granuleDescriptor = new GranuleDescriptor(DataUtilities.urlToFile(testUrl).getAbsolutePath()
                        , TEST_BBOX, spi, (Geometry) null);
		final GranuleOverviewLevelDescriptor granuleOverviewLevelDescriptor = granuleDescriptor.getLevel(0);
		assertNotNull(granuleOverviewLevelDescriptor);
		
		final Hints crsHints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);	
		final ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicFormat().getReader(testMosaic,crsHints);
		assertNotNull(reader);
		final RasterManager manager = new RasterManager(reader);
		
		// use imageio with defined tiles
		final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
		useJai.setValue(false);
		
		final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
		tileSize.setValue("10,10");
		
		// Creating a request
		final RasterLayerRequest request = new RasterLayerRequest(new GeneralParameterValue[] {useJai ,tileSize},manager);
		
		final ImageReadParam readParameters = new ImageReadParam();
		readParameters.setSourceRegion(new Rectangle(0,0,50,50));
		
		final AffineTransform2D gridToWorldTransform = granuleOverviewLevelDescriptor.getGridToWorldTransform();
		ImageLayout layout = new ImageLayout2().setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(10).setTileWidth(10);
		RenderingHints rHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
		Hints hints = new Hints(rHints);
		final RenderedImage raster = granuleDescriptor.loadRaster(readParameters, 0, TEST_BBOX, gridToWorldTransform.inverse(), 
		        request, hints).getRaster();
		assertEquals(raster.getWidth(), 50);
		assertEquals(raster.getHeight(), 50);
		
		AffineTransform translate = new AffineTransform(gridToWorldTransform);
		translate.preConcatenate(AffineTransform.getTranslateInstance(2, 2));
		
		final RenderedImage translatedRaster = granuleDescriptor.loadRaster(readParameters, 0, TEST_BBOX, new AffineTransform2D(translate).inverse(), request, hints).getRaster();
		assertEquals(translatedRaster.getWidth(), 50);
		assertEquals(translatedRaster.getHeight(), 50);
	}
	
}
