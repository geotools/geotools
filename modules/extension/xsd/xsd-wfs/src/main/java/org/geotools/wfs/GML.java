/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDForm;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.bindings.GML2ParsingUtils;
import org.geotools.referencing.CRS;
import org.geotools.util.PartiallyOrderedSet;
import org.geotools.wfs.gtxml.GTXML;
import org.geotools.xs.XS;
import org.geotools.xs.XSConfiguration;
import org.geotools.xs.XSSchema;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.geotools.xsd.StreamingParser;
import org.geotools.xsd.XSD;
import org.geotools.xsd.impl.ParserHandler.ContextCustomizer;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.Schema;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.MutablePicoContainer;
import org.xml.sax.SAXException;

/**
 * UtilityClass for encoding GML content.
 *
 * <p>This utility class uses a range of GeoTools technologies as required; if you would like finer
 * grain control over the encoding process please review the source code of this class and take your
 * own measures.
 *
 * <p>
 */
public class GML {
    /** Version of encoder to use */
    public static enum Version {
        GML2,
        GML3,
        WFS1_0,
        WFS1_1
    }

    /**
     * {@link ContextCustomizer} setting up a {@link FeatureTypeCache} that will not cache
     * dynamically build feature types, thus allowing all features to have their own schema, instead
     * of relying on the schema of the first feature found
     */
    private static class DynamicFeatureTypeCacheCustomizer implements ContextCustomizer {

        boolean dynamicTypeFound = false;

        /**
         * Does not cache feature types generated from a feature instance (as opposed to a feature
         * schema)
         */
        FeatureTypeCache dynamicFeatureTypeCache =
                new FeatureTypeCache() {
                    @Override
                    public void put(FeatureType type) {
                        // only add to cache if the feature type has been parsed from schema
                        if (Boolean.TRUE.equals(
                                type.getUserData().get(GML2ParsingUtils.PARSED_FROM_SCHEMA_KEY))) {
                            super.put(type);
                        } else {
                            dynamicTypeFound = true;
                        }
                    }
                };

        public boolean isDynamicTypeFound() {
            return dynamicTypeFound;
        }

        @Override
        public void customizeContext(MutablePicoContainer context) {
            Object instance = context.getComponentInstanceOfType(FeatureTypeCache.class);
            context.unregisterComponentByInstance(instance);
            context.registerComponentInstance(dynamicFeatureTypeCache);
        }
    }

    private Charset encoding = Charset.forName("UTF-8");

    private URL baseURL;

    /** GML Configuration to use */
    private Configuration gmlConfiguration;

    private String gmlNamespace;

    private String gmlLocation;

    /** Schema or profile used to map between Java classes and XML elements. */
    private List<Schema> schemaList = new ArrayList<Schema>();

    String prefix = null;

    String namespace = null;

    private final Version version;

    private boolean legacy;

    private CoordinateReferenceSystem crs;

    /**
     * Construct a GML utility class to work with the indicated version of GML.
     *
     * <p>Note that when working with GML2 you need to supply additional information prior to use
     * (in order to indicate where for XSD file is located).
     *
     * @param version Version of GML to use
     */
    public GML(Version version) {
        this.version = version;
        init();
    }

    /**
     * Engage legacy support for GML2.
     *
     * <p>The GML2 support for FeatureTransformer is much faster then that provided by the GTXML
     * parser/encoder. This speed is at the expense of getting the up front configuration exactly
     * correct (something you can only tell when parsing the produced result!). Setting this value
     * to false will use the same GMLConfiguration employed when parsing and has less risk of
     * producing invalid content.
     */
    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    /** Set the target namespace for the encoding. */
    public void setNamespace(String prefix, String namespace) {
        this.prefix = prefix;
        this.namespace = namespace;
    }

    /** Set the encoding to use. */
    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    /** Base URL to use when encoding */
    public void setBaseURL(URL baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Coordinate reference system to use when decoding.
     *
     * <p>In a few cases (such as decoding a SimpleFeatureType) the file format does not include the
     * required CooridinateReferenceSystem and you are asked to supply it.
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    /**
     * Set up out of the box configuration for GML encoding.
     *
     * <ul>
     *   <li>GML2
     *   <li>GML3
     * </ul>
     *
     * The following are not avialable yet:
     *
     * <ul>
     *   <li>gml3.2 - not yet available
     *   <li>wfs1.0 - not yet available
     *   <li>wfs1.1 - not yet available
     * </ul>
     */
    protected void init() {
        List<Schema> schemas = new ArrayList<Schema>();
        schemas.add(new XSSchema().profile()); // encoding of common java types
        Schema hack = new SchemaImpl(XS.NAMESPACE);

        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        builder.setName("date");
        builder.setBinding(Date.class);
        hack.put(new NameImpl(XS.DATETIME), builder.buildType());

        schemas.add(hack);

        // GML 2
        //
        if (Version.GML2 == version) {
            gmlNamespace = org.geotools.gml2.GML.NAMESPACE;
            gmlLocation = "gml/2.1.2/feature.xsd";
            gmlConfiguration = new org.geotools.gml2.GMLConfiguration();
            schemas.add(new org.geotools.gml2.GMLSchema().profile());
        }
        if (Version.WFS1_0 == version) {
            gmlNamespace = org.geotools.gml2.GML.NAMESPACE;
            gmlLocation = "gml/2.1.2/feature.xsd";
            gmlConfiguration = new org.geotools.wfs.v1_0.WFSConfiguration_1_0();

            schemas.add(new org.geotools.gml2.GMLSchema().profile());
        }
        // GML 3
        //
        if (Version.GML3 == version) {
            gmlNamespace = org.geotools.gml3.GML.NAMESPACE;
            gmlLocation = "gml/3.1.1/base/gml.xsd";
            gmlConfiguration = new org.geotools.gml3.GMLConfiguration();
            schemas.add(new org.geotools.gml3.GMLSchema().profile());
        }
        if (Version.WFS1_1 == version) {
            gmlNamespace = org.geotools.gml3.GML.NAMESPACE;
            gmlLocation = "gml/3.1.1/base/gml.xsd";
            gmlConfiguration = new org.geotools.wfs.v1_1.WFSConfiguration();

            schemas.add(new org.geotools.gml3.GMLSchema().profile());
        }
        schemaList = schemas;
    }

    private Entry<Name, AttributeType> searchSchemas(Class<?> binding) {
        // sort by isAssignable so we get the most specific match possible
        //
        Comparator<Entry<Name, AttributeType>> sort =
                new Comparator<Entry<Name, AttributeType>>() {
                    public int compare(
                            Entry<Name, AttributeType> o1, Entry<Name, AttributeType> o2) {
                        Class<?> binding1 = o1.getValue().getBinding();
                        Class<?> binding2 = o2.getValue().getBinding();
                        if (binding1.equals(binding2)) {
                            return 0;
                        }
                        if (binding1.isAssignableFrom(binding2)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                };
        List<Entry<Name, AttributeType>> match = new ArrayList<Entry<Name, AttributeType>>();

        // process the listed profiles recording all available matches
        for (Schema profile : schemaList) {
            for (Entry<Name, AttributeType> entry : profile.entrySet()) {
                AttributeType type = entry.getValue();
                if (type.getBinding().isAssignableFrom(binding)) {
                    match.add(entry);
                }
            }
        }
        Collections.sort(match, sort);

        Iterator<Entry<Name, AttributeType>> iter = match.iterator();
        if (iter.hasNext()) {
            Entry<Name, AttributeType> entry = iter.next();
            return entry;
        } else {
            return null; // no binding found that matches
        }
    }

    @SuppressWarnings("unchecked")
    public void encode(OutputStream out, SimpleFeatureCollection collection) throws IOException {

        if (version == Version.GML2) {
            if (legacy) {
                encodeLegacyGML2(out, collection);
            } else {
                throw new IllegalStateException(
                        "Cannot encode a feature collection using GML2 (only WFS)");
            }
        } else if (version == Version.WFS1_0) {
            org.geotools.wfs.v1_0.WFSConfiguration_1_0 configuration =
                    new org.geotools.wfs.v1_0.WFSConfiguration_1_0();
            configuration.getProperties().add(GMLConfiguration.OPTIMIZED_ENCODING);
            Encoder e = new Encoder(configuration);
            e.getNamespaces().declarePrefix(prefix, namespace);
            e.setIndenting(true);

            FeatureCollectionType featureCollectionType =
                    WfsFactory.eINSTANCE.createFeatureCollectionType();
            featureCollectionType.getFeature().add(collection);

            e.encode(featureCollectionType, org.geotools.wfs.WFS.FeatureCollection, out);
        } else if (version == Version.WFS1_1) {
            org.geotools.wfs.v1_1.WFSConfiguration configuration =
                    new org.geotools.wfs.v1_1.WFSConfiguration();
            configuration.getProperties().add(GMLConfiguration.OPTIMIZED_ENCODING);
            Encoder e = new Encoder(configuration);
            e.getNamespaces().declarePrefix(prefix, namespace);
            e.setIndenting(true);

            FeatureCollectionType featureCollectionType =
                    WfsFactory.eINSTANCE.createFeatureCollectionType();
            featureCollectionType.getFeature().add(collection);

            e.encode(featureCollectionType, org.geotools.wfs.WFS.FeatureCollection, out);
        } else {
            throw new IllegalStateException("Unable to handle requested version");
        }
    }

    private void encodeLegacyGML2(OutputStream out, SimpleFeatureCollection collection)
            throws IOException {
        final SimpleFeatureType TYPE = collection.getSchema();

        FeatureTransformer transform = new FeatureTransformer();
        transform.setIndentation(4);
        transform.setGmlPrefixing(true);

        if (prefix != null && namespace != null) {
            transform.getFeatureTypeNamespaces().declareDefaultNamespace(prefix, namespace);
            transform.addSchemaLocation(prefix, namespace);
            // transform.getFeatureTypeNamespaces().declareDefaultNamespace("", namespace );
        }

        if (TYPE.getName().getNamespaceURI() != null && TYPE.getUserData().get("prefix") != null) {
            String typeNamespace = TYPE.getName().getNamespaceURI();
            String typePrefix = (String) TYPE.getUserData().get("prefix");

            transform.getFeatureTypeNamespaces().declareNamespace(TYPE, typePrefix, typeNamespace);
        } else if (prefix != null && namespace != null) {
            // ignore namespace URI in feature type
            transform.getFeatureTypeNamespaces().declareNamespace(TYPE, prefix, namespace);
        }

        // we probably need to do a wfs feaure collection here?
        transform.setCollectionPrefix(null);
        transform.setCollectionNamespace(null);

        // other configuration
        transform.setCollectionBounding(true);
        transform.setEncoding(encoding);

        // configure additional feature namespace lookup
        transform.getFeatureNamespaces();

        String srsName = CRS.toSRS(TYPE.getCoordinateReferenceSystem());
        if (srsName != null) {
            transform.setSrsName(srsName);
        }

        try {
            transform.transform(collection, out);
        } catch (TransformerException e) {
            throw (IOException)
                    new IOException("Failed to encode feature collection:" + e).initCause(e);
        }
    }

    /**
     * Encode the provided SimpleFeatureType into an XSD file, using a target namespace
     *
     * <p>When encoding the simpleFeatureType:
     *
     * <ul>
     *   <li>target prefix/namespace can be provided by prefix and namespace parameters. This is the
     *       default for the entire XSD document.
     *   <li>simpleFeatureType.geName().getNamespaceURI() is used for the simpleFeatureType itself,
     *       providing simpleFeatureType.getUserData().get("prefix") is defined
     * </ul>
     *
     * @param simpleFeatureType To be encoded as an XSD document
     */
    public void encode(OutputStream out, SimpleFeatureType simpleFeatureType) throws IOException {
        XSDSchema xsd = xsd(simpleFeatureType);

        XSDResourceImpl.serialize(out, xsd.getElement(), encoding.name());
    }

    /**
     * Decode a typeName from the provided schemaLocation.
     *
     * <p>The XMLSchema does not include CoordinateReferenceSystem we need to ask you to supply this
     * information.
     *
     * @return SimpleFeatureType
     */
    public SimpleFeatureType decodeSimpleFeatureType(URL schemaLocation, Name typeName)
            throws IOException {
        if (Version.WFS1_1 == version) {
            final QName featureName =
                    new QName(typeName.getNamespaceURI(), typeName.getLocalPart());

            String namespaceURI = featureName.getNamespaceURI();
            String uri = schemaLocation.toExternalForm();
            Configuration wfsConfiguration =
                    new org.geotools.gml3.ApplicationSchemaConfiguration(namespaceURI, uri);

            FeatureType parsed = GTXML.parseFeatureType(wfsConfiguration, featureName, crs);
            return DataUtilities.simple(parsed);
        }

        if (Version.WFS1_0 == version) {
            final QName featureName =
                    new QName(typeName.getNamespaceURI(), typeName.getLocalPart());

            String namespaceURI = featureName.getNamespaceURI();
            String uri = schemaLocation.toExternalForm();

            XSD xsd = new org.geotools.gml2.ApplicationSchemaXSD(namespaceURI, uri);
            Configuration configuration =
                    new Configuration(xsd) {
                        {
                            addDependency(new XSConfiguration());
                            addDependency(gmlConfiguration); // use our GML configuration
                        }

                        protected void registerBindings(java.util.Map bindings) {
                            // we have no special bindings
                        }
                    };

            FeatureType parsed = GTXML.parseFeatureType(configuration, featureName, crs);
            return DataUtilities.simple(parsed);
        }
        return null;
    }

    /**
     * Decodes a feature collection from the stream provided. It assumes the features are uniform
     * and that all attributes are available in the first feature
     */
    public SimpleFeatureCollection decodeFeatureCollection(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        return decodeFeatureCollection(in, false);
    }

    /**
     * Decodes a feature collection from the stream provided.
     *
     * @param computeFullFeatureType When true, all features are parsed and then a global feature
     *     type is determined that has attributes covering all feature needs, when false, the first
     *     feature attributes
     */
    public SimpleFeatureCollection decodeFeatureCollection(
            InputStream in, boolean computeFullFeatureType)
            throws IOException, SAXException, ParserConfigurationException {
        if (Version.GML2 == version
                || Version.WFS1_0 == version
                || Version.GML2 == version
                || Version.GML3 == version
                || Version.WFS1_0 == version
                || Version.WFS1_1 == version) {
            Configuration cfg = gmlConfiguration;
            Parser parser = new Parser(cfg);
            DynamicFeatureTypeCacheCustomizer customizer = null;
            if (computeFullFeatureType) {
                customizer = new DynamicFeatureTypeCacheCustomizer();
                parser.setContextCustomizer(customizer);
            }
            Object obj = parser.parse(in);
            SimpleFeatureCollection collection = toFeatureCollection(obj);
            // have we figured out the schema feature by feature? If so, harmonize
            if (computeFullFeatureType && customizer.isDynamicTypeFound()) {
                SimpleFeatureType harmonizedType = getCompleteFeatureType(collection);
                collection = new ReTypingFeatureCollection(collection, harmonizedType);
            }

            return collection;
        }
        return null;
    }

    /** Gets the complete feature type of a collection having potentially mixed attributes */
    private SimpleFeatureType getCompleteFeatureType(SimpleFeatureCollection collection) {
        if (collection.isEmpty()) {
            return collection.getSchema();
        }

        // compute the largest feature type from features, assuming some attributes
        // are shared, while others appear only in some features
        PartiallyOrderedSet<AttributeDescriptor> attributes =
                new PartiallyOrderedSet<AttributeDescriptor>();
        Set<String> attributeNames = new HashSet<>();

        String typeName = null;
        try (SimpleFeatureIterator fi = collection.features()) {
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                SimpleFeatureType type = f.getFeatureType();
                if (typeName == null) {
                    typeName = type.getTypeName();
                }
                List<AttributeDescriptor> descriptorList =
                        f.getFeatureType().getAttributeDescriptors();
                for (int i = 0; i < descriptorList.size(); i++) {
                    AttributeDescriptor curr = descriptorList.get(i);
                    String name = curr.getLocalName();
                    if (!attributeNames.contains(name)) {
                        attributes.add(curr);
                        if (i > 0) {
                            AttributeDescriptor prev = descriptorList.get(i - 1);
                            attributes.setOrder(prev, curr);
                        }
                        if (i < descriptorList.size() - 1) {
                            AttributeDescriptor next = descriptorList.get(i + 1);
                            attributes.add(next);
                            attributes.setOrder(curr, next);
                        }
                    }
                }
            }
        }

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(typeName);
        for (AttributeDescriptor ad : attributes) {
            tb.add(ad);
        }

        return tb.buildFeatureType();
    }

    /**
     * Convert parse results into a SimpleFeatureCollection.
     *
     * @param obj SimpleFeatureCollection, Collection<?>, SimpleFeature, etc...
     * @return SimpleFeatureCollection of the results
     */
    private SimpleFeatureCollection toFeatureCollection(Object obj) {
        if (obj == null) {
            return null; // not available?
        }
        if (obj instanceof SimpleFeatureCollection) {
            return (SimpleFeatureCollection) obj;
        }
        if (obj instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) obj;
            SimpleFeatureCollection simpleFeatureCollection = simpleFeatureCollection(collection);
            return simpleFeatureCollection;
        }
        if (obj instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) obj;
            return DataUtilities.collection(feature);
        }
        if (obj instanceof FeatureCollectionType) {
            FeatureCollectionType collectionType = (FeatureCollectionType) obj;
            for (Object entry : collectionType.getFeature()) {
                SimpleFeatureCollection collection = toFeatureCollection(entry);
                if (entry != null) {
                    return collection;
                }
            }
            return null; // nothing found
        } else {
            throw new ClassCastException(
                    obj.getClass()
                            + " produced when FeatureCollection expected"
                            + " check schema use of AbstractFeatureCollection");
        }
    }

    /**
     * Allow the parsing of features as a stream; the returned iterator can be used to step through
     * the inputstream of content one feature at a time without loading everything into memory.
     *
     * <p>The schema used by the XML is consulted to determine what element extends AbstractFeature.
     *
     * @return Iterator that can be used to parse features one at a time
     */
    public SimpleFeatureIterator decodeFeatureIterator(InputStream in)
            throws IOException, ParserConfigurationException, SAXException {
        return decodeFeatureIterator(in, null);
    }

    /**
     * Allow the parsing of features as a stream; the returned iterator can be used to step through
     * the inputstream of content one feature at a time without loading everything into memory.
     *
     * <p>The use of an elementName is optional; and can be used as a workaround in cases where the
     * schema is not available or correctly defined. The returned elements are wrapped up as a
     * Feature if needed. This mehtod can be used to retrive only the Geometry elements from a GML
     * docuemnt.
     *
     * @param in InputStream used as a source of SimpleFeature content
     * @param elementName The simple feature element; the schema will be checked for an entry that
     *     extends AbstratFeatureType
     */
    public SimpleFeatureIterator decodeFeatureIterator(InputStream in, QName elementName)
            throws IOException, ParserConfigurationException, SAXException {
        if (Version.GML2 == version
                || Version.GML3 == version
                || Version.WFS1_0 == version
                || Version.WFS1_1 == version) {
            // ParserDelegate parserDelegate = new XSDParserDelegate( gmlConfiguration );
            StreamingParser parser;
            if (elementName != null) {
                parser = new StreamingParser(gmlConfiguration, in, elementName);
            } else {
                parser = new StreamingParser(gmlConfiguration, in, SimpleFeature.class);
            }
            return iterator(parser);
        }
        return null;
    }

    /**
     * Go through collection contents and morph contents into SimpleFeatures as required.
     *
     * @return SimpleFeatureCollection
     */
    private SimpleFeatureCollection simpleFeatureCollection(Collection<?> collection) {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        SimpleFeatureType schema = null;
        for (Object obj : collection) {
            if (schema == null) {
                schema = simpleType(obj);
            }
            SimpleFeature feature = simpleFeature(obj, schema);
            featureCollection.add(feature);
        }
        return featureCollection;
    }

    /**
     * Used to wrap up a StreamingParser as a Iterator<SimpleFeature>.
     *
     * <p>This iterator is actually forgiving; and willing to "morph" content into a SimpleFeature
     * if needed.
     *
     * <ul>
     *   <li>SimpleFeature - is returned as is
     *   <li>
     */
    protected SimpleFeatureIterator iterator(final StreamingParser parser) {
        return new SimpleFeatureIterator() {
            SimpleFeatureType schema;

            Object next;

            public boolean hasNext() {
                if (next != null) {
                    return true;
                }
                next = parser.parse();
                return next != null;
            }

            public SimpleFeature next() {
                if (next == null) {
                    next = parser.parse();
                }
                if (next != null) {
                    try {
                        if (schema == null) {
                            schema = simpleType(next);
                        }
                        SimpleFeature feature = simpleFeature(next, schema);
                        return feature;
                    } finally {
                        next = null; // we have tried processing this one now
                    }
                } else {
                    return null; // nothing left
                }
            }

            public void close() {
                schema = null;
            }
        };
    }

    protected SimpleFeatureType simpleType(Object obj) {
        if (obj instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) obj;
            return feature.getFeatureType();
        }
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
            build.setName("Unknown");
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                Class<?> binding = value == null ? Object.class : value.getClass();
                if (value instanceof Geometry) {
                    Geometry geom = (Geometry) value;
                    Object srs = geom.getUserData();
                    if (srs instanceof CoordinateReferenceSystem) {
                        build.add(key, binding, (CoordinateReferenceSystem) srs);
                    } else if (srs instanceof Integer) {
                        build.add(key, binding, (Integer) srs);
                    } else if (srs instanceof String) {
                        build.add(key, binding, (String) srs);
                    } else {
                        build.add(key, binding);
                    }
                } else {
                    build.add(key, binding);
                }
            }
            SimpleFeatureType schema = build.buildFeatureType();
            return schema;
        }
        if (obj instanceof Geometry) {
            Geometry geom = (Geometry) obj;
            Class<?> binding = geom.getClass();
            Object srs = geom.getUserData();

            SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
            build.setName("Unknown");
            if (srs instanceof CoordinateReferenceSystem) {
                build.add("the_geom", binding, (CoordinateReferenceSystem) srs);
            } else if (srs instanceof Integer) {
                build.add("the_geom", binding, (Integer) srs);
            } else if (srs instanceof String) {
                build.add("the_geom", binding, (String) srs);
            } else {
                build.add("the_geom", binding);
            }
            build.setDefaultGeometry("the_geom");
            SimpleFeatureType schema = build.buildFeatureType();
            return schema;
        }
        return null;
    }

    /**
     * Morph provided obj to a SimpleFeature if possible.
     *
     * @return SimpleFeature, or null if not possible
     */
    protected SimpleFeature simpleFeature(Object obj, SimpleFeatureType schema) {
        if (schema == null) {
            schema = simpleType(obj);
        }

        if (obj instanceof SimpleFeature) {
            return (SimpleFeature) obj;
        }
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            Object values[] = new Object[schema.getAttributeCount()];
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor descriptor = schema.getDescriptor(i);
                String key = descriptor.getLocalName();
                Object value = map.get(key);

                values[i] = value;
            }
            SimpleFeature feature = SimpleFeatureBuilder.build(schema, values, null);
            return feature;
        }
        if (obj instanceof Geometry) {
            Geometry geom = (Geometry) obj;
            SimpleFeatureBuilder build = new SimpleFeatureBuilder(schema);
            build.set(schema.getGeometryDescriptor().getName(), geom);

            SimpleFeature feature = build.buildFeature(null);
            return feature;
        }
        return null; // not available as a feature!
    }

    @SuppressWarnings("unchecked")
    protected XSDSchema xsd(SimpleFeatureType simpleFeatureType) throws IOException {
        XSDFactory factory = XSDFactory.eINSTANCE;
        XSDSchema xsd = factory.createXSDSchema();

        xsd.setSchemaForSchemaQNamePrefix("xsd");
        xsd.getQNamePrefixToNamespaceMap().put("xsd", XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);
        xsd.setElementFormDefault(XSDForm.get(XSDForm.QUALIFIED));

        if (baseURL == null) {
            throw new IllegalStateException("Please setBaseURL prior to encoding");
        }

        if (prefix != null || namespace != null) {
            xsd.setTargetNamespace(namespace);
            xsd.getQNamePrefixToNamespaceMap().put(prefix, namespace);
        }

        if (simpleFeatureType.getName().getNamespaceURI() != null
                && simpleFeatureType.getUserData().get("prefix") != null) {
            String providedNamespace = simpleFeatureType.getName().getNamespaceURI();
            String providedPrefix = (String) simpleFeatureType.getUserData().get("prefix");
            xsd.getQNamePrefixToNamespaceMap().put(providedPrefix, providedNamespace);
        }

        if (simpleFeatureType.getUserData().get("schemaURI") != null) {
            throw new IllegalArgumentException("Unable to support app-schema supplied types");
        }

        // import GML import
        XSDImport gml = factory.createXSDImport();
        gml.setNamespace(gmlNamespace);
        gml.setSchemaLocation(baseURL.toString() + "/" + gmlLocation);
        gml.setResolvedSchema(gmlConfiguration.getXSD().getSchema());
        xsd.getContents().add(gml);

        xsd.getQNamePrefixToNamespaceMap().put("gml", gmlNamespace);
        xsd.getQNamePrefixToNamespaceMap().put("gml", "http://www.opengis.net/gml");

        XSDElementDeclaration element = factory.createXSDElementDeclaration();
        element.setName(simpleFeatureType.getTypeName());

        XSDElementDeclaration _FEATURE = xsd.resolveElementDeclaration(gmlNamespace, "_Feature");
        element.setSubstitutionGroupAffiliation(_FEATURE);

        XSDComplexTypeDefinition ABSTRACT_FEATURE_TYPE =
                xsd.resolveComplexTypeDefinition(gmlNamespace, "AbstractFeatureType");

        XSDComplexTypeDefinition featureType = xsd(xsd, simpleFeatureType, ABSTRACT_FEATURE_TYPE);

        // package up and add to xsd
        element.setTypeDefinition(featureType);
        xsd.getContents().add(element);
        xsd.updateElement();
        return xsd;
    }

    /**
     * Build the XSD definition for the provided type.
     *
     * <p>The generated definition is recorded in the XSDSchema prior to being returned.
     *
     * @param xsd The XSDSchema being worked on
     * @param type ComplexType to capture as an encoding, usually a SimpleFeatureType
     * @param BASE_TYPE definition to use as the base type, or null
     * @return XSDComplexTypeDefinition generated for the provided type
     */
    @SuppressWarnings("unchecked")
    protected XSDComplexTypeDefinition xsd(
            XSDSchema xsd, ComplexType type, final XSDComplexTypeDefinition BASE_TYPE) {
        XSDFactory factory = XSDFactory.eINSTANCE;

        XSDComplexTypeDefinition definition = factory.createXSDComplexTypeDefinition();
        definition.setName(type.getName().getLocalPart());
        definition.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);

        if (BASE_TYPE != null) {
            definition.setBaseTypeDefinition(BASE_TYPE);
        }
        List<String> skip = Collections.emptyList();
        if (BASE_TYPE != null && "AbstractFeatureType".equals(BASE_TYPE.getName())) {
            // should look at ABSTRACT_FEATURE_TYPE to determine contents to skip
            skip = Arrays.asList(new String[] {"nounds", "description", "boundedBy"});
        }

        // attributes
        XSDModelGroup attributes = factory.createXSDModelGroup();
        attributes.setCompositor(XSDCompositor.SEQUENCE_LITERAL);

        Name anyName = new NameImpl(XS.NAMESPACE, XS.ANYTYPE.getLocalPart());

        for (PropertyDescriptor descriptor : type.getDescriptors()) {

            if (descriptor instanceof AttributeDescriptor) {
                AttributeDescriptor attributeDescriptor = (AttributeDescriptor) descriptor;

                if (skip.contains(attributeDescriptor.getLocalName())) {
                    continue;
                }

                XSDElementDeclaration attribute = factory.createXSDElementDeclaration();
                attribute.setName(attributeDescriptor.getLocalName());
                attribute.setNillable(attributeDescriptor.isNillable());

                Name name = attributeDescriptor.getType().getName();

                // return the first match.
                if (!anyName.equals(name)) {
                    AttributeType attributeType = attributeDescriptor.getType();

                    if (attributeType instanceof ComplexType) {
                        ComplexType complexType = (ComplexType) attributeType;
                        // any complex contents must resolve (we cannot encode against
                        // an abstract type for example)
                        if (xsd.resolveTypeDefinition(name.getNamespaceURI(), name.getLocalPart())
                                == null) {
                            // not yet added; better add it into the mix
                            xsd(xsd, complexType, null);
                        }
                    } else {
                        Class<?> binding = attributeType.getBinding();
                        Entry<Name, AttributeType> entry = searchSchemas(binding);
                        if (entry == null) {
                            throw new IllegalStateException(
                                    "No type for "
                                            + attribute.getName()
                                            + " ("
                                            + binding.getName()
                                            + ")");
                        }
                        name = entry.getKey();
                    }
                }

                XSDTypeDefinition attributeDefinition =
                        xsd.resolveTypeDefinition(name.getNamespaceURI(), name.getLocalPart());
                attribute.setTypeDefinition(attributeDefinition);

                XSDParticle particle = factory.createXSDParticle();
                particle.setMinOccurs(attributeDescriptor.getMinOccurs());
                particle.setMaxOccurs(attributeDescriptor.getMaxOccurs());
                particle.setContent(attribute);
                attributes.getContents().add(particle);
            }
        }

        // set up fatureType with attributes
        XSDParticle contents = factory.createXSDParticle();
        contents.setContent(attributes);

        definition.setContent(contents);
        xsd.getContents().add(definition);

        return definition;
    }
}
