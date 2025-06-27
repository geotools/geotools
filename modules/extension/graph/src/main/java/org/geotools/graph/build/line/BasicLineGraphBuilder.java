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

import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.line.BasicXYNode;

/**
 * An implementation of GraphBuilder extended from BasicGraphBuilder used to build graphs representing line networks.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class BasicLineGraphBuilder extends BasicGraphBuilder {

    /**
     * Returns a node of type BasicXYNode.
     *
     * @see BasicXYNode
     * @see org.geotools.graph.build.GraphBuilder#buildNode()
     */
    @Override
    public Node buildNode() {
        return new BasicXYNode();
    }
}
