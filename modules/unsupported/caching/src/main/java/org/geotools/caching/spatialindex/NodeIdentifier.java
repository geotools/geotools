/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.spatialindex;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/** Instances of this class provide unique identifiers for nodes,
 * and are used to store and retrieve nodes from their storage.
 * Implementors must take care that instances have to be immutable.
 * Nodes are basically identified by the region they represent.
 * Kinds of nodes or kinds of storage may require to use other elements to identify
 * nodes. Implementors must take care to override hashCode() and equals() accordingly.
 * NodeIdentifier should not reference the node they identify,
 * as they are likely to be used to passivate nodes in secondary storage.
 *
 * @author crousson
 *
 *
 *
 * @source $URL$
 */
public abstract class NodeIdentifier implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//wait a maximum of 5 minutes for lock; this should never be necessary it's just a safe guard against unexpected behaviour
	private static final long LOCK_TIMEOUT = 5 * 60; //5min timeout
	private static final TimeUnit LOCK_TIMEOUT_UNITS = TimeUnit.SECONDS;

	private boolean valid = false;
   
    transient ReentrantReadWriteLock lock;
    transient SoftReference<Node> node;
    
    NodeIdentifier(){
        lock = new ReentrantReadWriteLock();
    }

    public abstract Shape getShape();
    
    /**
     * 
     * @return if the nodes data is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the validity of the node; true means the data
     * in the node is ready for reading.
     * 
     * @param valid
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    /**
     * Acquire a write lock on the node
     */
    public void writeLock() throws Exception {
        if (!lock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)){
            throw new Exception("Timed out waiting for write lock on node."); 
        }
        //lock.writeLock().lock();
    }
    /**
     * Unlock the write lock
     */
    public void writeUnLock(){
        lock.writeLock().unlock();
    }
    /**
     * Acquire a read lock on the node
     */
    public void readLock() throws Exception {
        if (!lock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)){
            throw new Exception("Timed out waiting for read lock on node.");
        }
        //lock.readLock().lock();
    }
    /**
     * Unlock read lock
     */
    public void readUnLock(){
        lock.readLock().unlock();
    }
    
    /**
     *  Only want to write to nodes that aren't being read.
     */
    public boolean isWritable(){
        return lock.getReadLockCount() == 0;
    }
    public boolean isLocked(){
        return lock.getReadLockCount() >0 || lock.isWriteLocked();
    }
    
    /**
     * Sets the node associated with the nodeid; this node
     * is stored as a soft reference so getNode() may 
     * return null.
     * 
     * @param n
     */
    public void setNode(Node n){
        this.node = new SoftReference<Node>(n);
    }
    /**
     * May return null; node is held onto with a soft reference
     *
     * @return
     */
    public Node getNode(){
        if (this.node == null) return null;
        return this.node.get();
    }
    
    private void writeObject(ObjectOutputStream stream) throws IOException{
        stream.writeBoolean(valid);
    }
    
    private  void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
        this.valid = stream.readBoolean();
        this.lock = new ReentrantReadWriteLock();
    }
}
