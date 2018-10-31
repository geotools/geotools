/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.distribution;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import java.util.ArrayList;
import java.util.List;
import org.opengis.annotation.UML;
import org.opengis.util.CodeList;

/**
 * Method used to write to the medium.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "MD_MediumFormatCode", specification = ISO_19115)
public final class MediumFormat extends CodeList<MediumFormat> {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = 413822250362716958L;

    /** List of all enumerations of this type. Must be declared before any enum declaration. */
    private static final List<MediumFormat> VALUES = new ArrayList<MediumFormat>(6);

    /** CoPy In / Out (UNIX file format and command). */
    @UML(identifier = "cpio", obligation = CONDITIONAL, specification = ISO_19115)
    public static final MediumFormat CPIO = new MediumFormat("CPIO");

    /** Tap ARchive. */
    @UML(identifier = "tar", obligation = CONDITIONAL, specification = ISO_19115)
    public static final MediumFormat TAR = new MediumFormat("TAR");

    /** High sierra file system. */
    @UML(identifier = "highSierra", obligation = CONDITIONAL, specification = ISO_19115)
    public static final MediumFormat HIGH_SIERRA = new MediumFormat("HIGH_SIERRA");

    /** Information processing - volume and file structure of CD-ROM. */
    @UML(identifier = "iso9660", obligation = CONDITIONAL, specification = ISO_19115)
    public static final MediumFormat ISO_9660 = new MediumFormat("ISO_9660");

    /** Rock ridge interchange protocol (UNIX). */
    @UML(identifier = "iso9660RockRidge", obligation = CONDITIONAL, specification = ISO_19115)
    public static final MediumFormat ISO_9660_ROCK_RIDGE = new MediumFormat("ISO_9660_ROCK_RIDGE");

    /** Hierarchical file system (Macintosh). */
    @UML(identifier = "iso9660AppleHFS", obligation = CONDITIONAL, specification = ISO_19115)
    public static final MediumFormat ISO_9660_APPLE_HFS = new MediumFormat("ISO_9660_APPLE_HFS");

    /**
     * Constructs an enum with the given name. The new enum is automatically added to the list
     * returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private MediumFormat(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code MediumFormat}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static MediumFormat[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new MediumFormat[VALUES.size()]);
        }
    }

    /** Returns the list of enumerations of the same kind than this enum. */
    public MediumFormat[] family() {
        return values();
    }

    /**
     * Returns the medium format that matches the given string, or returns a new one if none match
     * it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static MediumFormat valueOf(String code) {
        return valueOf(MediumFormat.class, code);
    }
}
