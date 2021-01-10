package org.geotools.ows.wmts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Helper class to test WMS cascading
 *
 * @author Andrea Aime - GeoSolutions
 */
@Deprecated
public abstract class MockHttpClient extends org.geotools.data.ows.AbstractHttpClient {

    @Override
    public org.geotools.data.ows.HTTPResponse post(
            URL url, InputStream postContent, String postContentType) throws IOException {
        throw new UnsupportedOperationException(
                "POST not supported, if needed you have to override and implement");
    }

    @Override
    public org.geotools.data.ows.HTTPResponse get(URL url) throws IOException {
        return this.get(url, null);
    }

    @Override
    public org.geotools.data.ows.HTTPResponse get(URL url, Map<String, String> headers)
            throws IOException {
        throw new UnsupportedOperationException(
                "GET not supported, if needed you have to override and implement");
    }
}
