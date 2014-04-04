package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.capability.FilterCapabilities;

public class Filter_CapabilitiesBindingTest extends FESTestSupport {
	public void testParse() throws Exception {
        String xml = 
            "<fes:Filter_Capabilities xmlns:fes='http://www.opengis.net/fes/2.0'>" + 
	            "<fes:Id_Capabilities>" +
	            	"<fes:ResourceIdentifier name=\"fes:ResourceId\"/>" +
	            "</fes:Id_Capabilities>" + 
            "</fes:Filter_Capabilities>";

        buildDocument(xml);
        FilterCapabilities filterCapabilities = (FilterCapabilities) parse();
	}
}
