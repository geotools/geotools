package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

import org.opengis.util.CodeList;

/**
 * Used to create Axis keys based on an open set of machine readable strings.
 * <p>
 * An Open set of strings are represented as a CodeList in the GeoTools library. This
 * forms a data dictionary (or "formal vocabulary") that is constructed at runtime.
 * Often applications use this facility to store a common data dictionary in a shared
 * database or website; while still providing a facility that is familiar to Java 
 * developers. A CodeList walks and talks like an Enumeration; the only difference
 * for java developers is the ability to add new entries (as such it is very important
 * to use a default block in any switch statements concerning code lists.
 * <p>
 * This is a great tool for dealing well defined keys; where the definition is not
 * actually hardcoded into your application.
 * <p>
 * Please note that this Measure is considered Dimensionless; if your keys actually belong
 * to a domain you should use the appropriate value and Unit to define them.
 * 
 * @param <V> CodeList provided an open set of machine readable strings
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/impl/range/CodeMeasure.java $
 */
public class CodeMeasure<V extends CodeList<V>> extends Measure<V, Dimensionless> {
	private static final long serialVersionUID = 2403097126807167994L;
	private V value;

	/** Create a measure out of the provided Enum */
	private CodeMeasure( V value ){
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
		CodeMeasure<?> other = (CodeMeasure<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	/**
	 * Create a Measure wrapping around the provided CodeList element.
	 * @param <E>
	 * @param code
	 * @return
	 */
	public static <C extends CodeList<C>> Measure<C,Dimensionless> valueOf( C code ){
		return new CodeMeasure<C>( code );
	}
	/**
	 * Create a List of Measures representing the provided codes.
	 * @param <C>
	 * @param codes
	 * @return
	 */
	public static <C extends CodeList<C>> List<Measure<C,Dimensionless>> valueOf( Collection<C> codes ){
		List<Measure<C,Dimensionless>> list = new ArrayList<Measure<C,Dimensionless>>();
		for( C entry : codes ){
			list.add( new CodeMeasure<C>( entry ));
		}
		return list;
	}
	public static <C extends CodeList<C>> List<Measure<C,Dimensionless>> valueOf( C[] codes ){
		return valueOf( Arrays.asList( codes ));
	}
}
