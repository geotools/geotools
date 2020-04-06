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
package org.geotools.gml3.bindings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.util.Converters;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * Binding object for the type http://www.opengis.net/gml:FeaturePropertyType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="FeaturePropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Container for a feature - follow gml:AssociationType pattern.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence minOccurs="0"&gt;
 *          &lt;element ref="gml:_Feature"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 */
public class FeaturePropertyTypeBinding extends AbstractComplexBinding {
    /**
     * id set in the document, used to check against duplicate gml:id. If an gml:id is already
     * encoded for an featureMember, the next occurrence will be encoded with xlink:href
     */
    private XSDIdRegistry idSet;

    public FeaturePropertyTypeBinding(XSDIdRegistry idSet) {
        super();
        this.idSet = idSet;
    }

    /** @generated */
    public QName getTarget() {
        return GML.FeaturePropertyType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Feature.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return node.getChildValue(Feature.class);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (GML._Feature.equals(name)) {
            if (object instanceof SimpleFeature) {
                return object;
            } else if (object instanceof FeatureImpl) {
                ComplexAttribute complex = (ComplexAttribute) object;
                Identifier ident = complex.getIdentifier();
                if (ident == null) {
                    return object;
                }
                String id;
                if (ident instanceof FeatureId) {
                    id = ((FeatureId) ident).getRid();
                } else {
                    id = Converters.convert(ident.getID(), String.class);
                }
                if (idSet.idExists(id)) {
                    return null;
                }
                return object;
            } else if (object instanceof ComplexAttribute) {
                return ((ComplexAttribute) object).getProperties().iterator().next();
            }
        }
        return null;
    }

    /**
     * @see AbstractComplexBinding#encode(java.lang.Object, org.w3c.dom.Document,
     *     org.w3c.dom.Element)
     */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        if (object instanceof ComplexAttribute) {
            ComplexAttribute complex = (ComplexAttribute) object;
            checkXlinkHref(complex);
            GML3EncodingUtils.encodeClientProperties(complex, value);
            GML3EncodingUtils.encodeSimpleContent(complex, document, value);
        }
        return value;
    }

    /**
     * Check if the complex attribute contains a feature which id is pre-existing in the document.
     * If it's true, make sure it's only encoded as an xlink:href to the existing id.
     *
     * @param att The complex attribute itself
     */
    private void checkXlinkHref(ComplexAttribute att) {

        Identifier ident = att.getIdentifier();
        if (ident == null) {
            return;
        }
        String id;
        if (ident instanceof FeatureId) {
            id = ((FeatureId) ident).getRid();
        } else {
            id = Converters.convert(ident.getID(), String.class);
        }
        if (idSet.idExists(id)) {
            // XSD type ids can only appear once in the same document, otherwise the document is
            // not schema valid. Attributes of the same ids should be encoded as xlink:href to
            // the existing attribute.
            Object clientProperties = att.getUserData().get(Attributes.class);
            Map<Name, Object> map = null;
            if (clientProperties == null) {
                map = new HashMap<Name, Object>();
                att.getUserData().put(Attributes.class, map);
            } else {
                map = (Map<Name, Object>) clientProperties;
            }
            map.put(toTypeName(XLINK.HREF), "#" + id.toString());
            // make sure the value is not encoded
            att.setValue(Collections.emptyList());
        }
    }

    /** Convert a {@link QName} to a {@link Name}. */
    private static Name toTypeName(QName name) {
        if (XMLConstants.NULL_NS_URI.equals(name.getNamespaceURI())) {
            return new NameImpl(name.getLocalPart());
        } else {
            return new NameImpl(name.getNamespaceURI(), name.getLocalPart());
        }
    }
}
