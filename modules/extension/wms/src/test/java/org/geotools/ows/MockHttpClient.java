package org.geotools.ows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;

/**
 * Helper class to test WMS cascading
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class MockHttpClient implements HTTPClient {

    protected String user;

    protected String password;

    protected int connectTimeout;

    protected int readTimeout;

    protected boolean tryGzip;

    public HTTPResponse post(URL url, InputStream postContent, String postContentType)
            throws IOException {
        throw new UnsupportedOperationException(
                "POST not supported, if needed you have to override and implement");
    }

    public HTTPResponse get(URL url) throws IOException {
        throw new UnsupportedOperationException(
                "GET not supported, if needed you have to override and implement");
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /** @see org.geotools.data.ows.HTTPClient#setTryGzip(boolean) */
    @Override
    public void setTryGzip(boolean tryGZIP) {
        this.tryGzip = tryGZIP;
    }

    /** @see org.geotools.data.ows.HTTPClient#isTryGzip() */
    @Override
    public boolean isTryGzip() {
        return tryGzip;
    }
}
