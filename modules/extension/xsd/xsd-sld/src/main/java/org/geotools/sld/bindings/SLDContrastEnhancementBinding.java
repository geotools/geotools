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

import org.opengis.filter.FilterFactory;
import org.picocontainer.MutablePicoContainer;
import javax.xml.namespace.QName;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the element http://www.opengis.net/sld:ContrastEnhancement.
 *
 * <p>
 *        <pre>
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
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class SLDContrastEnhancementBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;
    FilterFactory filterFactory;

    public SLDContrastEnhancementBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.CONTRASTENHANCEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ContrastEnhancement.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        ContrastEnhancement ce = styleFactory.createContrastEnhancement();

        if (node.getChildValue("GammaValue") != null) {
            Double gamma = (Double) node.getChildValue("GammaValue");
            ce.setGammaValue(filterFactory.literal(gamma.doubleValue()));
        }

        if (node.getChild("Normalize") != null) {
            ce.setNormalize();
        } else {
            if (node.getChild("Histogram") != null) {
                ce.setHistogram();
            }
        }

        return ce;
    }
}
