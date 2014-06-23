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

import java.util.Iterator;

import javax.xml.namespace.QName;

import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.common.util.EList;
import org.geotools.wfs.v1_1.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/wfs:InsertResultsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="InsertResultsType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation xml:lang="en"&gt;
 *              Reports the list of identifiers of all features created
 *              by a transaction request.  New features are created using
 *              the Insert action and the list of idetifiers must be
 *              presented in the same order as the Insert actions were
 *              encountered in the transaction request.  Features may
 *              optionally be correlated with identifiers using the
 *              handle attribute (if it was specified on the Insert
 *              element).
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="Feature" type="wfs:InsertedFeatureType"/&gt;
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
public class InsertResultsTypeBinding extends AbstractComplexEMFBinding {
    public InsertResultsTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.InsertResultsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return InsertResultsType.class;
    }
    
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception{
        
        InsertResultsType resultType = (InsertResultsType) super.parse(instance, node, value);
        
        //remove 'none'
        Iterator it = resultType.getFeature().iterator();
        while (it.hasNext()) {
              EList fids = ((InsertedFeatureType)it.next()).getFeatureId();
              if (fids.size() == 1 && "none".equals(fids.get(0).toString())){
                  it.remove();
              }
        }
        
        return resultType;
    }

}
