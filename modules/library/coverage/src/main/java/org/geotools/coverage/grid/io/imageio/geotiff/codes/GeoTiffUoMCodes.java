/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.coverage.grid.io.imageio.geotiff.codes;

/**
 * @author Simone Giannecchini
 * @since 2.3
 *
 *
 * @source $URL$
 */
public final class GeoTiffUoMCodes {

	public static final int Angular_Arc_Minute = 9103;
	public static final int Angular_Arc_Second = 9104;
	public static final int Angular_Degree = 9102;
	public static final int Angular_DMS = 9107;
	public static final int Angular_DMS_Hemisphere = 9108;
	public static final int Angular_Gon = 9106;
	public static final int Angular_Grad = 9105;
	/**
	 * 6.3.1.4 Angular Units Codes These codes shall be used for any key that
	 * requires specification of an angular unit of measurement.
	 */
	public static final int Angular_Radian = 9101;
	public static final int Linear_Chain_Benoit = 9010;
	public static final int Linear_Chain_Sears = 9011;
	public static final int Linear_Fathom = 9014;
	public static final int Linear_Foot = 9002;
	public static final int Linear_Foot_Clarke = 9005;
	public static final int Linear_Foot_Indian = 9006;
	public static final int Linear_Foot_Modified_American = 9004;
	public static final int Linear_Foot_US_Survey = 9003;
	public static final int Linear_Link = 9007;
	public static final int Linear_Link_Benoit = 9008;
	public static final int Linear_Link_Sears = 9009;
	/**
	 * 6.3.1.3 Linear Units Codes There are several different kinds of units
	 * that may be used in geographically related raster data: linear units,
	 * angular units, units of time (e.g. for radar-return), CCD-voltages, etc.
	 * For this reason there will be a single, unique range for each kind of
	 * unit, broken down into the following currently defined ranges: Ranges: 0 =
	 * undefined [ 1, 2000] = Obsolete GeoTIFFWritingUtilities codes [2001,
	 * 8999] = Reserved by GeoTIFFWritingUtilities [9000, 9099] = EPSG Linear
	 * Units. [9100, 9199] = EPSG Angular Units. 32767 = user-defined unit
	 * [32768, 65535]= Private User Implementations
	 */
	public static final int Linear_Meter = 9001;
	public static final int Linear_Mile_International_Nautical = 9015;
	public static final int Linear_Yard_Indian = 9013;
	public static final int Linear_Yard_Sears = 9012;


	private GeoTiffUoMCodes() {
	}

}
