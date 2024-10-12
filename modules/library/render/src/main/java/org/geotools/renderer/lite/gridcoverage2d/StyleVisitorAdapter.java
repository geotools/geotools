/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import java.text.MessageFormat;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Halo;
import org.geotools.api.style.ImageOutline;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.renderer.i18n.ErrorKeys;

/**
 * Simple empty implementation for the {@link StyleVisitor} interface.
 *
 * <p>This class works as an adapter when we want to build simple implementations of the {@link
 * StyleVisitor} interface that can visit only a subclass of the {@link StyleVisitor} nodes.
 *
 * @author Simone Giannecchini. GeoSolutions
 */
public class StyleVisitorAdapter implements StyleVisitor {

    /** Default constructor for a {@link StyleVisitorAdapter}. */
    public StyleVisitorAdapter() {}

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.StyledLayerDescriptor)
     */
    @Override
    public void visit(StyledLayerDescriptor sld) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(StyledLayerDescriptor)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.NamedLayer)
     */
    @Override
    public void visit(NamedLayer layer) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(NamedLayer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.UserLayer)
     */
    @Override
    public void visit(UserLayer layer) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(UserLayer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.FeatureTypeConstraint)
     */
    @Override
    public void visit(FeatureTypeConstraint ftc) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(FeatureTypeConstraint)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Style)
     */
    @Override
    public void visit(Style style) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Style)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Rule)
     */
    @Override
    public void visit(Rule rule) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Rule)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.FeatureTypeStyle)
     */
    @Override
    public void visit(FeatureTypeStyle fts) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(FeatureTypeStyle)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Fill)
     */
    @Override
    public void visit(Fill fill) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Fill)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Stroke)
     */
    @Override
    public void visit(Stroke stroke) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Stroke)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Symbolizer)
     */
    @Override
    public void visit(Symbolizer sym) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Symbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.PointSymbolizer)
     */
    @Override
    public void visit(PointSymbolizer ps) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(PointSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.LineSymbolizer)
     */
    @Override
    public void visit(LineSymbolizer line) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(LineSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.PolygonSymbolizer)
     */
    @Override
    public void visit(PolygonSymbolizer poly) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(PolygonSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.TextSymbolizer)
     */
    @Override
    public void visit(TextSymbolizer text) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(TextSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.RasterSymbolizer)
     */
    @Override
    public void visit(RasterSymbolizer raster) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(RasterSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Graphic)
     */
    @Override
    public void visit(Graphic gr) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Graphic)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Mark)
     */
    @Override
    public void visit(Mark mark) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Mark)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ExternalGraphic)
     */
    @Override
    public void visit(ExternalGraphic exgr) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ExternalGraphic)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.PointPlacement)
     */
    @Override
    public void visit(PointPlacement pp) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(PointPlacement)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.AnchorPoint)
     */
    @Override
    public void visit(AnchorPoint ap) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(AnchorPoint)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Displacement)
     */
    @Override
    public void visit(Displacement dis) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Displacement)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.LinePlacement)
     */
    @Override
    public void visit(LinePlacement lp) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(LinePlacement)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Halo)
     */
    @Override
    public void visit(Halo halo) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Halo)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ColorMap)
     */
    @Override
    public void visit(ColorMap colorMap) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ColorMapTransform)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ColorMapEntry)
     */
    @Override
    public void visit(ColorMapEntry colorMapEntry) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ColorMapEntry)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ContrastEnhancement)
     */
    @Override
    public void visit(ContrastEnhancement ce) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ContrastEnhancement)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ChannelSelection)
     */
    @Override
    public void visit(ChannelSelection cs) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ChannelSelection)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.SelectedChannelType)
     */
    @Override
    public void visit(SelectedChannelType sct) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(SelectedChannelType)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.OverlapBehavior)
     */
    @Override
    public void visit(OverlapBehavior ob) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(OverlapBehavior)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ShadedRelief)
     */
    @Override
    public void visit(ShadedRelief sr) {
        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ShadedRelief)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ImageOutline)
     */
    @Override
    public void visit(ImageOutline io) {

        throw new UnsupportedOperationException(
                MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ImageOutline)"));
    }
}
