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
package org.geotools.data;


/**
 * Used in conjuction with {@link FeatureLocking} to lock features during a
 * transaction. This class is responsible for supplying a unique Authorization 
 * ID and expiry period.
 * <p>
 * 
 * A FeatureLock representing the Current Transaction has been provided as a 
 * static constant: {@link #TRANSACTION}.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 *
 * @source $URL$
 * @version $Id$
 *
 * @see <a
 *      href="http://vwfs.refractions.net/docs/Database_Research.pdf">Database
 *      Reseach</a>
 * @see <a
 *      href="http://vwfs.refractions.net/docs/Transactional_WFS_Design.pdf">Transactional
 *      WFS Design</a>
 * @see <a
 *      href="http://vwfs.refractions.net/docs/Design_Implications.pdf">Design
 *      Implications</a>
 * @see FeatureLockFactory
 */
public class FeatureLock {
    public static final FeatureLock TRANSACTION = new CurrentTransactionLock();
    protected String authorization;
    protected long duration;
    
    /**
     * Creates a new lock.
     * 
     * @param authorization LockId used to authorize the transaction
     * @param duration expiry period of this lock (in minutes)
     */
    public FeatureLock(String authorization, long duration ){
        this.authorization = authorization;
        this.duration = duration;
    }
    
    /**
     * Gets the ID used for transaction authorization.
     *
     * @return the authorization ID
     */
    public String getAuthorization(){
        return authorization;
    }

    /**
     * Gets the expiry time for this lock (in minutes).
     *
     * @return expiry period
     */
    public long getDuration(){
        return duration;
    }
}
