/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.datum;

/**
 * A textual description and/or a set of parameters identifying a particular reference level surface
 * used as a zero-height surface. The description includes its position with respect to the Earth
 * for any of the height types recognized by this standard. There are several types of Vertical
 * Datums, and each may place constraints on the {@linkplain
 * org.geotools.api.referencing.cs.CoordinateSystemAxis Coordinate Axis} with which it is combined
 * to create a {@linkplain org.geotools.api.referencing.crs.VerticalCRS Vertical CRS}.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface VerticalDatum extends Datum {
    /**
     * The type of this vertical datum. Default is "geoidal".
     *
     * @return The type of this vertical datum.
     */
    VerticalDatumType getVerticalDatumType();
}
