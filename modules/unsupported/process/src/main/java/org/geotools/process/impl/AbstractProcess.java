/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.impl;


import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;

/**
 * Provide an implementation of the process method to implement your own Process.
 * <p>
 * This is a straight forward abstract process that has all the fields filled in.
 * </p>
 * @author gdavis
 *
 *
 * @source $URL$
 */
public abstract class AbstractProcess implements Process {   
    protected ProcessFactory factory;
    
    protected AbstractProcess( ProcessFactory factory ){
        this.factory = factory;
    }    
     
}
