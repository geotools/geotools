package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FunctionTypeBindingTest extends FESTestSupport {
    @Test
    public void testParse() throws Exception {
        String xml = "<fes:Function xmlns:fes='"
                + FES.NAMESPACE
                + "' name='abs'>"
                + "   <fes:Literal>12</fes:Literal> "
                + "</fes:Function>";
        buildDocument(xml);

        Function f = (Function) parse();
        assertNotNull(f);
        assertEquals("abs", f.getName());
        assertEquals(1, f.getParameters().size());
        assertTrue(f.getParameters().get(0) instanceof Literal);
    }

    @Test
    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.function(), FES.Function);
        Element docElement = doc.getDocumentElement();
        assertEquals(FES.Function.getLocalPart(), docElement.getLocalName());
        assertEquals(FES.Function.getNamespaceURI(), docElement.getNamespaceURI());
        assertEquals("abs", docElement.getAttribute("name"));

        assertNotNull(getElementByQName(doc, FES.ValueReference));
    }
}
