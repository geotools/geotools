/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.coverage.grid.GridEnvelope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:GridEnvelopeType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name=&quot;GridEnvelopeType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Provides grid coordinate values for the diametrically opposed corners of an envelope that bounds a section of grid. The value of a single coordinate is the number of offsets from the origin of the grid in the direction of a specific axis.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name=&quot;low&quot; type=&quot;gml:integerList&quot;/&gt;
 *          &lt;element name=&quot;high&quot; type=&quot;gml:integerList&quot;/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class GridEnvelopeTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.GridEnvelopeType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GridEnvelope.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        if (node.getChild("low") != null) {
            int[] l = (int[]) node.getChildValue("low");
            int[] h = (int[]) node.getChildValue("high");

            GridEnvelope envelope = new GeneralGridEnvelope(l, h, true);
            return envelope;
        }

        return null;
    }

    public Element encode(Object object, Document document, Element value) throws Exception {
        GeneralEnvelope envelope = (GeneralEnvelope) object;

        if (envelope.isNull()) {
            value.appendChild(
                    document.createElementNS(
                            GML.NAMESPACE, org.geotools.gml3.GML.Null.getLocalPart()));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        GridEnvelope envelope = (GridEnvelope) object;

        if (envelope == null) {
            return null;
        }

        if (name.getLocalPart().equals("low")) {
            return envelope.getLow().getCoordinateValues();
        }

        if (name.getLocalPart().equals("high")) {
            return envelope.getHigh().getCoordinateValues();
        }

        return null;
    }
}
