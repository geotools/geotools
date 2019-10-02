package org.geotools.ows.wmts;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public abstract class MockHttpCommonClient extends HttpClient {

    private final MockHttpConnectionManager connectionManager;
    private final List<HttpMethod> calledMethods = new ArrayList<>();

    public MockHttpCommonClient() {
        super();

        this.connectionManager = new MockHttpConnectionManager();
        this.setHttpConnectionManager(this.connectionManager);
    }

    @Override
    public int executeMethod(HostConfiguration hostConfig, HttpMethod method, HttpState state)
            throws IOException {
        this.calledMethods.add(method);
        InputStream inputStream = this.getResponseInputStream(method);
        this.connectionManager.connection.setResponseInputStream(inputStream);
        return super.executeMethod(hostConfig, method, state);
    }

    public List<HttpMethod> getCalledMethods() {
        return this.calledMethods;
    }

    protected abstract InputStream getResponseInputStream(HttpMethod method) throws IOException;

    private static class MockHttpConnection extends HttpConnection {

        private InputStream inputStream;

        public MockHttpConnection(HttpConnectionManager httpConnectionManager) {
            super("", 0);
            this.setHttpConnectionManager(httpConnectionManager);
        }

        public void open() {
            this.isOpen = true;
        }

        @Override
        public void close() {
            this.isOpen = false;
        }

        public void setResponseInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public InputStream getResponseInputStream() throws IllegalStateException {
            return this.inputStream;
        }

        @Override
        public void flushRequestOutputStream() {}

        @Override
        public void write(byte[] data, int offset, int length) throws IllegalStateException {}

        @Override
        public String readLine(String charset) throws IOException, IllegalStateException {
            return HttpParser.readLine(this.inputStream, charset);
        }

        @Override
        public boolean isResponseAvailable() {
            return true;
        }
    }

    private static class MockHttpConnectionManager implements HttpConnectionManager {

        private final MockHttpConnection connection;
        private HttpConnectionManagerParams params;

        public MockHttpConnectionManager() {
            this.connection = new MockHttpConnection(this);
            this.params = new HttpConnectionManagerParams();
        }

        @Override
        public HttpConnection getConnection(HostConfiguration hostConfiguration) {
            return this.connection;
        }

        @Override
        public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout)
                throws HttpException {
            return this.connection;
        }

        @Override
        public HttpConnection getConnectionWithTimeout(
                HostConfiguration hostConfiguration, long timeout) {
            return this.connection;
        }

        @Override
        public void releaseConnection(HttpConnection conn) {}

        @Override
        public void closeIdleConnections(long idleTimeout) {}

        @Override
        public HttpConnectionManagerParams getParams() {
            return this.params;
        }

        @Override
        public void setParams(HttpConnectionManagerParams params) {
            this.params = params;
        }
    }
}
