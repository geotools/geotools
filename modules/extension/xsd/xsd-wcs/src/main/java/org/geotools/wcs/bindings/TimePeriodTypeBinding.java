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

package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml3.GML;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/wcs:TimePeriodType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name=&quot;TimePeriodType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;This is a variation of the GML TimePeriod, which allows the beginning and end of a time-period to be expressed in short-form inline using the begin/endPosition element, which allows an identifiable TimeInstant to be defined simultaneously with using it, or by reference, using xlinks on the begin/end elements. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name=&quot;beginPosition&quot; type=&quot;gml:TimePositionType&quot;/&gt;
 *          &lt;element name=&quot;endPosition&quot; type=&quot;gml:TimePositionType&quot;/&gt;
 *          &lt;element minOccurs=&quot;0&quot; name=&quot;timeResolution&quot; type=&quot;gml:TimeDurationType&quot;/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute default=&quot;#ISO-8601&quot; name=&quot;frame&quot; type=&quot;anyURI&quot; use=&quot;optional&quot;/&gt;
 *  &lt;/complexType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class TimePeriodTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.TimePeriodType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Period.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        Instant begining = new DefaultInstant((Position) node.getChild("beginPosition").getValue());
        Instant ending = new DefaultInstant((Position) node.getChild("endPosition").getValue());

        Period timePeriod = new DefaultPeriod(begining, ending);

        return timePeriod;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.xsd.AbstractComplexBinding#encode(java.lang.Object,
     *      org.w3c.dom.Document, org.w3c.dom.Element)
     */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Period timePeriod = (Period) object;

        if (timePeriod == null) {
            value.appendChild(document.createElementNS(GML.NAMESPACE, GML.Null.getLocalPart()));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        Period timePeriod = (Period) object;

        if (timePeriod == null) {
            return null;
        }

        if (name.getLocalPart().equals("beginPosition")) {
            return timePeriod.getBeginning().getPosition();
        }

        if (name.getLocalPart().equals("endPosition")) {
            return timePeriod.getEnding().getPosition();
        }

        return null;
    }
}
