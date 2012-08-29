package org.geotools.csw;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.DescribeRecordType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class CSWDescribeRecordTest {
    
    Parser parser = new Parser(new CSWConfiguration());

    @Test
    public void testParseDescribeRecord() throws Exception {
        DescribeRecordType dr = (DescribeRecordType) parser.parse(getClass().getResourceAsStream("DescribeRecord.xml"));
        assertEquals("CSW", dr.getService());
        assertEquals("2.0.2", dr.getVersion());
        assertEquals(2, dr.getTypeName().size());
        assertEquals(new QName("http://www.opengis.net/cat/csw/2.0.2", "Record"), dr.getTypeName().get(0));
        assertEquals(new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage"), dr.getTypeName().get(1));
    }
    
}
