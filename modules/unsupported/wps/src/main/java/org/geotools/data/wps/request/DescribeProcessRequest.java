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
package org.geotools.data.wps.request;

import org.geotools.data.ows.Request;

/**
 * Retrieves information about the given process on
 * the WPS Server. 
 * 
 * The only parameter it takes is a process which it uses to 
 * return the information about.
 * 
 * @author gdavis
 *
 *
 * @source $URL$
 */
public interface DescribeProcessRequest extends Request {
    /** Represents the PROCESS parameter */
    public static final String IDENTIFIER = "IDENTIFIER"; //$NON-NLS-1$
    
    /**
     * Sets the name of the process to look up
     * 
     * @param processname A comma-delimited list of unique process names
     */
    public void setIdentifier(String processnames);
}
