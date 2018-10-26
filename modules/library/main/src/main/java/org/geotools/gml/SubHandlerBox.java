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
package org.geotools.gml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

/**
 * Creates a simple OGC box.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class SubHandlerBox extends SubHandler {
    /** The logger for the GML module */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SubHandlerBox.class);

    /** */
    org.locationtech.jts.geom.Envelope e = new org.locationtech.jts.geom.Envelope();

    /** Creates a new instance of GMLBoxHandler. */
    public SubHandlerBox() {
        LOGGER.entering("SubHandlerBox", "new");
        LOGGER.exiting("SubHandlerBox", "new");
    }

    /**
     * Sets a corner.
     *
     * @param c the coordinate of the corner.
     */
    public void addCoordinate(Coordinate c) {
        LOGGER.entering("SubHandlerBox", "addCoordinate", c);
        e.expandToInclude(c);
        LOGGER.exiting("SubHandlerBox", "addCoordinate");
    }

    /**
     * Sets a corner.
     *
     * @param message The geometry to inspect.
     * @return Flag for a complete geometry.
     */
    public boolean isComplete(String message) {
        LOGGER.entering("SubHandlerBox", "isComplete", message);
        LOGGER.exiting("SubHandlerBox", "isComplete", Boolean.TRUE);

        return true;
    }

    /**
     * Builds and returns the polygon.
     *
     * @param geometryFactory the geometryFactory to be used to build the polygon.
     * @return the polygon.
     */
    public org.locationtech.jts.geom.Geometry create(
            org.locationtech.jts.geom.GeometryFactory geometryFactory) {
        LOGGER.entering("SubHandlerBox", "create", geometryFactory);

        Coordinate[] c = new Coordinate[5];
        c[0] = new Coordinate(e.getMinX(), e.getMinY());
        c[1] = new Coordinate(e.getMinX(), e.getMaxY());
        c[2] = new Coordinate(e.getMaxX(), e.getMaxY());
        c[3] = new Coordinate(e.getMaxX(), e.getMinY());
        c[4] = new Coordinate(e.getMinX(), e.getMinY());

        org.locationtech.jts.geom.LinearRing r = null;

        try {
            r = geometryFactory.createLinearRing(c);
        } catch (org.locationtech.jts.geom.TopologyException e) {
            LOGGER.log(Level.FINE, "Topology Exception in GMLBoxHandler", e);

            return null; // could this be handled better?
        }

        Polygon polygon = geometryFactory.createPolygon(r, null);
        LOGGER.exiting("SubHandlerBox", "create", polygon);
        polygon.setUserData(getSRS());
        polygon.setSRID(getSRID());

        return polygon;
    }
}
