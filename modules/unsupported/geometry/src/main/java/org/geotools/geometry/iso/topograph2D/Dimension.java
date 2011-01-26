/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.topograph2D;

/**
 * Constants representing the dimensions of a point, a curve and a surface.
 * Also, constants representing the dimensions of the empty geometry and
 * non-empty geometries, and a wildcard dimension meaning "any dimension".
 *
 * @source $URL$
 */
public class Dimension {

	/**
	 * Dimension value of a point (0).
	 */
	public final static int P = 0;

	/**
	 * Dimension value of a curve (1).
	 */
	public final static int L = 1;

	/**
	 * Dimension value of a surface (2).
	 */
	public final static int A = 2;

	/**
	 * Dimension value of the empty geometry (-1).
	 */
	public final static int FALSE = -1;

	/**
	 * Dimension value of non-empty geometries (= {P, L, A}).
	 */
	public final static int TRUE = -2;

	/**
	 * Dimension value for any dimension (= {FALSE, TRUE}).
	 */
	public final static int DONTCARE = -3;

	/**
	 * Converts the dimension value to a dimension symbol, for example,
	 * <code>TRUE => 'T'</code> .
	 * 
	 * @param dimensionValue
	 *            a number that can be stored in the
	 *            <code>IntersectionMatrix</code> . Possible values are
	 *            <code>{TRUE, FALSE, DONTCARE, 0, 1, 2}</code>.
	 * @return a character for use in the string representation of an
	 *         <code>IntersectionMatrix</code>. Possible values are
	 *         <code>{T, F, * , 0, 1, 2}</code> .
	 */
	public static char toDimensionSymbol(int dimensionValue) {
		switch (dimensionValue) {
		case FALSE:
			return 'F';
		case TRUE:
			return 'T';
		case DONTCARE:
			return '*';
		case P:
			return '0';
		case L:
			return '1';
		case A:
			return '2';
		}
		throw new IllegalArgumentException("Unknown dimension value: "
				+ dimensionValue);
	}

	/**
	 * Converts the dimension symbol to a dimension value, for example,
	 * <code>'*' => DONTCARE</code> .
	 * 
	 * @param dimensionSymbol
	 *            a character for use in the string representation of an
	 *            <code>IntersectionMatrix</code>. Possible values are
	 *            <code>{T, F, * , 0, 1, 2}</code> .
	 * @return a number that can be stored in the
	 *         <code>IntersectionMatrix</code> . Possible values are
	 *         <code>{TRUE, FALSE, DONTCARE, 0, 1, 2}</code>.
	 */
	public static int toDimensionValue(char dimensionSymbol) {
		switch (Character.toUpperCase(dimensionSymbol)) {
		case 'F':
			return FALSE;
		case 'T':
			return TRUE;
		case '*':
			return DONTCARE;
		case '0':
			return P;
		case '1':
			return L;
		case '2':
			return A;
		}
		throw new IllegalArgumentException("Unknown dimension symbol: "
				+ dimensionSymbol);
	}
}
