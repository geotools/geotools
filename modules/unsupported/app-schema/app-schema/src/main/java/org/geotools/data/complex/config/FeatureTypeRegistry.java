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
package org.geotools.data.complex.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDAttributeUseCategory;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.Types;
import org.geotools.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.GMLSchema;
import org.geotools.gml3.smil.SMIL20LANGSchema;
import org.geotools.gml3.smil.SMIL20Schema;
import org.geotools.util.logging.Logging;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xs.XS;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.Schema;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A registry of GeoTools {@link AttributeType} and {@link AttributeDescriptor} lazily parsed from
 * the EMF {@link XSDTypeDefinition} and {@link XSDElementDeclaration} added through
 * {@link #addSchemas(SchemaIndex)}.
 * <p>
 * This class is meant to be used in conjunction with {@link EmfAppSchemaReader}. See
 * {@link AppSchemaDataAccessConfigurator}
 * </p>
 * <p>
 * Usage:
 * 
 * <pre>
 * <code>
 * FeatureTypeRegistry registry = new FeatureTypeRegistry();
 * EmfAppSchemaReader schemaParser = EmfAppSchemaReader.newInstance();
 * URL schemaLocation1 = ...
 * SchemaIndex schemas = schemaParser.parse(schemaLocation1, null);
 * registry.addSchemas(schemas);
 * URL schemaLocation2 = ...
 * schemas = schemaParser.parse(schemaLocation1, null);
 * registry.addSchemas(schemas);
 * 
 * Name typeName = ...
 * FeatureType ft = (FeatureType)registry.getAttributeType(typeName);
 * </p>
 * 
 * @author Gabriel Roldan
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/config/FeatureTypeRegistry.java $
 * @version $Id$
 * 
 */
public class FeatureTypeRegistry {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.complex");

    /**
     * Caches the GML 3.1.1 types and its dependencies
     */
    private static Map<Name, AttributeType> FOUNDATION_TYPES = new HashMap<Name, AttributeType>();

    /**
     * Caches the GML 3.1.1 top level descriptors and its dependencies
     */
    private static Map<Name, AttributeDescriptor> FOUNDATION_DESCRIPTORS = new HashMap<Name, AttributeDescriptor>();

    private List<SchemaIndex> schemas;

    private HashMap<Name, AttributeDescriptor> descriptorRegistry;

    private HashMap<Name, AttributeType> typeRegistry;

    private HashMap<Name, AttributeType> anonTypeRegistry;

    private FeatureTypeFactory typeFactory;

    private NamespaceSupport namespaces;

    /**
     * stack of currently being built type names, used by
     * {@link #createType(Name, XSDTypeDefinition)} to prevent recursive type definitions by
     * proxy'ing a type that appears to be already being constructed and thus still not in the type
     * registry.
     */
    private Stack<Name> processingTypes;

    public FeatureTypeRegistry() {
        this(null);
    }

    public FeatureTypeRegistry(NamespaceSupport namespaces) {
        this.namespaces = namespaces;
        schemas = new ArrayList<SchemaIndex>();
        typeFactory = new ComplexFeatureTypeFactoryImpl();
        descriptorRegistry = new HashMap<Name, AttributeDescriptor>();
        typeRegistry = new HashMap<Name, AttributeType>();
        anonTypeRegistry = new HashMap<Name, AttributeType>();
        processingTypes = new Stack<Name>();

        if (FOUNDATION_TYPES.isEmpty()) {
            createFoundationTypes();
        }

        typeRegistry.putAll(FOUNDATION_TYPES);
        descriptorRegistry.putAll(FOUNDATION_DESCRIPTORS);

    }

    public void addSchemas(final SchemaIndex schemaIndex) {
        schemas.add(schemaIndex);
    }

    public AttributeDescriptor getDescriptor(final Name descriptorName,
            CoordinateReferenceSystem crs, List<AttributeMapping> attMappings) {
        AttributeDescriptor descriptor = descriptorRegistry.get(descriptorName);
        if (descriptor == null) {
            try {
                XSDElementDeclaration elemDecl = getElementDeclaration(descriptorName);
                descriptor = createAttributeDescriptor(null, elemDecl, crs, attMappings);
                LOGGER.finest("Registering attribute descriptor " + descriptor.getName());
                register(descriptor);
            } catch (NoSuchElementException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }
        return descriptor;
    }

    public AttributeDescriptor getDescriptor(final Name descriptorName) {
        return getDescriptor(descriptorName, null, null);
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
            throw new IllegalArgumentException("No top level element found in schemas: " + qname);
        }
        return elemDecl;
    }

    public AttributeType getAttributeType(final Name typeName) {
        return getAttributeType(typeName, null, null);
    }

    public AttributeType getAttributeType(final Name typeName, CoordinateReferenceSystem crs,
            List<AttributeMapping> attMappings) {
        AttributeType type = (AttributeType) typeRegistry.get(typeName);
        if (type == null) {
            XSDTypeDefinition typeDef = getTypeDefinition(typeName);

            LOGGER.finest("Creating attribute type " + typeDef.getQName());
            type = createType(typeDef, crs, attMappings);
            LOGGER.finest("Registering attribute type " + type.getName());
        } else {
            // LOGGER.finer("Ignoring type " +
            // typeDef.getQName()
            // + " as it already exists in the registry");
        }
        return type;
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

    private AttributeDescriptor createAttributeDescriptor(final XSDComplexTypeDefinition container,
            final XSDElementDeclaration elemDecl, CoordinateReferenceSystem crs,
            List<AttributeMapping> attMappings) {
        String targetNamespace = elemDecl.getTargetNamespace();
        String name = elemDecl.getName();
        Name elemName = Types.typeName(targetNamespace, name);

        AttributeType type;
        try {
            type = getTypeOf(elemDecl, crs, attMappings);
        } catch (NoSuchElementException e) {
            String msg = "Type not found for " + elemName + " at type container "
                    + container.getTargetNamespace() + "#" + container.getName() + " at "
                    + container.getSchema().getSchemaLocation();
            NoSuchElementException nse = new NoSuchElementException(msg);
            nse.initCause(e);
            throw nse;
        }
        int minOccurs = container == null ? 0 : Schemas.getMinOccurs(container, elemDecl);
        int maxOccurs = container == null ? Integer.MAX_VALUE : Schemas.getMaxOccurs(container,
                elemDecl);
        boolean nillable = elemDecl.isNillable();

        if (maxOccurs == -1) {
            // this happens when maxOccurs is set to "unbounded"
            maxOccurs = Integer.MAX_VALUE;
        }
        Object defaultValue = null;
        AttributeDescriptor descriptor;

        if (!(type instanceof AttributeTypeProxy)
                && Geometry.class.isAssignableFrom(type.getBinding())) {
            // create geometry descriptor with the simple feature type CRS
            GeometryType geomType = new GeometryTypeImpl(type.getName(), type.getBinding(), crs,
                    type.isIdentified(), type.isAbstract(), type.getRestrictions(),
                    type.getSuper(), type.getDescription());
            descriptor = typeFactory.createGeometryDescriptor(geomType, elemName, minOccurs,
                    maxOccurs, nillable, defaultValue);
        } else {
            descriptor = typeFactory.createAttributeDescriptor(type, elemName, minOccurs,
                    maxOccurs, nillable, defaultValue);
        }
        descriptor.getUserData().put(XSDElementDeclaration.class, elemDecl);

        return descriptor;
    }

    /**
     * If the type of elemDecl is annonymous creates a new type with the same name than the
     * atrribute and returns it. If it is not anonymous, looks it up on the registry and in case the
     * type does not exists in the registry uses a proxy.
     * 
     * @param elemDecl
     * @return
     */
    private AttributeType getTypeOf(XSDElementDeclaration elemDecl, CoordinateReferenceSystem crs,
            List<AttributeMapping> attMappings) {
        boolean hasToBeRegistered = false;
        XSDTypeDefinition typeDefinition;

        // TODO REVISIT, I'm not sure this is the way to find out if the
        // element's type is defined in line (an thus no need to register it
        // as a global type)
        if (elemDecl.isElementDeclarationReference()) {
            elemDecl = elemDecl.getResolvedElementDeclaration();
        }
        typeDefinition = elemDecl.getAnonymousTypeDefinition();
        if (typeDefinition == null) {
            hasToBeRegistered = true;
            typeDefinition = elemDecl.getTypeDefinition();
        }

        if (typeDefinition == null) {
            throw new NoSuchElementException("The element declaration "
                    + elemDecl.getTargetNamespace() + "#" + elemDecl.getName()
                    + " has a null type definition, can't continue, fix it on the schema");
        }
        AttributeType type;
        if (hasToBeRegistered) {
            String targetNamespace = typeDefinition.getTargetNamespace();
            String name = typeDefinition.getName();
            Name typeName = Types.typeName(targetNamespace, name);
            type = getAttributeType(typeName, crs, attMappings);
            if (type == null) {
                type = createType(typeName, typeDefinition, crs, attMappings, false);
            }
        } else {
            String name = elemDecl.getName();
            String targetNamespace = elemDecl.getTargetNamespace();
            Name overrideName = Types.typeName(targetNamespace, name);
            type = createType(overrideName, typeDefinition, crs, attMappings, true);
        }
        return type;
    }

    private AttributeType createProxiedType(final Name assignedName,
            final XSDTypeDefinition typeDefinition, Map typeRegistry) {
        AttributeType type;
        if (null == typeDefinition.getSimpleType()
                && typeDefinition instanceof XSDComplexTypeDefinition) {
            boolean isFeatureType = isDerivedFrom(typeDefinition, GML.AbstractFeatureType);
            if (isFeatureType) {
                type = new FeatureTypeProxy(assignedName, typeRegistry);
            } else {
                type = new ComplexTypeProxy(assignedName, typeRegistry);
            }
        } else {
            boolean isGeometryType = isDerivedFrom(typeDefinition, GML.AbstractGeometryType);
            if (isGeometryType) {
                type = new GeometryTypeProxy(assignedName, typeRegistry);
            } else {
                type = new AttributeTypeProxy(assignedName, typeRegistry);
            }
        }
        return type;
    }

    /**
     * Returns whether <code>typeDefinition</code> has an ancestor named <code>baseTypeName</code>.
     * 
     * @param typeDefinition
     * @param baseTypeName
     * @return
     */
    private boolean isDerivedFrom(final XSDTypeDefinition typeDefinition, final QName baseTypeName) {
        Name typeName = Types.toTypeName(baseTypeName);
        // XSDTypeDefinition baseTypeDefinition = Schemas.getBaseTypeDefinition(
        // typeDefinition, baseTypeName);
        // boolean isFeatureType = baseTypeDefinition != null;
        // return isFeatureType;
        return isDerivedFrom(typeDefinition, typeName);
    }

    private AttributeType createType(XSDTypeDefinition typeDefinition,
            CoordinateReferenceSystem crs, List<AttributeMapping> attMappings) {
        String targetNamespace = typeDefinition.getTargetNamespace();
        String name = typeDefinition.getName();
        Name typeName = Types.typeName(targetNamespace, name);
        return createType(typeName, typeDefinition, crs, attMappings, false);
    }

    /**
     * Creates an {@link AttributeType} that matches the xsd type definition as much as possible.
     * <p>
     * The original type definition given by the {@link XSDTypeDefinition} is kept as
     * AttributeType's metadata stored as a "user data" property using
     * <code>XSDTypeDefinition.class</code> as key.
     * </p>
     * <p>
     * If it is a complex attribute, it will contain all the properties declared in the
     * <code>typeDefinition</code>, as well as all the properties declared in its super types.
     * </p>
     * TODO: handle the case where the extension mechanism is restriction.
     * 
     * @param assignedName
     * @param typeDefinition
     * @return
     */
    private AttributeType createType(final Name assignedName,
            final XSDTypeDefinition typeDefinition, CoordinateReferenceSystem crs,
            List<AttributeMapping> attMappings, boolean anonymous) {

        AttributeType attType;
        // /////////
        if (processingTypes.contains(assignedName)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Recursion found for type " + assignedName + ". Proxying it.");
            }
            attType = createProxiedType(assignedName, typeDefinition, anonymous ? anonTypeRegistry
                    : typeRegistry);
            return attType;
        }
        processingTypes.push(assignedName);
        // //////////

        final XSDTypeDefinition baseType = typeDefinition.getBaseType();

        AttributeType superType = null;
        if (baseType != null) {
            String targetNamespace = baseType.getTargetNamespace();
            String name = baseType.getName();
            superType = getType(targetNamespace, name);
            if (superType == null) {
                superType = createType(baseType, crs, attMappings);
            }
        } else {
            LOGGER.warning(assignedName + " has no super type");
        }

        if (typeDefinition instanceof XSDComplexTypeDefinition) {
            XSDComplexTypeDefinition complexTypeDef;
            complexTypeDef = (XSDComplexTypeDefinition) typeDefinition;
            boolean includeParents = true;
            List children = Schemas.getChildElementDeclarations(typeDefinition, includeParents);

            final Collection<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>(
                    children.size());

            XSDElementDeclaration childDecl;
            AttributeDescriptor descriptor;
            for (Iterator it = children.iterator(); it.hasNext();) {
                childDecl = (XSDElementDeclaration) it.next();
                try {
                    descriptor = createAttributeDescriptor(complexTypeDef, childDecl, crs,
                            attMappings);
                    schema.add(descriptor);
                } catch (NoSuchElementException e) {
                    LOGGER.log(Level.WARNING, e.getMessage());
                    throw e;
                }
            }

            attType = createComplexAttributeType(assignedName, schema, complexTypeDef, superType);
        } else {
            Class<?> binding = String.class;
            boolean isIdentifiable = false;
            boolean isAbstract = false;
            List<Filter> restrictions = Collections.emptyList();
            InternationalString description = null;
            attType = typeFactory.createAttributeType(assignedName, binding, isIdentifiable,
                    isAbstract, restrictions, superType, description);
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

    /**
     * NOTE: to be called only by {@link #createType(Name, XSDTypeDefinition)}
     * 
     * @param assignedName
     * @param schema
     * @param typeDefinition
     * @param superType
     * @return
     */
    private AttributeType createComplexAttributeType(final Name assignedName,
            final Collection<PropertyDescriptor> schema,
            final XSDComplexTypeDefinition typeDefinition, final AttributeType superType) {

        AttributeType abstractFType = getType(GML.NAMESPACE, GML.AbstractFeatureType.getLocalPart());
        assert abstractFType != null;

        boolean isFeatureType = isDerivedFrom(typeDefinition, abstractFType.getName());

        boolean isAbstract = false;// TODO
        List<Filter> restrictions = Collections.emptyList();
        InternationalString description = null; // TODO

        AttributeType type;
        if (isFeatureType) {
            type = typeFactory.createFeatureType(assignedName, schema, null, isAbstract,
                    restrictions, superType, description);
        } else {
            boolean isIdentifiable = isIdentifiable((XSDComplexTypeDefinition) typeDefinition);
            type = typeFactory.createComplexType(assignedName, schema, isIdentifiable, isAbstract,
                    restrictions, superType, description);
        }
        return type;
    }

    /**
     * Determines if elements of the given complex type definition are required to have an
     * identifier by looking for a child element of <code>typeDefinition</code> of the form
     * <code>&lt;xs:attribute ref=&quot;gml:id&quot; use=&quot;required&quot; /&gt;</code>
     * 
     * @param typeDefinition
     * @return
     */
    private boolean isIdentifiable(XSDComplexTypeDefinition typeDefinition) {
        List attributeUses = typeDefinition.getAttributeUses();

        final String idAttName = GML.id.getLocalPart();

        for (Iterator it = attributeUses.iterator(); it.hasNext();) {
            XSDAttributeUse use = (XSDAttributeUse) it.next();
            XSDAttributeUseCategory useCategory = use.getUse();

            XSDAttributeDeclaration idAtt = use.getAttributeDeclaration();

            String targetNamespace = idAtt.getTargetNamespace();
            String name = idAtt.getName();
            if (GML.NAMESPACE.equals(targetNamespace) && idAttName.equals(name)) {
                if (XSDAttributeUseCategory.REQUIRED_LITERAL.equals(useCategory)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if <code>typeDefinition</code> is derived from a type named
     * <code>superTypeName</code>
     * 
     * @param typeDefinition
     * @param superTypeName
     * @return
     */
    private boolean isDerivedFrom(XSDTypeDefinition typeDefinition, final Name superTypeName) {

        XSDTypeDefinition baseType;
        final String superNS = superTypeName.getNamespaceURI();
        final String superName = superTypeName.getLocalPart();

        String targetNamespace;
        String name;
        while ((baseType = typeDefinition.getBaseType()) != null) {
            targetNamespace = baseType.getTargetNamespace();
            name = baseType.getName();
            if (XS.NAMESPACE.equals(targetNamespace) && XS.ANYTYPE.getLocalPart().equals(name)) {
                return false;
            }
            if (superNS.equals(targetNamespace) && superName.equals(name)) {
                return true;
            }
            typeDefinition = baseType;
        }
        return false;
    }

    private AttributeType getType(String namespace, String name) {
        Name typeName = Types.typeName(namespace, name);
        return getAttributeType(typeName, null, null);
    }

    private void createFoundationTypes() {
        synchronized (FOUNDATION_TYPES) {
            if (!FOUNDATION_TYPES.isEmpty()) {
                return;
            }
            Schema schema;
            schema = new XSSchema();
            importSchema(schema);

            schema = new SMIL20Schema();
            importSchema(schema);

            schema = new SMIL20LANGSchema();
            importSchema(schema);

            schema = new GMLSchema();
            importSchema(schema);

            LOGGER.info("Creating GMLConfiguration to get the prebuilt gml schemas from");
            GMLConfiguration configuration = new GMLConfiguration();
            LOGGER.info("Acquiring prebuilt gml schema and its dependencies");
            SchemaIndex index = Schemas.findSchemas(configuration);
            // schedule the gml schemas to be lazily loaded for any type missing from the above
            // import of prebuilt types
            addSchemas(index);

            FOUNDATION_TYPES.putAll(typeRegistry);
            FOUNDATION_DESCRIPTORS.putAll(descriptorRegistry);
            typeRegistry.clear();
            descriptorRegistry.clear();
        }
    }

    @SuppressWarnings("unchecked")
    private void importSchema(Schema schema) {
        for (Iterator it = schema.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Entry) it.next();
            Name key = (Name) entry.getKey();
            Object value = entry.getValue();
            if (typeRegistry.containsKey(key)) {
                LOGGER.finer("Ignoring " + key + " as it already exists. type "
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
}
