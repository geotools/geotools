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
package org.osgeo.geometry.primitive;

import org.osgeo.geometry.Geometry;

/**
 * A {@link GeometricPrimitive} is a contigous geometry with single dimensionality.
 * <p>
 * For every dimensionality, a specialized interface exists:
 * <ul>
 * <li>0D: {@link Point}</li>
 * <li>1D: {@link Curve}</li>
 * <li>2D: {@link Surface}</li>
 * <li>3D: {@link Solid}</li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 *
 * @version $Revision:$, $Date:$
 */
public interface GeometricPrimitive extends Geometry {

    /**
     * Convenience enum type for discriminating the different primitive variants.
     */
    public enum PrimitiveType {
        /** 0-dimensional primitive */
        Point,
        /** 1-dimensional primitive */
        Curve,
        /** 2-dimensional primitive */
        Surface,
        /** 3-dimensional primitive */
        Solid
    }

    /**
     * Must always return {@link Geometry.GeometryType#PRIMITIVE_GEOMETRY}.
     *
     * @return must always return {@link Geometry.GeometryType#PRIMITIVE_GEOMETRY}
     */
    public GeometryType getGeometryType();

    /**
     * Returns the type of primitive.
     *
     * @return the type of primitive
     */
    public PrimitiveType getPrimitiveType();
}
