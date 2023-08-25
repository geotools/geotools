/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.styling.visitor;

import static org.geotools.api.style.FeatureTypeStyle.RenderingSelectionOptions.NORMAL;

import java.util.Map;
import org.geotools.api.style.*;
import org.geotools.styling.*;

/**
 * This abstract class applies the include VendorOptions to a Style eg. <VendorOption
 * name="include">mapOnly|legendOnly|normal</VendorOption> The visitor provides generic methods to
 * avoid including in the deep copy of the Style being passed to it the FeatureTypeStyle or the Rule
 * or the Symbolizer for which the vendorOptions defined doesn't allow the rendering.
 */
public abstract class RenderingSelectorStyleVisitor extends DuplicatingStyleVisitor {

    @Override
    public void visit(org.geotools.api.style.FeatureTypeStyle fts) {
        if (canRender(fts.getOptions())) super.visit(fts);
    }

    @Override
    public void visit(org.geotools.api.style.Rule rule) {
        if (canRender(rule.getOptions())) super.visit(rule);
    }

    @Override
    public void visit(org.geotools.api.style.Symbolizer sym) {

        if (sym instanceof PointSymbolizer) {
            if (canRender(((PointSymbolizerImpl) sym).getOptions())) super.visit(sym);
        } else if (sym instanceof LineSymbolizer) {
            if (canRender(((LineSymbolizerImpl) sym).getOptions())) super.visit(sym);
        }
        if (sym instanceof PolygonSymbolizer) {
            if (canRender(((PolygonSymbolizerImpl) sym).getOptions())) super.visit(sym);
        }
        if (sym instanceof TextSymbolizer) {
            if (canRender(((TextSymbolizerImpl) sym).getOptions())) super.visit(sym);
        }
        if (sym instanceof RasterSymbolizer) {
            if (canRender(((RasterSymbolizerImpl) sym).getOptions())) super.visit(sym);
        } else {
            throw new IllegalStateException("Can't visit " + sym);
        }
    }

    @Override
    public void visit(org.geotools.api.style.PointSymbolizer ps) {
        if (canRender(((PolygonSymbolizerImpl) ps).getOptions())) super.visit(ps);
    }

    @Override
    public void visit(org.geotools.api.style.LineSymbolizer line) {

        if (canRender(((LineSymbolizerImpl) line).getOptions())) super.visit(line);
    }

    @Override
    public void visit(org.geotools.api.style.PolygonSymbolizer poly) {
        if (canRender(((PolygonSymbolizerImpl) poly).getOptions())) super.visit(poly);
    }

    @Override
    public void visit(org.geotools.api.style.TextSymbolizer text) {
        if (canRender(((TextSymbolizerImpl) text).getOptions())) super.visit(text);
    }

    @Override
    public void visit(org.geotools.api.style.RasterSymbolizer raster) {
        if (canRender(((RasterSymbolizerImpl) raster).getOptions())) super.visit(raster);
    }

    @Override
    protected Symbolizer copy(org.geotools.api.style.Symbolizer sym) {
        if (sym == null) return null;
        if (sym instanceof PointSymbolizer) {
            if (canRender(((PointSymbolizerImpl) sym).getOptions())) return super.copy(sym);
        } else if (sym instanceof LineSymbolizer) {
            if (canRender(((LineSymbolizerImpl) sym).getOptions())) return super.copy(sym);
        }
        if (sym instanceof PolygonSymbolizer) {
            if (canRender(((PolygonSymbolizerImpl) sym).getOptions())) return super.copy(sym);
        }
        if (sym instanceof TextSymbolizer) {
            if (canRender(((TextSymbolizerImpl) sym).getOptions())) return super.copy(sym);
        }
        if (sym instanceof RasterSymbolizer) {
            if (canRender(((RasterSymbolizerImpl) sym).getOptions())) return super.copy(sym);
        } else {
            throw new IllegalStateException("Can't copy " + sym);
        }

        return null;
    }

    /**
     * Check if the vendorOptions being passed allows the element they belong to, to be included in
     * the deep copy of the style.
     *
     * @param vendorOptions the vendorOptions map
     * @return true if the element can be included false otherwise
     */
    protected boolean canRender(Map<String, String> vendorOptions) {
        boolean canRenderer;
        String value =
                vendorOptions != null
                        ? vendorOptions.get(
                                org.geotools.styling.FeatureTypeStyleImpl.VENDOR_OPTION_INCLUSION)
                        : null;
        if (value == null) canRenderer = true;
        else if (value.equalsIgnoreCase(NORMAL.name())) canRenderer = true;
        else {
            canRenderer = canRenderInternal(value);
        }
        return canRenderer;
    }

    /**
     * Used by subclass to provide custom logic to check if the SLD element should be included in
     * the copy
     *
     * @param value the value of the <VendorOption
     *     name="include">mapOnly|legendOnly|normal</VendorOption>
     * @return true if the element should be included in the copy, false otherwise.
     */
    protected abstract boolean canRenderInternal(String value);
}
