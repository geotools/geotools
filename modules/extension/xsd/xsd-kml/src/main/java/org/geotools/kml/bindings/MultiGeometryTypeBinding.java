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
package org.geotools.kml.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:MultiGeometryType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="MultiGeometryType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:GeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" ref="kml:Geometry"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
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
public class MultiGeometryTypeBinding extends AbstractComplexBinding {
    GeometryFactory geometryFactory;

    public MultiGeometryTypeBinding(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.MultiGeometryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GeometryCollection.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List geometries = node.getChildValues(Geometry.class);

        if (geometries.isEmpty()) {
            return geometryFactory.createGeometryCollection(new Geometry[] {  });
        }

        //try to be smart about which subclass to return
        int i = 0;
        Class geometryClass = geometries.get(i++).getClass();

        for (; i < geometries.size(); i++) {
            Class clazz = geometries.get(i).getClass();

            if (!clazz.isAssignableFrom(geometryClass) && !geometryClass.isAssignableFrom(clazz)) {
                geometryClass = null;

                break;
            }
        }

        if (geometryClass != null) {
            if (geometryClass == Point.class) {
                //create a multi point
                return geometryFactory.createMultiPoint((Point[]) geometries.toArray(
                        new Point[geometries.size()]));
            }

            if ((geometryClass == LineString.class) || (geometryClass == LinearRing.class)) {
                //create a multi line string
                return geometryFactory.createMultiLineString((LineString[]) geometries.toArray(
                        new LineString[geometries.size()]));
            }

            if (geometryClass == Polygon.class) {
                //create a multi polygon
                return geometryFactory.createMultiPolygon((Polygon[]) geometries.toArray(
                        new Polygon[geometries.size()]));
            }
        }

        return geometryFactory.createGeometryCollection((Geometry[]) geometries.toArray(
                new Geometry[geometries.size()]));
    }
    
    public Object getProperty(Object object, QName name) throws Exception {
        GeometryCollection gc = (GeometryCollection) object;
        if ( KML.Geometry.equals( name ) ) {
            Geometry[] g = new Geometry[gc.getNumGeometries()];
            for ( int i = 0; i < g.length; i++ ) {
                g[i] = gc.getGeometryN(i);
            }
            return g;
        }
        
        return null;
    }
}
