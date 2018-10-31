package org.geotools.wmts.bindings;

import java.math.BigInteger;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.DimensionNameValueType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.GetTileType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:GetTile.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="GetTile" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;sequence&gt;
 *  				&lt;element name="Layer" type="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;A layer identifier has to be referenced&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="Style" type="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;A style identifier has to be referenced.&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="Format" type="ows:MimeType"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Output format of the tile&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element maxOccurs="unbounded" minOccurs="0" ref="wmts:DimensionNameValue"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Dimension name and value&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="TileMatrixSet" type="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;A TileMatrixSet identifier has to be referenced&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="TileMatrix" type="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;A TileMatrix identifier has to be referenced&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="TileRow" type="nonNegativeInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Row index of tile matrix&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="TileCol" type="nonNegativeInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Column index of tile matrix&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  			&lt;/sequence&gt;
 *  			&lt;attribute fixed="WMTS" name="service" type="string" use="required"/&gt;
 *  			&lt;attribute fixed="1.0.0" name="version" type="string" use="required"/&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class GetTileBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public GetTileBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.GetTile;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GetTileType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        GetTileType getTile = factory.createGetTileType();

        getTile.setLayer((String) node.getChildValue("Layer"));
        getTile.setStyle((String) node.getChildValue("Style"));
        getTile.setFormat((String) node.getChildValue("Format"));
        getTile.setService((String) node.getChildValue("service"));
        getTile.setVersion((String) node.getChildValue("version"));
        getTile.setTileCol((BigInteger) node.getChildValue("TileCol"));
        getTile.setTileRow((BigInteger) node.getChildValue("TileRow"));
        getTile.setTileMatrix((String) node.getChildValue("TileMatrix"));
        getTile.setTileMatrixSet((String) node.getChildValue("TileMatrixSet"));
        List<Node> children = node.getChildren(DimensionType.class);
        for (Node c : children) {
            getTile.getDimensionNameValue().add((DimensionNameValueType) c.getValue());
        }

        return getTile;
    }
}
