/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.oval;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Default implementation of {@code Oval}. */
public class OvalImpl implements Oval {

    private static final GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);
    private final ReferencedEnvelope envelope;

    /**
     * Creates a new oblong.
     *
     * @param minX minimum X ordinate
     * @param minY minimum Y ordinate
     * @param width span in the X direction
     * @param height span in the Y direction
     * @param crs coordinate reference system (may be {@code null})
     */
    public OvalImpl(
            double minX, double minY, double width, double height, CoordinateReferenceSystem crs) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must both be positive");
        }
        envelope = new ReferencedEnvelope(minX, minX + width, minY, minY + height, crs);
    }

    /** {@inheritDoc} */
    public double getArea() {
        return envelope.getArea();
    }

    /** {@inheritDoc} */
    public ReferencedEnvelope getBounds() {
        return new ReferencedEnvelope(envelope);
    }

    /** {@inheritDoc} */
    public Coordinate getCenter() {
        return envelope.centre();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Vertex 0 is at the min X and Y coordinate (lower left) with the subsequent vertices being
     * indexed clockwise.
     */
    public Coordinate[] getVertices() {
        Coordinate[] vertices = new Coordinate[4];
        vertices[0] = new Coordinate(envelope.getMinX(), envelope.getMinY());
        vertices[1] = new Coordinate(envelope.getMinX(), envelope.getMaxY());
        vertices[2] = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
        vertices[3] = new Coordinate(envelope.getMaxX(), envelope.getMinY());

        return vertices;
    }

    /** {@inheritDoc} */
    public Geometry toGeometry() {
        GeometricShapeFactory geometryShapeFactory = new GeometricShapeFactory(geomFactory);
        geometryShapeFactory.setEnvelope(this.envelope);
        geometryShapeFactory.setNumPoints(100);
        Geometry geometry = geometryShapeFactory.createEllipse();
        return geometry;
    }

    /** {@inheritDoc} */
    public Geometry toDenseGeometry(double maxSpacing) {
        return Densifier.densify(this.toGeometry(), maxSpacing);
    }
}
