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
package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Binding object for the type http://www.opengis.net/gml:NullType.
 * 
 * <p>
 * 
 * <pre>
 *  &lt;code&gt;
 *  &lt;simpleType name=&quot;NullType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *          If a bounding shape is not provided for a feature collection, 
 *          explain why. Allowable values are:
 *          innapplicable - the features do not have geometry
 *          unknown - the boundingBox cannot be computed
 *          unavailable - there may be a boundingBox but it is not divulged
 *          missing - there are no features
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;inapplicable&quot;/&gt;
 *          &lt;enumeration value=&quot;unknown&quot;/&gt;
 *          &lt;enumeration value=&quot;unavailable&quot;/&gt;
 *          &lt;enumeration value=&quot;missing&quot;/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt; 
 * 	
 *   &lt;/code&gt;
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class NullTypeBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.NullType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Envelope.class;
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
        Envelope e = new Envelope();
        e.setToNull();
        return e;
    }
    
    @Override
    public String encode(Object object, String value) throws Exception {
        return "unknown";
    }

}
