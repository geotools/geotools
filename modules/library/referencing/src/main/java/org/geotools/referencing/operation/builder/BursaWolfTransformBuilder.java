/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.GeocentricTranslation;

/**
 * Builds {@linkplain org.geotools.api.referencing.operation.MathTransform MathTransform} setup as BursaWolf
 * transformation from a list of {@linkplain org.geotools.referencing.operation.builder.MappedPosition MappedPosition}.
 * The calculation uses least square method. Calculated parameters can be used for following operations:
 *
 * <p>
 *
 * <p>The equations:
 *
 * <pre> X = q * R * x  +  T ,             </pre>
 *
 * Where X is the Matrix of destination points, q is the scale, R is the rotation Matrix, x is the Matrix of source
 * points and T is matrix of translation. Expressing the errors, we get this:
 *
 * <pre>        Err =  A * Dx + l </pre>
 *
 * where Err is the Error Matrix, A is Matrix of derivations, Dx is Matrix of difference changes of 7 parameters, and l
 * is value of DX, DY, DZ for calculated from approximate values. Using the least square method to minimalize the errors
 * we get this result:
 *
 * <pre>
 *  Dx = (A<sup>T</sup>A)<sup>-1</sup> A<sup>T</sup>l  </pre>
 *
 * @since 2.4
 * @version 14
 * @version $Id$
 * @author Jan Jezek
 */
public class BursaWolfTransformBuilder extends MathTransformBuilder {
    /** The Geodetic Datum of target reference system */
    private GeodeticDatum targetDatum;

    /** Matrix of source points */
    GeneralMatrix x;

    /** Matrix of destination points. */
    GeneralMatrix X;

    /** Bursa Wolf rotation in arc radians. */
    private double alfa = 0;

    /** Bursa Wolf rotation in arc radians. */
    private double beta = 0;

    /** Bursa Wolf rotation in arc radians. */
    private double gamma = 0;

    /** Bursa Wolf shift in meters. */
    private double dx = 0;

    /** Bursa Wolf shift in meters. */
    private double dy = 0;

    /** Bursa Wolf shift in meters. */
    private double dz = 0;

    /** Bursa Wolf scaling. */
    private double q = 1;

    /**
     * Creates a BursaWolfTransformBuilder.
     *
     * @param vectors list of {@linkplain MappedPosition mapped positions}.
     */
    public BursaWolfTransformBuilder(final List<MappedPosition> vectors) {
        super.setMappedPositions(vectors);

        x = new GeneralMatrix(vectors.size(), 3);
        X = new GeneralMatrix(vectors.size(), 3);
        x = getx();
        X = getX();
        this.getDxMatrix();
    }

    /**
     * Returns the minimum number of points required by this builder, which is 3.
     *
     * @return the minimum number of points required by this builder which is 3.
     */
    @Override
    public int getMinimumPointCount() {
        return 3;
    }

    /**
     * Returns the dimension for {@link #getSourceCRS source} and {@link #getTargetCRS target} CRS, which is 2.
     *
     * @return dimension for {@linkplain #getSourceCRS source} and {@link #getTargetCRS target} CRS, which is 2.
     */
    @Override
    public int getDimension() {
        return 3;
    }

    /**
     * Returns the required coordinate system type, which is {@linkplain CartesianCS cartesian CS}.
     *
     * @return coordinate system type
     */
    @Override
    public Class<? extends CartesianCS> getCoordinateSystemType() {
        return CartesianCS.class;
    }

    /**
     * Fills the x matrix by coordinates of source points.
     *
     * @return x matrix.
     */
    protected GeneralMatrix getx() {
        final Position[] sourcePoints = getSourcePoints();
        GeneralMatrix x = new GeneralMatrix(3 * sourcePoints.length, 1);

        for (int j = 0; j < x.getNumRow(); j = j + 3) {
            x.setElement(j, 0, sourcePoints[j / 3].getCoordinate()[0]);
            x.setElement(j + 1, 0, sourcePoints[j / 3].getCoordinate()[1]);
            x.setElement(j + 2, 0, sourcePoints[j / 3].getCoordinate()[2]);
        }

        return x;
    }

    /**
     * Fills the x matrix by coordinates of destination points.
     *
     * @return the X matrix
     */
    protected GeneralMatrix getX() {
        final Position[] sourcePoints = getSourcePoints();
        final Position[] targetPoints = getTargetPoints();
        GeneralMatrix X = new GeneralMatrix(3 * sourcePoints.length, 1);

        for (int j = 0; j < X.getNumRow(); j = j + 3) {
            X.setElement(j, 0, targetPoints[j / 3].getCoordinate()[0]);
            X.setElement(j + 1, 0, targetPoints[j / 3].getCoordinate()[1]);
            X.setElement(j + 2, 0, targetPoints[j / 3].getCoordinate()[2]);
        }

        return X;
    }

    /**
     * Generates rotation matrix around X axis.
     *
     * @return rotation Matrix
     */
    protected GeneralMatrix getRalfa() {
        GeneralMatrix Ralfa = new GeneralMatrix(3, 3);
        double[] m0 = {1, 0, 0};
        double[] m1 = {0, Math.cos(alfa), Math.sin(alfa)};
        double[] m2 = {0, -Math.sin(alfa), Math.cos(alfa)};
        Ralfa.setRow(0, m0);
        Ralfa.setRow(1, m1);
        Ralfa.setRow(2, m2);

        return Ralfa;
    }

    /**
     * Generates rotation matrix around Y axis.
     *
     * @return rotation Matrix.
     */
    protected GeneralMatrix getRbeta() {
        GeneralMatrix Rbeta = new GeneralMatrix(3, 3);
        double[] m0 = {Math.cos(beta), 0, -Math.sin(beta)};
        double[] m1 = {0, 1, 0};
        double[] m2 = {Math.sin(beta), 0, Math.cos(beta)};
        Rbeta.setRow(0, m0);
        Rbeta.setRow(1, m1);
        Rbeta.setRow(2, m2);

        return Rbeta;
    }

    /**
     * Generates rotation matrix around Z axis.
     *
     * @return rotation Matrix.
     */
    protected GeneralMatrix getRgamma() {
        GeneralMatrix Rgamma = new GeneralMatrix(3, 3);
        double[] m0 = {Math.cos(gamma), Math.sin(gamma), 0};
        double[] m1 = {-Math.sin(gamma), Math.cos(gamma), 0};
        double[] m2 = {0, 0, 1};
        Rgamma.setRow(0, m0);
        Rgamma.setRow(1, m1);
        Rgamma.setRow(2, m2);

        return Rgamma;
    }

    /**
     * Generates partial derivative with respect to alfa.
     *
     * @return Matrix, that represents partial derivation of rotation Matrix with respect to alfa.
     */
    protected GeneralMatrix getDRalfa() {
        GeneralMatrix dRa = new GeneralMatrix(3, 3);

        double[] m0 = {0, 0, 0};
        double[] m1 = {0, -Math.sin(alfa), Math.cos(alfa)};
        double[] m2 = {0, -Math.cos(alfa), -Math.sin(alfa)};

        dRa.setRow(0, m0);
        dRa.setRow(1, m1);
        dRa.setRow(2, m2);

        dRa.mul(dRa, getRbeta());
        dRa.mul(dRa, getRgamma());

        return specialMul(dRa, x);
    }

    /**
     * Generates partial derivative with respect to beta.
     *
     * @return Matrix, that represents partial derivation of rotation Matrix with respect to beta.
     */
    protected GeneralMatrix getDRbeta() {
        // GeneralMatrix dRbeta = new GeneralMatrix(3 * sourcePoints.size(), 1);
        GeneralMatrix dRb = new GeneralMatrix(3, 3);
        double[] m0 = {-Math.sin(beta), 0, -Math.cos(beta)};
        double[] m1 = {0, 0, 0};
        double[] m2 = {Math.cos(beta), 0, -Math.sin(beta)};
        dRb.setRow(0, m0);
        dRb.setRow(1, m1);
        dRb.setRow(2, m2);

        dRb.mul(getRalfa(), dRb);
        dRb.mul(dRb, getRgamma());

        return specialMul(dRb, x);
    }

    /**
     * Generates partial derivative with respect to gamma.
     *
     * @return Matrix, that represents partial derivation of rotation Matrix with respect to gamma.
     */
    protected GeneralMatrix getDRgamma() {
        //	GeneralMatrix dRgamma = new GeneralMatrix(3 * sourcePoints.size(), 1);
        GeneralMatrix dRg = new GeneralMatrix(3, 3);
        GeneralMatrix pom = new GeneralMatrix(3, 3);
        double[] m0 = {-Math.sin(gamma), Math.cos(gamma), 0};
        double[] m1 = {-Math.cos(gamma), -Math.sin(gamma), 0};
        double[] m2 = {0, 0, 0};
        dRg.setRow(0, m0);
        dRg.setRow(1, m1);
        dRg.setRow(2, m2);

        pom.mul(getRalfa(), getRbeta());
        dRg.mul(pom, dRg);

        return specialMul(dRg, x);
    }

    /**
     * Generates partial derivative in q (scale factor).
     *
     * @return rotation Matrix.
     */
    protected GeneralMatrix getDq() {
        //	GeneralMatrix Dq = new GeneralMatrix(3 * sourcePoints.size(), 1);
        GeneralMatrix R = new GeneralMatrix(3, 3);
        R.mul(getRalfa(), getRbeta());
        R.mul(R, getRgamma());

        return specialMul(R, x);
    }

    /**
     * Calculates the matrix of errors from aproximate values of prameters.
     *
     * @return the l matrix.
     */
    protected GeneralMatrix getl() {
        GeneralMatrix l = new GeneralMatrix(3 * getMappedPositions().size(), 1);
        GeneralMatrix R = new GeneralMatrix(3, 3);
        GeneralMatrix T = new GeneralMatrix(3, 1, new double[] {-dx, -dy, -dz});
        GeneralMatrix qMatrix = new GeneralMatrix(1, 1, new double[] {q});
        GeneralMatrix qx = new GeneralMatrix(X.getNumRow(), X.getNumCol());
        qx.mul(x, qMatrix);
        R.mul(getRalfa(), getRbeta());
        R.mul(getRgamma());

        l.sub(specialMul(R, qx), X);
        l = specialSub(T, l);

        return l;
    }

    /**
     * Method for multiplying matrix (3,3) by matrix of coordintes (3 number of coordinates,1)
     *
     * @param R ratrix
     * @param x matrix
     * @return matrix
     */
    protected GeneralMatrix specialMul(GeneralMatrix R, GeneralMatrix x) {
        GeneralMatrix dRx = new GeneralMatrix(3 * getMappedPositions().size(), 1);

        for (int i = 0; i < x.getNumRow(); i = i + 3) {
            GeneralMatrix subMatrix = new GeneralMatrix(3, 1);
            x.copySubMatrix(i, 0, 3, 1, 0, 0, subMatrix);
            subMatrix.mul(R, subMatrix);
            subMatrix.copySubMatrix(0, 0, 3, 1, i, 0, dRx);
        }

        return dRx;
    }

    /**
     * Method for addition matrix (3,3) with matrix of coordintes (3 number of coordinates,1)
     *
     * @param R ratrix
     * @param x matrix
     * @return matrix
     */
    private GeneralMatrix specialSub(GeneralMatrix R, GeneralMatrix x) {
        GeneralMatrix dRx = new GeneralMatrix(3 * getMappedPositions().size(), 1);

        for (int i = 0; i < x.getNumRow(); i = i + 3) {
            GeneralMatrix subMatrix = new GeneralMatrix(3, 1);
            x.copySubMatrix(i, 0, 3, 1, 0, 0, subMatrix);
            subMatrix.sub(R, subMatrix);
            subMatrix.copySubMatrix(0, 0, 3, 1, i, 0, dRx);
        }

        return dRx;
    }

    /**
     * Glues the submatrix of derivations into the A matrix.
     *
     * @return A mtarix
     */
    protected GeneralMatrix getA() {
        final int size = getMappedPositions().size();
        GeneralMatrix A = new GeneralMatrix(3 * size, 7);
        GeneralMatrix DT = new GeneralMatrix(3, 3);

        // the partial derivative with respect to dx,dy,dz.
        double[] m0 = {1, 0, 0};
        double[] m1 = {0, 1, 0};
        double[] m2 = {0, 0, 1};
        DT.setRow(0, m0);
        DT.setRow(1, m1);
        DT.setRow(2, m2);

        for (int i = 0; i < A.getNumRow(); i = i + 3) {
            DT.copySubMatrix(0, 0, 3, 3, i, 0, A);
        }

        getDRalfa().copySubMatrix(0, 0, 3 * size, 1, 0, 3, A);
        getDRbeta().copySubMatrix(0, 0, 3 * size, 1, 0, 4, A);
        getDRgamma().copySubMatrix(0, 0, 3 * size, 1, 0, 5, A);
        getDq().copySubMatrix(0, 0, 3 * size, 1, 0, 6, A);

        return A;
    }

    /**
     * Returns array of doubles of transformation parameters (dx, dy, dz, ex, ey, ez, scale).
     *
     * @return array of doubles of transformation parameters (dx, dy, dz, ex, ey, ez, scale).
     */
    protected double[] getParameters() {
        return getDxMatrix().getElements()[0];
    }

    /**
     * Method that claculates the parameters by iteration. The tolarance is set to 1 10<sub>-8</sub> and max ï¿½number
     * of steps is set to 20.
     *
     * @return Matrix of parameters (dx, dy, dz, ex, ey, ez, scale).
     */
    public GeneralMatrix getDxMatrix() {
        return getDxMatrix(0.00000001, 20);
    }

    /**
     * Iterates the parameters..
     *
     * @param tolerance for iteration steps (the max difference between last two steps)
     * @param maxSteps highest number of iteations.
     * @return GeneralMatrix of calculated parameters.
     */
    @SuppressWarnings("ShortCircuitBoolean") // see while() statement
    private GeneralMatrix getDxMatrix(double tolerance, int maxSteps) {
        // Matriix of new calculated coefficeients
        GeneralMatrix xNew = new GeneralMatrix(7, 1);

        // Matrix of coefficients claculated in previous iteration
        GeneralMatrix xOld = new GeneralMatrix(7, 1);

        // diference between each steps of old iteration
        GeneralMatrix dxMatrix = new GeneralMatrix(7, 1);

        GeneralMatrix zero = new GeneralMatrix(7, 1);
        zero.setZero();

        // i is a number of iterations
        int i = 0;

        // cicle for iteration
        do {
            xOld.set(new double[] {dx, dy, dz, alfa, beta, gamma, q});

            GeneralMatrix A = getA();
            GeneralMatrix l = getl();

            GeneralMatrix AT = A.clone();
            AT.transpose();

            GeneralMatrix ATA = new GeneralMatrix(7, 7);
            GeneralMatrix ATl = new GeneralMatrix(7, 1);

            // dx = A^T * A  * A^T * l
            ATA.mul(AT, A);
            ATA.invert();
            ATl.mul(AT, l);

            dxMatrix.mul(ATA, ATl);

            // New values of x = dx + previous values
            xOld.negate();
            xNew.sub(dxMatrix, xOld);

            // New values are setup for another iteration
            dx = xNew.getElement(0, 0);
            dy = xNew.getElement(1, 0);
            dz = xNew.getElement(2, 0);
            alfa = xNew.getElement(3, 0);
            beta = xNew.getElement(4, 0);
            gamma = xNew.getElement(5, 0);
            q = xNew.getElement(6, 0);

            i++;
        } while ((!dxMatrix.equals(zero, tolerance) & (i < maxSteps)));

        xNew.transpose();

        return xNew;
    }

    /**
     * Coverts radians to seconds.
     *
     * @param rad Angle in radians
     * @return Angle is seconds
     */
    private static double radiansToSeconds(double rad) {
        return (rad * (180 / Math.PI) * 3600);
    }

    /**
     * Returns Bursa Wolf Transformation parameters.
     *
     * @param Datum The target datum for this parameters.
     * @return parameters the BursaWolfParameters
     */
    public BursaWolfParameters getBursaWolfParameters(GeodeticDatum Datum) {
        BursaWolfParameters parameters = new BursaWolfParameters(Datum);
        parameters.dx = dx;
        parameters.dy = dy;
        parameters.dz = dz;
        parameters.ex = -radiansToSeconds(alfa);
        parameters.ey = -radiansToSeconds(beta);
        parameters.ez = -radiansToSeconds(gamma);
        parameters.ppm = (q - 1) * 1000000;

        return parameters;
    }

    public void setTargetGeodeticDatum(GeodeticDatum gd) {
        this.targetDatum = gd;
    }

    /**
     * Returns MathtTransform setup as BursaWolf transformation.
     *
     * @return calculated MathTransform
     * @throws FactoryException when the size of source and destination point is not the same or if the number of points
     *     is too small to define such transformation.
     */
    @Override
    protected MathTransform computeMathTransform() throws FactoryException {
        return new GeocentricTranslation(getBursaWolfParameters(targetDatum));
    }
}
