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

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Name of the medium.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/distribution/MediumName.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_MediumNameCode", specification=ISO_19115)
public final class MediumName extends CodeList<MediumName> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 2634504971646621701L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<MediumName> VALUES = new ArrayList<MediumName>(18);

    /**
     * Read-only optical disk.
     */
    @UML(identifier="cdRom", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CD_ROM = new MediumName("CD_ROM");

    /**
     * Digital versatile disk.
     */
    @UML(identifier="dvd", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName DVD = new MediumName("DVD");

    /**
     * Digital versatile disk digital versatile disk, read only.
     */
    @UML(identifier="dvdRom", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName DVD_ROM = new MediumName("DVD_ROM");

    /**
     * 3&frac12; inch magnetic disk.
     */
    @UML(identifier="3halfInchFloppy", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName FLOPPY_3_HALF_INCH = new MediumName("FLOPPY_3_HALF_INCH");

    /**
     * 5&frac14; inch magnetic disk.
     */
    @UML(identifier="5quarterInchFloppy", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName FLOPPY_5_QUARTER_INCH = new MediumName("FLOPPY_5_QUARTER_INCH");

    /**
     * 7 track magnetic tape.
     */
    @UML(identifier="7trackTape", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName TAPE_7_TRACK = new MediumName("TAPE_7_TRACK");

    /**
     * 9 track magnetic tape.
     */
    @UML(identifier="9trackTape", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName TAPE_9_TRACK = new MediumName("TAPE_9_TRACK");

    /**
     * 3480 cartridge tape drive.
     */
    @UML(identifier="3480Cartridge", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CARTRIDGE_3480 = new MediumName("CARTRIDGE_3480");

    /**
     * 3490 cartridge tape drive.
     */
    @UML(identifier="3490Cartridge", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CARTRIDGE_3490 = new MediumName("CARTRIDGE_3490");

    /**
     * 3580 cartridge tape drive.
     */
    @UML(identifier="3580Cartridge", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CARTRIDGE_3580 = new MediumName("CARTRIDGE_3580");

    /**
     * 4 millimetre magnetic tape.
     */
    @UML(identifier="4mmCartridgeTape", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CARTRIDGE_TAPE_4mm = new MediumName("CARTRIDGE_TAPE_4mm");

    /**
     * 8 millimetre magnetic tape.
     */
    @UML(identifier="8mmCartridgeTape", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CARTRIDGE_TAPE_8mm = new MediumName("CARTRIDGE_TAPE_8mm");

    /**
     * &frac14; inch magnetic tape.
     */
    @UML(identifier="1quarterInchCartridgeTape", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName CARTRIDGE_TAPE_1_QUARTER_INCH = new MediumName("CARTRIDGE_TAPE_1_QUARTER_INCH");

    /**
     * Half inch cartridge streaming tape drive.
     */
    @UML(identifier="digitalLinearTape", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName DIGITAL_LINEAR_TAPE = new MediumName("DIGITAL_LINEAR_TAPE");

    /**
     * Direct computer linkage.
     */
    @UML(identifier="onLine", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName ON_LINE = new MediumName("ON_LINE");

    /**
     * Linkage through a satellite communication system.
     */
    @UML(identifier="satellite", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName SATELLITE = new MediumName("SATELLITE");

    /**
     * Communication through a telephone network.
     */
    @UML(identifier="telephoneLink", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName TELEPHONE_LINK = new MediumName("TELEPHONE_LINK");

    /**
     * Pamphlet or leaflet giving descriptive information.
     */
    @UML(identifier="hardCopy", obligation=CONDITIONAL, specification=ISO_19115)
    public static final MediumName HARD_COPY = new MediumName("HARD_COPY");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private MediumName(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code MediumName}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static MediumName[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new MediumName[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public MediumName[] family() {
        return values();
    }

    /**
     * Returns the medium name that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static MediumName valueOf(String code) {
        return valueOf(MediumName.class, code);
    }
}
