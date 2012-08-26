package org.geotools.csw;

import static org.junit.Assert.*;
import net.opengis.cat.csw20.GetRecordsType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class CSWGetRecordsTest {
    
    Parser parser = new Parser(new CSWConfiguration());

    @Test
    public void testParseGetRecordsEbrimBrief() throws Exception {
        GetRecordsType gr = (GetRecordsType) parser.parse(getClass().getResourceAsStream("GetRecordsBrief.xml"));
        assertEquals("CSW", gr.getService());
        assertEquals("2.0.2", gr.getVersion());
        
    }
    
}
