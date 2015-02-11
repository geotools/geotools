/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.geotools.filter.v1_0.OGCBBOXTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.v3_2.GML;
import org.geotools.referencing.CRS;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Binding object for the type http://www.opengis.net/ogc:BBOXType.
 *
 * <p>
 *        <pre>
 *       <code>
 *  &lt;xsd:complexType name="BBOXType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" ref="fes:expression"/&gt;
 *                  &lt;xsd:any namespace="##other"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class BBOXTypeBinding extends OGCBBOXTypeBinding {

    public QName getTarget() {
        return FES.BBOXType;
    }

    public Class getType() {
        return BBOX.class;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        BBOX box = (BBOX) object;
        
        if (FES.ValueReference.equals(name)) {
            return box.getExpression1();
        }
        
        return null;
    }
    
    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        BBOX box = (BBOX) object;
        
        List properties = new ArrayList();
        
        XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
        particle.setContent(GML.getInstance().getSchema().resolveElementDeclaration(GML.Envelope.getLocalPart()));
        particle.setMinOccurs(0);
        particle.setMaxOccurs(-1);
        
        Envelope env;
        try {
            String srs = box.getSRS();
            if(srs != null) {
                CoordinateReferenceSystem crs = CRS.decode(srs);
                env = new ReferencedEnvelope(box.getMinX(), box.getMaxX(), box.getMinY(), box.getMaxY(), crs);
            }
        } catch(Throwable t) {
            // never mind
        }
        env = new Envelope(box.getMinX(), box.getMaxX(), box.getMinY(), box.getMaxY());
        
        properties.add( new Object[] {particle, env} );       
        return properties;
    }
}
