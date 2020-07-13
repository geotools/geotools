/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.CloseableIterator;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.FileResourceInfo;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.Range;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * {@link FileResourceInfo} implementation for ImageMosaic. The specific implementation is able to
 * retrieve support files such as, as an instance, prj and world file for TIFFs.
 */
public class ImageMosaicFileResourceInfo extends DefaultResourceInfo implements FileResourceInfo {

    static final Logger LOGGER = Logging.getLogger(ImageMosaicFileResourceInfo.class);

    /**
     * A {@link CloseableIterator} implementation taking care of retrieving {@link FileGroup}s from
     * a {@link SimpleFeatureIterator}.
     *
     * <p>Note on files grouping: Each returned FileGroup should contain a different file. When
     * dealing with multidimensional data, multiple features can contain same file (records will be
     * different in terms of time value, elevation value, and so on. Therefore, we need to aggregate
     * features related to the same file location by scanning the underlying iterator and caching
     * the next feature which doesn't belong to same file.
     *
     * <p>Important note: the featureIterator need to be get using a sortBy clause on location to
     * make sure underlying features come sorted by location.
     */
    class CloseableFileGroupIterator implements CloseableIterator<FileGroup> {

        public CloseableFileGroupIterator(SimpleFeatureIterator featureIterator) {
            this.featureIterator = featureIterator;
        }

        SimpleFeature cachedNext = null;

        SimpleFeatureIterator featureIterator;

        @Override
        public boolean hasNext() {
            return featureIterator.hasNext() || cachedNext != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation isn't supported");
        }

        @Override
        public FileGroup next() {
            SimpleFeature next = null;

            // look for cached feature
            if (cachedNext != null) {
                next = cachedNext;
                cachedNext = null;
            } else {
                next = featureIterator.next();
            }

            // Avoid adding the feature to a collection to reduce memory consumption
            // we only take note of the firstFeature
            int groupedFeatures = 0;
            SimpleFeature firstFeature = null;

            // resolve the location
            String granuleLocation = (String) next.getAttribute(locationAttributeName);
            URL resolved = pathType.resolvePath(parentLocation, granuleLocation);
            File file = null;
            if (resolved != null) {
                file = URLs.urlToFile(resolved);
                if (file != null && file.exists()) {
                    groupedFeatures++;
                    firstFeature = next;
                }
            }
            if (groupedFeatures == 0) {
                return null;
            }

            while (featureIterator.hasNext()) {
                // Group features sharing same location
                next = featureIterator.next();
                String nextLocation = (String) next.getAttribute(locationAttributeName);
                if (granuleLocation.equalsIgnoreCase(nextLocation)) {
                    groupedFeatures++;
                } else {
                    cachedNext = next;
                    break;
                }
            }

            // I have to group the features to get the ranges.
            return buildFileGroup(file, groupedFeatures > 1, firstFeature);
        }

        /**
         * Aggregate multipleFeatures related to the same file, on the same {@link FileGroup}. This
         * is usually needed when the underlying coverage isn't a simple 2D coverage but it has
         * multiple dimensions (as an instance, time, elevation, custom...)
         *
         * <p>The method also look for supportFiles.
         *
         * @paran aggregate whether aggregation queries should be invoked to extract domains
         * @param firstFeature sample feature to be used when no aggregation is needed
         */
        private FileGroup buildFileGroup(File file, boolean aggregate, SimpleFeature firstFeature) {
            // Looking for supportFiles for the current file
            // As an instance .prj and .tfw for un-georeferenced tifs
            String filePath = file.getAbsolutePath();
            List<File> supportFiles = null;
            SupportFilesCollector collector = SupportFilesCollector.getCollectorFor(file);
            if (collector != null) {
                supportFiles = collector.getSupportFiles(filePath);
            }
            Map<String, Object> metadataMap =
                    computeGroupMetadata(filePath, aggregate, firstFeature);

            return new FileGroup(file, supportFiles, metadataMap);
        }

        /** Collects features domain to be exposed as metadata */
        private Map<String, Object> computeGroupMetadata(
                String filePath, boolean aggregate, SimpleFeature firstFeature) {
            Map<String, Object> metadataMap = null;
            List<DimensionDescriptor> dimensionDescriptors =
                    rasterManager.getDimensionDescriptors();
            // extract metadata for the available domains
            if (dimensionDescriptors != null && !dimensionDescriptors.isEmpty()) {
                Filter filter = FF.equals(FF.property("location"), FF.literal(filePath));
                metadataMap = new HashMap<String, Object>();
                try {
                    // scan dimensions
                    for (DimensionDescriptor descriptor : dimensionDescriptors) {
                        String attribute = descriptor.getStartAttribute();
                        String name = descriptor.getName();
                        Comparable max = null;
                        Comparable min = null;
                        if (aggregate) {
                            Query query = new Query(typeName);
                            query.setFilter(filter);
                            query.setPropertyNames(Arrays.asList(attribute));
                            // Repeat the queries to avoid using a in-Memory
                            // featureCollection
                            // We may consider caching the features in case
                            // the collection size isn't too big

                            final MaxVisitor maxVisitor = new MaxVisitor(attribute);
                            granuleCatalog.computeAggregateFunction(query, maxVisitor);
                            max = maxVisitor.getMax();
                            MinVisitor minVisitor = new MinVisitor(attribute);
                            granuleCatalog.computeAggregateFunction(query, minVisitor);
                            min = minVisitor.getMin();
                        } else {
                            max = min = (Comparable) firstFeature.getAttribute(attribute);
                        }
                        addMetadaElement(name, min, max, metadataMap);
                    }

                    addBBOX(aggregate, filter, firstFeature, metadataMap);
                } catch (IOException e) {
                    throw new RuntimeException(
                            "Exception occurred while parsing the feature domains", e);
                }
            }
            return metadataMap;
        }

        /** Add the bbox element to the metadata Map */
        private void addBBOX(
                boolean aggregate,
                Filter filter,
                SimpleFeature firstFeature,
                Map<String, Object> metadataMap)
                throws IOException {
            ReferencedEnvelope envelope = null;
            if (aggregate) {
                BoundsVisitor boundsVisitor = new BoundsVisitor();
                Query query = new Query(typeName);
                query.setFilter(filter);
                granuleCatalog.computeAggregateFunction(query, boundsVisitor);
                envelope = boundsVisitor.getBounds();
            } else {
                envelope = new ReferencedEnvelope(firstFeature.getBounds());
            }
            if (envelope != null) {
                metadataMap.put(Utils.BBOX, envelope);
            }
        }

        /** Add a metadata element to the FileGroup metadata map */
        private void addMetadaElement(
                String name, Comparable min, Comparable max, Map<String, Object> metadataMap) {
            if (Utils.TIME_DOMAIN.equalsIgnoreCase(name) || min instanceof Date) {
                metadataMap.put(name.toUpperCase(), new DateRange((Date) min, (Date) max));
            } else if (Utils.ELEVATION_DOMAIN.equalsIgnoreCase(name) || min instanceof Number) {
                metadataMap.put(
                        name.toUpperCase(),
                        NumberRange.create(
                                ((Number) min).doubleValue(),
                                true,
                                ((Number) max).doubleValue(),
                                true));
            } else {
                metadataMap.put(name, new Range(String.class, (String) min, (String) max));
            }
        }

        @Override
        public void close() throws IOException {
            featureIterator.close();
        }
    }

    private static final FilterFactory2 FF = FeatureUtilities.DEFAULT_FILTER_FACTORY;

    /**
     * parentLocation used to rebuild full file paths in case the imageMosaic is storing granules
     * location on DB with relative paths
     */
    private String parentLocation;

    /** location attribute name on DB */
    private String locationAttributeName;

    /**
     * The RasterManager used to retrieve granule index info such as typeName, PathType,
     * granuleCatalog, dimensions and attributes
     */
    private RasterManager rasterManager;

    /** Whether the granules are stored on DB as relative or absolute paths. */
    private PathType pathType;

    /** typeName to retrieve granules for a specific coverage */
    private String typeName;

    /** The underlying granules catalog. Needed to retrieve granules location */
    private GranuleCatalog granuleCatalog;

    /**
     * ImageMosaicFileResourceInfo constructor
     *
     * @param rasterManager manager the {@link RasterManager} instance for underlying index info
     *     retrieval and management
     * @param parentLocation the granules parentLocation (relative paths refer to that)
     * @param locationAttributeName the actual location attribute name
     */
    public ImageMosaicFileResourceInfo(
            RasterManager rasterManager, String parentLocation, String locationAttributeName) {
        this.rasterManager = rasterManager;
        this.granuleCatalog = rasterManager.getGranuleCatalog();
        this.typeName = rasterManager.getTypeName();
        this.pathType = rasterManager.getPathType();
        this.parentLocation = parentLocation;
        this.locationAttributeName = locationAttributeName;
    }

    @Override
    public CloseableIterator<FileGroup> getFiles(Query query) {
        // normally the different type names are actually sharing the same files, but we cannot be
        // sure, a manually setup mosaic could indeed have multiple types with different files in
        // them...
        SimpleFeatureCollection fc = null;
        try {
            Query updatedQuery = query != null ? query : new Query();
            Filter filter = updatedQuery.getFilter();

            // TODO: Improve this check since it may contain multiple filters
            if (!"location".equalsIgnoreCase(locationAttributeName)
                    && filter instanceof LikeFilterImpl) {
                // Rewrap the filter to update the file search
                LikeFilterImpl likeFilter = (LikeFilterImpl) filter;
                AttributeExpressionImpl impl = (AttributeExpressionImpl) likeFilter.getExpression();
                String attribute = impl.getPropertyName();
                String value = likeFilter.getLiteral();
                if ("location".equalsIgnoreCase(attribute)) {
                    // The invoker provided a default "location" attribute.
                    // make sure to remap it to the actual location attribute
                    attribute = locationAttributeName;
                    updatedQuery.setFilter(FF.like(FF.property(attribute), value));
                }
            }
            final List<SortBy> clauses = new ArrayList<SortBy>(1);
            clauses.add(
                    new SortByImpl(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(locationAttributeName),
                            SortOrder.ASCENDING));
            final SortBy[] sb = clauses.toArray(new SortBy[] {});
            final boolean isSortBySupported =
                    granuleCatalog.getQueryCapabilities(typeName).supportsSorting(sb);
            if (isSortBySupported) {
                updatedQuery.setSortBy(sb);
            } else {
                LOGGER.severe(
                        "Sorting parameter ignored, underlying datastore cannot sort on "
                                + Arrays.toString(sb));
            }
            updatedQuery.setTypeName(typeName);

            // TODO: Make sure to add different iterator for stores
            // not supporting sortBy (which DB based stores don't support sorting?)

            // Get all the features matching the query
            fc = granuleCatalog.getGranules(updatedQuery);
            return new CloseableFileGroupIterator(fc.features());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
