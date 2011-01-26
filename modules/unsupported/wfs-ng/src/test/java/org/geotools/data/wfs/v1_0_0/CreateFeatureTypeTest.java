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
package org.geotools.data.wfs.v1_0_0;

import java.io.InputStream;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.schema.Schema;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Jesse
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/test/java/org/geotools/data/wfs/v1_0_0/CreateFeatureTypeTest.java $
 */
public class CreateFeatureTypeTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testSimple() throws Exception {
		InputStream in = TestData.openStream("xml/feature-type-simple.xsd");
		try{
	        Schema schema = SchemaFactory.getInstance(null, in);
	        SimpleFeatureType ft = WFS_1_0_0_DataStore.parseDescribeFeatureTypeResponse("WATER", schema);
	        assertNotNull(ft);
	        assertEquals(Integer.class, ft.getDescriptor("ID").getType().getBinding());
	        assertEquals(String.class, ft.getDescriptor("CODE").getType().getBinding());
	        assertEquals(Float.class, ft.getDescriptor("KM").getType().getBinding());
	        assertEquals(Polygon.class, ft.getDescriptor("GEOM").getType().getBinding());
		}finally{
			in.close();
		}
	}

	public void testChoiceGeom() throws Exception {
		InputStream in = TestData.openStream("xml/feature-type-choice.xsd");
		try{
	        Schema schema = SchemaFactory.getInstance(null, in);
	        SimpleFeatureType ft = WFS_1_0_0_DataStore.parseDescribeFeatureTypeResponse("WATER", schema);
	        assertNotNull(ft);
	        assertEquals(Integer.class, ft.getDescriptor("ID").getType().getBinding());
	        assertEquals(String.class, ft.getDescriptor("CODE").getType().getBinding());
	        assertEquals(Float.class, ft.getDescriptor("KM").getType().getBinding());
	        assertEquals(MultiPolygon.class, ft.getDescriptor("GEOM").getType().getBinding());
		}finally{
			in.close();
		}
	}
	
}
