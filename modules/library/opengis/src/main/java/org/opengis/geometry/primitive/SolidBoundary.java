/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.primitive;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The boundary of {@linkplain Solid solids}. Solid boundaries are similar to
 * {@linkplain SurfaceBoundary surface boundaries}. In normal 3-dimensional Euclidean
 * space, one {@linkplain Shell shell} is distinguished as the exterior. In the more
 * general case, this is not always possible.
 *
 * <blockquote><font size=2>
 * <strong>NOTE:</strong> An alternative use of solids with no external shell would be to define
 * "complements" of finite solids. These infinite solids would have only interior boundaries. If
 * this specification is extended to 4D Euclidean space, or if 3D compact manifolds are used
 * (probably not in geographic information), then other examples of bounded solids without exterior
 * boundaries are possible.
 * </font></blockquote>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/primitive/SolidBoundary.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see SurfaceBoundary
 */
@UML(identifier="GM_SolidBoundary", specification=ISO_19107)
public interface SolidBoundary extends PrimitiveBoundary {
    /**
     * Returns the exterior shell, or {@code null} if none.
     *
     * @return The exterior shell, or {@code null}.
     */
    @UML(identifier="exterior", obligation=MANDATORY, specification=ISO_19107)
    Shell getExterior();

    /**
     * Returns the interior shells.
     *
     * @return The interior shells. Never {@code null}, but may be an empty array.
     */
    @UML(identifier="interior", obligation=MANDATORY, specification=ISO_19107)
    Shell[] getInteriors();
}
