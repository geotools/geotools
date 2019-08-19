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
package org.geotools.sld.bindings;

import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the type http://www.opengis.net/sld:SymbolizerType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="SymbolizerType" abstract="true"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;SymbolizerType&quot; is an
 *              abstract type for encoding the graphical         properties
 *              used to portray geographic information.  Concrete symbol
 *              types are derived from this base type.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDSymbolizerTypeBinding extends AbstractComplexBinding {
    /** @generated */
    public QName getTarget() {
        return SLD.SYMBOLIZERTYPE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: implement
        return null;
    }
}
