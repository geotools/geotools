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
package org.geotools.referencing.crs;

import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

public class URNEPSGTest extends TestCase {

	public void test() throws Exception {
		CoordinateReferenceSystem crs1 = CRS.decode( "EPSG:4326" );
		CoordinateReferenceSystem crs2 = CRS.decode( "urn:x-ogc:def:crs:EPSG:6.11.2:4326" );
		
		assertEquals( crs1, crs2 );
	}
}
