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

import java.util.Iterator;

import javax.xml.namespace.QName;

import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.geotools.filter.v1_0.capabilities.OGC;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.impl.ElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;


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
public class InsertResultTypeBinding extends AbstractComplexEMFBinding {
    WfsFactory wfsfactory;
    
    public InsertResultTypeBinding(WfsFactory factory) {
        super(factory);
        this.wfsfactory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.InsertResultType;
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
    
    public Object parse(ElementInstance instance, Node node, Object value){
        
        InsertResultsType resultType = wfsfactory.createInsertResultsType();
        
        for (Object featureid : node.getChildValues("FeatureId")){            
            if (! "none".equals(featureid.toString())) {
                InsertedFeatureType feature = wfsfactory.createInsertedFeatureType();                
                feature.getFeatureId().add(featureid);
                resultType.getFeature().add(feature);
            }            
        }
        
        return resultType;
    }
        
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Element e = super.encode(object, document, value);
        
        InsertResultsType resultType = (InsertResultsType) object;
        
        Iterator it = resultType.getFeature().iterator();
        while (it.hasNext()) {
              Iterator fidit = ((InsertedFeatureType)it.next()).getFeatureId().iterator();
              while (fidit.hasNext()) {
                 Element node = document.createElementNS(OGC.NAMESPACE, "FeatureId");
                 node.setAttribute("fid", fidit.next().toString());
                 e.appendChild(node);
              }
        }
        
        return e;
    }

}
