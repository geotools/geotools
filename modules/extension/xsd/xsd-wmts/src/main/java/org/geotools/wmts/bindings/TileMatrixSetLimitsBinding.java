package org.geotools.wmts.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:TileMatrixSetLimits.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="TileMatrixSetLimits" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;
 *  				Metadata about a the limits of the tile row and tile col indices.
 *  			&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;sequence&gt;
 *  				&lt;element maxOccurs="unbounded" ref="wmts:TileMatrixLimits"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;
 *  							Metadata describing the limits of the TileMatrixSet indices.
 *  							Multiplicity must be the multiplicity of TileMatrix in this
 *  							TileMatrixSet.
 *  						&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  			&lt;/sequence&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class TileMatrixSetLimitsBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public TileMatrixSetLimitsBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.TileMatrixSetLimits;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TileMatrixSetLimitsType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        TileMatrixSetLimitsType limits = factory.createTileMatrixSetLimitsType();

        @SuppressWarnings("unchecked")
        List<Node> children = node.getChildren("TileMatrixLimits");
        for (Node c : children) {
            limits.getTileMatrixLimits().add((TileMatrixLimitsType) c.getValue());
        }
        return limits;
    }
}
