package org.geotools.kml.v22;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/kml/2.2 schema.
 *
 * @generated
 */
public final class KML extends XSD {

    /** singleton instance */
    private static final KML instance = new KML();
    
    /**
     * Returns the singleton instance.
     */
    public static final KML getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private KML() {
    }
    
    protected void addDependencies(Set dependencies) {
       //TODO: add dependencies here
    }
    
    /**
     * Returns 'http://www.opengis.net/kml/2.2'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'ogckml22.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("ogckml22.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/kml/2.2";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractColorStyleType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractColorStyleType");
    /** @generated */
    public static final QName AbstractContainerType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractContainerType");
    /** @generated */
    public static final QName AbstractFeatureType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractFeatureType");
    /** @generated */
    public static final QName AbstractGeometryType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractGeometryType");
    /** @generated */
    public static final QName AbstractLatLonBoxType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractLatLonBoxType");
    /** @generated */
    public static final QName AbstractObjectType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractObjectType");
    /** @generated */
    public static final QName AbstractOverlayType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractOverlayType");
    /** @generated */
    public static final QName AbstractStyleSelectorType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractStyleSelectorType");
    /** @generated */
    public static final QName AbstractSubStyleType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractSubStyleType");
    /** @generated */
    public static final QName AbstractTimePrimitiveType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractTimePrimitiveType");
    /** @generated */
    public static final QName AbstractViewType = 
        new QName("http://www.opengis.net/kml/2.2","AbstractViewType");
    /** @generated */
    public static final QName AliasType = 
        new QName("http://www.opengis.net/kml/2.2","AliasType");
    /** @generated */
    public static final QName altitudeModeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","altitudeModeEnumType");
    /** @generated */
    public static final QName angle180Type = 
        new QName("http://www.opengis.net/kml/2.2","angle180Type");
    /** @generated */
    public static final QName angle360Type = 
        new QName("http://www.opengis.net/kml/2.2","angle360Type");
    /** @generated */
    public static final QName angle90Type = 
        new QName("http://www.opengis.net/kml/2.2","angle90Type");
    /** @generated */
    public static final QName anglepos180Type = 
        new QName("http://www.opengis.net/kml/2.2","anglepos180Type");
    /** @generated */
    public static final QName anglepos90Type = 
        new QName("http://www.opengis.net/kml/2.2","anglepos90Type");
    /** @generated */
    public static final QName BalloonStyleType = 
        new QName("http://www.opengis.net/kml/2.2","BalloonStyleType");
    /** @generated */
    public static final QName BasicLinkType = 
        new QName("http://www.opengis.net/kml/2.2","BasicLinkType");
    /** @generated */
    public static final QName BoundaryType = 
        new QName("http://www.opengis.net/kml/2.2","BoundaryType");
    /** @generated */
    public static final QName CameraType = 
        new QName("http://www.opengis.net/kml/2.2","CameraType");
    /** @generated */
    public static final QName ChangeType = 
        new QName("http://www.opengis.net/kml/2.2","ChangeType");
    /** @generated */
    public static final QName colorModeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","colorModeEnumType");
    /** @generated */
    public static final QName colorType = 
        new QName("http://www.opengis.net/kml/2.2","colorType");
    /** @generated */
    public static final QName coordinatesType = 
        new QName("http://www.opengis.net/kml/2.2","coordinatesType");
    /** @generated */
    public static final QName CreateType = 
        new QName("http://www.opengis.net/kml/2.2","CreateType");
    /** @generated */
    public static final QName DataType = 
        new QName("http://www.opengis.net/kml/2.2","DataType");
    /** @generated */
    public static final QName dateTimeType = 
        new QName("http://www.opengis.net/kml/2.2","dateTimeType");
    /** @generated */
    public static final QName DeleteType = 
        new QName("http://www.opengis.net/kml/2.2","DeleteType");
    /** @generated */
    public static final QName displayModeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","displayModeEnumType");
    /** @generated */
    public static final QName DocumentType = 
        new QName("http://www.opengis.net/kml/2.2","DocumentType");
    /** @generated */
    public static final QName ExtendedDataType = 
        new QName("http://www.opengis.net/kml/2.2","ExtendedDataType");
    /** @generated */
    public static final QName FolderType = 
        new QName("http://www.opengis.net/kml/2.2","FolderType");
    /** @generated */
    public static final QName gridOriginEnumType = 
        new QName("http://www.opengis.net/kml/2.2","gridOriginEnumType");
    /** @generated */
    public static final QName GroundOverlayType = 
        new QName("http://www.opengis.net/kml/2.2","GroundOverlayType");
    /** @generated */
    public static final QName IconStyleType = 
        new QName("http://www.opengis.net/kml/2.2","IconStyleType");
    /** @generated */
    public static final QName ImagePyramidType = 
        new QName("http://www.opengis.net/kml/2.2","ImagePyramidType");
    /** @generated */
    public static final QName itemIconStateEnumType = 
        new QName("http://www.opengis.net/kml/2.2","itemIconStateEnumType");
    /** @generated */
    public static final QName itemIconStateType = 
        new QName("http://www.opengis.net/kml/2.2","itemIconStateType");
    /** @generated */
    public static final QName ItemIconType = 
        new QName("http://www.opengis.net/kml/2.2","ItemIconType");
    /** @generated */
    public static final QName KmlType = 
        new QName("http://www.opengis.net/kml/2.2","KmlType");
    /** @generated */
    public static final QName LabelStyleType = 
        new QName("http://www.opengis.net/kml/2.2","LabelStyleType");
    /** @generated */
    public static final QName LatLonAltBoxType = 
        new QName("http://www.opengis.net/kml/2.2","LatLonAltBoxType");
    /** @generated */
    public static final QName LatLonBoxType = 
        new QName("http://www.opengis.net/kml/2.2","LatLonBoxType");
    /** @generated */
    public static final QName LinearRingType = 
        new QName("http://www.opengis.net/kml/2.2","LinearRingType");
    /** @generated */
    public static final QName LineStringType = 
        new QName("http://www.opengis.net/kml/2.2","LineStringType");
    /** @generated */
    public static final QName LineStyleType = 
        new QName("http://www.opengis.net/kml/2.2","LineStyleType");
    /** @generated */
    public static final QName LinkType = 
        new QName("http://www.opengis.net/kml/2.2","LinkType");
    /** @generated */
    public static final QName listItemTypeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","listItemTypeEnumType");
    /** @generated */
    public static final QName ListStyleType = 
        new QName("http://www.opengis.net/kml/2.2","ListStyleType");
    /** @generated */
    public static final QName LocationType = 
        new QName("http://www.opengis.net/kml/2.2","LocationType");
    /** @generated */
    public static final QName LodType = 
        new QName("http://www.opengis.net/kml/2.2","LodType");
    /** @generated */
    public static final QName LookAtType = 
        new QName("http://www.opengis.net/kml/2.2","LookAtType");
    /** @generated */
    public static final QName MetadataType = 
        new QName("http://www.opengis.net/kml/2.2","MetadataType");
    /** @generated */
    public static final QName ModelType = 
        new QName("http://www.opengis.net/kml/2.2","ModelType");
    /** @generated */
    public static final QName MultiGeometryType = 
        new QName("http://www.opengis.net/kml/2.2","MultiGeometryType");
    /** @generated */
    public static final QName NetworkLinkControlType = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkControlType");
    /** @generated */
    public static final QName NetworkLinkType = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkType");
    /** @generated */
    public static final QName OrientationType = 
        new QName("http://www.opengis.net/kml/2.2","OrientationType");
    /** @generated */
    public static final QName PairType = 
        new QName("http://www.opengis.net/kml/2.2","PairType");
    /** @generated */
    public static final QName PhotoOverlayType = 
        new QName("http://www.opengis.net/kml/2.2","PhotoOverlayType");
    /** @generated */
    public static final QName PlacemarkType = 
        new QName("http://www.opengis.net/kml/2.2","PlacemarkType");
    /** @generated */
    public static final QName PointType = 
        new QName("http://www.opengis.net/kml/2.2","PointType");
    /** @generated */
    public static final QName PolygonType = 
        new QName("http://www.opengis.net/kml/2.2","PolygonType");
    /** @generated */
    public static final QName PolyStyleType = 
        new QName("http://www.opengis.net/kml/2.2","PolyStyleType");
    /** @generated */
    public static final QName refreshModeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","refreshModeEnumType");
    /** @generated */
    public static final QName RegionType = 
        new QName("http://www.opengis.net/kml/2.2","RegionType");
    /** @generated */
    public static final QName ResourceMapType = 
        new QName("http://www.opengis.net/kml/2.2","ResourceMapType");
    /** @generated */
    public static final QName ScaleType = 
        new QName("http://www.opengis.net/kml/2.2","ScaleType");
    /** @generated */
    public static final QName SchemaDataType = 
        new QName("http://www.opengis.net/kml/2.2","SchemaDataType");
    /** @generated */
    public static final QName SchemaType = 
        new QName("http://www.opengis.net/kml/2.2","SchemaType");
    /** @generated */
    public static final QName ScreenOverlayType = 
        new QName("http://www.opengis.net/kml/2.2","ScreenOverlayType");
    /** @generated */
    public static final QName shapeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","shapeEnumType");
    /** @generated */
    public static final QName SimpleDataType = 
        new QName("http://www.opengis.net/kml/2.2","SimpleDataType");
    /** @generated */
    public static final QName SimpleFieldType = 
        new QName("http://www.opengis.net/kml/2.2","SimpleFieldType");
    /** @generated */
    public static final QName SnippetType = 
        new QName("http://www.opengis.net/kml/2.2","SnippetType");
    /** @generated */
    public static final QName StyleMapType = 
        new QName("http://www.opengis.net/kml/2.2","StyleMapType");
    /** @generated */
    public static final QName styleStateEnumType = 
        new QName("http://www.opengis.net/kml/2.2","styleStateEnumType");
    /** @generated */
    public static final QName StyleType = 
        new QName("http://www.opengis.net/kml/2.2","StyleType");
    /** @generated */
    public static final QName TimeSpanType = 
        new QName("http://www.opengis.net/kml/2.2","TimeSpanType");
    /** @generated */
    public static final QName TimeStampType = 
        new QName("http://www.opengis.net/kml/2.2","TimeStampType");
    /** @generated */
    public static final QName unitsEnumType = 
        new QName("http://www.opengis.net/kml/2.2","unitsEnumType");
    /** @generated */
    public static final QName UpdateType = 
        new QName("http://www.opengis.net/kml/2.2","UpdateType");
    /** @generated */
    public static final QName vec2Type = 
        new QName("http://www.opengis.net/kml/2.2","vec2Type");
    /** @generated */
    public static final QName viewRefreshModeEnumType = 
        new QName("http://www.opengis.net/kml/2.2","viewRefreshModeEnumType");
    /** @generated */
    public static final QName ViewVolumeType = 
        new QName("http://www.opengis.net/kml/2.2","ViewVolumeType");

    /* Elements */
    /** @generated */
    public static final QName AbstractColorStyleGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractColorStyleGroup");
    /** @generated */
    public static final QName AbstractColorStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractColorStyleObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractColorStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractColorStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractContainerGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractContainerGroup");
    /** @generated */
    public static final QName AbstractContainerObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractContainerObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractContainerSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractContainerSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractFeatureGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractFeatureGroup");
    /** @generated */
    public static final QName AbstractFeatureObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractFeatureObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractFeatureSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractFeatureSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractGeometryGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractGeometryGroup");
    /** @generated */
    public static final QName AbstractGeometryObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractGeometryObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractGeometrySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractGeometrySimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractLatLonBoxObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractLatLonBoxObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractLatLonBoxSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractLatLonBoxSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractObjectGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractObjectGroup");
    /** @generated */
    public static final QName AbstractOverlayGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractOverlayGroup");
    /** @generated */
    public static final QName AbstractOverlayObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractOverlayObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractOverlaySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractOverlaySimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractStyleSelectorGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractStyleSelectorGroup");
    /** @generated */
    public static final QName AbstractStyleSelectorObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractStyleSelectorObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractStyleSelectorSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractStyleSelectorSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractSubStyleGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractSubStyleGroup");
    /** @generated */
    public static final QName AbstractSubStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractSubStyleObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractSubStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractSubStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractTimePrimitiveGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractTimePrimitiveGroup");
    /** @generated */
    public static final QName AbstractTimePrimitiveObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractTimePrimitiveObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractTimePrimitiveSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractTimePrimitiveSimpleExtensionGroup");
    /** @generated */
    public static final QName AbstractViewGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractViewGroup");
    /** @generated */
    public static final QName AbstractViewObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractViewObjectExtensionGroup");
    /** @generated */
    public static final QName AbstractViewSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AbstractViewSimpleExtensionGroup");
    /** @generated */
    public static final QName address = 
        new QName("http://www.opengis.net/kml/2.2","address");
    /** @generated */
    public static final QName Alias = 
        new QName("http://www.opengis.net/kml/2.2","Alias");
    /** @generated */
    public static final QName AliasObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AliasObjectExtensionGroup");
    /** @generated */
    public static final QName AliasSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","AliasSimpleExtensionGroup");
    /** @generated */
    public static final QName altitude = 
        new QName("http://www.opengis.net/kml/2.2","altitude");
    /** @generated */
    public static final QName altitudeMode = 
        new QName("http://www.opengis.net/kml/2.2","altitudeMode");
    /** @generated */
    public static final QName altitudeModeGroup = 
        new QName("http://www.opengis.net/kml/2.2","altitudeModeGroup");
    /** @generated */
    public static final QName BalloonStyle = 
        new QName("http://www.opengis.net/kml/2.2","BalloonStyle");
    /** @generated */
    public static final QName BalloonStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","BalloonStyleObjectExtensionGroup");
    /** @generated */
    public static final QName BalloonStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","BalloonStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName BasicLinkObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","BasicLinkObjectExtensionGroup");
    /** @generated */
    public static final QName BasicLinkSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","BasicLinkSimpleExtensionGroup");
    /** @generated */
    public static final QName begin = 
        new QName("http://www.opengis.net/kml/2.2","begin");
    /** @generated */
    public static final QName bgColor = 
        new QName("http://www.opengis.net/kml/2.2","bgColor");
    /** @generated */
    public static final QName bottomFov = 
        new QName("http://www.opengis.net/kml/2.2","bottomFov");
    /** @generated */
    public static final QName BoundaryObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","BoundaryObjectExtensionGroup");
    /** @generated */
    public static final QName BoundarySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","BoundarySimpleExtensionGroup");
    /** @generated */
    public static final QName Camera = 
        new QName("http://www.opengis.net/kml/2.2","Camera");
    /** @generated */
    public static final QName CameraObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","CameraObjectExtensionGroup");
    /** @generated */
    public static final QName CameraSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","CameraSimpleExtensionGroup");
    /** @generated */
    public static final QName Change = 
        new QName("http://www.opengis.net/kml/2.2","Change");
    /** @generated */
    public static final QName color = 
        new QName("http://www.opengis.net/kml/2.2","color");
    /** @generated */
    public static final QName colorMode = 
        new QName("http://www.opengis.net/kml/2.2","colorMode");
    /** @generated */
    public static final QName cookie = 
        new QName("http://www.opengis.net/kml/2.2","cookie");
    /** @generated */
    public static final QName coordinates = 
        new QName("http://www.opengis.net/kml/2.2","coordinates");
    /** @generated */
    public static final QName Create = 
        new QName("http://www.opengis.net/kml/2.2","Create");
    /** @generated */
    public static final QName Data = 
        new QName("http://www.opengis.net/kml/2.2","Data");
    /** @generated */
    public static final QName DataExtension = 
        new QName("http://www.opengis.net/kml/2.2","DataExtension");
    /** @generated */
    public static final QName Delete = 
        new QName("http://www.opengis.net/kml/2.2","Delete");
    /** @generated */
    public static final QName description = 
        new QName("http://www.opengis.net/kml/2.2","description");
    /** @generated */
    public static final QName displayMode = 
        new QName("http://www.opengis.net/kml/2.2","displayMode");
    /** @generated */
    public static final QName displayName = 
        new QName("http://www.opengis.net/kml/2.2","displayName");
    /** @generated */
    public static final QName Document = 
        new QName("http://www.opengis.net/kml/2.2","Document");
    /** @generated */
    public static final QName DocumentObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","DocumentObjectExtensionGroup");
    /** @generated */
    public static final QName DocumentSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","DocumentSimpleExtensionGroup");
    /** @generated */
    public static final QName drawOrder = 
        new QName("http://www.opengis.net/kml/2.2","drawOrder");
    /** @generated */
    public static final QName east = 
        new QName("http://www.opengis.net/kml/2.2","east");
    /** @generated */
    public static final QName end = 
        new QName("http://www.opengis.net/kml/2.2","end");
    /** @generated */
    public static final QName expires = 
        new QName("http://www.opengis.net/kml/2.2","expires");
    /** @generated */
    public static final QName ExtendedData = 
        new QName("http://www.opengis.net/kml/2.2","ExtendedData");
    /** @generated */
    public static final QName extrude = 
        new QName("http://www.opengis.net/kml/2.2","extrude");
    /** @generated */
    public static final QName fill = 
        new QName("http://www.opengis.net/kml/2.2","fill");
    /** @generated */
    public static final QName flyToView = 
        new QName("http://www.opengis.net/kml/2.2","flyToView");
    /** @generated */
    public static final QName Folder = 
        new QName("http://www.opengis.net/kml/2.2","Folder");
    /** @generated */
    public static final QName FolderObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","FolderObjectExtensionGroup");
    /** @generated */
    public static final QName FolderSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","FolderSimpleExtensionGroup");
    /** @generated */
    public static final QName gridOrigin = 
        new QName("http://www.opengis.net/kml/2.2","gridOrigin");
    /** @generated */
    public static final QName GroundOverlay = 
        new QName("http://www.opengis.net/kml/2.2","GroundOverlay");
    /** @generated */
    public static final QName GroundOverlayObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","GroundOverlayObjectExtensionGroup");
    /** @generated */
    public static final QName GroundOverlaySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","GroundOverlaySimpleExtensionGroup");
    /** @generated */
    public static final QName heading = 
        new QName("http://www.opengis.net/kml/2.2","heading");
    /** @generated */
    public static final QName hotSpot = 
        new QName("http://www.opengis.net/kml/2.2","hotSpot");
    /** @generated */
    public static final QName href = 
        new QName("http://www.opengis.net/kml/2.2","href");
    /** @generated */
    public static final QName httpQuery = 
        new QName("http://www.opengis.net/kml/2.2","httpQuery");
    /** @generated */
    public static final QName Icon = 
        new QName("http://www.opengis.net/kml/2.2","Icon");
    /** @generated */
    public static final QName IconStyle = 
        new QName("http://www.opengis.net/kml/2.2","IconStyle");
    /** @generated */
    public static final QName IconStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","IconStyleObjectExtensionGroup");
    /** @generated */
    public static final QName IconStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","IconStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName ImagePyramid = 
        new QName("http://www.opengis.net/kml/2.2","ImagePyramid");
    /** @generated */
    public static final QName ImagePyramidObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ImagePyramidObjectExtensionGroup");
    /** @generated */
    public static final QName ImagePyramidSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ImagePyramidSimpleExtensionGroup");
    /** @generated */
    public static final QName innerBoundaryIs = 
        new QName("http://www.opengis.net/kml/2.2","innerBoundaryIs");
    /** @generated */
    public static final QName ItemIcon = 
        new QName("http://www.opengis.net/kml/2.2","ItemIcon");
    /** @generated */
    public static final QName ItemIconObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ItemIconObjectExtensionGroup");
    /** @generated */
    public static final QName ItemIconSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ItemIconSimpleExtensionGroup");
    /** @generated */
    public static final QName key = 
        new QName("http://www.opengis.net/kml/2.2","key");
    /** @generated */
    public static final QName kml = 
        new QName("http://www.opengis.net/kml/2.2","kml");
    /** @generated */
    public static final QName KmlObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","KmlObjectExtensionGroup");
    /** @generated */
    public static final QName KmlSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","KmlSimpleExtensionGroup");
    /** @generated */
    public static final QName LabelStyle = 
        new QName("http://www.opengis.net/kml/2.2","LabelStyle");
    /** @generated */
    public static final QName LabelStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LabelStyleObjectExtensionGroup");
    /** @generated */
    public static final QName LabelStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LabelStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName latitude = 
        new QName("http://www.opengis.net/kml/2.2","latitude");
    /** @generated */
    public static final QName LatLonAltBox = 
        new QName("http://www.opengis.net/kml/2.2","LatLonAltBox");
    /** @generated */
    public static final QName LatLonAltBoxObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LatLonAltBoxObjectExtensionGroup");
    /** @generated */
    public static final QName LatLonAltBoxSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LatLonAltBoxSimpleExtensionGroup");
    /** @generated */
    public static final QName LatLonBox = 
        new QName("http://www.opengis.net/kml/2.2","LatLonBox");
    /** @generated */
    public static final QName LatLonBoxObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LatLonBoxObjectExtensionGroup");
    /** @generated */
    public static final QName LatLonBoxSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LatLonBoxSimpleExtensionGroup");
    /** @generated */
    public static final QName leftFov = 
        new QName("http://www.opengis.net/kml/2.2","leftFov");
    /** @generated */
    public static final QName LinearRing = 
        new QName("http://www.opengis.net/kml/2.2","LinearRing");
    /** @generated */
    public static final QName LinearRingObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LinearRingObjectExtensionGroup");
    /** @generated */
    public static final QName LinearRingSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LinearRingSimpleExtensionGroup");
    /** @generated */
    public static final QName LineString = 
        new QName("http://www.opengis.net/kml/2.2","LineString");
    /** @generated */
    public static final QName LineStringObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LineStringObjectExtensionGroup");
    /** @generated */
    public static final QName LineStringSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LineStringSimpleExtensionGroup");
    /** @generated */
    public static final QName LineStyle = 
        new QName("http://www.opengis.net/kml/2.2","LineStyle");
    /** @generated */
    public static final QName LineStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LineStyleObjectExtensionGroup");
    /** @generated */
    public static final QName LineStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LineStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName Link = 
        new QName("http://www.opengis.net/kml/2.2","Link");
    /** @generated */
    public static final QName linkDescription = 
        new QName("http://www.opengis.net/kml/2.2","linkDescription");
    /** @generated */
    public static final QName linkName = 
        new QName("http://www.opengis.net/kml/2.2","linkName");
    /** @generated */
    public static final QName LinkObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LinkObjectExtensionGroup");
    /** @generated */
    public static final QName LinkSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LinkSimpleExtensionGroup");
    /** @generated */
    public static final QName linkSnippet = 
        new QName("http://www.opengis.net/kml/2.2","linkSnippet");
    /** @generated */
    public static final QName listItemType = 
        new QName("http://www.opengis.net/kml/2.2","listItemType");
    /** @generated */
    public static final QName ListStyle = 
        new QName("http://www.opengis.net/kml/2.2","ListStyle");
    /** @generated */
    public static final QName ListStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ListStyleObjectExtensionGroup");
    /** @generated */
    public static final QName ListStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ListStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName Location = 
        new QName("http://www.opengis.net/kml/2.2","Location");
    /** @generated */
    public static final QName LocationObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LocationObjectExtensionGroup");
    /** @generated */
    public static final QName LocationSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LocationSimpleExtensionGroup");
    /** @generated */
    public static final QName Lod = 
        new QName("http://www.opengis.net/kml/2.2","Lod");
    /** @generated */
    public static final QName LodObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LodObjectExtensionGroup");
    /** @generated */
    public static final QName LodSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LodSimpleExtensionGroup");
    /** @generated */
    public static final QName longitude = 
        new QName("http://www.opengis.net/kml/2.2","longitude");
    /** @generated */
    public static final QName LookAt = 
        new QName("http://www.opengis.net/kml/2.2","LookAt");
    /** @generated */
    public static final QName LookAtObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LookAtObjectExtensionGroup");
    /** @generated */
    public static final QName LookAtSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","LookAtSimpleExtensionGroup");
    /** @generated */
    public static final QName maxAltitude = 
        new QName("http://www.opengis.net/kml/2.2","maxAltitude");
    /** @generated */
    public static final QName maxFadeExtent = 
        new QName("http://www.opengis.net/kml/2.2","maxFadeExtent");
    /** @generated */
    public static final QName maxHeight = 
        new QName("http://www.opengis.net/kml/2.2","maxHeight");
    /** @generated */
    public static final QName maxLodPixels = 
        new QName("http://www.opengis.net/kml/2.2","maxLodPixels");
    /** @generated */
    public static final QName maxSessionLength = 
        new QName("http://www.opengis.net/kml/2.2","maxSessionLength");
    /** @generated */
    public static final QName maxSnippetLines = 
        new QName("http://www.opengis.net/kml/2.2","maxSnippetLines");
    /** @generated */
    public static final QName maxWidth = 
        new QName("http://www.opengis.net/kml/2.2","maxWidth");
    /** @generated */
    public static final QName message = 
        new QName("http://www.opengis.net/kml/2.2","message");
    /** @generated */
    public static final QName Metadata = 
        new QName("http://www.opengis.net/kml/2.2","Metadata");
    /** @generated */
    public static final QName minAltitude = 
        new QName("http://www.opengis.net/kml/2.2","minAltitude");
    /** @generated */
    public static final QName minFadeExtent = 
        new QName("http://www.opengis.net/kml/2.2","minFadeExtent");
    /** @generated */
    public static final QName minLodPixels = 
        new QName("http://www.opengis.net/kml/2.2","minLodPixels");
    /** @generated */
    public static final QName minRefreshPeriod = 
        new QName("http://www.opengis.net/kml/2.2","minRefreshPeriod");
    /** @generated */
    public static final QName Model = 
        new QName("http://www.opengis.net/kml/2.2","Model");
    /** @generated */
    public static final QName ModelObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ModelObjectExtensionGroup");
    /** @generated */
    public static final QName ModelSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ModelSimpleExtensionGroup");
    /** @generated */
    public static final QName MultiGeometry = 
        new QName("http://www.opengis.net/kml/2.2","MultiGeometry");
    /** @generated */
    public static final QName MultiGeometryObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","MultiGeometryObjectExtensionGroup");
    /** @generated */
    public static final QName MultiGeometrySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","MultiGeometrySimpleExtensionGroup");
    /** @generated */
    public static final QName name = 
        new QName("http://www.opengis.net/kml/2.2","name");
    /** @generated */
    public static final QName near = 
        new QName("http://www.opengis.net/kml/2.2","near");
    /** @generated */
    public static final QName NetworkLink = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLink");
    /** @generated */
    public static final QName NetworkLinkControl = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkControl");
    /** @generated */
    public static final QName NetworkLinkControlObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkControlObjectExtensionGroup");
    /** @generated */
    public static final QName NetworkLinkControlSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkControlSimpleExtensionGroup");
    /** @generated */
    public static final QName NetworkLinkObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkObjectExtensionGroup");
    /** @generated */
    public static final QName NetworkLinkSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","NetworkLinkSimpleExtensionGroup");
    /** @generated */
    public static final QName north = 
        new QName("http://www.opengis.net/kml/2.2","north");
    /** @generated */
    public static final QName ObjectSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ObjectSimpleExtensionGroup");
    /** @generated */
    public static final QName open = 
        new QName("http://www.opengis.net/kml/2.2","open");
    /** @generated */
    public static final QName Orientation = 
        new QName("http://www.opengis.net/kml/2.2","Orientation");
    /** @generated */
    public static final QName OrientationObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","OrientationObjectExtensionGroup");
    /** @generated */
    public static final QName OrientationSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","OrientationSimpleExtensionGroup");
    /** @generated */
    public static final QName outerBoundaryIs = 
        new QName("http://www.opengis.net/kml/2.2","outerBoundaryIs");
    /** @generated */
    public static final QName outline = 
        new QName("http://www.opengis.net/kml/2.2","outline");
    /** @generated */
    public static final QName overlayXY = 
        new QName("http://www.opengis.net/kml/2.2","overlayXY");
    /** @generated */
    public static final QName Pair = 
        new QName("http://www.opengis.net/kml/2.2","Pair");
    /** @generated */
    public static final QName PairObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PairObjectExtensionGroup");
    /** @generated */
    public static final QName PairSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PairSimpleExtensionGroup");
    /** @generated */
    public static final QName phoneNumber = 
        new QName("http://www.opengis.net/kml/2.2","phoneNumber");
    /** @generated */
    public static final QName PhotoOverlay = 
        new QName("http://www.opengis.net/kml/2.2","PhotoOverlay");
    /** @generated */
    public static final QName PhotoOverlayObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PhotoOverlayObjectExtensionGroup");
    /** @generated */
    public static final QName PhotoOverlaySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PhotoOverlaySimpleExtensionGroup");
    /** @generated */
    public static final QName Placemark = 
        new QName("http://www.opengis.net/kml/2.2","Placemark");
    /** @generated */
    public static final QName PlacemarkObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PlacemarkObjectExtensionGroup");
    /** @generated */
    public static final QName PlacemarkSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PlacemarkSimpleExtensionGroup");
    /** @generated */
    public static final QName Point = 
        new QName("http://www.opengis.net/kml/2.2","Point");
    /** @generated */
    public static final QName PointObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PointObjectExtensionGroup");
    /** @generated */
    public static final QName PointSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PointSimpleExtensionGroup");
    /** @generated */
    public static final QName Polygon = 
        new QName("http://www.opengis.net/kml/2.2","Polygon");
    /** @generated */
    public static final QName PolygonObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PolygonObjectExtensionGroup");
    /** @generated */
    public static final QName PolygonSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PolygonSimpleExtensionGroup");
    /** @generated */
    public static final QName PolyStyle = 
        new QName("http://www.opengis.net/kml/2.2","PolyStyle");
    /** @generated */
    public static final QName PolyStyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PolyStyleObjectExtensionGroup");
    /** @generated */
    public static final QName PolyStyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","PolyStyleSimpleExtensionGroup");
    /** @generated */
    public static final QName range = 
        new QName("http://www.opengis.net/kml/2.2","range");
    /** @generated */
    public static final QName refreshInterval = 
        new QName("http://www.opengis.net/kml/2.2","refreshInterval");
    /** @generated */
    public static final QName refreshMode = 
        new QName("http://www.opengis.net/kml/2.2","refreshMode");
    /** @generated */
    public static final QName refreshVisibility = 
        new QName("http://www.opengis.net/kml/2.2","refreshVisibility");
    /** @generated */
    public static final QName Region = 
        new QName("http://www.opengis.net/kml/2.2","Region");
    /** @generated */
    public static final QName RegionObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","RegionObjectExtensionGroup");
    /** @generated */
    public static final QName RegionSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","RegionSimpleExtensionGroup");
    /** @generated */
    public static final QName ResourceMap = 
        new QName("http://www.opengis.net/kml/2.2","ResourceMap");
    /** @generated */
    public static final QName ResourceMapObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ResourceMapObjectExtensionGroup");
    /** @generated */
    public static final QName ResourceMapSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ResourceMapSimpleExtensionGroup");
    /** @generated */
    public static final QName rightFov = 
        new QName("http://www.opengis.net/kml/2.2","rightFov");
    /** @generated */
    public static final QName roll = 
        new QName("http://www.opengis.net/kml/2.2","roll");
    /** @generated */
    public static final QName rotation = 
        new QName("http://www.opengis.net/kml/2.2","rotation");
    /** @generated */
    public static final QName rotationXY = 
        new QName("http://www.opengis.net/kml/2.2","rotationXY");
    /** @generated */
    public static final QName scale = 
        new QName("http://www.opengis.net/kml/2.2","scale");
    /** @generated */
    public static final QName Scale = 
        new QName("http://www.opengis.net/kml/2.2","Scale");
    /** @generated */
    public static final QName ScaleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ScaleObjectExtensionGroup");
    /** @generated */
    public static final QName ScaleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ScaleSimpleExtensionGroup");
    /** @generated */
    public static final QName Schema = 
        new QName("http://www.opengis.net/kml/2.2","Schema");
    /** @generated */
    public static final QName SchemaData = 
        new QName("http://www.opengis.net/kml/2.2","SchemaData");
    /** @generated */
    public static final QName SchemaDataExtension = 
        new QName("http://www.opengis.net/kml/2.2","SchemaDataExtension");
    /** @generated */
    public static final QName SchemaExtension = 
        new QName("http://www.opengis.net/kml/2.2","SchemaExtension");
    /** @generated */
    public static final QName ScreenOverlay = 
        new QName("http://www.opengis.net/kml/2.2","ScreenOverlay");
    /** @generated */
    public static final QName ScreenOverlayObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ScreenOverlayObjectExtensionGroup");
    /** @generated */
    public static final QName ScreenOverlaySimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ScreenOverlaySimpleExtensionGroup");
    /** @generated */
    public static final QName screenXY = 
        new QName("http://www.opengis.net/kml/2.2","screenXY");
    /** @generated */
    public static final QName shape = 
        new QName("http://www.opengis.net/kml/2.2","shape");
    /** @generated */
    public static final QName SimpleData = 
        new QName("http://www.opengis.net/kml/2.2","SimpleData");
    /** @generated */
    public static final QName SimpleField = 
        new QName("http://www.opengis.net/kml/2.2","SimpleField");
    /** @generated */
    public static final QName SimpleFieldExtension = 
        new QName("http://www.opengis.net/kml/2.2","SimpleFieldExtension");
    /** @generated */
    public static final QName size = 
        new QName("http://www.opengis.net/kml/2.2","size");
    /** @generated */
    public static final QName snippet = 
        new QName("http://www.opengis.net/kml/2.2","snippet");
    /** @generated */
    public static final QName Snippet = 
        new QName("http://www.opengis.net/kml/2.2","Snippet");
    /** @generated */
    public static final QName sourceHref = 
        new QName("http://www.opengis.net/kml/2.2","sourceHref");
    /** @generated */
    public static final QName south = 
        new QName("http://www.opengis.net/kml/2.2","south");
    /** @generated */
    public static final QName state = 
        new QName("http://www.opengis.net/kml/2.2","state");
    /** @generated */
    public static final QName Style = 
        new QName("http://www.opengis.net/kml/2.2","Style");
    /** @generated */
    public static final QName StyleMap = 
        new QName("http://www.opengis.net/kml/2.2","StyleMap");
    /** @generated */
    public static final QName StyleMapObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","StyleMapObjectExtensionGroup");
    /** @generated */
    public static final QName StyleMapSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","StyleMapSimpleExtensionGroup");
    /** @generated */
    public static final QName StyleObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","StyleObjectExtensionGroup");
    /** @generated */
    public static final QName StyleSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","StyleSimpleExtensionGroup");
    /** @generated */
    public static final QName styleUrl = 
        new QName("http://www.opengis.net/kml/2.2","styleUrl");
    /** @generated */
    public static final QName targetHref = 
        new QName("http://www.opengis.net/kml/2.2","targetHref");
    /** @generated */
    public static final QName tessellate = 
        new QName("http://www.opengis.net/kml/2.2","tessellate");
    /** @generated */
    public static final QName text = 
        new QName("http://www.opengis.net/kml/2.2","text");
    /** @generated */
    public static final QName textColor = 
        new QName("http://www.opengis.net/kml/2.2","textColor");
    /** @generated */
    public static final QName tileSize = 
        new QName("http://www.opengis.net/kml/2.2","tileSize");
    /** @generated */
    public static final QName tilt = 
        new QName("http://www.opengis.net/kml/2.2","tilt");
    /** @generated */
    public static final QName TimeSpan = 
        new QName("http://www.opengis.net/kml/2.2","TimeSpan");
    /** @generated */
    public static final QName TimeSpanObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","TimeSpanObjectExtensionGroup");
    /** @generated */
    public static final QName TimeSpanSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","TimeSpanSimpleExtensionGroup");
    /** @generated */
    public static final QName TimeStamp = 
        new QName("http://www.opengis.net/kml/2.2","TimeStamp");
    /** @generated */
    public static final QName TimeStampObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","TimeStampObjectExtensionGroup");
    /** @generated */
    public static final QName TimeStampSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","TimeStampSimpleExtensionGroup");
    /** @generated */
    public static final QName topFov = 
        new QName("http://www.opengis.net/kml/2.2","topFov");
    /** @generated */
    public static final QName Update = 
        new QName("http://www.opengis.net/kml/2.2","Update");
    /** @generated */
    public static final QName UpdateExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","UpdateExtensionGroup");
    /** @generated */
    public static final QName UpdateOpExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","UpdateOpExtensionGroup");
    /** @generated */
    public static final QName Url = 
        new QName("http://www.opengis.net/kml/2.2","Url");
    /** @generated */
    public static final QName value = 
        new QName("http://www.opengis.net/kml/2.2","value");
    /** @generated */
    public static final QName viewBoundScale = 
        new QName("http://www.opengis.net/kml/2.2","viewBoundScale");
    /** @generated */
    public static final QName viewFormat = 
        new QName("http://www.opengis.net/kml/2.2","viewFormat");
    /** @generated */
    public static final QName viewRefreshMode = 
        new QName("http://www.opengis.net/kml/2.2","viewRefreshMode");
    /** @generated */
    public static final QName viewRefreshTime = 
        new QName("http://www.opengis.net/kml/2.2","viewRefreshTime");
    /** @generated */
    public static final QName ViewVolume = 
        new QName("http://www.opengis.net/kml/2.2","ViewVolume");
    /** @generated */
    public static final QName ViewVolumeObjectExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ViewVolumeObjectExtensionGroup");
    /** @generated */
    public static final QName ViewVolumeSimpleExtensionGroup = 
        new QName("http://www.opengis.net/kml/2.2","ViewVolumeSimpleExtensionGroup");
    /** @generated */
    public static final QName visibility = 
        new QName("http://www.opengis.net/kml/2.2","visibility");
    /** @generated */
    public static final QName west = 
        new QName("http://www.opengis.net/kml/2.2","west");
    /** @generated */
    public static final QName when = 
        new QName("http://www.opengis.net/kml/2.2","when");
    /** @generated */
    public static final QName width = 
        new QName("http://www.opengis.net/kml/2.2","width");
    /** @generated */
    public static final QName x = 
        new QName("http://www.opengis.net/kml/2.2","x");
    /** @generated */
    public static final QName y = 
        new QName("http://www.opengis.net/kml/2.2","y");
    /** @generated */
    public static final QName z = 
        new QName("http://www.opengis.net/kml/2.2","z");

    /* Attributes */

}
    