package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.List;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

import org.opengis.util.CodeList;

/**
 * Process Color is a subtractive model used when working with pigment. This
 * model is often used when printing.
 * <p>
 * This is a normal Java 5 enum capturing the closed set of CMYK names. It is
 * used as a basis for the definition of an Axis built around these constants.
 * <p>
 * Please understand that this is not the only possible subtractive color model
 * - a commerical alternative is the Pantone (tm)) colors.
 */
public class HSV extends CodeList<HSV> {
	private static final long serialVersionUID = 2772167658847829287L;

	private static List<HSV> ALL = new ArrayList<HSV>();

	public static HSV HUE = new HSV("Hue");
	public static HSV SATURATION = new HSV("Saturation");
	public static HSV VALUE = new HSV("Value");

	public HSV(String name) {
		super(name, ALL );
	}
	
	@Override
	public HSV[] family() {
		return ALL.toArray( new HSV[ ALL.size() ]);
	}
	
	/**
	 * Axis covering the full {@link HSV} range.
	 */
	public static final DefaultAxis<HSV,Dimensionless> AXIS
    	= new DefaultAxis<HSV,Dimensionless>("Additive Color", CodeMeasure.valueOf( ALL ), Unit.ONE );

	/**
	 * Axis around {@link #KEY }.
	 */
	public static final DefaultAxis<HSV,Dimensionless> INTENSITY_AXIS =
		new DefaultAxis<HSV,Dimensionless>("Intensity", CodeMeasure.valueOf(VALUE), Unit.ONE );

}
