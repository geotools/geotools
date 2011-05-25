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
package org.geotools.graph.build;

import java.util.Collection;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

/**
 * Build the physical components of a Graph. The GraphBuilder manages the 
 * details of component creation, addition, and removal from the graph.<BR>
 * <BR>
 * Graph components are created through calls to buildNode() and buildEdge(Node,
 * Node), and then added to the graph through calls to addNode(Node), and 
 * addEdge(Edge).<BR>
 * <BR>
 * The GraphBuilder class is the lower level of the graph construction process.
 * The builder does not manage the entities being modelled by the graph 
 * components. This is done at a higher level by the GraphGenerator. 
 * class.
 * 
 * @see Graph
 * @see GraphGenerator
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public interface GraphBuilder {
    
    /**
     * Returns the graph being built.
     * 
     * @return Graph The graph being built.
     */
    public Graph getGraph();

    /**
     * Builds a new node for the graph. This method does not add the new node to 
     * the graph, this must be done with the addNode(Node) method.
     * 
     * @return Node The newly built node.
     */
    public Node buildNode();
    
    /**
     * Builds a new edge for the graph. This method does not add the new node to 
     * the graph, this must be done with the addEdge(Edge) method.
     * 
     * @param nodeA Adjacent node to edge.
     * @param nodeB Adjacent node to edge.
     * 
     * @return Edge the newly built Edge.
     */
    public Edge buildEdge(Node nodeA, Node nodeB);
    
    /**
     * Adds a node to the graph. 
     * 
     * @param node Node to be added to graph.
     */
    public void addNode(Node node);
    
    /**
     * Adds an edge to the graph. 
     * 
     * @param edge Edge to be added to graph.
     */
    public void addEdge(Edge edge);
    
    /** 
     * Removes an node from the graph.
     * 
     * @param node Node to be removed from graph. 
     */
    public void removeNode(Node node);
    
    /**
     * Removes a collection of nodes from the graph.
     * 
     * @param nodes A collection of nodes to be removed from the graph.
     */
    public void removeNodes(Collection nodes);
    
    /**
     * Removes an edge from the graph.
     * 
     * @param edge Edge to be removed from graph.
     */
    public void removeEdge(Edge edge);
    
    /**
     * Removes a collection of edges from the graph.
     * 
     * @param edges Collection of edges to be removed from the graph.
     */
    public void removeEdges(Collection edges);
    
    /**
     * Returns a clone of the builder. A deep clone copies the underlying
     * graph structure, a non deep clone results in an empty builder
     * 
     * @param deep Deep or non deep clone.
     * 
     * @return A graph builder. 
     */
    public Object clone(boolean deep) throws Exception;
    
    /** Constructs a graph builder from a pre built graph. The nodes and edges
     * of the existing graph are imported into the builder. Relationships between
     * nodes and edges are assummed to be preexistant. 
     * 
     * @param g A pre built graph.
     */
    public void importGraph(Graph g);
    
}
