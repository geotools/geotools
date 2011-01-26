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

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.UnmodifiableGeometryException;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Basic data type for a geometric object consisting of one and only one point.
 * In most cases, the state of a {@code Point} is fully determined by its
 * position attribute. The only exception to this is if the {@code Point}
 * has been subclassed to provide additional non-geometric information such as
 * symbology.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see PrimitiveFactory#createPoint
 *
 * @todo Some associations are commented out for now.
 */
@UML(identifier="GM_Point", specification=ISO_19107)
public interface Point extends Primitive, Position {
    /**
     * Returns the direct position of this point. {@code Point} is the only subclass
     * of {@linkplain Primitive primitive} that cannot use {@linkplain Position positions}
     * to represent its defining geometry. A {@linkplain Position position} is either a
     * {@linkplain DirectPosition direct position} or a reference to a {@code Point}
     * (from which a {@linkplain DirectPosition direct position} may be obtained). By not
     * allowing {@code Point} to use this technique, infinitely recursive references
     * are prevented.
     *
     * @return The direct position.
     */
    @UML(identifier="position", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition getDirectPosition();

    /**
     * Sets the direct position of this point. {@code Point} is the only subclass
     * of {@linkplain Primitive primitive} that cannot use {@linkplain Position positions}
     * to represent its defining geometry. A {@linkplain Position position} is either a
     * {@linkplain DirectPosition direct position} or a reference to a {@code Point}
     * (from which a {@linkplain DirectPosition direct position} may be obtained). By not
     * allowing {@code Point} to use this technique, infinitely recursive references
     * are prevented.
     *
     * @param  position The direct position.
     * @throws UnmodifiableGeometryException if this geometry is not modifiable.
     *
     * @since GeoAPI 2.2
     */
    @UML(identifier="position", obligation=MANDATORY, specification=ISO_19107)
    void setDirectPosition(DirectPosition position) throws UnmodifiableGeometryException;

    /**
     * @deprecated Renamed as {@link #setDirectPosition} for consistency with {@link #getDirectPosition}.
     *
     * @param  position The direct position.
     * @throws UnmodifiableGeometryException if this geometry is not modifiable.
     */
    @Deprecated
    @UML(identifier="position", obligation=MANDATORY, specification=ISO_19107)
    void setPosition(DirectPosition position) throws UnmodifiableGeometryException;

    /**
     * Returns always {@code null}, since point has no boundary.
     *
     * @return Always {@code null}.
     */
    @UML(identifier="boundary", obligation=MANDATORY, specification=ISO_19107)
    PrimitiveBoundary getBoundary();

    /**
     * Returns the bearing, as a unit vector, of the tangent (at this {@code Point}) to
     * the curve between this {@code Point} and a passed {@linkplain Position position}.
     * The choice of the curve type for defining the bearing is dependent on the
     * {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system}
     * in which this {@code Point} is defined.
     * For example, in the Mercator projection, the curve is the rhumb line.
     * In 3D, geocentric coordinate system, the curve may be the geodesic joining the two
     * points along the surface of the geoid or ellipsoid in use. Implementations that support
     * this function shall specify the nature of the curve to be used.
     *
     * @param toPoint the destination point.
     * @return The tangent to the curve between this point and the passed position.
     */
    @UML(identifier="bearing", obligation=MANDATORY, specification=ISO_19107)
    Bearing getBearing(Position toPoint);

    /**
     * Returns always {@code null}, since points have no proxy.
     *
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Oriented")
    @UML(identifier="proxy", obligation=FORBIDDEN, specification=ISO_19107)
    OrientablePrimitive[] getProxy();

//    public org.opengis.geometry.complex.GM_CompositePoint composite[];
}
