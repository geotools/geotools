package org.geotools.tpk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.util.URLs;
import org.junit.Test;
import org.opengis.geometry.Envelope;

public class TPKFileTest {

    @Test
    public void testReadCompactCacheV1() {
        Map<Long, TPKZoomLevel> zoomLevelMap = new HashMap<>();
        File theTpk = URLs.urlToFile(getClass().getResource("sample_v1.tpk"));
        TPKFile theTPKFile = new TPKFile(theTpk, zoomLevelMap);
        assertEquals(9, zoomLevelMap.size());
        assertEquals(0, theTPKFile.getMinZoomLevel());
        assertEquals(8, theTPKFile.getMaxZoomLevel());

        // get the bounds computed from highest zoom level coverage
        Envelope bounds = theTPKFile.getBounds();

        // minimum longitude
        assertEquals(-118.12500, bounds.getMinimum(0), 0.000001);
        // maximum longitude
        assertEquals(-116.71875, bounds.getMaximum(0), 0.000001);

        // min latitude
        assertEquals(31.95216, bounds.getMinimum(1), 0.00001);
        // max latitude
        assertEquals(33.13755, bounds.getMaximum(1), 0.00001);

        // sample uses png tiles
        String imageFormat = theTPKFile.getImageFormat();
        assertEquals("PNG", imageFormat);

        // zoom level 0
        assertEquals(0, theTPKFile.getMinColumn(0));
        assertEquals(0, theTPKFile.getMaxColumn(0));
        assertEquals(0, theTPKFile.getMinRow(0));
        assertEquals(0, theTPKFile.getMaxRow(0));

        // zoom level 5
        assertEquals(5, theTPKFile.getMinColumn(5));
        assertEquals(5, theTPKFile.getMaxColumn(5));
        assertEquals(19, theTPKFile.getMinRow(5));
        assertEquals(19, theTPKFile.getMaxRow(5));

        // zoomlevel 8
        assertEquals(44, theTPKFile.getMinColumn(8));
        assertEquals(44, theTPKFile.getMaxColumn(8));
        assertEquals(152, theTPKFile.getMinRow(8));
        assertEquals(152, theTPKFile.getMaxRow(8));

        List<TPKTile> tiles1 = theTPKFile.getTiles(8, 152, 144, 39, 52, imageFormat);
        assertNotNull(tiles1);
        assertEquals(1, tiles1.size());

        // release held resources
        theTPKFile.close();

        // try our "restart" constructor
        TPKFile newTPKFile = new TPKFile(theTpk, zoomLevelMap, bounds, imageFormat);
        assertNotNull(newTPKFile);

        List<TPKTile> tiles = newTPKFile.getTiles(8, 152, 144, 39, 52, imageFormat);
        assertNotNull(tiles);
        assertEquals(1, tiles.size());

        TPKTile tile = tiles.get(0);
        assertEquals(44, tile.col);
        assertEquals(152, tile.row);
        assertNotNull(tile.tileData);
    }

    @Test
    public void testReadCompactCacheV2() {
        Map<Long, TPKZoomLevel> zoomLevelMap = new HashMap<>();
        File theTpk = URLs.urlToFile(getClass().getResource("sample_v2.tpkx"));
        TPKFile theTPKFile = new TPKFile(theTpk, zoomLevelMap);
        assertEquals(3, zoomLevelMap.size());
        assertEquals(0, theTPKFile.getMinZoomLevel());
        assertEquals(2, theTPKFile.getMaxZoomLevel());

        // get the bounds computed from highest zoom level coverage
        Envelope bounds = theTPKFile.getBounds();

        // minimum longitude
        assertEquals(-180.0, bounds.getMinimum(0), 0.000001);
        // maximum longitude
        assertEquals(180.0, bounds.getMaximum(0), 0.000001);

        // min latitude
        assertEquals(-85.05112, bounds.getMinimum(1), 0.00001);
        // max latitude
        assertEquals(85.05112, bounds.getMaximum(1), 0.00001);

        // sample uses png tiles
        String imageFormat = theTPKFile.getImageFormat();
        assertEquals("JPEG", imageFormat);

        // zoom level 0
        assertEquals(0, theTPKFile.getMinColumn(0));
        assertEquals(0, theTPKFile.getMaxColumn(0));
        assertEquals(0, theTPKFile.getMinRow(0));
        assertEquals(0, theTPKFile.getMaxRow(0));

        // zoom level 1
        assertEquals(0, theTPKFile.getMinColumn(1));
        assertEquals(1, theTPKFile.getMaxColumn(1));
        assertEquals(0, theTPKFile.getMinRow(1));
        assertEquals(1, theTPKFile.getMaxRow(1));

        // zoomlevel 2
        assertEquals(0, theTPKFile.getMinColumn(2));
        assertEquals(3, theTPKFile.getMaxColumn(2));
        assertEquals(0, theTPKFile.getMinRow(2));
        assertEquals(3, theTPKFile.getMaxRow(2));

        List<TPKTile> tiles1 = theTPKFile.getTiles(2, 3, 0, 0, 3, imageFormat);
        assertNotNull(tiles1);
        assertEquals(16, tiles1.size());

        // release held resources
        theTPKFile.close();

        // try our "restart" constructor
        TPKFile newTPKFile = new TPKFile(theTpk, zoomLevelMap, bounds, imageFormat);
        assertNotNull(newTPKFile);

        List<TPKTile> tiles = newTPKFile.getTiles(2, 3, 0, 0, 3, imageFormat);
        assertNotNull(tiles);
        assertEquals(16, tiles.size());

        TPKTile tile = tiles.get(0);
        assertEquals(0, tile.col);
        assertEquals(3, tile.row);
        assertNotNull(tile.tileData);

        tile = tiles.get(15);
        assertEquals(3, tile.col);
        assertEquals(0, tile.row);
        assertNotNull(tile.tileData);
    }
}
