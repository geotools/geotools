/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.XSD;
import org.opengis.feature.type.Schema;

/**
 * This interface contains the qualified names of all the types in the http://www.opengis.net/gml
 * schema.
 *
 * @generated
 */
public final class GML extends XSD {

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/gml";

    /* Type Definitions */
    /** @generated */
    public static final QName AbstractFeatureCollectionBaseType =
            new QName("http://www.opengis.net/gml", "AbstractFeatureCollectionBaseType");

    /** @generated */
    public static final QName AbstractFeatureCollectionType =
            new QName("http://www.opengis.net/gml", "AbstractFeatureCollectionType");

    /** @generated */
    public static final QName AbstractFeatureType =
            new QName("http://www.opengis.net/gml", "AbstractFeatureType");

    /** @generated */
    public static final QName AbstractGeometryCollectionBaseType =
            new QName("http://www.opengis.net/gml", "AbstractGeometryCollectionBaseType");

    /** @generated */
    public static final QName AbstractGeometryType =
            new QName("http://www.opengis.net/gml", "AbstractGeometryType");

    /** @generated */
    public static final QName BoundingShapeType =
            new QName("http://www.opengis.net/gml", "BoundingShapeType");

    /** @generated */
    public static final QName BoxType = new QName("http://www.opengis.net/gml", "BoxType");

    /** @generated */
    public static final QName CoordinatesType =
            new QName("http://www.opengis.net/gml", "CoordinatesType");

    /** @generated */
    public static final QName CoordType = new QName("http://www.opengis.net/gml", "CoordType");

    /** @generated */
    public static final QName FeatureAssociationType =
            new QName("http://www.opengis.net/gml", "FeatureAssociationType");

    /** @generated */
    public static final QName GeometryAssociationType =
            new QName("http://www.opengis.net/gml", "GeometryAssociationType");

    /** @generated */
    public static final QName GeometryCollectionType =
            new QName("http://www.opengis.net/gml", "GeometryCollectionType");

    /** @generated */
    public static final QName GeometryPropertyType =
            new QName("http://www.opengis.net/gml", "GeometryPropertyType");

    /** @generated */
    public static final QName LinearRingMemberType =
            new QName("http://www.opengis.net/gml", "LinearRingMemberType");

    /** @generated */
    public static final QName LinearRingType =
            new QName("http://www.opengis.net/gml", "LinearRingType");

    /** @generated */
    public static final QName LineStringMemberType =
            new QName("http://www.opengis.net/gml", "LineStringMemberType");

    /** @generated */
    public static final QName LineStringPropertyType =
            new QName("http://www.opengis.net/gml", "LineStringPropertyType");

    /** @generated */
    public static final QName LineStringType =
            new QName("http://www.opengis.net/gml", "LineStringType");

    /** @generated */
    public static final QName MultiGeometryPropertyType =
            new QName("http://www.opengis.net/gml", "MultiGeometryPropertyType");

    /** @generated */
    public static final QName MultiLineStringPropertyType =
            new QName("http://www.opengis.net/gml", "MultiLineStringPropertyType");

    /** @generated */
    public static final QName MultiLineStringType =
            new QName("http://www.opengis.net/gml", "MultiLineStringType");

    /** @generated */
    public static final QName MultiPointPropertyType =
            new QName("http://www.opengis.net/gml", "MultiPointPropertyType");

    /** @generated */
    public static final QName MultiPointType =
            new QName("http://www.opengis.net/gml", "MultiPointType");

    /** @generated */
    public static final QName MultiPolygonPropertyType =
            new QName("http://www.opengis.net/gml", "MultiPolygonPropertyType");

    /** @generated */
    public static final QName MultiPolygonType =
            new QName("http://www.opengis.net/gml", "MultiPolygonType");

    /** @generated */
    public static final QName NullType = new QName("http://www.opengis.net/gml", "NullType");

    /** @generated */
    public static final QName PointMemberType =
            new QName("http://www.opengis.net/gml", "PointMemberType");

    /** @generated */
    public static final QName PointPropertyType =
            new QName("http://www.opengis.net/gml", "PointPropertyType");

    /** @generated */
    public static final QName PointType = new QName("http://www.opengis.net/gml", "PointType");

    /** @generated */
    public static final QName PolygonMemberType =
            new QName("http://www.opengis.net/gml", "PolygonMemberType");

    /** @generated */
    public static final QName PolygonPropertyType =
            new QName("http://www.opengis.net/gml", "PolygonPropertyType");

    /** @generated */
    public static final QName PolygonType = new QName("http://www.opengis.net/gml", "PolygonType");

    /* Elements */
    /** @generated */
    public static final QName _Feature = new QName("http://www.opengis.net/gml", "_Feature");

    /** @generated */
    public static final QName _FeatureCollection =
            new QName("http://www.opengis.net/gml", "_FeatureCollection");

    /** @generated */
    public static final QName _Geometry = new QName("http://www.opengis.net/gml", "_Geometry");

    /** @generated */
    public static final QName _GeometryCollection =
            new QName("http://www.opengis.net/gml", "_GeometryCollection");

    /** @generated */
    public static final QName _geometryProperty =
            new QName("http://www.opengis.net/gml", "_geometryProperty");

    /** @generated */
    public static final QName boundedBy = new QName("http://www.opengis.net/gml", "boundedBy");

    /** @generated */
    public static final QName Box = new QName("http://www.opengis.net/gml", "Box");

    /** @generated */
    public static final QName centerLineOf =
            new QName("http://www.opengis.net/gml", "centerLineOf");

    /** @generated */
    public static final QName centerOf = new QName("http://www.opengis.net/gml", "centerOf");

    /** @generated */
    public static final QName coord = new QName("http://www.opengis.net/gml", "coord");

    /** @generated */
    public static final QName coordinates = new QName("http://www.opengis.net/gml", "coordinates");

    /** @generated */
    public static final QName coverage = new QName("http://www.opengis.net/gml", "coverage");

    /** @generated */
    public static final QName description = new QName("http://www.opengis.net/gml", "description");

    /** @generated */
    public static final QName edgeOf = new QName("http://www.opengis.net/gml", "edgeOf");

    /** @generated */
    public static final QName extentOf = new QName("http://www.opengis.net/gml", "extentOf");

    /** @generated */
    public static final QName featureMember =
            new QName("http://www.opengis.net/gml", "featureMember");

    /** @generated */
    public static final QName geometryMember =
            new QName("http://www.opengis.net/gml", "geometryMember");

    /** @generated */
    public static final QName geometryProperty =
            new QName("http://www.opengis.net/gml", "geometryProperty");

    /** @generated */
    public static final QName innerBoundaryIs =
            new QName("http://www.opengis.net/gml", "innerBoundaryIs");

    /** @generated */
    public static final QName LinearRing = new QName("http://www.opengis.net/gml", "LinearRing");

    /** @generated */
    public static final QName LineString = new QName("http://www.opengis.net/gml", "LineString");

    /** @generated */
    public static final QName lineStringMember =
            new QName("http://www.opengis.net/gml", "lineStringMember");

    /** @generated */
    public static final QName lineStringProperty =
            new QName("http://www.opengis.net/gml", "lineStringProperty");

    /** @generated */
    public static final QName location = new QName("http://www.opengis.net/gml", "location");

    /** @generated */
    public static final QName multiCenterLineOf =
            new QName("http://www.opengis.net/gml", "multiCenterLineOf");

    /** @generated */
    public static final QName multiCenterOf =
            new QName("http://www.opengis.net/gml", "multiCenterOf");

    /** @generated */
    public static final QName multiCoverage =
            new QName("http://www.opengis.net/gml", "multiCoverage");

    /** @generated */
    public static final QName multiEdgeOf = new QName("http://www.opengis.net/gml", "multiEdgeOf");

    /** @generated */
    public static final QName multiExtentOf =
            new QName("http://www.opengis.net/gml", "multiExtentOf");

    /** @generated */
    public static final QName MultiGeometry =
            new QName("http://www.opengis.net/gml", "MultiGeometry");

    /** @generated */
    public static final QName multiGeometryProperty =
            new QName("http://www.opengis.net/gml", "multiGeometryProperty");

    /** @generated */
    public static final QName MultiLineString =
            new QName("http://www.opengis.net/gml", "MultiLineString");

    /** @generated */
    public static final QName multiLineStringProperty =
            new QName("http://www.opengis.net/gml", "multiLineStringProperty");

    /** @generated */
    public static final QName multiLocation =
            new QName("http://www.opengis.net/gml", "multiLocation");

    /** @generated */
    public static final QName MultiPoint = new QName("http://www.opengis.net/gml", "MultiPoint");

    /** @generated */
    public static final QName multiPointProperty =
            new QName("http://www.opengis.net/gml", "multiPointProperty");

    /** @generated */
    public static final QName MultiPolygon =
            new QName("http://www.opengis.net/gml", "MultiPolygon");

    /** @generated */
    public static final QName multiPolygonProperty =
            new QName("http://www.opengis.net/gml", "multiPolygonProperty");

    /** @generated */
    public static final QName multiPosition =
            new QName("http://www.opengis.net/gml", "multiPosition");

    /** @generated */
    public static final QName name = new QName("http://www.opengis.net/gml", "name");

    /** @generated */
    public static final QName outerBoundaryIs =
            new QName("http://www.opengis.net/gml", "outerBoundaryIs");

    /** @generated */
    public static final QName Point = new QName("http://www.opengis.net/gml", "Point");

    /** @generated */
    public static final QName pointMember = new QName("http://www.opengis.net/gml", "pointMember");

    /** @generated */
    public static final QName pointProperty =
            new QName("http://www.opengis.net/gml", "pointProperty");

    /** @generated */
    public static final QName Polygon = new QName("http://www.opengis.net/gml", "Polygon");

    /** @generated */
    public static final QName polygonMember =
            new QName("http://www.opengis.net/gml", "polygonMember");

    /** @generated */
    public static final QName polygonProperty =
            new QName("http://www.opengis.net/gml", "polygonProperty");

    /** @generated */
    public static final QName position = new QName("http://www.opengis.net/gml", "position");

    /* Attributes */

    /** @generated */
    public static final QName remoteSchema =
            new QName("http://www.opengis.net/gml", "remoteSchema");

    /** Private constructor. */
    private GML() {}

    /** singleton instance. */
    private static GML instance = new GML();

    /** The singleton instance; */
    public static GML getInstance() {
        return instance;
    }

    @Override
    protected Schema buildTypeSchema() {
        return new GMLSchema();
    }

    @Override
    protected Schema buildTypeMappingProfile(Schema typeSchema) {
        Set profile = new LinkedHashSet();
        profile.add(name(GML.PointPropertyType));
        profile.add(name(GML.MultiPointPropertyType));
        profile.add(name(GML.LineStringPropertyType));
        profile.add(name(GML.MultiLineStringPropertyType));
        profile.add(name(GML.PolygonPropertyType));
        profile.add(name(GML.MultiPolygonPropertyType));
        profile.add(name(GML.GeometryPropertyType));
        return typeSchema.profile(profile);
    }

    protected void addDependencies(Set dependencies) {
        // add xlink dependency
        dependencies.add(XLINK.getInstance());
    }

    /** Returns 'http://www.opengis.net/gml'. */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns The location of 'feature.xsd'. */
    public String getSchemaLocation() {
        return getClass().getResource("feature.xsd").toString();
    }

    @Override
    protected XSDSchema buildSchema() throws IOException {
        XSDSchema schema = super.buildSchema();

        schema.resolveElementDeclaration(NAMESPACE, "_Feature")
                .eAdapters()
                .add(new SubstitutionGroupLeakPreventer());
        schema.eAdapters().add(new ReferencingDirectiveLeakPreventer());
        return schema;
    }
}
