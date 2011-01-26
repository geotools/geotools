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
package org.geotools.gml2.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * Binding object for the type http://www.opengis.net/gml:LineStringType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="LineStringType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         A LineString is defined by two or more
 *              coordinate tuples, with          linear interpolation
 *              between them.        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;element ref="gml:coord" minOccurs="2" maxOccurs="unbounded"/&gt;
 *                      &lt;element ref="gml:coordinates"/&gt;
 *                  &lt;/choice&gt;
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
public class GMLLineStringTypeBinding extends AbstractComplexBinding {
    CoordinateSequenceFactory csFactory;
    GeometryFactory gFactory;

    public GMLLineStringTypeBinding(CoordinateSequenceFactory csFactory, GeometryFactory gFactory) {
        this.csFactory = csFactory;
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.LineStringType;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LineString.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List coordinates = node.getChildren("coord");

        if (coordinates.size() == 1) {
            throw new RuntimeException("Linestring must have at least 2 coordinates");
        }

        if (!coordinates.isEmpty()) {
            Node cnode = (Node) coordinates.get(0);
            CoordinateSequence seq = (CoordinateSequence) cnode.getValue();
            int dimension = GMLUtil.getDimension(seq);

            CoordinateSequence lineSeq = csFactory.create(coordinates.size(), dimension);

            for (int i = 0; i < coordinates.size(); i++) {
                cnode = (Node) coordinates.get(i);
                seq = (CoordinateSequence) cnode.getValue();

                for (int j = 0; j < dimension; j++) {
                    lineSeq.setOrdinate(i, j, seq.getOrdinate(0, j));
                }
            }

            return gFactory.createLineString(lineSeq);
        }

        if (node.getChild("coordinates") != null) {
            Node cnode = (Node) node.getChild("coordinates");
            CoordinateSequence lineSeq = (CoordinateSequence) cnode.getValue();

            return gFactory.createLineString(lineSeq);
        }

        throw new RuntimeException("Could not find coordinates to build linestring");
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        LineString lineString = (LineString) object;

        if (GML.coordinates.equals(name)) {
            return lineString.getCoordinateSequence();
        }

        return null;
    }
}
