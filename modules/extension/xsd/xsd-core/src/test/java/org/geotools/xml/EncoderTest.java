package org.geotools.xml;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.namespace.QName;

import org.geotools.ml.Envelope;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.geotools.ml.bindings.ML;

import junit.framework.TestCase;

public class EncoderTest extends TestCase {

    public void testIndent() throws Exception {
        Mail ml = new Mail(BigInteger.valueOf(10), "hi", new Envelope("me@me.org", "you@you.org", 
            Calendar.getInstance(), "hey", null), null);
        Encoder e = new Encoder(new MLConfiguration());
        //e.setIndenting(true);
        //e.setIndentSize(4);

        System.out.println(e.encodeAsString(ml, new QName(ML.NAMESPACE, "mails")));
    }
}
