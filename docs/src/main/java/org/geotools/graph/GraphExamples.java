package org.geotools.graph;

import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.line.BasicLineGraphBuilder;
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
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;

public class GraphExamples {
void graphExample() throws Exception {
    SimpleFeatureSource featureSource = null;
    
    // graphExample start
    final LineGraphGenerator generator = new BasicLineGraphGenerator();
    SimpleFeatureCollection fc = featureSource.getFeatures();
    
    fc.accepts(new FeatureVisitor() {
        public void visit(Feature feature) {
            generator.add(feature);
        }
    }, null);
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
            if( related.hasNext() == false ){
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
