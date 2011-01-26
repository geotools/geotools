/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDFeatureTypeStyleBinding;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:CoverageStyle.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="CoverageStyle" type="se:CoverageStyleType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A CoverageStyle contains styling information specific to one
 *          Coverage offering. 
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * *
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="CoverageStyleType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:Name"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:CoverageName"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="se:SemanticTypeIdentifier"/&gt;
 *          &lt;xsd:choice maxOccurs="unbounded"&gt;
 *              &lt;xsd:element ref="se:Rule"/&gt;
 *              &lt;xsd:element ref="se:OnlineResource"/&gt;
 *          &lt;/xsd:choice&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="version" type="se:VersionType"/&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class CoverageStyleBinding extends FeatureTypeStyleBinding {

    public CoverageStyleBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.CoverageStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        FeatureTypeStyle fts = (FeatureTypeStyle) super.parse(instance, node, value);

        if (node.hasChild("CoverageName")) {
            QName name = (QName) node.getChildValue("CoverageName");
            fts.setFeatureTypeName(name.getPrefix() != null ? 
                name.getPrefix()+":"+name.getLocalPart() : name.getLocalPart());
        }

        return fts;
    }

}