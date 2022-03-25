/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.gce.imagemosaic.Utils.FF;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GranuleRemovalPolicy;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.data.CloseableIterator;
import org.geotools.data.FileGroupProvider;
import org.geotools.data.FileServiceInfo;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.filter.Filters;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;

/**
 * {@link GranuleStore} that purges a file data/metadata only if no coverage still refers to it (by
 * checking all coverages in the mosaic)
 */
class PurgingGranuleStore extends GranuleStoreDecorator {

    static final Logger LOGGER = Logging.getLogger(PurgingGranuleStore.class);

    private final RasterManager manager;

    public PurgingGranuleStore(GranuleStore delegate, RasterManager manager) {
        super(delegate);
        this.manager = manager;
    }

    @Override
    public int removeGranules(Filter filter, Hints hints) {
        GranuleRemovalPolicy policy =
                Optional.ofNullable(hints)
                        .map(h -> (GranuleRemovalPolicy) h.get(Hints.GRANULE_REMOVAL_POLICY))
                        .orElse(GranuleRemovalPolicy.NONE);

        int removed = 0;
        try {
            if (policy == GranuleRemovalPolicy.NONE) {
                // easy case, just remove the granules from the catalog
                removed = delegate.removeGranules(filter, hints);
            } else {
                // harder case, we need to remove metadata and eventually data, but only
                // if there are no more references to the files in the mosaic

                // collect affected granules
                Map<String, Integer> countsByFilter = countGranulesMatching(filter, manager);
                if (countsByFilter.isEmpty()) {
                    return 0;
                }
                Map<String, Integer> countsByLocation =
                        countGranulesMatchingLocations(countsByFilter.keySet());
                Set<String> removedLocations =
                        getRemovedLocations(countsByFilter, countsByLocation);

                // apply the purge policy as needed
                boolean deleteData = policy == GranuleRemovalPolicy.ALL;
                Filter removedLocationsFilter = buildLocationsFilter(manager, removedLocations);
                manager.getGranuleCatalog()
                        .getGranuleDescriptors(
                                new Query(manager.getTypeName(), removedLocationsFilter),
                                new FileRemovingGranuleVisitor(deleteData));

                // do the removal inside the catalog
                removed = delegate.removeGranules(filter);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to perform cleanup", e);
        }

        return removed;
    }

    private Set<String> getRemovedLocations(
            Map<String, Integer> countsByFilter, Map<String, Integer> countsByLocation) {
        Set<String> result = new HashSet<>();
        for (String location : countsByFilter.keySet()) {
            int matched = countsByFilter.get(location);
            int total = countsByLocation.get(location);
            if (matched >= total) {
                result.add(location);
            }
        }

        return result;
    }

    private Filter buildLocationsFilter(RasterManager manager, Set<String> locations) {
        PropertyName locationProperty = getLocationProperty(manager);
        List<Filter> filters =
                locations.stream()
                        .map(l -> FF.equal(locationProperty, FF.literal(l), false))
                        .collect(Collectors.toList());
        return Filters.or(FF, filters);
    }

    /**
     * Locates the granules matching the provided filter, and returns a map going from locations to
     * granule count for such location
     */
    private Map<String, Integer> countGranulesMatching(Filter filter, RasterManager manager)
            throws IOException {
        CalcResult result = countGranulesMatchingCalc(filter, manager);
        return calcToCountMap(result);
    }

    /**
     * Counts granule usages in all coverages managed by the reader, as the same file can act as a
     * source for multiple coverages
     */
    private Map<String, Integer> countGranulesMatchingLocations(Set<String> locations)
            throws IOException {
        ImageMosaicReader reader = manager.getParentReader();
        CalcResult calc = null;
        for (String coverageName : reader.getGridCoverageNames()) {
            RasterManager coverageManager = reader.getRasterManager(coverageName);
            Filter filter = buildLocationsFilter(coverageManager, locations);
            CalcResult coverageCalc = countGranulesMatchingCalc(filter, coverageManager);
            if (calc == null) {
                calc = coverageCalc;
            } else {
                calc = calc.merge(coverageCalc);
            }
        }
        return calcToCountMap(calc);
    }

    private Map<String, Integer> calcToCountMap(CalcResult result) {
        // the result is a map going from list of grouping attributes to value
        @SuppressWarnings("unchecked")
        Map<List<String>, Integer> map = result.toMap();
        return map.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().get(0), x -> x.getValue()));
    }

    private CalcResult countGranulesMatchingCalc(Filter filter, RasterManager manager)
            throws IOException {
        Query q = new Query(manager.getTypeName());
        q.setFilter(filter);
        SimpleFeatureCollection lc = manager.getGranuleCatalog().getGranules(q);
        List<Expression> groupByExpressions = Arrays.asList(getLocationProperty(manager));
        GroupByVisitor groupVisitor =
                new GroupByVisitor(Aggregate.COUNT, NilExpression.NIL, groupByExpressions, null);
        lc.accepts(groupVisitor, null);
        return groupVisitor.getResult();
    }

    private PropertyName getLocationProperty(RasterManager manager) {
        CatalogConfigurationBean configuration =
                manager.getConfiguration().getCatalogConfigurationBean();
        String locationAttribute =
                Optional.ofNullable(configuration.getLocationAttribute())
                        .orElse(Utils.DEFAULT_LOCATION_ATTRIBUTE);

        return FF.property(locationAttribute);
    }

    private class FileRemovingGranuleVisitor implements GranuleCatalogVisitor {
        private final boolean mDeleteData;

        public FileRemovingGranuleVisitor(boolean deleteData) {
            mDeleteData = deleteData;
        }

        @Override
        public void visit(GranuleDescriptor granule, SimpleFeature feature) {
            AbstractGridCoverage2DReader reader = null;
            try {
                reader = granule.getReader();
                File granuleFile = URLs.urlToFile(granule.getGranuleUrl());
                // check common sidecars not handled by the readers
                if (granuleFile != null) {
                    // check the common sidecars that are not format specific too
                    // ... overviews (it preserves the original full name and adds // .ovr)
                    removeFile(
                            new File(granuleFile.getParentFile(), granuleFile.getName() + ".ovr"));
                    // ... footprints
                    List<File> footprintFiles =
                            manager.getGranuleCatalog().getFootprintFiles(feature);
                    footprintFiles.forEach(this::removeFile);
                }
                // now to to ther real "content"
                if (reader instanceof StructuredGridCoverage2DReader) {
                    // only structured have a way to remove data and metadata
                    ((StructuredGridCoverage2DReader) reader).delete(mDeleteData);
                }
                if (mDeleteData) {
                    List<File> filesToRemove = new ArrayList<>();
                    // if we are removing data, get to the list of files and remove
                    ServiceInfo info = reader.getInfo();
                    // see if the reader can provide a full list of us
                    if (info instanceof FileServiceInfo) {
                        try (CloseableIterator<FileGroupProvider.FileGroup> it =
                                ((FileServiceInfo) info).getFiles(Query.ALL)) {
                            filesToRemove.addAll(getFilesToRemove(it));
                        }
                    }
                    // last chance, check if the granule URL is a file and remove it
                    // (might well be a S3 link, in which case we don't have supporting
                    // logic to actually do the removal yet)
                    if (granuleFile != null) {
                        // main file
                        filesToRemove.add(granuleFile);
                    }
                    // now close the reader or deletion won't work on Windows
                    reader.dispose();
                    for (File file : filesToRemove) {
                        removeFile(file);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(
                        Level.WARNING,
                        "Failed to perform cleanup for granule " + granule.getGranuleUrl(),
                        e);
            } finally {
                if (reader != null) {
                    reader.dispose();
                }
            }
        }

        private void removeFile(File file) {
            if (file.exists()) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    LOGGER.warning("Could not remove source file " + file + ": " + e.getMessage());
                }
            }
        }

        private List<File> getFilesToRemove(CloseableIterator<FileGroupProvider.FileGroup> it) {
            List<File> filesToRemove = new ArrayList<>();
            while (it.hasNext()) {
                FileGroupProvider.FileGroup group = it.next();
                filesToRemove.add(group.getMainFile());
                List<File> supportFiles = group.getSupportFiles();
                if (supportFiles != null) {
                    filesToRemove.addAll(supportFiles);
                }
            }
            return filesToRemove;
        }
    }
}
