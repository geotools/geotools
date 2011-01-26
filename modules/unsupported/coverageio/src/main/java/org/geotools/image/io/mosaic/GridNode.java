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

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;
import java.util.ListIterator;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import static org.geotools.image.io.mosaic.Tile.MASK;


/**
 * List of tiles inside the bounding box of a bigger (or equals in size) tile. This class fills
 * a similar purpose than RTree, except that we do not calculate any new bounding boxes. We try
 * to fit children in existing tile bounds on the assumption that most tile layouts are already
 * organized in some form pyramid.
 * <p>
 * The value of the inherited rectangle is the {@linkplain Tile#getAbsoluteRegion absolute region}
 * of the tile, computed and stored once for ever for efficienty during searchs. This class extends
 * {@link Rectangle} for pure opportunist reasons, in order to reduce the amount of object created
 * (because we will have thousands of TreeNodes) and for direct (no indirection, no virtual calls)
 * invocation of {@link Rectangle} services. We authorize ourself this unrecommendable practice only
 * because this class is not public. The inherited {@link Rectangle} should <string>never</strong>
 * be modified by anyone outside this class.
 * <p>
 * Note that the {@link #compareTo} method is inconsistent with {@link #equals}. It should
 * be considered as an implementation details exposed because this class is not public.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
@SuppressWarnings("serial") // Not expected to be serialized.
final class GridNode extends TreeNode implements Comparable<GridNode> {
    /**
     * The index, used for preserving order compared to the user-specified one.
     */
    private final int index;

    /**
     * The subsampling as an unsigned short greater than zero.
     * The value must be used in expressions like {@code subsampling & 0xFFFF}.
     * <p>
     * On {@link GridNode} creation, this is initialized to {@linkplain Tile#getSubsampling tile
     * subsampling}.  After the call to {@link #postTreeCreation}, the value is increased (never
     * reduced) to the greatest subsampling found in all children. Because children usually have
     * finer subsampling, this value is typically unmodified compared to its initial value.
     */
    private short xSubsampling, ySubsampling;

    /**
     * {@code true} if at least one tile overlaps an other tile in direct {@linkplain #children}.
     * As a special case, if a tile has exactly the same bounding box than an other tile, then we
     * do not consider it as an overlap. This is because those exact matchs are easy to handle by
     * {@link RTree}.
     */
    private boolean overlaps;

    /**
     * {@code true} if the children fill completly this node bounds.
     */
    private boolean dense;

    /**
     * Comparator for sorting tiles by descreasing subsamplings and area. The {@linkplain
     * GridNode#GridNode(Tile[]) constructor} expects this order for inserting a tile into
     * the smallest tile that can contains it. If two tiles have the same subsampling, then
     * they are sorted by descreasing area in absolute coordinates.
     * <p>
     * If two tiles have the same subsampling and area, then their order is restored on the
     * basis that initial order, when sorted by {@link TileManager}, should be efficient for
     * reading tiles sequentially.
     */
    private static final Comparator<GridNode> PRE_PROCESSING = new Comparator<GridNode>() {
        public int compare(final GridNode n1, final GridNode n2) {
            final int s1 = (n1.xSubsampling & MASK) * (n1.ySubsampling & MASK);
            final int s2 = (n2.xSubsampling & MASK) * (n2.ySubsampling & MASK);
            if (s1 > s2) return -1; // Greatest values first
            if (s1 < s2) return +1;
            final long a1 = (long) n1.width * (long) n1.height;
            final long a2 = (long) n2.width * (long) n2.height;
            if (a1 > a2) return -1;
            if (a1 < a2) return +1;
            return n1.index - n2.index;
        }
    };

    /**
     * Comparator for sorting tiles in the same order than the one specified at construction time.
     * <p>
     * This method is inconsistent with {@link #equals}. It is okay for our usage of it,
     * which should be restricted to this {@link GridNode} package-privated class only.
     */
    public int compareTo(final GridNode that) {
        return index - that.index;
    }

    /**
     * Creates a node for the specified bounds with no subsampling and no tile.
     */
    private GridNode(final Rectangle bounds) {
        super(bounds);
        index = -1;
    }

    /**
     * Creates a node for a single tile.
     *
     * @param  tile  The tile.
     * @param  index The original index in the user-specified array.
     * @throws IOException if an I/O operation was required and failed.
     */
    private GridNode(final Tile tile, final int index) throws IOException {
        super(tile.getAbsoluteRegion());
        this.tile = tile;
        final Dimension subsampling = tile.getSubsampling();
        xSubsampling = Tile.ensureStrictlyPositive(subsampling.width);
        ySubsampling = Tile.ensureStrictlyPositive(subsampling.height);
        this.index = index;
    }

    /**
     * Builds the root of a tree for the given tiles.
     *
     * @param  tiles The tiles to be inserted in the tree.
     * @throws IOException if an I/O operation was required and failed.
     */
    public GridNode(final Tile[] tiles) throws IOException {
        /*
         * Sorts the TreeNode with biggest tree first (this is required for the algorithm building
         * the tree). Note that the TreeNode array should be created before any sorting is applied,
         * because its creation may involve disk reading and those reading are more efficient when
         * performed in the tiles iteration order (assuming this array was sorted by TileManager).
         */
        GridNode[] nodes = new GridNode[tiles.length];
        for (int i=0; i<tiles.length; i++) {
            nodes[i] = new GridNode(tiles[i], i);
        }
        Arrays.sort(nodes, PRE_PROCESSING);
        /*
         * If every tiles have the same subsampling, we are probably in the case where a set of
         * input tiles, all having similar size, are given to MosaicImageWriter for creating a
         * pyramid of images. The RTree creating from such set of tiles will be very inefficient.
         * Adds a couple of fictious nodes with greater area so that the code after this block
         * can create a deeper tree structure. Note that this is a somewhat naive algorithm.
         * The aim is not to create a sophesticated RTree here; it is just to atenuate the
         * worst case scenario.
         */
        final boolean isFlat = isFlat(nodes);
        if (isFlat) {
            nodes = prependTree(nodes);
        }
        /*
         * Special case: checks if the first node contains all subsequent nodes. If this is true,
         * then there is no need to keep the special root TreeNode with the tile field set to null.
         * We can keep directly the first node instead. Note that this special case should NOT be
         * extended further the first node, otherwise the tiles prior the retained node would be
         * discarted.
         */
        GridNode root = null;
        if (nodes.length != 0) {
            root = nodes[0];
            for (int i=1; i<nodes.length; i++) {
                if (!root.contains(nodes[i])) {
                    root = null;
                    break;
                }
            }
        }
        if (root != null) {
            setBounds(root);
            tile         = root.tile;
            index        = root.index;
            xSubsampling = root.xSubsampling;
            ySubsampling = root.ySubsampling;
        } else {
            index = -1;
        }
        /*
         * Now inserts every nodes in the tree. At first we try to add each node into the smallest
         * parent that can contain it and align it on a grid. If the node can not be aligned, then
         * we add it into the smallest parent regardless of alignment. The grid condition produces
         * good results for TileLayout.CONSTANT_TILE_SIZE. However it may not work so well with
         * random tiles (open issue).
         */
        for (int i=(root != null ? 1 : 0); i<nodes.length; i++) {
            final GridNode child = nodes[i];
            final GridNode parent = smallest(child);
            if (!parent.overlaps) {
                TreeNode existing = parent.firstChildren();
                while (existing != null) {
                    if (child.intersects(existing) && !child.equals(existing)) {
                        parent.overlaps = true;
                        break;
                    }
                    existing = existing.nextSibling();
                }
            }
            parent.addChild(child);
        }
        if (isFlat) {
            removeEmpty();
        }
        /*
         * Calculates the bounds only for root node, if not already computed. We do not iterate
         * down the tree since every children should have their bounds set to the tile bounds.
         */
        if (root == null) {
            assert (width | height) < 0 : this;
            TreeNode child = firstChildren();
            while (child != null) {
                // No need to invoke setBounds for the first child since Rectangle.add(Rectangle)
                // takes care of that if the width or height is negative (specified in javadoc).
                add(child);
                child = child.nextSibling();
            }
        }
        splitOverlappingChildren(); // Must be after bounds calculation.
        postTreeCreation();
        assert checkValidity() : toTree();
    }

    /**
     * Returns the smallest tree node containing the given region. This method assumes that
     * {@code this} node, if non-empty, {@linkplain #contains contains} the given bounds.
     * Note that the constructor may invoke this method from the root with an empty bounding
     * box, which is valid.
     * <p>
     * This method tries to returns the smallest {@linkplain #isGridded gridded} node, if any.
     * By "gridded" we mean a node that can align the given bounds on a grid. If there is no
     * such node, then any node containing the bounds is returned.
     *
     * @param  The bounds to check for inclusion.
     * @return The smallest node, or {@code this} if none (never {@code null}).
     */
    private GridNode smallest(final Rectangle bounds) {
        long smallestArea;
        boolean gridded;
        if (isEmpty()) {
            smallestArea = Long.MAX_VALUE;
            gridded = false;
        } else {
            assert contains(bounds);
            smallestArea = (long) width * (long) height;
            gridded = isGridded(bounds);
        }
        GridNode smallest = this;
        GridNode child = (GridNode) firstChildren();
        while (child != null) {
            if (child.contains(bounds)) {
                final GridNode candidate = child.smallest(bounds);
                final boolean cg = candidate.isGridded(bounds);
                if (!gridded || cg) {
                    final long area = (long) candidate.width * (long) candidate.height;
                    if ((!gridded && cg) || (area < smallestArea)) {
                        // If the smallest node was not gridded while the candidate is gridded,
                        // retains the candidate inconditionnaly. Otherwise retains only if smaller.
                        smallestArea = area;
                        smallest = candidate;
                        gridded = cg;
                    }
                }
            }
            child = (GridNode) child.nextSibling();
        }
        return smallest;
    }

    /**
     * Returns {@code true} if the given child is layered on a grid in this node.
     */
    private boolean isGridded(final Rectangle child) {
        return width  % child.width  == 0 && (child.x - x) % child.width  == 0 &&
               height % child.height == 0 && (child.y - y) % child.height == 0;
    }

    /**
     * Returns the largest horizontal or vertical distance between this rectangle and the specified
     * one. Returns a negative number if the rectangles overlap. Diagonals are <strong>not</strong>
     * computed.
     * <p>
     * This method is not robust to integer arithmetic overflow. In such case, an
     * {@link AssertionError} is likely to be thrown if assertions are enabled.
     */
    private int distance(final Rectangle rect) {
        int dx = rect.x - x;
        if (dx >= 0) {
            dx -= width;
        } else {
            dx += rect.width;
            dx = -dx;
        }
        int dy = rect.y - y;
        if (dy >= 0) {
            dy -= height;
        } else {
            dy += rect.height;
            dy = -dy;
        }
        final int distance = Math.max(dx, dy);
        assert (intersects(rect) ? (dx < 0 && dy < 0) : (distance >= 0)) : distance;
        return distance;
    }

    /**
     * Makes sure that this node and all its children do not contains overlapping tiles. If at
     * least one overlapping is found, then the nodes are reorganized in non-overlapping sub-nodes.
     * Algorithm overview:
     * <p>
     * <ol>
     *   <li>For the current {@linkplain #children}, keeps the first node and remove every nodes
     *       that overlap with a previous one (except special cases described above). The removed
     *       nodes are stored in a temporary list.</li>
     *   <li>The nodes selected in the previous step are groupped in a new {@link GridNode},
     *       which will be the first {@linkplain #children} of this tile.</li>
     *   <li>Repeat the process with the nodes that were removed in the first step. Each new
     *       group is added as a new {@linkplain #children} in this node.</li>
     * </ol>
     * <p>
     * <b>special case:</b> if an overlapping is found but the two nodes have identical bounds,
     * then they are considered as if they did not overlap. This exception exists because this
     * trivial overlap is easy to detect and to process by {@link RTree}.
     */
    private void splitOverlappingChildren() {
        assert isLeaf() || !isEmpty() : this; // Requires that bounds has been computed.
        GridNode child = (GridNode) firstChildren();
        while (child != null) {
            child.splitOverlappingChildren();
            child = (GridNode) child.nextSibling();
        }
        if (!overlaps) {
            return;
        }
        List<GridNode> toProcess = new LinkedList<GridNode>();
        final List<GridNode> retained = new ArrayList<GridNode>();
        child = (GridNode) firstChildren();
        while (child != null) {
            toProcess.add(child);
            child = (GridNode) child.nextSibling();
        }
        removeChildren(); // Necessary in order to give children to other nodes.
        int bestIndex=0, bestDistance=0;
        /*
         * The loop below is for processing a group of nodes. A "group of nodes" is either
         * the initial children list (on the first iteration), or the nodes that have not be
         * retained in a previous run of this loop. In the later case, those remaining nodes
         * need to be examined again and again until they are classified in some group.
         */
        while (!toProcess.isEmpty()) {
            final List<GridNode> removed = new LinkedList<GridNode>();
            GridNode added = toProcess.remove(0);
            retained.add(added);
            /*
             * The loop below is for moving every non-overlapping nodes to the "retained" list,
             * begining with the first node that we retained unconditionnaly in the above line
             * (we need to start with one node in order to select non-overlapping nodes...)
             */
            ListIterator<GridNode> it;
            while ((it = toProcess.listIterator()).hasNext()) {
                GridNode best = null;
                /*
                 * The loop below is for removing every nodes that overlap with the "added" node,
                 * and select only one node (the "best" one) in the non-overlapping nodes. We
                 * select the closest tile (relative to the "added" one) rather than the first
                 * one because the retention order is significant.
                 */
                do {
                    final GridNode candidate = it.next();
                    final int distance = added.distance(candidate);
                    if (distance < 0) {
                        /*
                         * Found an overlapping tile.  Accept inconditionnaly the tile if its bounds
                         * is exactly equals to the "added" tile (this is the special case described
                         * in the method javadoc above). Otherwise remove it from the toProcess list
                         * and search for an other tile.
                         */
                        if (added.equals(candidate)) {
                            retained.add(candidate);
                        } else {
                            removed.add(candidate);
                        }
                        it.remove();
                    } else if (best == null || distance < bestDistance) {
                        /*
                         * The tile do not overlaps. Retain it only if it is the closest one
                         * to the "added" tile.  Otherwise left it in the toProcess list for
                         * consideration in a future iteration.
                         */
                        bestDistance = distance;
                        bestIndex = it.previousIndex();
                        best = candidate;
                        // Note: if the distance is 0 we could break the loop as an optimization
                        // (since we can't get anything better), but we don't because we still
                        // need to remove the overlapping tiles that may be present in toProcess.
                    }
                } while (it.hasNext());
                /*
                 * If we found no non-overlapping tile, we are done. The toProcess list should be
                 * empty now (it will be tested in an assert statement after the loop). Otherwise
                 * move the best tile from the "toProcess" to the "retained" list.
                 */
                if (best == null) {
                    break;
                }
                if (toProcess.remove(bestIndex) != best) {
                    throw new AssertionError(bestIndex);
                }
                retained.add(best);
                added = best;
            }
            assert toProcess.isEmpty() : toProcess;
            assert Collections.disjoint(retained, removed);
            final GridNode[] sorted = retained.toArray(new GridNode[retained.size()]);
            retained.clear();
            Arrays.sort(sorted, PRE_PROCESSING);
            child = new GridNode(this);
            assert child.isLeaf();
            for (TreeNode newChild : sorted) {
                child.addChild(newChild);
            }
            addChild(child);
            toProcess = removed;
        }
        overlaps = false;
    }

    /**
     * Remove empty nodes.
     */
    private void removeEmpty() {
        GridNode child = (GridNode) firstChildren();
        while (child != null) {
            final GridNode next = (GridNode) child.nextSibling();
            child.removeEmpty(); // May lost its nextSibling().
            child = next;
        }
        if (tile == null && isLeaf() && !isRoot()) {
            remove();
        }
    }

    /**
     * Invoked when the tree construction is completed with every nodes assigned to its final
     * parent. This method calculate the values that depend on the child hierarchy, including
     * subsampling.
     */
    private void postTreeCreation() {
        GridNode child = (GridNode) firstChildren();
        while (child != null) {
            child.postTreeCreation();
            if ((child.xSubsampling & MASK) > (xSubsampling & MASK)) xSubsampling = child.xSubsampling;
            if ((child.ySubsampling & MASK) > (ySubsampling & MASK)) ySubsampling = child.ySubsampling;
            child = (GridNode) child.nextSibling();
        }
        dense = isDense(this, this);
    }

    /**
     * Returns {@code true} if at least one tile having the given subsampling or a finer one
     * intersects the given region.
     *
     * @param  region The region to look for, in "absolute" coordinates.
     * @param  subsampling The maximal subsampling to look for.
     * @return {@code true} if at least one tile having the given subsampling or
     *         a finer one intersects the given region.
     */
    public boolean intersects(final Rectangle region, final Dimension subsampling) {
        if (intersects(region)) {
            if (tile != null) {
                final int xSubsampling, ySubsampling;
                if (isLeaf()) {
                    // Slight optimization: if we are a leaf, x/ySubsampling are garanteed
                    // to be equals to the value returned by Tile.getSubsampling().
                    xSubsampling = (this.xSubsampling & MASK);
                    ySubsampling = (this.ySubsampling & MASK);
                } else {
                    final Dimension candidate = tile.getSubsampling();
                    xSubsampling = candidate.width;
                    ySubsampling = candidate.height;
                }
                if (xSubsampling <= subsampling.width && ySubsampling <= subsampling.height) {
                    return true;
                }
            }
            GridNode child = (GridNode) firstChildren();
            while (child != null) {
                if (child.intersects(region, subsampling)) {
                    return true;
                }
                child = (GridNode) child.nextSibling();
            }
        }
        return false;
    }

    /**
     * Returns the subsampling along <var>x</var> axis.
     */
    public int getXSubsampling() {
        return xSubsampling & MASK;
    }

    /**
     * Returns the subsampling along <var>y</var> axis.
     */
    public int getYSubsampling() {
        return ySubsampling & MASK;
    }

    /**
     * Returns {@code true} if this node has more than one tile and some of them overlaps.
     */
    @Override
    public boolean hasOverlaps() {
        return overlaps;
    }

    /**
     * Returns {@code true} if every nodes in the given array use the same subsampling.
     *
     * @param  tiles The array of nodes to check for common subsampling.
     * @return {@code true} if every nodes in the given array use the same subsampling.
     */
    private static boolean isFlat(final GridNode[] nodes) {
        if (nodes != null && nodes.length != 0) {
            GridNode node = nodes[0];
            final short xSubsampling = node.xSubsampling;
            final short ySubsampling = node.ySubsampling;
            for (int i=1; i<nodes.length; i++) {
                node = nodes[i];
                if (node.xSubsampling != xSubsampling || node.ySubsampling != ySubsampling) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Inserts a tree of nodes without tiles before the nodes in the given array. This method is
     * typically invoked for {@linkplain #isFlat flat} array of nodes only, which are the "worst
     * case" scenario. This method tries to attenuate the effect of worst case scenario.
     * <p>
     * The order of nodes is significant. This method must prepend bigger nodes first, like what
     * we would get if the nodes where associated with real tiles and the array sorted with the
     * {@link #PRE_PROCESSING} comparator.
     *
     * @param  nodes The nodes for which to prepend a tree.
     * @return The nodes with a tree prepend before them.
     */
    private static GridNode[] prependTree(GridNode[] nodes) {
        final Dimension largest = new Dimension();
        final Rectangle bounds = new Rectangle(-1,-1);
        for (final GridNode node : nodes) {
            bounds.add(node);
            if (node.width  > largest.width)  largest.width  = node.width;
            if (node.height > largest.height) largest.height = node.height;
        }
        if (!bounds.isEmpty()) {
            /*
             * Asks for node that can contain at least 2×2 tiles, otherwise creating
             * those nodes would consume memory without significant performance gain.
             */
            largest.width  *= 2;
            largest.height *= 2;
            final int[][] divisors = MosaicBuilder.suggestedNumTiles(bounds, largest, 16, false);
            final int[] sx = divisors[0];
            final int[] sy = divisors[1];
            final Rectangle part = new Rectangle();
            final List<GridNode> list = new ArrayList<GridNode>();
            for (int i=0; i<sx.length; i++) {
                final int nx = sx[i];
                final int ny = sy[i];
                part.y      = bounds.y;
                part.width  = bounds.width  / nx;
                part.height = bounds.height / ny;
                for (int y=0; y<ny; y++) {
                    part.x = bounds.x;
                    for (int x=0; x<nx; x++) {
                        list.add(new GridNode(part));
                        part.x += part.width;
                    }
                    part.y += part.height;
                }
            }
            final int size = list.size();
            final GridNode[] old = nodes;
            nodes = list.toArray(new GridNode[size + old.length]);
            System.arraycopy(old, 0, nodes, size, old.length);
        }
        return nodes;
    }

    /**
     * Returns {@code true} if the rectangles in the given collection fill completly the given
     * ROI with no empty space.
     *
     * @todo This method is not yet correctly implemented. For now we performs a naive check
     *       which is suffisient for common {@link TileLayout}. We may need to revisit this
     *       method in a future version.
     */
    private static boolean isDense(final Rectangle roi, final Iterable<? extends Rectangle> regions) {
        Rectangle bounds = null;
        for (final Rectangle rect : regions) {
            final Rectangle inter = roi.intersection(rect);
            if (bounds == null) {
                bounds = inter;
            } else {
                bounds.add(inter); // See java.awt.Rectangle javadoc for empty rectangle handling.
            }
        }
        return bounds == null || bounds.equals(roi);
    }

    /**
     * Returns {@code true} if this node fills completly the given ROI with no empty space.
     *
     * @todo This method is not yet correctly implemented. For now we performs a naive check
     *       which is suffisient for common {@link TileLayout}. We may need to revisit this
     *       method in a future version.
     *
     * @todo Not yet used, but should be in a future version. See the TODO notice in {@link RTree}.
     */
    public boolean isDense(final Rectangle roi) {
        assert contains(roi);
        return dense;
    }
}
