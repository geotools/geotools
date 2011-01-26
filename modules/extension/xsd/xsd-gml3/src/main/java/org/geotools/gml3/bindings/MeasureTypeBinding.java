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

import java.net.URI;

import javax.measure.unit.BaseUnit;
import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.measure.Measure;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Binding object for the type http://www.opengis.net/gml:MeasureType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="MeasureType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Number with a scale.
 *        The value of uom (Units Of Measure) attribute is a reference to a Reference System for the amount, either a ratio or position scale. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="double"&gt;
 *              &lt;attribute name="uom" type="anyURI" use="required"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
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
public class MeasureTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MeasureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Measure.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Double d = Double.valueOf(node.getComponent().getText());
        URI uom = (URI) node.getAttributeValue(URI.class);

        if (uom != null) {
            return new Measure(d.doubleValue(), new BaseUnit(uom.toString()) {
                });
        }

        return new Measure(d.doubleValue(), null);
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        Measure measure = (Measure) object;
        value.appendChild(document.createTextNode("" + measure.doubleValue()));

        return value;
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("uom".equals(name.getLocalPart())) {
            Measure measure = (Measure) object;

            if (measure.getUnit() != null) {
                return new URI(((BaseUnit) measure.getUnit()).getSymbol());
            }
        }

        return null;
    }
}
