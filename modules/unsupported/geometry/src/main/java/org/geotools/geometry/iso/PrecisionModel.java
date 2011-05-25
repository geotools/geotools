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
package org.geotools.geometry.iso;

import java.io.Serializable;

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.Precision;
import org.opengis.geometry.PrecisionType;

/**
 * Specifies the precision model of the {@link Coordinate}s in a
 * {@link Geometry}. In other words, specifies the grid of allowable points for
 * all <code>Geometry</code>s.
 * <p>
 * The {@link makePrecise} method allows rounding a coordinate to a "precise"
 * value; that is, one whose precision is known exactly.
 * <p>
 * Coordinates are assumed to be precise in geometries. That is, the coordinates
 * are assumed to be rounded to the precision model given for the geometry. JTS
 * input routines automatically round coordinates to the precision model before
 * creating Geometries. All internal operations assume that coordinates are
 * rounded to the precision model. Constructive methods (such as boolean
 * operations) always round computed coordinates to the appropriate precision
 * model.
 * <p>
 * Currently three types of precision model are supported:
 * <ul>
 * <li>FLOATING - represents full double precision floating point. This is the
 * default precision model used in JTS
 * <li>FLOATING_SINGLE - represents single precision floating point.
 * <li>FIXED - represents a model with a fixed number of decimal places. A
 * Fixed Precision Model is specified by a scale factor. The scale factor
 * specifies the grid which numbers are rounded to. Input coordinates are mapped
 * to fixed coordinates according to the following equations:
 * <UL>
 * <LI> jtsPt.x = round( (inputPt.x * scale ) / scale
 * <LI> jtsPt.y = round( (inputPt.y * scale ) / scale
 * </UL>
 * </ul>
 * Coordinates are represented internally as Java double-precision values. Since
 * Java uses the IEEE-394 floating point standard, this provides 53 bits of
 * precision. (Thus the maximum precisely representable integer is
 * 9,007,199,254,740,992).
 * <p>
 * JTS methods currently do not handle inputs with different precision models.
 * 
 *
 *
 * @source $URL$
 * @version 1.7.2
 */
public class PrecisionModel implements Serializable, Precision {

	private static final long serialVersionUID = 7777263578777803835L;

	/**
	 * Fixed Precision indicates that coordinates have a fixed number of decimal
	 * places. The number of decimal places is determined by the log10 of the
	 * scale factor.
	 */
	public static final PrecisionType FIXED = PrecisionType.FIXED;

	/**
	 * Floating precision corresponds to the standard Java double-precision
	 * floating-point representation, which is based on the IEEE-754 standard
	 */
	public static final PrecisionType FLOATING = PrecisionType.DOUBLE;

	/**
	 * Floating single precision corresponds to the standard Java
	 * single-precision floating-point representation, which is based on the
	 * IEEE-754 standard
	 */
	public static final PrecisionType FLOATING_SINGLE = PrecisionType.FLOAT;

	/**
	 * The maximum precise value representable in a double. Since IEE754
	 * double-precision numbers allow 53 bits of mantissa, the value is equal to
	 * 2^53 - 1. This provides <i>almost</i> 16 decimal digits of precision.
	 */
	public final static double maximumPreciseValue = 9007199254740992.0;

	/**
	 * The type of PrecisionModel this represents.
	 */
	private PrecisionType modelType;

	/**
	 * The scale factor which determines the number of decimal places in fixed
	 * precision.
	 */
	private double scale;

	/**
	 * Creates a <code>PrecisionModel</code> with a default precision of
	 * FLOATING.
	 */
	public PrecisionModel() {
		// default is floating precision
		modelType = FLOATING;
	}

	/**
	 * Creates a <code>PrecisionModel</code> that specifies an explicit
	 * precision model type. If the model type is FIXED the scale factor will
	 * default to 1.
	 * 
	 * @param modelType
	 *            the type of the precision model
	 */
	public PrecisionModel(PrecisionType modelType) {
		this.modelType = modelType;
		if (modelType == FIXED) {
			setScale(1.0);
		}
	}


	/**
	 * Creates a <code>PrecisionModel</code> that specifies Fixed precision.
	 * Fixed-precision coordinates are represented as precise internal
	 * coordinates, which are rounded to the grid defined by the scale factor.
	 * 
	 * @param scale
	 *            amount by which to multiply a coordinate after subtracting the
	 *            offset, to obtain a precise coordinate
	 */
	public PrecisionModel(double scale) {
		modelType = FIXED;
		setScale(scale);
	}

	/**
	 * Copy constructor to create a new <code>PrecisionModel</code> from an
	 * existing one.
	 */
	public PrecisionModel(PrecisionModel pm) {
		modelType = pm.modelType;
		scale = pm.scale;
	}

	/**
	 * Tests whether the precision model supports floating point
	 * 
	 * @return <code>true</code> if the precision model supports floating
	 *         point
	 */
	public boolean isFloating() {
		return modelType == FLOATING || modelType == FLOATING_SINGLE;
	}

	/**
	 * Returns the maximum number of significant digits provided by this
	 * precision model. Intended for use by routines which need to print out
	 * precise values.
	 * 
	 * @return the maximum number of decimal places provided by this precision
	 *         model
	 */
	public int getMaximumSignificantDigits() {
		int maxSigDigits = 16;
		if (modelType == FLOATING) {
			maxSigDigits = 16;
		} else if (modelType == FLOATING_SINGLE) {
			maxSigDigits = 6;
		} else if (modelType == FIXED) {
			maxSigDigits = 1 + (int) Math.ceil(Math.log(getScale())
					/ Math.log(10));
		}
		return maxSigDigits;
	}

	/**
	 * Returns the multiplying factor used to obtain a precise coordinate. This
	 * method is private because PrecisionModel is intended to be an immutable
	 * (value) type.
	 * 
	 * @return the amount by which to multiply a coordinate after subtracting
	 *         the offset
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Gets the type of this PrecisionModel
	 * 
	 * @return the type of this PrecisionModel
	 */
	public PrecisionType getType() {
		return modelType;
	}
	
	

	/**
	 * Sets the multiplying factor used to obtain a precise coordinate. This
	 * method is private because PrecisionModel is intended to be an immutable
	 * (value) type.
	 * 
	 */
	private void setScale(double scale) {
		this.scale = Math.abs(scale);
	}



	/**
	 * Rounds a numeric value to the PrecisionModel grid. Asymmetric Arithmetic
	 * Rounding is used, to provide uniform rounding behaviour no matter where
	 * the number is on the number line.
	 * <p>
	 * <b>Note:</b> Java's <code>Math#rint</code> uses the "Banker's
	 * Rounding" algorithm, which is not suitable for precision operations
	 * elsewhere in JTS.
	 */
	public double makePrecise(double val) {
		if (modelType == FLOATING_SINGLE) {
			float floatSingleVal = (float) val;
			return (double) floatSingleVal;
		}
		if (modelType == FIXED) {
			return Math.round(val * scale) / scale;
			// return Math.rint(val * scale) / scale;
		}
		// modelType == FLOATING - no rounding necessary
		return val;
	}

	/**
	 * Rounds a Coordinate to the PrecisionModel grid.
	 */
	public void makePrecise(Coordinate coord) {
		// optimization for full precision
		if (modelType == FLOATING)
			return;

		coord.x = makePrecise(coord.x);
		coord.y = makePrecise(coord.y);
		// MD says it's OK that we're not makePrecise'ing the z [Jon Aquino]
	}

	public String toString() {
		String description = "UNKNOWN";
		if (modelType == FLOATING) {
			description = "Floating";
		} else if (modelType == FLOATING_SINGLE) {
			description = "Floating-Single";
		} else if (modelType == FIXED) {
			description = "Fixed (Scale=" + getScale() + ")";
		}
		return description;
	}

	public boolean equals(Object other) {
		if (!(other instanceof PrecisionModel)) {
			return false;
		}
		PrecisionModel otherPrecisionModel = (PrecisionModel) other;
		return modelType == otherPrecisionModel.modelType
				&& scale == otherPrecisionModel.scale;
	}

	/**
	 * Compares this {@link PrecisionModel} object with the specified object for
	 * order. A PrecisionModel is greater than another if it provides greater
	 * precision. The comparison is based on the value returned by the
	 * {@link getMaximumSignificantDigits} method. This comparison is not
	 * strictly accurate when comparing floating precision models to fixed
	 * models; however, it is correct when both models are either floating or
	 * fixed.
	 * 
	 * @param other
	 *            the <code>PrecisionModel</code> with which this
	 *            <code>PrecisionModel</code> is being compared
	 * @return a negative integer, zero, or a positive integer as this
	 *         <code>PrecisionModel</code> is less than, equal to, or greater
	 *         than the specified <code>PrecisionModel</code>
	 */
	public int compareTo(PrecisionModel other) {	
        if( other == null ) return 0;
		int sigDigits = getMaximumSignificantDigits();
		int otherSigDigits = other.getMaximumSignificantDigits();
		return (new Integer(sigDigits)).compareTo(new Integer(otherSigDigits));
		// if (sigDigits > otherSigDigits)
		// return 1;
		// else if
		// if (modelType == FLOATING && other.modelType == FLOATING) return 0;
		// if (modelType == FLOATING && other.modelType != FLOATING) return 1;
		// if (modelType != FLOATING && other.modelType == FLOATING) return -1;
		// if (modelType == FIXED && other.modelType == FIXED) {
		// if (scale > other.scale)
		// return 1;
		// else if (scale < other.scale)
		// return -1;
		// else
		// return 0;
		// }
		// Assert.shouldNeverReachHere("Unknown Precision Model type
		// encountered");
		// return 0;
	}

    public int compareTo( Precision precision) {
        if( precision == null ) return 0;
        
        int sigDigits = getMaximumSignificantDigits();
        int otherSigDigits = precision.getMaximumSignificantDigits();
        return (new Integer(sigDigits)).compareTo(new Integer(otherSigDigits));        
    }
        
    public void round( DirectPosition position ) {        
        if (modelType.isFloating()){ // somekind of optimization
            return;
        }
        double coords[] = position.getCoordinates();
        position.setOrdinate( 0, makePrecise( coords[0] ));
        position.setOrdinate( 1, makePrecise( coords[1] ));
        if( coords.length == 2 ) return;
        
        position.setOrdinate( 2, makePrecise( coords[2] ));    
        if( coords.length == 3 ) return;
        
        for( int axis = 3; axis < position.getDimension(); axis++ ){
            double ordinate = position.getOrdinate( axis );
            ordinate = makePrecise( ordinate );
            position.setOrdinate( axis, ordinate );
        }
    }
}
