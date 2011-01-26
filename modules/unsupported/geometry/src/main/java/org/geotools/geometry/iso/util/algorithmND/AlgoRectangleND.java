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
 * @author roehrig
 *
 *
 * @source $URL$
 */
public class AlgoRectangleND {
	
	/**
	 * Verifies whether a n-D point lays within the envelope, or at the border or corner of the envelope.
	 * 
	 * @param minCoord Lower Corner of the envelope
	 * @param maxCoord Upper Corner of the envelope
	 * @param coord Coordinate which has to be tested whether it´s within the envelope or not
	 * 
	 * @return TRUE, if the Coordinate lays within the envelope
	 */
	public static boolean contains(double minCoord[], double maxCoord[], double[] coord) {
		// Method tested - ok (SJ) - see JUNIT tests
		for (int i = 0, n = minCoord.length; i < n; ++i) {
			if ((coord[i] < minCoord[i]) || (coord[i] > maxCoord[i]))
				return false;
		}
		return true;
	}


	/**
	 * Verifies whether another envelope intersects with this envelope
	 * @param minCoord0 
	 * @param maxCoord0 
	 * @param minCoord1 
	 * @param maxCoord1 
	 * @return TRUE, if envelopes intersect; FALSE, if they dont intersect
	 */
	public static boolean intersects(
			double minCoord0[], double maxCoord0[],
			double minCoord1[], double maxCoord1[]) {
		// Method corrected and tested - ok (SJ) - see JUNIT tests
		
//		for (int i = 0, n = minCoord0.length; i < n; ++i)
//			if (contains(minCoord0, maxCoord0, minCoord1) || contains(minCoord0, maxCoord0, minCoord1))
//				return true;
//		for (int i = 0, n = minCoord0.length; i < n; ++i)
//			if (contains(minCoord1, maxCoord1, minCoord0) || contains(minCoord1, maxCoord1, minCoord0))
//				return true;

		for (int i = 0, n = minCoord0.length; i < n; ++i) {
			if (minCoord1[i] > maxCoord0[i] || maxCoord1[i] < minCoord0[i])
				return false;
		}
		return true;
		
	}

//	 AUSKOMMENTIERT, DA NICHT GENUTZT, NICHT KOMMENTIERT UND NICHT GETESTET (SJ)
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @return double
//	 */
//	public static double maxLength(double minCoord[], double maxCoord[]) {
//		double result = 0.0;
//		for (int i = 1, n = minCoord.length; i < n; ++i) {
//			if ( (maxCoord[i]-minCoord[i])>result ) result = maxCoord[i]-minCoord[i];
//		}
//		return result;
//	}

//	 AUSKOMMENTIERT, DA NICHT GENUTZT UND NICHT GETESTET (SJ)
//	/**
//	 * Expands the envelope with a direct Position 
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @param coord 
//	 */
//	public static void expand(double minCoord[], double maxCoord[], double coord[]) {
//		for (int i = 0, n = minCoord.length; i < n; ++i) {
//			if (coord[i] < minCoord[i]) minCoord[i] = coord[i];
//			if (coord[i] > maxCoord[i]) maxCoord[i] = coord[i];
//		}
//	}

//	 AUSKOMMENTIERT, DA NICHT GENUTZT, NICHT KOMMENTIERT UND NICHT GETESTET (SJ)
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @param coord
//	 */
//	public static void add(double minCoord[], double maxCoord[], double[] coord) {
//		for (int i = 0, n = minCoord.length; i < n; ++i) {
//			if ( coord[i] < minCoord[i]) minCoord[i] = coord[i];
//			if ( coord[i] > maxCoord[i]) maxCoord[i] = coord[i];
//		}
//	}

// AUSKOMMENTIERT, DA NICHT GENUTZT, NICHT KOMMENTIERT UND NICHT GETESTET (SJ)
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @return double[]
//	 */
//	public static double[] center(double minCoord[], double maxCoord[]) {
//		return AlgoPointND.scale(AlgoPointND.add(minCoord,maxCoord),0.5);
//	}
	

// AUSKOMMENTIERT DA FEHLERHAFT (NICHT ROBUST) s. JUNIT TESTS (SJ)
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @param factor
//	 */
//	public static void scale(double minCoord[], double maxCoord[], double factor) {
//		if (factor <= 0.0 || factor == 1.0) return;
//		double[] center = center(minCoord, maxCoord);
//		double[] resultMin = AlgoPointND.subtract(center,minCoord);
//		double[] resultMax = AlgoPointND.subtract(center,maxCoord);
//		resultMin = AlgoPointND.scale(resultMin, factor);
//		resultMax = AlgoPointND.scale(resultMax, factor);
//		resultMin = AlgoPointND.add(center, resultMin);
//		resultMax = AlgoPointND.add(center, resultMax);
//		for (int i=0, n=minCoord.length; i<n; ++i) {
//			minCoord[i] = resultMin[i];
//			maxCoord[i] = resultMax[i];
//		}
//	}


//	 AUSKOMMENTIERT, DA NICHT GETESTET UND NICHT DOKUMENTIERT (SJ)
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @param coord 
//	 * @return boolean
//	 */
//	public static boolean touches(double minCoord[], double maxCoord[], double[] coord) {
//		for (int i = 0, n = minCoord.length; i < n; ++i) {
//			if ((Math.abs(coord[i] - minCoord[i]) <= 0.0)
//					|| (Math.abs(coord[i] - maxCoord[i]) <= 0.0)) {
//				return true;
//			}
//		}
//		return false;
//	}

// AUSKOMMENTIERT, DA NICHT GETESTET UND NICHT DOKUMENTIERT (SJ)
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @param coord 
//	 * @param side
//	 * @return boolean
//	 */
//	public static boolean touches(double minCoord[], double maxCoord[], double[] coord, int side) {
//		// side = 0: left and right
//		// side = 1: bottom and top
//		// side = 2: front and back
//		int n = minCoord.length;
//		assert (side < n);
//		double coords = coord[side];
//		double min = minCoord[side];
//		double max = maxCoord[side];
//		return ((Math.abs(coords - min) <= 0.0) || 
//				(Math.abs(coords - max) <= 0.0));
//	}


//	 AUSKOMMENTIERT, DA NICHT GETESTET UND NICHT DOKUMENTIERT (SJ), DESWEITEREN HAT ES DIE SELBE FUNKTION GELIEFERT WIE DIE METHODE "contains".
//	/**
//	 * @param minCoord 
//	 * @param maxCoord 
//	 * @param p 
//	 * @return boolean
//	 */
//	public static boolean intersects(double minCoord[], double maxCoord[], double[] p) {
//		//return touches(minCoord, maxCoord, p) || contains(minCoord, maxCoord, p);
//		return contains(minCoord, maxCoord, p);
//	}
    
}
