/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.DerivedCRS;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.cs.CoordinateSystemAxis;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import si.uom.SI;

/**
 * A {@link ProjectionHandler} for projections that do warp in the East/West direction, it will replicate the geometries
 * generating a Google Maps like effect
 *
 * @author Andrea Aime - OpenGeo
 */
public class WrappingProjectionHandler extends ProjectionHandler {

    private static final Object LARGE_EARTH_OBJECT = new Object();

    /** The user data key to indicate that the geometry was pre-flipped */
    protected static final String PREFLIPPED_OBJECT = "PRE-FLIPPED";

    /**
     * The tolerance to consider a geometry without touching both datelines. Derived using trial and error. Any smaller
     * and Antarctica-like polygons are incorrectly tagged.
     */
    public static final double DATELINE_PROXIMITY_TOLERANCE = 1.99572;

    private int maxWraps;

    private boolean datelineWrappingCheckEnabled = true;
    public static final String DATELINE_WRAPPING_CHECK_ENABLED = "datelineWrappingCheckEnabled";
    double sourceHalfCircle;

    /** Provides the strategy with the area we want to render and its CRS (the SPI lookup will do this step) */
    public WrappingProjectionHandler(
            ReferencedEnvelope renderingEnvelope,
            ReferencedEnvelope validArea,
            CoordinateReferenceSystem sourceCrs,
            double centralMeridian,
            int maxWraps)
            throws FactoryException {
        super(sourceCrs, validArea, renderingEnvelope);
        this.maxWraps = maxWraps;
        // if we are wrapping, we query across the dateline no matter what
        this.queryAcrossDateline = true;
        // this will compute the target half circle size
        setCentralMeridian(centralMeridian);

        CoordinateSystemAxis axis = sourceCrs.getCoordinateSystem().getAxis(0);
        if (sourceCrs instanceof GeographicCRS
                || sourceCrs instanceof DerivedCRS && axis.getUnit().isCompatible(SI.RADIAN)) {
            sourceHalfCircle = 180;
        } else {
            // assume a simplified earth circumference, which is 40075 km
            @SuppressWarnings("unchecked")
            Unit<Length> sourceUnit = (Unit<Length>) axis.getUnit();
            UnitConverter converter = SI.METRE.getConverterTo(sourceUnit);
            this.sourceHalfCircle = converter.convert(40075000 / 2);
        }
    }

    /**
     * Set one of the supported projection parameters: - datelineWrappingCheckEnabled (boolean) if false disables the
     * heuristic for dateline wrapping check (true by default)
     */
    @Override
    public void setProjectionParameters(Map<String, Object> projectionParameters) {
        super.setProjectionParameters(projectionParameters);
        if (projectionParameters.containsKey(DATELINE_WRAPPING_CHECK_ENABLED)) {
            datelineWrappingCheckEnabled = (Boolean) projectionParameters.get(DATELINE_WRAPPING_CHECK_ENABLED);
        }
    }

    @Override
    public Geometry preProcess(Geometry geometry) throws TransformException, FactoryException {
        geometry = super.preProcess(geometry);
        if (geometry == null) {
            return null;
        }
        // no need to check about size on point layers
        if (geometry instanceof Point) return geometry;

        // if the object was already a large one, clone it and set the user data to indicate it was
        // if it was preflipped, clone it and set the user data to indicate it was
        // hopefully it will happen for few objects
        final double width = getWidth(geometry.getEnvelopeInternal(), sourceAxisOrder);
        if (width > sourceHalfCircle) {
            Geometry copy = geometry.copy();
            if (preflipped(width)) {
                copy.setUserData(PREFLIPPED_OBJECT);
            } else {
                copy.setUserData(LARGE_EARTH_OBJECT);
            }
            return copy;
        }

        return geometry;
    }

    private double getWidth(Envelope envelope, CRS.AxisOrder axisOrder) {
        final boolean northEast = axisOrder == CRS.AxisOrder.NORTH_EAST;
        if (northEast) {
            return envelope.getHeight();
        } else {
            return envelope.getWidth();
        }
    }

    @Override
    public Geometry postProcess(MathTransform mt, Geometry geometry) {
        // Let's check if the geometry is undoubtedly not going to need processing
        Envelope env = geometry.getEnvelopeInternal();
        final double width = getWidth(env, targetAxisOrder);
        final double reWidth = getWidth(renderingEnvelope, targetAxisOrder);

        // if it was large and still larger, or small and still small, it likely did not wrap
        if (width < targetHalfCircle && renderingEnvelope.contains(env) && reWidth <= targetHalfCircle * 2) {
            return geometry;
        }

        // Check if the geometry has wrapped the dateline. Heuristic: we assume
        // anything larger than half of the world might have wrapped it, however,
        // if it's touching both datelines then don't wrap it, as it might be something
        // like antarctica
        final boolean northEast = CRS.getAxisOrder(targetCRS) == CRS.AxisOrder.NORTH_EAST;
        if (datelineWrappingCheckEnabled
                && (geometry.getUserData() == LARGE_EARTH_OBJECT && width < targetHalfCircle
                        || geometry.getUserData() != LARGE_EARTH_OBJECT
                                && width > targetHalfCircle
                                && width < targetHalfCircle * 2
                        || geometry.getUserData() != null
                                && geometry.getUserData().equals(PREFLIPPED_OBJECT))) {
            final Geometry wrapped = geometry.copy();
            wrapped.apply(new WrappingCoordinateFilter(
                    targetHalfCircle,
                    targetHalfCircle * 2,
                    mt,
                    northEast,
                    geometry.getUserData() != null && geometry.getUserData().equals(PREFLIPPED_OBJECT)));
            wrapped.geometryChanged();
            geometry = wrapped;
            env = geometry.getEnvelopeInternal();
        }

        // The viewing area might contain the geometry multiple times due to
        // wrapping.
        // This is obvious for the geometries that wrapped the dateline, but the
        // viewing
        // area might be large enough to contain the same continent multiple
        // times (a-la Google Maps)
        List<Geometry> geoms = new ArrayList<>();
        Class geomType = null;

        // search the west-most location inside the current rendering envelope
        // (there may be many)
        double base, curr, lowLimit, highLimit;
        if (northEast) {
            base = env.getMinY();
            curr = env.getMinY();
            lowLimit = Math.max(
                    renderingEnvelope.getMinY(), renderingEnvelope.getMedian(1) - maxWraps * targetHalfCircle * 2);
            highLimit = Math.min(
                    renderingEnvelope.getMaxY(), renderingEnvelope.getMedian(1) + maxWraps * targetHalfCircle * 2);
        } else {
            base = env.getMinX();
            curr = env.getMinX();
            double geometryWidth = geometry.getEnvelopeInternal().getWidth();
            lowLimit = Math.max(
                    renderingEnvelope.getMinX() - geometryWidth,
                    renderingEnvelope.getMedian(0) - maxWraps * targetHalfCircle * 2);
            highLimit = Math.min(
                    renderingEnvelope.getMaxX() + geometryWidth,
                    renderingEnvelope.getMedian(0) + maxWraps * targetHalfCircle * 2);
        }
        while (curr > lowLimit) {
            curr -= targetHalfCircle * 2;
        }

        // clone and offset as necessary
        geomType = accumulate(geoms, geometry, geomType, renderingEnvelope);
        while (curr <= highLimit) {
            double offset = curr - base;
            if (Math.abs(offset) >= targetHalfCircle) {
                // we make a copy and offset it
                Geometry offseted = geometry.copy();
                offseted.apply(new OffsetOrdinateFilter(northEast ? 1 : 0, offset));
                offseted.geometryChanged();
                geomType = accumulate(geoms, offseted, geomType, renderingEnvelope);
            }

            curr += targetHalfCircle * 2;
        }

        // if we could not find any geom type we stumbled int an empty geom collection
        if (geomType == null) {
            return null;
        }

        // if we did not have to actually clone the geometries
        if (geoms.size() == 1) {
            return geoms.get(0);
        }

        // rewrap all the clones into a single geometry
        if (Point.class.equals(geomType)) {
            Point[] points = geoms.toArray(new Point[geoms.size()]);
            return geometry.getFactory().createMultiPoint(points);
        } else if (LineString.class.isAssignableFrom(geomType)) {
            LineString[] lines = geoms.toArray(new LineString[geoms.size()]);
            return geometry.getFactory().createMultiLineString(lines);
        } else if (Polygon.class.equals(geomType)) {
            Polygon[] polys = geoms.toArray(new Polygon[geoms.size()]);
            return geometry.getFactory().createMultiPolygon(polys);
        } else {
            return geometry.getFactory().createGeometryCollection(geoms.toArray(new Geometry[geoms.size()]));
        }
    }

    /**
     * In some cases the geometry is preflipped due to coordinate order and proximity to the dateline
     *
     * <p>An example is this line: LINESTRING (179.94028987 52.14290407, -179.9428079 52.40938205)
     *
     * @param width width of geometry to check for preflip condition
     * @return is preflipped
     */
    private boolean preflipped(double width) {
        // only applies to geographic coordinates
        if (!(sourceCRS instanceof GeographicCRS)) {
            return false;
        }
        // preflipped width will be very close to twice the source half circle
        // but only applies if it doesn't touch both datelines
        return width > sourceHalfCircle * DATELINE_PROXIMITY_TOLERANCE && width < sourceHalfCircle * 2;
    }

    /**
     * Adds the geometries into the collection by recursively splitting apart geometry collections, so that geoms will
     * contains only simple geometries.
     *
     * @return the geometry type that all geometries added to the collection conform to. Worst case it's going to be
     *     Geometry.class
     */
    static Class accumulate(List<Geometry> geoms, Geometry geometry, Class geomType, ReferencedEnvelope envelope) {
        Class gtype = null;
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            Geometry g = geometry.getGeometryN(i);
            Class lastType = gtype;

            if (g instanceof GeometryCollection) {
                gtype = accumulate(geoms, g, geomType, envelope);
            } else {
                if (envelope.intersects(g.getEnvelopeInternal())) {
                    geoms.add(g);
                    gtype = g.getClass();
                }
            }

            if (gtype == null) {
                gtype = g.getClass();
            } else if (geomType != null && !g.getClass().equals(geomType)
                    || lastType != null && !g.getClass().equals(lastType)) {
                // if we have different types, switch to Geometry type
                gtype = Geometry.class;
            }
        }
        return gtype;
    }

    @Override
    public boolean requiresProcessing(Geometry geometry) {
        return true;
    }

    public boolean isDatelineWrappingCheckEnabled() {
        return datelineWrappingCheckEnabled;
    }

    /**
     * Enables the check using the "half world" heuristic on the input geometries, if larger it assumes they spanned the
     * dateline. Enabled by default
     */
    public void setDatelineWrappingCheckEnabled(boolean datelineWrappingCheckEnabled) {
        this.datelineWrappingCheckEnabled = datelineWrappingCheckEnabled;
    }
}
