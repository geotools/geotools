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
package org.geotools.xsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.xs.XS;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

/**
 * Xml Schema for a particular namespace.
 *
 * <p>This class should is subclasses for the xs, gml, filter, sld, etc... schemas. Subclasses
 * should be implemented as singletons.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 */
public abstract class XSD {
    /** logging instance */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(XSD.class);

    /** schema contents */
    protected volatile XSDSchema schema;

    /** type schema */
    protected Schema typeSchema;
    /** type mapping profile */
    protected Schema typeMappingProfile;

    /** dependencies */
    private volatile Set<XSD> dependencies;

    protected XSD() {}

    /** Sets up the schema which maps xml schema types to attribute types. */
    protected Schema buildTypeSchema() {
        return new SchemaImpl(getNamespaceURI());
    }

    /** Sets up a profile which uniquely maps a set of java classes to a schema element. */
    protected Schema buildTypeMappingProfile(Schema typeSchema) {
        return typeSchema.profile(Collections.emptySet());
    }

    /**
     * Convenience method to turn a QName into a Name.
     *
     * <p>Useful for building type mapping profiles.
     *
     * @param qName The name to transform.
     */
    protected Name name(QName qName) {
        return new NameImpl(qName.getNamespaceURI(), qName.getLocalPart());
    }

    /** Returns the schema containing {@link AttributeType}'s for all xml types. */
    public final Schema getTypeSchema() {
        if (typeSchema == null) {
            synchronized (this) {
                typeSchema = buildTypeSchema();
            }
        }
        return typeSchema;
    }

    /**
     * Returns the subset of {@link #getTypeSchema()} which maintains a unique java class to xml
     * type mapping.
     */
    public final Schema getTypeMappingProfile() {
        if (typeMappingProfile == null) {
            synchronized (this) {
                typeMappingProfile = buildTypeMappingProfile(getTypeSchema());
            }
        }
        return typeMappingProfile;
    }

    /**
     * Transitively returns the type mapping profile for this schema and all schemas that this
     * schema depends on.
     */
    public final List<Schema> getAllTypeMappingProfiles() {
        List<Schema> profiles = new LinkedList<>();
        for (XSD xsd : getAllDependencies()) {
            Schema profile = xsd.getTypeMappingProfile();
            if (!profile.isEmpty()) {
                profiles.add(profile);
            }
        }

        return profiles;
    }

    /** The namespace uri of the schema. */
    public abstract String getNamespaceURI();

    /** The location on the local disk of the top level .xsd file which defines the schema. */
    public abstract String getSchemaLocation();

    /** The dependencies of this schema. */
    public final Set<XSD> getDependencies() {
        if (dependencies == null) {
            synchronized (this) {
                if (dependencies == null) {
                    Set<XSD> newDeps = new LinkedHashSet<>();

                    // bootstrap, every xsd depends on XS
                    newDeps.add(XS.getInstance());

                    // call subclass hook
                    addDependencies(newDeps);
                    dependencies = newDeps;
                }
            }
        }

        return dependencies;
    }

    /** Returns all dependencies , direct and transitive that this xsd depends on. */
    public List<XSD> getAllDependencies() {
        return allDependencies();
    }

    protected List<XSD> allDependencies() {
        LinkedList<XSD> unpacked = new LinkedList<>();

        Stack<XSD> stack = new Stack<>();
        stack.addAll(getDependencies());

        while (!stack.isEmpty()) {
            XSD xsd = stack.pop();

            if (!equals(xsd) && !unpacked.contains(xsd)) {
                unpacked.addFirst(xsd);
                stack.addAll(xsd.getDependencies());
            }
        }

        return unpacked;
    }

    /** Subclass hook to add additional dependencies. */
    protected void addDependencies(Set<XSD> dependencies) {}

    /** Returns the XSD object representing the contents of the schema. */
    public final XSDSchema getSchema() throws IOException {
        if (schema == null) {
            // buildSchema in general will need to parse dependencies, which through
            // schema locators will circle back to XSD.getSchema(), potentially causing
            // a deadlock, if this synchronization uses synchronized(this). Example:
            // 1) t1 loads OGC, holding a lock on it, and parses its own schema
            //    holding a lock on Schemas.classes
            // 2) t2 starts loading GML, holding a lock on it, and tries to parse its schema
            //    waiting on Schemas.classes lock
            // 3) t1 parsing finds an import from OGC filters to GML, the locators bring it
            //    to call GML.getSchema(), and we have the deadlock.
            // So the old synchronized(this) was replaced with synchronized(Schemas.class)
            // to avert the possibility of the above cited lock
            synchronized (Schemas.class) {
                if (schema == null) {
                    LOGGER.fine("building schema for schema: " + getNamespaceURI());
                    schema = buildSchema();
                }
            }
        }

        return schema;
    }

    /**
     * Builds the schema from the .xsd file specified by {@link #getSchemaLocation()}
     *
     * <p>This method may be extended, but should not be overridden.
     */
    protected XSDSchema buildSchema() throws IOException {
        // grab all the dependencies and create schema locators from the build
        // schemas
        List<XSDSchemaLocator> locators = new ArrayList<>();
        List<XSDSchemaLocationResolver> resolvers = new ArrayList<>();

        for (XSD dependency : allDependencies()) {
            SchemaLocator locator = dependency.createSchemaLocator();

            if (locator != null) {
                locators.add(locator);
            }

            SchemaLocationResolver resolver = dependency.createSchemaLocationResolver();

            if (resolver != null) {
                resolvers.add(resolver);
            }
        }

        XSDSchemaLocator suppSchemaLocator = getSupplementarySchemaLocator();

        if (suppSchemaLocator != null) {
            locators.add(suppSchemaLocator);
        }

        SchemaLocationResolver resolver = createSchemaLocationResolver();

        if (resolver != null) {
            resolvers.add(resolver);
        }

        // parse the location of the xsd with all the locators for dependent
        // schemas
        return Schemas.parse(getSchemaLocation(), locators, resolvers);
    }

    public SchemaLocator createSchemaLocator() {
        return new SchemaLocator(this);
    }

    public SchemaLocationResolver createSchemaLocationResolver() {
        return new SchemaLocationResolver(this);
    }

    /**
     * Returns the qualified name for the specified local part.
     *
     * @return The QName, built by simply prepending the namespace for this xsd.
     */
    public QName qName(String local) {
        return new QName(getNamespaceURI(), local);
    }

    /** Implementation of equals, equality is based soley on {@link #getNamespaceURI()}. */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof XSD) {
            XSD other = (XSD) obj;

            return getNamespaceURI().equals(other.getNamespaceURI());
        }

        return false;
    }

    @Override
    public final int hashCode() {
        return getNamespaceURI().hashCode();
    }

    @Override
    public String toString() {
        return getNamespaceURI();
    }

    /**
     * Optionally, a schema locator that helps locating (other) schema's used for includes/imports
     * that might already exist but are not in dependencies
     *
     * @return Schema Locator
     */
    public XSDSchemaLocator getSupplementarySchemaLocator() {
        return null;
    }

    /**
     * Remove all references to this schema, and all schemas built in the same resource set It is
     * important to call this method for every dynamic schema created that is not needed anymore,
     * because references in the static schema's will otherwise keep it alive forever
     */
    public void dispose() {
        if (schema != null) {
            ResourceSet rs = schema.eResource().getResourceSet();
            for (Resource r : rs.getResources()) {
                if (r instanceof XSDResourceImpl) {
                    Schemas.dispose(((XSDResourceImpl) r).getSchema());
                }
            }

            schema = null;
        }
    }
}
