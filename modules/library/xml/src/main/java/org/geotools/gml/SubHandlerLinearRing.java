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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.TopologyException;

/**
 * Creates a simple OGC LinearRing (a closed LineString).
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class SubHandlerLinearRing extends SubHandler {

    static final Logger LOGGER = Logging.getLogger(SubHandlerLinearRing.class);

    /** Internal coordinate list. */
    private ArrayList coordinateList = new ArrayList();

    /** Creates a new instance of GMLLinearRingHandler. */
    public SubHandlerLinearRing() {}

    /**
     * Adds a coordinate to the LinearRing.
     *
     * @param coordinate The coordinate to add to the LinearRing.
     */
    public void addCoordinate(Coordinate coordinate) {
        coordinateList.add(coordinate);
    }

    /**
     * Determine whether or not this LinearRing is ready to be created.
     *
     * @param message The current geometry type in the GML stream.
     * @return Ready for creation flag.
     */
    public boolean isComplete(String message) {
        // makes sure that this LinearRing has more than one coordinate and its first and last are
        // identical
        if (coordinateList.size() > 1) {
            Coordinate firstCoordinate = (Coordinate) coordinateList.get(0);
            Coordinate lastCoordinate = (Coordinate) coordinateList.get(coordinateList.size() - 1);

            if (lastCoordinate.equals2D(firstCoordinate)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Create the LinearRing.
     *
     * @param geometryFactory The geometry factory used for the build.
     * @return LinearRing geometry created.
     */
    public Geometry create(GeometryFactory geometryFactory) {
        try {
            Coordinate[] coords =
                    (Coordinate[]) coordinateList.toArray(new Coordinate[coordinateList.size()]);
            LinearRing ring = geometryFactory.createLinearRing(coords);
            ring.setUserData(getSRS());
            ring.setSRID(getSRID());
            return ring;
        } catch (TopologyException e) {
            LOGGER.log(Level.FINE, "Caught Topology exception in GMLLinearRingHandler", e);

            return null;
        }
    }
}
