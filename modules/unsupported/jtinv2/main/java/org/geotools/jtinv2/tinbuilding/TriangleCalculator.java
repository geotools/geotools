/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jtinv2.tinbuilding;

import org.geotools.jtinv2.main.InMemoryTinFactory;

/**
 * Calculates the triangles for a TIN. Implementations of this interface are passed
 * to the "calculateTriangles" method of the InMemoryTin class. This is an example
 * of the Visitor Design Pattern.
 */
public interface TriangleCalculator 
{	
	/**
	 * Returns the name for this triangle calculator.
	 */
	public abstract String getName();
	
	/**
	 * Indicates if this triangle calculator supports an outer boundary.
	 */
	public abstract boolean supportsAnOuterBoundary();
	
	/**
	 * Indicates if this triangle calculator supports holes.
	 */
	public abstract boolean supportsHoles();
	
	/**
	 * Indicates if this triangle calculator supports breaklines.
	 */
	public abstract boolean supportsBreaklines();
	
	/**
	 * Calculates the triangles for the InMemoryTin being built by the factory.
	 */
	public abstract void calculateTriangles(InMemoryTinFactory argFactory);
}
