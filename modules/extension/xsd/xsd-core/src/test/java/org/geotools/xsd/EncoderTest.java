package org.geotools.xsd;

import java.math.BigInteger;
import java.util.Calendar;
import org.geotools.ml.Envelope;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.junit.Assert;
import org.junit.Test;

public class EncoderTest {

    @Test
    public void testIndent() throws Exception {
        Mail ml =
                new Mail(
                        BigInteger.valueOf(10),
                        "hi",
                        new Envelope(
                                "me@me.org", "you@you.org", Calendar.getInstance(), "hey", null),
                        null);
        Encoder e = new Encoder(new MLConfiguration());
        // e.setIndenting(true);
        // e.setIndentSize(4);

        // System.out.println(e.encodeAsString(ml, new QName(ML.NAMESPACE, "mails")));
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
