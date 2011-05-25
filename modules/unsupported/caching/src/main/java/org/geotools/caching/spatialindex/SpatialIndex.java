// Spatial Index Library
//
// Copyright (C) 2002  Navel Ltd.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation;
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Contact information:
//  Mailing address:
//    Marios Hadjieleftheriou
//    University of California, Riverside
//    Department of Computer Science
//    Surge Building, Room 310
//    Riverside, CA 92521
//
//  Email:
//    marioh@cs.ucr.edu
package org.geotools.caching.spatialindex;

import java.util.Properties;


/** 
 * A generic contract for spatial indexes, such as quadtrees or r-trees.
 * Provides methods to insert, delete and query the index.
 * Note that implementations may be n-dimensional.
 *
 * @author Marios Hadjieleftheriou, marioh@cs.ucr.edu
 * @copyright Copyright (C) 2002  Navel Ltd.
 * Modified by Christophe Rousson
 * Modified by Emily Gouge
 *
 *
 * @source $URL$
 */
public interface SpatialIndex {
    public static final String INDEX_TYPE_PROPERTY = "SpatialIndex.Type";

    /**
     * This constant is used to check if two doubles are nearly equal.
     * Copied from original code by Marios Hadjieleftheriou.
     */
    public static final double EPSILON = 1.192092896e-07;

    /** Empty the index.
     * @throws IllegalStateException
     */
    public void clear() throws IllegalStateException;

    /** Insert new data in the index.
     *
     * @param data to insert
     * @param a n-dims shape
     */
    public void insertData(final Object data, final Shape shape);
    
    /** Traverse index to match data such as :
     *  <code>query.contains(Data.getShape())</code>
     *
     * @param query, a n-dims shape
     * @param visitor implementing visit() callback method
     */
    public void containmentQuery(final Shape query, final Visitor v);

    /** Traverse index to match data such as :
     *  <code>query.intersects(Data.getShape())</code>
     *
     * @param query, a n-dims shape
     * @param visitor implementing visit() callback method
     */
    public void intersectionQuery(final Shape query, final Visitor v);

    /** Traverse index to match data having query falling inside its shape, ie :
     * <code>Data.getShape().contains(query)</code>
     *
     * @param query, a n-dims point
     * @param visitor implementing visit() callback method
     */
    public void pointLocationQuery(final Point query, final Visitor v);

    /**
     * @param k
     * @param query
     * @param v
     * @param nnc
     */
    public void nearestNeighborQuery(int k, final Shape query, final Visitor v,
        NearestNeighborComparator nnc);

    /**
     * @param k
     * @param query
     * @param v
     */
    public void nearestNeighborQuery(int k, final Shape query, final Visitor v);


    /**
     * @return
     */
    public Properties getIndexProperties();

    /** Implementations may always return true.
     *
     * @return true if index is valid.
     *
     * TODO: define what is a valid index.
     */
    public boolean isIndexValid();

    /**
     * @return statistics about the index.
     */
    public Statistics getStatistics();

    /** Cause pending write operations to happen immediately.
     * Use this method to persist the index before disposal.
     *
     */
    public void flush();
    
    /**
     * Initializes the spatial index from 
     * a storage instance.
     * <p>This allows caches to be saved to storage
     * and reused.</p>
     * 
     * @param storage
     */
    public void initializeFromStorage(Storage storage);
}
