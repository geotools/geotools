package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.v2_0.FES;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.w3c.dom.Document;

/** Tests for checking {@link NotBinding} encoding. */
public class NotBindingTest {

    /** Test checking correct encoding for Not operator */
    @Test
    public void testNotOperatorEncoding() throws Exception {
        FilterFactory2 ff = new FilterFactoryImpl();
        Filter filter = ff.not(ff.isNull(ff.property("prop")));
        Configuration configuration = new org.geotools.filter.v2_0.FESConfiguration();
        Encoder encoder = new Encoder(configuration);
        encoder.setIndenting(true);
        Document encodedDoc = encoder.encodeAsDOM(filter, FES.Filter);
        XPath xpath = XPathFactory.newInstance().newXPath();
        defaultNamespaceContext(xpath);
        String prop =
                xpath.evaluate(
                        "/fes:Filter/fes:Not/fes:PropertyIsNull/fes:ValueReference", encodedDoc);
        assertEquals("prop", prop);
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
