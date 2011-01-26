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

import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.capability.Functions;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:FunctionsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="FunctionsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="Function_Names" type="ogc:Function_NamesType"/&gt;
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
public class FunctionsTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public FunctionsTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.FunctionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Functions.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        FunctionName[] functionNames = (FunctionName[]) node.getChildValue(FunctionName[].class);

        return factory.functions(functionNames);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("Function_Names".equals(name.getLocalPart())
                || "FunctionNames".equals(name.getLocalPart()) /* 1.1 */) {
            Functions functions = (Functions) object;

            return functions.getFunctionNames();
        }

        return null;
    }
}
