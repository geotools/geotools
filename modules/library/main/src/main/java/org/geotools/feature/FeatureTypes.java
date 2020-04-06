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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.filter.LengthFunction;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.Utilities;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Utility methods for working against the FeatureType interface.
 *
 * <p>Many methods from DataUtilities should be refractored here.
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

    static final Logger LOGGER = Logging.getLogger(FeatureTypes.class);

    /** the default namespace for feature types */
    // public static final URI = GMLSchema.NAMESPACE;
    public static final URI DEFAULT_NAMESPACE;

    static {
        URI uri;
        try {
            uri = new URI("http://www.opengis.net/gml");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unexpected URI syntax exception", e);
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
     * @param descriptor the descriptor whose lenght is to be investigated
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
     */
    public static SimpleFeatureType transform(
            SimpleFeatureType schema, CoordinateReferenceSystem crs, boolean forceOnlyMissing)
            throws SchemaException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(schema.getTypeName());
        tb.setNamespaceURI(schema.getName().getNamespaceURI());
        tb.setAbstract(schema.isAbstract());

        for (int i = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = schema.getDescriptor(i);
            if (attributeType instanceof GeometryDescriptor) {
                GeometryDescriptor geometryType = (GeometryDescriptor) attributeType;

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
     * Whether the feature type has the specified name, or is a descendent from it
     *
     * @param featureType typeName with parentage in question
     * @param name name to match against
     * @return true if featureType has the same name, or is a descendent of the indicated name
     */
    public static boolean matches(FeatureType featureType, Name name) {
        if (featureType.getName().equals(name)
                || (name.getNamespaceURI() == null
                        && featureType
                                .getName()
                                .getLocalPart()
                                .equalsIgnoreCase(name.getLocalPart()))) {
            return true;
        }

        try {
            return isDecendedFrom(
                    featureType,
                    name.getNamespaceURI() != null ? new URI(name.getNamespaceURI()) : null,
                    name.getLocalPart());
        } catch (URISyntaxException e) {
            LOGGER.log(Level.FINE, "Unexpected failure while feature type", e);
            return false;
        }
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
        if (!Objects.equals(typeNameA, typeNameB)) return false;

        String namespaceA = typeA.getName().getNamespaceURI();
        String namespaceB = typeB.getName().getNamespaceURI();
        return Objects.equals(namespaceA, namespaceB);
    }
}
