/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.xml.sax.SAXException;

/**
 * This is the main entry point into the XSI parsing routines.
 *
 * <p>Example Use:
 *
 * <pre>
 * Schema x = SchemaFactory.getInstance(&quot;MyTargetNameSpace&quot;,new URI(&quot;MyNameSpaceURI&quot;);
 * </pre>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class SchemaFactory {

    private static final Logger LOGGER = Logger.getLogger(SchemaFactory.class.getName());

    protected static SchemaFactory is = new SchemaFactory();

    /*
     * Holds onto instances when they are created ... my version of object
     * pooling Q: is there a better way? might suck up too much memory ... A:
     * not really, but the JVM might be better ... use the class to make
     * instances
     */
    private Map<URI, Schema> schemas = loadSchemas();

    /** Schema Resolver: uses local versions of schemas or cached ones if available. */
    File cacheDir;

    private SchemaResolver resolver;

    /*
     * The SAX parser to use if one is required ... isn't loaded until first
     * use.
     */
    private SAXParser parser;

    /** Default constructor. */
    protected SchemaFactory() {
        super();
        initResolver();
    }

    /** Initialize the schema resolver. */
    private void initResolver() {
        try {
            if (System.getProperty("schema.factory.cache", null) == null) {
                File tempFolder = File.createTempFile("schema", "cache");
                tempFolder.delete();
                tempFolder.mkdirs();
                cacheDir = tempFolder;
            } else {
                cacheDir = new File(System.getProperty("schema.factory.cache"));
            }

            resolver = new SchemaResolver(new SchemaCache(cacheDir, true));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    protected static SchemaFactory getInstance() {
        return is;
    }

    /*
     */
    private Map<URI, Schema> loadSchemas() {
        schemas = new HashMap<>();

        ClassLoader[] cls = findLoaders();
        String serviceId = "META-INF/services/" + Schema.class.getName();

        for (ClassLoader cl : cls) {
            try {
                Enumeration e = cl.getResources(serviceId);

                while (e.hasMoreElements()) {
                    URL res = (URL) e.nextElement();
                    try (BufferedReader rd =
                            new BufferedReader(new InputStreamReader(res.openStream(), "UTF" + "-8"))) {

                        while (rd.ready()) {
                            String factoryClassName = rd.readLine().trim();

                            try {
                                Schema s = (Schema) cl.loadClass(factoryClassName)
                                        .getDeclaredMethod("getInstance", new Class[0])
                                        .invoke(null, new Object[0]);
                                schemas.put(s.getTargetNamespace(), s);
                            } catch (IllegalArgumentException
                                    | ClassNotFoundException
                                    | NoSuchMethodException
                                    | InvocationTargetException
                                    | IllegalAccessException
                                    | SecurityException e1) {
                                XSISAXHandler.logger.warning(e1.toString());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                XSISAXHandler.logger.warning(e.toString());
            }
        }

        return schemas;
    }

    // stolen from FactoryFinder.findLoaders
    private ClassLoader[] findLoaders() {
        // lets get a class loader. By using the Thread's class loader, we allow
        // for more flexability.
        ClassLoader contextLoader = null;

        try {
            contextLoader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException se) {
            // do nothing
        }

        ClassLoader systemLoader = ReferencingFactoryFinder.class.getClassLoader();

        ClassLoader[] classLoaders;

        if (contextLoader == null || contextLoader == systemLoader) {
            classLoaders = new ClassLoader[1];
            classLoaders[0] = systemLoader;
        } else {
            classLoaders = new ClassLoader[2];
            classLoaders[0] = contextLoader;
            classLoaders[1] = systemLoader;
        }

        return classLoaders;
    }

    /**
     * Returns an instance of the desired class. There is no provision for: a) same instances each call b) different
     * instances each call c) this factory being thread safe
     *
     * @param desiredSchema URI the uri of which you want a schema instance.
     * @return Schema an instance of the desired schema.
     */
    public static Schema getInstance(URI targetNamespace, URI desiredSchema) throws SAXException {
        return getInstance(targetNamespace, desiredSchema, Level.WARNING);
    }

    public static Schema getInstance(URI targetNamespace, InputStream is1) throws SAXException {
        return getInstance(targetNamespace, is1, Level.WARNING);
    }

    public static synchronized Schema getInstance(String targetNamespace) {
        try {
            URI uri = new URI(targetNamespace);
            return getInstance(uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    /**
     * Returns an instance of the targetNamespace if it can be found ... null otherwise. targetNamespaces which can be
     * found are either hard-coded namespaces (SchemaFactory.properties), have already been parsed or were registered.
     *
     * @see #registerSchema(Strin,Schema)
     */
    public static synchronized Schema getInstance(URI targetNamespace) {
        return getInstance().getRealInstance(targetNamespace);
    }

    // TODO cache this on schema registry
    public static synchronized Schema[] getSchemas(String prefix) {
        if (prefix == null) return null;
        SchemaFactory sf = getInstance();
        Iterator i = sf.schemas.values().iterator();
        List<Schema> l = new LinkedList<>();
        while (i.hasNext()) {
            Schema s = (Schema) i.next();
            if (prefix.equals(s.getPrefix())) l.add(s);
        }
        return l.toArray(new Schema[l.size()]);
    }

    private synchronized Schema getRealInstance(URI targetNamespace) {
        Schema r = schemas.get(targetNamespace);

        if (r != null) {
            return r;
        }

        //        if (mappings.containsKey(targetNamespace)) {
        //            ClassLoader cl = SchemaFactory.class.getClassLoader();
        //
        //            try {
        //                Class c = cl.loadClass((String) mappings.get(targetNamespace));
        //                r = (Schema) c.getConstructor(new Class[0]).newInstance(new Object[0]);
        //                schemas.put(targetNamespace, r);
        //
        //                return r;
        //            } catch (Exception e) {
        //                return null;
        //            }
        //        }
        return null;
    }

    /**
     * Returns an instance of the desired class. There is no provision for: a) same instances each call b) different
     * instances each call c) this factory being thread safe
     *
     * @param targetNamespace The targetNamespace to search for.
     * @param desiredSchema URI the uri of which you want a schema instance.
     * @param level Level
     * @return Schema an instance of the desired schema.
     * @throws SAXException When something goes wrong
     */
    public static synchronized Schema getInstance(URI targetNamespace, URI desiredSchema, Level level)
            throws SAXException {
        return getInstance().getRealInstance(targetNamespace, desiredSchema, level);
    }

    private synchronized Schema getRealInstance(URI targetNamespace2, URI desiredSchema, Level level)
            throws SAXException {
        URI targetNamespace = targetNamespace2;
        if (targetNamespace == null || schemas.get(targetNamespace) == null) {
            setParser();

            XSISAXHandler contentHandler = getSAXHandler(desiredSchema);
            XSISAXHandler.setLogLevel(level);

            try {
                parser.parse(resolveSchema(desiredSchema), contentHandler);
            } catch (IOException e) {
                // tries to parse directly from original URL
                try {
                    parser.parse(desiredSchema.toString(), contentHandler);
                } catch (IOException e1) {
                    throw new SAXException(e1);
                }
            }

            Schema schema = contentHandler.getSchema();
            if (targetNamespace == null || "".equals(targetNamespace.toString())) {
                targetNamespace = schema.getTargetNamespace();
            }

            if (schemas.get(targetNamespace) != null) {
                schema = merge(schema, schemas.get(targetNamespace));
            }
            schemas.put(targetNamespace, schema);
            return schema;
        } else {
            if (!schemas.get(targetNamespace).includesURI(desiredSchema)) {
                Schema sh = schemas.get(targetNamespace);
                setParser();

                XSISAXHandler contentHandler = getSAXHandler(desiredSchema);
                XSISAXHandler.setLogLevel(level);

                try {
                    parser.parse(resolveSchema(desiredSchema), contentHandler);
                } catch (IOException e) {
                    throw new SAXException(e);
                }

                sh = merge(sh, contentHandler.getSchema());
                schemas.put(targetNamespace, sh); // over-write
            }
        }

        return schemas.get(targetNamespace);
    }

    /** */
    private String resolveSchema(URI schema) {
        return resolver.resolve(schema.toString());
    }

    protected XSISAXHandler getSAXHandler(URI uri) {
        return new XSISAXHandler(uri);
    }

    public static synchronized Schema getInstance(URI targetNamespace, InputStream is1, Level level)
            throws SAXException {
        return getInstance().getRealInstance(targetNamespace, is1, level);
    }

    private synchronized Schema getRealInstance(URI targetNamespace2, InputStream is1, Level level)
            throws SAXException {
        URI targetNamespace = targetNamespace2;

        if (targetNamespace == null || schemas.get(targetNamespace) == null) {
            XSISAXHandler contentHandler = parseSchema(is1, level);

            if (targetNamespace == null || "".equals(targetNamespace.toString())) {
                targetNamespace = contentHandler.getSchema().getTargetNamespace();
            }

            Schema schema = contentHandler.getSchema();
            if (schemas.get(targetNamespace) != null) {
                schema = merge(schema, schemas.get(targetNamespace));
            }

            schemas.put(targetNamespace, schema);

        } else {
            Schema sh = schemas.get(targetNamespace);
            XSISAXHandler contentHandler = parseSchema(is1, level);

            sh = merge(sh, contentHandler.getSchema());
            schemas.put(targetNamespace, sh); // over-write
        }
        return schemas.get(targetNamespace);
    }

    private XSISAXHandler parseSchema(InputStream is1, Level level) throws SAXException {
        setParser();

        XSISAXHandler contentHandler = getSAXHandler(null);
        XSISAXHandler.setLogLevel(level);

        try {
            parser.parse(is1, contentHandler);
        } catch (IOException e) {
            throw new SAXException(e);
        }
        return contentHandler;
    }

    /*
     * Creates a new Schema from merging the two schemas passed in. for instance
     * information, such as id, or version, s1 takes precedence.
     */
    private Schema merge(Schema s1, Schema s2) throws SAXException {
        return new MergedSchema(s1, s2);
    }

    /**
     * Registers a Schema instance with the factory. A clone is NOT created within this method. The Schema passed in is
     * associated with the specified targetNamespace. The Schema is not tested to ensure the intended targetNamespace
     * (schema.getTargetNamespace()) is equal to targetNamespace. The ramifications is that you may hack wildly within
     * the repository, but we aware you may have some 'undocumented features' as a result (odd Schemas being returned).
     */
    public static void registerSchema(URI targetNamespace, Schema schema) {
        getInstance().registerRealSchema(targetNamespace, schema);
    }

    private void registerRealSchema(URI targetNamespace, Schema schema) {
        schemas.put(targetNamespace, schema);
    }

    /*
     * convinience method to create an instance of a SAXParser if it is null.
     */
    private void setParser() throws SAXException {
        if (parser == null) {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setValidating(false);

            try {
                parser = spf.newSAXParser();
            } catch (ParserConfigurationException | SAXException e) {
                throw new SAXException(e);
            }
        }
    }

    /**
     * The Schema will contain references to the elements within the two Schemas provided in the constructor. In most
     * cases this is used to incorporate additional definitions into a targetNamespace by parsing an additional schema
     * file.
     *
     * @author dzwiers
     * @see Schema
     */
    private static class MergedSchema implements Schema {
        /*
         * This is all fairly self explanatory.
         *
         * @see Schema
         */
        private AttributeGroup[] attributeGroups;
        private Attribute[] attributes;
        private int block;
        private int finaL;
        private ComplexType[] complexTypes;
        private Element[] elements;
        private Group[] groups;
        private String id;
        private String version;
        private String prefix;
        private URI targetNamespace;
        private Schema[] imports;
        private SimpleType[] simpleTypes;
        private boolean aForm;
        private boolean eForm;
        private URI uri;

        /**
         * This completes the merge of two schemas, s1 and s2. When there is a conflict in data between the two schemas,
         * s1 is assumed to be correct.
         *
         * @param s1 Schema (Tie Winner)
         * @param s2 Schema (Tie Loser)
         * @throws SAXException When some thing bad happens (for example merging two targetNamespaces)
         */
        public MergedSchema(Schema s1, Schema s2) throws SAXException {
            if (s1.getId() == null || s1.getId().equals("")) {
                id = s2.getId();
            } else {
                id = s1.getId();
            }

            if (s1.getVersion() == null || s1.getVersion().equals("")) {
                version = s2.getVersion();
            } else {
                version = s1.getVersion();
            }

            if (s1.getTargetNamespace() == null
                    || s1.getTargetNamespace().toString().isEmpty()) {
                targetNamespace = s2.getTargetNamespace();
            } else {
                if (s2.getTargetNamespace() != null && !s1.getTargetNamespace().equals(s2.getTargetNamespace())) {
                    throw new SAXException("cannot merge two target namespaces. "
                            + s1.getTargetNamespace()
                            + " "
                            + s2.getTargetNamespace());
                }

                targetNamespace = s1.getTargetNamespace();
            }

            aForm = s1.isAttributeFormDefault() || s2.isAttributeFormDefault();
            eForm = s1.isElementFormDefault() || s2.isElementFormDefault();

            Map<Object, Object> m = new HashMap<>();

            AttributeGroup[] ag1 = s1.getAttributeGroups();

            if (ag1 == null) {
                ag1 = new AttributeGroup[0];
            }

            AttributeGroup[] ag2 = s2.getAttributeGroups();

            if (ag2 == null) {
                ag2 = new AttributeGroup[0];
            }

            for (AttributeGroup attributeGroup1 : ag1) m.put(attributeGroup1.getName(), attributeGroup1);

            for (AttributeGroup attributeGroup : ag2)
                if (!m.containsKey(attributeGroup.getName())) {
                    m.put(attributeGroup.getName(), attributeGroup);
                }

            attributeGroups = new AttributeGroup[m.size()];

            Object[] obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) attributeGroups[i] = (AttributeGroup) obj[i];

            m = new HashMap<>();

            Attribute[] a1 = s1.getAttributes();

            if (a1 == null) {
                a1 = new Attribute[0];
            }

            Attribute[] a2 = s2.getAttributes();

            if (a2 == null) {
                a2 = new Attribute[0];
            }

            for (Attribute attribute1 : a1) m.put(attribute1.getName(), attribute1);

            for (Attribute attribute : a2)
                if (!m.containsKey(attribute.getName())) {
                    m.put(attribute.getName(), attribute);
                }

            attributes = new Attribute[m.size()];
            obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) attributes[i] = (Attribute) obj[i];

            block = s1.getBlockDefault() | s2.getBlockDefault();
            finaL = s1.getFinalDefault() | s2.getFinalDefault();

            m = new HashMap<>();

            ComplexType[] c1 = s1.getComplexTypes();

            if (c1 == null) {
                c1 = new ComplexType[0];
            }

            ComplexType[] c2 = s2.getComplexTypes();

            if (c2 == null) {
                c2 = new ComplexType[0];
            }

            for (ComplexType complexType1 : c1) m.put(complexType1.getName(), complexType1);

            for (ComplexType complexType : c2)
                if (!m.containsKey(complexType.getName())) {
                    m.put(complexType.getName(), complexType);
                }

            complexTypes = new ComplexType[m.size()];
            obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) complexTypes[i] = (ComplexType) obj[i];

            m = new HashMap<>();

            SimpleType[] ss1 = s1.getSimpleTypes();

            if (ss1 == null) {
                ss1 = new SimpleType[0];
            }

            SimpleType[] ss2 = s2.getSimpleTypes();

            if (ss2 == null) {
                ss2 = new SimpleType[0];
            }

            for (SimpleType type : ss1) m.put(type.getName(), type);

            for (SimpleType simpleType : ss2)
                if (!m.containsKey(simpleType.getName())) {
                    m.put(simpleType.getName(), simpleType);
                }

            simpleTypes = new SimpleType[m.size()];
            obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) simpleTypes[i] = (SimpleType) obj[i];

            m = new HashMap<>();

            Element[] e1 = s1.getElements();

            if (e1 == null) {
                e1 = new Element[0];
            }

            Element[] e2 = s2.getElements();

            if (e2 == null) {
                e2 = new Element[0];
            }

            for (Element element1 : e1) m.put(element1.getName(), element1);

            for (Element element : e2)
                if (!m.containsKey(element.getName())) {
                    m.put(element.getName(), element);
                }

            elements = new Element[m.size()];
            obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) elements[i] = (Element) obj[i];

            m = new HashMap<>();

            Group[] g1 = s1.getGroups();

            if (g1 == null) {
                g1 = new Group[0];
            }

            Group[] g2 = s2.getGroups();

            if (g2 == null) {
                g2 = new Group[0];
            }

            for (Group item : g1) m.put(item.getName(), item);

            for (Group group : g2)
                if (!m.containsKey(group.getName())) {
                    m.put(group.getName(), group);
                }

            groups = new Group[m.size()];
            obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) groups[i] = (Group) obj[i];

            m = new HashMap<>();

            Schema[] i1 = s1.getImports();

            if (i1 == null) {
                i1 = new Schema[0];
            }

            Schema[] i2 = s2.getImports();

            if (i2 == null) {
                i2 = new Schema[0];
            }

            for (Schema value : i1) m.put(value.getTargetNamespace(), value);

            for (Schema schema : i2)
                if (!m.containsKey(schema.getTargetNamespace())) {
                    m.put(schema.getTargetNamespace(), schema);
                }

            imports = new Schema[m.size()];
            obj = m.values().toArray();

            for (int i = 0; i < obj.length; i++) imports[i] = (Schema) obj[i];

            URI u1 = s1.getURI();

            URI u2 = s2.getURI();

            if (u1 == null) {
                uri = u2;
            } else {
                if (u2 == null) {
                    uri = u1;
                } else {
                    uri = u2.relativize(u1);
                }
            }

            if (s1.getPrefix() == null || s1.getPrefix().equals("")) {
                prefix = s2.getPrefix();
            } else {
                prefix = s1.getPrefix();
            }
        }

        /** @see schema.Schema#getAttributeGroups() */
        @Override
        public AttributeGroup[] getAttributeGroups() {
            return attributeGroups;
        }

        /** @see schema.Schema#getAttributeDescriptors() */
        @Override
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.Schema#getBlockDefault() */
        @Override
        public int getBlockDefault() {
            return block;
        }

        /** @see schema.Schema#getComplexTypes() */
        @Override
        public ComplexType[] getComplexTypes() {
            return complexTypes;
        }

        /** @see schema.Schema#getElements() */
        @Override
        public Element[] getElements() {
            return elements;
        }

        /** @see schema.Schema#getFinalDefault() */
        @Override
        public int getFinalDefault() {
            return finaL;
        }

        /** @see schema.Schema#getGroups() */
        @Override
        public Group[] getGroups() {
            return groups;
        }

        /** @see schema.Schema#getId() */
        @Override
        public String getId() {
            return id;
        }

        /** @see schema.Schema#getImports() */
        @Override
        public Schema[] getImports() {
            return imports;
        }

        /** @see schema.Schema#getSimpleTypes() */
        @Override
        public SimpleType[] getSimpleTypes() {
            return simpleTypes;
        }

        /** @see schema.Schema#getTargetNamespace() */
        @Override
        public URI getTargetNamespace() {
            return targetNamespace;
        }

        /** @see schema.Schema#getVersion() */
        @Override
        public String getVersion() {
            return version;
        }

        /** @see schema.Schema#includesURI(java.net.URI) */
        @Override
        public boolean includesURI(URI uri1) {
            if (uri1 == null || this.uri == null) {
                return false;
            }

            return this.uri.equals(uri1);
        }

        /** @see schema.Schema#isAttributeFormDefault() */
        @Override
        public boolean isAttributeFormDefault() {
            return aForm;
        }

        /** @see schema.Schema#isElementFormDefault() */
        @Override
        public boolean isElementFormDefault() {
            return eForm;
        }

        /** @see schema.Schema#getURIs() */
        @Override
        public URI getURI() {
            return uri;
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        /** Returns the implementation hints. The default implementation returns en empty map. */
        @Override
        public Map<java.awt.RenderingHints.Key, ?> getImplementationHints() {
            return Collections.emptyMap();
        }
    }
}
