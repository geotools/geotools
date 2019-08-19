/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/gml:NameList.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;simpleType name="NameList"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML List based on XML Schema Name type.  An element of this type contains a space-separated list of Name values&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;list itemType="Name"/&gt;
 *  &lt;/simpleType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class NameListBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return GML.NameList;
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
    public Object parse(InstanceComponent instance, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
