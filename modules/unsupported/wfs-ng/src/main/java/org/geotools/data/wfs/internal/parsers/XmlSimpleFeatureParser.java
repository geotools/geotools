/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.data.wfs.internal.parsers;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.gml.stream.XmlStreamGeometryReader;
import org.geotools.gml3.GML;
import org.geotools.util.Converters;
import org.geotools.wfs.WFS;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.FactoryException;

/**
 * A {@link GetParser<SimpleFeature>} implementation that uses plain xml pull to parse a GetFeature
 * response.
 *
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *     <p>
 */
public class XmlSimpleFeatureParser implements GetParser<SimpleFeature> {

    private static final Logger LOGGER = Loggers.RESPONSES;

    private InputStream inputStream;

    private XMLStreamReader parser;

    private XmlStreamGeometryReader geometryReader;

    private SimpleFeatureType targetType;

    private SimpleFeatureBuilder builder;

    final String featureNamespace;

    final String featureName;

    private final Map<String, AttributeDescriptor> expectedProperties;

    private int numberOfFeatures = -1;

    public XmlSimpleFeatureParser(
            final InputStream getFeatureResponseStream,
            final SimpleFeatureType targetType,
            QName featureDescriptorName,
            String axisOrder)
            throws IOException {

        // this.inputStream = new TeeInputStream(inputStream, System.err);

        this.inputStream = getFeatureResponseStream;
        this.featureNamespace = featureDescriptorName.getNamespaceURI();
        this.featureName = featureDescriptorName.getLocalPart();
        this.targetType = targetType;
        this.builder = new SimpleFeatureBuilder(targetType);

        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            // disable DTDs
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            // disable external entities
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            parser = factory.createXMLStreamReader(inputStream, "UTF-8");

            geometryReader = new XmlStreamGeometryReader(parser, new GeometryFactory());
            geometryReader.setInvertAxisNeeded(crs -> WFSConfig.invertAxisNeeded(axisOrder, crs));

            // position at root element
            while (parser.hasNext()) {
                if (START_ELEMENT == parser.next()) {
                    break;
                }
            }
            parser.require(START_ELEMENT, null, WFS.FeatureCollection.getLocalPart());

            String nof = parser.getAttributeValue(null, "numberOfFeatures");
            if (nof != null) {
                try {
                    this.numberOfFeatures = Integer.valueOf(nof);
                } catch (NumberFormatException nfe) {
                    LOGGER.warning("Can't parse numberOfFeatures out of " + nof);
                }
            }
        } catch (XMLStreamException e) {
            throw new DataSourceException(e);
        }

        // HACK! use a case insensitive set to compare the comming attribute names with the ones in
        // the schema. Rationale being that the FGDC CubeWerx server has a missmatch in the case of
        // property names between what it states in a DescribeFeatureType and in a GetFeature
        // requests
        expectedProperties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (AttributeDescriptor desc : targetType.getAttributeDescriptors()) {
            expectedProperties.put(desc.getLocalName(), desc);
        }
    }

    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        if (null != geometryFactory) {
            this.geometryReader.setGeometryFactory(geometryFactory);
        }
    }

    @Override
    public FeatureType getFeatureType() {
        return targetType;
    }

    @Override
    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    @Override
    public void close() throws IOException {
        if (this.inputStream != null) {
            try {
                this.parser.close();
                this.parser = null;
                this.inputStream.close();
                this.inputStream = null;
            } catch (XMLStreamException e) {
                throw new DataSourceException(e);
            }
        }
    }

    @Override
    public SimpleFeature parse() throws IOException {
        final String fid;
        try {
            fid = seekFeature();
            if (fid == null) {
                return null;
            }
            int tagType;
            Object attributeValue;
            while (true) {
                tagType = parser.next();
                if (END_DOCUMENT == tagType) {
                    close();
                    return null;
                }

                if (END_ELEMENT == tagType
                        && featureNamespace.equals(parser.getNamespaceURI())
                        && featureName.equals(parser.getLocalName())) {
                    // found end of current feature
                    break;
                }
                if (START_ELEMENT == tagType) {
                    AttributeDescriptor descriptor = expectedProperties.get(parser.getLocalName());
                    if (descriptor != null) {
                        attributeValue = parseAttributeValue();
                        builder.set(descriptor.getLocalName(), attributeValue);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new DataSourceException(e);
        }
        SimpleFeature feature = builder.buildFeature(fid);
        return feature;
    }

    /**
     * Parses the value of the current attribute, parser cursor shall be on a feature attribute
     * START_ELEMENT event.
     */
    private Object parseAttributeValue() throws XMLStreamException, IOException {
        final String name = parser.getLocalName();
        final AttributeDescriptor attribute = expectedProperties.get(name);
        final AttributeType type = attribute.getType();
        Object parsedValue;
        if (type instanceof GeometryType) {
            parser.nextTag();
            try {
                parsedValue = geometryReader.readGeometry();
            } catch (FactoryException e) {
                throw new DataSourceException(e);
            }
        } else {
            String rawTextValue = parser.getElementText();
            Class<?> binding = type.getBinding();
            parsedValue = Converters.convert(rawTextValue, binding);
        }
        return parsedValue;
    }

    private String seekFeature() throws IOException, XMLStreamException {
        int tagType;

        while (true) {
            tagType = parser.next();
            if (tagType == END_DOCUMENT) {
                close();
                return null;
            }
            if (START_ELEMENT != tagType) {
                continue;
            }
            if (START_ELEMENT == tagType) {
                String namespace = parser.getNamespaceURI();
                String name = parser.getLocalName();
                if (featureNamespace.equals(namespace) && featureName.equals(name)) {
                    String featureId =
                            parser.getAttributeValue(
                                    GML.id.getNamespaceURI(), GML.id.getLocalPart());

                    if (featureId == null) {
                        featureId = parser.getAttributeValue(null, "fid");
                    }
                    // Mapserver hack
                    if (featureId == null) {
                        featureId = parser.getAttributeValue(null, "id");
                    }
                    return featureId;
                }
            }
        }
    }
}
