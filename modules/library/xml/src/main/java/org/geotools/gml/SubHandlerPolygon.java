/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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

// Java Topology Suite dependencies

import java.util.ArrayList;
import java.util.logging.Logger;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

/**
 * Creates a Polygon geometry.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class SubHandlerPolygon extends SubHandler {
    /** The logger for the GML module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SubHandlerPolygon.class);

    /** Factory for creating the Polygon geometry. */
    private GeometryFactory geometryFactory = new GeometryFactory();

    /** Handler for the LinearRings that comprise the Polygon. */
    private SubHandlerLinearRing currentHandler = new SubHandlerLinearRing();

    /** Stores Polygon's outer boundary (shell). */
    private LinearRing outerBoundary = null;

    /** Stores Polygon's inner boundaries (holes). */
    private ArrayList innerBoundaries = new ArrayList();

    /** Remembers the current location in the parsing stream (inner or outer boundary). */
    private int location = 0;

    /** Indicates that we are inside the inner boundary of the Polygon. */
    private int INNER_BOUNDARY = 1;

    /** Indicates that we are inside the outer boundary of the Polygon. */
    private int OUTER_BOUNDARY = 2;

    /** Creates a new instance of GMLPolygonHandler. */
    public SubHandlerPolygon() {}

    /**
     * Catches inner and outer LinearRings messages and handles them appropriately.
     *
     * @param message Name of sub geometry located.
     * @param type Type of sub geometry located.
     */
    public void subGeometry(String message, int type) {
        // if we have found a linear ring, either
        // add it to the list of inner boundaries if we are reading them
        // and at the end of the LinearRing
        // add it to the outer boundary if we are reading it and at the end of
        // the LinearRing
        // create a new linear ring, if we are at the start of a new linear ring
        if (message.equals("LinearRing")) {
            if (type == GEOMETRY_END) {
                if (location == INNER_BOUNDARY) {
                    innerBoundaries.add((LinearRing) currentHandler.create(geometryFactory));
                } else if (location == OUTER_BOUNDARY) {
                    outerBoundary = (LinearRing) currentHandler.create(geometryFactory);
                }
            } else if (type == GEOMETRY_START) {
                currentHandler = new SubHandlerLinearRing();
            }
        } else if (message.equals("outerBoundaryIs")) {
            //  or, if we are getting notice of an inner/outer boundary marker,
            // set current location appropriately
            LOGGER.finer("new outer Boundary");
            location = OUTER_BOUNDARY;
        } else if (message.equals("innerBoundaryIs")) {
            LOGGER.finer("new InnerBoundary");
            location = INNER_BOUNDARY;
        }
    }

    /**
     * Adds a coordinate to the current LinearRing.
     *
     * @param coordinate Name of sub geometry located.
     */
    public void addCoordinate(Coordinate coordinate) {
        currentHandler.addCoordinate(coordinate);
    }

    /**
     * Determines whether or not the geometry is ready to be returned.
     *
     * @param message Name of GML element that prompted this check.
     * @return Flag indicating whether or not the geometry is ready to be returned.
     */
    public boolean isComplete(String message) {
        // the conditions checked here are that the endGeometry message that
        // prompted this check is a Polygon and that this Polygon has an outer
        // boundary; if true, then return the all go signal
        if (message.equals("Polygon")) {
            if (outerBoundary != null) {
                return true;
            } else {
                return false;
            }
        }
        // otherwise, send this message to the subGeometry method for further
        // processing
        else {
            //            this.subGeometry(message, GEOMETRY_END);

            return false;
        }
    }

    /**
     * Returns the completed OGC Polygon.
     *
     * @param geometryFactory Geometry factory to be used in Polygon creation.
     * @return Completed OGC Polygon.
     */
    public Geometry create(GeometryFactory geometryFactory) {
        for (int i = 0; i < innerBoundaries.size(); i++) {
            LinearRing hole = (LinearRing) innerBoundaries.get(i);
            if (hole.crosses(outerBoundary)) {
                LOGGER.warning("Topology Error building polygon");

                return null;
            }
        }

        LinearRing[] rings =
                (LinearRing[]) innerBoundaries.toArray(new LinearRing[innerBoundaries.size()]);
        Polygon polygon = geometryFactory.createPolygon(outerBoundary, rings);
        polygon.setUserData(getSRS());
        polygon.setSRID(getSRID());
        return polygon;
    }
}
