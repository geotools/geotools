/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.graph;

import java.util.Iterator;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.data.simple.SimpleFeatureCollection;
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

public class GraphExamples {

    @SuppressFBWarnings
    void graphExample() throws Exception {
        SimpleFeatureSource featureSource = null;

        // graphExample start
        final LineGraphGenerator generator = new BasicLineGraphGenerator();
        SimpleFeatureCollection fc = featureSource.getFeatures();

        fc.accepts(
                new FeatureVisitor() {
                    @Override
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

            @Override
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
