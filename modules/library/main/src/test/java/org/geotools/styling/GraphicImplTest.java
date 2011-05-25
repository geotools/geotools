/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.net.URL;

import junit.framework.TestCase;

/**
 * This test case captures specific problems encountered with the GraphicImpl code.
 *
 *
 *
 * @source $URL$
 */
public class GraphicImplTest extends TestCase {

	/**
	 * Checks if creating a Graphic with an ExternalGraphics works.
	 */
	public void testWithExternalGraphics() throws Exception {
		StyleBuilder sb = new StyleBuilder();
		
        URL urlExternal = getClass().getResource("/data/sld/blob.gif");
        ExternalGraphic extg = sb.createExternalGraphic(urlExternal, "image/svg+xml");
        Graphic graphic = sb.createGraphic(extg, null, null);
        
        
		assertEquals(1, graphic.graphicalSymbols().size());
	}
	
	/**
	 * Checks if the Displacement settings are exported to XML
	 */
	public void testDisplacement() throws Exception {
           StyleBuilder sb = new StyleBuilder();
           
           Graphic graphic;
           {
               graphic = sb.createGraphic();
               Displacement disp = sb.createDisplacement(10.1, -5.5);
               graphic.setDisplacement(disp);
           }
           
           Displacement disp = graphic.getDisplacement();
           assertNotNull(disp);
           
           assertEquals(disp.getDisplacementX().toString(),"10.1");
           assertEquals(disp.getDisplacementY().toString(),"-5.5");
           
	}
}
