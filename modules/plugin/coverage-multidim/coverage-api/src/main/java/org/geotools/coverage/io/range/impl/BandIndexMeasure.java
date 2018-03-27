/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.range.impl;

import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;

import org.apache.commons.beanutils.ConversionException;
import org.geotools.coverage.io.range.Measure;

import tec.uom.se.AbstractUnit;

/**
 * {@link Measurable} subclass suitable for modeling a band index in a multiband
 * image axis.
 * 
 * <p>
 * The band index is a measurement of a {@link Dimensionless} quantity since it
 * is just a convenience ordering.
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public class BandIndexMeasure extends Measure<String, Dimensionless> {
	private static final long serialVersionUID = 3895010709415779953L;

	private Long index = null;

	private String bandMnemonic = null;

	public BandIndexMeasure(final int index, final String bandMnemonic) {
		this.index = (long) index;
		this.bandMnemonic = bandMnemonic != null ? bandMnemonic : Long
				.toString(this.index);
	}
	
	/** 
	 * Returns the value of this band index (dimensionless) as a double
	 * @param value
	 * @return value of this band index
	 */
	public double doubleValue(Unit<Dimensionless> value) {
		return index;
	}
	
	/**
	 * Returns the estimated integral value of this ban index (dimensionless) as a long.
	 * @param value
	 * @return
	 * @throws ArithmeticException
	 */
	public long longValue(Unit<Dimensionless> value) throws ArithmeticException {
		return index;
	}

	public int compareTo( o) {
		return this.index.compareTo(o.longValue(AbstractUnit.ONE));
	}

	@Override
	public Unit<Dimensionless> getUnit() {
		return AbstractUnit.ONE;
	}

	@Override
	public String getValue() {
		return this.bandMnemonic;
	}

	@Override
	public Measure<String, Dimensionless> to(Unit<Dimensionless> target) {
		if (target.isCompatible(AbstractUnit.ONE))
			return new BandIndexMeasure(this.index.intValue(),
					this.bandMnemonic);
		final StringBuilder buffer = new StringBuilder();
		buffer.append("Unable to perform requested conversion");
		buffer.append("\nsource UoM:").append("  ").append(AbstractUnit.ONE.toString());
		buffer.append("\ntarget UoM:").append("  ").append(target.toString());
		throw new ConversionException(buffer.toString());

	}
}
