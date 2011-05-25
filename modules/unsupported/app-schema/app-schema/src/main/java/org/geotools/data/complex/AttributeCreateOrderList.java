/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Collection of AttributeMappings that correspond to complex types that need to be created.
 * It returns them in an order such that parent elements are created first, and so children
 * elements can be slotted straight into them.
 * 
 * @author Russell Petty, GSV
 *
 *
 *
 * @source $URL$
 */
public class AttributeCreateOrderList {

    private String rootLabel; 
    private Map<String, List<TreeAttributeMapping>> childrenList = new HashMap<String, List<TreeAttributeMapping>>();


public AttributeCreateOrderList(String rootLabel) {
        this.rootLabel = rootLabel;
        List<TreeAttributeMapping> newAttMapping = new ArrayList<TreeAttributeMapping>();
        childrenList.put(rootLabel, newAttMapping);
    }

/**
 * Attempt to add an attribute to the list.
 * @param attMapping attribute to add
 * @return whether an item was added or not.
 */
public boolean put(TreeAttributeMapping attMapping) {
    String parentLabel = attMapping.getParentLabel();
    if(attMapping.getLabel() == null || parentLabel == null) {
        return false;
    }
    if(attMapping.getLabel().equals(parentLabel)) {
        throw new IllegalArgumentException("Parents label same as label!");
    }
    
    List<TreeAttributeMapping> newAttMapping;
    if(childrenList.containsKey(parentLabel)) {
        newAttMapping = childrenList.get(parentLabel);        
    } else {
        newAttMapping = new ArrayList<TreeAttributeMapping>();
        childrenList.put(parentLabel, newAttMapping);
    }
    newAttMapping.add(attMapping);
    return true;
}

public Iterator<TreeAttributeMapping> iterator() {
    return new InnerClass();
}

public void setRootLabel(String rootLabel) {
    this.rootLabel = rootLabel;
}

class InnerClass implements Iterator<TreeAttributeMapping>{
    boolean isInitialised = false;
    boolean isHasNextBeenCalled = false;    
    private Iterator<TreeAttributeMapping> currentListIterator;
    private Set<String> unprocessedTreeNodes = new HashSet<String>(childrenList.keySet());
    private Set<String> returnedUnprocessedNodes = new HashSet<String>();
    
    public boolean hasNext() {
        isHasNextBeenCalled = true;
        if(!isInitialised) {
            initialise(); 
        }
        
        if(currentListIterator.hasNext()) {
            return true;
        }
        
        if(unprocessedTreeNodes.isEmpty()) {
            return false;
        }

        getNextList();
        return true;
    }

    public TreeAttributeMapping next() {
        if(!isHasNextBeenCalled) {
            throw new IllegalStateException("next method called without hasNext being called first.");
        }
        isHasNextBeenCalled = false;
        TreeAttributeMapping next = currentListIterator.next();
        returnedUnprocessedNodes.add(next.getLabel());
        return next;        
    }

    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
   
    private void initialise() {
        isInitialised = true;        
        List<TreeAttributeMapping> currentList = childrenList.get(rootLabel);
        currentListIterator = currentList.iterator();
        unprocessedTreeNodes.remove(rootLabel);
    }
    
    private void getNextList() {
        Iterator<String> it = returnedUnprocessedNodes.iterator();
        boolean listFound = false;
        String element = null;;
        while(it.hasNext() && !listFound) {
            element = it.next();
            if(unprocessedTreeNodes.contains(element)) {
                listFound = true;
                unprocessedTreeNodes.remove(element);
                it.remove();
            } else {
                //returned element has no children to process
                //remove for efficiency.
                it.remove();
            }
        }
        if(!listFound) {
           throw new IllegalStateException("Error in tree structure.  No created elements link to unprocessed children elements");
        }
        List<TreeAttributeMapping> currentList = childrenList.get(element);
        currentListIterator = currentList.iterator();
    }
}
}
