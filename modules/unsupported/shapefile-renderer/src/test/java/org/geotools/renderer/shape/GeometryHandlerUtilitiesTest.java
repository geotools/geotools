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
package org.geotools.renderer.shape;

import java.awt.geom.AffineTransform;

import junit.framework.TestCase;

import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;


/**
 *
 *
 * @source $URL$
 */
public class GeometryHandlerUtilitiesTest extends TestCase {
    private static final double ACCURACY = 0.00000001;
    public static final AffineTransform at = AffineTransform.getScaleInstance(2,
            .5);
    public static final String ALBERS = "PROJCS[\"BC_Albers\",GEOGCS[\"GCS_North_American_1983\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS_1980\",6378137,298.257222101],TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],PROJECTION[\"Albers\"],PARAMETER[\"False_Easting\",1000000],PARAMETER[\"False_Northing\",0],PARAMETER[\"Central_Meridian\",-126],PARAMETER[\"Standard_Parallel_1\",50],PARAMETER[\"Standard_Parallel_2\",58.5],PARAMETER[\"Latitude_Of_Origin\",45],UNIT[\"Meter\",1]]";
    public static final MathTransform CANT_TRANSFORM_3RD_ELEMENT = new MathTransform() {
            public int getDimSource() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getSourceDimensions() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getDimTarget() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getTargetDimensions() {
                // TODO Auto-generated method stub
                return 0;
            }

            public DirectPosition transform(DirectPosition arg0,
                DirectPosition arg1)
                throws MismatchedDimensionException, TransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public void transform(double[] src, int srcOffset, double[] dest,
                    int destOffset, int numPoints) throws TransformException {
                	for (int i = srcOffset; i < srcOffset + numPoints * 2; i++) {
    					if(i == 4 || i == 5)
    					    throw new TransformException("boom");
    				}
                }

            public void transform(float[] arg0, int arg1, float[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public void transform(float[] arg0, int arg1, double[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public void transform(double[] arg0, int arg1, float[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public Matrix derivative(DirectPosition arg0)
                throws MismatchedDimensionException, TransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public MathTransform inverse()
                throws NoninvertibleTransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public boolean isIdentity() {
                // TODO Auto-generated method stub
                return false;
            }

            public String toWKT() throws UnsupportedOperationException {
                // TODO Auto-generated method stub
                return null;
            }
        };

    public static final MathTransform CANT_TRANSFORM_FIRST_ELEMENT = new MathTransform() {
            public int getDimSource() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getSourceDimensions() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getDimTarget() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getTargetDimensions() {
                // TODO Auto-generated method stub
                return 0;
            }

            public DirectPosition transform(DirectPosition arg0,
                DirectPosition arg1)
                throws MismatchedDimensionException, TransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public void transform(double[] src, int srcOffset, double[] dest,
                int destOffset, int numPoints) throws TransformException {
            	for (int i = srcOffset * 2; i < srcOffset + numPoints * 2; i++) {
					if(i == 0 || i == 1)
						throw new TransformException("boom");
				}
            }

            public void transform(float[] arg0, int arg1, float[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public void transform(float[] arg0, int arg1, double[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public void transform(double[] arg0, int arg1, float[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public Matrix derivative(DirectPosition arg0)
                throws MismatchedDimensionException, TransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public MathTransform inverse()
                throws NoninvertibleTransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public boolean isIdentity() {
                // TODO Auto-generated method stub
                return false;
            }

            public String toWKT() throws UnsupportedOperationException {
                // TODO Auto-generated method stub
                return null;
            }
        };

    public static final MathTransform NEVER_TRANSFORM = new MathTransform() {
            public int getDimSource() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getSourceDimensions() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getDimTarget() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getTargetDimensions() {
                // TODO Auto-generated method stub
                return 0;
            }

            public DirectPosition transform(DirectPosition arg0,
                DirectPosition arg1)
                throws MismatchedDimensionException, TransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public void transform(double[] arg0, int arg1, double[] arg2,
                int arg3, int arg4) throws TransformException {
                throw new TransformException("exception");
            }

            public void transform(float[] arg0, int arg1, float[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public void transform(float[] arg0, int arg1, double[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public void transform(double[] arg0, int arg1, float[] arg2,
                int arg3, int arg4) throws TransformException {
                // TODO Auto-generated method stub
            }

            public Matrix derivative(DirectPosition arg0)
                throws MismatchedDimensionException, TransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public MathTransform inverse()
                throws NoninvertibleTransformException {
                // TODO Auto-generated method stub
                return null;
            }

            public boolean isIdentity() {
                // TODO Auto-generated method stub
                return false;
            }

            public String toWKT() throws UnsupportedOperationException {
                // TODO Auto-generated method stub
                return null;
            }
        };

    public void testTransform() throws Exception {
        double[] src = new double[] { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5 };
        double[] dest = new double[8];

        MathTransform mt = ReferencingFactoryFinder.getMathTransformFactory(null)
                                        .createAffineTransform(new GeneralMatrix(
                    at));
        GeometryHandlerUtilities.transform(ShapeType.ARC, mt, src, dest, dest.length / 2);

        assertEquals(2d, dest[0], ACCURACY);
        assertEquals(.5d, dest[1], ACCURACY);
        assertEquals(4d, dest[2], ACCURACY);
        assertEquals(1d, dest[3], ACCURACY);
        assertEquals(6d, dest[4], ACCURACY);
        assertEquals(1.5d, dest[5], ACCURACY);
        assertEquals(8d, dest[6], ACCURACY);
        assertEquals(2d, dest[7], ACCURACY);

        src = new double[] { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5 };
        dest = new double[8];
        mt = NEVER_TRANSFORM;

        try {
            GeometryHandlerUtilities.transform(ShapeType.ARC, mt, src, dest, dest.length / 2);
            assertFalse("Shouldn't get here", true);
        } catch (Exception e) {
            //correct
        }

        dest = new double[] { 1, 1, 2, 2, 3, 3, 4, 4 };
        mt = CANT_TRANSFORM_FIRST_ELEMENT;
        GeometryHandlerUtilities.transform(ShapeType.POLYGON, mt, src, dest, dest.length / 2);

        assertEquals(2d, dest[0], ACCURACY);
        assertEquals(2d, dest[1], ACCURACY);
        assertEquals(2d, dest[2], ACCURACY);
        assertEquals(2d, dest[3], ACCURACY);
        assertEquals(3d, dest[4], ACCURACY);
        assertEquals(3d, dest[5], ACCURACY);
        assertEquals(4d, dest[6], ACCURACY);
        assertEquals(4d, dest[7], ACCURACY);

        dest = new double[] { 1, 1, 2, 2, 3, 3, 4, 4 };
        mt = CANT_TRANSFORM_3RD_ELEMENT;
        GeometryHandlerUtilities.transform(ShapeType.POLYGON, mt, src, dest, dest.length / 2);
        assertEquals(1d, dest[0], ACCURACY);
        assertEquals(1d, dest[1], ACCURACY);
        assertEquals(2d, dest[2], ACCURACY);
        assertEquals(2d, dest[3], ACCURACY);
        assertEquals(2d, dest[4], ACCURACY);
        assertEquals(2d, dest[5], ACCURACY);
        assertEquals(4d, dest[6], ACCURACY);
        assertEquals(4d, dest[7], ACCURACY);
    }
}
