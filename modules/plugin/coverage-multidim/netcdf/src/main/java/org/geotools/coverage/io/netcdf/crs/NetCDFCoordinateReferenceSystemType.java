/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.crs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.Projection;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.AlbersEqualArea;
import org.geotools.referencing.operation.projection.LambertAzimuthalEqualArea;
import org.geotools.referencing.operation.projection.LambertConformal1SP;
import org.geotools.referencing.operation.projection.LambertConformal2SP;
import org.geotools.referencing.operation.projection.Mercator1SP;
import org.geotools.referencing.operation.projection.Mercator2SP;
import org.geotools.referencing.operation.projection.Orthographic;
import org.geotools.referencing.operation.projection.PolarStereographic;
import org.geotools.referencing.operation.projection.RotatedPole;
import org.geotools.referencing.operation.projection.Stereographic;
import org.geotools.referencing.operation.projection.TransverseMercator;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.util.logging.Logging;

/**
 * Enum used to represent different coordinate reference systems stored within a NetCDF dataset. NetCDF CF supports
 * several types of projections through grid mapping.
 *
 * <p>Unsupported projections will be specified through the spatial_ref and GeoTransform global attributes defined by
 * GDAL.
 *
 * @see <a
 *     href="http://cfconventions.org/Data/cf-conventions/cf-conventions-1.6/build/cf-conventions.html#appendix-grid-mappings">NetCDF
 *     CF, Appendix F: Grid Mappings</a>
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public enum NetCDFCoordinateReferenceSystemType {
    WGS84 {
        @Override
        public NetCDFCoordinate[] getCoordinates() {
            return NetCDFCoordinate.LATLON_COORDS;
        }

        @Override
        public NetCDFProjection getNetCDFProjection() {
            // No projection/gridMapping is needed for WGS84.
            // Coordinates can be used as is
            return null;
        }
    },
    SPATIAL_REF {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            // No Specific NetCDF CF Projection is available for this type.
            // We need to parse the SPATIAL_REF attribute
            // to setup a proper one
            return null;
        }
    },
    ALBERS_EQUAL_AREA {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.ALBERS_EQUAL_AREA;
        }
    },
    LAMBERT_AZIMUTHAL_EQUAL_AREA {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.LAMBERT_AZIMUTHAL_EQUAL_AREA;
        }
    },
    LAMBERT_CONFORMAL_CONIC_1SP {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.LAMBERT_CONFORMAL_CONIC_1SP;
        }
    },
    LAMBERT_CONFORMAL_CONIC_2SP {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.LAMBERT_CONFORMAL_CONIC_2SP;
        }
    },
    MERCATOR_1SP {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.MERCATOR_1SP;
        }
    },
    MERCATOR_2SP {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.MERCATOR_2SP;
        }
    },
    TRANSVERSE_MERCATOR {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.TRANSVERSE_MERCATOR;
        }
    },
    ORTHOGRAPHIC {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.ORTHOGRAPHIC;
        }
    },
    POLAR_STEREOGRAPHIC {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.POLAR_STEREOGRAPHIC;
        }
    },
    STEREOGRAPHIC {
        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.STEREOGRAPHIC;
        }
    },
    ROTATED_POLE {
        @Override
        public NetCDFCoordinate[] getCoordinates() {
            return NetCDFCoordinate.RLATLON_COORDS;
        }

        @Override
        public NetCDFProjection getNetCDFProjection() {
            return NetCDFProjection.ROTATED_POLE;
        }
    };

    /* TODO: THESE CRSs still need to be added
     * AZIMUTHAL_EQUIDISTANT, LAMBERT_CYLINDRICAL_EQUAL_AREA
     */

    /**
     * Return a proper {@link NetCDFCoordinateReferenceSystemType} depending on the input OGC
     * {@link CoordinateReferenceSystem} instance.
     */
    public static NetCDFCoordinateReferenceSystemType parseCRS(CoordinateReferenceSystem crs) {
        NetCDFCoordinateReferenceSystemType crsType = null;
        if (crs instanceof DefaultGeographicCRS) {
            crsType = WGS84;
        } else if (crs instanceof DefaultDerivedCRS
                && ((DefaultDerivedCRS) crs).getConversionFromBase().getMathTransform() instanceof RotatedPole) {
            return ROTATED_POLE;
        } else if (crs instanceof ProjectedCRS) {
            ProjectedCRS projectedCRS = (ProjectedCRS) crs;
            // NetCDF supports a reduced number of CRS Type (WGS84 + some projection).
            // Some ProjectedCRS may have unit of measure resulting in concatenated transforms.
            if (hasConcatenatedTransform(projectedCRS)) {
                Projection projection = projectedCRS.getConversionFromBase();
                MathTransform mt = projection.getMathTransform();
                ConcatenatedTransform cmt = (ConcatenatedTransform) mt;
                crsType = extractProjectionType(cmt);
            } else {
                crsType = getProjectionType(projectedCRS);
            }

        } else {
            // Fallback on SPATIAL_REF to deal with projection which
            // doesn't have a CF Mapping.
            crsType = SPATIAL_REF;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Using a NetCDF CRS based on " + crsType);
        }
        return crsType;
    }

    private static NetCDFCoordinateReferenceSystemType extractProjectionType(ConcatenatedTransform cmt) {
        // Make sure we only have 2 transformations that are not concatenated further.
        if (!(cmt.transform1 instanceof ConcatenatedTransform) && !(cmt.transform2 instanceof ConcatenatedTransform)) {

            // Make sure the 1st transformation is supported
            NetCDFCoordinateReferenceSystemType projectionType = getProjectionType(cmt.transform1);
            if (projectionType != null) {
                return projectionType;
            }
        }
        throw new IllegalArgumentException("The specified projection's transformation is not supported: " + cmt);
    }

    private static boolean hasConcatenatedTransform(ProjectedCRS projectedCRS) {
        Projection projection = projectedCRS.getConversionFromBase();
        MathTransform mt = projection.getMathTransform();
        return mt instanceof ConcatenatedTransform;
    }

    private static NetCDFCoordinateReferenceSystemType getProjectionType(ProjectedCRS projectedCRS) {
        Projection projection = projectedCRS.getConversionFromBase();
        MathTransform transform = projection.getMathTransform();
        NetCDFCoordinateReferenceSystemType projectionType = getProjectionType(transform);
        if (projectionType == null) {
            throw new IllegalArgumentException("The specified projection is not supported: " + transform.getClass());
        }
        return projectionType;
    }

    /**
     * Return the Supported {@link NetCDFCoordinateReferenceSystemType} based on the conversionFromBase's MathTransform
     */
    private static NetCDFCoordinateReferenceSystemType getProjectionType(MathTransform transform) {
        if (transform instanceof TransverseMercator) {
            return TRANSVERSE_MERCATOR;
        } else if (transform instanceof LambertConformal1SP) {
            return LAMBERT_CONFORMAL_CONIC_1SP;
        } else if (transform instanceof LambertConformal2SP) {
            return LAMBERT_CONFORMAL_CONIC_2SP;
        } else if (transform instanceof LambertAzimuthalEqualArea) {
            return LAMBERT_AZIMUTHAL_EQUAL_AREA;
        } else if (transform instanceof Orthographic) {
            return ORTHOGRAPHIC;
        } else if (transform instanceof PolarStereographic) {
            return POLAR_STEREOGRAPHIC;
        } else if (transform instanceof Stereographic) {
            return STEREOGRAPHIC;
        } else if (transform instanceof Mercator1SP) {
            return MERCATOR_1SP;
        } else if (transform instanceof Mercator2SP) {
            return MERCATOR_2SP;
        } else if (transform instanceof AlbersEqualArea) {
            return ALBERS_EQUAL_AREA;
        } else if (transform instanceof RotatedPole) {
            return ROTATED_POLE;
        } else {
            return null;
        }
    }

    /**
     * Return the set of {@link NetCDFCoordinate}s for this NetCDF CRS type. As an instance, WGS84 type uses
     * Latitude,Longitude while Mercator uses GeoY,GeoX. Default implementation returns
     * {@link NetCDFCoordinate#YX_COORDS} since most part of the CRS Type are projected.
     */
    public NetCDFCoordinate[] getCoordinates() {
        return NetCDFCoordinate.YX_COORDS;
    }

    /**
     * Return a {@link NetCDFProjection} instance for this specific CRS type. Note that WGS84 CRS and SPATIAL_REF won't
     * return a NetCDF CF projection.
     */
    public abstract NetCDFProjection getNetCDFProjection();

    /**
     * Return the set of {@link NetCDFCoordinate}s for the current NetCDF CRS type, taking into account a reference crs.
     */
    public NetCDFCoordinate[] getCoordinates(CoordinateReferenceSystem crs) {
        NetCDFCoordinate[] axisCoordinates = getCoordinates();
        if (this != NetCDFCoordinateReferenceSystemType.WGS84) {
            // NetCDF only support WGS84 and a few projections
            // There might be the case that the projection contains a concatenatedTransform
            // due to a specific unit in the coordinate axis (as an instance, km
            // of classic meter).
            // Extract the unit of measure from the actual coordinate axis in that case.
            if (crs instanceof ProjectedCRS) {
                ProjectedCRS projectedCrs = (ProjectedCRS) crs;
                if (NetCDFCoordinateReferenceSystemType.hasConcatenatedTransform(projectedCrs)) {
                    Unit unit = crs.getCoordinateSystem().getAxis(0).getUnit();
                    NetCDFCoordinate[] newAxisCoordinates = new NetCDFCoordinate[2];
                    for (int i = 0; i < 2; i++) {
                        newAxisCoordinates[i] = new NetCDFCoordinate(axisCoordinates[i]);
                        newAxisCoordinates[i].setUnits(unit.toString());
                    }
                    axisCoordinates = newAxisCoordinates;
                }
            }
        }
        return axisCoordinates;
    }

    private static final Logger LOGGER = Logging.getLogger(NetCDFCoordinateReferenceSystemType.class);

    /**
     * Contains basic information related to a NetCDF Coordinate such as: - short name (as an instance: x) - long name
     * (as an instance: x coordinate of projection) - standard name (as an instance: projection_x_coordinate) - the name
     * of the associated dimension (as an instance: x) - the unit of measure of that coordinate (as an instance: m)
     */
    public static class NetCDFCoordinate {

        private static final NetCDFCoordinate LAT_COORDINATE = new NetCDFCoordinate(
                NetCDFUtilities.LAT,
                NetCDFUtilities.LATITUDE,
                NetCDFUtilities.LATITUDE,
                NetCDFUtilities.LAT,
                NetCDFUtilities.LAT_UNITS);

        private static final NetCDFCoordinate LON_COORDINATE = new NetCDFCoordinate(
                NetCDFUtilities.LON,
                NetCDFUtilities.LONGITUDE,
                NetCDFUtilities.LONGITUDE,
                NetCDFUtilities.LON,
                NetCDFUtilities.LON_UNITS);

        private static final NetCDFCoordinate RLAT_COORDINATE = new NetCDFCoordinate(
                NetCDFUtilities.RLAT,
                NetCDFUtilities.GRID_LATITUDE,
                NetCDFUtilities.GRID_LATITUDE,
                NetCDFUtilities.RLAT,
                NetCDFUtilities.RLATLON_UNITS);

        private static final NetCDFCoordinate RLON_COORDINATE = new NetCDFCoordinate(
                NetCDFUtilities.RLON,
                NetCDFUtilities.GRID_LONGITUDE,
                NetCDFUtilities.GRID_LONGITUDE,
                NetCDFUtilities.RLON,
                NetCDFUtilities.RLATLON_UNITS);

        private static final NetCDFCoordinate X_COORDINATE = new NetCDFCoordinate(
                NetCDFUtilities.X,
                NetCDFUtilities.X_COORD_PROJ,
                NetCDFUtilities.X_PROJ_COORD,
                NetCDFUtilities.X,
                NetCDFUtilities.M);

        private static final NetCDFCoordinate Y_COORDINATE = new NetCDFCoordinate(
                NetCDFUtilities.Y,
                NetCDFUtilities.Y_COORD_PROJ,
                NetCDFUtilities.Y_PROJ_COORD,
                NetCDFUtilities.Y,
                NetCDFUtilities.M);

        public NetCDFCoordinate(NetCDFCoordinate that) {
            this.shortName = that.getShortName();
            this.longName = that.getLongName();
            this.dimensionName = that.getDimensionName();
            this.standardName = that.getStandardName();
            this.units = that.getUnits();
        }

        public static final NetCDFCoordinate[] LATLON_COORDS = {LAT_COORDINATE, LON_COORDINATE};

        public static final NetCDFCoordinate[] RLATLON_COORDS = {RLAT_COORDINATE, RLON_COORDINATE};

        public static final NetCDFCoordinate[] YX_COORDS = {Y_COORDINATE, X_COORDINATE};

        /** short name. (as an instance: x) */
        private String shortName;

        /** the name of the associated dimension. (as an instance: x) */
        private String dimensionName;

        /** long name. (as an instance: x coordinate of projection) */
        private String longName;

        /** unit of measure of that coordinate (as an instance: m) */
        private String units;

        /** standard name (as an instance: projection_x_coordinate) */
        private String standardName;

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getDimensionName() {
            return dimensionName;
        }

        public void setDimensionName(String dimensionName) {
            this.dimensionName = dimensionName;
        }

        public String getLongName() {
            return longName;
        }

        public void setName(String longName) {
            this.longName = longName;
        }

        public String getStandardName() {
            return standardName;
        }

        public void setStandardName(String standardName) {
            this.standardName = standardName;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        @Override
        public String toString() {
            return "NetCDFCoordinate [shortName="
                    + shortName
                    + ", dimensionName="
                    + dimensionName
                    + ", longName="
                    + longName
                    + ", units="
                    + units
                    + ", standardName="
                    + standardName
                    + "]";
        }

        /** Create a {@link NetCDFCoordinate} instance with all the required information */
        public NetCDFCoordinate(
                String shortName, String longName, String standardName, String dimensionName, String units) {
            this.shortName = shortName;
            this.longName = longName;
            this.standardName = standardName;
            this.dimensionName = dimensionName;
            this.units = units;
        }
    }
}
