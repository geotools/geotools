/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import net.opengis.wfs20.ValueCollectionType;
import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.bindings.WFSParsingUtils;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:ValueCollectionType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType name="ValueCollectionType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="wfs:member"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="wfs:additionalValues"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="wfs:truncatedResponse"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attributeGroup ref="wfs:StandardResponseParameters"/&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class ValueCollectionTypeBinding extends AbstractComplexEMFBinding {

    public ValueCollectionTypeBinding(Wfs20Factory factory) {
        super(factory);
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.ValueCollectionType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return ValueCollectionType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (WFS.member.equals(name)) {
            return ((ValueCollectionType)object).getMember().iterator().next();
        }
        return null;
        //else {
        //    return WFSParsingUtils.FeatureCollectionType_getProperty((EObject)object, name);
        //}
    }

}