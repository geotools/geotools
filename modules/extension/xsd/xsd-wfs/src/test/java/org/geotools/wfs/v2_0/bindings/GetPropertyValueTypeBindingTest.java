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

import java.net.URI;

import javax.xml.namespace.QName;

import net.opengis.wfs20.GetPropertyValueType;
import net.opengis.wfs20.QueryType;

import org.geotools.wfs.v2_0.WFSTestSupport;
import org.opengis.filter.Id;

public class GetPropertyValueTypeBindingTest extends WFSTestSupport {

    public void testParse1() throws Exception {
        String xml = 
        "<wfs:GetPropertyValue service='WFS' version='2.0.0' valueReference='foo'" + 
        "   xmlns:myns='http://www.someserver.com/myns' " + 
        "   xmlns:wfs='http://www.opengis.net/wfs/2.0' " + 
        "   xmlns:fes='http://www.opengis.net/fes/2.0' " + 
        "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
        "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 " + 
        "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> " + 
        "   <wfs:Query typeNames='myns:InWaterA_1M' srsName='epsg:4326'> " + 
        "      <fes:Filter> " + 
        "         <fes:ResourceId rid='InWaterA_1M.1234'/> " + 
        "      </fes:Filter> " + 
        "   </wfs:Query> " + 
        "</wfs:GetPropertyValue> ";
        
        buildDocument(xml);
        
        GetPropertyValueType gf = (GetPropertyValueType) parse();
        assertNotNull(gf);
        assertEquals("foo", gf.getValueReference());
        QueryType q = (QueryType) gf.getAbstractQueryExpression();
        assertNotNull(q);
        
        assertEquals(new URI("epsg:4326"), q.getSrsName());
        assertEquals(1, q.getTypeNames().size());
        assertEquals(
            new QName("http://www.someserver.com/myns", "InWaterA_1M"), q.getTypeNames().get(0));
        
        Id f = (Id) q.getFilter();
        assertNotNull(f);
        
        assertEquals(1, f.getIdentifiers().size());
        assertEquals("InWaterA_1M.1234", f.getIdentifiers().iterator().next().getID());
    }
}
