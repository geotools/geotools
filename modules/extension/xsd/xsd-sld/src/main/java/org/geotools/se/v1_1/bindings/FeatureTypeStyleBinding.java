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
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:FeatureTypeStyle.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="FeatureTypeStyle" type="se:FeatureTypeStyleType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *        A FeatureTypeStyle contains styling information specific to one
 *        feature type.
 *      &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="FeatureTypeStyleType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:Name"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:FeatureTypeName"/&gt;
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
public class FeatureTypeStyleBinding extends SLDFeatureTypeStyleBinding {

    public FeatureTypeStyleBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.FeatureTypeStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        FeatureTypeStyle fts = (FeatureTypeStyle) super.parse(instance, node, value);
        
        if (node.hasChild("Description")) {
            Description d = (Description) node.getChildValue("Description");
            fts.getDescription().setTitle(d.getTitle());
            fts.getDescription().setAbstract(d.getAbstract());
        }
        
        //TODO: version
        if (node.hasChild("version")) {
        }
        
        return fts;
    }

}