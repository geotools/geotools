package org.geotools.wcs.v1_1;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import org.geotools.xsd.Parser;
import org.junit.Test;

public class GetCapabilitiesTest {

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseCapabilitiesRequest() throws Exception {
        String capRequestPath = "requestGetCapabilities.xml";
        HashMap<String, Object> caps =
                (HashMap<String, Object>)
                        parser.parse(getClass().getResourceAsStream(capRequestPath));
        assertEquals("WCS", caps.get("service"));

        List versions = (List) ((HashMap) caps.get("AcceptVersions")).get("Version");
        assertEquals("1.1.1", versions.get(0));
        assertEquals("1.1.0", versions.get(1));
        assertEquals("1.0.0", versions.get(2));

        assertEquals("OperationsMetadata", caps.get("Sections"));
        assertEquals("application/xml", caps.get("AcceptFormats"));
    }
}
