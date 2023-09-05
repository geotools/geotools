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
package org.geotools.api.data;

/**
 * A request for a Lock that last the duration of a transaction.
 *
 * <p>The single instance of this class is available as <code>FeatureLock.TRANSACTION</code>.
 */
class CurrentTransactionLock extends FeatureLock {

    CurrentTransactionLock() {
        super(null, -1);
    }
    /**
     * Transaction locks do not require Authorization.
     *
     * <p>Authorization is based on being on "holding" the Transaction rather than supplying an
     * authorization id.
     *
     * @return <code>CURRENT_TRANSACTION</code> to aid in debugging.
     * @see FeatureLock#getAuthorization()
     */
    @Override
    public String getAuthorization() {
        return toString();
    }

    /**
     * Transaciton locks are not held for a duration.
     *
     * <p>Any locking performed against the current Transaction is expected to expire when the
     * transaction finishes with a close or rollback
     *
     * @return <code>-1</code> representing an invalid duration
     * @see FeatureLock#getDuration()
     */
    @Override
    public long getDuration() {
        return -1;
    }

    @Override
    public String toString() {
        return "CURRENT_TRANSACTION";
    }
}
