package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlObject;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class PointHandler extends SymbolizerHandler<PointSymbolizer> {

    public PointHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createPointSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);
        context.push(new GraphicHandler(factory, sym.getGraphic()));
    }
}
