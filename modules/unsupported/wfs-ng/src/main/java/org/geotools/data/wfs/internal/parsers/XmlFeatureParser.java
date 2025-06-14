/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal.parsers;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.gml.stream.XmlStreamGeometryReader;
import org.geotools.gml3.GML;
import org.geotools.util.Converters;
import org.geotools.wfs.WFS;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Abstract form of XmlFeatureParser. Mostly taken out from @{@link XmlSimpleFeatureParser}.
 *
 * @author Adam Brown (Curtin University of Technology)
 */
public abstract class XmlFeatureParser<FT extends FeatureType, F extends Feature> implements GetParser<F> {

    protected FT targetType;

    private InputStream inputStream;

    protected XMLStreamReader parser;

    private XmlStreamGeometryReader geometryReader;

    final String featureNamespace;

    final String featureName;

    private int numberOfFeatures = -1;

    private static final Logger LOGGER = Loggers.RESPONSES;

    public XmlFeatureParser(
            final InputStream getFeatureResponseStream,
            final FT targetType,
            QName featureDescriptorName,
            WFSStrategy strategy)
            throws IOException {
        this.inputStream = getFeatureResponseStream;
        this.featureNamespace = featureDescriptorName.getNamespaceURI();
        this.featureName = featureDescriptorName.getLocalPart();
        this.targetType = targetType;

        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            // disable DTDs
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            // disable external entities
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            parser = factory.createXMLStreamReader(inputStream, "UTF-8");

            geometryReader = new XmlStreamGeometryReader(parser, new GeometryFactory());
            // TODO: XmlSimpleFeatureParser does check whether coordinates need to be inverted, this
            // didn't before using XmlStreamGeometryReader
            // geometryReader.setInvertAxisNeeded(crs -> WFSConfig.invertAxisNeeded(axisOrder,
            // crs));

            // position at root element
            while (parser.hasNext()) {
                if (START_ELEMENT == parser.next()) {
                    break;
                }
            }
            final String namespace = strategy == null
                    ? WFS.NAMESPACE
                    : strategy.getWfsConfiguration().getNamespaceURI();
            parser.require(START_ELEMENT, namespace, WFS.FeatureCollection.getLocalPart());

            String nof = parser.getAttributeValue(null, "numberOfFeatures");
            if (nof != null) {
                try {
                    this.numberOfFeatures = Integer.valueOf(nof);
                } catch (NumberFormatException nfe) {
                    LOGGER.warning("Can't parse numberOfFeatures out of " + nof);
                }
            }
        } catch (XMLStreamException e) {
            getFeatureResponseStream.close();
            this.inputStream = null;
            throw new DataSourceException(e);
        }
    }

    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        if (null != geometryFactory) {
            this.geometryReader.setGeometryFactory(geometryFactory);
        }
    }

    @Override
    public FT getFeatureType() {
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

    /** Parses the value of the current attribute, parser cursor shall be on a feature attribute START_ELEMENT event. */
    protected Object parseAttributeValue(AttributeDescriptor attribute) throws XMLStreamException, IOException {
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

    protected String seekFeature() throws IOException, XMLStreamException {
        int tagType;

        while (true) {
            tagType = parser.next();
            if (tagType == END_DOCUMENT) {
                close();
                return null;
            }

            if (START_ELEMENT == tagType) {
                String namespace = parser.getNamespaceURI();
                String name = parser.getLocalName();

                if (featureNamespace.equals(namespace) && featureName.equals(name)) {
                    String featureId = parser.getAttributeValue(GML.id.getNamespaceURI(), GML.id.getLocalPart());

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
