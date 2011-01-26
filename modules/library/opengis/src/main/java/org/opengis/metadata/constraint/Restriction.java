/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.constraint;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Limitation(s) placed upon the access or use of the data.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_RestrictionCode", specification=ISO_19115)
public final class Restriction extends CodeList<Restriction> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 7949159742645339894L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<Restriction> VALUES = new ArrayList<Restriction>(8);

    /**
     * Exclusive right to the publication, production, or sale of the rights to a literary,
     * dramatic, musical, or artistic work, or to the use of a commercial print or label,
     * granted by law for a specified period of time to an author, composer, artist, distributor.
     */
    @UML(identifier="copyright", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction COPYRIGHT = new Restriction("COPYRIGHT");

    /**
     * Government has granted exclusive right to make, sell, use or license an invention
     * or discovery.
     */
    @UML(identifier="patent", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction PATENT = new Restriction("PATENT");

    /**
     * Produced or sold information awaiting a patent.
     */
    @UML(identifier="patentPending", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction PATENT_PENDING = new Restriction("PATENT_PENDING");

    /**
     * A name, symbol, or other device identifying a product, officially registered and
     * legally restricted to the use of the owner or manufacturer.
     */
    @UML(identifier="trademark", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction TRADEMARK = new Restriction("TRADEMARK");

    /**
     * Formal permission to do something.
     */
    @UML(identifier="license", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction LICENSE = new Restriction("LICENSE");

    /**
     * Rights to financial benefit from and control of distribution of non-tangible property
     * that is a result of creativity.
     */
    @UML(identifier="intellectualPropertyRights", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction INTELLECTUAL_PROPERTY_RIGHTS = new Restriction("INTELLECTUAL_PROPERTY_RIGHTS");

    /**
     * Withheld from general circulation or disclosure.
     */
    @UML(identifier="restricted", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction RESTRICTED = new Restriction("RESTRICTED");

    /**
     * Limitation not listed.
     */
    @UML(identifier="otherRestictions", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Restriction OTHER_RESTRICTIONS = new Restriction("OTHER_RESTRICTIONS");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private Restriction(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code Restriction}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static Restriction[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new Restriction[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public Restriction[] family() {
        return values();
    }

    /**
     * Returns the restriction that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static Restriction valueOf(String code) {
        return valueOf(Restriction.class, code);
    }
}
