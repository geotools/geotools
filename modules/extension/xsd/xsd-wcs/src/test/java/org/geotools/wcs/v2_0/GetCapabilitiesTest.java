package org.geotools.wcs.v2_0;

import static org.junit.Assert.*;

import java.util.List;

import net.opengis.wcs20.GetCapabilitiesType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class GetCapabilitiesTest {

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseCapabilitiesRequest() throws Exception {
        String capRequestPath = "requestGetCapabilities.xml";
        GetCapabilitiesType caps = (GetCapabilitiesType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", caps.getService());

        List versions = caps.getAcceptVersions().getVersion();
        assertEquals("2.0.1", versions.get(0));
        assertEquals("2.0.0", versions.get(1));
        assertEquals("1.1.0", versions.get(2));

        List sections = caps.getSections().getSection();
        assertEquals(1, sections.size());
        assertEquals("OperationsMetadata", sections.get(0));
        
        List formats = caps.getAcceptFormats().getOutputFormat();
        assertEquals(1, formats.size());
        assertEquals("application/xml", formats.get(0));
    }

}
