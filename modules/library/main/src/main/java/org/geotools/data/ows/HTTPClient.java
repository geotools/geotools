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

/**
 * Defines the inteface by which an {@link AbstractOpenWebService} executes HTTP requests.
 *
 * @author groldan
 * @see HTTPResponse
 * @see SimpleHttpClient
 * @see AbstractOpenWebService#setHttpClient(HTTPClient)
 * @deprecated Copied to org.geotools.http
 * @see org.geotools.http.HTTPClient
 */
@Deprecated
public interface HTTPClient extends org.geotools.http.HTTPClient {}
