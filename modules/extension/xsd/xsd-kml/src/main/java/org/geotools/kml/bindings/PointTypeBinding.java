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

import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:PointType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="PointType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:GeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;group ref="kml:geometryElements"/&gt;
 *                  &lt;element ref="kml:coordinates"/&gt;
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
public class PointTypeBinding extends AbstractComplexBinding {
    GeometryFactory geometryFactory;

    public PointTypeBinding(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.PointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Point.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        CoordinateSequence coordinates = 
            (CoordinateSequence) node.getChildValue(KML.coordinates.getLocalPart());

        return geometryFactory.createPoint(coordinates.getCoordinate(0));
    }
    
    public Object getProperty(Object object, QName name) throws Exception {
        if ( KML.coordinates.equals( name ) ) {
            Point p = (Point) object;
            return p.getCoordinateSequence();
        }
        
        return null;
    }
}
