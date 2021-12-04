/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.geotools.coverage.grid.io.AbstractGridFormat.ELEVATION;
import static org.geotools.coverage.grid.io.AbstractGridFormat.TIME;
import static org.geotools.filter.capability.FunctionNameImpl.parameter;
import static org.geotools.gce.imagemosaic.ImageMosaicFormat.FILTER;
import static org.geotools.gce.imagemosaic.ImageMosaicFormat.MAX_ALLOWED_TILES;
import static org.geotools.gce.imagemosaic.ImageMosaicFormat.SORT_BY;
import static org.geotools.gce.imagemosaic.MosaicQueryBuilder.parseSortBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.CoverageReadingTransformation;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Polygon;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;

/**
 * Transformation returning the same granules that the {@link StructuredGridCoverage2DReader} would
 * have used as the input for image creation in the {@link
 * GridCoverage2DReader#read(GeneralParameterValue[])} method (with some exceptions, like footprints
 * usage, as this transformation only sets up a filter against the associated {@link
 * GranuleSource}).
 */
public class FootprintsTransformation extends FunctionImpl
        implements CoverageReadingTransformation {

    private static final String FOOTPRINTS = "footprints";
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "footprints",
                    parameter("result", SimpleFeatureCollection.class),
                    parameter("coverage", String.class, 0, 1));

    public FootprintsTransformation() {
        this.functionName = NAME;
    }

    @Override
    public SimpleFeatureCollection evaluate(Object object) {
        // sanity check
        if (!(object instanceof ReaderAndParams))
            throw new IllegalArgumentException(
                    "This filter function is supposed to be invoked on a ReaderAndParams object, "
                            + "but got this instead: "
                            + object);

        ReaderAndParams rap = (ReaderAndParams) object;
        GridCoverage2DReader reader = rap.getReader();

        if (reader instanceof StructuredGridCoverage2DReader) {
            return readStructured(rap);
        }
        return readSimple(reader, rap.getReadParameters());
    }

    private SimpleFeatureCollection readStructured(ReaderAndParams rap) {
        try {
            StructuredGridCoverage2DReader reader =
                    (StructuredGridCoverage2DReader) rap.getReader();

            String coverage = Converters.convert(getParameterValue(rap, 0), String.class);
            if (coverage == null) coverage = reader.getGridCoverageNames()[0];

            GranuleSource source = reader.getGranules(coverage, true);
            return source.getGranules(buildQuery(coverage, reader, rap.getReadParameters()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract the granules from the reader");
        }
    }

    private Query buildQuery(
            String coverage,
            StructuredGridCoverage2DReader reader,
            GeneralParameterValue[] readParameters)
            throws IOException {
        Query query = new Query(coverage);

        if (readParameters != null) {
            Integer maxTiles = getParameter(MAX_ALLOWED_TILES, Integer.class, readParameters);
            if (maxTiles != null) query.setMaxFeatures(maxTiles);

            String sorting = getParameter(SORT_BY, String.class, readParameters);
            if (sorting != null && !sorting.trim().isEmpty()) query.setSortBy(parseSortBy(sorting));

            Filter finalFilter = buildFilter(coverage, reader, readParameters);
            query.setFilter(finalFilter);
        }

        return query;
    }

    private Filter buildFilter(
            String coverage,
            StructuredGridCoverage2DReader reader,
            GeneralParameterValue[] readParameters)
            throws IOException {
        FilterFactory2 ff = FeatureUtilities.DEFAULT_FILTER_FACTORY;
        List<Filter> filters = new ArrayList<>();

        Filter filter = getParameter(FILTER, Filter.class, readParameters);
        if (filter != null) filters.add(filter);

        // bbox
        GridGeometry2D gg =
                getParameter(
                        AbstractGridFormat.READ_GRIDGEOMETRY2D,
                        GridGeometry2D.class,
                        readParameters);
        if (gg != null) {
            filters.add(ff.bbox(ff.property(""), gg.getEnvelope2D()));
        }

        List<DimensionDescriptor> dimensions = reader.getDimensionDescriptors(coverage);

        // time
        DimensionDescriptor timeDescriptor = getDimension(dimensions, DimensionDescriptor.TIME);
        List time = getParameter(TIME, List.class, readParameters);
        if (time != null && timeDescriptor != null) filters.add(buildFilter(timeDescriptor, time));

        // elevation
        DimensionDescriptor elevationDescriptor =
                getDimension(dimensions, DimensionDescriptor.ELEVATION);
        List elevation = getParameter(ELEVATION, List.class, readParameters);
        if (elevation != null && elevationDescriptor != null)
            filters.add(buildFilter(elevationDescriptor, elevation));

        // custom dimensions
        getCustomDimensions(reader, dimensions, readParameters)
                .forEach((dd, values) -> filters.add(buildFilter(dd, values)));

        if (filters.isEmpty()) {
            return Filter.INCLUDE;
        } else if (filters.size() == 1) {
            return filters.get(0);
        }
        return ff.and(filters);
    }

    /**
     * Getting the list of custom dimension values is tricky. Conditions:
     *
     * <ul>
     *   <li>The custom dimension is listed as a reader dynamic parameter
     *   <li>It matches a domain descriptor
     *   <li>Has a value among the read parameters
     * </ul>
     */
    private Map<DimensionDescriptor, List> getCustomDimensions(
            GridCoverage2DReader reader,
            List<DimensionDescriptor> dimensions,
            GeneralParameterValue[] readParameters)
            throws IOException {
        Set<ParameterDescriptor<List>> descriptors = reader.getDynamicParameters();
        Map<DimensionDescriptor, List> result = new HashMap<>();
        descriptors.forEach(
                pd -> {
                    DimensionDescriptor dd = getDimension(dimensions, pd.getName().getCode());
                    if (dd != null) {
                        List value = getParameter(pd, List.class, readParameters);
                        if (value != null) result.put(dd, value);
                    }
                });
        return result;
    }

    private DimensionDescriptor getDimension(List<DimensionDescriptor> dimensions, String name) {
        return dimensions.stream()
                .filter(dd -> name.equalsIgnoreCase(dd.getName()))
                .findFirst()
                .orElse(null);
    }

    private Filter buildFilter(DimensionDescriptor dd, List values) {
        return new DomainFilterBuilder(dd.getName(), dd.getStartAttribute(), dd.getEndAttribute())
                .createFilter(values);
    }

    private <T> T getParameter(
            ParameterDescriptor pd, Class<T> clazz, GeneralParameterValue[] readParameters) {
        return Arrays.stream(readParameters)
                .filter(p -> pd.getName().equals(p.getDescriptor().getName()))
                .filter(p -> p instanceof ParameterValue)
                .map(p -> (ParameterValue) p)
                .map(p -> Converters.convert(p.getValue(), clazz))
                .filter(v -> v != null)
                .findFirst()
                .orElse(null);
    }

    /** Not a structured reader... we'll return the footprint of the reader */
    private SimpleFeatureCollection readSimple(
            GridCoverage2DReader reader, GeneralParameterValue[] readParameters) {
        ReferencedEnvelope bbox = ReferencedEnvelope.reference(reader.getOriginalEnvelope());
        Polygon polygon = JTS.toGeometry(bbox);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, reader.getCoordinateReferenceSystem());
        tb.setName(FOOTPRINTS);
        SimpleFeatureType schema = tb.buildFeatureType();

        SimpleFeature feature =
                SimpleFeatureBuilder.build(schema, new Object[] {polygon}, FOOTPRINTS + ".1");

        return DataUtilities.collection(feature);
    }

    @Override
    public Query invertQuery(Query targetQuery, GridGeometry gridGeometry) {
        return targetQuery;
    }

    @Override
    public GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry) {
        return targetGridGeometry;
    }
}
