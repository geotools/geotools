package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class PointHandler extends SymbolizerHandler<PointSymbolizer> {

    public PointHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createPointSymbolizer(), factory);
    }

    @Override
    public void mapping(MappingStartEvent evt, YamlParseContext context) {
        super.mapping(evt, context);
        context.push(new PointGraphicHandler());
    }

    class PointGraphicHandler extends GraphicHandler {

        PointGraphicHandler() {
            super(PointHandler.this.factory, sym.getGraphic());
        }

        @Override
        public void scalar(ScalarEvent evt, YamlParseContext context) {
            super.scalar(evt, context);
            PointHandler.this.scalar(evt, context);
        }
    }
}
