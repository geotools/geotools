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
package org.geotools.wfs.v1_0;

import javax.xml.namespace.QName;

import net.opengis.wfs.OperationType;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/wfs:OperationsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="OperationsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="Operation" type="wfs:OperationType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class OperationsTypeBinding extends AbstractComplexEMFBinding {
    
    private WfsFactory factory;
    
    public OperationsTypeBinding(WfsFactory factory) {
        super(factory);
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.OperationsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return OperationsType.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {                
        OperationsType om = factory.createOperationsType();        
        for (Object ob : OperationType.VALUES){
            if (node.getChild(((OperationType) ob).getName()) != null){
                om.getOperation().add(ob);
            }
        }        
        return om;        
    } 
}
