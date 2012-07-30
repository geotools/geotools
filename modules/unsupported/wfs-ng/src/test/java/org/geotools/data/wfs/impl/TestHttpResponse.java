package org.geotools.data.wfs.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.geotools.data.ows.HTTPResponse;

public class TestHttpResponse implements HTTPResponse {

    private String contentType;

    private String charset;

    private String bodyContent;

    private URL contentUrl;

    public TestHttpResponse(String contentType, String charset, String bodyContent) {
        this.contentType = contentType;
        this.charset = charset;
        this.bodyContent = bodyContent;
    }

    public TestHttpResponse(String contentType, String charset, URL contentUrl) {
        this.contentType = contentType;
        this.charset = charset;
        this.contentUrl = contentUrl;
    }

    public TestHttpResponse(String contentType, String charset, InputStream contentInputStream) {
        this.contentType = contentType;
        this.charset = charset;
        BufferedReader reader = new BufferedReader(new InputStreamReader(contentInputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bodyContent = sb.toString();
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getResponseHeader(String headerName) {
        if ("charset".equalsIgnoreCase(headerName)) {
            return charset;
        }
        return null;
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        if (bodyContent != null) {
            return new ByteArrayInputStream(this.bodyContent.getBytes());
        }
        if (contentUrl != null) {
            return contentUrl.openStream();
        }
        throw new IllegalStateException();
    }
}