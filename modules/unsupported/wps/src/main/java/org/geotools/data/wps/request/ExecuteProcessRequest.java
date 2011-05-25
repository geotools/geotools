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

import java.util.List;

import net.opengis.wps10.DataType;

import org.geotools.data.ows.Request;

/**
 * Executes a process. 
 * 
 * @author gdavis
 *
 *
 * @source $URL$
 */
public interface ExecuteProcessRequest extends Request {
    /** Represents the PROCESS parameter */
    public static final String IDENTIFIER = "IDENTIFIER"; //$NON-NLS-1$
    
    /**
     * Sets the name of the process to execute
     * 
     * @param processname a unique process name
     */
    public void setIdentifier(String processname);
    
    /**
     * Sets an input for the process to execute
     * 
     * @param name the input name
     * @param value the list of input datatype objects 
     */    
    public void addInput(String name, List<DataType> value);
    
}
