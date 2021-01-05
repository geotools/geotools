package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.junit.Test;
import org.opengis.filter.spatial.DWithin;

public class DWithinBindingTest extends FESTestSupport {
    @Test
    public void testEncode() throws Exception {
        DWithin dwithin = FilterMockData.dwithin();
        encode(dwithin, FES.DWithin);
        // print(dom);
    }
}
