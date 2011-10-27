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
package org.geotools.filter.v2_0.bindings;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Within;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Polygon;

public class FilterTypeBindingTest extends FESTestSupport {

    public void testParseId() throws Exception {
        String xml = 
            "<fes:Filter xmlns:fes='http://www.opengis.net/fes/2.0'>" + 
                "<fes:ResourceId rid='InWaterA_1M.1234'/>" + 
            "</fes:Filter>";
        
        buildDocument(xml);
        Id f = (Id) parse();
        assertNotNull(f);
        
        assertEquals(1, f.getIdentifiers().size());
        assertEquals("InWaterA_1M.1234", f.getIdentifiers().iterator().next().getID());
    }
    
    public void testParseSpatial() throws Exception {
        String xml = 
            "<fes:Filter" + 
            "   xmlns:fes='http://www.opengis.net/fes/2.0' " + 
            "   xmlns:gml='http://www.opengis.net/gml/3.2' " +
            "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
            "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd" + 
            " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'> " + 
            "   <fes:Overlaps> " + 
            "      <fes:ValueReference>Geometry</fes:ValueReference> " + 
            "      <gml:Polygon gml:id='P1' srsName='urn:ogc:def:crs:EPSG::4326'> " + 
            "         <gml:exterior> " + 
            "            <gml:LinearRing> " + 
            "               <gml:posList>10 10 20 20 30 30 40 40 10 10</gml:posList> " + 
            "            </gml:LinearRing> " + 
            "         </gml:exterior> " + 
            "      </gml:Polygon> " + 
            "   </fes:Overlaps> " + 
            "</fes:Filter> ";
        buildDocument(xml);
        
        Overlaps f = (Overlaps) parse();
        assertNotNull(f);
        
        PropertyName e1 = (PropertyName) f.getExpression1();
        assertEquals("Geometry", e1.getPropertyName());
        
        Literal e2 = (Literal) f.getExpression2();
        assertTrue(e2.getValue() instanceof Polygon);
    }
    
    public void testParseLogical() throws Exception {
        String xml = 
            "<fes:Filter " + 
            "   xmlns:fes='http://www.opengis.net/fes/2.0' " + 
            "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
            "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 " + 
            "   http://schemas.opengis.net/filter/2.0/filterAll.xsd'> " + 
            "   <fes:And> " + 
            "      <fes:Or> " + 
            "         <fes:PropertyIsEqualTo> " + 
            "            <fes:ValueReference>FIELD1</fes:ValueReference> " + 
            "            <fes:Literal>10</fes:Literal> " + 
            "         </fes:PropertyIsEqualTo> " + 
            "         <fes:PropertyIsEqualTo> " + 
            "            <fes:ValueReference>FIELD1</fes:ValueReference> " + 
            "            <fes:Literal>20</fes:Literal> " + 
            "         </fes:PropertyIsEqualTo> " + 
            "      </fes:Or> " + 
            "      <fes:PropertyIsEqualTo> " + 
            "         <fes:ValueReference>STATUS</fes:ValueReference> " + 
            "         <fes:Literal>VALID</fes:Literal> " + 
            "      </fes:PropertyIsEqualTo> " + 
            "   </fes:And> " + 
            "</fes:Filter> ";
        buildDocument(xml);

        And f = (And) parse();
        assertNotNull(f);
        assertEquals(2, f.getChildren().size());
        
        Or f1 = (Or) f.getChildren().get(0);
        assertEquals(2, f1.getChildren().size());
        
        PropertyIsEqualTo f11 = (PropertyIsEqualTo) f1.getChildren().get(0);
        assertEquals("FIELD1", ((PropertyName)f11.getExpression1()).getPropertyName());
        assertEquals("10", ((Literal)f11.getExpression2()).evaluate(null, String.class));
        
        PropertyIsEqualTo f12 = (PropertyIsEqualTo) f1.getChildren().get(1);
        assertEquals("FIELD1", ((PropertyName)f12.getExpression1()).getPropertyName());
        assertEquals("20", ((Literal)f12.getExpression2()).evaluate(null, String.class));
        
        PropertyIsEqualTo f2 = (PropertyIsEqualTo) f.getChildren().get(1);
        assertEquals("STATUS", ((PropertyName)f2.getExpression1()).getPropertyName());
        assertEquals("VALID", ((Literal)f2.getExpression2()).evaluate(null, String.class));
    }
    
    public void testParse1() throws Exception {
        String xml = 
            "<fes:Filter " + 
            "   xmlns:fes='http://www.opengis.net/fes/2.0' " + 
            "   xmlns:gml='http://www.opengis.net/gml/3.2' " + 
            "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
            "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 " + 
            "   http://schemas.opengis.net/filter/2.0/filterAll.xsd " + 
            "   http://www.opengis.net/gml/3.2 " + 
            "   http://schemas.opengis.net/gml/3.2.1/gml.xsd'> " + 
            "   <fes:And> " + 
            "      <fes:Within> " + 
            "         <fes:ValueReference>WKB_GEOM</fes:ValueReference> " + 
            "         <gml:Polygon gml:id='P1' srsName='urn:ogc:def:crs:EPSG::4326'> " + 
            "            <gml:exterior> " + 
            "               <gml:LinearRing> " + 
            "                  <gml:posList>10 10 20 20 30 30 40 40 10 10</gml:posList> " + 
            "               </gml:LinearRing> " + 
            "            </gml:exterior> " + 
            "         </gml:Polygon> " + 
            "      </fes:Within> " + 
            "      <fes:PropertyIsBetween> " + 
            "         <fes:ValueReference>DEPTH</fes:ValueReference> " + 
            "         <fes:LowerBoundary> " + 
            "            <fes:Literal>400</fes:Literal> " + 
            "         </fes:LowerBoundary> " + 
            "         <fes:UpperBoundary> " + 
            "            <fes:Literal>800</fes:Literal> " + 
            "         </fes:UpperBoundary> " + 
            "      </fes:PropertyIsBetween> " + 
            "   </fes:And> " + 
            "</fes:Filter>"; 
        
        buildDocument(xml);

        And f = (And) parse();
        assertNotNull(f);
        assertEquals(2, f.getChildren().size());
        
        Within f1 = (Within) f.getChildren().get(0);
        assertEquals("WKB_GEOM", ((PropertyName)f1.getExpression1()).getPropertyName());
        assertTrue(f1.getExpression2().evaluate(null) instanceof Polygon);
        
        PropertyIsBetween f2 = (PropertyIsBetween) f.getChildren().get(1);
        assertEquals("DEPTH", ((PropertyName)f2.getExpression()).getPropertyName());
        assertEquals(400, f2.getLowerBoundary().evaluate(null, Integer.class).intValue());
        assertEquals(800, f2.getUpperBoundary().evaluate(null, Integer.class).intValue());
    }

    public void testEncodeId() throws Exception {
        Document doc = encode(FilterMockData.id(), FES.Filter);
        assertEquals("fes:Filter", doc.getDocumentElement().getNodeName());
        
        assertEquals(3, getElementsByQName(doc, FES.ResourceId).getLength());
    }

    public void testEncodeRsourceId() throws Exception {
        Document doc = encode(FilterMockData.resourceId(), FES.Filter);
        assertEquals("fes:Filter", doc.getDocumentElement().getNodeName());
        //print(doc);

        assertEquals(4, getElementsByQName(doc, FES.ResourceId).getLength());

    }

    public void testEncodeSpatial() throws Exception {
        Document doc = encode(FilterMockData.intersects(), FES.Filter);
        assertEquals("fes:Filter", doc.getDocumentElement().getNodeName());
        
        Element e = getElementByQName(doc, FES.Intersects);
        assertNotNull(e);

        assertNotNull(getElementByQName(e, FES.ValueReference));
        assertNotNull(getElementByQName(e, FES.Literal));
    }
    
    public void testEncodeComparison() throws Exception {
        Document doc = encode(FilterMockData.propertyIsEqualTo(), FES.Filter);
        assertEquals("fes:Filter", doc.getDocumentElement().getNodeName());
        
        Element e = getElementByQName(doc, FES.PropertyIsEqualTo);
        assertNotNull(e);
        assertEquals("true", e.getAttribute("matchCase"));
        
        assertNotNull(getElementByQName(e, FES.ValueReference));
        assertNotNull(getElementByQName(e, FES.Literal));
    }
    
    public void testEncodeLogic() throws Exception {
        Document doc = encode(FilterMockData.and(), FES.Filter);
        assertEquals("fes:Filter", doc.getDocumentElement().getNodeName());
        
        Element e = getElementByQName(doc, FES.And);
        assertNotNull(e);
        
        assertNotNull(getElementByQName(e, FES.PropertyIsEqualTo));
        assertNotNull(getElementByQName(e, FES.PropertyIsNotEqualTo));
    }
    
    public void testParseGmlWithoutSchemaLocation() throws Exception {
        String xml = 
        "<fes:Filter xmlns:gml='http://www.opengis.net/gml/3.2' xmlns:fes='http://www.opengis.net/fes/2.0'> " + 
        " <fes:BBOX> " + 
        "  <fes:ValueReference>it.geosolutions:the_geom</fes:ValueReference> " + 
        "  <gml:Envelope srsName='urn:ogc:def:crs:EPSG::2154'> " + 
        "   <gml:lowerCorner>834021 6504682</gml:lowerCorner> " + 
        "   <gml:upperCorner>943396 6604240</gml:upperCorner> " + 
        "  </gml:Envelope> " + 
        " </fes:BBOX> " + 
        "</fes:Filter> ";
        buildDocument(xml);
        BBOX f = (BBOX) parse();
        assertTrue(f.getExpression1() instanceof PropertyName);
        assertTrue(f.getExpression2() instanceof Literal);
    }

    public void testParseExtendedOperator() throws Exception {
        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        String xml = 
            "<fes:Filter " + 
              "xmlns:fes='http://www.opengis.net/fes/2.0' xmlns:myops='http://www.someserver.com/myops/1.0'> " + 
            "  <myops:strMatches> " + 
            "   <fes:ValueReference>bar</fes:ValueReference> " +
            "   <fes:Literal>baz</fes:Literal> " +
            "  </myops:strMatches> " + 
            "</fes:Filter>"; 
        buildDocument(xml);

        PropertyIsEqualTo f = (PropertyIsEqualTo) parse();
        assertTrue(f.getExpression1() instanceof Function);
        assertTrue(f.getExpression2() instanceof Literal);
        
        xml = 
            "<fes:Filter " + 
              "xmlns:fes='http://www.opengis.net/fes/2.0' xmlns:myops='http://www.someserver.com/myops/1.0'> " + 
            " <fes:And> " + 
            "  <myops:strMatches> " + 
            "   <fes:ValueReference>bar</fes:ValueReference> " +
            "   <fes:Literal>baz</fes:Literal> " +
            "  </myops:strMatches> " +
            "  <fes:PropertyIsBetween> " + 
               "<fes:ValueReference>Person/age</fes:ValueReference> " + 
            "   <fes:LowerBoundary> " + 
            "    <fes:Literal>18</fes:Literal> " + 
            "   </fes:LowerBoundary> " + 
            "   <fes:UpperBoundary> " + 
            "    <fes:Literal>200</fes:Literal> " + 
            "   </fes:UpperBoundary> " + 
            "  </fes:PropertyIsBetween> " + 
            " </fes:And> " + 
            "</fes:Filter>"; 
        buildDocument(xml);

        And a = (And) parse();
        List<Filter> ch = a.getChildren();
        
        assertEquals(2, ch.size());
        
        assertTrue(ch.get(0) instanceof PropertyIsEqualTo || ch.get(0) instanceof PropertyIsBetween);
        assertTrue(ch.get(1) instanceof PropertyIsEqualTo || ch.get(1) instanceof PropertyIsBetween);
        
    }
}
