package org.geotools.coverage.io.impl.range;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/impl/range/BandIndexMeasure.java $
 */
public class BandIndexMeasure extends Measure<String, Dimensionless>
		implements Measurable<Dimensionless> {
	private static final long serialVersionUID = 3895010709415779953L;

	private Long index = null;

	private String bandMnemonic = null;

	public BandIndexMeasure(final int index, final String bandMnemonic) {
		this.index = (long) index;
		this.bandMnemonic = bandMnemonic != null ? bandMnemonic : Long
				.toString(this.index);
	}

	public double doubleValue(Unit<Dimensionless> value) {
		return index;
	}

	public long longValue(Unit<Dimensionless> value) throws ArithmeticException {
		return index;
	}

	public int compareTo(Measurable<Dimensionless> o) {
		return this.index.compareTo(o.longValue(Unit.ONE));
	}

	@Override
	public Unit<Dimensionless> getUnit() {
		return Unit.ONE;
	}

	@Override
	public String getValue() {
		return this.bandMnemonic;
	}

	@Override
	public Measure<String, Dimensionless> to(Unit<Dimensionless> target) {
		if (target.isCompatible(Unit.ONE))
			return new BandIndexMeasure(this.index.intValue(),
					this.bandMnemonic);
		final StringBuilder buffer = new StringBuilder();
		buffer.append("Unable to perform requested conversion");
		buffer.append("\nsource UoM:").append("  ").append(Unit.ONE.toString());
		buffer.append("\ntarget UoM:").append("  ").append(target.toString());
		throw new ConversionException(buffer.toString());

	}
}
