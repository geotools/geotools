/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.cs;

import org.geotools.api.referencing.IdentifiedObject;

/**
 * The set of coordinate system axes that spans a given coordinate space. A coordinate system (CS) is derived from a set
 * of (mathematical) rules for specifying how coordinates in a given space are to be assigned to points. The coordinate
 * values in a coordinate tuple shall be recorded in the order in which the coordinate system axes associations are
 * recorded, whenever those coordinates use a coordinate reference system that uses this coordinate system.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see org.geotools.api.referencing.cs.CoordinateSystemAxis
 * @see javax.measure.Unit
 * @see org.geotools.api.referencing.datum.Datum
 * @see org.geotools.api.referencing.crs.CoordinateReferenceSystem
 */
public interface CoordinateSystem extends IdentifiedObject {
    /**
     * Returns the dimension of the coordinate system.
     *
     * @return The dimension of the coordinate system.
     */
    int getDimension();

    /**
     * Returns the axis for this coordinate system at the specified dimension. Each coordinate system must have at least
     * one axis.
     *
     * @param dimension The zero based index of axis.
     * @return The axis at the specified dimension.
     * @throws IndexOutOfBoundsException if {@code dimension} is out of bounds.
     */
    CoordinateSystemAxis getAxis(int dimension) throws IndexOutOfBoundsException;
}
