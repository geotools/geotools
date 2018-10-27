package org.geotools.wmts.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DatasetDescriptionSummaryBaseType;
import net.opengis.ows11.MetadataType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.LayerType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.URLTemplateType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wmts/1.0:LayerType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="LayerType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexContent&gt;
 *  			&lt;extension base="ows:DatasetDescriptionSummaryBaseType"&gt;
 *  				&lt;sequence&gt;
 *  					&lt;element maxOccurs="unbounded" ref="wmts:Style"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Metadata about the styles of this layer&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/element&gt;
 *  					&lt;element maxOccurs="unbounded" name="Format" type="ows:MimeType"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Supported valid output MIME types for a tile&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/element&gt;
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" name="InfoFormat" type="ows:MimeType"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;
 *  							Supported valid output MIME types for a FeatureInfo.
 *  							If there isn't any, The server do not support FeatureInfo requests
 *  							for this layer.&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/element&gt;
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" ref="wmts:Dimension"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Extra dimensions for a tile and FeatureInfo requests.&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/element&gt;
 *  					&lt;element maxOccurs="unbounded" ref="wmts:TileMatrixSetLink"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Reference to a tileMatrixSet and limits&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/element&gt;
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" name="ResourceURL" type="wmts:URLTemplateType"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;
 *  								URL template to a tile or a FeatureInfo resource on
 *  								resource oriented architectural style
 *  							&lt;/documentation&gt;
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
public class LayerTypeBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public LayerTypeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.LayerType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class<LayerType> getType() {
        return LayerType.class;
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
        LayerType layer = factory.createLayerType();

        List<Node> children;

        children = node.getChildren("Abstract");
        for (Node c : children) {
            layer.getAbstract().add(c.getValue());
        }
        children = node.getChildren("BoundingBox");
        for (Node c : children) {
            layer.getBoundingBox().add(c.getValue());
        }

        children = node.getChildren("WGS84BoundingBox");
        for (Node c : children) {
            layer.getWGS84BoundingBox().add(c.getValue());
        }

        layer.getDatasetDescriptionSummary()
                .addAll(node.getChildren(DatasetDescriptionSummaryBaseType.class));

        layer.getDimension().addAll(node.getChildValues(DimensionType.class));

        children = node.getChildren("Format");
        for (Node c : children) {
            layer.getFormat().add((String) c.getValue());
        }

        children = node.getChildren("InfoFormat");
        for (Node c : children) {
            layer.getInfoFormat().add((String) c.getValue());
        }

        layer.setIdentifier((CodeType) node.getChildValue("Identifier"));

        children = node.getChildren("Keyword");
        for (Node c : children) {
            layer.getKeywords().add(c.getValue());
        }
        children = node.getChildren(MetadataType.class);
        for (Node c : children) {
            layer.getMetadata().add(c.getValue());
        }
        children = node.getChildren("ResourceURL");
        for (Node c : children) {
            layer.getResourceURL().add((URLTemplateType) c.getValue());
        }
        children = node.getChildren("Style");
        for (Node c : children) {
            layer.getStyle().add((StyleType) c.getValue());
        }

        children = node.getChildren("TileMatrixSetLink");
        for (Node c : children) {
            layer.getTileMatrixSetLink().add((TileMatrixSetLinkType) c.getValue());
        }

        children = node.getChildren("Title");
        for (Node c : children) {
            layer.getTitle().add(c.getValue());
        }

        return layer;
    }
}
