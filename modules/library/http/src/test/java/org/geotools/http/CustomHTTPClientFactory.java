package org.geotools.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class CustomHTTPClientFactory extends AbstractHTTPClientFactory {

    @Override
    public List<Class<?>> clientClasses() {

        return Collections.singletonList(CustomHttpClient.class);
    }

    @Override
    public HTTPClient createClient(List<Class<? extends HTTPBehavior>> behaviors) {
        return new CustomHttpClient();
    }

    static class CustomHttpClient extends AbstractHttpClient {

        @Override
        public HTTPResponse get(URL url) throws IOException {
            return null;
        }

        @Override
        public HTTPResponse post(URL url, InputStream content, String contentType) throws IOException {
            return null;
        }
    }
}
