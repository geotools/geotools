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

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;


/**
 * Binding object for the type http://www.opengis.net/gml:CurveArrayPropertyType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="CurveArrayPropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A container for an array of curves. The elements are always contained in the array property, referencing geometry elements
 *                          or arrays of geometry elements is not supported.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:_Curve"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class CurveArrayPropertyTypeBinding extends AbstractComplexBinding {
    
    protected GeometryFactory gf;
    
    public CurveArrayPropertyTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.CurveArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        //return Curve[].class;
        return MultiLineString[].class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List curves = node.getChildValues(MultiLineString.class);
        
        //pick up any regular line strings as well
        for (LineString l : (List<LineString>) node.getChildValues(LineString.class)) {
            curves.add(gf.createMultiLineString(new LineString[]{l}));
        }
        return curves.toArray(new MultiLineString[curves.size()]);
    }
}
