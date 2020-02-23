/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.util;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.Types;
import org.geotools.util.factory.Hints;
import org.geotools.xlink.XLINK;
import org.geotools.xs.XSSchema;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * This is just a compilation of constants and static methods used in app-schema module.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ComplexFeatureConstants {
    /** Static attribute name used to link different feature types. */
    public static final String FEATURE_CHAINING_LINK_STRING = "FEATURE_LINK";

    public static final Name FEATURE_CHAINING_LINK_NAME =
            new NameImpl(FEATURE_CHAINING_LINK_STRING);

    /**
     * Static attribute descriptor used to link different feature types. This attribute won't appear
     * in the output document as it doesn't exist in the schema. Specifying the index would allow
     * more than one instances to be used in one type that can be chained by different parent
     * feature types.
     */
    public static final PropertyDescriptor FEATURE_CHAINING_LINK =
            new AttributeDescriptorImpl(
                    XSSchema.STRING_TYPE, FEATURE_CHAINING_LINK_NAME, 0, -1, true, null);

    /**
     * Static attribute name used for the auxiliary default geometry attribute, which is created
     * when &lt;defaultGeometry&gt; is specified in the feature type mapping configuration.
     *
     * <p>The constant represents to the local part of the qualified attribute name; the namespace
     * URI is generated at runtime and will be the same as the namespace URI of the feature
     * containing the geometry attribute.
     */
    public static final String DEFAULT_GEOMETRY_LOCAL_NAME = "__DEFAULT_GEOMETRY__";

    /** Name representation of xlink:href */
    public static final Name XLINK_HREF_NAME = Types.toTypeName(XLINK.HREF);

    /** Hints key for xlink:href used in ToXlinkHrefFunction */
    public static final Hints.Key STRING_KEY = new Hints.Key(String.class);

    /**
     * User data key to indicate the specified attribute index in the mapping file for the case of
     * multi-valued properties, e.g. gml:name[2]
     */
    public static final String MAPPED_ATTRIBUTE_INDEX = "MAPPED_ATTRIBUTE_INDEX";

    public static final String XPATH_SEPARATOR = "/";

    /**
     * Fake attribute name for simple contents of a complex type, eg. gml:name of gml:CodeType type
     */
    public static final Name SIMPLE_CONTENT = new NameImpl(null, "simpleContent");

    /** Constant to indicate the last row from denormalised rows. */
    public static final String LAST_INDEX = "LAST";

    /** Unpacks a value from an attribute container */
    public static Object unpack(Object value) {

        if (value instanceof org.opengis.feature.ComplexAttribute) {
            Property simpleContent =
                    ((org.opengis.feature.ComplexAttribute) value).getProperty(SIMPLE_CONTENT);
            if (simpleContent == null) {
                return null;
            } else {
                return simpleContent.getValue();
            }
        }

        if (value instanceof org.opengis.feature.Attribute) {
            return ((org.opengis.feature.Attribute) value).getValue();
        }

        return value;
    }
}
