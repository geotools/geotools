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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.util.DoubleOperation;
import org.geotools.geometry.iso.util.algorithmND.AlgoPointND;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Many of the geometric constructs in this International Standard require the
 * use of reference points which are organized into sequences or grids
 * (sequences of equal length sequences). PointArray::column[1..n] : Position
 * PointGrid::row[1..n] : PointArray
 * 
 * The class name follows the ISO19107. It is a confusing name, since it
 * contains an array of positions and not of points. The positions themselves
 * contain either a direct position or a point.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 * @source $URL$
 */

public class PointArrayImpl extends ArrayList<Position> implements PointArray {
    CoordinateReferenceSystem crs;
    
    /**
     * Please add content; according to ISO 19117 an empty PointArray
     * cannot exist.
     * @param crs
     */
    public PointArrayImpl(CoordinateReferenceSystem crs ) {
        this.crs = crs;
    }
    public PointArrayImpl( DirectPosition p1, DirectPosition p2 ){
        crs = p1.getCoordinateReferenceSystem();
        add( p1 );
        add( p2 );
    }
	/**
	 * Creates a new PointArray based on another PointArray. This constructor
	 * creates new Position objects.
	 * 
	 * @param aPointArray
	 */
	public PointArrayImpl(PointArray aPointArray) {
		if (aPointArray.isEmpty()){
			throw new IllegalArgumentException("Parameter PointArray is empty. Cannot create empty PointArray as we need the CRS");
        }
		// Position data will be cloned here
		
		//this.column = new ArrayList<PositionImpl>();
		//this.column = this.getFeatGeomFactory().getListFactory().getPositionList();

//		int coordDim = aPointArray.getFirst().getCoordinateDimension();
//		CoordinateFactoryImpl coordFactory = FeatGeomFactoryImpl
//				.getDefaultCoordinateFactory(coordDim);

		// TODO JR: Zur kenntnisnahme:
		// Wie in deinem Vorschlag von unserem Telefonat am 04/10 hole ich die CoordFactory über ein DP
        
		//CoordinateFactoryImpl coordFactory = this.getFeatGeomFactory().getCoordinateFactory();		
		for (int i = 0; i < aPointArray.size(); i++) {
            Position copy = new PositionImpl( aPointArray.getDirectPosition(i, null) );
			add( copy );
		}
        crs = getPosition(0).getPosition().getCoordinateReferenceSystem();
	}

    
	/**
	 * Construct a new PointArray. This constructor does not create new position
	 * objects.
	 * 
	 * @param positions
	 * 
	 */
	public PointArrayImpl(List<Position> positions) {
        super( positions );
		if (positions.size() == 0){
			throw new IllegalArgumentException("Parameter positions is empty. Cannot create empty PointArray as we need the CRS");
        }
        crs = getPosition(0).getPosition().getCoordinateReferenceSystem();
	}
	
	
	// TODO JR: Zur kenntnisnahme:
	// Wie in deinem Vorschlag von unserem Telefonat am 04/10 hole ich die CoordFactory über ein DP
	/**
	 * Returns the Feature Geometry Factory Instance based on the reference of the DirectPositions of this PointArray
	 * @return Factory instance
	 *
	private FeatGeomFactoryImpl getFeatGeomFactory() {
		if (this.isEmpty()) {
			return null;
		}
		DirectPositionImpl tDP = get(0).getPosition();
		return tDP.getGeometryFactory();		
	}*/
	
	/**
	 * Returns the Point array as Set of Position
	 * 
	 * @return the positions
	 */
	public List<Position> getPointArray() {
		return this;
	}

	/**
	 * Returns the coordiantes of the Position at index
	 * @param arg0
	 * @return double[]
	 */
	public double[] getCoordinate(int index) {
		// test ok
		
		Position pos = getPosition(index);
		return pos.getPosition().getCoordinates();

		// Auskommentiert und geändert durch Sanjay am 21.08.2006
		// der komplette code hat nicht soviel sinn gemacht, wurde nicht getestet
		// Position nicht berücksichtigt wurde
		// OLD CODE:
		// return (obj instanceof PointImpl)
		// (PointImpl)obj).getPosition().getCoordinates();
		// ((PointImpl)obj).getPosition().getCoordinates():
		// (double[])obj;
	}

	/**
	 * Gets the position at index
	 * 
	 * @param arg0
	 * @return PositionImpl
	 */
	public Position getPosition(int index) {
		// test ok
		return get(index);
	}

	/**
	 * Returns the first element
	 * 
	 * @return Position
	 */
	public Position getFirst() {
		// ok
		return get(0);
	}

	/**
	 * Returns the last element
	 * 
	 * @return Position
	 */
	public Position getLast() {
		// ok        
        return get(size() - 1);
	}

//	/**
//	 * @param index
//	 * @param position
//	 */
//	public void setDirectPosition(int index, PositionImpl position) {
//		assert ((index < this.column.size()) && (position != null));
//		this.column.set(index, position);
//	}

//	/**
//	 * @param positions
//	 * @param startPosition
//	 */
//	public void set(List<PositionImpl> positions, int startPosition) {
//		this.set(positions, startPosition, positions.size());
//	}
//
//	/**
//	 * @param positions
//	 * @param startPosition
//	 * @param count
//	 */
//	private void set(List<PositionImpl> positions, int startPosition, int count) {
//
//		assert (startPosition >= 0);
//		if ((startPosition + count) > positions.size())
//			count = (positions.size() - startPosition);
//		for (int i = 0; i < count; ++i) {
//			this.column.add(positions.get(i + startPosition));
//		}
//	}
	
	
//	/**
//	 * Extends the point array with a list of positions
//	 * 
//	 * @param positions to be added at the end of the point array
//	 */
//	public void addLast(List<PositionImpl> positions) {
//		this.column.addAll(positions);
//	}
//	
//	/**
//	 * Inserts a list of positions in beginning of the point array
//	 * 
//	 * @param positions to be added in the start of the point array
//	 */
//	public void addFirst(List<PositionImpl> positions) {
//		LinkedList newColumn = new LinkedList(positions);
//		newColumn.addAll(this.column);
//		this.column = newColumn;
//	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.PointArray#length()
	 */
	public int length() {
		// Implementation OK
		return size();
	}

//	/**
//	 * 
//	 */
//	public void reverse() {
//		Collections.reverse(this.column);
//	}

	/**
	 * Creates the absolute length over all points in point array
	 * 
	 * @return absolute length over all points in point array
	 */
	public double getDistanceSum() {
		// Test OK - Methode korrigiert
		double dist = 0.0;
		double[] c0 = this.getCoordinate(0);
		for (int i = 1; i < this.length(); i++) {
			double[] c1 = this.getCoordinate(i);
			dist = DoubleOperation.add(dist, AlgoPointND.getDistance(c0, c1));
			//dist += AlgoPointND.getDistanceSquare(c0, c1);
			c0 = c1;
		}
		//return Math.sqrt(dist);
		return dist;
	}

	/**
	 * Creates an envelope for all points in point array
	 * 
	 * @return envelope for all points in point array
	 */
	public EnvelopeImpl getEnvelope() {
        Position position = getPosition( 0 );        
		EnvelopeImpl env = new EnvelopeImpl( position );
		
		for (int i = 1, n = length(); i < n; i++) {
			double[] c1 = getCoordinate(i);
			env.expand(c1);
		}
		return env;
	}

	/**
	 * Removes the first occurrence of this position from the PointArray
	 * @param p
	 * @return boolean TRUE, if the Remove was successful
	 */
	public boolean removePosition(Position p) {
		// test ok
		return remove(p);
	}

	public String toString() {
		String rString = ""; //$NON-NLS-1$
		for (int i = 0; i < size(); i++) {
			rString += get(i) + ", ";
		}
		return rString;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.PointArray#getCoordinateReferenceSystem()
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.PointArray#get(int, org.opengis.geometry.coordinate.DirectPosition)
	 */
	public DirectPosition getPosition(int col, DirectPosition dest)
			throws IndexOutOfBoundsException {
		// Test ok (SJ)
		Position pos = get(col);

		double[] coords = pos.getPosition().getCoordinates();		
		if (dest != null) {
            if( dest instanceof DirectPositionImpl ){
                DirectPositionImpl fast = (DirectPositionImpl) dest;
                // Set coordinates in existing DP
                fast.setCoordinate(coords);    
            }
            else {
                for( int i=0; i<coords.length; i++ ){
                    dest.setOrdinate( i, coords[i] );
                }
            }			
		} else {
            dest = pos.getPosition();			
		}		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.PointArray#set(int, org.opengis.geometry.coordinate.DirectPosition)
	 */
	public void setPosition(int index, DirectPosition position)
			throws IndexOutOfBoundsException, UnsupportedOperationException {
		// Test ok
		// Set copy of the coordinates of the given DirectPosition
        Position pos = get(index);
        DirectPosition inPlace = pos.getPosition();
        double[] coord = position.getCoordinates();
        for( int i=0; i<coord.length; i++){
            inPlace.setOrdinate( i, coord[i] );
        }
	}

	/**
	 * Sets the Coordinates of the Position at index in the PointArray
	 * @param index
	 * @param coord
	 */
	public void set(int index, double[] coord) {
		// TODO test
		// Manipulate the coordinates at the Position entry at the index
		Position pos = get(index);
        DirectPosition position = pos.getPosition();
        for( int i=0; i<coord.length; i++){
            position.setOrdinate( i, coord[i] );
        }
	}


	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.PointArray#positions()
	 */
	public List positions() {
		// Test ok
		return this;
	}

	/**
	 * @param minSpacing
	 */
	public void merge(double minSpacing) {
		// TODO Test
		// TODO Documentation
		minSpacing *= minSpacing;
		double[] c0 = getCoordinate(0);
		for (int i = 1, n = length(); i < n; i++) {
			double[] c1 = getCoordinate(i);
			while (AlgoPointND.getDistanceSquare(c0, c1) < minSpacing) {
				this.remove(i);
				n--;
				c1 = getCoordinate(i);
			}
			c0 = c1;
		}
	}

	/**
	 * This method splits the sequence of positions according to a maximum
	 * distance. After splitting the distance between two positions will be
	 * maxSpacing or less. The length and shape of the LineString will not be
	 * changed.
	 * 
	 * @param maxSpacing
	 */
	public void split(double maxSpacing) {
		// TODO Test
		// TODO Documentation
		// gdavis: this looping structure seems broken, and I'm not 
		// sure what it's meant to do... can't this be re-written in
		// a readable way?

		double[] c0 = getCoordinate(0);
		for (int i = 1, n = length(); i < n; i++) {
			double[] c1 = getCoordinate(i);
			double[][] newCoords = AlgoPointND.split(c0, c1, maxSpacing);
			if (newCoords != null) {
				for (int j = 0; i < newCoords.length; j++, i++, n++) {
					this.set(i, newCoords[j]);
				}
			}
			c0 = c1;
		}
	}

	/**
	 * Creates a LineSegment connecting the Positins p0 and p1, whereas p0 is
	 * the position at arg0 and p1 is the position at (arg0+1). If (arg0+1) ==
	 * size() then returns the LineSegment connecting the last position with the
	 * first one
	 * 
	 * @return List<LineSegmentImpl>
	 */
	public List<? extends LineSegment> getLineSegments(CurveImpl parentCurve) {
		// test ok (SJ)
		return new LineSegmentsSequence(this, parentCurve);
	}
	
	/**
	 * Class to support on-the-fly generation of LineSegments
	 * 
	 * @author roehrig
	 * 
	 */
	public class LineSegmentsSequence extends AbstractList<LineSegmentImpl> {

		private PointArrayImpl pointArray;
		private int index;
		private double length;		
		private CurveImpl parentCurve = null;

		/**
		 * Create a Line Segment sequence by a pointarray and a parent curve
		 * 
		 * @param pointArray
		 * @param aParentCurve
		 */
		public LineSegmentsSequence(PointArrayImpl pointArray, CurveImpl aParentCurve) {
			this.pointArray = pointArray;
			this.index = 0;
			this.length = 0.0;
			this.parentCurve = aParentCurve;
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractList#get(int)
		 */
		public LineSegmentImpl get(int arg0) {
			double[] p0 = this.getStartCoordinate(arg0);
			double[] p1 = this.getEndCoordinate(arg0);

			// Calculate start param for this line segment
			double startParam = 0.0;
			for (int i=1; i<=arg0; i++) {
				startParam = DoubleOperation.add(
                        startParam,
                        AlgoPointND.getDistance(this.getStartCoordinate(i-1), this.getEndCoordinate(i-1))
				);
				//startParam += AlgoPointND.getDistance(this.getStartCoordinate(i-1), this.getEndCoordinate(i-1));
			}            
			LineSegmentImpl rSeg = new LineSegmentImpl( pointArray.getCoordinateReferenceSystem(), p0, p1, startParam );            
			rSeg.setCurve(this.parentCurve);

			return rSeg;
		}

		/**
		 * @param arg0
		 * @param dp
		 * @return DirectPosition
		 */
		public DirectPosition getStartDirectPositionCoordinate(int arg0,
				DirectPosition dp) {
			return this.pointArray.getPosition(arg0, dp);
		}

		/**
		 * @param arg0
		 * @param dp
		 * @return DirectPositionImpl
		 */
		public DirectPosition getEndDirectPositionCoordinate(int arg0,
				DirectPosition dp) {
			return this.pointArray.getPosition(arg0 + 1, dp);
		}

		/**
		 * @param arg0
		 * @return double[]
		 */
		public double[] getStartCoordinate(int arg0) {
			return this.pointArray.getCoordinate(arg0);
		}

		/**
		 * @param arg0
		 * @return double[]
		 */
		public double[] getEndCoordinate(int arg0) {
			return this.pointArray.getCoordinate(arg0 + 1);
		}

		public int size() {
			return this.pointArray.length() - 1;
		}

		/**
		 * @return boolean
		 */
		public boolean hasNext() {
			return this.index < (this.pointArray.length() - 2);
		}

		/**
		 * @return LineSegmentImpl
		 */
		public LineSegmentImpl next() {
			double[] p0 = this.getStartCoordinate(this.index);
			double[] p1 = this.getEndCoordinate(this.index);

//			LineSegmentImpl ls = FeatGeomFactoryImpl
//					.getDefaultCoordinateFactory(p0.length).createLineSegment(
//							p0, p1, this.length);
			LineSegmentImpl ls = new LineSegmentImpl( pointArray.getCoordinateReferenceSystem(), p0, p1, this.length);

			this.length = DoubleOperation.add(this.length, AlgoPointND.getDistance(p0, p1));
			//this.length += AlgoPointND.getDistance(p0, p1);
			
			return ls;
		}

	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.PointArray#getDimension()
	 */
	public int getDimension() {
		return 0;
		//return crs.getCoordinateSystem().getDimension();
	}
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((crs == null) ? 0 : crs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == this)
		    return true;
		if (!(obj instanceof List))
		    return false;
		
		ListIterator<Position> e1 = listIterator();
		ListIterator e2 = ((List) obj).listIterator();
		while(e1.hasNext() && e2.hasNext()) {
		    Position o1 = e1.next();
		    Object o2 = e2.next();
		    if (!(o1==null ? o2==null : o1.equals(o2)))
			return false;
		}
		if (!(!(e1.hasNext() || e2.hasNext())))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PointArrayImpl other = (PointArrayImpl) obj;
		if (crs == null) {
			if (other.crs != null)
				return false;
		} else if (!crs.equals(other.crs))
			return false;
		return true;
	}
	public DirectPosition getDirectPosition(int index, DirectPosition dest) throws IndexOutOfBoundsException {
		if (dest == null) {
				dest = new DirectPositionImpl(get(index));
		}
		else {
			assert(dest.getCoordinateReferenceSystem().equals(crs));
			DirectPosition dp = new DirectPositionImpl(get(index));
			for (int i=0; i < dp.getCoordinates().length; i++) {
				dest.setOrdinate(i, dp.getOrdinate(i));
			}
		}
		return dest;
	}
	public void setDirectPosition(int index, DirectPosition position) throws IndexOutOfBoundsException, UnsupportedOperationException {
		this.setPosition(index, position);
		
	}




}
