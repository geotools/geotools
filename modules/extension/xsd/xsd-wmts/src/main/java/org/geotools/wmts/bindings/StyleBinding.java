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

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.ows.bindings.DescriptionTypeBinding;
import org.geotools.wmts.WMTS;
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
public class StyleBinding extends DescriptionTypeBinding {

    wmtsv_1Factory wmtsv_1Factory;

    public StyleBinding(wmtsv_1Factory factory) {
        super(Ows10Factory.eINSTANCE);
        this.wmtsv_1Factory = factory;
    }

    /** @generated */
    @Override
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
    @Override
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
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        if (!(value instanceof StyleType)) {
            value = wmtsv_1Factory.createStyleType();
        }

        // Call DescriptionType parser to load the object with the DescriptionType values
        value = super.parse(instance, node, value);

        ((StyleType) value).setIdentifier((CodeType) node.getChildValue("Identifier"));
        Object def = node.getAttributeValue("isDefault");
        if (def != null) {
            ((StyleType) value).setIsDefault((boolean) def);
        } else {
            ((StyleType) value).setIsDefault(false);
        }

        List<Node> children = node.getChildren("LegendURL");
        for (Node c : children) {
            ((StyleType) value).getLegendURL().add((LegendURLType) c.getValue());
        }

        return value;
    }
}
