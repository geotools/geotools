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
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.capability.Functions;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Arithmetic_OperatorsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Arithmetic_OperatorsType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:Simple_Arithmetic"/&gt;
 *          &lt;xsd:element name="Functions" type="ogc:FunctionsType"/&gt;
 *      &lt;/xsd:choice&gt;
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
public class Arithmetic_OperatorsTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public Arithmetic_OperatorsTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Arithmetic_OperatorsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ArithmeticOperators.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //&lt;xsd:element ref="ogc:Simple_Arithmetic"/&gt;
        boolean simpleArithmetic = node.hasChild("Simple_Arithmetic")
            || node.hasChild("SimpleArithmetic"); //1.1

        //&lt;xsd:element name="Functions" type="ogc:FunctionsType"/&gt;
        Functions functions = (Functions) node.getChildValue(Functions.class);

        return factory.arithmeticOperators(simpleArithmetic, functions);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        ArithmeticOperators arithmetic = (ArithmeticOperators) object;

        if ((name.equals(OGC.Simple_Arithmetic)
                || name.equals(org.geotools.filter.v1_1.OGC.SimpleArithmetic))
                && arithmetic.hasSimpleArithmetic()) {
            return new Object();
        }

        if (name.getLocalPart().equals("Functions")) {
            return arithmetic.getFunctions();
        }

        return null;
    }
}
