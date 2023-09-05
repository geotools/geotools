/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * This class simply builds an SRTREE spatial index in memory for fast indexed geometric queries.
 *
 * <p>Since the {@link ImageMosaicReader} heavily uses spatial queries to find out which are the
 * involved tiles during mosaic creation, it is better to do some caching and keep the index in
 * memory as much as possible, hence we came up with this index.
 *
 * @author Simone Giannecchini, S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties URLs
 * @since 2.5
 */
abstract class AbstractGTDataStoreGranuleCatalog extends GranuleCatalog {

    /**
     * When true, the stack trace that created a store that wasn't closed is recorded and then
     * printed out when warning the user about this.
     */
    protected static final Boolean TRACE_ENABLED =
            "true".equalsIgnoreCase(System.getProperty("gt2.mosaic.index.trace"));

    /** Logger. */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(AbstractGTDataStoreGranuleCatalog.class);

    static final FilterFactory ff =
            CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
    protected final String parentLocation;

    private Throwable tracer;

    boolean wrapstore = false;

    protected Properties params;

    private DataStoreFactorySpi spi;

    public AbstractGTDataStoreGranuleCatalog(
            final Properties params,
            final CatalogConfigurationBeans configurations,
            final boolean create,
            final DataStoreFactorySpi spi,
            final Hints hints) {
        super(hints, configurations);
        Utilities.ensureNonNull("params", params);
        this.spi = spi;
        this.params = params;

        try {
            this.parentLocation = (String) params.get(Utils.Prop.PARENT_LOCATION);
            // assumes this one does not change by configuration
            this.wrapstore = configurations.first().isWrapStore();
            initTileIndexStore(params, create, spi);
        } catch (Throwable e) {
            handleInitializationException(e);
            throw new IllegalArgumentException(e);
        }
        if (TRACE_ENABLED) {
            this.tracer = new Throwable();
            this.tracer.fillInStackTrace();
        }
    }

    protected void initializeTypeNames(final Properties params) throws IOException {
        String typeName = null;
        if (params.containsKey(Utils.Prop.TYPENAME)) {
            typeName = (String) params.get(Utils.Prop.TYPENAME);
        }

        if (typeName != null) {
            checkMosaicSchema(typeName);
            addTypeName(typeName, false);
        } else if (!configurations.getTypeNames().isEmpty()) {
            for (String tn : configurations.getTypeNames()) {
                if (isValidMosaicSchema(tn)) getValidTypeNames().add(tn);
            }
        } else {
            // pick the first suitable type name
            String[] typeNames = getTileIndexStore().getTypeNames();
            if (typeNames != null) {
                for (String tn : typeNames) {
                    if (isValidMosaicSchema(tn)) {
                        addTypeName(tn, false);
                        break;
                    }
                }
            }
        }

        // if we got here and there is not typename in the list, we could not find one
        if (this.getValidTypeNames().size() == 0) {
            throw new IllegalArgumentException(
                    "Could not find a suitable mosaic type in the store");
        }

        if (this.getValidTypeNames().size() > 0) {
            // pick the first valid schema found
            for (String tn : getValidTypeNames()) {
                if (isValidMosaicSchema(tn)) {
                    extractBasicProperties(tn);
                    break;
                }
            }
        } else if (typeName != null && typeName.contains(",")) {
            String[] typeNames = typeName.split(",");
            for (String tn : typeNames) {
                extractBasicProperties(tn);
            }
        } else if (typeName != null) {
            extractBasicProperties(typeName);
        } else {
            extractBasicProperties(typeName);
        }
    }

    /** Called in case the initialization of the class failed, allows subclasses to clean up */
    protected abstract void handleInitializationException(Throwable t);

    /** Allows initialization of the tile index store before scanning type names. */
    protected abstract void initTileIndexStore(
            final Properties params, final boolean create, final DataStoreFactorySpi spi)
            throws IOException, MalformedURLException;

    /**
     * Returns true if the type is usable as a mosaic index, that is, it has a geometry and the
     * expected location property
     */
    private boolean isValidMosaicSchema(String typeName) throws IOException {
        SimpleFeatureType schema = getTileIndexStore().getSchema(typeName);

        return Utils.isValidMosaicSchema(schema, getLocationAttributeName(typeName));
    }

    private String getLocationAttributeName(String typeName) {
        return Optional.ofNullable(configurations.getByTypeName(typeName))
                .map(c -> c.getLocationAttribute())
                .orElse(Utils.DEFAULT_LOCATION_ATTRIBUTE);
    }

    /** Checks the provided schema, and throws an exception if not valid */
    private void checkMosaicSchema(String typeName) throws IOException {
        SimpleFeatureType schema = getTileIndexStore().getSchema(typeName);
        if (schema == null) {
            throw new IllegalArgumentException("Could not find typename " + schema);
        } else {
            checkMosaicSchema(schema);
        }
    }

    /** Checks the provided schema, and throws an exception if not valid */
    private void checkMosaicSchema(SimpleFeatureType schema) {
        String locationAttribute = getLocationAttributeName(schema.getTypeName());
        if (!Utils.isValidMosaicSchema(schema, locationAttribute)) {
            throw new IllegalArgumentException(
                    "Invalid mosaic schema "
                            + schema
                            + ", "
                            + "it should have a geometry and a location property of name "
                            + locationAttribute);
        }
    }

    /**
     * If the underlying store has been disposed we throw an {@link IllegalStateException}.
     *
     * <p>We need to arrive here with at least a read lock!
     *
     * @throws IllegalStateException in case the underlying store has been disposed.
     */
    private void checkStore() throws IllegalStateException {
        if (getTileIndexStore() == null) {
            throw new IllegalStateException("The index store has been disposed already.");
        }
    }

    private void extractBasicProperties(String typeName) throws IOException {

        if (typeName == null) {
            final String[] typeNames = getTileIndexStore().getTypeNames();
            if (typeNames == null || typeNames.length <= 0)
                throw new IllegalArgumentException(
                        "BBOXFilterExtractor::extractBasicProperties(): Problems when opening the index,"
                                + " no typenames for the schema are defined");

            if (typeName == null) {
                typeName = typeNames[0];
                addTypeName(typeName, false);
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.warning(
                            "BBOXFilterExtractor::extractBasicProperties(): passed typename is null, using: "
                                    + typeName);
            }

            // loading all the features into memory to build an in-memory index.
            for (String type : typeNames) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(
                            "BBOXFilterExtractor::extractBasicProperties(): Looking for type \'"
                                    + typeName
                                    + "\' in DataStore:getTypeNames(). Testing: \'"
                                    + type
                                    + "\'.");
                if (type.equalsIgnoreCase(typeName)) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine(
                                "BBOXFilterExtractor::extractBasicProperties(): SUCCESS -> type \'"
                                        + typeName
                                        + "\' is equalsIgnoreCase() to \'"
                                        + type
                                        + "\'.");
                    typeName = type;
                    addTypeName(typeName, false);
                    break;
                }
            }
        }

        final SimpleFeatureSource featureSource = getTileIndexStore().getFeatureSource(typeName);
        if (featureSource == null) {
            throw new IOException(
                    "BBOXFilterExtractor::extractBasicProperties(): unable to get a featureSource for the qualified name"
                            + typeName);
        }

        final FeatureType schema = featureSource.getSchema();
        if (schema != null && schema.getGeometryDescriptor() != null) {
            String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine(
                        "BBOXFilterExtractor::extractBasicProperties(): geometryPropertyName is set to \'"
                                + geometryPropertyName
                                + "\'.");

        } else {
            throw new IOException(
                    "BBOXFilterExtractor::extractBasicProperties(): unable to get a schema from the featureSource");
        }
    }

    @Override
    public void dispose() {
        try {
            if (multiScaleROIProvider != null) {
                multiScaleROIProvider.dispose();
            }
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        } finally {
            multiScaleROIProvider = null;
            disposeTileIndexStore();
        }
    }

    /** Allows subclasses to dispose the tile index store */
    protected abstract void disposeTileIndexStore();

    @Override
    @SuppressWarnings("deprecation")
    public int removeGranules(Query query) {
        return removeGranules(query, null);
    }

    @Override
    @SuppressWarnings("PMD.UseTryWithResources") // transaction is not necessarily created here
    public int removeGranules(Query query, Transaction transaction) {
        Utilities.ensureNonNull("query", query);
        query = mergeHints(query);
        // check if the index has been cleared
        checkStore();
        String typeName = query.getTypeName();
        SimpleFeatureStore fs = null;
        try {
            // create a writer that appends this features
            fs = (SimpleFeatureStore) getTileIndexStore().getFeatureSource(typeName);
            boolean rollback = true;
            Transaction t =
                    transaction == null || transaction == Transaction.AUTO_COMMIT
                            ? new DefaultTransaction()
                            : transaction;
            try {
                fs.setTransaction(t);
                final int retVal = fs.getFeatures(query).size(); // ensures we get a value
                if (retVal > 0) {
                    fs.removeFeatures(query.getFilter());
                    t.commit();
                }
                rollback = false;

                return retVal;
            } finally {
                // rollback/close only if the transaction was created locally, otherwise leave
                // the caller providing the transaction in control
                if (t != transaction) {
                    if (rollback) {
                        t.rollback();
                    }
                    t.close();
                }
            }

        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return -1;
        }
    }

    @Override
    public void addGranules(
            final String typeName,
            final Collection<SimpleFeature> granules,
            final Transaction transaction)
            throws IOException {
        Utilities.ensureNonNull("granuleMetadata", granules);
        // check if the index has been cleared
        checkStore();

        SimpleFeatureStore store =
                (SimpleFeatureStore) getTileIndexStore().getFeatureSource(typeName);
        store.setTransaction(transaction);

        ListFeatureCollection featureCollection =
                new ListFeatureCollection(getTileIndexStore().getSchema(typeName));

        // add them all
        Set<FeatureId> fids = new HashSet<>();
        for (SimpleFeature f : granules) {
            // Add the feature to the feature collection
            featureCollection.add(f);
            fids.add(ff.featureId(f.getID()));
        }
        store.addFeatures(featureCollection);
        store.setTransaction(null);
    }

    @Override
    public void getGranuleDescriptors(Query query, final GranuleCatalogVisitor visitor)
            throws IOException {
        Utilities.ensureNonNull("query", query);
        final Query q = mergeHints(query);
        String typeName = q.getTypeName();
        checkStore();

        //
        // Load tiles informations, especially the bounds, which will be
        // reused
        //
        final SimpleFeatureSource featureSource = getTileIndexStore().getFeatureSource(typeName);
        if (featureSource == null) {
            throw new NullPointerException(
                    "The provided SimpleFeatureSource is null, it's impossible to create an index!");
        }

        final SimpleFeatureCollection features = featureSource.getFeatures(q);
        if (features == null)
            throw new NullPointerException(
                    "The provided SimpleFeatureCollection is null, it's impossible to create an index!");

        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Index Loaded");

        // visiting the features from the underlying store, caring for early bail out
        CatalogConfigurationBean configuration = configurations.getByTypeQuery(query);
        try (SimpleFeatureIterator fi = features.features()) {
            while (fi.hasNext() && !visitor.isVisitComplete()) {
                final SimpleFeature sf = fi.next();
                MultiLevelROI footprint = getGranuleFootprint(sf);
                if (footprint == null || !footprint.isEmpty()) {
                    try {
                        final GranuleDescriptor granule =
                                new GranuleDescriptor(
                                        sf,
                                        configuration.suggestedFormat(),
                                        configuration.suggestedSPI(),
                                        configuration.suggestedIsSPI(),
                                        configuration.getPathType(),
                                        configuration.getLocationAttribute(),
                                        parentLocation,
                                        footprint,
                                        configuration.isHeterogeneous(),
                                        q.getHints());

                        visitor.visit(granule, sf);
                    } catch (Exception e) {
                        LOGGER.log(Level.FINE, "Skipping invalid granule", e);
                    }
                }
            }
        }
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return getGranules(q, Transaction.AUTO_COMMIT);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q, Transaction t) throws IOException {
        Utilities.ensureNonNull("query", q);
        q = mergeHints(q);
        String typeName = q.getTypeName();
        checkStore();

        //
        // Load tiles informations, especially the bounds, which will be
        // reused
        //
        final SimpleFeatureSource featureSource = getTileIndexStore().getFeatureSource(typeName);
        if (featureSource == null) {
            throw new NullPointerException(
                    "The provided SimpleFeatureSource is null, it's impossible to create an index!");
        }
        if (t != null && t != Transaction.AUTO_COMMIT) {
            if (featureSource instanceof SimpleFeatureStore) {
                ((SimpleFeatureStore) featureSource).setTransaction(t);
            } else {
                throw new IllegalArgumentException(
                        "A transaction has been specified, but the delegate tile index store is not writable");
            }
        }
        return featureSource.getFeatures(q);
    }

    @Override
    public BoundingBox getBounds(final String typeName) {
        try {
            checkStore();
            return this.getTileIndexStore().getFeatureSource(typeName).getBounds();
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }

        return null;
    }

    @Override
    public BoundingBox getBounds(String typeName, Transaction t) {
        try {
            checkStore();
            SimpleFeatureSource fs = this.getTileIndexStore().getFeatureSource(typeName);
            if (t != null && t != Transaction.AUTO_COMMIT) {
                if (fs instanceof SimpleFeatureStore) {
                    ((SimpleFeatureStore) fs).setTransaction(t);
                } else {
                    throw new IllegalArgumentException(
                            "A transaction has been specified, but the delegate tile index store is not writable");
                }
            }
            return fs.getBounds();
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }

        return null;
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("typeSpec", typeSpec);
        checkStore();

        final SimpleFeatureType featureType =
                DataUtilities.createType(namespace, typeName, typeSpec);
        checkMosaicSchema(featureType);
        getTileIndexStore().createSchema(featureType);
        String type = featureType.getTypeName();
        if (typeName != null) {
            addTypeName(typeName, true);
        }
        extractBasicProperties(type);
    }

    private void addTypeName(String typeName, final boolean check) {
        if (check && this.getValidTypeNames().contains(typeName)) {
            throw new IllegalArgumentException("This typeName already exists: " + typeName);
        }
        this.getValidTypeNames().add(typeName);
    }

    private void removeTypeName(String typeName) {
        if (this.getValidTypeNames().contains(typeName)) {
            getValidTypeNames().remove(typeName);
        }
    }

    @Override
    public String[] getTypeNames() {
        Set<String> validTypeNames = getValidTypeNames();
        if (validTypeNames != null && !validTypeNames.isEmpty()) {
            return validTypeNames.toArray(new String[] {});
        }
        return null;
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        Utilities.ensureNonNull("featureType", featureType);
        checkMosaicSchema(featureType);
        checkStore();

        getTileIndexStore().createSchema(featureType);
        String typeName = featureType.getTypeName();
        if (typeName != null) {
            addTypeName(typeName, true);
        }
        extractBasicProperties(typeName);
    }

    @Override
    public void removeType(String typeName) throws IOException {
        Utilities.ensureNonNull("featureType", typeName);
        checkStore();

        getTileIndexStore().removeSchema(typeName);
        removeTypeName(typeName);
    }

    @Override
    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        Utilities.ensureNonNull("typeSpec", typeSpec);
        Utilities.ensureNonNull("identification", identification);
        checkStore();
        final SimpleFeatureType featureType = DataUtilities.createType(identification, typeSpec);
        checkMosaicSchema(featureType);
        getTileIndexStore().createSchema(featureType);
        String typeName = featureType.getTypeName();
        if (typeName != null) {
            addTypeName(typeName, true);
        }
        extractBasicProperties(typeName);
    }

    @Override
    public SimpleFeatureType getType(String typeName) throws IOException {
        checkStore();

        if (this.getValidTypeNames().isEmpty() || !this.getValidTypeNames().contains(typeName)) {
            return null;
        }
        return getTileIndexStore().getSchema(typeName);
    }

    @Override
    public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
        query = mergeHints(query);
        checkStore();
        SimpleFeatureSource fs = getTileIndexStore().getFeatureSource(query.getTypeName());

        if (fs instanceof ContentFeatureSource)
            ((ContentFeatureSource) fs).accepts(query, function, null);
        else {
            final SimpleFeatureCollection collection = fs.getFeatures(query);
            collection.accepts(function, null);
        }
    }

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        try {
            checkStore();
            return getTileIndexStore().getFeatureSource(typeName).getQueryCapabilities();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Unable to collect QueryCapabilities", e);
            return null;
        }
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        super.finalize();

        // warn people
        if (this.getTileIndexStore() != null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "This granule catalog was not properly dispose as it still points to:"
                                + getTileIndexStore().getInfo().toString());
                if (TRACE_ENABLED) {
                    LOGGER.log(
                            Level.WARNING,
                            "The un-disposed granule catalog originated on this stack trace",
                            tracer);
                }
            }
            // try to dispose the underlying store if it has not been disposed yet
            this.dispose();
        }
    }

    @Override
    public int getGranulesCount(Query q) throws IOException {
        Utilities.ensureNonNull("query", q);
        q = mergeHints(q);
        String typeName = q.getTypeName();
        checkStore();

        //
        // Load tiles informations, especially the bounds, which will be
        // reused
        //
        final SimpleFeatureSource featureSource = getTileIndexStore().getFeatureSource(typeName);
        if (featureSource == null) {
            throw new NullPointerException(
                    "The provided SimpleFeatureSource is null, it's impossible to create an index!");
        }
        int count = featureSource.getCount(q);
        if (count == -1) {
            return featureSource.getFeatures(q).size();
        }
        return count;
    }

    @Override
    public void drop() throws IOException {
        // drop a datastore. Right now, only postGIS drop is supported
        final Map<?, ?> params = Utils.filterDataStoreParams(this.params, spi);

        // Use reflection to invoke dropDatabase on postGis factory DB
        final Method[] methods = spi.getClass().getMethods();
        boolean dropped = false;
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("dropDatabase")) {
                try {
                    method.invoke(spi, params);
                } catch (Exception e) {
                    throw new IOException("Unable to drop the database: ", e);
                }
                dropped = true;
                break;
            }
        }
        if (!dropped) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to drop catalog for SPI " + spi.getDisplayName());
            }
        }
    }

    /** Returns the tile index store */
    protected abstract DataStore getTileIndexStore();

    /**
     * Returns the set of valid type names (this is going to be a live collection, the code is
     * allowed to modify it)
     */
    protected abstract Set<String> getValidTypeNames();

    @Override
    protected CatalogConfigurationBeans getConfigurations() {
        return configurations;
    }

    @Override
    protected String getParentLocation() {
        return parentLocation;
    }
}
