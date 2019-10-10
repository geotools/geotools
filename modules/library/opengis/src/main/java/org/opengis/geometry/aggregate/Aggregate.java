/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $URL$
 **
 ** Copyright (C) 2003-2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.geometry.aggregate;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import java.util.Set;
import org.opengis.annotation.UML;
import org.opengis.geometry.Geometry;

/**
 * Geometry that is an aggregate of other geometries.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @since GeoAPI 1.0
 */
@UML(identifier = "GM_Aggregate", specification = ISO_19107)
public interface Aggregate extends Geometry {
    /**
     * Returns the set containing the elements that compose this aggregate. The set may be modified
     * if this geometry {@linkplain #isMutable is mutable}.
     *
     * @return The set containing the elements that compose this aggregate.
     */
    @UML(identifier = "element", obligation = MANDATORY, specification = ISO_19107)
    Set<? extends Geometry> getElements();
}
