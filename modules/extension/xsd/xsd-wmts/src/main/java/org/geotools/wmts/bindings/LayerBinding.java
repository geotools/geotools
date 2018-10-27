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
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:Layer.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *     &lt;element name="Layer" substitutionGroup="ows:DatasetDescriptionSummary" type="wmts:LayerType" xmlns="http://www.w3.org/2001/XMLSchema"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class LayerBinding extends AbstractSimpleBinding {

    wmtsv_1Factory factory;

    public LayerBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.Layer;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <b>
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
        layer.getWGS84BoundingBox().add(node.getChildValue("WGS84BoundingBox"));
        layer.getDatasetDescriptionSummary()
                .addAll(node.getChildren(DatasetDescriptionSummaryBaseType.class));
        layer.getDimension().addAll(node.getChildren(DimensionType.class));
        children = node.getChildren("Format");
        for (Node c : children) {
            layer.getFormat().add((String) c.getValue());
        }
        ;
        children = node.getChildren("InfoFormat");
        for (Node c : children) {
            layer.getInfoFormat().add((String) c.getValue());
        }
        layer.setIdentifier((CodeType) node.getChildValue("Identifier"));
        layer.getKeywords().addAll(node.getChildren("Keyword"));
        layer.getMetadata().addAll(node.getChildren(MetadataType.class));
        layer.getResourceURL().addAll(node.getChildren(URLTemplateType.class));
        children = node.getChildren("Style");
        for (Node c : children) {
            layer.getStyle().add((StyleType) c.getValue());
        }

        List<Node> children2 = node.getChildren(TileMatrixSetLinkType.class);
        for (Node c : children2) {
            layer.getTileMatrixSetLink().add((TileMatrixSetLinkType) c);
        }
        children = node.getChildren("Title");
        for (Node c : children) {
            layer.getTitle().add(c.getValue());
        }

        return layer;
    }
}
