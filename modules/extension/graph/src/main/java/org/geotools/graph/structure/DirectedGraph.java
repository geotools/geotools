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
package org.geotools.graph.structure;

/**
 * Represents a directed graph. 
 * 
 * In a directed graph, components have an explicit direction associated with 
 * them. <BR/> 
 * <BR/>
 * Directed nodes differentiate between those 
 * adjacent edges that originate at them (outgoing edges ) and those that 
 * terminate at them (incoming edges).<BR/>
 * <BR/> 
 * Directed edges differentiate between the node at the 
 * the source of the edge (source node) and the node at the destination of the 
 * edges (destination node).<BR/>
 * <BR/>
 * The following is a figure of an undirected graph, and one of the possible 
 * directed graphs that can be derived from it. Directionality is indicated 
 * by arrow heads on the edges of the graph.<BR/>
 * <BR/>
 * <IMG src="doc-files/undirected2directed.gif"><BR/>
 * <BR/>  
 * The following information summarizes the relationships in the directed
 * version of the graph.<BR/>
 * <BR/>
 * <TABLE border="1">
 *   <TH>Edge</TH>
 *   <TH>Source Node</TH>
 *   <TH>Destination Node</TH>
 *   <TR align="center"><TD>A</TD><TD>1</TD><TD>4</TD></TR>
 *   <TR align="center"><TD>B</TD><TD>2</TD><TD>4</TD></TR>
 *   <TR align="center"><TD>C</TD><TD>3</TD><TD>4</TD></TR>
 *   <TR align="center"><TD>D</TD><TD>4</TD><TD>5</TD></TR>
 *   <TR align="center"><TD>E</TD><TD>4</TD><TD>7</TD></TR>
 *   <TR align="center"><TD>F</TD><TD>4</TD><TD>6</TD></TR>
 *   <TR align="center"><TD>G</TD><TD>5</TD><TD>7</TD></TR>
 *   <TR align="center"><TD>H</TD><TD>6</TD><TD>7</TD></TR>
 * </TABLE><BR/>
 * <BR/>
 * <TABLE border="1">
 *   <TH>Node</TH>
 *   <TH>In Edges</TH>
 *   <TH>Out Edges</TH>
 *   <TR align="center"><TD>1</TD><TD> </TD><TD>A</TD></TR>
 *   <TR align="center"><TD>2</TD><TD> </TD><TD>B</TD></TR>
 *   <TR align="center"><TD>3</TD><TD> </TD><TD>C</TD></TR>
 *   <TR align="center"><TD>4</TD><TD>A,B,C</TD><TD>D,E,F</TD></TR>
 *   <TR align="center"><TD>5</TD><TD>D</TD><TD>G</TD></TR>
 *   <TR align="center"><TD>6</TD><TD>F</TD><TD>F</TD></TR>
 *   <TR align="center"><TD>7</TD><TD>E,G,H</TD><TD> </TD></TR>
 * </TABLE>
 * 
 * @see DirectedNode
 * @see DirectedEdge
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public interface DirectedGraph extends Graph {
 
}
