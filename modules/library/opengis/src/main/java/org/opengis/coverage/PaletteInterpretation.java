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
import java.awt.color.ColorSpace;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Describes the color entry in a color table.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/PaletteInterpretation.java $
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see ColorInterpretation
 * @see SampleDimension
 */
@UML(identifier="CV_PaletteInterpretation", specification=OGC_01004)
public final class PaletteInterpretation extends CodeList<PaletteInterpretation> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -7387623392932592485L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<PaletteInterpretation> VALUES = new ArrayList<PaletteInterpretation>(4);

    /**
     * Gray Scale color palette.
     *
     * @see ColorSpace#TYPE_GRAY
     */
    @UML(identifier="CV_Gray", obligation=CONDITIONAL, specification=OGC_01004)
    public static final PaletteInterpretation GRAY = new PaletteInterpretation("GRAY");

    /**
     * RGB (Red Green Blue) color palette.
     *
     * @see ColorSpace#TYPE_RGB
     */
    @UML(identifier="CV_RGB", obligation=CONDITIONAL, specification=OGC_01004)
    public static final PaletteInterpretation RGB = new PaletteInterpretation("RGB");

    /**
     * CYMK (Cyan Yellow Magenta blacK) color palette.
     *
     * @see ColorSpace#TYPE_CMYK
     */
    @UML(identifier="CV_CMYK", obligation=CONDITIONAL, specification=OGC_01004)
    public static final PaletteInterpretation CMYK = new PaletteInterpretation("CMYK");

    /**
     * HSL (Hue Saturation Lightness) color palette.
     *
     * @see ColorSpace#TYPE_HLS
     */
    @UML(identifier="CV_HLS", obligation=CONDITIONAL, specification=OGC_01004)
    public static final PaletteInterpretation HLS = new PaletteInterpretation("HLS");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private PaletteInterpretation(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code PaletteInterpretation}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static PaletteInterpretation[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new PaletteInterpretation[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public PaletteInterpretation[] family() {
        return values();
    }

    /**
     * Returns the palette interpretation that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static PaletteInterpretation valueOf(String code) {
        return valueOf(PaletteInterpretation.class, code);
    }
}
