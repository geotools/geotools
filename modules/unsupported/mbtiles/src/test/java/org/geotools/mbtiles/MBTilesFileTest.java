package org.geotools.mbtiles;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.junit.Test;

public class MBTilesFileTest {

    private final double DELTA = 1E-6;

    private static final Logger LOGGER = Logging.getLogger(MBTilesFileTest.class);

    @Test
    public void testMBTilesMetaData() throws IOException {

        try (MBTilesFile file = new MBTilesFile()) {
            file.init();

            MBTilesMetadata metadata = new MBTilesMetadata();
            metadata.setName("dummy name");
            metadata.setDescription("dummy description");
            metadata.setVersion("dummy version");
            metadata.setBoundsStr("0,0,100,100");
            metadata.setCenterStr("10,10,2");
            metadata.setFormatStr("png");
            metadata.setTypeStr("overlay");
            metadata.setMinZoomStr("0");
            metadata.setMaxZoomStr("5");
            file.saveMetaData(metadata);

            MBTilesMetadata metadata2 = file.loadMetaData();
            assertEquals(metadata.getName(), metadata2.getName());
            assertEquals(metadata.getDescription(), metadata2.getDescription());
            assertEquals(metadata.getVersion(), metadata2.getVersion());
            assertEquals(metadata.getBounds(), metadata2.getBounds());
            assertEquals(metadata.getFormat(), metadata2.getFormat());
            assertEquals(metadata.getType(), metadata2.getType());
            assertEquals(metadata.getMinZoom(), metadata2.getMinZoom());
            assertEquals(metadata.getMaxZoom(), metadata2.getMaxZoom());
            assertArrayEquals(metadata.getCenter(), metadata2.getCenter(), DELTA);
        }
    }

    @Test
    public void testMBTilesMetadataJPG() {
        MBTilesMetadata m = new MBTilesMetadata();
        m.setFormatStr("jpg"); // threw exception before JPG was added to enum
    }

    @Test
    public void testMBTilesInitTwice() throws IOException {
        try (MBTilesFile file = new MBTilesFile()) {
            file.init();
            file.init();
        }
    }

    @Test
    public void testMBTilesWithoutJournal() throws IOException {
        // Create the temporary file
        File temp = File.createTempFile("temp2_", ".mbtiles");
        final AtomicLong counter = new AtomicLong(0);
        try (MBTilesFile file = new MBTilesFile(temp, true)) {
            file.init();
            // Define the Journal file
            final File journal = new File(temp.getAbsolutePath() + "-journal");
            // Initialize the journal file
            // Define a counter thread
            Thread th = new Thread(() -> {
                while (true) {
                    if (journal.exists()) {
                        counter.incrementAndGet();
                    }
                }
            });
            // launch the thread
            th.start();
            // add data to the mbtile
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    MBTilesTile tile1 = new MBTilesTile(1, i, j);
                    tile1.setData("dummy data 1".getBytes(StandardCharsets.UTF_8));
                    file.saveTile(tile1);
                }
            }
            // Stop the thread
            try {
                th.interrupt();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        try {
            temp.delete();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        // Ensure journal has never been found
        assertEquals(0, counter.get());
    }

    @Test
    public void testMBTilesMinMaxZoomLevelMetaData() throws IOException, SQLException {
        try (MBTilesFile file = new MBTilesFile()) {
            file.init();
            file.saveMinMaxZoomMetadata(0, 14);
            MBTilesMetadata metadata = file.loadMetaData();
            assertEquals(0, metadata.getMinZoom());
            assertEquals(14, metadata.getMaxZoom());
        }
    }

    @Test
    public void testMBTilesTile() throws IOException, SQLException {

        try (MBTilesFile file = new MBTilesFile()) {
            file.init();

            MBTilesTile tile1 = new MBTilesTile(1, 0, 0);
            tile1.setData("dummy data 1".getBytes(StandardCharsets.UTF_8));
            MBTilesTile tile2 = new MBTilesTile(2, 0, 1);
            tile2.setData("dummy data 2".getBytes(StandardCharsets.UTF_8));

            file.saveTile(tile1);
            file.saveTile(tile2);

            MBTilesTile testTile = file.loadTile(1, 0, 0);
            assertArrayEquals(tile1.getData(), testTile.getData());

            testTile = file.loadTile(2, 0, 1);
            assertArrayEquals(tile2.getData(), testTile.getData());

            assertEquals(2, file.numberOfTiles());

            try (MBTilesFile.TileIterator it = file.tiles()) {
                assertTrue(it.hasNext());
                assertNotNull(it.next());
                assertTrue(it.hasNext());
                assertNotNull(it.next());
                assertFalse(it.hasNext());
            }

            assertEquals(1, file.minZoom());
            assertEquals(2, file.maxZoom());
            assertEquals(2, file.closestZoom(5));
            assertEquals(1, file.closestZoom(1));
            assertEquals(0, file.minColumn(1));
            assertEquals(0, file.maxColumn(2));
            assertEquals(0, file.minRow(1));
            assertEquals(1, file.maxRow(2));
        }
    }

    @Test
    public void testMBTilesGrid() throws IOException, SQLException {

        try (MBTilesFile file = new MBTilesFile()) {
            file.init();

            MBTilesGrid grid1 = new MBTilesGrid(1, 0, 0);
            grid1.setGrid("dummy data 1".getBytes(StandardCharsets.UTF_8));
            grid1.setGridDataKey("key1", "dummy value1");
            grid1.setGridDataKey("key2", "dummy value2");
            MBTilesGrid grid2 = new MBTilesGrid(2, 0, 1);
            grid2.setGridDataKey("key3", "dummy value3");
            grid2.setGridDataKey("key4", "dummy value4");
            grid2.setGrid("dummy data 2".getBytes(StandardCharsets.UTF_8));

            file.saveGrid(grid1);
            file.saveGrid(grid2);

            MBTilesGrid testGrid = file.loadGrid(1, 0, 0);
            assertArrayEquals(grid1.getGrid(), testGrid.getGrid());
            assertEquals(grid1.getGridDataKey("key1"), grid1.getGridDataKey("key1"));
            assertEquals(grid1.getGridDataKey("key2"), grid1.getGridDataKey("key2"));

            testGrid = file.loadGrid(2, 0, 1);
            assertArrayEquals(grid2.getGrid(), testGrid.getGrid());
            assertEquals(grid2.getGridDataKey("key3"), grid2.getGridDataKey("key3"));
            assertEquals(grid2.getGridDataKey("key4"), grid2.getGridDataKey("key4"));
        }
    }

    @Test
    public void testMBTilesBounds() throws IOException, SQLException {
        // this one has different bounds per zoom level, even in geographic terms
        try (MBTilesFile mbTilesFile =
                new MBTilesFile(new File("./src/test/resources/org/geotools/mbtiles/madagascar.mbtiles"))) {
            RectangleLong bounds = mbTilesFile.getTileBounds(7, true);
            assertEquals(79, bounds.getMinX());
            assertEquals(82, bounds.getMaxX());
            assertEquals(54, bounds.getMinY());
            assertEquals(59, bounds.getMaxY());
            RectangleLong boundsFull = mbTilesFile.getTileBounds(7, false);
            assertEquals(0, boundsFull.getMinX());
            assertEquals(127, boundsFull.getMaxX());
            assertEquals(0, boundsFull.getMinY());
            assertEquals(127, boundsFull.getMaxY());
        }
    }

    @Test
    public void testMBTilesMetadataCenter() throws IOException, SQLException {
        try (MBTilesFile mbTilesFile =
                new MBTilesFile(new File("./src/test/resources/org/geotools/mbtiles/madagascar.mbtiles"))) {
            MBTilesMetadata metadata = mbTilesFile.loadMetaData();
            double[] center = metadata.getCenter();
            assertEquals(-12.2168, center[0], DELTA);
            assertEquals(28.6135, center[1], DELTA);
            assertEquals(4, center[2], DELTA);
        }
    }

    @Test
    public void testWorldEnvelopeToTiles() throws IOException, SQLException {
        try (MBTilesFile mbTilesFile =
                new MBTilesFile(new File("./src/test/resources/org/geotools/mbtiles/madagascar.mbtiles"))) {
            RectangleLong bounds = mbTilesFile.toTilesRectangle(MBTilesFile.WORLD_ENVELOPE, 7);
            assertEquals(0, bounds.getMinX());
            assertEquals(128, bounds.getMaxX());
            assertEquals(0, bounds.getMinY());
            assertEquals(128, bounds.getMaxY());
        }
    }

    @Test
    public void testLocalEnvelopeToTiles() throws IOException, SQLException {
        try (MBTilesFile mbTilesFile =
                new MBTilesFile(new File("./src/test/resources/org/geotools/mbtiles/madagascar.mbtiles"))) {
            ReferencedEnvelope envelope =
                    new ReferencedEnvelope(0, 5000000, 0, 5000000, MBTilesFile.SPHERICAL_MERCATOR);
            RectangleLong bounds = mbTilesFile.toTilesRectangle(envelope, 7);
            assertEquals(64, bounds.getMinX());
            assertEquals(79, bounds.getMaxX());
            assertEquals(64, bounds.getMinY());
            assertEquals(79, bounds.getMaxY());
        }
    }
}
