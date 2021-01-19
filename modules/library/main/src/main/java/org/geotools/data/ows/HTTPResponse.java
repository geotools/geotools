/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import java.io.InputStream;

/**
 * Interface by which an {@link AbstractOpenWebService} retrieves the contents of an HTTP request
 * issued through its {@link HTTPClient}.
 *
 * <p>An HTTPResponse instance shall be {@link #dispose() disposed} as soon as it's {@link
 * #getResponseStream() response stream} has been consumed or is no longer needed. It's up to the
 * implementations to just close the actual {@link InputStream} or return the http connection to the
 * connection pool, or any other resource clean up task that needs to be done.
 *
 * @author groldan
 * @deprecated Copied to org.geotools.http
 * @see org.geotools.http.HTTPResponse
 */
@Deprecated
public interface HTTPResponse extends org.geotools.http.HTTPResponse {}
