/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.UML;

/**
 * A {@linkplain GriddedSurface gridded surface} that uses line strings as the horizontal and
 * vertical curves. This is not a polygonal surface, since each of the grid squares is a ruled
 * surface, and not necessarily planar.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "GM_BilinearGrid", specification = ISO_19107)
public interface BilinearGrid extends GriddedSurface {}
