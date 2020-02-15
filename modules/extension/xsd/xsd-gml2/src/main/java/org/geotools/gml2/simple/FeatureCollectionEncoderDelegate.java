/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml2.bindings.GMLEncodingUtils;
import org.geotools.util.Converters;
import org.geotools.xs.bindings.XSStringBinding;
import org.geotools.xsd.Binding;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.EncoderDelegate;
import org.geotools.xsd.SimpleBinding;
import org.geotools.xsd.impl.BindingLoader;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Base class for feature collection optimized GML encoder delegates
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime, GeoSolutions
 */
public abstract class FeatureCollectionEncoderDelegate implements EncoderDelegate {

    Encoder encoder;

    GMLDelegate gml;

    SimpleFeatureCollection features;

    HashMap<Class, GeometryEncoder> geometryEncoders;

    NamespaceSupport namespaces;

    QName boundedBy;

    QName name;

    protected boolean encodeGeometryIds = false;

    protected FeatureCollectionEncoderDelegate(
            SimpleFeatureCollection features, Encoder encoder, GMLDelegate gml) {
        this.features = features;
        this.gml = gml;
        this.encoder = encoder;
        this.namespaces = encoder.getNamespaces();
        this.encoder = encoder;
        this.geometryEncoders = new HashMap<Class, GeometryEncoder>();
        this.boundedBy = gml.getSchema().qName("boundedBy");
        this.name = gml.getSchema().qName("name");
        gml.registerGeometryEncoders(geometryEncoders, encoder);
    }

    public void encode(ContentHandler handler) throws Exception {
        GMLWriter output =
                new GMLWriter(
                        handler,
                        namespaces,
                        gml.getNumDecimals(),
                        gml.forceDecimalEncoding(),
                        gml.padWithZeros(),
                        gml.getGmlPrefix(),
                        gml.getEncodeMeasures());
        boolean featureBounds =
                !encoder.getConfiguration().hasProperty(GMLConfiguration.NO_FEATURE_BOUNDS);

        try (SimpleFeatureIterator fi = features.features()) {
            if (!fi.hasNext()) {
                return;
            }

            ObjectEncoder ee = gml.createEnvelopeEncoder(encoder);
            ee = gml.createEnvelopeEncoder(encoder);

            gml.startFeatures(output);

            AttributesImpl idatts = new AttributesImpl();
            gml.initFidAttribute(idatts);

            // scroll over the features, encode each of them
            SimpleFeature f = fi.next();
            FeatureTypeContextCache ftCache = new FeatureTypeContextCache();
            while (f != null) {
                // special handling for joined features, they need to be split and
                // encoded inside a tuple
                if (GMLEncodingUtils.isJoinedFeature(f) && gml.supportsTuples()) {
                    SimpleFeature[] splitted = GMLEncodingUtils.splitJoinedFeature(f);
                    gml.startTuple(output);
                    for (SimpleFeature feature : splitted) {
                        encodeFeature(output, featureBounds, ee, idatts, feature, ftCache);
                    }
                    gml.endTuple(output);
                } else {
                    encodeFeature(output, featureBounds, ee, idatts, f, ftCache);
                }

                if (fi.hasNext()) {
                    f = fi.next();
                } else {
                    f = null;
                }
            }

            gml.endFeatures(output);
        }
    }

    /** Encodes a single feature */
    private void encodeFeature(
            GMLWriter output,
            boolean featureBounds,
            ObjectEncoder ee,
            AttributesImpl idatts,
            SimpleFeature f,
            FeatureTypeContextCache ftCache)
            throws SAXException, Exception {
        gml.startFeature(output);

        FeatureTypeContext ftContext = ftCache.getFeatureTypeContext(f);

        idatts.setValue(0, f.getID());
        output.startElement(ftContext.featureQualifiedName, idatts);

        for (AttributeContext attribute : ftContext.attributes) {
            QualifiedName name = attribute.name;
            Object value1 = null;
            if (boundedBy.equals(name) && featureBounds) {
                value1 = f.getBounds();
            } else {
                int idx = attribute.attributeIndex;
                value1 = f.getAttribute(idx);
            }
            Object value = value1;

            if (value == null) {
                continue;
            }

            String gmlId = encodeGeometryIds ? f.getID() + "." + name.getLocalPart() : null;
            encodeValue(output, ee, value, attribute, gmlId);
        }

        output.endElement(ftContext.featureQualifiedName);

        gml.endFeature(output);
    }

    private void encodeValue(
            GMLWriter output,
            ObjectEncoder ee,
            Object value,
            AttributeContext attribute,
            String featureId)
            throws SAXException, Exception {
        output.startElement(
                attribute.name,
                getPropertyAttributes(
                        attribute.name, attribute.featureType, attribute.descriptor, value));

        if (value instanceof Geometry) {
            Geometry g = (Geometry) value;
            Integer dimension =
                    GML2EncodingUtils.getGeometryDimension(g, encoder.getConfiguration());
            AttributesImpl atts =
                    buildSrsAttributes(
                            ((GeometryDescriptor) attribute.descriptor)
                                    .getCoordinateReferenceSystem(),
                            dimension);
            GeometryEncoder geometryEncoder = getGeometryEncoder(value, attribute);
            geometryEncoder.encode(g, atts, output, featureId);
        } else if (value instanceof Envelope) {
            ReferencedEnvelope e = (ReferencedEnvelope) value;
            Integer dimension =
                    GML2EncodingUtils.getEnvelopeDimension(e, encoder.getConfiguration());
            AttributesImpl atts = buildSrsAttributes(e.getCoordinateReferenceSystem(), dimension);
            ee.encode(e, atts, output);
        } else if (attribute.binding instanceof SimpleBinding) {
            encodeSimpleBinding(output, value, attribute.binding);
        } else {
            // just encode string value
            output.characters(value.toString());
        }

        output.endElement(attribute.name);
    }

    /**
     * Allows subclasses to generate a list of attributes for the property being encoded. The
     * default implementation just returns null
     *
     * @param attribute The attribute being encoded
     * @param value The attribute value
     * @return A Attributes, or null if no attributes are desired
     */
    protected Attributes getPropertyAttributes(
            QualifiedName name,
            FeatureType featureType,
            AttributeDescriptor attribute,
            Object value) {
        return null;
    }

    private GeometryEncoder getGeometryEncoder(Object value, AttributeContext attribute) {
        Class<? extends Object> clazz = value.getClass();
        if (MultiLineString.class.equals(clazz)) {
            // we have a wrinkle with curve support, were we supposed to encode the
            // multi line string as a curve or not?
            if (attribute.binding.getTarget().getLocalPart().startsWith("MultiCurve")) {
                clazz = MultiCurve.class;
            }
        }
        GeometryEncoder encoder = geometryEncoders.get(clazz);
        while (encoder == null && clazz.getSuperclass() != null) {
            clazz = clazz.getSuperclass();
            encoder = geometryEncoders.get(clazz);
        }

        if (encoder == null) {
            throw new RuntimeException(
                    "Failed to find an appropriate geometry encoder for class " + value.getClass());
        } else {
            return encoder;
        }
    }

    private AttributesImpl buildSrsAttributes(CoordinateReferenceSystem crs, Integer dimension) {
        AttributesImpl atts = null;
        if (crs != null || dimension != null) {
            atts = new AttributesImpl();
            if (crs != null) {
                gml.setSrsNameAttribute(atts, crs);
            }
            if (dimension != null) {
                gml.setGeometryDimensionAttribute(atts, dimension);
            }
        }
        return atts;
    }

    private void encodeSimpleBinding(GMLWriter output, Object value, Binding binding)
            throws Exception, SAXException {
        if (!binding.getType().isInstance(value)) {
            Object converted = Converters.convert(value, binding.getType());
            if (converted != null) {
                value = converted;
            }
        }
        String encoded = ((SimpleBinding) binding).encode(value, null);
        if (encoded != null) {
            output.characters(encoded);
        }
    }

    /**
     * Encoding context for a single attribute, contains all the information we need repeatedly, so
     * that we don't need to look it up over and over
     *
     * @author Andrea Aime - GeoSolutions
     */
    static final class AttributeContext {
        QualifiedName name;

        int attributeIndex;

        Binding binding;

        AttributeDescriptor descriptor;

        FeatureType featureType;

        public AttributeContext(QualifiedName name) {
            this.name = name;
        }
    }

    /**
     * Encoding context for a feature type, contains all the information we need repeatedly, so that
     * we don't need to look it up over and over
     */
    final class FeatureTypeContext {

        private SimpleFeatureType featureType;

        private List<AttributeContext> attributes;

        private QualifiedName featureQualifiedName;

        public FeatureTypeContext(SimpleFeature f, GMLDelegate gml) {
            this.featureType = f.getFeatureType();
            QName featureName =
                    new QName(
                            featureType.getName().getNamespaceURI(),
                            featureType.getName().getLocalPart());

            // look up the element in the schema
            XSDElementDeclaration element =
                    encoder.getSchemaIndex().getElementDeclaration(featureName);
            if (element == null) {
                // create one
                element = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                element.setName(featureType.getName().getLocalPart());
                element.setTargetNamespace(featureType.getName().getNamespaceURI());
                element.setTypeDefinition(
                        encoder.getSchemaIndex().getTypeDefinition(GML.AbstractFeatureType));
            }

            // look up all the bindings for each property
            BindingLoader bindingLoader = encoder.getBindingLoader();

            // get all the properties
            List properties = gml.getFeatureProperties(f, element, encoder);

            attributes = setupAttributeContexts(properties, featureType, bindingLoader);

            featureQualifiedName = getFeatureQualifiedName(featureName);
        }

        /** Builds the list of {@link AttributeContext} for each attribute to be encoded */
        private List<AttributeContext> setupAttributeContexts(
                List properties, SimpleFeatureType schema, BindingLoader bindingLoader) {
            ArrayList<AttributeContext> attributes =
                    new ArrayList<AttributeContext>(properties.size());
            List<AttributeDescriptor> attributeDescriptors = schema.getAttributeDescriptors();
            for (Iterator p = properties.iterator(); p.hasNext(); ) {
                Object[] o = (Object[]) p.next();
                XSDParticle particle = (XSDParticle) o[0];
                XSDElementDeclaration content = (XSDElementDeclaration) particle.getContent();
                if (content.isElementDeclarationReference()) {
                    content = content.getResolvedElementDeclaration();
                }

                String prefix = namespaces.getPrefix(content.getTargetNamespace());
                QualifiedName contentName;
                if (prefix != null) {
                    contentName =
                            QualifiedName.build(
                                    content.getTargetNamespace(), content.getName(), prefix);
                } else {
                    contentName =
                            new QualifiedName(content.getTargetNamespace(), content.getName());
                }
                AttributeContext attribute = new AttributeContext(contentName);
                attribute.featureType = schema;
                attributes.add(attribute);
                int idx = getNameIndex(content.getName(), attributeDescriptors);
                attribute.attributeIndex = idx;
                if (idx != -1) {
                    attribute.descriptor = attributeDescriptors.get(idx);
                }

                if (name.equals(contentName)) {
                    // gml:name is a code type which is actually complex, but since we don't
                    // support code types for simple features, we just use xs:string
                    attribute.binding = new XSStringBinding();
                } else if (!boundedBy.equals(contentName)) {
                    XSDTypeDefinition contentType = content.getTypeDefinition();
                    if (contentType.getName() == null) {
                        // move up to a parent which is not null
                        while (contentType != null && contentType.getName() == null) {
                            XSDTypeDefinition baseType = contentType.getBaseType();
                            if (contentType.equals(baseType)) {
                                contentType = null;
                                continue;
                            }

                            contentType = baseType;
                        }
                    }
                    if (contentType == null || content.getName() == null) {
                        throw new IllegalArgumentException("Could not find non annonymous type");
                    }

                    QName contentTypeName =
                            new QName(contentType.getTargetNamespace(), contentType.getName());

                    Binding binding =
                            bindingLoader.loadBinding(contentTypeName, encoder.getContext());
                    attribute.binding = binding;
                }
            }
            return attributes;
        }

        private int getNameIndex(String name, List<AttributeDescriptor> attributeDescriptors) {
            for (int i = 0; i < attributeDescriptors.size(); i++) {
                if (name.equals(attributeDescriptors.get(i).getLocalName())) {
                    return i;
                }
            }

            return -1;
        }

        private QualifiedName getFeatureQualifiedName(QName featureName) {
            String featureNamespaceURI = featureName.getNamespaceURI();
            String featureLocalName = featureName.getLocalPart();
            String featurePrefix = namespaces.getPrefix(featureNamespaceURI);
            QualifiedName featureQualifiedName =
                    QualifiedName.build(featureNamespaceURI, featureLocalName, featurePrefix);
            return featureQualifiedName;
        }

        public boolean isCompatible(SimpleFeature sf) {
            SimpleFeatureType schema = sf.getFeatureType();
            return this.featureType == schema || this.featureType.equals(schema);
        }
    }

    /**
     * A cache for feature type contexts, to avoid rebuilding them in case a single feature
     * collection contains multiple feature types (wrong, but used in CompositeFeatureCollection in
     * GeoServer) and for joined results which makes us encode 2 or more features for each "member"
     * in the result
     */
    final class FeatureTypeContextCache {
        FeatureTypeContext last;

        Map<SimpleFeatureType, FeatureTypeContext> featureTypeContexts = new IdentityHashMap<>();

        public FeatureTypeContext getFeatureTypeContext(SimpleFeature f) {

            if (last != null && last.isCompatible(f)) {
                return last;
            } else {
                FeatureTypeContext result = featureTypeContexts.get(f.getFeatureType());
                if (result == null) {
                    result = new FeatureTypeContext(f, gml);
                    featureTypeContexts.put(f.getFeatureType(), result);
                }

                return result;
            }
        }
    }
}
