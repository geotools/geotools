package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.temporal.Begins;
import org.opengis.temporal.Period;
import org.w3c.dom.Document;

public class DWithinBindingTest extends FESTestSupport {

    public void testEncode() throws Exception {
        DWithin dwithin = FilterMockData.dwithin();
        Document dom = encode(dwithin, FES.DWithin);
        print(dom);
    }
}
