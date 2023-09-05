/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.coverage.grid.GridGeometry;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.api.data.Query;
import org.geotools.data.elasticsearch.GeohashUtil;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.vector.VectorProcess;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;

@SuppressWarnings("unused")
@DescribeProcess(
        title = "geoHashGridAgg",
        description =
                "Computes a grid from GeoHash grid aggregation buckets with values corresponding to doc_count values.")
public class GeoHashGridProcess implements VectorProcess {

    private static final FilterFactory FILTER_FACTORY = CommonFactoryFinder.getFilterFactory(null);

    public enum Strategy {
        BASIC(BasicGeoHashGrid.class),
        METRIC(MetricGeoHashGrid.class),
        NESTED_AGG(NestedAggGeoHashGrid.class);

        private final Class<? extends GeoHashGrid> clazz;

        Strategy(Class<? extends GeoHashGrid> clazz) {
            this.clazz = clazz;
        }

        GeoHashGrid createNewInstance() throws ReflectiveOperationException {
            return clazz.getConstructor().newInstance();
        }
    }

    @DescribeResult(description = "Output raster")
    public GridCoverage2D execute(

            // process data
            @DescribeParameter(name = "data", description = "Input features")
                    SimpleFeatureCollection obsFeatures,

            // process parameters
            @DescribeParameter(
                            name = "gridStrategy",
                            description = "GeoHash grid strategy",
                            defaultValue = "Basic",
                            min = 0)
                    String gridStrategy,
            @DescribeParameter(
                            name = "gridStrategyArgs",
                            description = "Grid strategy arguments",
                            min = 0)
                    List<String> gridStrategyArgs,
            @DescribeParameter(name = "emptyCellValue", description = "Default cell value", min = 0)
                    Float emptyCellValue,
            @DescribeParameter(name = "scaleMin", description = "Scale minimum", defaultValue = "0")
                    Float scaleMin,
            @DescribeParameter(name = "scaleMax", description = "Scale maximum", min = 0)
                    Float scaleMax,
            @DescribeParameter(
                            name = "useLog",
                            description = "Whether to use log values (default=false)",
                            defaultValue = "false")
                    Boolean useLog,

            // output image parameters
            @DescribeParameter(name = "outputBBOX", description = "Bounding box of the output")
                    ReferencedEnvelope argOutputEnv,
            @DescribeParameter(
                            name = "outputWidth",
                            description = "Width of output raster in pixels")
                    Integer argOutputWidth,
            @DescribeParameter(
                            name = "outputHeight",
                            description = "Height of output raster in pixels")
                    Integer argOutputHeight,
            @DescribeParameter(
                            name = "aggregationDefinition",
                            description = "Native Elasticsearch Aggregation definition",
                            min = 0)
                    String aggregationDefinition,
            @DescribeParameter(
                            name = "queryDefinition",
                            description = "Native Elasticsearch Query definition",
                            min = 0)
                    String queryDefinition,
            ProgressListener monitor)
            throws ProcessException {

        try {
            // setup sane defaults for aggregation definition, if missing
            if (aggregationDefinition == null)
                aggregationDefinition = defaultAggregation(argOutputEnv, argOutputWidth);

            // construct and populate grid
            final GeoHashGrid geoHashGrid =
                    Strategy.valueOf(gridStrategy.toUpperCase()).createNewInstance();
            geoHashGrid.setParams(gridStrategyArgs);
            geoHashGrid.setEmptyCellValue(emptyCellValue);
            geoHashGrid.setScale(new RasterScale(scaleMin, scaleMax, useLog));
            geoHashGrid.initalize(
                    argOutputEnv, obsFeatures, aggregationDefinition, queryDefinition);
            // convert to grid coverage
            return geoHashGrid.toGridCoverage2D();
        } catch (Exception e) {
            throw new ProcessException("Error executing GeoHashGridProcess", e);
        }
    }

    String defaultAggregation(ReferencedEnvelope envelope, Integer width) {
        int precision = GeohashUtil.getPrecisionFromScale(envelope, width);
        // sets an empty field, which the store will fill in later before encoding the request
        return "{\"agg\": {\"geohash_grid\": {\"size\": 65536, \"field\": \"\", \"precision\": "
                + precision
                + "}}}";
    }

    public Query invertQuery(
            @DescribeParameter(
                            name = "outputBBOX",
                            description = "Georeferenced bounding box of the output")
                    ReferencedEnvelope envelope,
            @DescribeParameter(
                            name = "outputWidth",
                            description = "Width of output raster in pixels")
                    Integer argOutputWidth,
            @DescribeParameter(
                            name = "aggregationDefinition",
                            description = "Native Elasticsearch Aggregation definition",
                            min = 0)
                    String aggregationDefinition,
            Query targetQuery,
            GridGeometry targetGridGeometry)
            throws ProcessException {

        final BBOXRemovingFilterVisitor visitor = new BBOXRemovingFilterVisitor();
        Filter filter = (Filter) targetQuery.getFilter().accept(visitor, null);
        String geometryName = visitor.getGeometryPropertyName();
        if (geometryName == null) {
            geometryName = "";
        }
        final BBOX bbox;
        try {
            boolean reproject =
                    !CRS.equalsIgnoreMetadata(
                            envelope.getCoordinateReferenceSystem(), DefaultGeographicCRS.WGS84);
            if (reproject) {
                envelope = envelope.transform(DefaultGeographicCRS.WGS84, false);
                Integer precision;
                if (aggregationDefinition == null) {
                    aggregationDefinition = defaultAggregation(envelope, argOutputWidth);
                }
                precision = GeohashUtil.getPrecision(aggregationDefinition);
                envelope = GridCoverageUtil.pad(envelope, precision);
            }

            bbox =
                    FILTER_FACTORY.bbox(
                            geometryName,
                            envelope.getMinX(),
                            envelope.getMinY(),
                            envelope.getMaxX(),
                            envelope.getMaxY(),
                            "EPSG:4326");
        } catch (Exception e) {
            throw new ProcessException("Unable to create bbox filter for feature source", e);
        }
        filter =
                (Filter)
                        FILTER_FACTORY
                                .and(filter, bbox)
                                .accept(new SimplifyingFilterVisitor(), null);
        targetQuery.setFilter(filter);

        final List<PropertyName> properties = new ArrayList<>();
        properties.add(FILTER_FACTORY.property("_aggregation"));
        targetQuery.setProperties(properties);
        return targetQuery;
    }
}
