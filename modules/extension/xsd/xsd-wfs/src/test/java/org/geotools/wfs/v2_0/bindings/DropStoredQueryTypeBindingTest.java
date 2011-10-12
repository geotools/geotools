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

import net.opengis.wfs20.CreateStoredQueryType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.StoredQueryDescriptionType;

import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFSTestSupport;

public class DropStoredQueryTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<wfs:CreateStoredQuery " + 
            "   xmlns:wfs='http://www.opengis.net/wfs/2.0' " + 
            "   xmlns:fes='http://www.opengis.org/fes/2.0' " + 
            "   xmlns:gml='http://www.opengis.net/gml/3.2' " + 
            "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
            "   xmlns:myns='http://www.someserver.com/myns' " + 
            "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 ../../wfs.xsd' " + 
            "   service='WFS' " + 
            "   version='2.0.0'> " + 
            "   <wfs:StoredQueryDefinition id='urn:StoredQueries:FeaturesInPolygon'> " + 
            "      <wfs:Title>Features In Polygon</wfs:Title> " + 
            "      <wfs:Abstract>Find all the features in a Polygon.</wfs:Abstract> " + 
            "      <wfs:Parameter name='AreaOfInterest' type='gml:Polygon'/> " + 
            "      <wfs:QueryExpressionText " + 
            "           returnFeatureTypes='myns:Parks myns:Lakes myns:Rivers' " + 
            "           language='urn:ogc:def:queryLanguage:OGC-WFS::WFS_QueryExpression' " + 
            "           isPrivate='false'> " + 
            "         <wfs:Query typeNames='myns:Parks'> " + 
            "            <fes:Filter> " + 
            "               <fes:Within> " + 
            "                  <fes:ValueReference>geometry</fes:ValueReference> " + 
            "                  ${AreaOfInterest} " + 
            "               </fes:Within> " + 
            "            </fes:Filter> " + 
            "         </wfs:Query> " + 
            "         <wfs:Query typeNames='myns:Lakes'> " + 
            "            <fes:Filter> " + 
            "               <fes:Within> " + 
            "                  <fes:ValueReference>region</fes:ValueReference> " + 
            "                  ${AreaOfInterest} " + 
            "               </fes:Within> " + 
            "            </fes:Filter> " + 
            "         </wfs:Query> " + 
            "         <wfs:Query typeNames='myns:Rivers'> " + 
            "            <fes:Filter> " + 
            "               <fes:Within> " + 
            "                  <fes:ValueReference>region</fes:ValueReference> " + 
            "                  ${AreaOfInterest} " + 
            "               </fes:Within> " + 
            "            </fes:Filter> " + 
            "         </wfs:Query> " + 
            "      </wfs:QueryExpressionText> " + 
            "   </wfs:StoredQueryDefinition> " + 
            "</wfs:CreateStoredQuery> ";
        buildDocument(xml);
        
        CreateStoredQueryType csq = (CreateStoredQueryType) parse();
        assertNotNull(csq);
        
        assertEquals(1, csq.getStoredQueryDefinition().size());
        
        StoredQueryDescriptionType  sqd = csq.getStoredQueryDefinition().get(0);
        assertEquals("Features In Polygon", sqd.getTitle().get(0).getValue());
        
        assertEquals(1, sqd.getParameter().size());
        ParameterExpressionType pe = sqd.getParameter().get(0);
        assertEquals("AreaOfInterest", pe.getName());
        assertEquals(GML.Polygon, pe.getType());
        
        assertEquals(1, sqd.getQueryExpressionText().size());
        QueryExpressionTextType qet = sqd.getQueryExpressionText().get(0);
        assertEquals("urn:ogc:def:queryLanguage:OGC-WFS::WFS_QueryExpression", qet.getLanguage());
        assertFalse(qet.isIsPrivate());
        assertEquals(3, qet.getReturnFeatureTypes().size());
        assertNotNull(qet.getValue());
        assertTrue(qet.getValue().contains("wfs:Query"));
    }
}
