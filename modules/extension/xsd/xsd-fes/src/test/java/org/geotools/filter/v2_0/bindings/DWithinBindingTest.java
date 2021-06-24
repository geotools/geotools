package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.spatial.DWithin;
import org.w3c.dom.Document;

public class DWithinBindingTest extends FESTestSupport {
    @Test
    public void testEncode() throws Exception {
        DWithin dwithin = FilterMockData.dwithin();
        encode(dwithin, FES.DWithin);
        // print(dom);
    }

    @Test
    public void testParseAndEncode() throws Exception {
        String dwithinStr =
                "<fes:DWithin xmlns:xs='http://www.w3.org/2001/XMLSchema' xmlns:fes='http://www.opengis.net/fes/2.0' xmlns:gml='http://www.opengis.net/gml/3.2'>"
                        + "<fes:ValueReference>geom</fes:ValueReference>"
                        + "<gml:MultiSurface>"
                        + "<gml:surfaceMember>"
                        + "<gml:Polygon>"
                        + "<gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">-3.47846563793341 48.722720523382868 -3.477146216817613 48.723266017150742 -3.476979820867556 48.723105499136544 -3.478344351997081 48.722562935940601 -3.47846563793341 48.722720523382868</gml:posList></gml:LinearRing></gml:exterior>"
                        + "</gml:Polygon>"
                        + "</gml:surfaceMember>"
                        + "</gml:MultiSurface>"
                        + "<fes:Distance units=\"dd\">0.00005</fes:Distance>"
                        + "</fes:DWithin>";

        buildDocument(dwithinStr);
        DWithin dwithin = (DWithin) parse();

        assertEquals(
                "geom", ((AttributeExpressionImpl) dwithin.getExpression1()).getPropertyName());
        assertNotNull(((LiteralExpressionImpl) dwithin.getExpression2()).getValue());
        Geometry geometry =
                (Geometry) ((LiteralExpressionImpl) dwithin.getExpression2()).getValue();
        assertEquals("MultiSurface", geometry.getGeometryType());

        Encoder encoder = new Encoder(createConfiguration());
        Document encodedDWithin = encoder.encodeAsDOM(dwithin, FES.DWithin);

        assertEquals(
                1,
                encodedDWithin
                        .getElementsByTagNameNS(GML.NAMESPACE, GML.Polygon.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                encodedDWithin
                        .getElementsByTagNameNS(GML.NAMESPACE, GML.MultiSurface.getLocalPart())
                        .getLength());
    }
}
