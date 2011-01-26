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

import java.io.IOException;


/**
 * This class describes a featureID based locking service.
 *
 * <p>
 * AbstractFeatureLocking, and others, may use this API to request locks on the
 * basis of FeatureID.
 * </p>
 *
 * <p>
 * This class is also used as a public api to manage locks.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public interface LockingManager {
    /**
     * Check if any locks exist held by the authorization <code>lockID</code>.
     *
     * <p>
     * (remember that the lock may have expired)
     * </p>
     *
     * @param authID Authorization for lock
     *
     * @return <code>true</code> if lock was found
     */
    boolean exists(String authID);

    /**
     * Release locks held by the authorization <code>lockID</code>.
     *
     * <p>
     * (remember that the lock may have expired)
     * </p>
     *
     * @param authID Authorization for lock
     * @param transaction Transaction with authorization for lockID
     *
     * @return <code>true</code> if lock was found and released
     *
     * @throws IOException DOCUMENT ME!
     */
    boolean release(String authID, Transaction transaction)
        throws IOException;

    /**
     * Refresh locks held by the authorization <code>lockID</code>.
     *
     * <p>
     * All features locked with the provied <code>lockID</code> will be locked
     * for additional time (the origional duration requested).
     * </p>
     *
     * <p>
     * (remember that the lock may have expired)
     * </p>
     *
     * @param authID Authorization for lock
     * @param transaction Transaction with authorization for lockID
     *
     * @return <code>true</code> if lock was found and refreshed
     *
     * @throws IOException DOCUMENT ME!
     */
    boolean refresh(String authID, Transaction transaction)
        throws IOException;

    /**
     * FeatureID based unlocking.
     *
     * @param typeName
     * @param authID
     * @param transaction
     * @param featureLock
     *
     * @throws IOException DOCUMENT ME!
     */
    void unLockFeatureID(String typeName, String authID, Transaction transaction,
        FeatureLock featureLock) throws IOException;

    /**
     * FeatureID based locking.
     *
     * @param typeName
     * @param authID
     * @param transaction
     * @param featureLock
     *
     * @throws IOException DOCUMENT ME!
     */
    void lockFeatureID(String typeName, String authID, Transaction transaction,
        FeatureLock featureLock) throws IOException;
}
