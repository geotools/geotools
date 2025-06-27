/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static java.lang.Math.atan2;
import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.SimplifiableFunction;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CompoundCRS;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.EngineeringCRS;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.CylindricalEqualArea;
import org.geotools.referencing.operation.projection.EquidistantCylindrical;
import org.geotools.referencing.operation.projection.Mercator;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Point;

/**
 * Function that allows correcting a heading angle based on the north direction in the point and CRS provided. Useful to
 * perform mapping with vectors (wind, currents) on CRSs that do not typically have the north up (e.g. Polar
 * Stereographic).
 */
public class NorthFix extends FunctionExpressionImpl implements SimplifiableFunction {

    static final Logger LOGGER = Logging.getLogger(NorthFix.class);

    public static final String FUNCTION_NAME = "northFix";

    public static final org.geotools.api.parameter.Parameter<Double> RESULT = parameter(
            "angle",
            Double.class,
            "North fix",
            "Returns the fixed angle, given the current CRS and position (in case the angle is missing, the result is the offset that needs to be added to the angle.");

    public static final org.geotools.api.parameter.Parameter<CoordinateReferenceSystem> TARGET_CRS = parameter(
            "targetCRS",
            CoordinateReferenceSystem.class,
            "Target coordinate reference system",
            "The target coordinate reference system used to paint the map");

    public static final org.geotools.api.parameter.Parameter<Point> POINT =
            parameter("point", Point.class, "point", "The point at which to perform the calculation");
    public static final Parameter<Double> ANGLE = new Parameter<>(
            "angle",
            Double.class,
            new SimpleInternationalString("angle"),
            new SimpleInternationalString("The angle to be fixed (optional, defaults to 0)"),
            false,
            0,
            1,
            null,
            null);

    public static final org.geotools.api.parameter.Parameter<CoordinateReferenceSystem> SOURCE_CRS = new Parameter<>(
            "sourceCRS",
            CoordinateReferenceSystem.class,
            new SimpleInternationalString("Source Coordinate Reference System"),
            new SimpleInternationalString(
                    "The CRS of the provided point. Optional, the function will look up the CRS of the point, and if the source is a simple feature, use its CRS instead. If none of the above is available, then the target and source CRS are assumed to be the same"),
            false,
            0,
            1,
            null,
            null);
    public static final int FULL_CIRCLE = 360;

    public static FunctionName NAME = new FunctionNameImpl(FUNCTION_NAME, RESULT, TARGET_CRS, POINT, ANGLE, SOURCE_CRS);

    public NorthFix() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        // hopefully the input is already a CRS
        CoordinateReferenceSystem targetCRS = getExpression(0).evaluate(feature, CoordinateReferenceSystem.class);
        // check if any fix is required, otherwise return the angle as is
        double angle = 0d;
        if (getParameters().size() >= 3) {
            // optional, defaults to zero
            angle = getExpression(2).evaluate(feature, Double.class);
        }
        if (!fixRequired(targetCRS)) return angle;

        // compute the correction angle
        Point point = getExpression(1).evaluate(feature, Point.class);
        if (point == null) return angle;
        CoordinateReferenceSystem sourceCRS = getSourceCRS(feature, point);
        try {
            double x = point.getX();
            double y = point.getY();
            double[] pt = {x, y};
            // we have a point in source CRS, move it to target CRS
            if (sourceCRS != null && !CRS.equalsIgnoreMetadata(sourceCRS, targetCRS)) {
                // go to target CRS
                MathTransform step1 = CRS.findMathTransform(sourceCRS, targetCRS, true);
                step1.transform(pt, 0, pt, 0, 1);
                // x and y updated in the target CRS
                x = pt[0];
                y = pt[1];
            }
            // go to WGS84, safe way to compute a second point north of the original one
            MathTransform step2 = CRS.findMathTransform(targetCRS, DefaultGeographicCRS.WGS84, true);
            step2.transform(pt, 0, pt, 0, 1);
            // move north and then back to target CRS
            pt[1] += 1e-2;
            // go back to target CRS
            step2.inverse().transform(pt, 0, pt, 0, 1);

            // atan2 used to compute the angle between the two points, taking one as the origin
            // see
            // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/atan2
            double northDirection = Angle.toDegrees(atan2(pt[1] - y, pt[0] - x));
            // compute correction angle considering north is normally at 90°, and normalize
            return normalizeAngle(angle - northDirection + 90);
        } catch (TransformException | FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    /** Normalize an angle to be between 0 and 360 */
    private double normalizeAngle(double angle) {
        return (angle % FULL_CIRCLE + FULL_CIRCLE) % FULL_CIRCLE;
    }

    private CoordinateReferenceSystem getSourceCRS(Object feature, Point point) {
        CoordinateReferenceSystem sorceCRS = null;
        if (getParameters().size() == 4) {
            // optional, defaults to zero
            sorceCRS = getExpression(3).evaluate(feature, CoordinateReferenceSystem.class);
        }
        if (sorceCRS == null && point.getUserData() instanceof CoordinateReferenceSystem) {
            sorceCRS = (CoordinateReferenceSystem) point.getUserData();
        }
        // Heuristic here... the feature CRS is more likely to be correct than the geometry srid,
        // which cannot carry an authority to begin with
        if (feature instanceof Feature) {
            sorceCRS = ((Feature) feature).getType().getCoordinateReferenceSystem();
        }
        if (sorceCRS == null && point.getSRID() > 0) {
            try {
                // assume it's an EPSG code (not always true, but we have no way to know the
                // authority)
                sorceCRS = CRS.decode("EPSG:" + point.getSRID(), true);
            } catch (FactoryException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Could not decode EPSG:" + point.getSRID(), e);
                }
            }
        }
        return sorceCRS;
    }

    static boolean fixRequired(CoordinateReferenceSystem crs) {
        // try to go for quick exit cases
        if (crs == null) {
            return false;
        }
        // flatten if necessary, we only care about the horizontal part
        if (crs instanceof CompoundCRS) {
            crs = org.geotools.referencing.CRS.getHorizontalCRS(crs);
        }
        // geographic and engineering CRSs are always north up
        if (crs instanceof GeographicCRS || crs instanceof EngineeringCRS) {
            return false;
        }

        if (crs instanceof ProjectedCRS) {
            ProjectedCRS projected = (ProjectedCRS) crs;
            MathTransform mt = projected.getConversionFromBase().getMathTransform();
            if (isNorthUpProjection(mt)) return false;
        }

        return true;
    }

    /**
     * Checks for the known north up projections implemented by GeoTools, see also a visual reference at <a
     * href="https://en.wikipedia.org/wiki/List_of_map_projections">Wikipedia</a>. If a north-up projection is not
     * listed here, that will result in a slower execution, but the result will be the same.
     */
    private static boolean isNorthUpProjection(MathTransform mt) {
        return mt instanceof Mercator || mt instanceof CylindricalEqualArea || mt instanceof EquidistantCylindrical;
    }

    @Override
    public Expression simplify(FilterFactory ff, FeatureType featureType) {
        List<Expression> parameters = new ArrayList<>(getParameters());

        // if target CRS is a literal it's possible to do useful optimizations
        if (parameters.get(0) instanceof Literal) {
            CoordinateReferenceSystem crs = parameters.get(0).evaluate(null, CoordinateReferenceSystem.class);

            if (!fixRequired(crs)) {
                // no fix required, remove the function, just return the angle as-is
                if (parameters.size() >= 3) return parameters.get(2);
                else return ff.literal(0d);
            } else {
                // otherwise, at least make sure the CRS needs no more conversions
                parameters.set(0, ff.literal(crs));
            }
        }

        // if the source CRS is a literal, we can try to pre-convert
        if (parameters.size() == 4) {
            if (parameters.get(3) instanceof Literal) {
                CoordinateReferenceSystem crs = parameters.get(3).evaluate(null, CoordinateReferenceSystem.class);
                if (crs != null) {
                    parameters.set(3, ff.literal(crs));
                }
            }
        } else if (featureType != null && parameters.get(2) instanceof PropertyName) {
            // otherwise, see if we can lookup the source CRS by looking up a geometry descriptor
            PropertyDescriptor pd = parameters.get(1).evaluate(featureType, PropertyDescriptor.class);
            if (pd instanceof GeometryDescriptor) {
                GeometryDescriptor gd = (GeometryDescriptor) pd;
                CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
                // found it, force it to be an explicit literal to avoid further lookups
                if (crs != null) {
                    parameters.add(3, ff.literal(crs));
                }
            }
        }

        NorthFix simplified = new NorthFix();
        simplified.setParameters(parameters);
        return simplified;
    }
}
