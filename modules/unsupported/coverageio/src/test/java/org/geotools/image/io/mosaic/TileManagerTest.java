/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.image.io.mosaic;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Collection;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link TileManager}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class TileManagerTest extends TestBase {
    /**
     * The region of interest to be queried.
     */
    private Rectangle regionOfInterest;

    /**
     * The subsampling to be queried.
     */
    private Dimension subsampling;

    /**
     * The tiles under examination.
     */
    private Collection<Tile> tiles;

    /**
     * If {@code true}, print the next files found.
     */
    private boolean print;

    /**
     * Initializes the fields to be used for tile searchs.
     */
    @Before
    public void initRegion() {
        regionOfInterest = new Rectangle(SOURCE_SIZE*4, SOURCE_SIZE*2);
        subsampling = new Dimension(90,90);
    }

    /**
     * Queries the tiles and performs some sanity check on them.
     *
     * @param expected Expected subsampling, or {@code null} if no subsampling change is allowed.
     */
    private void searchTiles(Dimension expected) throws IOException {
        tiles = manager.getTiles(regionOfInterest, subsampling, expected != null);
        final Tile[] array = tiles.toArray(new Tile[tiles.size()]);
        for (int i=0; i<array.length; i++) {
            final Tile tile = array[i];
            if (print) {
                System.out.println(tile);
            }
            final Rectangle bounds = tile.getAbsoluteRegion();
            assertFalse("Tiles should not be empty.", bounds.isEmpty());
            assertTrue("Must intersects the ROI.", regionOfInterest.intersects(bounds));
            for (int j=i+1; j<array.length; j++) {
                assertFalse("Expected no overlaps.", bounds.intersects(array[j].getAbsoluteRegion()));
            }
        }
        if (expected == null) {
            expected = subsampling;
        }
        for (int i=0; i<array.length; i++) {
            assertEquals("Expected uniform subsampling.", expected, array[i].getSubsampling());
        }
        print = false;
    }

    /**
     * Searchs the tiles again with a different subsampling requested, and ensures that we get the
     * same collection than previous invocation of {@link #searchTiles}. The purpose of this method
     * is to ensure that the automatic adjustment of subsampling works.
     *
     * @param xRequested Subsampling to request along the x axis.
     * @param yRequested Subsampling to request along the y axis.
     * @param  expected  The uniform subsampling expected in the selected tiles along both axis.
     * @param xExpected  The expected tile manager suggestion for subsampling along x axis.
     * @param yExpected  The expected tile manager suggestion for subsampling along y axis.
     */
    private void searchSameTiles(final int xRequested, final int yRequested, final int expected,
                                 final int xExpected,  final int yExpected)
            throws IOException
    {
        final Collection<Tile> selected = tiles;
        subsampling.setSize(xRequested, yRequested);
        searchTiles(new Dimension(expected, expected));
        assertNotSame(selected, tiles);
        assertEquals (selected, tiles);
        assertEquals (new Dimension(xExpected, yExpected), subsampling);
    }

    /**
     * Tests the search of tiles on a tile layout using constant tile size.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testConstantSizeLayout() throws IOException {
        int total = 0;

        subsampling.setSize(90, 90);
        searchTiles(null);
        assertEquals(1, tiles.size());
        total += tiles.size();
        searchSameTiles(100,120,  90,  90, 90);
        searchSameTiles( 90,100,  90,  90, 90);
        searchSameTiles(400,400,  90, 360,360);

        subsampling.setSize(45, 45);
        searchTiles(null);
        assertEquals(2, tiles.size());
        total += tiles.size();
        searchSameTiles(50,60,  45,  45,45);
        searchSameTiles(45,70,  45,  45,45);
        searchSameTiles(45,90,  45,  45,90);

        subsampling.setSize(15, 15);
        searchTiles(null);
        assertEquals(18, tiles.size());
        total += tiles.size();
        searchSameTiles(15,20,  15,  15,15);
        searchSameTiles(30,70,  15,  30,60);
        searchSameTiles(18,27,  15,  15,15);

        subsampling.setSize(9, 9);
        searchTiles(null);
        assertEquals(50, tiles.size());
        total += tiles.size();
        searchSameTiles(10,20,  9,   9,18);
        searchSameTiles(31,11,  9,  27, 9);
        searchSameTiles(97,13,  9,  90, 9);

        subsampling.setSize(5,5);
        searchTiles(null);
        assertEquals(162, tiles.size());
        total += tiles.size();
        searchSameTiles(7,12,  5,  5,10);

        subsampling.setSize(3,3);
        searchTiles(null);
        assertEquals(450, tiles.size());
        total += tiles.size();
        searchSameTiles(4,3,  3,  3,3);

        subsampling.setSize(1,1);
        searchTiles(null);
        assertEquals(4050, tiles.size());
        total += tiles.size();
        searchSameTiles(2,1,  1,  2,1);

        assertEquals(4733, total);
    }

    /**
     * Tests a few specific regions. They are region that were known to be have issues at
     * some point in the development process of the mosaic package. They should now be fixed.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testSpecific() throws IOException {
        regionOfInterest.x      = 31375;
        regionOfInterest.y      =  8488;
        regionOfInterest.width  = 16708;
        regionOfInterest.height =  3812;
        subsampling.width       =    23;
        subsampling.height      =    23;
        searchTiles(new Dimension(15,15));
        assertEquals(2, tiles.size());

        regionOfInterest.x      = 80898;
        regionOfInterest.y      = 21411;
        regionOfInterest.width  =  4792;
        regionOfInterest.height =  3190;
        subsampling.width       =     6;
        subsampling.height      =     6;
        searchTiles(new Dimension(5,5));
        assertEquals(4, tiles.size());
    }
}
