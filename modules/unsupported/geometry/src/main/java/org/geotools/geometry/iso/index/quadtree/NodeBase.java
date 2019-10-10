/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.index.quadtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.geometry.iso.index.ItemVisitor;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Envelope;

/**
 * The base class for nodes in a {@link Quadtree}.
 *
 * @version 1.7.2
 */
public abstract class NodeBase {

    // DEBUG private static int itemCount = 0; // debugging
    /**
     * Returns the index of the subquad that wholly contains the given envelope. If none does,
     * returns -1.
     */
    public static int getSubnodeIndex(Envelope env, Coordinate centre) {
        int subnodeIndex = -1;
        if (env.getMinX() >= centre.x) {
            if (env.getMinY() >= centre.y) subnodeIndex = 3;
            if (env.getMaxY() <= centre.y) subnodeIndex = 1;
        }
        if (env.getMaxX() <= centre.x) {
            if (env.getMinY() >= centre.y) subnodeIndex = 2;
            if (env.getMaxY() <= centre.y) subnodeIndex = 0;
        }
        return subnodeIndex;
    }

    protected List items = new ArrayList();

    /**
     * subquads are numbered as follows:
     *
     * <pre>
     *   2 | 3
     *   --+--
     *   0 | 1
     * </pre>
     */
    protected Node[] subnode = new Node[4];

    public NodeBase() {}

    public List getItems() {
        return items;
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void add(Object item) {
        items.add(item);
        // DEBUG itemCount++;
        // DEBUG System.out.print(itemCount);
    }

    /**
     * Removes a single item from this subtree.
     *
     * @param itemEnv the envelope containing the item
     * @param item the item to remove
     * @return <code>true</code> if the item was found and removed
     */
    public boolean remove(Envelope itemEnv, Object item) {
        // use envelope to restrict nodes scanned
        if (!isSearchMatch(itemEnv)) return false;

        boolean found = false;
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                found = subnode[i].remove(itemEnv, item);
                if (found) {
                    // trim subtree if empty
                    if (subnode[i].isPrunable()) subnode[i] = null;
                    break;
                }
            }
        }
        // if item was found lower down, don't need to search for it here
        if (found) return found;
        // otherwise, try and remove the item from the list of items in this
        // node
        found = items.remove(item);
        return found;
    }

    public boolean isPrunable() {
        return !(hasChildren() || hasItems());
    }

    public boolean hasChildren() {
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        boolean isEmpty = true;
        if (!items.isEmpty()) isEmpty = false;
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                if (!subnode[i].isEmpty()) isEmpty = false;
            }
        }
        return isEmpty;
    }

    // <<TODO:RENAME?>> Sounds like this method adds resultItems to items
    // (like List#addAll). Perhaps it should be renamed to "addAllItemsTo" [Jon
    // Aquino]
    public List addAllItems(List resultItems) {
        // this node may have items as well as subnodes (since items may not
        // be wholely contained in any single subnode
        resultItems.addAll(this.items);
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                subnode[i].addAllItems(resultItems);
            }
        }
        return resultItems;
    }

    protected abstract boolean isSearchMatch(Envelope searchEnv);

    public void addAllItemsFromOverlapping(Envelope searchEnv, List resultItems) {
        if (!isSearchMatch(searchEnv)) return;

        // this node may have items as well as subnodes (since items may not
        // be wholely contained in any single subnode
        resultItems.addAll(items);

        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                subnode[i].addAllItemsFromOverlapping(searchEnv, resultItems);
            }
        }
    }

    public void visit(Envelope searchEnv, ItemVisitor visitor) {
        if (!isSearchMatch(searchEnv)) return;

        // this node may have items as well as subnodes (since items may not
        // be wholely contained in any single subnode
        visitItems(searchEnv, visitor);

        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                subnode[i].visit(searchEnv, visitor);
            }
        }
    }

    private void visitItems(Envelope searchEnv, ItemVisitor visitor) {
        // would be nice to filter items based on search envelope, but can't
        // until they contain an envelope
        for (Iterator i = items.iterator(); i.hasNext(); ) {
            visitor.visitItem(i.next());
        }
    }

    // <<TODO:RENAME?>> In Samet's terminology, I think what we're returning
    // here is
    // actually level+1 rather than depth. (See p. 4 of his book) [Jon Aquino]
    int depth() {
        int maxSubDepth = 0;
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                int sqd = subnode[i].depth();
                if (sqd > maxSubDepth) maxSubDepth = sqd;
            }
        }
        return maxSubDepth + 1;
    }

    int size() {
        int subSize = 0;
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                subSize += subnode[i].size();
            }
        }
        return subSize + items.size();
    }

    int getNodeCount() {
        int subSize = 0;
        for (int i = 0; i < 4; i++) {
            if (subnode[i] != null) {
                subSize += subnode[i].size();
            }
        }
        return subSize + 1;
    }
}
