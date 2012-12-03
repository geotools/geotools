package org.geotools.kml.v22;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.kml.FolderStack;
import org.geotools.kml.StyleMap;
import org.geotools.kml.bindings.BoundaryTypeBinding;
import org.geotools.kml.bindings.ColorBinding;
import org.geotools.kml.bindings.ColorStyleTypeBinding;
import org.geotools.kml.bindings.ContainerTypeBinding;
import org.geotools.kml.bindings.CoordinatesTypeBinding;
import org.geotools.kml.bindings.DateTimeTypeBinding;
import org.geotools.kml.bindings.DocumentTypeBinding;
import org.geotools.kml.bindings.FeatureTypeBinding;
import org.geotools.kml.bindings.FolderBinding;
import org.geotools.kml.bindings.FolderTypeBinding;
import org.geotools.kml.bindings.GeometryTypeBinding;
import org.geotools.kml.bindings.KmlTypeBinding;
import org.geotools.kml.bindings.LabelStyleTypeBinding;
import org.geotools.kml.bindings.LatLonBoxTypeBinding;
import org.geotools.kml.bindings.LineStringTypeBinding;
import org.geotools.kml.bindings.LinearRingTypeBinding;
import org.geotools.kml.bindings.LocationTypeBinding;
import org.geotools.kml.bindings.LookAtTypeBinding;
import org.geotools.kml.bindings.MetadataTypeBinding;
import org.geotools.kml.bindings.MultiGeometryTypeBinding;
import org.geotools.kml.bindings.NameBinding;
import org.geotools.kml.bindings.ObjectTypeBinding;
import org.geotools.kml.bindings.PlacemarkTypeBinding;
import org.geotools.kml.bindings.PointTypeBinding;
import org.geotools.kml.bindings.PolygonTypeBinding;
import org.geotools.kml.bindings.RegionTypeBinding;
import org.geotools.kml.bindings.StyleTypeBinding;
import org.geotools.kml.v22.bindings.ExtendedDataTypeBinding;
import org.geotools.kml.v22.bindings.SchemaDataTypeBinding;
import org.geotools.kml.v22.bindings.SchemaTypeBinding;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

/**
 * Parser configuration for the http://www.opengis.net/kml/2.2 schema.
 *
 * @generated
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

    @Override
    protected void configureContext(MutablePicoContainer container) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        StyleBuilder styleBuilder = new StyleBuilder(styleFactory);

        container.registerComponentInstance(styleFactory);
        container.registerComponentInstance(styleBuilder);
        container.registerComponentInstance(new GeometryFactory());
        container.registerComponentInstance(CoordinateArraySequenceFactory.instance());
        container.registerComponentInstance(new StyleMap());
        container.registerComponentInstance(new FolderStack());
        SchemaRegistry schemaRegistry = new SchemaRegistry();
        KMLCustomSchemaHandlerFactory handlerFactory = new KMLCustomSchemaHandlerFactory(
                schemaRegistry);
        container.registerComponentInstance(schemaRegistry);
        container.registerComponentInstance(handlerFactory);
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
//        container.registerComponentImplementation(KML.AbstractColorStyleType,AbstractColorStyleTypeBinding.class);
        container.registerComponentImplementation(KML.AbstractColorStyleType,ColorStyleTypeBinding.class);
        container.registerComponentImplementation(KML.AbstractContainerType,ContainerTypeBinding.class);
        container.registerComponentImplementation(KML.AbstractFeatureType,FeatureTypeBinding.class);
        container.registerComponentImplementation(KML.AbstractGeometryType,GeometryTypeBinding.class);
//        container.registerComponentImplementation(KML.AbstractLatLonBoxType,AbstractLatLonBoxTypeBinding.class);
        container.registerComponentImplementation(KML.AbstractObjectType,ObjectTypeBinding.class);
//        container.registerComponentImplementation(KML.AbstractOverlayType,AbstractOverlayTypeBinding.class);
//        container.registerComponentImplementation(KML.AbstractStyleSelectorType,AbstractStyleSelectorTypeBinding.class);
//        container.registerComponentImplementation(KML.AbstractSubStyleType,AbstractSubStyleTypeBinding.class);
//        container.registerComponentImplementation(KML.AbstractTimePrimitiveType,AbstractTimePrimitiveTypeBinding.class);
//        container.registerComponentImplementation(KML.AbstractViewType,AbstractViewTypeBinding.class);
//        container.registerComponentImplementation(KML.AliasType,AliasTypeBinding.class);
//        container.registerComponentImplementation(KML.altitudeModeEnumType,AltitudeModeEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.angle180Type,Angle180TypeBinding.class);
//        container.registerComponentImplementation(KML.angle360Type,Angle360TypeBinding.class);
//        container.registerComponentImplementation(KML.angle90Type,Angle90TypeBinding.class);
//        container.registerComponentImplementation(KML.anglepos180Type,Anglepos180TypeBinding.class);
//        container.registerComponentImplementation(KML.anglepos90Type,Anglepos90TypeBinding.class);
//        container.registerComponentImplementation(KML.BalloonStyleType,BalloonStyleTypeBinding.class);
//        container.registerComponentImplementation(KML.BasicLinkType,BasicLinkTypeBinding.class);
        container.registerComponentImplementation(KML.BoundaryType,BoundaryTypeBinding.class);
//        container.registerComponentImplementation(KML.CameraType,CameraTypeBinding.class);
//        container.registerComponentImplementation(KML.ChangeType,ChangeTypeBinding.class);
//        container.registerComponentImplementation(KML.colorModeEnumType,ColorModeEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.colorType,ColorTypeBinding.class);
        container.registerComponentImplementation(KML.coordinatesType,CoordinatesTypeBinding.class);
//        container.registerComponentImplementation(KML.CreateType,CreateTypeBinding.class);
//        container.registerComponentImplementation(KML.DataType,DataTypeBinding.class);
        container.registerComponentImplementation(KML.dateTimeType,DateTimeTypeBinding.class);
//        container.registerComponentImplementation(KML.DeleteType,DeleteTypeBinding.class);
//        container.registerComponentImplementation(KML.displayModeEnumType,DisplayModeEnumTypeBinding.class);
        container.registerComponentImplementation(KML.DocumentType,DocumentTypeBinding.class);
        container.registerComponentImplementation(KML.ExtendedDataType,ExtendedDataTypeBinding.class);
        container.registerComponentImplementation(KML.FolderType,FolderTypeBinding.class);
        container.registerComponentImplementation(KML.Folder,FolderBinding.class);
//        container.registerComponentImplementation(KML.gridOriginEnumType,GridOriginEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.GroundOverlayType,GroundOverlayTypeBinding.class);
//        container.registerComponentImplementation(KML.IconStyleType,IconStyleTypeBinding.class);
//        container.registerComponentImplementation(KML.ImagePyramidType,ImagePyramidTypeBinding.class);
//        container.registerComponentImplementation(KML.itemIconStateEnumType,ItemIconStateEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.itemIconStateType,ItemIconStateTypeBinding.class);
//        container.registerComponentImplementation(KML.ItemIconType,ItemIconTypeBinding.class);
        container.registerComponentImplementation(KML.KmlType,KmlTypeBinding.class);
        container.registerComponentImplementation(KML.LabelStyleType,LabelStyleTypeBinding.class);
//        container.registerComponentImplementation(KML.LatLonAltBoxType,LatLonAltBoxTypeBinding.class);
        container.registerComponentImplementation(KML.LatLonBoxType,LatLonBoxTypeBinding.class);
        container.registerComponentImplementation(KML.LinearRingType,LinearRingTypeBinding.class);
        container.registerComponentImplementation(KML.LineStringType,LineStringTypeBinding.class);
//        container.registerComponentImplementation(KML.LineStyleType,LineStyleTypeBinding.class);
//        container.registerComponentImplementation(KML.LinkType,LinkTypeBinding.class);
//        container.registerComponentImplementation(KML.listItemTypeEnumType,ListItemTypeEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.ListStyleType,ListStyleTypeBinding.class);
        container.registerComponentImplementation(KML.LocationType,LocationTypeBinding.class);
//        container.registerComponentImplementation(KML.LodType,LodTypeBinding.class);
        container.registerComponentImplementation(KML.LookAtType,LookAtTypeBinding.class);
        container.registerComponentImplementation(KML.MetadataType,MetadataTypeBinding.class);
//        container.registerComponentImplementation(KML.ModelType,ModelTypeBinding.class);
        container.registerComponentImplementation(KML.MultiGeometryType,MultiGeometryTypeBinding.class);
//        container.registerComponentImplementation(KML.NetworkLinkControlType,NetworkLinkControlTypeBinding.class);
//        container.registerComponentImplementation(KML.NetworkLinkType,NetworkLinkTypeBinding.class);
//        container.registerComponentImplementation(KML.OrientationType,OrientationTypeBinding.class);
//        container.registerComponentImplementation(KML.PairType,PairTypeBinding.class);
//        container.registerComponentImplementation(KML.PhotoOverlayType,PhotoOverlayTypeBinding.class);
        container.registerComponentImplementation(KML.PlacemarkType,PlacemarkTypeBinding.class);
        container.registerComponentImplementation(KML.PointType,PointTypeBinding.class);
        container.registerComponentImplementation(KML.PolygonType,PolygonTypeBinding.class);
//        container.registerComponentImplementation(KML.PolyStyleType,PolyStyleTypeBinding.class);
//        container.registerComponentImplementation(KML.refreshModeEnumType,RefreshModeEnumTypeBinding.class);
        container.registerComponentImplementation(KML.RegionType,RegionTypeBinding.class);
//        container.registerComponentImplementation(KML.ResourceMapType,ResourceMapTypeBinding.class);
//        container.registerComponentImplementation(KML.ScaleType,ScaleTypeBinding.class);
        container.registerComponentImplementation(KML.SchemaDataType,SchemaDataTypeBinding.class);
        container.registerComponentImplementation(KML.SchemaType,SchemaTypeBinding.class);
//        container.registerComponentImplementation(KML.ScreenOverlayType,ScreenOverlayTypeBinding.class);
//        container.registerComponentImplementation(KML.shapeEnumType,ShapeEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.SimpleDataType,SimpleDataTypeBinding.class);
//        container.registerComponentImplementation(KML.SimpleFieldType,SimpleFieldTypeBinding.class);
//        container.registerComponentImplementation(KML.SnippetType,SnippetTypeBinding.class);
//        container.registerComponentImplementation(KML.StyleMapType,StyleMapTypeBinding.class);
//        container.registerComponentImplementation(KML.styleStateEnumType,StyleStateEnumTypeBinding.class);
        container.registerComponentImplementation(KML.StyleType,StyleTypeBinding.class);
//        container.registerComponentImplementation(KML.TimeSpanType,TimeSpanTypeBinding.class);
//        container.registerComponentImplementation(KML.TimeStampType,TimeStampTypeBinding.class);
//        container.registerComponentImplementation(KML.unitsEnumType,UnitsEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.UpdateType,UpdateTypeBinding.class);
//        container.registerComponentImplementation(KML.vec2Type,Vec2TypeBinding.class);
//        container.registerComponentImplementation(KML.viewRefreshModeEnumType,ViewRefreshModeEnumTypeBinding.class);
//        container.registerComponentImplementation(KML.ViewVolumeType,ViewVolumeTypeBinding.class);

        container.registerComponentImplementation(KML.name, NameBinding.class);

        container.registerComponentImplementation(KML.color,ColorBinding.class);
    
    }
} 