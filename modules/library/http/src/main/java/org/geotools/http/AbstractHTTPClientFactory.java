/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2020, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.LoggingHTTPClient;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
/**
 * 
 * @author Roar Brænden
 *
 */
public abstract class AbstractHTTPClientFactory implements HTTPClientFactory {

	private boolean logging = false;
	private String charset;

	public AbstractHTTPClientFactory() {
	}
	
	@Override
    public void logging(boolean logging) {
        this.logging = logging;
    }

	@Override
    public void logging(String charset) {
        this.logging = true;
        this.charset = charset;
    }
	
	@Override
	public HTTPClient getClient() {
		return getClient(GeoTools.getDefaultHints());
	}
	
	@Override
    public HTTPClient getClient(Hints hints) {
        HTTPClient client = createClient(hints);
        if (logging) {
            client = (charset == null
                            ? new LoggingHTTPClient(client)
                            : new LoggingHTTPClient(client, charset));
        }
        return client;
    }

	/**
	 * Create the main client.
	 * 
	 * @param hints
	 * @return
	 */
    protected abstract HTTPClient createClient(Hints hints);

}
