package org.geotools.wmts.bindings;

import java.net.URI;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
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
public class TileMatrixSetBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public TileMatrixSetBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
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
        TileMatrixSetType matrixSet = factory.createTileMatrixSetType();
        matrixSet.setBoundingBox((BoundingBoxType) node.getChildValue("BoundingBox"));
        matrixSet.setIdentifier((CodeType) node.getChildValue("Identifier"));
        matrixSet.setSupportedCRS(((URI) node.getChildValue("SupportedCRS")).toString());

        URI wkss = (URI) node.getChildValue("WellKnownScaleSet");
        if (wkss != null) {
            matrixSet.setWellKnownScaleSet(wkss.toString());
        }
        matrixSet.getAbstract().addAll(node.getChildren("abstract"));
        List<Node> children = node.getChildren("TileMatrix");
        for (Node c : children) {
            matrixSet.getTileMatrix().add((TileMatrixType) c.getValue());
        }

        return matrixSet;
    }
}
