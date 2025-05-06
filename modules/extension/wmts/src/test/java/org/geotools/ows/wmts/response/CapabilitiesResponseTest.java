/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.ows.wmts.response;

import java.io.File;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.ServiceException;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;

public class CapabilitiesResponseTest {

    @Test
    public void testServiceExceptionWithoutContentType() throws Exception {
        String exception = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?>\n"
                + "<ServiceExceptionReport version=\"1.1.0\">\n"
                + "<ServiceException>"
                + "Gatekeeper logon denied."
                + "</ServiceException>\n"
                + "</ServiceExceptionReport>";
        String contentType = "application/xml";

        HTTPResponse resp = new MockHttpResponse(exception, contentType);
        try {
            WMTSGetCapabilitiesResponse capaResp = new WMTSGetCapabilitiesResponse(resp);
            Assert.fail("Wrong to get a capabilities response. Content is: " + capaResp);
        } catch (ServiceException e) {
            Assert.assertEquals("Gatekeeper logon denied.", e.getMessage());
        }
    }

    @Test
    public void testOrdinaryCapabilitiesResponse() throws Exception {
        File getCaps = TestData.file(null, "ol.getcapa.xml");
        HTTPResponse resp = new MockHttpResponse(getCaps, "application/xml");
        WMTSGetCapabilitiesResponse capaResp = new WMTSGetCapabilitiesResponse(resp);
        try {
            Assert.assertNotNull(capaResp.getCapabilities());
        } finally {
            capaResp.dispose();
        }
    }
}
