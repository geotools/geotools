/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;

import org.geotools.geometry.iso.index.ArrayListVisitor;
import org.geotools.geometry.iso.index.ItemVisitor;
import org.geotools.geometry.iso.index.SpatialIndex;
import org.geotools.geometry.iso.topograph2D.Envelope;

/**
 * A Quadtree is a spatial index structure for efficient querying of 2D
 * rectangles. If other kinds of spatial objects need to be indexed they can be
 * represented by their envelopes
 * <p>
 * The quadtree structure is used to provide a primary filter for range
 * rectangle queries. The query() method returns a list of all objects which
 * <i>may</i> intersect the query rectangle. Note that it may return objects
 * which do not in fact intersect. A secondary filter is required to test for
 * exact intersection. Of course, this secondary filter may consist of other
 * tests besides intersection, such as testing other kinds of spatial
 * relationships.
 * 
 * <p>
 * This implementation does not require specifying the extent of the inserted
 * items beforehand. It will automatically expand to accomodate any extent of
 * dataset.
 * <p>
 * This data structure is also known as an <i>MX-CIF quadtree</i> following the
 * usage of Samet and others.
 * 
 *
 *
 * @source $URL$
 */
public class Quadtree implements SpatialIndex {
	/**
	 * Ensure that the envelope for the inserted item has non-zero extents. Use
	 * the current minExtent to pad the envelope, if necessary
	 */
	public static Envelope ensureExtent(Envelope itemEnv, double minExtent) {
		// The names "ensureExtent" and "minExtent" are misleading -- sounds
		// like
		// this method ensures that the extents are greater than minExtent.
		// Perhaps we should rename them to "ensurePositiveExtent" and
		// "defaultExtent".
		// [Jon Aquino]
		double minx = itemEnv.getMinX();
		double maxx = itemEnv.getMaxX();
		double miny = itemEnv.getMinY();
		double maxy = itemEnv.getMaxY();
		// has a non-zero extent
		if (minx != maxx && miny != maxy)
			return itemEnv;

		// pad one or both extents
		if (minx == maxx) {
			minx = minx - minExtent / 2.0;
			maxx = minx + minExtent / 2.0;
		}
		if (miny == maxy) {
			miny = miny - minExtent / 2.0;
			maxy = miny + minExtent / 2.0;
		}
		return new Envelope(minx, maxx, miny, maxy);
	}

	private Root root;

	/**
	 * 
	 * minExtent is the minimum envelope extent of all items inserted into the
	 * tree so far. It is used as a heuristic value to construct non-zero
	 * envelopes for features with zero X and/or Y extent. Start with a non-zero
	 * extent, in case the first feature inserted has a zero extent in both
	 * directions. This value may be non-optimal, but only one feature will be
	 * inserted with this value.
	 */
	private double minExtent = 1.0;

	/**
	 * Constructs a Quadtree with zero items.
	 */
	public Quadtree() {
		root = new Root();
	}

	/**
	 * Returns the number of levels in the tree.
	 */
	public int depth() {
		// I don't think it's possible for root to be null. Perhaps we should
		// remove the check. [Jon Aquino]
		// Or make an assertion [Jon Aquino 10/29/2003]
		if (root != null)
			return root.depth();
		return 0;
	}

	/**
	 * Returns the number of items in the tree.
	 * 
	 * @return the number of items in the tree
	 */
	public int size() {
		if (root != null)
			return root.size();
		return 0;
	}

	public void insert(Envelope itemEnv, Object item) {
		collectStats(itemEnv);
		Envelope insertEnv = ensureExtent(itemEnv, minExtent);
		root.insert(insertEnv, item);
	}

	/**
	 * Removes a single item from the tree.
	 * 
	 * @param itemEnv
	 *            the Envelope of the item to remove
	 * @param item
	 *            the item to remove
	 * @return <code>true</code> if the item was found
	 */
	public boolean remove(Envelope itemEnv, Object item) {
		Envelope posEnv = ensureExtent(itemEnv, minExtent);
		return root.remove(posEnv, item);
	}

	/*
	 * public List OLDquery(Envelope searchEnv) { /** the items that are matched
	 * are the items in quads which overlap the search envelope
	 */
	/*
	 * List foundItems = new ArrayList();
	 * root.addAllItemsFromOverlapping(searchEnv, foundItems); return
	 * foundItems; }
	 */

	public List query(Envelope searchEnv) {
		/**
		 * the items that are matched are the items in quads which overlap the
		 * search envelope
		 */
		ArrayListVisitor visitor = new ArrayListVisitor();
		query(searchEnv, visitor);
		return visitor.getItems();
	}

	public void query(Envelope searchEnv, ItemVisitor visitor) {
		/**
		 * the items that are matched are the items in quads which overlap the
		 * search envelope
		 */
		root.visit(searchEnv, visitor);
	}

	/**
	 * Return a list of all items in the Quadtree
	 */
	public List queryAll() {
		List foundItems = new ArrayList();
		root.addAllItems(foundItems);
		return foundItems;
	}

	private void collectStats(Envelope itemEnv) {
		double delX = itemEnv.getWidth();
		if (delX < minExtent && delX > 0.0)
			minExtent = delX;

		double delY = itemEnv.getWidth();
		if (delY < minExtent && delY > 0.0)
			minExtent = delY;
	}

}
