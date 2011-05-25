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
package org.geotools.process;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Used to report on the progress of a running {@link Process}.
 * <p>
 * The contents of the Map returned by get() are described by {@link ProcessFactory.getResultInfo()}
 * description.
 * 
 * @author Jody
 *
 *
 * @source $URL$
 */
public interface Progress extends Future<Map<String,Object>> {
    /**
     * Value of getProgress used to represent an undefined amount of work. 
     */
    public static float WORKING = -1.0f; 
    
    /**
     * Amount of work completed.
     * 
     * @return Percent completed, or WORKING if amount of work is unknown. 
     */
    public float getProgress();
    
    // consider these if anyone is interested on the user interface side
    // addChangeListener
    // removeChangeListener    
}
