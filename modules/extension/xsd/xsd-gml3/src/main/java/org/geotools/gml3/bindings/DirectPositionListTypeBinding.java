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

import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type
 * http://www.opengis.net/gml:DirectPositionListType.
 * 
 * <p>
 * 
 * <pre>
 *         <code>
 *  &lt;complexType name=&quot;DirectPositionListType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;DirectPositionList instances hold the coordinates for a sequence of direct positions within the same coordinate
 *                          reference system (CRS).&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base=&quot;gml:doubleList&quot;&gt;
 *              &lt;attributeGroup ref=&quot;gml:SRSReferenceGroup&quot;/&gt;
 *              &lt;attribute name=&quot;count&quot; type=&quot;positiveInteger&quot; use=&quot;optional&quot;&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;&quot;count&quot; allows to specify the number of direct positions in the list. If the attribute count is present then
 *                                                  the attribute srsDimension shall be present, too.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt;
 * </code>
 *         </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL$
 */
public class DirectPositionListTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.DirectPositionListType;
    }

    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return DirectPosition[].class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        int crsDimension = 2;
        Node dimensions = (Node) node.getAttribute("srsDimension");
        if (dimensions != null) {
            crsDimension = ((Number) dimensions.getValue()).intValue();
        }
        CoordinateReferenceSystem crs = GML3ParsingUtils.crs(node);

        // double[] values = (double[]) value;
        Double[] values = (Double[]) value;
        BigInteger coordinatesCount = (BigInteger) node.getAttributeValue("count");

        if (coordinatesCount == null) {
            coordinatesCount = BigInteger.valueOf(values.length / crsDimension);
        }

        final int coordCount = coordinatesCount.intValue();
        if (coordCount == 0) {
            return new DirectPosition[] {};
        }

        int dim = values.length / coordCount;

        //if ((dim < 1) || (dim > 2)) {
        if (dim < 1) {
            throw new IllegalArgumentException("dimension must be greater or equal to 1");
        }

        DirectPosition[] dps = new DirectPosition[coordCount];

        if (dim == 1) {
            for (int i = 0; i < coordCount; i++) {
                dps[i] = new DirectPosition1D(crs);
                dps[i].setOrdinate(0, values[i].doubleValue());
            }
        } else {
            int ordinateIdx = 0;
            // HACK: not sure if its correct to assign ordinates 0 to 0 and 1 to
            // 1 or it should be inferred from the crs
            for (int coordIndex = 0; coordIndex < coordCount; coordIndex++) {
                dps[coordIndex] = new DirectPosition2D(crs);
                dps[coordIndex].setOrdinate(0, values[ordinateIdx].doubleValue());
                dps[coordIndex].setOrdinate(1, values[ordinateIdx + 1].doubleValue());
                ordinateIdx += crsDimension;
            }
        }

        return dps;
    }

    public Element encode(Object object, Document document, Element value) throws Exception {
        // TODO: remove this when the parser can do lists
        DirectPosition[] dps = (DirectPosition[]) object;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < dps.length; i++) {
            sb.append(dps[i].getOrdinate(0) + " " + dps[i].getOrdinate(1));

            if (i < (dps.length - 1)) {
                sb.append(" ");
            }
        }

        value.appendChild(document.createTextNode(sb.toString()));

        return value;
    }
}
