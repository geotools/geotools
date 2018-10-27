/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.geotools.geopkg.TileMatrix;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gpkg:gridtype.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="gridtype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element name="zoomlevel" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element name="tilewidth" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="tileheight" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="matrixwidth" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="matrixheight" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="pixelxsize" type="xs:decimal"/&gt;
 *        &lt;xs:element name="pixelysize" type="xs:decimal"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class GridtypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GPKG.gridtype;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TileMatrix.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        int zoomlevel = (Integer) node.getChildValue("zoomlevel");
        int tilewidth = ((BigInteger) node.getChildValue("tilewidth")).intValue();
        int tilheight = ((BigInteger) node.getChildValue("tileheight")).intValue();
        int matrixwidth = ((BigInteger) node.getChildValue("matrixwidth")).intValue();
        int matrixheight = ((BigInteger) node.getChildValue("matrixheight")).intValue();
        double xpixelsize = ((BigDecimal) node.getChildValue("pixelxsize")).doubleValue();
        double ypixelsize = ((BigDecimal) node.getChildValue("pixelysize")).doubleValue();

        return new TileMatrix(
                zoomlevel, matrixwidth, matrixheight, tilewidth, tilheight, xpixelsize, ypixelsize);
    }
}
