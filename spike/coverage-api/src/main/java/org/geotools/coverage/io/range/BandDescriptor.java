package org.geotools.coverage.io.range;




import java.util.Set;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.geotools.util.NumberRange;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.util.InternationalString;


/**
 * Contains information for an individual sample dimension of {@linkplain Coverage coverage}.
 * This interface is applicable to any coverage type.
 * For {@linkplain org.opengis.coverage.grid.GridCoverage grid coverages},
 * the sample dimension refers to an individual band.
 *
 * <P>&nbsp;</P>
 * <TABLE WIDTH="80%" ALIGN="center" CELLPADDING="18" BORDER="4" BGCOLOR="#FFE0B0">
 *   <TR><TD>
 *     <P align="justify"><STRONG>WARNING: THIS CLASS WILL CHANGE.</STRONG> Current API is derived from OGC
 *     <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverages Implementation specification 1.0</A>.
 *     We plan to replace it by new interfaces derived from ISO 19123 (<CITE>Schema for coverage geometry
 *     and functions</CITE>). Current interfaces should be considered as legacy and are included in this
 *     distribution only because they were part of GeoAPI 1.0 release. We will try to preserve as much
 *     compatibility as possible, but no migration plan has been determined yet.</P>
 *   </TD></TR>
 * </TABLE>
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 */
public abstract class BandDescriptor{
	public enum BandInterpretation{
		PHYSICAL_PARAMETER_OBSERVATION,
		PHYSICAL_PARAMETER_PREDICTION,
		STATISTICAL_PARAMETER,
		SYNTHETIC_VALUE;
	}
	
	private BandInterpretation defaultBandInterpretation;
	
	private double[] defaultNoDatavalues;
	
	private NumberRange<Double> defaultRange;
	
	private MathTransform1D defaultSampleTransformation;
	
	private Name name;
	
	private InternationalString description;
	
	private Set<SampleDimensionType> defaultSampleDimensionTypes;

	/**
	 * The {@link Unit} for this {@link RangeDescriptor}.
	 */
	private Unit<?> uom;

	public BandDescriptor(											
			final BandInterpretation bandInterpretation,
			double[] defaultNoDatavalues, 
			NumberRange<Double> defaultRange,
			MathTransform1D defaultSampleTransformation, 
			Name name,
			InternationalString description,
			final Set<SampleDimensionType> sampleDimensionTypes,
	        final Unit<? extends Quantity> unit
	    	) {
		this.defaultBandInterpretation = bandInterpretation;
		this.defaultNoDatavalues = defaultNoDatavalues;
		this.defaultRange = defaultRange;
		this.defaultSampleTransformation = defaultSampleTransformation;
		this.name = name;
		this.description = description;
		this.defaultSampleDimensionTypes = sampleDimensionTypes;
	    this.uom=unit;
	}
	
	public BandInterpretation getDefaultColorInterpretation() {
		return defaultBandInterpretation;
	}

	public double[] getDefaultNoDatavalues() {
		return defaultNoDatavalues;
	}

	public NumberRange<Double> getDefaultRange() {
		return defaultRange;
	}

	public MathTransform1D getDefaultSampleTransformation() {
		return defaultSampleTransformation;
	}

	public Name getName() {
		return name;
	}

	public InternationalString getDescription() {
		return description;
	}

	public Set<SampleDimensionType> getDefaultSampleDimensionTypes() {
		return defaultSampleDimensionTypes;
	}
	/**
	 * Retrieves the Unit of measure for the values described by this RangeDescriptor.
	 *  
	 * <p>
	 * In case this {@link RangeDescriptor} is not made of measurable quantities we
	 * return <code>null</code>
	 * 
	 * @return the Unit of measure for the values described by this RangeDescriptor or
	 *         <code>null</code> in case this {@link RangeDescriptor} is not made of
	 *         measurable quantities
	 */
	public Unit<?> getUnitOfMeasure() {
		return uom; 
	}
	@Override
	public String toString() {
	    final StringBuilder sb = new StringBuilder();
	    final String lineSeparator = System.getProperty("line.separator", "\n");
	    sb.append("BandDescriptor:").append(lineSeparator);
	    sb.append("Name:").append("\t\t").append(name.toString()).append(lineSeparator);
	    sb.append("Description:").append("\t").append(description.toString()).append(lineSeparator);
	    sb.append("UoM:").append("\t").append(uom!=null?uom.toString():"null").append(lineSeparator);
	    sb.append("Default band interpretation:").append("\t").append(defaultBandInterpretation.toString()).append(lineSeparator);
        sb.append(lineSeparator);    
	    return sb.toString();			
	}
}
