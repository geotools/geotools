package org.geotools.coverage.io.range.impl;

import java.util.Collections;
import java.util.Set;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.BandDescriptor;
import org.geotools.coverage.io.range.RangeAxis;
import org.geotools.coverage.io.range.RangeUtilities;
import org.geotools.coverage.io.range.RangeAxis.WavelengthAxis;
import org.geotools.feature.NameImpl;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.referencing.operation.MathTransform1D;
/**
 * {@link BandDescriptor} for radiance.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class RadianceBandDescriptor extends BandDescriptor {
	
	
	
	public final static RadianceBandDescriptor LANDSAT7 = new RadianceBandDescriptor(
			null,
			null,
			null,
			Collections.<SampleDimensionType>emptySet(),
			RangeAxis.WavelengthAxis.WAVELENGTH_AXIS_NM,
//			Arrays.asList(
//					RangeAxis.WavelengthAxis.LANDSAT7_BLUE_AXIS_BIN,
//					RangeAxis.WavelengthAxis.GREEN_AXIS_BIN,
//					RangeAxis.WavelengthAxis.RED_AXIS_BIN,
//					RangeAxis.WavelengthAxis.NIR_AXIS_BIN,
//					RangeAxis.WavelengthAxis.SWIR_AXIS_BIN,
//					RangeAxis.WavelengthAxis.TIR_AXIS_BIN,
//					RangeAxis.WavelengthAxis.SWIR2_AXIS_BIN),
			RangeUtilities.RADIANCE_UOM);

	public RadianceBandDescriptor(
			final double[] defaultNoDatavalues, 
			final NumberRange<Double> defaultRange,
			final MathTransform1D defaultSampleTransformation, 
			final Set<SampleDimensionType> sampleDimensionTypes,
			final WavelengthAxis wavelengthAxis,
			final Unit<? extends Quantity> unit) {
		super(
				BandInterpretation.PHYSICAL_PARAMETER_OBSERVATION, 
				defaultNoDatavalues, 
				defaultRange,
				defaultSampleTransformation, 
				new NameImpl("RadianceBandDescriptor"), 
				new SimpleInternationalString("RadianceBandDescriptor"), 
				sampleDimensionTypes,
				unit);
	}

}
