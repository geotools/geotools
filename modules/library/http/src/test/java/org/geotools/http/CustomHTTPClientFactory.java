package org.geotools.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CustomHTTPClientFactory extends AbstractHTTPClientFactory {

    @Override
    public Class<? extends HTTPClient> getClientClass() {

        return CustomHttpClient.class;
    }

    @Override
    public HTTPClient createClient() {
        return new CustomHttpClient();
    }

    static class CustomHttpClient extends AbstractHttpClient {

        @Override
        public HTTPResponse get(URL url) throws IOException {
            return null;
        }

        @Override
        public HTTPResponse post(URL url, InputStream content, String contentType)
                throws IOException {
            return null;
        }
    }
}
