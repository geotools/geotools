/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import java.util.List;
import java.util.ArrayList;
import java.awt.image.DataBuffer;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Specifies the various dimension types for coverage values.
 * For grid coverages, these correspond to band types.
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/SampleDimensionType.java $
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see SampleDimension
 */
@UML(identifier="CV_SampleDimensionType", specification=OGC_01004)
public final class SampleDimensionType extends CodeList<SampleDimensionType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4153433145134818506L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<SampleDimensionType> VALUES = new ArrayList<SampleDimensionType>(11);

    /**
     * Unsigned 1 bit integers.
     *
     * @rename Renamed {@code CV_1BIT} as {@code UNSIGNED_1BIT} since we
     *         drop the prefix, but can't get a name starting with a digit.
     */
    @UML(identifier="CV_1BIT", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType UNSIGNED_1BIT = new SampleDimensionType("UNSIGNED_1BIT");

    /**
     * Unsigned 2 bits integers.
     *
     * @rename Renamed {@code CV_2BIT} as {@code UNSIGNED_2BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     */
    @UML(identifier="CV_2BIT", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType UNSIGNED_2BITS = new SampleDimensionType("UNSIGNED_2BITS");

    /**
     * Unsigned 4 bits integers.
     *
     * @rename Renamed {@code CV_4BIT} as {@code UNSIGNED_4BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     */
    @UML(identifier="CV_4BIT", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType UNSIGNED_4BITS = new SampleDimensionType("UNSIGNED_4BITS");

    /**
     * Unsigned 8 bits integers.
     *
     * @rename Renamed {@code CV_8BIT_U} as {@code UNSIGNED_8BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #SIGNED_8BITS
     * @see DataBuffer#TYPE_BYTE
     */
    @UML(identifier="CV_8BIT_U", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType UNSIGNED_8BITS = new SampleDimensionType("UNSIGNED_8BITS");

    /**
     * Signed 8 bits integers.
     *
     * @rename Renamed {@code CV_8BIT_S} as {@code SIGNED_8BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #UNSIGNED_8BITS
     */
    @UML(identifier="CV_8BIT_S", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType SIGNED_8BITS = new SampleDimensionType("SIGNED_8BITS");

    /**
     * Unsigned 16 bits integers.
     *
     * @rename Renamed {@code CV_16BIT_U} as {@code UNSIGNED_16BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #SIGNED_16BITS
     * @see DataBuffer#TYPE_USHORT
     */
    @UML(identifier="CV_16BIT_U", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType UNSIGNED_16BITS = new SampleDimensionType("UNSIGNED_16BITS");

    /**
     * Signed 16 bits integers.
     *
     * @rename Renamed {@code CV_16BIT_S} as {@code SIGNED_16BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #UNSIGNED_16BITS
     * @see DataBuffer#TYPE_SHORT
     */
    @UML(identifier="CV_16BIT_S", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType SIGNED_16BITS = new SampleDimensionType("SIGNED_16BITS");

    /**
     * Unsigned 32 bits integers.
     *
     * @rename Renamed {@code CV_32BIT_U} as {@code UNSIGNED_32BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #SIGNED_32BITS
     */
    @UML(identifier="CV_32BIT_U", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType UNSIGNED_32BITS = new SampleDimensionType("UNSIGNED_32BITS");

    /**
     * Signed 32 bits integers.
     *
     * @rename Renamed {@code CV_32BIT_S} as {@code SIGNED_32BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #UNSIGNED_32BITS
     * @see DataBuffer#TYPE_INT
     */
    @UML(identifier="CV_32BIT_S", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType SIGNED_32BITS = new SampleDimensionType("SIGNED_32BITS");

    /**
     * Simple precision floating point numbers.
     *
     * @rename Renamed {@code CV_32BIT_REAL} as {@code REAL_32BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #REAL_64BITS
     * @see DataBuffer#TYPE_FLOAT
     */
    @UML(identifier="CV_32BIT_REAL", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType REAL_32BITS = new SampleDimensionType("REAL_32BITS");

    /**
     * Double precision floating point numbers.
     *
     * @rename Renamed {@code CV_64BIT_REAL} as {@code REAL_64BITS} since we
     *         drop the prefix, but can't get a name starting with a digit.
     *
     * @see #REAL_32BITS
     * @see DataBuffer#TYPE_DOUBLE
     */
    @UML(identifier="CV_64BIT_REAL", obligation=CONDITIONAL, specification=OGC_01004)
    public static final SampleDimensionType REAL_64BITS = new SampleDimensionType("REAL_64BITS");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private SampleDimensionType(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code SampleDimensionType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static SampleDimensionType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new SampleDimensionType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public SampleDimensionType[] family() {
        return values();
    }

    /**
     * Returns the sample dimension type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static SampleDimensionType valueOf(String code) {
        return valueOf(SampleDimensionType.class, code);
    }
}
