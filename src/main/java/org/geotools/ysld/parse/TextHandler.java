package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class TextHandler extends SymbolizerHandler<TextSymbolizer> {

    public TextHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createTextSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();
        if (map.has("label")) {
            sym.setLabel(Util.expression(map.str("label"), factory));
        }
        context.push("font", new FontHandler());
        context.push("halo", new HaloHandler());
        context.push("placement", new PlacementHandler());
        context.push("fill", new FillHandler(factory) {
            @Override
            protected void fill(Fill fill) {
                sym.setFill(fill);
            }
        });
    }

    class FontHandler extends YsldParseHandler {

        Font font;

        protected FontHandler() {
            super(TextHandler.this.factory);

            FilterFactory ff = factory.filter;
            font = factory.style.createFont(
                ff.literal("serif"), ff.literal("normal"), ff.literal("normal"), ff.literal(10));
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            sym.setFont(font);

            YamlMap map = obj.map();

            if (map.has("family")) {
                font.setFontFamily(Util.expression(map.str("family"), factory));
            }
            if (map.has("size")) {
                font.setSize(Util.expression(map.str("size"), factory));
            }
            if (map.has("style")) {
                font.setStyle(Util.expression(map.str("style"), factory));
            }
            if (map.has("weight")) {
                font.setWeight(Util.expression(map.str("weight"), factory));
            }
        }
    }

    class HaloHandler extends YsldParseHandler {

        Halo halo;
        HaloHandler() {
            super(TextHandler.this.factory);
            halo = this.factory.style.createHalo(null, null);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            sym.setHalo(halo);

            YamlMap map = obj.map();

            context.push("fill", new FillHandler(factory) {
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

    class PlacementHandler extends YsldParseHandler {

        String type;

        PointPlacement point;
        LinePlacement line;

        protected PlacementHandler() {
            super(TextHandler.this.factory);
            point = factory.style.createPointPlacement(null, null, null);
            line = factory.style.createLinePlacement(null);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlMap map = obj.map();
            if (map.has("type")) {
                sym.setLabelPlacement("line".equals(map.str("type")) ? line : point);
            }
            else {
                sym.setLabelPlacement(point);
            }

            if (map.has("offset")) {
                line.setPerpendicularOffset(Util.expression(map.str("offset"), factory));
            }
            if (map.has("anchor")) {
                point.setAnchorPoint(Util.anchor(map.str("anchor"), factory));
            }
            if (map.has("displacement")) {
                point.setDisplacement(Util.displacement(map.str("displacement"), factory));
            }
            if (map.has("rotation")) {
                point.setRotation(Util.expression(map.str("rotation"), factory));
            }
        }
    }
}
