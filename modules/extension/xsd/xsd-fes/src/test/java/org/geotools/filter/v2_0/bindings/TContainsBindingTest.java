package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.temporal.Period;

public class TContainsBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<fes:Filter " + 
            "   xmlns:fes='http://www.opengis.net/fes/2.0' " + 
            "   xmlns:gml='http://www.opengis.net/gml/3.2' " + 
            "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
            "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd" + 
            " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'>" + 
            "   <fes:TContains> " + 
            "      <fes:ValueReference>timeInstanceAttribute</fes:ValueReference> " +
            "   <gml:TimePeriod gml:id='TP1'> " + 
            "      <gml:begin> " + 
            "        <gml:TimeInstant gml:id='TI1'> " + 
            "          <gml:timePosition>2005-05-17T08:00:00Z</gml:timePosition> " + 
            "        </gml:TimeInstant> " + 
            "      </gml:begin> " + 
            "      <gml:end> " + 
            "        <gml:TimeInstant gml:id='TI2'> " + 
            "          <gml:timePosition>2005-05-23T11:00:00Z</gml:timePosition> " + 
            "        </gml:TimeInstant> " + 
            "      </gml:end> " + 
            "    </gml:TimePeriod> " +  
            "   </fes:TContains> " + 
            "</fes:Filter>";
        buildDocument(xml);

        TContains contains = (TContains) parse();
        assertNotNull(contains);
        
        assertTrue(contains.getExpression1() instanceof PropertyName);
        assertEquals("timeInstanceAttribute", ((PropertyName)contains.getExpression1()).getPropertyName());

        assertTrue(contains.getExpression2() instanceof Literal);
        assertTrue(contains.getExpression2().evaluate(null) instanceof Period);
    }
}
