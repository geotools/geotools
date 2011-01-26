package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.Axis;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultLinearCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.EngineeringDatum;
import org.opengis.util.InternationalString;


public class WavelengthAxis<V> implements Axis<V,Length>{
	/**
	 * A single band.
	 */
	public static class Band extends Measure<Double,Length>{
		private static final long serialVersionUID = -4651829687224844766L;
		private String description;
		private double minValue;
		private double maxValue;
		private String name;

		Band( String band, double value, String description ){
			this.name = band;
			this.minValue = value;
			this.maxValue = value;
			this.description = description;
		}
		Band( String band, double from, double to, String description ) {
			this.name = band;
			this.minValue = from;
			this.maxValue = to;
			this.description = description;
		}
		@Override
		public double doubleValue(Unit<Length> length) {
			UnitConverter converter = SI.MICRO(SI.METER).getConverterTo( length );
			return converter.convert( getValue() );
		}

		@Override
		public Unit<Length> getUnit() {
			return SI.NANO(SI.METER);
		}

		@Override
		public java.lang.Double getValue() {
			return (minValue + maxValue) / 2.0;
		}
		public String getName(){
			return name;
		}
		public String getDescription(){
			return description;
		}
		@Override
		public Measure<java.lang.Double, Length> to(Unit<Length> length) {
			return Measure.valueOf( doubleValue(length), length );
		}
	}
	/** Violet light between 380-450 nm */
	public static final Band COLOR_VIOLET
		= new Band( "Violet", 380, 450, "Visible light between 380-450 nm" );
	/** Blue light between 450-495 nm */
	public static final Band COLOR_BLUE
		= new Band( "Blue", 450, 495, "Visible light between 450-495 nm" );
	/** Green light between 495-570 nm */	
	public static final Band COLOR_GREEN
		= new Band( "Green", 495, 570, "Visible light between 495-570 nm" );
	/** Yellow between 570-590 nm */
	public static final Band COLOR_YELLOW
		= new Band( "Yellow", 570,590, "Visible light between 570-590 nm" );
	/** Orange light between 590-620 nm */	
	public static final Band COLOR_ORANGE
		= new Band( "Green", 590,620, "Visible light between 590-620 nm" );
	/** Red between 620-750 nm */
	public static final Band COLOR_RED
		= new Band( "Yellow", 620, 750, "Visible light between 620-750 nm" );
	
	
	/** LANDSAT7 definition of BLUE */
	public static final Band BLUE
		= new Band( "BLUE", 450, 520, "useful for soil/vegetation discrimination, forest type mapping, and identifying man-made features");
	
	/** LANDSAT7 definition of GREEN */
	public static  final Band GREEN
		= new Band( "GREEN", 520, 610, "penetrates clear water fairly well, and gives excellent contrast between clear and turbid (muddy) water.");
	
	/** LANDSAT7 definition of RED */	
	public static  final Band RED
		= new Band("RED",  630, 690, "useful for identifying vegetation types, soils, and urban (city and town) features");
	
	/** LANDSAT7 definition of NIR */	
	public static  final Band NIR =
		new Band("NIR", 750, 900, " good for mapping shorelines and biomass content");
	
	/** LANDSAT7 definition of SWIR */
	public static  final Band SWIR =
		new Band("SWIR", 1550, 17560, "useful to measure the moisture content of soil and vegetation");
	
	/** LANDSAT7 definition of TIR */
	public static  final Band TIR
		= new Band("TIR", 10400, 12500, "useful to observe temperature");

	/** LANDSAT7 definition of SWIR2 */
	public static  final Band SWIR2
		= new Band("SWIR2", 2080, 23500, "useful to measure the moisture content of soil and vegetation");
		
	/**
	 * Keys for this {@link Axis}.
	 */
	private ArrayList<Measure<V, Length>> keys;
	private NameImpl name;
	
	/**
	 * Bands associated with RGB images.
	 * <p>
	 * Currently this is defined using COLOR_RED, COLOR_GREEN and COLOR_BLUE.
	 * This definition should be updated to use the standard sRGB definitions.
	 */
	public static final WavelengthAxis<Double> RGB;		
	static {
		final List<Band> RGB_BANDS = new ArrayList<Band>();
		RGB_BANDS.add( COLOR_RED );
		RGB_BANDS.add( COLOR_GREEN );
		RGB_BANDS.add( COLOR_BLUE );
		
		RGB = new WavelengthAxis<Double>("RGB-AXIS", RGB_BANDS );
	}
	
	/**
	 * Bands associated with LANDSAT8 readings.
	 */
	public static final WavelengthAxis<Double> LANDSAT7;
	
	static {
		final List<Band> LANDSAT_BANDS = new ArrayList<Band>();
		LANDSAT_BANDS.add(WavelengthAxis.BLUE);
		LANDSAT_BANDS.add(WavelengthAxis.GREEN);
		LANDSAT_BANDS.add(WavelengthAxis.RED);
		LANDSAT_BANDS.add(WavelengthAxis.NIR);
		LANDSAT_BANDS.add(WavelengthAxis.SWIR);
		LANDSAT_BANDS.add(WavelengthAxis.TIR);
		LANDSAT_BANDS.add(WavelengthAxis.SWIR2);
		LANDSAT7 = new WavelengthAxis<Double>("LANDSAT7-AXIS", LANDSAT_BANDS);
	}
	
	private static final DefaultEngineeringCRS CRS;
	static {
		CoordinateSystemAxis csAxis = new DefaultCoordinateSystemAxis(
				new SimpleInternationalString("Light"),
				"\u03BB", // LAMBDA
				AxisDirection.OTHER,
				SI.MICRO(SI.METER));
		DefaultLinearCS lightCS = new DefaultLinearCS("Light",csAxis);
		Map<String,Object> datumProperties = new HashMap<String,Object>();
		datumProperties.put("name", "light");
		
		EngineeringDatum lightDatum = new DefaultEngineeringDatum( datumProperties );		
		CRS = new DefaultEngineeringCRS("Wave Length", lightDatum, lightCS );		
	}
	
	public WavelengthAxis(String name, final List<? extends Measure<V, Length>> keys) {
		this.keys= new ArrayList<Measure<V, Length>>(keys);
		this.name= new NameImpl(name);
	}
	/**
	 * These are units of length; as such the are
	 * not restricted to a coordinate reference system.
	 */
	public SingleCRS getCoordinateReferenceSystem() {		
		return CRS;
	}

	public InternationalString getDescription() {
		return new SimpleInternationalString("Spectral Information");
	}

	public Measure<V, Length> getKey(int keyIndex) {
		return this.keys.get(keyIndex);
	}

	public List<Measure<V, Length>> getKeys() {
		return Collections.unmodifiableList(keys);
	}

	public Name getName() {
		return name;
	}

	public int getNumKeys() {
		return keys.size();
	}

	public Unit<Length> getUnitOfMeasure() {
		return SI.MICRO(SI.METER);
	}
}