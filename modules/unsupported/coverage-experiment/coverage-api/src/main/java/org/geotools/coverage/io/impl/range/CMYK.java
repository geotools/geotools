package org.geotools.coverage.io.impl.range;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

/**
 * Process Color is a subtractive model used when working with pigment. This
 * model is often used when printing.
 * <p>
 * This is a normal Java 5 enum capturing the closed set of CMYK names. It is
 * used as a basis for the definition of an Axis built around these constants.
 * <p>
 * Please understand that this is not the only possible subtractive color model
 * - a commercial alternative is the Pantone (tm)) colors.
 */
public enum CMYK {
	CYAN, MAGENTA, YELLOW, KEY;

	/**
	 * Axis covering the full {@link CMYK} range.
	 */
	public static final DefaultAxis<CMYK,Dimensionless> AXIS
		= new DefaultAxis<CMYK,Dimensionless>("Process Color", EnumMeasure.valueOf( CMYK.class ), Unit.ONE );

	/**
	 * Axis around {@link #KEY }.
	 */
	public static final DefaultAxis<CMYK,Dimensionless> BLACK_AXIS =
		new DefaultAxis<CMYK,Dimensionless>("Process Black", EnumMeasure.valueOf(KEY), Unit.ONE );
    
}