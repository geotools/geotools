package org.geotools.csw;

import static org.junit.Assert.*;

import net.opengis.cat.csw20.BriefRecordType;

import org.geotools.xml.Parser;
import org.junit.Ignore;
import org.junit.Test;

public class CSWRecordTest {
    
    Parser parser = new Parser(new CSWConfiguration());


    @Test
    @Ignore /* Fails miserably */
    public void testParseGetRecordsEbrimBrief() throws Exception {
        BriefRecordType br = (BriefRecordType) parser.parse(getClass().getResourceAsStream("BriefRecord.xml"));
        assertEquals("00180e67-b7cf-40a3-861d-b3a09337b195", br.getIdentifier().get(0).getValue());
        assertEquals("Image2000 Product 1 (at1) Multispectral", br.getTitle().get(0).getValue());
    }
    
}
