package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

/**
 * Used to create Axis keys based on a fixed set of machine readable strings.
 * <p>
 * Fixed set of strings are represented as an Enumeration in the GeoTools library. This
 * forms a data dictionary (or "formal vocabulary") that is well defined and closed
 * to further additions.
 * <p>
 * This is a great tool for dealing well defined keys such as RGB or CMKY; for most
 * data products you may wish to consider a CodeList instead.
 * <p>
 * Please note that this Measure is considered Dimensionless; if your keys actually belong
 * to a domain you should use the appropriate value and Unit to define them.
 * 
 * @param <V> Enumeration provided a fixed set of machine readable strings
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/impl/range/EnumMeasure.java $
 */
public class EnumMeasure<V extends Enum<V>> extends Measure<V, Dimensionless> {
	private static final long serialVersionUID = 2403097126807167994L;
	private V value;

	/** Create a measure out of the provided Enum */
	private EnumMeasure( V value ){
		this.value = value;
	}
	@Override
	public double doubleValue(Unit<Dimensionless> unit) {
		return value.ordinal();
	}

	@Override
	public Unit<Dimensionless> getUnit() {
		return Unit.ONE;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public Measure<V, Dimensionless> to(Unit<Dimensionless> unit) {
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnumMeasure<?> other = (EnumMeasure<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	/**
	 * Create a Measure wrapping around the provided Enumeration.
	 * @param <E>
	 * @param enumeration
	 * @return
	 */
	public static <E extends Enum<E>> EnumMeasure<E> valueOf( E enumeration ){
		return new EnumMeasure<E>( enumeration );
	}
	public static <E extends Enum<E>> List<Measure<E,Dimensionless>> valueOf( Class<E> elementType ){
		return valueOf( EnumSet.allOf( elementType ));
	}
	
	public static <E extends Enum<E>> List<Measure<E,Dimensionless>> valueOf( EnumSet<E> set ){
		List<Measure<E,Dimensionless>> list = new ArrayList<Measure<E,Dimensionless>>();
		for( E entry : set ){
			list.add( new EnumMeasure<E>( entry ));
		}
		return list;
	}
}
