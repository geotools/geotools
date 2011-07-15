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


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.geotools.TestData;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.string.StringFileNameExtractorSPI;
import org.geotools.gce.imagemosaic.properties.time.TimestampFileNameExtractorSPI;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/test/java/org/geotools/gce/imagemosaic/PropertiesCollectorTest.java $
 */
public class PropertiesCollectorTest extends Assert {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
        @Test
        public void test(){
    
            // get the spi
            final Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
            assertNotNull(spis);
            assertTrue(!spis.isEmpty());
            assertEquals(8,spis.size());

    
        }
	
        @Test
        public void testString() throws IOException{
                
                // get the spi
                final Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
                assertNotNull(spis);
                assertTrue(!spis.isEmpty());
                URL testUrl = TestData.url(this,"time_geotiff/stringregex.properties");
                // test a regex
                PropertiesCollectorSPI spi;
                final Iterator<PropertiesCollectorSPI> iterator = spis.iterator();
                while (iterator.hasNext()){
                    spi = iterator.next();
                    if (spi instanceof StringFileNameExtractorSPI){
                        final PropertiesCollector pc = spi.create(testUrl, Arrays.asList("string_attr"));
                        pc.collect(TestData.file(this,"time_geotiff/world.200401.3x5400x2700.tiff"));
                        return;
                    }
                }
                
                assertTrue(false);
                
        }
        
	@Test
	public void testTime() throws IOException{
		
		// get the spi
		final Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
		assertNotNull(spis);
		assertTrue(!spis.isEmpty());
		URL testUrl = TestData.url(this,"time_geotiff/timeregex.properties");
		// test a regex
		PropertiesCollectorSPI spi;
		final Iterator<PropertiesCollectorSPI> iterator = spis.iterator();
		while (iterator.hasNext()){
		    spi = iterator.next();
		    if (spi instanceof TimestampFileNameExtractorSPI){
		        final PropertiesCollector pc = spi.create(testUrl, Arrays.asList("time_attr"));
	                pc.collect(TestData.file(this,"time_geotiff/world.200401.3x5400x2700.tiff"));
	                return;
		    }
		}
		assertTrue(false);
	}

}
