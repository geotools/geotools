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
package org.geotools.data.wms.xml;

import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.ComplexTypeGT;
public class ogcComplexType extends ComplexTypeGT {
    public ogcComplexType( String name, ElementGrouping child, Attribute[] attrs, Element[] elems,
            Type parent, boolean _abstract, boolean mixed ) {
        super(null, name, OGCSchema.NAMESPACE, child, attrs, elems, mixed, parent, _abstract,
                false, null);
    }
    public ogcComplexType( String name, ElementGrouping child, Attribute[] attrs, Element[] elems ) {
        super(null, name, OGCSchema.NAMESPACE, child, attrs, elems, false, null, false, false, null);
    }
}
