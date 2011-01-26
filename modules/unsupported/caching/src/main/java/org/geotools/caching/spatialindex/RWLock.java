// Spatial Index Library
//
// Copyright (C) 2002  Navel Ltd.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation;
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License aint with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Contact information:
//  Mailing address:
//    Marios Hadjieleftheriou
//    University of California, Riverside
//    Department of Computer Science
//    Surge Building, Room 310
//    Riverside, CA 92521
//
//  Email:
//    marioh@cs.ucr.edu

// Readers/Writers lock by Allen Holub
package org.geotools.caching.spatialindex;

import java.util.LinkedList;


public class RWLock {
    private int active_readers;
    private int waiting_readers;
    private int active_writers;
    private final LinkedList writer_locks = new LinkedList();

    public synchronized void read_lock() {
        if ((active_writers == 0) && (writer_locks.size() == 0)) {
            ++active_readers;
        } else {
            ++waiting_readers;

            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized boolean read_lock_noblock() {
        if ((active_writers == 0) && (writer_locks.size() == 0)) {
            ++active_readers;

            return true;
        }

        return false;
    }

    public synchronized void read_unlock() {
        if (--active_readers == 0) {
            notify_writers();
        }
    }

    public void write_lock() {
        Object lock = new Object();

        synchronized (lock) {
            synchronized (this) {
                boolean okay_to_write = (writer_locks.size() == 0) && (active_readers == 0)
                    && (active_writers == 0);

                if (okay_to_write) {
                    ++active_writers;

                    return; // the "return" jumps over the "wait" call
                }

                writer_locks.addLast(lock);
            }

            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    synchronized public boolean write_lock_noblock() {
        if ((writer_locks.size() == 0) && (active_readers == 0) && (active_writers == 0)) {
            ++active_writers;

            return true;
        }

        return false;
    }

    public synchronized void write_unlock() {
        --active_writers;

        if (waiting_readers > 0) { // priority to waiting readers
            notify_readers();
        } else {
            notify_writers();
        }
    }

    private void notify_readers() // must be accessed from a
     { //  synchronized method
        active_readers += waiting_readers;
        waiting_readers = 0;
        notifyAll();
    }

    private void notify_writers() // must be accessed from a
     { //  synchronized method

        if (writer_locks.size() > 0) {
            Object oldest = writer_locks.removeFirst();
            ++active_writers;

            synchronized (oldest) {
                oldest.notify();
            }
        }
    }
}
