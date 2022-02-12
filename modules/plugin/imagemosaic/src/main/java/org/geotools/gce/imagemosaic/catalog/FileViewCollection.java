/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.data.FileGroupProvider;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureIterator;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.RasterManager;
import org.geotools.gce.imagemosaic.SupportFilesCollector;
import org.geotools.util.URLs;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

class FileViewCollection extends DecoratingSimpleFeatureCollection {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    public static final String IMAGEINDEX = "imageindex";

    private final PathType pathType;
    private final String parentLocation;
    private final String locationAttribute;
    private final SimpleFeatureType schema;
    private final SimpleFeatureBuilder featureBuilder;

    protected FileViewCollection(GranuleCatalog catalog, Query query, RasterManager manager)
            throws IOException {
        super(getDelegateCollection(catalog, query, manager));
        pathType = manager.getPathType();
        parentLocation = manager.getParentLocation();
        locationAttribute = manager.getLocationAttribute();

        // prepare the builder with the reduced schema (no location)
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(delegate.getSchema());
        tb.remove(locationAttribute);
        if (delegate.getSchema().getAttributeDescriptors().stream()
                .anyMatch(d -> IMAGEINDEX.equals(d.getLocalName()))) {
            // used by multidim files, in output we return info only about first slice
            tb.remove(IMAGEINDEX);
        }
        // slice
        this.schema = tb.buildFeatureType();
        this.featureBuilder = new SimpleFeatureBuilder(schema);
    }

    private static SimpleFeatureCollection getDelegateCollection(
            GranuleCatalog catalog, Query query, RasterManager manager) throws IOException {
        // the query needs to be sorted on location, for aggregation purposes, and
        // by dimensions, just to avoid randomness in output (we pick the first granule in the list
        // as the sample)
        List<SortBy> sorts = new ArrayList<>();

        // add the sort on location
        String locationAttribute = manager.getLocationAttribute();
        sorts.add(FF.sort(locationAttribute, SortOrder.ASCENDING));
        // sort on anything else
        manager.getDimensionDescriptors().stream()
                .map(dd -> FF.sort(dd.getStartAttribute(), SortOrder.ASCENDING))
                .forEach(sorts::add);

        if (query.getSortBy() != null && query.getSortBy().length > 0) {
            sorts.addAll(Arrays.asList(query.getSortBy()));
        }

        Query updated = new Query(query);
        updated.setSortBy(sorts.toArray(new SortBy[sorts.size()]));

        return catalog.getGranules(updated);
    }

    @Override
    public SimpleFeatureType getSchema() {
        return schema;
    }

    @Override
    public SimpleFeatureIterator features() {
        PushbackFeatureIterator pit = new PushbackFeatureIterator(delegate.features());
        return new DecoratingSimpleFeatureIterator(pit) {

            @Override
            public SimpleFeature next() throws NoSuchElementException {
                if (delegate == null || !hasNext()) throw new NoSuchElementException();
                SimpleFeature sample = pit.next();
                // eat away all features with the same location
                String location = (String) sample.getAttribute(locationAttribute);
                while (pit.hasNext()) {
                    SimpleFeature next = pit.next();
                    String nextLocation = (String) next.getAttribute(locationAttribute);
                    if (!location.equals(nextLocation)) {
                        pit.pushBack();
                        break;
                    }
                }
                return remapFeature(sample);
            }
        };
    }

    private SimpleFeature remapFeature(SimpleFeature feature) {
        String location = (String) feature.getAttribute(locationAttribute);
        URL path = pathType.resolvePath(parentLocation, location);

        schema.getAttributeDescriptors().stream()
                .filter(d -> !location.equals(d.getLocalName()))
                .forEach(
                        d ->
                                featureBuilder.set(
                                        d.getLocalName(), feature.getAttribute(d.getLocalName())));
        File mainFile = URLs.urlToFile(path);
        if (mainFile != null) {
            SupportFilesCollector collector = SupportFilesCollector.getCollectorFor(mainFile);
            List<File> supportFiles = Collections.emptyList();
            if (collector != null) {
                supportFiles = collector.getSupportFiles(mainFile.getPath());
            }
            featureBuilder.featureUserData(
                    GranuleSource.FILES,
                    new FileGroupProvider.FileGroup(
                            mainFile, supportFiles, Collections.emptyMap()));
        } else {
            featureBuilder.featureUserData(GranuleSource.FILES, path);
        }
        return featureBuilder.buildFeature(feature.getID());
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        return new FilteringSimpleFeatureCollection(this, filter);
    }
}
