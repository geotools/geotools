package org.geotools.data.wfs.v1_1_0;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.geotools.data.wfs.protocol.http.HTTPProtocol;
import org.geotools.data.wfs.protocol.http.HTTPResponse;

public class HttpProtocolWrapper implements HTTPProtocol {

    HTTPProtocol delegate;

    public HttpProtocolWrapper(HTTPProtocol delegate) {
        this.delegate = delegate;
    }

    public void setTryGzip(boolean tryGzip) {
        delegate.setTryGzip(tryGzip);
    }

    public boolean isTryGzip() {
        return delegate.isTryGzip();
    }

    public void setAuth(String username, String password) {
        delegate.setAuth(username, password);
    }

    public boolean isAuthenticating() {
        return delegate.isAuthenticating();
    }

    public void setTimeoutMillis(int milliseconds) {
        delegate.setTimeoutMillis(milliseconds);
    }

    public int getTimeoutMillis() {
        return delegate.getTimeoutMillis();
    }

    public HTTPResponse issueGet(URL baseUrl, Map<String, String> kvp) throws IOException {
        return delegate.issueGet(baseUrl, kvp);
    }

    public HTTPResponse issuePost(URL targetUrl, POSTCallBack callback) throws IOException {
        return delegate.issuePost(targetUrl, callback);
    }

    public URL createUrl(URL baseUrl, Map<String, String> queryStringKvp)
            throws MalformedURLException {
        return delegate.createUrl(baseUrl, queryStringKvp);
    }

}
