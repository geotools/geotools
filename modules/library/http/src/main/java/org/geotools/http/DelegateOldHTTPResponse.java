/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @deprecated Used in conjunction with the transit from namespace org.geotools.data.ows to
 *     org.geotools.http
 * @author Roar Brænden
 */
@Deprecated
public class DelegateOldHTTPResponse implements HTTPResponse {

    protected org.geotools.data.ows.HTTPResponse delegate;

    public DelegateOldHTTPResponse(org.geotools.data.ows.HTTPResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }

    @Override
    public String getContentType() {
        return delegate.getContentType();
    }

    @Override
    public String getResponseHeader(String headerName) {
        return delegate.getResponseHeader(headerName);
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        return delegate.getResponseStream();
    }

    @Override
    public String getResponseCharset() {
        return delegate.getResponseCharset();
    }
}
