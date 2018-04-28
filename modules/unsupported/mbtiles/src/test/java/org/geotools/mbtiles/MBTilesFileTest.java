package org.geotools.mbtiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.junit.Test;

public class MBTilesFileTest {

    private static final Logger LOGGER = Logging.getLogger(MBTilesFileTest.class);

    @Test
    public void testMBTilesMetaData() throws IOException {

        MBTilesFile file = new MBTilesFile();
        file.init();

        MBTilesMetadata metadata = new MBTilesMetadata();
        metadata.setName("dummy name");
        metadata.setDescription("dummy description");
        metadata.setVersion("dummy version");
        metadata.setBoundsStr("0,0,100,100");
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

        file.close();
    }

    @Test
    public void testMBTilesInitTwice() throws IOException {
        MBTilesFile file = new MBTilesFile();
        file.init();
        file.init();
    }

    @Test
    public void testMBTilesWithoutJournal() throws IOException {
        // Create the temporary file
        File temp = File.createTempFile("temp2_", ".mbtiles");
        MBTilesFile file = new MBTilesFile(temp, true);
        file.init();
        // Define the Journal file
        final File journal = new File(temp.getAbsolutePath() + "-journal");
        // Initialize the journal file
        final AtomicLong counter = new AtomicLong(0);
        // Define a counter thread
        Thread th =
                new Thread(
                        new Runnable() {

                            @Override
                            public void run() {

                                while (true) {
                                    if (journal.exists()) {
                                        counter.incrementAndGet();
                                    }
                                }
                            }
                        });
        // launch the thread
        th.start();
        // add data to the mbtile
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                MBTilesTile tile1 = new MBTilesTile(1, i, j);
                tile1.setData("dummy data 1".getBytes());
                file.saveTile(tile1);
            }
        }
        // Stop the thread
        try {
            th.interrupt();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Close files
        try {
            file.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            temp.delete();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        // Ensure journal has never been found
        assertTrue(counter.get() == 0);
    }

    @Test
    public void testMBTilesMinMaxZoomLevelMetaData() throws IOException, SQLException {
        MBTilesFile file = new MBTilesFile();
        file.init();
        file.saveMinMaxZoomMetadata(0, 14);
        MBTilesMetadata metadata = file.loadMetaData();
        assertEquals(0, metadata.getMinZoom());
        assertEquals(14, metadata.getMaxZoom());
    }

    @Test
    public void testMBTilesTile() throws IOException, SQLException {

        MBTilesFile file = new MBTilesFile();
        file.init();

        MBTilesTile tile1 = new MBTilesTile(1, 0, 0);
        tile1.setData("dummy data 1".getBytes());
        MBTilesTile tile2 = new MBTilesTile(2, 0, 1);
        tile2.setData("dummy data 2".getBytes());

        file.saveTile(tile1);
        file.saveTile(tile2);

        MBTilesTile testTile = file.loadTile(1, 0, 0);
        assertTrue(Arrays.equals(tile1.getData(), testTile.getData()));

        testTile = file.loadTile(2, 0, 1);
        assertTrue(Arrays.equals(tile2.getData(), testTile.getData()));

        assertEquals(2, file.numberOfTiles());

        MBTilesFile.TileIterator it = file.tiles();
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertFalse(it.hasNext());

        assertEquals(1, file.minZoom());
        assertEquals(2, file.maxZoom());
        assertEquals(2, file.closestZoom(5));
        assertEquals(1, file.closestZoom(1));
        assertEquals(0, file.minColumn(1));
        assertEquals(0, file.maxColumn(2));
        assertEquals(0, file.minRow(1));
        assertEquals(1, file.maxRow(2));

        file.close();
    }

    @Test
    public void testMBTilesGrid() throws IOException, SQLException {

        MBTilesFile file = new MBTilesFile();
        file.init();

        MBTilesGrid grid1 = new MBTilesGrid(1, 0, 0);
        grid1.setGrid("dummy data 1".getBytes());
        grid1.setGridDataKey("key1", "dummy value1");
        grid1.setGridDataKey("key2", "dummy value2");
        MBTilesGrid grid2 = new MBTilesGrid(2, 0, 1);
        grid2.setGridDataKey("key3", "dummy value3");
        grid2.setGridDataKey("key4", "dummy value4");
        grid2.setGrid("dummy data 2".getBytes());

        file.saveGrid(grid1);
        file.saveGrid(grid2);

        MBTilesGrid testGrid = file.loadGrid(1, 0, 0);
        assertTrue(Arrays.equals(grid1.getGrid(), testGrid.getGrid()));
        assertEquals(grid1.getGridDataKey("key1"), grid1.getGridDataKey("key1"));
        assertEquals(grid1.getGridDataKey("key2"), grid1.getGridDataKey("key2"));

        testGrid = file.loadGrid(2, 0, 1);
        assertTrue(Arrays.equals(grid2.getGrid(), testGrid.getGrid()));
        assertEquals(grid2.getGridDataKey("key3"), grid2.getGridDataKey("key3"));
        assertEquals(grid2.getGridDataKey("key4"), grid2.getGridDataKey("key4"));
    }
}
