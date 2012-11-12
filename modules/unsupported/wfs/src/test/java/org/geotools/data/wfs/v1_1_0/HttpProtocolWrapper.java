package org.geotools.data.wfs.v1_1_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;

public class HttpProtocolWrapper implements HTTPClient {

    HTTPClient delegate;

    public HttpProtocolWrapper(HTTPClient delegate) {
        this.delegate = delegate;
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#post(java.net.URL, java.io.InputStream,
     *      java.lang.String)
     */
    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType)
            throws IOException {
        return delegate.post(url, postContent, postContentType);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#get(java.net.URL)
     */
    @Override
    public HTTPResponse get(URL url) throws IOException {
        return delegate.get(url);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#getUser()
     */
    @Override
    public String getUser() {
        return delegate.getUser();
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#setUser(java.lang.String)
     */
    @Override
    public void setUser(String user) {
        delegate.setUser(user);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#getPassword()
     */
    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#setPassword(java.lang.String)
     */
    @Override
    public void setPassword(String password) {
        delegate.setPassword(password);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#getConnectTimeout()
     */
    @Override
    public int getConnectTimeout() {
        return delegate.getConnectTimeout();
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#setConnectTimeout(int)
     */
    @Override
    public void setConnectTimeout(int connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#getReadTimeout()
     */
    @Override
    public int getReadTimeout() {
        return delegate.getReadTimeout();
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#setReadTimeout(int)
     */
    @Override
    public void setReadTimeout(int readTimeout) {
        delegate.setReadTimeout(readTimeout);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#setTryGzip(boolean)
     */
    @Override
    public void setTryGzip(boolean tryGZIP) {
        delegate.setTryGzip(tryGZIP);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#isTryGzip()
     */
    @Override
    public boolean isTryGzip() {
        return delegate.isTryGzip();
    }
}
