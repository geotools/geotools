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

package org.geotools.wmts.bindings;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:TileMatrixLimits.
 *
 * <p>
 *
 * <pre>
 * <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="TileMatrixLimits" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;Metadata describing the limits of a TileMatrix
 *  						for this layer.&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;sequence&gt;
 *  				&lt;element name="TileMatrix" type="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Reference to a TileMatrix identifier&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="MinTileRow" type="positiveInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Minimum tile row index valid for this
 *  						layer. From 0 to maxTileRow&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="MaxTileRow" type="positiveInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Maximim tile row index valid for this
 *  						layer. From minTileRow to matrixWidth-1 of the tileMatrix
 *  						section of this tileMatrixSet&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="MinTileCol" type="positiveInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Minimum tile column index valid for this
 *  						layer. From 0 to maxTileCol&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="MaxTileCol" type="positiveInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Maximim tile column index valid for this layer.
 *  						From minTileCol to tileHeight-1 of the tileMatrix section
 *  						of this tileMatrixSet.&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  			&lt;/sequence&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 * </code>
 * </pre>
 *
 * @generated
 */
public class TileMatrixLimitsBinding extends AbstractComplexEMFBinding {
    wmtsv_1Factory factory;

    public TileMatrixLimitsBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.TileMatrixLimits;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TileMatrixLimitsType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        TileMatrixLimitsType limits = factory.createTileMatrixLimitsType();

        limits.setTileMatrix((String) node.getChildValue("TileMatrix"));

        limits.setMaxTileCol(toBigInt(node.getChildValue("MaxTileCol")));
        limits.setMaxTileRow(toBigInt(node.getChildValue("MaxTileRow")));
        limits.setMinTileCol(toBigInt(node.getChildValue("MinTileCol")));
        limits.setMinTileRow(toBigInt(node.getChildValue("MinTileRow")));

        return limits;
    }

    private static BigInteger toBigInt(Object o) {
        return BigInteger.valueOf(((Integer) o).longValue());
    }

    public Element encode(Object object, Document document, Element value) throws Exception {
        // This needs its own encode method because otherwise the EMF encoder will mistake
        // the contained wmts:TileMatrix for a TileMatrixType and try to encode it that
        // way, ending up with an empty element in the document

        Element e = super.encode(object, document, value);

        TileMatrixLimitsType matrixLimit = (TileMatrixLimitsType) object;

        if (matrixLimit.getTileMatrix() != null) {

            Element tileMatrixNode = document.createElementNS(WMTS.NAMESPACE, "TileMatrix");
            tileMatrixNode.setTextContent(matrixLimit.getTileMatrix());
            e.appendChild(tileMatrixNode);
        }

        return e;
    }
}
