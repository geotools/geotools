/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.xsd.PullParser;

/**
 * Handling url's in XSD documents with the same HTTP client as the WFS. Makes sure to reuse any http configuration.
 *
 * <p>Used in conjunction with {@link PullParser}. {@link PullParser#setURIHandler(URIHandler)}
 *
 * <p>Call {@link #dispose()} after parsing.
 */
public class XsdHttpHandler extends URIHandlerImpl {

    private final HTTPClient client;

    private Collection<HTTPResponse> responses = new LinkedList<>();

    /** Init with an existing HTTPClient to reuse any configuration. */
    public XsdHttpHandler(HTTPClient client) {
        this.client = client;
    }

    @Override
    public boolean canHandle(URI uri) {
        return "http".equals(uri.scheme()) || "https".equals(uri.scheme());
    }

    /**
     * Creates an input stream for the URI, assuming it's a URL, and returns it.
     *
     * @return an open input stream.
     * @exception IOException if there is a problem obtaining an open input stream.
     */
    @Override
    public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
        HTTPResponse httpResponse = client.get(new URL(uri.toString()));
        responses.add(httpResponse);
        InputStream result = httpResponse.getResponseStream();
        return result;
    }

    /** Disposing any HTTPResponse objects created during parsing. */
    public void dispose() {
        responses.forEach(r -> r.dispose());
    }
}
