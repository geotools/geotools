package org.geotools.filter.v2_0.bindings;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.LikeFilterFesImpl;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESConfiguration;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.SAXException;

public class LikeBindingTest {

    private static final Configuration FES_CONFIGURATION = new FESConfiguration();

    private static final Parser PARSER = new Parser(FES_CONFIGURATION);

    private static final Encoder ENCODER = new Encoder(FES_CONFIGURATION);

    @Test
    public void testParseLikeNoFunctions() throws Exception {
        Filter filter = loadFilter("like-func-none.xml");
        LikeFilterImpl like = assertType(filter, LikeFilterImpl.class);
        PropertyName property = assertType(like.getExpression(), PropertyName.class);
        assertEquals(property.getPropertyName(), "property");
        assertEquals(like.getLiteral(), "value");
    }

    @Test
    public void testParseLikeFunctionBoth() throws Exception {
        Filter filter = loadFilter("like-func-both.xml");
        LikeFilterFesImpl like = assertType(filter, LikeFilterFesImpl.class);
        Function property = assertType(like.getExpression1(), Function.class);
        Function value = assertType(like.getExpression2(), Function.class);
        assertEquals(property.getParameters().get(0).toString(), "property");
        assertEquals(value.getParameters().get(0).toString(), "value");
    }

    @Test
    public void testParseLikeFunctionProperty() throws Exception {
        Filter filter = loadFilter("like-func-property.xml");
        LikeFilterFesImpl like = assertType(filter, LikeFilterFesImpl.class);
        Function property = assertType(like.getExpression1(), Function.class);
        Literal value = assertType(like.getExpression2(), Literal.class);
        assertEquals(property.getParameters().get(0).toString(), "property");
        assertEquals(value.getValue(), "value");
    }

    @Test
    public void testParseLikeFunctionValue() throws Exception {
        Filter filter = loadFilter("like-func-value.xml");
        LikeFilterFesImpl like = assertType(filter, LikeFilterFesImpl.class);
        PropertyName property = assertType(like.getExpression1(), PropertyName.class);
        Function value = assertType(like.getExpression2(), Function.class);
        assertEquals(property.getPropertyName(), "property");
        assertEquals(value.getParameters().get(0).toString(), "value");
    }

    @Test
    public void testEncodeLikeFunctionBoth() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        LikeFilterFesImpl like =
                new LikeFilterFesImpl(
                        ff.function("strURLEncode", ff.literal("value"), ff.literal(false)));
        like.setExpression(ff.function("strURLEncode", ff.property("property"), ff.literal(true)));
        String xml = writeFilter(like);
        assertTrue(
                "XML was not written correctly, expected the expressions to be functions",
                xml.contains(
                        "<fes:Function name=\"strURLEncode\">"
                                + "<fes:ValueReference>property</fes:ValueReference>"
                                + "<fes:Literal>true</fes:Literal>"
                                + "</fes:Function>"
                                + "<fes:Function name=\"strURLEncode\">"
                                + "<fes:Literal>value</fes:Literal>"
                                + "<fes:Literal>false</fes:Literal>"
                                + "</fes:Function>"));
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
