/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;

import org.opengis.annotation.XmlElement;


/**
 * The ContrastEnhancement element defines contrast enhancement for a channel of a
 * false-color image or for a color image.
 * 
 * In the case of a color image, the relative grayscale brightness of a pixel color is used.
 * “Normalize” means to stretch the contrast so that the dimmest color is stretched to black
 * and the brightest color is stretched to white, with all colors in between stretched out
 * linearly. “Histogram” means to stretch the contrast based on a histogram of how many
 * colors are at each brightness level on input, with the goal of producing equal number of
 * pixels in the image at each brightness level on output. This has the effect of revealing
 * many subtle ground features.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("ContrastEnchancement:type")
public final class ContrastMethod extends CodeList<ContrastMethod> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -7328502367911363577L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<ContrastMethod> VALUES = new ArrayList<ContrastMethod>(3);

    /**
     * Normalize enchancement.
     * “Normalize” means to stretch the contrast so that the dimmest color is stretched to black
     * and the brightest color is stretched to white, with all colors in between stretched out
     * linearly.
     */
    @XmlElement("Normalize")
    public static final ContrastMethod NORMALIZE = new ContrastMethod("NORMALIZE");

    /**
     * Histogram enchancement.
     * “Histogram” means to stretch the contrast based on a histogram of how many
     * colors are at each brightness level on input, with the goal of producing equal number of
     * pixels in the image at each brightness level on output.
     */
    @XmlElement("Histogram")
    public static final ContrastMethod HISTOGRAM = new ContrastMethod("HISTOGRAM");

    /**
     * No enchancement.
     * this is the default value.
     */
    public static final ContrastMethod NONE = new ContrastMethod("NONE");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private ContrastMethod(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code ContrastType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static ContrastMethod[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new ContrastMethod[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public ContrastMethod[] family() {
        return values();
    }

    /**
     * Returns the contrast type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static ContrastMethod valueOf(String code) {
        return valueOf(ContrastMethod.class, code);
    }
}
