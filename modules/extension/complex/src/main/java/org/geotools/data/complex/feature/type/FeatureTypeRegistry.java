/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.feature.type.Schema;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AbstractLazyComplexTypeImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.feature.type.Types;
import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.util.logging.Logging;
import org.geotools.xs.XSSchema;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.complex.FeatureTypeRegistryConfiguration;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A registry of GeoTools {@link AttributeType} and {@link AttributeDescriptor} lazily parsed from the EMF
 * {@link XSDTypeDefinition} and {@link XSDElementDeclaration} added through {@link #addSchemas(SchemaIndex)}.
 *
 * <p>This class is meant to be used in conjunction with {@link EmfComplexFeatureReader}.
 *
 * @author Gabriel Roldan
 * @author Niels Charlier
 */
public class FeatureTypeRegistry {

    private static final Logger LOGGER = Logging.getLogger(FeatureTypeRegistry.class);

    private List<SchemaIndex> schemas;

    private HashMap<Name, AttributeDescriptor> descriptorRegistry;

    private HashMap<Name, AttributeType> typeRegistry;

    private HashMap<Name, AttributeType> anonTypeRegistry;

    private FeatureTypeFactory typeFactory;

    private FeatureTypeRegistryConfiguration helper;

    private boolean includeAttributes;

    private static volatile AttributeType XMLATTRIBUTE_TYPE;

    /**
     * stack of currently being built type names, used by {@link #createType(Name, XSDTypeDefinition)} to prevent
     * recursive type definitions by proxy'ing a type that appears to be already being constructed and thus still not in
     * the type registry.
     */
    private Stack<Name> processingTypes;

    public FeatureTypeRegistry(FeatureTypeFactory typeFactory, FeatureTypeRegistryConfiguration helper) {
        this(null, typeFactory, helper);
    }

    public FeatureTypeRegistry(
            NamespaceSupport namespaces, FeatureTypeFactory typeFactory, FeatureTypeRegistryConfiguration helper) {
        this(namespaces, typeFactory, helper, false);
    }

    public FeatureTypeRegistry(
            NamespaceSupport namespaces,
            FeatureTypeFactory typeFactory,
            FeatureTypeRegistryConfiguration helper,
            boolean includeAttributes) {

        schemas = new ArrayList<>();
        this.typeFactory = typeFactory;
        descriptorRegistry = new HashMap<>();
        typeRegistry = new HashMap<>();
        anonTypeRegistry = new HashMap<>();
        processingTypes = new Stack<>();
        this.helper = helper;
        this.includeAttributes = includeAttributes;

        createFoundationTypes();
    }

    public void addSchemas(final SchemaIndex schemaIndex) {
        schemas.add(schemaIndex);
    }

    /**
     * Destroy all schema Indexes. VERY important to that this is called to avoid memory leaks, because schema indexes
     * are kept alive otherwise by static schema's and in this way keep other schema's alive
     */
    public void disposeSchemaIndexes() {
        for (SchemaIndex schemaIndex : schemas) {
            schemaIndex.destroy();
        }
    }

    public AttributeDescriptor getDescriptor(final Name descriptorName, CoordinateReferenceSystem crs) {
        AttributeDescriptor descriptor = descriptorRegistry.get(descriptorName);

        if (descriptor == null) {
            // top level elements
            XSDElementDeclaration elemDecl = getElementDeclaration(descriptorName);
            descriptor = createAttributeDescriptor(null, elemDecl, crs);
            LOGGER.finest("Registering attribute descriptor " + descriptor.getName());
            register(descriptor);
        }
        return descriptor;
    }

    private XSDElementDeclaration getElementDeclaration(final Name descriptorName) {

        QName qname = Types.toQName(descriptorName);
        XSDElementDeclaration elemDecl = null;
        for (SchemaIndex schemaIndex : schemas) {
            elemDecl = schemaIndex.getElementDeclaration(qname);
            if (elemDecl != null) {
                break;
            }
        }
        if (elemDecl == null) {
            String msg = "No top level element found in schemas: " + qname;
            LOGGER.log(Level.WARNING, msg);
            throw new NoSuchElementException(msg);
        }
        return elemDecl;
    }

    public AttributeType getAttributeType(final Name typeName) {
        return getAttributeType(typeName, null, null);
    }

    public AttributeType getAttributeType(
            final Name typeName, XSDTypeDefinition xsdType, CoordinateReferenceSystem crs) {
        AttributeType type = typeRegistry.get(typeName);

        if (type == null || type instanceof AbstractLazyComplexTypeImpl) {
            // recreate lazy types too
            if (xsdType == null) {
                xsdType = getTypeDefinition(typeName);
            }
            LOGGER.finest("Creating attribute type " + typeName);
            type = createType(typeName, xsdType, crs, false);
            LOGGER.finest("Registering attribute type " + typeName);
        }
        return type;
    }

    private void setSubstitutionGroup(
            XSDComplexTypeDefinition container,
            XSDElementDeclaration elemDecl,
            PropertyDescriptor descriptor,
            CoordinateReferenceSystem crs) {

        if (descriptor.getUserData().get("substitutionGroup") != null) {
            // this has been done before
            return;
        }

        List<AttributeDescriptor> substitutionGroup = new ArrayList<>();
        descriptor.getUserData().put("substitutionGroup", substitutionGroup);

        int minOccurs = Schemas.getMinOccurs(container, elemDecl);
        int maxOccurs = Schemas.getMaxOccurs(container, elemDecl);
        boolean nillable = elemDecl.isNillable();

        Iterator substitutions = elemDecl.getSubstitutionGroup().iterator();
        XSDElementDeclaration sub;
        while (substitutions.hasNext()) {
            sub = (XSDElementDeclaration) substitutions.next();
            if (!sub.getName().equals(elemDecl.getName())
                    || !sub.getTargetNamespace().equals(elemDecl.getTargetNamespace())) {
                Name elemName = Types.typeName(sub.getTargetNamespace(), sub.getName());
                AttributeType type = getTypeOf(sub, crs);
                if (type != null) {
                    substitutionGroup.add(
                            createAttributeDescriptor(type, crs, elemName, minOccurs, maxOccurs, nillable, null));
                }
            }
        }

        XSDTypeDefinition typeDef = elemDecl.getType();

        if (typeDef instanceof XSDComplexTypeDefinition) {
            Name typeName = Types.typeName(typeDef.getTargetNamespace(), typeDef.getName());
            AttributeType attType = typeRegistry.get(typeName);

            if (!processingTypes.contains(typeName)) {
                // ignore processingTypes to avoid endless recursion
                if (attType == null || attType instanceof AbstractLazyComplexTypeImpl) {
                    // type is not yet registered or it's a lazy type from foundation types
                    // recreate lazy type to ensure everything is loaded
                    // it will eventually call this method so substitution groups will be set then
                    LOGGER.finest("Creating attribute type " + typeName);
                    createType(typeName, typeDef, crs, false);
                    LOGGER.finest("Registering attribute type " + typeName);
                } else if (attType instanceof ComplexType) {
                    // ensure substitution groups are set for children including non lazy foundation
                    // types
                    ComplexType complexType = (ComplexType) attType;
                    Collection<PropertyDescriptor> children = complexType.getDescriptors();

                    List<XSDParticle> childParticles = Schemas.getChildElementParticles(typeDef, true);

                    for (XSDParticle particle : childParticles) {
                        XSDElementDeclaration element = (XSDElementDeclaration) particle.getContent();

                        if (element.isElementDeclarationReference()) {
                            element = element.getResolvedElementDeclaration();
                        }
                        PropertyDescriptor childDesc = null;
                        for (PropertyDescriptor desc : children) {
                            if (desc.getName().getLocalPart().equals(element.getName())
                                    && desc.getName().getNamespaceURI().equals(element.getTargetNamespace())) {
                                childDesc = desc;
                                break;
                            }
                        }
                        if (childDesc != null) {
                            setSubstitutionGroup((XSDComplexTypeDefinition) typeDef, element, childDesc, crs);
                        }
                    }
                }
            }
        }
    }

    private XSDTypeDefinition getTypeDefinition(Name typeName) {
        QName qName = Types.toQName(typeName);
        XSDTypeDefinition typeDefinition = null;
        for (SchemaIndex schemaIndex : schemas) {
            typeDefinition = schemaIndex.getTypeDefinition(qName);
            if (typeDefinition != null) {
                break;
            }
        }
        if (typeDefinition == null) {
            throw new IllegalArgumentException("XSD type definition not found in schemas: " + qName);
        }
        return typeDefinition;
    }

    private void register(AttributeDescriptor descriptor) {
        Name name = descriptor.getName();
        descriptorRegistry.put(name, descriptor);
    }

    private void register(AttributeType type, boolean anonymous) {
        Name name = type.getName();
        Object old;
        if (anonymous) {
            old = anonTypeRegistry.put(name, type);
        } else {
            old = typeRegistry.put(name, type);
        }
        if (old != null) {
            LOGGER.fine(type.getName() + " replaced by new value.");
        }
    }

    public void register(AttributeType type) {
        register(type, false);
    }

    private AttributeDescriptor createAttributeDescriptor(
            final XSDComplexTypeDefinition container,
            final XSDElementDeclaration elemDecl,
            CoordinateReferenceSystem crs) {
        int minOccurs = container == null ? 0 : Schemas.getMinOccurs(container, elemDecl);
        int maxOccurs = container == null ? Integer.MAX_VALUE : Schemas.getMaxOccurs(container, elemDecl);

        return createAttributeDescriptor(elemDecl, minOccurs, maxOccurs, crs);
    }

    private AttributeDescriptor createAttributeDescriptor(
            AttributeType type,
            CoordinateReferenceSystem crs,
            Name elemName,
            int minOccurs,
            int maxOccurs,
            boolean nillable,
            Object defaultValue) {
        AttributeDescriptor descriptor = null;
        if (maxOccurs == -1) {
            // this happens when maxOccurs is set to "unbounded"
            maxOccurs = Integer.MAX_VALUE;
        }

        if (!(type instanceof AttributeTypeProxy)
                && (Geometry.class.isAssignableFrom(type.getBinding())
                        || CurvedGeometry.class.isAssignableFrom(type.getBinding()))) {
            // create geometry descriptor with the simple feature type CRS
            GeometryType geomType = new GeometryTypeImpl(
                    type.getName(),
                    type.getBinding(),
                    crs,
                    type.isIdentified(),
                    type.isAbstract(),
                    type.getRestrictions(),
                    type.getSuper(),
                    type.getDescription());
            descriptor = typeFactory.createGeometryDescriptor(
                    geomType, elemName, minOccurs, maxOccurs, nillable, defaultValue);
        } else {
            descriptor =
                    typeFactory.createAttributeDescriptor(type, elemName, minOccurs, maxOccurs, nillable, defaultValue);
        }
        return descriptor;
    }

    private AttributeDescriptor createAttributeDescriptor(
            final XSDElementDeclaration elemDecl, int minOccurs, int maxOccurs, CoordinateReferenceSystem crs) {
        String targetNamespace = elemDecl.getTargetNamespace();
        String name = elemDecl.getName();
        Name elemName = Types.typeName(targetNamespace, name);
        AttributeType type = getTypeOf(elemDecl, crs);
        boolean nillable = elemDecl.isNillable();
        Object defaultValue = null;
        AttributeDescriptor descriptor =
                createAttributeDescriptor(type, crs, elemName, minOccurs, maxOccurs, nillable, defaultValue);
        descriptor.getUserData().put(XSDElementDeclaration.class, elemDecl);

        return descriptor;
    }

    /**
     * If the type of elemDecl is annonymous creates a new type with the same name than the atrribute and returns it. If
     * it is not anonymous, looks it up on the registry and in case the type does not exists in the registry uses a
     * proxy.
     */
    private AttributeType getTypeOf(XSDElementDeclaration elemDecl, CoordinateReferenceSystem crs) {

        // TODO REVISIT, I'm not sure this is the way to find out if the
        // element's type is defined in line (an thus no need to register it
        // as a global type)
        if (elemDecl.isElementDeclarationReference()) {
            elemDecl = elemDecl.getResolvedElementDeclaration();
        }
        boolean hasToBeRegistered = false;
        XSDTypeDefinition typeDefinition = elemDecl.getAnonymousTypeDefinition();
        if (typeDefinition == null) {
            // anonymous types already has type definition inline in the element
            // so the handling is different
            hasToBeRegistered = true;
            typeDefinition = elemDecl.getTypeDefinition();
        }

        if (typeDefinition == null) {
            // last resort.. look in the lazy schemas
            QName qname = Types.toQName(Types.typeName(elemDecl.getTargetNamespace(), elemDecl.getName()));
            for (SchemaIndex schemaIndex : schemas) {
                elemDecl = schemaIndex.getElementDeclaration(qname);
                if (elemDecl != null) {
                    break;
                }
            }
            if (elemDecl != null) {
                if (elemDecl.isElementDeclarationReference()) {
                    elemDecl = elemDecl.getResolvedElementDeclaration();
                }
                typeDefinition = elemDecl.getAnonymousTypeDefinition();
                if (typeDefinition == null) {
                    typeDefinition = elemDecl.getTypeDefinition();
                }
            }
        }

        if (typeDefinition == null) {
            String msg = "The element declaration "
                    + elemDecl.getTargetNamespace()
                    + "#"
                    + elemDecl.getName()
                    + " has a null type definition, can't continue, fix it on the schema";
            LOGGER.warning(msg);
            throw new NoSuchElementException(msg);
        }

        AttributeType type;
        if (hasToBeRegistered) {
            String targetNamespace = typeDefinition.getTargetNamespace();
            String name = typeDefinition.getName();
            Name typeName = Types.typeName(targetNamespace, name);
            type = getAttributeType(typeName, typeDefinition, crs);
            if (type == null) {
                type = createType(typeName, typeDefinition, crs, false);
            }
        } else {
            String name = elemDecl.getName();
            String targetNamespace = elemDecl.getTargetNamespace();
            Name overrideName = Types.typeName(targetNamespace, name);
            type = createType(overrideName, typeDefinition, crs, true);
        }

        return type;
    }

    private AttributeType createProxiedType(
            final Name assignedName, final XSDTypeDefinition typeDefinition, Map typeRegistry) {
        AttributeType type;
        if (null == typeDefinition.getSimpleType() && typeDefinition instanceof XSDComplexTypeDefinition) {
            if (helper.isFeatureType(typeDefinition)) {
                type = new FeatureTypeProxy(assignedName, typeRegistry);
            } else {
                type = new ComplexTypeProxy(assignedName, typeRegistry);
            }
        } else {
            if (helper.isGeometryType(typeDefinition)) {
                type = new GeometryTypeProxy(assignedName, typeRegistry);
            } else {
                type = new AttributeTypeProxy(assignedName, typeRegistry);
            }
        }
        return type;
    }

    /**
     * Creates an {@link AttributeType} that matches the xsd type definition as much as possible.
     *
     * <p>The original type definition given by the {@link XSDTypeDefinition} is kept as AttributeType's metadata stored
     * as a "user data" property using <code>XSDTypeDefinition.class
     * </code> as key.
     *
     * <p>If it is a complex attribute, it will contain all the properties declared in the <code>
     * typeDefinition</code>, as well as all the properties declared in its super types. TODO: handle the case where the
     * extension mechanism is restriction.
     */
    private AttributeType createType(
            final Name assignedName,
            final XSDTypeDefinition typeDefinition,
            CoordinateReferenceSystem crs,
            boolean anonymous) {

        AttributeType attType;
        // /////////
        if (processingTypes.contains(assignedName)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Recursion found for type " + assignedName + ". Proxying it.");
            }
            attType = createProxiedType(assignedName, typeDefinition, anonymous ? anonTypeRegistry : typeRegistry);
            return attType;
        }
        processingTypes.push(assignedName);
        // //////////

        final XSDTypeDefinition baseType = typeDefinition.getBaseType();

        AttributeType superType = null;
        if (baseType != null) {
            String targetNamespace = baseType.getTargetNamespace();
            String name = baseType.getName();
            if (name != null) {
                Name baseTypeName = new NameImpl(targetNamespace, name);
                superType = getAttributeType(baseTypeName, baseType, crs);
            }
        } else {
            LOGGER.fine(assignedName + " has no super type");
        }

        if (typeDefinition instanceof XSDComplexTypeDefinition) {
            XSDComplexTypeDefinition complexTypeDef = (XSDComplexTypeDefinition) typeDefinition;
            boolean includeParents = true;
            List<XSDElementDeclaration> children = Schemas.getChildElementDeclarations(typeDefinition, includeParents);

            final Collection<PropertyDescriptor> schema = new ArrayList<>(children.size());

            XSDElementDeclaration childDecl;
            AttributeDescriptor descriptor;
            for (XSDElementDeclaration child : children) {
                childDecl = child;
                try {
                    descriptor = createAttributeDescriptor(complexTypeDef, childDecl, crs);
                } catch (NoSuchElementException e) {
                    String msg = "Failed to create descriptor for '"
                            + childDecl.getTargetNamespace()
                            + "#"
                            + childDecl.getName()
                            + " from container '"
                            + typeDefinition.getTargetNamespace()
                            + "#"
                            + typeDefinition.getName()
                            + "'";
                    NoSuchElementException nse = new NoSuchElementException(msg);
                    nse.initCause(e);
                    throw nse;
                }
                schema.add(descriptor);
            }

            if (includeAttributes) {
                for (XSDAttributeUse attgcontent : complexTypeDef.getAttributeUses()) {
                    XSDAttributeDeclaration att = attgcontent.getAttributeDeclaration();
                    descriptor = createAttributeDescriptor(
                            getXmlAttributeType(),
                            null,
                            new NameImpl(att.getTargetNamespace(), "@" + att.getName()),
                            0,
                            1,
                            false,
                            null);
                    schema.add(descriptor);
                }
            }

            // set substitution group for descriptors here
            for (XSDElementDeclaration elemDecl : children) {
                if (elemDecl.isElementDeclarationReference()) {
                    elemDecl = elemDecl.getResolvedElementDeclaration();
                }
                PropertyDescriptor att = null;
                for (PropertyDescriptor desc : schema) {
                    if (desc.getName().getLocalPart().equals(elemDecl.getName())
                            && desc.getName().getNamespaceURI().equals(elemDecl.getTargetNamespace())) {
                        att = desc;
                        break;
                    }
                }
                setSubstitutionGroup(complexTypeDef, elemDecl, att, crs);
            }
            attType = createComplexAttributeType(assignedName, schema, complexTypeDef, superType);
        } else {
            Class<?> binding = String.class;
            boolean isIdentifiable = false;
            boolean isAbstract = false;
            List<Filter> restrictions = Collections.emptyList();
            InternationalString description = null;
            attType = typeFactory.createAttributeType(
                    assignedName, binding, isIdentifiable, isAbstract, restrictions, superType, description);
        }

        attType.getUserData().put(XSDTypeDefinition.class, typeDefinition);

        processingTypes.pop();

        // even if the type is anonymous, it still has to be registered somewhere because
        // it's needed for the proxied types to find them. That's why we have 2 registries,
        // typeRegistry
        // and anonTypeRegistry. TypeRegistry is the global one, since anonymous types are meant to
        // be
        // local and shouldn't be searchable.
        register(attType, anonymous);

        return attType;
    }

    /** NOTE: to be called only by {@link #createType(Name, XSDTypeDefinition)} */
    private AttributeType createComplexAttributeType(
            final Name assignedName,
            final Collection<PropertyDescriptor> schema,
            final XSDComplexTypeDefinition typeDefinition,
            final AttributeType superType) {

        boolean isAbstract = false; // TODO
        List<Filter> restrictions = Collections.emptyList();
        InternationalString description = null; // TODO

        AttributeType type;
        if (helper.isFeatureType(typeDefinition)) {
            type = typeFactory.createFeatureType(
                    assignedName, schema, null, isAbstract, restrictions, superType, description);
        } else {
            boolean isIdentifiable = helper.isIdentifiable(typeDefinition);
            type = typeFactory.createComplexType(
                    assignedName, schema, isIdentifiable, isAbstract, restrictions, superType, description);
        }
        return type;
    }

    /** Caches the basic types */
    private static Map<Class<? extends FeatureTypeRegistryConfiguration>, Map<Name, AttributeType>> FOUNDATION_TYPES =
            new ConcurrentHashMap<>();

    private void createFoundationTypes() {
        Map<Name, AttributeType> foundationTypes =
                FOUNDATION_TYPES.computeIfAbsent(helper.getClass(), o -> new HashMap<>());
        synchronized (foundationTypes) {
            if (!foundationTypes.isEmpty()) {
                typeRegistry.putAll(foundationTypes);
                return;
            }

            importSchema(new XSSchema());

            for (Schema schema : helper.getSchemas()) {
                importSchema(schema);
            }

            for (Configuration config : helper.getConfigurations()) {
                addSchemas(Schemas.findSchemas(config));
            }

            foundationTypes.putAll(typeRegistry);
        }
    }

    protected void importSchema(Schema schema) {
        for (Entry<Name, AttributeType> nameAttributeTypeEntry : schema.entrySet()) {
            Name key = nameAttributeTypeEntry.getKey();
            Object value = nameAttributeTypeEntry.getValue();
            if (typeRegistry.containsKey(key)) {
                LOGGER.finer("Ignoring "
                        + key
                        + " as it already exists. type "
                        + value.getClass().getName());
            } else {
                LOGGER.finer("Importing " + key + " of type " + value.getClass().getName());
                if (value instanceof AttributeType) {
                    AttributeType type = (AttributeType) value;
                    register(type, false);
                } else if (value instanceof AttributeDescriptor) {
                    AttributeDescriptor descriptor = (AttributeDescriptor) value;
                    register(descriptor);
                }
            }
        }
        LOGGER.fine("Schema " + schema.getURI() + " imported successfully");
    }

    public AttributeType getXmlAttributeType() {
        if (XMLATTRIBUTE_TYPE == null) {
            XMLATTRIBUTE_TYPE = typeFactory.createAttributeType(
                    new NameImpl(null, "@attribute"), String.class, false, false, Collections.emptyList(), null, null);
        }
        return XMLATTRIBUTE_TYPE;
    }
}
