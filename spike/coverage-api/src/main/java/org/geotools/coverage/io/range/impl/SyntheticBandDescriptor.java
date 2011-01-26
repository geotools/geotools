package org.geotools.coverage.io.range.impl;

import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Collections;
import java.util.Set;

import javax.imageio.ImageTypeSpecifier;
import javax.measure.unit.Unit;

import org.geotools.coverage.TypeMap;
import org.geotools.coverage.io.range.BandDescriptor;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.referencing.operation.MathTransform1D;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class SyntheticBandDescriptor extends BandDescriptor {
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	public static class SyntheticBandDescriptorBuilder{
		
		private double[] defaultNoDatavalues;
		
		private NumberRange<Double> defaultRange;
		
		private MathTransform1D defaultSampleTransformation;
				
		private Set<SampleDimensionType> defaultSampleDimensionTypes;

		public SyntheticBandDescriptorBuilder() {
		}
		
		public SyntheticBandDescriptorBuilder setDefaultNoDatavalues(final double[] defaultNoDatavalues){
			this.defaultNoDatavalues=defaultNoDatavalues;	
			return this;
		}

		public SyntheticBandDescriptorBuilder setDefaultRange(NumberRange<Double> defaultRange) {
			this.defaultRange = defaultRange;
			return this;
		}

		public SyntheticBandDescriptorBuilder setDefaultSampleTransformation(
				MathTransform1D defaultSampleTransformation) {
			this.defaultSampleTransformation = defaultSampleTransformation;
			return this;
		}

		public SyntheticBandDescriptorBuilder setDefaultSampleDimensionTypes(
				Set<SampleDimensionType> defaultSampleDimensionTypes) {
			this.defaultSampleDimensionTypes = defaultSampleDimensionTypes;
			return this;
		}

		public SyntheticBandDescriptor build(){
			return new SyntheticBandDescriptor(
					this.defaultNoDatavalues,
					this.defaultRange,
					this.defaultSampleTransformation,
					this.defaultSampleDimensionTypes);
		}
		
	}
	
	public static SyntheticBandDescriptor create(final RenderedImage im){
		return create(im.getColorModel(),im.getSampleModel());
		
	}
	public static SyntheticBandDescriptor create(final ColorModel cm, final SampleModel sm){
		final SyntheticBandDescriptorBuilder builder =  new SyntheticBandDescriptorBuilder();
		builder.setDefaultSampleTransformation(LinearTransform1D.IDENTITY);
		builder.setDefaultSampleDimensionTypes(Collections.singleton(TypeMap.getSampleDimensionType(sm, 0)));
		builder.setDefaultRange(TypeMap.getRange(builder.defaultSampleDimensionTypes.iterator().next()));
		return builder.build();
		
	}
	public static SyntheticBandDescriptor create(final ImageTypeSpecifier it){
		return create(it.getColorModel(),it.getSampleModel());
	}

	public SyntheticBandDescriptor(
			final double[] defaultNoDatavalues, 
			final NumberRange<Double> defaultRange,
			final MathTransform1D defaultSampleTransformation, 
			final Set<SampleDimensionType> sampleDimensionTypes) {
		super(
				BandInterpretation.SYNTHETIC_VALUE, 
				defaultNoDatavalues, 
				defaultRange,
				defaultSampleTransformation, 
				new NameImpl("ImageSyntheticBand"), 
				new SimpleInternationalString("band description for data synthetically created"), 
				sampleDimensionTypes,
				Unit.ONE);
	}

}
