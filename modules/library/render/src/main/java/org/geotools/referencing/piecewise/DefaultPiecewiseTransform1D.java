/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.piecewise;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.operation.matrix.Matrix1;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Convenience implementation of the   {@link PiecewiseTransform1D}   interface which subclass the   {@link DefaultDomain1D}   class in order to provide a suitable framework to handle a list of   {@link PiecewiseTransform1DElement}   s. <p>
 * @author   Simone Giannecchini, GeoSolutions
 *
 *
 * @source $URL$
 */
public class DefaultPiecewiseTransform1D<T extends DefaultPiecewiseTransform1DElement> extends DefaultDomain1D<T>
		implements PiecewiseTransform1D<T> {

	private boolean hasDefaultValue;
	/**
     * @uml.property  name="defaultValue"
     */
	private double defaultValue;
	private int hashCode=-1;

	public DefaultPiecewiseTransform1D(
			final T[] domainElements,
			final  double defaultValue) {
		super(domainElements);
		this.hasDefaultValue=true;
		this.defaultValue=defaultValue;
	}



	public DefaultPiecewiseTransform1D(final T[] domainElements) {
		super(
				domainElements != null && !(domainElements instanceof DefaultConstantPiecewiseTransformElement[]) ? 
						domainElements : 
						null);
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform1D#transform(double)
	 */
	public double transform(final double value) throws TransformException {
		final T piece = findDomainElement(value);
		if (piece == null) {
			//do we have a default value?
			if(hasDefaultValue())
				return getDefaultValue();
			throw new TransformException(Errors.format(ErrorKeys.TRANSFORM_EVALUATION_$1,new Double(value)));
		}
		return piece.transform(value);
	}

	/**
	 * Gets the derivative of this transform at a point.
	 */
	public Matrix derivative(final DirectPosition point)
			throws TransformException {
		PiecewiseUtilities.checkDimension(point);
		return new Matrix1(derivative(point.getOrdinate(0)));
	}

	/**
	 * Gets the derivative of this function at a value.
	 * 
	 * @param value
	 *            The value where to evaluate the derivative.
	 * @return The derivative at the specified point.
	 * @throws TransformException
	 *             if the derivative can't be evaluated at the specified point.
	 */
	public double derivative(final double value)
			throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "derivate"));
	}

	/**
	 * Gets the dimension of input points, which is 1.
	 */
	public final int getSourceDimensions() {
		return 1;
	}

	/**
	 * Gets the dimension of output points, which is 1.
	 */
	public final int getTargetDimensions() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#inverse()
	 */
	public MathTransform1D inverse() throws NoninvertibleTransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "inverse"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#isIdentity()
	 */
	public boolean isIdentity() {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "isIdentity"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#toWKT()
	 */
	public String toWKT() throws UnsupportedOperationException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "toWKT"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(org.opengis.spatialschema.geometry.DirectPosition,
	 *      org.opengis.spatialschema.geometry.DirectPosition)
	 */
	public DirectPosition transform(final DirectPosition ptSrc,
			DirectPosition ptDst) throws MismatchedDimensionException,
			TransformException {
		// /////////////////////////////////////////////////////////////////////
		//
		// input checks
		//
		// /////////////////////////////////////////////////////////////////////
		PiecewiseUtilities.ensureNonNull("ptSrc", ptSrc);
		PiecewiseUtilities.checkDimension(ptSrc);
		if (ptDst == null) {
			ptDst = new GeneralDirectPosition(1);
		} else {
			PiecewiseUtilities.checkDimension(ptDst);
		}
		ptDst.setOrdinate(0, transform(ptSrc.getOrdinate(0)));
		return ptDst;
	}

	/**
	 * Transforms a list of coordinate point ordinal values.
	 */
	public void transform(final double[] srcPts, final int srcOff, final double[] dstPts,
			final int dstOff, final int numPts) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));
	}

	/**
	 * Transforms a list of coordinate point ordinal values.
	 */
	public void transform(final float[] srcPts, final int srcOff, final float[] dstPts,
			final int dstOff, final int numPts) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));
	}

	/**
	 * Transforms a list of coordinate point ordinal values.
	 */
	public void transform(final float[] srcPts, final int srcOff, final double[] dstPts,
			final int dstOff, final int numPts) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));
	}

	/**
	 * Transforms a list of coordinate point ordinal values.
	 */
	public void transform(final double[] srcPts, final int srcOff, final float[] dstPts,
			final int dstOff, final int numPts) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));
	}



	public boolean hasDefaultValue() {
		return hasDefaultValue;
	}



	/**
     * @return
     * @uml.property  name="defaultValue"
     */
	public double getDefaultValue() {
		return defaultValue;
	}




    @Override
    public int hashCode() {
        if(hashCode>=0)
            return hashCode;
        hashCode = 37;
        hashCode = Utilities.hash( defaultValue,hashCode );
        hashCode = Utilities.hash(  hasDefaultValue ,hashCode);
        hashCode = Utilities.hash(  super.hashCode(),hashCode );
        return hashCode;
    }



    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if((object instanceof DefaultPiecewiseTransform1D))
        {
            final DefaultPiecewiseTransform1D<?> that = (DefaultPiecewiseTransform1D<?>) object;
            if(this.hasDefaultValue!=that.hasDefaultValue)
                return false;
            if(Utilities.equals(defaultValue, that.defaultValue));
                return false;
        }
        return super.equals(object);
    }

    protected Class<?> getEquivalenceClass(){
        return DefaultPiecewiseTransform1D.class;
    }


}
