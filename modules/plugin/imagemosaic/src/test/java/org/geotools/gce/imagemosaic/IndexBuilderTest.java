package org.geotools.gce.imagemosaic;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.imagemosaic.IndexBuilder.ExceptionEvent;
import org.geotools.gce.imagemosaic.IndexBuilder.IndexBuilderConfiguration;
import org.geotools.gce.imagemosaic.IndexBuilder.ProcessingEvent;
import org.geotools.gce.imagemosaic.IndexBuilder.ProcessingEventListener;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
/**
 * Testing {@link IndexBuilder} and its related subclasses.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL$
 */
public class IndexBuilderTest extends Assert {
	
	private final class IndexBuilderListener extends ProcessingEventListener{

		@Override
		void exceptionOccurred(ExceptionEvent event) {
			throw new RuntimeException(event.getException());
			
		}

		@Override
		void getNotification(ProcessingEvent event) {
			
		}
		
	}

	@Test
//	@Ignore
	public void indexBuilderConfiguration() throws FileNotFoundException, IOException, CloneNotSupportedException{
		// create a stub configuration
		final IndexBuilderConfiguration c1= new IndexBuilderConfiguration();
		c1.setAbsolute(true);
		c1.setIndexName("index");
		c1.setLocationAttribute("location");
		c1.setRootMosaicDirectory(TestData.file(this,"/rgb").toString());
		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/rgb").toString()));
		assertNotNull(c1.toString());
		
		// create a second stub configuration
		final IndexBuilderConfiguration c2= new IndexBuilderConfiguration();
		c2.setAbsolute(true);
		c2.setIndexName("index");
		c2.setLocationAttribute("location");
		c2.setRootMosaicDirectory(TestData.file(this,"/rgb").toString());
		c2.setIndexingDirectories(Arrays.asList(TestData.file(this,"/rgb").toString()));
		assertTrue(c1.equals(c2));
		assertEquals(c1.hashCode(), c2.hashCode());
		
		IndexBuilderConfiguration c3 = c2.clone();
		assertTrue(c3.equals(c2));
		assertEquals(c3.hashCode(), c2.hashCode());
		
		//check errors
		final IndexBuilderConfiguration c4= new IndexBuilderConfiguration();
		assertNotNull(c4.toString());
		
	}
	
	@Test
	public void buildIndex() throws FileNotFoundException, IOException{
		
		//build a relative index and then make it run
		IndexBuilderConfiguration c1= new IndexBuilderConfiguration();
		c1.setAbsolute(true);
		c1.setIndexName("shpindex");
		c1.setLocationAttribute("location");
		c1.setAbsolute(false);
		c1.setRootMosaicDirectory(TestData.file(this,"/overview").toString());
		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/overview/0").toString()));
		assertNotNull(c1.toString());		
		//build the index
		IndexBuilder builder= new IndexBuilder(c1);
		builder.addProcessingEventListener(new IndexBuilderListener());
		builder.run();
		final File relativeMosaic=TestData.file(this,"/overview/"+c1.getIndexName()+".shp");
		assertTrue(relativeMosaic.exists());
		
		assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
		ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicReader(relativeMosaic);

		// limit yourself to reading just a bit of it
		ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
		GeneralEnvelope envelope = reader.getOriginalEnvelope();
		Dimension dim= new Dimension();
		dim.setSize(reader.getOriginalGridRange().getSpan(0)/2.0, reader.getOriginalGridRange().getSpan(1)/2.0);
		Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
		rasterArea.setSize(dim);
		GridEnvelope2D range= new GridEnvelope2D(rasterArea);
		gg.setValue(new GridGeometry2D(range,envelope));
		
		// use imageio with defined tiles
		final ParameterValue<Boolean> useJai = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
		useJai.setValue(false);
		
		final ParameterValue<String> tileSize = ImageMosaicFormat.SUGGESTED_TILE_SIZE.createValue();
		tileSize.setValue("128,128");
		
		// Test the output coverage
		GridCoverage2D coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg,useJai ,tileSize});
		Assert.assertNotNull(coverage);
		PlanarImage.wrapRenderedImage( coverage.getRenderedImage()).getTiles();;

		
		//build an absolute index and then make it run
		c1= new IndexBuilderConfiguration();
		c1.setAbsolute(true);
		c1.setIndexName("shpindex_absolute");
		c1.setLocationAttribute("location");
		c1.setAbsolute(true);
		c1.setRootMosaicDirectory(TestData.file(this,"/overview").toString());
		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/overview/0").toString()));
		assertNotNull(c1.toString());		
		//build the index
		builder= new IndexBuilder(c1);
		builder.addProcessingEventListener(new IndexBuilderListener());
		builder.run();
		final File absoluteMosaic=TestData.file(this,"/overview/"+c1.getIndexName()+".shp");
		assertTrue(absoluteMosaic.exists());
		
		assertTrue(new ImageMosaicFormat().accepts(absoluteMosaic));
		reader = (ImageMosaicReader) new ImageMosaicReader(absoluteMosaic);

		// limit yourself to reading just a bit of it
		gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
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

	@Before
	public void setUp() throws Exception {
		//force initial ImageIO set up and reordering
		new ImageMosaicFormatFactory();
	
	}
}
