package org.geotools.wmts.bindings;

import java.math.BigInteger;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:TileMatrix.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="TileMatrix" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;Describes a particular tile matrix.&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:DescriptionType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element ref="ows:Identifier"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Tile matrix identifier. Typically an abreviation of
 *  								the ScaleDenominator value or its equivalent pixel size&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element name="ScaleDenominator" type="double"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Scale denominator level of this tile matrix&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element name="TopLeftCorner" type="ows:PositionType"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									Position in CRS coordinates of the top-left corner of this tile matrix.
 *  									This are the  precise coordinates of the top left corner of top left
 *  									pixel of the 0,0 tile in SupportedCRS coordinates of this TileMatrixSet.
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element name="TileWidth" type="positiveInteger"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Width of each tile of this tile matrix in pixels.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element name="TileHeight" type="positiveInteger"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Height of each tile of this tile matrix in pixels&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element name="MatrixWidth" type="positiveInteger"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Width of the matrix (number of tiles in width)&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element name="MatrixHeight" type="positiveInteger"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Height of the matrix (number of tiles in height)&lt;/documentation&gt;
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
public class TileMatrixBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public TileMatrixBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.TileMatrix;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TileMatrixType.class;
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
            // we are in a
            // Contents/Layer/TileMatrixSetLink/TileMatrixSetLimits/TileMatrixLimits/TileMatrix
            // (simple) element
            return (String) value;
        }

        // we are in a Contents/TileMatrixSet/TileMatrix (complex) element
        TileMatrixType tileMatrix = factory.createTileMatrixType();
        tileMatrix.setIdentifier((CodeType) node.getChildValue("Identifier"));
        tileMatrix.setMatrixHeight((BigInteger) node.getChildValue("MatrixHeight"));
        tileMatrix.setMatrixWidth((BigInteger) node.getChildValue("MatrixWidth"));
        tileMatrix.setScaleDenominator((double) node.getChildValue("ScaleDenominator"));
        tileMatrix.setTileHeight((BigInteger) node.getChildValue("TileHeight"));
        tileMatrix.setTileWidth((BigInteger) node.getChildValue("TileWidth"));
        tileMatrix.setTopLeftCorner((List<Double>) node.getChildValue("TopLeftCorner"));
        return tileMatrix;
    }
}
