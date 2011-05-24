/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.complex;

import java.util.Set;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain Complex complex} with all the geometric properties of a surface. Thus, this
 * composite can be considered as a type of {@linkplain OrientableSurface orientable surface}.
 * Essentially, a composite surface is a collection of oriented surfaces that join in pairs on
 * common boundary curves and which, when considered as a whole, form a single surface.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/complex/CompositeSurface.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @todo This interface extends (indirectly) both {@link org.opengis.geometry.primitive.Primitive} and
 *       {@link org.opengis.geometry.complex.Complex}. Concequently, there is a clash in the semantics
 *       of some set theoretic operation. Specifically, {@code Primitive.contains(...)}
 *       (returns FALSE for end points) is different from {@code Complex.contains(...)}
 *       (returns TRUE for end points).
 */
@UML(identifier="GM_CompositeSurface", specification=ISO_19107)
public interface CompositeSurface extends Composite, OrientableSurface {
    /**
     * Returns the set of orientable surfaces that form the core of this complex.
     * To get a full representation of the elements in the {@linkplain Complex complex},
     * the {@linkplain org.opengis.geometry.primitive.Curve curves} and
     * {@linkplain org.opengis.geometry.primitive.Point points} on the boundary of the generator
     * set of {@linkplain org.opengis.geometry.primitive.Surface surfaces} would be added to the
     * curves in the generator list.
     *
     * @return The list of orientable surfaces in this composite.
     *
     * @see OrientableSurface#getComposite
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Composition")
    @UML(identifier="generator", obligation=MANDATORY, specification=ISO_19107)
    Set<OrientableSurface> getGenerators();
}
