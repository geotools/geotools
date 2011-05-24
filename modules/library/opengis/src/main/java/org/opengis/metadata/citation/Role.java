/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.citation;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Function performed by the responsible party.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/citation/Role.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@UML(identifier="CI_RoleCode", specification=ISO_19115)
public final class Role extends CodeList<Role> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -7763516018565534103L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<Role> VALUES = new ArrayList<Role>(11);

    /**
     * Party that supplies the resource.
     */
    @UML(identifier="resourceProvider", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role RESOURCE_PROVIDER = new Role("RESOURCE_PROVIDER");

    /**
     * Party that accepts accountability and responsibility for the data and ensures
     * appropriate care and maintenance of the resource.
     */
    @UML(identifier="custodian", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role CUSTODIAN = new Role("CUSTODIAN");

    /**
     * Party that owns the resource.
     */
    @UML(identifier="owner", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role OWNER = new Role("OWNER");

    /**
     * Party who uses the resource.
     */
    @UML(identifier="user", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role USER = new Role("USER");

    /**
     * Party who distributes the resource.
     */
    @UML(identifier="distributor", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role DISTRIBUTOR = new Role("DISTRIBUTOR");

    /**
     * Party who created the resource.
     */
    @UML(identifier="originator", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role ORIGINATOR = new Role("ORIGINATOR");

    /**
     * Party who can be contacted for acquiring knowledge about or acquisition of the resource.
     */
    @UML(identifier="pointOfContact", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role POINT_OF_CONTACT = new Role("POINT_OF_CONTACT");

    /**
     * Key party responsible for gathering information and conducting research.
     */
    @UML(identifier="principalInvestigator", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role PRINCIPAL_INVESTIGATOR = new Role("PRINCIPAL_INVESTIGATOR");

    /**
     * Party who has processed the data in a manner such that the resource has been modified.
     */
    @UML(identifier="processor", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role PROCESSOR = new Role("PROCESSOR");

    /**
     * Party who published the resource.
     */
    @UML(identifier="publisher", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role PUBLISHER = new Role("PUBLISHER");

    /**
     * Party who authored the resource.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="author", obligation=CONDITIONAL, specification=ISO_19115)
    public static final Role AUTHOR = new Role("AUTHOR");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private Role(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code Role}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static Role[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new Role[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public Role[] family() {
        return values();
    }

    /**
     * Returns the role that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static Role valueOf(String code) {
        return valueOf(Role.class, code);
    }
}
