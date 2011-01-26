/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.util.Collections;

import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.xlink.XLINK;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * This is just a compilation of constants used in app-schema module. 
 * @author Rini Angreani, CSIRO Earth Science and Resource Engineering
 */
public class ComplexFeatureConstants {
    /**
     * Static attribute name used to link different feature types.
     */
    public static final Name FEATURE_CHAINING_LINK_NAME = new NameImpl("FEATURE_LINK");

    /**
     * Static attribute descriptor used to link different feature types. This attribute won't appear
     * in the output document as it doesn't exist in the schema. Specifying the index would allow
     * more than one instances to be used in one type that can be chained by different parent
     * feature types.
     */
    public static final PropertyDescriptor FEATURE_CHAINING_LINK = new AttributeDescriptorImpl(
            XSSchema.STRING_TYPE, FEATURE_CHAINING_LINK_NAME, 0, -1, true, null);

    /**
     * Name representation of xlink:href
     */
    public static final Name XLINK_HREF_NAME = Types.toTypeName(XLINK.HREF);
    
    /**
     * Definition of xs:anyType as a complex type to get their client properties encoded.
     */
    @SuppressWarnings("unchecked")
    public static final ComplexType ANYTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
            "http://www.w3.org/2001/XMLSchema", "anyType"), null, false,
        true, Collections.EMPTY_LIST, null, null);      

    /**
     * Hints key for xlink:href used in ToXlinkHrefFunction
     */
    public static final Hints.Key STRING_KEY = new Hints.Key(String.class);

    /**
     * User data key to indicate the specified attribute index in the mapping file for the case of
     * multi-valued properties, e.g. gml:name[2]
     */
    public static final String MAPPED_ATTRIBUTE_INDEX = "MAPPED_ATTRIBUTE_INDEX";
}
