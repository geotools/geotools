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
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:PolygonType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="PolygonType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:GeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;group ref="kml:geometryElements"/&gt;
 *                  &lt;element minOccurs="0" name="outerBoundaryIs" type="kml:boundaryType"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0"
 *                      name="innerBoundaryIs" type="kml:boundaryType"/&gt;
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
 *
 * @source $URL$
 */
public class PolygonTypeBinding extends AbstractComplexBinding {
    GeometryFactory geometryFactory;

    public PolygonTypeBinding(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.PolygonType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Polygon.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        LinearRing outer = (LinearRing) node.getChildValue("outerBoundaryIs");
        LinearRing[] inner = null;

        if (node.hasChild("innerBoundaryIs")) {
            List l = node.getChildValues("innerBoundaryIs");
            inner = (LinearRing[]) l.toArray(new LinearRing[l.size()]);
        }

        return geometryFactory.createPolygon(outer, inner);
    }
    
    public Object getProperty(Object object, QName name) throws Exception {
        Polygon p = (Polygon) object;
        if ( "outerBoundaryIs".equals( name.getLocalPart() ) ) {
            return p.getExteriorRing();
        }
        else if ( "innerBoundaryIs".equals( name.getLocalPart() ) ) {
            if ( p.getNumInteriorRing() > 0 ) {
                LinearRing[] interior = new LinearRing[p.getNumInteriorRing()];
                for ( int i = 0; i < interior.length; i++ ) {
                    interior[i] = (LinearRing) p.getInteriorRingN(i);
                }
                
                return interior;
            }
        }
        
        return null;
    }
}
