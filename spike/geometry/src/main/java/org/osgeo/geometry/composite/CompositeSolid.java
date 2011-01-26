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

import org.osgeo.geometry.primitive.Solid;

/**
 * <code>CompositeSolid</code> is a geometry type with the same geometric properties as the (primitive) {@link Solid}
 * type. Essentially, it is a collection of solids that join in pairs on common boundary surfaces and which, when
 * considered as a whole, form a single solid.
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 *
 * @version. $Revision$, $Date$
 */
public interface CompositeSolid extends Solid, List<Solid> {

    /**
     * Must always return {@link Solid.SolidType#CompositeSolid}.
     *
     * @return {@link Solid.SolidType#CompositeSolid}
     */
    public SolidType getSolidType();
}
