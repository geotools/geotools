/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.datum;

import static org.opengis.annotation.Obligation.CONDITIONAL;
import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Specification.ISO_19111;
import static org.opengis.annotation.Specification.OGC_01009;

import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.opengis.annotation.UML;
import org.opengis.referencing.IdentifiedObject;

/**
 * Geometric figure that can be used to describe the approximate shape of the earth. In mathematical
 * terms, it is a surface formed by the rotation of an ellipse about its minor axis. An ellipsoid
 * requires two defining parameters:
 *
 * <p>
 *
 * <ul>
 *   <li>{@linkplain #getSemiMajorAxis semi-major axis} and {@linkplain #getInverseFlattening
 *       inverse flattening}, or
 *   <li>{@linkplain #getSemiMajorAxis semi-major axis} and {@linkplain #getSemiMinorAxis semi-minor
 *       axis}.
 * </ul>
 *
 * <p>There is not just one ellipsoid. An ellipsoid is a matter of choice, and therefore many
 * choices are possible. The size and shape of an ellipsoid was traditionally chosen such that the
 * surface of the geoid is matched as closely as possible locally, e.g. in a country. A number of
 * global best-fit ellipsoids are now available. An association of an ellipsoid with the earth is
 * made through the definition of the size and shape of the ellipsoid and the position and
 * orientation of this ellipsoid with respect to the earth. Collectively this choice is captured by
 * the concept of "{@linkplain GeodeticDatum geodetic datum}". A change of size, shape, position or
 * orientation of an ellipsoid will result in a change of geographic coordinates of a point and be
 * described as a different geodetic datum. Conversely geographic coordinates are unambiguous only
 * when associated with a geodetic datum.
 *
 * @departure ISO 19111 defines {@link #getSemiMinorAxis semiMinorAxis}, {@link
 *     #getInverseFlattening inverseFlattening} and {@link #isIvfDefinitive isSphere} in a separated
 *     structure named {@code secondDefiningParameter} of type {@code union}. The C/C++ concept of
 *     {@code union} doesn't exist in Java, but implementors can achieve the same functionality by
 *     providing different {@code Ellipsoid} subclasses computing one parameter on-the-fly from the
 *     other one. The {@link #isIvfDefinitive isIvfDefinitive} attribute imported from OGC 01-004
 *     can be used for distinguishing between the two cases.
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "CD_Ellipsoid", specification = ISO_19111)
public interface Ellipsoid extends IdentifiedObject {
    /**
     * Returns the linear unit of the {@linkplain #getSemiMajorAxis semi-major} and {@linkplain
     * #getSemiMinorAxis semi-minor} axis values.
     *
     * @return The axis linear unit.
     */
    @UML(identifier = "getAxisUnit", specification = OGC_01009)
    Unit<Length> getAxisUnit();

    /**
     * Length of the semi-major axis of the ellipsoid. This is the equatorial radius in {@linkplain
     * #getAxisUnit axis linear unit}.
     *
     * @return Length of semi-major axis.
     * @unitof Length
     */
    @UML(identifier = "semiMajorAxis", obligation = MANDATORY, specification = ISO_19111)
    double getSemiMajorAxis();

    /**
     * Length of the semi-minor axis of the ellipsoid. This is the polar radius in {@linkplain
     * #getAxisUnit axis linear unit}.
     *
     * @return Length of semi-minor axis.
     * @unitof Length
     */
    @UML(
            identifier = "secondDefiningParameter.semiMinorAxis",
            obligation = CONDITIONAL,
            specification = ISO_19111)
    double getSemiMinorAxis();

    /**
     * Returns the value of the inverse of the flattening constant. The inverse flattening is
     * related to the equatorial/polar radius by the formula
     *
     * <p><var>ivf</var>&nbsp;=&nbsp;<var>r</var><sub>e</sub>/(<var>r</var><sub>e</sub>-<var>r</var><sub>p</sub>).
     *
     * <p>For perfect spheres (i.e. if {@link #isSphere} returns {@code true}), the {@link
     * Double#POSITIVE_INFINITY POSITIVE_INFINITY} value is used.
     *
     * @return The inverse flattening value.
     * @unitof Scale
     */
    @UML(
            identifier = "secondDefiningParameter.inverseFlattening",
            obligation = CONDITIONAL,
            specification = ISO_19111)
    double getInverseFlattening();

    /**
     * Indicates if the {@linkplain #getInverseFlattening inverse flattening} is definitive for this
     * ellipsoid. Some ellipsoids use the IVF as the defining value, and calculate the polar radius
     * whenever asked. Other ellipsoids use the polar radius to calculate the IVF whenever asked.
     * This distinction can be important to avoid floating-point rounding errors.
     *
     * @return {@code true} if the {@linkplain #getInverseFlattening inverse flattening} is
     *     definitive, or {@code false} if the {@linkplain #getSemiMinorAxis polar radius} is
     *     definitive.
     */
    @UML(
            identifier = "CS_Ellipsoid.isIvfDefinitive",
            obligation = CONDITIONAL,
            specification = OGC_01009)
    boolean isIvfDefinitive();

    /**
     * {@code true} if the ellipsoid is degenerate and is actually a sphere. The sphere is
     * completely defined by the {@linkplain #getSemiMajorAxis semi-major axis}, which is the radius
     * of the sphere.
     *
     * @return {@code true} if the ellipsoid is degenerate and is actually a sphere.
     */
    @UML(
            identifier = "secondDefiningParameter.isSphere",
            obligation = CONDITIONAL,
            specification = ISO_19111)
    boolean isSphere();
}
