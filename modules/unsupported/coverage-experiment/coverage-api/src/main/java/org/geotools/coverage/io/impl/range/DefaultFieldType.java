package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

public class DefaultFieldType implements FieldType {
	private List<Axis<?, ?>> axes;
	private Name name;
	private InternationalString description;
	private Unit<Quantity> unit;
	private Set<SampleDimension> sampleDimensions;
	/**
	 * 
	 * @param name
	 * @param description
	 * @param unit
	 * @param axes
	 * @param samples
	 */
	public DefaultFieldType(Name name,
			InternationalString description,
			Unit<?> unit,
			List<Axis<?,?>> axes,
			Set<SampleDimension> samples) {
		this.name = name;
		this.description = description;
		this.axes = axes;
		this.sampleDimensions = samples;
	}

	public List<Axis<?,?>> getAxes() {
		return Collections.unmodifiableList(axes);
	}

	public List<Name> getAxesNames() {
		List<Name> list = new ArrayList<Name>();
		for( Axis<?,?> axis : axes ){
			list.add( axis.getName() );
		}
		return list;
	}

	public Axis<?,?> getAxis(Name name) {
		for( Axis<?,?> axis : axes ){
			if( name.equals( axis.getName() )){
				return axis;
			}
		}
		return null;
	}

	public InternationalString getDescription() {
		return description;
	}

	public Name getName() {
		return name;
	}

	public SampleDimension getSampleDimension(Measure<?,?> key) {
		return null; // TODO: need to figure out how to record this association
	}

	public Set<SampleDimension> getSampleDimensions() {
	    if (sampleDimensions!=null)
		return Collections.unmodifiableSet( sampleDimensions );
	    return Collections.emptySet();
	}
	
	/** Unit type for this field */
	public Unit<Quantity> getUnitOfMeasure() {
		return unit; // TODO Is this duplicated with sample dimensions ?
	}

}