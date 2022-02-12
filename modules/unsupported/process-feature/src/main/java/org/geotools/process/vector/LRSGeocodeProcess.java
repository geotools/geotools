/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import java.util.logging.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LengthIndexedLine;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;

@DescribeProcess(
        title = "Geocode point in LRS",
        description = "Extracts points at a given measure from LRS features")
public class LRSGeocodeProcess implements VectorProcess {
    private static final Logger LOGGER = Logging.getLogger(LRSGeocodeProcess.class);

    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Process the input data set.
     *
     * @param featureCollection the data set
     * @throws ProcessException error
     */
    @DescribeResult(name = "result", description = "Output feature collection")
    public FeatureCollection<? extends FeatureType, ? extends Feature> execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    FeatureCollection<? extends FeatureType, ? extends Feature> featureCollection,
            @DescribeParameter(
                            name = "from_measure_attb",
                            description = "Attribute providing start measure of feature")
                    String fromMeasureAttb,
            @DescribeParameter(
                            name = "to_measure_attb",
                            description = "Attribute providing end measure of feature")
                    String toMeasureAttb,
            @DescribeParameter(
                            name = "measure",
                            description = "Measure of the point along the feature to be computed")
                    Double measure)
            throws ProcessException {
        DefaultFeatureCollection results = new DefaultFeatureCollection();
        try {
            if (featureCollection == null || featureCollection.size() == 0) {
                LOGGER.info("No features provided in request");
                return results;
            }
            if (fromMeasureAttb == null
                    || featureCollection.getSchema().getDescriptor(fromMeasureAttb) == null) {
                throw new ProcessException(
                        "The from_measure_attb parameter was not provided or not defined in schema");
            }
            if (toMeasureAttb == null
                    || featureCollection.getSchema().getDescriptor(toMeasureAttb) == null) {
                throw new ProcessException("The to_measure_attb parameter was not provided");
            }
            if (measure == null) {
                throw new ProcessException("The measure parameter was not provided");
            }
            SimpleFeatureType targetFeatureType =
                    createTargetFeatureType(featureCollection.getSchema());

            try (FeatureIterator<? extends Feature> featureIterator =
                    featureCollection.features()) {
                Feature feature = featureIterator.next();
                Double featureFromMeasure =
                        (Double) feature.getProperty(fromMeasureAttb).getValue();
                Double featureToMeasure = (Double) feature.getProperty(toMeasureAttb).getValue();
                LengthIndexedLine lengthIndexedLine =
                        new LengthIndexedLine(
                                (Geometry) feature.getDefaultGeometryProperty().getValue());
                double featureLength = featureToMeasure - featureFromMeasure;
                double startOffset = measure - featureFromMeasure;
                double calcLength =
                        ((Geometry) feature.getDefaultGeometryProperty().getValue()).getLength();
                if (calcLength == 0) {
                    LOGGER.info("Edge feature has zero length");
                    return results;
                }
                if (featureLength == 0) {
                    LOGGER.info("Requested feature has zero length");
                    return results;
                }
                Coordinate point =
                        lengthIndexedLine.extractPoint(startOffset * calcLength / featureLength);
                results.add(createTargetFeature(feature, targetFeatureType, point));
            }
            return results;
        } catch (ProcessException e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.warning("Error executing method: " + e);
            throw new ProcessException("Error executing method: " + e, e);
        }
    }

    /**
     * Create the modified feature type.
     *
     * @param sourceFeatureType the source feature type
     * @return the modified feature type
     * @throws ProcessException errror
     */
    private SimpleFeatureType createTargetFeatureType(FeatureType sourceFeatureType)
            throws ProcessException {
        try {
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.setName(sourceFeatureType.getName().getLocalPart());
            typeBuilder.setNamespaceURI(sourceFeatureType.getName().getNamespaceURI());
            AttributeDescriptor geomAttbType = sourceFeatureType.getGeometryDescriptor();
            for (PropertyDescriptor attbType : sourceFeatureType.getDescriptors()) {
                if (attbType.equals(geomAttbType)) {
                    typeBuilder.add(geomAttbType.getLocalName(), Point.class);
                } else {
                    typeBuilder.add((AttributeDescriptor) attbType);
                }
            }
            typeBuilder.setDefaultGeometry(
                    sourceFeatureType.getGeometryDescriptor().getLocalName());
            return typeBuilder.buildFeatureType();
        } catch (Exception e) {
            LOGGER.warning("Error creating type: " + e);
            throw new ProcessException("Error creating type: " + e, e);
        }
    }

    /**
     * Create the modified feature.
     *
     * @param feature the source feature
     * @param targetFeatureType the modified feature type
     * @return the modified feature
     * @throws ProcessException error
     */
    private SimpleFeature createTargetFeature(
            Feature feature, SimpleFeatureType targetFeatureType, Coordinate geocodePoint)
            throws ProcessException {
        try {
            AttributeDescriptor geomAttbType = targetFeatureType.getGeometryDescriptor();
            Object[] attributes = new Object[targetFeatureType.getAttributeCount()];
            for (int i = 0; i < attributes.length; i++) {
                AttributeDescriptor attbType = targetFeatureType.getAttributeDescriptors().get(i);
                if (attbType.equals(geomAttbType)) {
                    Point point = geometryFactory.createPoint(geocodePoint);
                    point.setSRID(4326);
                    attributes[i] = point;
                } else {
                    attributes[i] = feature.getProperty(attbType.getName()).getValue();
                }
            }
            return SimpleFeatureBuilder.build(
                    targetFeatureType, attributes, feature.getIdentifier().getID());
        } catch (Exception e) {
            LOGGER.warning("Error creating feature: " + e);
            throw new ProcessException("Error creating feature: " + e, e);
        }
    }
}
