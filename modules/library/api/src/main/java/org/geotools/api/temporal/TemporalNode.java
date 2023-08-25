/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

/**
 * A zero dimensional topological primitive in time.
 *
 * @author Alexander Petkov
 */
public interface TemporalNode extends TemporalTopologicalPrimitive {
    /** An optional association that may link this temporal node to its corresponding instant. */
    Instant getRealization();

    /** Links this temporal node to the previous temporal edge. */
    TemporalEdge getPreviousEdge();

    /** Links this temporal node to the next temporal edge. */
    TemporalEdge getNextEdge();
}
