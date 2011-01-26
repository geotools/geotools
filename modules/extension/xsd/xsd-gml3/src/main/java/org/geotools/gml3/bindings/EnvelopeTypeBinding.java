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

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;


/**
 * Binding object for the type http://www.opengis.net/gml:EnvelopeType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="EnvelopeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions. The first direct
 *                          position is the "lower corner" (a coordinate position consisting of all the minimal ordinates for each dimension for all points within the envelope),
 *                          the second one the "upper corner" (a coordinate position consisting of all the maximal ordinates for each dimension for all points within the
 *                          envelope).&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;choice&gt;
 *          &lt;sequence&gt;
 *              &lt;element name="lowerCorner" type="gml:DirectPositionType"/&gt;
 *              &lt;element name="upperCorner" type="gml:DirectPositionType"/&gt;
 *          &lt;/sequence&gt;
 *          &lt;element maxOccurs="2" minOccurs="2" ref="gml:coord"&gt;
 *              &lt;annotation&gt;
 *                  &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
 *                  &lt;documentation&gt;deprecated with GML version 3.0&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element maxOccurs="2" minOccurs="2" ref="gml:pos"&gt;
 *              &lt;annotation&gt;
 *                  &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
 *                  &lt;documentation&gt;Deprecated with GML version 3.1. Use the explicit properties "lowerCorner" and "upperCorner" instead.&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element ref="gml:coordinates"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Deprecated with GML version 3.1.0. Use the explicit properties "lowerCorner" and "upperCorner" instead.&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/choice&gt;
 *      &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
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
public class EnvelopeTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.EnvelopeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Envelope.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        CoordinateReferenceSystem crs = GML3ParsingUtils.crs(node);

        if (node.getChild("lowerCorner") != null) {
            DirectPosition l = (DirectPosition) node.getChildValue("lowerCorner");
            DirectPosition u = (DirectPosition) node.getChildValue("upperCorner");

            return new ReferencedEnvelope(l.getOrdinate(0), u.getOrdinate(0), l.getOrdinate(1),
                u.getOrdinate(1), crs);
        }

        if (node.hasChild(Coordinate.class)) {
            List c = node.getChildValues(Coordinate.class);
            Coordinate c1 = (Coordinate) c.get(0);
            Coordinate c2 = (Coordinate) c.get(1);

            return new ReferencedEnvelope(c1.x, c2.x, c1.y, c2.y, crs);
        }

        if (node.hasChild(DirectPosition.class)) {
            List dp = node.getChildValues(DirectPosition.class);
            DirectPosition dp1 = (DirectPosition) dp.get(0);
            DirectPosition dp2 = (DirectPosition) dp.get(1);

            return new ReferencedEnvelope(dp1.getOrdinate(0), dp2.getOrdinate(0),
                dp1.getOrdinate(1), dp2.getOrdinate(1), crs);
        }

        if (node.hasChild(CoordinateSequence.class)) {
            CoordinateSequence seq = (CoordinateSequence) node.getChildValue(CoordinateSequence.class);

            return new ReferencedEnvelope(seq.getX(0), seq.getX(1), seq.getY(0), seq.getY(1), crs);
        }

        return null;
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        Envelope envelope = (Envelope) object;

        if (envelope.isNull()) {
            value.appendChild(document.createElementNS(GML.NAMESPACE, GML.Null.getLocalPart()));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        Envelope envelope = (Envelope) object;

        if (envelope.isNull()) {
            return null;
        }

        if (name.getLocalPart().equals("lowerCorner")) {
            return new DirectPosition2D(envelope.getMinX(), envelope.getMinY());
        }

        if (name.getLocalPart().equals("upperCorner")) {
            return new DirectPosition2D(envelope.getMaxX(), envelope.getMaxY());
        }

        if (envelope instanceof ReferencedEnvelope) {
            String localName = name.getLocalPart();
            if (localName.equals("srsName")) {
                return GML3EncodingUtils.toURI(((ReferencedEnvelope) envelope)
                        .getCoordinateReferenceSystem());
            } else if (localName.equals("srsDimension")) {
                CoordinateReferenceSystem crs = ((ReferencedEnvelope) envelope)
                        .getCoordinateReferenceSystem();
                if (crs != null) {
                    return crs.getCoordinateSystem().getDimension();
                }
            }
        }

        return null;
    }
}
