package org.geotools.data.shapefile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.index.LockTimeoutException;
import org.geotools.data.shapefile.index.TreeException;
import org.geotools.data.shapefile.index.quadtree.StoreException;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.SchemaException;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.util.Stopwatch;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

@RunWith(Parameterized.class)
@SuppressWarnings("PMD.SystemPrintln")
public class ShapeFileIndexerStressTest {

    private static final Logger LOGGER = Logging.getLogger(ShapeFileIndexerStressTest.class);

    private static final GeometryFactory GF = new GeometryFactory();

    @Parameters
    public static Integer[] featureCount() {
        return new Integer[] {1_000, 10_000, 100_000, 1_000_000, 10_000_000};
    }

    @Parameter(value = 0)
    public int featureCount;

    private ShapefileDataStore dataStore;

    public @Rule TemporaryFolder tmpDir = new TemporaryFolder();

    private File file;

    private static List<Stats> STATS = new ArrayList<>();

    static class Stats {
        int featureCount;
        long ellapsed;

        public static void printHeader() {
            System.out.println("|# Features|Index build time|");
            System.out.println("|----------|----------------|");
        }

        @Override
        public String toString() {
            return String.format("|%,d|%s|", featureCount, Stopwatch.getTimeString(ellapsed));
        }
    }

    @Before
    public void setUp() throws SchemaException, IOException {
        final String typeName = getClass().getSimpleName() + "_" + featureCount + ".shp";
        file = new File(tmpDir.getRoot(), typeName);
        URL url = file.toURI().toURL();

        dataStore = new ShapefileDataStore(url);
        dataStore.setIndexCreationEnabled(false);
        SimpleFeatureType featureType =
                DataUtilities.createType(typeName, "id:0,the_geom:LineString");
        dataStore.createSchema(featureType);
        LOGGER.info(String.format("Setting up %,d features shapefile %s...", featureCount, file));
        addFeatures(dataStore, featureType, featureCount);
        LOGGER.info(
                String.format(
                        "%,d features shapefile %s, %,d bytes...",
                        featureCount, file, file.length()));
    }

    @After
    public void tearDown() {
        dataStore.dispose();
        Stats.printHeader();
        for (Stats s : STATS) {
            System.out.println(s);
        }
    }

    private void addFeatures(ShapefileDataStore store, SimpleFeatureType type, final int count)
            throws IOException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                store.getFeatureWriterAppend(Transaction.AUTO_COMMIT);
        try {
            int recno = 0;
            int zlevel = 1;
            while (recno <= count) {
                double width = 360D / zlevel;
                double height = 180D / zlevel;
                for (double minx = -180d; minx < 180d; minx += width) {
                    for (double miny = -90d; miny < 90d; miny += height) {
                        double maxx = minx + width;
                        double maxy = miny + height;
                        addFeature(recno++, count, writer, minx, miny, minx, maxy);
                        addFeature(recno++, count, writer, minx, maxy, maxx, maxy);
                        addFeature(recno++, count, writer, maxx, maxy, maxx, miny);
                        addFeature(recno++, count, writer, maxx, miny, minx, miny);
                    }
                }
                ++zlevel;
            }
        } finally {
            writer.close();
        }
    }

    private void addFeature(
            int recno,
            int maxCount,
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer,
            double minx,
            double miny,
            double maxx,
            double maxy)
            throws IOException {
        if (recno < maxCount) {
            SimpleFeature f = writer.next();
            f.setAttribute("id", recno);
            LineString geom = buildLineString(minx, miny, maxx, maxy);
            f.setAttribute("the_geom", geom);
            writer.write();
        }
    }

    private LineString buildLineString(double x1, double y1, double x2, double y2) {
        CoordinateSequence coords = GF.getCoordinateSequenceFactory().create(2, 2);
        coords.setOrdinate(0, 0, x1);
        coords.setOrdinate(0, 1, y1);
        coords.setOrdinate(1, 0, x2);
        coords.setOrdinate(1, 1, y2);
        return GF.createLineString(coords);
    }

    @Test
    public void testIndex()
            throws TreeException, StoreException, IOException, LockTimeoutException {
        ShapeFileIndexer indexer = new ShapeFileIndexer();
        indexer.setShapeFileName(new ShpFiles(file));
        Stopwatch sw = new Stopwatch();
        indexer.index(true, new NullProgressListener());
        sw.stop();
        long ellapsedMillis = sw.getTime();
        Stats s = new Stats();
        s.featureCount = featureCount;
        s.ellapsed = ellapsedMillis;
        STATS.add(s);
    }
}
