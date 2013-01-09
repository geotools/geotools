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
import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
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
 *
 *
 * @source $URL$
 */
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

	static boolean INTERACTIVE;

	private URL timeURL;

	private URL timeAdditionalDomainsURL;

	private URL imposedEnvelopeURL;
	
	/**
	 * Testing crop capabilities.
	 * 
	 * @throws MismatchedDimensionException
	 * @throws IOException
	 * @throws FactoryException
	 */
	@Test
//        @Ignore	
	public void crop() throws MismatchedDimensionException, IOException,
			FactoryException {
		imageMosaicCropTest(rgbURL, "crop-rgbURL");
		
		imageMosaicCropTest(indexURL, "crop-indexURL");
		
		imageMosaicCropTest(grayURL, "crop-grayURL");
		
		imageMosaicCropTest(overviewURL, "crop-overviewURL");
		
		imageMosaicCropTest(indexAlphaURL, "crop-indexAlphaURL");
		
		imageMosaicCropTest(rgbAURL, "crop-rgbAURL");
		
		imageMosaicCropTest(index_unique_paletteAlphaURL,"crop-index_unique_paletteAlphaURL");

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
        @Ignore	
	public void alpha() throws IOException,
			MismatchedDimensionException, FactoryException {
		
		final String testName="alpha-";
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(rgbURL, null, null,testName+rgbURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(rgbURL, Color.black,Color.black,testName+rgbURL.getFile(), false);

//		if (INTERACTIVE)
//			imageMosaicSimpleParamsTest(rgbJarURL, null, null,testName+rgbJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(rgbJarURL, Color.black,Color.black,testName+rgbURL.getFile(), false);

		if (INTERACTIVE)
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
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(indexURL, null, null,testName+indexURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexURL, new Color(58, 49, 8),Color.black,testName+indexURL.getFile(), false);

//		if (INTERACTIVE)
//			imageMosaicSimpleParamsTest(indexJarURL, null, null,testName+indexJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(indexJarURL, new Color(58, 49, 8),Color.black,testName+indexJarURL.getFile(), false);
		
		
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(overviewURL, null, null,testName+overviewURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(overviewURL, new Color(58, 49, 8),Color.black,testName+overviewURL.getFile()+"-indexURL", false);		
		
		
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(indexAlphaURL, null, null,testName+indexAlphaURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(indexAlphaURL, new Color(41,41, 33), Color.black,testName+indexAlphaURL.getFile(), false);

//		if (INTERACTIVE)
//			imageMosaicSimpleParamsTest(indexAlphaJarURL, null, null,testName+indexAlphaJarURL.getFile()+"-original", false);
//		imageMosaicSimpleParamsTest(indexAlphaJarURL, new Color(41,41, 33), Color.black,testName+indexAlphaJarURL.getFile(), false);
		
		
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(grayURL, null, null,testName+grayURL.getFile()+"-original", false);
		imageMosaicSimpleParamsTest(grayURL, Color.black,Color.black, testName+grayURL.getFile(), false);
		
//		if (INTERACTIVE)
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
//	@Ignore
	public void overviews() throws IOException,	
			MismatchedDimensionException, FactoryException {
		final AbstractGridFormat format = TestUtils.getFormat(overviewURL);
		final ImageMosaicReader reader = TestUtils.getReader(overviewURL, format);

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
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize}, "overviews test");
	}	
	
	@Test
	public void timeElevationH2() throws Exception {
	    
    	final File workDir=new File(TestData.file(this, "."),"water temp3");
    	workDir.mkdir();
    	assertTrue(workDir.exists());
    	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
    	TestData.unzipFile(this, "water temp3/watertemp.zip");
	    final URL timeElevURL = TestData.url(this, "water temp3");
	    
	    //place H2 file in the dir
	    FileWriter out=null;
	    try{
	    	out = new FileWriter(new File(TestData.file(this, "."),"/water temp3/datastore.properties"));
	    	out.write("SPI=org.geotools.data.h2.H2DataStoreFactory\n");
	    	out.write("database=imagemosaic\n");
	    	out.write("dbtype=h2\n");
	    	out.write("Loose\\ bbox=true #important for performances\n");
	    	out.write("Estimated\\ extends=false #important for performances\n");
	    	out.write("user=geosolutions\n");
	    	out.write("passwd=fucktheworld\n");
	    	out.write("validate \\connections=true #important for avoiding errors\n");
	    	out.write("Connection\\ timeout=3600\n");
	    	out.write("max \\connections=10 #important for performances, internal pooling\n");
	    	out.write("min \\connections=5  #important for performances, internal pooling\n");
	    	out.flush();
	    } finally {
	    	if(out!=null){
	    		IOUtils.closeQuietly(out);
	    	}
	    }
	    
	    
	    // now start the test
		final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
		assertNotNull(format);
		ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
		assertNotNull(reader);
		
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length,10);
		
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
		assertNotNull(timeMetadata);
		assertEquals(2,timeMetadata.split(",").length);
		assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals(timeMetadata.split(",")[1],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		
		assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
		final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
		assertNotNull(elevationMetadata);
		assertEquals(2,elevationMetadata.split(",").length);
	        assertEquals(Double.parseDouble(elevationMetadata.split(",")[0]),Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM")),1E-6);
	        assertEquals(Double.parseDouble(elevationMetadata.split(",")[1]),Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM")),1E-6);
		
		
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
		Date date = sdf.parse("2008-10-31T00:00:00.000Z");
		timeValues.add(date);
		time.setValue(timeValues);
		
		final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
		bkg.setValue(new double[]{-9999.0});
		
		
		final ParameterValue<Boolean> direct= ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
		direct.setValue(false);
		
		final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
		elevation.setValue(Arrays.asList(100.0));
	                
		// Test the output coverage
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,time,bkg ,elevation ,direct}, "Time-Elevation Test");
		
		reader= TestUtils.getReader(timeElevURL, format);
                elevation.setValue(Arrays.asList(NumberRange.create(0.0,10.0)));
        
                // Test the output coverage
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, time, bkg, elevation,direct },
                        "Time-Elevation Test");
                
         // clean up
         if (!INTERACTIVE){        	
         	FileUtils.deleteDirectory( TestData.file(this, "water temp3"));
         }
	}	

	@Test
//	@Ignore
	public void timeElevation() throws IOException, ParseException, NoSuchAuthorityCodeException, FactoryException {
    	final File workDir=new File(TestData.file(this, "."),"watertemp2");
    	assertTrue(workDir.mkdir());
    	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
    	TestData.unzipFile(this, "watertemp2/watertemp.zip");
    	
	    final URL timeElevURL = TestData.url(this, "watertemp2");
	    
		final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
		assertNotNull(format);
		ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
		assertNotNull(format);
		
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length,10);
		
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
		assertNotNull(timeMetadata);
		assertEquals(2,timeMetadata.split(",").length);
		assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals(timeMetadata.split(",")[1],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		
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
		Date date = sdf.parse("2008-11-01T00:00:00.000Z");
		timeValues.add(date);
		time.setValue(timeValues);
		
		final ParameterValue<Boolean> direct= ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
		direct.setValue(false);
		
		final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
		bkg.setValue(new double[]{-9999.0});
		
		final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
		elevation.setValue(Arrays.asList(0.0));
	                
		// Test the output coverage
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,time,bkg ,elevation,direct}, "Time-Elevation Test");
		
		
		reader= TestUtils.getReader(timeElevURL, format);
        elevation.setValue(Arrays.asList(NumberRange.create(0.0,10.0)));
        
                // Test the output coverage
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, time, bkg, elevation,direct },
                        "Time-Elevation Test");
                
        // clean up
                if (!INTERACTIVE){
                 	FileUtils.deleteDirectory( TestData.file(this, "watertemp2"));
                 }
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
//        @Ignore
        public void timeDoubleElevation() throws IOException, ParseException, NoSuchAuthorityCodeException, FactoryException {
                // Check we can have an integer elevation too 
        	final File workDir=new File(TestData.file(this, "."),"watertemp1");
        	assertTrue(workDir.mkdir());
        	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
        		
                TestData.unzipFile(this, "watertemp1/watertemp.zip");
                final URL timeElevURL = TestData.url(this, "watertemp1");
        	    //place H2 file in the dir
        	    FileWriter out=null;
        	    try{
        	    	out = new FileWriter(new File(TestData.file(this, "."),"/watertemp1/indexer.properties"));
        	    	out.write("TimeAttribute=ingestion\n");
        	    	out.write("ElevationAttribute=elevation\n");
        	    	out.write("Schema=*the_geom:Polygon,location:String,ingestion:java.util.Date,elevation:Double\n");
        	    	out.write("PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](ingestion),DoubleFileNameExtractorSPI[elevationregex](elevation)\n");
        	    	out.flush();
        	    } finally {
        	    	if(out!=null){
        	    		IOUtils.closeQuietly(out);
        	    	}
        	    }
        	    
        	    
                final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
                assertNotNull(format);
                ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
                assertNotNull(format);
                
                final String[] metadataNames = reader.getMetadataNames();
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length,10);
                
                assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
                assertNotNull(timeMetadata);
                assertEquals(2,timeMetadata.split(",").length);
                assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
                assertEquals(timeMetadata.split(",")[1],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
                
                assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals(2,elevationMetadata.split(",").length);
                assertEquals("0.0",reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
                assertEquals("100.0",reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
                
                
                
                // clean up
                reader.dispose();
                if (!INTERACTIVE){
                 	FileUtils.deleteDirectory( TestData.file(this, "watertemp1"));
                }
        }
	
	@Test
//    @Ignore	
	public void imposedBBox() throws IOException, NoSuchAuthorityCodeException, FactoryException {
		final AbstractGridFormat format = TestUtils.getFormat(imposedEnvelopeURL);
		final ImageMosaicReader reader = TestUtils.getReader(imposedEnvelopeURL, format);
	
		
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
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,useJai }, "Imposed BBox");
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
//	@Ignore
	public void time() throws IOException, NoSuchAuthorityCodeException, FactoryException, ParseException {
	       
		final AbstractGridFormat format = TestUtils.getFormat(timeURL);
		ImageMosaicReader reader = TestUtils.getReader(timeURL, format);
	    
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length,10);
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals("2004-05-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		assertEquals("2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));		

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
		final Date timeD=formatD.parse("2004-02-01T00:00:00.000Z");
		time.setValue(new ArrayList(){{add(timeD);}});
		
		// Test the output coverage
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize,time}, "time test");
		
		// specify time range
		// Test the output coverage
		reader = TestUtils.getReader(timeURL, format);
        time.setValue(
                new ArrayList(){{
                    add(new DateRange(formatD.parse("2004-02-01T00:00:00.000Z"), formatD.parse("2004-03-01T00:00:00.000Z")));
                    }}
        );		
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,tileSize,time}, "time test");
		
	}	
	
    /**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     * @throws IOException
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     * @throws ParseException +
     */
    @Test
    // @Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDim() throws IOException,
            NoSuchAuthorityCodeException, FactoryException, ParseException {
    
        final AbstractGridFormat format = TestUtils
                .getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsURL,
                format);
    
        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 14);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000",
                reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("true", reader.getMetadataValue("HAS_WAVELENGTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("020,100", reader.getMetadataValue("WAVELENGTH_DOMAIN"));
    
        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD
                .createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE
                .createValue();
        tileSize.setValue("128,128");
    
        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2008-10-31T00:00:00.000Z");
        time.setValue(new ArrayList() {
            {
                add(timeD);
            }
        });
    
        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> waveLengthValue = null;
        final String selectedWaveLength = "020";
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("date")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } else if (param.getName().getCode().equalsIgnoreCase("wavelength")) {
                waveLengthValue = param.createValue();
                waveLengthValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }
        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai,
                tileSize, time, dateValue, waveLengthValue };
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, true);
        final String fileSource = (String) coverage
                .getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
    
        // Check the proper granule has been read
        final String baseName = FilenameUtils.getBaseName(fileSource);
        assertEquals(baseName, "NCOM_wattemp_" + selectedWaveLength + "_"
                + selectedDate + "_12");
        TestUtils.testCoverage(reader, values, "domain test", coverage, null);
    }

    /**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     * @throws IOException
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     * @throws ParseException +
     */
    @Test
    // @Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDimNoResultsDueToWrongDim() throws IOException,
            NoSuchAuthorityCodeException, FactoryException, ParseException {
    
        final AbstractGridFormat format = TestUtils
                .getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsURL,
                format);
    
        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 14);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000",
                reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("true", reader.getMetadataValue("HAS_WAVELENGTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("020,100", reader.getMetadataValue("WAVELENGTH_DOMAIN"));
    
        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD
                .createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE
                .createValue();
        tileSize.setValue("128,128");
    
        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2008-10-31T00:00:00.000Z");
        time.setValue(new ArrayList() {
            {
                add(timeD);
            }
        });
    
        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> waveLengthValue = null;
        final String selectedWaveLength = "030";
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("date")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } else if (param.getName().getCode().equalsIgnoreCase("wavelength")) {
                waveLengthValue = param.createValue();
                waveLengthValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }
        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai,
                tileSize, time, dateValue, waveLengthValue };
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNull(coverage);
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
//    @Ignore
    public void testHeterogeneousGranules() throws IOException,
            MismatchedDimensionException, FactoryException {

        final AbstractGridFormat format = TestUtils.getFormat(heterogeneousGranulesURL);
        ImageMosaicReader reader = TestUtils.getReader(heterogeneousGranulesURL, format);

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
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=QUALITY", rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = SPEED");
        reader = TestUtils.getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.SPEED);
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=SPEED", rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = NEAREST");
        reader = TestUtils.getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.NEAREST);
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
                "heterogeneous granules test: OverviewPolicy=NEAREST", rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = IGNORE");
        reader = TestUtils.getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.IGNORE);
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, useJai, op },
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
//  @Ignore	
	public void defaultParameterValue() throws IOException,	
			MismatchedDimensionException, FactoryException {

		final String baseTestName="testDefaultParameterValue";
		imageMosaicSimpleParamsTest(rgbURL, null, null,baseTestName+rgbURL.getFile(), false);
		imageMosaicSimpleParamsTest(rgbAURL, null,  null,baseTestName+rgbAURL.getFile(), false);
		imageMosaicSimpleParamsTest(overviewURL, null,null,baseTestName+overviewURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexURL, null, null,baseTestName+indexURL.getFile(), false);
		imageMosaicSimpleParamsTest(grayURL, null, null,baseTestName+grayURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexAlphaURL, null, null,baseTestName+indexAlphaURL.getFile(), false);		
	}
	
	
	@Test
//    @Ignore	
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
		final AbstractGridFormat format = TestUtils.getFormat(testURL);
		final ImageMosaicReader reader = TestUtils.getReader(testURL, format);

		// limit yourself to reading just a bit of it
		final ParameterValue<Color> inTransp =  AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
		inTransp.setValue(inputTransparent);
		final ParameterValue<Color> outTransp =  ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
		outTransp.setValue(outputTransparent);
		final ParameterValue<Boolean> blendPV =ImageMosaicFormat.FADING.createValue();
		blendPV.setValue(blend);

		// Test the output coverage
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] { inTransp, blendPV, outTransp }, title);
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
		final AbstractGridFormat format = TestUtils.getFormat(testURL);
		final ImageMosaicReader reader = TestUtils.getReader(testURL, format);


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
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, outTransp },title);

	}
	
    @Test
//    @Ignore
    public void testRequestInHole() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbAURL);
        final ImageMosaicReader reader = TestUtils.getReader(rgbAURL, format);

        assertNotNull(reader);

        // ask to extract an area that is inside the coverage bbox, but in a hole (no data)
        final ParameterValue<GridGeometry2D> ggp =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCrs(), 500000, 3200000, 1000, 1000);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        ggp.setValue(gg);

        // red background
        final ParameterValue<double[]> bgp =  ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bgp.setValue(new double[] {255, 0, 0, 255});
        
        // read and check we actually got a coverage in the requested area
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, bgp});
        assertNotNull(coverage);
        assertTrue(coverage.getEnvelope2D().intersects((Rectangle2D) env));
        
        // and that the color is the expected one given the background values provided
        RenderedImage ri = coverage.getRenderedImage();
        int[] pixel = new int[4];
        Raster tile = ri.getTile(ri.getMinTileX() + 1, ri.getMinTileY() + 1);
        tile.getPixel(tile.getMinX(), tile.getMinY(), pixel);
        assertEquals(255, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(255, pixel[3]);
    }
    
    @Test
//    @Ignore
    public void testRequestInOut() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbAURL);
        final ImageMosaicReader reader = TestUtils.getReader(rgbAURL, format);

        assertNotNull(reader);

        // ask to extract an area that is inside the coverage bbox, so that the area is partly
        // inside the raster, and partly outside
        final ParameterValue<GridGeometry2D> ggp =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCrs(), 64887, 2499342, 646897 - 64887 , 3155705 - 2499342);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        ggp.setValue(gg);

        // red background
        final ParameterValue<double[]> bgp =  ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bgp.setValue(new double[] {255, 0, 0, 255});
        
        // read and check we actually got a coverage in the requested area
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, bgp});
        assertNotNull(coverage);
        System.out.println(coverage.getEnvelope2D());
        System.out.println(env);
        assertTrue(coverage.getEnvelope2D().contains((Rectangle2D) env));
        
        // and that the color is the expected one given the background values provided
        RenderedImage ri = coverage.getRenderedImage();
        // ImageIO.write(ri, "PNG", new File("/tmp/mix.png"));
        System.out.println(ri.getNumXTiles());
        System.out.println(ri.getNumYTiles());
        int[] pixel = new int[4];
        Raster tile = ri.getTile(ri.getMinTileX() + ri.getNumXTiles()  - 1, 
                ri.getMinTileY() + ri.getNumYTiles() - 1);
        tile.getPixel(tile.getWidth() / 2, tile.getHeight() / 2, pixel);
        assertEquals(255, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(255, pixel[3]);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestRunner.run(ImageMosaicReaderTest.suite());

	}
	
	@BeforeClass
	public static void init(){
		
		//make sure CRS ordering is correct
		System.setProperty("org.geotools.referencing.forceXY", "true");
	    System.setProperty("user.timezone", "GMT");
		System.setProperty("org.geotools.shapefile.datetime", "true");

		INTERACTIVE = TestData.isInteractiveTest();
	}

	@Before
	public void setUp() throws Exception {
		//remove generated file
	    
		cleanUp();
		
		rgbURL = TestData.url(this, "rgb");
		heterogeneousGranulesURL = TestData.url(this, "heterogeneous");
		timeURL = TestData.url(this, "time_geotiff");
		timeAdditionalDomainsURL = TestData.url(this, "time_additionaldomains");
		
		overviewURL = TestData.url(this, "overview/");
		rgbAURL = TestData.url(this, "rgba/");
		
		indexURL = TestData.url(this, "index/");
		
		indexAlphaURL = TestData.url(this, "index_alpha/");
		
		grayURL = TestData.url(this, "gray/");
		
		index_unique_paletteAlphaURL = TestData.url(this,"index_alpha_unique_palette/");
		
		imposedEnvelopeURL=TestData.url(this,"env");
	
	}

	/**
	 * Cleaning up the generated files (shape and properties so that we recreate them.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void cleanUp() throws FileNotFoundException, IOException {
			if(INTERACTIVE)
				return;
			File dir=TestData.file(this, "overview/");
			File[] files = dir.listFiles(
					(FilenameFilter)FileFilterUtils.notFileFilter(
							FileFilterUtils.or(
									FileFilterUtils.or(
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
					FileFilterUtils.or(
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
	
	@AfterClass
	public static void close(){
		System.clearProperty("org.geotools.referencing.forceXY");
	}


}
