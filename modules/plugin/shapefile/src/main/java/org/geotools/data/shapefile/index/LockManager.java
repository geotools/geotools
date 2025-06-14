/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.index;

/** @author Tommaso Nolli */
public class LockManager {
    private static final int EXCLUSIVE_LOCK_TIMEOUT = 20;
    private static final int SHARED_LOCK_TIMEOUT = 10;
    public static final short READ = 1;
    public static final short WRITE = 2;
    private Lock exclusiveLock;
    private int leases;

    public LockManager() {}

    public synchronized void release(Lock lock) {
        LockImpl li = (LockImpl) lock;

        if (li.getType() == Lock.EXCLUSIVE) {
            this.exclusiveLock = null;
        } else {
            this.leases--;
        }

        this.notify();
    }

    /** */
    public synchronized Lock aquireExclusive() throws LockTimeoutException {
        int cnt = 0;

        while ((this.exclusiveLock != null || this.leases > 0) && cnt < EXCLUSIVE_LOCK_TIMEOUT) {
            cnt++;

            try {
                this.wait(500);
            } catch (InterruptedException e) {
                throw new LockTimeoutException(e);
            }
        }

        if (this.exclusiveLock != null || this.leases > 0) {
            throw new LockTimeoutException("Timeout aquiring exclusive lock");
        }

        this.exclusiveLock = new LockImpl(Lock.EXCLUSIVE);

        return this.exclusiveLock;
    }

    /** */
    public synchronized Lock aquireShared() throws LockTimeoutException {
        int cnt = 0;

        while (this.exclusiveLock != null && cnt < SHARED_LOCK_TIMEOUT) {
            cnt++;

            try {
                this.wait(500);
            } catch (InterruptedException e) {
                throw new LockTimeoutException(e);
            }
        }

        if (this.exclusiveLock != null) {
            throw new LockTimeoutException("Timeout aquiring shared lock");
        }

        this.leases++;

        return new LockImpl(Lock.SHARED);
    }

    /** @author Tommaso Nolli */
    private static class LockImpl implements Lock {
        private short type;

        /** @param type */
        public LockImpl(short type) {
            this.type = type;
        }

        /** @see org.geotools.index.Lock#getType() */
        @Override
        public short getType() {
            return this.type;
        }
    }
}
