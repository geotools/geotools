/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wms.xml.test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.schema.Schema;

public class WMSSchemaTest extends TestCase {
    
    public void testSchema() throws URISyntaxException{
        Schema v1 = SchemaFactory.getInstance(new URI("http://www.opengis.net/wms"));
        assertNotNull(v1);
        Schema v2 = WMSSchema.getInstance();
        assertNotNull(v2);
        assertEquals(v1,v2);
    }

	public void testGetCapabilities() throws Exception {
		
		File getCaps = TestData.file(this, "1.3.0Capabilities.xml");
        URL getCapsURL = getCaps.toURI().toURL();

		Object object = DocumentFactory.getInstance(getCapsURL.openStream(), null, Level.WARNING);

        Schema schema = WMSSchema.getInstance();
		SchemaFactory.getInstance(WMSSchema.NAMESPACE);
				
		assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);
		
		WMSCapabilities capabilities = (WMSCapabilities) object;
		
		assertEquals(capabilities.getVersion(), "1.3.0");
		assertEquals(capabilities.getService().getName(), "WMS");
		assertEquals(capabilities.getService().getTitle(), "World Map");
		assertEquals(capabilities.getService().get_abstract(), "None");
		assertEquals(capabilities.getService().getOnlineResource(), new URL("http://www2.demis.nl"));
		
		assertEquals(capabilities.getService().getLayerLimit(), 40);
		assertEquals(capabilities.getService().getMaxWidth(), 2000);
		assertEquals(capabilities.getService().getMaxHeight(), 2000);
		
		assertEquals(capabilities.getRequest().getGetCapabilities().getFormats().get(0), "text/xml");
		assertEquals(capabilities.getRequest().getGetCapabilities().getGet(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
		assertEquals(capabilities.getRequest().getGetCapabilities().getPost(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
		
		assertEquals(capabilities.getRequest().getGetMap().getFormats().size(), 5);
		assertEquals(capabilities.getRequest().getGetMap().getFormats().get(0), "image/gif");
		assertEquals(capabilities.getRequest().getGetMap().getFormats().get(1), "image/png");
		assertEquals(capabilities.getRequest().getGetMap().getFormats().get(2), "image/jpeg");
		assertEquals(capabilities.getRequest().getGetMap().getFormats().get(3), "image/bmp");
		assertEquals(capabilities.getRequest().getGetMap().getFormats().get(4), "image/swf");
		assertEquals(capabilities.getRequest().getGetMap().getGet(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
		
		assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().size(), 4);
		assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(0), "text/xml");
		assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(1), "text/plain");
		assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(2), "text/html");
		assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(3), "text/swf");
		assertEquals(capabilities.getRequest().getGetFeatureInfo().getGet(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
		
		Layer topLayer = (Layer) capabilities.getLayerList().get(0);
		assertNotNull(topLayer);
		assertNull(topLayer.getParent());
		assertFalse(topLayer.isQueryable());
		assertEquals(topLayer.getTitle(), "World Map");
		assertEquals(topLayer.getSrs().size(), 1);
		assertTrue(topLayer.getSrs().contains("CRS:84"));
		
		CRSEnvelope llbbox = topLayer.getLatLonBoundingBox();
		assertNotNull(llbbox);
		assertEquals(llbbox.getMinX(), -180, 0.0);
		assertEquals(llbbox.getMaxX(), 180, 0.0);
		assertEquals(llbbox.getMinY(), -90, 0.0);
		assertEquals(llbbox.getMaxY(), 90, 0.0);
		
		assertEquals(topLayer.getBoundingBoxes().size(), 1);
		
		CRSEnvelope bbox = (CRSEnvelope) topLayer.getBoundingBoxes().get("CRS:84");
		assertNotNull(bbox);
		assertEquals(bbox.getEPSGCode(), "CRS:84");
		assertEquals(bbox.getMinX(), -184, 0.0);
		assertEquals(bbox.getMaxX(), 180, 0.0);
		assertEquals(bbox.getMinY(), -90.0000000017335, 0.0);
		assertEquals(bbox.getMaxY(), 90, 0.0);
		
		Layer layer = (Layer) capabilities.getLayerList().get(1);
		assertEquals(layer.getParent(), topLayer);
		assertTrue(layer.isQueryable());
		assertEquals(layer.getName(), "Bathymetry");
		assertEquals(layer.getTitle(), "Bathymetry");
		
		// Added test to verify inheritance, should be same as previous llbbox
		llbbox = layer.getLatLonBoundingBox();
		assertNotNull(llbbox);
		assertEquals(llbbox.getMinX(), -180, 0.0);
		assertEquals(llbbox.getMaxX(), 180, 0.0);
		assertEquals(llbbox.getMinY(), -90, 0.0);
		assertEquals(llbbox.getMaxY(), 90, 0.0);
		
		bbox = (CRSEnvelope) layer.getBoundingBoxes().get("CRS:84");
		assertNotNull(bbox);
		assertEquals(bbox.getEPSGCode(), "CRS:84");
		assertEquals(bbox.getMinX(), -180, 0.0);
		assertEquals(bbox.getMaxX(), 180, 0.0);
		assertEquals(bbox.getMinY(), -90, 0.0);
		assertEquals(bbox.getMaxY(), 90, 0.0);
		
		assertEquals(capabilities.getLayerList().size(), 21);
		
		layer = (Layer) capabilities.getLayerList().get(20);
		assertEquals(layer.getParent(), topLayer);
		assertTrue(layer.isQueryable());
		assertEquals(layer.getName(), "Ocean features");
		assertEquals(layer.getTitle(), "Ocean features");
		
		// Added test to verify inheritance, should be same as previous llbbox
		llbbox = layer.getLatLonBoundingBox();
		assertNotNull(llbbox);
		assertEquals(llbbox.getMinX(), -180, 0.0);
		assertEquals(llbbox.getMaxX(), 180, 0.0);
		assertEquals(llbbox.getMinY(), -90, 0.0);
		assertEquals(llbbox.getMaxY(), 90, 0.0);
		
		bbox = (CRSEnvelope) layer.getBoundingBoxes().get("CRS:84");
		assertNotNull(bbox);
		assertEquals(bbox.getEPSGCode(), "CRS:84");
		assertEquals(bbox.getMinX(), -180, 0.0);
		assertEquals(bbox.getMaxX(), 179.999420166016, 0.0);
		assertEquals(bbox.getMinY(), -62.9231796264648, 0.0);
		assertEquals(bbox.getMaxY(), 68.6906585693359, 0.0);
		
	}
}
