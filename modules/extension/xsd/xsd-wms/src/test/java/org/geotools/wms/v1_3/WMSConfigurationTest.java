package org.geotools.wms.v1_3;

import static junit.framework.TestCase.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.geotools.xml.Parser;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class WMSConfigurationTest {

    @Test
    public void testValidate() throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMSConfiguration());
        p.setValidating(true);
        try (InputStream is = getClass().getResourceAsStream("./caps130.xml")) {
            p.parse(is);
        }
        if (!p.getValidationErrors().isEmpty()) {
            for (Iterator e = p.getValidationErrors().iterator(); e.hasNext();) {
                SAXParseException ex = (SAXParseException) e.next();
                System.out.println(
                        ex.getLineNumber() + "," + ex.getColumnNumber() + " -" + ex.toString());
            }
            fail("Document did not validate.");
        }

    }
}
