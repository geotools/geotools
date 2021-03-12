package org.geotools.wps.v2_0;

import net.opengis.wps20.GetStatusType;
import net.opengis.wps20.StatusInfoType;
import net.opengis.wps20.StatusTypeMember0;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;

public class WPSStatusTest extends WPSTestSupport {

    @Test
    public void testParseStatusRequest() throws Exception {
        Parser parser = new Parser(createConfiguration());
        Object o = parser.parse(getClass().getResourceAsStream("wpsStatusRequestExample.xml"));
        Assert.assertTrue(o instanceof GetStatusType);

        GetStatusType resultType = (GetStatusType) o;
        Assert.assertEquals("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66", resultType.getJobID());

        Encoder encoder = new Encoder(createConfiguration());
        String encodedGetStatus = encoder.encodeAsString(resultType, WPS.GetStatus);
        Assert.assertTrue(encodedGetStatus.contains("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66"));
    }

    @Test
    public void testParseStatusInfo() throws Exception {
        Parser parser = new Parser(createConfiguration());

        Object o = parser.parse(getClass().getResourceAsStream("wpsStatusResponseExample.xml"));
        Assert.assertTrue(o instanceof StatusInfoType);
        StatusInfoType resultType = (StatusInfoType) o;
        Assert.assertNotNull(resultType.getJobID());
        Assert.assertEquals("8ade9b04-8282-11eb-9a16-0050568030a3", resultType.getJobID());

        Assert.assertNotNull(StatusTypeMember0.get((String) resultType.getStatus()));

        Encoder encoder = new Encoder(createConfiguration());
        String encodedGetStatus = encoder.encodeAsString(resultType, WPS.StatusInfo);

        Assert.assertTrue(
                encodedGetStatus.contains(
                        "<wps:JobID>8ade9b04-8282-11eb-9a16-0050568030a3</wps:JobID>"));
        Assert.assertTrue(encodedGetStatus.contains("<wps:Status>Accepted</wps:Status>"));
    }
}
