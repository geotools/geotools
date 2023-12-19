/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.spatial;

import java.util.logging.Logger;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.data.util.DistanceBufferUtil;
import org.geotools.filter.CartesianDistanceFilter;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.distance.DistanceOp;

public class DWithinImpl extends CartesianDistanceFilter implements DWithin {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DWithinImpl.class);

    private static final ThreadLocal<Pair<Integer, GeodeticCalculator>> CALCULATORS =
            new ThreadLocal<>();

    private double distanceInMeters;

    public DWithinImpl(Expression e1, Expression e2) {
        super(e1, e2);
    }

    public DWithinImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2, matchAction);
    }

    @Override
    public void setDistance(double distance) {
        super.setDistance(distance);
        setDistanceInMeters();
    }

    @Override
    public void setUnits(String units) {
        super.setUnits(units);
        setDistanceInMeters();
    }

    private void setDistanceInMeters() {
        distanceInMeters = DistanceBufferUtil.getDistanceInMeters(this);
    }

    @Override
    public boolean evaluateInternal(Geometry left, Geometry right) {
        if (left.getSRID() != 0 && left.getSRID() == right.getSRID()) {
            try {
                DistanceOp op = new DistanceOp(left, right);
                Coordinate[] points = op.nearestPoints();
                GeodeticCalculator calculator = getCalculator(left);
                // we need to use positions to ensure that any CRS transform gets applied
                calculator.setStartingPosition(new Position2D(points[0].getX(), points[0].getY()));
                calculator.setDestinationPosition(
                        new Position2D(points[1].getX(), points[1].getY()));
                return calculator.getOrthodromicDistance() <= distanceInMeters;
            } catch (Exception e) {
                LOGGER.warning(
                        "Error calculating distance in meters, falling back to native units: " + e);
            }
        }

        return left.isWithinDistance(right, getDistance());
    }

    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public static Envelope buffer(Geometry geom, double distance, String units) {
        Envelope env = geom.getEnvelopeInternal();
        if (geom.getSRID() == 0) {
            env.expandBy(distance);
        } else {
            try {
                double distanceInMeters = DistanceBufferUtil.getDistanceInMeters(distance, units);
                double deltaX = 0, deltaY = 0;
                GeodeticCalculator calculator = getCalculator(geom);
                calculator.setStartingPosition(new Position2D(env.getMaxX(), env.getMaxY()));
                calculator.setDirection(0, distanceInMeters);
                Position north = calculator.getDestinationPosition();
                deltaX += (env.getMaxX() - north.getOrdinate(0));
                deltaY += (env.getMaxY() - north.getOrdinate(1));
                calculator.setDirection(90, distanceInMeters);
                Position east = calculator.getDestinationPosition();
                deltaX += (env.getMaxX() - east.getOrdinate(0));
                deltaY += (env.getMaxY() - east.getOrdinate(1));
                env.expandBy(deltaX, deltaY);
            } catch (Exception e) {
                LOGGER.warning(
                        "Error calculating distance in meters, falling back to native units: " + e);
                env.expandBy(distance);
            }
        }
        return env;
    }

    private static GeodeticCalculator getCalculator(Geometry geom) throws FactoryException {
        Pair<Integer, GeodeticCalculator> cached = CALCULATORS.get();
        if (cached == null || cached.getLeft() != geom.getSRID()) {
            GeodeticCalculator calc;
            if (geom.getSRID() == 4326) {
                calc = new GeodeticCalculator();
            } else {
                // using this constructor causes a transform step from native CRS into 4326
                calc = new GeodeticCalculator(CRS.decode("EPSG:" + geom.getSRID()));
            }
            cached = new ImmutablePair<>(geom.getSRID(), calc);
            CALCULATORS.set(cached);
        }
        return cached.getRight();
    }
}
