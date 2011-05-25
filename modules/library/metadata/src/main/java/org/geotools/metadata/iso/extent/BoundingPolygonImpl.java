/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.extent;

import java.util.Collection;
import org.opengis.metadata.extent.BoundingPolygon;
import org.opengis.geometry.Geometry;


/**
 * Boundary enclosing the dataset, expressed as the closed set of
 * (<var>x</var>,<var>y</var>) coordinates of the polygon. The last
 * point replicates first point.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class BoundingPolygonImpl extends GeographicExtentImpl implements BoundingPolygon {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8174011874910887918L;

    /**
     * The sets of points defining the bounding polygon.
     */
    private Collection<Geometry> polygons;

    /**
     * Constructs an initially empty bounding polygon.
     */
    public BoundingPolygonImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public BoundingPolygonImpl(final BoundingPolygon source) {
        super(source);
    }

    /**
     * Creates a bounding polygon initialized to the specified value.
     */
    public BoundingPolygonImpl(final Collection<Geometry> polygons) {
        setPolygons(polygons);
    }

    /**
     * Returns the sets of points defining the bounding polygon.
     */
    // No class is currently implementing {@linkplain org.opengis.geometry.Geometry}.
    public synchronized Collection<Geometry> getPolygons() {
        return polygons = nonNullCollection(polygons, Geometry.class);
    }

    /**
     * Set the sets of points defining the bounding polygon.
     */
    public synchronized void setPolygons(final Collection<? extends Geometry> newValues) {
        polygons = copyCollection(newValues, polygons, Geometry.class);
    }
}
