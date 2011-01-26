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
package org.geotools.graph.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MultiMap implements Map, Serializable {
  
  private Map m_map = null;
  private Class m_collectionClass;
  
  public MultiMap(Map map, Class collectionClass)  {
    m_map = map;
    m_collectionClass = collectionClass;
  }  
  
  public Object put(Object key, Object value) {
    Collection c = null;
    
    if ((c = (Collection)m_map.get(key)) == null) {
      try {
        c = (Collection)m_collectionClass.newInstance();
      } 
      catch (InstantiationException e) {
        throw new IllegalStateException(
          e.getClass().getName() + ": " + e.getMessage()  
        );
      } 
      catch (IllegalAccessException e) {
        throw new IllegalStateException(
          e.getClass().getName() + ": " + e.getMessage()  
        );
      }
      
      m_map.put(key, c);   
    }  
    
    c.add(value);
    return(c);
  }

  public void putItems(Object key, Collection items) {
    m_map.put(key, items);  
  }
  
  public int size() {
    return(m_map.size());
  }

  public void clear() {
    m_map.clear();
  }

  public boolean isEmpty() {
    return(m_map.isEmpty());
  }

  public boolean containsKey(Object key) {
    return(m_map.containsKey(key));
  }

  public boolean containsValue(Object value) {
    for (Iterator itr = values().iterator(); itr.hasNext();) {
      Collection c = (Collection)itr.next();
      if (c.contains(value)) return(true);  
    }
    return(false);
  }

  public Collection values() {
    return(m_map.values());
  }

  public void putAll(Map t) {
    for (Iterator itr = t.entrySet().iterator(); itr.hasNext();) {
      Map.Entry entry = (Map.Entry)itr.next();
      put(entry.getKey(), entry.getValue());
    }
  }

  public Set entrySet() {
    return(m_map.entrySet());
  }

  public Set keySet() {
    return(m_map.keySet());
  }

  public Object get(Object key) {
    Object obj = null;
    if ((obj = m_map.get(key)) == null) {
      try {
        obj = m_collectionClass.newInstance();
      } 
      catch (InstantiationException e) {
        throw new IllegalStateException(
          e.getClass().getName() + ": " + e.getMessage()  
        );
      } 
      catch (IllegalAccessException e) {
        throw new IllegalStateException(
          e.getClass().getName() + ": " + e.getMessage()  
        );
      }    
      putItems(key, (Collection)obj);
    }
    return(obj);
  }

  public Collection getItems(Object key) {
    return((Collection)get(key));  
  }
  
  public Object remove(Object key) {
    return(m_map.remove(key));
  }
  
  public Object remove(Object key, Object value) {
    Collection c = (Collection)m_map.get(key);
    c.remove(value);
    return(value);
  }
  

    
}
