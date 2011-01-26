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
package org.geotools.graph.traverse;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;

/**
 * Defines an algorithm in which to traverse the components of a graph. 
 * A graph iterator operates by repeatedly returing graph components to the 
 * caller. The order in which to return the components is specific to the 
 * iterator. However, <B>most</B> iterators follow the following conventions:<BR>
 * <BR>
 * <UL>
 *   <LI>Components are visited only once</LI>
 *   <LI>The next component to be returned is determined by the components that
 *       have been previously visited
 * </UL>
 * The following is an example of a GraphIterator. It returns nodes of a graph
 * in a standard <B>Depth First Search</B> order, starting from a specified node. 
 * The nodes have been numbered to correspond to the iteration pattern. <BR>
 * <BR>
 * <IMG src="doc-files/dfs.gif"/>
 * * indicates source of traversal<BR>
 * <BR>
 * <BR>
 * In order to analyze the traversal, the following terms are defined:<BR>
 * <BR>
 * The <B>Next Set</B> of a traversal is the set of components that will be 
 * visited in a later stage of the traversal.<BR>
 * The <B>Branch Set</B> of an component <B>e</B> is defined as the set of
 * components that can be visited in a later stage of the traversal as a direct
 * result of visiting <B>e</B>.
 * <BR>
 * <BR>
 * In most traversals, the two sets are related. The Next Set is built by 
 * analyzing the Branch Set of the component being visited in the current stage
 * of the traversal. Revisiting the above example, a Depth First Search 
 * Traversal operates as follows:<BR>
 * <BR>
 * <UL>
 *   <LI>Each node is visited only once.</LI>
 *   <LI>The Next Set is organized as a <B>Last In First Out</B> Queue (Stack).</LI>
 *   <LI>At each stage, every node in the Branch Set that has not yet been visited
 *       is added to the Next Set. 
 * </UL>
 * As well it is assumed that nodes related to a node are sorted in 
 * alphabetic order.
 * <BR>
 * <BR>
 * The following table summarizes the stages of the traversal:<BR>
 * <BR>
 * <TABLE border="1" style="font-family:Arial;font-size:10pt;" width="80%">
 *   <TH>Stage</TH>
 *   <TH>Visited Node</TH>
 *   <TH>Branch Set </TH>
 *   <TH>Next Set</TH>
 *   <TH>Comment</TH>
 *   <TR align="center">
 *     <TD align="center" width="10%">0</TD>
 *     <TD width="10%">&nbsp;</TD>
 *     <TD width="10%">&nbsp;</TD>
 *     <TD width="10%">{A}</TD>
 *     <TD width="40%" align="left">&nbsp;&nbsp;Initial stage, iteration starts explicitly from A</TD> 
 *   </TR>
 *   <TR align="center">
 *     <TD>1</TD><TD>A</TD><TD>{B,F}</TD><TD>{F,B}</TD>
 *     <TD align="left">&nbsp;&nbsp;Related nodes added to <B>Next Set</B> in LIFO order.</TD> 
 *   </TR>
 *   <TR align="center">
 *     <TD>2</TD><TD>F</TD><TD>{A,B}</TD><TD>{B,B}</TD> 
 *     <TD align="left">&nbsp;&nbsp;A already visited so not added to <B>Next Set</B><BR>
 *         &nbsp;&nbsp;B not yet visited so added to queue.</TD>
 *   </TR>
 *   <TR align="center">
 *     <TD>3</TD><TD>B</TD><TD>{A,C,D,E,F}</TD><TD>{B,E,D,C}</TD>
 *     <TD align="left">&nbsp;&nbsp;A,F already visited so not added to <B>Next Set</B></TD> 
 *   </TR>
 *   <TR align="center">
 *     <TD>4</TD><TD>B</TD><TD>&nbsp;</TD><TD>{E,D,C}</TD>
 *     <TD align="left">&nbsp;&nbsp;B already visited so ignore and move to next stage</TD>
 *   </TR>
 *   <TR align="center">
 *     <TD>5</TD><TD>E</TD><TD>{B}</TD><TD>{D,C}</TD> 
 *     <TD align="left">&nbsp;</TD>  
 *   </TR>
 *   <TR align="center">
 *     <TD>6</TD><TD>D</TD><TD>{B,C}</TD><TD>{C,C}</TD>
 *     <TD align="left">&nbsp;</TD>      
 *   </TR>
 *   <TR align="center">
 *     <TD>7</TD><TD>C</TD><TD>{B,D}</TD><TD>{C}</TD>
 *     <TD align="left">&nbsp;</TD>      
 *   </TR>
 *   <TR align="center">
 *     <TD>8</TD><TD align="center">C</TD><TD>&nbsp;</TD><TD>{ }</TD>
 *     <TD align="left">&nbsp;&nbsp;C already visited so ignore and move to next stage</TD>      
 *   </TR>
 *   <TR align="center">
 *     <TD>9</TD><TD>&nbsp;</TD><TD>&nbsp;</TD><TD>{ }</TD>
 *     <TD align="left">&nbsp;&nbsp;Next set empty, iteration complete.</TD>      
 *   </TR>
 * </TABLE><BR>
 * <BR>
 * At any stage of a travesal a branch may be <B>killed</B>.When a branch is 
 * killed at a stage of an iteration, no elements in the current <B>Branch Set</B> 
 * are added to the <B>Next Set</B>. This is illustrated by revisiting the 
 * Depth First Search Iteration, but this time killing the branch at node B.
 * The following table summarizes the stages of the traversal:<BR>
 * <BR>
 * <TABLE border="1" style="font-family:Arial;font-size:10pt;" width="80%">
 *   <TH>Stage</TH>
 *   <TH>Visited Node</TH>
 *   <TH>Branch Set </TH>
 *   <TH>Next Set</TH>
 *   <TH>Comment</TH>
 *   <TR align="center">
 *     <TD align="center" width="10%">0</TD>
 *     <TD width="10%">&nbsp;</TD>
 *     <TD width="10%">&nbsp;</TD>
 *     <TD width="10%">{A}</TD>
 *     <TD width="40%" align="left">&nbsp;&nbsp;Initial stage, iteration starts explicitly from A</TD> 
 *   </TR>
 *   <TR align="center">
 *     <TD>1</TD><TD>A</TD><TD>{B,F}</TD><TD>{F,B}</TD>
 *     <TD align="left">&nbsp;&nbsp;Related nodes added to <B>Next Set</B> in LIFO order.</TD> 
 *   </TR>
 *   <TR align="center">
 *     <TD>2</TD><TD>F</TD><TD>{A,B}</TD><TD>{B,B}</TD> 
 *     <TD align="left">&nbsp;&nbsp;A already visited so not added to <B>Next Set</B><BR>
 *         &nbsp;&nbsp;B not yet visited so added to queue.</TD>
 *   </TR>
 *   <TR align="center">
 *     <TD>3</TD><TD>B</TD><TD>{A,C,D,E,F}</TD><TD>{B}</TD>
 *     <TD align="left">&nbsp;&nbsp;<B>Branch Killed.</B> No nodes added to <B>Next Set</B></TD> 
 *   </TR>
 *   <TR align="center">
 *     <TD>4</TD><TD>B</TD><TD>&nbsp;</TD><TD>{ }</TD>
 *     <TD align="left">&nbsp;&nbsp;B already visited so ignore and move to next stage</TD>
 *   </TR>
 *   <TR align="center">
 *     <TD>9</TD><TD>&nbsp;</TD><TD>&nbsp;</TD><TD>{ }</TD>
 *     <TD align="left">&nbsp;&nbsp;Next set empty, iteration complete.</TD>      
 *   </TR>
 * </TABLE><BR>
 *  In this example, killing the branch at node B results in nodes C, D, and E
 *  never being visited.<BR>
 * 
 * @see GraphWalker
 * @see GraphTraversal
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public interface GraphIterator {
  
  /**
   * Sets the traversal for the iterator. 
   * 
   * @param traversal The traversal requesting components from the iterator.
   */
  public void setTraversal(GraphTraversal traversal);
  
  /**
   * Returns the traversal for the iterator.
   * 
   * @return The traversal requesting components from the iterator.
   */
  public GraphTraversal getTraversal();
  
  /**
   * Signals to the itereator that iteration is about to begin. This often 
   * results in the creation/initialization of any internal data structures 
   * used by the iterator.
   *  
   * @param graph The graph being whose components are being iterated over.
   * @todo DOCUMENT ME! 
   */
  public void init(Graph graph, GraphTraversal traversal);  
  
  /**
   * Returns the next graph component in the iteration. To signal to the caller
   * that the iteration is complete, null should be returned.
   *  
   * @return The next component in the iteration, or null if iteration is 
   * complete.
   * @todo DOCUMENT ME!
   * 
   */
  public Graphable next(GraphTraversal traversal);
  
  /**
   * Signals to the iterator that iteration should continue from the current
   * component in the traversal.
   * 
   * @param current The current component of the traversal.
   * @todo DOCUMENT ME!
   */
  public void cont(Graphable current, GraphTraversal traversal);
  
  /**
   * Signals the iterator to kill the branch at the current component.
   * 
   * @param current The current component of the traversal.
   * @todo DOCUMENT ME!
   */
  public void killBranch(Graphable current, GraphTraversal traversal);
}
