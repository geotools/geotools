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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Within;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MongoDataStore extends ContentDataStore {

    static final String KEY_mapping = "mapping";
    static final String KEY_encoding = "encoding";
    static final String KEY_collection = "collection";

    final MongoSchemaStore schemaStore;

    final MongoClient dataStoreClient;
    final DB dataStoreDB;

    @SuppressWarnings("deprecation")
    FilterCapabilities filterCapabilities;

    public MongoDataStore(String dataStoreURI) {
        this(dataStoreURI, null);
    }

    public MongoDataStore(String dataStoreURI, String schemaStoreURI) {
        this(dataStoreURI, schemaStoreURI, true);
    }

    public MongoDataStore(
            String dataStoreURI, String schemaStoreURI, boolean createDatabaseIfNeeded) {

        MongoClientURI dataStoreClientURI = createMongoClientURI(dataStoreURI);
        dataStoreClient = createMongoClient(dataStoreClientURI);
        dataStoreDB =
                createDB(
                        dataStoreClient, dataStoreClientURI.getDatabase(), !createDatabaseIfNeeded);
        if (dataStoreDB == null) {
            dataStoreClient.close(); // This smells bad...
            throw new IllegalArgumentException(
                    "Unknown mongodb database, \"" + dataStoreClientURI.getDatabase() + "\"");
        }

        schemaStore = createSchemaStore(schemaStoreURI);
        if (schemaStore == null) {
            dataStoreClient.close(); // This smells bad too...
            throw new IllegalArgumentException(
                    "Unable to initialize schema store with URI \"" + schemaStoreURI + "\"");
        }

        filterCapabilities = createFilterCapabilties();
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
        if (databaseMustExist && !mongoClient.getDatabaseNames().contains(databaseName)) {
            return null;
        }
        return mongoClient.getDB(databaseName);
    }

    private MongoSchemaStore createSchemaStore(String schemaStoreURI) {
        if (schemaStoreURI.startsWith("file:")) {
            try {
                return new MongoSchemaFileStore(schemaStoreURI);
            } catch (URISyntaxException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \""
                                + schemaStoreURI
                                + "\"",
                        e);
            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \""
                                + schemaStoreURI
                                + "\"",
                        e);
            }
        } else if (schemaStoreURI.startsWith("mongodb:")) {
            try {
                return new MongoSchemaDBStore(schemaStoreURI);
            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create mongodb-based schema store with URI \""
                                + schemaStoreURI
                                + "\"",
                        e);
            }
        } else {
            try {
                return new MongoSchemaFileStore("file:" + schemaStoreURI);
            } catch (URISyntaxException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \""
                                + schemaStoreURI
                                + "\"",
                        e);
            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Unable to create file-based schema store with URI \""
                                + schemaStoreURI
                                + "\"",
                        e);
            }
        }
        LOGGER.log(Level.SEVERE, "Unsupported URI \"{0}\" for schema store", schemaStoreURI);
        return null;
    }

    @SuppressWarnings("deprecation")
    final FilterCapabilities createFilterCapabilties() {
        FilterCapabilities capabilities = new FilterCapabilities();

        /* disable FilterCapabilities.LOGICAL_OPENGIS since it contains
            Or.class (in addtions to And.class and Not.class.  MongodB 2.4
            doesn't supprt '$or' with spatial operations.
        */
        //        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addType(And.class);
        capabilities.addType(Not.class);

        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(PropertyIsLike.class);

        capabilities.addType(BBOX.class);
        capabilities.addType(Intersects.class);
        capabilities.addType(Within.class);

        capabilities.addType(Id.class);

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
            throw new IllegalArgumentException(
                    "Unsupported coordinate reference system, only WGS84 supported");
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

        Set<String> collectionNames = new LinkedHashSet<String>(dataStoreDB.getCollectionNames());
        Set<String> typeNameSet = new LinkedHashSet<String>();

        for (String candidateTypeName : getSchemaStore().typeNames()) {
            try {
                SimpleFeatureType candidateSchema =
                        getSchemaStore().retrieveSchema(name(candidateTypeName));

                // extract collection that schema maps to either from user data attribute
                // or, if that's missing, the schema type name.
                String candidateCollectionName = collectionNameFromType(candidateSchema);

                // verify collection exists in db and has geometry index
                if (collectionNames.contains(candidateCollectionName)) {
                    // verify geometry exists and has mapping.
                    String geometryName = candidateSchema.getGeometryDescriptor().getLocalName();
                    String geometryMapping =
                            (String)
                                    candidateSchema
                                            .getDescriptor(geometryName)
                                            .getUserData()
                                            .get(KEY_mapping);
                    if (geometryMapping != null) {
                        DBCollection collection =
                                dataStoreDB.getCollection(candidateCollectionName);
                        Set<String> geometryIndices = MongoUtil.findIndexedGeometries(collection);
                        // verify geometry mapping is indexed...
                        if (geometryIndices.contains(geometryMapping)) {
                            typeNameSet.add(candidateTypeName);
                        } else {
                            LOGGER.log(
                                    Level.WARNING,
                                    "Ignoring type \"{0}\", the geometry attribute, \"{1}\", is mapped to document key \"{2}\" but it is not spatialy indexed in collection {3}",
                                    new Object[] {
                                        name(candidateTypeName),
                                        geometryName,
                                        geometryMapping,
                                        collection.getFullName()
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
                            new Object[] {
                                name(candidateTypeName),
                                dataStoreDB.getName(),
                                candidateCollectionName
                            });
                }
            } catch (IOException e) {
                LOGGER.log(
                        Level.WARNING,
                        "Ignoring type \"{0}\", an exception was thrown while attempting to retrieve the schema: {1}",
                        new Object[] {name(candidateTypeName), e});
            }
        }

        // Create set of collections w/o named schema
        Collection<String> collectionsToCheck = new LinkedList<String>(collectionNames);
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

        List<Name> typeNameList = new ArrayList<Name>();
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

    final MongoSchemaStore getSchemaStore() {
        return schemaStore;
    }

    @Override
    public void dispose() {
        dataStoreClient.close();
        schemaStore.close();
        super.dispose();
    }
}
