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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PriorityQueue implements Collection, Queue {
  public static double RESIZE_FACTOR = 1.5;
  
  private Comparator m_comparator = null;
  private Object[] m_values = null;
  private int m_count = 0;
  private HashMap m_obj2index = null;
  
  public PriorityQueue(Comparator comparator) {
    m_comparator = comparator;
    m_obj2index = new HashMap();
  }
  
  public void init(int size) {
    if (m_values == null || size > m_values.length) resize(size+1, false);    
    
    for (int i = 0; i < m_values.length; i++) {
      m_values[i] = null;  
    }
    m_count = 0; 
  }  
  
  public void insert(Object value) {
    ++m_count;
    if (m_count >= m_values.length) 
      resize((int)(m_values.length*RESIZE_FACTOR), true);
      
    m_values[m_count] = value;
    m_obj2index.put(value, new Integer(m_count));
    
    moveUp(m_count);
  }
  
  public Object extract() {
    if (m_count == 0) 
      throw new NoSuchElementException("Heap empty.");
      
    Object value = m_values[1];
    swap(1, m_count);
    
    m_values[m_count--] = null;
    moveDown(1);   
    
    m_obj2index.remove(value);
    return(value);
  }
  
  public Object getRoot() {
    if (m_count == 0) 
      throw new NoSuchElementException("Heap Empty.");
    
    return(m_values[1]);
  }
  
  public void update() {
    int n = (int)size()/2;
    for (int i = 1; i <= n; i++) {
      update(i);    
    }
  }
  
  public int update(int i) {
    //check parent, if value is less, propogate value up
    if (i > 1 && compare(i, i/2) < 0) {
      return(moveUp(i));
    }
    
    //check children, if value more, propogate value down
    if ((2*i <= size() && compare(i, 2) > 0) 
    || (2*i+1 <= size() && compare(i, 2*i+1) > 0)) {
      return(moveDown(i));
    } 
    
    return(i);
  }
  
  
  public void update(Object value) {
    Integer index = ((Integer)m_obj2index.get(value));
    if (index == null) {
      for (int i = 1; i < m_count; i++) {
        Object o = (Object)m_values[i];
        if (o == value) 
          System.out.println();
      }
    }
    update(index.intValue());
    //TODO: improve performance, dont use a linear search
//    for (int i = 1; i < m_values.length; i++) {
//      if (m_values[i] == value) {
//        update(i);
//        return;
//      }
//    }
  }
  
  public boolean isEmpty() { return(m_count == 0); }
  public int size() { return(m_count); }
  
  private int moveDown(int n) {
    int minchild = 0;
   
    if(2*n <= m_count) {
      minchild = 2*n;
      if (2*n + 1 <= m_count) {
        minchild = (compare(2*n, 2*n + 1) < 0) ? 
          2*n : 2*n + 1;    
      }
      
      if (compare(minchild, n) < 0) {
        swap(minchild, n);
        return(moveDown(minchild));  
      }
    } 
    return(n);
  }
  
  private int moveUp(int n) {
    int parent = (n % 2 == 0) ? n/2 : (n - 1)/2;
    if (parent > 0 && compare(n, parent) < 0) {
      swap(n, parent);
      return(moveUp(parent));  
    }  
    return(n);
  }
  
  private int compare(int i, int j) {
    return(m_comparator.compare(m_values[i], m_values[j]));  
  }
  
  private void resize(int size, boolean preserve) {
    Object[] resized = new Object[size];
    
    if (preserve) {
      for (int i = 0; i < m_values.length && i < size; i++) {
        resized[i] = m_values[i];   
      }
    }
    
    m_values = resized;
  }
  
  /** TODO: DOCUMENT ME! Note that this method should be used cautiously **/
  public void swap(int i, int j) {
    //first swap the reference to the indicies
    if (m_obj2index.get(m_values[i]) == null || m_obj2index.get(m_values[j]) == null) 
      System.out.println();
    
    Object tmp = m_obj2index.get(m_values[i]);
    m_obj2index.put(m_values[i], m_obj2index.get(m_values[j]));
    m_obj2index.put(m_values[j], tmp);
    
    //swap objects in array
    tmp = m_values[i];
    m_values[i] = m_values[j];
    m_values[j] = tmp;  
    
  }

  public void clear() {
    init(0);  
  }

  public Object[] toArray() {
    return(m_values);  
  }

  public boolean add(Object o) {
    insert(o);
    return(true);
  }

  public Object get(int i) {
   if (i == 0 && i > m_count) return(null);
   return(m_values[i]); 
   
  }
  
  public boolean contains(Object o) {
    for (int i = 0; i < m_values.length; i++) {
      if (m_values[i].equals(o)) return(true);  
    }
    return(false);
  }

  public boolean remove(Object o) {
    //TODO: improve performance, dont use a linear search
    for (int i = 1; i < m_values.length; i++) {
      if (m_values[i] == o) {
        remove(i);
        return(true);
      }
    }
    return(false);
  }

  public void remove(int i) {
    //check to see if item is last in heap
    if (i < m_count) {
      //first do a swap with last element in heap
      swap(i, m_count);
    
      //remove last element
      m_values[m_count--] = null;
    
      //update previous last element
      update(i);
    }
    else {
      //simply remove the last element
      m_values[m_count--] = null;
    }
  }
  
  public boolean addAll(Collection c) {
    for (Iterator itr = c.iterator(); itr.hasNext();) {
      add(itr.next());  
    }
    return(true);
  }

  public boolean containsAll(Collection c) {
    for (Iterator itr = c.iterator(); itr.hasNext();) {
      if (!contains(itr.next())) return(false);  
    }
    return(true);
  }

  public boolean removeAll(Collection c) {
    throw new UnsupportedOperationException(
      "Heap#removeAll(Collection) not supported"  
    );
  }

  public boolean retainAll(Collection c) {
    throw new UnsupportedOperationException(
        "Heap#retainAll(Collection) not supported"  
    );
  }

  public Iterator iterator() {
    return(
      new Iterator() {
        int i = 1;
        public void remove() {
          throw new UnsupportedOperationException(
            "Iterator#remove() not supported"  
          );
        }

        public boolean hasNext() {
          return(i < m_values.length);  
        }

        public Object next() {
          return(m_values[i++]);
        }
      }
    );
  }

  public Object[] toArray(Object[] a) {
    if (a.length < m_values.length)
      a = (Object[])Array.newInstance(
        a.getClass().getComponentType(), m_values.length
      );
    
    for (int i = 0; i < m_values.length; i++) {
      a[i] = m_values[i];  
    }
    if (a.length > m_values.length) a[m_values.length] = null;
    return(a);
  }
  
  //Queue implementation
  public Object deq() {
    return(extract());
  }

  public void enq(Object object) {
    insert(object);
  }
}

