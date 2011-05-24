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
 * Point in a pixel corresponding to the Earth location of the pixel.
 * <p>
 * This code list is restricted to the two-dimensional case. A similar code
 * list, {@link org.opengis.referencing.datum.PixelInCell}, can be used for
 * <var>n</var>-dimensional grid cell.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/spatial/PixelOrientation.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_PixelOrientationCode", specification=ISO_19115)
public final class PixelOrientation extends CodeList<PixelOrientation> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 7885677198357949308L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<PixelOrientation> VALUES = new ArrayList<PixelOrientation>(5);

    /**
     * Point in a pixel corresponding to the Earth location of the pixel.
     *
     * @see org.opengis.referencing.datum.PixelInCell#CELL_CENTER
     */
    @UML(identifier="center", obligation=CONDITIONAL, specification=ISO_19115)
    public static final PixelOrientation CENTER = new PixelOrientation("CENTER");

    /**
     * The corner in the pixel closest to the origin of the SRS; if two are at the same
     * distance from the origin, the one with the smallest x-value.
     *
     * @todo The sentence "<cite>closest to the origin of the SRS</cite> probably applies to
     *       positive coordinates only. For the general case including both positive and negative
     *       coordinates, we should probably read "in the direction of negative infinity". This
     *       interpretation should be clarified with ISO.
     *
     * @see org.opengis.referencing.datum.PixelInCell#CELL_CORNER
     */
    @UML(identifier="lowerLeft", obligation=CONDITIONAL, specification=ISO_19115)
    public static final PixelOrientation LOWER_LEFT = new PixelOrientation("LOWER_LEFT");

    /**
     * Next corner counterclockwise from the {@linkplain #LOWER_LEFT lower left}.
     */
    @UML(identifier="lowerRight", obligation=CONDITIONAL, specification=ISO_19115)
    public static final PixelOrientation LOWER_RIGHT = new PixelOrientation("LOWER_RIGHT");

    /**
     * Next corner counterclockwise from the {@linkplain #LOWER_RIGHT lower right}.
     */
    @UML(identifier="upperRight", obligation=CONDITIONAL, specification=ISO_19115)
    public static final PixelOrientation UPPER_RIGHT = new PixelOrientation("UPPER_RIGHT");

    /**
     * Next corner counterclockwise from the {@linkplain #UPPER_RIGHT upper right}.
     */
    @UML(identifier="upperLeft", obligation=CONDITIONAL, specification=ISO_19115)
    public static final PixelOrientation UPPER_LEFT = new PixelOrientation("UPPER_LEFT");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private PixelOrientation(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code PixelOrientation}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static PixelOrientation[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new PixelOrientation[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public PixelOrientation[] family() {
        return values();
    }

    /**
     * Returns the pixel orientation that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static PixelOrientation valueOf(String code) {
        return valueOf(PixelOrientation.class, code);
    }
}
