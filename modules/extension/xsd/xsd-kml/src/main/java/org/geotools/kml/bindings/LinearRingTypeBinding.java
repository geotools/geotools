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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.kml.KML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.*;

/**
 * Binding object for the type http://earth.google.com/kml/2.1:LinearRingType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType final="#all" name="LinearRingType"&gt;
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
 *
 * @generated
 */
public class LinearRingTypeBinding extends AbstractComplexBinding {
    CoordinateSequenceFactory csFactory;
    GeometryFactory geometryFactory;

    public LinearRingTypeBinding(
            GeometryFactory geometryFactory, CoordinateSequenceFactory csFactory) {
        this.geometryFactory = geometryFactory;
        this.csFactory = csFactory;
    }

    /** @generated */
    public QName getTarget() {
        return KML.LinearRingType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LinearRing.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        CoordinateSequence coordinates =
                (CoordinateSequence) node.getChildValue(KML.coordinates.getLocalPart());

        // If the last point is not the same as the first point jts will throw an error
        // where as other KML readers like google earth just auto close the polygon so
        // here we manually fix it even though it's invalid so KMls that work elsewhere work here
        Coordinate firstCoord = coordinates.getCoordinate(0);
        Coordinate lastCoord = coordinates.getCoordinate(coordinates.size() - 1);

        if (!firstCoord.equals3D(lastCoord)) {
            List<Coordinate> updateCoords =
                    new ArrayList<>(Arrays.asList(coordinates.toCoordinateArray()));
            updateCoords.add((Coordinate) firstCoord.clone());

            coordinates = csFactory.create(updateCoords.toArray(new Coordinate[0]));
        }

        return geometryFactory.createLinearRing(coordinates);
    }

    public Object getProperty(Object object, QName name) throws Exception {
        if (KML.coordinates.getLocalPart().equals(name.getLocalPart())) {
            LinearRing l = (LinearRing) object;
            return l.getCoordinateSequence();
        }

        return null;
    }
}
