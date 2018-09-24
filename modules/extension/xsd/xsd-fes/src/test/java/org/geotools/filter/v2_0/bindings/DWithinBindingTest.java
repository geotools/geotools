package org.geotools.filter.v2_0.bindings;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.spatial.DWithinImpl;
import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESConfiguration;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.DWithin;
import org.xml.sax.SAXException;

public class DWithinBindingTest {

    private static final Configuration CONFIGURATION = new FESConfiguration();

    private static final Parser PARSER = new Parser(CONFIGURATION);

    private static final Encoder ENCODER = new Encoder(CONFIGURATION);

    // TODO: Nothing being asserted - remove?
    public void testEncode() throws Exception {
        DWithin dwithin = FilterMockData.dwithin();
        //    Document dom = encode(dwithin, FES.DWithin);
        // print(dom);
    }

    @Test
    public void testDWithinBindingEncodesUnits() throws Exception {
        FilterFactory ff = new FilterFactoryImpl();
        DWithinImpl dWithin = new DWithinImpl(ff.property("location"), ff.literal("WKT (10, 30)"));
        dWithin.setDistance(542.3231);
        dWithin.setUnits("meters");
        String xml = writeFilter(dWithin);
        assertTrue(
                format("XML was not written correctly, expected DWithin operator %n%s", xml),
                xml.contains("<fes:DWithin>"));
        assertTrue(
                format("XML was not written correctly, expected units to be non-null %n%s", xml),
                xml.contains("<fes:Distance uom=\"meters\">542.3231</fes:Distance>"));
    }

    @Test
    public void testDWithinBindingParsesUnits() throws Exception {
        Filter filter = loadFilter("dwithin-units.xml");
        DWithin dWithin = assertType(filter, DWithin.class);
        assertEquals(542.3231, dWithin.getDistance(), 0.0);
        assertEquals("meters", dWithin.getDistanceUnits());
    }

    private static <T> T assertType(Object object, Class<T> clazz) {
        assertTrue(clazz.isInstance(object));
        return clazz.cast(object);
    }

    private static Filter loadFilter(String resource)
            throws IOException, SAXException, ParserConfigurationException {
        try (InputStream data =
                FESTestSupport.class.getClassLoader().getResourceAsStream(resource)) {
            return (Filter) PARSER.parse(data);
        }
    }

    private static String writeFilter(Filter filter) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ENCODER.encode(filter, FES.Filter, out);
            return new String(out.toByteArray());
        }
    }
}
