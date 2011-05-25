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
import java.util.Arrays;

import org.geotools.geometry.iso.util.DoubleOperation;
import org.geotools.geometry.iso.util.algorithmND.AlgoPointND;
import org.geotools.referencing.CRS;
import org.opengis.util.Cloneable;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Jackson Roehrig & Sanjay Jena
 * 
 *
 *
 * @source $URL$
 */
public class DirectPositionImpl implements DirectPosition, Cloneable, Serializable {
    private static final long serialVersionUID = 2327211794986364062L;

    /**
	 * The attribute "coordinate" is a sequence of Numbers that hold the
	 * coordinate of this position in the specified reference system.
	 * DirectPosition2D::coordinate : Sequence<Number>
	 * 
	 * In order that this system will be based on an euclidic 2D system, the
	 * coordinates will be stored in explicit variables for each X- and
	 * Y-Coordinate
	 */
	private double[] coordinate;

    /**
     * Coordinate Reference System used to determine meaning of above coordinates
     */
	private CoordinateReferenceSystem crs;
    
    public DirectPositionImpl( CoordinateReferenceSystem crs ){
		final int N = crs.getCoordinateSystem().getDimension();
		this.crs = crs;        
		this.coordinate = new double[N];
		for (int i = 0; i < N; ++i){
			this.coordinate[i] = Double.NaN;
        }
	}

	/**
	 * Creates a direct Position by using coordinates of another direct Position
	 * @param crs
	 * @param coord
	 */
	public DirectPositionImpl(CoordinateReferenceSystem crs, double[] coord) {
		this.crs = crs;
		assert (coord.length == crs.getCoordinateSystem().getDimension());
		this.coordinate = coord;
	}

    public DirectPositionImpl( Position position ){
        this( position.getPosition() );
    }
	/**
	 * Creates a direct Position by using coordinates of another direct Position
	 * 
	 * @param position
	 */
	public DirectPositionImpl(final DirectPosition position) {
		this.crs = position.getCoordinateReferenceSystem();
		// Comment by Sanjay
		// VORSICHT: Die folgende Codezeile verursachte, dass das selbe Objekt
		// (double Array) verwendet wurde; folglich wurde z.B. beim
		// Envelope Min und Max Position auf die selben Koordinaten zugegriffen.
		// this.coordinate=p.getCoordinate();
		// Bitte um kenntnisnahme und berücksichtigung in sourcen: Arrays müssen
		// explizit kopiert werden, nur elementare Datentypen werden automatisch
		// von Java neu erzeugt, alles andere sind nur Referenzen
		// TODO Das Klonen sollte in die Factory verlagert werden
        
        // the above seems to say that the array must be explicitly copied
        // but the direct position javadocs say that getCoordinate produces
        // a copy ... so we should be good without the clone.
		this.coordinate = position.getCoordinates(); //.clone()
	}

	/**
	 * @param crs
	 * @param x
	 * @param y
	 * @param z
	 */
	public DirectPositionImpl(CoordinateReferenceSystem crs, double x, double y,
			double z) {
		this.crs = crs;
		assert (3 == crs.getCoordinateSystem().getDimension());
		this.coordinate = new double[] { x, y, z };
	}

	/**
	 * @param crs
	 * @param x
	 * @param y
	 * @param z
	 * @param m
	 */
	public DirectPositionImpl( CoordinateReferenceSystem crs, double x, double y,
			double z, double m) {
		this.crs = crs;
		assert (5 == crs.getCoordinateSystem().getDimension());
		this.coordinate = new double[] { x, y, z, m };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.DirectPosition#getDimension()
	 */
	public int getDimension() {
		return crs.getCoordinateSystem().getDimension();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.DirectPosition#getCoordinate()
	 */
	public double[] getCoordinate() {
		return (double[]) this.coordinate.clone(); // JG: modified to return clone each time
	}

        @Deprecated
	public double[] getCoordinates() {
            return getCoordinate();
        }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.DirectPosition#getOrdinate(int)
	 */
	public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
		return this.coordinate[dimension];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.DirectPosition#setOrdinate(int,
	 *      double)
	 */
	public void setOrdinate(int dimension, double value)
			throws IndexOutOfBoundsException {
		// TODO semantic JR
		// TODO documentation
		if (dimension >= this.coordinate.length || dimension < 0){
			throw new IndexOutOfBoundsException("Index "+dimension+" out of coordinate range (max "+ coordinate.length+")"); //$NON-NLS-1$
        }
		this.coordinate[dimension] = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.DirectPosition#getCoordinateReferenceSystem()
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		// TODO semantic JR
		// TODO implementation Is the CRS correct/existent?
		// TODO test
		// TODO documentation
		return crs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.DirectPosition#clone()
	 */
	public DirectPositionImpl clone() {
		// Cloning the double array (in parameter) is important!
		// Return new DirectPosition by cloning the Coordiante array of double which define the position
		return new DirectPositionImpl( crs, coordinate.clone() );
	}

	/**
	 * @param coord
	 */
	public void setCoordinate(double[] coord) {
		if (coord.length != this.getDimension()){
			throw new IllegalArgumentException("Index "+coord.length+" out of coordinate range (expected "+getDimension()+")"); //$NON-NLS-1$
        }
		this.coordinate = coord;
	}

	/**
	 * Returns the x value of the coordinate represented by this DirectPosition
	 * @return x
	 */
	public double getX() {
		return this.coordinate[0];
	}

	/**
	 * Returns the y value of the coordinate represented by this DirectPosition
	 * @return y
	 */
	public double getY() {
		return this.coordinate[1];
	}

	/**
	 * Returns the z value of the coordinate represented by this DirectPosition
	 * @return z
	 */
	public double getZ() {
		return (this.getDimension() > 2) ? this.coordinate[2] : Double.NaN;
	}

	/**
	 * Sets the x value of the coordinate represented by this DirectPosition
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.coordinate[0] = x;
	}

	/**
	 * Sets the y value of the coordinate represented by this DirectPosition
	 * 
	 * @param y
	 */
	public void setY(double y) {
		this.coordinate[1] = y;
	}

	/**
	 * Sets the z value of the coordinate represented by this DirectPosition
	 * 
	 * @param z
	 */
	public void setZ(double z) {
		if (this.getDimension() > 2)
			this.coordinate[2] = z;
	}

	/**
	 * Compares coodinates of Direct Positions and allows a tolerance value in
	 * the comparison
	 * 
	 * @param position
	 *            Direct Position to compare with
	 * @param tol Epsilon tolerance value
	 * @return TRUE, if coordinates accord concording to the tolerance value, FALSE if they dont.
	 */
	public boolean equals(DirectPosition position, double tol) {
		int D = position.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
		if( D != crs.getCoordinateSystem().getDimension() ) return false;
		
		// use CRS.equalsIgnoreMetadata for effeciency and to avoid various issues with comparing
		// CRS such as coordinate order.
		if ( !CRS.equalsIgnoreMetadata(getCoordinateReferenceSystem(), position.getCoordinateReferenceSystem()) ) {
			return false;
		}
		
		// comparing a NaN ordinate to a non-NaN ordinate should return false, but two
		// ordinates that are both NaN should considered equal.
		for (int i = 0; i < D; ++i) {
			if (Double.isNaN(position.getOrdinate(i)) && Double.isNaN(this.coordinate[i]))
				continue;
			if (Math.abs(DoubleOperation.subtract(position.getOrdinate(i), this.coordinate[i])) > tol)
				return false;
		}
		return true;
	}	
	
	/**
	 * Compares coodinates of DirectPosition Implementation Note: Parameter has
	 * to be of Type DirectPosition (not DirectPositionImpl), so that the equals
	 * method is found for DirectPosition´s and DirectPositionImpl´s
	 * 
	 * @param p
	 *            DirectPosition
	 * @return TRUE, if the two DirectPositions describe the same point in the
	 *         Euclidian Space
	 */

	// Sanjay: The method was replaced by the equals(Object) method below,
	// because it was not recognized in all cases
	// public boolean equals(DirectPosition p) {
	// return this.equals(p, 0);
	// }
	// TODO JR: nach zur kenntnisnahme und zustimmung bitte obiges kommentar loeschen
	public boolean equals(Object o) {
		if (o instanceof DirectPosition)
			return this.equals((DirectPosition) o, 0);
		else if (o instanceof Position)
			return ((Position)o).equals(this);
		else
			return false;
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Arrays.hashCode(coordinate);
		result = PRIME * result + ((crs == null) ? 0 : crs.hashCode());
		return result;
	}



	
	public String toString() {
		double coord[] = this.getCoordinates();
		String str = "(" + Double.toString(coord[0]);
		for (int i = 1; i < coord.length; ++i) {
			str += " " + Double.toString(coord[i]);
		}
		return str + ")";
	}

	/**
	 * Calculates the distance to another direct position
	 * 
	 * @param p
	 *            direct Position
	 * @return Distance
	 */
	public double distance(DirectPosition p) {
		return AlgoPointND.getDistance(this.coordinate, p.getCoordinates());
	}

	/**
	 * Calculates the square of the distance to another direct position
	 * 
	 * @param p another direct Position
	 * @return Distance
	 */
	public double distanceSquare(DirectPosition p) {
		return AlgoPointND.getDistanceSquare(this.coordinate, p
				.getCoordinates());
	}
	
	
	

// Auskommentiert, da ungenutzt und nicht getestet
//	/**
//	 * Adds a DirectPosition to the position
//	 * 
//	 * @param p
//	 *            DirectPosition to add
//	 * @return new Position
//	 */
//	public DirectPositionImpl add(DirectPosition p) {
//		return new DirectPositionImpl(this.factory, AlgoPointND.add(
//				this.coordinate, p.getCoordinate()));
//	}

	/**
	 * Adds certain value to each ordinate of this direct position.
	 * 
	 * @param values Array of doubles. values[0] will be added to the X ordinate, values[1] to the Y value and an optional values[2] to the Z value. 
	 */
	public void add(double[] values) {
		if (this.coordinate.length != values.length)
			throw new MismatchedDimensionException();
		
		for (int i=0; i < this.coordinate.length; i++) {
			if (Double.compare(this.coordinate[i], Double.NaN) == 0) {
				this.coordinate[i] = values[i];
			}
			else {
				this.coordinate[i] = DoubleOperation.add(this.coordinate[i], values[i]);
			}
		}		
	}
	
	/**
	 * Adds the ordinates of another direct position to the ordinates of this direct position.
	 * 
	 * @param otherDP DirectPosition which ordinates shall be added to this DirectPosition 
	 */
	public void add(DirectPositionImpl otherDP) {
		if (this.coordinate.length != otherDP.getDimension())
			throw new MismatchedDimensionException();
		
		for (int i=0; i < this.coordinate.length; i++) {
			if (Double.compare(this.coordinate[i], Double.NaN) == 0) {
				this.coordinate[i] = otherDP.getOrdinate(i);
			}
			else {
				this.coordinate[i] = DoubleOperation.add(this.coordinate[i], otherDP.getOrdinate(i));
			}
		}		
	}

	/**
	 * Scales the ordinates of the DirectPosition by a factor:
	 * newOrdinate = oldOrdinate * factor
	 * 
	 * @param factor Scaling factor
	 */
	public void scale(double factor) {
		for (int i=0; i < this.coordinate.length; i++) {
			this.coordinate[i] = DoubleOperation.mult(this.coordinate[i], factor);
		}		
	}

	/**
	 * Divides the ordinates of the DirectPosition by a factor:
	 * newOrdinate = oldOrdinate / factor
	 * 
	 * @param factor Value of divisor
	 */
	public void divideBy(double factor) {
		for (int i=0; i < this.coordinate.length; i++) {
			this.coordinate[i] = DoubleOperation.div(this.coordinate[i], factor);
		}		
	}

	
	// TODO This method exists because of the extend of Position
	// Is this extend really correct??? Check interfaces!!!
	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.Position#getPosition()
	 */
        @Deprecated
	public DirectPosition getPosition() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.Position#getDirectPosition()
	 */
	public DirectPosition getDirectPosition() {
		return this;
	}


	
	
	
//	 Auskommentiert, da ungenutzt und nicht getestet
//	/**
//	 * Subtracts a direct position from the position
//	 * 
//	 * @param p
//	 * @return new Position
//	 */
//	public DirectPositionImpl subtract(DirectPositionImpl p) {
//		return new DirectPositionImpl(this.factory, AlgoPointND.subtract(
//				this.coordinate, p.coordinate));
//	}

// Auskommentiert, da die Methode auf einer nicht-robusten Methode scale-Methode von AlgoPointND basiert.
// Die Methode liefert daher teilweise falsche Ergebnisse. S. JUNIT Test (SJ)
//	 TODO 1) benoetigen wir die methode. wenn ja:
//        2) algorithmus als robuste version implementieren oder sind interne rundungsfehler ok?
//	/**
//	 * @param factor
//	 * @return DirectPositionImpl
//	 */
//	public DirectPositionImpl scale(double factor) {
//		return new DirectPositionImpl(this.factory, AlgoPointND.scale(
//				this.coordinate, factor));
//	}


// Auch diese Methode beruht auf einem Algorithmus, welcher wahrscheinlich nicht robust ist.
// TODO 1) benoetigen wir die methode. wenn ja:
//      2) algorithmus als robuste version implementieren oder sind interne rundungsfehler ok?
//	/**
//	 * @return GM_DirectPosition
//	 */
//	public DirectPositionImpl normalize() {
//		return new DirectPositionImpl(this.factory, AlgoPointND
//				.normalize(this.coordinate.clone()));
//	}

// Diese methode hat meiner meinung nach nichts in dieser klasse zu suchen (SJ)
// das interpolieren von einer straight line gehoert nicht zur aufgabe einer directPosition
// eher zu lineSegment. ich habe die vorhandene methode dort (in LineSegment) deswegen entsprechend angepasst.
// 
//	/**
//	 * 
//	 * @param p0
//	 * @param p1
//	 * @param r
//	 * @return DirectPositionImpl
//	 */
//	public static DirectPositionImpl evaluate(DirectPositionImpl p0,
//			DirectPositionImpl p1, double r) {
//		// TODO Documentation
//		// TODO Test
//		return new DirectPositionImpl(p0.factory, AlgoPointND.evaluate(
//				p0.coordinate, p1.coordinate, r));
//	}

//	 Diese methode hat meiner meinung nach nichts in dieser klasse zu suchen (SJ)
//	 das interpolieren von einer straight line gehoert nicht zur aufgabe einer directPosition
//	 eher zu lineSegment. ich habe die vorhandene methode dort (in LineSegment) deswegen entsprechend angepasst.
//	/**
//	 * @param p0
//	 * @param p1
//	 * @param eval
//	 * @return DirectPositionImpl
//	 */
//	public static DirectPositionImpl evaluate(DirectPositionImpl p0,
//			DirectPositionImpl p1, DirectPositionImpl eval) {
//		return new DirectPositionImpl(p0.factory, AlgoPointND.evaluate(
//				p0.coordinate, p1.coordinate, eval.coordinate));
//	}

	
// Nicht genutzt, nicht getestet
//	/**
//	 * Returns the length (Distance between origin and position)
//	 * 
//	 * @return Length
//	 */
//	public double length() {
//		return AlgoPointND.getDistanceToOrigin(this.coordinate);
//	}

//	 Nicht genutzt, nicht getestet
//	/**
//	 * @return double
//	 */
//	public double lengthSquare() {
//		return AlgoPointND.getDistanceToOriginSquare(this.coordinate);
//	}
	
	

	// TODO JR: Wenn du nichts dagegen hast, den folgenden teil bitte rausloeschen. solche methoden werden in robuster form in jts angeboten.
	
	// public double cross2D(DirectPositionImpl dp){
	// // corresponds to the 2*area of two vectors
	// return this.getX() * dp.getY() - this.getY() * dp.getX();
	// }
	//
	// public boolean intersectWithHorizontalLineFromRight2D(DirectPositionImpl
	// p0, DirectPositionImpl p1){
	// // returns true when a horizontal line passing at ME:
	// // 1) intersects the line with origin p0 and and p1 and
	// // 2) when ME is on the right side of the line
	// double x0 = p0.getX(); // line endpoint 2D coords
	// double y0 = p0.getY(); // line endpoint 2D coords
	// double x1 = p1.getX(); // line endpoint 2D coords
	// double y1 = p1.getY(); // line endpoint 2D coords
	// double xa = x0; // swap coordinates
	// double ya = y0; // swap coordinates
	// double xb = x1; // swap coordinates
	// double yb = y1; // swap coordinates
	// double max_x = Math.max(x0, x1); // maximum x coordinate
	// double min_x = Math.min(x0, x1); // minimum x coordinate
	// double max_y = Math.max(y0, y1); // maximum y coordinate
	// double min_y = Math.min(y0, y1); // minimum y coordinate
	//
	// // the horizontal line does not intersect the line to the
	// // left of location pt if:
	// // [1] if line is horizontal
	// if ( y0 == y1 ) return false;
	// // (2) if the y coordinate of point is outside the range
	// // max_y and min_y (but not including min_y)
	// if ( ((this.getY() < min_y) || (this.getY() >= max_y)) ) return false;
	// // (3) if given line is vertical and y coordinate of point is
	// // smaller than that of line
	// if ( ((x0 == x1) && (this.getX() < x0)) ) return false;
	// // (4) if inclined line is located to the right of given point
	// // (first reduce the problem to a case where yb >ya, always)
	// if ( (x0 != x1) ) {
	// if ( !(((x1 > x0) && (y1 > y0)) || ((x1 < x0) && (y1 > y0))) ) {
	// xa = x1;
	// ya = y1;
	// xb = x0;
	// yb = y0;
	// }
	// if ( (((this.getY() - ya) * (xb - xa)) > ((this.getX() - xa) * (yb -
	// ya))) ) {
	// return false;
	// }
	// }
	// // if we get here that is because the horizontal line passing
	// // at the location this intersects the given line to the left of the pt
	// return true;
	// }
	//
	// public double getAngle2D(DirectPositionImpl p1){
	// // * p1
	// // /
	// // /
	// // /
	// // *------>*
	// // (0,0) this
	// double angle = Math.atan2(p1.getY(), p1.getX()) - Math.atan2(this.getY(),
	// this.getX());
	// if ( angle < 0.0 ) angle = angle + 2 * Math.PI;
	// if ( angle > (2 * Math.PI) ) angle = angle - 2 * Math.PI;
	// return angle;
	// }
	//
	// public double minAngle2D(DirectPositionImpl p1, DirectPositionImpl p2){
	// double ang0 = ((DirectPositionImpl)
	// p1.subtract(this)).getAngle2D((DirectPositionImpl)p2.subtract(this));
	// double ang1 =
	// ((DirectPositionImpl)p2.subtract(p1)).getAngle2D((DirectPositionImpl)this.subtract(p1));
	// return Math.min(Math.min(ang0, ang1), Math.min(ang0, Math.PI - ang0 -
	// ang1));
	// }
	//    
	// public String toString() {
	// String str = Double.toString(this.coordinate[0]);
	// for (int i=1; i<this.getDimension(); ++i) {
	// str += "| " + Double.toString(this.coordinate[i]);
	// }
	// return "[DirectPosition: " + str + "]";
	// }
	//
	// /**
	// * Builds the scalar product
	// * @param p
	// * @return Scalar product
	// */
	// public double scalar(DirectPositionImpl p) {
	// double result = 0.0;
	// double pCoord[] = p.getCoordinate();
	// for (int i=0; i<this.getDimension(); ++i) {
	// result += this.coordinate[i] * pCoord[i];
	// }
	// return result;
	// }
	//
	// public static Object cross(DirectPositionImpl p0, DirectPositionImpl p1){
	// int n = Math.min(p0.getDimension(),p1.getDimension());
	// if (n==2) {
	// // corresponds to the 2*area of two vectors
	// double p0Coord[] = p0.getCoordinate();
	// double p1Coord[] = p1.getCoordinate();
	// return (Double)p0Coord[0] * p1Coord[1] - p0Coord[1] * p1Coord[0];
	// } else if (n==3) {
	// // TODO
	// assert false;
	// DirectPositionImpl result = null;
	// return null;
	// } else {
	// assert false;
	// return null;
	// }
	// }
}
