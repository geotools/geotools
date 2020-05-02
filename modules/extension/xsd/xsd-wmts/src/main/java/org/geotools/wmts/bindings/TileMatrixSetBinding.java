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

import java.net.URI;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.ows.bindings.DescriptionTypeBinding;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:TileMatrixSet.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="TileMatrixSet" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;Describes a particular set of tile matrices.&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:DescriptionType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element ref="ows:Identifier"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Tile matrix set identifier&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" ref="ows:BoundingBox"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									Minimum bounding rectangle surrounding
 *  									the visible layer presented by this tile matrix
 *  									set, in the supported CRS &lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element ref="ows:SupportedCRS"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Reference to one coordinate reference
 *  								system (CRS).&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="WellKnownScaleSet" type="anyURI"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Reference to a well known scale set.
 *  									urn:ogc:def:wkss:OGC:1.0:GlobalCRS84Scale,
 *  									urn:ogc:def:wkss:OGC:1.0:GlobalCRS84Pixel,
 *  									urn:ogc:def:wkss:OGC:1.0:GoogleCRS84Quad and
 *  									urn:ogc:def:wkss:OGC:1.0:GoogleMapsCompatible are
 *  								possible values that are defined in Annex E. It has to be consistent with the
 *  								SupportedCRS and with the ScaleDenominators of the TileMatrix elements.
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" ref="wmts:TileMatrix"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Describes a scale level and its tile matrix.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  					&lt;/sequence&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class TileMatrixSetBinding extends DescriptionTypeBinding {

    wmtsv_1Factory wmtsv_1Factory;

    public TileMatrixSetBinding(wmtsv_1Factory factory) {
        super(Ows10Factory.eINSTANCE);
        this.wmtsv_1Factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.TileMatrixSet;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class<TileMatrixSetType> getType() {
        return TileMatrixSetType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        if (node.getChildren().isEmpty()) {
            // we are in a Contents/Layer/TileMatrixSetLink/TileMatrixSet (simple) element
            return (String) value;
        }

        // we are in a Contents/TileMatrixSet (complex) element
        if (!(value instanceof TileMatrixSetType)) {
            value = wmtsv_1Factory.createTileMatrixSetType();
        }

        // Call DescriptionType parser to load the object with the DescriptionType values
        value = super.parse(instance, node, value);

        ((TileMatrixSetType) value)
                .setBoundingBox((BoundingBoxType) node.getChildValue("BoundingBox"));
        ((TileMatrixSetType) value).setIdentifier((CodeType) node.getChildValue("Identifier"));
        ((TileMatrixSetType) value)
                .setSupportedCRS(((URI) node.getChildValue("SupportedCRS")).toString());

        URI wkss = (URI) node.getChildValue("WellKnownScaleSet");
        if (wkss != null) {
            ((TileMatrixSetType) value).setWellKnownScaleSet(wkss.toString());
        }
        ((TileMatrixSetType) value).getAbstract().addAll(node.getChildren("abstract"));
        List<Node> children = node.getChildren("TileMatrix");
        for (Node c : children) {
            ((TileMatrixSetType) value).getTileMatrix().add((TileMatrixType) c.getValue());
        }

        return value;
    }
}
