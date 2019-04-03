/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.graph;

import java.util.Iterator;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.graph.build.line.BasicLineGraphGenerator;
import org.geotools.graph.build.line.LineGraphGenerator;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.SimpleGraphWalker;
import org.geotools.graph.traverse.standard.BreadthFirstIterator;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;

public class GraphExamples {

    @SuppressFBWarnings
    void graphExample() throws Exception {
        SimpleFeatureSource featureSource = null;

        // graphExample start
        final LineGraphGenerator generator = new BasicLineGraphGenerator();
        SimpleFeatureCollection fc = featureSource.getFeatures();

        fc.accepts(
                new FeatureVisitor() {
                    public void visit(Feature feature) {
                        generator.add(feature);
                    }
                },
                null);
        Graph graph = generator.getGraph();
        // graphExample end

        // visitor example start
        class OrphanVisitor implements GraphVisitor {
            private int count = 0;

            public int getCount() {
                return count;
            }

            public int visit(Graphable component) {
                Iterator related = component.getRelated();
                if (related.hasNext() == false) {
                    // no related components makes this an orphan
                    count++;
                }
                return GraphTraversal.CONTINUE;
            }
        }
        OrphanVisitor graphVisitor = new OrphanVisitor();

        SimpleGraphWalker sgv = new SimpleGraphWalker(graphVisitor);
        GraphIterator iterator = new BreadthFirstIterator();
        BasicGraphTraversal bgt = new BasicGraphTraversal(graph, sgv, iterator);

        bgt.traverse();

        System.out.println("Found orphans: " + graphVisitor.getCount());
        // visitor example end
    }
}
