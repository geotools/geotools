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

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import java.util.ArrayList;
import java.util.List;
import org.opengis.annotation.UML;
import org.opengis.util.CodeList;

/**
 * Class of information to which the referencing entity applies.
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "CI_OnLineFunctionCode", specification = ISO_19115)
public final class OnLineFunction extends CodeList<OnLineFunction> {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = 2333803519583053407L;

    /** List of all enumerations of this type. Must be declared before any enum declaration. */
    private static final List<OnLineFunction> VALUES = new ArrayList<OnLineFunction>(5);

    /** Online instructions for transferring data from one storage device or system to another. */
    @UML(identifier = "download", obligation = CONDITIONAL, specification = ISO_19115)
    public static final OnLineFunction DOWNLOAD = new OnLineFunction("DOWNLOAD");

    /** Online information about the resource. */
    @UML(identifier = "information", obligation = CONDITIONAL, specification = ISO_19115)
    public static final OnLineFunction INFORMATION = new OnLineFunction("INFORMATION");

    /** Online instructions for requesting the resource from the provider. */
    @UML(identifier = "offlineAccess", obligation = CONDITIONAL, specification = ISO_19115)
    public static final OnLineFunction OFFLINE_ACCESS = new OnLineFunction("OFFLINE_ACCESS");

    /** Online order process for obtaining the resource. */
    @UML(identifier = "order", obligation = CONDITIONAL, specification = ISO_19115)
    public static final OnLineFunction ORDER = new OnLineFunction("ORDER");

    /** Online search interface for seeking out information about the resource. */
    @UML(identifier = "search", obligation = CONDITIONAL, specification = ISO_19115)
    public static final OnLineFunction SEARCH = new OnLineFunction("SEARCH");

    /**
     * Constructs an enum with the given name. The new enum is automatically added to the list
     * returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private OnLineFunction(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code OnLineFunction}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static OnLineFunction[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new OnLineFunction[VALUES.size()]);
        }
    }

    /** Returns the list of enumerations of the same kind than this enum. */
    public OnLineFunction[] family() {
        return values();
    }

    /**
     * Returns the on line function that matches the given string, or returns a new one if none
     * match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static OnLineFunction valueOf(String code) {
        return valueOf(OnLineFunction.class, code);
    }
}
