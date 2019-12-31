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
package org.geotools.filter.v1_0;

import javax.measure.IncommensurableException;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.xml.namespace.QName;
import org.geotools.measure.Units;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import si.uom.SI;
import tec.uom.se.quantity.Quantities;

/**
 * Binding object for the type http://www.opengis.net/ogc:DistanceType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType mixed="true" name="DistanceType"&gt;
 *      &lt;xsd:attribute name="units" type="xsd:string" use="required"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class OGCDistanceTypeBinding extends AbstractComplexBinding {
    /** @generated */
    public QName getTarget() {
        return OGC.DistanceType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Quantity.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        String units = node.getAttributeValue("units").toString();
        Unit<?> unitType = Units.parseUnit(units);
        if (!unitType.isCompatible(SI.METRE)) {
            throw new IncommensurableException(
                    "Could not parse type: " + unitType + " into SI Metres");
        }
        return Quantities.getQuantity(Double.parseDouble((String) value), unitType);
    }

    /**
     * Encodes an object representing a {@link Quantity} into a DistanceType element type. Will
     * convert the type into SI Metres to ensure maximal compatibility with the OGC spec
     * implementations.
     *
     * @throws IncommensurableException If the supplied object does not represent a valid Length
     *     unit type (such as m, mi, etc.)
     */
    public Element encode(Object object, Document document, Element value) throws Exception {
        // noinspection unchecked
        Quantity<Length> distance = (Quantity<Length>) object;
        if (!distance.getUnit().isCompatible(SI.METRE)) {
            throw new IncommensurableException(
                    "Could not parse type: " + distance.getUnit() + " into SI Metres");
        }
        Quantity<Length> distanceInM = distance.to(SI.METRE);
        value.appendChild(document.createTextNode(distanceInM.getValue().toString()));
        value.setAttribute("units", SI.METRE.getSymbol());

        return value;
    }
}
