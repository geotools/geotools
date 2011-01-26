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
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import java.util.List;

// J2SE and extensions
import javax.vecmath.MismatchedSizeException;


/**
 * Builds {@linkplain org.opengis.referencing.operation.MathTransform
 * MathTransform} setup as Similar transformation from a list of {@linkplain
 * org.geotools.referencing.operation.builder.MappedPosition MappedPosition}.
 * The The calculation uses least square method. The similar transform
 * equation:<pre>                                                  
 *  [ x']   [  a -b  Tx  ] [ x ]   [ a*x - b*y + Tx ]
 *  [ y'] = [  b  a  Ty  ] [ y ] = [ b*x + a*y + Ty ] </pre>In the case
 * that we have more identical points we can write it like this (in Matrix):
 * <pre>                                           
 *  [ x'<sub>1</sub> ]      [ x<sub>1</sub> -y<sub>1</sub>  1 0 ]   [ a  ]
 *  [ x'<sub>2</sub> ]      [ x<sub>2</sub> -y<sub>2</sub>  1 0 ]   [ b  ]
 *  [  .  ]      [      .      ]   [ Tx ]                          
 *  [  .  ]      [      .      ] * [ Ty ]                          
 *  [ x'<sub>n</sub> ]   =  [ x<sub>n</sub>  y<sub>n</sub>  1 0 ]   
 *  [ y'<sub>1</sub> ]      [ y<sub>1</sub>  x<sub>1</sub>  0 1 ]   
 *  [ y'<sub>2</sub> ]      [ y<sub>2</sub>  x<sub>2</sub>  0 1 ]  
 *  [  .  ]      [      .      ]                                      
 *  [  .  ]      [      .      ]                                    
 *  [ y'<sub>n</sub> ]      [ y<sub>n</sub> x<sub>n</sub>  0  1 ]     
 *    x' = A*m  </pre>Using the least square method we get this result:
 * <pre><blockquote>
 *  m = (A<sup>T</sup>A)<sup>-1</sup> A<sup>T</sup>x'  </blockquote> </pre>
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
public class SimilarTransformBuilder extends ProjectiveTransformBuilder {
/**
     * Creates SimilarTransformBuilder for the set of properties. The {@linkplain java.util.List List} of {@linkplain
     * org.geotools.referencing.operation.builder.MappedPosition MappedPosition} is expected.
     *
     * @param vectors list of {@linkplain
     * org.geotools.referencing.operation.builder.MappedPosition MappedPosition}
     */
    public SimilarTransformBuilder(List <MappedPosition> vectors)
        throws MismatchedSizeException, MismatchedDimensionException,
            MismatchedReferenceSystemException {
        super.setMappedPositions(vectors);
    }

    protected void fillAMatrix() {
        super.A = new GeneralMatrix(2 * getSourcePoints().length, 4);

        int numRow = getSourcePoints().length * 2;

        // Creates X matrix
        for (int j = 0; j < (numRow / 2); j++) {
            A.setRow(j,
                new double[] {
                    getSourcePoints()[j].getCoordinates()[0],
                    -getSourcePoints()[j].getCoordinates()[1], 1, 0
                });
        }

        for (int j = numRow / 2; j < numRow; j++) {
            A.setRow(j,
                new double[] {
                    getSourcePoints()[j - (numRow / 2)].getCoordinates()[1],
                    getSourcePoints()[j - (numRow / 2)].getCoordinates()[0], 0,
                    1
                });
        }
    }

    /**
     * Returns the minimum number of points required by this builder,
     * which is 2.
     *
     * @return Returns the minimum number of points required by this builder,
     *         which is 2.
     */
    public int getMinimumPointCount() {
        return 2;
    }

    /**
     * Returns the matrix for Projective transformation setup as
     * Affine. The M matrix looks like this:
     * <pre>                                                       
     * [  a  -b  Tx  ]                           
     * [  b   a  Ty  ]                              
     * [  0   0  1   ]                                                                   
     * </pre>
     *
     * @return Matrix M.
     */
    protected GeneralMatrix getProjectiveMatrix() {
        GeneralMatrix M = new GeneralMatrix(3, 3);        
        double[] param = calculateLSM();
        double[] m0 = { param[0], -param[1], param[2] };
        double[] m1 = { param[1], param[0], param[3] };
        double[] m2 = { 0, 0, 1 };
        M.setRow(0, m0);
        M.setRow(1, m1);
        M.setRow(2, m2);

        return M;
    }
}
