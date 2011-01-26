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
package org.osgeo.geometry;

import org.osgeo.commons.ObjectProperties;
import org.osgeo.commons.crs.CRS;

/**
 * Base interface for all vector geometry types.
 * <p>
 * Root of the ISO 19107/GML 3.1.1/GML 3.2.1 compliant geometry type hierarchy.
 * </p>
 * <p>
 * <h4>Notes on the representation of GML geometries</h4>
 * The "StandardObjectProperties" defined by GML (e.g. multiple <code>gml:name</code> elements or
 * <code>gml:description</code>) which are inherited by any GML geometry type definition are treated in a specific way.
 * They are modelled using the {@link ObjectProperties} class. This design decision has been driven by the goal to make
 * the implementation less GML (and GML-version) specific and to allow for example to export a {@link Geometry} instance
 * as either GML 3.2.1 or GML 3.1.1 (different namespaces for the standard properties).
 * </p>
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface Geometry {

    /**
     * Convenience enum type for discriminating the different geometry variants.
     */
    public enum GeometryType {
        /** Envelope */
        ENVELOPE,
        /** Primitive geometry */
        PRIMITIVE_GEOMETRY,
        /** Composited geometry */
        COMPOSITE_GEOMETRY,
        /** Multi (aggregate) geometry */
        MULTI_GEOMETRY,
    }

    /**
     * Returns the type of geometry.
     * 
     * @return the type of geometry
     */
    public GeometryType getGeometryType();

    /**
     * Returns the id of the geometry.
     * <p>
     * In a GML representation of the geometry, this corresponds to the <code>gml:id</code> (GML 3 and later) or
     * <code>gid</code> (GML 2) attribute of the geometry element.
     * </p>
     * 
     * @return the id of the geometry, or null if it is an anonymous (unidentified) geometry
     */
    public String getId();

    /**
     * Returns the associated spatial reference system.
     * 
     * @return spatial reference system, may be null
     */
    public CRS getCoordinateSystem();

    /**
     * Returns attached properties (e.g. GML standard properties, such as <code>gml:name</code>).
     * 
     * @return the attached properties, may be null
     */
    public ObjectProperties getAttachedProperties();

    /**
     * Returns the coordinate dimension, i.e. the dimension of the space that the geometry is embedded in.
     * 
     * @return the coordinate dimension
     */
    public int getCoordinateDimension();
}
