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
import java.util.Set;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * Provides Feature based locking.
 *
 * <p>
 * Features from individual shapefiles, database tables, etc. can be protected
 * or reserved from modification through this interface.
 * </p>
 * <p>
 * To use please cast your SimpleFeatureSource to this interface.
 * <pre><code>
 * SimpleFeatureSource source = dataStore.getFeatureSource("roads");
 * if( source instanceof FeatureLocking ) {
 *     FeatureLocking locking = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
 *     ...
 * }
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author Ray Gallagher
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 *
 *
 *
 * @version $Id$
 */
public interface FeatureLocking<T extends FeatureType, F extends Feature>
        extends FeatureStore<T, F> {
    /**
     * All locking operations will operate against the provided <code>lock</code>.
     *
     * <p>This in in keeping with the stateful spirit of DataSource in which operations are against
     * the "current" transaction. If a FeatureLock is not provided lock operations will only be
     * applicable for the current transaction (they will expire on the next commit or rollback).
     *
     * <p>That is lockFeatures() operations will:
     *
     * <ul>
     *   <li>Be recorded against the provided FeatureLock.
     *   <li>Be recorded against the current transaction if no FeatureLock is provided.
     * </ul>
     *
     * <p>Calling this method with <code>setFeatureLock( FeatureLock.TRANSACTION
     * )</code> will revert to per transaction operation.
     *
     * <p>This design allows for the following:
     *
     * <ul>
     *   <li>cross DataSource FeatureLock usage
     *   <li>not having pass in the same FeatureLock object multiple times
     * </ul>
     *
     * @param lock FeatureLock configuration including authorization and requested duration
     */
    void setFeatureLock(FeatureLock lock);

    /**
     * FeatureLock features described by Query.
     *
     * <p>To implement WFS parcial Locking retrieve your features with a query operation first
     * before trying to lock them individually. If you are not into WFS please don't ask what
     * parcial locking is.
     *
     * @param query Query describing the features to lock
     * @return Number of features locked
     * @throws IOException Thrown if anything goes wrong
     */
    int lockFeatures(Query query) throws IOException;

    /**
     * FeatureLock features described by Filter.
     *
     * <p>To implement WFS parcial Locking retrieve your features with a query operation first
     * before trying to lock them individually. If you are not into WFS please don't ask what
     * parcial locking is.
     *
     * @param filter Filter describing the features to lock
     * @return Number of features locked
     * @throws IOException Thrown if anything goes wrong
     */
    int lockFeatures(Filter filter) throws IOException;

    /**
     * FeatureLock all Features.
     *
     * <p>The method does not prevent addFeatures() from being used (we could add a lockDataSource()
     * method if this functionality is required.
     *
     * @return Number of Features locked by this opperation
     */
    int lockFeatures() throws IOException;

    /**
     * Unlocks all Features.
     *
     * <p>Authorization must be provided prior before calling this method.
     *
     * <pre><code>
     * <b>void</b> releaseLock( String lockId, LockingDataSource ds ){
     *    ds.setAuthorization( "LOCK534" );
     *    ds.unLockFeatures();
     * }
     * </code></pre>
     */
    void unLockFeatures() throws IOException;

    /**
     * Unlock Features denoted by provided filter.
     *
     * <p>Authorization must be provided prior before calling this method.
     */
    void unLockFeatures(Filter filter) throws IOException;

    /**
     * Unlock Features denoted by provided query.
     *
     * <p>Authorization must be provided prior before calling this method.
     *
     * @param query Specifies fatures to unlock
     */
    void unLockFeatures(Query query) throws IOException;

    /** Idea for a response from a high-level lock( Query ) function. */
    public static class Response {
        String authID;
        Set<String> locked;
        Set<String> notLocked;

        public Response(FeatureLock lock, Set<String> lockedFids, Set<String> notLockedFids) {
            authID = lock.getAuthorization();
            locked = lockedFids;
            notLocked = notLockedFids;
        }

        public String getAuthorizationID() {
            return authID;
        }

        public Set<String> getLockedFids() {
            return locked;
        }

        public Set<String> getNotLockedFids() {
            return notLocked;
        }
    }
}
