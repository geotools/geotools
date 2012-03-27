package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.wfs.protocol.ConnectionFactory;

/**
 * Helper class for the tests
 * @author aaime
 *
 */
public class ConnectionFactoryWrapper implements ConnectionFactory {
    
    public ConnectionFactory delegate;
    
    public ConnectionFactoryWrapper(ConnectionFactory delegate) {
        this.delegate = delegate;
    }

    public String getAuthUsername() {
        return delegate.getAuthUsername();
    }

    public String getAuthPassword() {
        return delegate.getAuthPassword();
    }

    public boolean isTryGzip() {
        return delegate.isTryGzip();
    }

    public Charset getEncoding() {
        return delegate.getEncoding();
    }

    public HttpURLConnection getConnection(URL query, HttpMethod method) throws IOException {
        return delegate.getConnection(query, method);
    }

    public InputStream getInputStream(HttpURLConnection hc) throws IOException {
        return delegate.getInputStream(hc);
    }

    public InputStream getInputStream(URL query, HttpMethod method) throws IOException {
        return delegate.getInputStream(query, method);
    }

    
}
