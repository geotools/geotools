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
package org.geotools.xml.styling;

/**
 * This code generated using Refractions SchemaCodeGenerator For more information, view the attached
 * licensing information. CopyRight 105
 */
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.ComplexTypeGT;

public class sldComplexType extends ComplexTypeGT {
    public sldComplexType(
            String name,
            ElementGrouping child,
            Attribute[] attrs,
            Element[] elems,
            Type parent,
            boolean _abstract,
            boolean mixed) {
        super(
                null,
                name,
                sldSchema.NAMESPACE,
                child,
                attrs,
                elems,
                mixed,
                parent,
                _abstract,
                false,
                null);
    }

    public sldComplexType(String name, ElementGrouping child, Attribute[] attrs, Element[] elems) {
        super(
                null,
                name,
                sldSchema.NAMESPACE,
                child,
                attrs,
                elems,
                false,
                null,
                false,
                false,
                null);
    }
}
