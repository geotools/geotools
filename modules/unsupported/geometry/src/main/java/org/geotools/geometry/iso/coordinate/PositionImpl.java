/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.coordinate;

import java.io.Serializable;

import org.geotools.geometry.iso.primitive.PointImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Point;

/**
 * @author Jackson Roehrig & Sanjay Jena
 * 
 * The data type Position is a union type consisting of either a
 * DirectPosition2D or of a reference to a Point from which a DirectPosition2D
 * is obtained. The use of this data type allows the identification of a
 * position either directly as a coordinate (variant direct) or indirectly as a
 * reference to a Point (variant indirect). Position::direct [0,1] :
 * DirectPosition2D Position::indirect [0,1] : PointRef Position: {direct.isNull =
 * indirect.isNotNull}
 *
 * @source $URL$
 */

public class PositionImpl implements Position, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1588536548235183389L;
	// The Position is either represented by a DirectPosition or Point
	private Object position = null;

	/**
	 * Creates a new <code>Position</code> instance.
	 * 
	 * @param directPosition
	 *            an <code>DirectPosition2D</code> value
	 */
	public PositionImpl(final DirectPosition directPosition) {
		if (directPosition == null)
			throw new IllegalArgumentException("DirectPosition is null"); //$NON-NLS-1$
		this.position = directPosition;
	}

	/**
	 * Creates a new <code>Position</code> instance.
	 * 
	 * @param pointRef
	 *            an <code>PointRef</code> value
	 */
	public PositionImpl(final PointImpl pointRef) {
		if (pointRef == null)
			throw new IllegalArgumentException("PointRef not passed"); //$NON-NLS-1$
		this.position = pointRef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Position#getPosition()
	 */
        @Deprecated
	public DirectPosition getPosition() {
		// ok
		return (DirectPosition) this.position;
		//return (this.position instanceof DirectPositionImpl) ? (DirectPositionImpl) this.position : ((PointImpl) this.position).getPosition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Position#getDirectPosition()
	 */
	public DirectPosition getDirectPosition() {
		// ok
		return (DirectPosition) this.position;
		//return (this.position instanceof DirectPositionImpl) ? (DirectPositionImpl) this.position : ((PointImpl) this.position).getPosition();
	}

	/**
	 * Returns true, if the Position is representated as a PointReference.
	 * Returns false, if the Position is representated as a DirectPoint.
	 * 
	 * @return true if the Position is representated as a PointReference
	 */
	public boolean hasPoint() {
		return (this.position instanceof PointImpl);
	}

	/**
	 * Returns a Point or null
	 * 
	 * @return the Point if the position is of type Point. If position is an
	 *         instance of DirectPositionImpl, return a new Point if force is
	 *         true and null is force is false
	 */
	public PointImpl getPoint() {
		return this.hasPoint() ? (PointImpl) this.position : null;
	}

	/**
	 * @param position
	 *            The position to set.
	 */
	public void setDirectPosition(DirectPositionImpl position) {
		this.position = position;
	}

	/**
	 * Returns the coordinate dimension of the position
	 * 
	 * @return dimension
	 */
	public int getCoordinateDimension() {
		return this.hasPoint() ? ((PointImpl) this.position)
				.getCoordinateDimension()
				: ((DirectPositionImpl) this.position).getDimension();
	}

	public String toString() {
		return "[GM_Position: " + this.getPosition() + "]";
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// compare objs based on their type
		if (position instanceof DirectPosition || position instanceof DirectPositionImpl) {
			DirectPositionImpl dp = (DirectPositionImpl) position;
			return dp.equals(obj);
		}
		else if (position instanceof Point || position instanceof PointImpl) {
			PointImpl point = (PointImpl) position;
			return point.equals(obj);
		}
		else {
			return position.equals(obj);
		}
	}

}
