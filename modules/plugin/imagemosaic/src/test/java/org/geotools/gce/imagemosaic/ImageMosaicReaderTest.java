/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2013, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.JFrame;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
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

    private final static double DELTA = 1E-4;
    
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
	
	private URL timeAdditionalDomainsRangeURL;
	
	private URL timeRangesURL;

	private URL imposedEnvelopeURL;
	
    private final static String H2_SAMPLE_PROPERTIES = "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
            + "dbtype=h2\n" + "Loose\\ bbox=true #important for performances\n"
            + "Estimated\\ extends=false #important for performances\n" + "user=gs\n"
            + "passwd=gs\n" + "validate \\connections=true #important for avoiding errors\n"
            + "Connection\\ timeout=3600\n"
            + "max \\connections=10 #important for performances, internal pooling\n"
            + "min \\connections=5  #important for performances, internal pooling\n";

    private URL timeFormatURL;

    private URL oneBitURL;
	
	/**
	 * Testing crop capabilities.
	 * 
	 * @throws MismatchedDimensionException
	 * @throws IOException
	 * @throws FactoryException
	 */
	@Test
//        @Ignore	
	public void crop() throws Exception {
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
//        @Ignore	
	public void alpha() throws Exception{
		
		final String testName="alpha-";
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(rgbURL, null, null,testName+rgbURL.getFile()+"-original", false);
		GridCoverage2D coverage=imageMosaicSimpleParamsTest(rgbURL, Color.black,Color.black,testName+rgbURL.getFile(), false);
                ColorModel colorModel = coverage.getRenderedImage().getColorModel();
                assertTrue(colorModel.hasAlpha());
                assertTrue(colorModel instanceof ComponentColorModel);
                
		if (INTERACTIVE)
			// the input images have transparency and they do overlap, we need
			// to ask for blending mosaic.
			imageMosaicSimpleParamsTest(rgbAURL, null, null,testName+rgbAURL.getFile()+"-original", true);
		coverage=imageMosaicSimpleParamsTest(rgbAURL, Color.black,Color.black,testName+rgbAURL.getFile(), false);
                colorModel = coverage.getRenderedImage().getColorModel();
                assertTrue(colorModel.hasAlpha());
                assertTrue(colorModel instanceof ComponentColorModel);

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
		coverage = imageMosaicSimpleParamsTest(indexURL, new Color(58, 49, 8),Color.black,testName+indexURL.getFile(), false);
		colorModel = coverage.getRenderedImage().getColorModel();
                assertTrue(colorModel.hasAlpha());
		assertTrue(colorModel instanceof ComponentColorModel);
		
		
		
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(overviewURL, null, null,testName+overviewURL.getFile()+"-original", false);
		coverage = imageMosaicSimpleParamsTest(overviewURL, new Color(58, 49, 8),Color.black,testName+overviewURL.getFile()+"-indexURL", false);
		colorModel = coverage.getRenderedImage().getColorModel();
                assertTrue(colorModel.hasAlpha());
                assertTrue(colorModel instanceof ComponentColorModel);
                
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(indexAlphaURL, null, null,testName+indexAlphaURL.getFile()+"-original", false);
		coverage = imageMosaicSimpleParamsTest(indexAlphaURL, new Color(41,41, 33), Color.black,testName+indexAlphaURL.getFile(), false);
		colorModel = coverage.getRenderedImage().getColorModel();
                assertTrue(colorModel.hasAlpha());
                assertTrue(colorModel instanceof ComponentColorModel);
                
		
		
		if (INTERACTIVE)
			imageMosaicSimpleParamsTest(grayURL, null, null,testName+grayURL.getFile()+"-original", false);
		coverage = imageMosaicSimpleParamsTest(grayURL, Color.black,Color.black, testName+grayURL.getFile(), false);
		colorModel = coverage.getRenderedImage().getColorModel();
                assertTrue(colorModel.hasAlpha());
                assertTrue(colorModel instanceof ComponentColorModel);;
		

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
	public void overviews() throws Exception {
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
	
    /**
     * 
     * @throws IOException
     * @throws MismatchedDimensionException
     * @throws FactoryException
     */
    @Test
    // @Ignore
    public void readingResolutions() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(overviewURL);
        final ImageMosaicReader reader = TestUtils.getReader(overviewURL, format);
        double[] result = reader.getReadingResolutions(OverviewPolicy.QUALITY, new double[] { 32, 32 });
        assertEquals(16.0714285714285, result[0], DELTA);
        assertEquals(16.0427807486631, result[1], DELTA);
    }
	
	@Test
	    //@Ignore
	public void timeElevationH2() throws Exception {
	    
    	final File workDir=new File(TestData.file(this, "."),"water_temp3");
        if(!workDir.mkdir()){
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:"+workDir,workDir.mkdir());
        }
    	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
    	TestData.unzipFile(this, "water_temp3/watertemp.zip");
	    final URL timeElevURL = TestData.url(this, "water_temp3");
	    
	    //place H2 file in the dir
	    FileWriter out=null;
	    try{
	    	out = new FileWriter(new File(TestData.file(this, "."),"/water_temp3/datastore.properties"));
	    	out.write("SPI=org.geotools.data.h2.H2DataStoreFactory\n");
	    	out.write("database=imagemosaic\n");
	    	out.write("dbtype=h2\n");
	    	out.write("Loose\\ bbox=true #important for performances\n");
	    	out.write("Estimated\\ extends=false #important for performances\n");
	    	out.write("user=geosolutions\n");
	    	out.write("passwd=geosolutions\n");
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
		assertEquals(metadataNames.length,12);
		
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
		assertNotNull(timeMetadata);
		assertEquals(2,timeMetadata.split(",").length);
		assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals(timeMetadata.split(",")[1],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
		
		assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
		final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
		assertNotNull(elevationMetadata);
                assertEquals("0,100",elevationMetadata);
		assertEquals(2,elevationMetadata.split(",").length);
	        assertEquals(Double.parseDouble(elevationMetadata.split(",")[0]),Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM")),1E-6);
	        assertEquals(Double.parseDouble(elevationMetadata.split(",")[1]),Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM")),1E-6);
	        assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));
		
		
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
		reader.dispose();
		
		reader= TestUtils.getReader(timeElevURL, format);
                elevation.setValue(Arrays.asList(NumberRange.create(0.0,10.0)));
        
                // Test the output coverage
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, time, bkg, elevation,direct },"Time-Elevation Test");
                
         // clean up
         if (!INTERACTIVE){        	
         	FileUtils.deleteDirectory( TestData.file(this, "water_temp3"));
         }
	}	
	
	/**
	 * This test is used to check backward compatibility with old imagemosaics wich does not include
	 * the TypeName=MOSAICNAME into the generated MOSAICNAME.properties file
	 * 
	 * @throws Exception
	 */
    @Test
    // @Ignore
    public void testTypeNameBackwardsCompatibility() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "water_temp5");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils
                .copyFile(TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp5/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp5");

        // place H2 file in the dir
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    "/water_temp5/datastore.properties"));
            out.write("SPI=org.geotools.data.h2.H2DataStoreFactory\n");
            out.write("database=imagemosaic\n");
            out.write("dbtype=h2\n");
            out.write("Loose\\ bbox=true #important for performances\n");
            out.write("Estimated\\ extends=false #important for performances\n");
            out.write("user=geosolutions\n");
            out.write("passwd=geosolutions\n");
            out.write("validate\\ connections=true #important for avoiding errors\n");
            out.write("Connection\\ timeout=3600\n");
            out.write("max\\ connections=10 #important for performances, internal pooling\n");
            out.write("min\\ connections=5  #important for performances, internal pooling\n");
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }

        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);
        reader.dispose();
        format=null;
        
        // remove the TypeName=MOSAICNAME from MOSAICNAME.properties
        FileInputStream fin=null;
        FileWriter fw=null;
        try {
            File mosaicFile=new File(TestData.file(this, "."),
                    "/water_temp5/water_temp5.properties");
            fin = new FileInputStream(mosaicFile);
            Properties properties=new Properties();
            properties.load(fin);
            assertNotNull(properties.remove("TypeName"));
            fw = new FileWriter(mosaicFile);
            properties.store(fw, "");
        } finally {
            IOUtils.closeQuietly(fin);
            IOUtils.closeQuietly(fw);
        }
        
        // we should be able to load the mosaic also without the TypeName=MOSAICNAME
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp5"));
        }
    }

	@Test
//	@Ignore
	public void timeElevation() throws Exception {
    	final File workDir=new File(TestData.file(this, "."),"watertemp2");
    	if(!workDir.mkdir()){
    	    FileUtils.deleteDirectory(workDir);
    	    assertTrue("Unable to create workdir:"+workDir,workDir.mkdir());
    	}
    	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
    	TestData.unzipFile(this, "watertemp2/watertemp.zip");
    	
	    final URL timeElevURL = TestData.url(this, "watertemp2");
	    
		final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
		assertNotNull(format);
		ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
		assertNotNull(format);
		
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length,12);
		
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
		assertNotNull(timeMetadata);
		assertEquals(2,timeMetadata.split(",").length);
		assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals(timeMetadata.split(",")[1],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		assertEquals("java.util.Date", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
		
		assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
		final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
		assertNotNull(elevationMetadata);
		assertEquals("0,100",elevationMetadata);
		assertEquals(2,elevationMetadata.split(",").length);
	        assertEquals(elevationMetadata.split(",")[0],reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
	        assertEquals(elevationMetadata.split(",")[1],reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
	        assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));
		
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
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, time, bkg, elevation,direct },"Time-Elevation Test");
                
                // clean up
                if (!INTERACTIVE){
                 	FileUtils.deleteDirectory( TestData.file(this, "watertemp2"));
                 }
	}	

	

        @Test
//        @Ignore
        public void timeDoubleElevation() throws Exception {
                // Check we can have an integer elevation too 
        	final File workDir=new File(TestData.file(this, "."),"watertemp1");
                if(!workDir.mkdir()){
                    FileUtils.deleteDirectory(workDir);
                    assertTrue("Unable to create workdir:"+workDir,workDir.mkdir());
                }
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
                assertEquals(metadataNames.length,12);
                
                assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
                assertNotNull(timeMetadata);
                assertEquals(2,timeMetadata.split(",").length);
                assertEquals(timeMetadata.split(",")[0],reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
                assertEquals(timeMetadata.split(",")[1],reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
                assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
                
                assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals(2,elevationMetadata.split(",").length);
                assertEquals("0.0",reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
                assertEquals("100.0",reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
                assertEquals("java.lang.Double", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));
                
                
                
                // clean up
                reader.dispose();
                if (!INTERACTIVE){
                 	FileUtils.deleteDirectory( TestData.file(this, "watertemp1"));
                }
        }
	
	@Test
//    @Ignore	
	public void imposedBBox() throws Exception {
		final AbstractGridFormat format = TestUtils.getFormat(imposedEnvelopeURL);
		final ImageMosaicReader reader = TestUtils.getReader(imposedEnvelopeURL, format);
	
		
		//check envelope
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		assertNotNull(envelope);
		
		assertEquals(-180.0, envelope.getMinimum(0), 1E-6);
		assertEquals(-90.0, envelope.getMinimum(1), 1E-6);
		assertEquals(180.0, envelope.getMaximum(0), 1E-6);
		assertEquals(90.0, envelope.getMaximum(1), 1E-6);
		
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
	

	@Test
//	@Ignore
	public void time() throws Exception {
	       
		final AbstractGridFormat format = TestUtils.getFormat(timeURL);
		ImageMosaicReader reader = TestUtils.getReader(timeURL, format);
	    
		final String[] metadataNames = reader.getMetadataNames();
		assertNotNull(metadataNames);
		assertEquals(metadataNames.length, 12);
		assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
		assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
		assertEquals("2004-05-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
		assertEquals("2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));		
		assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
		
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
		
		// specify time
		final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
		
		final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
		final Date timeD=formatD.parse("2004-02-01T00:00:00.000Z");
		time.setValue(new ArrayList(){{add(timeD);}});
		
		// Test the output coverage
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,time}, "time test");
		
		// specify time range
		// Test the output coverage
		reader = TestUtils.getReader(timeURL, format);
                time.setValue(
                        new ArrayList(){{
                            add(new DateRange(formatD.parse("2004-02-01T00:00:00.000Z"), formatD.parse("2004-03-01T00:00:00.000Z")));
                            }}
                );		
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg,useJai ,time}, "time test");
		
	}	
	
	
    @Test
    public void testTimeFormat() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(timeFormatURL);
        ImageMosaicReader reader = TestUtils.getReader(timeFormatURL, format);
        
        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);
        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("2004-02-01T12:05:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2004-05-30T12:15:59.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("2004-02-01T12:05:00.000Z,2004-03-01T15:07:00.000Z,2004-04-15T19:05:00.000Z,2004-05-30T12:15:59.000Z", reader.getMetadataValue(metadataNames[0]));     
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
    }
	
    /**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     */
    @Test
     //@Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDim() throws Exception {
    
        final AbstractGridFormat format = TestUtils
                .getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsURL,
                format);
    
        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000",reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_XX_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));
    
        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD .createValue();
        useJai.setValue(false);
    
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
        ParameterValue<List<String>> depthValue = null;
        final String selectedWaveLength = "020";
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } else if (param.getName().getCode().equalsIgnoreCase("DEPTH")) {
                depthValue = param.createValue();
                depthValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }
        assertNotNull(depthValue);
        assertNotNull(dateValue);
        
        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai, time, dateValue, depthValue };
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
     * @throws Exception
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDimRanges() throws Exception {
    
        final AbstractGridFormat format = TestUtils
                .getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsRangeURL, format);
        try {
    
            final String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals(metadataNames.length, 18);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2008-10-31T00:00:00.000Z/2008-11-04T00:00:00.000Z/PT1S,2008-11-05T00:00:00.000Z/2008-11-07T00:00:00.000Z/PT1S",reader.getMetadataValue("TIME_DOMAIN"));
            assertEquals("2008-10-31T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            assertEquals("2008-11-07T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
            String expectedType = Boolean.getBoolean("org.geotools.shapefile.datetime") ? "java.sql.Timestamp" : "java.util.Date"; 
            assertEquals(expectedType, reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
    
            assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
            assertEquals("20/99,100/150",reader.getMetadataValue("ELEVATION_DOMAIN"));
            assertEquals("20", reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
            assertEquals("150", reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
            assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));
    
            assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
            assertEquals("20081031T000000,20081101T000000,20081105T000000",reader.getMetadataValue("DATE_DOMAIN"));
            assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));
    
            assertEquals("true", reader.getMetadataValue("HAS_WAVELENGTH_DOMAIN"));
            assertEquals("12/24,25/80", reader.getMetadataValue("WAVELENGTH_DOMAIN"));
            assertEquals("12", reader.getMetadataValue("WAVELENGTH_DOMAIN_MINIMUM"));
            assertEquals("80", reader.getMetadataValue("WAVELENGTH_DOMAIN_MAXIMUM"));
            assertEquals("java.lang.Integer", reader.getMetadataValue("WAVELENGTH_DOMAIN_DATATYPE"));
    
            // use imageio with defined tiles
            final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD .createValue();
            useJai.setValue(false);
        
            // specify time
            final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            final Date timeD = parseTimeStamp("2008-11-01T00:00:00.000Z");
            time.setValue(new ArrayList() {
                {
                    add(timeD);
                }
            });
        
            final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(new ArrayList() {
                {
                    add(34); // Elevation
                }
            });
            
            // specify additional Dimensions
            Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
            ParameterValue<List<String>> dateValue = null;
            ParameterValue<List<String>> waveLength = null;
            final String selectedWaveLength = "20";
            final String selectedDate = "20081031T000000";
            for (ParameterDescriptor param : params) {
                if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                    dateValue = param.createValue();
                    dateValue.setValue(new ArrayList<String>() {
                        {
                            add(selectedDate);
                        }
                    });
                } else if (param.getName().getCode().equalsIgnoreCase("WAVELENGTH")) {
                    waveLength = param.createValue();
                    waveLength.setValue(new ArrayList<String>() {
                        {
                            add(selectedWaveLength);
                        }
                    });
                }
            }
            assertNotNull(waveLength);
            assertNotNull(dateValue);
            
            // Test the output coverage
            GeneralParameterValue[] values = new GeneralParameterValue[] { useJai, dateValue, time, waveLength, elevation};
            final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, true);
            final String fileSource = (String) coverage
                    .getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        
            // Check the proper granule has been read
            final String baseName = FilenameUtils.getBaseName(fileSource);
            assertEquals(baseName, "temp_020_099_20081031T000000_20081103T000000_12_24");
            TestUtils.testCoverage(reader, values, "domain test", coverage, null);
        } finally {
            if(reader != null) {
                reader.dispose();
            }
        }
            
    }
    
    /**
     * Simple test method to test emptyMosaic creation support followed by harvesting.
     * dataset
     * @throws Exception
     */
    @Test
     //@Ignore
    public void testEmpytMosaic() throws Exception {
    
        final File workDir=new File(TestData.file(this, "."),"emptyMosaic");
        if(!workDir.mkdir()){
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:"+workDir,workDir.mkdir());
        }
        File zipFile = new File(workDir,"temperature.zip");
        FileUtils.copyFile(TestData.file(this, "temperature.zip"), zipFile);
        TestData.unzipFile(this, "emptyMosaic/temperature.zip");
        FileWriter out = new FileWriter(new File(TestData.file(this, "."),"/emptyMosaic/datastore.properties"));
        out.write("SPI=org.geotools.data.h2.H2DataStoreFactory\n");
        out.write("database=imagemosaic\n");
        out.write("dbtype=h2\n");
        out.write("Loose\\ bbox=true #important for performances\n");
        out.write("Estimated\\ extends=false #important for performances\n");
        out.write("user=geosolutions\n");
        out.write("passwd=geosolutions\n");
        out.write("validate\\ connections=true #important for avoiding errors\n");
        out.write("Connection\\ timeout=3600\n");
        out.write("max\\ connections=10 #important for performances, internal pooling\n");
        out.write("min\\ connections=5  #important for performances, internal pooling\n");
        out.flush();
        out.close();
        
        FileUtils.deleteQuietly(zipFile);
        
        final URL emptyMosaicURL = TestData.url(this, "emptyMosaic");
        final AbstractGridFormat mosaicFormat = TestUtils.getFormat(emptyMosaicURL);
        ImageMosaicReader reader = TestUtils.getReader(emptyMosaicURL, mosaicFormat);

        String[] metadataNames = reader.getMetadataNames();
        assertNull(metadataNames);

        File source = DataUtilities.urlToFile(timeRangesURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"singleHarvest1");
        if(directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }            
        FileUtils.copyDirectory(source, directory1);
        
        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        File renamed = new File(directory1, "temp_020_099_20081101T000000_20081104T000000.tiff");
        
        
        try {
            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());
            
            // the harvest put the file in the same coverage
            reader = TestUtils.getReader(emptyMosaicURL, mosaicFormat);
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2008-11-01T00:00:00.000Z/2008-11-04T00:00:00.000Z/PT1S", reader.getMetadataValue(metadataNames[0]));
            
            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(1, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                String expected = "../singleHarvest1/temp_020_099_20081101T000000_20081104T000000.tiff".replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals("2008-11-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }
            
        } finally {
            reader.dispose();
        }
    }
    
    
    /**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     * @throws Exception
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void granuleSourceTest() throws Exception {
    
        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsRangeURL, format);
    
        GranuleSource source = ((StructuredGridCoverage2DReader)reader).getGranules("time_domainsRanges", true);
        final int granules = source.getCount(null);
        final SimpleFeatureType type = source.getSchema();
        assertEquals("SimpleFeatureTypeImpl time_domainsRanges identified extends polygonFeature(the_geom:MultiPolygon,location:location,time:time,endtime:endtime,date:date,lowz:lowz,highz:highz,loww:loww,highw:highw)",type.toString());
        assertEquals(granules, 12);
        
        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("2008-10-31T00:00:00.000Z/2008-11-04T00:00:00.000Z/PT1S,2008-11-05T00:00:00.000Z/2008-11-07T00:00:00.000Z/PT1S",reader.getMetadataValue("TIME_DOMAIN"));
        assertEquals("2008-10-31T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2008-11-07T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.util.Date", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
        
        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("20/99,100/150",reader.getMetadataValue("ELEVATION_DOMAIN"));
        assertEquals("20", reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
        assertEquals("150", reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));
        
        
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T000000,20081101T000000,20081105T000000",reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_WAVELENGTH_DOMAIN"));
        assertEquals("12/24,25/80", reader.getMetadataValue("WAVELENGTH_DOMAIN"));
        assertEquals("12", reader.getMetadataValue("WAVELENGTH_DOMAIN_MINIMUM"));
        assertEquals("80", reader.getMetadataValue("WAVELENGTH_DOMAIN_MAXIMUM"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("WAVELENGTH_DOMAIN_DATATYPE"));
    
        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD .createValue();
        useJai.setValue(false);
    
        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final Date timeD = parseTimeStamp("2008-11-01T00:00:00.000Z");
        time.setValue(new ArrayList() {
            {
                add(timeD);
            }
        });
    
        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(new ArrayList() {
            {
                add(34); // Elevation
            }
        });
        
        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> waveLength = null;
        final String selectedWaveLength = "20";
        final String selectedDate = "20081031T000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } else if (param.getName().getCode().equalsIgnoreCase("WAVELENGTH")) {
                waveLength = param.createValue();
                waveLength.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }
        assertNotNull(waveLength);
        assertNotNull(dateValue);
        

        // Get the properties file and check if the SuggestedSPI parameter is set
        File covFile = DataUtilities.urlToFile(timeAdditionalDomainsRangeURL);
        File propFile = new File(covFile, "time_domainsRanges.properties");
        // Ensure the file exists
        assertTrue(propFile.exists());
        Properties props = Utils.loadPropertiesFromURL(DataUtilities.fileToURL(propFile));
        // ImageReaderSpi property
        String suggestedSpi = props.getProperty(Utils.Prop.SUGGESTED_SPI);
        // Check if the property exists
        assertNotNull(suggestedSpi);
        // Get the class indicated by the SPI
        Class<?> clazz = Class.forName(suggestedSpi);
        // Check if the class really exists
        assertNotNull(clazz);

        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai, dateValue, time, waveLength, elevation};
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, true);
        final String fileSource = (String) coverage
                .getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);

        // Check the proper granule has been read
        final String baseName = FilenameUtils.getBaseName(fileSource);
        assertEquals(baseName, "temp_020_099_20081031T000000_20081103T000000_12_24");
        TestUtils.testCoverage(reader, values, "domain test", coverage, null);
    }

    /**
     * Simple test method testing dimensions Descriptor for the sample
     * dataset
     * @throws Exception
     */
    @Test
    public void testDimensionsDescriptor() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsRangeURL, format);
        List<DimensionDescriptor> descriptors = ((StructuredGridCoverage2DReader)reader).getDimensionDescriptors("time_domainsRanges");
        assertNotNull(descriptors);
        assertEquals(4, descriptors.size());
        
        Map<String, DimensionDescriptor> dds = new HashMap<String, DimensionDescriptor>();
        for (DimensionDescriptor dd : descriptors) {
            dds.put(dd.getName(), dd);
        }

        DimensionDescriptor descriptor = dds.get("wavelength");
        assertEquals("wavelength", descriptor.getName());
        assertEquals("loww", descriptor.getStartAttribute());
        assertEquals("highw", descriptor.getEndAttribute());

        descriptor = dds.get("date");
        assertEquals("date", descriptor.getName());
        assertEquals("date", descriptor.getStartAttribute());
        assertNull(descriptor.getEndAttribute());

        descriptor = dds.get("TIME");
        assertEquals("TIME", descriptor.getName());
        assertEquals("time", descriptor.getStartAttribute());
        assertEquals("endtime", descriptor.getEndAttribute());
        assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getName(), descriptor.getUnits());
        assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getSymbol(), descriptor.getUnitSymbol());
        
        descriptor = dds.get("ELEVATION");
        assertEquals("ELEVATION", descriptor.getName());
        assertEquals("lowz", descriptor.getStartAttribute());
        assertEquals("highz", descriptor.getEndAttribute());
        
    }

    @Test
    public void testAdditionalDimRangesNoTimestamp() throws Exception {
        System.setProperty("org.geotools.shapefile.datetime", "false");
        timeAdditionalDimRanges();
    }
    
    /**
     * Tests that selection by range works properly 
     * @throws Exception
     */
    @Test
    public void timeTimeRangeSelection() throws Exception {
        final AbstractGridFormat format = TestUtils
                .getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsRangeURL, format);
    
        // specify a range that's below the available data 
        GridCoverage2D coverage = readCoverageInDateRange(reader, "2008-10-20T00:00:00.000Z", "2008-10-25T12:00:00.000Z");
        assertNull(coverage);
        
        // specify a range that's above the available data
        coverage = readCoverageInDateRange(reader, "2008-11-20T00:00:00.000Z", "2008-11-25T12:00:00.000Z");
        assertNull(coverage);
        
        // specify a range that's in a hole where no data is available
        coverage = readCoverageInDateRange(reader, "2008-11-04T12:00:00.000Z", "2008-11-04T18:00:00.000Z");
        assertNull(coverage);
        
        // specify a range that covers it all
        coverage = readCoverageInDateRange(reader, "2008-10-20T00:00:00.000Z", "2008-11-20T00:00:00.000Z");
        assertNotNull(coverage);
        
        // specify a range that overlaps with the first range on the low side
        coverage = readCoverageInDateRange(reader, "2008-10-28T00:00:00.000Z", "2008-10-31T18:00:00.000Z");
        assertNotNull(coverage);
        String fileSource = (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals("temp_020_099_20081031T000000_20081103T000000_12_24", FilenameUtils.getBaseName(fileSource));
        
        // specify a range that overlaps in the middle with the second range
        coverage = readCoverageInDateRange(reader, "2008-11-03T12:00:00.000Z", "2008-11-04T00:00:00.000Z");
        assertNotNull(coverage);
        fileSource = (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals("temp_020_099_20081101T000000_20081104T000000_12_24", FilenameUtils.getBaseName(fileSource));
        
        // specify a range matching an exact start of a range
        coverage = readCoverageInDateRange(reader, "2008-10-31T00:00:00.000Z", "2008-10-31T00:00:00.000Z");
        assertNotNull(coverage);
        fileSource = (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals("temp_020_099_20081031T000000_20081103T000000_12_24", FilenameUtils.getBaseName(fileSource));
        
        // specify a range matching an exact end of a range
        coverage = readCoverageInDateRange(reader, "2008-11-04T00:00:00.000Z", "2008-11-04T00:00:00.000Z");
        assertNotNull(coverage);
        fileSource = (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals("temp_020_099_20081101T000000_20081104T000000_12_24", FilenameUtils.getBaseName(fileSource));
    }
    
    private GridCoverage2D readCoverageInDateRange(ImageMosaicReader reader, String start, String end) throws Exception {
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        Date s = parseTimeStamp(start);
        Date e = parseTimeStamp(end);
        DateRange range = new DateRange(s, e); 
        time.setValue(Arrays.asList(range));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD .createValue();
        useJai.setValue(false);
        
        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(new ArrayList() {
            {
                add(34); // Elevation
            }
        });
        
        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> waveLength = null;
        final String selectedWaveLength = "20";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("WAVELENGTH")) {
                waveLength = param.createValue();
                waveLength.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }
        assertNotNull(waveLength);

        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai, time, waveLength, elevation };
        return TestUtils.getCoverage(reader, values, false);
    }

    
    private Date parseTimeStamp(String timeStamp) throws ParseException {
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatD.parse(timeStamp);
    }
    
    /**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     * @throws Exception
     */
    @Test
    //@Ignore
    @SuppressWarnings("rawtypes")
    public void multipleDimensionsStacked() throws Exception {
    
        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsURL,format);
    
        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000",reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));
        
        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));
    
        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");
    
        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } 
        }
        
        // Stacked bands
        final ParameterValue<String> paramStacked = ImageMosaicFormat.MERGE_BEHAVIOR.createValue();
        paramStacked.setValue(MergeBehavior.STACK.toString());
        
        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai,tileSize, time, dateValue,paramStacked };
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNotNull(coverage);
        
        // inspect reanderedImage
        final RenderedImage image= coverage.getRenderedImage();
        assertEquals("wrong number of bands detected",1,image.getSampleModel().getNumBands());
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
     * @throws Exception
     */
    @Test
    //@Ignore
    public void testHeterogeneousGranules() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(heterogeneousGranulesURL);
        ImageMosaicReader reader = TestUtils.getReader(heterogeneousGranulesURL, format);

        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(10,10);
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
	 * @throws Exception
	 */
	@Test
//  //@Ignore	
	public void defaultParameterValue() throws Exception {

		final String baseTestName="testDefaultParameterValue";
		imageMosaicSimpleParamsTest(rgbURL, null, null,baseTestName+rgbURL.getFile(), false);
		imageMosaicSimpleParamsTest(rgbAURL, null,  null,baseTestName+rgbAURL.getFile(), false);
		imageMosaicSimpleParamsTest(overviewURL, null,null,baseTestName+overviewURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexURL, null, null,baseTestName+indexURL.getFile(), false);
		imageMosaicSimpleParamsTest(grayURL, null, null,baseTestName+grayURL.getFile(), false);
		imageMosaicSimpleParamsTest(indexAlphaURL, null, null,baseTestName+indexAlphaURL.getFile(), false);		
	}
	

    /**
     * Testing one bit mosaics (black/white)	
     * @throws Exception
     */
    @Test
    public void oneBit() throws Exception {

        final String baseTestName = "oneBit";
        imageMosaicSimpleParamsTest(oneBitURL, null, null, baseTestName + oneBitURL.getFile(), false);
        imageMosaicSimpleParamsTest(oneBitURL, Color.white, null, baseTestName + oneBitURL.getFile(), false);
        imageMosaicSimpleParamsTest(oneBitURL, null, Color.white, baseTestName + oneBitURL.getFile(),false);
    }
	
	@Test
    //@Ignore	
	public void errors() throws Exception {
		final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
		
		////
		//
		// MOSAIC_LOCATION_ATTRIBUTE
		//
		// error for location attribute
		AbstractGridCoverage2DReader reader=null;
		try {
		    LOGGER.info("Testing Invalid location attribute. (A DataSourceException should be catched) ");
//			reader=(AbstractGridCoverage2DReader) new ImageMosaicReader(rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa")); 
			        
			reader =         ((AbstractGridFormat) GridFormatFinder.findFormat(rgbURL,hints))
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
	 * @throws Exception
	 */
	private GridCoverage2D imageMosaicSimpleParamsTest(
			final URL testURL, 
			final Color inputTransparent, 
			final Color outputTransparent, 
			final String title,
			final boolean blend) throws Exception {

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
		return TestUtils.checkCoverage(reader, new GeneralParameterValue[] { inTransp, blendPV, outTransp }, title);
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
	    ImageIOUtilities.visualize(image,title);

	}

	/**
	 * Tests {@link ImageMosaicReader} asking to crop the lower left quarter of
	 * the input coverage.
	 * 
	 * @param title
	 *            to use when showing image.
	 * 
	 * @throws Exception
	 */
	private void imageMosaicCropTest(URL testURL, String title)
			throws Exception{

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
		cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());
		gg.setValue(new GridGeometry2D(PixelInCell.CELL_CENTER,reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),cropEnvelope,null));
		final ParameterValue<Color> outTransp =  ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
		outTransp.setValue(Color.black);


		// test the coverage
		TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, outTransp },title);

	}
	
    @Test
    //@Ignore
    public void testRequestInHole() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbAURL);
        final ImageMosaicReader reader = TestUtils.getReader(rgbAURL, format);

        assertNotNull(reader);

        // ask to extract an area that is inside the coverage bbox, but in a hole (no data)
        final ParameterValue<GridGeometry2D> ggp =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCoordinateReferenceSystem(), 500000, 3200000, 1000, 1000);
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
    //@Ignore
    public void testRequestInOut() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbAURL);
        final ImageMosaicReader reader = TestUtils.getReader(rgbAURL, format);

        assertNotNull(reader);

        // ask to extract an area that is inside the coverage bbox, so that the area is partly
        // inside the raster, and partly outside
        final ParameterValue<GridGeometry2D> ggp =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCoordinateReferenceSystem(), 64887, 2499342, 646897 - 64887 , 3155705 - 2499342);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        ggp.setValue(gg);

        // red background
        final ParameterValue<double[]> bgp =  ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bgp.setValue(new double[] {255, 0, 0, 255});
        
        // read and check we actually got a coverage in the requested area
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, bgp});
        assertNotNull(coverage);
        assertTrue(coverage.getEnvelope2D().contains((Rectangle2D) env));
        
        // and that the color is the expected one given the background values provided
        RenderedImage ri = coverage.getRenderedImage();
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
	    CRS.reset("all");
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
		timeFormatURL = TestData.url(this, "time_format_geotiff");
		timeAdditionalDomainsURL = TestData.url(this, "time_additionaldomains");
		timeAdditionalDomainsRangeURL = TestData.url(this, "time_domainsRanges");
		timeRangesURL = TestData.url(this, "time_ranges");
		overviewURL = TestData.url(this, "overview/");
		rgbAURL = TestData.url(this, "rgba/");
		oneBitURL=TestData.url(this, "onebit/");
		
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
	 * @throws Exception
	 */
	private void cleanUp() throws Exception {
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
			
			dir = TestData.file(this, "time_domainsRanges");
			files = dir.listFiles((FilenameFilter)
                    FileFilterUtils.or(
                            FileFilterUtils.suffixFileFilter("shp"),
                            FileFilterUtils.suffixFileFilter("dbf"),
                            FileFilterUtils.suffixFileFilter("qix"),
                            FileFilterUtils.suffixFileFilter("shx"),
                            FileFilterUtils.suffixFileFilter("prj")
                    ));
            for(File file:files){
                file.delete();
            }
	}
	
	@After
	public void tearDown() throws Exception{
		cleanUp();

	}
	
	/**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     * @throws Exception

     */
    @Test
    //@Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDimNoResultsDueToWrongDim() throws Exception {

        final AbstractGridFormat format = TestUtils
                .getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = TestUtils.getReader(timeAdditionalDomainsURL,
                format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000",
                reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD
                .createValue();
        useJai.setValue(false);

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
        ParameterValue<List<String>> depthValue = null;
        final String selectedWaveLength = "030";
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } else if (param.getName().getCode().equalsIgnoreCase("DEPTH")) {
                depthValue = param.createValue();
                depthValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }        
        assertNotNull(depthValue);
        assertNotNull(dateValue);
        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai, time, dateValue, depthValue };
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNull(coverage);
    }

    /**
     * Simple test method accessing time and 2 custom dimensions for the sample
     * dataset
     * @throws Exception
     */
    @Test
    //@Ignore
    @SuppressWarnings("rawtypes")
    public void multipleDimensionsStackedSar() throws Exception {

        final URL sourceURL=TestData.file(this, "merge").toURI().toURL();
        final AbstractGridFormat format = TestUtils.getFormat(sourceURL);
        ImageMosaicReader reader = TestUtils.getReader(sourceURL,format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(15, metadataNames.length);
        assertEquals("false", reader.getMetadataValue("HAS_POLARIZ_DOMAIN"));
        assertEquals("true", reader.getMetadataValue("HAS_POLARIZATION_DOMAIN"));
        assertEquals("POLARIZATION", reader.getDynamicParameters().iterator().next().getName().getCode());
        assertEquals("HH,HV,VH,VV",reader.getMetadataValue("POLARIZATION_DOMAIN"));// ten characters limitation overcome!
        assertEquals("java.lang.String",reader.getMetadataValue("POLARIZATION_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("2012-01-01T00:00:00.000Z",reader.getMetadataValue("TIME_DOMAIN"));
        assertEquals("2012-01-01T00:00:00.000Z",reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2012-01-01T00:00:00.000Z",reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.sql.Timestamp",reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
    
        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");
    
        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2012-01-01T00:00:00.000Z");
        time.setValue(new ArrayList() {
            {
                add(timeD);
            }
        });
    
        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> polariz = null;
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("POLARIZATION")) {
                polariz = param.createValue();
                polariz.setValue(new ArrayList<String>() {
                    {
                        add("HH");
                        add("HV");
                        add("VV");
                    }
                });
            } 
        }
        
        // Stacked bands
        final ParameterValue<String> paramStacked = ImageMosaicFormat.MERGE_BEHAVIOR.createValue();
        paramStacked.setValue(MergeBehavior.STACK.toString());
        
        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] { useJai,tileSize, time, polariz,paramStacked };
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNotNull(coverage);
        
        // inspect reanderedImage
        final RenderedImage image= coverage.getRenderedImage();
        assertEquals("wrong number of bands detected",3,image.getSampleModel().getNumBands());
        assertEquals(DataBuffer.TYPE_SHORT, image.getSampleModel().getDataType());
        
    }
    
    @Test
    public void testHarvestSingleFile() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"singleHarvest1");
        File directory2 = new File(testDataDir,"singleHarvest2");
        if(directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }            
        FileUtils.copyDirectory(source, directory1);
        // remove all files besides month 2 and 5
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        // move month 5 to another dir, we'll harvet it later
        String monthFiveName = "world.200405.3x5400x2700.tiff";
        File monthFive = new File(directory1, monthFiveName);
        if(directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        } 
        directory2.mkdirs();
        File renamed = new File(directory2, monthFiveName);
        assertTrue(monthFive.renameTo(renamed));
        
        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
            
            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());
            
            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
            
            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                assertEquals("world.200402.3x5400x2700.tiff", f.getAttribute("location"));
                assertEquals("2004-02-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
                f = fi.next();
                String expected = "../singleHarvest2/world.200405.3x5400x2700.tiff".replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals("2004-05-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }
            
        } finally {
            reader.dispose();
        }
    }
    
    @Test
    public void testHarvestMultipleFiles() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"harvest1");
        File directory2 = new File(testDataDir,"harvest2");
        if(directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }            
        FileUtils.copyDirectory(source, directory1);
        if(directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        } 
        directory2.mkdirs();
        // Creation of a File Collection
        Collection<File> files = new ArrayList<File>();
        
        // move all files besides month 2 and 5 to the second directory and store them into a Collection
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
            files.add(renamed);
        }
        // remove all mosaic related files
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        
        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
            
            // now go and harvest the other directory
            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertEquals(2, summary.size());
            for (HarvestedSource hf : summary) {
                assertTrue(hf.success());
            }
            
            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", 
                    reader.getMetadataValue(metadataNames[0]));
        } finally {
            reader.dispose();
        }
    }

    
    @Test
    public void testHarvestDirectory() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"harvest1");
        File directory2 = new File(testDataDir,"harvest2");
        if(directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }            
        FileUtils.copyDirectory(source, directory1);
        if(directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        } 
        directory2.mkdirs();
        // move all files besides month 2 and 5 to the second directory
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
        }
        // remove all mosaic related files
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        
        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
            
            // now go and harvest the other directory
            List<HarvestedSource> summary = reader.harvest(null, directory2, null);
            assertEquals(2, summary.size());
            for (HarvestedSource hf : summary) {
                assertTrue(hf.success());
            }
            
            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", 
                    reader.getMetadataValue(metadataNames[0]));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestListSingleDirectory() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"harvest1");
        File directory2 = new File(testDataDir,"harvest2");
        if(directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        if(directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        // move all files besides month 2 and 5 to the second directory
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
        }
        // remove all mosaic related files
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }

        // Create a List of Files containing only the directory to harvest and check if the Reader reads it as a Directory
        List<File> files = new ArrayList<File>();
        files.add(directory2);

        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the file list
            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertEquals(2, summary.size());
            for (HarvestedSource hf : summary) {
                assertTrue(hf.success());
            }

            // the harvest put the files in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", 
                    reader.getMetadataValue(metadataNames[0]));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestListSingleFile() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"singleHarvest1");
        File directory2 = new File(testDataDir,"singleHarvest2");
        if(directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all files besides month 2 and 5
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for(File file : FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        // move month 5 to another dir, we'll harvet it later
        String monthFiveName = "world.200405.3x5400x2700.tiff";
        File monthFive = new File(directory1, monthFiveName);
        if(directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        File renamed = new File(directory2, monthFiveName);
        assertTrue(monthFive.renameTo(renamed));

        // Create a List of Files containing only the directory to harvest and check if the Reader reads it as a Directory
        List<File> files = new ArrayList<File>();
        files.add(renamed);

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                assertEquals("world.200402.3x5400x2700.tiff", f.getAttribute("location"));
                assertEquals("2004-02-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
                f = fi.next();
                String expected = "../singleHarvest2/world.200405.3x5400x2700.tiff".replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals("2004-05-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testRemoveCoverageNoDelete() throws Exception {

        final String referenceDir = "testRemove";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    referenceDir + "/datastore.properties"));
            out.write("database=imagemosaicremove\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        try {

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            File[] files = workDir.listFiles();
            assertNotNull(files);
            assertEquals(16, files.length);

            reader.removeCoverage(reader.getGridCoverageNames()[0], false);
            assertEquals(0, reader.getGridCoverageNames().length);
            assertEquals(16, files.length);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testRemoveCoverageDelete() throws Exception {

        final String referenceDir = "testRemove2";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    referenceDir + "/datastore.properties"));
            out.write("database=imagemosaicremove2\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        try {
            assertEquals(1, reader.getGridCoverageNames().length);
            File[] files = workDir.listFiles();
            assertNotNull(files);
            assertEquals(16, files.length);

            reader.removeCoverage(reader.getGridCoverageNames()[0], true);
            assertEquals(0, reader.getGridCoverageNames().length);
            files = workDir.listFiles();
            assertEquals(12, files.length);

        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testReaderDeleteAll() throws Exception {

        final String referenceDir = "testRemove3";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        FileUtils.deleteQuietly(new File(workDir + "/watertemp.zip"));
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    referenceDir + "/datastore.properties"));
            out.write("database=imagemosaicremove3\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);

        StructuredGridCoverage2DReader reader = null;
        try {

            // the harvest put the file in the same coverage
            reader = new ImageMosaicReader(timeElevURL);

            // delete all files associated to that mosaic (granules, auxiliary files, DB entries, ...)
            File[] files = workDir.listFiles();
            assertEquals(15, files.length);
            reader.delete(true);
            files = workDir.listFiles();
            assertEquals(0, files.length);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testReaderDeleteNoGranules() throws Exception {

        final String referenceDir = "testRemove4";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        FileUtils.deleteQuietly(new File(workDir + "/watertemp.zip"));
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    referenceDir + "/datastore.properties"));
            out.write("database=imagemosaicremove4\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);

        StructuredGridCoverage2DReader reader = null;
        try {

            reader = new ImageMosaicReader(timeElevURL);

            // delete metadata only (auxiliary files, DB entries, ...)
            File[] files = workDir.listFiles();
            assertEquals(15, files.length);
            reader.delete(false);
            files = workDir.listFiles();
            assertEquals(4, files.length);
        } finally {
            reader.dispose();
        }
    }

    
    @Test
    public void testHarvestError() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory = new File(testDataDir,"harvest-error");
        if(directory.exists()) {
            FileUtils.deleteDirectory(directory);
        }            
        FileUtils.copyDirectory(source, directory);
        // remove all files besides month 2
        for(File file : FileUtils.listFiles(directory, new RegexFileFilter("world\\.20040[^2].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for(File file : FileUtils.listFiles(directory, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        
        // ok, let's create a mosaic with the original granule
        URL harvestSingleURL = DataUtilities.fileToURL(directory);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
            
            // now go and try to make it harvest an invalid file
            File bogus = new File(directory, "test.tiff");
            assertTrue(bogus.createNewFile());
            List<HarvestedSource> summary = reader.harvest(null, bogus, null);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertFalse(hf.success());
            assertEquals("test.tiff", ((File) hf.getSource()).getName());
        } finally {
            reader.dispose();
        }
    }
    
    @Test
    public void testHarvestWithExternalMosaicDir() throws Exception {

        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File directory1 = new File(testDataDir,"externalindex");
        File directory2 = new File(testDataDir,"singleHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all files besides month 2 and 5
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter(
                "world\\.20040[^25].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"),
                null)) {
            assertTrue(file.delete());
        }

        // Editing indexer RootMosaicDirectory path
        InputStream stream = null;
        OutputStream outStream = null;
        try {

            final String indexerPath = directory1.getCanonicalPath() + "/indexer.properties";
            stream = new FileInputStream(indexerPath);
            String path = directory1.getCanonicalPath();
            path = path.replace("\\", "/");
            Properties prop = new Properties();
            prop.load(stream);
            
            outStream = new FileOutputStream(indexerPath);
            prop.setProperty(Prop.ROOT_MOSAIC_DIR, path);
            prop.store(outStream, null);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
        // move month 5 to another dir, we'll harvet it later
        String monthFiveName = "world.200405.3x5400x2700.tiff";
        File monthFive = new File(directory1, monthFiveName);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        File renamed = new File(directory2, monthFiveName);
        assertTrue(monthFive.renameTo(renamed));

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = DataUtilities.fileToURL(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                assertEquals("world.200402.3x5400x2700.tiff", f.getAttribute("location"));
                assertEquals("2004-02-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
                f = fi.next();
                String expected = "../singleHarvest2/world.200405.3x5400x2700.tiff".replace('/',
                        File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals("2004-05-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }

        } finally {
            reader.dispose();
        }
    }
    
    @Test
    public void testSetupExternalMosaicDir() throws Exception {
        File source = DataUtilities.urlToFile(timeURL);
        File testDataDir= TestData.file(this, ".");
        File data = new File(testDataDir,"externaldata");
        File mosaic = new File(testDataDir,"mosaicexternal");
        if (data.exists()) {
            FileUtils.deleteDirectory(data);
        }
        FileUtils.copyDirectory(source, data);
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        mosaic.mkdirs();
        // get rid of pre-configured data
        for (File file : FileUtils.listFiles(data, new RegexFileFilter("time_geotiff.*"),
                null)) {
            assertTrue(file.delete());
        }
        // move the indexer config files into the mosaic direcotry
        for (File file : FileUtils.listFiles(data, new RegexFileFilter(".*\\.properties"),
                null)) {
            File moved = new File(mosaic, file.getName());
            assertTrue(file.renameTo(moved));
        }

        // Editing indexer RootMosaicDirectory path
        InputStream stream = null;
        OutputStream outStream = null;
        try {
            final File indexer = new File(mosaic, "indexer.properties");
            stream = new FileInputStream(indexer);
            Properties prop = new Properties();
            prop.load(stream);
            
            outStream = new FileOutputStream(indexer);
            prop.setProperty(Prop.INDEXING_DIRECTORIES, data.getCanonicalPath());
            prop.store(outStream, null);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }

        // ok, let's create the mosaic and check it harvested the data in the "data" directory
        URL mosaicURL = DataUtilities.fileToURL(mosaic);
        final AbstractGridFormat format = TestUtils.getFormat(mosaicURL);
        ImageMosaicReader reader = TestUtils.getReader(mosaicURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals(metadataNames.length,12);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            assertEquals("2004-05-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
            assertEquals("2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));
            assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
        } finally {
            reader.dispose();
        }
    }
    
    
    @Test
    @Ignore
    public void oracle() throws Exception {
        final File workDir = TestData.file(this, "wattemp");

        final AbstractGridFormat format = TestUtils.getFormat(workDir.toURI().toURL());
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(workDir.toURI().toURL(), format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.util.Date", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DAT_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000", reader.getMetadataValue("DAT_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DAT_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_XX_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(reader.getOriginalGridRange().getSpan(0) / 2.0, reader.getOriginalGridRange()
                .getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        final ParameterValue<Boolean> direct = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        direct.setValue(false);

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] { -9999.0 });

        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> depthValue = null;
        final String selectedWaveLength = "020";
        final String selectedDate = "20081031T0000000";
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DAT")) {
                dateValue = param.createValue();
                dateValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedDate);
                    }
                });
            } else if (param.getName().getCode().equalsIgnoreCase("DEPTH")) {
                depthValue = param.createValue();
                depthValue.setValue(new ArrayList<String>() {
                    {
                        add(selectedWaveLength);
                    }
                });
            }
        }
        // Test the output coverage
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, bkg, direct, depthValue,
                dateValue }, "oracle Test");

    }

    @Test
    public void testExistingSchema() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "water_temp4");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils
                .copyFile(TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp4/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp4");
//


        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        reader.dispose();
        
        // append the parameter to the indexer.properties
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    "/water_temp4/indexer.properties"),true);
            out.write("UseExistingSchema=true\n");
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }
        
        // remove existing properties file and sample_image
        File sampleImage=new File(TestData.file(this, "."),"/water_temp4/sample_image");
        assertTrue(sampleImage.exists());
        sampleImage.delete();
        File mosaicProperties=new File(TestData.file(this, "."),"/water_temp4/water_temp4.properties");
        assertTrue(mosaicProperties.exists());
        mosaicProperties.delete();
        
        // now start the test
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);
        
        // the mosaic is correctly created
        assertTrue(sampleImage.exists());
        assertTrue(mosaicProperties.exists());

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp4"));
        }
    }
    
    @Test
    public void testUserProvidedName() throws Exception {
        final File workDir = new File(TestData.file(this, "."), "water_temp5");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils
                .copyFile(TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp5/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp5");
        
        // force the name
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    "/water_temp5/indexer.properties"),true);
            out.write("Name=test\n");
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }
        
        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);
        
        // the mosaic is correctly created
        File sampleImage=new File(TestData.file(this, "."),"/water_temp5/sample_image");
        File mosaicProperties=new File(TestData.file(this, "."),"/water_temp5/test.properties");
        assertTrue(sampleImage.exists());
        assertTrue(mosaicProperties.exists());

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp5"));
        }
    }


    @Test
    @Ignore
    public void testExistingOracleSchema() throws Exception {

        final String folder = "mosaictemp";
        final String zipFile = "mosaictemp.zip";
        final File workDir = new File(TestData.file(this, "."), folder);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils
                .copyFile(TestData.file(this, zipFile), new File(workDir, zipFile));
        TestData.unzipFile(this, folder + File.separatorChar + zipFile);
        final URL timeElevURL = DataUtilities.fileToURL(workDir);
//


        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        reader.dispose();
        
        // append the parameter to the indexer.properties
        FileWriter out = null;
        try {
            out = new FileWriter(new File(TestData.file(this, "."),
                    folder + File.separatorChar +"indexer.properties"),true);
            out.write("UseExistingSchema=true\n");
            out.flush();
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }
        
        // remove existing properties file and sample_image
        File sampleImage = new File(TestData.file(this, "."),folder + File.separatorChar + "sample_image");
        File mosaicProperties = new File(TestData.file(this, "."),folder + File.separatorChar + folder + ".properties");

        // now start the test
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);
        
        // the mosaic is correctly created
        assertTrue(sampleImage.exists());
        assertTrue(mosaicProperties.exists());

        // clean up
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, folder));
        }
    }

    @Test
    public void testPAMAuxiliaryFiles() throws Exception {
        final URL timePamURL = TestData.url(this, "pam");

        final AbstractGridFormat format = TestUtils.getFormat(timePamURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timePamURL, format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues= new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[]{time});
        Object object = coverage.getProperty(Utils.PAM_DATASET);
        assertNotNull(object);
        assertTrue(object instanceof PAMDataset);
        PAMDataset dataset = (PAMDataset) object;
        PAMRasterBand band = dataset.getPAMRasterBand().get(0);

        PAMParser parser = PAMParser.getInstance();
        assertEquals(0, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MINIMUM")), DELTA);
        assertEquals(255.0, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MAXIMUM")), DELTA);
        assertEquals(73.0352, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MEAN")), DELTA);
        assertEquals(84.3132, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_STDDEV")), DELTA);
    }

    @Test
    public void testPAMMerged() throws Exception {
        final URL timePamURL = TestData.url(this, "pam");

        final AbstractGridFormat format = TestUtils.getFormat(timePamURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timePamURL, format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);

        GridCoverage2D coverage = reader.read(null);
        Object object = coverage.getProperty(Utils.PAM_DATASET);
        assertNotNull(object);
        assertTrue(object instanceof PAMDataset);
        PAMDataset dataset = (PAMDataset) object;
        PAMRasterBand band = dataset.getPAMRasterBand().get(0);

        PAMParser parser = PAMParser.getInstance();
        assertEquals(0, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MINIMUM")), DELTA);
        assertEquals(255.0, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MAXIMUM")), DELTA);
        assertEquals(72.6912, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MEAN")), DELTA);
        assertEquals(83.2542, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_STDDEV")), DELTA);
    }


    @AfterClass
	public static void close(){
		System.clearProperty("org.geotools.referencing.forceXY");
	        CRS.reset("all");
	}


}
