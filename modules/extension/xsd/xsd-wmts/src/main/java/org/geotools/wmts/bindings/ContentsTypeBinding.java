package org.geotools.wmts.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows20.MetadataType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wmts/1.0:ContentsType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ContentsType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexContent&gt;
 *  			&lt;extension base="ows:ContentsBaseType"&gt;
 *  				&lt;sequence&gt;
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" ref="wmts:TileMatrixSet"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;A description of the geometry of a tile fragmentation&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/element&gt;
 *  				&lt;/sequence&gt;
 *  			&lt;/extension&gt;
 *  		&lt;/complexContent&gt;
 *  	&lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class ContentsTypeBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public ContentsTypeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.ContentsType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ContentsTypeBinding.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @SuppressWarnings("unchecked")
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        ContentsType contents = factory.createContentsType();

        List<Node> children = node.getChildren("Layer");
        for (Node c : children) {
            contents.getDatasetDescriptionSummary().add(c.getValue());
        }

        List<Node> children1 = node.getChildren("TileMatrixSet");
        for (Node c : children1) {
            contents.getTileMatrixSet().add((TileMatrixSetType) c.getValue());
        }

        List<Node> children2 = node.getChildren(MetadataType.class);
        for (Node c : children2) {
            contents.getOtherSource().add(c.getValue());
        }
        return contents;
    }
}
