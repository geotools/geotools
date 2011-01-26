/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import java.util.List;
import javax.vecmath.MismatchedSizeException;


/**
 * Builds {@linkplain MathTransform
 * MathTransform} setup as Projective transformation from a list of
 * {@linkplain org.geotools.referencing.operation.builder.MappedPosition
 * MappedPosition}. The calculation uses least square method. The Projective
 * transform equation: (2D). The calculation uses least square method.
 * Projective transform equation:<pre>  [ x']   [  m00  m01  m02  ] [ x ]
 *   [ y'] = [  m10  m11  m12  ] [ y ]
 *   [ 1 ]   [  m20  m21    1  ] [ 1 ]                           x' = m * x
 * </pre>In the case that we have more identical points we can write it
 * like this (in Matrix):<pre>
 *  [ x'<sub>1</sub> ]      [ x<sub>1</sub> y<sub>1</sub> 1  0  0  0 -x'x  -x'y]   [ m00 ]
 *  [ x'<sub>2</sub> ]      [ x<sub>2</sub> y<sub>2</sub> 1  0  0  0 -x'x  -x'y]   [ m01 ]
 *  [  .  ]      [             .              ]   [ m02 ]
 *  [  .  ]      [             .              ] * [ m10 ]
 *  [ x'<sub>n</sub> ]   =  [ x<sub>n</sub> y<sub>n</sub> 1  0  0  0 -x'x  -x'y]   [ m11 ]
 *  [ y'<sub>1</sub> ]      [ 0  0  0  x<sub>1</sub> y<sub>1</sub> 1 -y'x  -y'y]   [ m12 ]
 *  [ y'<sub>2</sub> ]      [ 0  0  0  x<sub>2</sub> y<sub>2</sub> 1  -y'x  -y'y]   [ m20 ]
 *  [  .  ]      [             .              ]   [ m21 ]
 *  [  .  ]      [             .              ]
 *  [ y'<sub>n</sub> ]      [ 0  0  0  x<sub>n</sub> y<sub>n</sub> 1  -y'x  -y'y]
 *  x' = A*m </pre>Using the least square method we get this result:
 * <pre><blockquote>
 *  m = (A<sup>T</sup>PA)<sup>-1</sup> A<sup>T</sup>Px'  </blockquote> </pre>
 *
 * @author Jan Jezek
 * @source $URL$
 * @version $Id$
 * @since 2.4
 */
public class ProjectiveTransformBuilder extends MathTransformBuilder {
    /** Matrix of derivations */
    protected GeneralMatrix A;

    /** Matrix of wights */
    protected GeneralMatrix P = null;

    /** Matrix of target values */
    protected GeneralMatrix X;

    protected ProjectiveTransformBuilder() {
    }

    /**
     * Creates ProjectiveTransformBuilder for the set of properties.
     *
     *
     * @param vectors list of {@linkplain MappedPosition
     *                MappedPosition}
     * @throws MismatchedSizeException
     *                 if the number of properties is not set properly.
     * @throws MismatchedDimensionException
     *                 if the dimension of properties is not set properly.
     * @throws MismatchedReferenceSystemException
     *                 -if there is mismatch in coordinate system in
     *                 {@linkplain MappedPosition MappedPosition}
     */
    public ProjectiveTransformBuilder(List <MappedPosition> vectors)
        throws MismatchedSizeException, MismatchedDimensionException,
            MismatchedReferenceSystemException {
        super.setMappedPositions(vectors);
    }

    /**
     * Returns the minimum number of points required by this builder,
     * which is 4 by default. Subclasses like {@linkplain AffineTransformBuilder
     * affine transform builders} will reduce this minimum.
     *
     * @return minimum number of points required by this builder, which is 4 by
     *         default.
     */
    public int getMinimumPointCount() {
        return 4;
    }

    /**
     * Returns the required coordinate system type, which is
     * {@linkplain CartesianCS cartesian CS}.
     *
     * @return required coordinate system type
     */
    public Class <? extends CartesianCS> getCoordinateSystemType() {
        return CartesianCS.class;
    }

    /**
     * Fills P matrix for m = (A<sup>T</sup>PA)<sup>-1</sup>
     * A<sup>T</sup>Px' equation
     *
     * @throws MissingInfoException if accuracy is not defined.
     */
    protected void fillPMatrix() throws MissingInfoException {
        this.P = new GeneralMatrix(getMappedPositions().size() * 2,
                getMappedPositions().size() * 2);

        for (int i = 0; i < getMappedPositions().size(); i = i + 2) {
            if (Double.compare(
                        (((MappedPosition) getMappedPositions().get(i))
                        .getAccuracy()), Double.NaN) == 0) {
                throw new MissingInfoException(
                    "Accuracy has to be defined for all points");
            }

            // weight for x
            P.setElement(i, i,
                1 / ((MappedPosition) getMappedPositions().get(i)).getAccuracy());
            // weight for y
            P.setElement(i + 1, i + 1,
                1 / ((MappedPosition) getMappedPositions().get(i)).getAccuracy());
        }
    }

    /**
     * Fills A matrix for m = (A<sup>T</sup>PA)<sup>-1</sup>
     * A<sup>T</sup>Px' equation
     */
    protected void fillAMatrix() {
        final DirectPosition[] sourcePoints = getSourcePoints();
        final DirectPosition[] targetPoints = getTargetPoints();
        A = new GeneralMatrix(2 * sourcePoints.length, 8);

        int numRow = 2 * sourcePoints.length;

        // fill first half of matrix
        for (int j = 0; j < ((2 * sourcePoints.length) / 2); j++) {
            double xs = sourcePoints[j].getCoordinates()[0];
            double ys = sourcePoints[j].getCoordinates()[1];
            double xd = targetPoints[j].getCoordinates()[0];

            A.setRow(j, new double[] { xs, ys, 1, 0, 0, 0, -xd * xs, -xd * ys });
        }

        // fill second half
        for (int j = numRow / 2; j < numRow; j++) {
            double xs = sourcePoints[j - (numRow / 2)].getCoordinates()[0];
            double ys = sourcePoints[j - (numRow / 2)].getCoordinates()[1];
            double yd = targetPoints[j - (numRow / 2)].getCoordinates()[1];

            A.setRow(j, new double[] { 0, 0, 0, xs, ys, 1, -yd * xs, -yd * ys });
        }
    }

    /**
     * Fills x' matrix for m = (A<sup>T</sup>PA)<sup>-1</sup>
     * A<sup>T</sup>Px' equation
     */
    protected void fillXMatrix() {
        X = new GeneralMatrix(2 * getTargetPoints().length, 1);

        int numRow = X.getNumRow();

        // Creates X matrix
        for (int j = 0; j < (numRow / 2); j++) {
            X.setElement(j, 0, getTargetPoints()[j].getCoordinates()[0]);
        }

        for (int j = numRow / 2; j < numRow; j++) {
            X.setElement(j, 0,
                getTargetPoints()[j - (numRow / 2)].getCoordinates()[1]);
        }
    }

    /**
     * Switch whether to include weights into the calculation. Weights are derived from each point accuracy.
     * Weight p = 1 / accuracy<sup>2<sup>.
     * @param include if true then the weights will be included onto the calculation. False is default.
     * @throws FactoryException if all or some of the {@linkplain #setMappedPositions(List) points} does not have accuracy setup properly.
     */
    public void includeWeights(boolean include) throws MissingInfoException {
        this.P = new GeneralMatrix(getMappedPositions().size() * 2,
                getMappedPositions().size() * 2);

        if (include) {
            fillPMatrix();
        } else {
            for (int j = 0; j < getMappedPositions().size(); j++) {
                P.setElement(j, j, 1);
            }
        }
    }

    /**
     * Calculates the parameters using the least square method. The
     * equation:
     * <pre><blockquote>
     *  m = (A<sup>T</sup>A)<sup>-1</sup> A<sup>T</sup>x'
     *  </blockquote> </pre>
     *
     * @return m matrix.
     */
    protected double[] calculateLSM() {
        fillAMatrix();
        // fillPMatrix();
        fillXMatrix();

        if (P == null) {
            try {
                includeWeights(false);
            } catch (FactoryException e) {
                // should never reach here - weights are not included
            }
        }

        GeneralMatrix AT = (GeneralMatrix) A.clone();
        AT.transpose();

        GeneralMatrix ATP = new GeneralMatrix(AT.getNumRow(), P.getNumCol());
        GeneralMatrix ATPA = new GeneralMatrix(AT.getNumRow(), A.getNumCol());
        GeneralMatrix ATPX = new GeneralMatrix(AT.getNumRow(), 1);
        GeneralMatrix x = new GeneralMatrix(A.getNumCol(), 1);
        ATP.mul(AT, P); // ATP
        ATPA.mul(ATP, A); // ATPA
        ATPX.mul(ATP, X); // ATPX
        ATPA.invert();        
        x.mul(ATPA, ATPX);
        ATPA.invert();

        x.transpose();

        return x.getElements()[0];
    }

    /**
     * Returns the matrix of parameters for Projective transformation.
     * This method should by override for the special cases like affine or
     * similar transformation. The M matrix looks like this:<pre>
     *
     *  [  m00  m01  m02  ]
     *  [  m10  m11  m12  ]
     *  [  m20  m21   1   ]
     * </pre>
     *
     * @return Matrix M
     */
    protected GeneralMatrix getProjectiveMatrix() {
        GeneralMatrix M = new GeneralMatrix(3, 3);

        // double[] param = generateMMatrix();
        double[] param = calculateLSM();
        double[] m0 = { param[0], param[1], param[2] };
        double[] m1 = { param[3], param[4], param[5] };
        double[] m2 = { param[6], param[7], 1 };
        M.setRow(0, m0);
        M.setRow(1, m1);
        M.setRow(2, m2);

        return M;
    }

    protected MathTransform computeMathTransform() {
        return ProjectiveTransform.create(getProjectiveMatrix());
    }
}
