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

import static org.geotools.styling.FeatureTypeStyle.RenderingSelectionOptions.NORMAL;

import java.util.Map;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

/**
 * This abstract class applies the include VendorOptions to a Style eg. <VendorOption
 * name="include">mapOnly|legendOnly|normal</VendorOption> The visitor provides generic methods to
 * avoid including in the deep copy of the Style being passed to it the FeatureTypeStyle or the Rule
 * or the Symbolizer for which the vendorOptions defined doesn't allow the rendering.
 */
public abstract class RenderingSelectorStyleVisitor extends DuplicatingStyleVisitor {

    @Override
    public void visit(FeatureTypeStyle fts) {
        if (canRender(fts.getOptions())) super.visit(fts);
    }

    @Override
    public void visit(Rule rule) {
        if (canRender(rule.getOptions())) super.visit(rule);
    }

    @Override
    public void visit(Symbolizer sym) {
        if (canRender(sym.getOptions())) super.visit(sym);
    }

    @Override
    public void visit(PointSymbolizer ps) {
        if (canRender(ps.getOptions())) super.visit(ps);
    }

    @Override
    public void visit(LineSymbolizer line) {
        if (canRender(line.getOptions())) super.visit(line);
    }

    @Override
    public void visit(PolygonSymbolizer poly) {
        if (canRender(poly.getOptions())) super.visit(poly);
    }

    @Override
    public void visit(TextSymbolizer text) {
        if (canRender(text.getOptions())) super.visit(text);
    }

    @Override
    public void visit(RasterSymbolizer raster) {
        if (canRender(raster.getOptions())) super.visit(raster);
    }

    @Override
    protected Symbolizer copy(Symbolizer symbolizer) {
        if (symbolizer == null) return null;
        if (canRender(symbolizer.getOptions())) return super.copy(symbolizer);
        else return null;
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
                        ? vendorOptions.get(FeatureTypeStyle.VENDOR_OPTION_INCLUSION)
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
