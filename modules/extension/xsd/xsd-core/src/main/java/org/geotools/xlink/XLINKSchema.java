/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xlink;

import java.util.Collections;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;

public class XLINKSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="_show"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="new"/&gt;
     *          &lt;enumeration value="replace"/&gt;
     *          &lt;enumeration value="embed"/&gt;
     *          &lt;enumeration value="other"/&gt;
     *          &lt;enumeration value="none"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType _SHOW_TYPE = 
        new AttributeTypeImpl(
            new NameImpl("http://www.w3.org/1999/xlink","_show"), java.lang.Object.class, false,
            false,Collections.EMPTY_LIST,XSSchema.STRING_TYPE, null
        );

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="_actuate"&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="onLoad"/&gt;
     *          &lt;enumeration value="onRequest"/&gt;
     *          &lt;enumeration value="other"/&gt;
     *          &lt;enumeration value="none"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType _ACTUATE_TYPE = 
        new AttributeTypeImpl(
            new NameImpl("http://www.w3.org/1999/xlink","_actuate"), java.lang.Object.class, false,
            false,Collections.EMPTY_LIST,XSSchema.STRING_TYPE, null
        );


    public XLINKSchema() {
        super("http://www.w3.org/1999/xlink");
        
        put(new NameImpl("http://www.w3.org/1999/xlink","_show"),_SHOW_TYPE);
        put(new NameImpl("http://www.w3.org/1999/xlink","_actuate"),_ACTUATE_TYPE);
    }
}