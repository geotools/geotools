package org.geotools.wmts.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:Style.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="Style" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:DescriptionType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element ref="ows:Identifier"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									An unambiguous reference to this style, identifying
 *  									a specific version when needed, normally used by software
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" minOccurs="0" ref="wmts:LegendURL"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Description of an image that represents
 *  								the legend of the map&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  					&lt;/sequence&gt;
 *  					&lt;attribute name="isDefault" type="boolean"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;This style is used when no style is specified&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
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
public class StyleBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public StyleBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.Style;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return StyleType.class;
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
        StyleType style = factory.createStyleType();

        style.setIdentifier((CodeType) node.getChildValue("Identifier"));
        Object def = node.getAttributeValue("isDefault");
        if (def != null) {
            style.setIsDefault((boolean) def);
        } else {
            style.setIsDefault(false);
        }

        List<Node> children = node.getChildren("LegendURL");
        for (Node c : children) {
            style.getLegendURL().add((LegendURLType) c.getValue());
        }
        List<Node> children2 = node.getChildren("Keywords");
        for (Node c : children2) {
            style.getKeywords().add(c.getValue());
        }
        List<Node> children3 = node.getChildren("title");
        for (Node c : children3) {
            style.getTitle().add(c.getValue());
        }
        List<Node> children4 = node.getChildren("Abstract");
        for (Node c : children4) {
            style.getAbstract().add(c.getValue());
        }

        return style;
    }
}
