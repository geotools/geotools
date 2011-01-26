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
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Represents a single connected component of a {@linkplain SolidBoundary solid boundary}.
 * A shell consists of a number of references to {@linkplain OrientableSurface orientable
 * surfaces} connected in a topological cycle (an object whose boundary is empty). Unlike a
 * {@linkplain Ring ring}, a {@code Shell}'s elements have no natural sort order. Like
 * {@linkplain Ring rings}, {@code Shell}s are simple.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see SolidBoundary
 * @see Ring
 */
@UML(identifier="GM_Shell", specification=ISO_19107)
public interface Shell extends CompositeSurface {
    /**
     * Always returns {@code true} since shell objects are simples.
     *
     * @return Always {@code true}.
     */
    @UML(identifier="isSimple", obligation=MANDATORY, specification=ISO_19107)
    boolean isSimple();
}
