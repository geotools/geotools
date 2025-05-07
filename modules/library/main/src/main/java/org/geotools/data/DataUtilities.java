/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.SerializationUtils;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFactory.Param;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureLocking;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.style.UserLayer;
import org.geotools.api.util.ProgressListener;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.collection.SpatialIndexFeatureCollection;
import org.geotools.data.collection.SpatialIndexFeatureSource;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.NullProgressListener;
import org.geotools.data.view.DefaultView;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.BridgeIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.PropertyNameResolvingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Utility functions for use with GeoTools with data classes.
 *
 * <p>These methods fall into several categories:
 *
 * <p>Conversion between common data structures.
 *
 * <ul>
 *   <li>{@link #collection} methods: creating/converting a {@link SimpleFeatureCollection} from a range of input.
 *   <li>{@link #simple} methods: adapting from generic Feature use to SimpleFeature. Used to convert to SimpleFeature,
 *       SimpleFeatureCollection,SimpleFeatureSource
 *   <li>{@link #list} to quickly copy features into a memory based list
 *   <li>{@link #reader} methods to convert to FeatureReader
 *   <li>{@link #expression} setup a FeatureSource wrapper around the provided data
 * </ul>
 *
 * <p>SimpleFeatureType and SimpleFeature encoding/decoding from String as used by the PropertyDataStore tutorials.
 *
 * <ul>
 *   <li>{@link #createType} methods: to create a SimpleFeatureType from a one line text string
 *   <li>{@link #encodeType}: text string representation of a SimpleFeaturerType
 *   <li>{@link #createFeature}: create a SimpleFeature from a one line text String
 *   <li>{@link #encodeFeature}: encode a feature as a single line text string
 * </ul>
 *
 * <p>Working with SimpleFeatureType (this class is immutable so we always have to make a modified copy):
 *
 * <ul>
 *   <li>{@link #createSubType(SimpleFeatureType, String[])} methods return a modified copy of an origional feature
 *       type. Used to cut down an exsiting feature type to reflect only the attributes requested when using
 *       {@link SimpleFeatureSource#getFeatures(Filter)}.
 *   <li>{@link #compare} and {@link #isMatch(AttributeDescriptor, AttributeDescriptor)} are used to check for types
 *       compatible with {@link #createSubType} used to verify that feature values can be copied across
 * </ul>
 *
 * <p>Manipulating individual features and data values:
 *
 * <ul>
 *   <li>{@link #reType} generates a cut down version of an original feature in the same manners as
 *       {@link #createSubType}
 *   <li>{@link #template} and {@link #defaultValue} methods which uses {@link AttributeDescriptor#getDefaultValue()}
 *       when creating new empty features
 *   <li>{@link #duplicate(Object)} used for deep copy of feature data
 *   <li>
 * </ul>
 *
 * And a grab bag of helpful utility methods for those implementing a DataStore:
 *
 * <ul>
 *   <li>{@link #includeFilters} and {@link #excludeFilters} work as a compound {@link FileFilter} making
 *       {@link File#listFiles} easier to use
 *   <li>{@link #propertyNames}, {@link #fidSet}, {@link #attributeNames} methods are used to double check a provided
 *       query and ensure it can be correctly handed. {@link #sortComparator}, {@link #resolvePropertyNames} and
 *       {@link #mixQueries} are used to prep a {@link Query} prior to use
 * </ul>
 *
 * @author Jody Garnett, Refractions Research
 */
public class DataUtilities {
    /** Type map used by {@link #createType(String, String)} methods */
    static Map<String, Class> typeMap = new HashMap<>();

    /** Reverse type map used by {@link #encodeType(FeatureType)} */
    static Map<Class, String> typeEncode = new HashMap<>();

    static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    static {
        typeEncode.put(String.class, "String");
        typeMap.put("String", String.class);
        typeMap.put("string", String.class);
        typeMap.put("\"\"", String.class);

        typeEncode.put(Integer.class, "Integer");
        typeMap.put("Integer", Integer.class);
        typeMap.put("int", Integer.class);
        typeMap.put("0", Integer.class);

        typeEncode.put(Double.class, "Double");
        typeMap.put("Double", Double.class);
        typeMap.put("double", Double.class);
        typeMap.put("0.0", Double.class);

        typeEncode.put(Float.class, "Float");
        typeMap.put("Float", Float.class);
        typeMap.put("float", Float.class);
        typeMap.put("0.0f", Float.class);

        typeEncode.put(Boolean.class, "Boolean");
        typeMap.put("Boolean", Boolean.class);
        typeMap.put("true", Boolean.class);
        typeMap.put("false", Boolean.class);

        typeEncode.put(UUID.class, "UUID");
        typeMap.put("UUID", UUID.class);

        typeEncode.put(Geometry.class, "Geometry");
        typeMap.put("Geometry", Geometry.class);
        typeMap.put("com.vividsolutions.jts.geom.Geometry", Geometry.class);
        typeMap.put("org.locationtech.jts.geom.Geometry", Geometry.class);

        typeEncode.put(Point.class, "Point");
        typeMap.put("Point", Point.class);
        typeMap.put("com.vividsolutions.jts.geom.Point", Point.class);
        typeMap.put("org.locationtech.jts.geom.Point", Point.class);

        typeEncode.put(LineString.class, "LineString");
        typeMap.put("LineString", LineString.class);
        typeMap.put("com.vividsolutions.jts.geom.LineString", LineString.class);
        typeMap.put("org.locationtech.jts.geom.LineString", LineString.class);

        typeEncode.put(Polygon.class, "Polygon");
        typeMap.put("Polygon", Polygon.class);
        typeMap.put("com.vividsolutions.jts.geom.Polygon", Polygon.class);
        typeMap.put("org.locationtech.jts.geom.Polygon", Polygon.class);

        typeEncode.put(MultiPoint.class, "MultiPoint");
        typeMap.put("MultiPoint", MultiPoint.class);
        typeMap.put("com.vividsolutions.jts.geom.MultiPoint", MultiPoint.class);
        typeMap.put("org.locationtech.jts.geom.MultiPoint", MultiPoint.class);

        typeEncode.put(MultiLineString.class, "MultiLineString");
        typeMap.put("MultiLineString", MultiLineString.class);
        typeMap.put("com.vividsolutions.jts.geom.MultiLineString", MultiLineString.class);
        typeMap.put("org.locationtech.jts.geom.MultiLineString", MultiLineString.class);

        typeEncode.put(MultiPolygon.class, "MultiPolygon");
        typeMap.put("MultiPolygon", MultiPolygon.class);
        typeMap.put("com.vividsolutions.jts.geom.MultiPolygon", MultiPolygon.class);
        typeMap.put("org.locationtech.jts.geom.MultiPolygon", MultiPolygon.class);

        typeEncode.put(GeometryCollection.class, "GeometryCollection");
        typeMap.put("GeometryCollection", GeometryCollection.class);
        typeMap.put("com.vividsolutions.jts.geom.GeometryCollection", GeometryCollection.class);
        typeMap.put("org.locationtech.jts.geom.GeometryCollection", GeometryCollection.class);

        typeEncode.put(Date.class, "Date");
        typeMap.put("Date", Date.class);
    }

    /**
     * Retrieve the attributeNames defined by the featureType
     *
     * @return array of simple attribute names
     */
    public static String[] attributeNames(SimpleFeatureType featureType) {
        String[] names = new String[featureType.getAttributeCount()];
        final int count = featureType.getAttributeCount();
        for (int i = 0; i < count; i++) {
            names[i] = featureType.getDescriptor(i).getLocalName();
        }
        return names;
    }

    /**
     * Traverses the filter and returns any encountered property names.
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the attributeName does not
     * match the actual name of the type.
     */
    public static String[] attributeNames(Filter filter, final SimpleFeatureType featureType) {
        if (filter == null) {
            return new String[0];
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        filter.accept(attExtractor, null);
        String[] attributeNames = attExtractor.getAttributeNames();
        return attributeNames;
    }

    /**
     * Traverses the filter and returns any encountered property names.
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the attributeName does not
     * match the actual name of the type.
     */
    public static Set<PropertyName> propertyNames(Filter filter, final SimpleFeatureType featureType) {
        if (filter == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        filter.accept(attExtractor, null);
        Set<PropertyName> propertyNames = attExtractor.getPropertyNameSet();
        return propertyNames;
    }

    /** Traverses the filter and returns any encountered property names. */
    public static String[] attributeNames(Filter filter) {
        return attributeNames(filter, null);
    }

    /** Traverses the filter and returns any encountered property names. */
    public static Set<PropertyName> propertyNames(Filter filter) {
        return propertyNames(filter, null);
    }

    /**
     * Traverses the expression and returns any encountered property names.
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the attributeName does not
     * match the actual name of the type.
     */
    public static String[] attributeNames(Expression expression, final SimpleFeatureType featureType) {
        if (expression == null) {
            return new String[0];
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        expression.accept(attExtractor, null);
        String[] attributeNames = attExtractor.getAttributeNames();
        return attributeNames;
    }

    /**
     * Traverses the expression and returns any encountered property names.
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the attributeName does not
     * match the actual name of the type.
     */
    public static Set<PropertyName> propertyNames(Expression expression, final SimpleFeatureType featureType) {
        if (expression == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        expression.accept(attExtractor, null);
        Set<PropertyName> propertyNames = attExtractor.getPropertyNameSet();
        return propertyNames;
    }

    /** Traverses the expression and returns any encountered property names. */
    public static String[] attributeNames(Expression expression) {
        return attributeNames(expression, null);
    }

    /** Traverses the expression and returns any encountered property names. */
    public static Set<PropertyName> propertyNames(Expression expression) {
        return propertyNames(expression, null);
    }

    /**
     * Compare attribute coverage between two feature types (allowing the identification of subTypes).
     *
     * <p>Strict compatibility is assumed meaning that both the local name and java binding are compatible (see
     * {@link #compareInternal(SimpleFeatureType, SimpleFeatureType, boolean)} for more details.
     *
     * <p>Namespace is not considered in this operations. You may still need to reType to get the correct namespace, or
     * reorder.
     *
     * <p>Please note this method will not result in a stable sort if used in a {@link Comparator} as -1 is used to
     * indicate incompatiblity (rather than simply "before").
     *
     * @param typeA FeatureType being compared
     * @param typeB FeatureType being compared against
     */
    public static int compare(SimpleFeatureType typeA, SimpleFeatureType typeB) {
        return compareInternal(typeA, typeB, true);
    }

    /**
     * Compare attribute coverage between two feature types (allowing the identification of subTypes).
     *
     * <p>loose compatibility is assumed based on local name (java binding may differ) (see
     * {@link #compareInternal(SimpleFeatureType, SimpleFeatureType, boolean)} for more details.
     *
     * <p>Namespace is not considered in this operations. You may still need to reType to get the correct namespace, or
     * reorder.
     *
     * <p>Please note this method will not result in a stable sort if used in a {@link Comparator} as -1 is used to
     * indicate incompatiblity (rather than simply "before").
     *
     * @param typeA FeatureType being compared
     * @param typeB FeatureType being compared against
     */
    public static int compareNames(SimpleFeatureType typeA, SimpleFeatureType typeB) {
        return compareInternal(typeA, typeB, false);
    }

    /**
     * Compare attribute coverage between two feature types (allowing the identification of subTypes).
     *
     * <p>The comparison results in a number with the following meaning:
     *
     * <ul>
     *   <li>1: if typeA is a sub type/reorder/renamespace of typeB
     *   <li>0: if typeA and typeB are the same type
     *   <li>-1: if typeA is not subtype of typeB
     * </ul>
     *
     * <p>Comparison is based on {@link AttributeDescriptor} - the {@link #isMatch(AttributeDescriptor,
     * AttributeDescriptor, boolean)} method is used to quickly confirm that the local name and java binding (depending
     * on strict flag value) are compatible.
     *
     * <p>Namespace is not considered in this operations. You may still need to reType to get the correct namespace, or
     * reorder.
     *
     * <p>Please note this method will not result in a stable sort if used in a {@link Comparator} as -1 is used to
     * indicate incompatiblity (rather than simply "before").
     *
     * @param typeA FeatureType being compared
     * @param typeB FeatureType being compared against
     * @param strict flag controlling the comparison check
     */
    protected static int compareInternal(SimpleFeatureType typeA, SimpleFeatureType typeB, boolean strict) {
        if (typeA == typeB) {
            return 0;
        }

        if (typeA == null) {
            return -1;
        }

        if (typeB == null) {
            return -1;
        }

        int countA = typeA.getAttributeCount();
        int countB = typeB.getAttributeCount();

        if (countA > countB) {
            return -1;
        }

        // may still be the same featureType (Perhaps they differ on namespace?)
        AttributeDescriptor a;
        int match = 0;

        for (int i = 0; i < countA; i++) {
            a = typeA.getDescriptor(i);

            if (isMatch(a, typeB.getDescriptor(i), strict)) {
                match++;
            } else if (!isMatch(a, typeB.getDescriptor(a.getLocalName()), strict)) {
                // cannot find any match for Attribute in typeA
                return -1;
            }
        }

        if ((countA == countB) && (match == countA)) {
            // all attributes in typeA agreed with typeB
            // (same order and type)
            return 0;
        }

        return 1;
    }

    /**
     * Quickly check if two descriptors are at all compatible.
     *
     * <p>This method checks the descriptors name and class binding to see if the values have any chance of being
     * compatible. Strict compatibility assumed (see also {@link #isMatch(AttributeDescriptor, AttributeDescriptor,
     * boolean)}.
     *
     * @param a descriptor to compare
     * @param b descriptor to compare
     * @return true to the descriptors name and binding class match
     */
    public static boolean isMatch(AttributeDescriptor a, AttributeDescriptor b) {
        return isMatch(a, b, true);
    }

    /**
     * Quickly check descriptors compatibility.
     *
     * <p>This method checks the descriptors name and class binding to see if the values have any chance of being
     * compatible.
     *
     * @param a descriptor to compare
     * @param b descriptor to compare
     * @param strict if true both descriptor name and class binding is checked otherwise a more loose form o
     *     compatibility is assumed where equality is determined by descriptor name only
     * @return true if compatibility comparison succeeds
     */
    public static boolean isMatch(AttributeDescriptor a, AttributeDescriptor b, boolean strict) {
        if (a == b) {
            return true;
        }

        if (b == null) {
            return false;
        }

        if (a == null) {
            return false;
        }

        if (a.equals(b)) {
            return true;
        }

        if (strict) {
            if (a.getLocalName().equals(b.getLocalName())
                    && a.getType().getBinding().equals(b.getType().getBinding())) {
                return true;
            }
        } else {
            if (a.getLocalName().equals(b.getLocalName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates duplicate of feature adjusted to the provided featureType.
     *
     * <p>Please note this implementation provides "deep copy" using {@link #duplicate(Object)} to copy each attribute.
     *
     * @param featureType FeatureType requested
     * @param feature Origional Feature from DataStore
     * @return An instance of featureType based on feature
     * @throws IllegalAttributeException If opperation could not be performed
     */
    public static SimpleFeature reType(SimpleFeatureType featureType, SimpleFeature feature)
            throws IllegalAttributeException {
        SimpleFeatureType origional = feature.getFeatureType();

        if (featureType.equals(origional)) {
            return SimpleFeatureBuilder.copy(feature);
        }

        String id = feature.getID();
        int numAtts = featureType.getAttributeCount();
        Object[] attributes = new Object[numAtts];
        String xpath;

        for (int i = 0; i < numAtts; i++) {
            AttributeDescriptor curAttType = featureType.getDescriptor(i);
            xpath = curAttType.getLocalName();
            attributes[i] = duplicate(feature.getAttribute(xpath));
        }

        return SimpleFeatureBuilder.build(featureType, attributes, id);
    }

    /**
     * Retypes the feature to match the provided featureType.
     *
     * <p>The duplicate parameter indicates how the new feature is to be formed:
     *
     * <ul>
     *   <li>dupliate is true: A "deep copy" is made of each attribute resulting in a safe "copy"Adjusts the attribute
     *       order to match the provided featureType.
     *   <li>duplicate is false: the attributes are simply reordered and are actually the same instances as those in the
     *       origional feature
     * </ul>
     *
     * In the future this method may simply return a "wrapper" when duplicate is false.
     *
     * <p>
     *
     * @param duplicate True to perform {@link #duplicate(Object)} on each attribute
     */
    public static SimpleFeature reType(SimpleFeatureType featureType, SimpleFeature feature, boolean duplicate)
            throws IllegalAttributeException {
        if (duplicate) {
            return reType(featureType, feature);
        }

        FeatureType origional = feature.getFeatureType();
        if (featureType.equals(origional)) {
            return feature;
        }
        String id = feature.getID();
        int numAtts = featureType.getAttributeCount();
        Object[] attributes = new Object[numAtts];

        for (int i = 0; i < numAtts; i++) {
            AttributeDescriptor curAttType = featureType.getDescriptor(i);
            attributes[i] = feature.getAttribute(curAttType.getLocalName());
        }
        return SimpleFeatureBuilder.build(featureType, attributes, id);
    }

    /**
     * Performs a deep copy of the provided object.
     *
     * <p>A number of tricks are used to make this as fast as possible:
     *
     * <ul>
     *   <li>Simple or Immutable types are copied as is (String, Integer, Float, URL, etc..)
     *   <li>JTS Geometry objects are cloned
     *   <li>Arrays and the Collection classes are duplicated element by element
     * </ul>
     *
     * This function is used recusively for (in order to handle complext features) no attempt is made to detect cycles
     * at this time so your milage may vary.
     *
     * @param src Source object
     * @return copy of source object
     */
    public static Object duplicate(Object src) {
        // JD: this method really needs to be replaced with somethign better

        if (src == null) {
            return null;
        }

        //
        // The following are things I expect
        // Features will contain.
        //
        if (src instanceof String
                || src instanceof Integer
                || src instanceof Double
                || src instanceof Float
                || src instanceof Byte
                || src instanceof Boolean
                || src instanceof Short
                || src instanceof Long
                || src instanceof Character
                || src instanceof Number) {
            return src;
        }

        if (src instanceof Date) {
            return new Date(((Date) src).getTime());
        }

        if (src instanceof URL || src instanceof URI) {
            return src; // immutable
        }

        if (src instanceof Object[]) {
            Object[] array = (Object[]) src;
            Object[] copy = new Object[array.length];

            for (int i = 0; i < array.length; i++) {
                copy[i] = duplicate(array[i]);
            }

            return copy;
        }

        if (src instanceof Geometry) {
            Geometry geometry = (Geometry) src;

            return geometry.copy();
        }

        if (src instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) src;
            return SimpleFeatureBuilder.copy(feature);
        }

        //
        // We are now into diminishing returns
        // I don't expect Features to contain these often
        // (eveything is still nice and recursive)
        //
        Class<? extends Object> type = src.getClass();

        if (type.isArray() && type.getComponentType().isPrimitive()) {
            int length = Array.getLength(src);
            Object copy = Array.newInstance(type.getComponentType(), length);
            System.arraycopy(src, 0, copy, 0, length);

            return copy;
        }

        if (type.isArray()) {
            int length = Array.getLength(src);
            Object copy = Array.newInstance(type.getComponentType(), length);

            for (int i = 0; i < length; i++) {
                Array.set(copy, i, duplicate(Array.get(src, i)));
            }

            return copy;
        }

        if (src instanceof List) {
            List list = (List) src;
            List<Object> copy = new ArrayList<>(list.size());

            for (Object o : list) {
                copy.add(duplicate(o));
            }

            return Collections.unmodifiableList(copy);
        }

        if (src instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (Map<Object, Object>) src;
            Map<Object, Object> copy = new HashMap<>(map.size());

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                copy.put(entry.getKey(), duplicate(entry.getValue()));
            }

            return Collections.unmodifiableMap(copy);
        }

        if (src instanceof GridCoverage) {
            return src; // inmutable
        }

        // last ditch effort is the source is serializable
        if (src instanceof Serializable) {
            return SerializationUtils.clone((Serializable) src);
        }

        //
        // I have lost hope and am returning the orgional reference
        // Please extend this to support additional classes.
        //
        // And good luck getting Cloneable to work
        throw new IllegalAttributeException(null, "Do not know how to deep copy " + type.getName());
    }

    /**
     * Constructs an empty feature to use as a Template for new content.
     *
     * <p>We may move this functionality to FeatureType.create( null )?
     *
     * @param featureType Type of feature we wish to create
     * @return A new Feature of type featureType
     */
    public static SimpleFeature template(SimpleFeatureType featureType) throws IllegalAttributeException {
        return SimpleFeatureBuilder.build(featureType, defaultValues(featureType), null);
    }

    /**
     * Use the provided featureType to create an empty feature.
     *
     * <p>The {@link #defaultValues(SimpleFeatureType)} method is used to generate the intial values (making use of
     * {@link AttributeDescriptor#getDefaultValue()} as required.
     *
     * @return Craeted feature
     */
    public static SimpleFeature template(SimpleFeatureType featureType, String featureID) {
        return SimpleFeatureBuilder.build(featureType, defaultValues(featureType), featureID);
    }

    /**
     * Produce a set of default values for the provided FeatureType
     *
     * @return Array of values, that are good starting point for data entry
     */
    public static Object[] defaultValues(SimpleFeatureType featureType) {
        return defaultValues(featureType, null);
    }

    /**
     * Create a new feature from the provided values, using appropriate default values for any nulls provided.
     *
     * @return newly created feature
     * @throws ArrayIndexOutOfBoundsException If the number of provided values does not match the featureType
     */
    public static SimpleFeature template(SimpleFeatureType featureType, Object... providedValues) {
        return SimpleFeatureBuilder.build(featureType, defaultValues(featureType, providedValues), null);
    }

    /**
     * Create a new feature from the provided values, using appropriate default values for any nulls provided.
     *
     * @param providedValues provided attributes
     * @return newly created feature
     * @throws ArrayIndexOutOfBoundsException If the number of provided values does not match the featureType
     */
    public static SimpleFeature template(SimpleFeatureType featureType, String featureID, Object... providedValues) {
        return SimpleFeatureBuilder.build(featureType, defaultValues(featureType, providedValues), featureID);
    }

    /**
     * Create default values matching the provided feature type.
     *
     * @return set of default values
     * @throws ArrayIndexOutOfBoundsException If the number of provided values does not match the featureType
     */
    public static Object[] defaultValues(SimpleFeatureType featureType, Object... values) {
        if (values == null) {
            values = new Object[featureType.getAttributeCount()];
        } else if (values.length != featureType.getAttributeCount()) {
            throw new ArrayIndexOutOfBoundsException("values");
        }

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            AttributeDescriptor descriptor = featureType.getDescriptor(i);
            values[i] = descriptor.getDefaultValue();
        }

        return values;
    }

    /**
     * Returns a non-null default value for the class that is passed in. This is a helper class an can't create a
     * default class for all types but it does support:
     *
     * <ul>
     *   <li>String
     *   <li>Object - will return empty string
     *   <li>Integer
     *   <li>Double
     *   <li>Long
     *   <li>Short
     *   <li>Float
     *   <li>BigDecimal
     *   <li>BigInteger
     *   <li>Character
     *   <li>Boolean
     *   <li>UUID
     *   <li>Timestamp
     *   <li>java.sql.Date
     *   <li>java.sql.Time
     *   <li>java.util.Date
     *   <li>JTS Geometries
     *   <li>Arrays - will return an empty array of the appropriate type
     * </ul>
     */
    public static Object defaultValue(Class type) {
        if (type == String.class || type == Object.class) {
            return "";
        }
        if (type == Integer.class) {
            return Integer.valueOf(0);
        }
        if (type == Double.class) {
            return Double.valueOf(0);
        }
        if (type == Long.class) {
            return Long.valueOf(0);
        }
        if (type == Short.class) {
            return Short.valueOf((short) 0);
        }
        if (type == Float.class) {
            return Float.valueOf(0.0f);
        }
        if (type == BigDecimal.class) {
            return BigDecimal.valueOf(0);
        }
        if (type == BigInteger.class) {
            return BigInteger.valueOf(0);
        }
        if (type == Character.class) {
            return Character.valueOf(' ');
        }
        if (type == Boolean.class) {
            return Boolean.FALSE;
        }
        if (type == UUID.class) {
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
        if (type == Timestamp.class) return new Timestamp(0);
        if (type == java.sql.Date.class) return new java.sql.Date(0);
        if (type == java.sql.Time.class) return new java.sql.Time(0);
        if (type == java.util.Date.class) return new java.util.Date(0);

        GeometryFactory fac = new GeometryFactory();
        Coordinate coordinate = new Coordinate(0, 0);
        Point point = fac.createPoint(coordinate);

        if (type == Point.class) {
            return point;
        }
        if (type == MultiPoint.class) {
            return fac.createMultiPoint(new Point[] {point});
        }
        LineString lineString = fac.createLineString(new Coordinate[] {new Coordinate(0, 0), new Coordinate(0, 1)});
        if (type == LineString.class) {
            return lineString;
        }
        LinearRing linearRing = fac.createLinearRing(new Coordinate[] {
            new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0)
        });
        if (type == LinearRing.class) {
            return linearRing;
        }
        if (type == MultiLineString.class) {
            return fac.createMultiLineString(new LineString[] {lineString});
        }
        Polygon polygon = fac.createPolygon(linearRing, new LinearRing[0]);
        if (type == Polygon.class) {
            return polygon;
        }
        if (type == MultiPolygon.class) {
            return fac.createMultiPolygon(new Polygon[] {polygon});
        }

        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        if (type == Geometry.class) { // return a point as default coordinate
            return fac.createGeometry(point);
        }

        throw new IllegalArgumentException(type + " is not supported by this method");
    }

    /**
     * Creates a FeatureReader<SimpleFeatureType, SimpleFeature> for testing.
     *
     * @param features Array of features
     * @return FeatureReader<SimpleFeatureType, SimpleFeature> spaning provided feature array
     * @throws IOException If provided features Are null or empty
     */
    public static FeatureReader<SimpleFeatureType, SimpleFeature> reader(final SimpleFeature... features)
            throws IOException {
        if ((features == null) || (features.length == 0)) {
            throw new IOException("Provided features where empty");
        }

        return new FeatureReader<>() {
            SimpleFeature[] array = features;

            int offset = -1;

            @Override
            public SimpleFeatureType getFeatureType() {
                return features[0].getFeatureType();
            }

            @Override
            public SimpleFeature next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more features");
                }

                return array[++offset];
            }

            @Override
            public boolean hasNext() {
                return (array != null) && (offset < (array.length - 1));
            }

            @Override
            public void close() {
                array = null;
                offset = -1;
            }
        };
    }

    /**
     * Wrap up an array of features as a FeatureSource.
     *
     * @param featureArray Array of features
     * @return FeatureSource
     */
    public static SimpleFeatureSource source(final SimpleFeature... featureArray) {
        final SimpleFeatureType featureType;

        if ((featureArray == null) || (featureArray.length == 0)) {
            featureType = FeatureTypes.EMPTY;
        } else {
            featureType = featureArray[0].getFeatureType();
        }
        ListFeatureCollection collection = new ListFeatureCollection(featureType, featureArray);
        for (SimpleFeature feature : collection) {
            if (feature.getFeatureType() != featureType) {
                String typeName = featureType.getTypeName();
                throw new IllegalStateException("Array inconsistent, expected each feature of type  " + typeName);
            }
        }
        return source(collection);
    }

    /**
     * Wraps up the provided feature collection in as a SimpleFeatureSource.
     *
     * <p>This is usually done for use by the renderer; allowing it to query the feature collection as required.
     *
     * @param collection Feature collection providing content
     * @return FeatureSource used to wrap the content
     * @throws NullPointerException if any of the features are null
     * @throws IllegalArgumentException If the provided collection is inconsistent (perhaps containing mixed feature
     *     types)
     */
    public static SimpleFeatureSource source(final FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        if (collection == null) {
            throw new NullPointerException("No content provided");
        }
        if (collection instanceof ListFeatureCollection) {
            ListFeatureCollection list = (ListFeatureCollection) collection;
            CollectionFeatureSource source = new CollectionFeatureSource(list);

            return source;
        }
        if (collection instanceof SpatialIndexFeatureCollection) {
            SpatialIndexFeatureCollection indexed = (SpatialIndexFeatureCollection) collection;
            SpatialIndexFeatureSource source = new SpatialIndexFeatureSource(indexed);

            return source;
        }
        if (collection instanceof TreeSetFeatureCollection) {
            TreeSetFeatureCollection tree = (TreeSetFeatureCollection) collection;
            CollectionFeatureSource source = new CollectionFeatureSource(tree);

            return source;
        }
        SimpleFeatureCollection simpleFeatureCollection = simple(collection);
        CollectionFeatureSource source = new CollectionFeatureSource(simpleFeatureCollection);
        return source;
    }

    /**
     * Return a 'view' of the given {@code DataStore} constrained by a {@code Query}.
     *
     * @param store the data store
     * @param query the query
     * @return the constrained view
     * @throws IOException if the data store cannot be accessed
     * @throws SchemaException if the query is incompatible with the store's contents
     */
    public static SimpleFeatureSource createView(final DataStore store, final Query query)
            throws IOException, SchemaException {
        return createView(store.getFeatureSource(query.getTypeName()), query);
    }

    /**
     * Return a 'view' of the given {@code FeatureSource} constrained by a {@code Query}.
     *
     * @param source feature source
     * @param query the query
     * @return the constrained view
     * @throws IOException if the data store cannot be accessed
     * @throws SchemaException if the query is incompatible with the store's contents
     */
    public static SimpleFeatureSource createView(final SimpleFeatureSource source, final Query query)
            throws IOException, SchemaException {
        return new DefaultView(source, query);
    }

    /**
     * Adapt a feature collection as a read-only DataStore.
     *
     * <p>See {@link UserLayer} for example use.
     *
     * @param features feature collection to adap
     * @return read-only DataStore
     */
    public static DataStore dataStore(final SimpleFeatureCollection features) {
        SimpleFeatureSource source = source(features);
        return dataStore(source);
    }
    /**
     * Adapt a single FeatureSource as a read-only DataStore.
     *
     * <p>See {@link UserLayer} for example use.
     *
     * @param source Feature source to adapt
     * @return read-only DataStore
     */
    public static DataStore dataStore(SimpleFeatureSource source) {
        return new DataStoreAdaptor(source);
    }
    /**
     * Adapt a collection to a reader for use with FeatureStore.setFeatures( reader ).
     *
     * @param collection Collection of SimpleFeature
     * @return FeatureRedaer over the provided contents
     * @throws IOException IOException if there is any problem reading the content.
     */
    public static FeatureReader<SimpleFeatureType, SimpleFeature> reader(Collection<SimpleFeature> collection)
            throws IOException {
        return reader(collection.toArray(new SimpleFeature[collection.size()]));
    }

    /**
     * Adapt a collection to a reader for use with FeatureStore.setFeatures( reader ).
     *
     * @param collection Collection of SimpleFeature
     * @return FeatureRedaer over the provided contents
     * @throws IOException IOException if there is any problem reading the content.
     */
    public static SimpleFeatureReader reader(FeatureCollection<SimpleFeatureType, SimpleFeature> collection)
            throws IOException {
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        final FeatureIterator<SimpleFeature> it = collection.features();
        return new SimpleFeatureReader() {
            @Override
            public SimpleFeatureType getFeatureType() {
                return collection.getSchema();
            }

            @Override
            public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
                return it.next();
            }

            @Override
            public boolean hasNext() throws IOException {
                return it.hasNext();
            }

            @Override
            public void close() throws IOException {
                it.close();
            }
        };
    }

    /**
     * Copies the provided features into a FeatureCollection.
     *
     * <p>Often used when gathering features for FeatureStore:
     *
     * <pre>
     * <code>
     * featureStore.addFeatures( DataUtilities.collection(array));
     * </code>
     * </pre>
     *
     * @param features Array of features
     * @return FeatureCollection
     */
    public static SimpleFeatureCollection collection(SimpleFeature... features) {
        // JG: There may be some performance to be gained by using ListFeatureCollection here
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        for (SimpleFeature feature : features) {
            collection.add(feature);
        }
        return collection;
    }

    /**
     * Copies the provided features into a FeatureCollection.
     *
     * <p>Often used when gathering a SimpleFeatureCollection into memory.
     *
     * @param featureCollection the features to add to a new feature collection.
     * @return FeatureCollection
     */
    public static DefaultFeatureCollection collection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection) {
        return new DefaultFeatureCollection(featureCollection);
    }

    /**
     * Checks if the provided iterator implements {@link Closeable}.
     *
     * <p>Any problems are logged at {@link Level#FINE}.
     */
    public static void close(Iterator<?> iterator) {
        if (iterator != null && iterator instanceof Closeable) {
            try {
                ((Closeable) iterator).close();
            } catch (IOException e) {
                String name = iterator.getClass().getPackage().toString();
                Logger log = Logger.getLogger(name);
                log.log(Level.FINE, e.getMessage(), e);
            }
        }
    }

    /**
     * Obtain the first feature from the collection as an exemplar.
     *
     * @return first feature from the featureCollection
     */
    public static <F extends Feature> F first(FeatureCollection<?, F> featureCollection) {
        if (featureCollection == null) {
            return null;
        }
        try (FeatureIterator<F> iter = featureCollection.features()) {
            while (iter.hasNext()) {
                F feature = iter.next();
                if (feature != null) {
                    return feature;
                }
            }
            return null; // not found!
        }
    }

    //
    // Conversion (or casting) from general feature model to simple feature model
    //
    /**
     * A safe cast to SimpleFeatureCollection; that will introduce a wrapper if it has to.
     *
     * <p>Please keep the use of this class to a minimum; if you are expecting a
     * FeatureCollection<SimpleFeatureType,SimpleFeature> please make use of SimpleFeatureCollection if you possibly
     * can.
     *
     * <p>So when are you stuck using this class?:
     *
     * <ul>
     *   <li>Offering backwards compatible constructors for legacy code prior to 2.7
     *   <li>implementing FeatureStore.addFeatures(...) since we cannot type narrow parameters
     * </ul>
     *
     * @param featureCollection will be returned as a SimpleFeatureCollection and wrapped only if needed
     * @return SimpleFeatureCollection
     * @since 2.7
     */
    public static SimpleFeatureCollection simple(
            FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection) {
        if (featureCollection instanceof SimpleFeatureCollection) {
            return (SimpleFeatureCollection) featureCollection;
        }
        if (featureCollection.getSchema() instanceof SimpleFeatureType) {
            return new SimpleFeatureCollectionBridge(featureCollection);
        }
        throw new IllegalArgumentException(
                "The provided feature collection contains complex features, cannot be bridged to a simple one");
    }

    public static SimpleFeatureReader simple(FeatureReader<SimpleFeatureType, SimpleFeature> reader) {
        if (reader instanceof SimpleFeatureReader) {
            return (SimpleFeatureReader) reader;
        } else {
            return new SimpleFeatureReaderBrige(reader);
        }
    }

    /**
     * A safe cast to SimpleFeatureSource; that will introduce a wrapper if it has to.
     *
     * <p>Please keep the use of this class to a minimum; if you are expecting a
     * FeatureSource<SimpleFeatureType,SimpleFeature> please make use of SimpleFeatureSource if you possibly can.
     *
     * @since 2.7
     */
    public static SimpleFeatureSource simple(FeatureSource source) {
        if (source instanceof FeatureLocking) {
            return simple((FeatureLocking) source);
        } else if (source instanceof FeatureStore) {
            return simple((FeatureStore) source);
        }

        if (source instanceof SimpleFeatureSource) {
            return (SimpleFeatureSource) source;
        }
        if (source.getSchema() instanceof SimpleFeatureType) {
            @SuppressWarnings("unchecked")
            FeatureSource<SimpleFeatureType, SimpleFeature> cast = source;
            return new SimpleFeatureSourceBridge(cast);
        }
        throw new IllegalArgumentException(
                "The provided feature source contains complex features, cannot be bridged to a simple one");
    }

    /**
     * A safe cast to SimpleFeatureStore; that will introduce a wrapper if it has to.
     *
     * <p>Please keep the use of this class to a minimum; if you are expecting a
     * FeatureStore<SimpleFeatureType,SimpleFeature> please make use of SimpleFeatureStore if you possibly can.
     *
     * @since 2.7
     */
    public static SimpleFeatureStore simple(FeatureStore store) {
        if (store instanceof FeatureLocking) {
            return simple((FeatureLocking) store);
        }

        if (store instanceof SimpleFeatureStore) {
            return (SimpleFeatureStore) store;
        }
        if (store.getSchema() instanceof SimpleFeatureType) {
            @SuppressWarnings("unchecked")
            FeatureStore<SimpleFeatureType, SimpleFeature> cast = store;
            return new SimpleFeatureStoreBridge(cast);
        }
        throw new IllegalArgumentException(
                "The provided feature store contains complex features, cannot be bridged to a simple one");
    }

    /**
     * Go through FeatureType description and convert to a SimpleFeatureType. Also ignores AbstractFeatureType
     * contributions such as name etc...
     *
     * @param featureType FeatureType being converted
     * @return SimpleFeatureType created by stripping any complicated content from the provided featureType
     */
    public static SimpleFeatureType simple(final FeatureType featureType) throws DataSourceException {
        if (featureType == null) {
            return null;
        }
        // go through the attributes and strip out complicated contents
        //
        Collection<PropertyDescriptor> descriptors = featureType.getDescriptors();
        List<PropertyDescriptor> attributes = new ArrayList<>(descriptors);

        List<String> simpleProperties = new ArrayList<>();
        List<AttributeDescriptor> simpleAttributes = new ArrayList<>();

        // HACK HACK!! the parser sets no namespace to the properties so we're
        // doing a hardcode property name black list
        final List<String> ignoreList =
                Arrays.asList(new String[] {"location", "metaDataProperty", "description", "name", "boundedBy"});

        for (Iterator<PropertyDescriptor> it = attributes.iterator(); it.hasNext(); ) {
            PropertyDescriptor property = it.next();
            if (!(property instanceof AttributeDescriptor)) {
                continue;
            }
            AttributeDescriptor descriptor = (AttributeDescriptor) property;
            Name name = descriptor.getName();

            if (ignoreList.contains(name.getLocalPart())) {
                it.remove();
            }
        }
        // / HACK END

        for (PropertyDescriptor descriptor : attributes) {
            Class<?> binding = descriptor.getType().getBinding();
            int maxOccurs = descriptor.getMaxOccurs();
            Name name = descriptor.getName();
            if (Object.class.equals(binding)) {
                continue; // skip complex
            }
            if (maxOccurs > 1) {
                continue; // skip multi value
            }
            if ("http://www.opengis.net/gml".equals(name.getNamespaceURI())) {
                continue; // skip AbstractFeature stuff
            }
            if (descriptor instanceof AttributeDescriptor) {
                AttributeDescriptor attribute = (AttributeDescriptor) descriptor;

                simpleAttributes.add(attribute);
                simpleProperties.add(attribute.getLocalName());
            }
        }

        String[] properties = simpleProperties.toArray(new String[simpleProperties.size()]);
        SimpleFeatureType simpleFeatureType;
        try {
            if (featureType instanceof SimpleFeature) {
                simpleFeatureType = DataUtilities.createSubType((SimpleFeatureType) featureType, properties);
            } else {
                SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
                build.setName(featureType.getName());
                build.setAttributes(simpleAttributes);

                GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
                if (defaultGeometry != null) {
                    build.setDefaultGeometry(defaultGeometry.getLocalName());
                }

                simpleFeatureType = build.buildFeatureType();
            }
        } catch (SchemaException e) {
            throw new DataSourceException(e);
        }
        return simpleFeatureType;
    }

    /**
     * A safe cast to SimpleFeatureLocking; that will introduce a wrapper if it has to.
     *
     * <p>Please keep the use of this class to a minimum; if you are expecting a
     * FeatureLocking<SimpleFeatureType,SimpleFeature> please make use of SimpleFeatureLocking if you possibly can.
     *
     * @since 2.7
     */
    public static SimpleFeatureLocking simple(FeatureLocking locking) {
        if (locking instanceof SimpleFeatureLocking) {
            return (SimpleFeatureLocking) locking;
        }
        if (locking.getSchema() instanceof SimpleFeatureType) {
            @SuppressWarnings("unchecked")
            FeatureLocking<SimpleFeatureType, SimpleFeature> cast = locking;
            return new SimpleFeatureLockingBridge(cast);
        }
        throw new IllegalArgumentException(
                "The provided feature store contains complex features, cannot be bridged to a simple one");
    }
    //
    // FeatureCollection Utility Methods
    //
    /**
     * Copies the provided features into a List.
     *
     * @return List of features copied into memory
     */
    public static <F extends Feature> List<F> list(FeatureCollection<?, F> featureCollection) {
        final ArrayList<F> list = new ArrayList<>();
        try (FeatureIterator<F> iter = featureCollection.features()) {
            while (iter.hasNext()) {
                F feature = iter.next();
                list.add(feature);
            }
        }
        return list;
    }
    /**
     * Copies the provided fetaures into a List.
     *
     * @param maxFeatures Maximum number of features to load
     * @return List of features copied into memory
     */
    public static <F extends Feature> List<F> list(FeatureCollection<?, F> featureCollection, int maxFeatures) {
        final ArrayList<F> list = new ArrayList<>();
        try (FeatureIterator<F> iter = featureCollection.features()) {
            for (int count = 0; iter.hasNext() && count < maxFeatures; count++) {
                F feature = iter.next();
                list.add(feature);
            }
        }
        return list;
    }
    /**
     * Iteator wrapped around the provided FeatureIterator, implementing {@link Closeable}.
     *
     * @see #close(Iterator)
     * @return Iterator wrapped around provided FeatureIterator, implements Closeable
     */
    public static <F extends Feature> Iterator<F> iterator(FeatureIterator<F> featureIterator) {
        return new BridgeIterator<>(featureIterator);
    }

    /**
     * Copies the feature ids from each and every feature into a set.
     *
     * <p>This method can be slurp an in memory record of the contents of a
     */
    public static Set<String> fidSet(FeatureCollection<?, ?> featureCollection) {
        final HashSet<String> fids = new HashSet<>();
        try {
            featureCollection.accepts(
                    feature -> fids.add(feature.getIdentifier().getID()), null);
        } catch (IOException ignore) {
        }
        return fids;
    }
    //
    // Conversion to java.util.Collection
    //
    /**
     * Used to quickly cast to a java.util.Collection.
     *
     * @return Collection
     */
    @SuppressWarnings("unchecked")
    public static <F extends Feature> Collection<F> collectionCast(FeatureCollection<?, F> featureCollection) {
        if (featureCollection instanceof Collection<?>) {
            return (Collection<F>) featureCollection;
        } else {
            throw new IllegalArgumentException("Require access to SimpleFeatureCollection implementing Collecion.add");
        }
    }
    //
    // Conversion to FeatureCollection
    //
    /**
     * Copies the provided features into a FeatureCollection.
     *
     * <p>Often used when gathering a SimpleFeatureCollection into memory.
     *
     * @param list features to add to a new FeatureCollection
     * @return FeatureCollection
     */
    public static SimpleFeatureCollection collection(List<SimpleFeature> list) {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        for (SimpleFeature feature : list) {
            collection.add(feature);
        }
        return collection;
    }

    /**
     * Copies the provided features into a FeatureCollection.
     *
     * <p>Often used when gathering features for FeatureStore:
     *
     * <pre>
     * <code>
     * featureStore.addFeatures( DataUtilities.collection(feature));
     * </code>
     * </pre>
     *
     * @param feature a feature to add to a new collection
     * @return FeatureCollection
     */
    public static SimpleFeatureCollection collection(SimpleFeature feature) {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        collection.add(feature);
        return collection;
    }

    /**
     * Copies the provided reader into a FeatureCollection, reader will be closed.
     *
     * <p>Often used when gathering features for FeatureStore:
     *
     * <pre>
     * <code>
     * featureStore.addFeatures( DataUtilities.collection(reader));
     * </code>
     * </pre>
     *
     * @return FeatureCollection
     */
    public static SimpleFeatureCollection collection(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        try (reader) {
            while (reader.hasNext()) {
                try {
                    collection.add(reader.next());
                } catch (NoSuchElementException e) {
                    throw (IOException) new IOException("EOF").initCause(e);
                } catch (IllegalAttributeException e) {
                    throw (IOException) new IOException().initCause(e);
                }
            }
        }
        return collection;
    }

    /**
     * Copies the provided reader into a FeatureCollection, reader will be closed.
     *
     * <p>Often used when gathering features for FeatureStore:
     *
     * <pre>
     * <code>
     * featureStore.addFeatures( DataUtilities.collection(reader));
     * </code>
     * </pre>
     *
     * @return FeatureCollection
     */
    public static SimpleFeatureCollection collection(SimpleFeatureIterator reader) throws IOException {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        try (reader) {
            while (reader.hasNext()) {
                try {
                    collection.add(reader.next());
                } catch (NoSuchElementException e) {
                    throw (IOException) new IOException("EOF").initCause(e);
                }
            }
        }
        return collection;
    }
    //
    // Attribute Value Utility Methods
    //
    /**
     * Used to compare if two values are equal.
     *
     * <p>This method is here to work around the fact that JTS Geometry requires a specific method to be called rather
     * than object.equals.
     *
     * <p>This method uses:
     *
     * <ul>
     *   <li>Object.equals( Object )
     *   <li>Geometry.equals( Geometry ) - similar to {@link Geometry#equalsExact(Geometry)}
     * </ul>
     *
     * @param att Attribute value
     * @param otherAtt Other value
     * @return True if the values are equal
     */
    public static boolean attributesEqual(Object att, Object otherAtt) {
        if (att == null) {
            if (otherAtt != null) {
                return false;
            }
        } else {
            // Note: for JTS Geometry objects this is equivalent
            // to equalsExact( Geometry )
            if (!att.equals(otherAtt)) {
                return false;
            }
        }

        return true;
    }

    //
    // TypeConversion methods used by FeatureReaders
    //
    /**
     * Create a derived FeatureType
     *
     * @param featureType Original feature type to derive from.
     * @param properties If null, every property of the featureType in input will be used
     * @param override Intended CoordinateReferenceSystem, if null original will be used
     * @return derived FeatureType
     */
    public static SimpleFeatureType createSubType(
            SimpleFeatureType featureType, String[] properties, CoordinateReferenceSystem override)
            throws SchemaException {
        URI namespaceURI = null;
        if (featureType.getName().getNamespaceURI() != null) {
            try {
                namespaceURI = new URI(featureType.getName().getNamespaceURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        return createSubType(featureType, properties, override, featureType.getTypeName(), namespaceURI);
    }
    /**
     * Create a derived FeatureType
     *
     * @param featureType Original feature type to derive from.
     * @param properties If null, every property of the featureType in input will be used
     * @param override Intended CoordinateReferenceSystem, if null original will be used
     * @param typeName Type name override
     * @param namespace Namespace override
     * @return derived FeatureType
     */
    public static SimpleFeatureType createSubType(
            SimpleFeatureType featureType,
            String[] properties,
            CoordinateReferenceSystem override,
            String typeName,
            URI namespace)
            throws SchemaException {

        if ((properties == null) && (override == null)) {
            return featureType;
        }

        if (properties == null) {
            properties = new String[featureType.getAttributeCount()];
            for (int i = 0; i < properties.length; i++) {
                properties[i] = featureType.getDescriptor(i).getLocalName();
            }
        }

        String namespaceURI = namespace != null ? namespace.toString() : null;
        boolean same = featureType.getAttributeCount() == properties.length
                && featureType.getTypeName().equals(typeName)
                && Utilities.equals(featureType.getName().getNamespaceURI(), namespaceURI);

        for (int i = 0; (i < featureType.getAttributeCount()) && same; i++) {
            AttributeDescriptor type = featureType.getDescriptor(i);
            same = type.getLocalName().equals(properties[i])
                    && (((override != null) && type instanceof GeometryDescriptor)
                            ? assertEquals(override, ((GeometryDescriptor) type).getCoordinateReferenceSystem())
                            : true);
        }

        if (same) {
            return featureType;
        }

        AttributeDescriptor[] types = new AttributeDescriptor[properties.length];

        for (int i = 0; i < properties.length; i++) {
            types[i] = featureType.getDescriptor(properties[i]);

            if ((override != null) && types[i] instanceof GeometryDescriptor) {
                AttributeTypeBuilder ab = new AttributeTypeBuilder();
                ab.init(types[i]);
                ab.setCRS(override);
                types[i] = ab.buildDescriptor(types[i].getLocalName(), ab.buildGeometryType());
            }
        }

        if (typeName == null) typeName = featureType.getTypeName();
        if (namespace == null && featureType.getName().getNamespaceURI() != null)
            try {
                namespace = new URI(featureType.getName().getNamespaceURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(typeName);
        tb.setNamespaceURI(namespace);
        tb.setCRS(null); // not interested in warnings from this simple method
        tb.addAll(types);
        setDefaultGeometry(tb, properties, featureType);
        return tb.buildFeatureType();
    }

    private static boolean assertEquals(Object o1, Object o2) {
        return o1 == null && o2 == null ? true : (o1 != null ? o1.equals(o2) : false);
    }

    /**
     * Create a type limited to the named properties provided.
     *
     * @return type limited to the named properties provided
     */
    public static SimpleFeatureType createSubType(SimpleFeatureType featureType, String... properties)
            throws SchemaException {
        if (properties == null) {
            return featureType;
        }

        boolean same = featureType.getAttributeCount() == properties.length;

        for (int i = 0; (i < featureType.getAttributeCount()) && same; i++) {
            same = featureType.getDescriptor(i).getLocalName().equals(properties[i]);
        }

        if (same) {
            return featureType;
        }

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(featureType.getName());
        tb.setCRS(null); // not interested in warnings from this simple method
        for (String property : properties) {
            // let's get the attribute descriptor corresponding to the current property
            AttributeDescriptor attributeDescriptor = featureType.getDescriptor(property);
            if (attributeDescriptor != null) {
                // if the property doesn't map to an attribute descriptor we ignore it
                // an attribute descriptor may be omitted for security proposes for example
                tb.add(attributeDescriptor);
            }
        }
        setDefaultGeometry(tb, properties, featureType);
        return tb.buildFeatureType();
    }

    private static void setDefaultGeometry(
            SimpleFeatureTypeBuilder typeBuilder, String[] properties, SimpleFeatureType featureType) {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        if (geometryDescriptor != null) {
            String propertyName = geometryDescriptor.getLocalName();
            if (Arrays.asList(properties).contains(propertyName)) {
                typeBuilder.setDefaultGeometry(propertyName);
            }
        }
    }
    //
    // Decoding (ie Parsing) support for PropertyAttributeReader and tutorials
    //
    /**
     * Utility method for FeatureType construction.
     *
     * <p>Will parse a String of the form: <i>"name:Type,name2:Type2,..."</i>
     *
     * <p>Where <i>Type</i> is defined by {@link createAttribute}:
     *
     * <ul>
     *   <li>Each attribute descriptor is defined using the form: <i>"name:Type:hint"</i>
     *   <li>Where <i>Type</i> is:
     *       <ul>
     *         <li>0,Interger,int: represents Interger
     *         <li>0.0, Double, double: represents Double
     *         <li>"",String,string: represents String
     *         <li>Geometry: represents Geometry, additional common geometry types supported including
     *             Point,LineString,Polygon,MultiLineString,MultiPolygon,MultiPoint,GeometryCollection
     *       </ul>
     *   <li>UUID
     *   <li>Date
     *   <li><i>full.class.path</i>: represents java type
     * </ul>
     *
     * <li>Where <i>hint</i> is "hint1;hint2;...;hintN", in which "hintN" is one of:
     *
     *     <ul>
     *       <li><code>nillable</code>
     *       <li><code>authority=text</code>
     *       <li><code>srid=number</code>
     *     </ul>
     *
     * <li>You may indicate the default Geometry with an astrix: "*geom:Geometry".
     * <li>You may also indicate the srid (used to look up a EPSG code): "*point:Point:3226"
     * <li>The CRS authority can be specified using the <code>authority</code> hint, if it's not EPSG (the default):
     *     "*point:Point:authority=IAU;srid=49900"
     *
     *     <p>Examples:
     *
     *     <ul>
     *       <li><code>name:"",age:0,geom:Geometry,centroid:Point,url:java.io.URL"</code>
     *       <li><code>id:String,polygonProperty:Polygon:srid=32615</code>
     *       <li><code>id:String,polygonProperty:Polygon:authority=IAU;srid=1000</code>
     *       <li><code>identifier:UUID,location:Point,*area:MultiPolygon,created:Date</code>
     *       <li><code>uuid:UUID,name:String,description:String,time:java.sql.Timestamp</code>
     *     </ul>
     *
     * @param typeName identification of FeatureType: (<i>namesapce</i>).<i>typeName</i>
     * @param typeSpec Specification of FeatureType attributes "name:Type,name2:Type2,..."
     */
    public static SimpleFeatureType createType(String typeName, String typeSpec) throws SchemaException {
        int split = typeName.lastIndexOf('.');
        String namespace = (split == -1) ? null : typeName.substring(0, split);
        String name = (split == -1) ? typeName : typeName.substring(split + 1);

        return createType(namespace, name, typeSpec);
    }

    /**
     * Utility method for FeatureType construction.
     *
     * <p>
     *
     * @param namespace Typename namespace used to qualify the provided name
     * @param name Typename name, as qualified by namespace
     * @param typeSpec Definition of attributes, for details see {@link #createType(String, String)}
     */
    public static SimpleFeatureType createType(String namespace, String name, String typeSpec) throws SchemaException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(name);
        builder.setNamespaceURI(namespace);

        String[] types = typeSpec.split(",");

        AttributeDescriptor attributeType;
        builder.setCRS(null); // not interested in warnings from this simple method
        for (int i = 0; i < types.length; i++) {
            boolean defaultGeometry = types[i].startsWith("*");
            if (types[i].startsWith("*")) {
                types[i] = types[i].substring(1);
            }

            attributeType = createAttribute(types[i]);
            builder.add(attributeType);

            if (defaultGeometry) {
                builder.setDefaultGeometry(attributeType.getLocalName());
            }
        }

        return builder.buildFeatureType();
    }

    //
    // Encoding support for PropertyFeatureWriter
    //
    /**
     * Encode the provided featureType as a String suitable for use with {@link #createType}.
     *
     * <p>The format of this string acts as the "header" information for the PropertyDataStore implementations.
     *
     * <p>Example:
     *
     * <pre><code>String header = "_="+DataUtilities.encodeType(schema);</code></pre>
     *
     * <p>For more information please review the PropertyDataStore tutorials.
     *
     * @return String representation of featureType suitable for use with {@link #createType}
     */
    public static String encodeType(SimpleFeatureType featureType) {
        Collection<PropertyDescriptor> types = featureType.getDescriptors();
        StringBuffer buf = new StringBuffer();

        for (PropertyDescriptor type : types) {
            buf.append(type.getName().getLocalPart());
            buf.append(":");
            buf.append(typeMap(type.getType().getBinding()));
            if (type instanceof GeometryDescriptor) {
                GeometryDescriptor gd = (GeometryDescriptor) type;
                Map.Entry<Citation, Integer> code = lookupCode(gd.getCoordinateReferenceSystem());
                if (code != null) {
                    Citation authority = code.getKey();
                    Integer srid = code.getValue();
                    if (authority == null || authority.getIdentifiers().isEmpty() || isEPSG(authority)) {
                        buf.append(":srid=" + srid);
                    } else {
                        String authorityCode =
                                authority.getIdentifiers().iterator().next().getCode();
                        buf.append(":authority=" + authorityCode + ";srid=" + srid);
                    }
                }
            }
            buf.append(",");
        }
        buf.delete(buf.length() - 1, buf.length()); // remove last ","
        return buf.toString();
    }

    private static boolean isEPSG(Citation authority) {
        // straightforward but does not always work
        if (Citations.EPSG.equals(authority)) return true;
        // a bit more laborious but should work
        return authority.getIdentifiers().stream().anyMatch(id -> id.getCode().equals("EPSG"));
    }

    /**
     * Quickly review provided crs checking for an "EPSG:SRID" reference identifier.
     *
     * <p>
     *
     * @see CRS#lookupEpsgCode(CoordinateReferenceSystem, boolean) for full search
     * @return an authority/srid pair, or null if not found
     */
    private static Map.Entry<Citation, Integer> lookupCode(CoordinateReferenceSystem crs) {
        Set<ReferenceIdentifier> identifiers;
        if (crs == null || (identifiers = crs.getIdentifiers()) == null || identifiers.isEmpty()) {
            return null; // not found
        }
        // search for EPSG:#### code, if there is one
        for (ReferenceIdentifier id : identifiers) {
            Citation authority = id.getAuthority();
            if (authority != null && authority.getTitle().equals(Citations.EPSG.getTitle())) {
                try {
                    return Map.entry(authority, Integer.parseInt(id.getCode()));
                } catch (NumberFormatException nanException) {
                    continue;
                }
            }
        }
        // otherwise pick the first authority available
        ReferenceIdentifier id = identifiers.iterator().next();
        return Map.entry(id.getAuthority(), Integer.parseInt(id.getCode()));
    }

    /**
     * Reads in SimpleFeature that has been encoded into a line of text.
     *
     * <p>Example:
     *
     * <pre>
     * SimpleFeatureType featureType =
     *    DataUtilities.createType("FLAG","id:Integer|name:String|geom:Geometry:4326");
     *
     * SimpleFeature feature =
     *    DataUtilities.createFeature( featureType, "fid1=1|Jody Garnett\\nSteering Committee|POINT(1,2)" );
     * </pre>
     *
     * This format is used by the PropertyDataStore tutorials. It amounts to:
     *
     * <ul>
     *   <li>
     *   <li>Encoding of <i>FeatureId</i> followed by the attributes
     *   <li>Attributes are seperated using the bar character
     *   <li>Geometry is handled using {@link WKTReader2}
     *   <li>Support for common escaped characters
     *   <li>Multi-line support using escaped line-feed characters
     *       <ul>
     *
     * @return SimpleFeature defined by the provided line of text
     */
    public static SimpleFeature createFeature(SimpleFeatureType featureType, String line) {
        String fid;

        int fidSplit = line.indexOf('=');
        int barSplit = line.indexOf('|');
        if (fidSplit == -1 || (barSplit != -1 && barSplit < fidSplit)) {
            fid = null; // we need to generate a feature id
        } else {
            fid = line.substring(0, fidSplit);
        }
        String data = line.substring(fidSplit + 1);
        String[] text = splitIntoText(data, featureType);
        Object[] values = new Object[text.length];
        for (int i = 0; i < text.length; i++) {
            AttributeDescriptor descriptor = featureType.getDescriptor(i);
            Object value = createValue(descriptor, text[i]);
            values[i] = value;
        }
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, values, fid);

        return feature;
    }

    /**
     * Split the provided text using | charater as a seperator.
     *
     * <p>This method respects the used of \ to "escape" a | character allowing representations such as the following:
     *
     * <pre>
     * String example="text|example of escaped \\| character|text";
     *
     * // represents: "text|example of escaped \| character|text"
     * String split=splitIntoText( example );</pre>
     *
     * @param data Origional raw text as stored
     * @return data split using | as seperator
     * @throws DataSourceException if the information stored is inconsistent with the headered provided
     */
    private static String[] splitIntoText(String data, SimpleFeatureType type) {
        // return data.split("|", -1); // use -1 as a limit to include empty trailing spaces
        // return data.split("[.-^\\\\]\\|",-1); //use -1 as limit to include empty trailing spaces

        String[] text = new String[type.getAttributeCount()];
        int i = 0;
        StringBuilder item = new StringBuilder();
        for (String str : data.split("\\|", -1)) {
            if (i == type.getAttributeCount()) {
                // limit reached
                throw new IllegalArgumentException("format error: expected "
                        + text.length
                        + " attributes, stopped after finding "
                        + i
                        + ". ["
                        + data
                        + "] split into "
                        + Arrays.asList(text));
            }
            if (str.endsWith("\\")) {
                item.append(str);
                item.append("|");
            } else {
                item.append(str);
                text[i] = item.toString();
                i++;
                item = new StringBuilder();
            }
        }
        if (i < type.getAttributeCount()) {
            throw new IllegalArgumentException("createFeature format error: expected "
                    + type.getAttributeCount()
                    + " attributes, but only found "
                    + i
                    + ". ["
                    + data
                    + "] split into "
                    + Arrays.asList(text));
        }
        return text;
    }
    /**
     * Reads an attribute value out of the raw text supplied to {@link #createFeature}.
     *
     * <p>This method is responsible for:
     *
     * <ul>
     *   <li>Handling: <code>"<null>"</code> as an explicit marker flag for a null value
     *   <li>Using {@link #decodeEscapedCharacters(String)} to unpack the raw text
     *   <li>Using {@link Converters} to convert the text to the requested value
     */
    private static Object createValue(AttributeDescriptor descriptor, String rawText) {
        String stringValue = null;
        try {
            // read the value and decode any interesting characters
            stringValue = decodeEscapedCharacters(rawText);
        } catch (RuntimeException huh) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", huh);
            stringValue = null;
        }
        // check for special <null> flag
        if ("<null>".equals(stringValue)) {
            stringValue = null;
        }
        if (stringValue == null) {
            if (descriptor.isNillable()) {
                return null; // it was an explicit "<null>"
            }
        }
        // Use of Converters to convert from String to requested java binding
        Object value = Converters.convert(stringValue, descriptor.getType().getBinding());

        if (descriptor.getType() instanceof GeometryType) {
            // this is to be passed on in the geometry objects so the srs name gets encoded
            CoordinateReferenceSystem crs = ((GeometryType) descriptor.getType()).getCoordinateReferenceSystem();
            if (crs != null) {
                // must be geometry, but check anyway
                if (value != null && value instanceof Geometry) {
                    ((Geometry) value).setUserData(crs);
                }
            }
        }
        return value;
    }
    /**
     * Produce a String encoding of SimpleFeature for use with {@link #createFeature}.
     *
     * <p>This method inlcudes the full featureId information.
     *
     * @param feature feature to encode, only SimpleFeature is supported at this time
     * @return text encoding for use with {@link #createFeature}
     */
    public static String encodeFeature(SimpleFeature feature) {
        return encodeFeature(feature, true);
    }
    /**
     * Produce a String encoding of SimpleFeature for use with {@link #createFeature}.
     *
     * <p>This method inlcudes the full featureId information.
     *
     * @param feature feature to encode, only SimpleFeature is supported at this time
     * @param includeFid true to include the optional feature id
     * @return text encoding for use with {@link #createFeature}
     */
    public static String encodeFeature(SimpleFeature feature, boolean includeFid) {
        StringBuilder build = new StringBuilder();
        if (includeFid) {
            String fid = feature.getID();
            if (feature.getUserData().containsKey(Hints.PROVIDED_FID)) {
                fid = (String) feature.getUserData().get(Hints.PROVIDED_FID);
            }
            build.append(fid);
            build.append("=");
        }
        for (int i = 0; i < feature.getAttributeCount(); i++) {
            Object attribute = feature.getAttribute(i);
            if (i != 0) {
                build.append("|"); // seperator character
            }
            if (attribute == null) {
                build.append("<null>"); // nothing!
            } else if (attribute instanceof String) {
                String txt = encodeString((String) attribute);
                build.append(txt);
            } else if (attribute instanceof Geometry) {
                Geometry geometry = (Geometry) attribute;
                String txt = geometry.toText();

                txt = encodeString(txt);
                build.append(txt);
            } else {
                String txt = Converters.convert(attribute, String.class);
                if (txt == null) { // could not convert?
                    txt = attribute.toString();
                }
                txt = encodeString(txt);
                build.append(txt);
            }
        }
        return build.toString();
    }

    /**
     * Uses {@link Converters} to parse the provided text into the correct values to create a feature.
     *
     * @param type FeatureType
     * @param fid Feature ID for new feature
     * @param text Text representation of values
     * @return newly created feature
     */
    public static SimpleFeature parse(SimpleFeatureType type, String fid, String... text)
            throws IllegalAttributeException {
        Object[] attributes = new Object[text.length];

        for (int i = 0; i < text.length; i++) {
            AttributeType attType = type.getDescriptor(i).getType();
            attributes[i] = Converters.convert(text[i], attType.getBinding());
        }
        return SimpleFeatureBuilder.build(type, attributes, fid);
    }

    //
    // Internal utility methods to support PropertyDataStore feature encoding / decoding
    //
    /**
     * Used to decode common whitespace chracters and escaped | characters.
     *
     * @param txt Origional raw text as stored
     * @return decoded text as provided for storage
     * @see PropertyAttributeWriter#encodeString(String)
     */
    private static String decodeEscapedCharacters(String txt) {
        // unpack whitespace constants
        txt = txt.replace("\\n", "\n");
        txt = txt.replaceAll("\\r", "\r");

        // unpack our our escaped characters
        txt = txt.replace("\\|", "|");
        // txt = txt.replace("\\\\", "\\" );

        return txt;
    }

    /**
     * Used to encode common whitespace characters and | character for safe transport.
     *
     * @return txt encoded for storage
     * @see PropertyAttributeReader#decodeString(String)
     */
    private static String encodeString(String txt) {
        // encode our escaped characters
        // txt = txt.replace("\\", "\\\\");
        txt = txt.replace("|", "\\|");

        // encode whitespace constants
        txt = txt.replace("\n", "\\n");
        txt = txt.replace("\r", "\\r");

        return txt;
    }
    /**
     * Internal method to access java binding using readable typename.
     *
     * @see #createType(String, String)
     */
    static Class<?> type(String typeName) throws ClassNotFoundException {
        if (typeMap.containsKey(typeName)) {
            return typeMap.get(typeName);
        }
        return Class.forName(typeName);
    }
    /**
     * Internal method to access the readable typename for the provided class.
     *
     * @see #createType(String, String)
     */
    static String typeMap(Class<?> type) {
        if (typeEncode.containsKey(type)) {
            return typeEncode.get(type);
        }
        return type.getName();
    }
    //
    // Query Support Methods for DataStore implementators
    //
    /**
     * Factory method to produce Comparator based on provided Query SortBy information.
     *
     * <p>This method handles:
     *
     * <ul>
     *   <li>{@link SortBy#NATURAL_ORDER}: As sorting by FeatureID
     * </ul>
     *
     * @return Comparator suitable for use with Arrays.sort( SimpleFeature[], comparator )
     */
    @SuppressWarnings("unchecked")
    public static Comparator<SimpleFeature> sortComparator(SortBy sortBy) {
        if (sortBy == null) {
            sortBy = SortBy.NATURAL_ORDER;
        }
        if (sortBy == SortBy.NATURAL_ORDER) {
            return (f1, f2) -> f1.getID().compareTo(f2.getID());
        } else if (sortBy == SortBy.REVERSE_ORDER) {
            return (f1, f2) -> {
                int compare = f1.getID().compareTo(f2.getID());
                return -compare;
            };
        } else {
            final PropertyName propertyName = sortBy.getPropertyName();
            final SortOrder sortOrder = sortBy.getSortOrder();
            return (f1, f2) -> {
                Object value1 = propertyName.evaluate(f1, Comparable.class);
                Object value2 = propertyName.evaluate(f2, Comparable.class);
                if (value1 == null || value2 == null) {
                    return 0; // cannot perform comparison
                }
                if (value1 instanceof Comparable && value1.getClass().isInstance(value2)) {
                    if (sortOrder == SortOrder.ASCENDING) {
                        return ((Comparable<Object>) value1).compareTo(value2);
                    } else {
                        return ((Comparable<Object>) value2).compareTo(value1);
                    }
                } else {
                    return 0; // cannot perform comparison
                }
            };
        }
    }

    /**
     * Takes two {@link Query}objects and produce a new one by mixing the restrictions of both of them.
     *
     * <p>The policy to mix the queries components is the following:
     *
     * <ul>
     *   <li>typeName: type names MUST match (not checked if some or both queries equals to <code>
     *       Query.ALL</code>)
     *   <li>handle: you must provide one since no sensible choice can be done between the handles of both queries
     *   <li>maxFeatures: the lower of the two maxFeatures values will be used (most restrictive)
     *   <li>attributeNames: the attributes of both queries will be joined in a single set of attributes. IMPORTANT:
     *       only <b><i>explicitly</i></b> requested attributes will be joint, so, if the method <code>
     *       retrieveAllProperties()</code> of some of the queries returns <code>true</code> it does not means that all
     *       the properties will be joined. You must create the query with the names of the properties you want to load.
     *   <li>filter: the filters of both queries are or'ed, then simplified using SimplifiyingFilterVisitor
     *   <li>sort: if the second query has a sorting it's used for output, otherwise the one of the first is used
     *   <li><b>any other query property is ignored</b> and no guarantees are made of their return values, so client
     *       code shall explicitly care of hints, startIndex, etc., if needed.
     * </ul>
     *
     * @param firstQuery Query against this DataStore
     * @return Query restricted to the limits of definitionQuery
     * @throws NullPointerException if some of the queries is null
     * @throws IllegalArgumentException if the type names of both queries do not match
     */
    public static Query mixQueries(Query firstQuery, Query secondQuery, String handle) {
        if ((firstQuery == null) && (secondQuery == null)) {
            // throw new NullPointerException("Cannot combine two null queries");
            return Query.ALL;
        }
        if (firstQuery == null || firstQuery.equals(Query.ALL)) {
            return secondQuery;
        } else if (secondQuery == null || secondQuery.equals(Query.ALL)) {
            return firstQuery;
        }
        if ((firstQuery.getTypeName() != null) && (secondQuery.getTypeName() != null)) {
            if (!firstQuery.getTypeName().equals(secondQuery.getTypeName())) {
                String msg =
                        "Type names do not match: " + firstQuery.getTypeName() + " != " + secondQuery.getTypeName();
                throw new IllegalArgumentException(msg);
            }
        }

        // mix versions, if possible
        String version;
        if (firstQuery.getVersion() != null) {
            if (secondQuery.getVersion() != null && !secondQuery.getVersion().equals(firstQuery.getVersion()))
                throw new IllegalArgumentException("First and second query refer different versions");
            version = firstQuery.getVersion();
        } else {
            version = secondQuery.getVersion();
        }

        // none of the queries equals Query.ALL, mix them
        // use the more restrictive max features field
        int maxFeatures = Math.min(firstQuery.getMaxFeatures(), secondQuery.getMaxFeatures());

        // join attributes names
        List<PropertyName> propNames = joinAttributes(firstQuery.getProperties(), secondQuery.getProperties());

        // join filters
        Filter filter = firstQuery.getFilter();
        Filter filter2 = secondQuery.getFilter();

        if ((filter == null) || filter.equals(Filter.INCLUDE)) {
            filter = filter2;
        } else if ((filter2 != null) && !filter2.equals(Filter.INCLUDE)) {
            filter = ff.and(filter, filter2);
        }
        filter = SimplifyingFilterVisitor.simplify(filter);
        Integer start = 0;
        if (firstQuery.getStartIndex() != null) {
            start = firstQuery.getStartIndex();
        }
        if (secondQuery.getStartIndex() != null) {
            start += secondQuery.getStartIndex();
        }
        // collect all hints
        Hints hints = new Hints();
        if (firstQuery.getHints() != null) {
            hints.putAll(firstQuery.getHints());
        }
        if (secondQuery.getHints() != null) {
            hints.putAll(secondQuery.getHints());
        }
        // build the mixed query
        String typeName = firstQuery.getTypeName() != null ? firstQuery.getTypeName() : secondQuery.getTypeName();
        // check the sort
        SortBy[] sort;
        if (secondQuery.getSortBy() != null && secondQuery.getSortBy().length > 0) {
            sort = secondQuery.getSortBy();
        } else {
            sort = firstQuery.getSortBy();
        }

        Query mixed = new Query(typeName, filter, maxFeatures, propNames, handle);
        mixed.setVersion(version);
        mixed.setHints(hints);
        mixed.setSortBy(sort);
        if (start != 0) {
            mixed.setStartIndex(start);
        }
        return mixed;
    }

    /** This method changes the query object by simplifying the filter using SimplifyingFilterVisitor */
    public static Query simplifyFilter(Query query) {
        if (query == null || query == Query.ALL) {
            return query;
        }
        Filter filter = SimplifyingFilterVisitor.simplify(query.getFilter());
        if (filter.equals(query.getFilter())) {
            return query;
        }
        Query result = new Query(query);
        result.setFilter(filter);
        return result;
    }

    /**
     * This method changes the query object so that all propertyName references are resolved to simple attribute names
     * against the schema of the feature source.
     *
     * <p>For example, this method ensures that propertyName's such as "gml:name" are rewritten as simply "name". This
     * method will not rewrite empty PropertyNames.
     */
    public static Query resolvePropertyNames(Query query, SimpleFeatureType schema) {
        Filter resolved = resolvePropertyNames(query.getFilter(), schema);
        if (resolved == query.getFilter()) {
            return query;
        }
        Query newQuery = new Query(query);
        newQuery.setFilter(resolved);
        return newQuery;
    }

    /** Transform provided filter; resolving property names */
    public static Filter resolvePropertyNames(Filter filter, SimpleFeatureType schema) {
        if (filter == null || filter == Filter.INCLUDE || filter == Filter.EXCLUDE) {
            return filter;
        }
        return (Filter) filter.accept(new PropertyNameResolvingVisitor(schema), null);
    }

    /**
     * Creates a set of attribute names from the two input lists of names, maintaining the order of the first list and
     * appending the non repeated names of the second.
     *
     * <p>In the case where both lists are <code>null</code>, <code>null</code> is returned.
     *
     * @param atts1 the first list of attribute names, who's order will be maintained
     * @param atts2 the second list of attribute names, from wich the non repeated names will be appended to the
     *     resulting list
     * @return Set of attribute names from <code>atts1</code> and <code>atts2</code>
     */
    private static List<PropertyName> joinAttributes(List<PropertyName> atts1, List<PropertyName> atts2) {

        if (atts1 == null && atts2 == null) {
            return null;
        }

        List<PropertyName> atts = new LinkedList<>();

        if (atts1 != null) {
            atts.addAll(atts1);
        }

        if (atts2 != null) {
            for (PropertyName propertyName : atts2) {
                if (!atts.contains(propertyName)) {
                    atts.add(propertyName);
                }
            }
        }
        return atts;
    }

    /**
     * Returns a list of properties of a simple feature type, including all properties from a given list, and all
     * mandatory (minoccurs > 0) added.
     *
     * @param type feature type
     * @param oldProps given list of properties
     * @return list of properties including all mandatory properties
     */
    public static List<PropertyName> addMandatoryProperties(SimpleFeatureType type, List<PropertyName> oldProps) {
        Iterator<PropertyDescriptor> ii = type.getDescriptors().iterator();

        List<PropertyName> properties = new ArrayList<>();

        while (ii.hasNext()) {

            PropertyDescriptor descr = ii.next();
            PropertyName propName = ff.property(descr.getName());

            if (oldProps != null && oldProps.contains(propName)) {
                properties.add(propName);
            } else if (((descr.getMinOccurs() > 0) && (descr.getMaxOccurs() != 0))) {
                // mandatory, add it
                properties.add(propName);
            }
        }

        return properties;
    }

    /**
     * Generate AttributeDescriptor based on String type specification (based on UML).
     *
     * <p>Will parse a String of the form: <i>"name:Type:hint" as described in {@link #createType}</i>
     *
     * @see #createType
     * @throws SchemaException If typeSpect could not be interpreted
     */
    static AttributeDescriptor createAttribute(String typeSpec) throws SchemaException {
        int split = typeSpec.indexOf(":");

        String name;
        String type;
        String hint = null;

        if (split == -1) {
            name = typeSpec;
            type = "String";
        } else {
            name = typeSpec.substring(0, split);

            int split2 = typeSpec.indexOf(":", split + 1);

            if (split2 == -1) {
                type = typeSpec.substring(split + 1);
            } else {
                type = typeSpec.substring(split + 1, split2);
                hint = typeSpec.substring(split2 + 1);
            }
        }

        try {
            boolean nillable = true;
            CoordinateReferenceSystem crs = null;

            int srid = -1;
            String authority = "EPSG";
            if (hint != null) {
                StringTokenizer st = new StringTokenizer(hint, ";");
                while (st.hasMoreTokens()) {
                    String h = st.nextToken();
                    h = h.trim();

                    // nillable?
                    // JD: i am pretty sure this hint is useless since the
                    // default is to make attributes nillable
                    if (h.equals("nillable")) {
                        nillable = true;
                    }
                    if (h.startsWith("authority=")) {
                        authority = h.split("=")[1];
                    }
                    // spatial reference identifier
                    if (h.startsWith("srid=")) {
                        String value = h.split("=")[1];
                        srid = Integer.parseInt(value);
                    }
                }
            }
            if (srid != -1) {
                try {
                    crs = CRS.decode(authority + ":" + srid);
                } catch (Exception e) {
                    String msg = "Error decoding srs: " + srid;
                    throw new SchemaException(msg, e);
                }
            }

            Class<?> clazz = type(type);
            if (Geometry.class.isAssignableFrom(clazz)) {
                GeometryType at = new GeometryTypeImpl(
                        new NameImpl(name), clazz, crs, false, false, Collections.emptyList(), null, null);
                return new GeometryDescriptorImpl(at, new NameImpl(name), 0, 1, nillable, null);
            } else {
                AttributeType at = new AttributeTypeImpl(
                        new NameImpl(name), clazz, false, false, Collections.emptyList(), null, null);
                return new AttributeDescriptorImpl(at, new NameImpl(name), 0, 1, nillable, null);
            }
        } catch (ClassNotFoundException e) {
            throw new SchemaException("Could not type " + name + " as:" + type, e);
        }
    }

    /**
     * Manually count the number of features from the provided FeatureIterator. This implementation is intended for
     * FeatureCollection implementors and test case verification. Client code should always call
     * {@link FeatureCollection#size()}
     *
     * @return number of featuers in feature collection
     */
    public static int count(FeatureIterator<?> iterator) {
        int count = 0;
        if (iterator != null) {
            try (iterator) {
                while (iterator.hasNext()) {
                    iterator.next();
                    count++;
                }
                return count;
            }
        }
        return count;
    }
    /**
     * Manually count the number of features in a feature collection using using {@link FeatureCollection#features()}.
     *
     * <p>This implementation is intended for FeatureCollection implementors and test case verification. Client code
     * should always call {@link FeatureCollection#size()}
     *
     * @return number of featuers in feature collection
     */
    public static int count(FeatureCollection<? extends FeatureType, ? extends Feature> collection) {
        int count = 0;
        try (FeatureIterator<? extends Feature> i = collection.features()) {
            while (i.hasNext()) {
                @SuppressWarnings("unused")
                Feature feature = i.next();
                count++;
            }
            return count;
        }
    }
    /**
     * Manually calculate the bounds from the provided FeatureIteator. This implementation is intended for
     * FeatureCollection implementors and test case verification. Client code should always call
     * {@link FeatureCollection#getBounds()}.
     */
    public static ReferencedEnvelope bounds(FeatureIterator<?> iterator) {
        if (iterator == null) {
            return null;
        }
        try (iterator) {
            ReferencedEnvelope bounds = null;
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                ReferencedEnvelope featureEnvelope = null;
                if (feature != null && feature.getBounds() != null) {
                    featureEnvelope = ReferencedEnvelope.reference(feature.getBounds());
                }

                if (featureEnvelope != null) {
                    if (bounds == null) {
                        bounds = new ReferencedEnvelope(featureEnvelope);
                    } else {
                        bounds.expandToInclude(featureEnvelope);
                    }
                }
            }
            return bounds;
        }
    }
    /**
     * Manually calculates the bounds of a feature collection using {@link FeatureCollection#features()}.
     *
     * <p>This implementation is intended for FeatureCollection implementors and test case verification. Client code
     * should always call {@link FeatureCollection#getBounds()}.
     *
     * @return bounds of features in feature collection
     */
    public static ReferencedEnvelope bounds(FeatureCollection<? extends FeatureType, ? extends Feature> collection) {
        try (FeatureIterator<? extends Feature> i = collection.features()) {
            // Implementation taken from DefaultFeatureCollection implementation - thanks IanS
            CoordinateReferenceSystem crs = collection.getSchema().getCoordinateReferenceSystem();
            ReferencedEnvelope bounds = new ReferencedEnvelope(crs);

            while (i.hasNext()) {
                Feature feature = i.next();
                if (feature == null) continue;

                BoundingBox geomBounds = feature.getBounds();
                // IanS - as of 1.3, JTS expandToInclude ignores "null" Envelope
                // and simply adds the new bounds...
                // This check ensures this behavior does not occur.
                if (geomBounds != null && !geomBounds.isEmpty()) {
                    bounds.include(geomBounds);
                }
            }
            return bounds;
        }
    }

    /**
     * Manually visit each feature using {@link FeatureCollection#features()}.
     *
     * <p>This method is intended to assist FeatureCollection implementors, and used to verify test-case results. Client
     * code should always call {@link FeatureCollection#accepts(FeatureVisitor, ProgressListener)}
     */
    public static void visit(FeatureCollection<?, ?> collection, FeatureVisitor visitor, ProgressListener progress)
            throws IOException {

        float size = progress != null ? collection.size() : 0;
        if (progress == null) {
            progress = new NullProgressListener();
        }
        float position = 0;
        progress.started();
        try (FeatureIterator<?> iterator = collection.features()) {
            while (!progress.isCanceled() && iterator.hasNext()) {
                Feature feature = null;
                try {
                    feature = iterator.next();
                    visitor.visit(feature);
                    if (size > 0) {
                        progress.progress(position++ / size);
                    }
                } catch (Exception erp) {
                    progress.exceptionOccurred(erp);
                    String fid = feature == null
                            ? "feature"
                            : feature.getIdentifier().toString();
                    throw new IOException("Problem with " + collection.getID() + " visiting " + fid + ":" + erp, erp);
                }
            }
        } finally {
            progress.complete();
        }
    }

    /**
     * Checks that a {@link File} is a real file, exists and is readable.
     *
     * @param file the {@link File} instance to check. Must not be null.
     * @param logger an optional {@link Logger} (can be null) where to log detailed info about the file properties
     *     (path/readable/hidden/writable)
     * @return {@code true} in case the file is a real file, exists and is readable; {@code false} otherwise.
     */
    public static boolean checkFileReadable(final File file, final Logger logger) {
        if (logger != null && logger.isLoggable(Level.FINE)) {
            final StringBuilder builder = new StringBuilder("Checking file:")
                    .append(file.getAbsolutePath())
                    .append("\n")
                    .append("canRead:")
                    .append(file.canRead())
                    .append("\n")
                    .append("isHidden:")
                    .append(file.isHidden())
                    .append("\n")
                    .append("isFile")
                    .append(file.isFile())
                    .append("\n")
                    .append("canWrite")
                    .append(file.canWrite())
                    .append("\n");
            logger.fine(builder.toString());
        }
        if (!file.exists() || !file.canRead() || !file.isFile()) return false;
        return true;
    }

    /**
     * Checks that the provided directory path refers to an existing/readable directory. Finally, return it as a
     * normalized directory path (removing double and single dot path steps if any) followed by the separator char if
     * missing ({@code '/'} On UNIX systems; {@code '\\} on Microsoft Windows systems.
     *
     * @param file the input file. Must not be null.
     * @return the re-formatted directory path.
     * @throws IllegalArgumentException in case the specified path doesn't rely on a existing/readable directory.
     */
    public static File checkDirectory(File file) throws IllegalArgumentException {
        String directoryPath = file.getPath();
        File inDir = file;
        if (!inDir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directoryPath);
        }
        if (!inDir.canRead()) {
            throw new IllegalArgumentException("Not a writable directory: " + directoryPath);
        }
        try {
            directoryPath = inDir.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        /*
         * directoryPath = FilenameUtils.normalize(directoryPath); if
         * (!directoryPath.endsWith(File.separator)){ directoryPath = directoryPath +
         * File.separator; }
         */
        // test to see if things are still good
        inDir = new File(directoryPath);
        if (!inDir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directoryPath);
        }
        if (!inDir.canRead()) {
            throw new IllegalArgumentException("Not a writable directory: " + directoryPath);
        }
        return new File(directoryPath);
    }

    /**
     * Verifies a Map of parameters against the Param information. Primarily used by classes implementing
     * DataAcessFactory.
     *
     * <p>It will ensure that:
     *
     * <ul>
     *   <li>params is not null
     *   <li>Everything is of the correct type (or upcovertable to the correct type without Error)
     *   <li>Required Parameters are present
     * </ul>
     *
     * </code></pre>
     *
     * @param arrayParameters Array of parameters returned by DataAccessFactory.getParametersInfo()
     * @return true if params is in agreement with getParametersInfo, override for additional checks.
     */
    public static boolean canProcess(Map<String, ?> params, Param... arrayParameters) {
        if (params == null) {
            return false;
        }
        for (Param param : arrayParameters) {
            Object value;
            if (!params.containsKey(param.key)) {
                if (param.required) {
                    return false; // missing required key!
                } else {
                    continue;
                }
            }
            try {
                value = param.lookUp(params);
            } catch (IOException e) {
                // could not upconvert/parse to expected type!
                // even if this parameter is not required
                // we are going to refuse to process
                // these params
                return false;
            }
            if (value == null) {
                if (param.required) {
                    return (false);
                }
            } else {
                if (!param.type.isInstance(value)) {
                    return false; // value was not of the required type
                }
                if (param.metadata != null) {
                    // check metadata
                    if (param.metadata.containsKey(Param.OPTIONS)) {
                        @SuppressWarnings("unchecked")
                        List<Object> options = (List<Object>) param.metadata.get(Param.OPTIONS);
                        if (options != null && !options.contains(value)) {
                            return false; // invalid option
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns a {@link IOFileFilter} obtained by excluding from the first input filter argument, the additional filter
     * arguments.
     *
     * @param inputFilter the initial filter from which to exclude other ones.
     * @param filters additional filters to be excluded
     * @return the updated {@link IOFileFilter}
     */
    public static FilenameFilter excludeFilters(final FilenameFilter inputFilter, final FilenameFilter... filters) {
        return (dir, name) -> {
            if (inputFilter.accept(dir, name)) {
                for (FilenameFilter exclude : filters) {
                    if (exclude.accept(dir, name)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        };
    }

    /**
     * Returns a {@link IOFileFilter} obtained by adding to the first input filter argument, the additional filter
     * arguments.
     *
     * @param inputFilter the initial filter to which to add other ones.
     * @param filters additional filters to be included in the main filter.
     * @return the updated {@link IOFileFilter}
     */
    public static FilenameFilter includeFilters(final FilenameFilter inputFilter, final FilenameFilter... filters) {
        return (dir, name) -> {
            if (inputFilter.accept(dir, name)) {
                return true;
            }
            for (FilenameFilter include : filters) {
                if (include.accept(dir, name)) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Create a non-simple template feature from feature type schema
     *
     * @param schema the feature type
     * @return a template feature
     */
    public static Feature templateFeature(FeatureType schema) {
        FeatureFactory ff = CommonFactoryFinder.getFeatureFactory(null);
        Collection<Property> value = new ArrayList<>();

        for (PropertyDescriptor pd : schema.getDescriptors()) {
            if (pd instanceof AttributeDescriptor) {
                if (pd instanceof GeometryDescriptor) {
                    value.add(new GeometryAttributeImpl(
                            ((AttributeDescriptor) pd).getDefaultValue(), (GeometryDescriptor) pd, null));
                } else {
                    value.add(new AttributeImpl(
                            ((AttributeDescriptor) pd).getDefaultValue(), (AttributeDescriptor) pd, null));
                }
            }
        }

        return ff.createFeature(value, schema, SimpleFeatureBuilder.createDefaultFeatureId());
    }

    /**
     * Converts a {@link java.util.Properties} object into a Map suitable for usage for {@link DataAccess} or
     * {@link DataStore} creation. The code assumes the properties do contain {@link String} keys, and may contain any
     * kind of object as values.
     */
    public static Map<String, Object> toConnectionParameters(Properties properties) {
        return properties.entrySet().stream().collect(Collectors.toMap(e -> (String) e.getKey(), e -> e.getValue()));
    }
}
