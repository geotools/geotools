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

import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Represent a single connected component of a {@linkplain SurfaceBoundary surface boundary}.
 * It consists of a number of references to {@linkplain OrientableCurve orientable curves}
 * connected in a cycle (an object whose boundary is empty). A {@code Ring} is structurally
 * similar to a {@linkplain CompositeCurve composite curve} in that the end point of each
 * {@linkplain OrientableCurve orientable curve} in the sequence is the start point of the next
 * {@linkplain OrientableCurve orientable curve} in the sequence. Since the sequence is circular,
 * there is no exception to this rule. Each ring, like all boundaries is a cycle and each ring is
 * simple.
 * <p>
 * Even though each {@code Ring} is simple, the boundary need not be simple. The easiest
 * case of this is where one of the interior rings of a surface is tangent to its exterior ring.
 * Implementations may enforce stronger restrictions on the interaction of boundary elements.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see SurfaceBoundary
 * @see Shell
 */
@UML(identifier="GM_Ring", specification=ISO_19107)
public interface Ring extends CompositeCurve {
    /**
     * Always returns {@code true} since ring objects are simples.
     *
     * @return Always {@code true}.
     */
    @UML(identifier="isSimple", obligation=MANDATORY, specification=ISO_19107)
    boolean isSimple();
}
