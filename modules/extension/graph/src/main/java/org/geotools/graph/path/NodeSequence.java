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
package org.geotools.graph.path;

import java.util.Iterator;

import org.geotools.graph.structure.Node;

/**
 * Represents a sequence of nodes in a graph.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public interface NodeSequence {
  
  /**
   * Returns the first node in the sequence.
   * 
   * @return Object of tupe Node.
   */
  public Node getFirst();
  
  /**
   * Returns the last node in the sequence.
   * 
   * @return Object of type node.
   */
  public Node getLast();
  
  /**
   * Returns the number of nodes in the sequence.
   * 
   * @return an integer representing the number of nodes in the sequence.
   */
  public int size();
  
  /**
   * Returns an iterator over the nodes.
   * 
   * @return An iterator.
   */
  public Iterator iterator();
  
  /**
   * Determines if the node sequence id valid based on the rules of the 
   * implementation.
   * 
   * @return True if valid, otherwise false.
   */
  public boolean isValid();    
}
