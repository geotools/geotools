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

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;

import org.geotools.referencing.piecewise.PiecewiseTransform1D;




/**
 * A {@link ColorMapTransform} is a special sub-interface of
 * {@link PiecewiseTransform1D} that can be used to render raw data.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 * @source $URL$
 */
public interface ColorMapTransform<T extends ColorMapTransformElement> extends PiecewiseTransform1D<T> {
	/**
	 * Retrieve the {@link ColorModel} associated to this
	 * {@link ColorMapTransform}.
	 * 
	 * @return
	 */
	public IndexColorModel getColorModel();

	/**
	 * Retrieve the {@link SampleModel} associated to this
	 * {@link ColorMapTransform}.
	 * 
	 * @return
	 */
	public SampleModel getSampleModel(final int width, final int height);
			
		
}
