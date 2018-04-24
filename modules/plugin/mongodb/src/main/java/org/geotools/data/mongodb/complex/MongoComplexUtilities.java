/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.complex;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.mongodb.AbstractCollectionMapper;
import org.geotools.data.mongodb.MongoFeature;
import org.geotools.data.mongodb.MongoGeometryBuilder;
import org.opengis.feature.Feature;

/** This class contains utilities methods for dealing with MongoDB complex features. */
public final class MongoComplexUtilities {

    // if this property is set to TRUE the system will expect nested collection full path to be
    // provided
    private static final boolean USE_LEGACY_PATHS =
            Boolean.parseBoolean(
                    System.getProperty(
                            "org.geotools.data.mongodb.complex.useLegacyPaths", "false"));

    // key used to store the parent JSON path in a feature user data map
    public static final String MONGO_PARENT_PATH = "MONGO_PARENT_PATH";

    private MongoComplexUtilities() {}

    /** Concat the parent path if it exists to the provided JSON path. */
    public static String resolvePath(Feature feature, String jsonPath) {
        Object parentPath = feature.getUserData().get(MONGO_PARENT_PATH);
        return parentPath == null ? jsonPath : parentPath + "." + jsonPath;
    }

    /** Store the parent path in a feature user data map. */
    public static void setParentPath(Feature feature, String parentPath) {
        feature.getUserData().put(MONGO_PARENT_PATH, parentPath);
    }

    /** If TRUE no recursive paths will be used, this onyl exists for backwards compatibility. */
    public static boolean useLegacyPaths() {
        return USE_LEGACY_PATHS;
    }

    /**
     * Will try to extract from the provided object the value that correspond to the given json
     * path.
     */
    public static Object getValue(Object object, String jsonPath) {
        // let's make sure we have a feature
        Feature feature = extractFeature(object, jsonPath);
        if (feature instanceof MongoFeature) {
            // no a nested element mongo feature
            MongoFeature mongoFeature = (MongoFeature) feature;
            return getValue(mongoFeature.getMongoObject(), jsonPath);
        }
        if (feature instanceof MongoCollectionFeature) {
            // a mongo feature in the context of a nested element
            MongoCollectionFeature collectionFeature = (MongoCollectionFeature) object;
            return getValue(
                    collectionFeature.getMongoFeature().getMongoObject(),
                    collectionFeature.getCollectionsIndexes(),
                    jsonPath);
        }
        // could not find a mongo feature, we can do nothing
        throw invalidFeature(feature, jsonPath);
    }

    /** Method for extracting or casting a feature from the provided object. */
    public static Feature extractFeature(Object feature, String jsonPath) {
        // we should have a feature
        if (!(feature instanceof Feature)) {
            // not a feature so nothing to do
            throw invalidFeature(feature, jsonPath);
        }
        // let's see if we have the a mongo feature in the user data
        Object mongoFeature =
                ((Feature) feature)
                        .getUserData()
                        .get(AbstractCollectionMapper.MONGO_OBJECT_FEATURE_KEY);
        // if we could not find a mongo feature in the user data we stick we the original feature
        return mongoFeature == null ? (Feature) feature : (Feature) mongoFeature;
    }

    /**
     * Helper method that creates an exception for when the provided object is not of the correct
     * type.
     */
    private static RuntimeException invalidFeature(Object feature, String jsonPath) {
        return new RuntimeException(
                String.format(
                        "No possible to obtain a mongo object from '%s' to extract '%s'.",
                        feature.getClass(), jsonPath));
    }

    /**
     * Will extract from the mongo db object the value that correspond to the given json path. If
     * the path contain a nested list of values an exception will be throw.
     */
    public static Object getValue(DBObject mongoObject, String jsonPath) {
        return getValue(mongoObject, Collections.emptyMap(), jsonPath);
    }

    /**
     * Will extract from the mongo db object the value that correspond to the given json path. The
     * provided collections indexes will be used to select the proper element for the collections
     * present in the path.
     */
    public static Object getValue(
            DBObject mongoObject, Map<String, Integer> collectionsIndexes, String jsonPath) {
        MongoObjectWalker walker = new MongoObjectWalker(mongoObject, collectionsIndexes, jsonPath);
        // try to convert the founded value to a geometry
        return convertGeometry(walker.getValue());
    }

    /**
     * Helper method that checks if a mongodb value is a geometry and perform the proper conversion.
     */
    private static Object convertGeometry(Object value) {
        if (!(value instanceof DBObject) || value instanceof List) {
            // not a mongodb object or a list of values so nothing to do
            return value;
        }
        DBObject object = (DBObject) value;
        Set keys = object.keySet();
        if (keys.size() != 2 || !keys.contains("coordinates") || !keys.contains("type")) {
            // is mongo db object but not a geometry
            return value;
        }
        // we have a geometry so let's try to convert it
        MongoGeometryBuilder builder = new MongoGeometryBuilder();
        try {
            // return the converted geometry
            return builder.toGeometry(object);
        } catch (Exception exception) {
            // well could not convert the mongo db object to a geometry
        }
        return value;
    }

    /** Utility class class to extract information from a MongoDB object giving a certain path. */
    private static final class MongoObjectWalker {

        private final Map<String, Integer> collectionsIndexes;
        private final String[] jsonPathParts;

        private String currentJsonPath;
        private int currentJsonPathPartIndex;
        private Object currentObject;

        MongoObjectWalker(
                DBObject mongoObject, Map<String, Integer> collectionsIndexes, String jsonPath) {
            this.collectionsIndexes = collectionsIndexes;
            this.jsonPathParts = jsonPath.split("\\.");
            this.currentJsonPath = "";
            this.currentJsonPathPartIndex = 0;
            this.currentObject = mongoObject;
        }

        Object getValue() {
            while (hasNext() && currentObject != null) {
                next();
            }
            // end of the walked path or NULL value found
            return currentObject;
        }

        private boolean hasNext() {
            // we have a next element if we still have paths parts or we are currently
            // walking a collection and there is a index defined for this collection
            return currentJsonPathPartIndex < jsonPathParts.length
                    || (currentObject instanceof BasicDBList
                            && collectionsIndexes.get(currentJsonPath) != null);
        }

        private void next() {
            // let's walk to the next path part using the current object
            if (currentObject instanceof List) {
                // the current object is a list, we need to select the current index
                currentObject = next((List) currentObject);
            } else if (currentObject instanceof DBObject) {
                currentObject = next((DBObject) currentObject);
            } else {
                throw new RuntimeException(
                        String.format(
                                "Trying to get data from a non MongoDB object, current json path is '%s'.",
                                currentJsonPath));
            }
        }

        private Object next(DBObject dbObject) {
            // we have a mongo db object, let's update the current json path
            currentJsonPath = concatPath(currentJsonPath, jsonPathParts[currentJsonPathPartIndex]);
            // get the value from the mongo db object that correspond to the current json path part
            Object result = dbObject.get(jsonPathParts[currentJsonPathPartIndex]);
            currentJsonPathPartIndex++;
            return result;
        }

        private Object next(List basicDBList) {
            // let's find current index for this collection
            Integer rawCollectionIndex = collectionsIndexes.get(currentJsonPath);
            if (rawCollectionIndex == null && basicDBList.size() == 1) {
                // this collection only has a single element and there is not index information for
                // this collection
                return basicDBList.get(0);
            }
            if (rawCollectionIndex == null) {
                throw new RuntimeException(
                        String.format(
                                "There is no index available for collection '%s'.",
                                currentJsonPath));
            }
            // just return the list element that matches the current index
            return basicDBList.get(rawCollectionIndex);
        }
    }

    /**
     * Will try to extract from the provided object all the values that correspond to the given json
     * path.
     */
    public static Object getValues(Object object, String jsonPath) {
        // let's make sure we have a feature
        Feature feature = extractFeature(object, jsonPath);
        if (feature instanceof MongoFeature) {
            // no a nested element mongo feature
            MongoFeature mongoFeature = (MongoFeature) feature;
            return getValues(mongoFeature.getMongoObject(), jsonPath);
        }
        if (feature instanceof MongoCollectionFeature) {
            // a mongo feature in the context of a nested element
            MongoCollectionFeature collectionFeature = (MongoCollectionFeature) object;
            return getValues(collectionFeature.getMongoFeature().getMongoObject(), jsonPath);
        }
        // could not find a mongo feature, we can do nothing
        throw invalidFeature(feature, jsonPath);
    }

    /**
     * Will extract from the mongo db object all the values that correspond to the given json path.
     * If the path contains nested collections the values from all the branches will be merged.
     */
    public static Object getValues(DBObject dbObject, String jsonPath) {
        if (jsonPath == null || jsonPath.isEmpty() || dbObject == null) {
            // nothing to do here
            return Collections.emptyList();
        }
        // let's split the json path in parts which will give us the necessary keys
        String[] jsonPathParts = jsonPath.split("\\.");
        // recursively get the values using an helper function
        List<Object> values = getValuesHelper(dbObject, jsonPathParts, new ArrayList<>(), 0);
        if (values.size() == 1) {
            // we only have a single value, let's extract it
            return values.get(0);
        }
        return values;
    }

    /**
     * Helper function that will walk a mongo db object and retrieve all the values for a certain
     * path.
     */
    private static List<Object> getValuesHelper(
            DBObject dbObject, String[] jsonPathParts, List<Object> values, int index) {
        // get the object corresponding to the current index
        Object object = dbObject.get(jsonPathParts[index]);
        if (object == null) {
            // we are done
            return values;
        }
        // check if we reach the end of the json path
        boolean finalPath = index == jsonPathParts.length - 1;
        index++;
        if (object instanceof List) {
            if (finalPath) {
                // we reached the end of the json path and we have a list, so let's add all the
                // elements of the list
                for (Object value : (List) object) {
                    values.add(convertGeometry(value));
                }
            } else {
                // well we have a list so we need to interact over each element of the list
                for (Object element : (List) object) {
                    getValuesHelper((DBObject) element, jsonPathParts, values, index);
                }
            }
        } else {
            if (finalPath) {
                // we reached the end of the json path so let's add this object
                values.add(convertGeometry(object));
            } else {
                // we need to go deeper in this object
                getValuesHelper((DBObject) object, jsonPathParts, values, index);
            }
        }
        // we return the list of founded values for commodity
        return values;
    }

    /** Simple method that adds an element ot a json path. */
    private static String concatPath(String parentPath, String path) {
        if (parentPath == null || parentPath.isEmpty()) {
            // first element of the path
            return path;
        }
        return parentPath + "." + path;
    }

    /** Compute the mappings for a mongo db object, this can be used to create a feature mapping. */
    public static Map<String, Class> findMappings(DBObject dbObject) {
        Map<String, Class> mappings = new HashMap<>();
        findMappingsHelper(dbObject, "", mappings);
        return mappings;
    }

    /** Helper method that will recursively walk a mongo db object and compute is mappings. */
    private static void findMappingsHelper(
            Object object, String parentPath, Map<String, Class> mappings) {
        if (object == null) {
            return;
        }
        if (object instanceof DBObject) {
            DBObject dbObject = (DBObject) object;
            for (String key : dbObject.keySet()) {
                Object value = dbObject.get(key);
                if (value == null) {
                    continue;
                }
                String path = concatPath(parentPath, key);
                if (value instanceof List) {
                    List list = (List) value;
                    if (!list.isEmpty()) {
                        findMappingsHelper(list.get(0), path, mappings);
                    }
                } else if (value instanceof DBObject) {
                    findMappingsHelper(value, path, mappings);
                } else {
                    mappings.put(path, value.getClass());
                }
            }
        } else {
            mappings.put(parentPath, object.getClass());
        }
    }
}
