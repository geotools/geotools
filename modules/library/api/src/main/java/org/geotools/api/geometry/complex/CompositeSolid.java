/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.geometry.complex;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19107;

import java.util.Set;
import org.geotools.api.annotation.UML;
import org.geotools.api.geometry.primitive.Solid;

/**
 * A {@linkplain Complex complex} with all the geometric properties of a solid. Essentially, a
 * composite solid is a set of solids that join in pairs on common boundary surfaces to form a
 * single solid.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (Geomatys)
 * @since GeoAPI 2.1
 * @todo This interface extends (indirectly) both {@link
 *     org.geotools.api.geometry.primitive.Primitive} and {@link
 *     org.geotools.api.geometry.complex.Complex}. Concequently, there is a clash in the semantics
 *     of some set theoretic operation. Specifically, {@code Primitive.contains(...)} (returns FALSE
 *     for end points) is different from {@code Complex.contains(...)} (returns TRUE for end
 *     points).
 */
@UML(identifier = "GM_CompositeSurface", specification = ISO_19107)
public interface CompositeSolid extends Composite, Solid {
    /**
     * Returns the set of solids that form the core of this complex. To get a full representation of
     * the elements in the {@linkplain Complex complex}, the {@linkplain
     * org.geotools.api.geometry.primitive.Surface surfaces}, {@linkplain
     * org.geotools.api.geometry.primitive.Curve curves} and {@linkplain
     * org.geotools.api.geometry.primitive.Point points} on the boundary of the generator set if
     * {@linkplain Solid solids} would have to be added to the generator list.
     *
     * @return The set of solids in this composite.
     * @see Solid#getComposite
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Override
    @UML(identifier = "generator", obligation = MANDATORY, specification = ISO_19107)
    Set<Solid> getGenerators();
}
