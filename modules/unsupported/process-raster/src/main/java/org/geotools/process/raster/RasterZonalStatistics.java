/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.media.jai.ROI;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.GridCoverage2DRIA;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.NumberRange;
import org.jaitools.media.jai.zonalstats.ZonalStats;
import org.jaitools.media.jai.zonalstats.ZonalStatsDescriptor;
import org.jaitools.media.jai.zonalstats.ZonalStatsOpImage;
import org.jaitools.numeric.Range;
import org.jaitools.numeric.Statistic;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * A process computing zonal statistics based on a raster data set and a set of polygonal zones of
 * interest
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Emanuele Tajariol (GeoSolutions)
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(
        title = "Raster Zonal Statistics",
        description =
                "Computes statistics for the distribution of a certain quantity in a set of polygonal zones.")
public class RasterZonalStatistics implements RasterProcess {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    @DescribeResult(
            name = "statistics",
            description =
                    "A feature collection with the attributes of the zone layer (prefixed by 'z_') and the statistics fields count,min,max,sum,avg,stddev")
    public SimpleFeatureCollection execute(
            @DescribeParameter(
                            name = "data",
                            description = "Input raster to compute statistics for")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "band",
                            description = "Source band used to compute statistics (default is 0)",
                            min = 0,
                            defaultValue = "0")
                    Integer band,
            @DescribeParameter(
                            name = "zones",
                            description = "Zone polygon features for which to compute statistics")
                    SimpleFeatureCollection zones,
            @DescribeParameter(
                            name = "classification",
                            description =
                                    "Raster whose values will be used as classes for the statistical analysis. Each zone reports statistics partitioned by classes according to the values of the raster. Must be a single band raster with integer values.",
                            min = 0)
                    GridCoverage2D classification) {
        int iband = 0;
        if (band != null) {
            iband = band;
        }

        return new RasterZonalStatisticsCollection(coverage, iband, zones, classification);
    }

    /**
     * A feature collection that computes zonal statitics in a streaming fashion
     *
     * @author Andrea Aime - OpenGeo
     */
    static class RasterZonalStatisticsCollection extends DecoratingSimpleFeatureCollection {
        GridCoverage2D coverage;

        SimpleFeatureType targetSchema;

        int band;

        GridCoverage2D classification;

        public RasterZonalStatisticsCollection(
                GridCoverage2D coverage,
                int band,
                SimpleFeatureCollection zones,
                GridCoverage2D classification) {
            super(zones);
            this.coverage = coverage;
            this.band = band;
            this.classification = classification;

            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor att : zones.getSchema().getAttributeDescriptors()) {
                tb.minOccurs(att.getMinOccurs());
                tb.maxOccurs(att.getMaxOccurs());
                tb.restrictions(att.getType().getRestrictions());
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor gatt = (GeometryDescriptor) att;
                    tb.crs(gatt.getCoordinateReferenceSystem());
                }
                tb.add("z_" + att.getLocalName(), att.getType().getBinding());
            }
            if (classification != null) {
                tb.add("classification", Integer.class);
            }
            tb.add("count", Long.class);
            tb.add("min", Double.class);
            tb.add("max", Double.class);
            tb.add("sum", Double.class);
            tb.add("avg", Double.class);
            tb.add("stddev", Double.class);
            tb.setName(zones.getSchema().getName());
            targetSchema = tb.buildFeatureType();
        }

        @Override
        public SimpleFeatureType getSchema() {
            return targetSchema;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new RasterZonalStatisticsIterator(
                    delegate.features(), coverage, band, targetSchema, classification);
        }
    }

    /** An iterator computing statistics as we go */
    static class RasterZonalStatisticsIterator implements SimpleFeatureIterator {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        SimpleFeatureIterator zones;

        SimpleFeatureBuilder builder;

        GridCoverage2D dataCoverage;

        int band;

        RenderedImage classificationRaster;

        List<SimpleFeature> features = new ArrayList<>();

        public RasterZonalStatisticsIterator(
                SimpleFeatureIterator zones,
                GridCoverage2D coverage,
                int band,
                SimpleFeatureType targetSchema,
                GridCoverage2D classification) {
            this.zones = zones;
            this.builder = new SimpleFeatureBuilder(targetSchema);
            this.dataCoverage = coverage;
            this.band = band;

            // prepare the classification image if necessary
            if (classification != null) {
                // find nodata values
                GridSampleDimension sampleDimension = classification.getSampleDimension(0);
                double[] nodataarr = sampleDimension.getNoDataValues();
                double[] nodata = nodataarr != null ? nodataarr : new double[] {Double.NaN};

                // this will adapt the classification image to the projection and image layout
                // of the data coverage
                classificationRaster =
                        GridCoverage2DRIA.create(classification, dataCoverage, nodata);
            }
        }

        @Override
        public void close() {
            zones.close();
        }

        @Override
        public boolean hasNext() {
            return !features.isEmpty() || zones.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            // build the next set of features if necessary
            if (features.isEmpty()) {
                // grab the current zone
                SimpleFeature zone = zones.next();

                try {
                    // grab the geometry and eventually reproject it to the
                    Geometry zoneGeom = (Geometry) zone.getDefaultGeometry();
                    CoordinateReferenceSystem dataCrs = dataCoverage.getCoordinateReferenceSystem();
                    CoordinateReferenceSystem zonesCrs =
                            builder.getFeatureType()
                                    .getGeometryDescriptor()
                                    .getCoordinateReferenceSystem();
                    if (!CRS.equalsIgnoreMetadata(zonesCrs, dataCrs)) {
                        zoneGeom =
                                JTS.transform(
                                        zoneGeom, CRS.findMathTransform(zonesCrs, dataCrs, true));
                    }

                    // gather the statistics
                    ZonalStats stats = processStatistics(zoneGeom);

                    // build the resulting feature
                    if (stats != null) {
                        if (classificationRaster != null) {
                            // if zonal stats we're going to build
                            for (Integer classZoneId : stats.getZones()) {
                                builder.addAll(zone.getAttributes());
                                builder.add(classZoneId);
                                addStatsToFeature(stats.zone(classZoneId));
                                features.add(builder.buildFeature(zone.getID()));
                            }
                        } else {
                            builder.addAll(zone.getAttributes());
                            addStatsToFeature(stats);
                            features.add(builder.buildFeature(zone.getID()));
                        }
                    } else {
                        builder.addAll(zone.getAttributes());
                        features.add(builder.buildFeature(zone.getID()));
                    }
                } catch (Exception e) {
                    throw new ProcessException(
                            "Failed to compute statistics on feature " + zone, e);
                }
            }
            // return the first feature in the current buffer
            SimpleFeature f = features.remove(0);
            return f;
        }

        /** Add the statistics to the feature builder */
        void addStatsToFeature(ZonalStats stats) {
            double sum = stats.statistic(Statistic.SUM).results().get(0).getValue();
            double avg = stats.statistic(Statistic.MEAN).results().get(0).getValue();
            double count = stats.statistic(Statistic.MEAN).results().get(0).getNumAccepted();
            builder.add(count); // count
            builder.add(stats.statistic(Statistic.MIN).results().get(0).getValue());
            builder.add(stats.statistic(Statistic.MAX).results().get(0).getValue());
            builder.add(sum);
            builder.add(avg);
            builder.add(stats.statistic(Statistic.SDEV).results().get(0).getValue());
        }

        private ZonalStats processStatistics(Geometry geometry) throws TransformException {
            // double checked with the tasmania simple test data, this transformation
            // actually lines up the polygons where they are supposed to be in raster space
            final AffineTransform dataG2WCorrected =
                    new AffineTransform(
                            (AffineTransform)
                                    dataCoverage
                                            .getGridGeometry()
                                            .getGridToCRS2D(PixelOrientation.UPPER_LEFT));
            final MathTransform w2gTransform;
            try {
                w2gTransform = ProjectiveTransform.create(dataG2WCorrected.createInverse());
            } catch (NoninvertibleTransformException e) {
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }

            GridCoverage2D cropped = null;
            try {
                // first off, cut the geometry around the coverage bounds if necessary
                ReferencedEnvelope coverageEnvelope =
                        new ReferencedEnvelope(dataCoverage.getEnvelope2D());
                ReferencedEnvelope geometryEnvelope =
                        new ReferencedEnvelope(
                                geometry.getEnvelopeInternal(),
                                dataCoverage.getCoordinateReferenceSystem());
                if (!coverageEnvelope.intersects((Envelope) geometryEnvelope)) {
                    // no intersection, no stats
                    return null;
                } else if (!coverageEnvelope.contains((Envelope) geometryEnvelope)) {
                    // the geometry goes outside of the coverage envelope, that makes
                    // the stats fail for some reason
                    geometry = JTS.toGeometry((Envelope) coverageEnvelope).intersection(geometry);
                    geometryEnvelope =
                            new ReferencedEnvelope(
                                    geometry.getEnvelopeInternal(),
                                    dataCoverage.getCoordinateReferenceSystem());
                }

                // check if the novalue is != from NaN
                GridSampleDimension sampleDimension = dataCoverage.getSampleDimension(0);
                List<Category> categories = sampleDimension.getCategories();
                List<Range<Double>> novalueRangeList = null;
                if (categories != null) {
                    for (Category category : categories) {
                        String catName = category.getName().toString();
                        if (catName.equalsIgnoreCase("no data")) {
                            NumberRange range = category.getRange();
                            double min = range.getMinimum();
                            double max = category.getRange().getMaximum();
                            if (!Double.isNaN(min) && !Double.isNaN(max)) {
                                // we have to filter those out
                                Range<Double> novalueRange = new Range<>(min, true, max, true);
                                novalueRangeList = new ArrayList<>();
                                novalueRangeList.add(novalueRange);
                            }
                            break;
                        }
                    }
                }

                /*
                 * crop on region of interest
                 */
                ParameterValueGroup param = PROCESSOR.getOperation("CoverageCrop").getParameters();
                param.parameter("Source").setValue(dataCoverage);
                param.parameter("Envelope").setValue(new GeneralEnvelope(geometryEnvelope));
                cropped = (GridCoverage2D) PROCESSOR.doOperation(param);

                // transform the geometry to raster space so that we can use it as a ROI source
                Geometry rasterSpaceGeometry = JTS.transform(geometry, w2gTransform);
                // System.out.println(rasterSpaceGeometry);
                // System.out.println(rasterSpaceGeometry.getEnvelopeInternal());

                // simplify the geometry so that it's as precise as the coverage, excess coordinates
                // just make it slower to determine the point in polygon relationship
                Geometry simplifiedGeometry =
                        DouglasPeuckerSimplifier.simplify(rasterSpaceGeometry, 1);
                // System.out.println(simplifiedGeometry.getEnvelopeInternal());

                // compensate for the jaitools range lookup poking the corner of the cells instead
                // of their center, this makes for odd results if the polygon is just slightly
                // misaligned with the coverage
                AffineTransformation at = new AffineTransformation();

                at.setToTranslation(-0.5, -0.5);
                simplifiedGeometry.apply(at);

                // build a shape using a fast point in polygon wrapper
                ROI roi = new ROIGeometry(simplifiedGeometry, false);

                // run the stats via JAI
                Statistic[] reqStatsArr = {
                    Statistic.MAX,
                    Statistic.MIN,
                    Statistic.RANGE,
                    Statistic.MEAN,
                    Statistic.SDEV,
                    Statistic.SUM
                };
                final ZonalStatsOpImage zsOp =
                        new ZonalStatsOpImage(
                                cropped.getRenderedImage(),
                                classificationRaster,
                                null,
                                null,
                                reqStatsArr,
                                new Integer[] {band},
                                roi,
                                null,
                                null,
                                null,
                                false,
                                novalueRangeList);
                return (ZonalStats) zsOp.getProperty(ZonalStatsDescriptor.ZONAL_STATS_PROPERTY);
            } finally {
                // dispose coverages
                if (cropped != null) {
                    cropped.dispose(true);
                }
            }
        }
    }
}
