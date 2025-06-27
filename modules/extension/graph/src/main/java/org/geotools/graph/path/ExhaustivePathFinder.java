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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.util.IndexedStack;

public class ExhaustivePathFinder {
    public static final int CONTINUE_PATH = 0;
    public static final int END_PATH_AND_CONTINUE = 1;
    public static final int END_PATH_AND_STOP = 2;
    public static final int KILL_PATH = 3;

    private int m_maxitr;
    private int m_maxplen;

    public ExhaustivePathFinder() {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public ExhaustivePathFinder(int maxitr, int maxplen) {
        m_maxitr = maxitr;
        m_maxplen = maxplen;
    }

    public Path getPath(Node from, Node to) {
        final Node dst = to;
        GraphVisitor visitor = component -> {
            if (component.equals(dst)) return END_PATH_AND_STOP;
            return CONTINUE_PATH;
        };
        List paths = getPaths(from, visitor);
        if (paths.isEmpty()) return null;
        return (Path) paths.get(0);
    }

    public List getPaths(Node from, Node to) {
        final Node dst = to;
        GraphVisitor visitor = component -> {
            if (component.equals(dst)) return END_PATH_AND_CONTINUE;
            return CONTINUE_PATH;
        };
        return getPaths(from, visitor);
    }

    public List getPaths(Node from, GraphVisitor visitor) {
        List<Path> paths = new ArrayList<>();

        // create a map to maintain iterator state
        Map<Node, Iterator<? extends Graphable>> node2related = new HashMap<>();

        // create the stack and place start node on
        IndexedStack<Node> stack = new IndexedStack<>();
        stack.push(from);

        int iterations = 0;
        O:
        while (!stack.isEmpty() && iterations++ < m_maxitr) {
            // peek the stack
            Node top = stack.peek();

            switch (visitor.visit(top)) {
                case END_PATH_AND_CONTINUE:
                    paths.add(new Path(stack));
                    stack.pop();
                    continue;

                case END_PATH_AND_STOP:
                    paths.add(new Path(stack));
                    break O;

                case KILL_PATH:
                    stack.pop();
                    continue;

                case CONTINUE_PATH:
            }

            Iterator<? extends Graphable> related = null;
            if ((related = node2related.get(top)) == null) {
                related = top.getRelated();
                node2related.put(top, related);
            }

            while (stack.size() < m_maxplen && related.hasNext()) {
                Node adj = (Node) related.next();
                if (stack.contains(adj)) continue;

                // push adjacent onto stack, and reset iterator
                stack.push(adj);
                node2related.put(adj, adj.getRelated());

                continue O;
            }

            // all adjacent have been processed or are in stack
            stack.pop();
        }

        return paths;
    }
}
