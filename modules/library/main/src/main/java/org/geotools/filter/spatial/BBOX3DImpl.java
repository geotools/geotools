/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
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

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX3D;
import org.geotools.api.geometry.BoundingBox3D;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

/**
 * A 3D BBOX Filter Implementation Supports filtering with BBOXes that have 3D coordinates including a minimum and
 * maximum for the z-axis.
 *
 * @author Niels Charlier
 */
public class BBOX3DImpl implements BBOX3D {

    PropertyName property;
    ReferencedEnvelope3D envelope;
    FilterFactory factory;

    public BBOX3DImpl(PropertyName propertyName, ReferencedEnvelope3D env, FilterFactory factory) {
        this.property = propertyName;
        this.envelope = env;
        this.factory = factory;
    }

    public PropertyName getProperty() {
        return property;
    }

    public String getPropertyName() {
        return property.getPropertyName();
    }

    @Override
    public BoundingBox3D getBounds() {
        return envelope;
    }

    @Override
    public Expression getExpression1() {
        return property;
    }

    @Override
    public Expression getExpression2() {
        //        // in this case, the 3D BBOX falls back to regular 2D bbox behaviour (until there
        // is more
        //        // support for 3D geometries)
        //        // 3DBBOX must be run as a post-filter in order to support the third coordinate.
        //
        //        Coordinate[] coords = new Coordinate[5];
        //        coords[0] = new Coordinate(envelope.getMinX(), envelope.getMinY());
        //        coords[1] = new Coordinate(envelope.getMinX(), envelope.getMaxY());
        //        coords[2] = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
        //        coords[3] = new Coordinate(envelope.getMaxX(), envelope.getMinY());
        //        coords[4] = new Coordinate(envelope.getMinX(), envelope.getMinY());
        //
        //        LinearRing ring = null;
        //
        //        GeometryFactory gfac = new GeometryFactory();
        //        try {
        //            ring = gfac.createLinearRing(coords);
        //        } catch (TopologyException tex) {
        //            throw new IllegalFilterException(tex.toString());
        //        }
        //
        //        Polygon polygon = gfac.createPolygon(ring, null);
        //        if (envelope instanceof ReferencedEnvelope3D) {
        //            ReferencedEnvelope3D refEnv = (ReferencedEnvelope3D) envelope;
        //            polygon.setUserData(refEnv.getCoordinateReferenceSystem());
        //        }
        //
        //        return factory.literal(polygon);
        return factory.literal(envelope);
    }

    @Override
    public Object accept(FilterVisitor visitor, Object context) {
        return visitor.visit(this, context);
    }

    public ReferencedEnvelope3D get3DEnvelope(Geometry geom) {
        Coordinate[] coordinates = geom.getCoordinates();

        ReferencedEnvelope3D env = new ReferencedEnvelope3D();
        for (Coordinate coordinate : coordinates) {
            env.expandToInclude(coordinate);
        }
        return env;
    }

    @Override
    public boolean evaluate(Object feature) {

        Geometry other = Converters.convert(property.evaluate(feature), Geometry.class);
        if (other == null) return false;

        return get3DEnvelope(other).intersects(envelope);
    }

    // THIS GARGABE IS HERE TO ALLOW OLD DATASTORES NOT USING PROPER OGC FILTERS TO WORK
    // WILL BE REMOVED WHEN THERE IS NOTHING LEFT USING THEM

    public boolean isMatchingCase() {
        return false;
    }

    public boolean contains(SimpleFeature feature) {
        return evaluate((Object) feature);
    }

    public boolean evaluate(SimpleFeature feature) {
        return evaluate((Object) feature);
    }

    @Override
    public MatchAction getMatchAction() {
        return MatchAction.ANY;
    }

    @Override
    public String toString() {
        return "BBOX3D [property=" + property + ", envelope=" + envelope + "]";
    }
}
