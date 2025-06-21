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
package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.api.data.CloseableIterator;
import org.geotools.api.data.FileResourceInfo;
import org.geotools.api.data.Query;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog.WrappedCoverageSlicesCatalog;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.Range;
import org.geotools.util.URLs;

/** {@link FileResourceInfo} implementation for NetCDF. */
class NetCDFFileResourceInfo extends DefaultResourceInfo implements FileResourceInfo {

    /**
     * A {@link CloseableIterator} implementation taking care of retrieving {@link FileGroup}s from a
     * {@link CoverageSlice}'s list.
     *
     * <p>Note on files grouping: Each returned FileGroup should contain a different file. When dealing with
     * multidimensional data using a shared catalog, multiple features can contain same file (records will be different
     * in terms of time value, elevation value, and so on. Therefore, we need to aggregate features related to the same
     * file location by scanning the underlying iterator and caching the next feature which doesn't belong to same file.
     */
    class WrappedCoverageSlicesToFileGroupIterator extends SimpleCoverageSlicesToFileGroupIterator {

        public WrappedCoverageSlicesToFileGroupIterator(List<CoverageSlice> slices) {
            super(slices);
            file = URLs.urlToFile(sourceURL);
            location = file.getAbsolutePath();
        }

        private File file;
        private String location;
        private CoverageSlice cachedNext = null;

        @Override
        public boolean hasNext() {
            return super.hasNext() || cachedNext != null;
        }

        @Override
        public FileGroup next() {
            CoverageSlice next = null;

            // look for cached feature
            if (cachedNext != null) {
                next = cachedNext;
                cachedNext = null;
            } else {
                next = slicesIterator.next();
            }

            int groupedFeatures = 0;

            // resolve the location
            List<CoverageSlice> relevantSlices = new ArrayList<>();
            relevantSlices.add(next);
            File file = null;
            if (sourceURL != null) {
                file = URLs.urlToFile(sourceURL);
                if (file != null && file.exists()) {
                    groupedFeatures++;
                }
            }
            if (groupedFeatures == 0) {
                return null;
            }

            while (slicesIterator.hasNext()) {
                // Group features sharing same location
                next = slicesIterator.next();
                relevantSlices.add(next);
                String nextLocation = (String) next.getOriginator().getAttribute(CoverageSlice.Attributes.LOCATION);
                if (location.equalsIgnoreCase(nextLocation)) {
                    groupedFeatures++;
                } else {
                    cachedNext = next;
                    break;
                }
            }

            // I have to group the features to get the ranges.
            try {
                return buildFileGroup(relevantSlices);
            } catch (IOException e) {
                throw new RuntimeException("Exception occurred while populating the fileGroup:", e);
            }
        }

        /**
         * Aggregate multipleFeatures related to the same file, on the same {@link FileGroup}. This is usually needed
         * when the underlying coverage isn't a simple 2D coverage but it has multiple dimensions (as an instance, time,
         * elevation, custom...)
         *
         * <p>The method also look for supportFiles.
         */
        private FileGroup buildFileGroup(List<CoverageSlice> slices) throws IOException {
            List<File> supportFiles = null;
            Map<String, Object> metadataMap = computeSlicesMetadata(slices);
            // Change this when we start supporting multiple BBOXes within same file
            metadataMap.put(Utils.BBOX, new ReferencedEnvelope(slices.get(0).getGranuleBBOX()));
            return new FileGroup(file, supportFiles, metadataMap);
        }
    }

    /**
     * A {@link CloseableIterator} implementation taking care of retrieving {@link FileGroup}s from an
     * {@link CoverageSlice}'s list.
     */
    class SimpleCoverageSlicesToFileGroupIterator implements CloseableIterator<FileGroup> {

        public SimpleCoverageSlicesToFileGroupIterator(List<CoverageSlice> slices) {
            this.slices = slices;
            this.slicesIterator = slices.iterator();
        }

        protected List<CoverageSlice> slices = null;

        protected Iterator<CoverageSlice> slicesIterator;

        @Override
        public boolean hasNext() {
            return slicesIterator.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation isn't supported");
        }

        @Override
        public FileGroup next() {
            File file = null;
            if (sourceURL != null) {
                file = URLs.urlToFile(sourceURL);
                if (file == null || !file.exists()) {
                    throw new IllegalArgumentException("Unable to get a FileGroup on top of file:  " + file);
                }
            }

            while (slicesIterator.hasNext()) {
                // scroll all the slices
                slicesIterator.next();
            }

            // I have to group the features to get the ranges.
            try {
                return buildFileGroupOnSlices(file);
            } catch (IOException e) {
                throw new RuntimeException("Exception occurred while populating the fileGroup:", e);
            }
        }

        private FileGroup buildFileGroupOnSlices(File file) throws IOException {
            List<File> supportFiles = null;
            Map<String, Object> metadataMap = computeSlicesMetadata(slices);
            metadataMap.put(Utils.BBOX, new ReferencedEnvelope(reader.getOriginalEnvelope(coverageName)));
            return new FileGroup(file, supportFiles, metadataMap);
        }

        @SuppressWarnings("unchecked")
        protected Map<String, Object> computeSlicesMetadata(List<CoverageSlice> slices) throws IOException {
            Map<String, Object> metadataMap = null;
            List<DimensionDescriptor> dimensionDescriptors = reader.getDimensionDescriptors(coverageName);
            // extract metadata for the available domains
            if (dimensionDescriptors != null && !dimensionDescriptors.isEmpty()) {
                metadataMap = new HashMap<>();
                // scan dimensions
                for (DimensionDescriptor descriptor : dimensionDescriptors) {
                    String attribute = descriptor.getStartAttribute();
                    String name = descriptor.getName();
                    Comparable max = null;
                    Comparable min = null;
                    Comparable val = null;
                    for (CoverageSlice slice : slices) {
                        val = (Comparable) slice.getOriginator().getAttribute(attribute);
                        if (min == null) {
                            min = val;
                        }
                        if (max == null) {
                            max = val;
                        }
                        int minCheck = min.compareTo(val);
                        int maxCheck = max.compareTo(val);
                        min = minCheck < 0 ? min : val;
                        max = maxCheck > 0 ? max : val;
                    }
                    addMetadaElement(name, min, max, metadataMap);
                }
            }
            return metadataMap;
        }

        /** Add a metadata element to the FileGroup metadata map */
        protected void addMetadaElement(String name, Comparable min, Comparable max, Map<String, Object> metadataMap) {
            if (Utils.TIME_DOMAIN.equalsIgnoreCase(name) || min instanceof Date) {
                metadataMap.put(name.toUpperCase(), new DateRange((Date) min, (Date) max));
            } else if (Utils.ELEVATION_DOMAIN.equalsIgnoreCase(name) || min instanceof Number) {
                metadataMap.put(
                        name.toUpperCase(),
                        NumberRange.create(((Number) min).doubleValue(), true, ((Number) max).doubleValue(), true));
            } else {
                metadataMap.put(name, new Range<>(String.class, (String) min, (String) max));
            }
        }

        @Override
        public void close() throws IOException {
            // Does nothing... the underlying iterator
            // is made on top of a List
        }
    }

    private NetCDFReader reader;
    private String coverageName;

    /** The underlying slices catalog. Needed to retrieve granules location */
    CoverageSlicesCatalog slicesCatalog;

    private URL sourceURL;

    /** ImageMosaicFileGroupProvider constructor */
    public NetCDFFileResourceInfo(
            NetCDFReader reader, String coverageName, CoverageSlicesCatalog slicesCatalog, URL sourceURL) {
        this.reader = reader;
        this.slicesCatalog = slicesCatalog;
        this.coverageName = coverageName;
        this.sourceURL = sourceURL;
    }

    @Override
    public CloseableIterator<FileGroup> getFiles(Query query) {
        // normally the different type names are actually sharing the same files, but we cannot be
        // sure, a manually setup mosaic could indeed have multiple types with different files in
        // them...
        List<CoverageSlice> fc = null;
        try {
            // WrappedCoverageSlicesCatalog share the DB. Therefore I have to deal with
            // the location attribute
            boolean sharedCatalog = slicesCatalog instanceof WrappedCoverageSlicesCatalog;
            Query updatedQuery = query != null && sharedCatalog ? query : new Query();

            if (sharedCatalog) {
                final List<SortBy> clauses = new ArrayList<>(1);
                clauses.add(new SortByImpl(
                        FeatureUtilities.DEFAULT_FILTER_FACTORY.property(CoverageSlice.Attributes.LOCATION),
                        SortOrder.ASCENDING));
                final SortBy[] sb = clauses.toArray(new SortBy[] {});
                final boolean isSortBySupported =
                        slicesCatalog.getQueryCapabilities(coverageName).supportsSorting(sb);
                if (isSortBySupported) {
                    updatedQuery.setSortBy(sb);
                }
            }

            updatedQuery.setTypeName(coverageName);

            // TODO: Make sure to add different iterator for stores
            // not supporting sortBy

            // Get all the features matching the query
            fc = slicesCatalog.getGranules(updatedQuery);

            // They are already an in memory list
            return sharedCatalog
                    ? new WrappedCoverageSlicesToFileGroupIterator(fc)
                    : new SimpleCoverageSlicesToFileGroupIterator(fc);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
