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

import javax.xml.namespace.QName;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.Position1D;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.Position3D;
import org.geotools.gml.producer.CoordinateFormatter;
import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:DirectPositionType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="DirectPositionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;DirectPosition instances hold the coordinates for a position within some coordinate reference system (CRS). Since
 *                          DirectPositions, as data types, will often be included in larger objects (such as geometry elements) that have references to CRS, the
 *                          "srsName" attribute will in general be missing, if this particular DirectPosition is included in a larger element with such a reference to a
 *                          CRS. In this case, the CRS is implicitly assumed to take on the value of the containing object's CRS.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="gml:doubleList"&gt;
 *              &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class DirectPositionTypeBinding extends AbstractComplexBinding {
    GeometryFactory factory;

    CoordinateFormatter formatter;

    public DirectPositionTypeBinding(GeometryFactory factory) {
        this.factory = factory;
    }

    public DirectPositionTypeBinding(GeometryFactory factory, CoordinateFormatter formatter) {
        this.factory = factory;
        this.formatter = formatter;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return GML.DirectPositionType;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
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
        return CoordinateSequence.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        CoordinateReferenceSystem crs = GML3ParsingUtils.crs(node);

        // double[] position = (double[]) value;
        Double[] position = (Double[]) value;
        Position dp = null;

        if (position.length < 2) {
            dp = crs != null ? new Position1D(crs) : new Position1D();
            dp.setOrdinate(0, position[0].doubleValue());
        } else if (position.length < 3) {
            dp = crs != null ? new Position2D(crs) : new Position2D();
            dp.setOrdinate(0, position[0].doubleValue());
            dp.setOrdinate(1, position[1].doubleValue());
        } else {
            dp = crs != null ? new Position3D(crs) : new Position3D();
            dp.setOrdinate(0, position[0].doubleValue());
            dp.setOrdinate(1, position[1].doubleValue());
            dp.setOrdinate(2, position[2].doubleValue());
        }

        return dp;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        CoordinateSequence cs = (CoordinateSequence) object;
        boolean hasm = cs.hasM();
        StringBuffer sb = new StringBuffer();

        // assume either zero or one coordinate
        if (cs.size() >= 1) {
            int dim = cs.getDimension();
            for (int d = 0; d < dim; d++) {
                double v = cs.getOrdinate(0, d);
                // if has M coordinate and no Z(dim=3), fill empty Z coordinate with 0
                if (hasm && dim == 3 && d == 2) {
                    formatAndAppend(sb, 0);
                }
                if (Double.isNaN(v) && d > 1) {
                    continue;
                }

                formatAndAppend(sb, v);
            }
            if (dim > 0) {
                sb.setLength(sb.length() - 1);
            }
        }

        value.appendChild(document.createTextNode(sb.toString()));

        return value;
    }

    private void formatAndAppend(StringBuffer sb, double v) {
        // separator char is a blank
        if (formatter != null) {
            formatter.format(v, sb);
        } else {
            sb.append(String.valueOf(v));
        }
        sb.append(" ");
    }
}
