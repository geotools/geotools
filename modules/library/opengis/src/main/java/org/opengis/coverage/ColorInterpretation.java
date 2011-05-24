/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Specifies the mapping of a band to a color model component.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/ColorInterpretation.java $
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see PaletteInterpretation
 * @see SampleDimension
 *
 */
@UML(identifier="CV_ColorInterpretation", specification=OGC_01004)
public final class ColorInterpretation extends CodeList<ColorInterpretation> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 6947933527594223350L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<ColorInterpretation> VALUES = new ArrayList<ColorInterpretation>(14);

    /**
     * Band is not associated with a color model component.
     */
    @UML(identifier="CV_Undefined", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation UNDEFINED = new ColorInterpretation("UNDEFINED");

    /**
     * Band is an index into a lookup table.
     *
     * @see #PALETTE_INDEX
     * @see PaletteInterpretation#GRAY
     */
    @UML(identifier="CV_GrayIndex", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation GRAY_INDEX = new ColorInterpretation("GRAY_INDEX");

    /**
     * Band is a color index into a color table.
     *
     * @see #GRAY_INDEX
     */
    @UML(identifier="CV_PaletteIndex", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation PALETTE_INDEX = new ColorInterpretation("PALETTE_INDEX");

    /**
     * Red Band for the {@linkplain PaletteInterpretation#RGB RGB} color model components.
     *
     * @see #GREEN_BAND
     * @see #BLUE_BAND
     * @see #ALPHA_BAND
     * @see PaletteInterpretation#RGB
     */
    @UML(identifier="CV_RedBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation RED_BAND = new ColorInterpretation("RED_BAND");

    /**
     * Greend Band for the {@linkplain PaletteInterpretation#RGB RGB} color model components.
     *
     * @see #RED_BAND
     * @see #BLUE_BAND
     * @see #ALPHA_BAND
     * @see PaletteInterpretation#RGB
     */
    @UML(identifier="CV_GreenBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation GREEN_BAND = new ColorInterpretation("GREEN_BAND");

    /**
     * Blue Band for the {@linkplain PaletteInterpretation#RGB RGB} color model components.
     *
     * @see #RED_BAND
     * @see #GREEN_BAND
     * @see #ALPHA_BAND
     * @see PaletteInterpretation#RGB
     */
    @UML(identifier="CV_BlueBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation BLUE_BAND = new ColorInterpretation("BLUE_BAND");

    /**
     * Alpha Band for the {@linkplain PaletteInterpretation#RGB RGB} color model components.
     * Alpha band may or may not be present.
     *
     * @see #RED_BAND
     * @see #GREEN_BAND
     * @see #BLUE_BAND
     * @see PaletteInterpretation#RGB
     */
    @UML(identifier="CV_AlphaBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation ALPHA_BAND = new ColorInterpretation("ALPHA_BAND");

    /**
     * Hue Band for the {@linkplain PaletteInterpretation#HLS HLS} color model.
     *
     * @see #SATURATION_BAND
     * @see #LIGHTNESS_BAND
     * @see PaletteInterpretation#HLS
     */
    @UML(identifier="CV_HueBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation HUE_BAND = new ColorInterpretation("HUE_BAND");

    /**
     * Saturation Band for the {@linkplain PaletteInterpretation#HLS HLS} color model.
     *
     * @see #HUE_BAND
     * @see #LIGHTNESS_BAND
     * @see PaletteInterpretation#HLS
     */
    @UML(identifier="CV_SaturationBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation SATURATION_BAND = new ColorInterpretation("SATURATION_BAND");

    /**
     * Lightness Band for the {@linkplain PaletteInterpretation#HLS HLS} color model.
     *
     * @see #HUE_BAND
     * @see #SATURATION_BAND
     * @see PaletteInterpretation#HLS
     */
    @UML(identifier="CV_LightnessBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation LIGHTNESS_BAND = new ColorInterpretation("LIGHTNESS_BAND");

    /**
     * Cyan Band for the {@linkplain PaletteInterpretation#CMYK CMYK} color model.
     *
     * @see #MAGENTA_BAND
     * @see #YELLOW_BAND
     * @see #BLACK_BAND
     * @see PaletteInterpretation#CMYK
     */
    @UML(identifier="CV_CyanBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation CYAN_BAND = new ColorInterpretation("CYAN_BAND");

    /**
     * Magenta Band for the {@linkplain PaletteInterpretation#CMYK CMYK} color model.
     *
     * @see #CYAN_BAND
     * @see #YELLOW_BAND
     * @see #BLACK_BAND
     * @see PaletteInterpretation#CMYK
     */
    @UML(identifier="CV_MagentaBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation MAGENTA_BAND = new ColorInterpretation("MAGENTA_BAND");

    /**
     * Yellow Band for the {@linkplain PaletteInterpretation#CMYK CMYK} color model.
     *
     * @see #CYAN_BAND
     * @see #MAGENTA_BAND
     * @see #BLACK_BAND
     * @see PaletteInterpretation#CMYK
     */
    @UML(identifier="CV_YellowBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation YELLOW_BAND = new ColorInterpretation("YELLOW_BAND");

    /**
     * Black Band for the {@linkplain PaletteInterpretation#CMYK CMYK} color model.
     *
     * @see #CYAN_BAND
     * @see #MAGENTA_BAND
     * @see #YELLOW_BAND
     * @see PaletteInterpretation#CMYK
     */
    @UML(identifier="CV_BlackBand", obligation=CONDITIONAL, specification=OGC_01004)
    public static final ColorInterpretation BLACK_BAND = new ColorInterpretation("BLACK_BAND");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private ColorInterpretation(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code ColorInterpretation}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static ColorInterpretation[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new ColorInterpretation[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public ColorInterpretation[] family() {
        return values();
    }

    /**
     * Returns the color interpretation that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static ColorInterpretation valueOf(String code) {
        return valueOf(ColorInterpretation.class, code);
    }
}
