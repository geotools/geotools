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

import java.io.Flushable;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.geotools.api.data.DelegatingFeatureWriter;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.FeatureLockException;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Transaction;
import org.geotools.api.data.Transaction.State;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.util.SuppressFBWarnings;

/**
 * Provides In-Process FeatureLocking support for DataStore implementations.
 *
 * <p>If at all possible DataStore implementations should provide a real Feature Locking support that is persisted to
 * disk or database and resepected by other processes.
 *
 * <p>This class provides a stop gap solution that implementations may use for GeoServer compatability.
 *
 * @author Jody Garnett, Refractions Research
 * @author Chris Holmes, TOPP
 * @task REVISIT: I'm not sure that the map within a map is a good idea, it makes things perhaps too complicated. A
 *     nasty bug came about with releasing, as allLocks put locks into a new collection, and the iterator just removed
 *     them from that set instead of from the storage. This is now fixed, but the loop to do it is really damn complex.
 *     I'm not sure of the solution, but there should be something that is less confusing.
 */
public class InProcessLockingManager implements LockingManager {
    /** lockTable access by typeName stores Transactions or MemoryLocks */
    protected final Map<String, Map<String, Lock>> lockTables = new HashMap<>();

    /**
     * Aquire lock on featureID.
     *
     * <p>This method will fail if Lock is already held by another.
     *
     * @param typeName TypeName storing feature
     * @param featureID FeatureID to lock
     * @param transaction Transaction to lock against
     * @param featureLock FeatureLock describing lock request
     * @throws FeatureLockException Indicates a problem with the lock request
     */
    @Override
    @SuppressFBWarnings("UW_UNCOND_WAIT")
    public synchronized void lockFeatureID(
            String typeName, String featureID, Transaction transaction, FeatureLock featureLock)
            throws FeatureLockException {
        Lock lock = getLock(typeName, featureID);

        // This is a loop so we can wait on Transaction Locks
        //
        while (lock != null) {
            // we have a conflict
            if (lock instanceof TransactionLock) {
                TransactionLock tlock = (TransactionLock) lock;

                if (transaction == tlock.transaction) {
                    // lock already held by this transacstion
                    // we could just consider returning here
                    //
                    throw new FeatureLockException("Transaction Lock is already held by this Transaction", featureID);
                } else {
                    // we should wait till it is available and then grab
                    // the lock
                    try {
                        synchronized (tlock) {
                            tlock.wait();
                        }

                        lock = getLock(typeName, featureID);
                    } catch (InterruptedException interupted) {
                        throw new FeatureLockException(
                                "Interupted while waiting for Transaction Lock", featureID, interupted);
                    }
                }
            } else if (lock instanceof MemoryLock) {
                MemoryLock mlock = (MemoryLock) lock;
                throw new FeatureLockException("Feature Lock is held by Authorization " + mlock.authID, featureID);
            } else {
                throw new FeatureLockException("Lock is already held " + lock, featureID);
            }
        }

        // Lock is Available
        //
        lock = createLock(transaction, featureLock);
        locks(typeName).put(featureID, lock);
    }

    /**
     * Lock for typeName & featureID if it exists.
     *
     * <p>This method will not return expired locks.
     *
     * @return Lock if exists, or null
     */
    protected Lock getLock(String typeName, String featureID) {
        Map<String, Lock> locks = locks(typeName);
        // LOGGER.info("checking for lock " + typeName + ", " + featureID
        //    + " in locks " + locks);

        synchronized (locks) {
            if (locks.containsKey(featureID)) {
                Lock lock = locks.get(featureID);

                if (lock.isExpired()) {
                    locks.remove(featureID);
                    // LOGGER.info("returning null");

                    return null;
                } else {
                    // LOGGER.info("returing " + lock);

                    return lock;
                }
            } else {
                // LOGGER.info("locks did not contain key, returning null");

                // not found
                return null;
            }
        }
    }

    /**
     * Creates the right sort of In-Process Lock.
     *
     * @return In-Process Lock
     * @throws FeatureLockException When a Transaction lock is requested against Transaction.AUTO_COMMIT
     */
    protected synchronized Lock createLock(Transaction transaction, FeatureLock featureLock)
            throws FeatureLockException {
        if (featureLock == FeatureLock.TRANSACTION) {
            // we need a Transacstion Lock
            if (transaction == Transaction.AUTO_COMMIT) {
                throw new FeatureLockException("We cannot issue a Transaction lock against AUTO_COMMIT");
            }

            TransactionLock lock = (TransactionLock) transaction.getState(this);

            if (lock == null) {
                lock = new TransactionLock();
                transaction.putState(this, lock);

                return lock;
            } else {
                return lock;
            }
        } else {
            return new MemoryLock(featureLock);
        }
    }

    /**
     * Access to a Map of locks for typeName
     *
     * @param typeName typeName
     * @return Map of Transaction or MemoryLock by featureID
     */
    public Map<String, Lock> locks(String typeName) {
        synchronized (lockTables) {
            if (lockTables.containsKey(typeName)) {
                return lockTables.get(typeName);
            } else {
                Map<String, Lock> locks = new HashMap<>();
                lockTables.put(typeName, locks);

                return locks;
            }
        }
    }

    /**
     * Set of all locks.
     *
     * @return Set of all locks
     */
    public Set<Lock> allLocks() {
        synchronized (lockTables) {
            Set<Lock> set = new HashSet<>();
            Map<String, Lock> fidLocks;

            for (Map<String, Lock> stringLockMap : lockTables.values()) {
                fidLocks = stringLockMap;
                set.addAll(fidLocks.values());
            }

            return set;
        }
    }

    /**
     * Checks mutability of featureID for this transaction.
     *
     * <p>Two behaviors are defined by FeatureLocking:
     *
     * <ul>
     *   <li>TransactionLock (Blocking): lock held by a Transaction<br>
     *       Authorization is granted to the Transaction holding the Lock. Conflict will result in a block until the
     *       Transaction holding the lock completes. (This behavior is equivalent to a Database row-lock, or a java
     *       synchronized statement)
     *   <li>FeatureLock (Error): lock held by a FeatureLock<br>
     *       Authorization is based on the set of Authorization IDs held by the provided Transaction. Conflict will
     *       result in an error. (This behavior is equivalent to the WFS locking specification)
     * </ul>
     *
     * <p>Right now we are just going to error out with an exception
     *
     * @param typeName Feature type to check against
     * @param featureID FeatureID to check
     * @param transaction Provides Authorization
     * @throws FeatureLockException If transaction does not have sufficient authroization
     */
    public void assertAccess(String typeName, String featureID, Transaction transaction) throws FeatureLockException {
        Lock lock = getLock(typeName, featureID);

        // LOGGER.info("asserting access on lock for " + typeName + ", fid: "
        //  + featureID + ", transaction: " + transaction + ", lock " + lock);

        if ((lock != null) && !lock.isAuthorized(transaction)) {
            throw new FeatureLockException("Transaction does not have authorization for " + typeName + ":" + featureID);
        }
    }

    /**
     * Provides a wrapper on the provided writer that checks locks.
     *
     * @param writer FeatureWriter requiring access control
     * @param transaction Transaction being used
     * @return FeatureWriter with lock checking
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> checkedWriter(
            final FeatureWriter<SimpleFeatureType, SimpleFeature> writer, final Transaction transaction) {
        if (writer instanceof Flushable) {
            return new LockingFlushingFeatureWriter(writer, transaction);
        } else {
            return new LockingFeatureWriter(writer, transaction);
        }
    }

    /**
     * Release indicated featureID, must have correct authroization.
     *
     * @throws IOException If lock could not be released
     */
    @Override
    public synchronized void unLockFeatureID(
            String typeName, String featureID, Transaction transaction, FeatureLock featureLock) throws IOException {
        assertAccess(typeName, featureID, transaction);
        locks(typeName).remove(featureID);
    }

    /**
     * Refresh locks held by the authorization <code>authID</code>.
     *
     * <p>(remember that the lock may have expired)
     *
     * @param authID Authorization identifing Lock to refresh
     * @param transaction Transaction with authorization for lockID
     * @return <code>true</code> if lock was found and refreshed
     * @throws IOException If transaction not authorized to refresh authID
     * @throws IllegalArgumentException If authID or transaction not provided
     */
    @Override
    public synchronized boolean refresh(String authID, Transaction transaction) throws IOException {
        if (authID == null) {
            throw new IllegalArgumentException("lockID required");
        }

        if ((transaction == null) || (transaction == Transaction.AUTO_COMMIT)) {
            throw new IllegalArgumentException("Tansaction required (with authorization for " + authID + ")");
        }

        Lock lock;
        boolean refresh = false;

        for (Iterator<Lock> i = allLocks().iterator(); i.hasNext(); ) {
            lock = i.next();

            if (lock.isExpired()) {
                i.remove();
            } else if (lock.isMatch(authID)) {
                if (lock.isAuthorized(transaction)) {
                    lock.refresh();
                    refresh = true;
                } else {
                    throw new IOException("Not authorized to refresh " + lock);
                }
            }
        }

        return refresh;
    }

    /**
     * Release locks held by the authorization <code>authID</code>.
     *
     * <p>(remember that the lock may have expired)
     *
     * @param authID Authorization identifing Lock to release
     * @param transaction Transaction with authorization for lockID
     * @return <code>true</code> if lock was found and released
     * @throws IOException If transaction not authorized to release authID
     * @throws IllegalArgumentException If authID or transaction not provided
     */
    @Override
    public boolean release(String authID, Transaction transaction) throws IOException {
        // LOGGER.info("release called on lock: " + authID + ", trans: "
        //  + transaction);

        if (authID == null) {
            throw new IllegalArgumentException("lockID required");
        }

        if ((transaction == null) || (transaction == Transaction.AUTO_COMMIT)) {
            throw new IllegalArgumentException("Tansaction required (with authorization for " + authID + ")");
        }

        Lock lock;
        boolean release = false;

        // This could be done more efficiently, and perhaps cleaner,
        // but these maps within a map are just nasty.  The previous way of
        // calling iterator.remove() didn't actually remove anything, as it
        // was only iterating through the values of a map, which I believe
        // java just copies, so it's immutable.  Or perhaps we just moved
        // through too many iterator layers...
        for (Map<String, Lock> fidMap : lockTables.values()) {
            Set<String> unLockedFids = new HashSet<>();

            for (String fid : fidMap.keySet()) {
                lock = fidMap.get(fid);
                // LOGGER.info("checking lock " + lock + ", is match "
                //    + lock.isMatch(authID));

                if (lock.isExpired()) {
                    unLockedFids.add(fid);

                    // fidMap.remove(fid); concurrent modification error.
                } else if (lock.isMatch(authID)) {
                    // LOGGER.info("matches, is authorized: "
                    //    + lock.isAuthorized(transaction));

                    if (lock.isAuthorized(transaction)) {
                        unLockedFids.add(fid);

                        // fidMap.remove(fid);
                        release = true;
                    } else {
                        throw new IOException("Not authorized to release " + lock);
                    }
                }
            }

            for (String unLockedFid : unLockedFids) {
                fidMap.remove(unLockedFid);
            }
        }

        return release;
    }

    /**
     * Implment lockExists.
     *
     * <p>Remeber lock may have expired.
     *
     * @return true if lock exists for authID
     * @see LockingManager#lockExists(java.lang.String)
     */
    @Override
    public boolean exists(String authID) {
        // LOGGER.info("checking existence of lock: " + authID + " in "
        //    + allLocks());

        if (authID == null) {
            return false;
        }

        Lock lock;

        for (Iterator<Lock> i = allLocks().iterator(); i.hasNext(); ) {
            lock = i.next();

            if (lock.isExpired()) {
                i.remove();
            } else if (lock.isMatch(authID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Used by test cases
     *
     * @return Return if feature is currently locked
     */
    public boolean isLocked(String typeName, String featureID) {
        return getLock(typeName, featureID) != null;
    }

    /**
     * Represents In-Process locks for Transactions or FeatureLocks.
     *
     * @author Jody Garnett, Refractions Research
     */
    public interface Lock {
        /**
         * Check if lock has expired, it will be removed if so
         *
         * @return <code>true</code> if Lock has gone stale
         */
        boolean isExpired();

        /**
         * Check if authID matches this lock
         *
         * @return <code>true</code> if authID matches
         */
        boolean isMatch(String authID);

        /**
         * Check if transaction is authorized for this lock
         *
         * @return <code>true</code> if transaction is authorized
         */
        boolean isAuthorized(Transaction transaction);

        /** Refresh lock */
        void refresh();

        /** Release lock */
        void release();
    }

    /**
     * Class representing TransactionDuration locks.
     *
     * <p>Implements Transasction.State so it can remomve itself when commit() or rollback() is called.
     *
     * <p>Threads may wait on this object, it will notify when it releases the lock due to a commit or rollback
     * opperation
     *
     * @author Jody Garnett, Refractions Research
     */
    static class TransactionLock implements Lock, State {
        /** This will be non-null while lock is fresh */
        Transaction transaction;

        /**
         * A new TranasctionLock for use.
         *
         * <p>The lock will be stale until added to Tranasction.putState( key, Lock )
         */
        TransactionLock() {}

        /**
         * Transaction locks do not match authIDs
         *
         * @param authID Authorization ID being checked
         * @return <code>false</code>
         */
        @Override
        public boolean isMatch(String authID) {
            return false;
        }

        /**
         * <code>true</code> if Lock has gone stale
         *
         * @return <code>true</code> if lock is stale
         */
        @Override
        public boolean isExpired() {
            return transaction != null;
        }

        /** Release lock - notify those who are waiting */
        @Override
        public void release() {
            transaction = null;
            notifyAll();
        }

        /** TransactionLocks do not need to be refreshed */
        @Override
        public void refresh() {
            // do not need to implement
        }

        /**
         * <code>true </code> if tranasction is the same one that provided this lock
         *
         * @param transaction Transaction to check authorization against
         * @return true if transaction is authorized
         */
        @Override
        public boolean isAuthorized(Transaction transaction) {
            return this.transaction == transaction;
        }

        /**
         * Call back from Transaction.State
         *
         * @param AuthID AuthoID being added to transaction
         * @throws IOException Not used
         */
        @Override
        public void addAuthorization(String AuthID) throws IOException {
            // we don't need this callback
        }

        /**
         * Will make lock stale on commit
         *
         * @throws IOException If anything goes wrong
         */
        @Override
        public void commit() throws IOException {
            release();
        }

        /**
         * Will make lock stale on rollback
         *
         * @throws IOException If anything goes wrong
         */
        @Override
        public void rollback() throws IOException {
            release();
        }

        /**
         * Will make lock stale if removed from Transaction
         *
         * @param transaction Transaction on putState, or null on removeState
         */
        @Override
        public void setTransaction(Transaction transaction) {
            if (transaction == null) {
                release();
            }

            this.transaction = transaction;
        }

        @Override
        public String toString() {
            return "TranasctionLock(" + !isExpired() + ")";
        }
    }

    /**
     * Class referenced by featureID in locks( typeName).
     *
     * <p>FeatureLock is the request - MemoryLock is the result.
     *
     * @author Jody Garnett, Refractions Reasearch Inc.
     */
    static class MemoryLock implements Lock {
        String authID;
        long duration;
        long expiry;

        MemoryLock(FeatureLock lock) {
            this(lock.getAuthorization(), lock.getDuration());
        }

        MemoryLock(String id, long length) {
            authID = id;
            this.duration = length;
            expiry = System.currentTimeMillis() + length;
        }

        @Override
        public boolean isMatch(String id) {
            return authID.equals(id);
        }

        @Override
        public void refresh() {
            expiry = System.currentTimeMillis() + duration;
        }

        @Override
        public void release() {}

        @Override
        public boolean isExpired() {
            if (duration == 0) {
                return false; // perma lock
            }

            long now = System.currentTimeMillis();

            return now >= expiry;
        }

        @Override
        public boolean isAuthorized(Transaction transaction) {
            // LOGGER.info("checking authorization on " + this.toString() + ", "
            //  + ((transaction != Transaction.AUTO_COMMIT)
            //  ? transaction.getAuthorizations().toString() : "autocommit"));

            return (transaction != Transaction.AUTO_COMMIT)
                    && transaction.getAuthorizations().contains(authID);
        }

        @Override
        public String toString() {
            if (duration == 0) {
                return "MemoryLock(" + authID + "|PermaLock)";
            }

            long now = System.currentTimeMillis();
            long delta = (expiry - now);
            long dur = duration;

            return "MemoryLock(" + authID + "|" + delta + "ms|" + dur + "ms)";
        }
    }

    class LockingFeatureWriter implements DelegatingFeatureWriter<SimpleFeatureType, SimpleFeature> {

        private final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        private final Transaction transaction;
        private final String typeName;

        protected SimpleFeature live = null;

        public LockingFeatureWriter(FeatureWriter<SimpleFeatureType, SimpleFeature> writer, Transaction transaction) {
            this.writer = writer;
            this.transaction = transaction;
            this.typeName = writer.getFeatureType().getTypeName();
        }

        @Override
        public FeatureWriter<SimpleFeatureType, SimpleFeature> getDelegate() {
            return writer;
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return writer.getFeatureType();
        }

        @Override
        public SimpleFeature next() throws IOException {
            live = writer.next();
            return live;
        }

        @Override
        public void remove() throws IOException {
            if (live != null) {
                assertAccess(typeName, live.getID(), transaction);
            }

            writer.remove();
            live = null;
        }

        @Override
        public void write() throws IOException {
            if (live != null) {
                assertAccess(typeName, live.getID(), transaction);
            }

            writer.write();
            live = null;
        }

        @Override
        public boolean hasNext() throws IOException {
            live = null;
            return writer.hasNext();
        }

        @Override
        public void close() throws IOException {
            live = null;
            if (writer != null) {
                writer.close();
            }
        }
    }

    class LockingFlushingFeatureWriter extends LockingFeatureWriter implements Flushable {

        private final Flushable flushable;

        public LockingFlushingFeatureWriter(
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer, Transaction transaction) {
            super(writer, transaction);
            this.flushable = (Flushable) writer;
        }

        @Override
        public void flush() throws IOException {
            live = null;
            if (flushable != null) {
                flushable.flush();
            }
        }
    }
}
