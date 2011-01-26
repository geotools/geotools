/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.aggregate;

import java.util.Set;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * An aggregate class containing only instances of {@link OrientableSurface}.
 * The association role {@link #getElements element} shall be the set of
 * {@linkplain OrientableSurface orientable surfaces} contained in this {@code MultiSurface}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Sanjay Jena
 * @author Prof. Dr. Jackson Roehrig
 * @since GeoAPI 2.1
 */
@UML(identifier="GM_MultiSurface", specification=ISO_19107)
public interface MultiSurface extends MultiPrimitive {
    /**
     * Returns the set containing the {@linkplain OrientableSurface orientable surfaces}
     * that compose this {@code MultiSurface}. The set may be modified if this geometry
     * {@linkplain #isMutable is mutable}.
     */
    @UML(identifier="element", obligation=MANDATORY, specification=ISO_19107)
    Set<OrientableSurface> getElements();

    /**
     * Returns the accumulated area of all {@linkplain OrientableSurface orientable surfaces}
     * contained in this {@code MultiSurface}. The area of a 2-dimensional geometric object
     * shall be a numeric measure of its surface area (in a square unit of distance). Since
     * area is an accumulation (integral) of the product of two distances, its return value
     * shall be in a unit of measure appropriate for measuring distances squared, such as
     * meters squared (m<sup>2</sup>).
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> Consistent with the definition of surface as a set of
     * {@linkplain DirectPosition direct positions}, holes in the surfaces will not contribute to
     * the total area. If the usual Green's Theorem (or more general Stokes' Theorem) integral is
     * used, the integral around the holes in the surface are subtracted from the integral
     * about the exterior of the surface patch.
     * </font></blockquote>
     *
     * @return The area.
     * @unitof Area
     */
    @UML(identifier="area", obligation=MANDATORY, specification=ISO_19107)
    double getArea();
}
