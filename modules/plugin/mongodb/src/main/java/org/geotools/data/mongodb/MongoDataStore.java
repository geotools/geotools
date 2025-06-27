/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.mongodb.complex.JsonSelectAllFunction;
import org.geotools.data.mongodb.complex.JsonSelectFunction;
import org.geotools.data.mongodb.data.SchemaStoreDirectoryProvider;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.http.HTTPClient;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;

@SuppressWarnings("deprecation") // DB was replaced by MongoDatabase but API is not the same
public class MongoDataStore extends ContentDataStore {

    private static final Logger LOGGER = Logging.getLogger(MongoDataStore.class);

    static final String KEY_mapping = "mapping";
    static final String KEY_encoding = "encoding";
    static final String KEY_collection = "collection";

    final MongoSchemaStore schemaStore;

    final MongoClient dataStoreClient;
    final DB dataStoreDB;

    final boolean deactivateOrNativeFilter;

    // for reading schema from hosted files
    private HTTPClient httpClient;

    FilterCapabilities filterCapabilities;

    // parameters for precise schema generation from actual mongodb data
    private MongoSchemaInitParams schemaInitParams;

    public MongoDataStore(String dataStoreURI) {
        this(dataStoreURI, null);
    }

    public MongoDataStore(String dataStoreURI, String schemaStoreURI) {
        this(dataStoreURI, schemaStoreURI, true);
    }

    public MongoDataStore(String dataStoreURI, String schemaStoreURI, boolean createDatabaseIfNeeded) {
        this(dataStoreURI, schemaStoreURI, createDatabaseIfNeeded, null, null);
    }

    public MongoDataStore(
            String dataStoreURI, String schemaStoreURI, boolean createDatabaseIfNeeded, HTTPClient httpClient) {
        // helpful for unit tests
        this(dataStoreURI, schemaStoreURI, createDatabaseIfNeeded, null, httpClient);
    }

    public MongoDataStore(
            String dataStoreURI,
            String schemaStoreURI,
            boolean createDatabaseIfNeeded,
            MongoSchemaInitParams schemaInitParams,
            HTTPClient httpClient) {

        MongoClientURI dataStoreClientURI = createMongoClientURI(dataStoreURI);
        dataStoreClient = createMongoClient(dataStoreClientURI);
        dataStoreDB = createDB(dataStoreClient, dataStoreClientURI.getDatabase(), !createDatabaseIfNeeded);

        if (dataStoreDB == null) {
            dataStoreClient.close(); // This smells bad...
            throw new IllegalArgumentException(
                    "Unknown mongodb database, \"" + dataStoreClientURI.getDatabase() + "\"");
        }

        this.deactivateOrNativeFilter = isMongoVersionLessThan2_6(dataStoreClientURI);
        this.httpClient = httpClient;
        schemaStore = createSchemaStore(schemaStoreURI);
        if (schemaStore == null) {
            dataStoreClient.close(); // This smells bad too...
            throw new IllegalArgumentException("Unable to initialize schema store with URI \"" + schemaStoreURI + "\"");
        }

        filterCapabilities = createFilterCapabilties();

        if (schemaInitParams != null) this.schemaInitParams = schemaInitParams;
        else this.schemaInitParams = MongoSchemaInitParams.builder().build();
    }

    /**
     * Checks if MongoDB version is less than 2.6.0.
     *
     * @return true if version less than 2.6.0 is found, otherwise false.
     */
    private boolean isMongoVersionLessThan2_6(MongoClientURI dataStoreClientURI) {
        boolean deactivateOrAux = false;
        // check server version
        if (dataStoreClient != null && dataStoreClientURI != null && dataStoreClientURI.getDatabase() != null) {
            Document result = dataStoreClient
                    .getDatabase(dataStoreClientURI.getDatabase())
                    .runCommand(new BsonDocument("buildinfo", new BsonString("")));
            if (result.containsKey("versionArray")) {
                @SuppressWarnings("unchecked")
                List<Integer> versionArray = (List) result.get("versionArray");
                // if MongoDB server version < 2.6.0 disable native $or operator
                if (versionArray.get(0) < 2 || versionArray.get(0) == 2 && versionArray.get(1) < 6) {
                    deactivateOrAux = true;
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown Mongo Client");
        }
        return deactivateOrAux;
    }

    final MongoClientURI createMongoClientURI(String dataStoreURI) {
        if (dataStoreURI == null) {
            throw new IllegalArgumentException("dataStoreURI may not be null");
        }
        if (!dataStoreURI.startsWith("mongodb://")) {
            throw new IllegalArgumentException(
                    "incorrect scheme for URI, expected to begin with \"mongodb://\", found URI of \""
                            + dataStoreURI
                            + "\"");
        }
        return new MongoClientURI(dataStoreURI.toString());
    }

    final MongoClient createMongoClient(MongoClientURI mongoClientURI) {
        try {
            return new MongoClient(mongoClientURI);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown mongodb host(s)", e);
        }
    }

    final DB createDB(MongoClient mongoClient, String databaseName, boolean databaseMustExist) {
        if (databaseMustExist
                && !StreamSupport.stream(mongoClient.listDatabaseNames().spliterator(), false)
                        .anyMatch(name -> databaseName.equalsIgnoreCase(name))) {
            return null;
        }
        return mongoClient.getDB(databaseName);
    }

    private synchronized MongoSchemaStore createSchemaStore(String schemaStoreURI) {
        if (schemaStoreURI.startsWith("file:")) {
            try {
                return new MongoSchemaFileStore(schemaStoreURI);
            } catch (URISyntaxException | IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \"" + schemaStoreURI + "\"",
                        e);
            }
        } else if (schemaStoreURI.startsWith("mongodb:")) {
            try {
                return new MongoSchemaDBStore(schemaStoreURI);
            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create mongodb-based schema store with URI \"" + schemaStoreURI + "\"",
                        e);
            }
        } else if (schemaStoreURI.startsWith(MongoSchemaFileStore.PRE_FIX_HTTP)) {
            try {

                File downloadedFile = MongoUtil.downloadSchemaFile(
                        dataStoreDB.getName(),
                        new URL(schemaStoreURI),
                        httpClient,
                        SchemaStoreDirectoryProvider.getHighestPriority());
                if (MongoUtil.isZipFile(downloadedFile)) {
                    File extractedFileLocation =
                            MongoUtil.extractZipFile(downloadedFile.getParentFile(), downloadedFile);
                    LOGGER.log(
                            Level.INFO,
                            "Found Schema Files at " + extractedFileLocation.toString() + "after extracting ");
                    return new MongoSchemaFileStore(extractedFileLocation.toURI());
                } else
                    return new MongoSchemaFileStore(
                            downloadedFile.getParentFile().toURI());

            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \"" + schemaStoreURI + "\"",
                        e);
            }
        } else {
            try {
                return new MongoSchemaFileStore("file:" + schemaStoreURI);
            } catch (URISyntaxException | IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \"" + schemaStoreURI + "\"",
                        e);
            }
        }
        LOGGER.log(Level.SEVERE, "Unsupported URI \"{0}\" for schema store", schemaStoreURI);
        return null;
    }

    final FilterCapabilities createFilterCapabilties() {
        FilterCapabilities capabilities = new FilterCapabilities();

        if (deactivateOrNativeFilter) {
            /*
             * disable FilterCapabilities.LOGICAL_OPENGIS since it contains Or.class (in
             * additions to And.class and Not.class. MongodB 2.4 doesn't support '$or' with
             * spatial operations.
             */
            capabilities.addType(And.class);
            capabilities.addType(Not.class);
        } else {
            // default behavior, '$or' is fully supported from MongoDB 2.6.0 version
            capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        }

        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(PropertyIsLike.class);

        capabilities.addType(BBOX.class);
        capabilities.addType(Intersects.class);
        capabilities.addType(Within.class);

        capabilities.addType(Id.class);
        capabilities.addType(JsonSelectFunction.class);
        capabilities.addType(JsonSelectAllFunction.class);

        /*
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(ExcludeFilter.class);

        //temporal filters
        capabilities.addType(After.class);
        capabilities.addType(Before.class);
        capabilities.addType(Begins.class);
        capabilities.addType(BegunBy.class);
        capabilities.addType(During.class);
        capabilities.addType(Ends.class);
        capabilities.addType(EndedBy.class);*/

        return capabilities;
    }

    public FilterCapabilities getFilterCapabilities() {
        return filterCapabilities;
    }

    @Override
    public void createSchema(SimpleFeatureType incoming) throws IOException {

        final String geometryMapping = "geometry";

        CoordinateReferenceSystem incomingCRS = incoming.getCoordinateReferenceSystem();
        if (incomingCRS == null) {
            incoming.getGeometryDescriptor().getCoordinateReferenceSystem();
        }
        if (!CRS.equalsIgnoreMetadata(incomingCRS, DefaultGeographicCRS.WGS84)) {
            throw new IllegalArgumentException("Unsupported coordinate reference system, only WGS84 supported");
        }
        // Need to generate FeatureType instance with proper namespace URI
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(incoming);
        builder.setName(name(incoming.getTypeName()));
        incoming = builder.buildFeatureType();

        String gdName = incoming.getGeometryDescriptor().getLocalName();
        for (AttributeDescriptor ad : incoming.getAttributeDescriptors()) {
            String adName = ad.getLocalName();
            if (gdName.equals(adName)) {
                ad.getUserData().put(KEY_mapping, geometryMapping);
                ad.getUserData().put(KEY_encoding, "GeoJSON");
            } else {
                ad.getUserData().put(KEY_mapping, "properties." + adName);
            }
        }
        // pre-populating this makes view creation easier...
        incoming.getUserData().put(KEY_collection, incoming.getTypeName());

        // Collection needs to exist (with index) so that it's returned with createTypeNames()
        dataStoreDB
                .createCollection(incoming.getTypeName(), new BasicDBObject())
                .createIndex(new BasicDBObject(geometryMapping, "2dsphere"));

        // Store FeatureType instance since it can't be inferred (no documents)
        ContentEntry entry = entry(incoming.getName());
        ContentState state = entry.getState(null);
        state.setFeatureType(incoming);

        schemaStore.storeSchema(incoming);
    }

    private String collectionNameFromType(SimpleFeatureType type) {
        String collectionName = (String) type.getUserData().get(KEY_collection);
        return collectionName != null ? collectionName : type.getTypeName();
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {

        Set<String> collectionNames = new LinkedHashSet<>(dataStoreDB.getCollectionNames());
        Set<String> typeNameSet = new LinkedHashSet<>();

        for (String candidateTypeName : getSchemaStore().typeNames()) {
            try {
                SimpleFeatureType candidateSchema = getSchemaStore().retrieveSchema(name(candidateTypeName));

                // extract collection that schema maps to either from user data attribute
                // or, if that's missing, the schema type name.
                String candidateCollectionName = collectionNameFromType(candidateSchema);

                // verify collection exists in db and has geometry index
                if (collectionNames.contains(candidateCollectionName)) {
                    // verify geometry exists and has mapping.
                    String geometryName =
                            candidateSchema.getGeometryDescriptor().getLocalName();
                    String geometryMapping = (String) candidateSchema
                            .getDescriptor(geometryName)
                            .getUserData()
                            .get(KEY_mapping);
                    if (geometryMapping != null) {
                        DBCollection collection = dataStoreDB.getCollection(candidateCollectionName);
                        Set<String> geometryIndices = MongoUtil.findIndexedGeometries(collection);
                        // verify geometry mapping is indexed...
                        if (geometryIndices.contains(geometryMapping)) {
                            typeNameSet.add(candidateTypeName);
                        } else {
                            LOGGER.log(
                                    Level.WARNING,
                                    "Ignoring type \"{0}\", the geometry attribute, \"{1}\", is mapped to document key \"{2}\" but it is not spatialy indexed in collection {3}",
                                    new Object[] {
                                        name(candidateTypeName), geometryName, geometryMapping, collection.getFullName()
                                    });
                        }
                    } else {
                        LOGGER.log(
                                Level.WARNING,
                                "Ignoring type \"{0}\", the geometry attribute \"{1}\" is not mapped to a document key",
                                new Object[] {name(candidateTypeName), geometryName});
                    }
                } else {
                    LOGGER.log(
                            Level.WARNING,
                            "Ignoring type \"{0}\", the collection it maps \"{1}.{2}\" does not exist",
                            new Object[] {name(candidateTypeName), dataStoreDB.getName(), candidateCollectionName});
                }
            } catch (IOException e) {
                LOGGER.log(
                        Level.WARNING,
                        "Ignoring type \"{0}\", an exception was thrown while attempting to retrieve the schema: {1}",
                        new Object[] {name(candidateTypeName), e});
            }
        }

        // Create set of collections w/o named schema
        Collection<String> collectionsToCheck = new LinkedList<>(collectionNames);
        collectionsToCheck.removeAll(typeNameSet);

        // Check collection set to see if we can use any of them
        for (String collectionName : collectionsToCheck) {
            // make sure it's not system collection
            if (!collectionName.startsWith("system.")) {
                DBCollection collection = dataStoreDB.getCollection(collectionName);
                Set<String> geometryIndexSet = MongoUtil.findIndexedGeometries(collection);
                // verify collection has an indexed geometry property
                if (!geometryIndexSet.isEmpty()) {
                    typeNameSet.add(collectionName);
                } else {
                    LOGGER.log(
                            Level.INFO,
                            "Ignoring collection \"{0}\", unable to find key with spatial index",
                            new Object[] {collection.getFullName()});
                }
            }
        }

        List<Name> typeNameList = new ArrayList<>();
        for (String name : typeNameSet) {
            typeNameList.add(name(name));
        }

        return typeNameList;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        ContentState state = entry.getState(null);
        SimpleFeatureType type = state.getFeatureType();
        if (type == null) {
            type = schemaStore.retrieveSchema(entry.getName());
            if (type != null) {
                state.setFeatureType(type);
            }
        }
        String collectionName = type != null ? collectionNameFromType(type) : entry.getTypeName();
        return new MongoFeatureStore(entry, null, dataStoreDB.getCollection(collectionName));
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction tx) throws IOException {
        if (tx != Transaction.AUTO_COMMIT) {
            throw new IllegalArgumentException("Transactions not currently supported");
        }
        return super.getFeatureWriter(typeName, filter, tx);
    }

    @Override
    protected ContentState createContentState(ContentEntry entry) {
        ContentState state = super.createContentState(entry);
        try {
            SimpleFeatureType type = schemaStore.retrieveSchema(entry.getName());
            if (type != null) {
                state.setFeatureType(type);
            }
        } catch (IOException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Exception thrown while attempting to retrieve the schema for {0}: {1}",
                    new Object[] {entry.getName(), e});
        }
        return state;
    }

    public final MongoSchemaStore getSchemaStore() {
        return schemaStore;
    }

    @Override
    public void dispose() {
        dataStoreClient.close();
        schemaStore.close();
        super.dispose();
    }

    /** Cleans current memory cached entries. */
    public void cleanEntries() {
        LOGGER.info("Proceeding to clean all store cached entries");
        for (ContentEntry entry : entries.values()) {
            entry.dispose();
        }
        entries.clear();
    }

    public Optional<MongoSchemaInitParams> getSchemaInitParams() {
        return Optional.ofNullable(schemaInitParams);
    }

    public void setSchemaInitParams(MongoSchemaInitParams schemaInitParams) {
        this.schemaInitParams = schemaInitParams;
    }
}
