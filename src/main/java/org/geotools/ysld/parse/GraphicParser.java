package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

public class GraphicParser extends YsldParseHandler {

    Graphic g;

    GraphicParser(Factory factory) {
        this(factory, factory.styleBuilder.createGraphic(null, null, null));
    }

    GraphicParser(Factory factory, Graphic g) {
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
            super(GraphicParser.this.factory);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlSeq seq = obj.seq();
            for (YamlObject o : seq) {
                context.push(o, "mark", new MarkParser(factory) {
                    @Override
                    protected void mark(Mark mark) {
                        g.graphicalSymbols().add(mark);
                    }
                });
                context.push(o, "external", new ExternalGraphicParser(factory) {
                    @Override
                    protected void externalGraphic(ExternalGraphic externalGraphic) {
                        g.graphicalSymbols().add(externalGraphic);
                    }
                });
            }
        }
    }
}
