package org.geotools.coverage.io.range;

import java.awt.image.SampleModel;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.RangeAxis.WavelengthAxis;
import org.geotools.feature.NameImpl;
import org.geotools.util.MeasurementRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.util.InternationalString;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 *
 * @param <V>
 * @param <QA>
 */
public abstract class RangeAxisBin<V> {//implements Comparable<RangeAxisBin<V>>{

	/**
	 * Implementation of {@link RangeAxis} for multibands images.
	 * 
	 * <p>
	 * This implementation of RangeAxis can be seen as a stub implementation since in
	 * this case we do not really have an {@link RangeAxis} for this kind of data, or
	 * rather we have an rangeAxis that just represents an ordinal or a certain set of .
	 * 
	 * @author Simone Giannecchini, GeoSolutions
	 * @todo add convenience constructor based on {@link SampleDimension} and or
	 *       {@link SampleModel}
	 */
	public static class StringAxisBin extends RangeAxisBin<String> {

	    /**
	     * 
	     */
	    public StringAxisBin(final Name name,final InternationalString description,final RangeAxis rangeAxis, final String bandName) {
	    	super(name,description,rangeAxis,bandName );
	    }


	    

		/**
	     * @see org.geotools.coverage.io.range.RangeAxis#getCoordinateReferenceSystem()
	     */
	    public SingleCRS getCoordinateReferenceSystem() {
	        return null;
	    }


	    /**
	     * @see org.geotools.coverage.io.range.RangeAxis#getUnitOfMeasure()
	     */
	    public Unit<Dimensionless> getUnitOfMeasure() {
	        return Unit.ONE;
	    }

	}

	 /**
	  * A special {@link RangeAxisBin} that can be used to indicate bin over a {@link WavelengthAxis}.
	  * 
	  * @author Simone Giannecchini, GeoSolutions SAS
	  *
	  */
	public static class WavelengthBin extends RangeAxisBin<MeasurementRange<Double>>{
				
//		public int compareTo(RangeAxisBin<MeasurementRange<Double>> o) {
//			if(!o.value.getUnits().isCompatible(this.getUnit()))
//				throw new IllegalArgumentException();
//			return 0;
//		}
		/**
		 * 
		 */
		private static final long serialVersionUID = -3977921692927799401L;
		
		public WavelengthBin( Name name, double value, InternationalString description, WavelengthAxis axis ){
			super(
					name,
					description,
					axis,
					MeasurementRange.create(value, value, axis.getUnitOfMeasure()));
			
		}
	
		public WavelengthBin( String name, double value, String description, WavelengthAxis axis ){
			super(
					new NameImpl(name),
					new SimpleInternationalString(description),
					axis,
					MeasurementRange.create(value, value, axis.getUnitOfMeasure()));
			
		}		
		
		
		public WavelengthBin( Name name, double from, double to, InternationalString description, WavelengthAxis axis ) {
			super(
					name,
					description,
					axis,
					MeasurementRange.create(from, to, axis.getUnitOfMeasure()));
		}
		public WavelengthBin( String name, double from, double to, String description, WavelengthAxis axis ) {
			super(
					new NameImpl(name),
					new SimpleInternationalString(description),
					axis,
					MeasurementRange.create(from, to, axis.getUnitOfMeasure()));
		}		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 227920699316120413L;
	
	/**
	 * The {@link RangeAxisBin} instance that is used as a reference by this {@link RangeAxisBin}.
	 */
	private RangeAxis rangeAxis;

	public RangeAxisBin(
			final Name name, 
			final InternationalString description, 
			final RangeAxis rangeAxis,
			final V value) {
		this.description = description;
		this.name = name;
		this.rangeAxis=rangeAxis;
		this.value=value;
	}
	
	public RangeAxisBin(
			final Name name, 
			final RangeAxis rangeAxis,
			final V value) {
		this(name, new SimpleInternationalString(name.getLocalPart()), rangeAxis, value);
	}
	
	public RangeAxisBin(
			final String name, 
			final String description, 
			final RangeAxis rangeAxis,
			final V value) {
		this(new NameImpl(name), new SimpleInternationalString(description), rangeAxis, value);
	}
	
	public RangeAxisBin(
			final String name, 
			final RangeAxis rangeAxis,
			final V value) {
		this(new NameImpl(name), new SimpleInternationalString(name), rangeAxis, value);
	}
	
	
	
	private V value;
	private InternationalString description;
	private Name name;
	public InternationalString getDescription(){
		return description;
	}

	public Name getName(){
		return name;
	}


	public Unit<?> getUnit() {
		return rangeAxis.getUnitOfMeasure();
	}

	public V getValue() {
		return value;
	}
	
	public RangeAxis getAxis(){
		return rangeAxis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rangeAxis == null) ? 0 : rangeAxis.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RangeAxisBin other = (RangeAxisBin) obj;
		if (rangeAxis == null) {
			if (other.rangeAxis != null)
				return false;
		} else if (!rangeAxis.equals(other.rangeAxis))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		final StringBuilder builder= new StringBuilder();
		builder.append("RangeAxis  bin description").append("\n");
		builder.append("Name:").append("\t\t\t\t\t").append(name.toString()).append("\n");
		builder.append("Description:").append("\t\t\t\t").append(description.toString()).append("\n");
		builder.append("Value:").append("\t\t\t\t\t").append(value.toString()).append("\n");
		builder.append("RangeAxis:").append("\n").append(rangeAxis.toString()).append("\n");
		return builder.toString();
	}

	public boolean belongsTo(final RangeAxis rangeAxis){
		return rangeAxis.equals(this.rangeAxis);
	}
	
	public boolean compatibleWith(final RangeAxis rangeAxis){
		return rangeAxis.compatibleWith(this.rangeAxis);
	}
	
}
