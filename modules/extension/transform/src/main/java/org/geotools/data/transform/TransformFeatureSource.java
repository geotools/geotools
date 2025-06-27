/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * A feature source that can transform a source feature source using a set of expressions
 *
 * <p>Usages:
 *
 * <ul>
 *   <li>hide, rename fields - compute new fields
 *   <li>build geom from x,y (we need to add a new Point filter function and have a special treatment of it in
 *       simplifying filter visitor so that it turns bbox filters against it into a filter on x,y)
 *   <li>on the fly simplification for WFS (just use environment variables) and in general dynamic processing based on
 *       params without stored queries
 * </ul>
 *
 * @author Andrea Aime - GeoSolutions
 */
public class TransformFeatureSource implements SimpleFeatureSource {

    protected static final Logger LOGGER = Logging.getLogger(TransformFeatureSource.class);

    protected static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    protected Transformer transformer;

    protected SimpleFeatureSource source;

    /**
     * Creates a transformed feature source from the original source, giving it a certain name and a set of computed
     * properties
     */
    public TransformFeatureSource(SimpleFeatureSource source, Name name, List<Definition> definitions)
            throws IOException {
        this.transformer = new Transformer(source, name, definitions, null);
        this.source = source;

        LOGGER.log(Level.FINE, "Transformed target schema for this feature source is {0}", transformer.getSchema());
    }

    @Override
    public Name getName() {
        return transformer.getName();
    }

    @Override
    public ResourceInfo getInfo() {
        return new DefaultResourceInfo(this);
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return new SingleFeatureSourceDataStore(this);
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return new QueryCapabilities() {

            @Override
            public boolean isOffsetSupported() {
                return true;
            }

            @Override
            public boolean isReliableFIDSupported() {
                return source.getQueryCapabilities().isReliableFIDSupported();
            }

            @Override
            public boolean isJoiningSupported() {
                return false;
            }

            @Override
            public boolean isUseProvidedFIDSupported() {
                return source.getQueryCapabilities().isUseProvidedFIDSupported();
            }

            @Override
            public boolean isVersionSupported() {
                return source.getQueryCapabilities().isVersionSupported();
            }

            @Override
            public boolean supportsSorting(SortBy... sortAttributes) {
                // we use external sorting if any of the sort attributes is computed
                for (SortBy sortBy : sortAttributes) {
                    if (sortBy == SortBy.NATURAL_ORDER || sortBy == SortBy.REVERSE_ORDER) {
                        // fine, we support this
                        continue;
                    } else {
                        String pn = sortBy.getPropertyName().getPropertyName();
                        AttributeDescriptor descriptor = transformer.getSchema().getDescriptor(pn);
                        if (descriptor == null) {
                            return false;
                        } else {
                            Class<?> binding = descriptor.getType().getBinding();
                            if (!Comparable.class.isAssignableFrom(binding)) {
                                if (LOGGER.isLoggable(Level.FINE)) {
                                    LOGGER.log(
                                            Level.FINE,
                                            "Can't sort on {0} because its property type {1} is not comparable",
                                            new Object[] {descriptor.getLocalName(), binding});
                                }
                                return false;
                            }
                        }
                    }
                }

                return true;
            }
        };
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException("We don't support feature listeners at the moment");
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException("We don't support feature listeners at the moment");
    }

    @Override
    public SimpleFeatureType getSchema() {
        return transformer.getSchema();
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        // get the geom properties, if any is really computed we return null,
        // otherwise if they are just renamed we pass down the bounds request
        List<String> geometryNames = transformer.getGeometryPropertyNames();
        List<String> filtered = getSelectedAttributes(geometryNames, query);

        // if no geometry, no bounds
        if (filtered.isEmpty()) {
            LOGGER.log(Level.FINE, "No geometry properties in query {0}", query);
            return null;
        }

        // collect the original attribute names
        List<String> originalNames = transformer.getOriginalNames(filtered);
        if (originalNames.size() < filtered.size()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Some of the geometry attributes is the result of a general transformation "
                                + "(not a rename), can't compute the bbox quickly");
            }

            return null;
        }

        // re-shape the query
        Query txQuery = transformer.transformQuery(query);
        txQuery.setPropertyNames(originalNames);

        // let the world know
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                    Level.FINE,
                    "The original query for bounds computation{0} has been tranformed to {1}",
                    new Object[] {query, txQuery});
        }

        return source.getBounds(txQuery);
    }

    /** Returns the set of names actually selected by the query */
    private List<String> getSelectedAttributes(List<String> attributeNames, Query query) {
        if (query.getPropertyNames() == null) {
            return attributeNames;
        }
        List<String> pnames = Arrays.asList(query.getPropertyNames());
        List<String> result = new ArrayList<>();
        for (String an : attributeNames) {
            if (pnames.contains(an)) {
                result.add(an);
            }
        }

        return result;
    }

    @Override
    public int getCount(Query query) throws IOException {
        // transforming does not change count, but we have to transform the filter
        Query txQuery = transformer.transformQuery(query);
        txQuery.setPropertyNames(Query.ALL_NAMES);

        // let the world know
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                    Level.FINE,
                    "The original query for count computation{0} has been transformed to {1}",
                    new Object[] {query, txQuery});
        }

        return source.getCount(txQuery);
    }

    @Override
    public Set<Key> getSupportedHints() {
        // set up hints
        Set<Key> hints = new HashSet<>();
        hints.addAll(source.getSupportedHints());
        hints.add(Hints.FEATURE_DETACHED);

        return hints;
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Query.ALL);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new Query(transformer.getSchema().getTypeName(), filter));
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return new TransformFeatureCollection(this, transformer, query);
    }
}
