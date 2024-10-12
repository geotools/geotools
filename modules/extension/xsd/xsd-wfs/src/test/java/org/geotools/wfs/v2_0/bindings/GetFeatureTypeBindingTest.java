package org.geotools.wfs.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import javax.xml.namespace.QName;
import net.opengis.wfs20.AllSomeType;
import net.opengis.wfs20.GetFeatureType;
import net.opengis.wfs20.GetFeatureWithLockType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.ResultTypeType;
import net.opengis.wfs20.StoredQueryType;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.junit.Test;

public class GetFeatureTypeBindingTest extends WFSTestSupport {
    @Test
    public void testParse1() throws Exception {
        String xml = "<wfs:GetFeature "
                + "   service='WFS' "
                + "   version='2.0.0' "
                + "   outputFormat='application/gml+xml; version=3.2' "
                + "   xmlns:myns='http://www.someserver.com/myns' "
                + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                + "   <wfs:Query typeNames='myns:InWaterA_1M' srsName='epsg:4326'> "
                + "      <fes:Filter> "
                + "         <fes:ResourceId rid='InWaterA_1M.1234'/> "
                + "      </fes:Filter> "
                + "   </wfs:Query> "
                + "</wfs:GetFeature> ";

        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);

        assertEquals(1, gf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);

        assertEquals(new URI("epsg:4326"), q.getSrsName());
        assertEquals(1, q.getTypeNames().size());
        assertEquals(
                new QName("http://www.someserver.com/myns", "InWaterA_1M"),
                q.getTypeNames().get(0));

        Id f = (Id) q.getFilter();
        assertNotNull(f);

        assertEquals(1, f.getIdentifiers().size());
        assertEquals("InWaterA_1M.1234", f.getIdentifiers().iterator().next().getID());
    }

    @Test
    public void testParse2() throws Exception {
        String xml = "<wfs:GetFeature count='100'"
                + "   service='WFS' "
                + "   version='2.0.0' "
                + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:myns='http://www.someserver.com/myns' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                + "   <wfs:Query typeNames='myns:InWaterA_1M'> "
                + "      <wfs:PropertyName>myns:wkbGeom</wfs:PropertyName> "
                + "      <wfs:PropertyName>myns:tileId</wfs:PropertyName> "
                + "      <wfs:PropertyName>myns:facId</wfs:PropertyName> "
                + "      <fes:Filter> "
                + "         <fes:ResourceId rid='InWaterA_1M.1013'/> "
                + " "
                + "      </fes:Filter> "
                + "   </wfs:Query> "
                + "</wfs:GetFeature> ";
        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);

        assertEquals(BigInteger.valueOf(100), gf.getCount());
        assertEquals(1, gf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);

        assertNotNull(q.getFilter());
        assertEquals(3, q.getPropertyNames().size());

        assertEquals(
                new QName("http://www.someserver.com/myns", "wkbGeom"),
                q.getPropertyNames().get(0));
        assertEquals(
                new QName("http://www.someserver.com/myns", "tileId"),
                q.getPropertyNames().get(1));
        assertEquals(
                new QName("http://www.someserver.com/myns", "facId"),
                q.getPropertyNames().get(2));
    }

    @Test
    public void testParse3() throws Exception {
        String xml = "<GetFeature "
                + "   version='2.0.0' "
                + "   service='WFS' "
                + "   xmlns='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:myns='http://www.someserver.com/myns' "
                + "   xmlns:yourns='http://demo.cubewerx.com/yourns' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                + "   <Query typeNames='myns:InWaterA_1M'/> "
                + "   <Query typeNames='myns:BuiltUpA_1M'/> "
                + "   <Query typeNames='yourns:RoadL_1M'/> "
                + "</GetFeature> ";
        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);

        assertEquals(3, gf.getAbstractQueryExpression().size());

        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);
        assertEquals(
                new QName("http://www.someserver.com/myns", "InWaterA_1M"),
                q.getTypeNames().get(0));

        q = (QueryType) gf.getAbstractQueryExpression().get(1);
        assertNotNull(q);
        assertEquals(
                new QName("http://www.someserver.com/myns", "BuiltUpA_1M"),
                q.getTypeNames().get(0));

        q = (QueryType) gf.getAbstractQueryExpression().get(2);
        assertNotNull(q);
        assertEquals(
                new QName("http://demo.cubewerx.com/yourns", "RoadL_1M"),
                q.getTypeNames().get(0));
    }

    @Test
    public void testParse4() throws Exception {
        String xml = "<GetFeature "
                + "   version='2.0.0' "
                + "   service='WFS' "
                + "   handle='Query01' "
                + "   resultType='hits' "
                + "   xmlns='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                + "   xmlns:myns='http://www.someserver.com/myns' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd "
                + "                       http://www.opengis.net/gml/3.2 "
                + "                       http://schemas.opengis.net/gml/3.2.1/gml.xsd "
                + "                       http://www.someserver.com/myns ./GetFeature_07.xsd'> "
                + " "
                + "   <Query typeNames='myns:Hydrography'> "
                + "      <PropertyName>myns:geoTemp</PropertyName> "
                + "      <PropertyName>myns:depth</PropertyName> "
                + "      <PropertyName>myns:temperature</PropertyName> "
                + "      <fes:Filter> "
                + "         <fes:Not> "
                + "            <fes:Disjoint> "
                + "               <fes:ValueReference>myns:geoTemp</fes:ValueReference> "
                + "               <gml:Envelope srsName='urn:ogc;def:crs:EPSG::4326'> "
                + "                  <gml:lowerCorner>46.2023 -57.9118 </gml:lowerCorner> "
                + "                  <gml:upperCorner>51.8145 -46.6873</gml:upperCorner> "
                + "               </gml:Envelope> "
                + "            </fes:Disjoint> "
                + "         </fes:Not> "
                + "      </fes:Filter> "
                + "      <fes:SortBy> "
                + "         <fes:SortProperty> "
                + "            <fes:ValueReference>myns:depth</fes:ValueReference> "
                + "         </fes:SortProperty> "
                + "         <fes:SortProperty> "
                + "            <fes:ValueReference>myns:temperature</fes:ValueReference> "
                + " "
                + "            <fes:SortOrder>DESC</fes:SortOrder> "
                + "         </fes:SortProperty> "
                + "      </fes:SortBy> "
                + "   </Query> "
                + "</GetFeature> ";
        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);
        assertEquals(ResultTypeType.HITS, gf.getResultType());

        assertEquals(1, gf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);

        Not f = (Not) q.getFilter();
        assertNotNull(f);

        Disjoint d = (Disjoint) f.getFilter();
        assertNotNull(d);

        assertEquals(2, q.getSortBy().size());
    }

    @Test
    public void testParse5() throws Exception {
        String xml = "<GetFeature "
                + "   version='2.0.0' "
                + "   service='WFS' "
                + "   count='10' "
                + "   xmlns:wfs='http://www.opengis.net/wfs/2.0'"
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                + "   xmlns:tiger='http://www.census.gov'> "
                + "   <wfs:Query typeNames='tiger:tiger_roads'> "
                + "      <PropertyName>tiger:the_geom</PropertyName> "
                + "      <fes:Filter> "
                + "         <fes:DWithin> "
                + "             <fes:ValueReference>the_geom</fes:ValueReference> "
                + "             <gml:Point srsName='urn:ogc:def:crs:EPSG:3067'> "
                + "                 <gml:pos>219158.795 6800828.901</gml:pos> "
                + "             </gml:Point> "
                + "             <fes:Distance uom='m'>100</fes:Distance> "
                + "        </fes:DWithin> "
                + "      </fes:Filter> "
                + "   </wfs:Query> "
                + "</GetFeature> ";
        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);

        assertEquals(1, gf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);

        DWithin f = (DWithin) q.getFilter();
        assertNotNull(f);
        assertEquals(100.0, f.getDistance(), 0d);
        assertEquals("m", f.getDistanceUnits());
    }

    @Test
    public void testParseGetFeatureWithLock() throws Exception {
        String xml = "<wfs:GetFeatureWithLock "
                + "   service='WFS' expiry='124' lockAction='all'"
                + "   version='2.0.0' "
                + "   outputFormat='application/gml+xml; version=3.2' "
                + "   xmlns:myns='http://www.someserver.com/myns' "
                + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                + "   <wfs:Query typeNames='myns:InWaterA_1M' srsName='epsg:4326'> "
                + "      <fes:Filter> "
                + "         <fes:ResourceId rid='InWaterA_1M.1234'/> "
                + "      </fes:Filter> "
                + "   </wfs:Query> "
                + "</wfs:GetFeatureWithLock> ";
        buildDocument(xml);

        GetFeatureWithLockType gf = (GetFeatureWithLockType) parse();
        assertNotNull(gf);

        assertEquals(BigInteger.valueOf(124), gf.getExpiry());
        assertEquals(AllSomeType.ALL, gf.getLockAction());

        assertEquals(1, gf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);

        assertEquals(new URI("epsg:4326"), q.getSrsName());
        assertEquals(1, q.getTypeNames().size());
        assertEquals(
                new QName("http://www.someserver.com/myns", "InWaterA_1M"),
                q.getTypeNames().get(0));

        Id f = (Id) q.getFilter();
        assertNotNull(f);

        assertEquals(1, f.getIdentifiers().size());
        assertEquals("InWaterA_1M.1234", f.getIdentifiers().iterator().next().getID());
    }

    @Test
    public void testParseStoredQuery() throws Exception {
        String xml = "<wfs:GetFeature "
                + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:fes='http://www.opengis.org/fes/2.0' "
                + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 ../../wfs.xsd' "
                + "   resolve='all' "
                + "   resolveDepth='2' "
                + "   service='WFS' "
                + "   version='2.0.0'> "
                + "   <wfs:StoredQuery id='urn:CubeWerx:StoredQueries:FeaturesInPolygon'> "
                + "      <wfs:Parameter name='AreaOfInterest'> "
                + "        <gml:Polygon gml:id='P1'> "
                + "          <gml:exterior> "
                + "            <gml:LinearRing> "
                + "              <gml:posList>10 10 10 20 20 20 20 10 10 10</gml:posList> "
                + "            </gml:LinearRing> "
                + "          </gml:exterior> "
                + "        </gml:Polygon> "
                + "      </wfs:Parameter> "
                + "   </wfs:StoredQuery>"
                + " </wfs:GetFeature>";
        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);

        assertEquals(1, gf.getAbstractQueryExpression().size());

        StoredQueryType sq = (StoredQueryType) gf.getAbstractQueryExpression().get(0);
        assertEquals(1, sq.getParameter().size());
        ParameterType p = sq.getParameter().get(0);
        assertEquals("AreaOfInterest", p.getName());
        assertNotNull(p.getValue());
        assertTrue(p.getValue().contains("gml:Polygon"));
    }

    @Test
    public void testParseJoinQuery() throws Exception {
        String xml = "<GetFeature "
                + "   service='WFS' "
                + "   version='2.0.0' "
                + "   xmlns='http://www.opengis.net/wfs/2.0' "
                + "   xmlns:myns='http://www.someserver.com/myns' "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                + "   <Query typeNames='myns:RoadSegments myns:Bridges' aliases='a b'> "
                + "      <fes:Filter> "
                + "         <fes:And> "
                + "            <fes:PropertyIsEqualTo> "
                + "               <fes:ValueReference>a/RoadName</fes:ValueReference> "
                + "               <fes:Literal>Main St.</fes:Literal> "
                + " "
                + "            </fes:PropertyIsEqualTo> "
                + "            <fes:PropertyIsEqualTo> "
                + "               <fes:ValueReference>a/RoadCode</fes:ValueReference> "
                + "               <fes:ValueReference>b/RoadCode</fes:ValueReference> "
                + "            </fes:PropertyIsEqualTo> "
                + "         </fes:And> "
                + "      </fes:Filter> "
                + "   </Query> "
                + " "
                + "</GetFeature>";
        buildDocument(xml);

        GetFeatureType gf = (GetFeatureType) parse();
        assertNotNull(gf);

        assertEquals(1, gf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) gf.getAbstractQueryExpression().get(0);
        assertNotNull(q);

        assertEquals(2, q.getTypeNames().size());
        assertEquals(2, q.getAliases().size());
    }
}
