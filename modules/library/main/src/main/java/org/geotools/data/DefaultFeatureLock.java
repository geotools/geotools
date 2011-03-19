/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

/**
 * Used in conjuction with {@link FeatureLocking} to lock features during a
 * transaction. This class is responsible for supplying a unique Authorization 
 * ID and expiry period.
 * </p> 
 * @source $URL$
 * 
 * @deprecated Please use {@link FeatureLock}
 */
public class DefaultFeatureLock extends FeatureLock {
    //private final String authorization;
    //private final long duration;

    /**
     * Package private constructor - use DefaultFeatureLockFactory methods.
     * @see DefaultFeatureLockFactory.     
     */
    DefaultFeatureLock(String id, long duration){
        super( id, duration );
    }
}
