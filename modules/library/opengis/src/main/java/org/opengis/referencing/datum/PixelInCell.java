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

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Specification of the way the image grid is associated with the image data attributes.
 * <p>
 * This code list is similar to {@link org.opengis.metadata.spatial.PixelOrientation}
 * except that the later is more clearly restricted to the two-dimensional case.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/datum/PixelInCell.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="CD_PixelInCell", specification=ISO_19111)
public final class PixelInCell extends CodeList<PixelInCell> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 2857889370030758462L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<PixelInCell> VALUES = new ArrayList<PixelInCell>(2);

    /**
     * The origin of the image coordinate system is the centre of a grid cell or image pixel.
     *
     * @see org.opengis.metadata.spatial.PixelOrientation#CENTER
     */
    @UML(identifier="cell center", obligation=CONDITIONAL, specification=ISO_19111)
    public static final PixelInCell CELL_CENTER = new PixelInCell("CELL_CENTER");

    /**
     * The origin of the image coordinate system is the corner of a grid cell, or half-way
     * between the centres of adjacent image pixels.
     *
     * @see org.opengis.metadata.spatial.PixelOrientation#LOWER_LEFT
     */
    @UML(identifier="cell corner", obligation=CONDITIONAL, specification=ISO_19111)
    public static final PixelInCell CELL_CORNER = new PixelInCell("CELL_CORNER");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private PixelInCell(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code PixelInCell}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static PixelInCell[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new PixelInCell[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public PixelInCell[] family() {
        return values();
    }

    /**
     * Returns the pixel in cell that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static PixelInCell valueOf(String code) {
        return valueOf(PixelInCell.class, code);
    }
}
