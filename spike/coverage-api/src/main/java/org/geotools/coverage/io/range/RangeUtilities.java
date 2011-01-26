package org.geotools.coverage.io.range;

import javax.measure.quantity.Length;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class RangeUtilities {
	public static final Unit<Length> WAVELENGTH_UOM=SI.NANO(SI.METER);
	
	public static final Unit<? extends Quantity> RADIANCE_UOM=SI.WATT.divide(SI.SQUARE_METRE).divide(SI.STERADIAN);
}
