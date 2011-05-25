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
package org.geotools.graph.traverse.standard;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.SourceGraphIterator;
import org.geotools.graph.util.PriorityQueue;

/**
*  <B> A Star </B>:  It uses a function (usually denoted f(x)) to determine the order in 
*  which the algorithm visits nodes, f(x) is a sum of two functions: 
*     - The path-cost function (usually denoted g(x), which may or may not be a heuristic) and
*     - An admissible "heuristic estimate" (usually denoted h(x)).
*
*   the itaration oparates as follows:
*
*     // COST(n,n') : the real cost to go from n to n'
*     OPEN = [Source]
*     CLOSE = []
*
* 
*     while ( |OPEN| > 0 ) 
*         n = a node in OPEN with less f 
*         remove n from OPEN 
*         add n to CLOSE
*         if ( n == target ) {
*            return  // path find
*
*         // if n != target
*         for each node n' that relates to n do 
*             if n' in OPEN 
*                 if (g(n) + COST(n,n')) < g(n') 
*                     g(n') = g(n) + COST(n,n')
*                     parent(n') = n
*             else 
*                 g(n') = g(n) + COST(n,n')
*                 parent(n') = n
*                 add n' to OPEN
*     // end while
*
*
* (for details see http://en.wikipedia.org/wiki/A_star)
*
* @author Germán E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
 *
 *
 * @source $URL$
*/
public class AStarIterator extends SourceGraphIterator{
        /** function necesaries for A Star algorithm*/
        private AStarFunctions m_afuncs;
        /** queue that represents the open list of the A Star algorithm */
        private PriorityQueue m_pqueue;
        /** map of graph node to internal A* node **/
        private HashMap m_nodemap;



       public AStarIterator (Node source, AStarFunctions afuncs) {
               AStarNode asn;

               m_afuncs = afuncs;
               asn = new AStarNode(source,afuncs.h(source));
               asn.setG(0);
               setSource(source);
               m_nodemap = new HashMap();
               m_nodemap.put(source, asn);
               m_pqueue = new PriorityQueue(comparator);
               m_pqueue.init(100);
               m_pqueue.add(asn);
       }

       /**
       * Does Nothing. All the work was already done by the constructor.
       */
       public void init(Graph graph, GraphTraversal traversal) {}

       /**
       * Returns the next node in the priority queue. if the queue is empty then there is
       * no path from the source to the destiny in this graph.
       *
       * @see org.geotools.graph.traverse.GraphIterator#next()  
       */
       public Graphable next(GraphTraversal traversal) {
               if (m_pqueue.isEmpty()) {
                       return null;
               }
               AStarNode next = (AStarNode) m_pqueue.extract();
               return(next.getNode());
       }


        /**
        * Makes a step of the A* algorithm. Takes the current node, looks for its neighbours. The ones
        * which are closed are discarted. The ones "in" the opened queue are checked to see if its
        * necessary to update them. The rest of the nodes are initialized as AStarNodes and inserted
        * in the opened queue.
        * 
        * @see org.geotools.graph.traverse.GraphIterator#cont(Graphable)
        */
        public void cont(Graphable current, GraphTraversal traversal) {
                Node currdn = (Node) current;
                AStarNode currAsn;
                AStarNode nextAsn;


                currAsn = (AStarNode) m_nodemap.get(currdn);
                if (currAsn == null) {
                        throw new IllegalArgumentException("AStarIterator: The node is not in the open list");
                }
                currAsn.close();
                for (Iterator itr = currdn.getRelated(); itr.hasNext();) {
                        Node next = (Node) itr.next();
                        if (m_nodemap.containsKey(next)) {
                                nextAsn = (AStarNode) m_nodemap.get(next);
                                if(!nextAsn.isClosed()) {
                                        if ((currAsn.getG() + m_afuncs.cost(currAsn, nextAsn)) < nextAsn.getG()) {
                                                nextAsn.setG(currAsn.getG() + m_afuncs.cost(currAsn, nextAsn));
                                                nextAsn.setParent(currAsn);
                                                m_pqueue.update(nextAsn);
                                        }
                                }
                        } else {        // create new AStarNode
                                nextAsn = new AStarNode(next,m_afuncs.h(next));
                                nextAsn.setG(currAsn.getG() + m_afuncs.cost(currAsn, nextAsn));
                                nextAsn.setParent(currAsn);
                                m_pqueue.add(nextAsn);
                                m_nodemap.put(next, nextAsn);
                       }
                }
        }


        /**
        * Kills the branch of the traversal 
        * 
        * @see org.geotools.graph.traverse.GraphIterator#killBranch(Graphable)
        */
        public void killBranch(Graphable current, GraphTraversal traversal) {
                //do nothing  
        }

        /** */
        public Node getParent(Node n) {
                AStarNode asn = (AStarNode) m_nodemap.get(n);
                return (asn == null ? null : asn.getParent() == null ? null : asn.getParent().getNode());
        }

        /** Decides wich node has more priority */
        private static Comparator comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                        AStarNode n1 = (AStarNode) o1;
                        AStarNode n2 = (AStarNode) o2;
                        return(n1.getF() < n2.getF() ? -1 : n1.getF() > n2.getF() ? 1 : 0);
                }
        };

        /**
        * Internal data structure used to track node costs, and parent nodes.
        *
        * @author German E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
        *
        */
        public static class AStarNode {
                /** Node of the graph asociated with this A Star Node. */
                private Node node;

                /** AStarNode parent of this node. */
                private AStarNode parent;

                /** the real cost of the path so far. */
                private double g;

                /** the value of the heuristic function. */
                private double h;

                /** The node is in the CLOSE list */
                private boolean closed;

                public AStarNode (Node n, double h_val) {
                        node = n;
                        parent = null;
                        g = Double.POSITIVE_INFINITY;
                        h = h_val;
                        closed = false;
                }

                public boolean isClosed() {
                        return closed;
                }

                public void close () {
                        closed = true;
                }

                public double getG() {
                        return g;
                }

                public double getH() {
                        return h;
                }

                public double getF() {
                        return (g+h);
                }

                public AStarNode getParent() {
                        return parent;
                }

                public Node getNode() {
                        return node;
                }

                public void setG (double value) {
                        g = value;
                }

                public void setH (double value) {
                        h = value;
                }

                public void setNode (Node n) {
                        node = n;
                }

                public void setParent (AStarNode an) {
                        parent = an;
                }
        }	

        /**
        * Defines the functions needed by A Star.
        * 
        * @author German E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
        *
        */
        public static abstract class AStarFunctions {
                private Node dest;

                /** Creates a new instance and sets up the destination node for the algorithm */
                public AStarFunctions(Node destination){
                        dest = destination;
                }

                /** Sets up the destination node for the algorithm */
                public void setDestination(Node destination){
                        dest = destination;
                }

                /** Defines the cost of going from one node to another */
                public abstract double cost(AStarNode n1, AStarNode n2);

                /** Defines the heuristic function for n */
                public abstract double h(Node n);

                public Node getDest(){
                        return dest;
                }
        }
}
