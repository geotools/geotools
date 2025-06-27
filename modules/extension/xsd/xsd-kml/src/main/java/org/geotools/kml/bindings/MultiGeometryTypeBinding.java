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
import org.geotools.kml.KML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Binding object for the type http://earth.google.com/kml/2.1:MultiGeometryType.
 *
 * <p>
 *
 * <pre>
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
 *
 * @generated
 */
public class MultiGeometryTypeBinding extends AbstractComplexBinding {
    GeometryFactory geometryFactory;

    public MultiGeometryTypeBinding(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return KML.MultiGeometryType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return GeometryCollection.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List<Geometry> geometries = node.getChildValues(Geometry.class);

        if (geometries.isEmpty()) {
            return geometryFactory.createGeometryCollection(new Geometry[] {});
        }

        // try to be smart about which subclass to return
        Class<?> geometryClass = null;
        for (Geometry g : geometries) {
            Class<?> clazz = g.getClass();
            if (geometryClass == null) {
                geometryClass = clazz;
            } else {
                if (!clazz.isAssignableFrom(geometryClass) && !geometryClass.isAssignableFrom(clazz)) {
                    geometryClass = null;
                    break;
                }
            }
        }

        if (geometryClass != null) {
            if (geometryClass == Point.class) {
                // create a multi point
                return geometryFactory.createMultiPoint(geometries.toArray(new Point[geometries.size()]));
            }

            if (geometryClass == LineString.class || geometryClass == LinearRing.class) {
                // create a multi line string
                return geometryFactory.createMultiLineString(geometries.toArray(new LineString[geometries.size()]));
            }

            if (geometryClass == Polygon.class) {
                // create a multi polygon
                return geometryFactory.createMultiPolygon(geometries.toArray(new Polygon[geometries.size()]));
            }
        }

        return geometryFactory.createGeometryCollection(geometries.toArray(new Geometry[geometries.size()]));
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        GeometryCollection gc = (GeometryCollection) object;
        if (KML.Geometry.getLocalPart().equals(name.getLocalPart())) {
            Geometry[] g = new Geometry[gc.getNumGeometries()];
            for (int i = 0; i < g.length; i++) {
                g[i] = gc.getGeometryN(i);
            }
            return g;
        }

        return null;
    }
}
