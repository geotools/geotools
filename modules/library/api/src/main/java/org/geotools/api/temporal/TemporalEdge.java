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
 * One-dimensional topological primitive in time.
 *
 * @author Alexander Petkov
 */
public interface TemporalEdge extends TemporalTopologicalPrimitive {
    /** An optional association that links this edge to the corresponding period. */
    Period getRealization();

    /** Links this edge to the node that is its start. */
    TemporalNode getStart();

    /** Links this edge to the node that is its end. */
    TemporalNode getEnd();
}
