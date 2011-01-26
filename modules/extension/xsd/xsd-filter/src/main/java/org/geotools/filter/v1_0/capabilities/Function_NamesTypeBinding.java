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
package org.geotools.filter.v1_0.capabilities;

import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.FunctionName;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Function_NamesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Function_NamesType"&gt;
 *      &lt;xsd:sequence maxOccurs="unbounded"&gt;
 *          &lt;xsd:element name="Function_Name" type="ogc:Function_NameType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class Function_NamesTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Function_NamesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return FunctionName[].class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List functions = node.getChildValues(FunctionName.class);

        return functions.toArray(new FunctionName[functions.size()]);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (name.getLocalPart().equals("Function_Name")
                || name.getLocalPart().equals("FunctionName") /* 1.1 */) {
            return object;
        }

        return null;
    }
}
