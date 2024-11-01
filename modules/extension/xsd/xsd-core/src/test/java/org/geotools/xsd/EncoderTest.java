package org.geotools.xsd;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.geotools.ml.Envelope;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.geotools.ml.bindings.ML;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class EncoderTest {

    @Test
    @Ignore // needs a string comparison ignoring newlines...
    public void testIndent() throws Exception {
        Mail ml = new Mail(
                BigInteger.valueOf(10),
                "hi",
                new Envelope("me@me.org", "you@you.org", Calendar.getInstance(), "hey", null),
                null);
        Encoder e = new Encoder(new MLConfiguration());
        e.setIndenting(true);
        e.setIndentSize(4);

        String mail = e.encodeAsString(ml, new QName(ML.NAMESPACE, "mails"));
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ml:mails xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:ml=\"http://mails/refractions/net\">\n"
                        + "    <ml:mail>\n"
                        + "        <ml:envelope From=\"me@me.org\">\n"
                        + "            <ml:From>me@me.org</ml:From>\n"
                        + "            <ml:To>you@you.org</ml:To>\n"
                        + "        </ml:envelope>\n"
                        + "    </ml:mail>\n"
                        + "</ml:mails>";
        assertEquals(expected, mail);
    }

    /** Tests for {@link org.geotools.xsd.Encoder#setOmitXMLDeclaration(boolean)} */
    @Test
    public void testSetOmitXMLDeclaration() {
        Encoder encoder = new Encoder(new MLConfiguration());
        encoder.setOmitXMLDeclaration(false);
        Assert.assertFalse(encoder.isOmitXMLDeclaration());
        encoder.setOmitXMLDeclaration(true);
        Assert.assertTrue(encoder.isOmitXMLDeclaration());
    }

    /** Tests for {@link org.geotools.xsd.Encoder#setIndenting(boolean)} */
    @Test
    public void testSetIndenting() {
        Encoder encoder = new Encoder(new MLConfiguration());
        encoder.setIndenting(false);
        Assert.assertFalse(encoder.isIndenting());
        encoder.setIndenting(true);
        Assert.assertTrue(encoder.isIndenting());
    }
}
