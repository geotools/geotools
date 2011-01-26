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
package org.geotools.graph.build.polygon;

import java.util.Iterator;
import java.util.List;

import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * An implementation of GraphGenerator used to build graphs from a set 
 * of polygons.
 * <p>
 * This graph generator takes {@link com.vividsolutions.jts.geom.Polygon} 
 * objects as input when constructing a graph. The following code constructs
 * a graph from a set of polygons.
 * 
 * <pre>
 * 	<code>
 *  //get some polygons
 *  Polygon[] polygons = ...
 * 
 *  //determine what the relationship will be
 *  PolygonGraphGenerator rel = new PolygonGraphGenerator.PolygonRelationship() {
 *   
 *     public boolean related(Polygon p1, Polygon p2) {
 *	     return p1.intersects(p2);
 *     }
 *	
 *     public boolean equal(Polygon p1, Polygon p2) {
 *        return p1.equals(p2);
 *     }
 *  }
 *  //create the generator 
 *  PolygonGraphGenerator gg = new PolygonGraphGenerator(new BasicGraphBuilder(),rel);
 *  
 *  //start building
 *  for (int i = 0; i < polygons.length; i++) {
 *    gg.add(polygons[i]);
 *  }
 * 	</code>
 * </pre>
 * </p>
 * For each distinct polygon added to the graph, a node is created. If two 
 * polygons are considered equal, only a single node is created. If two 
 * polygons are considered related, the associated nodes share an edge. Equality
 * and relationship is determined by {@link org.geotools.graph.build.polygon.PolygonGraphGenerator.PolygonRelationship}
 * interface. An instance of this interface is passed to the generator at construction.

 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class PolygonGraphGenerator implements GraphGenerator {

	/**
	 * Determines the relationship among two polygons.
	 */
	public static interface PolygonRelationship {
		/**
		 * Determines if two polygons are related in any way. Rel
		 * @param p1
		 * @param p2
		 */
		boolean related(Polygon p1, Polygon p2);
		
		boolean equal(Polygon p1, Polygon p2);
	}
	
	/** store polygon to node mapping in spatial index **/
	Quadtree index;
	/** relationship between polygons in graph **/
	PolygonRelationship rel;
	/** the node/edge builder **/
	GraphBuilder builder;
	
	public PolygonGraphGenerator(GraphBuilder builder,PolygonRelationship rel) {
		setGraphBuilder(builder);
		this.rel = rel;
		
		index = new Quadtree();
	}
	
	public Graphable add(Object obj) {
		Node node = (Node) get(obj);
		if (node == null) {
			node = builder.buildNode();
			builder.addNode(node);
			
			node.setObject(obj);
			relate(node);
			
			//TODO: the envelope should be buffered by some tolerance
			index.insert(((Polygon)obj).getEnvelopeInternal(),node);
		}
		
		return node;
	}

	public Graphable get(Object obj) {
		Polygon polygon = (Polygon)obj;
		return find(polygon);
	}

	public Graphable remove(Object obj) {
		Node node = (Node) get(obj);
		if (node != null) {
			Polygon polygon = (Polygon) node.getObject();
			index.remove(polygon.getEnvelopeInternal(),node);
			
			builder.removeNode(node);
		}
		
		return node;
	}

	public void setGraphBuilder(GraphBuilder builder) {
		this.builder = builder;

	}

	public GraphBuilder getGraphBuilder() {
		return builder;
	}

	public Graph getGraph() {
		return builder.getGraph();
	}
	
	protected Node find(Polygon polygon) {
		List close = index.query(polygon.getEnvelopeInternal());
		for (Iterator itr = close.iterator(); itr.hasNext();) {
			Node node = (Node)itr.next();
			Polygon p = (Polygon)node.getObject();
			
			if (rel.equal(polygon,p)) {
				return node;
			}
		}
		
		return null;
	}
	
	protected void relate(Node node) {
		Polygon polygon = (Polygon)node.getObject();
		List close = index.query(polygon.getEnvelopeInternal());
		
		for (Iterator itr = close.iterator(); itr.hasNext();) {
			Node n = (Node)itr.next();
			Polygon p = (Polygon)n.getObject();
			
			if (!rel.equal(polygon,p) && rel.related(polygon,p)) {
				Edge edge = builder.buildEdge(node,n);
				builder.addEdge(edge);
				builder.addEdge(edge);
			}
		}
	}

}
