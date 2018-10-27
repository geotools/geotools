package org.geotools.jts;

import java.io.File;
import java.util.List;
import java.util.Random;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;

public class SnapToLine {

    public static void main(String[] args) throws Exception {

        /*
         * Open a shapefile. You should choose one with line features
         * (LineString or MultiLineString geometry)
         *
         */
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource source = store.getFeatureSource();

        // Check that we have line features
        Class<?> geomBinding = source.getSchema().getGeometryDescriptor().getType().getBinding();
        boolean isLine =
                geomBinding != null
                        && (LineString.class.isAssignableFrom(geomBinding)
                                || MultiLineString.class.isAssignableFrom(geomBinding));

        if (!isLine) {
            System.out.println("This example needs a shapefile with line features");
            return;
        }

        // load shapefile end (docs marker)

        final SpatialIndex index = new STRtree();
        FeatureCollection features = source.getFeatures();
        System.out.println("Slurping in features ...");
        features.accepts(
                new FeatureVisitor() {

                    @Override
                    public void visit(Feature feature) {
                        SimpleFeature simpleFeature = (SimpleFeature) feature;
                        Geometry geom = (MultiLineString) simpleFeature.getDefaultGeometry();
                        // Just in case: check for  null or empty geometry
                        if (geom != null) {
                            Envelope env = geom.getEnvelopeInternal();
                            if (!env.isNull()) {
                                index.insert(env, new LocationIndexedLine(geom));
                            }
                        }
                    }
                },
                new NullProgressListener());

        // cache features end (docs marker)

        /*
         * For test data, we generate a large number of points placed randomly
         * within the bounding rectangle of the features.
         */
        final int NUM_POINTS = 10000;
        ReferencedEnvelope bounds = features.getBounds();
        Coordinate[] points = new Coordinate[NUM_POINTS];
        Random rand = new Random(file.hashCode());
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i] =
                    new Coordinate(
                            bounds.getMinX() + rand.nextDouble() * bounds.getWidth(),
                            bounds.getMinY() + rand.nextDouble() * bounds.getHeight());
        }

        // generate points end (docs marker)

        /*
         * We defined the maximum distance that a line can be from a point
         * to be a candidate for snapping (1% of the width of the feature
         * bounds for this example).
         */
        final double MAX_SEARCH_DISTANCE = bounds.getSpan(0) / 100.0;

        // Maximum time to spend running the snapping process (milliseconds)
        final long DURATION = 5000;

        int pointsProcessed = 0;
        int pointsSnapped = 0;
        long elapsedTime = 0;
        long startTime = System.currentTimeMillis();
        while (pointsProcessed < NUM_POINTS
                && (elapsedTime = System.currentTimeMillis() - startTime) < DURATION) {

            // Get point and create search envelope
            Coordinate pt = points[pointsProcessed++];
            Envelope search = new Envelope(pt);
            search.expandBy(MAX_SEARCH_DISTANCE);

            /*
             * Query the spatial index for objects within the search envelope.
             * Note that this just compares the point envelope to the line envelopes
             * so it is possible that the point is actually more distant than
             * MAX_SEARCH_DISTANCE from a line.
             */
            List<LocationIndexedLine> lines = index.query(search);

            // Initialize the minimum distance found to our maximum acceptable
            // distance plus a little bit
            double minDist = MAX_SEARCH_DISTANCE + 1.0e-6;
            Coordinate minDistPoint = null;

            for (LocationIndexedLine line : lines) {
                LinearLocation here = line.project(pt);
                Coordinate point = line.extractPoint(here);
                double dist = point.distance(pt);
                if (dist < minDist) {
                    minDist = dist;
                    minDistPoint = point;
                }
            }

            if (minDistPoint == null) {
                // No line close enough to snap the point to
                System.out.println(pt + "- X");

            } else {
                System.out.printf("%s - snapped by moving %.4f\n", pt.toString(), minDist);
                pointsSnapped++;
            }
        }

        System.out.printf(
                "Processed %d points (%.2f points per second). \n" + "Snapped %d points.\n\n",
                pointsProcessed, 1000.0 * pointsProcessed / elapsedTime, pointsSnapped);
    }
}
