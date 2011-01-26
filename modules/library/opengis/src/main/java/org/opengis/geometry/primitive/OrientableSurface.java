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

import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A surface and an orientation inherited from {@link OrientablePrimitive}. If the orientation is
 * positive, then the {@code OrientableSurface} is a {@linkplain Surface surface}. If the
 * orientation is negative, then the {@code OrientableSurface} is a reference to a
 * {@linkplain Surface surface} with an upNormal that reverses the direction for this
 * {@code OrientableSurface}, the sense of "the top of the surface".
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_OrientableSurface", specification=ISO_19107)
public interface OrientableSurface extends OrientablePrimitive {
    /**
     * Returns the set of circular sequences of {@linkplain OrientableCurve orientable curve} that
     * limit the extent of this {@code OrientableSurface}. These curves shall be organized
     * into one circular sequence of curves for each boundary component of the
     * {@code OrientableSurface}. In cases where "exterior" boundary is not
     * well defined, all the rings of the {@linkplain SurfaceBoundary surface boundary}
     * shall be listed as "interior".
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> The concept of exterior boundary for a surface is really only
     * valid in a 2-dimensional plane. A bounded cylinder has two boundary components, neither
     * of which can logically be classified as its exterior. Thus, in 3 dimensions, there is no
     * valid definition of exterior that covers all cases.
     * </font></blockquote>
     *
     * @return The sets of positions on the boundary.
     */
    @UML(identifier="boundary", obligation=MANDATORY, specification=ISO_19107)
    SurfaceBoundary getBoundary();

    /**
     * Returns the primitive associated with this {@code OrientableSurface}.
     *
     * @return The primitive, or {@code null} if the association is
     *         not available or not implemented that way.
     *
     * @see Surface#getProxy
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Oriented")
    @UML(identifier="primitive", obligation=OPTIONAL, specification=ISO_19107)
    Surface getPrimitive();

    /**
     * Returns the owner of this orientable surface. This method is <em>optional</em> since
     * the association in ISO 19107 is navigable only from {@code CompositeSurface} to
     * {@code OrientableSurface}, not the other way.
     *
     * @return The owner of this orientable surface, or {@code null} if the association is
     *         not available or not implemented that way.
     *
     * @see CompositeSurface#getGenerators
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Composition")
    @UML(identifier="composite", obligation=OPTIONAL, specification=ISO_19107)
    CompositeSurface getComposite();
}
