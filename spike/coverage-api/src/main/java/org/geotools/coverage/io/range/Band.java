package org.geotools.coverage.io.range;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.media.jai.Histogram;

import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
@SuppressWarnings("deprecation")
public abstract class Band {
	
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions S.A.S.
	 *
	 * @param <V>
	 * @param <QA>
	 */
	public static class BandKey {
		
		private final Set<RangeAxisBin<?>> bins;
	
		public BandKey(final Collection<? extends RangeAxisBin<?>> bins) {
			this.bins = new HashSet<RangeAxisBin<?>>(bins);
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bins == null) ? 0 : bins.hashCode());
			return result;
		}
	
		@Override
		public String toString() {
			final StringBuilder builder= new StringBuilder();
			builder.append("Description of band key:").append("\n");
			for(RangeAxisBin<?> bin:bins){
				builder.append("Description of bin:\n").append(bin.toString()).append("\n");
			}
			return builder.toString();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BandKey other = (BandKey) obj;
			
			return Utilities.deepEquals(this.bins, other.bins);
		}
		
		
	}

	public abstract Histogram getHistogram();
	
	/**
	 * TODO add formal parameters
	 * @return
	 */
	public abstract Histogram getApproximatedHistogram();
	
	public abstract Range<Double> getRange();
	
	/**
	 * TODO add formal parameters
	 * @return
	 */
	public abstract double[] getApproximatedExtrema();
	
	public abstract BandDescriptor getType();

	/**
	 * Values to indicate no data values for the sample dimension.
	 * For low precision sample dimensions, this will often be no data values.
	 *
	 * @return The values to indicate no data values for the sample dimension.
	 *
	 * @see #getMinimumValue
	 * @see #getMaximumValue
	 */
	public abstract double[] getNoDataValues();

	public abstract MetadataNode getMetadata(String metadataDomain,final ProgressListener listener);

	public abstract Name getName() ;

	/**
	 * Sample dimension title or description.
	 * This string may be null or empty if no description is present.
	 *
	 * @return A description for this sample dimension.
	 */
	public abstract InternationalString getDescription();

	/**
	 * The transform which is applied to grid values for this sample dimension.
	 * This transform is often defined as
	 * <var>y</var> = {@linkplain #getOffset offset} + {@link #getScale scale}&times;<var>x</var> where
	 * <var>x</var> is the grid value and <var>y</var> is the geophysics value.
	 * However, this transform may also defines more complex relationship, for
	 * example a logarithmic one. In order words, this transform is a generalization of
	 * {@link #getScale}, {@link #getOffset} and {@link #getNoDataValues} methods.
	 *
	 * @return The transform from sample to geophysics values, or {@code null} if
	 *         it doesn't apply.
	 *
	 * @see #getScale
	 * @see #getOffset
	 * @see #getNoDataValues
	 */
	public abstract MathTransform1D getSampleTransformation();

	/**
	 * TODO
	 * 
	 * @param metadataDomain
	 * @return
	 */
	public abstract MetadataNode getMetadata(String metadataDomain);

	public abstract ColorInterpretation getColorInterpretation() ;

	public abstract SampleDimensionType getSampleDimensionType() ;
	
}
