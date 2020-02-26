/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeGroupContent;
import org.eclipse.xsd.XSDAttributeGroupDefinition;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.XSDWildcard;
import org.eclipse.xsd.impl.XSDImportImpl;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.eclipse.xsd.util.XSDUtil;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.xsd.impl.SchemaIndexImpl;
import org.geotools.xsd.impl.TypeWalker;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Utility class for performing various operations.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class Schemas {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(Schemas.class);

    /*
     * Name of the system property forcing the import of external schemas in any case,
     * whereas the default behavior is to force the import only if the importing schema
     * has no element nor type on its own.
     */
    static final String FORCE_SCHEMA_IMPORT = "org.geotools.xml.forceSchemaImport";

    static {
        // need to register custom factory to load schema resources
        Resource.Factory.Registry.INSTANCE
                .getExtensionToFactoryMap()
                .put("xsd", new XSDResourceFactoryImpl());
    }

    /**
     * Finds all the XSDSchemas used by the {@link Configuration configuration} by looking at the
     * configuration's schema locator and its dependencies.
     *
     * @param configuration the {@link Configuration} for which to find all its related schemas
     * @return a {@link SchemaIndex} holding the schemas related to <code>configuration</code>
     */
    public static final SchemaIndex findSchemas(Configuration configuration) {
        Set configurations = new HashSet(configuration.allDependencies());
        configurations.add(configuration);

        List resolvedSchemas = new ArrayList(configurations.size());

        for (Iterator it = configurations.iterator(); it.hasNext(); ) {
            Configuration conf = (Configuration) it.next();

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("looking up schema for " + conf.getNamespaceURI());
            }

            XSDSchemaLocator locator = new SchemaLocator(conf.getXSD());

            if (locator == null) {
                LOGGER.fine("No schema locator for " + conf.getNamespaceURI());

                continue;
            }

            String namespaceURI = conf.getNamespaceURI();
            String schemaLocation = null;

            try {
                URL location = new URL(conf.getXSD().getSchemaLocation());
                schemaLocation = location.toExternalForm();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("schema location: " + schemaLocation);
            }

            XSDSchema schema = locator.locateSchema(null, namespaceURI, schemaLocation, null);

            if (schema != null) {
                resolvedSchemas.add(schema);
            }
        }

        XSDSchema[] schemas =
                (XSDSchema[]) resolvedSchemas.toArray(new XSDSchema[resolvedSchemas.size()]);
        SchemaIndex index = new SchemaIndexImpl(schemas);

        return index;
    }

    /**
     * Finds all {@link XSDSchemaLocationResolver}'s used by the configuration.
     *
     * @param configuration The parser configuration.
     * @return A list of location resolvers, empty if none found.
     */
    public static List findSchemaLocationResolvers(Configuration configuration) {
        List all = configuration.allDependencies();
        List resolvers = new ArrayList();

        for (Iterator c = all.iterator(); c.hasNext(); ) {
            configuration = (Configuration) c.next();

            XSDSchemaLocationResolver resolver = new SchemaLocationResolver(configuration.getXSD());

            if (resolver != null) {
                resolvers.add(resolver);
            }
        }

        return resolvers;
    }

    /**
     * Parses a schema at the specified location.
     *
     * @param location A uri pointing to the location of the schema.
     * @return The parsed schema, or null if the schema could not be parsed.
     * @throws IOException In the event of a schema parsing error.
     */
    public static final XSDSchema parse(String location) throws IOException {
        return parse(location, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    /**
     * Parses a schema at the specified location.
     *
     * @param location A uri pointing to the location of the schema.
     * @param locators A list of schema locator objects to be used when parsing imports/includes of
     *     the main schema.
     * @param resolvers A list of schema location resolvers used to override schema locations
     *     encountered in an instance document or an imported schema.
     * @return The parsed schema, or null if the schema could not be parsed.
     * @throws IOException In the event of a schema parsing error.
     */
    public static final XSDSchema parse(String location, List locators, List resolvers)
            throws IOException {
        return parse(location, locators, resolvers, null);
    }

    /**
     * Parses a schema at the specified location.
     *
     * @param location A uri pointing to the location of the schema.
     * @param locators A list of schema locator objects to be used when parsing imports/includes of
     *     the main schema.
     * @param resolvers A list of schema location resolvers used to override schema locations
     *     encountered in an instance document or an imported schema.
     * @param uriHandlers A list of uri handlers to inject into the parsing chain, to handle
     *     externally referenced resources, like external schemas, etc...
     * @return The parsed schema, or null if the schema could not be parsed.
     * @throws IOException In the event of a schema parsing error.
     */
    public static final XSDSchema parse(
            String location, List locators, List resolvers, List<URIHandler> uriHandlers)
            throws IOException {
        ResourceSet resourceSet = new ResourceSetImpl();

        // add the specialized schema location resolvers
        if ((resolvers != null) && !resolvers.isEmpty()) {
            AdapterFactory adapterFactory = new SchemaLocationResolverAdapterFactory(resolvers);
            resourceSet.getAdapterFactories().add(adapterFactory);
        }

        // add the specialized schema locators as adapters
        if ((locators != null) && !locators.isEmpty()) {
            AdapterFactory adapterFactory = new SchemaLocatorAdapterFactory(locators);
            resourceSet.getAdapterFactories().add(adapterFactory);
        }

        // add the specifialized uri handlers
        if (uriHandlers != null && !uriHandlers.isEmpty()) {
            resourceSet.getURIConverter().getURIHandlers().addAll(0, uriHandlers);
        }
        return parse(location, resourceSet);
    }

    public static final XSDSchema parse(String location, ResourceSet resourceSet)
            throws IOException {
        // check for case of file url, make sure it is an absolute reference
        File locationFile = null;
        try {
            locationFile = URLs.urlToFile(new URL(location));
        } catch (MalformedURLException e) {
            // Some tests use relative file URLs, which Schemas.parse cannot
            // support as it does not permit a context URL. Treat them
            // as local file paths. Naughty, naughty.
            locationFile = new File(location);
        }
        if (locationFile != null && locationFile.exists()) {
            location = locationFile.getCanonicalFile().toURI().toString();
        }

        URI uri = URI.createURI(location);

        XSDResourceImpl xsdMainResource =
                (XSDResourceImpl) resourceSet.createResource(URI.createURI(".xsd"));
        xsdMainResource.setURI(uri);

        // read resource before synchronize: Shorter lock duration and prevention of deadlock
        // if remote schema is created on same JVM and synchronizes on Schemas, too.
        Map<Object, Object> options = resourceSet.getLoadOptions();
        Map<?, ?> response = getOrCreateResponseFrom(options);
        byte[] resourceData = readUriResource(uri, resourceSet, response);

        // schema building has effects on referenced schemas, it will alter them -> we need
        // to synchronize this call so that only one of these operations is active at any time
        synchronized (Schemas.class) {
            try (InputStream in = new ByteArrayInputStream(resourceData)) {
                xsdMainResource.load(in, options);
            } finally {
                Long timeStamp = (Long) response.get(URIConverter.RESPONSE_TIME_STAMP_PROPERTY);
                if (timeStamp != null) {
                    xsdMainResource.setTimeStamp(timeStamp);
                }
            }
            XSDSchema schema = xsdMainResource.getSchema();
            if (schema != null) {
                // if schema contains no element declarations, nor type definitions,
                // force import of external schemas (if any), since it does not happen
                // automatically;
                // import can be forced no matter what is in the importing schema by setting
                // the system property "org.geotools.xml.forceSchemaImport" to "true".
                String forceSchemaImport = System.getProperty(FORCE_SCHEMA_IMPORT);
                boolean alwaysForce = false;
                if (forceSchemaImport != null) {
                    alwaysForce = forceSchemaImport.equalsIgnoreCase("true");
                }
                if (alwaysForce || hasNoElementsNorTypes(schema)) {
                    forceImport((XSDSchemaImpl) schema);
                }
            }

            return schema;
        }
    }

    /**
     * Fetches the contents of the URI into a byte[].
     *
     * @return The resource data
     */
    private static byte[] readUriResource(URI uri, ResourceSet resourceSet, Map<?, ?> response)
            throws IOException {
        Map<Object, Object> options = resourceSet.getLoadOptions();
        URIConverter uriConverter = getUriConverter(resourceSet);

        Map<Object, Object> loadMap = new HashMap<>(options);
        loadMap.put(URIConverter.OPTION_RESPONSE, response);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream inputStream = uriConverter.createInputStream(uri, loadMap)) {
            IOUtils.copy(inputStream, out);
        }
        return out.toByteArray();
    }

    /**
     * Fetches the map to be used as reponse from the given options, creating a new one if not
     * existing.
     *
     * @return a map to be used as response
     */
    private static Map<?, ?> getOrCreateResponseFrom(Map<Object, Object> options) {
        Map<?, ?> response =
                (options == null) ? null : (Map<?, ?>) options.get(URIConverter.OPTION_RESPONSE);
        if (response == null) {
            response = new HashMap<Object, Object>();
        }
        return response;
    }

    private static URIConverter getUriConverter(ResourceSet resourceSet) {
        URIConverter uriConverter = resourceSet.getURIConverter();
        return uriConverter;
    }

    private static boolean hasNoElementsNorTypes(XSDSchema schema) {
        if (schema == null) {
            return false;
        }

        return schema.getElementDeclarations().isEmpty() && schema.getTypeDefinitions().isEmpty();
    }

    private static void forceImport(XSDSchemaImpl schema) {
        if (schema != null) {
            for (XSDSchemaContent content : schema.getContents()) {
                if (content instanceof XSDImportImpl) {
                    XSDImportImpl importDirective = (XSDImportImpl) content;
                    schema.resolveSchema(importDirective.getNamespace());
                }
            }
        }
    }

    /**
     * Imports one schema into another.
     *
     * @param schema The schema being imported into.
     * @param importee The schema being imported.
     * @return The import object.
     */
    public static final XSDImport importSchema(XSDSchema schema, final XSDSchema importee)
            throws IOException {
        Resource resource = schema.eResource();
        if (resource == null) {
            final ResourceSet resourceSet = new ResourceSetImpl();
            resource = (XSDResourceImpl) resourceSet.createResource(URI.createURI(".xsd"));
            resource.getContents().add(schema);
        }

        XSDImport imprt = XSDFactory.eINSTANCE.createXSDImport();
        imprt.setNamespace(importee.getTargetNamespace());
        schema.getContents().add(imprt);

        List<XSDSchemaLocator> locators = new ArrayList<XSDSchemaLocator>();
        locators.add(
                new XSDSchemaLocator() {
                    public XSDSchema locateSchema(
                            XSDSchema xsdSchema,
                            String namespaceURI,
                            String rawSchemaLocationURI,
                            String resolvedSchemaLocationURI) {

                        if (importee.getTargetNamespace().equals(namespaceURI)) {
                            return importee;
                        }

                        return null;
                    }
                });
        AdapterFactory adapterFactory = new SchemaLocatorAdapterFactory(locators);
        resource.getResourceSet().getAdapterFactories().add(adapterFactory);
        return imprt;
    }

    /**
     * Remove all references to a schema It is important to call this method for every dynamic
     * schema created that is not needed anymore, because references in the static schema's will
     * otherwise keep it alive forever
     *
     * @param schema to be flushed
     */
    public static final void dispose(XSDSchema schema) {
        for (XSDSchemaContent content : schema.getContents()) {
            if (content instanceof XSDSchemaDirective) {
                XSDSchemaDirective directive = (XSDSchemaDirective) content;
                XSDSchema resolvedSchema = directive.getResolvedSchema();

                if (resolvedSchema != null) {
                    synchronized (Schemas.class) {
                        synchronized (resolvedSchema.eAdapters()) {
                            resolvedSchema.getReferencingDirectives().remove(directive);
                            for (XSDElementDeclaration dec :
                                    resolvedSchema.getElementDeclarations()) {
                                if (dec == null) {
                                    continue;
                                }
                                List<XSDElementDeclaration> toRemove =
                                        new ArrayList<XSDElementDeclaration>();
                                for (XSDElementDeclaration subs : dec.getSubstitutionGroup()) {
                                    if (subs != null
                                            && subs.getContainer() != null
                                            && subs.getContainer().equals(schema)) {
                                        toRemove.add(subs);
                                    }
                                }
                                dec.getSubstitutionGroup().removeAll(toRemove);
                            }
                        }
                    }
                }
            }
        }
    }

    public static final List validateImportsIncludes(String location) throws IOException {
        return validateImportsIncludes(location, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    public static final List validateImportsIncludes(
            String location, XSDSchemaLocator[] locators, XSDSchemaLocationResolver[] resolvers)
            throws IOException {
        return validateImportsIncludes(
                location,
                (locators != null) ? Arrays.asList(locators) : Collections.EMPTY_LIST,
                (resolvers != null) ? Arrays.asList(resolvers) : Collections.EMPTY_LIST);
    }

    public static final List validateImportsIncludes(String location, List locators, List resolvers)
            throws IOException {

        // create a parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            throw (IOException) new IOException("could not create parser").initCause(e);
        }

        SchemaImportIncludeValidator validator =
                new SchemaImportIncludeValidator(locators, resolvers);

        // queue of files to parse
        LinkedList q = new LinkedList();
        q.add(location);

        while (!q.isEmpty()) {
            location = (String) q.removeFirst();
            validator.setBaseLocation(location);

            try {
                parser.parse(location, validator);
            } catch (SAXException e) {
                throw (IOException) new IOException("parse error").initCause(e);
            }

            // check for errors
            if (!validator.errors.isEmpty()) {
                return validator.errors;
            }

            if (!validator.next.isEmpty()) {
                q.addAll(validator.next);
            }
        }

        return Collections.EMPTY_LIST;
    }

    static final class SchemaImportIncludeValidator extends DefaultHandler {

        /** base location */
        String baseLocation;
        /** locators for resolving to schemas directely */
        List locators;
        /** locators for resolving to absolute schema locations */
        List resolvers;
        /** tracking seen namespaces and schema locations */
        Set seen;
        /** list of errors encountered */
        List errors;
        /** next set of locations to process */
        List next;

        SchemaImportIncludeValidator(List locators, List resolvers) {
            this.locators = locators;
            this.resolvers = resolvers;
            seen = new HashSet();
            errors = new ArrayList();
            next = new ArrayList();
        }

        public void setBaseLocation(String baseLocation) {
            this.baseLocation = baseLocation;
        }

        public void startDocument() throws SAXException {
            next.clear();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            // process import
            if ("import".equals(localName)) {
                // get the namespace + location
                String namespace = attributes.getValue("namespace");
                String schemaLocation = attributes.getValue("schemaLocation");

                // do not validate if we have already seen this import
                if (seen.contains(namespace)) {
                    return;
                }
                seen.add(namespace);

                if (schemaLocation != null) {
                    // look for a locator or resolver that can handle it
                    for (Iterator l = locators.iterator(); l.hasNext(); ) {
                        XSDSchemaLocator locator = (XSDSchemaLocator) l.next();

                        // check for schema locator which canHandle
                        if (locator instanceof SchemaLocator) {
                            if (((SchemaLocator) locator)
                                    .canHandle(null, namespace, schemaLocation, null)) {
                                // cool, return here and do not recurse
                                return;
                            }
                        }
                    }

                    // resolve
                    String resolvedSchemaLocation = resolve(namespace, schemaLocation);
                    if (resolvedSchemaLocation != null) {
                        recurse(resolvedSchemaLocation);
                    } else {
                        errors.add("Could not resolve import: " + namespace + "," + schemaLocation);
                    }
                } else {
                    errors.add("No schemaLocation attribute for namespace import: " + namespace);
                }
            } else if ("include".equals(localName)) {
                // process include
                String location = attributes.getValue("location");
                String resolvedLocation = resolve(null, location);

                if (resolvedLocation != null) {
                    recurse(resolvedLocation);
                } else {
                    errors.add("Could not resolve include: " + location);
                }
            }
        }

        String resolve(String namespace, String location) {
            // look for a location resolver capable of handling it
            for (Iterator r = resolvers.iterator(); r.hasNext(); ) {
                XSDSchemaLocationResolver resolver = (XSDSchemaLocationResolver) r.next();
                if (resolver instanceof SchemaLocationResolver) {
                    if (((SchemaLocationResolver) resolver).canHandle(null, namespace, location)) {
                        // can handle, actually resolve and recurse
                        String resolvedSchemaLocation =
                                resolver.resolveSchemaLocation(null, namespace, location);
                        if (resolvedSchemaLocation != null) {
                            return resolvedSchemaLocation;
                        }
                    }
                }
            }

            // attempt tp resolve manuualy
            File file = new File(location);
            if (file.exists()) {
                return file.getAbsolutePath();
            } else {
                // try relative to base location
                if (!file.isAbsolute()) {
                    File dir = new File(baseLocation).getParentFile();
                    if (dir != null) {
                        file = new File(dir, location);
                        if (file.exists()) {
                            return file.getAbsolutePath();
                        }
                    }
                }
            }

            return null;
        }

        void recurse(String location) {
            // do not recurse if we have already processed this one
            if (seen.contains(location)) {
                return;
            }

            // mark it as seen
            seen.add(location);

            // add to queue for processing of in next round
            next.add(location);
        }
    }

    /**
     * Returns a list of all child element declarations of the specified type, no order is
     * guaranteed.
     *
     * @param type The type.
     * @return A list of @link XSDElementDeclaration objects, one for each child element.
     */
    public static final List getChildElementDeclarations(XSDTypeDefinition type) {
        return getChildElementDeclarations(type, true);
    }

    /**
     * Returns the particle for an element declaration that is part of a type.
     *
     * @param type The type definition.
     * @param name The naem of the child element declaration.
     * @param includeParents Flag to control wether parent types are included.
     * @return The particle representing the element declaration, or <code>null</code> if it could
     *     not be found.
     */
    public static final XSDParticle getChildElementParticle(
            XSDTypeDefinition type, String name, boolean includeParents) {
        List particles = getChildElementParticles(type, includeParents);

        for (Iterator p = particles.iterator(); p.hasNext(); ) {
            XSDParticle particle = (XSDParticle) p.next();
            XSDElementDeclaration element = (XSDElementDeclaration) particle.getContent();

            if (element.isElementDeclarationReference()) {
                element.getResolvedElementDeclaration();
            }

            if (name.equals(element.getName())) {
                return particle;
            }
        }

        return null;
    }

    /**
     * Returns a list of all child element particles that corresponde to element declarations of the
     * specified type, no order is guaranteed.
     *
     * <p>The <code>includeParents</code> flag controls if this method should returns those elements
     * defined on parent types.
     *
     * @param type THe type.
     * @param includeParents flag indicating if parent types should be processed
     * @return A list of {@link XSDParticle}.
     */
    public static final List getChildElementParticles(
            XSDTypeDefinition type, boolean includeParents) {
        final HashSet contents = new HashSet();
        final ArrayList particles = new ArrayList();
        TypeWalker.Visitor visitor =
                new TypeWalker.Visitor() {
                    public boolean visit(XSDTypeDefinition type) {
                        // simple types dont have children
                        if (type instanceof XSDSimpleTypeDefinition) {
                            return true;
                        }

                        XSDComplexTypeDefinition cType = (XSDComplexTypeDefinition) type;

                        ElementVisitor visitor =
                                new ElementVisitor() {
                                    public void visit(XSDParticle particle) {
                                        XSDElementDeclaration element =
                                                (XSDElementDeclaration) particle.getContent();

                                        if (element.isElementDeclarationReference()) {
                                            element = element.getResolvedElementDeclaration();
                                        }

                                        // make sure unique
                                        if (!contents.contains(element)) {
                                            contents.add(element);
                                            particles.add(particle);
                                        }
                                    }
                                };

                        visitElements(cType, visitor);

                        return true;
                    }
                };

        if (includeParents) {
            // walk up the type hierarchy of the element to generate a list of
            // possible elements
            new TypeWalker().rwalk(type, visitor);
        } else {
            // just visit this type
            visitor.visit(type);
        }

        return new ArrayList(particles);
    }

    /**
     * Returns a list of all xs:any element particles that correspond to element declarations of the
     * specified type.
     *
     * @param type The type.
     * @return A list of {@link XSDParticle}.
     */
    public static final List getAnyElementParticles(XSDTypeDefinition type) {
        final HashSet contents = new HashSet();
        final ArrayList particles = new ArrayList();
        TypeWalker.Visitor visitor =
                new TypeWalker.Visitor() {
                    public boolean visit(XSDTypeDefinition type) {
                        // simple types doesn't have children
                        if (type instanceof XSDSimpleTypeDefinition) {
                            return true;
                        }

                        XSDComplexTypeDefinition cType = (XSDComplexTypeDefinition) type;

                        ElementVisitor visitor =
                                new ElementVisitor() {
                                    public void visit(XSDParticle particle) {
                                        XSDWildcard element = (XSDWildcard) particle.getContent();

                                        // make sure unique
                                        if (!contents.contains(element)) {
                                            contents.add(element);
                                            particles.add(particle);
                                        }
                                    }
                                };

                        visitAnyElements(cType, visitor);

                        return true;
                    }
                };

        // just visit this type
        visitor.visit(type);

        return new ArrayList(particles);
    }

    /**
     * Method to visit xs:any element. The method differs from visitElements only by the fact that
     * it visits xs:any rather than <element>
     *
     * @param cType XSDComplexTypeDefinition
     * @param visitor ElementVisitor
     */
    private static void visitAnyElements(XSDComplexTypeDefinition cType, ElementVisitor visitor) {
        // simple content cant define children
        if ((cType.getContent() == null)
                || (cType.getContent() instanceof XSDSimpleTypeDefinition)) {
            return;
        }

        // use a queue to simulate the recursion
        LinkedList queue = new LinkedList();
        queue.addLast(cType.getContent());

        while (!queue.isEmpty()) {
            XSDParticle particle = (XSDParticle) queue.removeFirst();

            // analyze type of particle content
            int pType = XSDUtil.nodeType(particle.getElement());

            if (pType == XSDConstants.ANY_ELEMENT) {
                visitor.visit(particle);
            } else {
                // model group
                XSDModelGroup grp = null;

                switch (pType) {
                    case XSDConstants.GROUP_ELEMENT:
                        XSDModelGroupDefinition grpDef =
                                (XSDModelGroupDefinition) particle.getContent();

                        if (grpDef.isModelGroupDefinitionReference()) {
                            grpDef = grpDef.getResolvedModelGroupDefinition();
                        }

                        grp = grpDef.getModelGroup();

                        break;

                    case XSDConstants.CHOICE_ELEMENT:
                    case XSDConstants.ALL_ELEMENT:
                    case XSDConstants.SEQUENCE_ELEMENT:
                        grp = (XSDModelGroup) particle.getContent();

                        break;
                }

                if (grp != null) {
                    // enque all particles in the group
                    List parts = grp.getParticles();

                    // TODO: this check isa bit hacky.. .figure out why this is the case
                    if (parts.isEmpty()) {
                        parts = grp.getContents();
                    }

                    // add in reverse order to front of queue to maintain order
                    for (int i = parts.size() - 1; i >= 0; i--) {
                        queue.addFirst(parts.get(i));
                    }
                }
            }
        } // while
    }

    /**
     * Returns a list of all child element declarations of the specified type, no order is
     * guaranteed.
     *
     * <p>The <code>includeParents</code> flag controls if this method should returns those elements
     * defined on parent types.
     *
     * @param type The type
     * @param includeParents flag indicating if parent types should be processed
     * @return A list of @link XSDElementDeclaration objects, one for each child element.
     */
    public static final List getChildElementDeclarations(
            XSDTypeDefinition type, boolean includeParents) {
        List particles = getChildElementParticles(type, includeParents);
        List elements = new ArrayList();

        for (Iterator p = particles.iterator(); p.hasNext(); ) {
            XSDParticle particle = (XSDParticle) p.next();
            XSDElementDeclaration decl = (XSDElementDeclaration) particle.getContent();

            if (decl.isElementDeclarationReference()) {
                decl = decl.getResolvedElementDeclaration();
            }

            elements.add(decl);
        }

        return elements;
    }

    /**
     * Returns the base type defintion of <code>type</code> named <code>parentTypeName<code>.
     * <p>
     * This method will handle the case in which the <code>parentTypeName == type.getTypeName()</code>.
     * If no such parent type is found this method will return <code>null</code>.
     * </p>
     * @param type The type.
     * @param parentTypeName The name of the base type to return.
     *
     * @return The base type, or null if it could not be found.
     */
    public static final XSDTypeDefinition getBaseTypeDefinition(
            XSDTypeDefinition type, final QName parentTypeName) {
        final List found = new ArrayList();

        TypeWalker.Visitor visitor =
                new TypeWalker.Visitor() {
                    public boolean visit(XSDTypeDefinition type) {
                        if (nameMatches(type, parentTypeName)) {
                            found.add(type);

                            return false;
                        }

                        return true;
                    }
                };

        new TypeWalker().walk(type, visitor);

        return found.isEmpty() ? null : (XSDTypeDefinition) found.get(0);
    }

    /**
     * Determines if the type of an element is a sub-type of another element.
     *
     * @param e1 The element.
     * @param e2 The element to be tested as a base type.
     * @since 2.5
     */
    public static final boolean isBaseType(XSDElementDeclaration e1, XSDElementDeclaration e2) {
        XSDTypeDefinition type = e1.getType();
        while (type != null) {
            if (type.equals(e2.getType())) {
                return true;
            }

            if (type.equals(type.getBaseType())) {
                break;
            }

            type = type.getBaseType();
        }

        return false;
    }
    /**
     * Returns the minimum number of occurences of an element within a complex type.
     *
     * @param type The type definition containg the declaration <code>element</code>
     * @param element The declaration of the element.
     * @return The minimum number of occurences.
     * @throws IllegalArgumentException If the element declaration cannot be locaated withing the
     *     type definition.
     */
    public static final int getMinOccurs(
            XSDComplexTypeDefinition type, XSDElementDeclaration element) {
        final XSDElementDeclaration fElement = element;
        final ArrayList minOccurs = new ArrayList();

        ElementVisitor visitor =
                new ElementVisitor() {
                    public void visit(XSDParticle particle) {
                        XSDElementDeclaration decl = (XSDElementDeclaration) particle.getContent();

                        if (decl.isElementDeclarationReference()) {
                            decl = decl.getResolvedElementDeclaration();
                        }

                        if (decl == fElement) {
                            if (particle.isSetMinOccurs()) {
                                minOccurs.add(Integer.valueOf(particle.getMinOccurs()));
                            } else if (particle.getContainer() instanceof XSDModelGroup
                                    && particle.getContainer().getContainer()
                                            instanceof XSDParticle) {
                                particle = (XSDParticle) particle.getContainer().getContainer();
                                minOccurs.add(Integer.valueOf(particle.getMinOccurs()));
                            } else {
                                minOccurs.add(1);
                            }
                        }
                    }
                };

        visitElements(type, visitor, true);

        if (minOccurs.isEmpty()) {
            throw new IllegalArgumentException(
                    "Element: " + element + " not found in type: " + type);
        }

        return ((Integer) minOccurs.get(0)).intValue();
    }

    /**
     * Returns the minimum number of occurences of an element within a complex type.
     *
     * @param type The type definition containg the declaration <code>element</code>
     * @param element The declaration of the element.
     * @return The minimum number of occurences.
     * @throws IllegalArgumentException If the element declaration cannot be locaated withing the
     *     type definition.
     */
    public static final int getMaxOccurs(
            XSDComplexTypeDefinition type, XSDElementDeclaration element) {
        final XSDElementDeclaration fElement = element;
        final ArrayList maxOccurs = new ArrayList();

        ElementVisitor visitor =
                new ElementVisitor() {
                    public void visit(XSDParticle particle) {
                        XSDElementDeclaration decl = (XSDElementDeclaration) particle.getContent();

                        if (decl.isElementDeclarationReference()) {
                            decl = decl.getResolvedElementDeclaration();
                        }

                        if (decl == fElement) {
                            if (particle.isSetMaxOccurs()) {
                                maxOccurs.add(Integer.valueOf(particle.getMaxOccurs()));
                            } else if (particle.getContainer() instanceof XSDModelGroup
                                    && particle.getContainer().getContainer()
                                            instanceof XSDParticle) {
                                particle = (XSDParticle) particle.getContainer().getContainer();
                                maxOccurs.add(Integer.valueOf(particle.getMaxOccurs()));
                            } else {
                                maxOccurs.add(1);
                            }
                        }
                    }
                };

        visitElements(type, visitor, true);

        if (maxOccurs.isEmpty()) {
            throw new IllegalArgumentException(
                    "Element: " + element + " not found in type: " + type);
        }

        return ((Integer) maxOccurs.get(0)).intValue();
    }

    private static void visitElements(
            XSDComplexTypeDefinition cType, ElementVisitor visitor, boolean includeParents) {
        if (includeParents) {
            LinkedList baseTypes = new LinkedList();
            XSDTypeDefinition baseType = cType.getBaseType();

            while ((baseType != null) && (baseType != baseType.getBaseType())) {
                if (baseType instanceof XSDComplexTypeDefinition) {
                    baseTypes.addLast(baseType);
                }

                baseType = baseType.getBaseType();
            }

            for (Iterator it = baseTypes.iterator(); it.hasNext(); ) {
                baseType = (XSDTypeDefinition) it.next();
                visitElements((XSDComplexTypeDefinition) baseType, visitor);
            }
        }

        visitElements(cType, visitor);
    }

    private static void visitElements(XSDComplexTypeDefinition cType, ElementVisitor visitor) {
        // simple content cant define children
        if ((cType.getContent() == null)
                || (cType.getContent() instanceof XSDSimpleTypeDefinition)) {
            return;
        }

        // use a queue to simulate the recursion
        LinkedList queue = new LinkedList();
        queue.addLast(cType.getContent());

        while (!queue.isEmpty()) {
            XSDParticle particle = (XSDParticle) queue.removeFirst();

            // analyze type of particle content
            int pType = XSDUtil.nodeType(particle.getElement());

            if (pType == XSDConstants.ELEMENT_ELEMENT) {
                visitor.visit(particle);
            } else {
                // model group
                XSDModelGroup grp = null;

                switch (pType) {
                    case XSDConstants.GROUP_ELEMENT:
                        XSDModelGroupDefinition grpDef =
                                (XSDModelGroupDefinition) particle.getContent();

                        if (grpDef.isModelGroupDefinitionReference()) {
                            grpDef = grpDef.getResolvedModelGroupDefinition();
                        }

                        grp = grpDef.getModelGroup();

                        break;

                    case XSDConstants.CHOICE_ELEMENT:
                    case XSDConstants.ALL_ELEMENT:
                    case XSDConstants.SEQUENCE_ELEMENT:
                        grp = (XSDModelGroup) particle.getContent();

                        break;
                }

                if (grp != null) {
                    // enque all particles in the group
                    List parts = grp.getParticles();

                    // TODO: this check isa  bit hacky.. .figure out why this is the case
                    if (parts.isEmpty()) {
                        parts = grp.getContents();
                    }

                    // add in reverse order to front of queue to maintain order
                    for (int i = parts.size() - 1; i >= 0; i--) {
                        queue.addFirst(parts.get(i));
                    }
                }
            }
        } // while
    }

    /**
     * Returns an element declaration that is contained in the type of another element declaration.
     * The following strategy is used to locate the child element declaration.
     *
     * <ol>
     *   <li>The immediate children of the specified element are examined, if a match is found, it
     *       is returned. If 1. does not match, global elements that derive from the immediate
     *       children are examined.
     * </ol>
     *
     * @param parent the containing element declaration
     * @param qName the qualified name of the contained element
     * @return The contained element declaration, or false if containment is not satisfied.
     */
    public static final XSDElementDeclaration getChildElementDeclaration(
            XSDElementDeclaration parent, QName qName) {
        // look for a match in a direct child
        List children = getChildElementDeclarations(parent.getType());

        for (Iterator itr = children.iterator(); itr.hasNext(); ) {
            XSDElementDeclaration element = (XSDElementDeclaration) itr.next();

            if (nameMatches(element, qName)) {
                return element;
            }
        }

        // couldn't find one, look for match in derived elements
        ArrayList derived = new ArrayList();

        for (Iterator itr = children.iterator(); itr.hasNext(); ) {
            XSDElementDeclaration child = (XSDElementDeclaration) itr.next();
            derived.addAll(getDerivedElementDeclarations(child));
        }

        for (Iterator itr = derived.iterator(); itr.hasNext(); ) {
            XSDElementDeclaration child = (XSDElementDeclaration) itr.next();

            if (nameMatches(child, qName)) {
                return child;
            }
        }

        return null;
    }

    /**
     * Returns a list of all top level elements that are of a type derived from the type of the
     * specified element.
     *
     * @param element The element.
     * @return All elements which are of a type derived from the type of the specified element.
     */
    public static final List getDerivedElementDeclarations(XSDElementDeclaration element) {
        List elements = element.getSchema().getElementDeclarations();
        List derived = new ArrayList();

        for (Iterator itr = elements.iterator(); itr.hasNext(); ) {
            XSDElementDeclaration derivee = (XSDElementDeclaration) itr.next();

            if (derivee.equals(element)) {
                continue; // same element
            }

            XSDTypeDefinition type = derivee.getType();

            while (true) {
                if (type.equals(element.getType())) {
                    derived.add(derivee);

                    break;
                }

                if (type.equals(type.getBaseType())) {
                    break;
                }

                type = type.getBaseType();
            }
        }

        return derived;
    }

    /**
     * Returns a list of all attribute declarations declared in the type (or any base type) of the
     * specified element.
     *
     * <p>This method is just a shortcut for {@link #getAttributeDeclarations(XSDTypeDefinition)
     * getAttributeDeclarations(element.getType()}
     *
     * @param element The element.
     * @return A list of @link XSDAttributeDeclaration objects, one for each attribute of the
     *     element.
     */
    public static final List getAttributeDeclarations(XSDElementDeclaration element) {
        return getAttributeDeclarations(element.getType());
    }

    /**
     * Returns a list of all attribute declarations declared in the type (or any base type) of the
     * specified element.
     *
     * @param type The type definition
     * @return A list of @link XSDAttributeDeclaration objects, one for each attribute of the
     *     element.
     */
    public static final List getAttributeDeclarations(XSDTypeDefinition type) {
        return getAttributeDeclarations(type, true);
    }

    /**
     * Returns a list of all attribute declarations declared in the type (and optionally any base
     * type) of the specified element.
     *
     * @param type The element type
     * @param includeParents Wether to include parent types.
     * @return A list of @link XSDAttributeDeclaration objects, one for each attribute of the
     *     element.
     */
    public static final List getAttributeDeclarations(
            XSDTypeDefinition type, boolean includeParents) {
        final ArrayList attributes = new ArrayList();

        // walk up the type hierarchy of the element to generate a list of atts
        TypeWalker.Visitor visitor =
                new TypeWalker.Visitor() {
                    public boolean visit(XSDTypeDefinition type) {
                        // simple types dont have attributes
                        if (type instanceof XSDSimpleTypeDefinition) {
                            return true;
                        }

                        XSDComplexTypeDefinition cType = (XSDComplexTypeDefinition) type;

                        // get all the attribute content (groups,or uses) and add to q
                        List attContent = cType.getAttributeContents();

                        for (Iterator itr = attContent.iterator(); itr.hasNext(); ) {
                            XSDAttributeGroupContent content =
                                    (XSDAttributeGroupContent) itr.next();

                            if (content instanceof XSDAttributeUse) {
                                // an attribute, add it to the list
                                XSDAttributeUse use = (XSDAttributeUse) content;
                                attributes.add(use.getAttributeDeclaration());
                            } else if (content instanceof XSDAttributeGroupDefinition) {
                                // attribute group, add all atts in group to list
                                XSDAttributeGroupDefinition attGrp =
                                        (XSDAttributeGroupDefinition) content;

                                if (attGrp.isAttributeGroupDefinitionReference()) {
                                    attGrp = attGrp.getResolvedAttributeGroupDefinition();
                                }

                                List uses = attGrp.getAttributeUses();

                                for (Iterator aitr = uses.iterator(); aitr.hasNext(); ) {
                                    XSDAttributeUse use = (XSDAttributeUse) aitr.next();
                                    attributes.add(use.getAttributeDeclaration());
                                }
                            }
                        }

                        return true;
                    }
                };

        if (includeParents) {
            // walk up the type hierarchy of the element to generate a list of
            // possible elements
            new TypeWalker().walk(type, visitor);
        } else {
            // just visit this type
            visitor.visit(type);
        }

        return attributes;
    }

    /**
     * Returns an attribute declaration that is contained in the type of another element
     * declaration.
     *
     * @param element The containing element declaration.
     * @param qName The qualified name of the contained attribute
     * @return The contained attribute declaration, or false if containment is not satisfied.
     */
    public static final XSDAttributeDeclaration getAttributeDeclaration(
            XSDElementDeclaration element, QName qName) {
        List atts = getAttributeDeclarations(element);

        for (Iterator itr = atts.iterator(); itr.hasNext(); ) {
            XSDAttributeDeclaration att = (XSDAttributeDeclaration) itr.next();

            if (nameMatches(att, qName)) {
                return att;
            }
        }

        return null;
    }

    /**
     * Returns a flat list of imports from the specified schema.
     *
     * <p>The method recurses into imported schemas. The list returned is filtered so that duplicate
     * includes are removed. Two includes are considered equal if they have the same target
     * namespace.
     *
     * @param schema The top-level schema.
     * @return A list containing objects of type {@link XSDImport}.
     */
    public static final List getImports(XSDSchema schema) {
        LinkedList queue = new LinkedList();
        ArrayList imports = new ArrayList();
        HashSet added = new HashSet();

        queue.addLast(schema);

        while (!queue.isEmpty()) {
            schema = (XSDSchema) queue.removeFirst();

            List contents = schema.getContents();

            for (Iterator itr = contents.iterator(); itr.hasNext(); ) {
                XSDSchemaContent content = (XSDSchemaContent) itr.next();

                if (content instanceof XSDImport) {
                    XSDImport imprt = (XSDImport) content;

                    if (!added.contains(imprt.getNamespace())) {
                        imports.add(imprt);
                        added.add(imprt.getNamespace());

                        XSDSchema resolvedSchema = imprt.getResolvedSchema();
                        if (resolvedSchema == null) {
                            LOGGER.info(
                                    "Schema import wasn't resolved: "
                                            + imprt.getNamespace()
                                            + " declared location: "
                                            + imprt.getSchemaLocation());
                        } else {
                            queue.addLast(resolvedSchema);
                        }
                    }
                }
            }
        }

        return imports;
    }

    /**
     * Returns a flat list of includes from the specified schema.
     *
     * <p>The method recurses into included schemas. The list returned is filtered so that duplicate
     * includes are removed. Two includes are considered equal if they have the same uri location
     *
     * @param schema The top-level schema.
     * @return A list containing objects of type {@link XSDInclude}.
     */
    public static final List getIncludes(XSDSchema schema) {
        LinkedList queue = new LinkedList();
        ArrayList includes = new ArrayList();
        HashSet added = new HashSet();

        queue.addLast(schema);

        while (!queue.isEmpty()) {
            schema = (XSDSchema) queue.removeFirst();

            List contents = schema.getContents();

            for (Iterator itr = contents.iterator(); itr.hasNext(); ) {
                XSDSchemaContent content = (XSDSchemaContent) itr.next();

                if (content instanceof XSDInclude) {
                    XSDInclude include = (XSDInclude) content;

                    if (!added.contains(include.getSchemaLocation())) {
                        includes.add(include);
                        added.add(include.getSchemaLocation());

                        if (include.getIncorporatedSchema() != null) {
                            queue.addLast(include.getIncorporatedSchema());
                        } else {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.fine("include: " + include + " resulted in null schema");
                            }
                        }
                    }
                }
            }
        }

        return includes;
    }

    /**
     * Searches <code>schema</code> for an element which matches <code>name</code>.
     *
     * @param schema The schema
     * @param name The element to search for
     * @return The element declaration, or null if it could not be found.
     */
    public static XSDElementDeclaration getElementDeclaration(XSDSchema schema, QName name) {
        for (Iterator e = schema.getElementDeclarations().iterator(); e.hasNext(); ) {
            XSDElementDeclaration element = (XSDElementDeclaration) e.next();

            if (element.getTargetNamespace().equals(name.getNamespaceURI())) {
                if (element.getName().equals(name.getLocalPart())) {
                    return element;
                }
            }
        }

        return null;
    }

    /**
     * Method for comparing the name of a schema component to a qualified name. The component name
     * and the qualified name match if both the namespaces match, and the local parts match.
     * Prefixes are ignored. Two strings will match if one of the following conditions hold.
     *
     * <ul>
     *   <li>Both strings are null.
     *   <li>Both strings are the empty string.
     *   <li>One string is null, and the other is the empty string.
     *   <li>Both strings are non-null and non-empty and equals() return true.
     * </ul>
     *
     * @param component The component in question.
     * @param qName The qualifined name.
     */
    public static final boolean nameMatches(XSDNamedComponent component, QName qName) {
        String ns1 = component.getTargetNamespace();
        String ns2 = qName.getNamespaceURI();
        String n1 = component.getName();
        String n2 = qName.getLocalPart();

        ns1 = "".equals(ns1) ? null : ns1;
        ns2 = "".equals(ns2) ? null : ns2;
        n1 = "".equals(n1) ? null : n1;
        n2 = "".equals(n2) ? null : n2;

        if ((ns1 == null) && (ns2 != null)) {
            // try the default namespace
            if (component.getSchema() != null) {
                ns1 = component.getSchema().getTargetNamespace();

                if ("".equals(ns1)) {
                    ns1 = null;
                }
            }
        }

        return Utilities.equals(ns1, ns2) && Utilities.equals(n1, n2);

        //
        //        //is this the element we are looking for
        //        if ((component.getTargetNamespace() == null)
        //                || "".equals(component.getTargetNamespace())) {
        //            if ((qName.getNamespaceURI() == null)
        //                    || "".equals(qName.getNamespaceURI())) {
        //                //do a local name match
        //            	String n1 = component.getName();
        //            	if ( "".equals( n1 ) ) {
        //            		n1 = null;
        //            	}
        //            	String n2 = qName.getLocalPart();
        //            	if ( "".equals( n2 ) ) {
        //            		n2 = null;
        //            	}
        //                return (n1 == null && n2 == null) || n1.equals( n2 );
        //            }
        //
        //            //assume default namespace
        //            if (component.getSchema().getTargetNamespace()
        //                             .equals(qName.getNamespaceURI())
        //                    && component.getName().equals(qName.getLocalPart())) {
        //                return true;
        //            }
        //        } else if (component.getTargetNamespace().equals(qName.getNamespaceURI())
        //                && component.getName().equals(qName.getLocalPart())) {
        //            return true;
        //        }
    }

    /**
     * Returns the namespace prefix mapped to the targetNamespace of the schema.
     *
     * @param schema The schema in question.
     * @return The namesapce prefix, or <code>null</code> if not found.
     */
    public static String getTargetPrefix(XSDSchema schema) {
        String ns = schema.getTargetNamespace();
        Map pre2ns = schema.getQNamePrefixToNamespaceMap();

        for (Iterator itr = pre2ns.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry entry = (Map.Entry) itr.next();

            if (entry.getKey() == null) {
                continue; // default prefix
            }

            if (entry.getValue().equals(ns)) {
                return (String) entry.getKey();
            }
        }

        return null;
    }

    /**
     * Obtains all instances of a particular class from a container by navigating up the container
     * hierachy.
     *
     * @param container The container.
     * @param clazz The class.
     * @return A list of all instances of <code>clazz</code>, or the empty list if none found.
     */
    public static List getComponentInstancesOfType(PicoContainer container, Class clazz) {
        List instances = new ArrayList();

        while (container != null) {
            List l = container.getComponentInstancesOfType(clazz);
            instances.addAll(l);
            container = container.getParent();
        }

        return instances;
    }

    /**
     * Obtains all component adapters of a particular class from a container by navigating up the
     * container hierarchy.
     *
     * @param container The container.
     * @param clazz The class.
     * @return A list of all adapters for components of class <code>clazz</code>, or the empty list
     *     if none found.
     */
    public static List<ComponentAdapter> getComponentAdaptersOfType(
            PicoContainer container, Class clazz) {
        List instances = new ArrayList();

        while (container != null) {
            List l = container.getComponentAdaptersOfType(clazz);
            instances.addAll(l);
            container = container.getParent();
        }

        return instances;
    }

    /**
     * Unregisters a component in the container and all parent containers.
     *
     * @param container The container.
     * @param key The key of the component.
     */
    public static void unregisterComponent(PicoContainer container, final Object key) {
        // go to the top of the hierachy
        while (container.getParent() != null) {
            container = container.getParent();
        }

        container.accept(
                new PicoVisitor() {
                    public Object traverse(Object node) {
                        return null;
                    }

                    public void visitContainer(PicoContainer container) {
                        if (container instanceof MutablePicoContainer) {
                            ((MutablePicoContainer) container).unregisterComponent(key);
                        }
                    }

                    public void visitComponentAdapter(ComponentAdapter adapter) {}

                    public void visitParameter(Parameter parameter) {}
                });
    }

    /** Returns the name of the element represented by the particle as a QName. */
    public static QName getParticleName(XSDParticle particle) {
        XSDElementDeclaration content = (XSDElementDeclaration) particle.getContent();
        if (content.isElementDeclarationReference()) {
            content = content.getResolvedElementDeclaration();
        }
        return new QName(content.getTargetNamespace(), content.getName());
    }

    /**
     * Simple visitor interface for visiting elements which are part of complex types. This
     * interface is private api because there is probably a better way of visiting the contents of a
     * type.
     *
     * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
     */
    private static interface ElementVisitor {
        /** The particle containing the element. */
        void visit(XSDParticle element);
    }

    static class SchemaLocatorAdapterFactory extends AdapterFactoryImpl {
        SchemaLocatorAdapter adapter;

        public SchemaLocatorAdapterFactory(List /*<XSDSchemaLocator>*/ locators) {
            adapter = new SchemaLocatorAdapter(locators);
        }

        public boolean isFactoryForType(Object type) {
            return type == XSDSchemaLocator.class;
        }

        public Adapter adaptNew(Notifier notifier, Object type) {
            return adapter;
        }
    }

    static class SchemaLocatorAdapter extends AdapterImpl implements XSDSchemaLocator {
        List /*<XSDSchemaLocator>*/ locators;

        public SchemaLocatorAdapter(List /*<XSDSchemaLocator>*/ locators) {
            this.locators = new ArrayList(locators);
        }

        public boolean isAdapterForType(Object type) {
            return type == XSDSchemaLocator.class;
        }

        public XSDSchema locateSchema(
                XSDSchema xsdSchema,
                String namespaceURI,
                String rawSchemaLocationURI,
                String resolvedSchemaLocationURI) {
            for (int i = 0; i < locators.size(); i++) {
                XSDSchemaLocator locator = (XSDSchemaLocator) locators.get(i);
                XSDSchema schema =
                        locator.locateSchema(
                                xsdSchema,
                                namespaceURI,
                                rawSchemaLocationURI,
                                resolvedSchemaLocationURI);

                if (schema != null) {
                    return schema;
                }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Could not locate schema for: " + rawSchemaLocationURI + ".");
            }

            return null;
        }
    }

    static class SchemaLocationResolverAdapterFactory extends AdapterFactoryImpl {
        SchemaLocationResolverAdapter adapter;

        public SchemaLocationResolverAdapterFactory(
                List /*<XSDSchemaLocationResolver>*/ resolvers) {
            adapter = new SchemaLocationResolverAdapter(resolvers);
        }

        public boolean isFactoryForType(Object type) {
            return type == XSDSchemaLocationResolver.class;
        }

        public Adapter adaptNew(Notifier notifier, Object type) {
            return adapter;
        }
    }

    static class SchemaLocationResolverAdapter extends AdapterImpl
            implements XSDSchemaLocationResolver {
        List /*<XSDSchemaLocationResolver>*/ resolvers;

        public SchemaLocationResolverAdapter(List /*<XSDSchemaLocationResolver>*/ resolvers) {
            this.resolvers = new ArrayList(resolvers);
        }

        public boolean isAdapterForType(Object type) {
            return type == XSDSchemaLocationResolver.class;
        }

        public String resolveSchemaLocation(
                XSDSchema schema, String namespaceURI, String rawSchemaLocationURI) {
            for (int i = 0; i < resolvers.size(); i++) {
                XSDSchemaLocationResolver resolver = (XSDSchemaLocationResolver) resolvers.get(i);
                String resolved =
                        resolver.resolveSchemaLocation(schema, namespaceURI, rawSchemaLocationURI);

                if (resolved != null) {
                    return resolved;
                }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Could not resolve schema location: "
                                + rawSchemaLocationURI
                                + " to physical location.");
            }

            return null;
        }
    }
}
