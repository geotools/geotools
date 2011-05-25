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
package org.geotools.kml;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://earth.google.com/kml/2.1 schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class KML extends XSD {
    /** singleton instance */
    private static final KML instance = new KML();

    /** @generated */
    public static final String NAMESPACE = "http://earth.google.com/kml/2.1";

    /* Type Definitions */
    /** @generated */
    public static final QName altitudeModeEnum = new QName("http://earth.google.com/kml/2.1",
            "altitudeModeEnum");

    /** @generated */
    public static final QName angle180 = new QName("http://earth.google.com/kml/2.1", "angle180");

    /** @generated */
    public static final QName angle360 = new QName("http://earth.google.com/kml/2.1", "angle360");

    /** @generated */
    public static final QName angle90 = new QName("http://earth.google.com/kml/2.1", "angle90");

    /** @generated */
    public static final QName anglepos90 = new QName("http://earth.google.com/kml/2.1", "anglepos90");

    /** @generated */
    public static final QName BalloonStyleType = new QName("http://earth.google.com/kml/2.1",
            "BalloonStyleType");

    /** @generated */
    public static final QName boundaryType = new QName("http://earth.google.com/kml/2.1",
            "boundaryType");

    /** @generated */
    public static final QName ChangeType = new QName("http://earth.google.com/kml/2.1", "ChangeType");

    /** @generated */
    public static final QName color = new QName("http://earth.google.com/kml/2.1", "color");

    /** @generated */
    public static final QName colorModeEnum = new QName("http://earth.google.com/kml/2.1",
            "colorModeEnum");

    /** @generated */
    public static final QName ColorStyleType = new QName("http://earth.google.com/kml/2.1",
            "ColorStyleType");

    /** @generated */
    public static final QName ContainerType = new QName("http://earth.google.com/kml/2.1",
            "ContainerType");

    /** @generated */
    public static final QName CoordinatesType = new QName("http://earth.google.com/kml/2.1",
            "CoordinatesType");

    /** @generated */
    public static final QName CreateType = new QName("http://earth.google.com/kml/2.1", "CreateType");

    /** @generated */
    public static final QName dateTimeType = new QName("http://earth.google.com/kml/2.1",
            "dateTimeType");

    /** @generated */
    public static final QName DeleteType = new QName("http://earth.google.com/kml/2.1", "DeleteType");

    /** @generated */
    public static final QName DocumentType = new QName("http://earth.google.com/kml/2.1",
            "DocumentType");

    /** @generated */
    public static final QName FeatureType = new QName("http://earth.google.com/kml/2.1",
            "FeatureType");

    /** @generated */
    public static final QName FolderType = new QName("http://earth.google.com/kml/2.1", "FolderType");

    /** @generated */
    public static final QName GeometryType = new QName("http://earth.google.com/kml/2.1",
            "GeometryType");

    /** @generated */
    public static final QName GroundOverlayType = new QName("http://earth.google.com/kml/2.1",
            "GroundOverlayType");

    /** @generated */
    public static final QName IconStyleIconType = new QName("http://earth.google.com/kml/2.1",
            "IconStyleIconType");

    /** @generated */
    public static final QName IconStyleType = new QName("http://earth.google.com/kml/2.1",
            "IconStyleType");

    /** @generated */
    public static final QName IconType = new QName("http://earth.google.com/kml/2.1", "IconType");

    /** @generated */
    public static final QName itemIconStateEnum = new QName("http://earth.google.com/kml/2.1",
            "itemIconStateEnum");

    /** @generated */
    public static final QName itemIconStateType = new QName("http://earth.google.com/kml/2.1",
            "itemIconStateType");

    /** @generated */
    public static final QName ItemIconType = new QName("http://earth.google.com/kml/2.1",
            "ItemIconType");

    /** @generated */
    public static final QName KmlType = new QName("http://earth.google.com/kml/2.1", "KmlType");

    /** @generated */
    public static final QName LabelStyleType = new QName("http://earth.google.com/kml/2.1",
            "LabelStyleType");

    /** @generated */
    public static final QName LatLonAltBoxType = new QName("http://earth.google.com/kml/2.1",
            "LatLonAltBoxType");

    /** @generated */
    public static final QName LatLonBoxType = new QName("http://earth.google.com/kml/2.1",
            "LatLonBoxType");

    /** @generated */
    public static final QName LinearRingType = new QName("http://earth.google.com/kml/2.1",
            "LinearRingType");

    /** @generated */
    public static final QName LineStringType = new QName("http://earth.google.com/kml/2.1",
            "LineStringType");

    /** @generated */
    public static final QName LineStyleType = new QName("http://earth.google.com/kml/2.1",
            "LineStyleType");

    /** @generated */
    public static final QName LinkType = new QName("http://earth.google.com/kml/2.1", "LinkType");

    /** @generated */
    public static final QName listItemTypeEnum = new QName("http://earth.google.com/kml/2.1",
            "listItemTypeEnum");

    /** @generated */
    public static final QName ListStyleType = new QName("http://earth.google.com/kml/2.1",
            "ListStyleType");

    /** @generated */
    public static final QName LocationType = new QName("http://earth.google.com/kml/2.1",
            "LocationType");

    /** @generated */
    public static final QName LodType = new QName("http://earth.google.com/kml/2.1", "LodType");

    /** @generated */
    public static final QName LookAtType = new QName("http://earth.google.com/kml/2.1", "LookAtType");

    /** @generated */
    public static final QName MetadataType = new QName("http://earth.google.com/kml/2.1",
            "MetadataType");

    /** @generated */
    public static final QName ModelType = new QName("http://earth.google.com/kml/2.1", "ModelType");

    /** @generated */
    public static final QName MultiGeometryType = new QName("http://earth.google.com/kml/2.1",
            "MultiGeometryType");

    /** @generated */
    public static final QName NetworkLinkControlType = new QName("http://earth.google.com/kml/2.1",
            "NetworkLinkControlType");

    /** @generated */
    public static final QName NetworkLinkType = new QName("http://earth.google.com/kml/2.1",
            "NetworkLinkType");

    /** @generated */
    public static final QName ObjectType = new QName("http://earth.google.com/kml/2.1", "ObjectType");

    /** @generated */
    public static final QName OrientationType = new QName("http://earth.google.com/kml/2.1",
            "OrientationType");

    /** @generated */
    public static final QName OverlayType = new QName("http://earth.google.com/kml/2.1",
            "OverlayType");

    /** @generated */
    public static final QName PlacemarkType = new QName("http://earth.google.com/kml/2.1",
            "PlacemarkType");

    /** @generated */
    public static final QName PointType = new QName("http://earth.google.com/kml/2.1", "PointType");

    /** @generated */
    public static final QName PolygonType = new QName("http://earth.google.com/kml/2.1",
            "PolygonType");

    /** @generated */
    public static final QName PolyStyleType = new QName("http://earth.google.com/kml/2.1",
            "PolyStyleType");

    /** @generated */
    public static final QName refreshModeEnum = new QName("http://earth.google.com/kml/2.1",
            "refreshModeEnum");

    /** @generated */
    public static final QName RegionType = new QName("http://earth.google.com/kml/2.1", "RegionType");

    /** @generated */
    public static final QName ReplaceType = new QName("http://earth.google.com/kml/2.1",
            "ReplaceType");

    /** @generated */
    public static final QName ScaleType = new QName("http://earth.google.com/kml/2.1", "ScaleType");

    /** @generated */
    public static final QName ScreenOverlayType = new QName("http://earth.google.com/kml/2.1",
            "ScreenOverlayType");

    /** @generated */
    public static final QName SnippetType = new QName("http://earth.google.com/kml/2.1",
            "SnippetType");

    /** @generated */
    public static final QName StyleMapPairType = new QName("http://earth.google.com/kml/2.1",
            "StyleMapPairType");

    /** @generated */
    public static final QName StyleMapType = new QName("http://earth.google.com/kml/2.1",
            "StyleMapType");

    /** @generated */
    public static final QName StyleSelectorType = new QName("http://earth.google.com/kml/2.1",
            "StyleSelectorType");

    /** @generated */
    public static final QName styleStateEnum = new QName("http://earth.google.com/kml/2.1",
            "styleStateEnum");

    /** @generated */
    public static final QName StyleType = new QName("http://earth.google.com/kml/2.1", "StyleType");

    /** @generated */
    public static final QName TimePrimitiveType = new QName("http://earth.google.com/kml/2.1",
            "TimePrimitiveType");

    /** @generated */
    public static final QName TimeSpanType = new QName("http://earth.google.com/kml/2.1",
            "TimeSpanType");

    /** @generated */
    public static final QName TimeStampType = new QName("http://earth.google.com/kml/2.1",
            "TimeStampType");

    /** @generated */
    public static final QName unitsEnum = new QName("http://earth.google.com/kml/2.1", "unitsEnum");

    /** @generated */
    public static final QName UpdateType = new QName("http://earth.google.com/kml/2.1", "UpdateType");

    /** @generated */
    public static final QName vec2Type = new QName("http://earth.google.com/kml/2.1", "vec2Type");

    /** @generated */
    public static final QName viewRefreshModeEnum = new QName("http://earth.google.com/kml/2.1",
            "viewRefreshModeEnum");

    /* Elements */
    /** @generated */
    public static final QName BalloonStyle = new QName("http://earth.google.com/kml/2.1",
            "BalloonStyle");

    /** @generated */
    public static final QName coordinates = new QName("http://earth.google.com/kml/2.1",
            "coordinates");

    /** @generated */
    public static final QName Document = new QName("http://earth.google.com/kml/2.1", "Document");

    /** @generated */
    public static final QName Feature = new QName("http://earth.google.com/kml/2.1", "Feature");

    /** @generated */
    public static final QName Folder = new QName("http://earth.google.com/kml/2.1", "Folder");

    /** @generated */
    public static final QName Geometry = new QName("http://earth.google.com/kml/2.1", "Geometry");

    /** @generated */
    public static final QName GroundOverlay = new QName("http://earth.google.com/kml/2.1",
            "GroundOverlay");

    /** @generated */
    public static final QName Icon = new QName("http://earth.google.com/kml/2.1", "Icon");

    /** @generated */
    public static final QName IconStyle = new QName("http://earth.google.com/kml/2.1", "IconStyle");

    /** @generated */
    public static final QName kml = new QName("http://earth.google.com/kml/2.1", "kml");

    /** @generated */
    public static final QName LabelStyle = new QName("http://earth.google.com/kml/2.1", "LabelStyle");

    /** @generated */
    public static final QName LatLonAltBox = new QName("http://earth.google.com/kml/2.1",
            "LatLonAltBox");

    /** @generated */
    public static final QName LatLonBox = new QName("http://earth.google.com/kml/2.1", "LatLonBox");

    /** @generated */
    public static final QName LinearRing = new QName("http://earth.google.com/kml/2.1", "LinearRing");

    /** @generated */
    public static final QName LineString = new QName("http://earth.google.com/kml/2.1", "LineString");

    /** @generated */
    public static final QName LineStyle = new QName("http://earth.google.com/kml/2.1", "LineStyle");

    /** @generated */
    public static final QName Link = new QName("http://earth.google.com/kml/2.1", "Link");

    /** @generated */
    public static final QName ListStyle = new QName("http://earth.google.com/kml/2.1", "ListStyle");

    /** @generated */
    public static final QName Location = new QName("http://earth.google.com/kml/2.1", "Location");

    /** @generated */
    public static final QName Lod = new QName("http://earth.google.com/kml/2.1", "Lod");

    /** @generated */
    public static final QName LookAt = new QName("http://earth.google.com/kml/2.1", "LookAt");

    /** @generated */
    public static final QName Model = new QName("http://earth.google.com/kml/2.1", "Model");

    /** @generated */
    public static final QName MultiGeometry = new QName("http://earth.google.com/kml/2.1",
            "MultiGeometry");

    /** @generated */
    public static final QName NetworkLink = new QName("http://earth.google.com/kml/2.1",
            "NetworkLink");

    /** @generated */
    public static final QName Object = new QName("http://earth.google.com/kml/2.1", "Object");

    /** @generated */
    public static final QName Orientation = new QName("http://earth.google.com/kml/2.1",
            "Orientation");

    /** @generated */
    public static final QName Placemark = new QName("http://earth.google.com/kml/2.1", "Placemark");

    /** @generated */
    public static final QName Point = new QName("http://earth.google.com/kml/2.1", "Point");

    /** @generated */
    public static final QName Polygon = new QName("http://earth.google.com/kml/2.1", "Polygon");

    /** @generated */
    public static final QName PolyStyle = new QName("http://earth.google.com/kml/2.1", "PolyStyle");

    /** @generated */
    public static final QName Region = new QName("http://earth.google.com/kml/2.1", "Region");

    /** @generated */
    public static final QName Scale = new QName("http://earth.google.com/kml/2.1", "Scale");

    /** @generated */
    public static final QName ScreenOverlay = new QName("http://earth.google.com/kml/2.1",
            "ScreenOverlay");

    /** @generated */
    public static final QName Style = new QName("http://earth.google.com/kml/2.1", "Style");

    /** @generated */
    public static final QName StyleMap = new QName("http://earth.google.com/kml/2.1", "StyleMap");

    /** @generated */
    public static final QName StyleSelector = new QName("http://earth.google.com/kml/2.1",
            "StyleSelector");

    /** @generated */
    public static final QName styleUrl = new QName("http://earth.google.com/kml/2.1", "styleUrl");

    /** @generated */
    public static final QName TimePrimitive = new QName("http://earth.google.com/kml/2.1",
            "TimePrimitive");

    /** @generated */
    public static final QName TimeSpan = new QName("http://earth.google.com/kml/2.1", "TimeSpan");

    /** @generated */
    public static final QName TimeStamp = new QName("http://earth.google.com/kml/2.1", "TimeStamp");

    /**
     * private constructor
     */
    private KML() {
    }

    /**
     * Returns the singleton instance.
     */
    public static final KML getInstance() {
        return instance;
    }

    protected void addDependencies(Set dependencies) {
        //TODO: add dependencies here
    }

    /**
     * Returns 'http://earth.google.com/kml/2.1'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'kml21.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("kml21.xsd").toString();
    }

    /* Attributes */
}
