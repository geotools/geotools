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

import org.geotools.api.style.*;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;

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
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(StyledLayerDescriptor)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.NamedLayer)
     */
    @Override
    public void visit(NamedLayer layer) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(NamedLayer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.UserLayer)
     */
    @Override
    public void visit(UserLayer layer) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(UserLayer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.FeatureTypeConstraint)
     */
    @Override
    public void visit(FeatureTypeConstraint ftc) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(FeatureTypeConstraint)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Style)
     */
    @Override
    public void visit(org.geotools.api.style.Style style) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Style)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Rule)
     */
    @Override
    public void visit(org.geotools.api.style.Rule rule) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Rule)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.FeatureTypeStyle)
     */
    @Override
    public void visit(org.geotools.api.style.FeatureTypeStyle fts) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(FeatureTypeStyle)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Fill)
     */
    @Override
    public void visit(org.geotools.api.style.Fill fill) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Fill)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Stroke)
     */
    @Override
    public void visit(org.geotools.api.style.Stroke stroke) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Stroke)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Symbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.Symbolizer sym) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Symbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.PointSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.PointSymbolizer ps) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(PointSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.LineSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.LineSymbolizer line) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(LineSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.PolygonSymbolizer poly) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(PolygonSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.TextSymbolizer text) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(TextSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.RasterSymbolizer raster) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(RasterSymbolizer)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Graphic)
     */
    @Override
    public void visit(org.geotools.api.style.Graphic gr) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Graphic)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Mark)
     */
    @Override
    public void visit(org.geotools.api.style.Mark mark) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Mark)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.ExternalGraphic)
     */
    @Override
    public void visit(org.geotools.api.style.ExternalGraphic exgr) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ExternalGraphic)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.PointPlacement)
     */
    @Override
    public void visit(org.geotools.api.style.PointPlacement pp) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(PointPlacement)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.AnchorPoint)
     */
    @Override
    public void visit(org.geotools.api.style.AnchorPoint ap) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(AnchorPoint)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Displacement)
     */
    @Override
    public void visit(org.geotools.api.style.Displacement dis) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Displacement)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.LinePlacement)
     */
    @Override
    public void visit(org.geotools.api.style.LinePlacement lp) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(LinePlacement)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Halo)
     */
    @Override
    public void visit(org.geotools.api.style.Halo halo) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(Halo)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.ColorMap)
     */
    @Override
    public void visit(org.geotools.api.style.ColorMap colorMap) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ColorMapTransform)"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ColorMapEntry)
     */
    @Override
    public void visit(org.geotools.api.style.ColorMapEntry colorMapEntry) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ColorMapEntry)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.ContrastEnhancement)
     */
    @Override
    public void visit(org.geotools.api.style.ContrastEnhancement ce) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ContrastEnhancement)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.ChannelSelection)
     */
    @Override
    public void visit(org.geotools.api.style.ChannelSelection cs) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ChannelSelection)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.SelectedChannelType)
     */
    @Override
    public void visit(org.geotools.api.style.SelectedChannelType sct) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(SelectedChannelType)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.OverlapBehavior)
     */
    @Override
    public void visit(org.geotools.api.style.OverlapBehavior ob) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(OverlapBehavior)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.ShadedRelief)
     */
    @Override
    public void visit(org.geotools.api.style.ShadedRelief sr) {
        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ShadedRelief)"));
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ImageOutline)
     */
    @Override
    public void visit(org.geotools.api.style.ImageOutline io) {

        throw new UnsupportedOperationException(
                Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "visit(ImageOutline)"));
    }
}
