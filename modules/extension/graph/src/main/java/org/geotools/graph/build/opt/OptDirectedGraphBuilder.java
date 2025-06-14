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
package org.geotools.graph.build.opt;

import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicDirectedGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.opt.OptDirectedEdge;
import org.geotools.graph.structure.opt.OptDirectedNode;

/**
 * An implementation of GraphBuilder that builds optimized directed graph components.
 *
 * @see org.geotools.graph.structure.opt.OptDirectedNode
 * @see org.geotools.graph.structure.opt.OptDirectedEdge
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class OptDirectedGraphBuilder extends BasicDirectedGraphBuilder {

    /**
     * Creates an optimized directed node.
     *
     * @see GraphBuilder#buildNode()
     * @see OptDirectedNode
     */
    @Override
    public Node buildNode() {
        return new OptDirectedNode();
    }

    /**
     * Creates an optimized directed edge.
     *
     * @see GraphBuilder#buildEdge()
     * @see OptDirectedEdge
     */
    @Override
    public Edge buildEdge(Node nodeA, Node nodeB) {
        return new OptDirectedEdge((OptDirectedNode) nodeA, (OptDirectedNode) nodeB);
    }
}
