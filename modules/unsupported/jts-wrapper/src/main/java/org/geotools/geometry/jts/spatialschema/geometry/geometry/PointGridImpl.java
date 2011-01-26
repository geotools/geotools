/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/PointGridImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// J2SE direct dependencies
import java.util.ArrayList;
import java.util.List;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.PointGrid;
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;


/**
 * A grid of points. The grid may be see as a sequences of equal length sequences.
 *  
 * @UML datatype GM_PointGrid
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 * @source $URL$
 * @version 2.0
 */
public class PointGridImpl implements PointGrid {
    
    //*************************************************************************
    //  Fields
    //*************************************************************************
    
    private PointArray[] pointGrid;
    
    //*************************************************************************
    //  Constructor
    //*************************************************************************
    
    //*************************************************************************
    //  implement the PointGrid interface
    //*************************************************************************

    /**
     * Returns the width of this grid. All {@linkplain PointArray point array}
     * in this grid must have this length.
     *
     * @return The grid width.
     * @see PointArray#size
     */
    public int width() {
        return pointGrid[0].length();
    }

    /**
     * Returns the length of this array. This is equivalent to
     * {@code getRows().size()}.
     *
     * @return The grid height.
     */
    public int height() {
        return pointGrid.length;
    }

    /**
     * Returns the point at the given index. This is equivalent to
     * {@code getRow(row).get(column)}.
     *
     * @param  row The row index from 0 inclusive to {@link #height} exclusive.
     * @param  column The column index from 0 inclusive to {@link #width} exclusive.
     * @return The point at the given index.
     * @throws IndexOutOfBoundsException if an index is out of bounds.
     */
    public DirectPosition get(int row, int column) throws IndexOutOfBoundsException {
        return getInternal( row, column );
    }
    
    /**
     * Gets a copy of the {@code DirectPosition} at the particular location in this 
     * {@code PointGrid}. If the {@code dest} argument is non-null, that object
     * will be populated with the value from the array. In all cases, the position in insulated
     * from changes in the {@code PointArray}, and vice-versa. Consequently, the same
     * {@code DirectPosition} object can be reused for fetching many points from this grid.
     * Example:
     * <blockquote><pre>
     * &nbsp;DirectPosition position = null;
     * &nbsp;for (int j=0; j&lt;grid.height(); j++) {
     * &nbsp;    for (int i=0; i&lt;grid.width(); i++) {
     * &nbsp;        position = array.get(j, i, position);
     * &nbsp;        // Do some processing...
     * &nbsp;    }
     * &nbsp;}
     * </pre></blockquote>
     *
     * @param  row The row index from 0 inclusive to {@link #height} exclusive.
     * @param  column The column index from 0 inclusive to {@link #width} exclusive.
     * @param  dest An optionnaly pre-allocated direct position.
     * @return The {@code dest} argument, or a new object if {@code dest} was null.
     * @throws IndexOutOfBoundsException if an index is out of bounds.
     */
    public DirectPosition get(int row, int column, DirectPosition dest) throws IndexOutOfBoundsException {
        DirectPosition target = new DirectPositionImpl(getInternal( row, column ));
        
        if (dest == null || !dest.getCoordinateReferenceSystem().equals(target.getCoordinateReferenceSystem())){ 
            return target;
        }
        for (int i = 0; i < target.getDimension(); i++) {
            dest.setOrdinate(i, target.getOrdinate(i));
        }
        return dest;
    }
    /**
     * Used to replace removed PointArray.get( column ) method.
     * <p>
     * Please note all example code uses getTarget( row, col ).clone()
     * when returning a direct position to client code.
     * </p>
     * @param row
     * @param column
     * @return DirectPosition 
     */
     DirectPosition getInternal( int row, int column ){
        PointArray pointArray = pointGrid[row];
        return (DirectPosition) pointArray.positions().get( column ); 
    }

    /**
     * Set the point at the given index. The point coordinates will be copied, i.e. changes
     * to the given {@code position} after this method call will not be reflected into
     * this point array. Consequently, the same {@code DirectPosition} object can be
     * reused for setting many points in this array.
     *
     * @param  row The row index from 0 inclusive to {@link #height} exclusive.
     * @param  column The column index from 0 inclusive to {@link #width} exclusive.
     * @param  position The point to set at the given location in this array.
     * @throws IndexOutOfBoundsException if an index is out of bounds.
     * @throws UnsupportedOperationException if this grid is immutable.
     */
    public void set(int row, int column, DirectPosition position) throws IndexOutOfBoundsException,
                                                                         UnsupportedOperationException {
        DirectPosition target = new DirectPositionImpl(getInternal(row, column));
        
        if (position.getCoordinateReferenceSystem().equals(target.getCoordinateReferenceSystem())) {
            for (int i = 0; i < position.getDimension(); i++) {
                target.setOrdinate(i, position.getOrdinate(i));
            }
        }
    }
    /**
     * Returns the row at the given index.
     *
     * @param  row The index from 0 inclusive to {@link #height} exclusive.
     * @return The row at the given index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public PointArray getRow(int row) throws IndexOutOfBoundsException {
        return pointGrid[row];
    }

    /**
     * Returns all rows in this array.
     *
     * @return The rows in this array.
     * @UML mandatory row
     *
     * @revisit Should changes in this list be forwarded to the underlying {@code PointGrid}?
     *          In the mean time, it is probably safe for implementor to make this list immutable.
     */
    public List/*<PointArray>*/ rows() {
        List returnable = new ArrayList(pointGrid.length);
        for (int i = 0; i < pointGrid.length; i++) {
            returnable.add(pointGrid[i]);
        }
        return returnable;
    }
}
