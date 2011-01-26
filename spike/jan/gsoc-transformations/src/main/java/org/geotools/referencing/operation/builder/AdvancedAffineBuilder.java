/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.MismatchedSizeException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;


/**
 * Builder for affine transformation with possibility to set several constrains
 * for affine parameters that will be respected during calculation. This is convenient
 * for example to use when you want affine transformation with skew parameter equal to zero.
 *
 * @author jezekjan
 * @since
 * @source $URL$
 * @version $Id$
 */
public class AdvancedAffineBuilder extends MathTransformBuilder {
    /**mark for key to specify sx - scale in x constrain */
    public static final String SX = "sx";

    /**mark for key to specify sy - scale in y constrain */
    public static final String SY = "sy";

    /**mark for key to specify sxy - skew  constrain */
    public static final String SXY = "sxy";

    /**mark for key to specify phix - rotation constrain */
    public static final String PHIX = "phix";
    
    /**mark for key to specify phix - rotation constrain */
    public static final String PHIY = "phiy";

    /**mark for key to specify tx - translation in x constrain */
    public static final String TX = "tx";

    /**mark for key to specify ty - translation in y constrain */
    public static final String TY = "ty";

    /** translation in x */
    private double tx;

    /** translation in y */
    private double ty;

    /** scale in x */
    private double sx;

    /** scale in y */
    private double sy;

    /** x rotation in radians */
    private double phix;
    
    /** x rotation in radians */
    private double phiy;

    /** Map of constrains - parameter name as key and its required value*/
    private Map<String, Double> valueConstrain = new HashMap<String, Double>();
    
    /** Map of constrains - parameters (represented by string) are equal to each other*/
    private Map<String, String> equalConstrain = new HashMap<String, String>();


    /**Affine transformation for approximate values*/
    private final AffineTransform2D affineTrans;

    /**
     * Constructs builder from set of GCPs
     * @param vectors GCPs
     */
    public AdvancedAffineBuilder(final List<MappedPosition> vectors)
        throws MismatchedSizeException, MismatchedDimensionException,
            MismatchedReferenceSystemException, FactoryException {
        /**
         * use constructor with approximate values taken from 6 parameters of affine transform
         */       
        this(vectors, (AffineTransform2D) (new AffineTransformBuilder(vectors).getMathTransform()));
    }

    /**
     * Constructs affine transform from GCPs and approximate values for calculation. This constructor
     * should be used when the default calculation is divergating.
     * @param vectors GCPs
     * @param affineTrans approximate affine transformation
     * @throws MismatchedSizeException
     * @throws MismatchedDimensionException
     * @throws MismatchedReferenceSystemException
     * @throws FactoryException
     */
    public AdvancedAffineBuilder(final List<MappedPosition> vectors, AffineTransform2D affineTrans)
        throws MismatchedSizeException, MismatchedDimensionException,
            MismatchedReferenceSystemException, FactoryException {
        super.setMappedPositions(vectors);

        /**
         * sets approximate values
         */
        this.affineTrans = affineTrans;

        AffineToGeometric a2g = new AffineToGeometric(affineTrans);

        sx = a2g.getXScale();
        sy = a2g.getYScale();
        
        phix = a2g.getXRotation();
        phiy = a2g.getYRotation();
        tx = a2g.getXTranslate();
        ty = a2g.getYTranslate();

    }

    /**
     * Sets constrain that {@code param} is equal to {@code value}.  Be aware that the calculation may diverge in the case you set some values
     * that are not 'close' to approximate values. I the case of divergence you can set approximate
     * values using proper constructor
     * @param param parameter name - set one of AdvancedAffineBuilder static variables.
     * @param value required value
     */
    public void setConstrain(String param, double value) {
        valueConstrain.put(param, value);
    }
    
    /**
     * 
     * @param param
     * @param param
     */
    public void setEqualConstrain(String param1, String param2) {
        equalConstrain.put(param1, param2);
    }

    /**
     * Clears all constrains
     */
    public void clearConstrains() {
        valueConstrain.clear();
    }

    /**
     * Generates A matrix (Matrix of derivation of affine equation). Each column is derivation by
     * each transformation parameter (sx, sy, sxy, phi, tx,ty). The rows are derivations of fx and fy.
     *
     * @return A matrix
     */
    protected GeneralMatrix getA() {
        GeneralMatrix A = new GeneralMatrix(2 * this.getMappedPositions().size(), 6);

        double cosphix = Math.cos(phix);
        double sinphix = Math.sin(phix);
        
        double cosphiy = Math.cos(phiy);
        double sinphiy = Math.sin(phiy);

        /**
         * Each row is calculated with values of proper GCPs
         */
        for (int j = 0; j < (A.getNumRow() / 2); j++) {
            double x = getSourcePoints()[j].getOrdinate(0);
            double y = getSourcePoints()[j].getOrdinate(1);

            /*************************
             * 
             * Derivation X
             * 
             **************************/
            double dxsx = cosphix*x;
                       
            double dxsy = - sinphiy * y;
                 
            double dxphix = -sx*sinphix* x;
            
            double dxphiy = -sy*cosphiy* y ;
            
            double dxtx = 1;
            
            double dxty = 0;

            /*************************
             * 
             * Derivation Y
             * 
             ***********************/
            double dysx = sinphix * x;
                       
            double dysy = cosphiy * y;
                 
            double dyphix =  sx*cosphix*x;
            
            double dyphiy = -sy*sinphiy* y ;
            
            double dytx = 0;
            
            double dyty = 1;
         
            A.setRow(j,                   new double[] { dxsx, dxsy, dxphix, dxphiy, dxtx, dxty });
            A.setRow(A.getNumRow()/2 + j, new double[] { dysx, dysy, dyphix, dyphiy, dytx, dyty });
        }

        return A;
    }

    /**
     * Fill L matrix. This matrix contains differences between expected value and value
     * calculated from affine parameters
     * @return l matrix
     */
    protected GeneralMatrix getL() {
        GeneralMatrix l = new GeneralMatrix(2 * this.getMappedPositions().size(), 1);

        double cosphix = Math.cos(phix);
        double sinphix = Math.sin(phix);
        double cosphiy = Math.cos(phiy);
        double sinphiy = Math.sin(phiy);

        for (int j = 0; j < (l.getNumRow() / 2); j++) {
            double x = getSourcePoints()[j].getOrdinate(0);
            double y = getSourcePoints()[j].getOrdinate(1);

            /* a1 is target value - transfomed value*/
            double dx = getTargetPoints()[j].getOrdinate(0)
                - (sx*cosphix*x -  sy*sinphiy*y + tx);                
            double dy = getTargetPoints()[j].getOrdinate(1)
                - (sx*sinphix*x + sy*cosphiy*y + ty);            
        
            l.setElement(j, 0, dx);
            l.setElement((l.getNumRow() / 2) + j, 0, dy);
        }

        return l;
    }

    /**
     * Ask for dx matrix with default number of iterations and precision constrain.
     * @return dx matrix
     * @throws FactoryException
     */
    private GeneralMatrix getDxMatrix() throws FactoryException {
        return getDxMatrix(0.000000001, 300);
    }

    /**
     * Get matrix of affine coefficients as a result of LSM.
     * @param tolerance tolerance for iteration.
     * @param maxSteps max steps of iteration
     * @return dx matrix
     * @throws FactoryException
     */
    private GeneralMatrix getDxMatrix(double tolerance, int maxSteps)
        throws FactoryException {
        /**
         * Matrix of new calculated coefficients
         */
        GeneralMatrix xNew = new GeneralMatrix(6, 1);

        /**
         * Matrix of coefficients calculated in previous iteration
         */
        GeneralMatrix xOld = new GeneralMatrix(6, 1);

        /**
         * Difference between each steps of iteration
         */
        GeneralMatrix dxMatrix = new GeneralMatrix(6, 1);

        /**
         * Zero matrix
         */
        GeneralMatrix zero = new GeneralMatrix(6, 1);
        zero.setZero();

        /**
         * Result
         */
        GeneralMatrix xk = new GeneralMatrix(6 + valueConstrain.size(), 1);

        // i is a number of iterations
        int i = 0;

        // iteration
        do {
            xOld.set(new double[] { sx, sy, phix, phiy, tx, ty });

            GeneralMatrix A = getA();
            GeneralMatrix l = getL();

            GeneralMatrix AT = A.clone();
            AT.transpose();

            GeneralMatrix ATA = new GeneralMatrix(6, 6);
            GeneralMatrix ATl = new GeneralMatrix(6, 1);

            ATA.mul(AT, A);
            ATl.mul(AT, l);

            /**constrains**/
            GeneralMatrix AB = createAB(ATA, getB());

            AB.invert();
            AB.negate();

            GeneralMatrix AU = createAU(ATl, getU());
            xk.mul(AB, AU);

            xk.copySubMatrix(0, 0, 6, xk.getNumCol(), 0, 0, dxMatrix);
            dxMatrix.negate();

            // New values of x = dx + previous values
            xOld.negate();
            xNew.sub(dxMatrix, xOld);

            // New values are setup for another iteration
            
            sx = xNew.getElement(0, 0);
            sy = xNew.getElement(1, 0);
            phix = xNew.getElement(2, 0);
            phiy = xNew.getElement(3, 0);
            tx = xNew.getElement(4, 0);
            ty = xNew.getElement(5, 0);

            i++;
      
            if (i > maxSteps) { //&& oldDxMatrix.getElement(0, 0) < dxMatrix.getElement(0, 0)){          	
                throw new FactoryException("Calculation of transformation is divergating");
            }
        } while ((!dxMatrix.equals(zero, tolerance)));

        xNew.transpose();

        return xNew;
    }

    @Override
    public int getMinimumPointCount() {
        return 3;
    }

    /**
     * Fill matrix of derivations of constrains by affine parameters.
     * @return B matrix
     */
    protected GeneralMatrix getB() {
        GeneralMatrix B = new GeneralMatrix(valueConstrain.size(), 6);
        int i = 0;

        if (valueConstrain.containsKey(SX)) {
            B.setRow(i, new double[] { 1, 0, 0, 0, 0, 0 });
            i++;
        }

        if (valueConstrain.containsKey(SY)) {
            B.setRow(i, new double[] { 0, 1, 0, 0, 0, 0 });
            i++;
        }

        if (valueConstrain.containsKey(PHIX)) {
            B.setRow(i, new double[] { 0, 0, 1, 0, 0, 0 });
            i++;
        }

        if (valueConstrain.containsKey(PHIY)) {
            B.setRow(i, new double[] { 0, 0, 0, 1, 0, 0 });
            i++;
        }

        if (valueConstrain.containsKey(TX)) {
            B.setRow(i, new double[] { 0, 0, 0, 0, 1, 0 });
            i++;
        }

        if (valueConstrain.containsKey(TY)) {
            B.setRow(i, new double[] { 0, 0, 0, 0, 0, 1 });
            i++;
        }
        if (valueConstrain.containsKey(SXY)) {
            B.setRow(i, new double[] { 0, 0, -1, 1, 0, 0 });
            i++;
        }

        return B;
    }
   

    /**
     * Fill matrix of constrain values (e.g. for constrain skew = 0 the value is 0)
     * @return U matrix
     */
    protected GeneralMatrix getU() {
        GeneralMatrix U = new GeneralMatrix(valueConstrain.size(), 1);
        int i = 0;

        if (valueConstrain.containsKey(SX)) {
            U.setRow(i, new double[] { -sx + valueConstrain.get(SX) });
            i++;
        }

        if (valueConstrain.containsKey(SY)) {
            U.setRow(i, new double[] { -sy + valueConstrain.get(SY) });
            i++;
        }

        if (valueConstrain.containsKey(PHIX)) {
            U.setRow(i, new double[] { -phix + valueConstrain.get(PHIX)});
            i++;
        }

        if (valueConstrain.containsKey(PHIY)) {
            U.setRow(i, new double[] { -phiy + valueConstrain.get(PHIY) });
            i++;
        }

        if (valueConstrain.containsKey(TX)) {
            U.setRow(i, new double[] { -tx + valueConstrain.get(TX) });
            i++;
        }
        if (valueConstrain.containsKey(SXY)) {
                U.setRow(i, new double[] { (phix-phiy) +  valueConstrain.get(SXY) });
                i++;                
        } else if (valueConstrain.containsKey(TY)) {
            U.setRow(i, new double[] { -ty + valueConstrain.get(TY) });
            i++;
        }

        return U;
    }

    /**
     * Joins A <sup>T</sup> matrix with L
     * @param ATl
     * @param U
     * @return matrix constructs from ATl and U
     */
    private GeneralMatrix createAU(GeneralMatrix ATl, GeneralMatrix U) {
        GeneralMatrix AU = new GeneralMatrix(ATl.getNumRow() + U.getNumRow(), ATl.getNumCol());

        ATl.copySubMatrix(0, 0, ATl.getNumRow(), ATl.getNumCol(), 0, 0, AU);
        U.copySubMatrix(0, 0, U.getNumRow(), U.getNumCol(), ATl.getNumRow(), 0, AU);

        return AU;
    }

    /**
     * Joins A matrix with B.
     * result is:
     * (A B )
     * (B 0 )
     *
     * @param ATA
     * @param B
     * @return matrix constructs from ATA and B
     */
    private GeneralMatrix createAB(GeneralMatrix ATA, GeneralMatrix B) {
        GeneralMatrix BT = B.clone();
        BT.transpose();

        GeneralMatrix AAB = new GeneralMatrix(ATA.getNumRow() + B.getNumRow(),
                ATA.getNumCol() + BT.getNumCol());

        ATA.copySubMatrix(0, 0, ATA.getNumRow(), ATA.getNumCol(), 0, 0, AAB);
        B.copySubMatrix(0, 0, B.getNumRow(), B.getNumCol(), ATA.getNumRow(), 0, AAB);
        BT.copySubMatrix(0, 0, BT.getNumRow(), BT.getNumCol(), 0, ATA.getNumCol(), AAB);

        GeneralMatrix zero = new GeneralMatrix(B.getNumRow(), B.getNumRow());
        zero.setZero();
        zero.copySubMatrix(0, 0, zero.getNumRow(), zero.getNumCol(), B.getNumCol(), B.getNumCol(),
            AAB);

        return AAB;
    }

    /**
     * Calculates coefficients of Projective transformation matrix from geometric parameters.
     *
     * @return Projective Matrix
     * @throws FactoryException
     */
    protected GeneralMatrix getProjectiveMatrix() throws FactoryException {
        GeneralMatrix M = new GeneralMatrix(3, 3);

        /**
         * Runs calculation of parameter values
         */
        double[] param = getDxMatrix().getElements()[0];

        /**
         * calcuates matrix coefficients form geometric coefficients
         */
        double a11 =  sx * Math.cos(phix);
        double a12 = -sy * Math.sin(phiy); 
        double a21 =  sx*  Math.sin(phix);
        double a22 =  sy * Math.cos(phiy);
              
        /**
         * Fill the metrix
         */
        double[] m0 = { a11, a12, param[4] };
        double[] m1 = { a21, a22, param[5] };
        double[] m2 = { 0, 0, 1 };
        M.setRow(0, m0);
        M.setRow(1, m1);
        M.setRow(2, m2);

        return M;
    }
   
    @Override
    protected MathTransform computeMathTransform() throws FactoryException {
       /* if (valueConstrain.size() == 0) {
            return affineTrans;
        }*/

        return ProjectiveTransform.create(getProjectiveMatrix());
    }
}
