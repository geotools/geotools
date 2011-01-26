package org.geotools.coverage.io.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 * @param <V>
 * @param <QA>
 */
public class RangeDescriptor {
	/**
	 * The {@link Name} for this {@link RangeDescriptor}.
	 */
	private Name name;
	
	/**
	 * The description for this {@link RangeDescriptor}.
	 */
	private InternationalString description;

	
	private List<? extends BandDescriptor> bandDescriptors;

	/**
	 * The {@link List} of {@link RangeAxis} for this {@link RangeDescriptor}.
	 */
	private List <? extends RangeAxis>axes;

	/**
	 * The list of {@link Name}s for the {@link RangeAxis} instances of this {@link RangeDescriptor}.
	 */
	private List<Name> axesNames;

	private  List<? extends RangeAxisBin <?>> defaultAxisBins;


	/**
	 * 
	 * @param name
	 * @param description
	 * @param unit
	 * @param axes
	 * @param samples
	 */
	public RangeDescriptor(
			final Name fieldName,
	        final InternationalString fieldDescription,
	        final List<? extends BandDescriptor> bands,
	        final List<? extends RangeAxis> axes,	
	        final List<? extends RangeAxisBin<?>>  defaultAxisBins  		
	        ) {

	    this.axes = axes;
	    axesNames = new ArrayList<Name>(axes.size());
	    for (RangeAxis rangeAxis : axes) {
	        axesNames.add(rangeAxis.getName());
	    }
	    this.defaultAxisBins=defaultAxisBins;		
	    this.name = fieldName;
	    this.description = fieldDescription;
        this.bandDescriptors = bands;
	}
	/**
	 * Simple Implementation of toString method for debugging purpose.
	 */
	public String toString(){
	    final StringBuilder sb = new StringBuilder();
	    final String lineSeparator = System.getProperty("line.separator", "\n");
	    sb.append("FIELD TYPE description:").append(lineSeparator);
	    sb.append("Name:").append("\t\t").append(name.toString()).append(lineSeparator);
	    sb.append("Description:").append("\t").append(description.toString()).append(lineSeparator);
        sb.append("BandDescriptors: ").append(lineSeparator);
	    for (BandDescriptor b : bandDescriptors) {
	        sb.append(b.toString()).append(lineSeparator);
	    }      
	    sb.append("Axes: ").append(lineSeparator);
	    for (RangeAxis rangeAxis : axes) {
	        sb.append(rangeAxis.toString()).append(lineSeparator);
	    }        
        sb.append(lineSeparator);    
	    return sb.toString();
	}

	/**
	 * Get the description of the {@link RangeDescriptor}
	 * 
	 * @return description of the {@link RangeDescriptor}
	 */
	public InternationalString getDescription() {
		return description;
	}

	
	public  BandDescriptor getBandDescriptor(final int index) {
		return bandDescriptors.get(index);
	}
	/**
	 * {@link List} of all the axes of the {@link RangeDescriptor}
	 * 
	 * @return a {@link List} of all the {@link RangeAxis} instances for this
	 *         {@link RangeDescriptor}
	 */
	public List<? extends RangeAxis> getAxes() {
		return Collections.unmodifiableList(axes);
	}
	/**
	 * {@link List} of all the {@link RangeAxis} instances
	 * {@link org.opengis.feature.type.Name}s.
	 * 
	 * @return a {@link List} of all the {@link RangeAxis} instances
	 *         {@link org.opengis.feature.type.Name}s.
	 */
	public List<Name> getAxesNames() {
	    return Collections.unmodifiableList(axesNames);
	}
	/**
	 * Get the RangeAxis by name
	 * 
	 * @param name
	 *                name of the RangeAxis
	 * TODO improve me             
	 * @return RangeAxis instance or null if not found
	 */
	public RangeAxis getAxis(Name name) {
	    for (RangeAxis rangeAxis : axes) {
	        if (rangeAxis.getName().toString().equalsIgnoreCase(name.toString()) ||
	                rangeAxis.getName().getLocalPart().equalsIgnoreCase(name.getLocalPart()))
	            return  rangeAxis;
	    }
	    throw new IllegalArgumentException("Unable to find axis for the specified name.");
	}
	public List<? extends RangeAxisBin <?> > getDefaultAxisBins(){
		return this.defaultAxisBins;		
	}
	


}
