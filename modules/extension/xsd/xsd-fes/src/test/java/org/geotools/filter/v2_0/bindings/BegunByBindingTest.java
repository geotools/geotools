package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.temporal.Instant;
import org.geotools.filter.v2_0.FESTestSupport;
import org.junit.Test;

public class BegunByBindingTest extends FESTestSupport {
    @Test
    public void testParse() throws Exception {
        String xml = "<fes:Filter "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd"
                + " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'>"
                + "   <fes:BegunBy> "
                + "      <fes:ValueReference>timeInstanceAttribute</fes:ValueReference> "
                + "        <gml:TimeInstant gml:id='TI1'> "
                + "          <gml:timePosition>2005-05-17T08:00:00Z</gml:timePosition> "
                + "        </gml:TimeInstant> "
                + "   </fes:BegunBy> "
                + "</fes:Filter>";
        buildDocument(xml);

        BegunBy begunBy = (BegunBy) parse();
        assertNotNull(begunBy);

        assertTrue(begunBy.getExpression1() instanceof PropertyName);
        assertEquals("timeInstanceAttribute", ((PropertyName) begunBy.getExpression1()).getPropertyName());

        assertTrue(begunBy.getExpression2() instanceof Literal);
        assertTrue(begunBy.getExpression2().evaluate(null) instanceof Instant);
    }
}
