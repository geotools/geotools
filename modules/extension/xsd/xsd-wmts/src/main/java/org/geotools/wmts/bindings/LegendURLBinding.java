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

import java.math.BigInteger;
import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.ows.bindings.OnlineResourceTypeBinding;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:LegendURL.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="LegendURL" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;
 *          Zero or more LegendURL elements may be provided, providing an
 *          image(s) of a legend relevant to each Style of a Layer.  The Format
 *          element indicates the MIME type of the legend. minScaleDenominator
 *          and maxScaleDenominator attributes may be provided to indicate to
 *          the client which scale(s) (inclusive) the legend image is appropriate
 *          for.  (If provided, these values must exactly match the scale
 *          denominators of available TileMatrixes.)  width and height
 *          attributes may be provided to assist client applications in laying
 *          out space to display the legend.
 *        &lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:OnlineResourceType"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;The URL from which the legend image can be retrieved&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  					&lt;attribute name="format" type="ows:MimeType"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;A supported output format for the legend image&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="minScaleDenominator" type="double"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Denominator of the minimum scale (inclusive) for which this legend image is valid&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="maxScaleDenominator" type="double"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Denominator of the maximum scale (exclusive) for which this legend image is valid&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="width" type="positiveInteger"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Width (in pixels) of the legend image&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="height" type="positiveInteger"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Height (in pixels) of the legend image&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  				&lt;/extension&gt;
 *  				&lt;!--/attributeGroup--&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *    </pre>
 *
 * @generated
 */
public class LegendURLBinding extends OnlineResourceTypeBinding {

    wmtsv_1Factory factory;

    public LegendURLBinding(wmtsv_1Factory factory) {
        super(Ows10Factory.eINSTANCE);
        this.factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WMTS.LegendURL;
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
        return LegendURLType.class;
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

        if (!(value instanceof LegendURLType)) {
            value = factory.createLegendURLType();
        }

        // Call OnlineResourceType parser to load the object with the OnlineResourceType values
        value = super.parse(instance, node, value);

        Object childValue = node.getChildValue("format");
        if (childValue != null) {
            ((LegendURLType) value).setFormat((String) node.getAttribute("format").getValue());
        }

        childValue = node.getChildValue("height");
        if (childValue != null) {
            ((LegendURLType) value).setHeight((BigInteger) node.getAttribute("height").getValue());
        }

        childValue = node.getChildValue("width");
        if (childValue != null) {
            ((LegendURLType) value).setWidth((BigInteger) node.getAttribute("width").getValue());
        }

        childValue = node.getChildValue("maxScaleDenominator");
        if (childValue != null) {
            ((LegendURLType) value).setMaxScaleDenominator(((Double) childValue).doubleValue());
        }

        childValue = node.getChildValue("minScaleDenominator");
        if (childValue != null) {
            ((LegendURLType) value).setMinScaleDenominator(((Double) childValue).doubleValue());
        }

        return value;
    }
}
