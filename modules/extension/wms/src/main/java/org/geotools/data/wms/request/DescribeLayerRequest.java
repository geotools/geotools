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

import org.geotools.data.ows.Request;

/**
 * Retrieves information about the layers or feature types available on
 * the Web Map Server. It is used to assist user symbolization.
 * 
 * The only parameter it takes is a list of named layers which it uses to 
 * return the information about.
 * @source $URL$
 */
public interface DescribeLayerRequest extends Request {
    /** Represents the LAYERS parameter */
    public static final String LAYERS = "LAYERS"; //$NON-NLS-1$
    
    /**
     * TODO Change this to be a String[] or List<String> so we can encode properly.
     * Sets the LAYERS parameter
     * 
     * @param layers A comma delimited String of named layers
     */
    public void setLayers(String layers);
}
