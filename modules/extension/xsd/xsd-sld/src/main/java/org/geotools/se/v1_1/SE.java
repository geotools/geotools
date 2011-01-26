/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1;

import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/se schema.
 *
 * @generated
 */
public final class SE extends XSD {

    /** singleton instance */
    private static final SE instance = new SE();
    
    /**
     * Returns the singleton instance.
     */
    public static final SE getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private SE() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add(OGC.getInstance());
    }
    
    /**
     * Returns 'http://www.opengis.net/se'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'FeatureStyle.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("FeatureStyle.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/se";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AnchorPointType = 
        new QName("http://www.opengis.net/se","AnchorPointType");
    /** @generated */
    public static final QName BaseSymbolizerType = 
        new QName("http://www.opengis.net/se","BaseSymbolizerType");
    /** @generated */
    public static final QName CategorizeType = 
        new QName("http://www.opengis.net/se","CategorizeType");
    /** @generated */
    public static final QName ChangeCaseType = 
        new QName("http://www.opengis.net/se","ChangeCaseType");
    /** @generated */
    public static final QName ChannelSelectionType = 
        new QName("http://www.opengis.net/se","ChannelSelectionType");
    /** @generated */
    public static final QName ColorMapType = 
        new QName("http://www.opengis.net/se","ColorMapType");
    /** @generated */
    public static final QName ColorReplacementType = 
        new QName("http://www.opengis.net/se","ColorReplacementType");
    /** @generated */
    public static final QName ConcatenateType = 
        new QName("http://www.opengis.net/se","ConcatenateType");
    /** @generated */
    public static final QName ContrastEnhancementType = 
        new QName("http://www.opengis.net/se","ContrastEnhancementType");
    /** @generated */
    public static final QName CoverageStyleType = 
        new QName("http://www.opengis.net/se","CoverageStyleType");
    /** @generated */
    public static final QName DescriptionType = 
        new QName("http://www.opengis.net/se","DescriptionType");
    /** @generated */
    public static final QName directionType = 
        new QName("http://www.opengis.net/se","directionType");
    /** @generated */
    public static final QName DisplacementType = 
        new QName("http://www.opengis.net/se","DisplacementType");
    /** @generated */
    public static final QName ElseFilterType = 
        new QName("http://www.opengis.net/se","ElseFilterType");
    /** @generated */
    public static final QName ExternalGraphicType = 
        new QName("http://www.opengis.net/se","ExternalGraphicType");
    /** @generated */
    public static final QName FeatureTypeStyleType = 
        new QName("http://www.opengis.net/se","FeatureTypeStyleType");
    /** @generated */
    public static final QName FillType = 
        new QName("http://www.opengis.net/se","FillType");
    /** @generated */
    public static final QName FontType = 
        new QName("http://www.opengis.net/se","FontType");
    /** @generated */
    public static final QName FormatDateType = 
        new QName("http://www.opengis.net/se","FormatDateType");
    /** @generated */
    public static final QName FormatNumberType = 
        new QName("http://www.opengis.net/se","FormatNumberType");
    /** @generated */
    public static final QName FunctionType = 
        new QName("http://www.opengis.net/se","FunctionType");
    /** @generated */
    public static final QName GeometryType = 
        new QName("http://www.opengis.net/se","GeometryType");
    /** @generated */
    public static final QName GraphicFillType = 
        new QName("http://www.opengis.net/se","GraphicFillType");
    /** @generated */
    public static final QName GraphicStrokeType = 
        new QName("http://www.opengis.net/se","GraphicStrokeType");
    /** @generated */
    public static final QName GraphicType = 
        new QName("http://www.opengis.net/se","GraphicType");
    /** @generated */
    public static final QName HaloType = 
        new QName("http://www.opengis.net/se","HaloType");
    /** @generated */
    public static final QName HistogramType = 
        new QName("http://www.opengis.net/se","HistogramType");
    /** @generated */
    public static final QName ImageOutlineType = 
        new QName("http://www.opengis.net/se","ImageOutlineType");
    /** @generated */
    public static final QName InlineContentType = 
        new QName("http://www.opengis.net/se","InlineContentType");
    /** @generated */
    public static final QName InterpolateType = 
        new QName("http://www.opengis.net/se","InterpolateType");
    /** @generated */
    public static final QName InterpolationPointType = 
        new QName("http://www.opengis.net/se","InterpolationPointType");
    /** @generated */
    public static final QName LabelPlacementType = 
        new QName("http://www.opengis.net/se","LabelPlacementType");
    /** @generated */
    public static final QName LegendGraphicType = 
        new QName("http://www.opengis.net/se","LegendGraphicType");
    /** @generated */
    public static final QName LinePlacementType = 
        new QName("http://www.opengis.net/se","LinePlacementType");
    /** @generated */
    public static final QName LineSymbolizerType = 
        new QName("http://www.opengis.net/se","LineSymbolizerType");
    /** @generated */
    public static final QName MapItemType = 
        new QName("http://www.opengis.net/se","MapItemType");
    /** @generated */
    public static final QName MarkType = 
        new QName("http://www.opengis.net/se","MarkType");
    /** @generated */
    public static final QName MethodType = 
        new QName("http://www.opengis.net/se","MethodType");
    /** @generated */
    public static final QName ModeType = 
        new QName("http://www.opengis.net/se","ModeType");
    /** @generated */
    public static final QName NormalizeType = 
        new QName("http://www.opengis.net/se","NormalizeType");
    /** @generated */
    public static final QName OnlineResourceType = 
        new QName("http://www.opengis.net/se","OnlineResourceType");
    /** @generated */
    public static final QName ParameterValueType = 
        new QName("http://www.opengis.net/se","ParameterValueType");
    /** @generated */
    public static final QName PointPlacementType = 
        new QName("http://www.opengis.net/se","PointPlacementType");
    /** @generated */
    public static final QName PointSymbolizerType = 
        new QName("http://www.opengis.net/se","PointSymbolizerType");
    /** @generated */
    public static final QName PolygonSymbolizerType = 
        new QName("http://www.opengis.net/se","PolygonSymbolizerType");
    /** @generated */
    public static final QName RasterSymbolizerType = 
        new QName("http://www.opengis.net/se","RasterSymbolizerType");
    /** @generated */
    public static final QName RecodeType = 
        new QName("http://www.opengis.net/se","RecodeType");
    /** @generated */
    public static final QName RuleType = 
        new QName("http://www.opengis.net/se","RuleType");
    /** @generated */
    public static final QName searchDirectionType = 
        new QName("http://www.opengis.net/se","searchDirectionType");
    /** @generated */
    public static final QName SelectedChannelType = 
        new QName("http://www.opengis.net/se","SelectedChannelType");
    /** @generated */
    public static final QName ShadedReliefType = 
        new QName("http://www.opengis.net/se","ShadedReliefType");
    /** @generated */
    public static final QName StringLengthType = 
        new QName("http://www.opengis.net/se","StringLengthType");
    /** @generated */
    public static final QName StringPositionType = 
        new QName("http://www.opengis.net/se","StringPositionType");
    /** @generated */
    public static final QName stripOffPositionType = 
        new QName("http://www.opengis.net/se","stripOffPositionType");
    /** @generated */
    public static final QName StrokeType = 
        new QName("http://www.opengis.net/se","StrokeType");
    /** @generated */
    public static final QName SubstringType = 
        new QName("http://www.opengis.net/se","SubstringType");
    /** @generated */
    public static final QName SvgParameterType = 
        new QName("http://www.opengis.net/se","SvgParameterType");
    /** @generated */
    public static final QName SymbolizerType = 
        new QName("http://www.opengis.net/se","SymbolizerType");
    /** @generated */
    public static final QName TextSymbolizerType = 
        new QName("http://www.opengis.net/se","TextSymbolizerType");
    /** @generated */
    public static final QName ThreshholdsBelongToType = 
        new QName("http://www.opengis.net/se","ThreshholdsBelongToType");
    /** @generated */
    public static final QName TrimType = 
        new QName("http://www.opengis.net/se","TrimType");
    /** @generated */
    public static final QName VersionType = 
        new QName("http://www.opengis.net/se","VersionType");
    /** @generated */
    public static final QName _OverlapBehavior = 
        new QName("http://www.opengis.net/se","_OverlapBehavior");

    /* Elements */
    /** @generated */
    public static final QName AnchorPoint = 
        new QName("http://www.opengis.net/se","AnchorPoint");
    /** @generated */
    public static final QName AnchorPointX = 
        new QName("http://www.opengis.net/se","AnchorPointX");
    /** @generated */
    public static final QName AnchorPointY = 
        new QName("http://www.opengis.net/se","AnchorPointY");
    /** @generated */
    public static final QName BaseSymbolizer = 
        new QName("http://www.opengis.net/se","BaseSymbolizer");
    /** @generated */
    public static final QName BlueChannel = 
        new QName("http://www.opengis.net/se","BlueChannel");
    /** @generated */
    public static final QName BrightnessOnly = 
        new QName("http://www.opengis.net/se","BrightnessOnly");
    /** @generated */
    public static final QName Categorize = 
        new QName("http://www.opengis.net/se","Categorize");
    /** @generated */
    public static final QName ChangeCase = 
        new QName("http://www.opengis.net/se","ChangeCase");
    /** @generated */
    public static final QName ChannelSelection = 
        new QName("http://www.opengis.net/se","ChannelSelection");
    /** @generated */
    public static final QName ColorMap = 
        new QName("http://www.opengis.net/se","ColorMap");
    /** @generated */
    public static final QName ColorReplacement = 
        new QName("http://www.opengis.net/se","ColorReplacement");
    /** @generated */
    public static final QName Concatenate = 
        new QName("http://www.opengis.net/se","Concatenate");
    /** @generated */
    public static final QName ContrastEnhancement = 
        new QName("http://www.opengis.net/se","ContrastEnhancement");
    /** @generated */
    public static final QName CoverageName = 
        new QName("http://www.opengis.net/se","CoverageName");
    /** @generated */
    public static final QName CoverageStyle = 
        new QName("http://www.opengis.net/se","CoverageStyle");
    /** @generated */
    public static final QName Data = 
        new QName("http://www.opengis.net/se","Data");
    /** @generated */
    public static final QName DateValue = 
        new QName("http://www.opengis.net/se","DateValue");
    /** @generated */
    public static final QName Description = 
        new QName("http://www.opengis.net/se","Description");
    /** @generated */
    public static final QName Displacement = 
        new QName("http://www.opengis.net/se","Displacement");
    /** @generated */
    public static final QName DisplacementX = 
        new QName("http://www.opengis.net/se","DisplacementX");
    /** @generated */
    public static final QName DisplacementY = 
        new QName("http://www.opengis.net/se","DisplacementY");
    /** @generated */
    public static final QName ElseFilter = 
        new QName("http://www.opengis.net/se","ElseFilter");
    /** @generated */
    public static final QName ExternalGraphic = 
        new QName("http://www.opengis.net/se","ExternalGraphic");
    /** @generated */
    public static final QName FeatureTypeName = 
        new QName("http://www.opengis.net/se","FeatureTypeName");
    /** @generated */
    public static final QName FeatureTypeStyle = 
        new QName("http://www.opengis.net/se","FeatureTypeStyle");
    /** @generated */
    public static final QName Fill = 
        new QName("http://www.opengis.net/se","Fill");
    /** @generated */
    public static final QName Font = 
        new QName("http://www.opengis.net/se","Font");
    /** @generated */
    public static final QName Format = 
        new QName("http://www.opengis.net/se","Format");
    /** @generated */
    public static final QName FormatDate = 
        new QName("http://www.opengis.net/se","FormatDate");
    /** @generated */
    public static final QName FormatNumber = 
        new QName("http://www.opengis.net/se","FormatNumber");
    /** @generated */
    public static final QName Function = 
        new QName("http://www.opengis.net/se","Function");
    /** @generated */
    public static final QName GammaValue = 
        new QName("http://www.opengis.net/se","GammaValue");
    /** @generated */
    public static final QName Gap = 
        new QName("http://www.opengis.net/se","Gap");
    /** @generated */
    public static final QName GeneralizeLine = 
        new QName("http://www.opengis.net/se","GeneralizeLine");
    /** @generated */
    public static final QName Geometry = 
        new QName("http://www.opengis.net/se","Geometry");
    /** @generated */
    public static final QName Graphic = 
        new QName("http://www.opengis.net/se","Graphic");
    /** @generated */
    public static final QName GraphicFill = 
        new QName("http://www.opengis.net/se","GraphicFill");
    /** @generated */
    public static final QName GraphicStroke = 
        new QName("http://www.opengis.net/se","GraphicStroke");
    /** @generated */
    public static final QName GrayChannel = 
        new QName("http://www.opengis.net/se","GrayChannel");
    /** @generated */
    public static final QName GreenChannel = 
        new QName("http://www.opengis.net/se","GreenChannel");
    /** @generated */
    public static final QName Halo = 
        new QName("http://www.opengis.net/se","Halo");
    /** @generated */
    public static final QName Histogram = 
        new QName("http://www.opengis.net/se","Histogram");
    /** @generated */
    public static final QName ImageOutline = 
        new QName("http://www.opengis.net/se","ImageOutline");
    /** @generated */
    public static final QName InitialGap = 
        new QName("http://www.opengis.net/se","InitialGap");
    /** @generated */
    public static final QName InlineContent = 
        new QName("http://www.opengis.net/se","InlineContent");
    /** @generated */
    public static final QName Interpolate = 
        new QName("http://www.opengis.net/se","Interpolate");
    /** @generated */
    public static final QName InterpolationPoint = 
        new QName("http://www.opengis.net/se","InterpolationPoint");
    /** @generated */
    public static final QName IsAligned = 
        new QName("http://www.opengis.net/se","IsAligned");
    /** @generated */
    public static final QName IsRepeated = 
        new QName("http://www.opengis.net/se","IsRepeated");
    /** @generated */
    public static final QName Label = 
        new QName("http://www.opengis.net/se","Label");
    /** @generated */
    public static final QName LabelPlacement = 
        new QName("http://www.opengis.net/se","LabelPlacement");
    /** @generated */
    public static final QName LegendGraphic = 
        new QName("http://www.opengis.net/se","LegendGraphic");
    /** @generated */
    public static final QName Length = 
        new QName("http://www.opengis.net/se","Length");
    /** @generated */
    public static final QName LinePlacement = 
        new QName("http://www.opengis.net/se","LinePlacement");
    /** @generated */
    public static final QName LineSymbolizer = 
        new QName("http://www.opengis.net/se","LineSymbolizer");
    /** @generated */
    public static final QName LookupString = 
        new QName("http://www.opengis.net/se","LookupString");
    /** @generated */
    public static final QName LookupValue = 
        new QName("http://www.opengis.net/se","LookupValue");
    /** @generated */
    public static final QName MapItem = 
        new QName("http://www.opengis.net/se","MapItem");
    /** @generated */
    public static final QName Mark = 
        new QName("http://www.opengis.net/se","Mark");
    /** @generated */
    public static final QName MarkIndex = 
        new QName("http://www.opengis.net/se","MarkIndex");
    /** @generated */
    public static final QName MaxScaleDenominator = 
        new QName("http://www.opengis.net/se","MaxScaleDenominator");
    /** @generated */
    public static final QName MinScaleDenominator = 
        new QName("http://www.opengis.net/se","MinScaleDenominator");
    /** @generated */
    public static final QName Name = 
        new QName("http://www.opengis.net/se","Name");
    /** @generated */
    public static final QName NegativePattern = 
        new QName("http://www.opengis.net/se","NegativePattern");
    /** @generated */
    public static final QName Normalize = 
        new QName("http://www.opengis.net/se","Normalize");
    /** @generated */
    public static final QName NumericValue = 
        new QName("http://www.opengis.net/se","NumericValue");
    /** @generated */
    public static final QName OnlineResource = 
        new QName("http://www.opengis.net/se","OnlineResource");
    /** @generated */
    public static final QName Opacity = 
        new QName("http://www.opengis.net/se","Opacity");
    /** @generated */
    public static final QName OverlapBehavior = 
        new QName("http://www.opengis.net/se","OverlapBehavior");
    /** @generated */
    public static final QName Pattern = 
        new QName("http://www.opengis.net/se","Pattern");
    /** @generated */
    public static final QName PerpendicularOffset = 
        new QName("http://www.opengis.net/se","PerpendicularOffset");
    /** @generated */
    public static final QName PointPlacement = 
        new QName("http://www.opengis.net/se","PointPlacement");
    /** @generated */
    public static final QName PointSymbolizer = 
        new QName("http://www.opengis.net/se","PointSymbolizer");
    /** @generated */
    public static final QName PolygonSymbolizer = 
        new QName("http://www.opengis.net/se","PolygonSymbolizer");
    /** @generated */
    public static final QName Position = 
        new QName("http://www.opengis.net/se","Position");
    /** @generated */
    public static final QName Radius = 
        new QName("http://www.opengis.net/se","Radius");
    /** @generated */
    public static final QName RasterSymbolizer = 
        new QName("http://www.opengis.net/se","RasterSymbolizer");
    /** @generated */
    public static final QName Recode = 
        new QName("http://www.opengis.net/se","Recode");
    /** @generated */
    public static final QName RedChannel = 
        new QName("http://www.opengis.net/se","RedChannel");
    /** @generated */
    public static final QName ReliefFactor = 
        new QName("http://www.opengis.net/se","ReliefFactor");
    /** @generated */
    public static final QName Rotation = 
        new QName("http://www.opengis.net/se","Rotation");
    /** @generated */
    public static final QName Rule = 
        new QName("http://www.opengis.net/se","Rule");
    /** @generated */
    public static final QName SemanticTypeIdentifier = 
        new QName("http://www.opengis.net/se","SemanticTypeIdentifier");
    /** @generated */
    public static final QName ShadedRelief = 
        new QName("http://www.opengis.net/se","ShadedRelief");
    /** @generated */
    public static final QName Size = 
        new QName("http://www.opengis.net/se","Size");
    /** @generated */
    public static final QName SourceChannelName = 
        new QName("http://www.opengis.net/se","SourceChannelName");
    /** @generated */
    public static final QName StringLength = 
        new QName("http://www.opengis.net/se","StringLength");
    /** @generated */
    public static final QName StringPosition = 
        new QName("http://www.opengis.net/se","StringPosition");
    /** @generated */
    public static final QName StringValue = 
        new QName("http://www.opengis.net/se","StringValue");
    /** @generated */
    public static final QName Stroke = 
        new QName("http://www.opengis.net/se","Stroke");
    /** @generated */
    public static final QName Substring = 
        new QName("http://www.opengis.net/se","Substring");
    /** @generated */
    public static final QName SvgParameter = 
        new QName("http://www.opengis.net/se","SvgParameter");
    /** @generated */
    public static final QName Symbolizer = 
        new QName("http://www.opengis.net/se","Symbolizer");
    /** @generated */
    public static final QName TextSymbolizer = 
        new QName("http://www.opengis.net/se","TextSymbolizer");
    /** @generated */
    public static final QName Threshold = 
        new QName("http://www.opengis.net/se","Threshold");
    /** @generated */
    public static final QName Trim = 
        new QName("http://www.opengis.net/se","Trim");
    /** @generated */
    public static final QName Value = 
        new QName("http://www.opengis.net/se","Value");
    /** @generated */
    public static final QName WellKnownName = 
        new QName("http://www.opengis.net/se","WellKnownName");

    public static final QName VendorOption = 
        new QName("http://www.opengis.net/se","VendorOption");
    
    /* Attributes */

}
    