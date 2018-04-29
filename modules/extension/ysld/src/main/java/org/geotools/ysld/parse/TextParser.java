/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.filter.FilterFactory;

/** Handles parsing a Ysld "text" symbolizer property into a {@link Symbolizer} object. */
public class TextParser extends SymbolizerParser<TextSymbolizer> {

    public TextParser(Rule rule, Factory factory) {
        super(rule, factory.style.createTextSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();
        if (map.has("label")) {
            sym.setLabel(Util.expression(map.str("label"), factory));
        }
        if (map.has("priority")) {
            sym.setPriority(Util.expression(map.str("priority"), factory));
        }

        context.push(new FontHandler());
        context.push("halo", new HaloParser());
        context.push(new PlacementParser());
        context.push(
                new FillParser(factory) {
                    @Override
                    protected void fill(Fill fill) {
                        sym.setFill(fill);
                    }
                });
        context.push(
                "graphic",
                new GraphicParser(factory) {
                    @Override
                    protected void graphic(Graphic g) {
                        if (sym instanceof TextSymbolizer2) {
                            ((TextSymbolizer2) sym).setGraphic(g);
                        }
                    }
                });
    }

    class FontHandler extends YsldParseHandler {

        Font font;

        protected FontHandler() {
            super(TextParser.this.factory);

            FilterFactory ff = factory.filter;
            font =
                    factory.style.createFont(
                            ff.literal("serif"),
                            ff.literal("normal"),
                            ff.literal("normal"),
                            ff.literal(10));
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            sym.setFont(font);

            YamlMap map = obj.map();

            if (map.has("font-family")) {
                font.setFontFamily(Util.expression(map.str("font-family"), factory));
            }
            if (map.has("font-size")) {
                font.setSize(Util.expression(map.str("font-size"), factory));
            }
            if (map.has("font-style")) {
                font.setStyle(Util.expression(map.str("font-style"), factory));
            }
            if (map.has("font-weight")) {
                font.setWeight(Util.expression(map.str("font-weight"), factory));
            }
        }
    }

    class HaloParser extends YsldParseHandler {

        Halo halo;

        HaloParser() {
            super(TextParser.this.factory);
            halo = this.factory.style.createHalo(null, null);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            sym.setHalo(halo);

            YamlMap map = obj.map();

            context.push(
                    new FillParser(factory) {
                        @Override
                        protected void fill(Fill fill) {
                            halo.setFill(fill);
                        }
                    });

            if (map.has("radius")) {
                halo.setRadius(Util.expression(map.str("radius"), factory));
            }
        }
    }

    class PlacementParser extends YsldParseHandler {

        String type;

        PointPlacement point;

        LinePlacement line;

        protected PlacementParser() {
            super(TextParser.this.factory);
            point = factory.style.createPointPlacement(null, null, null);
            line = factory.style.createLinePlacement(null);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlMap map = obj.map();
            if (map.has("placement")) {
                sym.setLabelPlacement("line".equals(map.str("placement")) ? line : point);
            } else {
                sym.setLabelPlacement(point);
            }

            if (map.has("offset")) {
                line.setPerpendicularOffset(Util.expression(map.str("offset"), factory));
            }
            if (map.has("anchor")) {
                point.setAnchorPoint(Util.anchor(map.get("anchor"), factory));
            }
            if (map.has("displacement")) {
                point.setDisplacement(Util.displacement(map.get("displacement"), factory));
            }
            if (map.has("rotation")) {
                point.setRotation(Util.expression(map.str("rotation"), factory));
            }
            // anchor point is manditory for SLD encoding
            if (point.getAnchorPoint() == null) {
                AnchorPoint defaultAnchor =
                        factory.style.getDefaultPointPlacement().getAnchorPoint();
                point.setAnchorPoint(defaultAnchor);
            }
        }
    }
}
