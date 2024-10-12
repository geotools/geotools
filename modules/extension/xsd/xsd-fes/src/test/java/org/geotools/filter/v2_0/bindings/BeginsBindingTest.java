package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.temporal.Period;
import org.geotools.filter.v2_0.FESTestSupport;
import org.junit.Test;

public class BeginsBindingTest extends FESTestSupport {
    @Test
    public void testParse() throws Exception {
        String xml = "<fes:Filter "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd"
                + " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'>"
                + "   <fes:Begins> "
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
                + "   </fes:Begins> "
                + "</fes:Filter>";
        buildDocument(xml);

        Begins begins = (Begins) parse();
        assertNotNull(begins);

        assertTrue(begins.getExpression1() instanceof PropertyName);
        assertEquals("timeInstanceAttribute", ((PropertyName) begins.getExpression1()).getPropertyName());

        assertTrue(begins.getExpression2() instanceof Literal);
        assertTrue(begins.getExpression2().evaluate(null) instanceof Period);
    }
}
