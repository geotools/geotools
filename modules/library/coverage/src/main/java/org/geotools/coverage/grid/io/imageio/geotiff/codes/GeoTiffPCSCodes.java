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
 * @author Simone Giannecchini
 * @since 2.3
 *
 *
 * @source $URL$
 */
public final class GeoTiffPCSCodes {

	/**
	 * 6.3.3.1 Projected CS Type Codes Ranges: [ 1, 1000] = Obsolete EPSG/POSC
	 * Projection System Codes [20000, 32760] = EPSG Projection System codes
	 * 32767 = user-defined [32768, 65535] = Private User Implementations
	 * Special Ranges: 1. For PCS utilizing GeogCS with code in range 4201
	 * through 4321: As far as is possible the PCS code will be of the format
	 * gggzz where ggg is (geodetic datum code -4000) and zz is zone. 2. For PCS
	 * utilizing GeogCS with code out of range 4201 through 4321 (i.e. geodetic
	 * datum code 6201 through 6319). PCS code 20xxx where xxx is a sequential
	 * number. 3. Other: WGS72 / UTM northern hemisphere: 322zz where zz is UTM
	 * zone number WGS72 / UTM southern hemisphere: 323zz where zz is UTM zone
	 * number WGS72BE / UTM northern hemisphere: 324zz where zz is UTM zone
	 * number WGS72BE / UTM southern hemisphere: 325zz where zz is UTM zone
	 * number WGS84 / UTM northern hemisphere: 326zz where zz is UTM zone number
	 * WGS84 / UTM southern hemisphere: 327zz where zz is UTM zone number US
	 * State Plane (NAD27): 267xx/320xx US State Plane (NAD83): 269xx/321xx
	 * Note: These are only a subset of the possible values
	 */
	public static final int PCS_WGS72_UTM_zone_1N = 32201;
	public static final int PCS_WGS72_UTM_zone_1S = 32301;
	public static final int PCS_WGS72_UTM_zone_60N = 32260;
	public static final int PCS_WGS72_UTM_zone_60S = 32360;
	public static final int PCS_WGS72BE_UTM_zone_1N = 32401;
	public static final int PCS_WGS72BE_UTM_zone_1S = 32501;
	public static final int PCS_WGS72BE_UTM_zone_60S = 32560;
	public static final int PCS_WGS84_UTM_zone_1N = 32601;
	public static final int PCS_WGS84_UTM_zone_1S = 32701;
	public static final int PCS_WGS84_UTM_zone_60N = 32660;
	public static final int PCS_WGS84_UTM_zone_60S = 32760;
	/**
	 * PCSCitationGeoKey Key ID = 3073 Type = ASCII As with all the "Citation"
	 * GeoKeys, this is provided to give an ASCII reference to published
	 * documentation on the Projected Coordinate System particularly if this is
	 * a "user-defined" PCS.
	 */
	public static final int PCSCitationGeoKey = 3073;
	/**
	 * ProjAzimuthAngleGeoKey Key ID = 3094 Type = DOUBLE Units: GeogAzimuthUnit
	 * Azimuth angle east of true north of the central line passing through the
	 * projection center (for elliptical (Hotine) Oblique Mercator). Note that
	 * this is the standard method of measuring azimuth, but is opposite the
	 * usual mathematical convention of positive indicating counter-clockwise.
	 */
	public static final int ProjAzimuthAngleGeoKey = 3094;
	/**
	 * ProjCenterEastingGeoKey Key ID = 3090 Type = DOUBLE Units: ProjLinearUnit
	 * Gives the easting coordinate of the center. This is NOT the False
	 * Easting.
	 */
	public static final int ProjCenterEastingGeoKey = 3090;
	/**
	 * ProjCenterLatGeoKey Key ID = 3089 Type = DOUBLE Units: GeogAngularUnit
	 * Latitude of Center of Projection. Note that this is not necessarily the
	 * origin of the projection.
	 */
	public static final int ProjCenterLatGeoKey = 3089;
	/**
	 * ProjCenterLongGeoKey Key ID = 3088 Type = DOUBLE Units: GeogAngularUnit
	 * Longitude of Center of Projection. Note that this is not necessarily the
	 * origin of the projection.
	 */
	public static final int ProjCenterLongGeoKey = 3088;
	/**
	 * ProjCenterNorthingGeoKey Key ID = 3091 Type = DOUBLE Units:
	 * ProjLinearUnit Gives the northing coordinate of the center. This is NOT
	 * the False Northing. NOTE this value is incorrectly named at
	 * http://www.remotesensing.org/geotiff/spec/geotiff2.7.html#2.7
	 */
	public static final int ProjCenterNorthingGeoKey = 3091;
	/**
	 * ProjCoordTransGeoKey Key ID = 3075 Type = SHORT (code) Values: Section
	 * 6.3.3.3 codes
	 * http://www.remotesensing.org/geotiff/spec/geotiff6.html#6.3.3.3 Allows
	 * specification of the coordinate transformation method used. Note: this
	 * does not include the definition of the corresponding Geographic
	 * Coordinate System to which the projected CS is related; only the
	 * transformation method is defined here. GeoKeys Required for
	 * "user-defined" Coordinate Transformations: PCSCitationGeoKey (additional
	 * parameter geokeys depending on the Coord. Trans. specified).
	 */
	public static final int ProjCoordTransGeoKey = 3075;
	/**
	 * ProjectedCSTypeGeoKey Key ID = 3072 Type = SHORT (codes) Values: Section
	 * 6.3.3.1 codes This code is provided to specify the projected coordinate
	 * system. GeoKey requirements for "user-defined" PCS families:
	 * PCSCitationGeoKey ProjectionGeoKey
	 */
	public static final int ProjectedCSTypeGeoKey = 3072;
	/**
	 * ProjectionGeoKey Key ID = 3074 Type = SHORT (code) Values: Section
	 * 6.3.3.2 codes
	 * http://www.remotesensing.org/geotiff/spec/geotiff6.html#6.3.3.2 Allows
	 * specification of the coordinate transformation method and projection zone
	 * parameters. Note : when associated with an appropriate Geographic
	 * Coordinate System, this forms a Projected Coordinate System. GeoKeys
	 * Required for "user-defined" Projections: PCSCitationGeoKey
	 * ProjCoordTransGeoKey ProjLinearUnitsGeoKey (additional parameters
	 * depending on ProjCoordTransGeoKey).
	 */
	public static final int ProjectionGeoKey = 3074;
	/**
	 * ProjFalseEastingGeoKey Key ID = 3082 Type = DOUBLE Units: ProjLinearUnit
	 * Gives the easting coordinate of the map projection Natural origin.
	 */
	public static final int ProjFalseEastingGeoKey = 3082;
	/**
	 * ProjFalseNorthingGeoKey Key ID = 3083 Type = DOUBLE Units: ProjLinearUnit
	 * Gives the northing coordinate of the map projection Natural origin.
	 */
	public static final int ProjFalseNorthingGeoKey = 3083;
	/**
	 * ProjFalseOriginEastingGeoKey Key ID = 3086 Type = DOUBLE Units:
	 * ProjLinearUnit Gives the easting coordinate of the false origin. This is
	 * NOT the False Easting, which is the easting attached to the Natural
	 * origin.
	 */
	public static final int ProjFalseOriginEastingGeoKey = 3086;
	/**
	 * ProjFalseOriginLatGeoKey Key ID = 3085 Type = DOUBLE Units:
	 * GeogAngularUnit Gives the latitude of the False origin.
	 */
	public static final int ProjFalseOriginLatGeoKey = 3085;
	/**
	 * ProjFalseOriginLongGeoKey Key ID = 3084 Type = DOUBLE Units:
	 * GeogAngularUnit Gives the longitude of the False origin.
	 */
	public static final int ProjFalseOriginLongGeoKey = 3084;
	/**
	 * ProjFalseOriginNorthingGeoKey Key ID = 3087 Type = DOUBLE Units:
	 * ProjLinearUnit Gives the northing coordinate of the False origin. This is
	 * NOT the False Northing, which is the northing attached to the Natural
	 * origin.
	 */
	public static final int ProjFalseOriginNorthingGeoKey = 3087;
	/**
	 * ProjLinearUnitsGeoKey Key ID = 3076 Type = SHORT (code) Values: Section
	 * 6.3.1.3 codes Defines linear units used by this projection.
	 * http://www.remotesensing.org/geotiff/spec/geotiff6.html#6.3.1.3
	 */
	public static final int ProjLinearUnitsGeoKey = 3076;
	/**
	 * ProjLinearUnitSizeGeoKey Key ID = 3077 Type = DOUBLE Units: meters
	 * Defines size of user-defined linear units in meters.
	 */
	public static final int ProjLinearUnitSizeGeoKey = 3077;
	/**
	 * ProjNatOriginLatGeoKey Key ID = 3081 Type = DOUBLE Units: GeogAngularUnit
	 * Alias: ProjOriginLatGeoKey Latitude of map-projection Natural origin.
	 */
	public static final int ProjNatOriginLatGeoKey = 3081;
	/**
	 * ProjNatOriginLongGeoKey Key ID = 3080 Type = DOUBLE Units:
	 * GeogAngularUnit Alias: ProjOriginLongGeoKey Longitude of map-projection
	 * Natural origin.
	 */
	public static final int ProjNatOriginLongGeoKey = 3080;
	public static final int ProjRectifiedGridAngle = 3094;
	/**
	 * ProjScaleAtCenterGeoKey Key ID = 3093 Type = DOUBLE Units: none Scale at
	 * Center. This is a ratio, so no units are required.
	 */
	public static final int ProjScaleAtCenterGeoKey = 3093;
	/**
	 * ProjScaleAtNatOriginGeoKey Key ID = 3092 Type = DOUBLE Units: none Alias:
	 * ProjScaleAtOriginGeoKey (Rev. 0.2) Scale at Natural Origin. This is a
	 * ratio, so no units are required.
	 */
	public static final int ProjScaleAtNatOriginGeoKey = 3092;
	/**
	 * ProjStdParallel1GeoKey Key ID = 3078 Type = DOUBLE Units: GeogAngularUnit
	 * Alias: ProjStdParallelGeoKey (from Rev 0.2) Latitude of primary Standard
	 * Parallel.
	 */
	public static final int ProjStdParallel1GeoKey = 3078;
	/**
	 * ProjStdParallel2GeoKey Key ID = 3079 Type = DOUBLE Units: GeogAngularUnit
	 * Latitude of second Standard Parallel.
	 */
	public static final int ProjStdParallel2GeoKey = 3079;
	/**
	 * ProjStraightVertPoleLongGeoKey Key ID = 3095 Type = DOUBLE Units:
	 * GeogAngularUnit Longitude at Straight Vertical Pole. For polar
	 * stereographic.
	 */
	public static final int ProjStraightVertPoleLongGeoKey = 3095;
	/**
	 * 6.3.1.1 Model Type Codes Ranges: 0 = undefined [ 1, 32766] = GeoTIFFWritingUtilities Reserved Codes 32767 = user-defined [32768, 65535] = Private User Implementations GeoTIFFWritingUtilities defined CS Model Type Codes: ModelTypeProjected = 1 Projection Coordinate System ModelTypeGeographic = 2 Geographic latitude-longitude System ModelTypeGeocentric = 3 Geocentric (X,Y,Z) Coordinate System Notes: 1. ModelTypeGeographic and ModelTypeProjected correspond to the FGDC metadata Geographic and Planar-Projected coordinate system types.
	 */
	public static final int ModelTypeProjected = 1;

	private GeoTiffPCSCodes() {
	}

}
