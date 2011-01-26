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

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Binding object for the type http://www.opengis.net/gml/3.2:MultiGeometryType.
 * 
 * <p>
 * 
 * <pre>
 *  &lt;code&gt;
 *  &lt;complexType name=&quot;MultiGeometryType&quot;&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometricAggregateType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot; ref=&quot;gml:geometryMember&quot;/&gt;
 *                  &lt;element minOccurs=&quot;0&quot; ref=&quot;gml:geometryMembers&quot;/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 *   &lt;/code&gt;
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL$
 */
public class MultiGeometryTypeBinding extends AbstractComplexBinding {

    GeometryFactory factory;
    
    public MultiGeometryTypeBinding( GeometryFactory factory ) {
        this.factory = factory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MultiGeometryType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return GeometryCollection.class;
    }

    public int getExecutionMode() {
        return BEFORE;
    }
    
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {

        ArrayList geometries = new ArrayList();

        if (node.hasChild(Geometry.class)) {
            geometries.addAll(node.getChildValues(Geometry.class));
        }

        if (node.hasChild(Geometry[].class)) {
            Geometry[] g = (Geometry[]) node.getChildValue(Geometry[].class);

            for (int i = 0; i < g.length; i++)
                geometries.add(g[i]);
        }

        return factory.createGeometryCollection((Geometry[]) geometries.toArray(new Geometry[geometries.size()]));
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (GML.geometryMember.equals(name)) {
            GeometryCollection multiGeometry = (GeometryCollection) object;
            Geometry[] members = new Geometry[multiGeometry.getNumGeometries()];

            for (int i = 0; i < members.length; i++) {
                members[i] = (Geometry) multiGeometry.getGeometryN(i);
            }

            return members;
        }

        return null;
    }

}
