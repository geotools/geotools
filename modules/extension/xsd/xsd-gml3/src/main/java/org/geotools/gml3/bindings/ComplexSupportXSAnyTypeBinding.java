/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeImpl;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.util.Converters;
import org.geotools.xlink.XLINK;
import org.geotools.xml.Schemas;
import org.geotools.xs.XS;
import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.identity.Identifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * A replacement for {@link XSAnyTypeBinding} that adds support for {@link ComplexAttribute} and
 * related behaviours.
 * 
 * <p>
 * 
 * This binding that searches the substitution group of XSD element children to find properties of a
 * complex attribute. This is necessary to support the GML property type pattern, in which a
 * property (a property-type type) contains a property that is a member of a substitution group.
 * gml:AttributeType is the canonical example of the property type pattern.
 * 
 * <p>
 * 
 * gml:FeaturePropertyType is an example of the property type pattern that has an explicit binding
 * {@link FeaturePropertyTypeBinding}, but because an application schema may define more property
 * types whose names are not known at compile time, a binding like
 * {@link FeaturePropertyTypeBinding} cannot be written. This class exists to handle these
 * application-schema-defined property types.
 * 
 * <p>
 * 
 * This class supports the encoding of XML complexType with simpleContent through extraction of a
 * simpleContent property, as well as encoding XML attributes stored in the UserData map.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 *
 * @source $URL$
 */
public class ComplexSupportXSAnyTypeBinding extends XSAnyTypeBinding {

    private XSDIdRegistry idSet;

    public ComplexSupportXSAnyTypeBinding(XSDIdRegistry idRegistry) {
        this.idSet = idRegistry;
    }

    /**
     * @see org.geotools.xml.AbstractComplexBinding#getProperty(java.lang.Object,
     *      javax.xml.namespace.QName)
     */
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (object instanceof ComplexAttribute) {
            ComplexAttribute complex = (ComplexAttribute) object;
            Property property = complex.getProperty(toTypeName(name));
            if (property != null && !(property instanceof ComplexAttribute)) {
                return property.getValue();
            }
            if ("id".equals(name.getLocalPart())) {
                return complex.getIdentifier();
            }
        }
        return null;
    }

    /**
     * Convert a {@link QName} to a {@link Name}.
     * 
     * @param name
     * @return
     */
    private static Name toTypeName(QName name) {
        if (XMLConstants.NULL_NS_URI.equals(name.getNamespaceURI())) {
            return new NameImpl(name.getLocalPart());
        } else {
            return new NameImpl(name.getNamespaceURI(), name.getLocalPart());
        }
    }

    /**
     * @see org.geotools.xml.AbstractComplexBinding#getProperties(java.lang.Object,
     *      org.eclipse.xsd.XSDElementDeclaration)
     */   
    @SuppressWarnings("unchecked")
    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        List<Object[/* 2 */]> properties = new ArrayList<Object[/* 2 */]>();
        XSDTypeDefinition typeDef = element.getTypeDefinition();
        boolean isAnyType = typeDef.getName() != null && typeDef.getTargetNamespace() != null
                && typeDef.getName().equals(XS.ANYTYPE.getLocalPart())
                && typeDef.getTargetNamespace().equals(XS.NAMESPACE);
        if (isAnyType) {
            Collection complexAtts;
            if (object instanceof Collection) {
                // collection of features
                complexAtts = (Collection) object;
            } else if (object instanceof ComplexAttribute) {
                // get collection of features from this attribute
                complexAtts = ((ComplexAttribute) object).getProperties();
            } else {
                return null;
            }
            for (Object complex : complexAtts) {
                if (complex instanceof ComplexAttribute) {
                    PropertyDescriptor descriptor = ((Attribute) complex).getDescriptor();
                    if (descriptor.getUserData() != null) {
                        Object propertyElement = descriptor.getUserData().get(
                                XSDElementDeclaration.class);
                        if (propertyElement != null
                                && propertyElement instanceof XSDElementDeclaration) {
                            XSDParticle substitutedChildParticle = XSDFactory.eINSTANCE
                                    .createXSDParticle();
                            substitutedChildParticle.setMaxOccurs(descriptor.getMaxOccurs());
                            substitutedChildParticle.setMinOccurs(descriptor.getMinOccurs());
                            XSDElementDeclaration wrapper = XSDFactory.eINSTANCE
                                    .createXSDElementDeclaration();
                            wrapper
                                    .setResolvedElementDeclaration((XSDElementDeclaration) propertyElement);
                            substitutedChildParticle.setContent(wrapper);
                            properties.add(new Object[] { substitutedChildParticle, complex });
                        }
                    }
                }
            }
            return properties;
        }
        if (object instanceof ComplexAttribute) {
            ComplexAttribute complex = (ComplexAttribute) object;
            for (XSDParticle childParticle : (List<XSDParticle>) Schemas.getChildElementParticles(
                    element.getTypeDefinition(), true)) {
                XSDElementDeclaration childElement = (XSDElementDeclaration) childParticle
                        .getContent();
                if (childElement.isElementDeclarationReference()) {
                    childElement = childElement.getResolvedElementDeclaration();
                }
                for (XSDElementDeclaration e : (List<XSDElementDeclaration>) childElement
                        .getSubstitutionGroup()) {
                    Name name = new NameImpl(e.getTargetNamespace(), e.getName());
                    Collection<Property> nameProperties = complex.getProperties(name);
                    if (!nameProperties.isEmpty()) {
                        // Particle creation stolen from BindingPropertyExtractor.
                        // I do not know why a wrapper is required; monkey see, monkey do.
                        // Without the wrapper, get an NPE in BindingPropertyExtractor.
                        XSDParticle substitutedChildParticle = XSDFactory.eINSTANCE
                                .createXSDParticle();
                        substitutedChildParticle.setMaxOccurs(childParticle.getMaxOccurs());
                        substitutedChildParticle.setMinOccurs(childParticle.getMinOccurs());
                        XSDElementDeclaration wrapper = XSDFactory.eINSTANCE
                                .createXSDElementDeclaration();
                        wrapper.setResolvedElementDeclaration(e);
                        substitutedChildParticle.setContent(wrapper);
                        for (Property property : nameProperties) {
                            /*
                             * Note : Returning simple feature value is not necessary as it has been
                             * taken care in the 1st For Loop of BindingPropertyExtractor.java -
                             * List properties(Object, XSDElementDeclaration) method
                             */
                            if (property instanceof ComplexAttribute) {
                                properties.add(new Object[] { substitutedChildParticle, property });
                            }
                        }
                    }
                }
            }
        }

        List<XSDParticle> anyElementParticles = new ArrayList<XSDParticle>(Schemas
                .getAnyElementParticles(element.getTypeDefinition()));
        if (anyElementParticles.size() > 0) {
            Collection complexAtts = null;
            if (object instanceof Collection) {
                // collection of features
                complexAtts = (Collection) object;
            } else if (object instanceof ComplexAttribute) {
                // get collection of features from this attribute
                complexAtts = ((ComplexAttribute) object).getProperties();
            }

            // If child elements can't be retrieved from
            // Schemas.getChildElementParticles(element.getTypeDefinition(), true)),
            // properties list is empty. In this case, add all the complex
            // attributes in the object to the properties list.
            if (properties.isEmpty()) {
                for (Object complex : complexAtts) {
                    // only process complex attributes
                    if (complex instanceof ComplexAttribute) {
                        ComplexAttribute newComplexAtt = (ComplexAttribute) complex;
                        PropertyDescriptor descriptor = newComplexAtt.getDescriptor();
                        if (descriptor.getUserData() != null) {
                            Object propertyElement = descriptor.getUserData().get(
                                    XSDElementDeclaration.class);
                            if (propertyElement != null
                                    && propertyElement instanceof XSDElementDeclaration) {
                                XSDParticle substitutedChildParticle = XSDFactory.eINSTANCE
                                        .createXSDParticle();
                                substitutedChildParticle.setMaxOccurs(descriptor.getMaxOccurs());
                                substitutedChildParticle.setMinOccurs(descriptor.getMinOccurs());
                                XSDElementDeclaration wrapper = XSDFactory.eINSTANCE
                                        .createXSDElementDeclaration();
                                wrapper
                                        .setResolvedElementDeclaration((XSDElementDeclaration) propertyElement);
                                substitutedChildParticle.setContent(wrapper);
                                properties.add(new Object[] { substitutedChildParticle,
                                        newComplexAtt });
                            }
                        }
                    }
                }
            }

            /*
             * properties list is not empty.
             * 
             * It's possible <any> and <element> co-exist in the same type. For example, 
             * 
             * <sequence>
             *     <any/> 
             *     <element name="..." type="..."> 
             * </sequence>
             * 
             * In this case, only add <any> complex attributes to the properties list. The following
             * code is not covered in unit test, as app-schema, doesn't support <any> as a
             * targetAttribute in a mapping file. 
             */
            else {
                List<XSDParticle> elementParticles = new ArrayList<XSDParticle>(Schemas
                        .getChildElementParticles(element.getTypeDefinition(), false));
                for (Object complex : complexAtts) {
                    if (complex instanceof ComplexAttribute) {
                        ComplexAttribute newComplexAtt = (ComplexAttribute) complex;
                        PropertyDescriptor descriptor = ((Attribute) complex).getDescriptor();
                        if (descriptor.getUserData() != null) {
                            Object propertyElement = descriptor.getUserData().get(
                                    XSDElementDeclaration.class);
                            if (propertyElement != null
                                    && propertyElement instanceof XSDElementDeclaration) {
                                XSDParticle substitutedChildParticle = XSDFactory.eINSTANCE
                                        .createXSDParticle();
                                substitutedChildParticle.setMaxOccurs(descriptor.getMaxOccurs());
                                substitutedChildParticle.setMinOccurs(descriptor.getMinOccurs());
                                XSDElementDeclaration wrapper = XSDFactory.eINSTANCE
                                        .createXSDElementDeclaration();
                                wrapper
                                        .setResolvedElementDeclaration((XSDElementDeclaration) propertyElement);
                                substitutedChildParticle.setContent(wrapper);
                                boolean propertyExist = false;
                                for (XSDParticle childParticle : elementParticles) {
                                    XSDElementDeclaration childElement = (XSDElementDeclaration) childParticle
                                            .getContent();
                                    if (childElement.isElementDeclarationReference()) {
                                        childElement = childElement.getResolvedElementDeclaration();
                                    }
                                    String existingName = childElement.getName();
                                    String newName = newComplexAtt.getDescriptor().getName()
                                            .getLocalPart();
                                    if (existingName.equals(newName)) {
                                        propertyExist = true;
                                        break;
                                    }
                                }
                                if (!propertyExist) {
                                    properties.add(new Object[] { substitutedChildParticle,
                                            newComplexAtt });
                                }
                            }
                        }
                    }
                }
            }

        }
        return properties;
    }
    
    /**
     * Check if the complex attribute contains a feature which id is pre-existing in the document.
     * If it's true, make sure it's only encoded as an xlink:href to the existing id.
     * 
     * @param value
     *            The complex attribute value
     * @param att
     *            The complex attribute itself
     */
    private void checkXlinkHref(Object value, ComplexAttribute att) {
        if (value != null && value instanceof ComplexAttribute) {
            ComplexAttribute object = (ComplexAttribute) value;
            // Only worry about features for now, as non feature types don't get ids encoded yet.
            // See GEOS-3738. To encode xlink:href to an id that doesn't exist in the doc is wrong
            if (!(object.getType() instanceof FeatureTypeImpl)) {
                // we are checking the type, not the object as FeatureImpl, because they could still
                // be non-features that are constructed as features for the purpose of feature
                // chaining.
                return;
            }
            Identifier ident = object.getIdentifier();
            if (ident == null) {
                return;
            }
            String id = Converters.convert(ident.getID(), String.class);
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
        return;
    }

    /**
     * @see org.geotools.xml.AbstractComplexBinding#encode(java.lang.Object, org.w3c.dom.Document,
     *      org.w3c.dom.Element)
     */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        if (object instanceof ComplexAttribute) {
            ComplexAttribute complex = (ComplexAttribute) object;
            if (complex.getProperties().size() == 1) {
                Property prop = complex.getProperties().iterator().next();
                checkXlinkHref(prop, complex);
            }
            GML3EncodingUtils.encodeClientProperties(complex, value);
            GML3EncodingUtils.encodeSimpleContent(complex, document, value);
        }
        return value;
    }

}
