/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.spatial;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Name of point and vector spatial objects used to locate zero-, one-, and twodimensional
 * spatial locations in the dataset.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_GeometricObjectTypeCode", specification=ISO_19115)
public final class GeometricObjectType extends CodeList<GeometricObjectType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -8910485325021913980L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<GeometricObjectType> VALUES = new ArrayList<GeometricObjectType>(6);

    /**
     * Set of geometric primitives such that their boundaries can be represented as a
     * union of other primitives.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="complex", obligation=CONDITIONAL, specification=ISO_19115)
    public static final GeometricObjectType COMPLEX = new GeometricObjectType("COMPLEX");

    /**
     * Connected set of curves, solids or surfaces.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="composite", obligation=CONDITIONAL, specification=ISO_19115)
    public static final GeometricObjectType COMPOSITE = new GeometricObjectType("COMPOSITE");

    /**
     * Bounded, 1-dimensional geometric primitive, representing the continuous image of a line.
     */
    @UML(identifier="curve", obligation=CONDITIONAL, specification=ISO_19115)
    public static final GeometricObjectType CURVE = new GeometricObjectType("CURVE");

    /**
     * Zero-dimensional geometric primitive, representing a position but not having an extent.
     */
    @UML(identifier="point", obligation=CONDITIONAL, specification=ISO_19115)
    public static final GeometricObjectType POINT = new GeometricObjectType("POINT");

    /**
     * Bounded, connected 3-dimensional geometric primitive, representing the
     * continuous image of a region of space.
     */
    @UML(identifier="solid", obligation=CONDITIONAL, specification=ISO_19115)
    public static final GeometricObjectType SOLID = new GeometricObjectType("SOLID");

    /**
     * Bounded, connected 2-dimensional geometric, representing the continuous image
     * of a region of a plane.
     */
    @UML(identifier="surface", obligation=CONDITIONAL, specification=ISO_19115)
    public static final GeometricObjectType SURFACE = new GeometricObjectType("SURFACE");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private GeometricObjectType(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code GeometricObjectType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static GeometricObjectType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new GeometricObjectType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public GeometricObjectType[] family() {
        return values();
    }

    /**
     * Returns the geometric object type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static GeometricObjectType valueOf(String code) {
        return valueOf(GeometricObjectType.class, code);
    }
}
