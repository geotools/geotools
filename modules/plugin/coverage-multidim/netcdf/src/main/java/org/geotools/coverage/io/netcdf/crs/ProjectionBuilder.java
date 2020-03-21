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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.format.ParserException;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.measure.Units;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.OperationMethod;
import si.uom.NonSI;
import si.uom.SI;

/**
 * Class used to create an OGC {@link ProjectedCRS} instance on top of Projection name, parameters
 * and Ellipsoid. A default datum will be created on top of that ellipsoid.
 */
public class ProjectionBuilder {

    private static final String NAME = "name";

    private static final String DEFAULT_DATUM_NAME = NetCDFUtilities.UNKNOWN;

    public static final String AXIS_UNIT = "axisUnit";

    /** Cached {@link MathTransformFactory} for building {@link MathTransform} objects. */
    private static final MathTransformFactory mtFactory;

    public static final EllipsoidalCS DEFAULT_ELLIPSOIDAL_CS =
            DefaultEllipsoidalCS.GEODETIC_2D.usingUnit(NonSI.DEGREE_ANGLE);

    static {
        Hints hints = GeoTools.getDefaultHints().clone();

        mtFactory = ReferencingFactoryFinder.getMathTransformFactory(hints);
    }

    private static final Logger LOGGER = Logging.getLogger(ProjectionBuilder.class);

    /**
     * Quick method to create a {@link CoordinateReferenceSystem} instance, given the OGC
     * ProjectionName, such as "lambert_conformal_conic_2sp"), a custom code number for it, the
     * semiMajor, the inverseFlattening (when infinity, assuming the reference ellipsoid is a
     * spheroid), and the Projection Params through a <key,value> map (as an instance:
     * <"central_meridian",-95>)
     */
    public static CoordinateReferenceSystem createProjection(
            String projectionName,
            String code,
            Double semiMajor,
            Double inverseFlattening,
            Map<String, Double> params)
            throws FactoryException {

        ParameterValueGroup parameters = getProjectionParameters(projectionName);

        Ellipsoid ellipsoid = createEllipsoid(semiMajor, inverseFlattening);

        // Datum
        Set<String> keys = params.keySet();
        for (String key : keys) {
            parameters.parameter(key).setValue(params.get(key));
        }
        return buildCRS(
                buildProperties(projectionName, Citations.EPSG, code), parameters, ellipsoid);
    }

    /** Get Projection parameters from the specified projection name. */
    public static ParameterValueGroup getProjectionParameters(String projectionName)
            throws NoSuchIdentifierException {
        return mtFactory.getDefaultParameters(projectionName);
    }

    /**
     * Make sure to set SEMI_MINOR and SEMI_MAJOR projection's parameters from the ellipsoid
     * definition
     */
    public static void updateEllipsoidParams(ParameterValueGroup parameters, Ellipsoid ellipsoid) {
        Utilities.ensureNonNull("ellipsoid", ellipsoid);
        Utilities.ensureNonNull("parameters", parameters);
        double semiMajor = ellipsoid.getSemiMajorAxis();
        double inverseFlattening = ellipsoid.getInverseFlattening();

        // setting missing parameters
        parameters
                .parameter(NetCDFUtilities.SEMI_MINOR)
                .setValue(semiMajor * (1 - (1 / inverseFlattening)));
        parameters.parameter(NetCDFUtilities.SEMI_MAJOR).setValue(semiMajor);
    }

    /** Create a {@link DefiningConversion} object from the input {@link MathTransform} */
    public static DefiningConversion createConversionFromBase(
            String name, MathTransform transform) {
        return new DefiningConversion(
                Collections.singletonMap(NAME, name),
                new DefaultOperationMethod(transform),
                transform);
    }

    static Map<String, Object> buildProperties(String name, Citation authority, String code) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(IdentifiedObject.NAME_KEY, name);
        props.put(IdentifiedObject.IDENTIFIERS_KEY, new NamedIdentifier(authority, code));
        return props;
    }

    /**
     * Build an ellipsoid provided semiMajor and inverseFlattening.
     *
     * @param semiMajor the semiMajor axis length in meters
     * @param inverseFlattening the inverseFlattening (when infinity, the ellipsoid will be a
     *     spheroid)
     */
    private static Ellipsoid createEllipsoid(Double semiMajor, Double inverseFlattening) {
        Map<String, Number> ellipsoidParams = new HashMap<String, Number>();
        ellipsoidParams.put(NetCDFUtilities.SEMI_MAJOR, semiMajor);
        if (!Double.isInfinite(inverseFlattening)) {
            ellipsoidParams.put(NetCDFUtilities.INVERSE_FLATTENING, inverseFlattening);
        }
        return createEllipsoid(NetCDFUtilities.UNKNOWN, ellipsoidParams);
    }

    /**
     * Build a Default {@link GeodeticDatum} on top of a specific {@link Ellipsoid} instance, using
     * {@link DefaultPrimeMeridian#GREENWICH} as primeMeridian.
     */
    public static GeodeticDatum createGeodeticDatum(String name, Ellipsoid ellipsoid) {
        return new DefaultGeodeticDatum(name, ellipsoid, DefaultPrimeMeridian.GREENWICH);
    }

    /**
     * Build a {@link GeographicCRS} given the name to be assigned and the {@link GeodeticDatum} to
     * be used. {@link EllipsoidalCS} is {@value #DEFAULT_ELLIPSOIDAL_CS}
     */
    public static GeographicCRS createGeographicCRS(String name, GeodeticDatum datum) {
        return createGeographicCRS(name, datum, DEFAULT_ELLIPSOIDAL_CS);
    }

    /**
     * Build a {@link GeographicCRS} given the name to be assigned, the {@link GeodeticDatum} to be
     * used and the {@link EllipsoidalCS}.
     */
    public static GeographicCRS createGeographicCRS(
            String name, GeodeticDatum datum, EllipsoidalCS ellipsoidalCS) {
        final Map<String, String> props = new HashMap<String, String>();
        props.put(NAME, name);
        return new DefaultGeographicCRS(props, datum, ellipsoidalCS);
    }

    public static ProjectedCRS createProjectedCRS(
            Map<String, ?> props,
            GeographicCRS baseCRS,
            DefiningConversion conversionFromBase,
            MathTransform transform) {
        return new DefaultProjectedCRS(
                props, conversionFromBase, baseCRS, transform, DefaultCartesianCS.PROJECTED);
    }

    /**
     * Build a {@link ProjectedCRS} given the base {@link GeographicCRS}, the {@link
     * DefiningConversion} instance from Base as well as the {@link MathTransform} from the base CRS
     * to returned CRS. The derivedCS is {@link DefaultCartesianCS#PROJECTED} by default.
     */
    public static ProjectedCRS createProjectedCRS(
            Map<String, ?> props,
            GeographicCRS baseCRS,
            DefiningConversion conversionFromBase,
            MathTransform transform,
            CartesianCS derivedCS) {
        // Create the projected CRS
        return new DefaultProjectedCRS(props, conversionFromBase, baseCRS, transform, derivedCS);
    }

    /**
     * Build a custom {@link Ellipsoid} provided the name and a Map contains <key,number> parameters
     * describing that ellipsoid. Supported params are {@link #SEMI_MAJOR}, {@link #SEMI_MINOR},
     * {@link NetCDFUtilities#INVERSE_FLATTENING}
     */
    public static Ellipsoid createEllipsoid(String name, Map<String, Number> ellipsoidParams) {
        Number semiMajor = NetCDFUtilities.DEFAULT_EARTH_RADIUS;
        Number semiMinor = null;
        Number inverseFlattening = Double.NEGATIVE_INFINITY;
        if (ellipsoidParams != null && !ellipsoidParams.isEmpty()) {
            if (ellipsoidParams.containsKey(NetCDFUtilities.SEMI_MAJOR)) {
                semiMajor = ellipsoidParams.get(NetCDFUtilities.SEMI_MAJOR);
            }
            if (ellipsoidParams.containsKey(NetCDFUtilities.SEMI_MINOR)) {
                semiMinor = ellipsoidParams.get(NetCDFUtilities.SEMI_MINOR);
            }
            if (ellipsoidParams.containsKey(NetCDFUtilities.INVERSE_FLATTENING)) {
                inverseFlattening = ellipsoidParams.get(NetCDFUtilities.INVERSE_FLATTENING);
            }
        }
        if (semiMinor != null) {
            return DefaultEllipsoid.createEllipsoid(
                    name, semiMajor.doubleValue(), semiMinor.doubleValue(), SI.METRE);
        } else {
            return DefaultEllipsoid.createFlattenedSphere(
                    name, semiMajor.doubleValue(), inverseFlattening.doubleValue(), SI.METRE);
        }
    }

    /**
     * Build a Projected {@link CoordinateReferenceSystem} parsing Conversion parameters and
     * Ellipsoid
     */
    public static CoordinateReferenceSystem buildCRS(
            Map<String, ?> props, ParameterValueGroup parameters, Ellipsoid ellipsoid)
            throws NoSuchIdentifierException, FactoryException {
        // Refine the parameters by adding the required ellipsoid's related params
        updateEllipsoidParams(parameters, ellipsoid);

        // Datum
        final GeodeticDatum datum = createGeodeticDatum(DEFAULT_DATUM_NAME, ellipsoid);

        // Base Geographic CRS
        GeographicCRS baseCRS = createGeographicCRS(NetCDFUtilities.UNKNOWN, datum);

        // create math transform
        String name = NetCDFUtilities.UNKNOWN;
        Unit unit = getUnit(props);
        if (props != null && !props.isEmpty() && props.containsKey(NetCDFUtilities.NAME)) {
            name = (String) props.get(NetCDFUtilities.NAME);
        }
        DefiningConversion conversionFromBase = new DefiningConversion(name, parameters);
        DefaultCartesianCS derivedCS = createCartesianCS(name, unit);

        MathTransform transform = mtFactory.createBaseToDerived(baseCRS, parameters, derivedCS);
        OperationMethod method = conversionFromBase.getMethod();
        if (!(method instanceof MathTransformProvider)) {
            OperationMethod opMethod = mtFactory.getLastMethodUsed();
            if (opMethod instanceof MathTransformProvider) {
                final Map<String, Object> copy = new HashMap<String, Object>(props);
                copy.put(
                        DefaultProjectedCRS.CONVERSION_TYPE_KEY,
                        ((MathTransformProvider) opMethod).getOperationType());
                props = copy;
            }
        }

        return ProjectionBuilder.createProjectedCRS(
                props, baseCRS, conversionFromBase, transform, derivedCS);
    }

    private static DefaultCartesianCS createCartesianCS(String name, Unit unit) {
        return new DefaultCartesianCS(
                name,
                new DefaultCoordinateSystemAxis(
                        Vocabulary.formatInternational(VocabularyKeys.EASTING),
                        "E",
                        AxisDirection.EAST,
                        unit),
                new DefaultCoordinateSystemAxis(
                        Vocabulary.formatInternational(VocabularyKeys.NORTHING),
                        "N",
                        AxisDirection.NORTH,
                        unit));
    }

    private static Unit getUnit(Map<String, ?> props) {
        Unit unit = SI.METRE;
        if (props != null && !props.isEmpty() && props.containsKey(AXIS_UNIT)) {
            String axisUnit = (String) props.remove(AXIS_UNIT);
            try {
                unit = Units.parseUnit(axisUnit);
            } catch (ParserException | UnsupportedOperationException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "Unabe to parse the specified axis unit: "
                                    + axisUnit
                                    + "Falling back on \"m (meter)\" as default for this projection's "
                                    + "coordinate axis unit");
                }
            }
        }
        return unit;
    }

    public static MathTransform createTransform(ParameterValueGroup parameters)
            throws NoSuchIdentifierException, FactoryException {
        return mtFactory.createParameterizedTransform(parameters);
    }

    /** Get a {@link ParameterValueGroup} parameters instance for the specified projectionName. */
    public static ParameterValueGroup getDefaultparameters(String projectionName)
            throws NoSuchIdentifierException {
        Utilities.ensureNonNull("projectionName", projectionName);
        return mtFactory.getDefaultParameters(projectionName);
    }
}
