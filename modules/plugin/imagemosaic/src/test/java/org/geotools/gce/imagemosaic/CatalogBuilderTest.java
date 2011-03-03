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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ExceptionEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ProcessingEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ProcessingEventListener;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
/**
 * Testing {@link CatalogBuilder} and its related subclasses.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/imagemosaic/src/test/java/org/geotools/gce/imagemosaic/CatalogBuilderTest.java $
 */
public class CatalogBuilderTest extends Assert {
	
	private final class CatalogBuilderListener extends ProcessingEventListener{

		@Override
		public void exceptionOccurred(ExceptionEvent event) {
			throw new RuntimeException(event.getException());
			
		}

		@Override
		public void getNotification(ProcessingEvent event) {
			
		}
		
	}

	@Test
	public void catalogBuilderConfiguration() throws FileNotFoundException, IOException, CloneNotSupportedException{
		// create a stub configuration
		final CatalogBuilderConfiguration c1= new CatalogBuilderConfiguration();
		c1.setIndexName("index");
		c1.setLocationAttribute("location");
		c1.setAbsolute(true);
		c1.setRootMosaicDirectory(TestData.file(this,"/rgb").toString());
		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/rgb").toString()));
		assertNotNull(c1.toString());
		
		// create a second stub configuration
		final CatalogBuilderConfiguration c2= new CatalogBuilderConfiguration();
		c2.setIndexName("index");
		c2.setLocationAttribute("location");
		c2.setAbsolute(true);
		c2.setRootMosaicDirectory(TestData.file(this,"/rgb").toString());
		c2.setIndexingDirectories(Arrays.asList(TestData.file(this,"/rgb").toString()));
		assertTrue(c1.equals(c2));
		assertEquals(c1.hashCode(), c2.hashCode());
		
		CatalogBuilderConfiguration c3 = c2.clone();
		assertTrue(c3.equals(c2));
		assertEquals(c3.hashCode(), c2.hashCode());
		
		//check errors
		final CatalogBuilderConfiguration c4= new CatalogBuilderConfiguration();
		assertNotNull(c4.toString());
		
	}
	
	@Test
	public void buildCatalog() throws FileNotFoundException, IOException{
            CatalogBuilder builder = null;
            ImageMosaicReader reader = null;
            ParameterValue<GridGeometry2D> gg = null;
            GeneralEnvelope envelope = null;
            Dimension dim = null;
            Rectangle rasterArea = null;
            GridEnvelope2D range = null;
            GridCoverage2D coverage = null;
            final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
            useJai.setValue(false);
            
            final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
            tileSize.setValue("128,128");

	    
		//build a relative index and then make it run
		CatalogBuilderConfiguration c1= new CatalogBuilderConfiguration();
		c1.setIndexName("shpindex");
		c1.setLocationAttribute("location");
		c1.setAbsolute(false);
		c1.setRootMosaicDirectory(TestData.file(this,"/overview").toString());
		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/overview/0").toString()));
		assertNotNull(c1.toString());		
		//build the index
		builder= new CatalogBuilder(c1);
		builder.addProcessingEventListener(new CatalogBuilderListener());
		builder.run();
		final File relativeMosaic=TestData.file(this,"/overview/"+c1.getIndexName()+".shp");
		assertTrue(relativeMosaic.exists());
		
		assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
		reader = (ImageMosaicReader) new ImageMosaicReader(relativeMosaic);

		// limit yourself to reading just a bit of it
		gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
		envelope = reader.getOriginalEnvelope();
		dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		
		// Test the output coverage
		coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg,useJai ,tileSize});
		Assert.assertNotNull(coverage);
		PlanarImage.wrapRenderedImage( coverage.getRenderedImage()).getTiles();;

		
		//build an absolute index and then make it run
		CatalogBuilderConfiguration c2= new CatalogBuilderConfiguration();
		c2.setIndexName("shpindex_absolute");
		c2.setLocationAttribute("location");
		c2.setAbsolute(true);
		c2.setRootMosaicDirectory(TestData.file(this,"/overview").toString());
		c2.setIndexingDirectories(Arrays.asList(TestData.file(this,"/overview/0").toString()));
		assertNotNull(c2.toString());		
		//build the index
		builder= new CatalogBuilder(c2);
		builder.addProcessingEventListener(new CatalogBuilderListener());
		builder.run();
		final File absoluteMosaic=TestData.file(this,"/overview/"+c2.getIndexName()+".shp");
		assertTrue(absoluteMosaic.exists());
		
		assertTrue(new ImageMosaicFormat().accepts(absoluteMosaic));
		reader = (ImageMosaicReader) new ImageMosaicReader(absoluteMosaic);

		// limit yourself to reading just a bit of it
		gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
		envelope = reader.getOriginalEnvelope();
		dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		
		// Test the output coverage
		coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg,useJai ,tileSize});
		Assert.assertNotNull(coverage);
		PlanarImage.wrapRenderedImage( coverage.getRenderedImage()).getTiles();;
	}
	
	
	@Test
	public void buildCachingIndex() throws FileNotFoundException, IOException {
		CatalogBuilder builder = null;
		ImageMosaicReader reader = null;
		FileInputStream inStream = null;
		CatalogBuilderConfiguration c1 = new CatalogBuilderConfiguration();
		c1.setIndexName("shpindex");
		c1.setLocationAttribute("location");
		c1.setAbsolute(false);
		c1.setRootMosaicDirectory(TestData.file(this, "/caching").toString());
		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/caching").toString()));
		Properties prop = new Properties();
		
		
		try {
			
			c1.setCaching(false);
			
	
			// build the index
			builder = new CatalogBuilder(c1);
			builder.addProcessingEventListener(new CatalogBuilderListener());
			builder.run();
			final File relativeMosaic = TestData.file(this, "/caching/" + c1.getIndexName() + ".shp");
			final File propertiesFile = TestData.file(this, "/caching/" + c1.getIndexName() + ".properties");
			assertTrue(relativeMosaic.exists());
			inStream = new FileInputStream(propertiesFile);
			prop.load(inStream);
			String value = prop.getProperty("Caching");
			assertNotNull(value);
			assertTrue (value.toLowerCase().equals("false"));
			
	
			assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
			reader = (ImageMosaicReader) new ImageMosaicReader(relativeMosaic);
			
			GranuleCatalog catalog = reader.rasterManager.granuleCatalog;
			assertTrue(catalog.getClass().toString().endsWith("GTDataStoreGranuleCatalog"));
		} finally {
			try {
				if (inStream != null){
					inStream.close();
				}
			} catch (Throwable t){
				//Eat exception
			}
			
			try {
				if (reader != null){
					reader.dispose();
				}
			} catch (Throwable t){
				//Eat exception
			}
		}
		
		try {
			
			c1.setCaching(true);
	
			// build the index
			builder = new CatalogBuilder(c1);
			builder.addProcessingEventListener(new CatalogBuilderListener());
			builder.run();
			final File relativeMosaic = TestData.file(this, "/caching/" + c1.getIndexName() + ".shp");
			final File propertiesFile = TestData.file(this, "/caching/" + c1.getIndexName() + ".properties");
			inStream = new FileInputStream(propertiesFile);
			prop.load(inStream);
			
			String value = prop.getProperty("Caching");
			assertNotNull(value);
			assertTrue (value.toLowerCase().equals("true"));
			
			assertTrue(relativeMosaic.exists());
	
			assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
			reader = (ImageMosaicReader) new ImageMosaicReader(relativeMosaic);
			
			GranuleCatalog catalog = reader.rasterManager.granuleCatalog;
			assertTrue(catalog.getClass().toString().endsWith("STRTreeGranuleCatalog"));

		} finally {
			try {
				if (inStream != null){
					inStream.close();
				}
			} catch (Throwable t){
				//Eat exception
			}
			
			try {
				if (reader != null){
					reader.dispose();
				}
			} catch (Throwable t){
				//Eat exception
			}
		}
		
	}
}
