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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.filter.SortByImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.test.OnlineTestCase;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/**
 * Testing index in postgis database
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class ImageMosaicPostgisIndexTest extends OnlineTestCase {
	
	/**
	 * Simple Class for better testing raster manager
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	private static class MyImageMosaicReader extends ImageMosaicReader{

		public MyImageMosaicReader(Object source) throws IOException {
			super(source);
		}

		public MyImageMosaicReader(Object source, Hints uHints)
				throws IOException {
			super(source, uHints);
		}
		
		public RasterManager getRasterManager(){
			return rasterManager;
		}
		
	}

	@Override
	protected Properties createExampleFixture() {
		// create sample properties file for postgis datastore
		final Properties props= new Properties();
		props.setProperty("SPI", "org.geotools.data.postgis.PostgisNGDataStoreFactory");
		props.setProperty("host", "localhost");
		props.setProperty("port", "5432");
		props.setProperty("user", "xxx");
		props.setProperty("passwd", "xxx");
		props.setProperty("database", "ddd");
		props.setProperty("schema", "public");
		props.setProperty("Loose bbox", "true");
		props.setProperty("Estimated extends=", "false");
		props.setProperty("validate connections", "true");
		props.setProperty("Connection timeout", "10");
		props.setProperty("preparedStatements", "false");
		return props;
	}

	/* (non-Javadoc)
	 * @see org.geotools.test.OnlineTestCase#getFixtureId()
	 */
	@Override
	protected String getFixtureId() {
		return "postgis_datastore";
	}
	
	/**
	 * Complex test for Postgis indexing on db.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testPostgisIndexing() throws Exception{
    	final File workDir=new File(TestData.file(this, "."),"watertemp4");
    	assertTrue(workDir.mkdir());
    	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
    	TestData.unzipFile(this, "watertemp4/watertemp.zip");
	    final URL timeElevURL = TestData.url(this, "watertemp4");
	    
	    //place datastore.properties file in the dir for the indexing
	    FileWriter out=null;
	    try{
	    	out = new FileWriter(new File(TestData.file(this, "."),"/watertemp4/datastore.properties"));
	    	
	    	final Set<Object> keyset = fixture.keySet();
	    	for(Object key:keyset){
	    		final String key_=(String) key;
	    		final String value=fixture.getProperty(key_);
	    		
	    		out.write(key_.replace(" ", "\\ ")+"="+value.replace(" ", "\\ ")+"\n");
	    	}
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
                        
        // Test the output coverage
		elevation.setValue(Arrays.asList(NumberRange.create(0.0,10.0)));
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] { gg, time, bkg, elevation,direct },"Time-Elevation Test");
                
        reader.dispose();

		
	}

	/**
	 * Complex test for Postgis indexing on db.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSortingAndLimiting() throws Exception{
    	final File workDir=new File(TestData.file(this, "."),"watertemp4");
    	assertTrue(workDir.mkdir());
    	FileUtils.copyFile(TestData.file(this, "watertemp.zip"), new File(workDir,"watertemp.zip"));
    	TestData.unzipFile(this, "watertemp4/watertemp.zip");
	    final URL timeElevURL = TestData.url(this, "watertemp4");
	    
	    //place datastore.properties file in the dir for the indexing
	    FileWriter out=null;
	    try{
	    	out = new FileWriter(new File(TestData.file(this, "."),"/watertemp4/datastore.properties"));
	    	
	    	final Set<Object> keyset = fixture.keySet();
	    	for(Object key:keyset){
	    		final String key_=(String) key;
	    		final String value=fixture.getProperty(key_);
	    		out.write(key_.replace(" ", "\\ ")+"="+value.replace(" ", "\\ ")+"\n");
	    	}
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
		assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));

		// dispose and create new reader
		reader.dispose();
		final MyImageMosaicReader reader1 = new MyImageMosaicReader(timeElevURL);
		final RasterManager rm = reader1.getRasterManager();
		
		// query
		final SimpleFeatureType type = rm.granuleCatalog.getType();
		Query query = null;
		if (type != null){
			// creating query
			query= new Query(rm.granuleCatalog.getType().getTypeName());
			
			// sorting and limiting
            // max number of elements
            query.setMaxFeatures(1);
            
            // sorting
            final SortBy[] clauses=new SortBy[]{
            		new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("ingestion"),SortOrder.DESCENDING),
            		new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("elevation"),SortOrder.ASCENDING),
            };
            query.setSortBy(clauses);
			
		}
		
		// checking that we get a single feature and that feature is correct
		Collection<GranuleDescriptor> features = rm.getGranules(query);
		assertEquals(features.size(), 1);
		GranuleDescriptor granule=features.iterator().next();
		SimpleFeature sf=granule.getOriginator();
		assertNotNull(sf);
		Object ingestion = sf.getAttribute("ingestion");
		assertTrue(ingestion instanceof Timestamp);
		final GregorianCalendar gc=  new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		gc.setTimeInMillis(1225497600000l);
		assertEquals(0,(((Timestamp)ingestion).compareTo(gc.getTime())));		
		Object elevation = sf.getAttribute("elevation");
		assertTrue(elevation instanceof Integer);
		assertEquals(((Integer)elevation).intValue(), 0);
		
		
		
		// Reverting order (the previous timestamp shouldn't match anymore)
		final SortBy[] clauses=new SortBy[]{
	                        new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("ingestion"),SortOrder.ASCENDING),
	                        new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("elevation"),SortOrder.DESCENDING),
	            };
	            query.setSortBy(clauses);
	            
	         // checking that we get a single feature and that feature is correct
	                features = rm.getGranules(query);
	                assertEquals(features.size(), 1);
	                granule=features.iterator().next();
	                sf=granule.getOriginator();
	                assertNotNull(sf);
	                ingestion = sf.getAttribute("ingestion");
	                assertTrue(ingestion instanceof Timestamp);
	                assertNotSame(0,(((Timestamp)ingestion).compareTo(gc.getTime())));               
	                elevation = sf.getAttribute("elevation");
	                assertTrue(elevation instanceof Integer);
	                assertNotSame(((Integer)elevation).intValue(), 0);
		
	}
	
	@Override
	protected void setUpInternal() throws Exception {
		super.setUpInternal();
		
		//make sure CRS ordering is correct
		System.setProperty("org.geotools.referencing.forceXY", "true");
	    System.setProperty("user.timezone", "GMT");
	}

	@Override
	protected void tearDownInternal() throws Exception {
		
        // clean up disk
//        if (!ImageMosaicReaderTest.INTERACTIVE){        	
//        	FileUtils.deleteDirectory( TestData.file(this, "watertemp4"));
//        }		
        
        // delete tables
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
           "jdbc:postgresql://"+fixture.getProperty("host")+":"+fixture.getProperty("port")+"/"+fixture.getProperty("database"),fixture.getProperty("user"), fixture.getProperty("passwd"));
        Statement st = connection.createStatement();
        st.execute("DROP TABLE IF EXISTS watertemp4");
        st.close();
        connection.close();
        
        System.clearProperty("org.geotools.referencing.forceXY");
        
		super.tearDownInternal();
        
	}

}
