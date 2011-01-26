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

import org.geotools.util.NumberRange;

/**
 * The {@link ConstantColorMapElement} is a special type of
 * {@link ColorMapTransformElement} that is used to render no data values.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @todo simplify
 */
class ConstantColorMapElement extends LinearColorMapElement
		implements ColorMapTransformElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4754147707013696371L;

	ConstantColorMapElement(CharSequence name, final Color color,
			final NumberRange<? extends Number> inRange, final int outVal)
			throws IllegalArgumentException {
		super(name, new Color[] { color }, inRange, NumberRange.create(outVal,
				outVal));
	}
	
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final short value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final int value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final float value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}

	
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final double value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}




}
