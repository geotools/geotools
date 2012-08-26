package org.geotools.csw;

import net.opengis.cat.csw20.GetCapabilitiesType;

import org.geotools.xml.Parser;
import org.junit.Ignore;
import org.junit.Test;

public class CSWCapabilitiesTest {
    
    Parser parser = new Parser(new CSWConfiguration());

    @Test
    @Ignore
    public void testParseCapabilitiesRequest() throws Exception {
        String capRequestPath = "GetCapabilities.xml";
        GetCapabilitiesType caps = (GetCapabilitiesType) parser.parse(getClass().getResourceAsStream(capRequestPath));
                
    }
}
