/*
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.composite;

import java.util.List;

import org.osgeo.geometry.Geometry;
import org.osgeo.geometry.primitive.GeometricPrimitive;

/**
 * A <code>CompositeGeometry</code> is a geometric complex with underlying core geometries that are (as a whole)
 * isomorphic to a geometry primitive. E.g., a composite curve is a collection of curves whose geometry interface could
 * be satisfied by a single curve (albeit a much more complex one). Composites are intended for use as attribute values
 * in datasets in which the underlying geometry has been decomposed, usually to expose its topological nature.
 * 
 * @param <T>
 *            type of the composited geometries
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface CompositeGeometry<T extends GeometricPrimitive> extends Geometry, List<T> {

    /**
     * Returns {@link Geometry.GeometryType#COMPOSITE_GEOMETRY}.
     * 
     * @return {@link Geometry.GeometryType#COMPOSITE_GEOMETRY}
     */
    public GeometryType getGeometryType();
}
