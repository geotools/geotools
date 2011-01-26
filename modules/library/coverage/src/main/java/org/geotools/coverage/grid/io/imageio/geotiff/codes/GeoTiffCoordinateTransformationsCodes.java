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
 * 
 * ProjCoordTransGeoKey<br>
 * Key ID = 3075<br>
 * Type = SHORT (code)<br>
 * Values: Section 6.3.3.3 codes<br>
 * Allows specification of the coordinate transformation method used. Note: this
 * does not include the definition of the correspondingì Geographic Coordinate
 * System to which the projected CS is related; only the transformation method
 * is defined here.<br>
 * <br>
 * <br>
 * <strong>GeoKeys Required for "user-defined" Coordinate Transformations</strong><br>
 * <br>
 * <br>

 * 
 * @author Simone Giannecchini
 * @since 2.3
 * 
 *
 * @source $URL$
 */
public final class GeoTiffCoordinateTransformationsCodes {

	public static final short CT_TransverseMercator = 1;

	public static final short CT_TransvMercator_Modified_Alaska = 2;

	public static final short CT_ObliqueMercator = 3;

	public static final short CT_ObliqueMercator_Laborde = 4;

	public static final short CT_ObliqueMercator_Rosenmund = 5;

	public static final short CT_ObliqueMercator_Spherical = 6;

	public static final short CT_Mercator = 7;

	public static final short CT_LambertConfConic_2SP = 8;

	public static final short CT_LambertConfConic = CT_LambertConfConic_2SP;

	public static final short CT_LambertConfConic_1SP = 9;

	public static final short CT_LambertConfConic_Helmert = CT_LambertConfConic_1SP;

	public static final short CT_LambertAzimEqualArea = 10;

	public static final short CT_AlbersEqualArea = 11;

	public static final short CT_AzimuthalEquidistant = 12;

	public static final short CT_EquidistantConic = 13;

	public static final short CT_Stereographic = 14;

	public static final short CT_PolarStereographic = 15;

	public static final short CT_ObliqueStereographic = 16;

	public static final short CT_Equirectangular = 17;

	public static final short CT_CassiniSoldner = 18;

	public static final short CT_Gnomonic = 19;

	public static final short CT_MillerCylindrical = 20;

	public static final short CT_Orthographic = 21;

	public static final short CT_Polyconic = 22;

	public static final short CT_Robinson = 23;

	public static final short CT_Sinusoidal = 24;

	public static final short CT_VanDerGrinten = 25;

	public static final short CT_NewZealandMapGrid = 26;

	public static final short CT_TransvMercator_SouthOriented = 27;

	public static final short CT_SouthOrientedGaussConformal = CT_TransvMercator_SouthOriented;

	public static final short CT_AlaskaConformal = CT_TransvMercator_Modified_Alaska;

	public static final short CT_TransvEquidistCylindrical = CT_CassiniSoldner;

	public static final short CT_ObliqueMercator_Hotine = CT_ObliqueMercator;

	public static final short CT_SwissObliqueCylindrical = CT_ObliqueMercator_Rosenmund;

	public static final short CT_GaussBoaga = CT_TransverseMercator;

	public static final short CT_GaussKruger = CT_TransverseMercator;

	private GeoTiffCoordinateTransformationsCodes() {
	}

}
