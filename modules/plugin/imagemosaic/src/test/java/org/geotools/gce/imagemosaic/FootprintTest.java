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

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.imagemosaic.IndexBuilder.IndexBuilderConfiguration;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
/**
 * Testing footprint management.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.6.x/modules/plugin/imagemosaic/src/test/java/org/geotools/gce/imagemosaic/FootprintTest.java $
 */
public class FootprintTest extends Assert {
	
	
		@Test
        public void footprintTest() throws FileNotFoundException, IOException, CloneNotSupportedException{
            final File file = TestData.file(this, "footprint");
            final String datapath = file.getAbsolutePath();
                IndexBuilderConfiguration c1= new IndexBuilderConfiguration();
                c1.setAbsolute(false);
                c1.setIndexName("mosaic_index");
                c1.setLocationAttribute("location");
                c1.setRootMosaicDirectory(datapath);
                c1.setIndexingDirectories(Arrays.asList(datapath));
                assertNotNull(c1.toString());
                
                //build the index
                IndexBuilder builder= new IndexBuilder(c1);
                builder.run();
                ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicReader(datapath);

                // limit yourself to reading just a bit of it
                ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
                GeneralEnvelope envelope = reader.getOriginalEnvelope();
                Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
                GridEnvelope2D range= new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range,envelope));
                
                // use imageio with defined tiles
                final ParameterValue<Boolean> useJai = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
                useJai.setValue(true);
                
                final ParameterValue<Boolean> handleFootprint = ImageMosaicFormat.HANDLE_FOOTPRINT.createValue();
                handleFootprint.setValue(true);
                
                final ParameterValue<String> tileSize = ImageMosaicFormat.SUGGESTED_TILE_SIZE.createValue();
                tileSize.setValue("128,128");
                
                // Test the output coverage
                GridCoverage2D coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] {handleFootprint,useJai,gg,tileSize});

                // Test the output coverage
                Assert.assertNotNull(coverage);
        }
		@Before
		public void setUp() throws Exception {
			//force initial ImageIO set up and reordering
			new ImageMosaicFormatFactory();
		
		}
	
}
