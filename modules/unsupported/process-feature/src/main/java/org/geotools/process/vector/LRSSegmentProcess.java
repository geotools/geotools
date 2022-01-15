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

import java.util.List;
import java.util.logging.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.linearref.LengthIndexedLine;
import org.locationtech.jts.operation.linemerge.LineMerger;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;

@DescribeProcess(
        title = "Extract Segment in LRS",
        description = "Extracts segment between a given start and end measure from LRS features")
public class LRSSegmentProcess implements VectorProcess {
    private static final Logger LOGGER = Logging.getLogger(LRSSegmentProcess.class);

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
                            name = "from_measure",
                            description = "Measure for start of segment to extract")
                    Double fromMeasure,
            @DescribeParameter(
                            name = "to_measure",
                            description = "Measure for end of segment to extract")
                    Double toMeasure)
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
            if (fromMeasure == null) {
                throw new ProcessException("The from_measure parameter was not provided");
            }
            if (toMeasure == null) {
                throw new ProcessException("The to_measure parameter was not provided");
            }
            if (fromMeasure.doubleValue() == toMeasure.doubleValue()) {
                LOGGER.info("Zero length segment requested");
                return results;
            }

            Feature firstFeature = null;
            LineMerger lineMerger = new LineMerger();
            if (toMeasure.doubleValue() > fromMeasure.doubleValue()) {
                try (FeatureIterator<? extends Feature> featureIterator =
                        featureCollection.features()) {
                    while (featureIterator.hasNext()) {
                        Feature feature = featureIterator.next();
                        if (firstFeature == null) firstFeature = feature;
                        if (processFeature(
                                fromMeasureAttb,
                                toMeasureAttb,
                                fromMeasure,
                                toMeasure,
                                lineMerger,
                                feature)) {
                            continue;
                        }
                    }
                }
                @SuppressWarnings("unchecked")
                List<LineString> merged = (List<LineString>) lineMerger.getMergedLineStrings();
                results.add(
                        createTargetFeature(
                                firstFeature,
                                (SimpleFeatureType) firstFeature.getType(),
                                new MultiLineString(
                                        merged.toArray(new LineString[merged.size()]),
                                        geometryFactory)));
            }

            return results;
        } catch (ProcessException e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.warning("Error executing method: " + e);
            throw new ProcessException("Error executing method: " + e, e);
        }
    }

    private boolean processFeature(
            String fromMeasureAttb,
            String toMeasureAttb,
            Double fromMeasure,
            Double toMeasure,
            LineMerger lineMerger,
            Feature feature) {
        Double featureFromMeasure = (Double) feature.getProperty(fromMeasureAttb).getValue();
        Double featureToMeasure = (Double) feature.getProperty(toMeasureAttb).getValue();

        if (fromMeasure < featureToMeasure && toMeasure > featureFromMeasure) {
            try {
                if (fromMeasure <= featureFromMeasure && toMeasure >= featureToMeasure) {
                    lineMerger.add((Geometry) feature.getDefaultGeometryProperty().getValue());
                } else if (fromMeasure > featureFromMeasure && toMeasure < featureToMeasure) {
                    LengthIndexedLine lengthIndexedLine =
                            new LengthIndexedLine(
                                    (Geometry) feature.getDefaultGeometryProperty().getValue());
                    double featureLength = featureToMeasure - featureFromMeasure;
                    double startOffset = fromMeasure - featureFromMeasure;
                    double stopOffset = toMeasure - featureFromMeasure;
                    double calcLength =
                            ((Geometry) feature.getDefaultGeometryProperty().getValue())
                                    .getLength();
                    if (calcLength == 0 || featureLength == 0) return true;
                    Geometry extracted =
                            lengthIndexedLine.extractLine(
                                    startOffset * calcLength / featureLength,
                                    stopOffset * calcLength / featureLength);
                    if (!extracted.isEmpty()) lineMerger.add(extracted);
                } else if (fromMeasure > featureFromMeasure) {
                    LengthIndexedLine lengthIndexedLine =
                            new LengthIndexedLine(
                                    (Geometry) feature.getDefaultGeometryProperty().getValue());
                    double featureLength = featureToMeasure - featureFromMeasure;
                    double startOffset = fromMeasure - featureFromMeasure;
                    double calcLength =
                            ((Geometry) feature.getDefaultGeometryProperty().getValue())
                                    .getLength();
                    if (calcLength == 0 || featureLength == 0) return true;
                    Geometry extracted =
                            lengthIndexedLine.extractLine(
                                    startOffset * calcLength / featureLength, calcLength);
                    if (!extracted.isEmpty()) lineMerger.add(extracted);
                } else {
                    LengthIndexedLine lengthIndexedLine =
                            new LengthIndexedLine(
                                    (Geometry) feature.getDefaultGeometryProperty().getValue());
                    double featureLength = featureToMeasure - featureFromMeasure;
                    double stopOffset = toMeasure - featureFromMeasure;
                    double calcLength =
                            ((Geometry) feature.getDefaultGeometryProperty().getValue())
                                    .getLength();
                    if (calcLength == 0 || featureLength == 0) return true;
                    Geometry extracted =
                            lengthIndexedLine.extractLine(
                                    0, stopOffset * calcLength / featureLength);
                    if (extracted.isEmpty() || extracted.getLength() == 0.0) {
                        LOGGER.info(
                                "Empty segment: featureFromMeasure="
                                        + featureFromMeasure
                                        + " featureToMeasure:"
                                        + featureToMeasure
                                        + " toMeasure:"
                                        + toMeasure
                                        + " fromMeasure:"
                                        + fromMeasure);
                    } else {
                        lineMerger.add(extracted);
                    }
                }
            } catch (Exception e) {
                LOGGER.warning(
                        "Error merging line strings: "
                                + e
                                + " featureFromMeasure="
                                + featureFromMeasure
                                + " featureToMeasure:"
                                + featureToMeasure
                                + " toMeasure:"
                                + toMeasure
                                + " fromMeasure:"
                                + fromMeasure);
            }
        }
        return false;
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
            Feature feature, SimpleFeatureType targetFeatureType, MultiLineString multiLineString)
            throws ProcessException {
        try {
            AttributeDescriptor geomAttbType = targetFeatureType.getGeometryDescriptor();
            Object[] attributes = new Object[targetFeatureType.getAttributeCount()];
            for (int i = 0; i < attributes.length; i++) {
                AttributeDescriptor attbType = targetFeatureType.getAttributeDescriptors().get(i);
                if (attbType.equals(geomAttbType)) {
                    attributes[i] = multiLineString;
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
