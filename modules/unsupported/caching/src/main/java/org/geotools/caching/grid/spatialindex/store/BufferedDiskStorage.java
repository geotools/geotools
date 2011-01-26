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
package org.geotools.caching.grid.spatialindex.store;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Storage;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.type.FeatureType;

/**
 * Disk storage that buffers read/writes of nodes.
 * 
 * <p>To ensure all data has been written flush() should be called.</p>
 * 
 *
 *
 * @source $URL$
 */
public class BufferedDiskStorage implements Storage {
    public final static String BUFFER_SIZE_PROPERTY = "BufferedDiskStorage.BufferSize";
    protected static Logger logger = org.geotools.util.logging.Logging.getLogger("org.geotools.caching.spatialindex.store");
    
    private DiskStorage storage;			//underlying disk storage
    private Map<NodeIdentifier, Node> buffer;	//buffer
    private Set<Node> dirtyNodes;			//dirty nodes that need to be flushed to disk

    private int buffer_size;

    /**
     * Creates a new buffer with the given size.
     * @param buffersize
     */
    private BufferedDiskStorage( int buffersize ) {
        this.buffer_size = buffersize;
        buffer = Collections.synchronizedMap(new LinkedHashMap<NodeIdentifier, Node>(buffer_size, .75f, true));
        dirtyNodes = Collections.synchronizedSet(new HashSet<Node>());
    }

    public static Storage createInstance( Properties pset ) {
        try {
            int buffer_size = Integer.parseInt(pset.getProperty(BUFFER_SIZE_PROPERTY));
            BufferedDiskStorage instance = new BufferedDiskStorage(buffer_size);
            instance.storage = (DiskStorage) DiskStorage.createInstance(pset);
            
            return instance;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("BufferedDiskStorage : invalid property set.", e);
        }
    }

    public static Storage createInstance() {
        BufferedDiskStorage instance = new BufferedDiskStorage(100);
        instance.storage = (DiskStorage) DiskStorage.createInstance();

        return instance;
    }

    /**
     * Clears the buffer
     */
    public void clear() {
    	dirtyNodes.clear();
        buffer.clear();    
        storage.clear();
        
    }

    /**
     * Disposes of the storage
     */
    public void dispose() {
        storage.dispose();
    }

    /**
     * Gets a particular node.
     * <p>First looks in buffer; if not found will
     * look in the underlying storage</p>
     */
    public Node get( NodeIdentifier id ) {
        Node ret = buffer.get(id);
        if (ret == null) {
            ret = storage.get(id);
            if (ret != null) {
                put(ret);
            }
        }
        return ret;
    }

    /**
     * Adds a node to the storage.
     * @param entry
     */
    private void putNode( Node entry ) {
        if (entry != null) {
            if (buffer.size() == buffer_size) {
                synchronized (buffer) {
                    Iterator<NodeIdentifier> it = buffer.keySet().iterator();
                    buffer.remove(it.next());
                    //TODO: should I flush here??
                }
                buffer.put(entry.getIdentifier(), entry);
            } else {
                buffer.put(entry.getIdentifier(), entry);
            }
        }
    }

    /**
     * Adds a node to the storage.
     */
    public void put( Node n ) {
        if (buffer.containsKey(n.getIdentifier())) {
            dirtyNodes.add(n);
        } else {
            putNode(n);
            dirtyNodes.add(n);
        }
    }

    /**
     * Removes a node from the storage.
     */
    public void remove( NodeIdentifier id ) {
        if (buffer.containsKey(id)) {
            buffer.remove(id);    
        } else {
            storage.remove(id);
        }
    }

    /**
     * Get storage properties
     */
    public Properties getPropertySet() {
        Properties pset = storage.getPropertySet();
        pset.setProperty(STORAGE_TYPE_PROPERTY, BufferedDiskStorage.class.getCanonicalName());
        pset.setProperty(BUFFER_SIZE_PROPERTY, new Integer(buffer_size).toString());
        return pset;
    }
    
    /**
     * Writes all dirty nodes to underlying disk storage.
     */
    public void flush() {
        synchronized (dirtyNodes) {
            for( Iterator<Node> it = dirtyNodes.iterator(); it.hasNext(); ) {
                Node entry = it.next();
                storage.put(entry);
            }
            dirtyNodes.clear();
        }
        storage.flush();
    }

    /**
     * Finds the unique node identifier
     */
    public NodeIdentifier findUniqueInstance( NodeIdentifier id ) {
        if (buffer.containsKey(id)) {
            return buffer.get(id).getIdentifier();
        } else {
            return storage.findUniqueInstance(id);
        }
    }
    /**
     * Adds a feature type to the storage
     */
    public void addFeatureType( FeatureType ft ) {
        this.storage.addFeatureType(ft);
    }

    /**
     * Removes feature types from the store
     */
    public Collection<FeatureType> getFeatureTypes() {
        return this.storage.getFeatureTypes();
    }

    /**
     * Clears all feature types
     */
    public void clearFeatureTypes() {
        this.storage.clearFeatureTypes();
    }

    /**
     * gets the bounds of the store
     */
    public ReferencedEnvelope getBounds() {
        return this.storage.getBounds();
    }

    /**
     * Sets the bounds of the data store.
     */
    public void setBounds( ReferencedEnvelope bounds ) {
        this.storage.setBounds(bounds);
    }
}
