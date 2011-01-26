/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

import javax.media.jai.PlanarImage;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.datum.PixelInCell;

/**
 * Testing {@link ImageMosaicReader}.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de 
 * @since 2.3
 * 
 */
@SuppressWarnings("deprecation")
public class ImageMosaicReaderTest{

	public static junit.framework.Test suite() { 
	    return new JUnit4TestAdapter(ImageMosaicReaderTest.class); 
	}

	private URL rgbURL;

	private URL indexURL;

	private URL indexAlphaURL;

	private URL grayURL;

	private URL index_unique_paletteAlphaURL;

	private URL rgbAURL;

	private URL overviewURL;

	private URL rgbJarURL;
	
	private URL indexJarURL;
	
	private URL indexAlphaJarURL;
	
	private URL grayJarURL;
	
	private URL index_unique_paletteAlphaJarURL;
	
	private boolean interactive;

	/**
	 * Testing crop capabilities.
	 * 
	 * @throws MismatchedDimensionException
	 * @throws IOException
	 * @throws FactoryException
	 */
	@Test
	@Ignore
	public void crop() throws MismatchedDimensionException, IOException,
			FactoryException {
		imageMosaicCropTest(rgbURL, "crop-rgbURL");
		imageMosaicCropTest(rgbJarURL, "crop-rgbJarURL");
		
		imageMosaicCropTest(indexURL, "crop-indexURL");
		imageMosaicCropTest(indexJarURL, "crop-indexJarURL");
		
		imageMosaicCropTest(grayURL, "crop-grayURL");
		imageMosaicCropTest(grayJarURL, "crop-grayJarURL");
		
		imageMosaicCropTest(overviewURL, "crop-overviewURL");
		
		imageMosaicCropTest(indexAlphaURL, "crop-indexAlphaURL");
		imageMosaicCropTest(indexAlphaJarURL, "crop-indexAlphaJarURL");
		
		imageMosaicCropTest(rgbAURL, "crop-rgbAURL");
		
		imageMosaicCropTest(index_unique_paletteAlphaURL,"crop-index_unique_paletteAlphaURL");
		imageMosaicCropTest(index_unique_paletteAlphaJarURL,"crop-index_unique_paletteAlphaJarURL");

	}

	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	@Ignore
	public void alpha() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		
		final String testName="alpha-";
		if (interactive)
			imageMosaicSimpleParamsTest(rgbURL, null, null,testName+rgbURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(rgbURL, Color.black,Color.black,testName+rgbURL.getFile(), false);

		if (interactive)
			imageMosaicSimpleParamsTest(rgbJarURL, null, null,testName+rgbJarURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(rgbJarURL, Color.black,Color.black,testName+rgbURL.getFile(), false);

		if (interactive)
			// the input images have transparency and they do overlap, we need
			// to ask for blending mosaic.
			imageMosaicSimpleParamsTest(rgbAURL, null, null,testName+rgbAURL.getFile()+"-original", true);
		imageMosaicSimpleParamsTest(rgbAURL, Color.black,Color.black,testName+rgbAURL.getFile(), false);

		// //
		//
		// This images have borders that are black and have a color model that
		// is IndexColorModel but all with different palette hence a color
		// conversion will be applied to go to RGB.
		//
		// When we do the input transparent color we will add transparency to
		// the images but only where the transparent color resides. Moreover the
		// background will be transparent.
		//
		// //
		if (interactive)
			imageMosaicSimpleParamsTest(indexURL, null, null,testName+indexURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexURL, new Color(58, 49, 8),Color.black,testName+indexURL.getFile(), false);

		if (interactive)
			imageMosaicSimpleParamsTest(indexJarURL, null, null,testName+indexJarURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexJarURL, new Color(58, 49, 8),Color.black,testName+indexJarURL.getFile(), false);
		
		
		if (interactive)
			imageMosaicSimpleParamsTest(overviewURL, null, null,testName+overviewURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(overviewURL, new Color(58, 49, 8),Color.black,testName+overviewURL.getFile()+"-indexURL", false);		
		
		
		if (interactive)
			imageMosaicSimpleParamsTest(indexAlphaURL, null, null,testName+indexAlphaURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexAlphaURL, new Color(41,41, 33), Color.black,testName+indexAlphaURL.getFile(), false);

		if (interactive)
			imageMosaicSimpleParamsTest(indexAlphaJarURL, null, null,testName+indexAlphaJarURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexAlphaJarURL, new Color(41,41, 33), Color.black,testName+indexAlphaJarURL.getFile(), false);
		
		
		if (interactive)
			imageMosaicSimpleParamsTest(grayURL, null, null,testName+grayURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(grayURL, Color.black,Color.black, testName+grayURL.getFile(), false);
		
		if (interactive)
			imageMosaicSimpleParamsTest(grayJarURL, null, null,testName+grayJarURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(grayJarURL, Color.black,Color.black, testName+grayJarURL.getFile(), false);


	}
	
	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
//	@Ignore
	public void overviews() throws IOException,	
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		final AbstractGridFormat format = getFormat(overviewURL);
		final ImageMosaicReader reader = getReader(overviewURL, format);

		// limit yourself to reading just a bit of it
		final ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		final Dimension dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		final ParameterValue<Boolean> useJai = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
		useJai.setValue(false);
		
		final ParameterValue<String> tileSize = ImageMosaicFormat.SUGGESTED_TILE_SIZE.createValue();
		tileSize.setValue("128,128");
		
		// Test the output coverage
		checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize}, "overviews test");
	}	
 
	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	@Ignore
	public void defaultParameterValue() throws IOException,	
			MismatchedDimensionException, NoSuchAuthorityCodeException {

		final String baseTestName="testDefaultParameterValue-";
		imageMosaicSimpleParamsTest(rgbURL, null, null,baseTestName+rgbURL.getFile(), false);
		imageMosaicSimpleParamsTest(rgbAURL, null,  null,baseTestName+rgbAURL.getFile(), false);
		imageMosaicSimpleParamsTest(overviewURL, null,null,baseTestName+overviewURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexURL, null, null,baseTestName+indexURL.getFile(), false);
		imageMosaicSimpleParamsTest(grayURL, null, null,baseTestName+grayURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexAlphaURL, null, null,baseTestName+indexAlphaURL.getFile(), false);

		// And again with URL that points into a JAR
		imageMosaicSimpleParamsTest(rgbJarURL, null, null,baseTestName+rgbJarURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexJarURL, null, null,baseTestName+indexJarURL.getFile(), false);
		imageMosaicSimpleParamsTest(grayJarURL, null, null,baseTestName+grayJarURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexAlphaJarURL, null, null,baseTestName+indexAlphaJarURL.getFile(), false);
	}
	
	
	@Test
	@Ignore
	public void errors() {
		////
		//
		// MOSAIC_LOCATION_ATTRIBUTE
		//
		// error for location attribute
		AbstractGridCoverage2DReader reader=null;
		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL)).getReader(rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));
			Assert.assertNull(reader);
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}
		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbJarURL)).getReader(rgbJarURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));
			Assert.assertNull(reader);
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}
		

		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL)).getReader(rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "location"));
			Assert.assertNotNull(reader);
			reader.dispose();
			Assert.assertTrue(true);
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}

		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbJarURL)).getReader(rgbJarURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "location"));
			Assert.assertNotNull(reader);
			reader.dispose();
			Assert.assertTrue(true);
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}

		////
		//
		// MAX_ALLOWED_TILES
		//
		////
		// error for num tiles
		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL)).getReader(rgbURL,new Hints(Hints.MAX_ALLOWED_TILES, Integer.valueOf(2)));
			Assert.assertNotNull(reader);
			
			//read the coverage
			@SuppressWarnings("unused")
			GridCoverage2D gc = (GridCoverage2D) reader.read(null);
			Assert.fail("MAX_ALLOWED_TILES was not respected");
		} catch (Throwable e) {

			if (reader != null) 
				reader.dispose();
			
			Assert.assertTrue(true);
		}

		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL)).getReader(rgbURL,new Hints(Hints.MAX_ALLOWED_TILES,Integer.valueOf(1000)));
			Assert.assertNotNull(reader);
			//read the coverage
			GridCoverage2D gc = (GridCoverage2D) reader.read(null);
			Assert.assertTrue(true);
			gc.dispose(true);
			reader.dispose();
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Tests the {@link ImageMosaicReader}
	 * 
	 * @param title
	 * 
	 * @param threshold
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	private void imageMosaicSimpleParamsTest(
			final URL testURL, 
			final Color inputTransparent, 
			final Color outputTransparent, 
			final String title,
			final boolean blend) throws IOException, MismatchedDimensionException,
			NoSuchAuthorityCodeException {

		// Get the resources as needed.
		Assert.assertNotNull(testURL);
		final AbstractGridFormat format = getFormat(testURL);
		final ImageMosaicReader reader = getReader(testURL, format);

		// limit yourself to reading just a bit of it
		final ParameterValue<Color> inTransp =  ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
		inTransp.setValue(inputTransparent);
		final ParameterValue<Color> outTransp =  ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
		outTransp.setValue(outputTransparent);
		final ParameterValue<Boolean> blendPV =ImageMosaicFormat.FADING.createValue();
		blendPV.setValue(blend);

		// Test the output coverage
		checkCoverage(reader, new GeneralParameterValue[] { inTransp, blendPV, outTransp }, title);
	}

	/**
	 * Tests the creation of a {@link GridCoverage2D} using the provided
	 * {@link ImageMosaicReader} as well as the provided {@link ParameterValue}.
	 * 
	 * @param reader
	 *            to use for creating a {@link GridCoverage2D}.
	 * @param value
	 *            that control the actions to take for creating a
	 *            {@link GridCoverage2D}.
	 * @param title
	 *            to print out as the head of the frame in case we visualize it.
	 * @return 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void checkCoverage(final ImageMosaicReader reader,
			GeneralParameterValue[] values, String title) throws IOException {
		// Test the coverage
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(values);
		Assert.assertNotNull(coverage);
		if (interactive)
			show( coverage.getRenderedImage(), title);
		else
			PlanarImage.wrapRenderedImage( coverage.getRenderedImage()).getTiles();;
			
		for(GeneralParameterValue pv:values){
			if(pv.getDescriptor().getName().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())){
				
				Parameter<GridGeometry2D> param= (Parameter<GridGeometry2D>) pv;
				// check envelope if it has been requested
				Assert.assertEquals(param.getValue().getEnvelope(), coverage.getEnvelope());

			}
		}
		
		if (!interactive){
			// dispose stuff
			coverage.dispose(true);
			reader.dispose();
		}
	}

	/**
	 * Shows the provided {@link RenderedImage} ina {@link JFrame} using the
	 * provided <code>title</code> as the frame's title.
	 * 
	 * @param image
	 *            to show.
	 * @param title
	 *            to use.
	 */
	static void show(RenderedImage image, String title) {
		final JFrame jf = new JFrame(title);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(new ScrollingImagePanel(image, 800, 800));
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				jf.pack();
				jf.setVisible(true);

			}
		});

	}

	/**
	 * returns an {@link AbstractGridCoverage2DReader} for the provided
	 * {@link URL} and for the providede {@link AbstractGridFormat}.
	 * 
	 * @param testURL
	 *            points to a valid object to create an
	 *            {@link AbstractGridCoverage2DReader} for.
	 * @param format
	 *            to use for instantiating such a reader.
	 * @return a suitable {@link ImageMosaicReader}.
	 */
	private ImageMosaicReader getReader(URL testURL,
			final AbstractGridFormat format) {
		return getReader(testURL, format, null);

	}

	private ImageMosaicReader getReader(URL testURL,
			final AbstractGridFormat format, Hints hints) {
//		Get a reader
		final ImageMosaicReader reader = (ImageMosaicReader) format.getReader(testURL, hints);
		Assert.assertNotNull(reader);
		return reader;
	}

	/**
	 * Tries to get an {@link AbstractGridFormat} for the provided URL.
	 * 
	 * @param testURL
	 *            points to a shapefile that is the index of a certain mosaic.
	 * @return a suitable {@link AbstractGridFormat}.
	 */
	private AbstractGridFormat getFormat(URL testURL) {

		// Get format
		final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(testURL);
		Assert.assertNotNull(format);
		Assert.assertFalse("UknownFormat", format instanceof UnknownFormat);
		return format;
	}

	/**
	 * Tests {@link ImageMosaicReader} asking to crop the lower left quarter of
	 * the input coverage.
	 * 
	 * @param title
	 *            to use when showing image.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws FactoryException
	 */
	private void imageMosaicCropTest(URL testURL, String title)
			throws IOException, MismatchedDimensionException, FactoryException {

		// Get the resources as needed.
		Assert.assertNotNull(testURL);
		final AbstractGridFormat format = getFormat(testURL);
		final ImageMosaicReader reader = getReader(testURL, format);


		// crop
		final ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				oldEnvelope.getLowerCorner().getOrdinate(0)
						+ oldEnvelope.getSpan(0) / 2,
				oldEnvelope.getLowerCorner().getOrdinate(1)
						+ oldEnvelope.getSpan(1) / 2 }, new double[] {
				oldEnvelope.getUpperCorner().getOrdinate(0),
				oldEnvelope.getUpperCorner().getOrdinate(1) });
		cropEnvelope.setCoordinateReferenceSystem(reader.getCrs());
		gg.setValue(new GridGeometry2D(PixelInCell.CELL_CENTER,reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),cropEnvelope,null));
		final ParameterValue<Color> outTransp =  ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
		outTransp.setValue(Color.black);


		// test the coverage
		checkCoverage(reader, new GeneralParameterValue[] { gg, outTransp },title);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestRunner.run(ImageMosaicReaderTest.suite());

	}

	@Before
	public void setUp() throws Exception {
		//remove generated file
		cleanUp();
		
		rgbURL = TestData.url(this, "rgb/mosaic.shp");
		rgbJarURL = new URL("jar:"+TestData.url(this, "rgb.jar").toExternalForm()+"!/rgb/mosaic.shp");
		
		overviewURL = TestData.url(this, "overview/");
		rgbAURL = TestData.url(this, "rgba/");
		
		indexURL = TestData.url(this, "index/modis.shp");
		indexJarURL = new URL("jar:"+TestData.url(this, "index.jar").toExternalForm()+"!/index/modis.shp");
		
		indexAlphaURL = TestData.url(this, "index_alpha/modis.shp");
		indexAlphaJarURL = new URL("jar:"+TestData.url(this, "index_alpha.jar").toExternalForm()+"!/index_alpha/modis.shp");
		
		grayURL = TestData.url(this, "gray/dof.shp");
		grayJarURL = new URL("jar:"+TestData.url(this, "gray.jar").toExternalForm()+"!/gray/dof.shp");
		
		index_unique_paletteAlphaURL = TestData.url(this,"index_alpha_unique_palette/dof.shp");
		index_unique_paletteAlphaJarURL = new URL("jar:"+TestData.url(this, "index_alpha_unique_palette.jar").toExternalForm()+"!/index_alpha_unique_palette/dof.shp");
		
		interactive =TestData.isInteractiveTest();

	}

	/**
	 * Cleaning up the generated files (shape and properties so that we recreate them.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void cleanUp() throws FileNotFoundException, IOException {
			if(interactive)
				return;
			File dir=TestData.file(this, "overview/");
			File[] files = dir.listFiles((FilenameFilter)FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("tif")));
			for(File file:files){
				file.delete();
			}
			
			dir=TestData.file(this, "rgba/");
			files = dir.listFiles((FilenameFilter)FileFilterUtils.notFileFilter(
					FileFilterUtils.orFileFilter(
							FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("png")),
							FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("wld"))
					)));
			for(File file:files){
				file.delete();
			}
	}
	
	@After
	public void tearDown() throws FileNotFoundException, IOException{
		cleanUp();
	}


}
