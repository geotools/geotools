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

/**
 * This is used to represent the absence of a Transaction and the use of AutoCommit.
 *
 * <p>This class serves as the implementation of the constant Transaction.NONE. It is a NullObject
 * and we feel no need to make this class public.
 */
class AutoCommitTransaction implements Transaction {
    /**
     * Authorization IDs are not stored by AutoCommit.
     *
     * <p>Authorization IDs are only stored for the duration of a Transaction.
     *
     * @return Set of authorizations
     * @throws UnsupportedOperationException AUTO_COMMIT does not support this
     */
    public Set<String> getAuthorizations() {
        throw new UnsupportedOperationException(
                "Authorization IDs are not valid for AutoCommit Transaction");
    }

    /**
     * AutoCommit does not save State.
     *
     * <p>While symetry would be good, state should be commited not stored for later.
     *
     * @param key Key that is not used to Store State
     * @param state State we are not going to externalize
     * @throws UnsupportedOperationException AutoCommit does not support State
     */
    public void putState(Object key, State state) {
        throw new UnsupportedOperationException(
                "AutoCommit does not support the putState opperations");
    }

    /**
     * AutoCommit does not save State.
     *
     * <p>While symetry would be good, state should be commited not stored for later.
     *
     * @param key Key that is not used to Store State
     * @throws UnsupportedOperationException AutoCommit does not support State
     */
    public void removeState(Object key) {
        throw new UnsupportedOperationException(
                "AutoCommit does not support the removeState opperations");
    }

    /**
     * I am not sure should AutoCommit be able to save sate?
     *
     * <p>While symetry would be good, state should be commited not stored for later.
     *
     * @param key Key used to retrieve State
     * @return State earlier provided with putState
     * @throws UnsupportedOperationException As Autocommit does not support State
     */
    public State getState(Object key) {
        throw new UnsupportedOperationException(
                "AutoCommit does not support the getState opperations");
    }

    /**
     * Implemented as a NOP since this Transaction always commits.
     *
     * <p>This allows the following workflow:
     *
     * <pre>
     * <code>
     * Transaction t = roads.getTransaction();
     * try{
     *     roads.addFeatures( features );
     *     roads.getTransaction().commit();
     * }
     * catch( IOException erp ){
     *     //something went wrong;
     *     roads.getTransaction().rollback();
     * }
     * </code>
     * </pre>
     */
    public void commit() {
        // implement a NOP
    }

    /** Implements a NOP since AUTO_COMMIT does not maintain State. */
    public void close() {
        // no state to clean up after
    }

    /**
     * Auto commit mode cannot support the rollback opperation.
     *
     * @throws IOException if Rollback fails
     */
    public void rollback() throws IOException {
        throw new IOException("AutoCommit cannot support the rollback opperation");
    }

    /**
     * Authorization IDs are not stored by AutoCommit.
     *
     * <p>Authorization IDs are only stored for the duration of a Transaction.
     *
     * @param authID Authorization ID
     * @throws IOException If set authorization fails
     */
    public void addAuthorization(String authID) throws IOException {
        throw new IOException("Authorization IDs are not valid for AutoCommit Transaction");
    }

    /**
     * AutoCommit does not save State.
     *
     * <p>While symmetry would be good, state should be committed not stored for later.
     *
     * @param key Key that is not used to Store Property
     * @return Property associated with key, or null
     * @throws UnsupportedOperationException AutoCommit does not support State
     */
    public Object getProperty(Object key) {
        throw new UnsupportedOperationException(
                "AutoCommit does not support the getProperty opperations");
    }

    /**
     * Implementation of addProperty.
     *
     * @see org.geotools.data.Transaction#addProperty(java.lang.Object, java.lang.Object)
     */
    public void putProperty(Object key, Object value) {
        throw new UnsupportedOperationException(
                "AutoCommit does not support the addProperty opperations");
    }
}
