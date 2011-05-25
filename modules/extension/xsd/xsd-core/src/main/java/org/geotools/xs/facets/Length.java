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

import org.eclipse.xsd.XSDLengthFacet;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import java.lang.reflect.Array;
import java.util.Collection;

/**
 * length is the number of units of length, where units of length varies
 * depending on the type that is being ???derived??? from.
 * <p>
 * The value of length ???must??? be a nonNegativeInteger.
 * </p>
 * Length is defined as:
 * <ul>
 * <li>string length is measured in units of characters
 * <li>anyURI length is measured in units of characters
 * <li>hexBinary and base64Binary length is measured in octets (8bits) on
 * binary data
 * <li>list length is measured in number of list items
 * </ul>
 * This is xml so length string applies to datatypes derrived from string.
 * </p>
 * Simon the spec says: <quote>
 * For string and datatypes ???derived??? from string, length will not always
 * coincide with "string length" as perceived by some users or with the number
 * of storage units in some digital representation. Therefore, care should be
 * taken when specifying a value for length and in attempting to infer storage
 * requirements from a given value for length.
 * </quote>
 * <p>
 * Example:
 * <pre><code>
 * &lt;simpleType name='productCode'>
 *  &lt;restriction base='string'>
 *    &lt;length value='8' fixed='true'/>
 *  &lt;/restriction>
 * &lt;/simpleType>
 * </code></pre>
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public abstract class Length {
    /**
     * string and anyURI measured length is measured in units of characters
     */
    public static final Length CHARACTERS = new Length() {
            public void validate(XSDTypeDefinition definition, Object value)
                throws IllegalArgumentException {
                String text = (String) value;

                if (text.length() > length(definition)) {
                    throw new IllegalArgumentException(text);
                }
            }
        };

    /**
     * hexBinary and base64Binary length is measured in octets (8bits) on
     * binary data
     */
    public static final Length OCTETS = new Length() {
            public void validate(XSDTypeDefinition definition, Object value)
                throws IllegalArgumentException {
                String text = (String) value;

                if (text.getBytes().length > length(definition)) {
                    throw new IllegalArgumentException(text);
                }
            }
        };

    /**
     * By default this one understands Collection, Array and Integer.
     * <p>
     * So if you are checking a custom object please pass in an Integer representing
     * the size of your object.
     * </p>
     */
    public static final Length LIST = new Length() {
            public int length(XSDTypeDefinition definition) {
                try {
                    XSDSimpleTypeDefinition simple = definition
                        .getSimpleType();
                    XSDLengthFacet facet = simple.getLengthFacet();

                    if (facet == null) {
                        return Integer.MAX_VALUE;
                    }

                    return Integer.parseInt(facet.getLexicalValue());
                } catch (NumberFormatException ignore) {
                    return Integer.MIN_VALUE;
                }
            }

            public void validate(XSDTypeDefinition definition, Object value)
                throws IllegalArgumentException {
                int length = Integer.MIN_VALUE;

                if (value instanceof Collection) {
                    length = ((Collection) value).size();
                }

                if (value.getClass().isArray()) {
                    length = Array.getLength(value);
                }

                if (value instanceof Integer) {
                    length = ((Integer) value).intValue();
                }

                String text = (String) value;

                if (text.getBytes().length > length(definition)) {
                    throw new IllegalArgumentException(text);
                }
            }
        };

    private Length() {
    }

    public int length(XSDTypeDefinition definition) {
        try {
            XSDSimpleTypeDefinition simple = (XSDSimpleTypeDefinition) definition;
            XSDLengthFacet facet = simple.getLengthFacet();

            if (facet == null) {
                return Integer.MAX_VALUE;
            }

            return Integer.parseInt(facet.getLexicalValue());
        } catch (NumberFormatException ignore) {
            return Integer.MIN_VALUE;
        }
    }

    public abstract void validate(XSDTypeDefinition definition, Object value)
        throws IllegalArgumentException;
}
