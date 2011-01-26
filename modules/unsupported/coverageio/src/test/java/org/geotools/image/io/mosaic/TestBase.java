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
import java.io.File;
import java.io.IOException;
import javax.imageio.spi.ImageReaderSpi;

import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Base class for tests. The {@linkplain #sourceTiles source tiles} are 8 tiles from Nasa
 * Blue Marble. The {@linkplain #targetTiles target tiles} are a few thousands of smaller
 * tiles created from the source tiles by the {@linkplain #builder}. A {@linkplain #manager
 * tile manager} is created for those target tiles.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public abstract class TestBase {
    /**
     * Source tile size for BlueMarble mosaic.
     */
    protected static final int SOURCE_SIZE = 21600;

    /**
     * Target tile size for BlueMarble mosaic.
     */
    protected static final int TARGET_SIZE = 960;

    /**
     * Subsamplings to be used along X and Y axes. We fix those values in order to protect
     * the test suite from changes in the algorithm computing default subsampling values.
     *
     * @todo The commented-out values are the ones for a smaller tile size (480 instead of 960).
     *       It may be better to use them since it use different values for X and Y axis.
     */
    private static final int[]
            X_SUBSAMPLING = new int[] {1,3,5,9,15,45,90}, //{1,2,3,3,5,6,9,10,10,15,18,18,30,45,90},
            Y_SUBSAMPLING = new int[] {1,3,5,9,15,45,90}; //{1,2,3,4,4,6,8, 8,12,16,16,24,24,48,90};

    /**
     * The mosaic builder used for creating {@link #targetTiles}.
     */
    protected MosaicBuilder builder;

    /**
     * Tiles given as input to the {@linkplain #builder}.
     */
    protected Tile[] sourceTiles;

    /**
     * Tiles produces as output by the {@linkplain #builder}.
     */
    protected Tile[] targetTiles;

    /**
     * The tile manager for {@link #targetTiles}.
     */
    protected TileManager manager;

    /**
     * The tile manager factory to be given to the {@linkplain #builder}, or {@code null} for the
     * default one. Subclasses can override this method in order to test specific implementations
     * of {@link TileManager}.
     *
     * @return The tile manager factory to use.
     * @throws IOException If an I/O operation was required and failed.
     */
    protected TileManagerFactory getTileManagerFactory() throws IOException {
        return new TileManagerFactory(null) {
            @Override
            protected TileManager createGeneric(final Tile[] tiles) throws IOException {
                return new ComparedTileManager(tiles);
            }
        };
    }

    /**
     * Initializes every fields declared in this {@link TestBase} class.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Before
    public final void initTileManager() throws IOException {
        assertTrue("Assertions should be enabled.", MosaicBuilder.class.desiredAssertionStatus());

        builder = new MosaicBuilder(getTileManagerFactory());
        assertNull("No initial provider expected.", builder.getTileReaderSpi());
        builder.setTileReaderSpi("png");
        final ImageReaderSpi spi = builder.getTileReaderSpi();
        assertNotNull("Provider should be defined.", spi);

        final File directory = new File("geodata"); // Dummy directory - will not be read.
        final int S = SOURCE_SIZE; // For making reading easier below.
        sourceTiles = new Tile[] {
            new Tile(spi, new File(directory, "A1.png"), 0, new Rectangle(0*S, 0, S, S)),
            new Tile(spi, new File(directory, "B1.png"), 0, new Rectangle(1*S, 0, S, S)),
            new Tile(spi, new File(directory, "C1.png"), 0, new Rectangle(2*S, 0, S, S)),
            new Tile(spi, new File(directory, "D1.png"), 0, new Rectangle(3*S, 0, S, S)),
            new Tile(spi, new File(directory, "A2.png"), 0, new Rectangle(0*S, S, S, S)),
            new Tile(spi, new File(directory, "B2.png"), 0, new Rectangle(1*S, S, S, S)),
            new Tile(spi, new File(directory, "C2.png"), 0, new Rectangle(2*S, S, S, S)),
            new Tile(spi, new File(directory, "D2.png"), 0, new Rectangle(3*S, S, S, S))
        };
        final Dimension[] subsamplings = new Dimension[
                Math.max(X_SUBSAMPLING.length, Y_SUBSAMPLING.length)];
        assertEquals(subsamplings.length, X_SUBSAMPLING.length);
        assertEquals(subsamplings.length, Y_SUBSAMPLING.length);
        for (int i=0; i<subsamplings.length; i++) {
            subsamplings[i] = new Dimension(X_SUBSAMPLING[i], Y_SUBSAMPLING[i]);
        }
        builder.setSubsamplings(subsamplings);
        builder.setTileSize(new Dimension(TARGET_SIZE, TARGET_SIZE));
        builder.setTileDirectory(new File("S960")); // Dummy directory - will not be written.
        manager = builder.createTileManager(sourceTiles);
        targetTiles = manager.getTiles().toArray(new Tile[manager.getTiles().size()]);
    }

    /**
     * Shows the given tree in a Swing widget. This is used for debugging purpose only.
     */
    final void show(final javax.swing.tree.TreeNode root) {
        final Thread thread = Thread.currentThread();
        final JFrame frame = new JFrame("TreeNode");
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) {
                thread.interrupt();
            }
        });
        frame.add(new JScrollPane(new JTree(root)));
        frame.pack();
        frame.setVisible(true);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // Go back to work.
        }
        frame.dispose();
    }
}
