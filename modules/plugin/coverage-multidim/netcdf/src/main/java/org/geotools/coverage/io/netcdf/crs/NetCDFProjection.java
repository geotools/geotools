/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.Ellipsoid;
import si.uom.SI;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.constants.CF;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.unidata.geoloc.LatLonPointImpl;

/**
 * Class used to properly setup NetCDF CF Projection parameters. Given a known OGC Projection, it
 * will take care of remapping the Projection's parameters to NetCDF CF GridMapping parameters if
 * supported.
 *
 * @see <a
 *     href="http://cfconventions.org/Data/cf-conventions/cf-conventions-1.6/build/cf-conventions.html#appendix-grid-mappings">NetCDF
 *     CF, Appendix F: Grid Mappings</a>
 */
public class NetCDFProjection {

    /** A Custom {@link CRSAuthorityFactory} used to parse custom NetCDF/GRIB CRSs */
    private static List<CRSAuthorityFactory> crsFactories = new LinkedList<CRSAuthorityFactory>();

    public static final String PARAMS_SEPARATOR = "#";

    private static final java.util.logging.Logger LOGGER =
            Logger.getLogger(NetCDFProjection.class.toString());

    /**
     * Inner class for CoordinateReferenceSystem parsing on top of different NetCDF attributes, such
     * as {@link NetCDFUtilities#SPATIAL_REF}, {@link NetCDFUtilities#CERP_ESRI_PE_STRING}, {@link
     * NetCDFUtilities#GRID_MAPPING}.
     */
    abstract static class CRSParser {
        protected Attribute attribute;

        private CRSParser(Attribute attribute) {
            this.attribute = attribute;
        }

        /** Parse the Coordinate Reference System */
        abstract CoordinateReferenceSystem parseCoordinateReferenceSystem(
                Variable variable, Map<String, Object> crsProperties) throws FactoryException;

        /**
         * Extract the {@link CoordinateReferenceSystem} from a NetCDF attribute (if present)
         * containing a WKT String
         *
         * @param wktAttribute the NetCDF {@link Attribute} if any, containing WKT definition.
         */
        private static CoordinateReferenceSystem parseWKT(Attribute wktAttribute) {
            CoordinateReferenceSystem crs = null;
            if (wktAttribute != null) {
                String wkt = wktAttribute.getStringValue();
                try {
                    crs = CRS.parseWKT(wkt);
                } catch (FactoryException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("Unable to setup a CRS from the specified WKT: " + wkt);
                    }
                }
            }
            return crs;
        }

        /** Get a proper {@link CRSParser} for the input Variable. */
        public static NetCDFProjection.CRSParser getCRSParser(Variable var) {
            if (var != null) {
                Attribute attr = null;
                // check on crs related attributes which may contain a fully
                // defined WKT or a grid mapping reference
                if ((attr = var.findAttribute(NetCDFUtilities.CERP_ESRI_PE_STRING)) != null) {
                    return new WKTCRSParser(attr);
                } else if ((attr = var.findAttribute(NetCDFUtilities.SPATIAL_REF)) != null) {
                    return new WKTCRSParser(attr);
                } else if ((attr = var.findAttribute(NetCDFUtilities.GRID_MAPPING_NAME)) != null) {
                    return new GridMappingCRSParser(attr);
                }
            }
            return null;
        }

        /** A CRSParser implementation based on a WKT String */
        static class WKTCRSParser extends CRSParser {
            public WKTCRSParser(Attribute attribute) {
                super(attribute);
            }

            @Override
            public CoordinateReferenceSystem parseCoordinateReferenceSystem(
                    Variable variable, Map<String, Object> crsProperties) {
                return parseWKT(attribute);
            }
        }

        /** A CRSParser implementation based on a GridMapping definition */
        static class GridMappingCRSParser extends CRSParser {
            // This type of parser extracts the type of projection from the attribute
            // and setup a CRS from the variable's attributes containing definitions

            public GridMappingCRSParser(Attribute attribute) {
                super(attribute);
            }

            @Override
            public CoordinateReferenceSystem parseCoordinateReferenceSystem(
                    Variable var, Map<String, Object> crsProperties) throws FactoryException {

                String mappingName = attribute.getStringValue();
                String projectionName = getProjectionName(mappingName, var);

                // Getting the proper projection and set the projection parameters
                NetCDFProjection projection = supportedProjections.get(projectionName);
                if (projection == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Unsupported grid_mapping_name: " + projectionName);
                    }
                    return null;
                }
                String ogcName = projection.getOGCName();

                // The OGC projection parameters
                ParameterValueGroup netcdfParameters =
                        ProjectionBuilder.getDefaultparameters(ogcName);

                // Get the OGC to NetCDF projection parameters
                Map<String, String> paramsMapping = projection.getParameters();
                Set<String> keys = paramsMapping.keySet();
                for (String ogcParameterKey : keys) {
                    handleParam(paramsMapping, netcdfParameters, ogcParameterKey, var);
                }

                Map<String, Object> map = new HashMap<String, Object>(crsProperties);
                map.put(NetCDFUtilities.NAME, projectionName);
                return ProjectionBuilder.buildCRS(
                        map,
                        projection.getOgcParameters(netcdfParameters),
                        buildEllipsoid(var, SI.METRE));
            }
        }
    }

    /** NetCDF CF projection constructor */
    public NetCDFProjection(
            String projectionName, String ogcName, Map<String, String> parametersMapping) {
        this.name = projectionName;
        this.ogcName = ogcName;
        this.netCDFParametersMapping = Collections.unmodifiableMap(parametersMapping);
    }

    /**
     * Mapping between OGC Referencing Parameters and NetCDF Projection attributes
     *
     * <p>As an instance: CENTRAL_MERIDIAN <-> CF.LONGITUDE_OF_PROJECTION_ORIGIN SCALE_FACTOR <->
     * CF.SCALE_FACTOR_AT_CENTRAL_MERIDIAN
     */
    private Map<String, String> netCDFParametersMapping;

    /** The NetCDF-CF GridMapping name */
    private String name;

    /** The OGC Projection name, needed to instantiate projection parameters */
    private String ogcName;

    /** Returns the underlying unmodifiable Referencing to NetCDF parameters mapping. */
    public Map<String, String> getParameters() {
        return netCDFParametersMapping;
    }

    /** Return the NetCDF CF GridMapping name */
    public String getName() {
        return name;
    }

    /** Return the OGC/GeoTools projection name */
    public String getOGCName() {
        return ogcName;
    }

    /**
     * Subclasses override this if they wish to adjust OGC parameters after they are read from
     * NetCDF. This is the inverse of {@link #getNetcdfParameters(ParameterValueGroup)}.
     *
     * @param netcdfParameters parameter values read from NetCDF
     * @return parameter values used for OGC projection
     */
    public ParameterValueGroup getOgcParameters(ParameterValueGroup netcdfParameters) {
        return netcdfParameters;
    }

    /**
     * Subclasses override this if they wish to adjust OGC parameters before they are written to
     * NetCDF. This is the inverse of {@link #getOgcParameters(ParameterValueGroup)}.
     *
     * @param ogcParameters parameter values used for OGC projection
     * @return parameter values written to NetCDF
     */
    public ParameterValueGroup getNetcdfParameters(ParameterValueGroup ogcParameters) {
        return ogcParameters;
    }

    /**
     * Currently supported NetCDF projections. Check the CF Document
     *
     * @see <a
     *     href="http://cfconventions.org/Data/cf-conventions/cf-conventions-1.6/build/cf-conventions.html#appendix-grid-mappings">NetCDF
     *     CF, Appendix F: Grid Mappings</a>
     */
    public static final NetCDFProjection ALBERS_EQUAL_AREA;

    public static final NetCDFProjection MERCATOR_1SP;
    public static final NetCDFProjection MERCATOR_2SP;
    public static final NetCDFProjection LAMBERT_AZIMUTHAL_EQUAL_AREA;
    public static final NetCDFProjection TRANSVERSE_MERCATOR;
    public static final NetCDFProjection ORTHOGRAPHIC;
    public static final NetCDFProjection POLAR_STEREOGRAPHIC;
    public static final NetCDFProjection STEREOGRAPHIC;
    public static final NetCDFProjection LAMBERT_CONFORMAL_CONIC_1SP;
    public static final NetCDFProjection LAMBERT_CONFORMAL_CONIC_2SP;
    public static final NetCDFProjection ROTATED_POLE;

    /** The map of currently supported NetCDF CF Grid mappings */
    private static final Map<String, NetCDFProjection> supportedProjections =
            new HashMap<String, NetCDFProjection>();

    static {

        // CF parameters may be made of 2 Parameters separated by a SEPARATOR_CHAR
        // This is used on writing operations when we need to map the same OGC input parameter
        // to N different NetCDF parameters
        // As an instance this happens in Lambert_conformal_conic_1sp where
        // latitude of origin maps on both latitude_of_projection_origin and standard_parallel

        // Setting up Albers conical equal area
        Map<String, String> alberseq_mapping = new HashMap<String, String>();
        alberseq_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_CENTRAL_MERIDIAN);
        alberseq_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        alberseq_mapping.put(NetCDFUtilities.STANDARD_PARALLEL_1, CF.STANDARD_PARALLEL);
        alberseq_mapping.put(NetCDFUtilities.STANDARD_PARALLEL_2, CF.STANDARD_PARALLEL);
        alberseq_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        alberseq_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);
        ALBERS_EQUAL_AREA =
                new NetCDFProjection(
                        CF.ALBERS_CONICAL_EQUAL_AREA, "Albers_Conic_Equal_Area", alberseq_mapping);

        // Setting up Lambert Azimuthal equal area
        Map<String, String> lazeq_mapping = new HashMap<String, String>();
        lazeq_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_PROJECTION_ORIGIN);
        lazeq_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        lazeq_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        lazeq_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);
        LAMBERT_AZIMUTHAL_EQUAL_AREA =
                new NetCDFProjection(
                        CF.LAMBERT_AZIMUTHAL_EQUAL_AREA,
                        CF.LAMBERT_AZIMUTHAL_EQUAL_AREA,
                        lazeq_mapping);

        // Setting up Transverse Mercator
        Map<String, String> tm_mapping = new HashMap<String, String>();
        tm_mapping.put(NetCDFUtilities.SCALE_FACTOR, CF.SCALE_FACTOR_AT_CENTRAL_MERIDIAN);
        tm_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_CENTRAL_MERIDIAN);
        tm_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        tm_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        tm_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);
        TRANSVERSE_MERCATOR =
                new NetCDFProjection(CF.TRANSVERSE_MERCATOR, CF.TRANSVERSE_MERCATOR, tm_mapping);

        // Setting up Orthographic
        Map<String, String> ortho_mapping = new HashMap<String, String>();
        ortho_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_PROJECTION_ORIGIN);
        ortho_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        ortho_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        ortho_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);
        ORTHOGRAPHIC = new NetCDFProjection(CF.ORTHOGRAPHIC, CF.ORTHOGRAPHIC, ortho_mapping);

        // Setting up Polar Stereographic
        Map<String, String> polarstereo_mapping = new HashMap<String, String>();
        polarstereo_mapping.put(
                NetCDFUtilities.CENTRAL_MERIDIAN, CF.STRAIGHT_VERTICAL_LONGITUDE_FROM_POLE);
        polarstereo_mapping.put(
                NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        polarstereo_mapping.put(NetCDFUtilities.SCALE_FACTOR, CF.SCALE_FACTOR_AT_PROJECTION_ORIGIN);
        polarstereo_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        polarstereo_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);
        POLAR_STEREOGRAPHIC =
                new NetCDFProjection(
                        CF.POLAR_STEREOGRAPHIC, CF.POLAR_STEREOGRAPHIC, polarstereo_mapping);

        // Setting up Stereographic
        Map<String, String> stereo_mapping = new HashMap<String, String>();
        stereo_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_PROJECTION_ORIGIN);
        stereo_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        stereo_mapping.put(NetCDFUtilities.SCALE_FACTOR, CF.SCALE_FACTOR_AT_PROJECTION_ORIGIN);
        stereo_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        stereo_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);
        STEREOGRAPHIC = new NetCDFProjection(CF.STEREOGRAPHIC, CF.STEREOGRAPHIC, stereo_mapping);

        // Setting up Lambert Conformal Conic base params
        Map<String, String> lcc_mapping = new HashMap<String, String>();
        lcc_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_CENTRAL_MERIDIAN);
        lcc_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        lcc_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        lcc_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);

        // Setting up Lambert Conformal Conic 1SP
        Map<String, String> lcc_1sp_mapping = new HashMap<String, String>();
        lcc_1sp_mapping.putAll(lcc_mapping);
        lcc_1sp_mapping.put(
                NetCDFUtilities.LATITUDE_OF_ORIGIN,
                CF.LATITUDE_OF_PROJECTION_ORIGIN + PARAMS_SEPARATOR + CF.STANDARD_PARALLEL);
        LAMBERT_CONFORMAL_CONIC_1SP =
                new NetCDFProjection(
                        CF.LAMBERT_CONFORMAL_CONIC,
                        CF.LAMBERT_CONFORMAL_CONIC + "_1SP",
                        lcc_1sp_mapping);

        // Setting up Lambert Conformal Conic 2SP
        Map<String, String> lcc_2sp_mapping = new HashMap<String, String>();
        lcc_2sp_mapping.putAll(lcc_mapping);
        lcc_2sp_mapping.put(NetCDFUtilities.STANDARD_PARALLEL_1, CF.STANDARD_PARALLEL);
        lcc_2sp_mapping.put(NetCDFUtilities.STANDARD_PARALLEL_2, CF.STANDARD_PARALLEL);
        LAMBERT_CONFORMAL_CONIC_2SP =
                new NetCDFProjection(
                        CF.LAMBERT_CONFORMAL_CONIC,
                        CF.LAMBERT_CONFORMAL_CONIC + "_2SP",
                        lcc_2sp_mapping);

        // Settinc up Mercator base params
        Map<String, String> mercator_mapping = new HashMap<String, String>();
        mercator_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.LONGITUDE_OF_CENTRAL_MERIDIAN);
        mercator_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.LATITUDE_OF_PROJECTION_ORIGIN);
        mercator_mapping.put(NetCDFUtilities.FALSE_EASTING, CF.FALSE_EASTING);
        mercator_mapping.put(NetCDFUtilities.FALSE_NORTHING, CF.FALSE_NORTHING);

        // Setting up Mercator 1SP
        Map<String, String> mercator_1sp_mapping = new HashMap<String, String>();
        mercator_1sp_mapping.putAll(mercator_mapping);
        mercator_1sp_mapping.put(
                NetCDFUtilities.SCALE_FACTOR, CF.SCALE_FACTOR_AT_PROJECTION_ORIGIN);
        MERCATOR_1SP =
                new NetCDFProjection(CF.MERCATOR, CF.MERCATOR + "_1SP", mercator_1sp_mapping);

        // Setting up Mercator 2SP
        Map<String, String> mercator_2sp_mapping = new HashMap<String, String>();
        mercator_2sp_mapping.putAll(mercator_mapping);
        mercator_2sp_mapping.put(NetCDFUtilities.STANDARD_PARALLEL_1, CF.STANDARD_PARALLEL);
        MERCATOR_2SP =
                new NetCDFProjection(CF.MERCATOR, CF.MERCATOR + "_2SP", mercator_2sp_mapping);

        // Setting up Rotated Pole
        Map<String, String> rotated_pole_mapping = new HashMap<String, String>();
        rotated_pole_mapping.put(NetCDFUtilities.CENTRAL_MERIDIAN, CF.GRID_NORTH_POLE_LONGITUDE);
        rotated_pole_mapping.put(NetCDFUtilities.LATITUDE_OF_ORIGIN, CF.GRID_NORTH_POLE_LATITUDE);
        ROTATED_POLE =
                new NetCDFProjection(
                        CF.ROTATED_LATITUDE_LONGITUDE, "Rotated_Pole", rotated_pole_mapping) {

                    /*
                     * Convert north_pole_longitude and north_pole_latitude to central_meridian and latitude_of_origin.
                     */
                    @Override
                    public ParameterValueGroup getOgcParameters(
                            ParameterValueGroup netcdfParameters) {
                        double lonNorthPole =
                                (Double)
                                        netcdfParameters
                                                .parameter(NetCDFUtilities.CENTRAL_MERIDIAN)
                                                .getValue();
                        double latNorthPole =
                                (Double)
                                        netcdfParameters
                                                .parameter(NetCDFUtilities.LATITUDE_OF_ORIGIN)
                                                .getValue();
                        // Rotated pole is ambiguous so we assume an origin in the northern
                        // hemisphere
                        if (latNorthPole >= 90 || latNorthPole <= 0) {
                            throw new RuntimeException(
                                    "Unexpected north pole latitude: " + latNorthPole);
                        }
                        double lonOrigin = LatLonPointImpl.lonNormal(lonNorthPole + 180);
                        double latOrigin = 90 - latNorthPole;
                        ParameterValueGroup ogcParameters = netcdfParameters.clone();
                        ogcParameters
                                .parameter(NetCDFUtilities.CENTRAL_MERIDIAN)
                                .setValue(lonOrigin);
                        ogcParameters
                                .parameter(NetCDFUtilities.LATITUDE_OF_ORIGIN)
                                .setValue(latOrigin);
                        return ogcParameters;
                    }

                    /*
                     * Convert central_meridian and latitude_of_origin to north_pole_longitude and north_pole_latitude.
                     */
                    @Override
                    public ParameterValueGroup getNetcdfParameters(
                            ParameterValueGroup ogcParameters) {
                        double lonOrigin =
                                (Double)
                                        ogcParameters
                                                .parameter(NetCDFUtilities.CENTRAL_MERIDIAN)
                                                .getValue();
                        double latOrigin =
                                (Double)
                                        ogcParameters
                                                .parameter(NetCDFUtilities.LATITUDE_OF_ORIGIN)
                                                .getValue();
                        // Rotated pole is ambiguous so we assumed above an origin in the
                        // northern hemisphere and do not expect anything else here
                        if (latOrigin >= 90 || latOrigin <= 0) {
                            throw new RuntimeException(
                                    "Unexpected latitude of origin: " + latOrigin);
                        }
                        double lonNorthPole = LatLonPointImpl.lonNormal(lonOrigin + 180);
                        double latNorthPole = 90 - latOrigin;
                        ParameterValueGroup netcdfParameters = ogcParameters.clone();
                        netcdfParameters
                                .parameter(NetCDFUtilities.CENTRAL_MERIDIAN)
                                .setValue(lonNorthPole);
                        netcdfParameters
                                .parameter(NetCDFUtilities.LATITUDE_OF_ORIGIN)
                                .setValue(latNorthPole);
                        return netcdfParameters;
                    }
                };

        supportedProjections.put(CF.ALBERS_CONICAL_EQUAL_AREA, ALBERS_EQUAL_AREA);
        supportedProjections.put(CF.MERCATOR + "_1SP", MERCATOR_1SP);
        supportedProjections.put(CF.MERCATOR + "_1SP", MERCATOR_2SP);
        supportedProjections.put(TRANSVERSE_MERCATOR.name, TRANSVERSE_MERCATOR);
        supportedProjections.put(CF.LAMBERT_CONFORMAL_CONIC + "_1SP", LAMBERT_CONFORMAL_CONIC_1SP);
        supportedProjections.put(CF.LAMBERT_CONFORMAL_CONIC + "_2SP", LAMBERT_CONFORMAL_CONIC_2SP);
        supportedProjections.put(LAMBERT_AZIMUTHAL_EQUAL_AREA.name, LAMBERT_AZIMUTHAL_EQUAL_AREA);
        supportedProjections.put(ORTHOGRAPHIC.name, ORTHOGRAPHIC);
        supportedProjections.put(POLAR_STEREOGRAPHIC.name, POLAR_STEREOGRAPHIC);
        supportedProjections.put(STEREOGRAPHIC.name, STEREOGRAPHIC);
        supportedProjections.put(ROTATED_POLE.name, ROTATED_POLE);

        for (final CRSAuthorityFactory factory :
                ReferencingFactoryFinder.getCRSAuthorityFactories(null)) {
            // Retrieve the registered custom factory
            final CRSAuthorityFactory f = (CRSAuthorityFactory) factory;

            // There may be multiple factories. Let take them in prioritized order
            // using the linkedList
            if (f instanceof NetCDFCRSAuthorityFactory) {
                crsFactories.add(f);
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("NetCDF CRS Factory found: " + f);
                }
            }
        }

        // TODO:
        // AZIMUTHAL_EQUIDISTANT, LAMBERT_CYLINDRICAL_EQUAL_AREA,
    }

    /** Get a NetCDF Projection definition referred by name */
    public static NetCDFProjection getSupportedProjection(String projectionName) {
        if (supportedProjections.containsKey(projectionName)) {
            return supportedProjections.get(projectionName);
        } else {
            LOGGER.severe("The specified projection isn't currently supported: " + projectionName);
            return null;
        }
    }

    /**
     * Extract the georeferencing projection information from the specified variable and setup a
     * {@link CoordinateReferenceSystem} instance
     */
    public static CoordinateReferenceSystem parseProjection(Variable var) throws FactoryException {
        return parseProjection(var, CRSParser.getCRSParser(var));
    }

    /**
     * Extract the georeferencing projection information from the specified variable and setup a
     * {@link CoordinateReferenceSystem} instance
     */
    public static CoordinateReferenceSystem parseProjection(Variable var, CRSParser crsParser)
            throws FactoryException {
        return parseProjection(var, crsParser, Collections.emptyMap());
    }

    /**
     * Extract the georeferencing projection information from the specified variable and setup a
     * {@link CoordinateReferenceSystem} instance
     */
    public static CoordinateReferenceSystem parseProjection(
            Variable var, CRSParser crsParser, Map<String, Object> crsProperties)
            throws FactoryException {
        if (crsParser == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "No referencing attributes have been found.\n "
                                + "Unable to parse a CF projection from this variable.\n"
                                + "This probably means that is WGS84 or unsupported");
            }
            return null;
        }

        return crsParser.parseCoordinateReferenceSystem(var, crsProperties);
    }

    /**
     * Get the NetCDF Attribute related to the specified OGC parameter and set the proper value
     * within the OGC parameters map.
     */
    private static void handleParam(
            Map<String, String> parametersMapping,
            ParameterValueGroup ogcParameters,
            String ogcParameterKey,
            Variable var) {
        String netCDFattributeName = getInputAttribute(parametersMapping.get(ogcParameterKey));

        Double value = null;

        // Special case for standard parallels
        if (ogcParameterKey.equalsIgnoreCase(NetCDFUtilities.STANDARD_PARALLEL_1)
                || ogcParameterKey.equalsIgnoreCase(NetCDFUtilities.STANDARD_PARALLEL_2)) {
            Attribute attribute = var.findAttribute(netCDFattributeName);
            if (attribute != null) {
                final int numValues = attribute.getLength();
                if (numValues > 1) {
                    // Get the proper standard parallel if that's the case
                    int index =
                            ogcParameterKey.equalsIgnoreCase(NetCDFUtilities.STANDARD_PARALLEL_1)
                                    ? 0
                                    : 1;
                    Number number = (Number) attribute.getValue(index);
                    value = number.doubleValue();
                } else {
                    value = attribute.getNumericValue().doubleValue();
                }
            }
        } else {

            Attribute attribute = var.findAttribute(netCDFattributeName);
            if (attribute != null) {
                // Get the parameter value and handle special management for longitudes outside
                // -180, 180
                value = attribute.getNumericValue().doubleValue();
                if (netCDFattributeName.contains("meridian")
                        || netCDFattributeName.contains("longitude")) {
                    value = value - (360) * Math.floor(value / (360) + 0.5);
                }
            }
        }
        if (value != null) {
            // Set the OGC parameter
            ogcParameters.parameter(ogcParameterKey).setValue(value);
        }
    }

    private static String getInputAttribute(String cfParam) {
        if (cfParam != null) {
            return cfParam.contains(PARAMS_SEPARATOR)
                    ? cfParam.split(PARAMS_SEPARATOR)[0]
                    : cfParam;
        }
        return null;
    }

    /**
     * Build a custom ellipsoid, looking for definition parameters from a GridMapping variable
     *
     * @param gridMappingVariable the variable to be analyzed
     * @param linearUnit the linear Unit to be used for the ellipsoid
     */
    private static Ellipsoid buildEllipsoid(Variable gridMappingVariable, Unit<Length> linearUnit) {
        Number semiMajorAxis = null;
        Number semiMinorAxis = null;
        Double inverseFlattening = Double.NEGATIVE_INFINITY;

        // Preparing ellipsoid params to be sent to the NetCDFProjectionBuilder class
        // in order to get back an Ellipsoid
        Map<String, Number> ellipsoidParams = new HashMap<String, Number>();

        // Looking for semiMajorAxis first
        Attribute semiMajorAxisAttribute = gridMappingVariable.findAttribute(CF.SEMI_MAJOR_AXIS);
        if (semiMajorAxisAttribute != null) {
            semiMajorAxis = semiMajorAxisAttribute.getNumericValue();
            ellipsoidParams.put(NetCDFUtilities.SEMI_MAJOR, semiMajorAxis);
        }

        // If not present, maybe it's a sphere. Looking for the radius
        if (semiMajorAxis == null) {
            semiMajorAxisAttribute = gridMappingVariable.findAttribute(CF.EARTH_RADIUS);
            if (semiMajorAxisAttribute != null) {
                semiMajorAxis = semiMajorAxisAttribute.getNumericValue();
                ellipsoidParams.put(NetCDFUtilities.SEMI_MAJOR, semiMajorAxis);
            }
        }

        // Looking for semiMinorAxis
        Attribute semiMinorAxisAttribute = gridMappingVariable.findAttribute(CF.SEMI_MINOR_AXIS);
        if (semiMinorAxisAttribute != null) {
            semiMinorAxis = semiMinorAxisAttribute.getNumericValue();
            ellipsoidParams.put(NetCDFUtilities.SEMI_MINOR, semiMinorAxis);
        }

        if (semiMinorAxis == null) {
            // Looking for inverse Flattening
            Attribute inverseFlatteningAttribute =
                    gridMappingVariable.findAttribute(CF.INVERSE_FLATTENING);
            if (inverseFlatteningAttribute != null) {
                inverseFlattening = inverseFlatteningAttribute.getNumericValue().doubleValue();
            }
            ellipsoidParams.put(NetCDFUtilities.INVERSE_FLATTENING, inverseFlattening);
        }

        // Ellipsoid parameters have been set. Getting back an Ellipsoid from the
        // builder
        return ProjectionBuilder.createEllipsoid(NetCDFUtilities.UNKNOWN, ellipsoidParams);
    }

    /**
     * Adjust the mappingName if needed. This may happen for some projections where different
     * standard parallels may require _1SP or _2SP suffix.
     *
     * @param mappingName the input netCDF mappingName
     * @param var the gridMapping variable
     */
    private static String getProjectionName(String mappingName, Variable var) {
        String projectionName = mappingName;
        if (mappingName.equalsIgnoreCase(CF.LAMBERT_CONFORMAL_CONIC)) {
            Attribute standardParallel = var.findAttribute(CF.STANDARD_PARALLEL);
            // special Management for multiple standard parallels to use
            // the proper projection
            projectionName =
                    CF.LAMBERT_CONFORMAL_CONIC
                            + (standardParallel.getLength() == 1 ? "_1SP" : "_2SP");
        } else if (mappingName.equalsIgnoreCase(CF.MERCATOR)) {
            Attribute standardParallel = var.findAttribute(CF.STANDARD_PARALLEL);
            projectionName = CF.MERCATOR + (standardParallel == null ? "_2SP" : "_1SP");
        }
        return projectionName;
    }

    /**
     * Look for a SPATIAL_REF global attribute and parsing it (as WKT) to setup a {@link
     * CoordinateReferenceSystem}
     */
    public static CoordinateReferenceSystem parseProjection(NetcdfDataset dataset) {
        Attribute attribute = dataset.findAttribute(NetCDFUtilities.SPATIAL_REF);
        return CRSParser.parseWKT(attribute);
    }

    /** Check if any custom EPSG maps the provided crs and return that one */
    public static CoordinateReferenceSystem lookupForCustomEpsg(CoordinateReferenceSystem crs)
            throws FactoryException {
        if (!crsFactories.isEmpty()) {
            for (CRSAuthorityFactory crsFactory : crsFactories) {
                Set<String> codes = crsFactory.getAuthorityCodes(CoordinateReferenceSystem.class);
                for (String code : codes) {
                    CoordinateReferenceSystem decodedCRS = CRS.decode("EPSG:" + code);
                    if (CRS.equalsIgnoreMetadata(decodedCRS, crs)) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine("Found valid epsgCode for the custom CRS: " + code);
                        }
                        return decodedCRS;
                    }
                }
            }
        }
        return crs;
    }

    public static CoordinateReferenceSystem lookForVariableCRS(
            NetcdfDataset dataset, CoordinateReferenceSystem defaultCrs) throws FactoryException {
        return lookForVariableCRS(dataset, defaultCrs, Collections.emptyMap());
    }

    /** Look for a CoordinateReferenceSystem defined into a variable */
    public static CoordinateReferenceSystem lookForVariableCRS(
            NetcdfDataset dataset,
            CoordinateReferenceSystem defaultCrs,
            Map<String, Object> crsProperties)
            throws FactoryException {
        List<Variable> variables = dataset.getVariables();
        CoordinateReferenceSystem crs = defaultCrs;
        for (Variable variable : variables) {
            CRSParser attrib = CRSParser.getCRSParser(variable);
            if (attrib != null) {
                // Referencing info found
                crs = NetCDFProjection.parseProjection(variable, attrib, crsProperties);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Detected NetCDFProjection through gridMapping variable: "
                                    + (crs != null ? crs.toWKT() : "null"));
                }
                break;
            }
        }
        return crs;
    }

    /**
     * Look for a dataset global {@link CoordinateReferenceSystem} definition provided through a
     * spatial_ref global attribute.
     */
    public static CoordinateReferenceSystem lookForDatasetCRS(NetcdfDataset dataset) {
        CoordinateReferenceSystem projection = NetCDFProjection.parseProjection(dataset);
        if (projection != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Detected NetCDFProjection through spatial_ref attribute: "
                                + projection.toWKT());
            }
        }
        return projection;
    }
}
