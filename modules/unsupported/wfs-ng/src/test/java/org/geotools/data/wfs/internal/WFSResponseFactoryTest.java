/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.geotools.data.wfs.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.TestHttpClient;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.TestWFSClient;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.internal.parsers.ServiceExceptionResponseFactory;
import org.geotools.ows.ServiceException;
import org.geotools.util.factory.FactoryNotFoundException;
import org.junit.Test;

public class WFSResponseFactoryTest {
    private final String CONTENT_TYPE = "application/vnd.ogc.se_xml;charset=ISO-8859-1";

    private GetFeatureRequest createRequest() throws Exception {
        URL capaUrl = WFSTestData.url("KartverketNo/GetCapabilities.xml");
        TestWFSClient client = new TestWFSClient(capaUrl, new TestHttpClient());
        return client.createGetFeatureRequest();
    }

    @Test
    public void checkWFSExtensionsReturnServiceExceptionFactory() throws Exception {
        GetFeatureRequest request = createRequest();
        WFSResponseFactory responseFactory = WFSExtensions.findResponseFactory(request, CONTENT_TYPE);
        assertEquals(responseFactory.getClass(), ServiceExceptionResponseFactory.class);
    }

    @Test
    public void checkServiceExceptionIsThrown() throws Exception {
        GetFeatureRequest request = createRequest();
        String body =
                """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <ServiceExceptionReport>
                  <ServiceException code="60">error occured</ServiceException>
                </ServiceExceptionReport>""";
        TestHttpResponse response = new TestHttpResponse(CONTENT_TYPE, "UTF-8", body);
        WFSResponseFactory responseFactory = new ServiceExceptionResponseFactory();
        ServiceException thrown =
                assertThrows(ServiceException.class, () -> responseFactory.createResponse(request, response));
        assertEquals("error occured", thrown.getMessage());
        assertEquals("60", thrown.getCode());
    }

    @Test
    public void checkHttpResponseIsDisposedWhenResponseIsNotRecognized() throws Exception {
        GetFeatureRequest request = createRequest();
        String body = "<html><body>502 Bad Gateway</body></html>";
        DisposeTrackingResponse response = new DisposeTrackingResponse("text/xml; subtype=gml/3.1.1", body);
        assertThrows(IllegalStateException.class, () -> request.createResponse(response));
        assertTrue("the HTTP response must be disposed", response.disposed);
    }

    @Test
    public void checkHttpResponseIsDisposedWhenFeatureCollectionCannotBeParsed() throws Exception {
        GetFeatureRequest request = createRequest();
        request.setTypeName(new QName("http://www.example.org/test", "Road"));
        String body = "<wfs:FeatureCollection xmlns:wfs=\"http://www.opengis.net/wfs\" <<< not xml";
        DisposeTrackingResponse response = new DisposeTrackingResponse("text/xml; subtype=gml/3.1.1", body);
        assertThrows(IOException.class, () -> request.createResponse(response));
        assertTrue("the HTTP response must be disposed", response.disposed);
    }

    @Test
    public void checkHttpResponseIsDisposedWhenNoResponseFactoryMatches() throws Exception {
        GetFeatureRequest request = createRequest();
        String body = "<html><body>Service unavailable</body></html>";
        DisposeTrackingResponse response = new DisposeTrackingResponse("text/html", body);
        assertThrows(FactoryNotFoundException.class, () -> request.createResponse(response));
        assertTrue("the HTTP response must be disposed", response.disposed);
    }

    private static class DisposeTrackingResponse extends TestHttpResponse {

        private boolean disposed;

        DisposeTrackingResponse(String contentType, String body) {
            super(contentType, "UTF-8", body);
        }

        @Override
        public void dispose() {
            disposed = true;
        }
    }
}
