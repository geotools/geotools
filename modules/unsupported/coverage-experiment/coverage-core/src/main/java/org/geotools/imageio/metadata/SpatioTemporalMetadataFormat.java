/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.metadata;

import java.util.List;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormatImpl;

import org.geotools.resources.UnmodifiableArrayList;
import org.opengis.referencing.cs.CoordinateSystem;

public class SpatioTemporalMetadataFormat extends IIOMetadataFormatImpl {

    public static final String FORMAT_NAME = "org_geotools_gce_nplugin_spatiotemporalMetadata_1.0";

    /**
     * The geographic
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
     * type. This is often used together with the
     * {@linkplain #ELLIPSOIDAL ellipsoidal} coordinate system type.
     * 
     * @see #setCoordinateReferenceSystem
     */
    public static final String GEOGRAPHIC = "geographic";

    /**
     * The geographic
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
     * type with a vertical axis. This is often used together with a
     * three-dimensional {@linkplain #ELLIPSOIDAL ellipsoidal} coordinate system
     * type.
     * <p>
     * If the coordinate reference system has no vertical axis, or has
     * additional axis of other kind than vertical (for example only a temporal
     * axis), then the type should be the plain {@value #GEOGRAPHIC}. This is
     * because such CRS are usually constructed as
     * {@linkplain org.opengis.referencing.crs.CompoundCRS compound CRS} rather
     * than a CRS with a three-dimensional coordinate system.
     * <p>
     * To be strict, a 3D CRS should be allowed only if the vertical axis is of
     * the kind "height above the ellipsoid" (as opposed to "height above the
     * geoid" for example), otherwise we have a compound CRS. But many datafile
     * don't make this distinction.
     * 
     * @see #setCoordinateReferenceSystem
     */
    public static final String GEOGRAPHIC_3D = "geographic3D";

    /**
     * The projected
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
     * type. This is often used together with the
     * {@linkplain #CARTESIAN cartesian} coordinate system type.
     * 
     * @see #setCoordinateReferenceSystem
     */
    public static final String PROJECTED = "projected";

    /**
     * The derived
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
     * type.
     * 
     * @see #setCoordinateReferenceSystem
     */
    public static final String DERIVED = "derived";

    /**
     * The projected
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
     * type with a vertical axis. This is often used together with a
     * three-dimensional {@linkplain #CARTESIAN cartesian} coordinate system
     * type.
     * <p>
     * If the coordinate reference system has no vertical axis, or has
     * additional axis of other kind than vertical (for example only a temporal
     * axis), then the type should be the plain {@value #PROJECTED}. This is
     * because such CRS are usually constructed as
     * {@linkplain org.opengis.referencing.crs.CompoundCRS compound CRS} rather
     * than a CRS with a three-dimensional coordinate system.
     * <p>
     * To be strict, a 3D CRS should be allowed only if the vertical axis is of
     * the kind "height above the ellipsoid" (as opposed to "height above the
     * geoid" for example), otherwise we have a compound CRS. But many datafile
     * don't make this distinction.
     * 
     * @see #setCoordinateReferenceSystem
     */
    public static final String PROJECTED_3D = "projected3D";

    /**
     * The ellipsoidal {@linkplain CoordinateSystem coordinate system} type.
     * 
     * @see #setCoordinateSystem
     */
    public static final String ELLIPSOIDAL = "ellipsoidal";

    /**
     * The cartesian {@linkplain CoordinateSystem coordinate system} type.
     * 
     * @see #setCoordinateSystem
     */
    public static final String CARTESIAN = "cartesian";

    /**
     * The geophysics {@linkplain Band sample dimension} type. Pixels
     * in the {@linkplain java.awt.image.RenderedImage rendered image} produced
     * by the image reader contain directly geophysics values like temperature
     * or elevation. Sample type is typically {@code float} or {@code double}
     * and missing value, if any, <strong>must</strong> be one of
     * {@linkplain Float#isNaN NaN values}.
     */
    public static final String GEOPHYSICS = "geophysics";

    /**
     * The packed {@linkplain Band sample dimension} type. Pixels in
     * the {@linkplain java.awt.image.RenderedImage rendered image} produced by
     * the image reader contain packed data, typically as {@code byte} or
     * {@code short} integer type. Conversions to geophysics values are
     * performed by the application of a scale and offset. Some special values
     * are typically used for missing values.
     */
    public static final String PACKED = "packed";

    /**
     * Enumeration of valid coordinate reference system types.
     */
    static final List<String> CRS_TYPES = UnmodifiableArrayList
            .wrap(new String[] { GEOGRAPHIC, PROJECTED, DERIVED });

    /**
     * Enumeration of valid coordinate system types.
     */
    static final List<String> CS_TYPES = UnmodifiableArrayList
            .wrap(new String[] { ELLIPSOIDAL, CARTESIAN });

    /**
     * Enumeration of valid axis directions. We do not declare {@link String}
     * constants for them since they are already available as
     * {@linkplain org.opengis.referencing.cs.AxisDirection axis direction} code
     * list.
     */
    static final List<String> DIRECTIONS = UnmodifiableArrayList
            .wrap(new String[] { "north", "east", "south", "west", "up", "down" });

    /**
     * Enumeration of valid pixel orientation. We do not declare {@link String}
     * constants for them since they are already available as
     * {@linkplain org.opengis.metadata.spatial.PixelOrientation pixel
     * orientation} code list.
     */
    static final List<String> PIXEL_ORIENTATIONS = UnmodifiableArrayList
            .wrap(new String[] { "center", "lower left", "lower right",
                    "upper right", "upper left" });

    /**
     * Enumeration of valid sample dimention types.
     */
    static final List<String> SAMPLE_TYPES = UnmodifiableArrayList.wrap(new String[] { GEOPHYSICS, PACKED });

    // ////////////////////////////////////////////////////////////////////////
    // 
    // Metadata nodes names
    //
    // ////////////////////////////////////////////////////////////////////////

    // ////
    //
    // Common element (deeply used by several elements)
    //
    // ////
    public final static String MD_COMM_NAME = "name";
    public final static String MD_COMM_REMARKS = "remarks";
    public final static String MD_COMM_IDENTIFIER = "identifier";
    public final static String MD_COMM_ALIAS = "alias";
    public final static String MD_COMM_ATTRIBUTETYPE = "type";
    public final static String MD_COMM_ATTRIBUTEVALUE = "value";

    // ////
    //
    // CRSs elements
    //
    // ////
    public final static String MD_COORDINATEREFERENCESYSTEM = "CoordinateReferenceSystem";
    public final static String MD_VERTICALCRS = "VerticalCRS";
    public final static String MD_TEMPORALCRS = "TemporalCRS";
    public final static String MD_CRS = "CoordinateReferenceSystem";

    // ////
    //
    // Coordinate System element (deeply used in CRSs)
    //
    // ////
    public final static String MD_COORDINATESYSTEM = "CoordinateSystem";
    public final static String MD_SCRS_DERIVED_CRS = "DerivedCRS";
    public final static String MD_SCRS_PROJECTED_CRS = "ProjectedCRS";
    public final static String MD_SCRS_BASE_CRS = "BaseCRS";
    public final static String MD_CS_AXES = "Axes";

    // ////
    //
    // Axis element
    //
    // ////
    public final static String MD_AXIS = "Axis";
    public final static String MD_AX_ABBREVIATION = "axisAbbrev";
    public final static String MD_AX_DIRECTION = "axisDirection";
    public final static String MD_AX_UOM = "axisUoM";
    public final static String MD_AX_MIN = "minimumValue";
    public final static String MD_AX_MAX = "maximumValue";
    public final static String MD_AX_RANGEMEANING = "rangeMeaning";

    // ////
    //
    // definedByConversion element
    //
    // ////

    public final static String MD_SCRS_DEFINED_BY_CONVERSION = "definedByConversion";
    public final static String MD_SCRS_DBC_FORMULA = "formula";
    public final static String MD_SCRS_DBC_SRC_DIM = "srcDim";
    public final static String MD_SCRS_DBC_TARGET_DIM = "targetDim";
    public final static String MD_SCRS_DBC_PARAMETERS = "parameters";
    public final static String MD_SCRS_DBC_PARAMETER_VALUE = "parameter";

    // ////
    //
    // Datum elements
    //
    // ////
    public final static String MD_DATUM = "Datum";

    // Datum common elements
    public final static String MD_DTM_ANCHORPOINT = "anchorPoint";
    public final static String MD_DTM_REALIZATIONEPOCH = "realizationEpoch";

    // //
    //
    // Geodetic Datum
    //
    // //
    public final static String MD_DTM_GEODETIC = "GeodeticDatum";

    // Ellipsoid
    public final static String MD_DTM_GD_ELLIPSOID = "Ellipsoid";
    public final static String MD_DTM_GD_EL_SEMIMAJORAXIS = "semiMajorAxis";
    public final static String MD_DTM_GD_EL_SECONDDEFPARAM = "secondDefiningParameter";
    public final static String MD_DTM_GD_EL_SEMIMINORAXIS = "semiMinorAxis";
    public final static String MD_DTM_GD_EL_INVERSEFLATTENING = "inverseFlattening";
    public final static String MD_DTM_GD_EL_UNIT = "unit";
    public final static String MD_DTM_GD_EL_SPHERE = "sphere";

    // Prime Meridian
    public final static String MD_DTM_GEODETIC_PRIMEMERIDIAN = "PrimeMeridian";
    public final static String MD_DTM_GD_PM_GREENWICHLONGITUDE = "greenwichLongitude";

    // //
    //
    // Engineering Datum
    //
    // //
    public final static String MD_DTM_ENGINEERING = "EngineeringDatum";

    // //
    //
    // Temporal Datum
    //
    // //
    public final static String MD_DTM_TEMPORAL = "TemporalDatum";
    public final static String MD_DTM_TD_ORIGIN = "origin";

    // //
    //
    // Vertical Datum
    //
    // //
    public final static String MD_DTM_VERTICAL = "VerticalDatum";
    public final static String MD_DTM_VD_TYPE = "verticalDatumType";

    // //
    //
    // Image Datum
    //
    // //
    public final static String MD_DTM_IMAGE = "ImageDatum";
    public final static String MD_DTM_ID_PIXELINCELL = "pixelInCell";

    // ////
    //
    // BoundedBy
    //
    // ////
    public final static String MD_BOUNDEDBY = "boundedBy";

    // //
    //
    // BoundingBox
    //
    // //
    public final static String MD_BB_BOUNDINGBOX = "BoundingBox";
    public final static String MD_BB_SRSNAME = "srsName";
    public final static String MD_BB_LC = "lowerCorner";
    public final static String MD_BB_UC = "upperCorner";

    // //
    //
    // Vertical Extent (VE) related node names
    //
    // //
    public final static String MD_BB_VERTICALEXTENT = "VerticalExtent";
    public final static String MD_BB_VE_TYPE = "type";

    // Single Value
    public final static String MD_BB_VE_SINGLEVALUE = "singleValue";

    // Vertical Range
    public final static String MD_BB_VE_VERTICALRANGE = "verticalRange";
    public final static String MD_BB_VE_VR_MIN = "minimum";
    public final static String MD_BB_VE_VR_MAX = "maximum";
    public final static String MD_BB_VE_VR_RES = "resolution";

    // //
    //
    // Temporal Extent (TE) related node names
    //
    // //
    public final static String MD_BB_TEMPORALEXTENT = "TemporalExtent";
    public final static String MD_BB_TE_TYPE = "type";

    // Time Position
    public final static String MD_BB_TE_TIMEPOSITION = "timePosition";

    // Time Period
    public final static String MD_BB_TE_TIMEPERIOD = "timePeriod";
    public final static String MD_BB_TE_TP_BEGIN = "beginPosition";
    public final static String MD_BB_TE_TP_END = "endPosition";
    public final static String MD_BB_TE_TP_RES = "timeResolution";

    // //
    //
    // Rectified Grid
    //
    // //
    public final static String MD_RECTIFIEDGRID = "RectifiedGrid";
    public final static String MD_RG_DIMENSION = "dimension";

    // limits
    public final static String MD_RG_LIMITS = "limits";
    public final static String MD_RG_LI_RASTERLAYOUT = "RasterLayout";
    public final static String MD_RG_LI_RL_LOW = "low";
    public final static String MD_RG_LI_RL_HIGH = "high";

    // axes
    public final static String MD_RG_AXESNAMES = "axesNames";
    public final static String MD_RG_AX_AXISNAME = "axisName";

    // origin
    public final static String MD_RG_ORIGIN = "origin";
    public final static String MD_RG_OR_POINT = "Point";
    public final static String MD_RG_OR_PT_COORD = "coordinates";
    public final static String MD_RG_OR_PT_ID = "id";
    public final static String MD_RG_OR_PT_SRSURI = "srsName";

    // offset vectors
    public final static String MD_RG_OFFSETVECTORS = "offsetVectors";
    public final static String MD_RG_OV_OFFSETVECTOR = "offsetVector";

    // //
    //
    // Bands
    //
    // //
    public final static String MD_BANDS = "Bands";
    public final static String MD_BD = "Band";
    public final static String MD_BD_KEY = "key";
    public final static String MD_BD_DESCRIPTION = "description";
    public final static String MD_BD_NODATA = "noDataValues";
    public final static String MD_BD_VALIDRANGE = "validRange";
    public final static String MD_BD_UOM = "uom";
    public final static String MD_BD_SCALE = "scale";
    public final static String MD_BD_OFFSET = "offset";

    // //
    //
    // Categories
    //
    // //
    public final static String MD_BD_CATEGORIES = "categories";
    public final static String MD_BD_CAT = "category";
    public final static String MD_BD_CAT_NAME = "name";
    public final static String MD_BD_CAT_DESCRIPTION = "description";
    public final static String MD_BD_CAT_RANGE = "range";

    // // //
    // //
    // // Calibration
    // //
    // // //
    // public final static String MD_SD_CALIBRATION = "Calibration";
    //
    // public final static String MD_SD_CAL_SCALE = "scale";
    //
    // public final static String MD_SD_CAL_OFFSET = "offset";
    //
    // public final static String MD_SD_CAL_VALUE = "value";
    //
    // public final static String MD_SD_CAL_ERROR = "error";

    // //
    //
    // Statistics
    //
    // //
    public final static String MD_BD_STATISTICS = "statistics";
    public final static String MD_BD_STAT_MEAN = "mean";
    public final static String MD_BD_STAT_MODE = "offset";
    public final static String MD_BD_STAT_STDDEV = "standardDeviation";
    public final static String MD_BD_STAT_HISTOGRAM = "Histogram";
    public final static String MD_BD_STAT_HIST_BINS = "bins";
    public final static String MD_BD_STAT_HIST_VALUES = "values";

    protected SpatioTemporalMetadataFormat() {
        super(FORMAT_NAME, CHILD_POLICY_SOME);
        addElement(MD_COORDINATEREFERENCESYSTEM, FORMAT_NAME, CHILD_POLICY_SOME);
        addElement(MD_COORDINATESYSTEM, MD_COORDINATEREFERENCESYSTEM, CHILD_POLICY_SOME);

        // //
        // BoundedBy node
        // //
        addElement(MD_BOUNDEDBY, FORMAT_NAME, CHILD_POLICY_ALL);

        // Envelope
        addElement(MD_BB_BOUNDINGBOX, MD_BOUNDEDBY, CHILD_POLICY_ALL);
        addAttribute(MD_BB_BOUNDINGBOX, MD_BB_SRSNAME, DATATYPE_STRING, true, null);
        addElement(MD_BB_LC, MD_BB_BOUNDINGBOX, CHILD_POLICY_EMPTY);
        addElement(MD_BB_UC, MD_BB_BOUNDINGBOX, CHILD_POLICY_EMPTY);

        // Vertical Extent
        addElement(MD_BB_VERTICALEXTENT, MD_BOUNDEDBY, CHILD_POLICY_CHOICE);
        addElement(MD_BB_VE_SINGLEVALUE, MD_BB_VERTICALEXTENT,CHILD_POLICY_EMPTY);

        addElement(MD_BB_VE_VERTICALRANGE, MD_BB_VERTICALEXTENT,CHILD_POLICY_SOME);
        addElement(MD_BB_VE_VR_MIN, MD_BB_VE_VERTICALRANGE, CHILD_POLICY_EMPTY);
        addElement(MD_BB_VE_VR_MAX, MD_BB_VE_VERTICALRANGE, CHILD_POLICY_EMPTY);
        addElement(MD_BB_VE_VR_RES, MD_BB_VE_VERTICALRANGE, CHILD_POLICY_EMPTY);

        // Temporal Extent
        addElement(MD_BB_TEMPORALEXTENT, MD_BOUNDEDBY, CHILD_POLICY_CHOICE);
        addElement(MD_BB_TE_TIMEPOSITION, MD_BB_TEMPORALEXTENT, CHILD_POLICY_EMPTY);
        addElement(MD_BB_TE_TIMEPERIOD, MD_BB_TEMPORALEXTENT, CHILD_POLICY_EMPTY);

        addElement(MD_BB_TE_TP_BEGIN, MD_BB_TE_TIMEPERIOD, CHILD_POLICY_EMPTY);
        addElement(MD_BB_TE_TP_END, MD_BB_TE_TIMEPERIOD, CHILD_POLICY_EMPTY);
        addElement(MD_BB_TE_TP_RES, MD_BB_TE_TIMEPERIOD, CHILD_POLICY_EMPTY);

        // //
        // Rectified Grid
        // //
        addElement(MD_RECTIFIEDGRID, FORMAT_NAME, CHILD_POLICY_ALL);
        addElement(MD_RG_LIMITS, MD_RECTIFIEDGRID, CHILD_POLICY_ALL);
        addElement(MD_RG_LI_RASTERLAYOUT, MD_RG_LIMITS, CHILD_POLICY_EMPTY);
        addAttribute(MD_RG_LI_RASTERLAYOUT, MD_RG_LI_RL_LOW, DATATYPE_STRING,true, null);
        addAttribute(MD_RG_LI_RASTERLAYOUT, MD_RG_LI_RL_HIGH, DATATYPE_STRING,true, null);

        addElement(MD_RG_ORIGIN, MD_RECTIFIEDGRID, CHILD_POLICY_ALL);
        addElement(MD_RG_OR_POINT, MD_RG_ORIGIN, CHILD_POLICY_ALL);
        addElement(MD_RG_OR_PT_COORD, MD_RG_OR_POINT, CHILD_POLICY_EMPTY);

        addElement(MD_BANDS, FORMAT_NAME, CHILD_POLICY_REPEAT);

        // Band Element
        addElement(MD_BD, MD_BANDS, CHILD_POLICY_ALL);
        addElement(MD_BD_KEY, MD_BD, CHILD_POLICY_EMPTY);
        addElement(MD_BD_DESCRIPTION, MD_BD, CHILD_POLICY_EMPTY);
        addElement(MD_BD_NODATA, MD_BD, CHILD_POLICY_EMPTY);
        addElement(MD_BD_VALIDRANGE, MD_BD, CHILD_POLICY_EMPTY);
        addElement(MD_BD_SCALE, MD_BD, CHILD_POLICY_EMPTY);
        addElement(MD_BD_OFFSET, MD_BD, CHILD_POLICY_EMPTY);

        // Band -> Categories elements
        addElement(MD_BD_CATEGORIES, MD_BD, CHILD_POLICY_REPEAT);
        addElement(MD_BD_CAT, MD_BD_CATEGORIES, CHILD_POLICY_ALL);
        addElement(MD_BD_CAT_NAME, MD_BD_CAT, CHILD_POLICY_EMPTY);
        addElement(MD_BD_CAT_DESCRIPTION, MD_BD_CAT, CHILD_POLICY_EMPTY);
        addElement(MD_BD_CAT_RANGE, MD_BD_CAT, CHILD_POLICY_EMPTY);

        // // Band -> Calibration
        // addElement(MD_SD_CALIBRATION, MD_SD, CHILD_POLICY_ALL);
        // addElement(MD_SD_CAL_SCALE, MD_SD_CALIBRATION, CHILD_POLICY_ALL);
        // addElement(MD_SD_CAL_VALUE, MD_SD_CAL_SCALE, CHILD_POLICY_EMPTY);
        // addElement(MD_SD_CAL_ERROR, MD_SD_CAL_SCALE, CHILD_POLICY_EMPTY);
        // addElement(MD_SD_CAL_OFFSET, MD_SD_CALIBRATION, CHILD_POLICY_ALL);
        // addElement(MD_SD_CAL_VALUE, MD_SD_CAL_OFFSET, CHILD_POLICY_EMPTY);
        // addElement(MD_SD_CAL_ERROR, MD_SD_CAL_OFFSET, CHILD_POLICY_EMPTY);

        // Band -> Statistics
        addElement(MD_BD_STATISTICS, MD_BD, CHILD_POLICY_SOME);
        addElement(MD_BD_STAT_MEAN, MD_BD_STATISTICS, CHILD_POLICY_EMPTY);
        addElement(MD_BD_STAT_MODE, MD_BD_STATISTICS, CHILD_POLICY_EMPTY);
        addElement(MD_BD_STAT_STDDEV, MD_BD_STATISTICS, CHILD_POLICY_EMPTY);
        addElement(MD_BD_STAT_HISTOGRAM, MD_BD_STATISTICS, CHILD_POLICY_ALL);
        addAttribute(MD_BD_STAT_HISTOGRAM, MD_BD_STAT_HIST_BINS,DATATYPE_INTEGER, true, null);
        addElement(MD_BD_STAT_HIST_VALUES, MD_BD_STAT_HISTOGRAM,CHILD_POLICY_EMPTY);
    }

    public boolean canNodeAppear(String elementName,
            ImageTypeSpecifier imageType) {
        return false;
    }
}
