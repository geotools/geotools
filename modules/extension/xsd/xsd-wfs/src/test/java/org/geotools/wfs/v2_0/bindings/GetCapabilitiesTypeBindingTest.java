package org.geotools.wfs.v2_0.bindings;

import static org.junit.Assert.assertNotNull;

import net.opengis.wfs20.GetCapabilitiesType;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.junit.Test;

public class GetCapabilitiesTypeBindingTest extends WFSTestSupport {
    @Test
    public void testParse() throws Exception {
        String xml = "<GetCapabilities service='WFS' xmlns='http://www.opengis.net/wfs/2.0' "
                + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "xsi:schemaLocation='http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd'/>";
        buildDocument(xml);

        GetCapabilitiesType gc = (GetCapabilitiesType) parse();
        assertNotNull(gc);
    }
}
