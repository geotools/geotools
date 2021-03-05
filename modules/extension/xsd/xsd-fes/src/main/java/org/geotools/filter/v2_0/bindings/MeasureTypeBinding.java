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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.fes20.MeasureType;
import org.geotools.filter.v1_0.DistanceUnits;
import org.geotools.filter.v2_0.FES;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;

/**
 * Binding object for the type http://www.opengis.net/fes/2.0:MeasureType.
 *
 * <p>
 *
 * <pre>
 *       <code>
 *
 *  &lt;xsd:complexType name="MeasureType"&gt;
 *      &lt;xsd:simpleContent&gt;
 *          &lt;xsd:extension base="xsd:double"&gt;
 *              &lt;xsd:attribute name="uom" type="fes:UomIdentifier" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:simpleContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *  &lt;xsd:simpleType name="UomIdentifier"&gt;
 *      &lt;xsd:union memberTypes="fes:UomSymbol fes:UomURI"/&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *  &lt;xsd:simpleType name="UomSymbol"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:pattern value="[^: \n\r\t]+"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *         </code>
 *  </pre>
 */
public class MeasureTypeBinding extends AbstractComplexBinding {

    FilterFactory filterFactory2;

    public MeasureTypeBinding(FilterFactory2 filterFactory) {
        this.filterFactory2 = filterFactory;
    }

    @Override
    public QName getTarget() {
        return FES.MeasureType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return MeasureType.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Object uom = node.getAttributeValue("uom");
        DistanceUnits distanceUnits =
                DistanceUnits.of((Double) value, uom != null ? uom.toString() : null);
        return distanceUnits;
    }
}
