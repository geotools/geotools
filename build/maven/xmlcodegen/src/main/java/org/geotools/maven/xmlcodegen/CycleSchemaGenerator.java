/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.maven.xmlcodegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AbstractLazyAttributeTypeImpl;
import org.geotools.feature.type.AbstractLazyComplexTypeImpl;
import org.geotools.util.Utilities;
import org.geotools.xml.Schemas;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Schema generator that uses subclasses of {@link AbstractLazyAttributeTypeImpl} and
 * {@link AbstractLazyComplexTypeImpl} to support cyclically-defined types. Types are sorted by
 * name.
 * 
 * <p>
 * 
 * Much of the code that builds the attributes is based on code snippets stolen from
 * {@link SchemaGenerator}, written by Justin Deoliveira.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @author Justin Deoliveira
 * 
 */
public class CycleSchemaGenerator extends SchemaGenerator {

    /**
     * The depth integer required by the parent API. This class does not use it, so we put the
     * maximum value here to defend against accidentally calling the wrong parent method.
     */
    private static final int DUMMY_DEPTH = Integer.MAX_VALUE;

    /**
     * Constructor.
     * 
     * @param schema
     *            the schema we are building
     */
    public CycleSchemaGenerator(XSDSchema schema) {
        super(schema);
    }

    /**
     * Build an AttributeType for a simple type.
     * 
     * @see org.geotools.maven.xmlcodegen.SchemaGenerator#createType(org.eclipse.xsd.XSDSimpleTypeDefinition,
     *      int)
     */
    @Override
    protected AttributeType createType(final XSDSimpleTypeDefinition xsdType, int depth) {
        if (types.containsKey(xsdType)) {
            return (AttributeType) types.get(xsdType);
        }
        // import?
        if (!xsdType.getTargetNamespace().equals(schema.getTargetNamespace())) {
            return (AttributeType) findType(xsdType);
        }
        System.err.println("Creating simple type " + name(xsdType));
        AttributeType gtType = new AbstractLazyAttributeTypeImpl(name(xsdType), Object.class,
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                XSDTypeDefinition baseType = xsdType.getBaseType();
                if (baseType != null && baseType.getName() != null && !baseType.equals(xsdType)) {
                    return createType(baseType, Integer.MAX_VALUE);
                } else {
                    return null;
                }
            }
        };
        types.put(xsdType, gtType);
        return gtType;
    }

    /**
     * Build an AttributeType for a complex type.
     * 
     * @see org.geotools.maven.xmlcodegen.SchemaGenerator#createType(org.eclipse.xsd.XSDComplexTypeDefinition,
     *      int)
     */
    @Override
    protected AttributeType createType(final XSDComplexTypeDefinition xsdType, int depth) {
        // already processed?
        if (types.containsKey(xsdType)) {
            return (AttributeType) types.get(xsdType);
        }
        // import?
        if (!xsdType.getTargetNamespace().equals(schema.getTargetNamespace())) {
            return findType(xsdType);
        }
        System.err.println("Creating complex type " + name(xsdType));
        ComplexType gtType = new AbstractLazyComplexTypeImpl(name(xsdType), false,
                xsdType.isAbstract(), null, null) {

            @Override
            public AttributeType buildSuper() {
                XSDTypeDefinition baseType = xsdType.getBaseType();
                if (baseType != null && baseType.getName() != null && !baseType.equals(xsdType)) {
                    return createType(baseType, DUMMY_DEPTH);
                } else {
                    return null;
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                if (!followComplexTypes) {
                    // might need to generate shallow schema classes while bootstrapping
                    return null;
                } else {
                    List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
                    for (XSDParticle particle : (List<XSDParticle>) Schemas
                            .getChildElementParticles(xsdType, false)) {
                        XSDElementDeclaration element = (XSDElementDeclaration) particle
                                .getContent();
                        if (element.isElementDeclarationReference()) {
                            element = element.getResolvedElementDeclaration();
                        }
                        XSDTypeDefinition childType = element.getTypeDefinition();
                        if (childType == null) {
                            childType = findGlobalElementXSDType(element);
                        }
                        AttributeType gtType = null;
                        if (childType != null) {
                            gtType = createType(childType, DUMMY_DEPTH);
                        } else {
                            // set to xs:anyType
                            gtType = xsAnyType();
                        }
                        if (gtType == null) {
                            throw new RuntimeException();
                        }
                        int minOccurs = particle.getMinOccurs();
                        int maxOccurs = particle.getMaxOccurs();
                        if (maxOccurs == -1) {
                            maxOccurs = Integer.MAX_VALUE;
                        }
                        boolean isNillable = element.isNillable();
                        // TODO: default value
                        AttributeDescriptor ad = factory.createAttributeDescriptor(gtType,
                                new NameImpl(element.getTargetNamespace(), element.getName()),
                                minOccurs, maxOccurs, isNillable, null);
                        properties.add(ad);
                    }
                    for (XSDAttributeDeclaration attribute : (List<XSDAttributeDeclaration>) Schemas
                            .getAttributeDeclarations(xsdType, false)) {
                        if (attribute.isAttributeDeclarationReference()) {
                            attribute = attribute.getResolvedAttributeDeclaration();
                        }
                        XSDSimpleTypeDefinition type = attribute.getTypeDefinition();
                        if (type == null) {
                            // look up in global schema
                            for (XSDAttributeDeclaration ad : schema.getAttributeDeclarations()) {
                                if (Utilities.equals(ad.getTargetNamespace(),
                                        attribute.getTargetNamespace())
                                        && Utilities.equals(ad.getName(), attribute.getName())) {
                                    type = ad.getTypeDefinition();
                                    break;
                                }
                            }
                        }
                        if (type == null) {
                            throw new RuntimeException(
                                    "Could not locate type definition for attribute "
                                            + name(attribute) + " of " + getName());
                        }
                        if (type.getName() == null) {
                            // TODO: deal with anonymous attribute types
                            continue;
                        }
                        AttributeType gtType = createType(type, DUMMY_DEPTH);
                        // TODO: if attribute is required
                        AttributeDescriptor ad = factory.createAttributeDescriptor(gtType,
                                name(attribute), 0, 1, true, null);
                        properties.add(ad);
                    }
                    return properties;
                }
            }

        };
        types.put(xsdType, gtType);
        return gtType;
    }

    /**
     * Return the short class name of the schema template.
     * 
     * @see org.geotools.maven.xmlcodegen.SchemaGenerator#getSchemaClassTemplateName()
     */
    @Override
    protected String getSchemaClassTemplateName() {
        return "CycleSchemaClassTemplate";
    }

    /**
     * Return a list of types sorted by name.
     * 
     * @see org.geotools.maven.xmlcodegen.SchemaGenerator#sort()
     */
    @Override
    public List<AttributeType> sort() {
        List<AttributeType> sorted = new ArrayList<AttributeType>(
                (Collection<AttributeType>) types.values());
        Collections.sort(sorted, new Comparator<AttributeType>() {
            @Override
            public int compare(AttributeType at1, AttributeType at2) {
                return ((NameImpl) at1.getName()).compareTo((NameImpl) at2.getName());
            }
        });
        return sorted;
    }

}
