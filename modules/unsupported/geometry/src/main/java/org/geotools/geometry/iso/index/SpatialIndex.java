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
package org.geotools.geometry.iso.index;

import java.util.List;

import org.geotools.geometry.iso.topograph2D.Envelope;

/**
 * The basic operations supported by classes implementing spatial index
 * algorithms.
 * <p>
 * A spatial index typically provides a primary filter for range rectangle
 * queries. A secondary filter is required to test for exact intersection. The
 * secondary filter may consist of other kinds of tests, such as testing other
 * spatial relationships.
 * 
 *
 *
 * @source $URL$
 */
public interface SpatialIndex {
	/**
	 * Adds a spatial item with an extent specified by the given
	 * {@link Envelope} to the index
	 */
	void insert(Envelope itemEnv, Object item);

	/**
	 * Queries the index for all items whose extents intersect the given search
	 * {@link Envelope} Note that some kinds of indexes may also return objects
	 * which do not in fact intersect the query envelope.
	 * 
	 * @param searchEnv
	 *            the envelope to query for
	 * @return a list of the items found by the query
	 */
	List query(Envelope searchEnv);

	/**
	 * Queries the index for all items whose extents intersect the given search
	 * {@link Envelope}, and applies an {@link ItemVisitor} to them. Note that
	 * some kinds of indexes may also return objects which do not in fact
	 * intersect the query envelope.
	 * 
	 * @param searchEnv
	 *            the envelope to query for
	 * @param visitor
	 *            a visitor object to apply to the items found
	 */
	void query(Envelope searchEnv, ItemVisitor visitor);

	/**
	 * Removes a single item from the tree.
	 * 
	 * @param itemEnv
	 *            the Envelope of the item to remove
	 * @param item
	 *            the item to remove
	 * @return <code>true</code> if the item was found
	 */
	boolean remove(Envelope itemEnv, Object item);

}
