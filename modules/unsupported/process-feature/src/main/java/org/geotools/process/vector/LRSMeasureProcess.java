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
import org.geotools.measure.Measure;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LengthIndexedLine;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

@DescribeProcess(
        title = "Measure point in LRS",
        description =
                "Computes the measure of a point along a feature (as feature with attribute lrs_measure). The point is measured along the nearest feature.")
public class LRSMeasureProcess implements VectorProcess {
    private static final Logger LOGGER = Logging.getLogger(LRSMeasureProcess.class);

    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Process the input data set.
     *
     * @param featureCollection the data set
     * @param crs the CRS
     * @param point the given point
     * @return the snapped to feature
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
            @DescribeParameter(name = "point", description = "Point whose location to measure")
                    Point point,
            @DescribeParameter(
                            name = "crs",
                            min = 0,
                            description =
                                    "Coordinate reference system to use for input (default is the input collection CRS)")
                    CoordinateReferenceSystem crs)
            throws ProcessException {
        DefaultFeatureCollection results = new DefaultFeatureCollection();
        try {
            if (featureCollection == null || featureCollection.size() == 0) {
                LOGGER.info("No features provided in request");
                return results;
            }
            if (crs == null) {
                GeometryDescriptor gd = featureCollection.getSchema().getGeometryDescriptor();
                if (gd != null) {
                    crs = gd.getCoordinateReferenceSystem();
                }
            }
            if (crs == null) {
                throw new ProcessException(
                        "The CRS parameter was not provided and the feature collection does not have a default one either");
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
            if (point == null) {
                throw new ProcessException("The point parameter was not provided");
            }

            CoordinateReferenceSystem epsg4326;
            try {
                epsg4326 = CRS.decode("EPSG:4326");
            } catch (Exception e) {
                throw new ProcessException("Unknown CRS code: EPSG:4326", e);
            }
            MathTransform crsTransform = CRS.findMathTransform(crs, epsg4326);

            FeatureType targetFeatureType = createTargetFeatureType(featureCollection.getSchema());
            Feature nearestFeature = null;
            double nearestDistance = 9e9;
            Coordinate[] nearestCoords = null;
            try (FeatureIterator<? extends Feature> featureIterator =
                    featureCollection.features()) {
                while (featureIterator.hasNext()) {
                    SimpleFeature f = (SimpleFeature) featureIterator.next();
                    if (f.getDefaultGeometryProperty().getValue() == null) continue;
                    DistanceOp op =
                            new DistanceOp(
                                    point, (Geometry) f.getDefaultGeometryProperty().getValue());
                    Coordinate[] co = op.nearestPoints();
                    double[] co0 = {
                        co[0].x, co[0].y,
                    };
                    double[] co1 = {
                        co[1].x, co[1].y,
                    };
                    double[] geo0 = new double[2];
                    double[] geo1 = new double[2];
                    crsTransform.transform(co0, 0, geo0, 0, 1);
                    crsTransform.transform(co1, 0, geo1, 0, 1);

                    // get distance
                    Measure m = DefaultGeographicCRS.WGS84.distance(geo0, geo1);
                    if (m.doubleValue() > nearestDistance) continue;
                    nearestFeature = f;
                    nearestDistance = m.doubleValue();
                    nearestCoords = co;
                }
            }
            if (nearestFeature != null) {
                LengthIndexedLine lengthIndexedLine =
                        new LengthIndexedLine(
                                (Geometry) nearestFeature.getDefaultGeometryProperty().getValue());
                double lineIndex = lengthIndexedLine.indexOf(nearestCoords[1]);
                double lineLength =
                        ((Geometry) nearestFeature.getDefaultGeometryProperty().getValue())
                                .getLength();
                Double featureFromMeasure =
                        (Double) nearestFeature.getProperty(fromMeasureAttb).getValue();
                Double featureToMeasure =
                        (Double) nearestFeature.getProperty(toMeasureAttb).getValue();
                double lrsMeasure =
                        featureFromMeasure
                                + (featureToMeasure - featureFromMeasure) * lineIndex / lineLength;
                nearestFeature
                        .getDefaultGeometryProperty()
                        .setValue(
                                geometryFactory.createPoint(
                                        new Coordinate(nearestCoords[1].x, nearestCoords[1].y)));
                results.add(
                        createTargetFeature(
                                nearestFeature, (SimpleFeatureType) targetFeatureType, lrsMeasure));
                return results;
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
            for (PropertyDescriptor attbType : sourceFeatureType.getDescriptors()) {
                typeBuilder.add((AttributeDescriptor) attbType);
            }
            typeBuilder.minOccurs(1).maxOccurs(1).nillable(false).add("lrs_measure", Double.class);
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
            Feature feature, SimpleFeatureType targetFeatureType, Double lrsMeasure)
            throws ProcessException {
        try {
            AttributeDescriptor lrsMeasureAttbType = targetFeatureType.getDescriptor("lrs_measure");
            Object[] attributes = new Object[targetFeatureType.getAttributeCount()];
            for (int i = 0; i < attributes.length; i++) {
                AttributeDescriptor attbType = targetFeatureType.getAttributeDescriptors().get(i);
                if (attbType.equals(lrsMeasureAttbType)) {
                    attributes[i] = lrsMeasure;
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
