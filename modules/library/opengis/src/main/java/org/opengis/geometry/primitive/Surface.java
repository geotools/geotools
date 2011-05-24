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

import java.util.List;
import org.opengis.geometry.coordinate.GenericSurface;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Surface with a positive orientation.
 * {@code Surface} is a subclass of {@link Primitive} and is the basis for 2-dimensional
 * geometry. Unorientable surfaces such as the M&ouml;bius band are not allowed. The orientation of
 * a surface chooses an "up" direction through the choice of the upward normal, which, if the
 * surface is not a cycle, is the side of the surface from which the exterior boundary appears
 * counterclockwise. Reversal of the surface orientation reverses the curve orientation of each
 * boundary component, and interchanges the conceptual "up" and "down" direction of the surface.
 * If the surface is the boundary of a solid, the "up" direction is usually outward. For closed
 * surfaces, which have no boundary, the up direction is that of the surface patches, which must
 * be consistent with one another. Its included {@linkplain SurfacePatch surface patches} describe
 * the interior structure of a {@code Surface}.
 *
 * <blockquote><font size=2>
 * <strong>NOTE:</strong> Other than the restriction on orientability, no other "validity" condition is required for GM_Surface.
 * </font></blockquote>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/primitive/Surface.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see PrimitiveFactory#createSurface(List)
 * @see PrimitiveFactory#createSurface(SurfaceBoundary)
 */
@UML(identifier="GM_Surface", specification=ISO_19107)
public interface Surface extends OrientableSurface, GenericSurface {
    /**
     * Relates this {@code Surface} to a set of {@linkplain SurfacePatch surface patches} that
     * shall be joined together to form this surface. Depending on the interpolation method, the set
     * of patches may require significant additional structure.
     *
     * If the surface {@linkplain #getCoordinateDimension coordinate dimension} is 2, then the
     * entire {@code Surface} is one logical patch defined by linear interpolation from the
     * boundary.
     *
     * @return The list of surface patches. Should never be {@code null} neither empty.
     *
     * @see SurfacePatch#getSurface
     * @see Curve#getSegments
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Segmentation")
    @UML(identifier="patch", obligation=MANDATORY, specification=ISO_19107)
    List<? extends SurfacePatch> getPatches();

    /**
     * Returns the orientable surfaces associated with this surface.
     *
     * @return The orientable surfaces as an array of length 2.
     *
     * @see OrientableSurface#getPrimitive
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Oriented")
    @UML(identifier="proxy", obligation=MANDATORY, specification=ISO_19107)
    OrientableSurface[] getProxy();
}
