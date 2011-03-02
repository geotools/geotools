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
package org.geotools.gml3.bindings.ext;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class SurfaceTypeBinding extends AbstractComplexBinding {

    GeometryFactory gf;
    
    public SurfaceTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.SurfaceType;
    }
    
    @Override
    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return MultiPolygon.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Polygon[] patches = (Polygon[])node.getChildValue(Polygon[].class);
        return gf.createMultiPolygon(patches);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("patches".equals(name.getLocalPart())) {
            MultiPolygon multiSurface = (MultiPolygon) object;
            Polygon[] members = new Polygon[multiSurface.getNumGeometries()];

            for (int i = 0; i < members.length; i++) {
                members[i] = (Polygon) multiSurface.getGeometryN(i);
            }

            return members;
        }

        return null;
    }
}
