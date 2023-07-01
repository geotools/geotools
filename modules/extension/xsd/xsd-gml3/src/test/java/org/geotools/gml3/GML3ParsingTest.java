package org.geotools.gml3;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.xsd.Parser;
import org.geotools.xsd.StreamingParser;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.temporal.Period;
import org.w3c.dom.Document;

public class GML3ParsingTest {

    @Test
    public void testWithoutSchema() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("states.xml")) {
            GMLConfiguration gml = new GMLConfiguration();
            StreamingParser parser = new StreamingParser(gml, in, SimpleFeature.class);

            int nfeatures = 0;
            SimpleFeature f = null;
            while ((f = (SimpleFeature) parser.parse()) != null) {
                nfeatures++;
                Assert.assertNotNull(f.getAttribute("STATE_NAME"));
                Assert.assertNotNull(f.getAttribute("STATE_ABBR"));
                Assert.assertTrue(f.getAttribute("SAMP_POP") instanceof String);
            }
            Assert.assertEquals(49, nfeatures);
        }
    }

    @Test
    public void testWithSchema() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        // copy the schema to a temporary file
        File xsd = new File("target/states.xsd");
        // xsd.deleteOnExit();

        Document schema = db.parse(getClass().getResourceAsStream("states.xsd"));
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new DOMSource(schema), new StreamResult(xsd));

        // update the schemaLocation to point at the schema
        Document instance = db.parse(getClass().getResourceAsStream("states.xml"));
        instance.getDocumentElement()
                .setAttribute("schemaLocation", "http://www.openplans.org/topp target/states.xsd");

        File xml = new File("target/states.xml");
        // xml.deleteOnExit();
        tx.transform(new DOMSource(instance), new StreamResult(xml));

        try (InputStream in = new FileInputStream(xml)) {
            GMLConfiguration gml = new GMLConfiguration();
            StreamingParser parser = new StreamingParser(gml, in, SimpleFeature.class);

            int nfeatures = 0;
            SimpleFeature f = null;
            while ((f = (SimpleFeature) parser.parse()) != null) {
                nfeatures++;
                Assert.assertNotNull(f.getAttribute("STATE_NAME"));
                Assert.assertNotNull(f.getAttribute("STATE_ABBR"));
                Assert.assertTrue(f.getAttribute("SAMP_POP") instanceof Double);
            }

            Assert.assertEquals(49, nfeatures);
        }
    }

    @Test
    public void testParse3D() throws Exception {
        Parser p = new Parser(new GMLConfiguration());
        Object g = p.parse(GML3ParsingTest.class.getResourceAsStream("polygon3d.xml"));
        assertThat(g, instanceOf(Polygon.class));

        Polygon polygon = (Polygon) g;
        Assert.assertEquals(3, CoordinateSequences.coordinateDimension(polygon));
        Geometry expected =
                new WKTReader()
                        .read(
                                "POLYGON((94000 471000 10, 94001 471000 11, 94001 471001 12, 94000 471001 13, 94000 471000 10))");
        Assert.assertTrue(CoordinateSequences.equalsND(expected, polygon));
    }

    @Test
    public void testParseTimePeriodByPosition() throws Exception {
        Parser p = new Parser(new GMLConfiguration());
        Object g = p.parse(GML3ParsingTest.class.getResourceAsStream("timePeriodByPosition.xml"));
        assertThat(g, instanceOf(Period.class));

        Period period = (Period) g;
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        java.util.Calendar calendar = java.util.Calendar.getInstance(gmt);
        calendar.clear();
        calendar.set(2006, 5, 28, 4, 8, 0);
        Assert.assertEquals(calendar.getTime(), period.getBeginning().getPosition().getDate());
        calendar.set(2009, 5, 28, 6, 8, 0);
        Assert.assertEquals(calendar.getTime(), period.getEnding().getPosition().getDate());
    }
}
