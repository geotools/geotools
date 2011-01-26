/**
 * 
 */
package org.geotools.coverage.io.range.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import junit.framework.Assert;

import org.geotools.coverage.io.impl.range.DefaultAxis;
import org.geotools.coverage.io.impl.range.EnumMeasure;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultLinearCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.SampleDimension;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.EngineeringDatum;
import org.junit.Test;
/**
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class EnumMeasureTest extends Assert {
	
	/** Bands captured as an enumeration used as an example below */
	enum Band {
		BLUE,GREE,RED,NIR,SWIT,TIR,SWR2
	};
	
	/**
	 * This test uses use the default implementations
	 * to express 7 bands of a landsat image.
	 */
	@Test
	public void testLandsatAxis(){
		CoordinateSystemAxis csAxis = new DefaultCoordinateSystemAxis(
			new SimpleInternationalString("light"),
			"light",
			AxisDirection.OTHER,
			SI.MICRO(SI.METER));
		
		DefaultLinearCS lightCS = new DefaultLinearCS("light",csAxis);
		Map<String,Object> datumProperties = new HashMap<String,Object>();
		datumProperties.put("name", "light");
		
		EngineeringDatum lightDatum = new DefaultEngineeringDatum( datumProperties );		
		DefaultEngineeringCRS lightCRS = new DefaultEngineeringCRS("wave length", lightDatum, lightCS );
		
		List<Measure<Band, Dimensionless>> keys = EnumMeasure.valueOf( Band.class );
		
		DefaultAxis<Band, Dimensionless> axis =
			new DefaultAxis<Band,Dimensionless>(
				new NameImpl("Bands"),
				new SimpleInternationalString("Landsat bands by wavelength"),
				keys,
				Unit.ONE );
		
		Map<Measure<Integer,Dimensionless>,SampleDimension> samples = new HashMap<Measure<Integer,Dimensionless>, SampleDimension>();
	}

}
