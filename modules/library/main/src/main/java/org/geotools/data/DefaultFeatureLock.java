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
 * Used to lock features when used with LockingDataSource.
 * <p>
 * This class is responsible for supplying a unique Authorization ID and
 * expirery date for LockingDataSource locking operations.
 * </p> 
 * Example:
 * <table border=1, bgcolor="lightgray", width="100%"><tr><td><code><pre>
 * FeatureLock lock1 = FeatureLockFactory.generate( 18*60*60 ); // expire in 18 min
 * FeatureLock lock3 = FeatureLockFactory.generate( "MyLock", 30*60*60 ); // expire in 30 min
 * </pre></code></td></tr></table>
 * <p>
 * Although it is tempting to have these FeatureLock objects stored in a static
 * repository in the manner of GeoServer's TypeRepository.InternalLock -
 * that decision should be left to the individual DataSources.
 * </p>
 * <p>An AbstractLockingDataSource with appropriate overrideable callbacks
 * may be an elegent way to acomplish this.</p>
 * 
 * @see <a href="http://vwfs.refractions.net/docs/Database_Research.pdf">Database_Research.pdf</a>
 * @see <a href="http://vwfs.refractions.net/docs/Transactional_WFS_Design.pdf">Transactional_WFS_Design.pdf</a>
 * @see <a href="http://vwfs.refractions.net/docs/Design_Implications.pdf">Design_Implications.pdf</a>
 * @author jgarnett, Refractions Research, Inc.
 * </p> 
 * @source $URL$
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
    /** LockId used for transaction authorization. */                
    public String getAuthorization(){
        return authorization;
    }
    /** Time from now the lock will expire */    
    public long getDuration(){        
        return duration;
    }
}
