package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public class PointHandler extends SymbolizerHandler<PointSymbolizer> {

    public PointHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createPointSymbolizer(), factory);
    }

    @Override
    public void mapping(MappingStartEvent evt, Deque<YamlParseHandler> handlers) {
        super.mapping(evt, handlers);
        handlers.push(new PointGraphicHandler());
    }

    class PointGraphicHandler extends GraphicHandler {

        PointGraphicHandler() {
            super(PointHandler.this.factory, sym.getGraphic());
        }

        @Override
        public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
            super.scalar(evt, handlers);
            PointHandler.this.scalar(evt, handlers);
        }
    }
}
