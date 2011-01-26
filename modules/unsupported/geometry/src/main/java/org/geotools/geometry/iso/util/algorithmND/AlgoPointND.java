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
package org.geotools.geometry.iso.util.algorithmND;

/**
 * This class offers methods to process basic operations on double arrays in n-dimensional space.
 * In majority, these algorithms are non-robust in sense that floating-point rounding errors may occur.
 * 
 * @author Jackson Roehrig and Sanjay Jena
 *
 *
 * @source $URL$
 */
public class AlgoPointND {

	/**
	 * Returns the square of the distance between two points
	 * 
	 * @param p0
	 * @param p1
	 * @return double
	 */
	public static double getDistanceSquare(double[] p0, double[] p1) {
		double sum = 0.0;
		for (int i=0, n=p0.length; i<n; ++i) {
			double a = p1[i] - p0[i];
			sum += a*a;
		}
		return sum;
	}
	/**
	 * Returns the distance between two points
	 * 
	 * @param p0
	 * @param p1
	 * @return double
	 */
	public static double getDistance(double[] p0, double[] p1) {
		// OK
		double sum = 0.0;
		for (int i=0, n=p0.length; i<n; ++i) {
			double a = p1[i] - p0[i];
			sum += a*a;
		}
		return Math.sqrt(sum);
	}
	
	/**
	 * Returns the distance from the coordinate represented by <code>p0</code> to the origin of the coordinate system.
	 * 
	 * Note: This method is NON-ROBUST due to floating-point rounding errors.
	 *       Inexact results according to floating-point rounding errors may be caused.
	 * 
	 * @param p0
	 * @return double
	 */
	public static double getDistanceToOrigin(double[] p0) {
		// Test OK
		double sum = 0.0;
		for (int i=0, n=p0.length; i<n; ++i) {
			sum += p0[i]*p0[i];
		}
		return Math.sqrt(sum);
	}
	
//	/**
//	 * @param p0
//	 * @return double
//	 */
//	public static double getDistanceToOriginSquare(double[] p0) {
//		double sum = 0.0;
//		for (int i=0, n=p0.length; i<n; ++i) {
//			sum += p0[i]*p0[i];
//		}
//		return sum;
//	}
	
	/**
	 * Substracts a coordinate <code>p0</code> from a coordinate <code>p1</code>.
	 * 
	 * Note: This method is NON-ROBUST due to floating-point rounding errors.
	 *       Inexact results according to floating-point rounding errors may be caused.
	 * 
	 * @param p0
	 * @param p1
	 * @return p1 - p0 : double[]
	 */
	public static double[] subtract(double[] p0, double[] p1) {
		// Test OK
		int n = p0.length;
		double result[]= new double[n];
		for (int i=0; i<n; ++i) {
			result[i]= p1[i] - p0[i];
		}
		return result;
	}

	/**
	 * Adds a coordinate to another coordinate
	 * 
	 * @param p0
	 * @param p1
	 * @return double[]
	 */
	public static double[] add(double[] p0, double[] p1) {
		// Test OK
		int n = p0.length;
		double result[]= new double[n];
		for (int i=0; i<n; ++i) {
			result[i]= p0[i] + p1[i];
		}
		return result;
	}

//	/**
//	 * @param p0
//	 * @param factor 
//	 * @return double[]
//	 */
//	public static double[] add(double[] p0, double factor) {
//		int n = p0.length;
//		double result[]= new double[n];
//		for (int i=0; i<n; ++i) {
//			result[i]= p0[i] + factor;
//		}
//		return result;
//	}

    /**
     * @param p0
     * @param factor
     * @return double[]
     */
    public static double[] scale(double[] p0, double factor){
		int n = p0.length;
		double result[]= new double[n];
		for (int i=0; i<n; ++i) {
			result[i]= p0[i]*factor;
		}
		return result;
    }
	

	/**
	 * @param c0
	 * @param c1
	 * @param maxSpacingSquare is the square of the max. allowed spacing
	 * @return only the intermediate coordinates
	 */
	public static double[][] split(double[] c0, double[] c1, double maxSpacingSquare) {
		// TODO Test
		// TODO Documentation
		double distSquare = getDistanceSquare(c0,c1);
		if (distSquare>maxSpacingSquare) {
			int n = (int)Math.ceil(Math.sqrt( distSquare/ maxSpacingSquare));
			double r = 1.0 / n;
			double[][] result = new double[n-1][];
			for (int i=1; i<n; ++i) {
				result[i-1] =  evaluate(c0, c1, i*r);
				//result[i] =  evaluate(c0, c1, i*r);
			}
			return result;
		}
		return null;
	}
	
//	/**
//	 * @param c0
//	 * @param n
//	 * @return double[]
//	 */
//	public static double[] divide(double[] c0, int n) {
//		int nc = c0.length;
//		double[] result = new double[nc];
//		for (int i=0; i<nc; ++i) {
//			result[i] = c0[i]/n;
//		}
//		return result;
//	}
	
	/**
	 * Interpolation of a straigt line given by two coordinates <code>c0</code> and <code>c1</code>.
	 * The method will return a double array which describes the coordinate at distance <code>r</code>
	 * on this straight line, whith a parametrisation of 0.0 at <code>c0</code> and 1.0 at <code>c1</code>.
	 * 
	 * Note: This method is NON-ROBUST due to floating-point rounding errors.
	 *       Inexact results according to floating-point rounding errors may be caused.
	 * 
	 * @param c0
	 * @param c1
	 * @param r
	 * @return double[]
	 */
	public static double[] evaluate(double[] c0, double[] c1, double r) {
		// ok
		int n = c0.length;
		double[] result = new double[n];
		double s = 1.0 - r;
		for (int i=0; i<n; ++i) {
			result[i] = s * c0[i] + r * c1[i];
		}
		return result;
	}
	
//	/**
//	 * @param c0
//	 * @param c1
//	 * @param r
//	 * @return double[]
//	 */
//	public static double[] evaluate(double[] c0, double[] c1, double[] r) {
//		int n = c0.length;
//		double[] result = new double[n];
//		for (int i=0; i<n; ++i) {
//			result[i] = (1.0 - r[i]) * c0[i] + r[i] * c1[i];
//		}
//		return result;
//	}
	
	/**
	 * Normalizes a vector which begins at the origin and ends at <code>p</code> to length 1
	 * 
	 * Note: This method is NON-ROBUST due to floating-point rounding errors.
	 *       Inexact results according to floating-point rounding errors may be caused.
	 * 
	 * @param p the end point of the vector
	 * 
	 * @return double[] End point of the normalized vector
	 * @return NULL if the result is not valid (for example when <code>p</code> is the origin)
	 */
	public static double[] normalize(double[] p) {
		// Test OK
        double len = getDistanceToOrigin(p);
        double[] rCoord = null;
        if ( len > 0.0 ) {
        	rCoord = scale(p,1.0 / len);
        } else {
    		for (int i=0, n=p.length; i<n; ++i) {
    			p[i] = 0.0;
    		}
        }
        return rCoord;
	}

}
