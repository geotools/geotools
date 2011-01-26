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
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.awt.Rectangle;
import java.io.IOException;

import org.geotools.util.Utilities;
import org.geotools.resources.OptionalDependencies;


/**
 * Base class for tree node wrapping a {@link Tile}. Children are managed as a linked list.
 * <p>
 * This class extends {@link Rectangle} for pure opportunist reasons, in order to reduce the amount
 * of object created (we will have thousands of {@code TreeNode}s) and for direct (without the cost
 * of an additional pointer) invocation of {@link Rectangle} methods. We do this unrecommendable
 * practice only because this class is not public. The inherited {@link Rectangle} should
 * <string>never</strong> be modified by anyone outside this class.
 * <p>
 * The inherited {@linkplain #x x}, {@linkplain #y y}, {@linkplain #width width} and
 * {@linkplain #height height} fields are tile bounds in <cite>absolute</cite> coordinates,
 * as returned by {@link Tile#getAbsoluteRegion}. They must be wide enough for including
 * all children bounds.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
class TreeNode extends Rectangle implements Iterable<TreeNode>, javax.swing.tree.TreeNode {
    // TODO: should implements org.geotools.gui.swing.tree.TreeNode
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 1669511478839242773L;

    /**
     * The tile wrapped by this node, or {@code null} if we should read only the children,
     * not the tile itself.
     */
    protected Tile tile;

    /**
     * The parent, or {@code null} if none.
     */
    private TreeNode parent;

    /**
     * The first and last children {@linkplain #addChild added} to this node,
     * or {@code null} if none.
     */
    private TreeNode firstChildren, lastChildren;

    /**
     * The previous and next sibling, or {@code null} if none. Used in order to created
     * a linked list of children.
     */
    private TreeNode previousSibling, nextSibling;

    /**
     * Creates an initially empty rectangle. Width and height are set to -1, which
     * stands for non-existant rectangle according {@link Rectangle} documentation.
     * Other fields (including subsamplings) are set to 0 or {@code null}.
     */
    protected TreeNode() {
        super(-1, -1);
    }

    /**
     * Creates a node for the specified bounds with no tile.
     *
     * @param bounds The tile bounds in absolute coordinates.
     */
    protected TreeNode(final Rectangle bounds) {
        super(bounds);
    }

    /**
     * Adds the given tile as a child of this tile. This method do nothing if the given child
     * is null (which is typically the case when it doesn't intercept the region of interest).
     * <p>
     * Note that this method has pre-conditions checked by {@code assert} statements. If the
     * {@link TreeNode} class become public, we would need to performs inconditional checks.
     *
     * @throws ClassCastException If a subclass has some restrictions about the kind
     *         of child accepted, and the given child doesn't meet those restrictions.
     */
    public void addChild(final TreeNode child) throws ClassCastException {
        if (child != null) {
            assert child.isRoot() && (tile == null || contains(child)) : child;
            child.parent = this;
            if (lastChildren == null) {
                lastChildren = firstChildren = child;
            } else {
                child.previousSibling = lastChildren;
                lastChildren.nextSibling = child;
                lastChildren = child;
            }
        }
    }

    /**
     * Removes all children.
     */
    public void removeChildren() {
        TreeNode child = firstChildren;
        while (child != null) {
            assert child.parent == this;
            final TreeNode next = child.nextSibling;
            child.previousSibling = null;
            child.nextSibling     = null;
            child.parent          = null;
            child = next;
        }
        firstChildren = lastChildren = null;
    }

    /**
     * Removes this tile and all its children from the tree.
     */
    public void remove() {
        if (previousSibling != null) {
            previousSibling.nextSibling = nextSibling;
        }
        if (nextSibling != null) {
            nextSibling.previousSibling = previousSibling;
        }
        if (parent != null) {
            if (parent.firstChildren == this) {
                parent.firstChildren = nextSibling;
            }
            if (parent.lastChildren == this) {
                parent.lastChildren = previousSibling;
            }
            parent = null;
        }
        previousSibling = null;
        nextSibling = null;
    }

    /**
     * Returns the number of non-null tiles, including in children.
     */
    final int getTileCount() {
        int count = (tile != null) ? 1 : 0;
        TreeNode child = firstChildren;
        while (child != null) {
            count += child.getTileCount();
            child = child.nextSibling;
        }
        return count;
    }

    /**
     * Returns the tile in this node, or {@code null} if none. This is also the <cite>Swing</cite>
     * user object which is formatted (when available) by the {@link #toString} method, as required
     * by {@link javax.swing.JTree}.
     */
    public final Tile getUserObject() {
        return tile;
    }

    /**
     * Returns the parent node that contains this child node, or {@code null} if this node
     * is the root node.
     */
    public final TreeNode getParent() {
        return parent;
    }

    /**
     * Returns {@code true} if this node has no parent. Note that having no parent
     * implies having no sibling neither (otherwise the tree would be malformed).
     */
    public final boolean isRoot() {
        return parent == null && previousSibling == null && nextSibling == null;
    }

    /**
     * Returns {@code true} if this node has no children. Note that this is slightly different from
     * {@link #getAllowsChildren} which returns {@code false} if this node <strong>can not</strong>
     * have children.
     */
    public final boolean isLeaf() {
        assert (firstChildren == null) == (lastChildren == null);
        return firstChildren == null;
    }

    /**
     * Return {@code true} in almost every case since there is no reason to prevent a tile from
     * containing smaller tiles, unless the tile bounds {@linkplain #isEmpty is empty}.
     */
    public final boolean getAllowsChildren() {
        return !super.isEmpty();
    }

    /**
     * Returns {@code true} if this node has more than one tile and some of them overlaps.
     * The default implementation returns {@code false}.
     */
    public boolean hasOverlaps() {
        return false;
    }

    /**
     * If this node has exactly one child, returns that child. Otherwise returns {@code null}.
     */
    public final TreeNode getChild() {
        return (firstChildren == lastChildren) ? firstChildren : null;
    }

    /**
     * Returns the number of children. This method is provided for <cite>Swing</cite> usage.
     * When using {@link TreeNode} directly, consider using {@link #iterator} instead.
     */
    public final int getChildCount() {
        // Don't invoke assertValid() directly or indirectly since it could cause never-ending loop.
        int count = 0;
        TreeNode child = firstChildren;
        while (child != null) {
            child = child.nextSibling;
            count++;
        }
        return count;
    }

    /**
     * Returns the children at the given index. This method is provided for <cite>Swing</cite>
     * usage. When using {@link TreeNode} directly, consider using {@link #iterator} instead.
     */
    public final TreeNode getChildAt(int index) {
        TreeNode child = firstChildren;
        while (child != null) {
            if (index == 0) {
                return child;
            }
            child = child.nextSibling;
            index--;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns the index of given node. If this node does not contain the given one,
     * -1 will be returned. This method is provided for <cite>Swing</cite> usage.
     */
    public final int getIndex(final javax.swing.tree.TreeNode node) {
        int index = 0;
        TreeNode child = firstChildren;
        while (child != null) {
            if (child == node) {
                assert getChildAt(index) == node : index;
                return index;
            }
            child = child.nextSibling;
            index++;
        }
        return -1;
    }

    /**
     * Returns the first children, or {@code null} if none. Used as an alternative to the
     * iterator, since profiling suggests that it has a perceptible cost.
     */
    final TreeNode firstChildren() {
        return firstChildren;
    }

    /**
     * Returns the next sibling, or {@code null} if none. Used as an alternative to the
     * iterator, since profiling suggests that it has a perceptible cost.
     */
    final TreeNode nextSibling() {
        return nextSibling;
    }

    /**
     * Returns an enumeration over the children. This method is provided for <cite>Swing</cite>
     * usage. When using {@link TreeNode} directly, consider using {@link #iterator} instead.
     */
    public final Enumeration<TreeNode> children() {
        return new Iter(firstChildren);
    }

    /**
     * Returns an iterator over the children.
     */
    public final Iterator<TreeNode> iterator() {
        return new Iter(firstChildren);
    }

    /**
     * The iterator over the children.
     */
    private static final class Iter implements Iterator<TreeNode>, Enumeration<TreeNode> {
        /**
         * The next children to return, or {@code null} if we have reached
         * the end of iteration.
         */
        private TreeNode children;

        /**
         * The last children returned, or {@code null} if none or removed.
         * Used for {@link #remove} implementation.
         */
        private TreeNode last;

        /**
         * Creates an iterator starting at the given element.
         */
        public Iter(final TreeNode first) {
            children = first;
        }

        /**
         * Returns {@code true} if there is more elements to return.
         */
        public boolean hasMoreElements() {
            return hasNext();
        }

        /**
         * Returns {@code true} if there is more elements to return.
         */
        public boolean hasNext() {
            return children != null;
        }

        /**
         * Returns the next element.
         */
        public TreeNode next() {
            if (children != null) {
                last = children;
                children = children.nextSibling;
                return last;
            }
            throw new NoSuchElementException();
        }

        /**
         * Returns the next element.
         */
        public TreeNode nextElement() {
            return next();
        }

        /**
         * Removes the last children returned by this iterator.
         */
        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            last.remove();
            last = null;
        }
    }

    /**
     * Removes the given tile.
     *
     * @param  tile The tile to remove.
     * @return {@code true} if the given tile was found and removed.
     * @throws IOException if an I/O operation was required and failed.
     */
    public final boolean remove(final Tile tile) throws IOException {
        return contains(tile.getAbsoluteRegion(), tile, true);
    }

    /**
     * Returns {@code true} if this tree contains all the given tile.
     * This method is insensitive to the order of elements in the given array.
     *
     * @param  tiles The tiles to test for presence in this tree.
     * @return {@code true} if every given tiles are present in this tree.
     * @throws IOException if an I/O operation was required and failed.
     */
    public final boolean containsAll(final Collection<Tile> tiles) throws IOException {
        for (final Tile tile : tiles) {
            if (!contains(tile.getAbsoluteRegion(), tile, false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if this tree contains the given tile. This method
     * invokes itself recursively for scanning through the subtrees.
     *
     * @param  region The result of {@link Tile#getAbsoluteRegion}.
     * @param  candidate The tile to test for presence in this tree.
     * @param  remove {@code true} if the node containing the tile should be removed.
     * @return {@code true} if the given tile is presents in this tree.
     */
    private boolean contains(final Rectangle region, final Tile candidate, final boolean remove) {
        if (equals(region)) {
            if (remove) {
                tile = null;
            }
            return true;
        }
        if (super.contains(region)) {
            TreeNode child = firstChildren;
            while (child != null) {
                if (child.contains(region, candidate, remove)) {
                    if (remove && child.tile == null && child.isLeaf()) {
                        child.remove();
                    }
                    return true;
                }
                child = child.nextSibling;
            }
        }
        return false;
    }

    /**
     * Returns the tiles entirely contained in the given region of interest (ROI).
     * The returned collection is a copy that can be modified without altering the tree.
     */
    public final Collection<Tile> containedIn(final Rectangle roi) {
        final List<Tile> tiles = new LinkedList<Tile>();
        containedIn(roi, tiles);
        return tiles;
    }

    /**
     * Returns the tiles entirely contained in the given region of interest (ROI).
     * This method invokes itself recursively down the tree.
     */
    private void containedIn(final Rectangle roi, final Collection<Tile> tiles) {
        if (roi.contains(this)) {
            copy(tiles);
        } else if (roi.intersects(this)) {
            TreeNode child = firstChildren;
            while (child != null) {
                child.containedIn(roi, tiles);
                child = child.nextSibling;
            }
        }
    }

    /**
     * Copies inconditionnaly every tiles (including children) to the given list.
     * This method invokes itself recursively down the tree.
     */
    private void copy(final Collection<Tile> tiles) {
        if (tile != null) {
            tiles.add(tile);
        }
        TreeNode child = firstChildren;
        while (child != null) {
            child.copy(tiles);
            child = child.nextSibling;
        }
    }

    /**
     * Returns the tiles intersecting the given region of interest (ROI).
     * The returned collection is a copy that can be modified without altering the tree.
     */
    public final Collection<Tile> intersecting(final Rectangle roi) {
        final List<Tile> tiles = new LinkedList<Tile>();
        intersecting(roi, tiles);
        return tiles;
    }

    /**
     * Adds the tiles intersecting the given region of interest (ROI) to the given array.
     * This method invokes itself recursively down the tree.
     */
    private void intersecting(final Rectangle roi, final Collection<Tile> tiles) {
        if (intersects(roi)) {
            if (tile != null) {
                tiles.add(tile);
            }
            TreeNode child = firstChildren;
            while (child != null) {
                child.intersecting(roi, tiles);
                child = child.nextSibling;
            }
        }
    }

    /**
     * Copies to the specified list the tiles in this node and every children nodes.
     */
    final void getTiles(final List<Tile> tiles) {
        if (tile != null) {
            tiles.add(tile);
        }
        TreeNode child = firstChildren;
        while (child != null) {
            child.getTiles(tiles);
            child = child.nextSibling;
        }
    }

    /**
     * Compares this rectangle with the specified one for equality. This method
     * <strong>must</strong> be semantically identical to {@link Rectangle#equals}
     * and the inherited {@link Rectangle#hashCode} must be unchanged. This is required
     * for proper working of {@link #filter}.
     */
    @Override
    public final boolean equals(final Object other) {
        return (other == this) || super.equals(other);
    }

    /**
     * Compares this tree and all its chilren with the specified one for equality.
     */
    public final boolean deepEquals(final TreeNode other) {
        if (other == this) {
            return true;
        }
        if (!equals(other) || !Utilities.equals(tile, other.tile)) {
            return false;
        }
        final Iterator<TreeNode> it1 = this .iterator();
        final Iterator<TreeNode> it2 = other.iterator();
        while (it1.hasNext()) {
            if (!it2.hasNext()) {
                return false;
            }
            final TreeNode t1 = it1.next();
            final TreeNode t2 = it2.next();
            return (t1 == t2) || (t1 != null && t1.deepEquals(t2));
        }
        return !it2.hasNext();
    }

    /**
     * Returns a string representation of this node as a tree. This is for debugging purpose only.
     */
    public final String toTree() {
        return OptionalDependencies.toString(this);
    }

    /**
     * Returns the string representation of this node. This string should holds in a single line
     * since it may be displayed in a {@link javax.swing.JTree}. The content may change in any
     * future version. It is provided mostly for debugging purpose.
     */
    @Override
    public final String toString() {
        // Don't invoke assertValid() directly or indirectly since it could cause never-ending loop.
        String text;
        if (tile != null) {
            text = tile.toString();
        } else {
            text = super.toString();
            text = text.substring(text.lastIndexOf('.') + 1);
        }
        if (!isLeaf()) {
            final StringBuilder buffer = new StringBuilder(text).
                    append(" (").append(getChildCount()).append(" childs");
            if (hasOverlaps()) {
                buffer.append(", overlaps");
            }
            text = buffer.append(')').toString();
        }
        return text;
    }

    /**
     * Invoked in assertion for checking the validity of the whole tree. Checks in this method body
     * are performed inconditionnaly though explicit {@code if ... throw new AssertionError(...)}
     * statements rather than {@code assert} statements because the whole methods is expected to be
     * invoked in an {@code assert} statement.
     *
     * @return {@code true} on success.
     */
    boolean checkValidity() {
        TreeNode child = firstChildren;
        if (child != null) {
            if (child.previousSibling != null) {
                throw new AssertionError(this);
            }
            if (isLeaf() || !getAllowsChildren()) {
                throw new AssertionError(this);
            }
            for (int index=0; ; index++) {
                if (child.parent != this) {
                    throw new AssertionError(child);
                }
                if (getIndex(child) != index) {
                    throw new AssertionError(child);
                }
                if (!child.checkValidity()) {
                    return false;
                }
                final TreeNode next = child.nextSibling;
                if (next == null) {
                    break;
                }
                if (next.previousSibling != child) {
                    throw new AssertionError(child);
                }
                if (!contains(child) && width >= 0 && height >= 0) {
                    throw new AssertionError(child);
                }
                child = next;
            }
        }
        if (child != lastChildren) {
            throw new AssertionError(this);
        }
        if (tile != null) {
            if (isEmpty()) {
                throw new AssertionError(this);
            }
            final Rectangle bounds;
            try {
                bounds = tile.getAbsoluteRegion();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            if (!bounds.contains(this)) {
                throw new AssertionError(this);
            }
        }
        return true;
    }
}
