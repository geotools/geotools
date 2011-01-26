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
package org.geotools.referencing.operation.builder.algorithm;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

/**
 * Implementation of TPS Interpolation based on thin plate spline (TPS) algorithm
 *
 * @see <A HREF="http://elonen.iki.fi/code/tpsdemo/index.html">Pages about TPS</A>
 *
 * @author jezekjan
 *
 */
public class TPSInterpolation extends AbstractInterpolation {
	
    /**Main matrix (according http://elonen.iki.fi/code/tpsdemo/index.html)*/
    private GeneralMatrix L;

    /**Matrix of target values (according http://elonen.iki.fi/code/tpsdemo/index.html)*/
    private GeneralMatrix V;

    /** Helper constant for generating matrix dimensions*/
    private final int number = super.getPositions().size();
    
    private final GeneralMatrix result;

    public TPSInterpolation(HashMap positions) {
    	super(positions);
    	  L = new GeneralMatrix(number + 3, number + 3);

          fillKsubMatrix();
          fillPsubMatrix();
          fillOsubMatrix();

          L.invert();

          GeneralMatrix V = fillVMatrix(0);
          result = new GeneralMatrix(number + 3, 1);
          result.mul(L, V);
        
    }

    /**
     *
     * @param positions HashMap containing {@link org.opengis.geometry.DirectPosition} as
     * key and value of general parameter as value
     * @param dx Value of step in x direction between generated cells
     * @param dy Value of step in y direction between generated cells
     * @param envelope Envelope that should be filled by generated grid
     */
  /*  public TPSInterpolation(HashMap positions, double dx, double dy, Envelope envelope) {
        super(positions, dx, dy, envelope);

        L = new GeneralMatrix(number + 3, number + 3);

        fillKsubMatrix();
        fillPsubMatrix();
        fillOsubMatrix();

        L.invert();

        GeneralMatrix V = fillVMatrix(0);
        result = new GeneralMatrix(number + 3, 1);
        result.mul(L, V);
    }*/ 
    
   public TPSInterpolation(Map<DirectPosition, Float> positions, int xNumOfCells, int yNumOfCells, Envelope envelope) {
       super(positions, xNumOfCells, yNumOfCells, envelope);

    L = new GeneralMatrix(number + 3, number + 3);

    fillKsubMatrix();
    fillPsubMatrix();
    fillOsubMatrix();

    L.invert();

    GeneralMatrix V = fillVMatrix(0);
    result = new GeneralMatrix(number + 3, 1);
    result.mul(L, V);
    }
    public float getValue(DirectPosition p) {
        // TODO Auto-generated method stub
        return calculateTPSFunction(result, p);
    }

    /**
     * Computes target point using TPS formula.
     * @param v matrix of useful coefficients
     * @param p position where we want the value
     * @return calculated shift
     */
    private float calculateTPSFunction(GeneralMatrix v, DirectPosition p) {
        double a1 = v.getElement(v.getNumRow() - 3, 0);
        double a2 = v.getElement(v.getNumRow() - 2, 0);
        double a3 = v.getElement(v.getNumRow() - 1, 0);

        float result;
        double sum = 0;

        Iterator iter = getPositions().keySet().iterator();

        for (int i = 0; i < (v.getNumRow() - 3); i++) {
            double dist = ((Point2D) p).distance((Point2D) iter.next());

            sum = sum + (v.getElement(i, 0) * functionU(dist));
        }

        result = (float) (a1 + (a2 * p.getOrdinate(0)) + (a3 * p.getOrdinate(1)) + sum);

        return result;
    }

    /**
     * Calculates U function for distance
     * @param distance distance
     * @return log(distance)*distance<sub>2</sub> or 0 if distance = 0
     */
    private double functionU(double distance) {
        if (distance == 0) {
            return 0;
        }

        return distance * distance * Math.log(distance);
    }

    /**
     * Calculates U function where distance = ||p_i, p_j|| (from source points)
     * @param p_i p_i
     * @param p_j p_j
     * @return log(distance)*distance<sub>2</sub> or 0 if distance = 0
     */
    private double calculateFunctionU(DirectPosition p_i, DirectPosition p_j) {
        double distance = ((Point2D) p_i).distance((Point2D) p_j);

        return functionU(distance);
    }

    /**
     * Fill K submatrix (<a href="http://elonen.iki.fi/code/tpsdemo/index.html"> see more here</a>)
     */
    private void fillKsubMatrix() {
        double alfa = 0;

        Object[] positions = getPositions().keySet().toArray();

        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                double u = calculateFunctionU((DirectPosition) positions[i],
                        (DirectPosition) positions[j]);
                L.setElement(i, j, u);
                L.setElement(j, i, u);
                alfa = alfa + (u * 2); // same for upper and lower part
            }
        }

        alfa = alfa / (number * number);
    }

    /**
     * Fill L submatrix (<a href="http://elonen.iki.fi/code/tpsdemo/index.html"> see more here</a>)
     */
    private void fillPsubMatrix() {
        Iterator iter = getPositions().keySet().iterator();

        for (int i = 0; i < number; i++) {
            L.setElement(i, i, 0);

            DirectPosition source = (DirectPosition) iter.next();

            L.setElement(i, number + 0, 1);
            L.setElement(i, number + 1, source.getCoordinates()[0]);
            L.setElement(i, number + 2, source.getCoordinates()[1]);

            L.setElement(number + 0, i, 1);
            L.setElement(number + 1, i, source.getCoordinates()[0]);
            L.setElement(number + 2, i, source.getCoordinates()[1]);
        }
    }

    /**
     * Fill O submatrix (<a href="http://elonen.iki.fi/code/tpsdemo/index.html"> see more here</a>)
     */
    private void fillOsubMatrix() {
        for (int i = number; i < (number + 3); i++) {
            for (int j = number; j < (number + 3); j++) {
                L.setElement(i, j, 0);
            }
        }
    }

    /**
     * Fill V matrix (matrix of target values)
     * @param dim 0 for dx, 1 for dy.
     * @return V Matrix
     */
    private GeneralMatrix fillVMatrix(int dim) {
        V = new GeneralMatrix(number + 3, 1);

        Iterator<DirectPosition> iter = getPositions().keySet().iterator();

        for (int i = 0; i < number; i++) {           
            V.setElement(i, 0, getPositions().get(iter.next()));
        }

        V.setElement(V.getNumRow() - 3, 0, 0);
        V.setElement(V.getNumRow() - 2, 0, 0);
        V.setElement(V.getNumRow() - 1, 0, 0);

        return V;
    }
}
