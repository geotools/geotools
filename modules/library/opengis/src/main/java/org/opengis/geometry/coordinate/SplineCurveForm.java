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

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Indicates which sort of curve may be approximated by a particular B-spline.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/SplineCurveForm.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_SplineCurveForm", specification=ISO_19107)
public final class SplineCurveForm extends CodeList<SplineCurveForm> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 7692137703533158212L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<SplineCurveForm> VALUES = new ArrayList<SplineCurveForm>(5);

    /**
     * A connected sequence of line segments represented by a 1 degree B-spline (a line string).
     */
    @UML(identifier="polylineForm", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm POLYLINE_FORM = new SplineCurveForm(
                                       "POLYLINE_FORM");

    /**
     * An arc of a circle or a complete circle.
     */
    @UML(identifier="circularArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm CIRCULAR_ARC = new SplineCurveForm(
                                       "CIRCULAR_ARC");

    /**
     * An arc of an ellipse or a complete ellipse.
     */
    @UML(identifier="ellipticalArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm ELLIPTICAL_ARC = new SplineCurveForm(
                                       "ELLIPTICAL_ARC");

    /**
     * An arc of a finite length of a parabola.
     */
    @UML(identifier="parabolicArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm PARABOLIC_ARC = new SplineCurveForm(
                                       "PARABOLIC_ARC");

    /**
     * An arc of a finite length of one branch of a hyperbola.
     */
    @UML(identifier="hyperbolicArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm HYPERBOLIC_ARC = new SplineCurveForm(
                                       "HYPERBOLIC_ARC");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private SplineCurveForm(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code SplineCurveForm}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static SplineCurveForm[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new SplineCurveForm[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public SplineCurveForm[] family() {
        return values();
    }

    /**
     * Returns the spline curve form that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static SplineCurveForm valueOf(String code) {
        return valueOf(SplineCurveForm.class, code);
    }
}
