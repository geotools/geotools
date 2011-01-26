/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Color;

import org.geotools.referencing.piecewise.DomainElement1D;
import org.geotools.referencing.piecewise.PiecewiseTransform1DElement;



/**
 * {@link ColorMapTransformElement}s are a special type of
 * {@link PiecewiseTransform1DElement}s that can be used to generate specific renderings
 * as the result of specific transformations applied to the input values.
 * 
 * <p>
 * A popular example is represented by a {@link DomainElement1D} used to classify
 * values which means applying a color to all the pixels of an image whose value
 * falls in the {@link DomainElement1D}'s range.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL$
 */
public interface ColorMapTransformElement extends PiecewiseTransform1DElement {
	/**
	 * Returns the set of colors for this category. Change to the returned array
	 * will not affect this category.
	 * 
	 * @see GridSampleDimension#getColorModel
	 */
	public Color[] getColors();
}
