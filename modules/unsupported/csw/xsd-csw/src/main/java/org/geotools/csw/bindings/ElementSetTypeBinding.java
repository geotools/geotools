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
package org.geotools.csw.bindings;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.ElementSetType;

import org.geotools.csw.CSW;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

public class ElementSetTypeBinding extends AbstractSimpleBinding {
    public ElementSetTypeBinding() {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return CSW.ElementSetType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return ElementSetType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return ElementSetType.get((String) value);
    }

    public String encode(Object object, String value) throws Exception {
        // just return the value passed in, subclasses should override to provide new value
        return value;
    }
}
