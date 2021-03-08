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
import net.opengis.wmts.v_1.ThemeType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.ows.bindings.DescriptionTypeBinding;
import org.geotools.wmts.WMTS;
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
public class ThemeBinding extends DescriptionTypeBinding {

    wmtsv_1Factory factory;

    public ThemeBinding(wmtsv_1Factory factory) {
        super(Ows10Factory.eINSTANCE);
        this.factory = factory;
    }

    /** @generated */
    @Override
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
    @Override
    public Class<?> getType() {
        return ThemeType.class;
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

        if (!(value instanceof ThemeType)) {
            value = factory.createThemeType();
        }

        // Call DescriptionType parser to load the object with the DescriptionType values
        value = super.parse(instance, node, value);

        ((ThemeType) value).setIdentifier((CodeType) node.getChildValue("Identifier"));
        for (Object c : node.getChildValues("LayerRef")) {
            ((ThemeType) value).getLayerRef().add(c.toString());
        }
        @SuppressWarnings("unchecked")
        List<ThemeType> themes = node.getChildValues("Theme");
        ((ThemeType) value).getTheme().addAll(themes);

        return value;
    }
}
