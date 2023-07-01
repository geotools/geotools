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

public class FunctionTypeBindingTest extends FESTestSupport {
    @Test
    public void testParse() throws Exception {
        String xml =
                "<fes:Function xmlns:fes='"
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
        Document dom = encode(FilterMockData.function(), FES.Function);
        assertEquals("fes:Function", dom.getDocumentElement().getNodeName());
        assertEquals("abs", dom.getDocumentElement().getAttribute("name"));

        assertNotNull(getElementByQName(dom, FES.ValueReference));
    }
}
