package org.geotools.mbtiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Test;

public class MBTilesFileTest {
    
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
        file.saveMetaData(metadata);   
        
        MBTilesMetadata metadata2 = file.loadMetaData();
        assertEquals(metadata.getName(), metadata2.getName());
        assertEquals(metadata.getDescription(), metadata2.getDescription());
        assertEquals(metadata.getVersion(), metadata2.getVersion());
        assertEquals(metadata.getBounds(), metadata2.getBounds());
        assertEquals(metadata.getFormat(), metadata2.getFormat());
        assertEquals(metadata.getType(), metadata2.getType());
        
        file.close();        
    }
    
    @Test
    public void testMBTilesTile() throws IOException, SQLException {
        
        MBTilesFile file = new MBTilesFile();
        file.init();
                
        MBTilesTile tile1 = new MBTilesTile(1,0,0);
        tile1.setData("dummy data 1".getBytes());
        MBTilesTile tile2 = new MBTilesTile(2,0,1);
        tile2.setData("dummy data 2".getBytes());
       
        file.saveTile(tile1);
        file.saveTile(tile2);
        
        MBTilesTile testTile = file.loadTile(1,0,0);
        assertTrue(Arrays.equals(tile1.getData(), testTile.getData()));
        
        testTile = file.loadTile(2,0,1);
        assertTrue(Arrays.equals(tile2.getData(), testTile.getData()));
        
        assertEquals(2, file.numberOfTiles());
        
        MBTilesFile.TileIterator it = file.tiles();
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertFalse(it.hasNext());
        
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
                
        MBTilesGrid grid1 = new MBTilesGrid(1,0,0);
        grid1.setGrid("dummy data 1".getBytes());
        grid1.setGridDataKey("key1", "dummy value1");
        grid1.setGridDataKey("key2", "dummy value2");
        MBTilesGrid grid2 = new MBTilesGrid(2,0,1);
        grid2.setGridDataKey("key3", "dummy value3");
        grid2.setGridDataKey("key4", "dummy value4");
        grid2.setGrid("dummy data 2".getBytes());
       
        file.saveGrid(grid1);
        file.saveGrid(grid2);
        
        MBTilesGrid testGrid = file.loadGrid(1,0,0);
        assertTrue(Arrays.equals(grid1.getGrid(), testGrid.getGrid()));
        assertEquals(grid1.getGridDataKey("key1"), grid1.getGridDataKey("key1"));
        assertEquals(grid1.getGridDataKey("key2"), grid1.getGridDataKey("key2"));
        
        testGrid = file.loadGrid(2,0,1);
        assertTrue(Arrays.equals(grid2.getGrid(), testGrid.getGrid()));
        assertEquals(grid2.getGridDataKey("key3"), grid2.getGridDataKey("key3"));
        assertEquals(grid2.getGridDataKey("key4"), grid2.getGridDataKey("key4"));
        
    }

}
