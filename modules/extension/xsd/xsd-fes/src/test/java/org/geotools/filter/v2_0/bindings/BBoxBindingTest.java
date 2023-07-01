package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.xsd.Encoder;
import org.junit.Assert;
import org.junit.Test;

public class BBoxBindingTest extends FESTestSupport {

    @Test
    public void testEncode() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        ReferencedEnvelope env =
                new ReferencedEnvelope(
                        Double.valueOf(48.81752162),
                        Double.valueOf(48.81915540),
                        Double.valueOf(-3.47568429),
                        Double.valueOf(-3.47261370),
                        CRS.decode("EPSG:4326"));
        Filter bboxFilter = ff.bbox(ff.property("geom"), env);
        Encoder encoder = new Encoder(createConfiguration());
        String encodedFilter = encoder.encodeAsString(bboxFilter, FES.Filter);
        Assert.assertTrue(encodedFilter.contains("<fes:ValueReference>geom</fes:ValueReference>"));
        Assert.assertTrue(
                encodedFilter.contains(
                        "<gml:Envelope srsDimension=\"2\" srsName=\"urn:ogc:def:crs:EPSG::4326\">"));
        Assert.assertTrue(
                encodedFilter.contains("<gml:lowerCorner>48.817522 -3.475684</gml:lowerCorner>"));
        Assert.assertTrue(
                encodedFilter.contains("<gml:upperCorner>48.819155 -3.472614</gml:upperCorner>"));
    }

    @Test
    public void testParse() throws Exception {
        String xml =
                "<fes:Filter "
                        + "xmlns:xs='http://www.w3.org/2001/XMLSchema' "
                        + "xmlns:fes='http://www.opengis.net/fes/2.0' "
                        + "xmlns:gml='http://www.opengis.net/gml/3.2'>"
                        + "		<fes:BBOX>"
                        + "			<fes:ValueReference>geom</fes:ValueReference>"
                        + "			<gml:Envelope srsDimension='2' srsName='urn:ogc:def:crs:EPSG::4326'>"
                        + "				<gml:lowerCorner>48.817522 -3.475684</gml:lowerCorner>"
                        + "				<gml:upperCorner>48.819155 -3.472614</gml:upperCorner>"
                        + "			</gml:Envelope>"
                        + "		</fes:BBOX>"
                        + "</fes:Filter>";

        buildDocument(xml);
        BBOX bbox = (BBOX) parse();
        assertNotNull(bbox);

        assertTrue(bbox.getExpression1() instanceof PropertyName);
        assertEquals("geom", ((PropertyName) bbox.getExpression1()).getPropertyName());

        assertTrue(bbox.getExpression2() instanceof Literal);
        assertTrue(bbox.getExpression2().evaluate(null) instanceof ReferencedEnvelope);

        ReferencedEnvelope env = (ReferencedEnvelope) bbox.getExpression2().evaluate(null);
        assertEquals(Double.valueOf(48.817522), env.getMinX(), 0d);
        assertEquals(Double.valueOf(-3.475684), env.getMinY(), 0d);
        assertEquals(Double.valueOf(48.819155), env.getMaxX(), 0d);
        assertEquals(Double.valueOf(-3.472614), env.getMaxY(), 0d);
    }
}
