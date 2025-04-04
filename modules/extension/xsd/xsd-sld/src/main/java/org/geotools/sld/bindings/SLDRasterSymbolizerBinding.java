/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.sld.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ImageOutline;
import org.geotools.api.style.OverlapBehaviorEnum;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.StyleFactory;
import org.geotools.sld.CssParameter;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:RasterSymbolizer.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="RasterSymbolizer" substitutionGroup="sld:Symbolizer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;RasterSymbolizer&quot; is
 *              used to specify the rendering of raster/
 *              matrix-coverage data (e.g., satellite images, DEMs).       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:complexContent&gt;
 *              &lt;xsd:extension base="sld:SymbolizerType"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Opacity" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:ChannelSelection" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:OverlapBehavior" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:ColorMap" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:ContrastEnhancement" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:ShadedRelief" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:ImageOutline" minOccurs="0"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *              &lt;/xsd:extension&gt;
 *          &lt;/xsd:complexContent&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDRasterSymbolizerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDRasterSymbolizerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.RASTERSYMBOLIZER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
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
        return RasterSymbolizer.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        RasterSymbolizer rs = styleFactory.createRasterSymbolizer();

        // &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
        if (node.hasChild("Geometry")) {
            Expression geometry = (Expression) node.getChildValue("Geometry");
            if (geometry instanceof PropertyName) {
                PropertyName propertyName = (PropertyName) geometry;
                rs.setGeometryPropertyName(propertyName.getPropertyName());
            } else {
                rs.setGeometry(geometry);
            }
        }

        // &lt;xsd:element ref="sld:Opacity" minOccurs="0"/&gt;
        if (node.hasChild("Opacity")) {
            rs.setOpacity((Expression) node.getChildValue("Opacity"));
        }

        // &lt;xsd:element ref="sld:ChannelSelection" minOccurs="0"/&gt;
        if (node.hasChild("ChannelSelection")) {
            rs.setChannelSelection((ChannelSelection) node.getChildValue("ChannelSelection"));
        }

        // &lt;xsd:element ref="sld:OverlapBehavior" minOccurs="0"/&gt;
        if (node.hasChild("OverlapBehavior")) {
            rs.setOverlapBehavior((OverlapBehaviorEnum) node.getChildValue("OverlapBehavior"));
        }

        // &lt;xsd:element ref="sld:ColorMap" minOccurs="0"/&gt;
        if (node.hasChild("ColorMap")) {
            rs.setColorMap((ColorMap) node.getChildValue("ColorMap"));
        }

        // &lt;xsd:element ref="sld:ContrastEnhancement" minOccurs="0"/&gt;
        if (node.hasChild("ContrastEnhancement")) {
            rs.setContrastEnhancement((ContrastEnhancement) node.getChildValue("ContrastEnhancement"));
        }

        // &lt;xsd:element ref="sld:ShadedRelief" minOccurs="0"/&gt;
        if (node.hasChild("ShadedRelief")) {
            rs.setShadedRelief((ShadedRelief) node.getChildValue("ShadedRelief"));
        }

        // &lt;xsd:element ref="sld:ImageOutline" minOccurs="0"/&gt;
        if (node.hasChild("ImageOutline")) {
            ImageOutline imageOutput = (ImageOutline) node.getChildValue("ImageOutline");
            rs.setImageOutline(imageOutput.getSymbolizer());
        }

        // &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
        for (CssParameter param : node.getChildValues(CssParameter.class)) {
            rs.getOptions().put(param.getName(), param.getExpression().evaluate(null, String.class));
        }

        return rs;
    }
}
