package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public class GraphicHandler extends YsldParseHandler {

    Graphic g;

    GraphicHandler(Factory factory) {
        this(factory, factory.styleBuilder.createGraphic(null, null, null));
    }

    GraphicHandler(Factory factory, Graphic g) {
        super(factory);
        this.g = g;
    }

    protected void graphic(Graphic g) {
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        graphic(g);

        YamlMap map = obj.map();

        if (map.has("anchor")) {
            g.setAnchorPoint(Util.anchor(map.str("anchor"), factory));
        }

        if (map.has("opacity")) {
            g.setOpacity(Util.expression(map.str("opacity"), factory));
        }

        if (map.has("size")) {
            g.setSize(Util.expression(map.str("size"), factory));
        }

        if (map.has("displacement")) {
            g.setDisplacement(Util.displacement(map.str("displacement"), factory));
        }

        if (map.has("rotation")) {
            g.setRotation(Util.expression(map.str("rotation"), factory));
        }

        if (map.has("gap")) {
            g.setGap(Util.expression(map.str("gap"), factory));
        }

        if (map.has("initial-gap")) {
            g.setInitialGap(Util.expression(map.str("initial-gap"), factory));
        }

        context.push("symbols", new SymbolsHandler());
    }

    class SymbolsHandler extends YsldParseHandler {

        protected SymbolsHandler() {
            super(GraphicHandler.this.factory);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlSeq seq = obj.seq();
            for (YamlObject o : seq) {
                context.push(o, "mark", new MarkHandler(factory) {
                    @Override
                    protected void mark(Mark mark) {
                        g.graphicalSymbols().add(mark);
                    }
                });
                context.push(o, "external", new ExternalGraphicHandler(factory) {
                    @Override
                    protected void externalGraphic(ExternalGraphic externalGraphic) {
                        g.graphicalSymbols().add(externalGraphic);
                    }
                });
            }
        }
    }
}
