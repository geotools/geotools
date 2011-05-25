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

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.AStarIterator;
import org.geotools.graph.traverse.standard.AStarIterator.AStarFunctions;

/**
 * Calculates the shortest path between two nodes using the A Star algorithm
 * (for details see http://en.wikipedia.org/wiki/A_star) 
 * @see AStarIterator 
 * @author Germán E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
 *
 *
 * @source $URL$
 */
public class AStarShortestPathFinder implements GraphWalker {
        /** Graphs to calculate paths for **/
        private Graph m_graph;

        /** Graph traversal used for the A Star iteration **/
        private GraphTraversal m_traversal;

        /** Underling A Star iterator **/
        private AStarIterator m_iterator;

        /**  */
        private Node m_target;

        /**
        * Constructs a new path finder
        *
        * @param graph Graph where we will perform the search.
        * @param source Node to calculate path from.
        * @param target Node to calculate path to.
        * @param weighter Associates weights with edges in the graph.
        */
        public AStarShortestPathFinder (
                            Graph graph, Node source, Node target, AStarFunctions afuncs
        ) {
                m_graph = graph;
                m_target = target;
                m_iterator = new AStarIterator(source, afuncs);
                m_traversal = new BasicGraphTraversal(graph,this,m_iterator);
        }

        /**
        * Performs the graph traversal and calculates the shortest path from 
        * the source node to destiny node in the graph.
        */
        public void calculate() {
                m_traversal.init();
                m_traversal.traverse(); 
        }


        /**
        *
        * @see GraphWalker#visit(Graphable, GraphTraversal)
        */
        public int visit(Graphable element, GraphTraversal traversal) {
                if (element.equals(m_target)) {
                        return(GraphTraversal.STOP);
                } else {
                        return(GraphTraversal.CONTINUE);
                }
        }

        /**
        * Returns a path <B>from</B> the target <B>to</B> the source. If the desired path is
        * the opposite (from the source to the target), the <i>reverse</i> or the <i>riterator</i> 
        * methods from the <b>Path<b> class can be used.
        *
        * @see Path#riterator()
        * @see Path#reverse()
        *
        * @return A path from the target to the source.
        * @throws WrongPathException 
        */
        public Path getPath() throws WrongPathException {
                Path path = new Path();

                path.add(m_target);
                Node parent = m_iterator.getParent(m_target);
                while ( parent != null ) {
                        path.add(parent);
                        parent =  m_iterator.getParent(parent);
                }
                if (!path.getLast().equals(m_iterator.getSource())) {
                        throw new WrongPathException("getPath: The path obtained doesn't begin correctly");
                }
                return(path);
        }

        /**
        * Does nothing.
        *
        * @see GraphWalker#finish()
        */
        public void finish() {}
}

class WrongPathException extends Exception{
        String message;

        public WrongPathException(String msj){
               message = msj;
        }
        
        public String getMessage(){
        	return message;
        }
}
