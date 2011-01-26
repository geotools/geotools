/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.net.URL;
import java.util.List;

/**
 * Represents an operation used by Open Web Services for requests and responses.
 * 
 * @author rgould
 * @source $URL$
 */
public class OperationType {
	protected List<String> formats;
	protected URL get;
	protected URL post;

    /**
     * Each format is usually a MIME type string that can be used to indicate
     * what format the server's response should be in.
     * 
     * @return a List of Strings, each of which usually represent a mime type
     */
    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    /**
     * @return the URL where a GET request should be made
     */
    public URL getGet() {
        return get;
    }
    public void setGet(URL get) {
        this.get = get;
    }

    /**
     * @return the URL where a POST request should be made
     */
    public URL getPost() {
        return post;
    }
    
    public void setPost(URL post) {
        this.post = post;
    }
}
