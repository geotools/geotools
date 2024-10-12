package org.geotools.xsd;

import javax.xml.namespace.QName;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.geotools.ml.bindings.ML;
import org.junit.Assert;
import org.junit.Test;

public class PullParserTest {

    @Test
    public void testParse() throws Exception {
        PullParser parser = new PullParser(
                new MLConfiguration(), ML.class.getResourceAsStream("mails.xml"), new QName(ML.NAMESPACE, "mail"));

        Mail m = (Mail) parser.parse();
        Assert.assertNotNull(m);
        Assert.assertEquals(0, m.getId().intValue());

        m = (Mail) parser.parse();
        Assert.assertNotNull(m);
        Assert.assertEquals(1, m.getId().intValue());

        Assert.assertNull(parser.parse());
    }
}
