package org.geotools.wps.v2_0;

import junit.framework.TestCase;
import net.opengis.wps20.GetStatusType;
import net.opengis.wps20.StatusInfoType;
import net.opengis.wps20.StatusTypeMember0;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.junit.Assert;

public class WPSStatusTest extends TestCase {

    public void testParseStatusRequest() throws Exception {
        WPSConfiguration wps = new WPSConfiguration();
        Parser parser = new Parser(wps);
        Object o = parser.parse(getClass().getResourceAsStream("wpsStatusRequestExample.xml"));
        assertTrue(o instanceof GetStatusType);

        GetStatusType resultType = (GetStatusType) o;
        assertEquals("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66", resultType.getJobID());

        Encoder encoder = new Encoder(wps);
        String encodedGetStatus = encoder.encodeAsString(resultType, WPS.GetStatus);
        Assert.assertTrue(encodedGetStatus.contains("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66"));
    }

    public void testParseStatusInfo() throws Exception {
        WPSConfiguration wps = new WPSConfiguration();
        Parser parser = new Parser(wps);

        Object o = parser.parse(getClass().getResourceAsStream("wpsStatusResponseExample.xml"));
        assertTrue(o instanceof StatusInfoType);
        StatusInfoType resultType = (StatusInfoType) o;
        assertNotNull(resultType.getJobID());
        assertEquals("8ade9b04-8282-11eb-9a16-0050568030a3", resultType.getJobID());

        assertNotNull(StatusTypeMember0.get((String) resultType.getStatus()));

        Encoder encoder = new Encoder(wps);
        String encodedGetStatus = encoder.encodeAsString(resultType, WPS.StatusInfo);

        Assert.assertTrue(
                encodedGetStatus.contains(
                        "<wps:JobID>8ade9b04-8282-11eb-9a16-0050568030a3</wps:JobID>"));
        Assert.assertTrue(encodedGetStatus.contains("<wps:Status>Accepted</wps:Status>"));
    }
}
