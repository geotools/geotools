/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.driver.CoverageIO;
import org.geotools.coverage.io.driver.Driver;
import org.geotools.coverage.io.geotiff.GeoTiffDriver;
import org.geotools.data.Parameter;
import org.geotools.data.ServiceInfo;
import org.geotools.test.TestData;
import org.opengis.feature.type.Name;
import org.opengis.geometry.Envelope;

/**
 * Testing {@link GeoTiffFormatFactorySpi}.
 * 
 * @author Simone Giannecchini
 * 
 */
public class GeoTiffServiceTest extends TestCase {

	/**
	 * @param arg0
	 */
	public GeoTiffServiceTest(String arg0) {
		super(arg0);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(GeoTiffServiceTest.class);
	}

	public void testIsAvailable() throws Exception {
		for( Driver driver : CoverageIO.getAvailableDrivers() ){
			if( driver instanceof GeoTiffDriver ){
				return;
			}
		}
		fail("GeoTiffDriver not found");
	}
	
	public void testFileT() throws Exception {
//		File file = TestData.file(GeoTiffReaderTest.class, "t.tiff");
//		
//		Map<String,Serializable> params = new HashMap<String,Serializable>();
//		params.put("url",file.toURI().toURL());
//		CoverageAccess access = CoverageIO.connect(params);
//		assertNotNull(access);
//		
//		assertTrue( access.getSupportedAccessTypes().contains( AccessType.READ_ONLY ));
//		assertTrue( access.getDriver() instanceof GeoTiffDriver );
//		assertEquals( params, access.getConnectParameters() );
//		
//		ServiceInfo info = access.getInfo(null);
//		assertNotNull( "info required", info );
//		assertNotNull( "title based on filename", info.getTitle() );
//		assertNotNull( "description", info.getDescription() );
//		assertEquals( "expected source to match file URI", file.toURI(), info.getSource() );
//		
//		// An example of schema would be "GTOPO30" data produce by http://www.usgs.gov/
//		// schema may be: http://edc.usgs.gov/products/elevation/gtopo30/gtopo30.html
//		//
//		assertNotNull( "expected schema to be DEM", info.getSchema() );
//		Map<String, Parameter<?>> parameterInfo;
//		
//		parameterInfo = access.getAccessParameterInfo(AccessType.READ_ONLY);
//		assertNotNull( parameterInfo );
//		
//		parameterInfo = access.getAccessParameterInfo(AccessType.READ_WRITE);
//		assertNotNull( parameterInfo );
//		
//		List<Name> names = access.getNames( null );
//		assertFalse( names.isEmpty() );
//		assertEquals( names.size(), access.getCoveragesNumber(null) );
//		for( Name name : names ){
//			Envelope extent = access.getExtent(name, null );
//			assertNotNull( extent );
//			assertNotNull( extent.getCoordinateReferenceSystem() );
//			assertTrue( extent.getSpan(0) > 0 );
//			assertTrue( extent.getSpan(1) > 0 );
//			
//			CoverageSource source = access.access( name, null, AccessType.READ_ONLY, null, null );
//			assertNotNull( "read access", source );
//			
//			CoverageStore store = (CoverageStore) access.access( name, null, AccessType.READ_WRITE, null, null );
//			assertNotNull( "write access", store );
//			
//		}
	}
}