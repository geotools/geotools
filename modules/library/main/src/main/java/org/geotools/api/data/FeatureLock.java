/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.api.data;

import java.util.concurrent.TimeUnit;

/**
 * Used in conjuction with {@link FeatureLocking} to lock features during a transaction. This class is responsible for
 * supplying a unique Authorization ID and expiry period.
 *
 * <p>A FeatureLock representing the current transaction has been provided as a static constant: {@link #TRANSACTION}.
 *
 * <p>Lock duration is measured in milliseconds, although you shoudl take into account the abilities of different
 * databases and servers. WFS 1.1 measures lock expiry time in minuets, WFS 2.0 measures lick expiry time in seconds..
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @version $Id$
 * @see <a href="http://vwfs.refractions.net/docs/Database_Research.pdf">Database Reseach</a>
 * @see <a href="http://vwfs.refractions.net/docs/Transactional_WFS_Design.pdf">Transactional WFS Design</a>
 * @see <a href="http://vwfs.refractions.net/docs/Design_Implications.pdf">Design Implications</a>
 */
public class FeatureLock {
    /** Lock requested for the duration of the Transaction (until next commit or revert). */
    @SuppressWarnings(
            "ClassInitializationDeadlock") // CurrentTransactionLock is a subclass of the containing class FeatureLock
    public static final FeatureLock TRANSACTION = new CurrentTransactionLock();

    protected String authorization;
    protected long duration;

    /**
     * Creates a new lock.
     *
     * @param authorization LockId used to authorize the transaction
     * @param duration expiry period of this lock (in milliseconds)
     */
    public FeatureLock(String authorization, long duration) {
        this.authorization = authorization;
        this.duration = duration;
    }

    /**
     * Creates a new lock.
     *
     * @param authorization LockId used to authorize the transaction
     * @param duration Expiry period
     * @param unit Time unit for expiry period
     */
    public FeatureLock(String authorization, long duration, TimeUnit unit) {
        this(authorization, TimeUnit.MILLISECONDS.convert(duration, unit));
    }
    /**
     * Gets the ID used for transaction authorization.
     *
     * @return the authorization ID
     */
    public String getAuthorization() {
        return authorization;
    }

    /**
     * The expiry time for this lock (in milliseconds).
     *
     * @return expiry period in milliseconds
     */
    public long getDuration() {
        return duration;
    }
}
