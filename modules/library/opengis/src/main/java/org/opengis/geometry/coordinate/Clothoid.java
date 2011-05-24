/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The clothoid (or Cornu's spiral), a plane curve whose curvature is a fixed function of
 * its length. In suitably chosen co-ordinates it is given by Fresnel's integrals:
 *
 * <P><center>(TODO: paste the equation here)</center></P>
 *
 * This geometry is mainly used as a transition curve between curves of type straight
 * line/circular arc or circular arc/circular arc. With this curve type it is possible
 * to achieve a C2-continous transition between the above mentioned curve types. One
 * formula for the clothoid is <var>A</var><sup>2</sup> = <var>R</var>&times;<var>t</var>
 * where <var>A</var> is a constant, <var>R</var> is the varying radius of curvature along
 * the curve and <var>t</var> is the length along the curve and given in the Fresnel integrals.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/Clothoid.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_Clothoid", specification=ISO_19107)
public interface Clothoid extends CurveSegment {
    /**
     * Returns an affine mapping that places the curve defined by the Fresnel Integrals
     * into the coordinate reference system of this object.
     */
    @UML(identifier="refLocation", obligation=MANDATORY, specification=ISO_19107)
    AffinePlacement getReferenceLocation();

    /**
     * Gives the value for <var>A</var> in the equations above.
     */
    @UML(identifier="scaleFactor", obligation=MANDATORY, specification=ISO_19107)
    double getScaleFactor();

    /**
     * Returns the arc length distance from the inflection point that will be the
     * {@linkplain #getStartPoint start point} for this curve segment. This shall
     * be lower limit <var>t</var> used in the Fresnel integral and is the value
     * of the constructive parameter of this curve segment at its start point. The
     * start parameter can be either positive or negative. The parameter <var>t</var>
     * acts as a constructive parameter.
     *
     * <P>NOTE: If 0 lies between the {@linkplain #getStartConstructiveParam start constructive
     * parameter} and {@linkplain #getEndConstructiveParam end constructive parameter} of the
     * clothoid, then the curve goes through the clothoid's inflection point, and the direction
     * of its radius of curvature, given by the second derivative vector, changes sides
     * with respect to the tangent vector. The term "length" for the parameter {@code t}
     * is applicable only in the parameter space, and its relation to arc length after use of
     * the placement, and with respect to the coordinate reference system of the curve is not
     * deterministic.</P>
     */
    @UML(identifier="startParameter", obligation=MANDATORY, specification=ISO_19107)
    double getStartConstructiveParam();

    /**
     * Returns the arc length distance from the inflection point that will be the
     * {@linkplain #getEndPoint end point} for this curve segment. This shall be
     * upper limit <var>t</var> used in the Fresnel integral and is the constructive
     * parameter of this curve segment at its end point. The end constructive param
     * can be either positive or negative.
     */
    @UML(identifier="endParameter", obligation=MANDATORY, specification=ISO_19107)
    double getEndConstructiveParam();
}
