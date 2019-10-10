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

import java.math.BigInteger;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.gml.RectifiedGridType;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:GridType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name=&quot;GridType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Implicitly defines an unrectified grid, which is a network composed of two or more sets of equally spaced parallel lines in which the members of each set intersect the members of the other sets at right angles. This profile does not extend AbstractGeometryType, so it defines the srsName attribute.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometryType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element name=&quot;limits&quot; type=&quot;gml:GridLimitsType&quot;/&gt;
 *                  &lt;element maxOccurs=&quot;unbounded&quot; name=&quot;axisName&quot; type=&quot;string&quot;/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name=&quot;dimension&quot; type=&quot;positiveInteger&quot; use=&quot;required&quot;/&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class GridTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.RectifiedGridType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        RectifiedGridType grid = Gml4wcsFactory.eINSTANCE.createRectifiedGridType();

        if (node.hasAttribute("srsName")) {
            grid.setSrsName(node.getAttributeValue("srsName").toString());
        }

        grid.setDimension((BigInteger) node.getAttribute("dimension").getValue());

        GeneralGridEnvelope limitsEnvelope = (GeneralGridEnvelope) node.getChildValue("limits");

        //        GridLimitsType limits = Gml4wcsFactory.eINSTANCE.createGridLimitsType();
        //        GridEnvelopeType gridEnelope = Gml4wcsFactory.eINSTANCE.createGridEnvelopeType();
        //        List l = new ArrayList();
        //             l.add(limitsEnvelope.getLow(0));
        //             l.add(limitsEnvelope.getLow(1));
        //        List h = new ArrayList();
        //             h.add(limitsEnvelope.getHigh(0));
        //             h.add(limitsEnvelope.getHigh(1));

        grid.setDimension(BigInteger.valueOf(2));
        grid.setLimits(
                new GridEnvelope2D(
                        (int) limitsEnvelope.getLow(0), (int) limitsEnvelope.getLow(1),
                        (int) limitsEnvelope.getHigh(0), (int) limitsEnvelope.getHigh(1)));

        List<Node> axisNames = node.getChildren("axisName");
        if (axisNames != null && !axisNames.isEmpty()) {
            for (Node axisName : axisNames) {
                grid.getAxisName().add(axisName.getValue());
            }
        }

        return grid;
        //       return super.parse(instance, node, value);
    }
}
