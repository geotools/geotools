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

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.feature.NameImpl;
import org.geotools.sld.CssParameter;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.StyleFactory;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.style.SemanticType;
import org.opengis.util.InternationalString;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:FeatureTypeStyle.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="FeatureTypeStyle"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;       A FeatureTypeStyle contains styling
 *              information specific to one       feature type.  This is the
 *              SLD level that separates the &apos;layer&apos;
 *              handling from the &apos;feature&apos; handling.     &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:FeatureTypeName" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:SemanticTypeIdentifier" minOccurs="0" maxOccurs="unbounded"/&gt;
 *              &lt;xsd:element ref="sld:Rule" maxOccurs="unbounded"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDFeatureTypeStyleBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDFeatureTypeStyleBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    public QName getTarget() {
        return SLD.FEATURETYPESTYLE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
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
    public Class getType() {
        return FeatureTypeStyle.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        FeatureTypeStyle featureTypeStyle = styleFactory.createFeatureTypeStyle();

        // &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
        if (node.hasChild("Name")) {
            featureTypeStyle.setName((String) node.getChildValue("Name"));
        }

        // &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
        if (node.hasChild("Title")) {
            featureTypeStyle
                    .getDescription()
                    .setTitle((InternationalString) node.getChildValue("Title"));
        }

        // &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
        if (node.hasChild("Abstract")) {
            featureTypeStyle
                    .getDescription()
                    .setAbstract((InternationalString) node.getChildValue("Abstract"));
        }

        // &lt;xsd:element ref="sld:FeatureTypeName" minOccurs="0"/&gt;
        if (node.hasChild("FeatureTypeName")) {
            // sld 1.0 FTN is a String, in SE 1.1 it is a QName
            Object ftn = node.getChildValue("FeatureTypeName");
            if (ftn instanceof QName) {
                QName qn = (QName) ftn;
                ftn =
                        qn.getPrefix() != null && !"".equals(qn.getPrefix().trim())
                                ? qn.getPrefix() + ":" + qn.getLocalPart()
                                : qn.getLocalPart();
            }
            featureTypeStyle.featureTypeNames().add(new NameImpl(ftn.toString()));
        }

        // &lt;xsd:element ref="sld:SemanticTypeIdentifier" minOccurs="0" maxOccurs="unbounded"/&gt;
        if (node.hasChild("SemanticTypeIdentifier")) {
            List ids = node.getChildValues("SemanticTypeIdentifier");
            ids.forEach(
                    id ->
                            featureTypeStyle
                                    .semanticTypeIdentifiers()
                                    .add(SemanticType.valueOf((String) id)));
        }

        // &lt;xsd:element ref="sld:Rule" maxOccurs="unbounded"/&gt;
        if (node.hasChild("Rule")) {
            List rules = node.getChildValues("Rule");
            featureTypeStyle.rules().clear();
            featureTypeStyle.rules().addAll(rules);
        }

        // &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
        for (CssParameter param : (List<CssParameter>) node.getChildValues(CssParameter.class)) {
            featureTypeStyle
                    .getOptions()
                    .put(param.getName(), param.getExpression().evaluate(null, String.class));
        }

        return featureTypeStyle;
    }
}
