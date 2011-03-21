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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

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
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
public class ImageMosaicReaderTest extends Assert{

    private final static Logger LOGGER = Logger.getLogger(ImageMosaicReaderTest.class.toString());
    
	public static junit.framework.Test suite() { 
	    return new JUnit4TestAdapter(ImageMosaicReaderTest.class); 
	}

	private URL rgbURL;
	
	private URL heterogeneousGranulesURL;

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

	private URL timeURL;

	private URL imposedEnvelopeURL;
	
	/**
	 * Testing crop capabilities.
	 * 
	 * @throws MismatchedDimensionException
	 * @throws IOException
	 * @throws FactoryException
	 */
	@Test
	public void crop() throws MismatchedDimensionException, IOException,
			FactoryException {
		imageMosaicCropTest(rgbURL, "crop-rgbURL");
//		imageMosaicCropTest(rgbJarURL, "crop-rgbJarURL");
		
		imageMosaicCropTest(indexURL, "crop-indexURL");
//		imageMosaicCropTest(indexJarURL, "crop-indexJarURL");
		
		imageMosaicCropTest(grayURL, "crop-grayURL");
//		imageMosaicCropTest(grayJarURL, "crop-grayJarURL");
		
		imageMosaicCropTest(overviewURL, "crop-overviewURL");
		
		imageMosaicCropTest(indexAlphaURL, "crop-indexAlphaURL");
//		imageMosaicCropTest(indexAlphaJarURL, "crop-indexAlphaJarURL");
		
		imageMosaicCropTest(rgbAURL, "crop-rgbAURL");
		
		imageMosaicCropTest(index_unique_paletteAlphaURL,"crop-index_unique_paletteAlphaURL");
//		imageMosaicCropTest(index_unique_paletteAlphaJarURL,"crop-index_unique_paletteAlphaJarURL");

	}

	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws FactoryException 
	 */
	@Test
	public void alpha() throws IOException,
			MismatchedDimensionException, FactoryException {
		
		final String testName="alpha-";
		if (interactive)
			imageMosaicSimpleParamsTest(rgbURL, null, null,testName+rgbURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(rgbURL, Color.black,Color.black,testName+rgbURL.getFile(), false);

//		if (interactive)
//			imageMosaicSimpleParamsTest(rgbJarURL, null, null,testName+rgbJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(rgbJarURL, Color.black,Color.black,testName+rgbURL.getFile(), false);

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

//		if (interactive)
//			imageMosaicSimpleParamsTest(indexJarURL, null, null,testName+indexJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(indexJarURL, new Color(58, 49, 8),Color.black,testName+indexJarURL.getFile(), false);
		
		
		if (interactive)
			imageMosaicSimpleParamsTest(overviewURL, null, null,testName+overviewURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(overviewURL, new Color(58, 49, 8),Color.black,testName+overviewURL.getFile()+"-indexURL", false);		
		
		
		if (interactive)
			imageMosaicSimpleParamsTest(indexAlphaURL, null, null,testName+indexAlphaURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexAlphaURL, new Color(41,41, 33), Color.black,testName+indexAlphaURL.getFile(), false);

//		if (interactive)
//			imageMosaicSimpleParamsTest(indexAlphaJarURL, null, null,testName+indexAlphaJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(indexAlphaJarURL, new Color(41,41, 33), Color.black,testName+indexAlphaJarURL.getFile(), false);
		
		
		if (interactive)
			imageMosaicSimpleParamsTest(grayURL, null, null,testName+grayURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(grayURL, Color.black,Color.black, testName+grayURL.getFile(), false);
		
//		if (interactive)
//			imageMosaicSimpleParamsTest(grayJarURL, null, null,testName+grayJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(grayJarURL, Color.black,Color.black, testName+grayJarURL.getFile(), false);


	}
	
	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws FactoryException 
	 */
	@Test
	public void overviews() throws IOException,	
			MismatchedDimensionException, FactoryException {
		final AbstractGridFormat format = getFormat(overviewURL);
		final ImageMosaicReader reader = getReader(overviewURL, format);

		// limit yourself to reading just a bit of it
		final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		final Dimension dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
		useJai.setValue(false);
		
		final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
		tileSize.setValue("128,128");
		
		// Test the output coverage
		checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize}, "overviews test");
	}	
	
	/**
	 * 
	 * @throws IOException
	 * @throws ParseException 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	public void timeElevation() throws IOException, ParseException, NoSuchAuthorityCodeException, FactoryException {
	        TestData.unzipFile(this, "ensmean.zip");
	        final URL timeElevURL = TestData.url(this, "ensmean");
	        System.setProperty("org.geotools.shapefile.datetime", "true");
		final AbstractGridFormat format = getFormat(timeElevURL);
		assertNotNull(format);
		ImageMosaicReader reader = getReader(timeElevURL, format);
		assertNotNull(format);
		
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length,10);
		
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
		assertNotNull(timeMetadata);
		assertEquals(16,timeMetadata.split(",").length);
		assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals(timeMetadata.split(",")[15],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		
		assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
		final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
		assertNotNull(elevationMetadata);
		assertEquals(2,elevationMetadata.split(",").length);
	        assertEquals(elevationMetadata.split(",")[0],reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
	        assertEquals(elevationMetadata.split(",")[1],reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
		
		
		// limit yourself to reading just a bit of it
		final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		final Dimension dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		
		// use imageio with defined tiles
		final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
		final List<Date> timeValues= new ArrayList<Date>();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		Date date = sdf.parse("2008-10-20T14:00:00.000Z");
		timeValues.add(date);
		time.setValue(timeValues);
		
		final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
		bkg.setValue(new double[]{-9999.0});
		
		final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
		elevation.setValue(Arrays.asList(10.0));
	                
		// Test the output coverage
		checkCoverage(reader, new GeneralParameterValue[] {gg,time,bkg ,elevation}, "Time-Elevation Test");
		
		reader= getReader(timeElevURL, format);
                elevation.setValue(Arrays.asList(NumberRange.create(0.0,10.0)));
        
                // Test the output coverage
                checkCoverage(reader, new GeneralParameterValue[] { gg, time, bkg, elevation },
                        "Time-Elevation Test");
	}	
	
	
	@Test
	public void imposedBBox() throws IOException, NoSuchAuthorityCodeException, FactoryException {
		final AbstractGridFormat format = getFormat(imposedEnvelopeURL);
		final ImageMosaicReader reader = getReader(imposedEnvelopeURL, format);
	
		
		//check envelope
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		assertNotNull(envelope);
		
		assertEquals(envelope.getMinimum(0), -180.0,1E-6);
		assertEquals(envelope.getMinimum(1), -90.0,1E-6);
		assertEquals(envelope.getMaximum(0), 180.0,1E-6);
		assertEquals(envelope.getMaximum(1), 90.0,1E-6);
		
		// limit yourself to reading just a bit of it
		final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
		final Dimension dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/3.0, reader.getOriginalGridRange().getSpan(1)/3.0);
		final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
		useJai.setValue(false);
		
		
		// Test the output coverage
		checkCoverage(reader, new GeneralParameterValue[] {gg,useJai }, "Imposed BBox");
	}	
	
	/**
	 * 
	 * @throws IOException
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 * @throws ParseException 
	 */
	@Test
	public void time() throws IOException, NoSuchAuthorityCodeException, FactoryException, ParseException {
	        System.setProperty("org.geotools.shapefile.datetime", "true");
		final AbstractGridFormat format = getFormat(timeURL);
		ImageMosaicReader reader = getReader(timeURL, format);
		
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length,10);
		assertEquals("2004-01-01T00:00:00.000Z,2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z,2004-06-01T00:00:00.000Z,2004-07-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		assertEquals("2004-01-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals("2004-07-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		// limit yourself to reading just a bit of it
		final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		final Dimension dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
		useJai.setValue(false);
		final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
		tileSize.setValue("128,128");
		
		// specify time
		final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
		
		final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
		final Date timeD=formatD.parse("2004-01-01T00:00:00.000Z");
		time.setValue(new ArrayList(){{add(timeD);}});
		
		// Test the output coverage
		checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize,time}, "time test");
		
		// specify time range
		// Test the output coverage
		reader = getReader(timeURL, format);
                time.setValue(
                        new ArrayList(){{
                            add(new DateRange(formatD.parse("2004-01-01T00:00:00.000Z"), formatD.parse("2004-02-01T00:00:00.000Z")));
                            }}
                );		
                checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize,time}, "time test");
		
	}	
	
    /**
     * Tests the {@link ImageMosaicReader} with support for different
     * resolutions/different number of overviews.
     * 
     * world_a.tif => Pixel Size = (0.833333333333333,-0.833333333333333); 4
     * overviews world_b1.tif => Pixel Size =
     * (1.406250000000000,-1.406250000000000); 2 overviews world_b2.tif => Pixel
     * Size = (0.666666666666667,-0.666666666666667); 0 overviews
     * 
     * @throws IOException
     * @throws MismatchedDimensionException
     * @throws FactoryException
     */
    @Test
    public void testHeterogeneousGranules() throws IOException,
            MismatchedDimensionException, FactoryException {

        final AbstractGridFormat format = getFormat(heterogeneousGranulesURL);
        ImageMosaicReader reader = getReader(heterogeneousGranulesURL, format);

        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(reader.getOriginalGridRange().getSpan(0) / 7.0, reader.getOriginalGridRange().getSpan(1) / 7.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        final ParameterValue<OverviewPolicy> op = AbstractGridFormat.OVERVIEW_POLICY.createValue();

        LOGGER.info("\nTesting with OverviewPolicy = QUALITY");
        op.setValue(OverviewPolicy.QUALITY);
        checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=QUALITY", rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = SPEED");
        reader = getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.SPEED);
        checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=SPEED", rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = NEAREST");
        reader = getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.NEAREST);
        checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=NEAREST", rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = IGNORE");
        reader = getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.IGNORE);
        checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=IGNORE", rasterArea);

    }
	
	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws FactoryException 
	 */
	@Test
	public void defaultParameterValue() throws IOException,	
			MismatchedDimensionException, FactoryException {

		final String baseTestName="testDefaultParameterValue-";
		imageMosaicSimpleParamsTest(rgbURL, null, null,baseTestName+rgbURL.getFile(), false);
		imageMosaicSimpleParamsTest(rgbAURL, null,  null,baseTestName+rgbAURL.getFile(), false);
		imageMosaicSimpleParamsTest(overviewURL, null,null,baseTestName+overviewURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexURL, null, null,baseTestName+indexURL.getFile(), false);
		imageMosaicSimpleParamsTest(grayURL, null, null,baseTestName+grayURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexAlphaURL, null, null,baseTestName+indexAlphaURL.getFile(), false);
//
//		// And again with URL that points into a JAR
//		imageMosaicSimpleParamsTest(rgbJarURL, null, null,baseTestName+rgbJarURL.getFile(), false);
//		imageMosaicSimpleParamsTest(indexJarURL, null, null,baseTestName+indexJarURL.getFile(), false);
//		imageMosaicSimpleParamsTest(grayJarURL, null, null,baseTestName+grayJarURL.getFile(), false);
//		imageMosaicSimpleParamsTest(indexAlphaJarURL, null, null,baseTestName+indexAlphaJarURL.getFile(), false);
	}
	
	
	@Test
	public void errors() throws NoSuchAuthorityCodeException, FactoryException {
		final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
		
		////
		//
		// MOSAIC_LOCATION_ATTRIBUTE
		//
		// error for location attribute
		AbstractGridCoverage2DReader reader=null;
		try {
		    LOGGER.info("Testing Invalid location attribute. (A DataSourceException should be catched) ");
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL,hints))
					.getReader(rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));
			Assert.assertNull(reader);
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}
//		try {
//			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbJarURL)).getReader(rgbJarURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));
//			Assert.assertNull(reader);
//		} catch (Throwable e) {
//			Assert.fail(e.getLocalizedMessage());
//		}
		

		try {
			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL,hints)).getReader(rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "location"));
			Assert.assertNotNull(reader);
			reader.dispose();
			Assert.assertTrue(true);
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}

//		try {
//			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat) GridFormatFinder.findFormat(rgbJarURL)).getReader(rgbJarURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "location"));
//			Assert.assertNotNull(reader);
//			reader.dispose();
//			Assert.assertTrue(true);
//		} catch (Throwable e) {
//			Assert.fail(e.getLocalizedMessage());
//		}

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
	 * @throws FactoryException 
	 */
	private void imageMosaicSimpleParamsTest(
			final URL testURL, 
			final Color inputTransparent, 
			final Color outputTransparent, 
			final String title,
			final boolean blend) throws IOException, MismatchedDimensionException,
			FactoryException {

		// Get the resources as needed.
		Assert.assertNotNull(testURL);
		final AbstractGridFormat format = getFormat(testURL);
		final ImageMosaicReader reader = getReader(testURL, format);

		// limit yourself to reading just a bit of it
		final ParameterValue<Color> inTransp =  AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
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
	private void checkCoverage(final ImageMosaicReader reader,
                GeneralParameterValue[] values, String title) throws IOException {
	    checkCoverage(reader, values, title, null);
	}
	
	private void checkCoverage(final ImageMosaicReader reader,
			GeneralParameterValue[] values, String title, Rectangle rect) throws IOException {
		// Test the coverage
		final GridCoverage2D coverage = getCoverage(reader, values);
		testCoverage(reader, values, title, coverage, rect);
	}

	
	@SuppressWarnings("unchecked")
	private void testCoverage(final ImageMosaicReader reader,
			GeneralParameterValue[] values, String title,
			final GridCoverage2D coverage, final Rectangle rect) {
	    final RenderedImage image = coverage.getRenderedImage(); 
		if (interactive)
			show(image, title);
		else
			PlanarImage.wrapRenderedImage(image).getTiles();
		
		if(values!=null)	
			for(GeneralParameterValue pv:values){
				if(pv.getDescriptor().getName().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())){
					
					Parameter<GridGeometry2D> param= (Parameter<GridGeometry2D>) pv;
					// check envelope if it has been requested
					assertTrue(CRS.equalsIgnoreMetadata(
							param.getValue().getEnvelope().getCoordinateReferenceSystem(), 
							coverage.getCoordinateReferenceSystem()));
					
//					// I check the envelope for this equality so that I can take into account CRS that differs only in metadata
//					// but that essentially are the same thing
//					assertTrue(
//							
//							XRectangle2D.equalsEpsilon(
//									((GeneralEnvelope)coverage.getEnvelope()).toRectangle2D(), 
//									((GeneralEnvelope)param.getValue().getEnvelope()).toRectangle2D()
//							)
//					);
	
				}
			}
		if (rect != null){
		    assertEquals(image.getWidth(), rect.width); 
		    assertEquals(image.getHeight(), rect.height);
		}
		
		if (!interactive){
			// dispose stuff
			coverage.dispose(true);
			reader.dispose();
		}
	}

	private GridCoverage2D getCoverage(final ImageMosaicReader reader,
			GeneralParameterValue[] values) throws IOException {
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(values);
		Assert.assertNotNull(coverage);
		return coverage;
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
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private ImageMosaicReader getReader(URL testURL,
			final AbstractGridFormat format) throws NoSuchAuthorityCodeException, FactoryException {

		final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
		return getReader(testURL, format, hints);

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
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private AbstractGridFormat getFormat(URL testURL) throws NoSuchAuthorityCodeException, FactoryException {

		final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
		// Get format
		final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(testURL,hints);
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
		final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
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
		
		rgbURL = TestData.url(this, "rgb");
		heterogeneousGranulesURL = TestData.url(this, "heterogeneous");
		timeURL = TestData.url(this, "time_geotiff");
		rgbJarURL = new URL("jar:"+TestData.url(this, "rgb.jar").toExternalForm()+"!/rgb/mosaic.shp");
		
		overviewURL = TestData.url(this, "overview/");
		rgbAURL = TestData.url(this, "rgba/");
		
		indexURL = TestData.url(this, "index/");
		indexJarURL = new URL("jar:"+TestData.url(this, "index.jar").toExternalForm()+"!/index/modis.shp");
		
		indexAlphaURL = TestData.url(this, "index_alpha/");
		indexAlphaJarURL = new URL("jar:"+TestData.url(this, "index_alpha.jar").toExternalForm()+"!/index_alpha/modis.shp");
		
		grayURL = TestData.url(this, "gray/");
		grayJarURL = new URL("jar:"+TestData.url(this, "gray.jar").toExternalForm()+"!/gray/dof.shp");
		
		index_unique_paletteAlphaURL = TestData.url(this,"index_alpha_unique_palette/");
		index_unique_paletteAlphaJarURL = new URL("jar:"+TestData.url(this, "index_alpha_unique_palette.jar").toExternalForm()+"!/index_alpha_unique_palette/dof.shp");
		
		imposedEnvelopeURL=TestData.url(this,"env");
		
		interactive = TestData.isInteractiveTest();

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
			File[] files = dir.listFiles(
					(FilenameFilter)FileFilterUtils.notFileFilter(
							FileFilterUtils.orFileFilter(
									FileFilterUtils.orFileFilter(
											FileFilterUtils.suffixFileFilter("tif"),
											FileFilterUtils.suffixFileFilter("aux")
									),
									FileFilterUtils.nameFileFilter("datastore.properties")
							)
					)
			);
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
