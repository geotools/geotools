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

import org.geotools.geometry.iso.DimensionModel;
import org.geotools.geometry.iso.UnsupportedDimensionException;
import org.geotools.geometry.iso.util.algorithmND.AlgoRectangleND;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * An envlope represents the bounding box of a geometric object.
 * Regardless to the dimension, the envelope can be encoded by two <code>DirectPosition</code>s. 
 *
 * @source $URL$
 */
public class EnvelopeImpl implements Envelope, Serializable {
    private static final long serialVersionUID = -6397319034308589090L;
    
    static final int X = 0;
    static final int Y = 1;
    static final int Z = 2;
    
	// protected DirectPositionImpl pMin = null; // Lower Corner (Left bottom)
	private DirectPosition pMin = null; // Lower Corner (Left bottom)

	// protected DirectPositionImpl pMax = null; // Upper Corner (Right top)
	private DirectPosition pMax = null; // Upper Corner (Right top)

    /** Used to create Envelope on request */
    private PositionFactory factory;
    
	/**
	 * Constructor
	 * 
	 * @param env
	 */
	public EnvelopeImpl(Envelope env) {
        pMin = new DirectPositionImpl(env.getLowerCorner());
		pMax = new DirectPositionImpl(env.getUpperCorner());
	}

	/**
	 * Constructor
	 * 
	 * @param p0
	 * @param p1
	 */
	public EnvelopeImpl(DirectPosition p0, DirectPosition p1) {
		this.setValues(p0, p1);
	}

    /**
     * @param p0
     */
    public EnvelopeImpl(Position position) {
        this( position.getPosition() );
    }
	/**
	 * @param p0
	 */
	public EnvelopeImpl(DirectPosition position) {
		this.pMin = new DirectPositionImpl(position);
		this.pMax = new DirectPositionImpl(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getDimension()
	 */
	public int getDimension() {
		// TODO semantic JR
		// The coordinate dimension of the envelope is the same as the
		// coordinate dimension of one of his points
		return this.pMin.getDimension();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getCoordinateReferenceSystem()
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return pMin.getCoordinateReferenceSystem();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getMinimum(int)
	 */
	public double getMinimum(int dimension) {
		// TODO semantic JR, SJ Was soll diese Methode bewirken? Ich verstehe die JavaDoc nicht ganz.
		// TODO implementation
		// TODO test
		// TODO documentation
		// Return zero, which is the lowest index the coordinate could have
		return pMin.getOrdinate(dimension);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getMaximum(int)
	 */
	public double getMaximum(int dimension) {
		// TODO semantic JR, SJ Was soll diese Methode bewirken? Ich verstehe die JavaDoc nicht ganz.
		// TODO implementation
		// TODO test
		// TODO documentation
		// Return the coordinate dimension minus 1
		//return this.pMin.getDimension() - 1;
		return pMax.getOrdinate(dimension);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getCenter(int)
	 */
	public double getCenter(int dimension) {
		return (pMax.getOrdinate(dimension)-pMin.getOrdinate(dimension))/2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getMedian(int)
	 */
	public double getMedian(int dimension) {
		return (pMax.getOrdinate(dimension)-pMin.getOrdinate(dimension))/2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getLength(int)
	 */
	public double getLength(int dimension) {
		return (pMax.getOrdinate(dimension)-pMin.getOrdinate(dimension));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getSpan(int)
	 */
	public double getSpan(int dimension) {
		return (pMax.getOrdinate(dimension)-pMin.getOrdinate(dimension));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getUpperCorner()
	 */
	public DirectPosition getUpperCorner() {
		// Return the upper corner of the envelope
		return this.pMax;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.Envelope#getLowerCorner()
	 */
	public DirectPosition getLowerCorner() {
		// Return the lower corner of the envelope
		return this.pMin;
	}

	/**
	 * @param p0
	 * @param p1
	 */
	public void setValues(DirectPosition p0, DirectPosition p1) {
		if (p0 == null || p1 == null || p0.getDimension() != p1.getDimension())
			throw new IllegalArgumentException("Error 1 on setValues"); //$NON-NLS-1$
        
        CoordinateReferenceSystem crs = p0.getCoordinateReferenceSystem();
		double[] min = p0.getCoordinates();
		double[] max = p1.getCoordinates();
		// Check wheater all Min values are smaller than max values
		for (int i = 0, n = p0.getDimension(); i < n; ++i) {
			if (min[i] > max[i]) {
				double tmp = min[i];
				min[i] = max[i];
				max[i] = tmp;
			}
		}

		this.pMin = new DirectPositionImpl( crs, min);
		this.pMax = new DirectPositionImpl( crs, max);
	}

	/**
	 * @param env
	 */
	public void setValues(EnvelopeImpl env) {
		this.pMin = new DirectPositionImpl(env.getLowerCorner());
		this.pMax = new DirectPositionImpl(env.getUpperCorner());
	}

	/**
	 * @param p
	 * @return EnvelopeImpl
	 */
	public static EnvelopeImpl createEnvelope(DirectPosition[] p) {
		if (p.length == 0){
			return null;
        }
		EnvelopeImpl result = new EnvelopeImpl(p[0]);
		for (int i = 1; i < p.length; ++i) {
			if (p[i] != null){
				result.add(p[i]);
            }
		}
		return result;
	}
    
	/**
	 * Unions an envelope with an another envelope
	 * 
	 * @param env
	 */
	public void expand(Envelope env) {
		this.expand(env.getLowerCorner().getCoordinates());
		this.expand(env.getUpperCorner().getCoordinates());
	}

	/**
	 * Expands the envelope with a direct Position
	 * 
	 * @param coord
	 */
	public void expand(double coord[]) {
		int n = Math.min(this.getDimension(), coord.length);
		double min[] = this.pMin.getCoordinates();
		double max[] = this.pMax.getCoordinates();
		for (int i = 0; i < n; ++i) {
			if (coord[i] < min[i])
				this.pMin.setOrdinate(i, coord[i]);
			if (coord[i] > max[i])
				this.pMax.setOrdinate(i, coord[i]);
		}
	}

	public String toString() {
		return "[Envelope: " + this.getLowerCorner() + " - " //$NON-NLS-1$//$NON-NLS-2$
				+ this.getUpperCorner() + "]"; //$NON-NLS-1$
	}

	/**
	 * @param coord
	 */
	public void add(double[] coord) {
		assert (coord.length == this.getDimension());
        
		double[] minCoord = this.pMin.getCoordinates();
		double[] maxCoord = this.pMax.getCoordinates();
        
		for (int i = 0; i < this.getDimension(); ++i) {
			double ci = coord[i];
			double cmini = minCoord[i];
			double cmaxi = maxCoord[i];
			if (!Double.isNaN(ci) && ((ci < cmini) || Double.isNaN(cmini)))
				this.pMin.setOrdinate(i, ci);
			if (!Double.isNaN(ci) && ((ci > cmaxi) || Double.isNaN(cmaxi)))
				this.pMax.setOrdinate(i, ci);
		}
        
	}

	/**
	 * @param p
	 */
	public void add(DirectPosition p) {
		this.add(p.getCoordinates());
	}

	/**
	 * @param env
	 */
	public void add(EnvelopeImpl env) {
		this.add( env.getLowerCorner() );
		this.add( env.getUpperCorner() );
	}

// Auskommentiert, da es die scale methode von DP nutzt. diese ist nicht robust.
//	/**
//	 * @return DirectPositionImpl
//	 */
//	public DirectPositionImpl center() {
//		return (this.pMin.add(this.pMax)).scale(0.5);
//	}


	/**
	 * Compares coordinates between the envelope and another envelope Test OK
	 * 
	 * @param env
	 * @return boolean
	 */
	public boolean equals(Envelope env) {
		return (this.getUpperCorner().equals(env.getUpperCorner()) && this
				.getLowerCorner().equals(env.getLowerCorner()));
	}

	/**
	 * Verifies whether another envelope intersects with this envelope
	 * 
	 * @param other
	 * @return TRUE, if envelopes intersect; FALSE, if they dont intersect
	 */
	public boolean intersects(Envelope other) {
		return AlgoRectangleND.intersects(this.pMin.getCoordinates(), this.pMax
				.getCoordinates(), other.getLowerCorner().getCoordinates(),
				other.getUpperCorner().getCoordinates());
	}

	/**
	 * Verifies wheater the coordinate of a Direct Position intersects with the
	 * envelope
	 * 
	 * @param dp
	 * @return boolean
	 */
	public boolean intersects(DirectPosition dp) {
//		return AlgoRectangleND.intersects(this.pMin.getCoordinates(), this.pMax
//				.getCoordinates(), dp.getCoordinates());
		return AlgoRectangleND.contains(this.pMin.getCoordinates(), this.pMax
				.getCoordinates(), dp.getCoordinates());

	}
	
	/**
	 * The North East corner of this Envelope
	 * 
	 * @return
	 */
	public DirectPosition getNECorner() {
		// Test ok
		return this.getUpperCorner();
	}

	/**
	 * The South West corner of this Envelope
	 * 
	 * @return
	 */
	public DirectPosition getSWCorner() {
		// Test ok
		return this.getLowerCorner();
	}

	/**
	 * The South East corner of this Envelope
	 * 2D and 2.5D only!
	 * 
	 * In 2.5D, the z value will be set equal to the z value of the lower corner z value.
	 * 
	 * @return
	 * @throws UnsupportedDimensionException 
	 */
	public DirectPosition getSECorner() throws UnsupportedDimensionException {
        
		DirectPosition rDP = null;
		CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
        int D = DimensionModel.toD( getCoordinateReferenceSystem() );
		if ( D == DimensionModel.TWO_DIMENSIONIAL ) {
			rDP = new DirectPositionImpl( crs, new double[]{
               this.pMax.getOrdinate(X),
               this.pMin.getOrdinate(Y)
            });
		} else
            if ( D == DimensionModel.TWOoFIVE_DIMENSIONIAL ) {
			rDP = new DirectPositionImpl( crs, new double[]{
                        this.pMax.getOrdinate(X),
                        this.pMin.getOrdinate(Y),
                        this.pMin.getOrdinate(Z)}
			);
		} else {
			throw new UnsupportedDimensionException("3d not supported.");
		}
		return rDP;
	}

	/**
	 * The North West corner of this Envelope
	 * 2D and 2.5D only!
	 * 
	 * In 2.5D, the z value will be set equal to the z value of the lower corner z value.
	 * 
	 * @return
	 * @throws UnsupportedDimensionException 
	 */
	public DirectPositionImpl getNWCornerOld() throws UnsupportedDimensionException {
		
		DirectPositionImpl rDP = null;
		CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
        int D = DimensionModel.toD( crs );
        if ( D == DimensionModel.TWO_DIMENSIONIAL ) {
        	rDP = new DirectPositionImpl( crs, new double[]{this.pMin.getOrdinate(X), this.pMax.getOrdinate(Y)});
		} else
            if ( D == DimensionModel.TWOoFIVE_DIMENSIONIAL ) {
            	rDP = new DirectPositionImpl( crs, new double[]{this.pMin.getOrdinate(X), this.pMax.getOrdinate(Y), this.pMin.getOrdinate(Z)});
            } else {
            	throw new UnsupportedDimensionException("3d not supported.");
            }
		return rDP;
	}
	public DirectPositionImpl getNWCorner() throws UnsupportedDimensionException {
		
		DirectPositionImpl rDP = null;
		CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
        int D = DimensionModel.toD( crs );
        if ( D == DimensionModel.TWO_DIMENSIONIAL ) {
        	rDP = new DirectPositionImpl( crs, new double[]{this.pMin.getOrdinate(X), this.pMax.getOrdinate(Y)});
		} else
            if ( D == DimensionModel.TWOoFIVE_DIMENSIONIAL ) {
            	rDP = new DirectPositionImpl( crs, new double[]{this.pMin.getOrdinate(X), this.pMax.getOrdinate(Y), this.pMin.getOrdinate(Z)});
            } else {
            	throw new UnsupportedDimensionException("3d not supported.");
            }
		return rDP;
	}

	/**
	 * Verifies whether a DirectPosition2D lays within the envelope or at its
	 * border Test OK
	 * 
	 * @param p
	 * @return TRUE, if the DirectPosition2D lays within the envelope
	 */
	public boolean contains(DirectPosition p) {
		// TODO Semantics: Should return true, if a DirectPosition lays on the
		// border of the envelope?
		return AlgoRectangleND.contains(this.pMin.getCoordinates(), this.pMax
				.getCoordinates(), p.getCoordinates());
	}

//	/**
//	 * @param p
//	 * @return boolean
//	 */
//	public boolean touches(DirectPosition p) {
//		return AlgoRectangleND.touches(this.pMin.getCoordinates(), this.pMax
//				.getCoordinates(), p.getCoordinates());
//	}

//	/**
//	 * @param p
//	 * @param side
//	 * @return boolean
//	 */
//	public boolean touches(DirectPositionImpl p, int side) {
//		return AlgoRectangleND.touches(this.pMin.getCoordinates(), this.pMax
//				.getCoordinates(), p.getCoordinates(), side);
//	}

//	/**
//	 * @param env
//	 * @return boolean
//	 */
//	public boolean contains(Envelope env) {
//		double[] min = this.pMin.getCoordinates();
//		double[] max = this.pMax.getCoordinates();
//		return AlgoRectangleND.contains(min, max, env.getLowerCorner()
//				.getCoordinates())
//				&& AlgoRectangleND.contains(min, max, env.getUpperCorner()
//						.getCoordinates());
//	}

//	/**
//	 * @param factor
//	 * @return EnvelopeImpl
//	 */
//	public EnvelopeImpl scale(double factor) {
//		EnvelopeImpl result = new EnvelopeImpl(this);
//		if (factor <= 0.0 || factor == 1.0)
//			return result;
//		DirectPositionImpl p0 = this.center();
//		DirectPositionImpl p1;
//		p1 = this.pMin.subtract(p0);
//		p1 = p1.scale(factor);
//		result.pMin = p0.add(p1);
//		p1 = this.pMax.subtract(p0);
//		p1 = p1.scale(factor);
//		result.pMax = p0.add(p1);
//		return result;
//	}
	
//	/**
//	 * @return double
//	 */
//	public double maxLength() {
//		double result = 0.0;
//		double minCoord[] = this.pMin.getCoordinates();
//		double maxCoord[] = this.pMax.getCoordinates();
//		int n = Math.min(minCoord.length, maxCoord.length);
//		for (int i = 1; i < n; ++i) {
//			if ((maxCoord[i] - minCoord[i]) > result)
//				result = maxCoord[i] - minCoord[i];
//		}
//		return result;
//	}
	
	
}
