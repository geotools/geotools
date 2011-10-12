/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import net.opengis.wfs20.DropStoredQueryType;

import org.geotools.wfs.v2_0.WFSTestSupport;

public class CreateStoredQueryTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<wfs:DropStoredQuery " + 
            "   xmlns:wfs='http://www.opengis.net/wfs/2.0' " + 
            "   service='WFS' " + 
            "   version='2.0.0' id='foobar'/> ";
            
        buildDocument(xml);
        
        DropStoredQueryType dsq = (DropStoredQueryType) parse();
        assertNotNull(dsq);
        
        assertEquals("foobar", dsq.getId());
    }
}
