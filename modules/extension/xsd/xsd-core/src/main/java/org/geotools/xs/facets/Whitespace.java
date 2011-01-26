/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xs.facets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Captures the whitespace facet.
 * <p>
 * Constants and utility method for old fashion facet goodness. See BooleanXOHandler for an example.
 * <p>
 * Here is an example use:<pre><code>
 * &lt;simpleType name='token'&gt;
 *   &lt;restriction base='normalizedString'&gt;
 *     &lt;whiteSpace value='collapse'/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </code></pre>
 * @see a longing for Java 5 Enum construct
 * @see <a href="http://www.w3.org/TR/xmlschema-2/#rf-whiteSpace">This web page</a>
 * @author jgarnett
 * @since 1.0.0
 *
 * @source $URL$
 */
public abstract class Whitespace implements Comparable {
    /**
     * No normalization is done, the value is not changed
     * <p>
     * Simon the spec says: <i> No normalization is done, the value is not changed (this is the
     * behavior required by [XML 1.0 (Second Edition)] for element content) </i>
     * </p>
     */
    public static final Whitespace PRESERVE = new Whitespace("preserve", 0) {
            public String preparse(String text) {
                return text;
            }
        };

    /**
     * All occurrences of tab, line feed and carriage return are replaced with space.
     * <p>
     * Simon the spec says: <i> All occurrences of #x9 (tab), #xA (line feed) and #xD (carriage
     * return) are replaced with #x20 (space) </i>
     * </p>
     */
    public static final Whitespace REPLACE = new Whitespace("replace", 1) {
            public String preparse(String text) {
                StringBuffer replace = new StringBuffer(text);

                for (int i = 0; i < replace.length(); i++) {
                    char ch = replace.charAt(i);

                    if (('\t' == ch) || ('\n' == ch) || ('\r' == ch)) {
                        replace.setCharAt(i, ' ');
                    }
                }

                return replace.toString();
            }
        };

    /**
     * All occurrences of tab, line feed and carriage return are replaced with space.
     * <p>
     * Simon the spec says: <i> All occurrences of #x9 (tab), #xA (line feed) and #xD (carriage
     * return) are replaced with #x20 (space) </i>
     * </p>
     */
    public static final Whitespace COLLAPSE = new Whitespace("collapse", 2) {
            public String preparse(String text) {
                text = REPLACE.preparse(text);
                text = text.trim();

                StringBuffer collapse = new StringBuffer(text);
                int i = 0;

                for (; i < collapse.length(); i++) {
                    if (' ' == collapse.charAt(i)) {
                        for (++i; (i < collapse.length()) && (' ' == collapse.charAt(i));) {
                            collapse.deleteCharAt(i);
                        }
                    }
                }

                return collapse.toString();
            }
        };

    //
    // Fake the ENUM thing for the Java 14 crowd
    //
    private static List values = new ArrayList();

    static {
        values.add(PRESERVE);
        values.add(REPLACE);
        values.add(COLLAPSE);
    }

    private int ordinal;
    private String name;

    private Whitespace(String name, int number) {
        this.ordinal = number;
        this.name = name;
    }

    /** Handle whitespace */
    public abstract String preparse(String text);

    public String name() {
        return name;
    }

    public int ordinal() {
        return ordinal;
    }

    public int hashCode() {
        return ordinal;
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Ha Ha");
    }

    public boolean equals(Object other) {
        return (other != null) && other instanceof Whitespace
        && (((Whitespace) other).ordinal == ordinal);
    }

    public int compareTo(Object other) {
        if ((other == null) || !(other instanceof Whitespace)) {
            return -1;
        }

        int ord = ((Whitespace) other).ordinal;

        if (ordinal == ord) {
            return 0;
        }

        if (ordinal < ord) {
            return -1;
        }

        return 1;
    }

    /**
     * Returns the enum constant of the specified enum type with the specified name. The name must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.)
     * @param whitespace
     */
    public static Whitespace valueOf(String whitespace) {
        for (Iterator i = values.iterator(); i.hasNext();) {
            Whitespace item = (Whitespace) i.next();

            if (whitespace.equals(item.name)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Returns the Class object corresponding to this enum constant's enum type.
     *
     * @return Whitespace.class
     */
    public Class getDeclaringClass() {
        return Whitespace.class;
    }

    public static List values() {
        return values;
    }
}
