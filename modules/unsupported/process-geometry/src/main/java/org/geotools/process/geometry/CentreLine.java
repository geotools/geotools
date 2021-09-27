package org.geotools.process.geometry;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.graph.build.line.LineStringGraphGenerator;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.standard.DijkstraIterator.EdgeWeighter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class CentreLine {
    public static final GeometryFactory GF = new GeometryFactory();

    public static SimpleFeatureCollection extractCentreLine(
            SimpleFeatureCollection features, double perc) {
        List<SimpleFeature> ret = new ArrayList<>();
        SimpleFeatureType schema = features.getSchema();
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(schema.getName());
        String geomName = schema.getGeometryDescriptor().getLocalName();
        ftb.add(geomName, MultiLineString.class, schema.getCoordinateReferenceSystem());
        ftb.setCRS(schema.getCoordinateReferenceSystem());
        ftb.setDefaultGeometry(geomName);
        for (AttributeDescriptor prop : schema.getAttributeDescriptors()) {
            if (prop.getLocalName().equalsIgnoreCase(geomName)) {
                continue;
            }
            ftb.add(prop);
        }
        SimpleFeatureType outSchema = ftb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(outSchema);
        try (SimpleFeatureIterator itr = features.features()) {
            while (itr.hasNext()) {
                SimpleFeature feature = itr.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();

                Geometry outGeom = getCentreLine(geom, perc);

                builder.addAll(feature.getAttributes());
                builder.set(geomName, outGeom);
                SimpleFeature duplicate = builder.buildFeature(feature.getID());
                ret.add(duplicate);
            }
        }

        return DataUtilities.collection(ret);
    }

    public static Geometry getCentreLine(Geometry geom) {
        return getCentreLine(geom, 50);
    }
    /**
     * @param geom
     * @param perc_density TODO
     * @return
     */
    public static Geometry getCentreLine(Geometry geom, double perc_density) {
        Geometry skel = Skeletonize.getSkeleton(geom, perc_density);
        // System.out.println(skel);
        Geometry outGeom = reduceToCentreLine(skel);
        // System.out.println(outGeom);
        outGeom =
                TopologyPreservingSimplifier.simplify(
                        outGeom, outGeom.getLength() * (perc_density / 100.0));
        outGeom = JTS.smooth(outGeom, 0.1, GF);
        // System.out.println(outGeom);
        return outGeom;
    }

    /**
     * Find the "longest shortest path" between any pair of perimeter nodes. Based on an idea at
     * https://bl.ocks.org/veltman/403f95aee728d4a043b142c52c113f82 by Noah Veltman
     * (https://bl.ocks.org/veltman)
     *
     * @param geom the skeleton of the polygon
     * @return the longest path through the geometry
     */
    private static Geometry reduceToCentreLine(Geometry geom) {
        LineStringGraphGenerator gen = new LineStringGraphGenerator();
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            Geometry g = geom.getGeometryN(i);
            if (g.isEmpty()) {
                continue;
            }
            gen.add(g);
        }
        Graph graph = gen.getGraph();
        EdgeWeighter weighter =
                e -> {
                    Geometry g = (Geometry) e.getObject();
                    return g.getLength();
                };
        double bestLen = Double.NEGATIVE_INFINITY;
        Path bestPath = null;
        for (Node source : graph.getNodesOfDegree(1)) {
            // calculate the cost(distance) of each graph node to the node closest to
            // the origin
            // System.out.println("starting at " + source.getObject());
            DijkstraShortestPathFinder dspf =
                    new DijkstraShortestPathFinder(graph, source, weighter);
            dspf.calculate();
            for (Node dest : graph.getNodesOfDegree(1)) {
                // System.out.println("\troute to " + dest.getObject());
                if (dest.equals(source)) {
                    continue;
                }
                // get length
                double len = 0.0;
                Path path = dspf.getPath(dest);
                if (path == null) { // no path to dest
                    continue;
                }
                for (Edge e : path.getEdges()) {
                    Geometry g = (Geometry) e.getObject();
                    len += g.getLength();
                }
                if (len > bestLen) {
                    bestPath = path;
                    bestLen = len;
                }
            }
        }
        ArrayList<LineString> edges = new ArrayList<>();
        for (Edge e : bestPath.getEdges()) {
            Geometry g = (Geometry) e.getObject();
            edges.add((LineString) g);
        }
        return GF.createMultiLineString(GeometryFactory.toLineStringArray(edges));
    }
}
