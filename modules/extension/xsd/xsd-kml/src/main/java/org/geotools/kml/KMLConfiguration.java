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

import org.picocontainer.MutablePicoContainer;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.kml.bindings.BoundaryTypeBinding;
import org.geotools.kml.bindings.ColorBinding;
import org.geotools.kml.bindings.ColorStyleTypeBinding;
import org.geotools.kml.bindings.ContainerTypeBinding;
import org.geotools.kml.bindings.CoordinatesTypeBinding;
import org.geotools.kml.bindings.DateTimeTypeBinding;
import org.geotools.kml.bindings.DocumentTypeBinding;
import org.geotools.kml.bindings.FeatureTypeBinding;
import org.geotools.kml.bindings.FolderTypeBinding;
import org.geotools.kml.bindings.GeometryTypeBinding;
import org.geotools.kml.bindings.KmlTypeBinding;
import org.geotools.kml.bindings.LabelStyleTypeBinding;
import org.geotools.kml.bindings.LatLonBoxTypeBinding;
import org.geotools.kml.bindings.LineStringTypeBinding;
import org.geotools.kml.bindings.LineStyleTypeBinding;
import org.geotools.kml.bindings.LinearRingTypeBinding;
import org.geotools.kml.bindings.LocationTypeBinding;
import org.geotools.kml.bindings.LookAtTypeBinding;
import org.geotools.kml.bindings.MetadataTypeBinding;
import org.geotools.kml.bindings.MultiGeometryTypeBinding;
import org.geotools.kml.bindings.ObjectTypeBinding;
import org.geotools.kml.bindings.PlacemarkTypeBinding;
import org.geotools.kml.bindings.PointTypeBinding;
import org.geotools.kml.bindings.PolyStyleTypeBinding;
import org.geotools.kml.bindings.PolygonTypeBinding;
import org.geotools.kml.bindings.RegionTypeBinding;
import org.geotools.kml.bindings.StyleMap;
import org.geotools.kml.bindings.StyleTypeBinding;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.Configuration;


/**
 * Parser configuration for the http://earth.google.com/kml/2.1 schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class KMLConfiguration extends Configuration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public KMLConfiguration() {
        super(KML.getInstance());

        //TODO: add dependencies here
    }

    /**
     * Places an instance of {@link GeometryFactory}.
     */
    protected void configureContext(MutablePicoContainer container) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        StyleBuilder styleBuilder = new StyleBuilder(styleFactory);

        container.registerComponentInstance(styleFactory);
        container.registerComponentInstance(styleBuilder);
        container.registerComponentInstance(new GeometryFactory());
        container.registerComponentInstance(CoordinateArraySequenceFactory.instance());
        container.registerComponentInstance(new StyleMap());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        //Types
        //container.registerComponentImplementation(KML.altitudeModeEnum,
        //    AltitudeModeEnumBinding.class);
        //container.registerComponentImplementation(KML.angle180, Angle180Binding.class);
        //container.registerComponentImplementation(KML.angle360, Angle360Binding.class);
        //container.registerComponentImplementation(KML.angle90, Angle90Binding.class);
        //container.registerComponentImplementation(KML.anglepos90, Anglepos90Binding.class);
        //container.registerComponentImplementation(KML.BalloonStyleType,
        //    BalloonStyleTypeBinding.class);
        container.registerComponentImplementation(KML.boundaryType, BoundaryTypeBinding.class);
        //container.registerComponentImplementation(KML.ChangeType, ChangeTypeBinding.class);
        container.registerComponentImplementation(KML.color, ColorBinding.class);
        //container.registerComponentImplementation(KML.colorModeEnum, ColorModeEnumBinding.class);
        container.registerComponentImplementation(KML.ColorStyleType, ColorStyleTypeBinding.class);
        container.registerComponentImplementation(KML.ContainerType, ContainerTypeBinding.class);
        container.registerComponentImplementation(KML.CoordinatesType, CoordinatesTypeBinding.class);
        //container.registerComponentImplementation(KML.CreateType, CreateTypeBinding.class);
        container.registerComponentImplementation(KML.dateTimeType, DateTimeTypeBinding.class);
        //container.registerComponentImplementation(KML.DeleteType, DeleteTypeBinding.class);
        container.registerComponentImplementation(KML.DocumentType, DocumentTypeBinding.class);
        container.registerComponentImplementation(KML.FeatureType, FeatureTypeBinding.class);
        container.registerComponentImplementation(KML.FolderType, FolderTypeBinding.class);
        container.registerComponentImplementation(KML.GeometryType, GeometryTypeBinding.class);
        //container.registerComponentImplementation(KML.GroundOverlayType,
        //   GroundOverlayTypeBinding.class);
        //container.registerComponentImplementation(KML.IconStyleIconType,
        //    IconStyleIconTypeBinding.class);
        //container.registerComponentImplementation(KML.IconStyleType, IconStyleTypeBinding.class);
        //container.registerComponentImplementation(KML.IconType, IconTypeBinding.class);
        //container.registerComponentImplementation(KML.itemIconStateEnum,
        //    ItemIconStateEnumBinding.class);
        //container.registerComponentImplementation(KML.itemIconStateType,
        //    ItemIconStateTypeBinding.class);
        //container.registerComponentImplementation(KML.ItemIconType, ItemIconTypeBinding.class);
        container.registerComponentImplementation(KML.KmlType, KmlTypeBinding.class);
        container.registerComponentImplementation(KML.LabelStyleType, LabelStyleTypeBinding.class);
        //container.registerComponentImplementation(KML.LatLonAltBoxType,
        //    LatLonAltBoxTypeBinding.class);
        container.registerComponentImplementation(KML.LatLonBoxType, LatLonBoxTypeBinding.class);
        container.registerComponentImplementation(KML.LinearRingType, LinearRingTypeBinding.class);
        container.registerComponentImplementation(KML.LineStringType, LineStringTypeBinding.class);
        container.registerComponentImplementation(KML.LineStyleType, LineStyleTypeBinding.class);
        //container.registerComponentImplementation(KML.LinkType, LinkTypeBinding.class);
        //container.registerComponentImplementation(KML.listItemTypeEnum,
        //    ListItemTypeEnumBinding.class);
        //container.registerComponentImplementation(KML.ListStyleType, ListStyleTypeBinding.class);
        container.registerComponentImplementation(KML.LocationType, LocationTypeBinding.class);
        //container.registerComponentImplementation(KML.LodType, LodTypeBinding.class);
        container.registerComponentImplementation(KML.LookAtType, LookAtTypeBinding.class);
        container.registerComponentImplementation(KML.MetadataType, MetadataTypeBinding.class);
        //container.registerComponentImplementation(KML.ModelType, ModelTypeBinding.class);
        container.registerComponentImplementation(KML.MultiGeometryType,
            MultiGeometryTypeBinding.class);
        //container.registerComponentImplementation(KML.NetworkLinkControlType,
        //    NetworkLinkControlTypeBinding.class);
        //container.registerComponentImplementation(KML.NetworkLinkType, NetworkLinkTypeBinding.class);
        container.registerComponentImplementation(KML.ObjectType, ObjectTypeBinding.class);
        //container.registerComponentImplementation(KML.OrientationType, OrientationTypeBinding.class);
        //container.registerComponentImplementation(KML.OverlayType, OverlayTypeBinding.class);
        container.registerComponentImplementation(KML.PlacemarkType, PlacemarkTypeBinding.class);
        container.registerComponentImplementation(KML.PointType, PointTypeBinding.class);
        container.registerComponentImplementation(KML.PolygonType, PolygonTypeBinding.class);
        container.registerComponentImplementation(KML.PolyStyleType, PolyStyleTypeBinding.class);
        //container.registerComponentImplementation(KML.refreshModeEnum, RefreshModeEnumBinding.class);
        container.registerComponentImplementation(KML.RegionType, RegionTypeBinding.class);
        //container.registerComponentImplementation(KML.ReplaceType, ReplaceTypeBinding.class);
        //container.registerComponentImplementation(KML.ScaleType, ScaleTypeBinding.class);
        //container.registerComponentImplementation(KML.ScreenOverlayType,
        //    ScreenOverlayTypeBinding.class);
        //container.registerComponentImplementation(KML.SnippetType, SnippetTypeBinding.class);
        //container.registerComponentImplementation(KML.StyleMapPairType,
        //    StyleMapPairTypeBinding.class);
        //container.registerComponentImplementation(KML.StyleMapType, StyleMapTypeBinding.class);
        //container.registerComponentImplementation(KML.StyleSelectorType,
        //    StyleSelectorTypeBinding.class);
        //container.registerComponentImplementation(KML.styleStateEnum, StyleStateEnumBinding.class);
        container.registerComponentImplementation(KML.StyleType, StyleTypeBinding.class);

        //container.registerComponentImplementation(KML.TimePrimitiveType,
        //    TimePrimitiveTypeBinding.class);
        //container.registerComponentImplementation(KML.TimeSpanType, TimeSpanTypeBinding.class);
        //container.registerComponentImplementation(KML.TimeStampType, TimeStampTypeBinding.class);
        //container.registerComponentImplementation(KML.unitsEnum, UnitsEnumBinding.class);
        //container.registerComponentImplementation(KML.UpdateType, UpdateTypeBinding.class);
        //container.registerComponentImplementation(KML.vec2Type, Vec2TypeBinding.class);
        //container.registerComponentImplementation(KML.viewRefreshModeEnum,
        //    ViewRefreshModeEnumBinding.class);
    }
}
