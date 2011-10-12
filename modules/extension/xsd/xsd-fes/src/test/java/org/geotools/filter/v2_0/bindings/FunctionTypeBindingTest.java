package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.w3c.dom.Document;

public class FunctionTypeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<fes:Function xmlns:fes='" + FES.NAMESPACE + "' name='abs'>" + 
            "   <fes:Literal>12</fes:Literal> " + 
            "</fes:Function>";
        buildDocument(xml);

        Function f = (Function) parse();
        assertNotNull(f);
        assertEquals("abs", f.getName());
        assertEquals(1, f.getParameters().size());
        assertTrue(f.getParameters().get(0) instanceof Literal);
    }
    
    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.function(), FES.Function);
        assertEquals("fes:Function", dom.getDocumentElement().getNodeName());
        assertEquals("abs", dom.getDocumentElement().getAttribute("name"));
        
        assertNotNull(getElementByQName(dom, FES.ValueReference));
    }
}
