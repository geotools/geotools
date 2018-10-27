/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.LengthFunction;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Utilities;
import org.geotools.util.factory.FactoryRegistryException;
import org.locationtech.jts.geom.*;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Utility methods for working against the FeatureType interface.
 *
 * <p>Many methods from FeatureTypes should be refractored here.
 *
 * <p>Responsibilities:
 *
 * <ul>
 *   <li>Schema construction from String spec
 *   <li>Schema Force CRS
 * </ul>
 *
 * @author Jody Garnett, Refractions Research
 * @since 2.1.M3
 */
public class FeatureTypes {

    /** the default namespace for feature types */
    // public static final URI = GMLSchema.NAMESPACE;
    public static final URI DEFAULT_NAMESPACE;

    static {
        URI uri;
        try {
            uri = new URI("http://www.opengis.net/gml");
        } catch (URISyntaxException e) {
            uri = null; // will never happen
        }
        DEFAULT_NAMESPACE = uri;
    }

    /** abstract base type for all feature types */
    public static final SimpleFeatureType ABSTRACT_FEATURE_TYPE;

    static {
        SimpleFeatureType featureType = null;
        try {
            featureType =
                    FeatureTypes.newFeatureType(
                            null, "Feature", new URI("http://www.opengis.net/gml"), true);
        } catch (Exception e) {
            // shold not happen
        }
        ABSTRACT_FEATURE_TYPE = featureType;
    }

    /** default feature collection name */
    public static final NameImpl DEFAULT_TYPENAME =
            new NameImpl("AbstractFeatureCollectionType", DEFAULT_NAMESPACE.toString());

    /** represent an unbounded field length */
    public static final int ANY_LENGTH = -1;

    /** An feature type with no attributes */
    public static final SimpleFeatureType EMPTY =
            new SimpleFeatureTypeImpl(
                    new NameImpl("Empty"),
                    Collections.EMPTY_LIST,
                    null,
                    false,
                    Collections.EMPTY_LIST,
                    null,
                    null);

    /**
     * This is a 'suitable replacement for extracting the expected field length of an attribute
     * absed on its "facets" (ie Filter describing type restrictions);
     *
     * <p>This code is copied from the ShapefileDataStore where it was written (probably by
     * dzwiers). Cholmes is providing documentation.
     *
     * @param descriptor the AttributeType
     * @return an int indicating the max length of field in characters, or ANY_LENGTH
     */
    public static int getFieldLength(PropertyDescriptor descriptor) {
        PropertyType type = descriptor.getType();
        Integer length = null;
        while (type != null) {
            // TODO: We should really go through all the restrictions and find
            // the minimum of all the length restrictions; for now we assume an
            // override behavior.
            for (Filter f : type.getRestrictions()) {
                Integer filterLength = null;
                try {
                    if (f == null) {
                        continue;
                    }
                    if (f instanceof PropertyIsLessThan) {
                        BinaryComparisonOperator cf = (BinaryComparisonOperator) f;
                        if (cf.getExpression1() instanceof LengthFunction) {
                            filterLength = cf.getExpression2().evaluate(null, Integer.class) - 1;
                        }
                    } else if (f instanceof PropertyIsLessThanOrEqualTo) {
                        BinaryComparisonOperator cf = (BinaryComparisonOperator) f;
                        if (cf.getExpression1() instanceof LengthFunction) {
                            filterLength = cf.getExpression2().evaluate(null, Integer.class);
                        }
                    } else if (f instanceof PropertyIsGreaterThan) {
                        BinaryComparisonOperator cf = (BinaryComparisonOperator) f;
                        if (cf.getExpression2() instanceof LengthFunction) {
                            filterLength = cf.getExpression1().evaluate(null, Integer.class) - 1;
                        }
                    } else if (f instanceof PropertyIsGreaterThanOrEqualTo) {
                        BinaryComparisonOperator cf = (BinaryComparisonOperator) f;
                        if (cf.getExpression2() instanceof LengthFunction) {
                            filterLength = cf.getExpression1().evaluate(null, Integer.class);
                        }
                    }
                } catch (NullPointerException e) {
                    // was not an integer eh? Continue, worst case we'll return ANY_LENGTH
                }

                if (filterLength != null) {
                    if (length == null || filterLength < length) {
                        length = filterLength;
                    }
                }
            }
            type = type.getSuper();
        }

        return length != null ? length : ANY_LENGTH;
    }

    /**
     * Forces the specified CRS on all geometry attributes
     *
     * @param schema the original schema
     * @param crs the forced crs
     * @return
     * @throws SchemaException
     */
    public static SimpleFeatureType transform(
            SimpleFeatureType schema, CoordinateReferenceSystem crs) throws SchemaException {
        return transform(schema, crs, false);
    }

    /**
     * Forces the specified CRS on geometry attributes (all or some, depends on the parameters).
     *
     * @param schema the original schema
     * @param crs the forced crs
     * @param forceOnlyMissing if true, will force the specified crs only on the attributes that do
     *     miss one
     * @return
     * @throws SchemaException
     */
    public static SimpleFeatureType transform(
            SimpleFeatureType schema, CoordinateReferenceSystem crs, boolean forceOnlyMissing)
            throws SchemaException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(schema.getTypeName());
        tb.setNamespaceURI(schema.getName().getNamespaceURI());
        tb.setAbstract(schema.isAbstract());

        GeometryDescriptor defaultGeometryType = null;
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = schema.getDescriptor(i);
            if (attributeType instanceof GeometryDescriptor) {
                GeometryDescriptor geometryType = (GeometryDescriptor) attributeType;
                AttributeDescriptor forced;

                tb.descriptor(geometryType);
                if (!forceOnlyMissing || geometryType.getCoordinateReferenceSystem() == null) {
                    tb.crs(crs);
                }

                tb.add(geometryType.getLocalName(), geometryType.getType().getBinding());
            } else {
                tb.add(attributeType);
            }
        }
        if (schema.getGeometryDescriptor() != null) {
            tb.setDefaultGeometry(schema.getGeometryDescriptor().getLocalName());
        }

        tb.setSuperType((SimpleFeatureType) schema.getSuper());

        return tb.buildFeatureType();
    }

    /**
     * Applies transform to all geometry attribute.
     *
     * @param feature Feature to be transformed
     * @param schema Schema for target transformation - transform( schema, crs )
     * @param transform MathTransform used to transform coordinates - reproject( crs, crs )
     * @return transformed Feature of type schema
     * @throws TransformException
     * @throws MismatchedDimensionException
     * @throws IllegalAttributeException
     */
    public static SimpleFeature transform(
            SimpleFeature feature, SimpleFeatureType schema, MathTransform transform)
            throws MismatchedDimensionException, TransformException, IllegalAttributeException {
        feature = SimpleFeatureBuilder.copy(feature);

        GeometryDescriptor geomType = schema.getGeometryDescriptor();
        Geometry geom = (Geometry) feature.getAttribute(geomType.getLocalName());

        geom = JTS.transform(geom, transform);

        feature.setAttribute(geomType.getLocalName(), geom);

        return feature;
    }

    /**
     * The most specific way to create a new FeatureType.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     * @param superTypes A Collection of types the FeatureType will inherit from. Currently, all
     *     types inherit from feature in the opengis namespace.
     * @return A new FeatureType created from the given arguments.
     * @throws FactoryRegistryException If there are problems creating a factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in some way.
     */
    public static SimpleFeatureType newFeatureType(
            AttributeDescriptor[] types,
            String name,
            URI ns,
            boolean isAbstract,
            SimpleFeatureType[] superTypes)
            throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, ns, isAbstract, superTypes, null);
    }

    /**
     * The most specific way to create a new FeatureType.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     * @param superTypes A Collection of types the FeatureType will inherit from. Currently, all
     *     types inherit from feature in the opengis namespace.
     * @return A new FeatureType created from the given arguments.
     * @throws FactoryRegistryException If there are problems creating a factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in some way.
     */
    public static SimpleFeatureType newFeatureType(
            AttributeDescriptor[] types,
            String name,
            URI ns,
            boolean isAbstract,
            SimpleFeatureType[] superTypes,
            AttributeDescriptor defaultGeometry)
            throws FactoryRegistryException, SchemaException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName(name);
        tb.setNamespaceURI(ns);
        tb.setAbstract(isAbstract);
        if (types != null) {
            tb.addAll(types);
        }

        if (defaultGeometry != null) {
            // make sure that the default geometry was one of the types specified
            boolean add = true;
            for (int i = 0; i < types.length; i++) {
                if (types[i] == defaultGeometry) {
                    add = false;
                    break;
                }
            }
            if (add) {
                tb.add(defaultGeometry);
            }
            tb.setDefaultGeometry(defaultGeometry.getLocalName());
        }
        if (superTypes != null && superTypes.length > 0) {
            if (superTypes.length > 1) {
                throw new SchemaException("Can only specify a single super type");
            }
            tb.setSuperType(superTypes[0]);

        } else {
            // use the default super type
            tb.setSuperType(ABSTRACT_FEATURE_TYPE);
        }
        return (SimpleFeatureType) tb.buildFeatureType();
    }

    /**
     * The most specific way to create a new FeatureType.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     * @param superTypes A Collection of types the FeatureType will inherit from. Currently, all
     *     types inherit from feature in the opengis namespace.
     * @return A new FeatureType created from the given arguments.
     * @throws FactoryRegistryException If there are problems creating a factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in some way.
     */
    public static SimpleFeatureType newFeatureType(
            AttributeDescriptor[] types,
            String name,
            URI ns,
            boolean isAbstract,
            SimpleFeatureType[] superTypes,
            GeometryDescriptor defaultGeometry)
            throws FactoryRegistryException, SchemaException {
        return newFeatureType(
                types, name, ns, isAbstract, superTypes, (AttributeDescriptor) defaultGeometry);
    }

    /**
     * Create a new FeatureType with the given AttributeTypes. A short cut for calling <code>
     * newFeatureType(types,name,ns,isAbstract,null)</code>.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     * @return A new FeatureType created from the given arguments.
     * @throws FactoryRegistryException If there are problems creating a factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in some way.
     */
    public static SimpleFeatureType newFeatureType(
            AttributeDescriptor[] types, String name, URI ns, boolean isAbstract)
            throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, ns, isAbstract, null);
    }

    /**
     * Create a new FeatureType with the given AttributeTypes. A short cut for calling <code>
     * newFeatureType(types,name,ns,false,null)</code>.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @return A new FeatureType created from the given arguments.
     * @throws FactoryRegistryException If there are problems creating a factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in some way.
     */
    public static SimpleFeatureType newFeatureType(AttributeDescriptor[] types, String name, URI ns)
            throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, ns, false);
    }

    /**
     * Create a new FeatureType with the given AttributeTypes. A short cut for calling <code>
     * newFeatureType(types,name,null,false,null)</code>. Useful for test cases or datasources which
     * may not allow a namespace.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @return A new FeatureType created from the given arguments.
     * @throws FactoryRegistryException If there are problems creating a factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in some way.
     */
    public static SimpleFeatureType newFeatureType(AttributeDescriptor[] types, String name)
            throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, DEFAULT_NAMESPACE, false);
    }

    /**
     * Walks up the type hierarchy of the feature returning all super types of the specified feature
     * type. The search terminates when a non-FeatureType or null is found. The original featureType
     * is not included as an ancestor, only its strict ancestors.
     */
    public static List<FeatureType> getAncestors(FeatureType featureType) {
        List<FeatureType> ancestors = new ArrayList<FeatureType>();
        while (featureType.getSuper() instanceof FeatureType) {
            FeatureType superType = (FeatureType) featureType.getSuper();
            ancestors.add(superType);
            featureType = superType;
        }
        return ancestors;
    }

    /**
     * A query of the the types ancestor information.
     *
     * <p>This utility method may be used as common implementation for <code>
     * FeatureType.isDecendedFrom( namespace, typeName )</code>, however for specific uses, such as
     * GML, an implementor may be able to provide a more efficient implemenation based on prior
     * knolwege.
     *
     * <p>This is a proper check, if the provided FeatureType matches the given namespace and
     * typename it is <b>not </b> considered to be decended from itself.
     *
     * @param featureType typeName with parentage in question
     * @param namespace namespace to match against, or null for a "wildcard"
     * @param typeName typename to match against, or null for a "wildcard"
     * @return true if featureType is a decendent of the indicated namespace & typeName
     */
    public static boolean isDecendedFrom(FeatureType featureType, URI namespace, String typeName) {
        if (featureType == null) return false;
        List<FeatureType> ancestors = getAncestors(featureType);
        for (FeatureType superType : ancestors) {
            if (namespace == null) {
                // dont match on namespace
                if (Utilities.equals(superType.getName().getLocalPart(), typeName)) {
                    return true;
                }
            } else {
                if (Utilities.equals(superType.getName().getNamespaceURI(), namespace.toString())
                        && Utilities.equals(superType.getName().getLocalPart(), typeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDecendedFrom(FeatureType featureType, FeatureType isParentType) {
        try {
            return isDecendedFrom(
                    featureType,
                    new URI(isParentType.getName().getNamespaceURI()),
                    isParentType.getName().getLocalPart());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /** Exact equality based on typeNames, namespace, attributes and ancestors */
    public static boolean equals(SimpleFeatureType typeA, SimpleFeatureType typeB) {
        return equals(typeA, typeB, false);
    }

    /**
     * Exact equality based on typeNames, namespace, attributes and ancestors, including the user
     * maps contents
     */
    public static boolean equalsExact(SimpleFeatureType typeA, SimpleFeatureType typeB) {
        return equals(typeA, typeB, true);
    }

    /** Exact equality based on typeNames, namespace, attributes and ancestors */
    static boolean equals(
            SimpleFeatureType typeA, SimpleFeatureType typeB, boolean compareUserMaps) {
        if (typeA == typeB) return true;

        if (typeA == null || typeB == null) {
            return false;
        }

        if (!typeA.equals(typeB)) {
            return false;
        }

        if (compareUserMaps) {
            if (!equals(typeA.getUserData(), typeB.getUserData())) return false;
        }

        return equalsId(typeA, typeB)
                && equals(
                        typeA.getAttributeDescriptors(),
                        typeB.getAttributeDescriptors(),
                        compareUserMaps)
                && equalsAncestors(typeA, typeB);
    }

    static boolean equals(List attributesA, List attributesB, boolean compareUserMaps) {
        return equals(
                (AttributeDescriptor[])
                        attributesA.toArray(new AttributeDescriptor[attributesA.size()]),
                (AttributeDescriptor[])
                        attributesB.toArray(new AttributeDescriptor[attributesB.size()]),
                compareUserMaps);
    }

    public static boolean equals(List attributesA, List attributesB) {
        return equals(attributesA, attributesB, false);
    }

    public static boolean equalsExact(List attributesA, List attributesB) {
        return equals(attributesA, attributesB, true);
    }

    public static boolean equals(
            AttributeDescriptor attributesA[], AttributeDescriptor attributesB[]) {
        return equals(attributesA, attributesB, false);
    }

    public static boolean equalsExact(
            AttributeDescriptor attributesA[], AttributeDescriptor attributesB[]) {
        return equals(attributesA, attributesB, true);
    }

    static boolean equals(
            AttributeDescriptor attributesA[],
            AttributeDescriptor attributesB[],
            boolean compareUserMaps) {
        if (attributesA.length != attributesB.length) return false;

        for (int i = 0, length = attributesA.length; i < length; i++) {
            if (!equals(attributesA[i], attributesB[i], compareUserMaps)) return false;
        }
        return true;
    }
    /**
     * This method depends on the correct implementation of FeatureType equals
     *
     * <p>We may need to write an implementation that can detect cycles,
     *
     * @param typeA
     * @param typeB
     */
    public static boolean equalsAncestors(SimpleFeatureType typeA, SimpleFeatureType typeB) {
        return ancestors(typeA).equals(ancestors(typeB));
    }

    public static Set ancestors(SimpleFeatureType featureType) {
        if (featureType == null || getAncestors(featureType).isEmpty()) {
            return Collections.EMPTY_SET;
        }
        return new HashSet(getAncestors(featureType));
    }

    public static boolean equals(AttributeDescriptor a, AttributeDescriptor b) {
        return equals(a, b, false);
    }

    public static boolean equalsExact(AttributeDescriptor a, AttributeDescriptor b) {
        return equals(a, b, true);
    }

    static boolean equals(AttributeDescriptor a, AttributeDescriptor b, boolean compareUserMaps) {
        if (a == b) return true;

        if (a == null) return true;

        if (!a.equals(b)) return false;

        if (compareUserMaps) {
            if (!equals(a.getUserData(), b.getUserData())) return false;
            if (!equals(a.getType().getUserData(), b.getType().getUserData())) return false;
        }

        return true;
    }

    /**
     * Tolerant map comparison. Two maps are considered to be equal if they express the same
     * content. So for example two null maps are equal, but also a null and an empty one are
     */
    static boolean equals(Map a, Map b) {
        if (a == b) return true;

        // null == null handled above
        if (a == null || b == null) return false;

        if (a != null && a.size() == 0 && b == null) return true;

        if (b != null && b.size() == 0 && a == null) return true;

        return a.equals(b);
    }

    /** Quick check of namespace and typename */
    public static boolean equalsId(SimpleFeatureType typeA, SimpleFeatureType typeB) {
        if (typeA == typeB) return true;

        if (typeA == null || typeB == null) {
            return false;
        }

        String typeNameA = typeA.getTypeName();
        String typeNameB = typeB.getTypeName();
        if (typeNameA == null && typeNameB != null) return false;
        else if (!typeNameA.equals(typeNameB)) return false;

        String namespaceA = typeA.getName().getNamespaceURI();
        String namespaceB = typeB.getName().getNamespaceURI();
        if (namespaceA == null && namespaceB == null) return true;

        if (namespaceA == null && namespaceB != null) return false;
        else if (!namespaceA.equals(namespaceB)) return false;

        return true;
    }

    /**
     * Retrieve the attributeNames defined by the featureType
     *
     * @param featureType
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
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
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
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     */
    public static Set<PropertyName> propertyNames(
            Filter filter, final SimpleFeatureType featureType) {
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
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     */
    public static String[] attributeNames(
            Expression expression, final SimpleFeatureType featureType) {
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
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     */
    public static Set<PropertyName> propertyNames(
            Expression expression, final SimpleFeatureType featureType) {
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
     * Compare attribute coverage between two feature types (allowing the identification of
     * subTypes).
     *
     * <p>The comparison results in a number with the following meaning:
     *
     * <ul>
     *   <li>1: if typeA is a sub type/reorder/renamespace of typeB
     *   <li>0: if typeA and typeB are the same type
     *   <li>-1: if typeA is not subtype of typeB
     * </ul>
     *
     * <p>Comparison is based on {@link AttributeDescriptor} - the {@link
     * #isMatch(AttributeDescriptor, AttributeDescriptor)} method is used to quickly confirm that
     * the local name and java binding are compatible.
     *
     * <p>Namespace is not considered in this opperations. You may still need to reType to get the
     * correct namesapce, or reorder.
     *
     * <p>Please note this method will not result in a stable sort if used in a {@link Comparator}
     * as -1 is used to indicate incompatiblity (rather than simply "before").
     *
     * @param typeA FeatureType beind compared
     * @param typeB FeatureType being compared against
     */
    public static int compare(SimpleFeatureType typeA, SimpleFeatureType typeB) {
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

            if (isMatch(a, typeB.getDescriptor(i))) {
                match++;
            } else if (isMatch(a, typeB.getDescriptor(a.getLocalName()))) {
                // match was found in a different position
            } else {
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
     * <p>This method checks the descriptors name and class binding to see if the values have any
     * chance of being compatible.
     *
     * @param a descriptor to compare
     * @param b descriptor to compare
     * @return true to the descriptors name and binding class match
     */
    public static boolean isMatch(AttributeDescriptor a, AttributeDescriptor b) {
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

        if (a.getLocalName().equals(b.getLocalName()) && a.getClass().equals(b.getClass())) {
            return true;
        }

        return false;
    }

    /**
     * Creates duplicate of feature adjusted to the provided featureType.
     *
     * <p>Please note this implementation provides "deep copy" using {@link #duplicate(Object)} to
     * copy each attribute.
     *
     * @param featureType FeatureType requested
     * @param feature Origional Feature from DataStore
     * @return An instance of featureType based on feature
     * @throws org.opengis.feature.IllegalAttributeException If opperation could not be performed
     */
    public static SimpleFeature reType(SimpleFeatureType featureType, SimpleFeature feature)
            throws org.opengis.feature.IllegalAttributeException {
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
     *   <li>dupliate is true: A "deep copy" is made of each attribute resulting in a safe
     *       "copy"Adjusts the attribute order to match the provided featureType.
     *   <li>duplicate is false: the attributes are simply reordered and are actually the same
     *       instances as those in the origional feature
     * </ul>
     *
     * In the future this method may simply return a "wrapper" when duplicate is false.
     *
     * <p>
     *
     * @param featureType
     * @param feature
     * @param duplicate True to perform {@link #duplicate(Object)} on each attribute
     * @return
     * @throws org.opengis.feature.IllegalAttributeException
     */
    public static SimpleFeature reType(
            SimpleFeatureType featureType, SimpleFeature feature, boolean duplicate)
            throws org.opengis.feature.IllegalAttributeException {
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
        String xpath;

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
     * This function is used recusively for (in order to handle complext features) no attempt is
     * made to detect cycles at this time so your milage may vary.
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
            List<Object> copy = new ArrayList<Object>(list.size());

            for (Iterator i = list.iterator(); i.hasNext(); ) {
                copy.add(duplicate(i.next()));
            }

            return Collections.unmodifiableList(copy);
        }

        if (src instanceof Map) {
            Map map = (Map) src;
            Map copy = new HashMap(map.size());

            for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                copy.put(entry.getKey(), duplicate(entry.getValue()));
            }

            return Collections.unmodifiableMap(copy);
        }

        if (src instanceof GridCoverage) {
            return src; // inmutable
        }

        //
        // I have lost hope and am returning the orgional reference
        // Please extend this to support additional classes.
        //
        // And good luck getting Cloneable to work
        throw new org.opengis.feature.IllegalAttributeException(
                null, "Do not know how to deep copy " + type.getName());
    }

    /**
     * Constructs an empty feature to use as a Template for new content.
     *
     * <p>We may move this functionality to FeatureType.create( null )?
     *
     * @param featureType Type of feature we wish to create
     * @return A new Feature of type featureType
     */
    public static SimpleFeature template(SimpleFeatureType featureType)
            throws org.opengis.feature.IllegalAttributeException {
        return SimpleFeatureBuilder.build(featureType, defaultValues(featureType), null);
    }

    /**
     * Use the provided featureType to create an empty feature.
     *
     * <p>The {@link #defaultValues(SimpleFeatureType)} method is used to generate the intial values
     * (making use of {@link AttributeDescriptor#getDefaultValue()} as required.
     *
     * @param featureType
     * @param featureID
     * @return Craeted feature
     */
    public static SimpleFeature template(SimpleFeatureType featureType, String featureID) {
        return SimpleFeatureBuilder.build(featureType, defaultValues(featureType), featureID);
    }

    /**
     * Produce a set of default values for the provided FeatureType
     *
     * @param featureType
     * @return Array of values, that are good starting point for data entry
     */
    public static Object[] defaultValues(SimpleFeatureType featureType) {
        return defaultValues(featureType, null);
    }

    /**
     * Create a new feature from the provided values, using appropriate default values for any nulls
     * provided.
     *
     * @param featureType
     * @param providedValues
     * @return newly created feature
     * @throws ArrayIndexOutOfBoundsException If the number of provided values does not match the
     *     featureType
     */
    public static SimpleFeature template(SimpleFeatureType featureType, Object[] providedValues) {
        return SimpleFeatureBuilder.build(
                featureType, defaultValues(featureType, providedValues), null);
    }

    /**
     * Create a new feature from the provided values, using appropriate default values for any nulls
     * provided.
     *
     * @param featureType
     * @param featureID
     * @param providedValues provided attributes
     * @return newly created feature
     * @throws ArrayIndexOutOfBoundsException If the number of provided values does not match the
     *     featureType
     */
    public static SimpleFeature template(
            SimpleFeatureType featureType, String featureID, Object[] providedValues) {
        return SimpleFeatureBuilder.build(
                featureType, defaultValues(featureType, providedValues), featureID);
    }

    /**
     * Create default values matching the provided feature type.
     *
     * @param featureType
     * @param values
     * @return set of default values
     * @throws ArrayIndexOutOfBoundsException If the number of provided values does not match the
     *     featureType
     */
    public static Object[] defaultValues(SimpleFeatureType featureType, Object[] values) {
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
     * Provides a defautlValue for attributeType.
     *
     * <p>Will return null if attributeType isNillable(), or attempt to use Reflection, or
     * attributeType.parse( null )
     *
     * @param attributeType
     * @return null for nillable attributeType, attempt at reflection
     * @deprecated Please {@link AttributeDescriptor#getDefaultValue()}
     */
    public static Object defaultValue(AttributeDescriptor attributeType)
            throws org.opengis.feature.IllegalAttributeException {
        Object value = attributeType.getDefaultValue();

        if (value == null && !attributeType.isNillable()) {
            return null; // sometimes there is no valid default value :-(
        }
        return value;
    }

    /**
     * Returns a non-null default value for the class that is passed in. This is a helper class an
     * can't create a default class for all types but it does support:
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
     *
     * @param type
     * @return
     */
    public static Object defaultValue(Class type) {
        if (type == String.class || type == Object.class) {
            return "";
        }
        if (type == Integer.class) {
            return new Integer(0);
        }
        if (type == Double.class) {
            return new Double(0);
        }
        if (type == Long.class) {
            return new Long(0);
        }
        if (type == Short.class) {
            return new Short((short) 0);
        }
        if (type == Float.class) {
            return new Float(0.0f);
        }
        if (type == BigDecimal.class) {
            return BigDecimal.valueOf(0);
        }
        if (type == BigInteger.class) {
            return BigInteger.valueOf(0);
        }
        if (type == Character.class) {
            return new Character(' ');
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
        LineString lineString =
                fac.createLineString(new Coordinate[] {new Coordinate(0, 0), new Coordinate(0, 1)});
        if (type == LineString.class) {
            return lineString;
        }
        LinearRing linearRing =
                fac.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0),
                            new Coordinate(0, 1),
                            new Coordinate(1, 1),
                            new Coordinate(1, 0),
                            new Coordinate(0, 0)
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

        throw new IllegalArgumentException(type + " is not supported by this method");
    }

    //
    // Attribute Value Utility Methods
    //
    /**
     * Used to compare if two values are equal.
     *
     * <p>This method is here to work around the fact that JTS Geometry requires a specific method
     * to be called rather than object.equals.
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
     * @throws SchemaException
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

        return createSubType(
                featureType, properties, override, featureType.getTypeName(), namespaceURI);
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
     * @throws SchemaException
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
        boolean same =
                featureType.getAttributeCount() == properties.length
                        && featureType.getTypeName().equals(typeName)
                        && Utilities.equals(featureType.getName().getNamespaceURI(), namespaceURI);

        for (int i = 0; (i < featureType.getAttributeCount()) && same; i++) {
            AttributeDescriptor type = featureType.getDescriptor(i);
            same =
                    type.getLocalName().equals(properties[i])
                            && (((override == null) || !(type instanceof GeometryDescriptor))
                                    || Objects.equals(
                                            override,
                                            ((GeometryDescriptor) type)
                                                    .getCoordinateReferenceSystem()));
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

    /**
     * Create a type limited to the named properties provided.
     *
     * @param featureType
     * @param properties
     * @return type limited to the named properties provided
     * @throws SchemaException
     */
    public static SimpleFeatureType createSubType(
            SimpleFeatureType featureType, String[] properties) throws SchemaException {
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
        for (int i = 0; i < properties.length; i++) {
            // let's get the attribute descriptor corresponding to the current property
            AttributeDescriptor attributeDescriptor = featureType.getDescriptor(properties[i]);
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
            SimpleFeatureTypeBuilder typeBuilder,
            String[] properties,
            SimpleFeatureType featureType) {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        if (geometryDescriptor != null) {
            String propertyName = geometryDescriptor.getLocalName();
            if (Arrays.asList(properties).contains(propertyName)) {
                typeBuilder.setDefaultGeometry(propertyName);
            }
        }
    }

    /**
     * Manually calculate the bounds from the provided FeatureIteator. This implementation is
     * intended for FeatureCollection implementors and test case verification. Client code should
     * always call {@link FeatureCollection#getBounds()}.
     *
     * @param iterator
     * @return
     */
    public static ReferencedEnvelope bounds(FeatureIterator<?> iterator) {
        if (iterator == null) {
            return null;
        }
        try {
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
        } finally {
            iterator.close();
        }
    }
    /**
     * Manually calculates the bounds of a feature collection using {@link
     * FeatureCollection#features()}.
     *
     * <p>This implementation is intended for FeatureCollection implementors and test case
     * verification. Client code should always call {@link FeatureCollection#getBounds()}.
     *
     * @param collection
     * @return bounds of features in feature collection
     */
    public static ReferencedEnvelope bounds(
            FeatureCollection<? extends FeatureType, ? extends Feature> collection) {
        FeatureIterator<? extends Feature> i = collection.features();
        try {
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
        } finally {
            if (i != null) {
                i.close();
            }
        }
    }
}
