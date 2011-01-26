/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.geotools.factory.GeoTools;

/**
 * Default implementation of {@link HTTPProtocol} based on apache's common-http-client
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: DefaultHTTPProtocol.java 31929 2008-11-28 19:10:03Z groldan $
 * @since 2.6
 * @source $URL:
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/protocol/http/DefaultHTTPProtocol.java $
 */
@SuppressWarnings("nls")
public class DefaultHTTPProtocol extends AbstractHttpProtocol implements HTTPProtocol {

    /**
     * An {@link HTTPResponse} wrapping an executed {@link GetMethod} or {@link PostMethod} from the
     * apache commons-httpclient package
     * 
     * @author Gabriel Roldan (OpenGeo)
     */
    public class HTTPClientResponse implements HTTPResponse {

        private HttpMethodBase method;

        public HTTPClientResponse(HttpMethodBase method) {
            this.method = method;
        }

        /**
         * @see HTTPResponse#getResponseStream()
         */
        public InputStream getResponseStream() throws IOException {
            final InputStream in;
            {
                InputStream responseStream = method.getResponseBodyAsStream();

                String encoding = getResponseHeader("Content-Encoding");
                if (encoding != null) {
                    if (encoding.toLowerCase().indexOf("gzip") > -1) {
                        LOGGER.finest("Response is GZIP encoded, wrapping with gzip input stream");
                        responseStream = new GZIPInputStream(responseStream);
                    }
                } else {
                    LOGGER.finest("HTTP response is not gzip encoded");
                }
                in = responseStream;
            }
            
            InputStream releaseConnectionInputStream = new InputStream() {

                @Override
                public int read() throws IOException {
                    return in.read();
                }

                @Override
                public void close() throws IOException {
                    method.releaseConnection();
                }
            };
            return releaseConnectionInputStream;
        }

        public String getContentType() {
            return getResponseHeader("Content-Type");
        }

        public String getResponseCharset() {
            String responseCharSet = method.getResponseCharSet();
            return responseCharSet;
        }

        /**
         * @see HTTPResponse#getResponseHeader()
         */
        public String getResponseHeader(String headerName) {
            Header header = method.getResponseHeader(headerName);
            String headerValue = null;
            if (header != null) {
                headerValue = header.getValue();
            }
            return headerValue;
        }

        /**
         * @see HTTPResponse#getTargetUrl()
         */
        public String getTargetUrl() {
            try {
                URI uri = method.getURI();
                return uri.toString();
            } catch (URIException e) {
                LOGGER.log(Level.FINE, "can't get HTTP request URI", e);
            }
            return null;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(getClass().getSimpleName());
            sb.append("[Method=").append(method.getName());
            sb.append("\n\tHTTP version=").append(method.getEffectiveVersion());
            sb.append("\n\tstatus code=").append(method.getStatusCode());
            sb.append("\n\tpath=").append(method.getPath());
            sb.append("\n\tquery string=").append(method.getQueryString());
            sb.append("\n\tresponse charset=").append(getResponseCharset());
            Header[] responseHeaders = method.getResponseHeaders();
            sb.append("\n\tresponse headers=");
            for (Header header : responseHeaders) {
                sb.append(header.toExternalForm());
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public HTTPResponse issuePost(final URL targetUrl, final POSTCallBack callback)
            throws IOException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("About to issue POST request to " + targetUrl.toExternalForm());
        }

        final String uri = targetUrl.toExternalForm();

        final PostMethod postRequest = new PostMethod(uri);

        final RequestEntity requestEntity = new RequestEntity() {
            public long getContentLength() {
                return callback.getContentLength();
            }

            public String getContentType() {
                return callback.getContentType();
            }

            public boolean isRepeatable() {
                return false;
            }

            public void writeRequest(OutputStream out) throws IOException {
                callback.writeBody(out);
            }
        };

        postRequest.setRequestEntity(requestEntity);

        HTTPResponse httpResponse = issueRequest(postRequest);

        return httpResponse;
    }

    public HTTPResponse issueGet(final URL baseUrl, final Map<String, String> kvp)
            throws IOException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("About to issue GET request to " + baseUrl.toExternalForm()
                    + " with query parameters: " + kvp);
        }

        final String uri = createUri(baseUrl, kvp);
        GetMethod getRequest = new GetMethod(uri);

        HTTPResponse httpResponse = issueRequest(getRequest);

        return httpResponse;
    }

    private HttpClient client;

    /**
     * 
     * @param httpRequest
     *            either a {@link HttpMethod} or {@link PostMethod} set up with the request to be
     *            sent
     * @return
     * @throws IOException
     */
    private HTTPResponse issueRequest(final HttpMethodBase httpRequest) throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Executing HTTP request: " + httpRequest.getURI());
        }
        if (client == null) {
            client = new HttpClient();
            client.getParams().setParameter("http.useragent",
                    "GeoTools " + GeoTools.getVersion() + " WFS DataStore");
        }
        if (timeoutMillis > 0) {
            client.getParams().setSoTimeout(timeoutMillis);
        }

        // TODO: remove this
        System.err.println("Executing HTTP request: " + httpRequest);

        if (isTryGzip()) {
            LOGGER.finest("Adding 'Accept-Encoding=gzip' header to request");
            httpRequest.addRequestHeader("Accept-Encoding", "gzip");
        }

        int statusCode;
        try {
            statusCode = client.executeMethod(httpRequest);
        } catch (IOException e) {
            httpRequest.releaseConnection();
            throw e;
        }

        if (statusCode != HttpStatus.SC_OK) {
            httpRequest.releaseConnection();
            String statusText = HttpStatus.getStatusText(statusCode);
            throw new IOException("Request failed with status code " + statusCode + "("
                    + statusText + "): " + httpRequest.getURI());
        }

        HTTPResponse httpResponse = new HTTPClientResponse(httpRequest);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Got " + httpResponse);
        }
        return httpResponse;
    }
}
