package org.geotools.csw;

import static org.junit.Assert.*;
import net.opengis.cat.csw20.GetDomainType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class GetDomainTest {
    Parser parser = new Parser(new CSWConfiguration());
    
    @Test
    public void testParseGetDomainParameter() throws Exception {
        GetDomainType gd = (GetDomainType) parser.parse(getClass().getResourceAsStream(
                "GetDomainParameter.xml"));
        // check the attributes
        assertEquals("CSW", gd.getService());
        assertEquals("2.0.2", gd.getVersion());
        
        assertEquals("GetRecords.outputFormat", gd.getParameterName());
    }
    
    @Test
    public void testParseGetDomainProperty() throws Exception {
        GetDomainType gd = (GetDomainType) parser.parse(getClass().getResourceAsStream(
                "GetDomainProperty.xml"));
        // check the attributes
        assertEquals("CSW", gd.getService());
        assertEquals("2.0.2", gd.getVersion());
        
        assertEquals("foo", gd.getPropertyName());
    }
}
