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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.impl.AttributeImpl;

/**
 * Binding object for the type http://www.opengis.net/wfs:AllSomeType.
 * 
 * <p>
 * 
 * <pre>
 *         <code>
 *  &lt;xsd:simpleType name=&quot;AllSomeType&quot;&gt;
 *      &lt;xsd:restriction base=&quot;xsd:string&quot;&gt;
 *          &lt;xsd:enumeration value=&quot;ALL&quot;/&gt;
 *          &lt;xsd:enumeration value=&quot;SOME&quot;/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 * </code>
 *         </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class AllSomeTypeBinding extends AbstractSimpleBinding {
    private final WfsFactory factory;

    public AllSomeTypeBinding(WfsFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.AllSomeType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return AllSomeType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        AttributeImpl att = (AttributeImpl) instance;
        String text = att.getText();
        AllSomeType allSomeType = AllSomeType.get(text);
        return allSomeType;
    }
}
