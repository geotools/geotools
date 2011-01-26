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

import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.style.OverlapBehavior;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the element http://www.opengis.net/sld:OverlapBehavior.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="OverlapBehavior"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         &quot;OverlapBehavior&quot; tells a
 *              system how to behave when multiple         raster images in
 *              a layer overlap each other, for example with
 *              satellite-image scenes.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:choice&gt;
 *              &lt;xsd:element ref="sld:LATEST_ON_TOP"/&gt;
 *              &lt;xsd:element ref="sld:EARLIEST_ON_TOP"/&gt;
 *              &lt;xsd:element ref="sld:AVERAGE"/&gt;
 *              &lt;xsd:element ref="sld:RANDOM"/&gt;
 *          &lt;/xsd:choice&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class SLDOverlapBehaviorBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.OVERLAPBEHAVIOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return OverlapBehavior.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //&lt;xsd:element ref="sld:LATEST_ON_TOP"/&gt;
        if (node.hasChild("LATEST_ON_TOP")) {
            return OverlapBehavior.LATEST_ON_TOP;
        }

        //&lt;xsd:element ref="sld:EARLIEST_ON_TOP"/&gt;
        if (node.hasChild("EARLIEST_ON_TOP")) {
            return OverlapBehavior.EARLIEST_ON_TOP;
        }

        //&lt;xsd:element ref="sld:AVERAGE"/&gt;
        if (node.hasChild("AVERAGE")) {
            return OverlapBehavior.AVERAGE;
        }

        //&lt;xsd:element ref="sld:RANDOM"/&gt;
        if (node.hasChild("RANDOM")) {
            return OverlapBehavior.RANDOM;
        }

        return null;
    }
}
