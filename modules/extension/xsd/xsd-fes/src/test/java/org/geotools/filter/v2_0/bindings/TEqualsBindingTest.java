package org.geotools.filter.v2_0.bindings;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.temporal.TEquals;
import org.opengis.temporal.Period;
import org.w3c.dom.Document;

public class TEqualsBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml =
                "<fes:Filter "
                        + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                        + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                        + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                        + "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd"
                        + " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'>"
                        + "   <fes:TEquals> "
                        + "      <fes:ValueReference>timeInstanceAttribute</fes:ValueReference> "
                        + "   <gml:TimePeriod gml:id='TP1'> "
                        + "      <gml:begin> "
                        + "        <gml:TimeInstant gml:id='TI1'> "
                        + "          <gml:timePosition>2005-05-17T08:00:00Z</gml:timePosition> "
                        + "        </gml:TimeInstant> "
                        + "      </gml:begin> "
                        + "      <gml:end> "
                        + "        <gml:TimeInstant gml:id='TI2'> "
                        + "          <gml:timePosition>2005-05-23T11:00:00Z</gml:timePosition> "
                        + "        </gml:TimeInstant> "
                        + "      </gml:end> "
                        + "    </gml:TimePeriod> "
                        + "   </fes:TEquals> "
                        + "</fes:Filter>";
        buildDocument(xml);

        TEquals equals = (TEquals) parse();
        assertNotNull(equals);

        assertTrue(equals.getExpression1() instanceof PropertyName);
        assertEquals(
                "timeInstanceAttribute",
                ((PropertyName) equals.getExpression1()).getPropertyName());

        assertTrue(equals.getExpression2() instanceof Literal);
        assertTrue(equals.getExpression2().evaluate(null) instanceof Period);
    }

    /**
     * Test for checking correct "Any" match action instead "ANY". <br>
     * See issue GEOT-6092
     */
    @Test
    public void testPropertyEqualsMatchEncoding() throws Exception {
        FilterFactory2 ff = new FilterFactoryImpl();
        Filter filter = ff.equal(ff.property("prop"), ff.literal("abc"), true, MatchAction.ANY);
        Configuration configuration = new org.geotools.filter.v2_0.FESConfiguration();
        Encoder encoder = new Encoder(configuration);
        encoder.setIndenting(true);
        Document encodedDoc = encoder.encodeAsDOM(filter, FES.Filter);
        XPath xpath = XPathFactory.newInstance().newXPath();
        defaultNamespaceContext(xpath);
        String matchAction =
                xpath.evaluate("/fes:Filter/fes:PropertyIsEqualTo/@matchAction", encodedDoc);
        assertEquals("Any", matchAction);
    }

    private void defaultNamespaceContext(XPath xpath) {
        xpath.setNamespaceContext(
                new NamespaceContext() {
                    @Override
                    public Iterator getPrefixes(String namespaceURI) {
                        return null;
                    }

                    @Override
                    public String getPrefix(String namespaceURI) {
                        return null;
                    }

                    @Override
                    public String getNamespaceURI(String prefix) {
                        if ("fes".equals(prefix)) return "http://www.opengis.net/fes/2.0";
                        return null;
                    }
                });
    }
}
