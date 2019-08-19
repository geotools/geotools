/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.geotools.data.Base64;

/** Decodes the base64 data and provides an appropriate InputStream. */
public class DataUrlConnection extends URLConnection {

    /**
     * Must be overridden.
     *
     * @param url the data url
     */
    protected DataUrlConnection(final URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException {
        // nothing to be done
    }

    @Override
    public InputStream getInputStream() {
        String url = this.url.toExternalForm();
        url = url.substring(url.lastIndexOf(",") + 1);
        return new ByteArrayInputStream(Base64.decode(url));
    }
}
