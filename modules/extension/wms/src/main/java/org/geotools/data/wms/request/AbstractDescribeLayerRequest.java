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
package org.geotools.data.wms.request;

import java.net.URL;
import java.util.Properties;

/**
 * Describes an abstract DescribeLayer request. Provides everything except
 * the versioning info, which subclasses must implement.
 * 
 * @author Richard Gould
 * @source $URL$
 */
public abstract class AbstractDescribeLayerRequest extends AbstractWMSRequest implements DescribeLayerRequest {

    /**
     * Constructs a basic DescribeLayerRequest, without versioning info.
     * 
     * @param onlineResource the location of the request
     * @param properties a set of properties to use. Can be null.
     */
    public AbstractDescribeLayerRequest( URL onlineResource, Properties properties ) {
        super(onlineResource, properties);
    }
    
    protected void initRequest() {
        setProperty(REQUEST, "DescribeLayer");
	}

	/**
     * @see org.geotools.data.wms.request.DescribeLayerRequest#setLayers(java.lang.String)
     */
    public void setLayers( String layers ) {
        setProperty(LAYERS, layers);
    }

    protected abstract void initVersion();
}
