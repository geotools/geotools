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

import java.util.StringTokenizer;
import javax.xml.namespace.QName;
import org.geotools.geometry.jts.JTS;
import org.geotools.gml2.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:CoordinatesType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="CoordinatesType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         Coordinates can be included in a single
 *              string, but there is no          facility for validating
 *              string content. The value of the &apos;cs&apos; attribute
 *              is the separator for coordinate values, and the value of the
 *              &apos;ts&apos;          attribute gives the tuple separator
 *              (a single space by default); the          default values may
 *              be changed to reflect local usage.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="string"&gt;
 *              &lt;attribute name="decimal" type="string" use="optional" default="."/&gt;
 *              &lt;attribute name="cs" type="string" use="optional" default=","/&gt;
 *              &lt;attribute name="ts" type="string" use="optional" default=" "/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class GMLCoordinatesTypeBinding extends AbstractComplexBinding {
    CoordinateSequenceFactory csFactory;

    public GMLCoordinatesTypeBinding(CoordinateSequenceFactory csFactory) {
        this.csFactory = csFactory;
    }

    /** @generated */
    public QName getTarget() {
        return GML.CoordinatesType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return CoordinateSequence.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * Returns an object of type {@see org.locationtech.jts.geom.CoordinateSequence} TODO: this
     * method should do more validation of the string
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // get the coordinate and tuple seperators
        String decimal = ".";
        String cs = ",";
        String ts = " ";

        if (node.getAttribute("decimal") != null) {
            decimal = (String) node.getAttribute("decimal").getValue();
        }

        if (node.getAttribute("cs") != null) {
            cs = (String) node.getAttribute("cs").getValue();
        }

        if (node.getAttribute("ts") != null) {
            ts = (String) node.getAttribute("ts").getValue();
        }

        // do the parsing
        String text = instance.getText();

        // eliminate newlines, repeated spaces, etc
        final String anyBlankSeq = "\\s+";
        final String singleSpace = " ";
        text = text.replaceAll(anyBlankSeq, singleSpace);

        // first tokenize by tuple seperators
        StringTokenizer tuples = new StringTokenizer(text, ts);
        CoordinateSequence seq = null;
        int i = 0;
        int ncoords = tuples.countTokens(); // number of coordinates
        if (cs.equals(ts)) {
            ncoords = ncoords / 2;
        }

        while (tuples.hasMoreTokens()) {
            String tuple = tuples.nextToken();

            // next tokenize by coordinate seperator
            String[] oords = tuple.split(cs);

            if (cs.equals(ts) && oords.length == 1 && tuples.hasMoreTokens()) {
                String tempX = oords[0];
                oords = new String[2];
                oords[0] = tempX;
                oords[1] = tuples.nextToken();
            }

            // next tokenize by decimal
            String x = null;

            // next tokenize by decimal
            String y = null;

            // next tokenize by decimal
            String z = null;

            // must be at least 1D
            x = ".".equals(decimal) ? oords[0] : oords[0].replaceAll(decimal, ".");

            // check for 2 and 3 D
            if (oords.length > 1) {
                y = ".".equals(decimal) ? oords[1] : oords[1].replaceAll(decimal, ".");
            }

            if (oords.length > 2) {
                z = ".".equals(decimal) ? oords[2] : oords[2].replaceAll(decimal, ".");
            }

            if (seq == null) {
                seq = JTS.createCS(csFactory, ncoords, oords.length);
            }

            seq.setOrdinate(i, CoordinateSequence.X, Double.parseDouble(x));

            if (y != null) {
                seq.setOrdinate(i, CoordinateSequence.Y, Double.parseDouble(y));
            }

            if (z != null) {
                seq.setOrdinate(i, CoordinateSequence.Z, Double.parseDouble(z));
            }

            i++;
        }

        return seq;
    }

    public Element encode(Object object, Document document, Element value) throws Exception {
        CoordinateSequence coordinates = (CoordinateSequence) object;
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate c = coordinates.getCoordinate(i);
            buf.append(c.x);

            boolean y = (coordinates.getDimension() > 1) && !Double.valueOf(c.y).isNaN();

            if (y) {
                buf.append("," + c.y);
            }

            boolean z = y && (coordinates.getDimension() > 2) && !Double.valueOf(c.getZ()).isNaN();

            if (z) {
                buf.append("," + c.getZ());
            }

            if (i < (coordinates.size() - 1)) {
                buf.append(" ");
            }
        }

        value.appendChild(document.createTextNode(buf.toString()));

        return value;
    }
}
