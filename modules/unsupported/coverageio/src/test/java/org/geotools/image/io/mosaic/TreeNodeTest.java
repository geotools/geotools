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

import java.util.Set;
import java.util.Random;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import org.geotools.resources.OptionalDependencies;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link TreeNode} and {@link RTree}. The later is merely a wrapper around
 * {@link TreeNode} except for the {@link RTree#searchTiles} method, which is not
 * tested here (see {@link TileManagerTest} for that).
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class TreeNodeTest extends TestBase {
    /**
     * The root of an RTree for {@link #targetTiles}.
     */
    private TreeNode root;

    /**
     * Initializes every fields in this class.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Before
    public void initTreeNode() throws IOException {
        assertEquals(4733, targetTiles.length);
        root = new GridNode(targetTiles);
    }

    /**
     * Ensures that the view as a Swing tree is the same one that we get if we copy every
     * nodes in the default Swing tree node implementations.
     */
    @Test
    public void testSwingTree() {
        final javax.swing.tree.TreeNode copy = OptionalDependencies.copy(root);
        final int n = assertTreeEqual(root, copy, null, null);
        assertEquals(4737, n);
        final String text1 = OptionalDependencies.toString(root);
        final String text2 = OptionalDependencies.toString(copy);
        assertEquals(text1, text2);
    }

    /**
     * Ensures that the given nodes are equals. This method invokes itself recursively for
     * checking children equality. Returns the number of node compared.
     */
    private static int assertTreeEqual(
            final javax.swing.tree.TreeNode node,       final javax.swing.tree.TreeNode copy,
            final javax.swing.tree.TreeNode nodeParent, final javax.swing.tree.TreeNode copyParent)
    {
        int n = 1;
        assertSame(copyParent, copy.getParent());
        assertSame(nodeParent, node.getParent());
        assertEquals(copy.isLeaf(),            node.isLeaf());
        assertEquals(copy.getAllowsChildren(), node.getAllowsChildren());
        assertEquals(copy.toString(),          node.toString());
        assertEquals(copy.getChildCount(),     node.getChildCount());
        for (int i=0; i<copy.getChildCount(); i++) {
            assertEquals(i, copy.getIndex(copy.getChildAt(i)));
            assertEquals(i, node.getIndex(node.getChildAt(i)));
            n += assertTreeEqual(node.getChildAt(i), copy.getChildAt(i), node, copy);
        }
        return n;
    }

    /**
     * Tests with a set of files corresponding to a Blue Marble mosaic.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testTreeNode() throws IOException {
        // GridNode has many assert statements, so we want them enabled.
        assertTrue(GridNode.class.desiredAssertionStatus());
        assertNotNull(root.getUserObject());
        assertEquals(root, root);
        assertTrue (root.containsAll(manager.getTiles()));
        assertFalse(root.containsAll(Arrays.asList(sourceTiles)));
        assertTrue (((GridNode) root).isDense(root));
        final Rectangle bounds = new Rectangle(SOURCE_SIZE*4, SOURCE_SIZE*2);
        final Rectangle roi = new Rectangle();
        final Random random = new Random(4353223575290515986L);
        for (int i=0; i<100; i++) {
            roi.x      = random.nextInt(bounds.width);
            roi.y      = random.nextInt(bounds.height);
            roi.width  = random.nextInt(bounds.width  / 4);
            roi.height = random.nextInt(bounds.height / 4);
            final Set<Tile> intersect1 = toSet(root.intersecting(roi));
            final Set<Tile> intersect2 = intersecting(targetTiles, roi);
            final Set<Tile> contained1 = toSet(root.containedIn(roi));
            final Set<Tile> contained2 = containedIn(targetTiles, roi);
            assertEquals(intersect2, intersect1);
            assertEquals(contained2, contained1);
            assertFalse (intersect1.isEmpty()); // Only for our test suite (since empty set are not forbidden)
            assertTrue  (intersect1.containsAll(contained1));
            assertFalse (contained1.containsAll(intersect1));
            if (false) {
                System.out.print(roi);
                System.out.print(" intersect=");
                System.out.print(intersect1.size());
                System.out.print(" contained=");
                System.out.println(contained1.size());
            }
        }
        /*
         * Creates a copy and ensure it is identical to the original one.
         */
        final GridNode tree2 = new GridNode(targetTiles);
        assertEquals(root, tree2);
        /*
         * Tests removal of nodes.
         */
        assertEquals(root, tree2);
        for (int i=0; i<targetTiles.length; i += 10) {
            assertTrue(tree2.remove(targetTiles[i]));
        }
        assertFalse(root.deepEquals(tree2));
        for (int i=0; i<20; i++) {
            roi.x      = random.nextInt(bounds.width);
            roi.y      = random.nextInt(bounds.height);
            roi.width  = random.nextInt(bounds.width  / 4);
            roi.height = random.nextInt(bounds.height / 4);
            final Set<Tile> intersect1 = toSet(tree2.intersecting(roi));
            final Set<Tile> intersect2 = intersecting(targetTiles, roi);
            final Set<Tile> contained1 = toSet(tree2.containedIn(roi));
            final Set<Tile> contained2 = containedIn(targetTiles, roi);
            boolean removedSome = false;
            for (int j=0; j<targetTiles.length; j += 10) {
                final Tile tile = targetTiles[j];
                removedSome |= intersect2.remove(tile);
                removedSome |= contained2.remove(tile);
            }
            assertTrue  (removedSome);
            assertEquals(intersect2, intersect1);
            assertEquals(contained2, contained1);
            assertTrue  (intersect1.containsAll(contained1));
            assertFalse (contained1.containsAll(intersect1));
        }
    }

    /**
     * Tests the {@link RTree} class.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testRTree() throws IOException {
        final RTree tree = new RTree(root);
        if (false) {
            /*
             * For some unknown reason, using directly the root as the TreeNode leads to display
             * anomalies. We have to copy in Swing default implementation. I don't know why since
             * we have done our best in "testSwingTree" for ensuring that the original and the
             * copy were identical.
             */
            show(OptionalDependencies.copy(root));
        }
        assertEquals(new Rectangle(SOURCE_SIZE*4, SOURCE_SIZE*2), tree.getBounds());
        assertEquals(new Dimension(TARGET_SIZE,   TARGET_SIZE),   tree.getTileSize());
        int[] subsamplings;
        /*
         * Subsampling 15 in the first series and subsampling 9 in the second series are repeated
         * twice because the last occurence is a "virtual tile" generated in order to separate
         * tiles that would otherwise overlap. In the schema below, the tree on the left side is
         * what we get before overlapping tiles are separated. Tiles with subsamplings 15 and 9
         * are mixed in the same level because both of them are divisors of 45 while none of them
         * are the divisor of the other (9 is not a divisor of 15). So GridNode automatically
         * generate an extra level with same subsampling in order to separate them.
         *
         *   90                          90
         *   +---45                      +---45
         *   |   +---15                  |   +---15
         *   |   +---15                  |   |   +---15
         *   |   |  ...                  |   |   +---15
         *   |   +---09                  |   |      ...
         *   |   +---09                  |   +---09
         *   |      ...                  |       +---09
         *   +---45                      |       +---09
         *       +---15                  |          ...
         *       +---15                  +---45
         *       |  ...                      +---15
         *       +---09                      |   +---15
         *       +---09                      |   +---15
         *          ...                      |      ...
         */
        subsamplings = new int[] {5,15,15,45,90};
        checkSubsampling((GridNode) root, subsamplings, subsamplings.length, 3, 0);
        subsamplings = new int[] {1,3,9,9,45,90};
        checkSubsampling((GridNode) root, subsamplings, subsamplings.length, 4, 1);
    }

    /**
     * Ensures that every children have the expected subsampling. This method invokes itself
     * recursively down the tree. It is an helper method for {@link #testRTree} only. Checking
     * subsampling is a convenient way to ensure that every tiles are where they should be.
     *
     * @param node         The node to test.
     * @param subsamplings The expected subsamplings for every levels in the tree.
     * @param level        The level as an index in the {@code subsamplings} array.
     * @param branching    For the first node without tile, the branch to select.
     */
    private static void checkSubsampling(final GridNode node, final int[] subsamplings,
            int level, final int branchPoint, final int branchToSelect) throws IOException
    {
        final String message = node.toString();
        assertTrue(message, --level >= 0);
        final int subsampling = subsamplings[level];
        assertEquals(message, subsampling, node.getXSubsampling());
        assertEquals(message, subsampling, node.getYSubsampling());

        final GridNode parent = (GridNode) node.getParent();
        if (parent != null) {
            assertTrue(message, parent.contains(node));
            assertTrue(message, parent.getIndex(node) >= 0);
        }
        final Tile tile = node.getUserObject();
        if (tile != null) {
            final Dimension d = tile.getSubsampling();
            assertEquals(message, subsampling, d.width);
            assertEquals(message, subsampling, d.height);
            assertEquals(message, tile.getAbsoluteRegion(), node);
            assertEquals(message, node, tile.getAbsoluteRegion()); // Tests reflexibility.
        } else if (parent != null) {
            assertTrue(message, node.equals(parent));
        }

        if (level != branchPoint) {
            for (final TreeNode child : node) {
                assertTrue(message, node.contains(child));
                checkSubsampling((GridNode) child, subsamplings, level, branchPoint, branchToSelect);
            }
        } else {
            assertEquals(message, 2, node.getChildCount());
            final TreeNode child = node.getChildAt(branchToSelect);
            assertNull(message, child.getUserObject());
            assertTrue(message, node.contains(child));
            checkSubsampling((GridNode) child, subsamplings, level, branchPoint, branchToSelect);
        }
    }

    /**
     * Copies the given collection into a set.
     */
    private static Set<Tile> toSet(final Collection<Tile> tiles) {
        final Set<Tile> asSet = new LinkedHashSet<Tile>(tiles);
        assertEquals(tiles.size(), asSet.size());
        return asSet;
    }

    /**
     * Returns the tiles intersecting the given region.
     */
    private static Set<Tile> intersecting(final Tile[] tiles, final Rectangle region) throws IOException {
        final Set<Tile> interest = new LinkedHashSet<Tile>();
        for (final Tile tile : tiles) {
            if (region.intersects(tile.getAbsoluteRegion())) {
                assertTrue(interest.add(tile));
            }
        }
        return interest;
    }

    /**
     * Returns the tiles entirely contained in the given region.
     */
    private static Set<Tile> containedIn(final Tile[] tiles, final Rectangle region) throws IOException {
        final Set<Tile> interest = new LinkedHashSet<Tile>();
        for (final Tile tile : tiles) {
            if (region.contains(tile.getAbsoluteRegion())) {
                assertTrue(interest.add(tile));
            }
        }
        return interest;
    }
}
