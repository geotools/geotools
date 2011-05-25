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
package org.geotools.graph.build.line;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * Builds a graph representing a line network in which edges in the network are
 * represented by LineString geometries. This implementation is a wrapper around
 * a LineGraphGenerator which sets underlying edge objects to be LineString
 * objects, and underlying Node objects to be Point objects. While generating
 * the graph, the generator uses the visited flag of created components to 
 * determine when to create underying objects. For this reason it is not recomended 
 * to modify the visited flag of any graph components. 
 * 
 * @see com.vividsolutions.jts.geom.LineString
 * @see com.vividsolutions.jts.geom.Point
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class LineStringGraphGenerator extends BasicLineGraphGenerator {

  private static GeometryFactory gf = new GeometryFactory();
  
  public Graphable add(Object obj) {
  	LineString ls = null;
  	if (obj instanceof MultiLineString)
  		ls = (LineString) ((MultiLineString)obj).getGeometryN(0);
  	else
  		ls = (LineString)obj;
    
    //parent class expects a line segment
    Edge e = (Edge)super.add(
      new LineSegment(
        ls.getCoordinateN(0), ls.getCoordinateN(ls.getNumPoints()-1)
      )
    );
    
    //over write object to be the linestring
    e.setObject(ls);
    return(e);
  }

  public Graphable remove(Object obj) {
    LineString ls = (LineString)obj;
    
    //parent ecpexts a line segment
    return(
      super.remove(
        new LineSegment(
          ls.getCoordinateN(0), ls.getCoordinateN(ls.getNumPoints()-1)
        )  
      )
    );
  }

  public Graphable get(Object obj) {
    LineString ls = (LineString)obj;
      
    //parent ecpexts a line segment
    return(
      super.get(
        new LineSegment(
          ls.getCoordinateN(0), ls.getCoordinateN(ls.getNumPoints()-1)
        )  
      )
    );
  }


  protected void setObject(Node n, Object obj) {
    //set underlying object to be point instead of coordinate
    Coordinate c = (Coordinate)obj;
    n.setObject(gf.createPoint(c));
  }

//  /** underlying line graph generator **/
//  private LineGraphGenerator m_generator;
//  
//  /**
//   * Constructs a new LineStringGraphGenerator.
//   *
//   */
//  public LineStringGraphGenerator() {
//    this(new BasicLineGraphGenerator());	
//  }
//  
//  /**
//   * Constructs a new LineStringGraphGenerator.
//   * 
//   * @param generator Underlying generator.
//   */
//  public LineStringGraphGenerator(LineGraphGenerator generator) {
//    m_generator = generator;  
//  }
//  
//  /**
//   * Adds a LineString object to the graph. An edge is created to represent the 
//   * LineString and its underlying object is set to the LineString.
//   * 
//   * @param obj A LineString.
//   * 
//   * @see GraphGenerator#add(Object)
//   */
//  public Graphable add(Object obj) {
//    LineString line = (LineString)obj;
//    Coordinate[] c = line.getCoordinates();
//    
//    //instruct the underlying line graph generate and edge represented by 
//    // the endpoints of the linestring, set the underlying object to be the
//    //linstring itself
//    Edge e = (Edge)m_generator.add(
//      new LineSegment(c[0], c[c.length-1])
//    );  
//    e.setObject(line);
//    
//    //check nodes of edge, if not visited, set object to point
//    if (!e.getNodeA().isVisited()) {
//      e.getNodeA().setVisited(true);
//      e.getNodeA().setObject(line.getPointN(0));
//    }
//    
//    if (!e.getNodeB().isVisited()) {
//      e.getNodeB().setVisited(true);
//      e.getNodeB().setObject(line.getPointN(c.length-1));  
//    }
//    
//    return(e);
//  }
//
//  /**
//   * Returns the edge created to represent a LineString object.
//   * 
//   * @param obj A LineString.
//   * 
//   * @see GraphGenerator#get(Object)
//   */
//  public Graphable get(Object obj) {
//    LineString line = (LineString)obj;
//    Coordinate[] c = line.getCoordinates();
//    
//    Edge e = (Edge)m_generator.get(
//      new LineSegment(c[0], c[c.length-1])
//    );
//    
//    return(e);
//  }
//
//  /**
//   * Removes an edge from the graph which represents a LineString object.
//   * 
//   * @param A LineString.
//   * 
//   * @see GraphGenerator#remove(Object)
//   */
//  public Graphable remove(Object obj) {
//    LineString line = (LineString)obj;
//    Coordinate[] c = line.getCoordinates();
//    
//    Edge e = (Edge)m_generator.remove(
//      new LineSegment(c[0], c[c.length-1])
//    );
//    
//    return(e);
//  }
//
//  /**
//   * Sets the underlying builder of the underlying generator.
//   * 
//   * @see GraphGenerator#setGraphBuilder(GraphBuilder)
//   */
//  public void setGraphBuilder(GraphBuilder builder) {
//    m_generator.setGraphBuilder(builder);  
//  }
//
//  /**
//   * Returns the underlying builder of the underlying generator.
//   * 
//   * @see GraphGenerator#getGraphBuilder()
//   */
//  public GraphBuilder getGraphBuilder() {
//    return(m_generator.getGraphBuilder());
//  }
//
//  /**
//   * @see GraphGenerator#getGraph()
//   */
//  public Graph getGraph() {
//    return(m_generator.getGraph());
//  }
//
//  public Node getNode(Coordinate c) {
//    return(m_generator.getNode(c)); 
//  }
//
//  public Edge getEdge(Coordinate c1, Coordinate c2) {
//    return(m_generator.getEdge(c1,c2));
//  }
}
