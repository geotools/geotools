package org.geotools.wmts.bindings;

import java.net.URI;
import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.ThemeType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:Theme.
 *
 * <p>
 *
 * <pre>
 * <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="Theme" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:DescriptionType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element ref="ows:Identifier"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Name of the theme&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" minOccurs="0" ref="wmts:Theme"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									Metadata describing the child (subordinate) themes
 *  									of this theme where layers available on this server
 *  									can be classified
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" minOccurs="0" name="LayerRef" type="anyURI"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Reference to layer&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  					&lt;/sequence&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 * </code>
 * </pre>
 *
 * @generated
 */
public class ThemeBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public ThemeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.Theme;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ThemeType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        ThemeType theme = factory.createThemeType();

        theme.setIdentifier((CodeType) node.getChildValue("Identifier"));
        for (Object c : node.getChildValues("LayerRef")) {
            theme.getLayerRef().add(((URI) c).toString());
        }
        theme.getTheme().addAll(node.getChildValues("Theme"));

        return theme;
    }
}
