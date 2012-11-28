package org.geotools.xml;

import javax.xml.namespace.QName;

import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.geotools.ml.bindings.ML;

import junit.framework.TestCase;

public class PullParserTest extends TestCase {

    public void testParse() throws Exception {
        PullParser parser = new PullParser(new MLConfiguration(), 
            ML.class.getResourceAsStream("mails.xml"), new QName(ML.NAMESPACE, "mail"));
        
        Mail m = (Mail) parser.parse();
        assertNotNull(m);
        assertEquals(0, m.getId().intValue());

        m = (Mail) parser.parse();
        assertNotNull(m);
        assertEquals(1, m.getId().intValue());

        assertNull(parser.parse());
    }
}
