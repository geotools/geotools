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
package org.geotools.xsd.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeGroupDefinition;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.util.SoftValueHashMap;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;

public class SchemaIndexImpl implements SchemaIndex {
    /** The schemas */
    XSDSchema[] schemas;

    /** Indexes */
    HashMap<QName, XSDElementDeclaration> elementIndex;

    HashMap<QName, XSDAttributeDeclaration> attributeIndex;
    HashMap<QName, XSDAttributeGroupDefinition> attributeGroupIndex;
    HashMap<QName, XSDComplexTypeDefinition> complexTypeIndex;
    HashMap<QName, XSDSimpleTypeDefinition> simpleTypeIndex;

    /** Cache of elements to children */
    SoftValueHashMap<XSDElementDeclaration, OrderedMap<QName, XSDParticle>> element2children =
            new SoftValueHashMap<>(1000);

    /** Cache of elements to attributes */
    HashMap<XSDElementDeclaration, List<XSDAttributeDeclaration>> element2attributes =
            new HashMap<>();

    /** Adapter for tracking changes to schemas. */
    SchemaAdapter adapter;

    public SchemaIndexImpl(XSDSchema[] schemas) {
        this.schemas = new XSDSchema[schemas.length + 1];
        adapter = new SchemaAdapter();

        // set the schemas passed in
        for (int i = 0; i < schemas.length; i++) {
            this.schemas[i] = schemas[i];
            synchronized (this.schemas[i].eAdapters()) {
                this.schemas[i].eAdapters().add(adapter);
            }
        }

        // add the schema for xml schema itself
        this.schemas[schemas.length] = schemas[0].getSchemaForSchema();
    }

    @Override
    public void destroy() {
        // remove the adapter from the schemas
        if (schemas == null) {
            return;
        }
        for (XSDSchema schema : schemas) {
            synchronized (schema.eAdapters()) {
                schema.eAdapters().remove(adapter);
            }
        }
        schemas = null;
    }

    @Override
    public XSDSchema[] getSchemas() {
        return schemas;
    }

    public XSDImport[] getImports() {
        Collection<XSDImport> imports = find(XSDImport.class);

        return imports.toArray(new XSDImport[imports.size()]);
    }

    public XSDInclude[] getIncludes() {
        Collection<XSDInclude> includes = find(XSDInclude.class);

        return includes.toArray(new XSDInclude[includes.size()]);
    }

    @Override
    public XSDElementDeclaration getElementDeclaration(QName qName) {
        return (XSDElementDeclaration) lookup(getElementIndex(), qName);

        // return (XSDElementDeclaration) getElementIndex().get(qName);
    }

    @Override
    public XSDAttributeDeclaration getAttributeDeclaration(QName qName) {
        return (XSDAttributeDeclaration) lookup(getAttributeIndex(), qName);

        // return (XSDAttributeDeclaration) getAttributeIndex().get(qName);
    }

    @Override
    public XSDAttributeGroupDefinition getAttributeGroupDefinition(QName qName) {
        return (XSDAttributeGroupDefinition) lookup(getAttributeGroupIndex(), qName);

        // return (XSDAttributeGroupDefinition) getAttributeGroupIndex().get(qName);
    }

    @Override
    public XSDComplexTypeDefinition getComplexTypeDefinition(QName qName) {
        return (XSDComplexTypeDefinition) lookup(getComplexTypeIndex(), qName);

        // return (XSDComplexTypeDefinition) getComplexTypeIndex().get(qName);
    }

    @Override
    public XSDSimpleTypeDefinition getSimpleTypeDefinition(QName qName) {
        return (XSDSimpleTypeDefinition) lookup(getSimpleTypeIndex(), qName);

        // return (XSDSimpleTypeDefinition) getSimpleTypeIndex().get(qName);
    }

    @Override
    public XSDTypeDefinition getTypeDefinition(QName qName) {
        XSDTypeDefinition type = getComplexTypeDefinition(qName);

        if (type == null) {
            type = getSimpleTypeDefinition(qName);
        }

        return type;
    }

    protected XSDNamedComponent lookup(Map index, QName qName) {
        XSDNamedComponent component = (XSDNamedComponent) index.get(qName);

        if (component != null) {
            return component;
        }

        // check for namespace wildcard
        if ("*".equals(qName.getNamespaceURI())) {
            List<XSDNamedComponent> matches = new ArrayList<>();

            for (Object o : index.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                QName name = (QName) entry.getKey();

                if (name.getLocalPart().equals(qName.getLocalPart())) {
                    matches.add((XSDNamedComponent) entry.getValue());
                }
            }

            if (matches.size() == 1) {
                return matches.get(0);
            }
        }

        return null;
    }

    protected OrderedMap<QName, XSDParticle> children(XSDElementDeclaration parent) {
        OrderedMap<QName, XSDParticle> children = element2children.get(parent);

        if (children == null) {
            synchronized (this) {
                if (children == null) {
                    children = new ListOrderedMap<>();

                    for (XSDParticle particle :
                            Schemas.getChildElementParticles(parent.getType(), true)) {
                        XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

                        if (child.isElementDeclarationReference()) {
                            child = child.getResolvedElementDeclaration();
                        }

                        QName childName = null;

                        if (child.getTargetNamespace() != null) {
                            childName = new QName(child.getTargetNamespace(), child.getName());
                        } else if (parent.getTargetNamespace() != null) {
                            childName = new QName(parent.getTargetNamespace(), child.getName());
                        } else if (parent.getType().getTargetNamespace() != null) {
                            childName =
                                    new QName(
                                            parent.getType().getTargetNamespace(), child.getName());
                        } else {
                            childName = new QName(null, child.getName());
                        }

                        children.put(childName, particle);
                    }

                    element2children.put(parent, children);
                }
            }
        }

        return children;
    }

    @Override
    public XSDElementDeclaration getChildElement(XSDElementDeclaration parent, QName childName) {
        OrderedMap<QName, XSDParticle> children = children(parent);
        XSDParticle particle = children.get(childName);

        if (particle != null) {
            XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

            if (child.isElementDeclarationReference()) {
                child = child.getResolvedElementDeclaration();
            }

            return child;
        }

        if ("*".equals(childName.getNamespaceURI())) {
            // do a check just on local name
            List<XSDParticle> matches = new ArrayList<>();

            for (Map.Entry<QName, XSDParticle> entry : children.entrySet()) {
                QName name = entry.getKey();

                if (name.getLocalPart().equals(childName.getLocalPart())) {
                    matches.add(entry.getValue());
                }
            }

            if (matches.size() == 1) {
                particle = matches.get(0);

                XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

                if (child.isElementDeclarationReference()) {
                    child = child.getResolvedElementDeclaration();
                }

                return child;
            }
        }

        return null;
    }

    @Override
    public List<XSDParticle> getChildElementParticles(XSDElementDeclaration parent) {
        return new ArrayList<>(children(parent).values());
    }

    @Override
    public List getAttributes(XSDElementDeclaration element) {
        List<XSDAttributeDeclaration> attributes = element2attributes.get(element);

        if (attributes == null) {
            attributes = Schemas.getAttributeDeclarations(element);
            element2attributes.put(element, attributes);
        }

        return Collections.unmodifiableList(attributes);
    }

    protected <T> Collection<T> find(Class<?> c) {
        List<T> found = new ArrayList<>();

        for (XSDSchema schema : schemas) {
            List<XSDSchemaContent> content = schema.getContents();

            for (XSDSchemaContent o : content) {
                if (c.isAssignableFrom(o.getClass())) {
                    @SuppressWarnings("unchecked")
                    T cast = (T) o;
                    found.add(cast);
                }
            }
        }

        return found;
    }

    protected HashMap getElementIndex() {
        if (elementIndex == null) {
            synchronized (this) {
                if (elementIndex == null) {
                    buildElementIndex();
                }
            }
        }

        return elementIndex;
    }

    protected HashMap getAttributeIndex() {
        if (attributeIndex == null) {
            synchronized (this) {
                if (attributeIndex == null) {
                    buildAttriubuteIndex();
                }
            }
        }

        return attributeIndex;
    }

    protected HashMap getAttributeGroupIndex() {
        if (attributeGroupIndex == null) {
            synchronized (this) {
                if (attributeGroupIndex == null) {
                    buildAttributeGroupIndex();
                }
            }
        }

        return attributeGroupIndex;
    }

    protected HashMap getComplexTypeIndex() {
        if (complexTypeIndex == null) {
            synchronized (this) {
                if (complexTypeIndex == null) {
                    buildComplexTypeIndex();
                }
            }
        }

        return complexTypeIndex;
    }

    protected HashMap getSimpleTypeIndex() {
        if (simpleTypeIndex == null) {
            synchronized (this) {
                if (simpleTypeIndex == null) {
                    buildSimpleTypeIndex();
                }
            }
        }

        return simpleTypeIndex;
    }

    protected void buildElementIndex() {
        elementIndex = new HashMap<>();

        for (XSDSchema schema : schemas) {
            for (XSDElementDeclaration element : schema.getElementDeclarations()) {
                QName qName = new QName(element.getTargetNamespace(), element.getName());
                elementIndex.put(qName, element);
            }
        }
    }

    protected void buildAttriubuteIndex() {
        attributeIndex = new HashMap<>();

        for (XSDSchema schema : schemas) {
            for (XSDAttributeDeclaration attribute : schema.getAttributeDeclarations()) {
                QName qName = new QName(attribute.getTargetNamespace(), attribute.getName());
                attributeIndex.put(qName, attribute);
            }
        }
    }

    protected void buildAttributeGroupIndex() {
        attributeGroupIndex = new HashMap<>();

        for (XSDSchema schema : schemas) {
            for (XSDAttributeGroupDefinition group : schema.getAttributeGroupDefinitions()) {
                QName qName = new QName(group.getTargetNamespace(), group.getName());
                attributeGroupIndex.put(qName, group);
            }
        }
    }

    protected void buildComplexTypeIndex() {
        complexTypeIndex = new HashMap<>();

        for (XSDSchema schema : schemas) {
            for (XSDTypeDefinition type : schema.getTypeDefinitions()) {
                if (type instanceof XSDComplexTypeDefinition) {
                    QName qName = new QName(type.getTargetNamespace(), type.getName());
                    complexTypeIndex.put(qName, (XSDComplexTypeDefinition) type);
                }
            }
        }
    }

    protected void buildSimpleTypeIndex() {
        simpleTypeIndex = new HashMap<>();

        for (XSDSchema schema : schemas) {
            for (XSDTypeDefinition type : schema.getTypeDefinitions()) {
                if (type instanceof XSDSimpleTypeDefinition) {
                    QName qName = new QName(type.getTargetNamespace(), type.getName());
                    simpleTypeIndex.put(qName, (XSDSimpleTypeDefinition) type);
                }
            }
        }
    }

    class SchemaAdapter implements Adapter {
        Notifier target;
        Notification last;

        @Override
        public Notifier getTarget() {
            return target;
        }

        @Override
        public void setTarget(Notifier target) {
            this.target = target;
        }

        @Override
        public boolean isAdapterForType(Object object) {
            return object instanceof XSDSchema;
        }

        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getEventType() == Notification.ADD) {
                switch (notification.getFeatureID(XSDSchema.class)) {
                    case XSDPackage.XSD_SCHEMA__ATTRIBUTE_DECLARATIONS:
                        synchronized (SchemaIndexImpl.this) {
                            attributeIndex = null;
                        }

                        break;

                    case XSDPackage.XSD_SCHEMA__ELEMENT_DECLARATIONS:
                        synchronized (SchemaIndexImpl.this) {
                            elementIndex = null;
                        }

                        break;

                    case XSDPackage.XSD_SCHEMA__TYPE_DEFINITIONS:
                        synchronized (SchemaIndexImpl.this) {
                            complexTypeIndex = null;
                            simpleTypeIndex = null;
                        }

                        break;

                    case XSDPackage.XSD_SCHEMA__ATTRIBUTE_GROUP_DEFINITIONS:
                        synchronized (SchemaIndexImpl.this) {
                            attributeGroupIndex = null;
                        }

                        break;
                }
            }
        }
    }
}
