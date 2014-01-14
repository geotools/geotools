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
import org.geotools.geometry.DirectPosition3D;
import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.CoordinateSequence;

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
 *
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
        return CoordinateSequence.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        int crsDimension = GML3ParsingUtils.dimensions(node);
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
        } else if(dim == 2){
            int ordinateIdx = 0;
            // HACK: not sure if its correct to assign ordinates 0 to 0 and 1 to
            // 1 or it should be inferred from the crs
            for (int coordIndex = 0; coordIndex < coordCount; coordIndex++) {
                dps[coordIndex] = new DirectPosition2D(crs);
                dps[coordIndex].setOrdinate(0, values[ordinateIdx].doubleValue());
                dps[coordIndex].setOrdinate(1, values[ordinateIdx + 1].doubleValue());
                ordinateIdx += crsDimension;
            }
        } else {
            int ordinateIdx = 0;
            // HACK: not sure if its correct to assign ordinates 0 to 0 and 1 to
            // 1 or it should be inferred from the crs
            for (int coordIndex = 0; coordIndex < coordCount; coordIndex++) {
                dps[coordIndex] = new DirectPosition3D(crs); 
                dps[coordIndex].setOrdinate(0, values[ordinateIdx].doubleValue());
                dps[coordIndex].setOrdinate(1, values[ordinateIdx + 1].doubleValue());
                dps[coordIndex].setOrdinate(2, values[ordinateIdx + 2].doubleValue());
                ordinateIdx += crsDimension;
            }

        }

        return dps;
    }

    /**
     * 
     * @param object a CoordinateSequence
     * 
     * @see org.geotools.xml.AbstractComplexBinding#encode(java.lang.Object, org.w3c.dom.Document, org.w3c.dom.Element)
     */
    public Element encode(Object object, Document document, Element value) throws Exception {
        // TODO: remove this when the parser can do lists
        CoordinateSequence cs = (CoordinateSequence) object;
        StringBuffer sb = new StringBuffer();

        int dim = CoordinateSequences.coordinateDimension(cs);
        int size = cs.size();
        int nOrdWithSpace = size * dim - 1;
        int count = 0;
        for (int i = 0; i < size; i++) {
        	for (int d = 0; d < dim; d++) {
	            sb.append(cs.getOrdinate(i, d));
	
	            if (count < nOrdWithSpace) {
	                sb.append(" ");
	            }
	            count++;

        	}
        }

        value.appendChild(document.createTextNode(sb.toString()));

        return value;
    }
      
}
