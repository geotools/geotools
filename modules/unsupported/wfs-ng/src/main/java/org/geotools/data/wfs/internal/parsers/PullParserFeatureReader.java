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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.operation.transform.AffineTransform2D;
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
 * {@link GetFeatureParser} for {@link WFSFeatureReader} that uses the geotools {@link PullParser}
 * to fetch Features out of a WFS GetFeature response.
 *
 * @author Niels Charlier
 */
public class PullParserFeatureReader implements GetFeatureParser {

    private PullParser parser;

    private InputStream inputStream;

    private FeatureType featureType;

    private final String axisOrder;

    GeometryCoordinateSequenceTransformer transformer;

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

    /** @see GetFeatureParser#close() */
    public void close() throws IOException {
        if (inputStream != null) {
            try {
                inputStream.close();
            } finally {
                inputStream = null;
                parser = null;
            }
        }
    }

    /** @see GetFeatureParser#parse() */
    public SimpleFeature parse() throws IOException {
        Object parsed;
        try {
            parsed = parser.parse();
        } catch (XMLStreamException | SAXException e) {
            throw new IOException(e);
        }
        SimpleFeature feature = (SimpleFeature) parsed;
        if (feature != null && feature.getDefaultGeometry() != null) {
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

    private Geometry invertGeometryCoordinates(Geometry geometry) throws TransformException {
        return transformer.transform(geometry);
    }

    /** @see GetFeatureParser#getNumberOfFeatures() */
    public int getNumberOfFeatures() {
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
