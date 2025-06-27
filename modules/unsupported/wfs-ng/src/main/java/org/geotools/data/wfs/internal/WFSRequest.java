/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.HttpMethod.GET;
import static org.geotools.data.wfs.internal.HttpMethod.POST;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.AbstractRequest;
import org.geotools.data.ows.Request;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.util.factory.FactoryNotFoundException;
import org.geotools.util.logging.Logging;

public abstract class WFSRequest extends AbstractRequest implements Request {

    protected final WFSStrategy strategy;

    protected final WFSOperationType operation;

    protected final WFSConfig config;

    private final boolean doPost;

    private QName typeName;

    private String outputFormat;

    private String handle;

    public WFSRequest(final WFSOperationType operation, final WFSConfig config, final WFSStrategy strategy) {

        super(url(operation, config, strategy), null);
        this.operation = operation;
        this.config = config;
        this.strategy = strategy;
        this.handle = strategy.newRequestHandle(operation);

        switch (config.getPreferredMethod()) {
            case HTTP_POST:
                this.doPost = strategy.supportsOperation(operation, POST);
                break;
            case HTTP_GET:
                this.doPost = !strategy.supportsOperation(operation, GET);
                break;
            default:
                this.doPost = strategy.supportsOperation(operation, POST);
                break;
        }

        this.outputFormat = strategy.getDefaultOutputFormat(operation);

        if (!this.doPost) {
            setProperty(SERVICE, "WFS");
            setProperty(VERSION, strategy.getVersion());
            setProperty(REQUEST, operation.getName());
        }
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    /** @param outputFormat the outputFormat to set */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setTypeName(QName typeName) {
        this.typeName = typeName;
    }

    public QName getTypeName() {
        return typeName;
    }

    public WFSStrategy getStrategy() {
        return strategy;
    }

    private static URL url(final WFSOperationType operation, final WFSConfig config, final WFSStrategy strategy) {

        final boolean suportsGet = strategy.supportsOperation(operation, GET);
        final boolean suportsPost = strategy.supportsOperation(operation, POST);
        if (!(suportsGet || suportsPost)) {
            throw new IllegalArgumentException("WFS doesn't support " + operation.getName());
        }

        HttpMethod method;
        switch (config.getPreferredMethod()) {
            case AUTO:
            case HTTP_POST:
                method = suportsPost ? POST : GET;
                break;
            default:
                method = suportsPost ? POST : GET;
                break;
        }

        URL targetUrl = strategy.getOperationURL(operation, method);

        return targetUrl;
    }

    public WFSOperationType getOperation() {
        return operation;
    }

    @Override
    public boolean requiresPost() {
        return doPost;
    }

    @Override
    protected void initService() {}

    @Override
    protected void initVersion() {}

    @Override
    protected void initRequest() {}

    @Override
    public URL getFinalURL() {
        if (requiresPost()) {
            return super.getFinalURL();
        }

        URL finalURL = strategy.buildUrlGET(this);
        return finalURL;
    }

    @Override
    public String getPostContentType() {
        // As per WFS 1.1.0 (OGC 04-094) 6.5.1
        // "When using the HTTP POST method, the content type for XML encoded WFS requests must be
        // set to text/xml."
        //  .. and
        // As per WFS 2.0.0 (OGC 09-025r1 and ISO/DIS 19142) Annex D.2
        // "When using the HTTP POST method, the content type for XML encoded WFS requests shall be
        // set to text/xml."
        return "text/xml";
    }

    @Override
    public void performPostOutput(OutputStream outputStream) throws IOException {

        try (InputStream in = strategy.getPostContents(this)) {
            IOUtils.copy(in, outputStream);
        }
    }

    @Override
    public WFSResponse createResponse(HTTPResponse response) throws ServiceException, IOException {

        final String contentType = response.getContentType();

        if (contentType == null) {
            Logging.getLogger(WFSRequest.class)
                    .warning(this.getOperation() + " request returned null content type for URL " + getFinalURL());
        }

        WFSResponseFactory responseFactory;
        try {
            responseFactory = WFSExtensions.findResponseFactory(this, contentType);
        } catch (FactoryNotFoundException fnf) {
            Loggers.MODULE.log(Level.WARNING, fnf.getMessage());
            try {
                if (contentType != null && contentType.startsWith("text")) {
                    byte[] buff = new byte[1024];
                    response.getResponseStream().read(buff);
                    Loggers.MODULE.info("Failed response snippet: " + new String(buff, StandardCharsets.UTF_8));
                }
                throw fnf;
            } catch (Exception ignore) {
                throw fnf;
            }
        }

        WFSResponse wfsResponse = responseFactory.createResponse(this, response);

        return wfsResponse;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getOperation().toString());
        sb.append("[");
        sb.append("\n\thandle: ").append(handle);
        sb.append("\n\toutputFormat: <").append(outputFormat).append(">");
        sb.append("\n\tmethod: ").append(doPost ? "POST" : "GET");
        sb.append("\n\tonlineResource: <").append(onlineResource).append(">");
        try {
            sb.append("\n\tfinal URL: <").append(getFinalURL()).append(">");
        } catch (Exception e) {
            sb.append("\n\tfinal URL error: <").append(e.getMessage()).append(">");
        }
        return sb.append("\n]").toString();
    }
}
