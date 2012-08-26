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

import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.ElementSetType;

import org.geotools.csw.CSW;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.SimpleContentComplexEMFBinding;

public class ElementSetNameTypeBinding extends SimpleContentComplexEMFBinding {
    

    public ElementSetNameTypeBinding() {
        super(Csw20Factory.eINSTANCE, CSW.ElementSetNameType);
    }

    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        
        ElementSetNameType result = (ElementSetNameType) createEObject(value);
        result.setValue(ElementSetType.get((String) value));
        Node typeNames = node.getAttribute("typeNames");
        if(typeNames != null) {
            result.setTypeNames((List<QName>) typeNames.getValue());
        }
        
        
        return result;
    }

}
