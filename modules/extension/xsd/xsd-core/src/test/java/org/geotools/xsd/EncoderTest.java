package org.geotools.xsd;

import java.math.BigInteger;
import java.util.Calendar;
import junit.framework.TestCase;
import org.geotools.ml.Envelope;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;

public class EncoderTest extends TestCase {

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
    public void testSetOmitXMLDeclaration() {
        Encoder encoder = new Encoder(new MLConfiguration());
        encoder.setOmitXMLDeclaration(false);
        assertFalse(encoder.isOmitXMLDeclaration());
        encoder.setOmitXMLDeclaration(true);
        assertTrue(encoder.isOmitXMLDeclaration());
    }

    /** Tests for {@link org.geotools.xsd.Encoder#setIndenting(boolean)} */
    public void testSetIndenting() {
        Encoder encoder = new Encoder(new MLConfiguration());
        encoder.setIndenting(false);
        assertFalse(encoder.isIndenting());
        encoder.setIndenting(true);
        assertTrue(encoder.isIndenting());
    }
}
