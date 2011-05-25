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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.SpatialIndex;
import org.geotools.caching.spatialindex.Storage;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.type.FeatureType;


/** 
 * A simple in-memory storage relying on LinkedHashMap.
 * 
 * @author crousson
 *
 *
 *
 * @source $URL$
 */
public class MemoryStorage implements Storage {
    private HashMap<NodeIdentifier, Node> map;
    private ArrayList<FeatureType> featureTypes = null;
    private ReferencedEnvelope bounds = null;
    
    private MemoryStorage() {
        this.map = new HashMap<NodeIdentifier, Node>();
        featureTypes = new ArrayList<FeatureType>();
    }

    public static Storage createInstance(Properties pset) {
        return new MemoryStorage();
    }

    public static Storage createInstance() {
        return new MemoryStorage();
    }

    public Node get(NodeIdentifier id) {
        return map.get(id);
    }

    public void put(Node n) {
        if (!map.containsKey(n.getIdentifier())) {
            map.put(n.getIdentifier(), n);
        }
    }

    public void remove(NodeIdentifier id) {
        map.remove(id);
    }

    public void clear() {
        map.clear();
    }

    public void setParent(SpatialIndex index) {
        // do nothing - we do not need back link for this storage
    }

    public void flush() {
        // do nothing
    }
    
    public void dispose(){
        clear();
    }

    public Properties getPropertySet() {
        Properties pset = new Properties();
        pset.setProperty(STORAGE_TYPE_PROPERTY, MemoryStorage.class.getCanonicalName());

        return pset;
    }

    public NodeIdentifier findUniqueInstance(NodeIdentifier id) {
        if (map.containsKey(id)) {
            return map.get(id).getIdentifier();
        } else {
            return id;
        }
    }

    public void addFeatureType( FeatureType ft ) {
        featureTypes.add(ft);
    }

    public Collection<FeatureType> getFeatureTypes() {
        return Collections.unmodifiableCollection(featureTypes);
    }

    public void clearFeatureTypes(){
        featureTypes.clear();
    }

    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    public void setBounds( ReferencedEnvelope bounds ) {
        this.bounds = bounds;
    }
    
}
