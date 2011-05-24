/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Transforms multi-dimensional coordinate points. This interface transforms coordinate
 * value for a point given in the {@linkplain CoordinateOperation#getSourceCRS source
 * coordinate reference system} to coordinate value for the same point in the
 * {@linkplain CoordinateOperation#getTargetCRS target coordinate reference system}.
 *
 * In a {@linkplain Conversion conversion}, the transformation is accurate to within the
 * limitations of the computer making the calculations. In a {@linkplain Transformation
 * transformation}, where some of the operational parameters are derived from observations,
 * the transformation is accurate to within the limitations of those observations.
 *
 * If a client application wishes to query the source and target
 * {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference systems}
 * of an operation, then it should keep hold of the {@link CoordinateOperation} interface,
 * and use the contained math transform object whenever it wishes to perform a transform.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/operation/MathTransform.java $
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see java.awt.geom.AffineTransform
 * @see javax.media.jai.PerspectiveTransform
 * @see javax.media.j3d.Transform3D
 * @see MathTransformFactory
 * @see CoordinateOperation#getMathTransform
 */
@UML(identifier="CT_MathTransform", specification=OGC_01009)
public interface MathTransform {
    /**
     * Gets the dimension of input points.
     *
     * @return The dimension of input points.
     */
    @UML(identifier="getDimSource", specification=OGC_01009)
    int getSourceDimensions();

    /**
     * Gets the dimension of output points.
     *
     * @return The dimension of output points.
     */
    @UML(identifier="getDimTarget", specification=OGC_01009)
    int getTargetDimensions();

    /**
     * Transforms the specified {@code ptSrc} and stores the result in
     * {@code ptDst}. If {@code ptDst} is {@code null}, a new
     * {@link DirectPosition} object is allocated and then the result of the
     * transformation is stored in this object. In either case, {@code ptDst},
     * which contains the transformed point, is returned for convenience.
     * If {@code ptSrc} and {@code ptDst} are the same object,
     * the input point is correctly overwritten with the transformed point.
     *
     * @param  ptSrc the specified coordinate point to be transformed.
     * @param  ptDst the specified coordinate point that stores the result of transforming
     *         {@code ptSrc}, or {@code null}.
     * @return the coordinate point after transforming {@code ptSrc} and storing the result
     *         in {@code ptDst}, or a newly created point if {@code ptDst} was null.
     * @throws MismatchedDimensionException if {@code ptSrc} or
     *         {@code ptDst} doesn't have the expected dimension.
     * @throws TransformException if the point can't be transformed.
     */
    @UML(identifier="transform", specification=OGC_01009)
    DirectPosition transform(DirectPosition ptSrc, DirectPosition ptDst)
            throws MismatchedDimensionException, TransformException;

    /**
     * Transforms a list of coordinate point ordinal values.
     * This method is provided for efficiently transforming many points.
     * The supplied array of ordinal values will contain packed ordinal
     * values. For example, if the source dimension is 3, then the ordinals
     * will be packed in this order:
     *
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *  <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param  srcPts the array containing the source point coordinates.
     * @param  srcOff the offset to the first point to be transformed in the source array.
     * @param  dstPts the array into which the transformed point coordinates are returned.
     *                 May be the same than {@code srcPts}.
     * @param  dstOff the offset to the location of the first transformed point that is
     *                stored in the destination array.
     * @param  numPts the number of point objects to be transformed.
     * @throws TransformException if a point can't be transformed. Some implementations will stop
     *         at the first failure, wile some other implementations will fill the untransformable
     *         points with {@linkplain Double#NaN NaN} values, continue and throw the exception
     *         only at end. Implementations that fall in the later case should set the {@linkplain
     *         TransformException#getLastCompletedTransform last completed transform} to {@code this}.
     */
    @UML(identifier="transformList", specification=OGC_01009)
    void transform(double[] srcPts, int srcOff,
                   double[] dstPts, int dstOff, int numPts) throws TransformException;

    /**
     * Transforms a list of coordinate point ordinal values.
     * This method is provided for efficiently transforming many points.
     * The supplied array of ordinal values will contain packed ordinal
     * values.  For example, if the source dimension is 3, then the ordinals
     * will be packed in this order:
     *
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *  <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param  srcPts the array containing the source point coordinates.
     * @param  srcOff the offset to the first point to be transformed in the source array.
     * @param  dstPts the array into which the transformed point coordinates are returned.
     *                May be the same than {@code srcPts}.
     * @param  dstOff the offset to the location of the first transformed point that is
     *                stored in the destination array.
     * @param  numPts the number of point objects to be transformed.
     * @throws TransformException if a point can't be transformed. Some implementations will stop
     *         at the first failure, wile some other implementations will fill the untransformable
     *         points with {@linkplain Double#NaN NaN} values, continue and throw the exception
     *         only at end. Implementations that fall in the later case should set the {@linkplain
     *         TransformException#getLastCompletedTransform last completed transform} to {@code this}.
     */
    void transform(float[] srcPts, int srcOff,
                   float[] dstPts, int dstOff, int numPts) throws TransformException;

    /**
     * Transforms a list of coordinate point ordinal values.
     * This method is provided for efficiently transforming many points.
     * The supplied array of ordinal values will contain packed ordinal
     * values.  For example, if the source dimension is 3, then the ordinals
     * will be packed in this order:
     *
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *  <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param  srcPts the array containing the source point coordinates.
     * @param  srcOff the offset to the first point to be transformed in the source array.
     * @param  dstPts the array into which the transformed point coordinates are returned.
     * @param  dstOff the offset to the location of the first transformed point that is
     *                stored in the destination array.
     * @param  numPts the number of point objects to be transformed.
     * @throws TransformException if a point can't be transformed. Some implementations will stop
     *         at the first failure, wile some other implementations will fill the untransformable
     *         points with {@linkplain Double#NaN NaN} values, continue and throw the exception
     *         only at end. Implementations that fall in the later case should set the {@linkplain
     *         TransformException#getLastCompletedTransform last completed transform} to {@code this}.
     *
     * @since GeoAPI 2.2
     */
    void transform(float [] srcPts, int srcOff,
                   double[] dstPts, int dstOff, int numPts) throws TransformException;

    /**
     * Transforms a list of coordinate point ordinal values.
     * This method is provided for efficiently transforming many points.
     * The supplied array of ordinal values will contain packed ordinal
     * values.  For example, if the source dimension is 3, then the ordinals
     * will be packed in this order:
     *
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *  <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param  srcPts the array containing the source point coordinates.
     * @param  srcOff the offset to the first point to be transformed in the source array.
     * @param  dstPts the array into which the transformed point coordinates are returned.
     * @param  dstOff the offset to the location of the first transformed point that is
     *                stored in the destination array.
     * @param  numPts the number of point objects to be transformed.
     * @throws TransformException if a point can't be transformed. Some implementations will stop
     *         at the first failure, wile some other implementations will fill the untransformable
     *         points with {@linkplain Double#NaN NaN} values, continue and throw the exception
     *         only at end. Implementations that fall in the later case should set the {@linkplain
     *         TransformException#getLastCompletedTransform last completed transform} to {@code this}.
     *
     * @since GeoAPI 2.2
     */
    void transform(double[] srcPts, int srcOff,
                   float [] dstPts, int dstOff, int numPts) throws TransformException;

    /**
     * Gets the derivative of this transform at a point. The derivative is the
     * matrix of the non-translating portion of the approximate affine map at
     * the point. The matrix will have dimensions corresponding to the source
     * and target coordinate systems. If the input dimension is <var>M</var>,
     * and the output dimension is <var>N</var>, then the matrix will have size
     * <code>N&times;M</code>. The elements of the matrix
     *
     *              <code>{e<sub>n,m</sub> : n=0..(N-1)}</code>
     *
     * form a vector in the output space which is parallel to the displacement
     * caused by a small change in the <var>m</var>'th ordinate in the input space.
     * <p>
     * For example, if the input dimension is 4 and the
     * output dimension is 3, then a small displacement
     *
     * <code>(x<sub>0</sub>,&nbsp;x<sub>1</sub>,&nbsp;x<sub>2</sub>,&nbsp;x<sub>3</sub>)</code>
     *
     * in the input space will result in a displacement
     *
     * <code>(y<sub>0</sub>,&nbsp;y<sub>1</sub>,&nbsp;y<sub>2</sub>)</code>
     *
     * in the output space computed as below (<code>e<sub>n,m</sub></code>
     * are the matrix's elements):
     *
     * <pre>
     * [ y<sub>0</sub> ]     [ e<sub>00</sub>  e<sub>01</sub>  e<sub>02</sub>  e<sub>03</sub> ] [ x<sub>0</sub> ]
     * [ y<sub>1</sub> ]  =  [ e<sub>10</sub>  e<sub>11</sub>  e<sub>12</sub>  e<sub>13</sub> ] [ x<sub>1</sub> ]
     * [ y<sub>2</sub> ]     [ e<sub>20</sub>  e<sub>21</sub>  e<sub>22</sub>  e<sub>23</sub> ] [ x<sub>2</sub> ]
     *    <sub> </sub>          <sub>  </sub>   <sub>  </sub>   <sub>  </sub>   <sub>  </sub>   [ x<sub>3</sub> ]
     * </pre>
     *
     * @param  point The coordinate point where to evaluate the derivative. Null
     *         value is accepted only if the derivative is the same everywhere.
     *         For example affine transform accept null value since they produces
     *         identical derivative no matter the coordinate value. But most map
     *         projection will requires a non-null value.
     * @return The derivative at the specified point (never {@code null}).
     *         This method never returns an internal object: changing the matrix
     *         will not change the state of this math transform.
     * @throws NullPointerException if the derivative dependents on coordinate
     *         and {@code point} is {@code null}.
     * @throws MismatchedDimensionException if {@code point} doesn't have
     *         the expected dimension.
     * @throws TransformException if the derivative can't be evaluated at the
     *         specified point.
     */
    @UML(identifier="derivative", specification=OGC_01009)
    Matrix derivative(final DirectPosition point)
            throws MismatchedDimensionException, TransformException;

    /**
     * Creates the inverse transform of this object. The target of the inverse transform
     * is the source of the original. The source of the inverse transform is the target
     * of the original. Using the original transform followed by the inverse's transform
     * will result in an identity map on the source coordinate space, when allowances for
     * error are made. This method may fail if the transform is not one to one. However,
     * all cartographic projections should succeed.
     *
     * @return The inverse transform.
     * @throws NoninvertibleTransformException if the transform can't be inversed.
     */
    @UML(identifier="inverse", specification=OGC_01009)
    MathTransform inverse() throws NoninvertibleTransformException;

    /**
     * Tests whether this transform does not move any points.
     *
     * @return {@code true} if this {@code MathTransform} is
     *         an identity transform; {@code false} otherwise.
     */
    @UML(identifier="isIdentity", specification=OGC_01009)
    boolean isIdentity();

    /**
     * Returns a <cite>Well Known Text</cite> (WKT) for this object. Well know text are
     * <A HREF="../doc-files/WKT.html">defined in extended Backus Naur form</A>.
     * This operation may fails if an object is too complex for the WKT format capability.
     *
     * @return The <A HREF="../doc-files/WKT.html"><cite>Well Known Text</cite> (WKT)</A> for this object.
     * @throws UnsupportedOperationException If this object can't be formatted as WKT.
     */
    @UML(identifier="getWKT", specification=OGC_01009)
    String toWKT() throws UnsupportedOperationException;
}
