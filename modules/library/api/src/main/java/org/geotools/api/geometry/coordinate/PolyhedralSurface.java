/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.geometry.coordinate;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19107;

import java.util.List;
import org.geotools.api.annotation.UML;
import org.geotools.api.geometry.primitive.Surface;

/**
 * A surface composed of {@linkplain Polygon polygon surfaces} connected along their common boundary
 * curves. This differs from {@link Surface} only in the restriction on the types of surface patches
 * acceptable.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 * @see GeometryFactory#createPolyhedralSurface
 */
@UML(identifier = "GM_PolyhedralSurface", specification = ISO_19107)
public interface PolyhedralSurface extends Surface {
    /** Associates this surface with its individual facet polygons. */
    @Override
    @UML(identifier = "patch", obligation = MANDATORY, specification = ISO_19107)
    List<? extends Polygon> getPatches();
}
