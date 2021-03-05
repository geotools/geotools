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

import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.se.v1_1.bindings.AnchorPointBinding;
import org.geotools.se.v1_1.bindings.BaseSymbolizerBinding;
import org.geotools.se.v1_1.bindings.BrightnessOnlyBinding;
import org.geotools.se.v1_1.bindings.CategorizeBinding;
import org.geotools.se.v1_1.bindings.ChangeCaseBinding;
import org.geotools.se.v1_1.bindings.ChannelSelectionBinding;
import org.geotools.se.v1_1.bindings.ColorMapBinding;
import org.geotools.se.v1_1.bindings.ConcatenateBinding;
import org.geotools.se.v1_1.bindings.ContrastEnhancementBinding;
import org.geotools.se.v1_1.bindings.CoverageNameBinding;
import org.geotools.se.v1_1.bindings.CoverageStyleBinding;
import org.geotools.se.v1_1.bindings.DescriptionBinding;
import org.geotools.se.v1_1.bindings.DirectionTypeBinding;
import org.geotools.se.v1_1.bindings.DisplacementBinding;
import org.geotools.se.v1_1.bindings.ExternalGraphicBinding;
import org.geotools.se.v1_1.bindings.FeatureTypeStyleBinding;
import org.geotools.se.v1_1.bindings.FillBinding;
import org.geotools.se.v1_1.bindings.FontBinding;
import org.geotools.se.v1_1.bindings.FormatDateBinding;
import org.geotools.se.v1_1.bindings.FormatNumberBinding;
import org.geotools.se.v1_1.bindings.GeometryBinding;
import org.geotools.se.v1_1.bindings.GraphicBinding;
import org.geotools.se.v1_1.bindings.GraphicFillBinding;
import org.geotools.se.v1_1.bindings.GraphicStrokeBinding;
import org.geotools.se.v1_1.bindings.HaloBinding;
import org.geotools.se.v1_1.bindings.HistogramBinding;
import org.geotools.se.v1_1.bindings.ImageOutlineBinding;
import org.geotools.se.v1_1.bindings.InlineContentBinding;
import org.geotools.se.v1_1.bindings.InterpolateBinding;
import org.geotools.se.v1_1.bindings.InterpolationPointBinding;
import org.geotools.se.v1_1.bindings.LabelPlacementBinding;
import org.geotools.se.v1_1.bindings.LegendGraphicBinding;
import org.geotools.se.v1_1.bindings.LinePlacementBinding;
import org.geotools.se.v1_1.bindings.LineSymbolizerBinding;
import org.geotools.se.v1_1.bindings.MarkBinding;
import org.geotools.se.v1_1.bindings.NormalizeBinding;
import org.geotools.se.v1_1.bindings.OnlineResourceBinding;
import org.geotools.se.v1_1.bindings.OverlapBehaviorBinding;
import org.geotools.se.v1_1.bindings.ParameterValueTypeBinding;
import org.geotools.se.v1_1.bindings.PointPlacementBinding;
import org.geotools.se.v1_1.bindings.PointSymbolizerBinding;
import org.geotools.se.v1_1.bindings.PolygonSymbolizerBinding;
import org.geotools.se.v1_1.bindings.RasterSymbolizerBinding;
import org.geotools.se.v1_1.bindings.RuleBinding;
import org.geotools.se.v1_1.bindings.SearchDirectionTypeBinding;
import org.geotools.se.v1_1.bindings.SelectedChannelTypeBinding;
import org.geotools.se.v1_1.bindings.ShadedReliefBinding;
import org.geotools.se.v1_1.bindings.StringLengthBinding;
import org.geotools.se.v1_1.bindings.StringPositionBinding;
import org.geotools.se.v1_1.bindings.StripOffPositionTypeBinding;
import org.geotools.se.v1_1.bindings.StrokeBinding;
import org.geotools.se.v1_1.bindings.SubstringBinding;
import org.geotools.se.v1_1.bindings.SvgParameterBinding;
import org.geotools.se.v1_1.bindings.SymbolizerTypeBinding;
import org.geotools.se.v1_1.bindings.TextSymbolizerBinding;
import org.geotools.se.v1_1.bindings.ThreshholdsBelongToTypeBinding;
import org.geotools.se.v1_1.bindings.TrimBinding;
import org.geotools.se.v1_1.bindings.VendorOptionBinding;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/se schema.
 *
 * @generated
 */
public class SEConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public SEConfiguration() {
        super(SE.getInstance());

        addDependency(new OGCConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    @Override
    protected final void registerBindings(MutablePicoContainer container) {
        // Types
        container.registerComponentImplementation(SE.directionType, DirectionTypeBinding.class);
        // container.registerComponentImplementation(SE.FunctionType,FunctionTypeBinding.class);
        // container.registerComponentImplementation(SE.MethodType,MethodTypeBinding.class);
        // container.registerComponentImplementation(SE.ModeType,ModeTypeBinding.class);
        container.registerComponentImplementation(
                SE.ParameterValueType, ParameterValueTypeBinding.class);
        container.registerComponentImplementation(
                SE.searchDirectionType, SearchDirectionTypeBinding.class);
        container.registerComponentImplementation(
                SE.SelectedChannelType, SelectedChannelTypeBinding.class);
        container.registerComponentImplementation(
                SE.stripOffPositionType, StripOffPositionTypeBinding.class);
        container.registerComponentImplementation(SE.SymbolizerType, SymbolizerTypeBinding.class);
        container.registerComponentImplementation(
                SE.ThreshholdsBelongToType, ThreshholdsBelongToTypeBinding.class);
        // container.registerComponentImplementation(SE.VersionType,VersionTypeBinding.class);

        // Elements
        container.registerComponentImplementation(SE.AnchorPoint, AnchorPointBinding.class);
        container.registerComponentImplementation(SE.BaseSymbolizer, BaseSymbolizerBinding.class);
        container.registerComponentImplementation(SE.BrightnessOnly, BrightnessOnlyBinding.class);
        container.registerComponentImplementation(SE.Categorize, CategorizeBinding.class);
        container.registerComponentImplementation(SE.ChangeCase, ChangeCaseBinding.class);
        container.registerComponentImplementation(
                SE.ChannelSelection, ChannelSelectionBinding.class);
        container.registerComponentImplementation(SE.ColorMap, ColorMapBinding.class);
        // container.registerComponentImplementation(SE.ColorReplacement,ColorReplacementBinding.class);
        container.registerComponentImplementation(SE.Concatenate, ConcatenateBinding.class);
        container.registerComponentImplementation(
                SE.ContrastEnhancement, ContrastEnhancementBinding.class);
        container.registerComponentImplementation(SE.CoverageName, CoverageNameBinding.class);
        container.registerComponentImplementation(SE.CoverageStyle, CoverageStyleBinding.class);

        container.registerComponentImplementation(SE.Description, DescriptionBinding.class);
        container.registerComponentImplementation(SE.Displacement, DisplacementBinding.class);
        container.registerComponentImplementation(SE.ExternalGraphic, ExternalGraphicBinding.class);
        container.registerComponentImplementation(
                SE.FeatureTypeStyle, FeatureTypeStyleBinding.class);
        container.registerComponentImplementation(SE.Fill, FillBinding.class);
        container.registerComponentImplementation(SE.Font, FontBinding.class);
        container.registerComponentImplementation(SE.FormatDate, FormatDateBinding.class);
        container.registerComponentImplementation(SE.FormatNumber, FormatNumberBinding.class);
        container.registerComponentImplementation(SE.Geometry, GeometryBinding.class);
        container.registerComponentImplementation(SE.Graphic, GraphicBinding.class);
        container.registerComponentImplementation(SE.GraphicFill, GraphicFillBinding.class);
        container.registerComponentImplementation(SE.GraphicStroke, GraphicStrokeBinding.class);
        container.registerComponentImplementation(SE.Halo, HaloBinding.class);
        container.registerComponentImplementation(SE.Histogram, HistogramBinding.class);
        container.registerComponentImplementation(SE.ImageOutline, ImageOutlineBinding.class);
        container.registerComponentImplementation(SE.InlineContent, InlineContentBinding.class);
        container.registerComponentImplementation(SE.Interpolate, InterpolateBinding.class);
        container.registerComponentImplementation(
                SE.InterpolationPoint, InterpolationPointBinding.class);
        container.registerComponentImplementation(SE.LabelPlacement, LabelPlacementBinding.class);
        container.registerComponentImplementation(SE.LegendGraphic, LegendGraphicBinding.class);
        container.registerComponentImplementation(SE.LinePlacement, LinePlacementBinding.class);
        container.registerComponentImplementation(SE.LineSymbolizer, LineSymbolizerBinding.class);
        // container.registerComponentImplementation(SE.MapItem,MapItemBinding.class);
        container.registerComponentImplementation(SE.Mark, MarkBinding.class);
        container.registerComponentImplementation(SE.Normalize, NormalizeBinding.class);
        container.registerComponentImplementation(SE.OnlineResource, OnlineResourceBinding.class);
        container.registerComponentImplementation(SE.OverlapBehavior, OverlapBehaviorBinding.class);

        container.registerComponentImplementation(SE.PointPlacement, PointPlacementBinding.class);
        container.registerComponentImplementation(SE.PointSymbolizer, PointSymbolizerBinding.class);
        container.registerComponentImplementation(
                SE.PolygonSymbolizer, PolygonSymbolizerBinding.class);
        container.registerComponentImplementation(
                SE.RasterSymbolizer, RasterSymbolizerBinding.class);
        // container.registerComponentImplementation(SE.Recode,RecodeBinding.class);
        container.registerComponentImplementation(SE.Rule, RuleBinding.class);
        container.registerComponentImplementation(SE.ShadedRelief, ShadedReliefBinding.class);
        container.registerComponentImplementation(SE.StringLength, StringLengthBinding.class);
        container.registerComponentImplementation(SE.StringPosition, StringPositionBinding.class);
        container.registerComponentImplementation(SE.Stroke, StrokeBinding.class);
        container.registerComponentImplementation(SE.Substring, SubstringBinding.class);
        container.registerComponentImplementation(SE.SvgParameter, SvgParameterBinding.class);
        container.registerComponentImplementation(SE.TextSymbolizer, TextSymbolizerBinding.class);
        container.registerComponentImplementation(SE.Trim, TrimBinding.class);
        container.registerComponentImplementation(SE.VendorOption, VendorOptionBinding.class);
    }

    @Override
    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(StyleFactory.class, StyleFactoryImpl.class);
        container.registerComponentInstance(ResourceLocator.class, new DefaultResourceLocator());
    }

    @Override
    protected void configureParser(Parser parser) {
        parser.setHandleMixedContent(true);
    }
}
