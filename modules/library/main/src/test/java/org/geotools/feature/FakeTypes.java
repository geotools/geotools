/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.feature;

import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.FeatureTypeImpl;

public class FakeTypes {
    // *** Taken from XSSchema ***
    public static final AttributeType ANYTYPE_TYPE =
            new AttributeTypeImpl(
                    /* name: */ new NameImpl("http://www.w3.org/2001/XMLSchema", "anyType"),
                    /* binding: */ java.lang.Object.class,
                    /* identified: */ false,
                    /* abstract: */ false,
                    /* restrictions: */ Collections.emptyList(),
                    /* superType: */ null,
                    /* description: */ null);

    public static final AttributeType ANYSIMPLETYPE_TYPE =
            new AttributeTypeImpl(
                    /* name: */ new NameImpl("http://www.w3.org/2001/XMLSchema", "anySimpleType"),
                    /* binding: */ java.lang.Object.class,
                    /* identified: */ false,
                    /* abstract: */ false,
                    /* restrictions: */ Collections.emptyList(),
                    /* superType: */ ANYTYPE_TYPE,
                    /* description: */ null);

    public static final AttributeType STRING_TYPE =
            new AttributeTypeImpl(
                    /* name: */ new NameImpl("http://www.w3.org/2001/XMLSchema", "string"),
                    /* binding: */ String.class,
                    /* identified: */ false,
                    /* abstract: */ false,
                    /* restrictions: */ Collections.emptyList(),
                    /* superType: */ ANYSIMPLETYPE_TYPE,
                    /* description: */ null);

    public static final AttributeType BOOLEAN_TYPE =
            new AttributeTypeImpl(
                    /* name: */ new NameImpl("http://www.w3.org/2001/XMLSchema", "boolean"),
                    /* binding: */ Boolean.class,
                    /* identified: */ false,
                    /* abstract: */ false,
                    /* restrictions: */ Collections.emptyList(),
                    /* superType: */ ANYSIMPLETYPE_TYPE,
                    /* description: */ null);

    // ***************************

    // *** Taken from GMLSchema ***
    public static final AttributeType GEOMETRYPROPERTYTYPE_TYPE = build_GEOMETRYPROPERTYTYPE_TYPE();

    private static AttributeType build_GEOMETRYPROPERTYTYPE_TYPE() {
        AttributeType builtType =
                new AttributeTypeImpl(
                        new NameImpl("http://www.opengis.net/gml", "GeometryPropertyType"),
                        org.locationtech.jts.geom.Geometry.class,
                        false,
                        false,
                        Collections.emptyList(),
                        FakeTypes.ANYTYPE_TYPE,
                        null);

        return builtType;
    }

    public static final AttributeType NULLTYPE_TYPE = build_NULLTYPE_TYPE();

    private static AttributeType build_NULLTYPE_TYPE() {
        AttributeType builtType =
                new AttributeTypeImpl(
                        new NameImpl("http://www.opengis.net/gml", "NullType"),
                        Object.class,
                        false,
                        false,
                        Collections.emptyList(),
                        FakeTypes.ANYSIMPLETYPE_TYPE,
                        null);

        return builtType;
    }

    // ***************************

    // *** EarthResource's Mine ***
    public static class Mine {
        public static final String MINE_NAMESPACE = "urn:org:example";

        public static final QName NAME_Mine = new QName(MINE_NAMESPACE, "Mine", "er");

        public static final Name NAME_mineName = new NameImpl(MINE_NAMESPACE, "mineName");

        public static final Name NAME_MineName = new NameImpl(MINE_NAMESPACE, "MineName");

        public static final Name NAME_MineType = new NameImpl(MINE_NAMESPACE, "MineType");

        public static final Name NAME_isPreferred = new NameImpl(MINE_NAMESPACE, "isPreferred");

        public static final Name NAME_MineNameType = new NameImpl(MINE_NAMESPACE, "MineNameType");

        public static final Name NAME_MineNamePropertyType =
                new NameImpl(MINE_NAMESPACE, "MineNamePropertyType");

        // (1)
        public static final AttributeDescriptor ISPREFERRED_DESCRIPTOR =
                new AttributeDescriptorImpl(
                        /* type: */ BOOLEAN_TYPE,
                        /* name: */ NAME_isPreferred,
                        /* min: */ 1,
                        /* max: */ 1,
                        /* isNillable: */ false,
                        /* defaultValue: */ false);

        // (2)
        public static final AttributeDescriptor mineNAME_DESCRIPTOR =
                new AttributeDescriptorImpl(
                        /* type: */ STRING_TYPE,
                        /* name: */ NAME_mineName,
                        /* min: */ 1,
                        /* max: */ 1,
                        /* isNillable: */ false,
                        /* defaultValue: */ null);

        public static List<PropertyDescriptor> MINENAMETYPE_SCHEMA =
                List.of(ISPREFERRED_DESCRIPTOR, mineNAME_DESCRIPTOR);

        // (3)
        public static final ComplexType MINENAMETYPE_TYPE =
                new ComplexTypeImpl(
                        /* name: */ NAME_MineNameType,
                        /* properties: */ MINENAMETYPE_SCHEMA,
                        /* identified: */ false,
                        /* isAbstract: */ false,
                        /* restrictions: */ Collections.emptyList(),
                        /* superType: */ ANYTYPE_TYPE,
                        /* description: */ null);

        // (4)
        public static final AttributeDescriptor MINENAME_DESCRIPTOR =
                new AttributeDescriptorImpl(
                        /* type: */ MINENAMETYPE_TYPE,
                        /* name: */ NAME_MineName,
                        /* min: */ 1,
                        /* max: */ 1,
                        /* isNillable: */ false,
                        /* defaultValue: */ null);

        public static List<PropertyDescriptor> MINENAMEPROPERTYTYPE_SCHEMA =
                List.of(MINENAME_DESCRIPTOR);

        // (5)
        public static final ComplexType MINENAMEPROPERTYTYPE_TYPE =
                new ComplexTypeImpl(
                        /* name: */ NAME_MineNamePropertyType,
                        /* properties: */ MINENAMEPROPERTYTYPE_SCHEMA,
                        /* identified: */ false,
                        /* isAbstract: */ false,
                        /* restrictions: */ Collections.emptyList(),
                        /* superType: */ ANYTYPE_TYPE,
                        /* description: */ null);

        // (6)
        public static final AttributeDescriptor MINEmineNAME_DESCRIPTOR =
                new AttributeDescriptorImpl(
                        /* type: */ MINENAMEPROPERTYTYPE_TYPE,
                        /* name: */ NAME_mineName,
                        /* min: */ 1,
                        /* max: */ 3, // This is used for one of the tests.
                        /* isNillable: */ false,
                        /* defaultValue: */ null);

        public static List<PropertyDescriptor> MINETYPE_SCHEMA = List.of(MINEmineNAME_DESCRIPTOR);
        // (7)
        public static final FeatureType MINETYPE_TYPE =
                new FeatureTypeImpl(
                        /* name: */ NAME_MineType,
                        /* properties: */ MINETYPE_SCHEMA, // This is only a subset of mine
                        // information, the real one has some
                        // other details but they've been omitted
                        // here.
                        /* defaultGeometry: */ null,
                        /* isAbstract: */ false,
                        /* restrictions: */ Collections.emptyList(),
                        /* superType: */ ANYTYPE_TYPE, // In real life it's actually a
                        // MiningFeatureType but I don't think it
                        // matters.
                        /* description: */ null);
    }
}
