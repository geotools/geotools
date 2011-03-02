/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.Circle;


/**
 *
 * @author Erik van de Pol. B3Partners BV.
 */
public class ArcTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;
    CoordinateSequenceFactory csFactory;
    ArcParameters arcParameters;

    public ArcTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory, ArcParameters arcParameters) {
        this.gFactory = gFactory;
        this.csFactory = csFactory;
        this.arcParameters = arcParameters;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.ArcType;
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

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {

        LineString arcLineString = GML3ParsingUtils.lineString(node, gFactory, csFactory);
        
        Coordinate[] arcCoordinates = arcLineString.getCoordinates();
        if (arcCoordinates.length != 3) {
            // maybe log this instead and return null
            throw new RuntimeException(
                    "GML3 parser exception: The number of coordinates of an Arc should be 3. It currently is: " + arcCoordinates.length + "; " + arcLineString);
        }

        Coordinate c1 = arcCoordinates[0];
        Coordinate c2 = arcCoordinates[1];
        Coordinate c3 = arcCoordinates[2];

        // determine whether we need to reverse our input.
        boolean mustReverse = laidOutClockwise(c1, c2, c3);

        if (mustReverse) {
            // swap coords 1 and 3
            Coordinate cTemp = c1;
            c1 = c3;
            c3 = cTemp;
        }

        Circle circle = new Circle(c1, c2, c3);
        double tolerance = arcParameters.getLinearizationTolerance().getTolerance(circle);
        Coordinate[] resultCoordinates = circle.linearizeArc(c1, c2, c3, tolerance);

        if (mustReverse) {
            // reverse back
            List<Coordinate> reversingCoordinates = Arrays.asList(resultCoordinates);
            Collections.reverse(reversingCoordinates);
            resultCoordinates = (Coordinate[])
                    reversingCoordinates.toArray(new Coordinate[reversingCoordinates.size()]);
        }

        LineString resultLineString = gFactory.createLineString(resultCoordinates);

        return resultLineString;
    }

    /**
     * Returns whether the input coordinates are laid out clockwise on their corresponding circle.
     * Only works correctly if the Euclidean distance between c1 and c2 is equal to the Euclidean distance between c2 and c3.
     * @param c1
     * @param c2
     * @param c3
     * @return true if input coordinates are laid out clockwise on their corresponding circle. false otherwise.
     */
    protected boolean laidOutClockwise(Coordinate c1, Coordinate c2, Coordinate c3) {
        double x1 = c1.x;
        double y1 = c1.y;
        double x2 = c2.x;
        double y2 = c2.y;
        double x3 = c3.x;
        double y3 = c3.y;
        
        double midY = y1 - (y1 - y3) / 2;
        
        return  (x1 < x3 && midY < y2) ||
                (x1 > x3 && midY > y2) ||
                (Double.compare(x1, x3) == 0 && (
                    (y1 < y3 && x1 > x2) || // x1 == x3 == midX in this case and the case below
                    (y1 > y3 && x1 < x2)
                    // Double.compare(y1, y3) == 0 degenerate case omitted
                ));
    }

}
