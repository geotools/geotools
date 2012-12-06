package org.geotools.ows.v2_0;

import net.opengis.ows20.ExceptionReportType;
import net.opengis.ows20.ExceptionType;
import net.opengis.ows20.Ows20Factory;

import org.geotools.xml.Encoder;
import org.junit.Test;

public class ExceptionReportTest {

    @Test
    public void testEncodeException() throws Exception {
        ExceptionType e = Ows20Factory.eINSTANCE.createExceptionType();
        e.setExceptionCode("testCode");
        e.setLocator("testLocator");
        e.setExceptionText("testText");

        ExceptionReportType report = Ows20Factory.eINSTANCE.createExceptionReportType();
        report.setVersion("2.0");
        report.getException().add(e);

        Encoder encoder = new Encoder(new OWSConfiguration());
        encoder.setIndenting(true);
        encoder.setIndentSize(2);
        
        // used to throw an exception here
        encoder.encodeAsString(report, OWS.ExceptionReport);
    }
}
