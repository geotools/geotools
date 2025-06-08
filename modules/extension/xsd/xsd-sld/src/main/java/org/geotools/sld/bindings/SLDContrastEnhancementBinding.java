/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethodStrategy;
import org.geotools.api.style.StyleFactory;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:ContrastEnhancement.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="ContrastEnhancement"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         &quot;ContrastEnhancement&quot;
 *              defines the &apos;stretching&apos; of contrast for a
 *              channel of a false-color image or for a whole grey/color
 *              image.         Contrast enhancement is used to make ground
 *              features in images         more visible.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:choice minOccurs="0"&gt;
 *                  &lt;xsd:element ref="sld:Normalize"/&gt;
 *                  &lt;xsd:element ref="sld:Histogram"/&gt;
 *              &lt;/xsd:choice&gt;
 *              &lt;xsd:element ref="sld:GammaValue" minOccurs="0"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDContrastEnhancementBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;
    FilterFactory filterFactory;

    public SLDContrastEnhancementBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.CONTRASTENHANCEMENT;
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
        return ContrastEnhancement.class;
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
        ContrastEnhancement ce = styleFactory.createContrastEnhancement();

        if (node.getChild("GammaValue") != null) {
            Expression gamma = (Expression) node.getChildValue("GammaValue");
            ce.setGammaValue(gamma);
        }

        if (node.getChild("Normalize") != null) {
            SLDNormalizeBinding binding = new SLDNormalizeBinding(styleFactory, filterFactory);
            Node child = node.getChild("Normalize");
            ce.setMethod(((ContrastMethodStrategy) binding.parse(instance, child, value)).getMethod());
        } else if (node.getChild("Histogram") != null) {
            SLDHistogramBinding binding = new SLDHistogramBinding();
            Node child = node.getChild("Histogram");
            ce.setMethod(((ContrastMethodStrategy) binding.parse(instance, child, value)).getMethod());
        }

        return ce;
    }
}
