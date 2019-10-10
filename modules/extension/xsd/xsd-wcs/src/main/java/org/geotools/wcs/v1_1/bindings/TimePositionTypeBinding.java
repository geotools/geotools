/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
 *    (c) 2001 - 2013 OpenPlans
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
package org.geotools.wcs.v1_1.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml3.v3_2.GML;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.temporal.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:TimePositionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType final=&quot;#all&quot; name=&quot;TimePositionType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *        Indeterminate time values are also allowed, as described in ISO 19108. The indeterminatePosition
 *        attribute can be used alone or it can qualify a specific value for temporal position (e.g. before
 *        2002-12, after 1019624400). For time values that identify position within a calendar, the
 *        calendarEraName attribute provides the name of the calendar era to which the date is
 *        referenced (e.g. the Meiji era of the Japanese calendar).
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base=&quot;gml:TemporalPositionType&quot;&gt;
 *              &lt;attribute name=&quot;calendarEraName&quot; type=&quot;string&quot; use=&quot;optional&quot;/&gt;
 *              &lt;attribute default=&quot;#ISO-8601&quot; name=&quot;frame&quot; type=&quot;anyURI&quot; use=&quot;optional&quot;/&gt;
 *              &lt;attribute name=&quot;indeterminatePosition&quot;
 *                  type=&quot;gml:TimeIndeterminateValueType&quot; use=&quot;optional&quot;/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class TimePositionTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.TimePositionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Position.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Position timePosition = new DefaultPosition(new SimpleInternationalString((String) value));
        return timePosition;
    }

    /*
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Position timePosition = (Position) object;

        if (timePosition == null) {
            value.appendChild(
                    document.createElementNS(
                            GML.NAMESPACE, org.geotools.gml3.GML.Null.getLocalPart()));
        } else {
            value.appendChild(document.createTextNode(timePosition.getDateTime().toString()));
        }
        return null;
    }

    public Object getProperty(Object object, QName name) {
        if (name.getLocalPart().equals("frame")) {
            return "ISO-8601";
        }

        if (name.getLocalPart().equals("calendarEraName")) {
            return null;
        }

        if (name.getLocalPart().equals("indeterminatePosition")) {
            return null;
        }

        return null;
    }
}
