package org.geotools.data.mongodb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.geotools.http.HTTPResponse;
import org.junit.Assert;

/**
 * Helper class to mock HTTP responses
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MockHttpResponse implements HTTPResponse {

    String contentType;

    Map<String, String> headers;

    byte[] response;

    boolean disposed;

    public MockHttpResponse(String response, String contentType, String... headers) {
        this(response.getBytes(StandardCharsets.UTF_8), contentType, headers);
    }

    public MockHttpResponse(URL response, String contentType, String... headers) throws IOException {
        this(IOUtils.toByteArray(response.openStream()), contentType, headers);
    }

    public MockHttpResponse(byte[] response, String contentType, String... headers) {
        this.response = response;
        this.contentType = contentType;
        this.headers = new HashMap<>();

        if (headers != null) {
            if (headers.length % 2 != 0) {
                throw new IllegalArgumentException("The headers must be a alternated sequence of keys "
                        + "and values, should have an even number of entries");
            }

            for (int i = 0; i < headers.length; i += 2) {
                String key = headers[i];
                String value = headers[i++];
                this.headers.put(key, value);
            }
        }
    }

    @Override
    public void dispose() {
        if (!disposed) {
            Assert.fail("The response input stream got grabbed, but not closed");
        }
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public String getResponseHeader(String headerName) {
        return headers.get(headerName);
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        return new ByteArrayInputStream(response) {
            @Override
            public void close() throws IOException {
                disposed = true;
                super.close();
            }
        };
    }

    /**
     * @return {@code null}
     * @see org.geotools.data.ows.HTTPResponse#getResponseCharset()
     */
    @Override
    public String getResponseCharset() {
        return null;
    }
}
