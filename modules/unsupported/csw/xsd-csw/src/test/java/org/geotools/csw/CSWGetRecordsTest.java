package org.geotools.csw;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.ElementSetType;
import net.opengis.cat.csw20.GetRecordsType;
import net.opengis.cat.csw20.QueryType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class CSWGetRecordsTest {
    
    Parser parser = new Parser(new CSWConfiguration());

    @Test
    public void testParseGetRecordsEbrimBrief() throws Exception {
        GetRecordsType gr = (GetRecordsType) parser.parse(getClass().getResourceAsStream("GetRecordsBrief.xml"));
        assertEquals("CSW", gr.getService());
        assertEquals("2.0.2", gr.getVersion());
        
        // check the attributes
        assertEquals("application/xml", gr.getOutputFormat());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", gr.getOutputSchema());
        
        // the query
        QueryType query = (QueryType) gr.getQuery();
        List<QName> expected = new ArrayList<QName>();
        String rimNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
        expected.add(new QName(rimNamespace, "Service"));
        expected.add(new QName(rimNamespace, "Classification"));
        expected.add(new QName(rimNamespace, "Association"));
        assertEquals(expected, query.getTypeNames());
        
        // the element set name
        ElementSetNameType esn = query.getElementSetName();
        expected.clear();
        expected.add(new QName(rimNamespace, "Service"));
        assertEquals(expected, esn.getTypeNames());
        assertEquals(ElementSetType.BRIEF, esn.getValue());
    }
    
}
