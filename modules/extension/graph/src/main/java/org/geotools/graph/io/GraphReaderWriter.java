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
package org.geotools.graph.io;

import org.geotools.graph.structure.Graph;

/**
 * Reads and writes features to and from some permanent form. 
 *  
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public interface GraphReaderWriter {
  
  /**
   * Sets a property for the reader/writer.
   * 
   * @param name Name of the property.
   * @param obj Value of the property.
   */
  public void setProperty(String name, Object obj);
  
  /**
   * Returns a property for the reader/writer. This method will return null
   * if the property has not been set with a call to setProperty(String,Object).
   * 
   * @param name Name of the property.
   * @return Value of the property or null if the property is undefined.
   */
  public Object getProperty(String name);
  
  /**
   * Creates a Graph from some permanent representation.
   * 
   * @return The represented graph.
   * 
   * @throws Exception
   */
  public Graph read() throws Exception;
  
  /**
   * Writes the graph to a permanent representation.
   * 
   * @param g The graph to be 
   * @throws Exception
   */
  public void write(Graph g) throws Exception; 

}
