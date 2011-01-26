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

import java.util.logging.Logger;


/**
 * Creates the appropriate SubHandler element for a given OGC simple geometry
 * type.
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL$
 * @version $Id$
 */
public class SubHandlerFactory {
    /** The logger for the GML module */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gml");

    /** List of all valid OGC multi geometry types. */
    private static final java.util.Collection BASE_GEOMETRY_TYPES = new java.util.Vector(java.util.Arrays
            .asList(new String[] { "MultiPoint", "MultiLineString", "MultiPolygon" }));

    /**
     * Empty constructor.
     */
    public SubHandlerFactory() {
        LOGGER.entering("SubHandlerFactory", "new");
        LOGGER.exiting("SubHandlerFactory", "new");
    }

    /**
     * Creates a new SubHandler, based on the appropriate OGC simple geometry
     * type.  Note that some types are aggregated into a generic 'multi' type.
     *
     * @param type Type of SubHandler to return.
     *
     * @return DOCUMENT ME!
     *
     * @task TODO: throw an exception, not return a null
     */
    public SubHandler create(String type) {
        LOGGER.entering("SubHandlerFactory", "create", type);

        SubHandler returnValue = null;

        if (type.equals("Point")) {
            returnValue = new SubHandlerPoint();
        } else if (type.equals("LineString")) {
            returnValue = new SubHandlerLineString();
        } else if (type.equals("LinearRing")) {
            returnValue = new SubHandlerLinearRing();
        } else if (type.equals("Polygon")) {
            returnValue = new SubHandlerPolygon();
        } else if (type.equals("Box")) {
            returnValue = new SubHandlerBox();
        } else if (BASE_GEOMETRY_TYPES.contains(type)) {
            returnValue = new SubHandlerMulti();
        } else {
            returnValue = null; // should be throwing an exception here!
        }

        LOGGER.exiting("SubHandlerFactory", "create", returnValue);

        return returnValue;
    }
}
