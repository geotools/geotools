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

import java.util.List;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.*;
import org.geotools.xs.bindings.XSQNameBinding;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:ReturnFeatureTypesListType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:simpleType name="ReturnFeatureTypesListType"&gt;
 *      &lt;xsd:list itemType="xsd:QName"/&gt;
 *  &lt;/xsd:simpleType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class ReturnFeatureTypesListTypeBinding extends AbstractSimpleBinding {

    NamespaceContext namespaceContext;
    
    public ReturnFeatureTypesListTypeBinding(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.ReturnFeatureTypesListType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return List.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }
    
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return super.parse(instance, value);
    }
    
    @Override
    public String encode(Object object, String value) throws Exception {
        StringBuffer sb = new StringBuffer();
        List l = (List) object;
        if (l != null && !l.isEmpty()) {
            for (Object o : l) {
                sb.append(new XSQNameBinding(namespaceContext).encode(o, null)).append(" ");
            }
            sb.setLength(sb.length()-1);
        }
        
        return sb.toString();
    }

}