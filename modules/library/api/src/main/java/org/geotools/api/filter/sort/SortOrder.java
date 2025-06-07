/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.filter.sort;

import static org.geotools.api.annotation.Obligation.CONDITIONAL;
import static org.geotools.api.annotation.Specification.OGC_02059;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.annotation.UML;
import org.geotools.api.util.CodeList;

/**
 * Captures the {@link SortBy} order, {@code ASC} or {@code DESC}.
 *
 * @see <a href="http://schemas.opengis.net/filter/1.1.0/sort.xsd">
 * @author Jody Garnett (Refractions Research)
 * @since GeoAPI 2.1
 */
public final class SortOrder extends CodeList<SortOrder> {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = 7840334200112859571L;

    /** The list of enumeration available in this virtual machine. <strong>Must be declared first!</strong>. */
    private static final List<SortOrder> VALUES = new ArrayList<>(2);

    /**
     * Represents acending order.
     *
     * <p>Note this has the string representation of {@code "ASC"} to agree with the Filter 1.1 specification.
     */
    @UML(identifier = "ASC", obligation = CONDITIONAL, specification = OGC_02059)
    public static final SortOrder ASCENDING = new SortOrder("ASCENDING", "ASC");

    /**
     * Represents descending order.
     *
     * <p>Note this has the string representation of {@code "DESC"} to agree with the Filter 1.1 specification.
     */
    @UML(identifier = "DESC", obligation = CONDITIONAL, specification = OGC_02059)
    public static final SortOrder DESCENDING = new SortOrder("DESCENDING", "DESC");

    /** The SQL keyword for this sorting order. */
    private final String sqlKeyword;

    /**
     * Constructs an enum with the given name. The new enum is automatically added to the list returned by
     * {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     * @param sqlKeyword The SQL keyword for this sorting order.
     */
    private SortOrder(final String name, final String sqlKeyword) {
        super(name, VALUES);
        this.sqlKeyword = sqlKeyword;
    }

    /**
     * Returns the element name for this sorting order as a SQL {@code "ASC"} or {@code "DESC"} keyword.
     *
     * <p>We have chosen to use the full names {@link #ASCENDING} and {@link #DESCENDING} for our code list. The
     * original XML schema matches the SQL convention of {@code ASC} and {@code DESC}.
     */
    public String toSQL() {
        return sqlKeyword;
    }

    /**
     * Returns the list of {@code SortOrder}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static SortOrder[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new SortOrder[VALUES.size()]);
        }
    }

    /** Returns the list of enumerations of the same kind than this enum. */
    @Override
    public SortOrder[] family() {
        return values();
    }

    /**
     * Returns the sort order that matches the given string, or returns a new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static SortOrder valueOf(String code) {
        return valueOf(SortOrder.class, code);
    }
}
