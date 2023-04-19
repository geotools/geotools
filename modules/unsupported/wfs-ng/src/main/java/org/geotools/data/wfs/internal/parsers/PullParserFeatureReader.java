/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.http.HTTPClient;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.PullParser;
import org.geotools.xsd.impl.ParserHandler.ContextCustomizer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;

/**
 * {@link GetParser<SimpleFeature>} for {@link WFSFeatureReader} that uses the geotools {@link
 * PullParser} to fetch Features out of a WFS GetFeature response.
 *
 * @author Niels Charlier
 */
public class PullParserFeatureReader implements GetParser<SimpleFeature> {
    private static final Logger LOGGER = Logging.getLogger(PullParserFeatureReader.class);
    private PullParser parser;

    private InputStream inputStream;

    private FeatureType featureType;

    private final String axisOrder;

    GeometryCoordinateSequenceTransformer transformer;

    private long lastLogMessage = 0;
    private long logCounter = 0;
    private final long WAIT_LOG = 5000;

    private XsdHttpHandler httpHandler = null;

    public PullParserFeatureReader(
            final Configuration wfsConfiguration,
            final InputStream getFeatureResponseStream,
            final FeatureType featureType,
            String axisOrder)
            throws IOException {
        this.inputStream = getFeatureResponseStream;
        this.featureType = featureType;
        this.axisOrder = axisOrder;

        this.parser =
                new PullParser(
                        wfsConfiguration,
                        getFeatureResponseStream,
                        new QName(
                                featureType.getName().getNamespaceURI(),
                                featureType.getName().getLocalPart()));
        transformer = new GeometryCoordinateSequenceTransformer();
        transformer.setMathTransform(new AffineTransform2D(0, 1, 1, 0, 0, 0));
    }

    /**
     * Initialise a feature reader with the used http client, to ensure reuse of the configuration.
     */
    public PullParserFeatureReader(
            final Configuration wfsConfiguration,
            final InputStream getFeatureResponseStream,
            final FeatureType featureType,
            String axisOrder,
            HTTPClient client)
            throws IOException {
        this(wfsConfiguration, getFeatureResponseStream, featureType, axisOrder);
        this.httpHandler = new XsdHttpHandler(client);
        this.parser.setURIHandler(httpHandler);
    }

    /** @see GetParser<SimpleFeature>#close() */
    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            try {
                inputStream.close();

            } finally {
                inputStream = null;
                parser = null;
            }
        }

        if (httpHandler != null) {
            httpHandler.dispose();
        }
    }

    /** @see GetParser<SimpleFeature>#parse() */
    @Override
    public SimpleFeature parse() throws IOException {
        Object parsed;
        try {
            parsed = parser.parse();
        } catch (XMLStreamException | SAXException e) {
            throw new IOException(
                    "Error parsing xml for features of type: "
                            + (featureType == null ? "Unknown" : featureType.getName()),
                    e);
        }
        if (parsed == null) {
            return null;
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            logCounter++;
            long time = System.currentTimeMillis();
            if (lastLogMessage == 0) {
                LOGGER.fine("First feature parsed.");
                lastLogMessage = time;
            } else if (time - lastLogMessage > WAIT_LOG) {
                LOGGER.fine(String.format("Number of features parsed: %d", logCounter));
                lastLogMessage = time;
            }
        }
        if (!(parsed instanceof SimpleFeature)) {
            throw new IOException(
                    "Couldn't parse SimpleFeature of type "
                            + featureType
                            + " from xml.\n"
                            + entriesInMap(parsed));
        }
        SimpleFeature feature = (SimpleFeature) parsed;
        if (feature.getDefaultGeometry() != null) {
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            if (geometry.getUserData() instanceof CoordinateReferenceSystem) {
                CoordinateReferenceSystem crs = (CoordinateReferenceSystem) geometry.getUserData();
                if (WFSConfig.invertAxisNeeded(axisOrder, crs)) {
                    try {
                        feature.setDefaultGeometry(invertGeometryCoordinates(geometry));
                    } catch (TransformException e) {
                        throw new IOException(e);
                    }
                }
            }
        }
        return feature;
    }

    @SuppressWarnings("unchecked")
    private String entriesInMap(Object parsedMap) {
        if (!(parsedMap instanceof Map)) {
            return parsedMap.toString();
        }
        return ((Map<String, Object>) parsedMap)
                .entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue() + "\n")
                        .collect(Collectors.joining());
    }

    private Geometry invertGeometryCoordinates(Geometry geometry) throws TransformException {
        return transformer.transform(geometry);
    }

    /** @see GetParser<SimpleFeature>#getNumberOfFeatures() */
    @Override
    public int getNumberOfFeatures() {
        LOGGER.warning("Pull Parser doesn't implement counting features");
        return -1;
    }

    @Override
    public FeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        // TODO implement?
    }

    public void setContextCustomizer(ContextCustomizer contextCustomizer) {
        parser.setContextCustomizer(contextCustomizer);
    }
}
